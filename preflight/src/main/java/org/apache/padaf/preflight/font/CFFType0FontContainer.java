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
name|font
package|;
end_package

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
name|Collection
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
name|fontbox
operator|.
name|cff
operator|.
name|CFFFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|CFFFont
operator|.
name|Mapping
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

begin_class
specifier|public
class|class
name|CFFType0FontContainer
extends|extends
name|AbstractFontContainer
block|{
comment|/** 	 * Object which contains the CFF data extracted by the 	 * CFFParser object 	 */
specifier|private
name|List
argument_list|<
name|CFFFont
argument_list|>
name|fontObject
init|=
literal|null
decl_stmt|;
specifier|public
name|CFFType0FontContainer
parameter_list|(
name|CompositeFontContainer
name|container
parameter_list|)
block|{
name|super
argument_list|(
name|container
operator|.
name|getFont
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|cidKnownByFont
operator|.
name|putAll
argument_list|(
name|container
operator|.
name|cidKnownByFont
argument_list|)
expr_stmt|;
name|this
operator|.
name|isFontProgramEmbedded
operator|=
name|container
operator|.
name|isFontProgramEmbedded
expr_stmt|;
name|this
operator|.
name|errors
operator|.
name|addAll
argument_list|(
name|container
operator|.
name|errors
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|checkCID
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
block|{
if|if
condition|(
name|isAlreadyComputedCid
argument_list|(
name|cid
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// ---- build the font container and keep it in the Handler.
name|boolean
name|cidFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|CFFFont
name|font
range|:
name|this
operator|.
name|fontObject
control|)
block|{
name|Collection
argument_list|<
name|Mapping
argument_list|>
name|cMapping
init|=
name|font
operator|.
name|getMappings
argument_list|()
decl_stmt|;
for|for
control|(
name|Mapping
name|mapping
range|:
name|cMapping
control|)
block|{
comment|// -- REMARK : May be this code must be changed like the Type1FontContainer to Map the SID with the character name?
comment|// -- Not enough PDF with this kind of Font to test the current implementation
if|if
condition|(
name|mapping
operator|.
name|getSID
argument_list|()
operator|==
name|cid
condition|)
block|{
name|cidFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|cidFound
condition|)
block|{
name|GlyphException
name|ge
init|=
operator|new
name|GlyphException
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_FONTS_GLYPH_MISSING
argument_list|,
name|cid
argument_list|,
literal|"CID "
operator|+
name|cid
operator|+
literal|" is missing from the Composite Font format \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\""
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|ge
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ge
throw|;
block|}
specifier|final
name|float
name|widthProvidedByPdfDictionary
init|=
name|this
operator|.
name|font
operator|.
name|getFontWidth
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|float
name|defaultGlyphWidth
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|font
operator|.
name|getFontDescriptor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|defaultGlyphWidth
operator|=
name|this
operator|.
name|font
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getMissingWidth
argument_list|()
expr_stmt|;
block|}
name|float
name|widthInFontProgram
init|=
literal|0
decl_stmt|;
try|try
block|{
comment|// ---- Search the CID in all CFFFont in the FontProgram
for|for
control|(
name|CFFFont
name|cff
range|:
name|fontObject
control|)
block|{
name|widthInFontProgram
operator|=
name|cff
operator|.
name|getWidth
argument_list|(
name|cid
argument_list|)
expr_stmt|;
if|if
condition|(
name|widthInFontProgram
operator|!=
name|defaultGlyphWidth
condition|)
block|{
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|GlyphException
name|ge
init|=
operator|new
name|GlyphException
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_FONTS_DAMAGED
argument_list|,
name|cid
argument_list|,
literal|"Unable to get width of the CID/SID : "
operator|+
name|cid
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|ge
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ge
throw|;
block|}
name|checkWidthsConsistency
argument_list|(
name|cid
argument_list|,
name|widthProvidedByPdfDictionary
argument_list|,
name|widthInFontProgram
argument_list|)
expr_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|setFontObject
parameter_list|(
name|List
argument_list|<
name|CFFFont
argument_list|>
name|fontObject
parameter_list|)
block|{
name|this
operator|.
name|fontObject
operator|=
name|fontObject
expr_stmt|;
block|}
block|}
end_class

end_unit

