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
name|annotation
operator|.
name|pdfa
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_CIRCLE
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_FREETEXT
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_HIGHLIGHT
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_INK
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_LINE
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_LINK
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_POPUP
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_PRINTERMARK
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUARE
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUILGGLY
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_STAMP
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_STRIKEOUT
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_TEXT
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_TRAPNET
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_UNDERLINE
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
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_WIDGET
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
name|annotation
operator|.
name|AnnotationValidatorFactory
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
name|annotation
operator|.
name|FreeTextAnnotationValidator
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
name|annotation
operator|.
name|InkAnnotationValidator
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
name|annotation
operator|.
name|LineAnnotationValidator
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
name|annotation
operator|.
name|LinkAnnotationValidator
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
name|annotation
operator|.
name|MarkupAnnotationValidator
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
name|annotation
operator|.
name|PopupAnnotationValidator
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
name|annotation
operator|.
name|PrintMarkAnnotationValidator
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
name|annotation
operator|.
name|RubberStampAnnotationValidator
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
name|annotation
operator|.
name|SquareCircleAnnotationValidator
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
name|annotation
operator|.
name|TextAnnotationValidator
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
name|annotation
operator|.
name|TrapNetAnnotationValidator
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
name|annotation
operator|.
name|WidgetAnnotationValidator
import|;
end_import

begin_comment
comment|/**  * Factory to instantiate AnnotationValidator for a PDF/A-1b validation.  */
end_comment

begin_class
specifier|public
class|class
name|PDFAbAnnotationFactory
extends|extends
name|AnnotationValidatorFactory
block|{
annotation|@
name|Override
specifier|protected
name|void
name|initializeClasses
parameter_list|()
block|{
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_TEXT
argument_list|,
name|TextAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_LINK
argument_list|,
name|LinkAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_FREETEXT
argument_list|,
name|FreeTextAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_LINE
argument_list|,
name|LineAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUARE
argument_list|,
name|SquareCircleAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_CIRCLE
argument_list|,
name|SquareCircleAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_HIGHLIGHT
argument_list|,
name|MarkupAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_UNDERLINE
argument_list|,
name|MarkupAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_STRIKEOUT
argument_list|,
name|MarkupAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUILGGLY
argument_list|,
name|MarkupAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_STAMP
argument_list|,
name|RubberStampAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_INK
argument_list|,
name|InkAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_POPUP
argument_list|,
name|PopupAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_WIDGET
argument_list|,
name|WidgetAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_PRINTERMARK
argument_list|,
name|PrintMarkAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|validatorClasses
operator|.
name|put
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_TRAPNET
argument_list|,
name|TrapNetAnnotationValidator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

