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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CmapSubtable
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
name|TTFParser
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
name|io
operator|.
name|IOUtils
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gid2cid
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|hasIdentityCid2Gid
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
name|CmapSubtable
name|cmap
decl_stmt|;
comment|// may be null
specifier|private
name|Matrix
name|fontMatrix
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
name|PDStream
name|ff2Stream
init|=
name|fd
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
name|PDStream
name|ff3Stream
init|=
name|fd
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
comment|// Acrobat looks in FontFile too, even though it is not in the spec, see PDFBOX-2599
if|if
condition|(
name|ff2Stream
operator|==
literal|null
operator|&&
name|ff3Stream
operator|==
literal|null
condition|)
block|{
name|ff2Stream
operator|=
name|fd
operator|.
name|getFontFile
argument_list|()
expr_stmt|;
block|}
name|TrueTypeFont
name|ttfFont
init|=
literal|null
decl_stmt|;
name|boolean
name|fontIsDamaged
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|ff2Stream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// embedded
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|ttfFont
operator|=
name|ttfParser
operator|.
name|parse
argument_list|(
name|ff2Stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
comment|// TTF parser is buggy
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not read embedded TTF for font "
operator|+
name|getBaseFont
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not read embedded TTF for font "
operator|+
name|getBaseFont
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|ff3Stream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// embedded
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
name|ff3Stream
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
comment|// todo: we need more abstraction to support CFF fonts here
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented: OpenType font with CFF table "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
throw|;
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
name|error
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
name|e
parameter_list|)
comment|// TTF parser is buggy
block|{
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
comment|// find font or substitute
name|CIDFontMapping
name|mapping
init|=
name|FontMapper
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
block|}
name|ttf
operator|=
name|ttfFont
expr_stmt|;
name|cmap
operator|=
name|ttf
operator|.
name|getUnicodeCmap
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|cid2gid
operator|=
name|readCIDToGIDMap
argument_list|()
expr_stmt|;
name|gid2cid
operator|=
name|invert
argument_list|(
name|cid2gid
argument_list|)
expr_stmt|;
name|COSBase
name|map
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|)
decl_stmt|;
name|hasIdentityCid2Gid
operator|=
name|map
operator|instanceof
name|COSName
operator|&&
operator|(
operator|(
name|COSName
operator|)
name|map
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Identity"
argument_list|)
expr_stmt|;
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
return|return
name|ttf
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
specifier|private
name|int
index|[]
name|readCIDToGIDMap
parameter_list|()
throws|throws
name|IOException
block|{
name|int
index|[]
name|cid2gid
init|=
literal|null
decl_stmt|;
name|COSBase
name|map
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|map
decl_stmt|;
name|InputStream
name|is
init|=
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|mapAsBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|int
name|numberOfInts
init|=
name|mapAsBytes
operator|.
name|length
operator|/
literal|2
decl_stmt|;
name|cid2gid
operator|=
operator|new
name|int
index|[
name|numberOfInts
index|]
expr_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|numberOfInts
condition|;
name|index
operator|++
control|)
block|{
name|int
name|gid
init|=
operator|(
name|mapAsBytes
index|[
name|offset
index|]
operator|&
literal|0xff
operator|)
operator|<<
literal|8
operator||
name|mapAsBytes
index|[
name|offset
operator|+
literal|1
index|]
operator|&
literal|0xff
decl_stmt|;
name|cid2gid
index|[
name|index
index|]
operator|=
name|gid
expr_stmt|;
name|offset
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|cid2gid
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|invert
parameter_list|(
name|int
index|[]
name|cid2gid
parameter_list|)
block|{
if|if
condition|(
name|cid2gid
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|inverse
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cid2gid
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|inverse
operator|.
name|put
argument_list|(
name|cid2gid
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|inverse
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
name|boolean
name|hasUnicodeMap
init|=
name|parent
operator|.
name|getCMapUCS2
argument_list|()
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|cid2gid
operator|!=
literal|null
condition|)
block|{
comment|// Acrobat allows non-embedded GIDs - todo: can we find a test PDF for this?
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
elseif|else
if|if
condition|(
name|hasIdentityCid2Gid
operator|||
operator|!
name|hasUnicodeMap
condition|)
block|{
comment|// same as above, but for the default Identity CID2GIDMap or when there is no
comment|// ToUnicode CMap to fallback to, see PDFBOX-2599 and PDFBOX-2560
comment|// todo: can we find a test PDF for the Identity case?
return|return
name|codeToCID
argument_list|(
name|code
argument_list|)
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
return|return
literal|0
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
comment|// todo: invert the ToUnicode CMap?
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
literal|"No glyph for U+%04X in font %s"
argument_list|,
name|unicode
argument_list|,
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
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
name|cid
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
name|cid
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
name|int
name|cid
init|=
name|codeToCID
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

