begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|GeneralPath
import|;
end_import

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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|Type2CharString
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
name|cmap
operator|.
name|CMap
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
name|CmapLookup
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
name|GlyphData
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
name|OTFParser
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
name|OpenTypeFont
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
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
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
name|pdmodel
operator|.
name|common
operator|.
name|PDRectangle
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * Type 2 CIDFont (TrueType).  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDCIDFontType2
extends|extends
name|PDCIDFont
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDCIDFontType2
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|TrueTypeFont
name|ttf
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|cid2gid
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isEmbedded
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isDamaged
decl_stmt|;
specifier|private
specifier|final
name|CmapLookup
name|cmap
decl_stmt|;
comment|// may be null
specifier|private
name|Matrix
name|fontMatrix
decl_stmt|;
specifier|private
name|BoundingBox
name|fontBBox
decl_stmt|;
comment|/**      * Constructor.      *       * @param fontDictionary The font dictionary according to the PDF specification.      * @param parent The parent font.      * @throws IOException      */
specifier|public
name|PDCIDFontType2
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|,
name|PDType0Font
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|fontDictionary
argument_list|,
name|parent
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param fontDictionary The font dictionary according to the PDF specification.      * @param parent The parent font.      * @param trueTypeFont The true type font used to create the parent font      * @throws IOException      */
specifier|public
name|PDCIDFontType2
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|,
name|PDType0Font
name|parent
parameter_list|,
name|TrueTypeFont
name|trueTypeFont
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|trueTypeFont
operator|!=
literal|null
condition|)
block|{
name|ttf
operator|=
name|trueTypeFont
expr_stmt|;
name|isEmbedded
operator|=
literal|true
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|fontIsDamaged
init|=
literal|false
decl_stmt|;
name|TrueTypeFont
name|ttfFont
init|=
literal|null
decl_stmt|;
name|PDStream
name|stream
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|stream
operator|=
name|fd
operator|.
name|getFontFile2
argument_list|()
expr_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
name|stream
operator|=
name|fd
operator|.
name|getFontFile3
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
comment|// Acrobat looks in FontFile too, even though it is not in the spec, see PDFBOX-2599
name|stream
operator|=
name|fd
operator|.
name|getFontFile
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// embedded OTF or TTF
name|OTFParser
name|otfParser
init|=
operator|new
name|OTFParser
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|OpenTypeFont
name|otf
init|=
name|otfParser
operator|.
name|parse
argument_list|(
name|stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|ttfFont
operator|=
name|otf
expr_stmt|;
if|if
condition|(
name|otf
operator|.
name|isPostScript
argument_list|()
condition|)
block|{
comment|// PDFBOX-3344 contains PostScript outlines instead of TrueType
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Found CFF/OTF but expected embedded TTF font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|otf
operator|.
name|hasLayoutTables
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"OpenType Layout tables used in font "
operator|+
name|getBaseFont
argument_list|()
operator|+
literal|" are not implemented in PDFBox and will be ignored"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NullPointerException
decl||
name|IOException
name|e
parameter_list|)
block|{
comment|// NPE due to TTF parser being buggy
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not read embedded OTF for font "
operator|+
name|getBaseFont
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|isEmbedded
operator|=
name|ttfFont
operator|!=
literal|null
expr_stmt|;
name|isDamaged
operator|=
name|fontIsDamaged
expr_stmt|;
if|if
condition|(
name|ttfFont
operator|==
literal|null
condition|)
block|{
name|ttfFont
operator|=
name|findFontOrSubstitute
argument_list|()
expr_stmt|;
block|}
name|ttf
operator|=
name|ttfFont
expr_stmt|;
block|}
name|cmap
operator|=
name|ttf
operator|.
name|getUnicodeCmapLookup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|cid2gid
operator|=
name|readCIDToGIDMap
argument_list|()
expr_stmt|;
block|}
specifier|private
name|TrueTypeFont
name|findFontOrSubstitute
parameter_list|()
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|ttfFont
decl_stmt|;
name|CIDFontMapping
name|mapping
init|=
name|FontMappers
operator|.
name|instance
argument_list|()
operator|.
name|getCIDFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|,
name|getFontDescriptor
argument_list|()
argument_list|,
name|getCIDSystemInfo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapping
operator|.
name|isCIDFont
argument_list|()
condition|)
block|{
name|ttfFont
operator|=
name|mapping
operator|.
name|getFont
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ttfFont
operator|=
operator|(
name|TrueTypeFont
operator|)
name|mapping
operator|.
name|getTrueTypeFont
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mapping
operator|.
name|isFallback
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using fallback font "
operator|+
name|ttfFont
operator|.
name|getName
argument_list|()
operator|+
literal|" for CID-keyed TrueType font "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ttfFont
return|;
block|}
annotation|@
name|Override
specifier|public
name|Matrix
name|getFontMatrix
parameter_list|()
block|{
if|if
condition|(
name|fontMatrix
operator|==
literal|null
condition|)
block|{
comment|// 1000 upem, this is not strictly true
name|fontMatrix
operator|=
operator|new
name|Matrix
argument_list|(
literal|0.001f
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0.001f
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|fontMatrix
return|;
block|}
annotation|@
name|Override
specifier|public
name|BoundingBox
name|getBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|fontBBox
operator|==
literal|null
condition|)
block|{
name|fontBBox
operator|=
name|generateBoundingBox
argument_list|()
expr_stmt|;
block|}
return|return
name|fontBBox
return|;
block|}
specifier|private
name|BoundingBox
name|generateBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getFontDescriptor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|PDRectangle
name|bbox
init|=
name|getFontDescriptor
argument_list|()
operator|.
name|getFontBoundingBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|bbox
operator|!=
literal|null
operator|&&
operator|(
name|Float
operator|.
name|compare
argument_list|(
name|bbox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|bbox
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|bbox
operator|.
name|getUpperRightY
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|)
condition|)
block|{
return|return
operator|new
name|BoundingBox
argument_list|(
name|bbox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|bbox
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
name|bbox
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|ttf
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|codeToCID
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|CMap
name|cMap
init|=
name|parent
operator|.
name|getCMap
argument_list|()
decl_stmt|;
comment|// Acrobat allows bad PDFs to use Unicode CMaps here instead of CID CMaps, see PDFBOX-1283
if|if
condition|(
operator|!
name|cMap
operator|.
name|hasCIDMappings
argument_list|()
operator|&&
name|cMap
operator|.
name|hasUnicodeMappings
argument_list|()
condition|)
block|{
return|return
name|cMap
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
return|;
comment|// actually: code -> CID
block|}
return|return
name|cMap
operator|.
name|toCID
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * Returns the GID for the given character code.      *      * @param code character code      * @return GID      * @throws IOException      */
annotation|@
name|Override
specifier|public
name|int
name|codeToGID
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isEmbedded
condition|)
block|{
comment|// The conforming reader shall select glyphs by translating characters from the
comment|// encoding specified by the predefined CMap to one of the encodings in the TrueType
comment|// font's 'cmap' table. The means by which this is accomplished are implementation-
comment|// dependent.
comment|// omit the CID2GID mapping if the embedded font is replaced by an external font
if|if
condition|(
name|cid2gid
operator|!=
literal|null
operator|&&
operator|!
name|isDamaged
condition|)
block|{
comment|// Acrobat allows non-embedded GIDs - todo: can we find a test PDF for this?
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using non-embedded GIDs in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
return|return
name|cid2gid
index|[
name|cid
index|]
return|;
block|}
else|else
block|{
comment|// fallback to the ToUnicode CMap, test with PDFBOX-1422 and PDFBOX-2560
name|String
name|unicode
init|=
name|parent
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to find a character mapping for "
operator|+
name|code
operator|+
literal|" in "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Acrobat is willing to use the CID as a GID, even when the font isn't embedded
comment|// see PDFBOX-2599
return|return
name|codeToCID
argument_list|(
name|code
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|unicode
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Trying to map multi-byte character using 'cmap', result will be poor"
argument_list|)
expr_stmt|;
block|}
comment|// a non-embedded font always has a cmap (otherwise FontMapper won't load it)
return|return
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|unicode
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// If the TrueType font program is embedded, the Type 2 CIDFont dictionary shall contain
comment|// a CIDToGIDMap entry that maps CIDs to the glyph indices for the appropriate glyph
comment|// descriptions in that font program.
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|cid2gid
operator|!=
literal|null
condition|)
block|{
comment|// use CIDToGIDMap
if|if
condition|(
name|cid
operator|<
name|cid2gid
operator|.
name|length
condition|)
block|{
return|return
name|cid2gid
index|[
name|cid
index|]
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
else|else
block|{
comment|// "Identity" is the default CIDToGIDMap
if|if
condition|(
name|cid
operator|<
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
condition|)
block|{
return|return
name|cid
return|;
block|}
else|else
block|{
comment|// out of range CIDs map to GID 0
return|return
literal|0
return|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|float
name|getHeight
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
comment|// todo: really we want the BBox, (for text extraction:)
return|return
operator|(
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
operator|.
name|getAscender
argument_list|()
operator|+
operator|-
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
operator|.
name|getDescender
argument_list|()
operator|)
operator|/
name|ttf
operator|.
name|getUnitsPerEm
argument_list|()
return|;
comment|// todo: shouldn't this be the yMax/yMin?
block|}
annotation|@
name|Override
specifier|public
name|float
name|getWidthFromFont
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|gid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|int
name|width
init|=
name|ttf
operator|.
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
decl_stmt|;
name|int
name|unitsPerEM
init|=
name|ttf
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
if|if
condition|(
name|unitsPerEM
operator|!=
literal|1000
condition|)
block|{
name|width
operator|*=
literal|1000f
operator|/
name|unitsPerEM
expr_stmt|;
block|}
return|return
name|width
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|encode
parameter_list|(
name|int
name|unicode
parameter_list|)
block|{
name|int
name|cid
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|isEmbedded
condition|)
block|{
comment|// embedded fonts always use CIDToGIDMap, with Identity as the default
if|if
condition|(
name|parent
operator|.
name|getCMap
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Identity-"
argument_list|)
condition|)
block|{
if|if
condition|(
name|cmap
operator|!=
literal|null
condition|)
block|{
name|cid
operator|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|unicode
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// if the CMap is predefined then there will be a UCS-2 CMap
if|if
condition|(
name|parent
operator|.
name|getCMapUCS2
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cid
operator|=
name|parent
operator|.
name|getCMapUCS2
argument_list|()
operator|.
name|toCID
argument_list|(
name|unicode
argument_list|)
expr_stmt|;
block|}
block|}
comment|// otherwise we require an explicit ToUnicode CMap
if|if
condition|(
name|cid
operator|==
operator|-
literal|1
condition|)
block|{
comment|//TODO: invert the ToUnicode CMap?
comment|// see also PDFBOX-4233
name|cid
operator|=
literal|0
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// a non-embedded font always has a cmap (otherwise it we wouldn't load it)
name|cid
operator|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|unicode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cid
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"No glyph for U+%04X (%c) in font %s"
argument_list|,
name|unicode
argument_list|,
operator|(
name|char
operator|)
name|unicode
argument_list|,
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|encodeGlyphId
argument_list|(
name|cid
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|encodeGlyphId
parameter_list|(
name|int
name|glyphId
parameter_list|)
block|{
comment|// CID is always 2-bytes (16-bit) for TrueType
return|return
operator|new
name|byte
index|[]
block|{
call|(
name|byte
call|)
argument_list|(
name|glyphId
operator|>>
literal|8
operator|&
literal|0xff
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|glyphId
operator|&
literal|0xff
argument_list|)
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmbedded
parameter_list|()
block|{
return|return
name|isEmbedded
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDamaged
parameter_list|()
block|{
return|return
name|isDamaged
return|;
block|}
comment|/**      * Returns the embedded or substituted TrueType font. May be an OpenType font if the font is      * not embedded.      */
specifier|public
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|()
block|{
return|return
name|ttf
return|;
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ttf
operator|instanceof
name|OpenTypeFont
operator|&&
operator|(
operator|(
name|OpenTypeFont
operator|)
name|ttf
operator|)
operator|.
name|isPostScript
argument_list|()
condition|)
block|{
comment|// we're not supposed to have CFF fonts inside PDCIDFontType2, but if we do,
comment|// then we treat their CIDs as GIDs, see PDFBOX-3344
name|int
name|cid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|Type2CharString
name|charstring
init|=
operator|(
operator|(
name|OpenTypeFont
operator|)
name|ttf
operator|)
operator|.
name|getCFF
argument_list|()
operator|.
name|getFont
argument_list|()
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
decl_stmt|;
return|return
name|charstring
operator|.
name|getPath
argument_list|()
return|;
block|}
else|else
block|{
name|int
name|gid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|GlyphData
name|glyph
init|=
name|ttf
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|glyph
operator|!=
literal|null
condition|)
block|{
return|return
name|glyph
operator|.
name|getPath
argument_list|()
return|;
block|}
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getNormalizedPath
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|hasScaling
init|=
name|ttf
operator|.
name|getUnitsPerEm
argument_list|()
operator|!=
literal|1000
decl_stmt|;
name|float
name|scale
init|=
literal|1000f
operator|/
name|ttf
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|int
name|gid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|GeneralPath
name|path
init|=
name|getPath
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// Acrobat only draws GID 0 for embedded CIDFonts, see PDFBOX-2372
if|if
condition|(
name|gid
operator|==
literal|0
operator|&&
operator|!
name|isEmbedded
argument_list|()
condition|)
block|{
name|path
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
comment|// empty glyph (e.g. space, newline)
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
else|else
block|{
if|if
condition|(
name|hasScaling
condition|)
block|{
name|path
operator|.
name|transform
argument_list|(
name|AffineTransform
operator|.
name|getScaleInstance
argument_list|(
name|scale
argument_list|,
name|scale
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|codeToGID
argument_list|(
name|code
argument_list|)
operator|!=
literal|0
return|;
block|}
block|}
end_class

end_unit

