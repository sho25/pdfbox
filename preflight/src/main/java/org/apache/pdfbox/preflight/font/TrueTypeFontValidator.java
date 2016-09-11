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
name|ERROR_FONTS_ENCODING
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
name|encoding
operator|.
name|Encoding
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
name|encoding
operator|.
name|MacRomanEncoding
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
name|encoding
operator|.
name|WinAnsiEncoding
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
name|PDFontDescriptor
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
name|PDTrueTypeFont
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
name|font
operator|.
name|container
operator|.
name|TrueTypeContainer
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
name|descriptor
operator|.
name|TrueTypeDescriptorHelper
import|;
end_import

begin_class
specifier|public
class|class
name|TrueTypeFontValidator
extends|extends
name|SimpleFontValidator
argument_list|<
name|TrueTypeContainer
argument_list|>
block|{
specifier|public
name|TrueTypeFontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDTrueTypeFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
argument_list|,
name|font
operator|.
name|getCOSObject
argument_list|()
argument_list|,
operator|new
name|TrueTypeContainer
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createFontDescriptorHelper
parameter_list|()
block|{
name|this
operator|.
name|descriptorHelper
operator|=
operator|new
name|TrueTypeDescriptorHelper
argument_list|(
name|context
argument_list|,
operator|(
name|PDTrueTypeFont
operator|)
name|font
argument_list|,
name|fontContainer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|checkEncoding
parameter_list|()
block|{
name|PDTrueTypeFont
name|ttFont
init|=
operator|(
name|PDTrueTypeFont
operator|)
name|font
decl_stmt|;
name|PDFontDescriptor
name|fd
init|=
name|ttFont
operator|.
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
comment|/*              * only MacRomanEncoding or WinAnsiEncoding are allowed for a non symbolic font.              */
if|if
condition|(
name|fd
operator|.
name|isNonSymbolic
argument_list|()
condition|)
block|{
name|Encoding
name|encodingValue
init|=
name|ttFont
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|encodingValue
operator|instanceof
name|MacRomanEncoding
operator|||
name|encodingValue
operator|instanceof
name|WinAnsiEncoding
operator|)
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
name|ERROR_FONTS_ENCODING
argument_list|,
name|fd
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The Encoding is invalid for the NonSymbolic TTF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*              * For symbolic font, no encoding entry is allowed and only one encoding entry is expected into the FontFile              * CMap (Check latter when the FontFile stream will be checked)              */
if|if
condition|(
name|fd
operator|.
name|isSymbolic
argument_list|()
operator|&&
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
operator|!=
literal|null
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
name|ERROR_FONTS_ENCODING
argument_list|,
name|fd
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The Encoding should be missing for the Symbolic TTF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

