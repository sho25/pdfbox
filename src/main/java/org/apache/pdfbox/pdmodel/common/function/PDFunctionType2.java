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
name|COSDictionary
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
name|COSFloat
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|Math
import|;
end_import

begin_comment
comment|/**  * This class represents a type 2 function in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFunctionType2
extends|extends
name|PDDictionaryFunction
block|{
specifier|private
name|COSArray
name|C0
decl_stmt|,
name|C1
decl_stmt|;
comment|/**      * Constructor to create a new blank type 2 function.      */
specifier|protected
name|PDFunctionType2
parameter_list|()
block|{
name|super
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param functionDictionary The prepopulated function dictionary.      */
specifier|public
name|PDFunctionType2
parameter_list|(
name|COSDictionary
name|functionDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**     * {@inheritDoc}     */
specifier|public
name|COSArray
name|Eval
parameter_list|(
name|COSArray
name|input
parameter_list|)
throws|throws
name|IOException
block|{
comment|//This function performs exponential interpolation.
comment|//It uses only a single value as its input, but may produce a multi-valued output.
comment|//See PDF Reference section 3.9.2.
name|double
name|x
init|=
name|input
operator|.
name|toFloatArray
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|COSArray
name|y
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|getC0
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
comment|//y[j] = C0[j] + x^N*(C1[j] - C0[j])
name|float
name|FofX
init|=
call|(
name|float
call|)
argument_list|(
operator|(
operator|(
name|COSFloat
operator|)
name|C0
operator|.
name|get
argument_list|(
name|j
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
operator|+
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|pow
argument_list|(
name|x
argument_list|,
operator|(
name|double
operator|)
name|getN
argument_list|()
argument_list|)
operator|*
operator|(
operator|(
operator|(
name|COSFloat
operator|)
name|C1
operator|.
name|get
argument_list|(
name|j
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
operator|-
operator|(
operator|(
name|COSFloat
operator|)
name|C0
operator|.
name|get
argument_list|(
name|j
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
operator|)
argument_list|)
decl_stmt|;
name|y
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|FofX
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|y
return|;
block|}
specifier|protected
name|COSArray
name|getC0
parameter_list|()
block|{
if|if
condition|(
name|C0
operator|==
literal|null
condition|)
block|{
name|C0
operator|=
name|getRangeArray
argument_list|(
literal|"C0"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|C0
return|;
block|}
specifier|protected
name|COSArray
name|getC1
parameter_list|()
block|{
if|if
condition|(
name|C1
operator|==
literal|null
condition|)
block|{
comment|//can't use getRangeArray here as the default is 1.0, not 0.0.
name|C1
operator|=
operator|(
name|COSArray
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"C1"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|C1
operator|==
literal|null
condition|)
block|{
name|C1
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"C1"
argument_list|,
name|C1
argument_list|)
expr_stmt|;
name|C1
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|C1
return|;
block|}
specifier|protected
name|float
name|getN
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"N"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

