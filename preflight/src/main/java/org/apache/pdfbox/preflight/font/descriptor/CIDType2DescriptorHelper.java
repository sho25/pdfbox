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
name|ERROR_FONTS_CIDSET_MISSING_FOR_SUBSET
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
name|ERROR_FONTS_CID_DAMAGED
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
name|FONT_DICTIONARY_KEY_CIDSET
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
name|PDFontLike
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
name|FontValidator
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
name|CIDType2Container
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

begin_class
specifier|public
class|class
name|CIDType2DescriptorHelper
extends|extends
name|FontDescriptorHelper
argument_list|<
name|CIDType2Container
argument_list|>
block|{
specifier|public
name|CIDType2DescriptorHelper
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFontLike
name|font
parameter_list|,
name|CIDType2Container
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
comment|/**      * If the embedded font is a subset, the CIDSet entry is mandatory and must be a Stream. If the CIDSet entry doesn't      * respects conditions, the FontContainer is updated.      *       * @param pfDescriptor      */
specifier|protected
name|void
name|checkCIDSet
parameter_list|(
name|PDFontDescriptor
name|pfDescriptor
parameter_list|)
block|{
if|if
condition|(
name|FontValidator
operator|.
name|isSubSet
argument_list|(
name|pfDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
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
name|COSBase
name|cidset
init|=
name|pfDescriptor
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|FONT_DICTIONARY_KEY_CIDSET
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidset
operator|==
literal|null
operator|||
operator|!
name|COSUtils
operator|.
name|isStream
argument_list|(
name|cidset
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDSET_MISSING_FOR_SUBSET
argument_list|,
name|pfDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": The CIDSet entry is missing for the Composite Subset"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|ff2
init|=
name|fontDescriptor
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
if|if
condition|(
name|ff2
operator|!=
literal|null
condition|)
block|{
comment|// Stream validation should be done by the StreamValidateHelper. Process font specific check
name|COSStream
name|stream
init|=
name|ff2
operator|.
name|getStream
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
literal|"The FontFile is missing for "
operator|+
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
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
block|}
block|}
name|checkCIDSet
argument_list|(
name|fontDescriptor
argument_list|)
expr_stmt|;
return|return
name|ff2
return|;
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
name|ERROR_FONTS_CID_DAMAGED
argument_list|,
literal|"The FontFile can't be read"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

