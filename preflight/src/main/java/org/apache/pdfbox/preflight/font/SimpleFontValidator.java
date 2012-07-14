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
name|ERROR_FONTS_DICTIONARY_INVALID
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

begin_class
specifier|public
specifier|abstract
class|class
name|SimpleFontValidator
parameter_list|<
name|T
extends|extends
name|FontContainer
parameter_list|>
extends|extends
name|FontValidator
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|SimpleFontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|T
name|fContainer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
argument_list|,
name|fContainer
argument_list|)
expr_stmt|;
block|}
comment|/**    * Call this method to validate the font wrapped by this object.    * If the validation failed, the error is updated in the FontContainer     * with the right error code.    *     * Errors that are saved in the container will be added on the PreflightContext if the font is used later.    *     * @return    */
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|checkMandatoryField
argument_list|()
expr_stmt|;
name|createFontDescriptorHelper
argument_list|()
expr_stmt|;
name|processFontDescriptorValidation
argument_list|()
expr_stmt|;
name|checkEncoding
argument_list|()
expr_stmt|;
name|checkToUnicode
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|checkMandatoryField
parameter_list|()
block|{
name|COSDictionary
name|fontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|boolean
name|areFieldsPResent
init|=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|areFieldsPResent
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"Some required fields are missing from the Font dictionary."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|createFontDescriptorHelper
parameter_list|()
function_decl|;
specifier|protected
name|void
name|processFontDescriptorValidation
parameter_list|()
block|{
name|this
operator|.
name|descriptorHelper
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

