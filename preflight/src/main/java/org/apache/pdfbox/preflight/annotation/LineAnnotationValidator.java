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
name|PreflightConstants
operator|.
name|ERROR_ANNOT_FORBIDDEN_COLOR
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
name|PDAnnotationLine
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

begin_comment
comment|/**  * Validation class for the LineAnnotation  */
end_comment

begin_class
specifier|public
class|class
name|LineAnnotationValidator
extends|extends
name|AnnotationValidator
block|{
comment|/**      * PDFBox object which wraps the annotation dictionary      */
specifier|protected
name|PDAnnotationLine
name|pdLine
init|=
literal|null
decl_stmt|;
specifier|public
name|LineAnnotationValidator
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|annotDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|ctx
argument_list|,
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdLine
operator|=
operator|new
name|PDAnnotationLine
argument_list|(
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdAnnot
operator|=
name|this
operator|.
name|pdLine
expr_stmt|;
block|}
comment|/**      * In addition of the AnnotationValidator.validate() method, this method executes the checkIColors method.      *       * @see AnnotationValidator#validate()      */
annotation|@
name|Override
specifier|public
name|boolean
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|boolean
name|isValid
init|=
name|super
operator|.
name|validate
argument_list|()
decl_stmt|;
name|isValid
operator|=
name|checkIColors
argument_list|()
operator|&&
name|isValid
expr_stmt|;
return|return
name|isValid
return|;
block|}
comment|/**      * Return true if the IC field is present in the Annotation dictionary and if the RGB profile is used in the      * DestOutputProfile of the OutputIntent dictionary.      *       * @return the state of the IC field validation.      */
specifier|protected
name|boolean
name|checkIColors
parameter_list|()
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|this
operator|.
name|pdLine
operator|.
name|getInteriorColor
argument_list|()
operator|!=
literal|null
operator|&&
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
return|return
literal|true
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see AnnotationValidator#checkMandatoryFields()      */
annotation|@
name|Override
specifier|protected
name|boolean
name|checkSpecificMandatoryFields
parameter_list|()
block|{
return|return
name|this
operator|.
name|annotDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|L
argument_list|)
return|;
block|}
block|}
end_class

end_unit

