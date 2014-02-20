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
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
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
name|function
operator|.
name|type4
operator|.
name|ExecutionContext
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
name|function
operator|.
name|type4
operator|.
name|InstructionSequence
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
name|function
operator|.
name|type4
operator|.
name|InstructionSequenceBuilder
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
name|function
operator|.
name|type4
operator|.
name|Operators
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
comment|/**  * This class represents a Type 4 (PostScript calculator) function in a PDF document.  *<p>  * See section 3.9.4 of the PDF 1.4 Reference.  *  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFunctionType4
extends|extends
name|PDFunction
block|{
specifier|private
specifier|static
specifier|final
name|Operators
name|OPERATORS
init|=
operator|new
name|Operators
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|InstructionSequence
name|instructions
decl_stmt|;
comment|/**      * Constructor.      *      * @param functionStream The function stream.      * @throws IOException if an I/O error occurs while reading the function      */
specifier|public
name|PDFunctionType4
parameter_list|(
name|COSBase
name|functionStream
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|functionStream
argument_list|)
expr_stmt|;
name|this
operator|.
name|instructions
operator|=
name|InstructionSequenceBuilder
operator|.
name|parse
argument_list|(
name|getPDStream
argument_list|()
operator|.
name|getInputStreamAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getFunctionType
parameter_list|()
block|{
return|return
literal|4
return|;
block|}
comment|/**     * {@inheritDoc}     */
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
comment|//Setup the input values
name|int
name|numberOfInputValues
init|=
name|input
operator|.
name|length
decl_stmt|;
name|ExecutionContext
name|context
init|=
operator|new
name|ExecutionContext
argument_list|(
name|OPERATORS
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|numberOfInputValues
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|PDRange
name|domain
init|=
name|getDomainForInput
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|float
name|value
init|=
name|clipToRange
argument_list|(
name|input
index|[
name|i
index|]
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
decl_stmt|;
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|push
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|//Execute the type 4 function.
name|instructions
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|//Extract the output values
name|int
name|numberOfOutputValues
init|=
name|getNumberOfOutputParameters
argument_list|()
decl_stmt|;
name|int
name|numberOfActualOutputValues
init|=
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|numberOfActualOutputValues
operator|<
name|numberOfOutputValues
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The type 4 function returned "
operator|+
name|numberOfActualOutputValues
operator|+
literal|" values but the Range entry indicates that "
operator|+
name|numberOfOutputValues
operator|+
literal|" values be returned."
argument_list|)
throw|;
block|}
name|float
index|[]
name|outputValues
init|=
operator|new
name|float
index|[
name|numberOfOutputValues
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|numberOfOutputValues
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|PDRange
name|range
init|=
name|getRangeForOutput
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|outputValues
index|[
name|i
index|]
operator|=
name|context
operator|.
name|popReal
argument_list|()
expr_stmt|;
name|outputValues
index|[
name|i
index|]
operator|=
name|clipToRange
argument_list|(
name|outputValues
index|[
name|i
index|]
argument_list|,
name|range
operator|.
name|getMin
argument_list|()
argument_list|,
name|range
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Return the resulting array
return|return
name|outputValues
return|;
block|}
block|}
end_class

end_unit

