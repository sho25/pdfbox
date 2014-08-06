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
name|content
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_FONTS_ENCODING_ERROR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_FONTS_UNKNOWN_FONT_REF
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_CONTENT_STREAM_UNSUPPORTED_OP
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
name|COSInteger
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
name|cos
operator|.
name|COSStream
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
name|PDResources
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
name|PDStream
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
name|PDFont
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
name|state
operator|.
name|PDGraphicsState
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
name|form
operator|.
name|PDFormXObject
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
name|state
operator|.
name|PDTextState
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
name|state
operator|.
name|RenderingMode
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
name|ValidationResult
operator|.
name|ValidationError
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
name|exception
operator|.
name|ValidationException
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
name|font
operator|.
name|container
operator|.
name|FontContainer
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
name|font
operator|.
name|util
operator|.
name|GlyphException
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
name|operator
operator|.
name|Operator
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
name|operator
operator|.
name|Operator
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
name|operator
operator|.
name|OperatorProcessor
import|;
end_import

begin_class
specifier|public
class|class
name|PreflightContentStream
extends|extends
name|PreflightStreamEngine
block|{
specifier|public
name|PreflightContentStream
parameter_list|(
name|PreflightContext
name|_context
parameter_list|,
name|PDPage
name|_page
parameter_list|)
block|{
name|super
argument_list|(
name|_context
argument_list|,
name|_page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Process the validation of a PageContent (The page is initialized by the constructor)      *      * @return A list of validation error. This list is empty if the validation succeed.      * @throws ValidationException      */
specifier|public
name|void
name|validPageContentStream
parameter_list|()
throws|throws
name|ValidationException
block|{
try|try
block|{
name|PDStream
name|pstream
init|=
name|this
operator|.
name|processeedPage
operator|.
name|getContents
argument_list|()
decl_stmt|;
if|if
condition|(
name|pstream
operator|!=
literal|null
condition|)
block|{
name|processStream
argument_list|(
name|processeedPage
operator|.
name|findResources
argument_list|()
argument_list|,
name|pstream
operator|.
name|getStream
argument_list|()
argument_list|,
name|processeedPage
operator|.
name|findCropBox
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ContentStreamException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to check the ContentStream : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Process the validation of a XObject Form      *       * @param xobj      * @return A list of validation error. This list is empty if the validation succeed.      * @throws ValidationException      */
specifier|public
name|void
name|validXObjContentStream
parameter_list|(
name|PDFormXObject
name|xobj
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|initStream
argument_list|(
name|xobj
operator|.
name|getBBox
argument_list|()
argument_list|)
expr_stmt|;
name|processSubStream
argument_list|(
name|xobj
operator|.
name|getResources
argument_list|()
argument_list|,
name|xobj
operator|.
name|getCOSStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentStreamException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to check the ContentStream : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Process the validation of a Tiling Pattern      *       * @param pattern      * @return A list of validation error. This list is empty if the validation succeed.      * @throws ValidationException      */
specifier|public
name|void
name|validPatternContentStream
parameter_list|(
name|COSStream
name|pattern
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|COSDictionary
name|res
init|=
operator|(
name|COSDictionary
operator|)
name|pattern
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
name|initStream
argument_list|(
name|processeedPage
operator|.
name|findCropBox
argument_list|()
argument_list|)
expr_stmt|;
name|processSubStream
argument_list|(
operator|new
name|PDResources
argument_list|(
name|res
argument_list|)
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentStreamException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to check the ContentStream : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.util.PDFStreamEngine#processOperator(org.apache.pdfbox .util.PDFOperator, java.util.List)      */
specifier|protected
name|void
name|processOperator
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
comment|/*          * Here is a copy of the super method because the else block is different. (If the operator is unknown, throw an          * exception)          */
name|String
name|operation
init|=
name|operator
operator|.
name|getOperation
argument_list|()
decl_stmt|;
name|OperatorProcessor
name|processor
init|=
operator|(
name|OperatorProcessor
operator|)
name|contentStreamEngineOperators
operator|.
name|get
argument_list|(
name|operation
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|registerError
argument_list|(
literal|"The operator \""
operator|+
name|operation
operator|+
literal|"\" isn't supported."
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_UNSUPPORTED_OP
argument_list|)
expr_stmt|;
return|return;
block|}
comment|/*          * Process Specific Validation. The Generic Processing is useless for PDFA validation          */
if|if
condition|(
literal|"BI"
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|validImageFilter
argument_list|(
name|operator
argument_list|)
expr_stmt|;
name|validImageColorSpace
argument_list|(
name|operator
argument_list|)
expr_stmt|;
block|}
name|checkShowTextOperators
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
name|checkColorOperators
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|validRenderingIntent
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
name|checkSetColorSpaceOperators
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
name|validNumberOfGraphicStates
argument_list|(
name|operator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Process Text Validation. According to the operator one of the both method will be called.      * (validStringDefinition(PDFOperator operator, List<?> arguments) / validStringArray(PDFOperator operator, List<?>      * arguments))      *       * @param operator      * @param arguments      * @throws ContentStreamException      * @throws IOException      */
specifier|protected
name|void
name|checkShowTextOperators
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|ContentStreamException
throws|,
name|IOException
block|{
name|String
name|op
init|=
name|operator
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Tj"
operator|.
name|equals
argument_list|(
name|op
argument_list|)
operator|||
literal|"'"
operator|.
name|equals
argument_list|(
name|op
argument_list|)
operator|||
literal|"\""
operator|.
name|equals
argument_list|(
name|op
argument_list|)
condition|)
block|{
name|validStringDefinition
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"TJ"
operator|.
name|equals
argument_list|(
name|op
argument_list|)
condition|)
block|{
name|validStringArray
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Process Text Validation for the Operands of a Tj, "'" and "\"" operator.      *       * If the validation fails for an unexpected reason, a IOException is thrown. If the validation fails due to      * validation error, a ContentStreamException is thrown. (Use the ValidationError attribute to know the cause)      *       * @param operator      * @param arguments      * @throws ContentStreamException      * @throws IOException      */
specifier|private
name|void
name|validStringDefinition
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|ContentStreamException
throws|,
name|IOException
block|{
comment|/*          * For a Text operator, the arguments list should contain only one COSString object          */
if|if
condition|(
literal|"\""
operator|.
name|equals
argument_list|(
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|!=
literal|3
condition|)
block|{
name|registerError
argument_list|(
literal|"Invalid argument for the operator : "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return;
block|}
name|Object
name|arg0
init|=
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|arg1
init|=
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
name|arg2
init|=
name|arguments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|arg0
operator|instanceof
name|COSInteger
operator|||
name|arg0
operator|instanceof
name|COSFloat
operator|)
operator|||
operator|!
operator|(
name|arg1
operator|instanceof
name|COSInteger
operator|||
name|arg1
operator|instanceof
name|COSFloat
operator|)
condition|)
block|{
name|registerError
argument_list|(
literal|"Invalid argument for the operator : "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|arg2
operator|instanceof
name|COSString
condition|)
block|{
name|validText
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|arg2
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|registerError
argument_list|(
literal|"Invalid argument for the operator : "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
else|else
block|{
name|Object
name|objStr
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
name|objStr
operator|instanceof
name|COSString
condition|)
block|{
name|validText
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|objStr
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|objStr
operator|instanceof
name|COSInteger
operator|)
condition|)
block|{
name|registerError
argument_list|(
literal|"Invalid argument for the operator : "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|/**      * Process Text Validation for the Operands of a TJ operator.      *       * If the validation fails for an unexpected reason, a IOException is thrown. If the validation fails due to      * validation error, a ContentStreamException is thrown. (Use the ValidationError attribute to know the cause)      *       * @param operator      * @param arguments      * @throws ContentStreamException      * @throws IOException      */
specifier|private
name|void
name|validStringArray
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|ContentStreamException
throws|,
name|IOException
block|{
for|for
control|(
name|Object
name|object
range|:
name|arguments
control|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|COSArray
condition|)
block|{
name|validStringArray
argument_list|(
name|operator
argument_list|,
operator|(
operator|(
name|COSArray
operator|)
name|object
operator|)
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|COSString
condition|)
block|{
name|validText
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|object
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|COSInteger
operator|||
name|object
operator|instanceof
name|COSFloat
operator|)
condition|)
block|{
name|registerError
argument_list|(
literal|"Invalid argument for the operator : "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|,
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|/**      * Process the validation of a Text operand contains in a ContentStream This validation checks that :      *<UL>      *<li>The font isn't missing if the Rendering Mode isn't 3      *<li>The font metrics are consistent      *<li>All character used in the text are defined in the font program.      *</UL>      *       * @param string      * @throws IOException      */
specifier|public
name|void
name|validText
parameter_list|(
name|byte
index|[]
name|string
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TextSize accessible through the TextState
name|PDTextState
name|textState
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
decl_stmt|;
specifier|final
name|RenderingMode
name|renderingMode
init|=
name|textState
operator|.
name|getRenderingMode
argument_list|()
decl_stmt|;
specifier|final
name|PDFont
name|font
init|=
name|textState
operator|.
name|getFont
argument_list|()
decl_stmt|;
if|if
condition|(
name|font
operator|==
literal|null
condition|)
block|{
comment|// Unable to decode the Text without Font
name|registerError
argument_list|(
literal|"Text operator can't be process without Font"
argument_list|,
name|ERROR_FONTS_UNKNOWN_FONT_REF
argument_list|)
expr_stmt|;
return|return;
block|}
name|FontContainer
name|fontContainer
init|=
name|context
operator|.
name|getFontContainer
argument_list|(
name|font
operator|.
name|getCOSObject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|renderingMode
operator|==
name|RenderingMode
operator|.
name|NEITHER
operator|&&
operator|(
name|fontContainer
operator|==
literal|null
operator|||
operator|!
name|fontContainer
operator|.
name|isEmbeddedFont
argument_list|()
operator|)
condition|)
block|{
comment|// font not embedded and rendering mode is 3. Valid case and nothing to check
return|return;
block|}
elseif|else
if|if
condition|(
name|fontContainer
operator|==
literal|null
condition|)
block|{
comment|// Font Must be embedded if the RenderingMode isn't 3
name|registerError
argument_list|(
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|" is unknown wasn't found by the FontHelperValdiator"
argument_list|,
name|ERROR_FONTS_UNKNOWN_FONT_REF
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
operator|!
name|fontContainer
operator|.
name|isValid
argument_list|()
operator|&&
operator|!
name|fontContainer
operator|.
name|errorsAleadyMerged
argument_list|()
condition|)
block|{
name|context
operator|.
name|addValidationErrors
argument_list|(
name|fontContainer
operator|.
name|getAllErrors
argument_list|()
argument_list|)
expr_stmt|;
name|fontContainer
operator|.
name|setErrorsAleadyMerged
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|fontContainer
operator|.
name|isValid
argument_list|()
operator|&&
name|fontContainer
operator|.
name|errorsAleadyMerged
argument_list|()
condition|)
block|{
comment|// font already computed
return|return;
block|}
name|int
name|codeLength
init|=
literal|1
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
name|string
operator|.
name|length
condition|;
name|i
operator|+=
name|codeLength
control|)
block|{
comment|// explore the string to detect character code (length can be 1 or 2 bytes)
name|int
name|cid
init|=
operator|-
literal|1
decl_stmt|;
name|codeLength
operator|=
literal|1
expr_stmt|;
try|try
block|{
comment|// according to the encoding, extract the character identifier
name|cid
operator|=
name|font
operator|.
name|encodeToCID
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
expr_stmt|;
if|if
condition|(
name|cid
operator|==
operator|-
literal|1
operator|&&
name|i
operator|+
literal|1
operator|<
name|string
operator|.
name|length
condition|)
block|{
comment|// maybe a multibyte encoding
name|codeLength
operator|++
expr_stmt|;
name|cid
operator|=
name|font
operator|.
name|encodeToCID
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
expr_stmt|;
block|}
name|fontContainer
operator|.
name|checkGlyphWith
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|registerError
argument_list|(
literal|"Encoding can't interpret the character code"
argument_list|,
name|ERROR_FONTS_ENCODING_ERROR
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|GlyphException
name|e
parameter_list|)
block|{
if|if
condition|(
name|renderingMode
operator|!=
name|RenderingMode
operator|.
name|NEITHER
condition|)
block|{
name|registerError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

