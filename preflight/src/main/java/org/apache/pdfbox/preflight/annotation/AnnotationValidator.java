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
name|ACTIONS_PROCESS
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
name|PreflightConfiguration
operator|.
name|GRAPHIC_PROCESS
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
name|ERROR_ANNOT_FORBIDDEN_COLOR
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
name|ERROR_ANNOT_FORBIDDEN_FLAG
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
name|ERROR_ANNOT_INVALID_AP_CONTENT
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
name|ERROR_ANNOT_INVALID_CA
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
name|ERROR_ANNOT_MISSING_AP_N_CONTENT
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
name|COSDocument
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
name|graphics
operator|.
name|xobject
operator|.
name|PDXObjectForm
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
name|PDAnnotation
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
name|PDAppearanceDictionary
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
name|graphic
operator|.
name|ICCProfileWrapper
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
name|COSUtils
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
specifier|abstract
class|class
name|AnnotationValidator
block|{
specifier|protected
name|AnnotationValidatorFactory
name|annotFact
init|=
literal|null
decl_stmt|;
specifier|protected
name|PreflightContext
name|ctx
init|=
literal|null
decl_stmt|;
specifier|protected
name|COSDocument
name|cosDocument
init|=
literal|null
decl_stmt|;
comment|/** 	 * COSDictionary of the annotation 	 */
specifier|protected
name|COSDictionary
name|annotDictionary
init|=
literal|null
decl_stmt|;
comment|/** 	 * Instance of PDAnnotation built using the annotDictionary 	 */
specifier|protected
name|PDAnnotation
name|pdAnnot
init|=
literal|null
decl_stmt|;
specifier|public
name|AnnotationValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|annotDictionary
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|ctx
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|annotDictionary
operator|=
name|annotDictionary
expr_stmt|;
name|this
operator|.
name|cosDocument
operator|=
name|this
operator|.
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Checks if flags of the annotation are authorized. 	 *<UL> 	 *<li>Print flag must be 1 	 *<li>NoView flag must be 0 	 *<li>Hidden flag must be 0 	 *<li>Invisible flag must be 0 	 *</UL> 	 * If one of these flags is invalid, the errors list is updated with the 	 * ERROR_ANNOT_FORBIDDEN_FLAG ValidationError code. 	 *  	 * @return false if a flag is invalid, true otherwise. 	 */
specifier|protected
name|boolean
name|checkFlags
parameter_list|()
block|{
name|boolean
name|result
init|=
name|this
operator|.
name|pdAnnot
operator|.
name|isPrinted
argument_list|()
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|!
name|this
operator|.
name|pdAnnot
operator|.
name|isHidden
argument_list|()
expr_stmt|;
name|result
operator|=
name|result
operator|&&
operator|!
name|this
operator|.
name|pdAnnot
operator|.
name|isInvisible
argument_list|()
expr_stmt|;
name|result
operator|=
name|result
operator|&&
operator|!
name|this
operator|.
name|pdAnnot
operator|.
name|isNoView
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_FORBIDDEN_FLAG
argument_list|,
literal|"Flags of "
operator|+
name|pdAnnot
operator|.
name|getSubtype
argument_list|()
operator|+
literal|" annotation are invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/** 	 * Check if the CA value is 1.0. Return true if the CA element is missing or 	 * if the value is 1.0. Return false otherwise and update the list of 	 * errors present in the DocumentHandler. 	 *  	 * @return 	 */
specifier|protected
name|boolean
name|checkCA
parameter_list|()
block|{
name|COSBase
name|ca
init|=
name|this
operator|.
name|pdAnnot
operator|.
name|getDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CA
argument_list|)
decl_stmt|;
if|if
condition|(
name|ca
operator|!=
literal|null
condition|)
block|{
name|float
name|caf
init|=
name|COSUtils
operator|.
name|getAsFloat
argument_list|(
name|ca
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|caf
operator|!=
literal|1.0f
condition|)
block|{
comment|// ---- Only 1.0 is authorized as value
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_INVALID_CA
argument_list|,
literal|"CA entry is invalid. Expected 1.0 / Read "
operator|+
name|caf
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|//else  optional field,  ok
return|return
literal|true
return|;
block|}
comment|/** 	 * Return true if the C field is present in the Annotation dictionary and if 	 * the RGB profile is used in the DestOutputProfile of the OutputIntent 	 * dictionary. 	 *  	 * @return 	 */
specifier|protected
name|boolean
name|checkColors
parameter_list|()
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|this
operator|.
name|pdAnnot
operator|.
name|getColour
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|searchRGBProfile
argument_list|()
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_FORBIDDEN_COLOR
argument_list|,
literal|"Annotation uses a Color profile which isn't the same than the profile contained by the OutputIntent"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/** 	 * Search the RGB Profile in OutputIntents dictionaries 	 *  	 * @return true if a rgb profile is found, false otherwise. 	 */
specifier|protected
name|boolean
name|searchRGBProfile
parameter_list|()
throws|throws
name|ValidationException
block|{
name|ICCProfileWrapper
name|iccpw
init|=
name|ICCProfileWrapper
operator|.
name|getOrSearchICCProfile
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
if|if
condition|(
name|iccpw
operator|!=
literal|null
condition|)
block|{
return|return
name|iccpw
operator|.
name|isRGBColorSpace
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** 	 * This method checks the AP entry of the Annotation Dictionary. If the AP key 	 * is missing, this method returns true. If the AP key exists, only the N 	 * entry is authorized and must be a Stream which define the appearance of the 	 * annotation. (Currently, only the type of the N entry is checked because of 	 * the Appearance stream is a Form XObject, so it will be checked by the 	 * Graphics Helper) 	 *  	 * If the AP content isn't valid, this method return false and updates the 	 * errors list. 	 * @return 	 */
specifier|protected
name|boolean
name|checkAP
parameter_list|()
throws|throws
name|ValidationException
block|{
name|PDAppearanceDictionary
name|ap
init|=
name|this
operator|.
name|pdAnnot
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|ap
operator|!=
literal|null
condition|)
block|{
name|COSDictionary
name|apDict
init|=
name|ap
operator|.
name|getDictionary
argument_list|()
decl_stmt|;
comment|// Only N entry is authorized
if|if
condition|(
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
operator|!=
literal|null
operator|||
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|R
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_INVALID_AP_CONTENT
argument_list|,
literal|"Only the N Appearance is authorized"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// N entry is required
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_MISSING_AP_N_CONTENT
argument_list|,
literal|"The N Appearance must be present"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// the N entry must be a Stream (Dictionaries are forbidden)
name|COSBase
name|apn
init|=
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isStream
argument_list|(
name|apn
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_INVALID_AP_CONTENT
argument_list|,
literal|"The N Appearance must be a Stream"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// Appearance stream is a XObjectForm, check it.
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
operator|new
name|PDXObjectForm
argument_list|(
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|apn
argument_list|,
name|cosDocument
argument_list|)
argument_list|)
argument_list|,
name|GRAPHIC_PROCESS
argument_list|)
expr_stmt|;
block|}
block|}
comment|//else  ok, nothing to check,this field is optional
return|return
literal|true
return|;
block|}
comment|/** 	 * Extract a list of ActionManager from the Annotation dictionary and valid 	 * them. If an action is invalid, the errors list is updated and the method 	 * returns false. Otherwise, the method returns true and the errors list 	 * doesn't change. 	 *  	 * @return 	 * @throws ValidationException 	 */
specifier|protected
name|boolean
name|checkActions
parameter_list|()
throws|throws
name|ValidationException
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
name|annotDictionary
argument_list|,
name|ACTIONS_PROCESS
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/** 	 * This method validates the Popup entry. This entry shall contain an other 	 * Annotation. This annotation is validated with the right 	 * AnnotationValidator. 	 *  	 * @param errors 	 * @return 	 * @throws ValidationException 	 */
specifier|protected
name|boolean
name|checkPopup
parameter_list|()
throws|throws
name|ValidationException
block|{
name|COSBase
name|cosPopup
init|=
name|this
operator|.
name|annotDictionary
operator|.
name|getItem
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_POPUP
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosPopup
operator|!=
literal|null
condition|)
block|{
name|COSDictionary
name|popupDict
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|cosPopup
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|popupDict
operator|==
literal|null
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"An Annotation has a Popup entry, but the value is missing or isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|AnnotationValidator
name|popupVal
init|=
name|this
operator|.
name|annotFact
operator|.
name|getAnnotationValidator
argument_list|(
name|ctx
argument_list|,
name|popupDict
argument_list|)
decl_stmt|;
return|return
name|popupVal
operator|.
name|validate
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/** 	 * Execute validation of the Annotation dictionary. 	 *  	 * @return true if validation succeed, false otherwise. 	 * @throws ValidationException 	 */
specifier|public
name|boolean
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|boolean
name|isValide
init|=
name|checkMandatoryFields
argument_list|()
decl_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkFlags
argument_list|()
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkColors
argument_list|()
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkAP
argument_list|()
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkCA
argument_list|()
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkActions
argument_list|()
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkPopup
argument_list|()
expr_stmt|;
return|return
name|isValide
return|;
block|}
comment|/** 	 * Checks if all mandatory fields of an annotation are present. If some fields 	 * are missing, the method returns false and the errors list is updated. 	 *  	 * @param errors 	 *          list of errors which is updated if validation fails 	 * @return true if validation succeed, false otherwise. 	 */
specifier|protected
specifier|abstract
name|boolean
name|checkMandatoryFields
parameter_list|()
function_decl|;
comment|/** 	 * Initialize the annotFact attribute of this object. 	 * This method must be called by the Factory at the creation of this object. 	 * Only the Factory should call this method. 	 *   	 * @param fact 	 */
specifier|public
specifier|final
name|void
name|setFactory
parameter_list|(
name|AnnotationValidatorFactory
name|fact
parameter_list|)
block|{
name|this
operator|.
name|annotFact
operator|=
name|fact
expr_stmt|;
block|}
block|}
end_class

end_unit

