begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
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
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDPage
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
name|font
operator|.
name|PDType3CharProc
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
name|graphics
operator|.
name|image
operator|.
name|PDInlineImage
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
name|preflight
operator|.
name|PreflightContext
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
name|preflight
operator|.
name|content
operator|.
name|PreflightStreamEngine
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
import|;
end_import

begin_comment
comment|/**  * This class is used to parse a glyph of a Type3 font program. If the glyph is parsed without error, the width of the  * glyph is accessible through the getWidth method.  */
end_comment

begin_class
specifier|public
class|class
name|PreflightType3Stream
extends|extends
name|PreflightStreamEngine
block|{
specifier|private
specifier|final
name|PDType3CharProc
name|charProc
decl_stmt|;
specifier|private
name|boolean
name|firstOperator
init|=
literal|true
decl_stmt|;
specifier|private
name|float
name|width
init|=
literal|0
decl_stmt|;
specifier|private
name|PDInlineImage
name|image
init|=
literal|null
decl_stmt|;
specifier|private
name|BoundingBox
name|box
init|=
literal|null
decl_stmt|;
specifier|public
name|PreflightType3Stream
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDType3CharProc
name|charProc
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|this
operator|.
name|charProc
operator|=
name|charProc
expr_stmt|;
block|}
specifier|public
name|void
name|showType3Character
parameter_list|(
name|PDType3CharProc
name|charProc
parameter_list|)
throws|throws
name|IOException
block|{
name|processChildStream
argument_list|(
name|charProc
argument_list|,
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
comment|// dummy page (resource lookup may fail)
block|}
comment|/**      * This will parse a type3 stream and create an image from it.      *       * @return The image that was created.      *       * @throws IOException      *             If there is an error processing the stream.      */
specifier|public
name|Image
name|createImage
parameter_list|()
throws|throws
name|IOException
block|{
name|showType3Character
argument_list|(
name|charProc
argument_list|)
expr_stmt|;
return|return
name|image
operator|.
name|getImage
argument_list|()
return|;
block|}
comment|/**      * This is used to handle an operation.      *       * @param operator      *            The operation to perform.      * @param operands      *            The list of arguments.      *       * @throws IOException      *             If there is an error processing the operation.      */
specifier|protected
name|void
name|processOperator
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
name|operands
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|processOperator
argument_list|(
name|operator
argument_list|,
name|operands
argument_list|)
expr_stmt|;
name|String
name|operation
init|=
name|operator
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|.
name|equals
argument_list|(
literal|"BI"
argument_list|)
condition|)
block|{
name|image
operator|=
operator|new
name|PDInlineImage
argument_list|(
name|operator
operator|.
name|getImageParameters
argument_list|()
argument_list|,
name|operator
operator|.
name|getImageData
argument_list|()
argument_list|,
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
name|validateImageFilter
argument_list|(
name|operator
argument_list|)
expr_stmt|;
name|validateImageColorSpace
argument_list|(
name|operator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|.
name|equals
argument_list|(
literal|"d0"
argument_list|)
condition|)
block|{
name|checkType3FirstOperator
argument_list|(
name|operands
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equals
argument_list|(
literal|"d1"
argument_list|)
condition|)
block|{
name|COSNumber
name|llx
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|COSNumber
name|lly
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|COSNumber
name|urx
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|COSNumber
name|ury
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|box
operator|=
operator|new
name|BoundingBox
argument_list|()
expr_stmt|;
name|box
operator|.
name|setLowerLeftX
argument_list|(
name|llx
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|box
operator|.
name|setLowerLeftY
argument_list|(
name|lly
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|box
operator|.
name|setUpperRightX
argument_list|(
name|urx
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|box
operator|.
name|setUpperRightY
argument_list|(
name|ury
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|checkType3FirstOperator
argument_list|(
name|operands
argument_list|)
expr_stmt|;
block|}
name|checkColorOperators
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|validateRenderingIntent
argument_list|(
name|operator
argument_list|,
name|operands
argument_list|)
expr_stmt|;
name|checkSetColorSpaceOperators
argument_list|(
name|operator
argument_list|,
name|operands
argument_list|)
expr_stmt|;
name|validateNumberOfGraphicStates
argument_list|(
name|operator
argument_list|)
expr_stmt|;
name|firstOperator
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * According to the PDF Reference, the first operator in a CharProc of a Type3 font must be "d0" or "d1". This      * method process this validation. This method is called by the processOperator method.      *       * @param arguments      * @throws IOException      */
specifier|private
name|void
name|checkType3FirstOperator
parameter_list|(
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|firstOperator
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Type3 CharProc : First operator must be d0 or d1"
argument_list|)
throw|;
block|}
name|Object
name|obj
init|=
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Number
condition|)
block|{
name|width
operator|=
operator|(
operator|(
name|Number
operator|)
name|obj
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSNumber
condition|)
block|{
name|width
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected argument type. Expected : COSInteger or Number / Received : "
operator|+
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * @return the width of the CharProc glyph description      */
specifier|public
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|this
operator|.
name|width
return|;
block|}
block|}
end_class

end_unit

