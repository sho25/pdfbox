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
name|process
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
name|PreflightConfiguration
operator|.
name|ANNOTATIONS_PROCESS
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
name|ACROFORM_DICTIONARY_KEY_NEED_APPEARANCES
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
name|ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTIONS_FIELD
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
name|ERROR_ACTION_FORBIDDEN_WIDGET_ACTION_FIELD
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
name|ERROR_SYNTAX_DICT_INVALID
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
name|ERROR_SYNTAX_NOCATALOG
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
name|ERROR_SYNTAX_BODY
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
name|PDDocumentCatalog
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
name|interactive
operator|.
name|action
operator|.
name|PDFormFieldAdditionalActions
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
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
name|interactive
operator|.
name|form
operator|.
name|PDAcroForm
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
name|interactive
operator|.
name|form
operator|.
name|PDFieldTreeNode
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
name|utils
operator|.
name|ContextHelper
import|;
end_import

begin_class
specifier|public
class|class
name|AcroFormValidationProcess
extends|extends
name|AbstractProcess
block|{
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
name|PDAcroForm
name|acroForm
init|=
name|catalog
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
if|if
condition|(
name|acroForm
operator|!=
literal|null
condition|)
block|{
name|checkNeedAppearences
argument_list|(
name|ctx
argument_list|,
name|acroForm
argument_list|)
expr_stmt|;
try|try
block|{
name|exploreFields
argument_list|(
name|ctx
argument_list|,
name|acroForm
operator|.
name|getFields
argument_list|()
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
literal|"Unable to get the list of fields : "
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
block|}
else|else
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_NOCATALOG
argument_list|,
literal|"There is no Catalog entry in the Document"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks if the NeedAppearances entry is present. If it is, the value must be false.      *       * If the entry is invalid, the ERROR_SYNTAX_DICT_INVALID (1.2.3) error is return.      *       * @param ctx the preflight context.      * @param acroForm the AcroForm.      */
specifier|protected
name|void
name|checkNeedAppearences
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
if|if
condition|(
name|acroForm
operator|.
name|getDictionary
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|ACROFORM_DICTIONARY_KEY_NEED_APPEARANCES
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"NeedAppearance is present with the value \"true\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This function explores all fields and their children to validate them.      *       * @see #validateField(PreflightContext, PDFieldTreeNode)       *       * @param ctx the preflight context.      * @param lFields the list of fields, can be null.      * @return the result of the validation.      * @throws IOException      */
specifier|protected
name|boolean
name|exploreFields
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|lFields
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|lFields
operator|!=
literal|null
condition|)
block|{
comment|// the list can be null if the field doesn't have children
for|for
control|(
name|Object
name|obj
range|:
name|lFields
control|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|PDFieldTreeNode
condition|)
block|{
if|if
condition|(
operator|!
name|validateField
argument_list|(
name|ctx
argument_list|,
operator|(
name|PDFieldTreeNode
operator|)
name|obj
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|PDAnnotationWidget
condition|)
block|{
comment|// "A field's children in the hierarchy may also include widget annotations"
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
operator|(
operator|(
name|PDAnnotationWidget
operator|)
name|obj
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|ANNOTATIONS_PROCESS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_BODY
argument_list|,
literal|"Field can only have fields or widget annotations as KIDS"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * A and AA field are forbidden, this method checks if they are present and checks all children of this field. If the      * an Additional Action is present the error code ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTIONS_FIELD (6.2.3) is added      * to the error list If the an Action is present (in the Widget Annotation) the error      * ERROR_ACTION_FORBIDDEN_WIDGET_ACTION_FIELD (6.2.4) is added to the error list. (Remark : The widget validation      * will be done by the AnnotationValidationHelper, but some actions are authorized in a standard Widget)      *       * @param ctx the preflight context.      * @param aField an acro forms field.      * @return the result of the check for A or AA entries.      * @throws IOException      */
specifier|protected
name|boolean
name|validateField
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|PDFieldTreeNode
name|aField
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|res
init|=
literal|true
decl_stmt|;
name|PDFormFieldAdditionalActions
name|aa
init|=
name|aField
operator|.
name|getActions
argument_list|()
decl_stmt|;
if|if
condition|(
name|aa
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTIONS_FIELD
argument_list|,
literal|"\"AA\" must not be used in a Field dictionary"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|=
literal|false
expr_stmt|;
block|}
comment|/*          * The widget validation will be done by the widget annotation, a widget contained in a Field can't have action.          */
name|PDAnnotationWidget
name|widget
init|=
name|aField
operator|.
name|getWidget
argument_list|()
decl_stmt|;
if|if
condition|(
name|res
operator|&&
name|widget
operator|!=
literal|null
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
name|widget
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|ANNOTATIONS_PROCESS
argument_list|)
expr_stmt|;
name|COSBase
name|act
init|=
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|A
argument_list|)
decl_stmt|;
if|if
condition|(
name|act
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_WIDGET_ACTION_FIELD
argument_list|,
literal|"\"A\" must not be used in a widget annotation"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|res
operator|=
name|res
operator|&&
name|exploreFields
argument_list|(
name|ctx
argument_list|,
name|aField
operator|.
name|getKids
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

