begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|function
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * This class represents a Type 3 (stitching) function in a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFunctionType3
extends|extends
name|PDFunction
block|{
specifier|private
name|COSArray
name|functions
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|encode
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|bounds
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFunction
index|[]
name|functionsArray
init|=
literal|null
decl_stmt|;
specifier|private
name|float
index|[]
name|boundsValues
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      * @param functionStream The function .      */
specifier|public
name|PDFunctionType3
parameter_list|(
name|COSBase
name|functionStream
parameter_list|)
block|{
name|super
argument_list|(
name|functionStream
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|getFunctionType
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
comment|/**     * {@inheritDoc}     */
annotation|@
name|Override
specifier|public
name|float
index|[]
name|eval
parameter_list|(
name|float
index|[]
name|input
parameter_list|)
throws|throws
name|IOException
block|{
comment|//This function is known as a "stitching" function. Based on the input, it decides which child function to call.
comment|// All functions in the array are 1-value-input functions
comment|//See PDF Reference section 3.9.3.
name|PDFunction
name|function
init|=
literal|null
decl_stmt|;
name|float
name|x
init|=
name|input
index|[
literal|0
index|]
decl_stmt|;
name|PDRange
name|domain
init|=
name|getDomainForInput
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// clip input value to domain
name|x
operator|=
name|clipToRange
argument_list|(
name|x
argument_list|,
name|domain
operator|.
name|getMin
argument_list|()
argument_list|,
name|domain
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|functionsArray
operator|==
literal|null
condition|)
block|{
name|COSArray
name|ar
init|=
name|getFunctions
argument_list|()
decl_stmt|;
name|functionsArray
operator|=
operator|new
name|PDFunction
index|[
name|ar
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ar
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|functionsArray
index|[
name|i
index|]
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|ar
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|functionsArray
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// This doesn't make sense but it may happen ...
name|function
operator|=
name|functionsArray
index|[
literal|0
index|]
expr_stmt|;
name|PDRange
name|encRange
init|=
name|getEncodeForParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|x
operator|=
name|interpolate
argument_list|(
name|x
argument_list|,
name|domain
operator|.
name|getMin
argument_list|()
argument_list|,
name|domain
operator|.
name|getMax
argument_list|()
argument_list|,
name|encRange
operator|.
name|getMin
argument_list|()
argument_list|,
name|encRange
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|boundsValues
operator|==
literal|null
condition|)
block|{
name|boundsValues
operator|=
name|getBounds
argument_list|()
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
name|int
name|boundsSize
init|=
name|boundsValues
operator|.
name|length
decl_stmt|;
comment|// create a combined array containing the domain and the bounds values
comment|// domain.min, bounds[0], bounds[1], ...., bounds[boundsSize-1], domain.max
name|float
index|[]
name|partitionValues
init|=
operator|new
name|float
index|[
name|boundsSize
operator|+
literal|2
index|]
decl_stmt|;
name|int
name|partitionValuesSize
init|=
name|partitionValues
operator|.
name|length
decl_stmt|;
name|partitionValues
index|[
literal|0
index|]
operator|=
name|domain
operator|.
name|getMin
argument_list|()
expr_stmt|;
name|partitionValues
index|[
name|partitionValuesSize
operator|-
literal|1
index|]
operator|=
name|domain
operator|.
name|getMax
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|boundsValues
argument_list|,
literal|0
argument_list|,
name|partitionValues
argument_list|,
literal|1
argument_list|,
name|boundsSize
argument_list|)
expr_stmt|;
comment|// find the partition
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|partitionValuesSize
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|x
operator|>=
name|partitionValues
index|[
name|i
index|]
operator|&&
operator|(
name|x
operator|<
name|partitionValues
index|[
name|i
operator|+
literal|1
index|]
operator|||
operator|(
name|i
operator|==
name|partitionValuesSize
operator|-
literal|2
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|x
argument_list|,
name|partitionValues
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
operator|==
literal|0
operator|)
operator|)
condition|)
block|{
name|function
operator|=
name|functionsArray
index|[
name|i
index|]
expr_stmt|;
name|PDRange
name|encRange
init|=
name|getEncodeForParameter
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|x
operator|=
name|interpolate
argument_list|(
name|x
argument_list|,
name|partitionValues
index|[
name|i
index|]
argument_list|,
name|partitionValues
index|[
name|i
operator|+
literal|1
index|]
argument_list|,
name|encRange
operator|.
name|getMin
argument_list|()
argument_list|,
name|encRange
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|function
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"partition not found in type 3 function"
argument_list|)
throw|;
block|}
block|}
name|float
index|[]
name|functionValues
init|=
operator|new
name|float
index|[]
block|{
name|x
block|}
decl_stmt|;
comment|// calculate the output values using the chosen function
name|float
index|[]
name|functionResult
init|=
name|function
operator|.
name|eval
argument_list|(
name|functionValues
argument_list|)
decl_stmt|;
comment|// clip to range if available
return|return
name|clipToRange
argument_list|(
name|functionResult
argument_list|)
return|;
block|}
comment|/**      * Returns all functions values as COSArray.      *       * @return the functions array.       */
specifier|public
name|COSArray
name|getFunctions
parameter_list|()
block|{
if|if
condition|(
name|functions
operator|==
literal|null
condition|)
block|{
name|functions
operator|=
call|(
name|COSArray
call|)
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FUNCTIONS
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|functions
return|;
block|}
comment|/**      * Returns all bounds values as COSArray.      *       * @return the bounds array.       */
specifier|public
name|COSArray
name|getBounds
parameter_list|()
block|{
if|if
condition|(
name|bounds
operator|==
literal|null
condition|)
block|{
name|bounds
operator|=
call|(
name|COSArray
call|)
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BOUNDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|bounds
return|;
block|}
comment|/**      * Returns all encode values as COSArray.      *       * @return the encode array.       */
specifier|public
name|COSArray
name|getEncode
parameter_list|()
block|{
if|if
condition|(
name|encode
operator|==
literal|null
condition|)
block|{
name|encode
operator|=
call|(
name|COSArray
call|)
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|encode
return|;
block|}
comment|/**      * Get the encode for the input parameter.      *      * @param n The function parameter number.      *      * @return The encode parameter range or null if none is set.      */
specifier|private
name|PDRange
name|getEncodeForParameter
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|COSArray
name|encodeValues
init|=
name|getEncode
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDRange
argument_list|(
name|encodeValues
argument_list|,
name|n
argument_list|)
return|;
block|}
block|}
end_class

end_unit

