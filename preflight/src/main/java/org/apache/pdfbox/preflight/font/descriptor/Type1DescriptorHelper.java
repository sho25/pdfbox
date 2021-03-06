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
operator|.
name|descriptor
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
name|ERROR_FONTS_CHARSET_MISSING_FOR_SUBSET
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
name|ERROR_FONTS_FONT_FILEX_INVALID
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
name|ERROR_FONTS_TYPE1_DAMAGED
import|;
end_import

begin_class
specifier|public
class|class
name|Type1DescriptorHelper
extends|extends
name|FontDescriptorHelper
argument_list|<
name|Type1Container
argument_list|>
block|{
specifier|public
name|Type1DescriptorHelper
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDSimpleFont
name|font
parameter_list|,
name|Type1Container
name|fontContainer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
argument_list|,
name|fontContainer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|checkMandatoryFields
parameter_list|(
name|COSDictionary
name|fDescriptor
parameter_list|)
block|{
name|boolean
name|result
init|=
name|super
operator|.
name|checkMandatoryFields
argument_list|(
name|fDescriptor
argument_list|)
decl_stmt|;
comment|/*          * if this font is a subset, the CharSet entry must be present in the FontDescriptor          */
if|if
condition|(
name|isSubSet
argument_list|(
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|charsetStr
init|=
name|fontDescriptor
operator|.
name|getCharSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|charsetStr
operator|==
literal|null
operator|||
name|charsetStr
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CHARSET_MISSING_FOR_SUBSET
argument_list|,
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The Charset entry is missing for the Type1 Subset"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDStream
name|extractFontFile
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|PDStream
name|ff1
init|=
name|fontDescriptor
operator|.
name|getFontFile
argument_list|()
decl_stmt|;
name|PDStream
name|ff3
init|=
name|fontDescriptor
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
if|if
condition|(
name|ff1
operator|!=
literal|null
condition|)
block|{
name|COSStream
name|stream
init|=
name|ff1
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The FontFile is missing"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|fContainer
operator|.
name|notEmbedded
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
name|boolean
name|hasLength1
init|=
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|)
operator|>
literal|0
decl_stmt|;
name|boolean
name|hasLength2
init|=
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH2
argument_list|)
operator|>
literal|0
decl_stmt|;
name|boolean
name|hasLength3
init|=
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH3
argument_list|)
operator|>=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|hasLength1
operator|&&
name|hasLength2
operator|&&
name|hasLength3
operator|)
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The FontFile is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|ff1
return|;
block|}
else|else
block|{
return|return
name|ff3
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processFontFile
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|,
name|PDStream
name|fontFile
parameter_list|)
block|{
if|if
condition|(
name|font
operator|.
name|isDamaged
argument_list|()
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_TYPE1_DAMAGED
argument_list|,
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The FontFile can't be read"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

