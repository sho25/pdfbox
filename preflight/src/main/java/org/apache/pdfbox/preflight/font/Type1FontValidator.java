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
name|font
operator|.
name|PDSimpleFont
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
name|Type1Container
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
name|Type1DescriptorHelper
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
name|FONT_DICTIONARY_VALUE_ENCODING_MAC
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
name|FONT_DICTIONARY_VALUE_ENCODING_MAC_EXP
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
name|FONT_DICTIONARY_VALUE_ENCODING_PDFDOC
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
name|FONT_DICTIONARY_VALUE_ENCODING_STD
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
name|FONT_DICTIONARY_VALUE_ENCODING_WIN
import|;
end_import

begin_class
specifier|public
class|class
name|Type1FontValidator
extends|extends
name|SimpleFontValidator
argument_list|<
name|Type1Container
argument_list|>
block|{
specifier|public
name|Type1FontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDSimpleFont
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
name|Type1Container
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
name|Type1DescriptorHelper
argument_list|(
name|context
argument_list|,
operator|(
name|PDSimpleFont
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
name|COSBase
name|encoding
init|=
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|COSDocument
name|cosDocument
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|COSUtils
operator|.
name|isString
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|String
name|encodingName
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|encodingName
operator|.
name|equals
argument_list|(
name|FONT_DICTIONARY_VALUE_ENCODING_MAC
argument_list|)
operator|||
name|encodingName
operator|.
name|equals
argument_list|(
name|FONT_DICTIONARY_VALUE_ENCODING_MAC_EXP
argument_list|)
operator|||
name|encodingName
operator|.
name|equals
argument_list|(
name|FONT_DICTIONARY_VALUE_ENCODING_WIN
argument_list|)
operator|||
name|encodingName
operator|.
name|equals
argument_list|(
name|FONT_DICTIONARY_VALUE_ENCODING_PDFDOC
argument_list|)
operator|||
name|encodingName
operator|.
name|equals
argument_list|(
name|FONT_DICTIONARY_VALUE_ENCODING_STD
argument_list|)
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

