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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|CMAPEncodingEntry
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
name|ttf
operator|.
name|TrueTypeFont
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
name|pdfbox
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
name|PDFont
import|;
end_import

begin_class
specifier|public
class|class
name|TrueTypeFontContainer
extends|extends
name|AbstractFontContainer
block|{
comment|/** 	 * Object which contains the TrueType font data extracted by the 	 * TrueTypeParser object 	 */
specifier|private
name|TrueTypeFont
name|fontObject
init|=
literal|null
decl_stmt|;
specifier|private
name|CMAPEncodingEntry
name|cmap
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|numberOfLongHorMetrics
decl_stmt|;
specifier|private
name|int
name|unitsPerEm
decl_stmt|;
specifier|private
name|int
index|[]
name|glyphWidths
decl_stmt|;
specifier|public
name|TrueTypeFontContainer
parameter_list|(
name|PDFont
name|fd
parameter_list|)
block|{
name|super
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
name|void
name|setFontObjectAndInitializeInnerFields
parameter_list|(
name|TrueTypeFont
name|fontObject
parameter_list|)
block|{
name|this
operator|.
name|fontObject
operator|=
name|fontObject
expr_stmt|;
name|this
operator|.
name|unitsPerEm
operator|=
name|this
operator|.
name|fontObject
operator|.
name|getHeader
argument_list|()
operator|.
name|getUnitsPerEm
argument_list|()
expr_stmt|;
name|this
operator|.
name|glyphWidths
operator|=
name|this
operator|.
name|fontObject
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getAdvanceWidth
argument_list|()
expr_stmt|;
comment|// ---- In a Mono space font program, the length of the AdvanceWidth array must be one.
comment|// ---- According to the TrueType font specification, the Last Value of the AdvanceWidth array
comment|// ---- is apply to the subsequent glyphs. So if the GlyphId is greater than the length of the array
comment|// ---- the last entry is used.
name|this
operator|.
name|numberOfLongHorMetrics
operator|=
name|this
operator|.
name|fontObject
operator|.
name|getHorizontalHeader
argument_list|()
operator|.
name|getNumberOfHMetrics
argument_list|()
expr_stmt|;
block|}
name|void
name|setCMap
parameter_list|(
name|CMAPEncodingEntry
name|cmap
parameter_list|)
block|{
name|this
operator|.
name|cmap
operator|=
name|cmap
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
name|widthInFontProgram
decl_stmt|;
name|int
name|innerFontCid
init|=
name|cid
decl_stmt|;
if|if
condition|(
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
operator|==
literal|1
operator|&&
name|cmap
operator|.
name|getPlatformId
argument_list|()
operator|==
literal|3
condition|)
block|{
try|try
block|{
name|Encoding
name|fontEncoding
init|=
name|this
operator|.
name|font
operator|.
name|getFontEncoding
argument_list|()
decl_stmt|;
name|String
name|character
init|=
name|fontEncoding
operator|.
name|getCharacter
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|character
operator|==
literal|null
condition|)
block|{
name|GlyphException
name|e
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
literal|"The character \""
operator|+
name|cid
operator|+
literal|"\" in the font program \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\"is missing from the Charater Encoding."
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
name|char
index|[]
name|characterArray
init|=
name|character
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|characterArray
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|innerFontCid
operator|=
operator|(
name|int
operator|)
name|characterArray
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
comment|// TODO OD-PDFA-87 A faire?
name|innerFontCid
operator|=
operator|(
name|int
operator|)
name|characterArray
index|[
literal|0
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|characterArray
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|cmap
operator|.
name|getGlyphId
argument_list|(
operator|(
name|int
operator|)
name|characterArray
index|[
name|i
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
name|GlyphException
name|e
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
literal|"A glyph for the character \""
operator|+
name|cid
operator|+
literal|"\" in the font program \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\"is missing. There are "
operator|+
name|characterArray
operator|.
name|length
operator|+
literal|" glyph used by this character..."
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|GlyphException
name|e
init|=
operator|new
name|GlyphException
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_FONTS_ENCODING_IO
argument_list|,
name|cid
argument_list|,
literal|"Unable to get the encoding object from the PDFont object during the validation of cid \""
operator|+
name|cid
operator|+
literal|"\" in the font program \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\"."
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|// search glyph
name|int
name|glyphId
init|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|innerFontCid
argument_list|)
decl_stmt|;
if|if
condition|(
name|glyphId
operator|==
literal|0
condition|)
block|{
name|GlyphException
name|e
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
literal|"Glyph for the character \""
operator|+
name|cid
operator|+
literal|"\" in the font program \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\"is missing."
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
comment|// compute glyph width
name|float
name|glypdWidth
init|=
name|glyphWidths
index|[
name|numberOfLongHorMetrics
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|glyphId
operator|<
name|numberOfLongHorMetrics
condition|)
block|{
name|glypdWidth
operator|=
name|glyphWidths
index|[
name|glyphId
index|]
expr_stmt|;
block|}
name|widthInFontProgram
operator|=
operator|(
operator|(
name|glypdWidth
operator|*
literal|1000
operator|)
operator|/
name|unitsPerEm
operator|)
expr_stmt|;
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
block|}
end_class

end_unit

