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
name|util
operator|.
name|operator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|util
operator|.
name|Matrix
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
name|COSNumber
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * @author Huault : huault@free.fr  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|ShowTextGlyph
extends|extends
name|OperatorProcessor
block|{
comment|/**      * TJ Show text, allowing individual glyph positioning.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If there is an error processing this operator.      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|arraySize
init|=
name|array
operator|.
name|size
argument_list|()
decl_stmt|;
name|float
name|fontsize
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getFontSize
argument_list|()
decl_stmt|;
name|float
name|horizontalScaling
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getHorizontalScaling
argument_list|()
operator|/
literal|100
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arraySize
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|next
init|=
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSNumber
condition|)
block|{
name|float
name|adjustment
init|=
operator|(
operator|(
name|COSNumber
operator|)
name|next
operator|)
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|Matrix
name|adjMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|adjustment
operator|=
operator|-
operator|(
name|adjustment
operator|/
literal|1000
operator|)
operator|*
name|horizontalScaling
operator|*
name|fontsize
expr_stmt|;
comment|// TODO vertical writing mode
name|adjMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
name|adjustment
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTextMatrix
argument_list|(
name|adjMatrix
operator|.
name|multiply
argument_list|(
name|context
operator|.
name|getTextMatrix
argument_list|()
argument_list|,
name|adjMatrix
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|COSString
condition|)
block|{
name|context
operator|.
name|processEncodedText
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|next
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown type in array for TJ operation:"
operator|+
name|next
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

