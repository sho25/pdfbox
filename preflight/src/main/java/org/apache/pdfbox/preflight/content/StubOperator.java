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
name|ERROR_SYNTAX_ARRAY_TOO_LONG
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
name|ERROR_SYNTAX_LITERAL_TOO_LONG
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
name|ERROR_SYNTAX_NAME_TOO_LONG
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
name|ERROR_SYNTAX_NUMERIC_RANGE
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
name|ERROR_SYNTAX_TOO_MANY_ENTRIES
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
name|MAX_ARRAY_ELEMENTS
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
name|MAX_DICT_ENTRIES
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
name|MAX_NAME_SIZE
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
name|MAX_NEGATIVE_FLOAT
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
name|MAX_POSITIVE_FLOAT
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
name|MAX_STRING_LENGTH
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
name|Arrays
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
name|contentstream
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
name|contentstream
operator|.
name|operator
operator|.
name|OperatorName
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
name|OperatorProcessor
import|;
end_import

begin_comment
comment|/**  * This implementation of OperatorProcessor allow the operator validation according PDF/A rules without compute the  * operator actions.  */
end_comment

begin_class
specifier|public
class|class
name|StubOperator
extends|extends
name|OperatorProcessor
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_NO_OPERANDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|STROKE_PATH
argument_list|,
name|OperatorName
operator|.
name|FILL_NON_ZERO
argument_list|,
name|OperatorName
operator|.
name|LEGACY_FILL_NON_ZERO
argument_list|,
name|OperatorName
operator|.
name|FILL_EVEN_ODD
argument_list|,
name|OperatorName
operator|.
name|FILL_NON_ZERO_AND_STROKE
argument_list|,
name|OperatorName
operator|.
name|FILL_EVEN_ODD_AND_STROKE
argument_list|,
name|OperatorName
operator|.
name|CLOSE_FILL_NON_ZERO_AND_STROKE
argument_list|,
name|OperatorName
operator|.
name|CLOSE_FILL_EVEN_ODD_AND_STROKE
argument_list|,
name|OperatorName
operator|.
name|CLOSE_AND_STROKE
argument_list|,
name|OperatorName
operator|.
name|END_MARKED_CONTENT
argument_list|,
name|OperatorName
operator|.
name|CLOSE_PATH
argument_list|,
name|OperatorName
operator|.
name|CLIP_NON_ZERO
argument_list|,
name|OperatorName
operator|.
name|CLIP_EVEN_ODD
argument_list|,
name|OperatorName
operator|.
name|ENDPATH
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_STRING_OPERANDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|BEGIN_MARKED_CONTENT
argument_list|,
name|OperatorName
operator|.
name|SET_GRAPHICS_STATE_PARAMS
argument_list|,
name|OperatorName
operator|.
name|SET_RENDERINGINTENT
argument_list|,
name|OperatorName
operator|.
name|SHADING_FILL
argument_list|,
name|OperatorName
operator|.
name|SHOW_TEXT
argument_list|,
name|OperatorName
operator|.
name|SHOW_TEXT_LINE
argument_list|,
name|OperatorName
operator|.
name|MARKED_CONTENT_POINT
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_TAG_AND_PROPERTY_OPERANDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|BEGIN_MARKED_CONTENT_SEQ
argument_list|,
name|OperatorName
operator|.
name|MARKED_CONTENT_POINT_WITH_PROPS
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_NUMBER_OPERANDS_6
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|CURVE_TO
argument_list|,
name|OperatorName
operator|.
name|TYPE3_D1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_NUMBER_OPERANDS_4
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|CURVE_TO_REPLICATE_FINAL_POINT
argument_list|,
name|OperatorName
operator|.
name|CURVE_TO_REPLICATE_INITIAL_POINT
argument_list|,
name|OperatorName
operator|.
name|APPEND_RECT
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_NUMBER_OPERANDS_2
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|MOVE_TO
argument_list|,
name|OperatorName
operator|.
name|LINE_TO
argument_list|,
name|OperatorName
operator|.
name|TYPE3_D0
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_NUMBER_OPERANDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|NON_STROKING_GRAY
argument_list|,
name|OperatorName
operator|.
name|STROKING_COLOR_GRAY
argument_list|,
name|OperatorName
operator|.
name|SET_FLATNESS
argument_list|,
name|OperatorName
operator|.
name|SET_LINE_MITERLIMIT
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CHECK_ARRAY_OPERANDS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|OperatorName
operator|.
name|SHOW_TEXT_ADJUSTED
argument_list|)
decl_stmt|;
specifier|public
name|StubOperator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.util.operator.OperatorProcessor#process(org.apache.pdfbox .util.PDFOperator,      * java.util.List)      */
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Operator
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
name|String
name|opName
init|=
name|operator
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|CHECK_NO_OPERANDS
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNoOperands
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_STRING_OPERANDS
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkStringOperands
argument_list|(
name|arguments
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_TAG_AND_PROPERTY_OPERANDS
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkTagAndPropertyOperands
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_NUMBER_OPERANDS_6
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNumberOperands
argument_list|(
name|arguments
argument_list|,
literal|6
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_NUMBER_OPERANDS_4
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNumberOperands
argument_list|(
name|arguments
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_NUMBER_OPERANDS_2
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNumberOperands
argument_list|(
name|arguments
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_NUMBER_OPERANDS
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNumberOperands
argument_list|(
name|arguments
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CHECK_ARRAY_OPERANDS
operator|.
name|contains
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkArrayOperands
argument_list|(
name|arguments
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OperatorName
operator|.
name|SHOW_TEXT_LINE_AND_SPACE
operator|.
name|equals
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|checkNumberOperands
argument_list|(
name|arguments
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|checkStringOperands
argument_list|(
name|arguments
operator|.
name|subList
argument_list|(
literal|2
argument_list|,
name|arguments
operator|.
name|size
argument_list|()
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// ---- Some operators are processed by PDFBox Objects.
comment|// ---- Other operators are authorized but not used.
block|}
comment|/**      * If the arguments list of Operator isn't empty, this method throws a ContentStreamException.      *       * @param arguments      * @throws ContentStreamException      */
specifier|private
name|void
name|checkNoOperands
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|ContentStreamException
block|{
if|if
condition|(
name|arguments
operator|!=
literal|null
operator|&&
operator|!
name|arguments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
block|}
comment|/**      * If the arguments list of Operator doesn't have String parameter, this method throws a ContentStreamException.      *       * @param arguments      * @param length      * @throws ContentStreamException      */
specifier|private
name|void
name|checkStringOperands
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|ContentStreamException
block|{
if|if
condition|(
name|arguments
operator|==
literal|null
operator|||
name|arguments
operator|.
name|isEmpty
argument_list|()
operator|||
name|arguments
operator|.
name|size
argument_list|()
operator|!=
name|length
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
operator|++
name|i
control|)
block|{
name|COSBase
name|arg
init|=
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|arg
operator|instanceof
name|COSName
operator|)
operator|&&
operator|!
operator|(
name|arg
operator|instanceof
name|COSString
operator|)
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSName
operator|&&
operator|(
operator|(
name|COSName
operator|)
name|arg
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|>
name|MAX_NAME_SIZE
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_NAME_TOO_LONG
argument_list|,
literal|"A Name operand is too long"
argument_list|)
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSString
operator|&&
operator|(
operator|(
name|COSString
operator|)
name|arg
operator|)
operator|.
name|getString
argument_list|()
operator|.
name|getBytes
argument_list|()
operator|.
name|length
operator|>
name|MAX_STRING_LENGTH
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_LITERAL_TOO_LONG
argument_list|,
literal|"A String operand is too long"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * If the arguments list of Operator doesn't have Array parameter, this method throws a ContentStreamException.      *       * @param arguments      * @param length      * @throws ContentStreamException      */
specifier|private
name|void
name|checkArrayOperands
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|ContentStreamException
block|{
if|if
condition|(
name|arguments
operator|==
literal|null
operator|||
name|arguments
operator|.
name|isEmpty
argument_list|()
operator|||
name|arguments
operator|.
name|size
argument_list|()
operator|!=
name|length
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
operator|++
name|i
control|)
block|{
name|COSBase
name|arg
init|=
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|arg
operator|instanceof
name|COSArray
operator|)
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
if|if
condition|(
operator|(
operator|(
name|COSArray
operator|)
name|arg
operator|)
operator|.
name|size
argument_list|()
operator|>
name|MAX_ARRAY_ELEMENTS
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_ARRAY_TOO_LONG
argument_list|,
literal|"Array has "
operator|+
operator|(
operator|(
name|COSArray
operator|)
name|arg
operator|)
operator|.
name|size
argument_list|()
operator|+
literal|" elements"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * If the arguments list of Operator doesn't have Number parameters (Int, float...), this method throws a      * ContentStreamException.      *       * @param arguments      *            the arguments list to check      * @param length      *            the expected size of the list      * @throws ContentStreamException      */
specifier|private
name|void
name|checkNumberOperands
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|ContentStreamException
block|{
if|if
condition|(
name|arguments
operator|==
literal|null
operator|||
name|arguments
operator|.
name|isEmpty
argument_list|()
operator|||
name|arguments
operator|.
name|size
argument_list|()
operator|!=
name|length
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
for|for
control|(
name|COSBase
name|arg
range|:
name|arguments
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|arg
operator|instanceof
name|COSFloat
operator|)
operator|&&
operator|!
operator|(
name|arg
operator|instanceof
name|COSInteger
operator|)
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSInteger
operator|&&
operator|(
operator|(
operator|(
name|COSInteger
operator|)
name|arg
operator|)
operator|.
name|longValue
argument_list|()
operator|>
name|Integer
operator|.
name|MAX_VALUE
operator|||
operator|(
operator|(
name|COSInteger
operator|)
name|arg
operator|)
operator|.
name|longValue
argument_list|()
operator|<
name|Integer
operator|.
name|MIN_VALUE
operator|)
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_NUMERIC_RANGE
argument_list|,
literal|"Invalid integer range in a Number operand"
argument_list|)
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSFloat
operator|&&
operator|(
operator|(
operator|(
name|COSFloat
operator|)
name|arg
operator|)
operator|.
name|doubleValue
argument_list|()
operator|>
name|MAX_POSITIVE_FLOAT
operator|||
operator|(
operator|(
name|COSFloat
operator|)
name|arg
operator|)
operator|.
name|doubleValue
argument_list|()
operator|<
name|MAX_NEGATIVE_FLOAT
operator|)
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_NUMERIC_RANGE
argument_list|,
literal|"Invalid float range in a Number operand"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * The given arguments list is valid only if the first argument is a Tag (A String) and if the second argument is a      * String or a Dictionary      *       * @param arguments      * @throws ContentStreamException      */
specifier|private
name|void
name|checkTagAndPropertyOperands
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|ContentStreamException
block|{
if|if
condition|(
name|arguments
operator|==
literal|null
operator|||
name|arguments
operator|.
name|isEmpty
argument_list|()
operator|||
name|arguments
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
name|COSBase
name|arg
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
operator|!
operator|(
name|arg
operator|instanceof
name|COSName
operator|)
operator|&&
operator|!
operator|(
name|arg
operator|instanceof
name|COSString
operator|)
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSName
operator|&&
operator|(
operator|(
name|COSName
operator|)
name|arg
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|>
name|MAX_NAME_SIZE
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_NAME_TOO_LONG
argument_list|,
literal|"A Name operand is too long"
argument_list|)
throw|;
block|}
if|if
condition|(
name|arg
operator|instanceof
name|COSString
operator|&&
operator|(
operator|(
name|COSString
operator|)
name|arg
operator|)
operator|.
name|getString
argument_list|()
operator|.
name|getBytes
argument_list|()
operator|.
name|length
operator|>
name|MAX_STRING_LENGTH
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_LITERAL_TOO_LONG
argument_list|,
literal|"A String operand is too long"
argument_list|)
throw|;
block|}
name|COSBase
name|arg2
init|=
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|arg2
operator|instanceof
name|COSName
operator|)
operator|&&
operator|!
operator|(
name|arg2
operator|instanceof
name|COSString
operator|)
operator|&&
operator|!
operator|(
name|arg2
operator|instanceof
name|COSDictionary
operator|)
condition|)
block|{
throw|throw
name|createInvalidArgumentsError
argument_list|()
throw|;
block|}
if|if
condition|(
name|arg2
operator|instanceof
name|COSName
operator|&&
operator|(
operator|(
name|COSName
operator|)
name|arg2
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|>
name|MAX_NAME_SIZE
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_NAME_TOO_LONG
argument_list|,
literal|"A Name operand is too long"
argument_list|)
throw|;
block|}
if|if
condition|(
name|arg2
operator|instanceof
name|COSString
operator|&&
operator|(
operator|(
name|COSString
operator|)
name|arg2
operator|)
operator|.
name|getString
argument_list|()
operator|.
name|getBytes
argument_list|()
operator|.
name|length
operator|>
name|MAX_STRING_LENGTH
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_LITERAL_TOO_LONG
argument_list|,
literal|"A String operand is too long"
argument_list|)
throw|;
block|}
if|if
condition|(
name|arg2
operator|instanceof
name|COSDictionary
operator|&&
operator|(
operator|(
name|COSDictionary
operator|)
name|arg2
operator|)
operator|.
name|size
argument_list|()
operator|>
name|MAX_DICT_ENTRIES
condition|)
block|{
throw|throw
name|createLimitError
argument_list|(
name|ERROR_SYNTAX_TOO_MANY_ENTRIES
argument_list|,
literal|"Dictionary has "
operator|+
operator|(
operator|(
name|COSDictionary
operator|)
name|arg2
operator|)
operator|.
name|size
argument_list|()
operator|+
literal|" entries"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create a ContentStreamException with ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT.      *       * @return the ContentStreamException created.      */
specifier|private
name|ContentStreamException
name|createInvalidArgumentsError
parameter_list|()
block|{
name|ContentStreamException
name|ex
init|=
operator|new
name|ContentStreamException
argument_list|(
literal|"Invalid arguments"
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setErrorCode
argument_list|(
name|ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
comment|/**      * Create a ContentStreamException with ERROR_SYNTAX_CONTENT_STREAM_INVALID_ARGUMENT.      *       * @return the ContentStreamException created.      */
specifier|private
name|ContentStreamException
name|createLimitError
parameter_list|(
name|String
name|errorCode
parameter_list|,
name|String
name|details
parameter_list|)
block|{
name|ContentStreamException
name|ex
init|=
operator|new
name|ContentStreamException
argument_list|(
name|details
argument_list|)
decl_stmt|;
name|ex
operator|.
name|setErrorCode
argument_list|(
name|errorCode
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

