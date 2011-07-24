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
name|padaf
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
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_CA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_D
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_N
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_R
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
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
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
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
name|padaf
operator|.
name|preflight
operator|.
name|DocumentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
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
name|padaf
operator|.
name|preflight
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
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
name|padaf
operator|.
name|preflight
operator|.
name|actions
operator|.
name|AbstractActionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|actions
operator|.
name|ActionManagerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|graphics
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
name|padaf
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
name|ActionManagerFactory
name|actionFact
init|=
literal|null
decl_stmt|;
specifier|protected
name|COSDocument
name|cosDoc
init|=
literal|null
decl_stmt|;
specifier|protected
name|DocumentHandler
name|handler
init|=
literal|null
decl_stmt|;
comment|/**    * COSDictionary of the annotation    */
specifier|protected
name|COSDictionary
name|annotDictionary
init|=
literal|null
decl_stmt|;
comment|/**    * Instance of PDAnnotation built using the annotDictionary    */
specifier|protected
name|PDAnnotation
name|pdAnnot
init|=
literal|null
decl_stmt|;
specifier|public
name|AnnotationValidator
parameter_list|(
name|DocumentHandler
name|handler
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
name|handler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|cosDoc
operator|=
name|handler
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
expr_stmt|;
name|this
operator|.
name|annotDictionary
operator|=
name|annotDictionary
expr_stmt|;
block|}
comment|/**    * Checks if flags of the annotation are authorized.    *<UL>    *<li>Print flag must be 1    *<li>NoView flag must be 0    *<li>Hidden flag must be 0    *<li>Invisible flag must be 0    *</UL>    * If one of these flags is invalid, the errors list is updated with the    * ERROR_ANNOT_FORBIDDEN_FLAG ValidationError code.    *     * @param errors    *          list of errors which is updated if validation fails    * @return false if a flag is invalid, true otherwise.    */
specifier|protected
name|boolean
name|checkFlags
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
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
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
comment|/**    * Check if the CA value is 1.0. Return true if the CA element is missing or    * if the value is 1.0. Return false otherwise and update the given list of    * errors.    *     * @param errors    *          list of errors which is updated if validation fails    * @return    */
specifier|protected
name|boolean
name|checkCA
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
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
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_CA
argument_list|)
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
name|this
operator|.
name|pdAnnot
operator|.
name|getDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_CA
argument_list|)
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
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
comment|/**    * Return true if the C field is present in the Annotation dictionary and if    * the RGB profile is used in the DestOutputProfile of the OutputIntent    * dictionary.    *     * @param errors    *          list of errors which is updated if no RGB profile is found when    *          the C element is present    * @return    */
specifier|protected
name|boolean
name|checkColors
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
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
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
comment|/**    * Search the RGB Profile in OutputIntents dictionaries    *     * @return true if a rgb profile is found, false otherwise.    */
specifier|protected
name|boolean
name|searchRGBProfile
parameter_list|()
block|{
name|ICCProfileWrapper
name|iccpw
init|=
name|this
operator|.
name|handler
operator|.
name|getIccProfileWrapper
argument_list|()
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
comment|/**    * This method checks the AP entry of the Annotation Dictionary. If the AP key    * is missing, this method returns true. If the AP key exists, only the N    * entry is authorized and must be a Stream which define the appearance of the    * annotation. (Currently, only the type of the N entry is checked because of    * the Appearance stream is a Form XObject, so it will be checked by the    * Graphics Helper)    *     * If the AP content isn't valid, this method return false and updates the    * errors list.    *     * @param errors    *          list of errors which is updated if validation fails    * @return    */
specifier|protected
name|boolean
name|checkAP
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
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
comment|// ---- Only N entry is authorized
if|if
condition|(
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_D
argument_list|)
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
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_R
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_N
argument_list|)
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// ---- N entry is required
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
comment|// ---- the N entry must be a Stream (Dictionaries are forbidden)
name|COSBase
name|apn
init|=
name|apDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_KEY_N
argument_list|)
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
name|this
operator|.
name|cosDoc
argument_list|)
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
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
block|}
block|}
comment|//else  ok, nothing to check,this field is optional
return|return
literal|true
return|;
block|}
comment|/**    * Extract a list of ActionManager from the Annotation dictionary and valid    * them. If an action is invalid, the errors list is updated and the method    * returns false. Otherwise, the method returns true and the errors list    * doesn't change.    *     * @param errors    *          list of errors which is updated if validation fails    * @return    * @throws ValidationException    */
specifier|protected
name|boolean
name|checkActions
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|actionFact
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|AbstractActionManager
argument_list|>
name|la
init|=
name|actionFact
operator|.
name|getActions
argument_list|(
name|annotDictionary
argument_list|,
name|cosDoc
argument_list|)
decl_stmt|;
for|for
control|(
name|AbstractActionManager
name|aMng
range|:
name|la
control|)
block|{
if|if
condition|(
operator|!
name|aMng
operator|.
name|valid
argument_list|(
name|errors
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * This method validates the Popup entry. This entry shall contain an other    * Annotation. This annotation is validated with the right    * AnnotationValidator.    *     * @param errors    * @return    * @throws ValidationException    */
specifier|protected
name|boolean
name|checkPopup
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
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
name|COSName
operator|.
name|getPDFName
argument_list|(
name|ANNOT_DICTIONARY_VALUE_SUBTYPE_POPUP
argument_list|)
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
name|this
operator|.
name|cosDoc
argument_list|)
decl_stmt|;
if|if
condition|(
name|popupDict
operator|==
literal|null
condition|)
block|{
name|errors
operator|.
name|add
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
name|popupDict
argument_list|,
name|handler
argument_list|,
name|errors
argument_list|)
decl_stmt|;
return|return
name|popupVal
operator|.
name|validate
argument_list|(
name|errors
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Execute validation of the Annotation dictionary.    *     * @param errors    *          list of errors which is updated if validation fails    * @return true if validation succeed, false otherwise.    * @throws ValidationException    */
specifier|public
name|boolean
name|validate
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
throws|throws
name|ValidationException
block|{
name|boolean
name|isValide
init|=
name|checkMandatoryFields
argument_list|(
name|errors
argument_list|)
decl_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkFlags
argument_list|(
name|errors
argument_list|)
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkColors
argument_list|(
name|errors
argument_list|)
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkAP
argument_list|(
name|errors
argument_list|)
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkCA
argument_list|(
name|errors
argument_list|)
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkActions
argument_list|(
name|errors
argument_list|)
expr_stmt|;
name|isValide
operator|=
name|isValide
operator|&&
name|checkPopup
argument_list|(
name|errors
argument_list|)
expr_stmt|;
return|return
name|isValide
return|;
block|}
comment|/**    * Checks if all mandatory fields of an annotation are present. If some fields    * are missing, the method returns false and the errors list is updated.    *     * @param errors    *          list of errors which is updated if validation fails    * @return true if validation succeed, false otherwise.    */
specifier|protected
specifier|abstract
name|boolean
name|checkMandatoryFields
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
function_decl|;
comment|/**    * Initialize the annotFact attribute of this object.    * This method must be called by the Factory at the creation of this object.    * Only the Factory should call this method.    *      * @param fact    */
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
comment|//  /**
comment|//   * Return an instance of AnnotationValidator if the annotation subtype is
comment|//   * authorized for a PDF/A. Otherwise, returns null and the given list is
comment|//   * updated with the right error code.
comment|//   *
comment|//   * If the subtype isn't mentioned in the PDF/A specification and if it doesn't
comment|//   * exist in the PDF Reference 1.4, it will be considered as an invalid
comment|//   * annotation. Here is the list of Annotations which appear after the PDF 1.4
comment|//   * :
comment|//   *<UL>
comment|//   *<li>Polygon (1.5)
comment|//   *<li>Polyline (1.5)
comment|//   *<li>Caret (1.5)
comment|//   *<li>Screen (1.5)
comment|//   *<li>Watermark (1.6)
comment|//   *<li>3D (1.6)
comment|//   *</UL>
comment|//   *
comment|//   * @param annotDic
comment|//   * @param handler
comment|//   * @param errors
comment|//   * @return
comment|//   */
comment|//  public static AnnotationValidator getAnnotationValidator(
comment|//      COSDictionary annotDic, DocumentHandler handler,
comment|//      List<ValidationError> errors) {
comment|//    AnnotationValidator av = null;
comment|//
comment|//    String subtype = annotDic.getNameAsString(COSName
comment|//        .getPDFName(DICTIONARY_KEY_SUBTYPE));
comment|//    if (subtype == null || "".equals(subtype)) {
comment|//      errors.add(new ValidationError(ERROR_ANNOT_MISSING_SUBTYPE));
comment|//    } else {
comment|//      if (ANNOT_DICTIONARY_VALUE_SUBTYPE_TEXT.equals(subtype)) {
comment|//        av = new TextAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_LINK.equals(subtype)) {
comment|//        av = new LinkAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_FREETEXT.equals(subtype)) {
comment|//        av = new FreeTextAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_LINE.equals(subtype)) {
comment|//        av = new LineAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUARE.equals(subtype)
comment|//          || ANNOT_DICTIONARY_VALUE_SUBTYPE_CIRCLE.equals(subtype)) {
comment|//        av = new SquareCircleAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_HIGHLIGHT.equals(subtype)
comment|//          || ANNOT_DICTIONARY_VALUE_SUBTYPE_UNDERLINE.equals(subtype)
comment|//          || ANNOT_DICTIONARY_VALUE_SUBTYPE_STRIKEOUT.equals(subtype)
comment|//          || ANNOT_DICTIONARY_VALUE_SUBTYPE_SQUILGGLY.equals(subtype)) {
comment|//        av = new MarkupAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_STAMP.equals(subtype)) {
comment|//        av = new RubberStampAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_INK.equals(subtype)) {
comment|//        av = new InkAnnotationValdiator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_POPUP.equals(subtype)) {
comment|//        av = new PopupAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_WIDGET.equals(subtype)) {
comment|//        av = new WidgetAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_PRINTERMARK.equals(subtype)) {
comment|//        av = new PrintMarkAnnotationValidator(handler, annotDic);
comment|//      } else if (ANNOT_DICTIONARY_VALUE_SUBTYPE_TRAPNET.equals(subtype)) {
comment|//        av = new TrapNetAnnotationValidator(handler, annotDic);
comment|//      } else {
comment|//        errors.add(new ValidationError(ERROR_ANNOT_FORBIDDEN_SUBTYPE,
comment|//            "The subtype isn't authorized : " + subtype));
comment|//      }
comment|//    }
comment|//    return av;
comment|//  }
block|}
end_class

end_unit

