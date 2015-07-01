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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|FontBoxFont
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
name|CmapTable
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
name|PostScriptTable
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
name|PDDocument
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
name|encoding
operator|.
name|BuiltInEncoding
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
name|GlyphList
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
name|MacOSRomanEncoding
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
name|Type1Encoding
import|;
end_import

begin_comment
comment|/**  * TrueType font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDTrueTypeFont
extends|extends
name|PDSimpleFont
implements|implements
name|PDVectorFont
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
name|PDTrueTypeFont
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|START_RANGE_F000
init|=
literal|0xF000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|START_RANGE_F100
init|=
literal|0xF100
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|START_RANGE_F200
init|=
literal|0xF200
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|INVERTED_MACOS_ROMAN
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
name|MacOSRomanEncoding
operator|.
name|INSTANCE
operator|.
name|getCodeToNameMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|codeToName
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|INVERTED_MACOS_ROMAN
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|INVERTED_MACOS_ROMAN
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Loads a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param file a ttf file.      * @return a PDTrueTypeFont instance.      * @throws IOException If there is an error loading the data.      *      * @deprecated Use {@link PDType0Font#load(PDDocument, File)} instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
name|PDTrueTypeFont
name|loadTTF
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDTrueTypeFont
argument_list|(
name|doc
argument_list|,
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Loads a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param input a ttf file stream      * @return a PDTrueTypeFont instance.      * @throws IOException If there is an error loading the data.      *      * @deprecated Use {@link PDType0Font#load(PDDocument, InputStream)} instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
name|PDTrueTypeFont
name|loadTTF
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDTrueTypeFont
argument_list|(
name|doc
argument_list|,
name|input
argument_list|)
return|;
block|}
specifier|private
name|CmapSubtable
name|cmapWinUnicode
init|=
literal|null
decl_stmt|;
specifier|private
name|CmapSubtable
name|cmapWinSymbol
init|=
literal|null
decl_stmt|;
specifier|private
name|CmapSubtable
name|cmapMacRoman
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|cmapInitialized
init|=
literal|false
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCode
decl_stmt|;
comment|// for embedding
specifier|private
specifier|final
name|TrueTypeFont
name|ttf
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
comment|/**      * Creates a new TrueType font from a Font dictionary.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDTrueTypeFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
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
name|getFontDescriptor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|PDFontDescriptor
name|fd
init|=
name|super
operator|.
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
comment|// substitute
if|if
condition|(
name|ttfFont
operator|==
literal|null
condition|)
block|{
name|FontMapping
argument_list|<
name|TrueTypeFont
argument_list|>
name|mapping
init|=
name|FontMapper
operator|.
name|getTrueTypeFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|,
name|getFontDescriptor
argument_list|()
argument_list|)
decl_stmt|;
name|ttfFont
operator|=
name|mapping
operator|.
name|getFont
argument_list|()
expr_stmt|;
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
literal|"Using fallback font '"
operator|+
name|ttfFont
operator|+
literal|"' for '"
operator|+
name|getBaseFont
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
name|ttf
operator|=
name|ttfFont
expr_stmt|;
name|readEncoding
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the PostScript name of the font.      */
specifier|public
specifier|final
name|String
name|getBaseFont
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Encoding
name|readEncodingFromFont
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getStandard14AFM
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// read from AFM
return|return
operator|new
name|Type1Encoding
argument_list|(
name|getStandard14AFM
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// synthesize an encoding, so that getEncoding() is always usable
name|PostScriptTable
name|post
init|=
name|ttf
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|code
init|=
literal|0
init|;
name|code
operator|<=
literal|256
condition|;
name|code
operator|++
control|)
block|{
name|int
name|gid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|>
literal|0
condition|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|post
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|post
operator|.
name|getName
argument_list|(
name|gid
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|// GID pseudo-name
name|name
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|gid
argument_list|)
expr_stmt|;
block|}
name|codeToName
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|BuiltInEncoding
argument_list|(
name|codeToName
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates a new TrueType font for embedding.      */
specifier|private
name|PDTrueTypeFont
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|InputStream
name|ttfStream
parameter_list|)
throws|throws
name|IOException
block|{
name|PDTrueTypeFontEmbedder
name|embedder
init|=
operator|new
name|PDTrueTypeFontEmbedder
argument_list|(
name|document
argument_list|,
name|dict
argument_list|,
name|ttfStream
argument_list|)
decl_stmt|;
name|encoding
operator|=
name|embedder
operator|.
name|getFontEncoding
argument_list|()
expr_stmt|;
name|ttf
operator|=
name|embedder
operator|.
name|getTrueTypeFont
argument_list|()
expr_stmt|;
name|setFontDescriptor
argument_list|(
name|embedder
operator|.
name|getFontDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|isEmbedded
operator|=
literal|true
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
name|glyphList
operator|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|readCode
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|in
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getBaseFont
argument_list|()
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
comment|/**      * Returns the embedded or substituted TrueType font.      */
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
name|float
name|width
init|=
name|ttf
operator|.
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
decl_stmt|;
name|float
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
name|float
name|getHeight
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
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|protected
name|byte
index|[]
name|encode
parameter_list|(
name|int
name|unicode
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|getEncoding
argument_list|()
operator|.
name|contains
argument_list|(
name|getGlyphList
argument_list|()
operator|.
name|codePointToName
argument_list|(
name|unicode
argument_list|)
argument_list|)
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
literal|"U+%04X is not available in this font's Encoding"
argument_list|,
name|unicode
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|getGlyphList
argument_list|()
operator|.
name|codePointToName
argument_list|(
name|unicode
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|inverted
init|=
name|getInvertedEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ttf
operator|.
name|hasGlyph
argument_list|(
name|name
argument_list|)
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
name|int
name|code
init|=
name|inverted
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|code
block|}
return|;
block|}
else|else
block|{
comment|// use TTF font's built-in encoding
name|String
name|name
init|=
name|getGlyphList
argument_list|()
operator|.
name|codePointToName
argument_list|(
name|unicode
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ttf
operator|.
name|hasGlyph
argument_list|(
name|name
argument_list|)
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
name|int
name|gid
init|=
name|ttf
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Integer
name|code
init|=
name|getGIDToCode
argument_list|()
operator|.
name|get
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|null
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
literal|"U+%04X is not available in this font's Encoding"
argument_list|,
name|unicode
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|new
name|byte
index|[]
block|{
call|(
name|byte
call|)
argument_list|(
name|int
argument_list|)
name|code
block|}
return|;
block|}
block|}
comment|/**      * Inverts the font's code -> GID mapping. Any duplicate (GID -> code) mappings will be lost.      */
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|getGIDToCode
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|gidToCode
operator|!=
literal|null
condition|)
block|{
return|return
name|gidToCode
return|;
block|}
name|gidToCode
operator|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|code
init|=
literal|0
init|;
name|code
operator|<=
literal|255
condition|;
name|code
operator|++
control|)
block|{
name|int
name|gid
init|=
name|codeToGID
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|gidToCode
operator|.
name|containsKey
argument_list|(
name|gid
argument_list|)
condition|)
block|{
name|gidToCode
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|gidToCode
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
name|GeneralPath
name|getPath
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
comment|// some glyphs have no outlines (e.g. space, table, newline)
if|if
condition|(
name|glyph
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|glyph
operator|.
name|getPath
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
comment|// handle glyph names and uniXXXX names
name|int
name|gid
init|=
name|ttf
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
try|try
block|{
comment|// handle GID pseudo-names
name|gid
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|gid
operator|>
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
condition|)
block|{
name|gid
operator|=
literal|0
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|gid
operator|=
literal|0
expr_stmt|;
block|}
block|}
comment|// I'm assuming .notdef paths are not drawn, as it PDFBOX-2421
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
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
else|else
block|{
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
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|gid
init|=
name|ttf
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|gid
operator|!=
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|FontBoxFont
name|getFontBoxFont
parameter_list|()
block|{
return|return
name|ttf
return|;
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
comment|/**      * Returns the GID for the given character code.      *      * @param code character code      * @return GID (glyph index)      */
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
name|extractCmapTable
argument_list|()
expr_stmt|;
name|int
name|gid
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|isSymbolic
argument_list|()
condition|)
comment|// non-symbolic
block|{
name|String
name|name
init|=
name|encoding
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
comment|// (3, 1) - (Windows, Unicode)
if|if
condition|(
name|cmapWinUnicode
operator|!=
literal|null
condition|)
block|{
name|String
name|unicode
init|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
operator|.
name|toUnicode
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
name|int
name|uni
init|=
name|unicode
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|gid
operator|=
name|cmapWinUnicode
operator|.
name|getGlyphId
argument_list|(
name|uni
argument_list|)
expr_stmt|;
block|}
block|}
comment|// (1, 0) - (Macintosh, Roman)
if|if
condition|(
name|gid
operator|==
literal|0
operator|&&
name|cmapMacRoman
operator|!=
literal|null
condition|)
block|{
name|Integer
name|macCode
init|=
name|INVERTED_MACOS_ROMAN
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|macCode
operator|!=
literal|null
condition|)
block|{
name|gid
operator|=
name|cmapMacRoman
operator|.
name|getGlyphId
argument_list|(
name|macCode
argument_list|)
expr_stmt|;
block|}
block|}
comment|// 'post' table
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
name|gid
operator|=
name|ttf
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
comment|// symbolic
block|{
comment|// (3, 0) - (Windows, Symbol)
if|if
condition|(
name|cmapWinSymbol
operator|!=
literal|null
condition|)
block|{
name|gid
operator|=
name|cmapWinSymbol
operator|.
name|getGlyphId
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
name|code
operator|>=
literal|0
operator|&&
name|code
operator|<=
literal|0xFF
condition|)
block|{
comment|// the CMap may use one of the following code ranges,
comment|// so that we have to add the high byte to get the
comment|// mapped value
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
comment|// F000 - F0FF
name|gid
operator|=
name|cmapWinSymbol
operator|.
name|getGlyphId
argument_list|(
name|code
operator|+
name|START_RANGE_F000
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
comment|// F100 - F1FF
name|gid
operator|=
name|cmapWinSymbol
operator|.
name|getGlyphId
argument_list|(
name|code
operator|+
name|START_RANGE_F100
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
comment|// F200 - F2FF
name|gid
operator|=
name|cmapWinSymbol
operator|.
name|getGlyphId
argument_list|(
name|code
operator|+
name|START_RANGE_F200
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// (1, 0) - (Mac, Roman)
if|if
condition|(
name|gid
operator|==
literal|0
operator|&&
name|cmapMacRoman
operator|!=
literal|null
condition|)
block|{
name|gid
operator|=
name|cmapMacRoman
operator|.
name|getGlyphId
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|gid
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't map code "
operator|+
name|code
operator|+
literal|" in font "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|gid
return|;
block|}
comment|/**      * extract all useful "cmap" subtables.      */
specifier|private
name|void
name|extractCmapTable
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|cmapInitialized
condition|)
block|{
return|return;
block|}
name|CmapTable
name|cmapTable
init|=
name|ttf
operator|.
name|getCmap
argument_list|()
decl_stmt|;
if|if
condition|(
name|cmapTable
operator|!=
literal|null
condition|)
block|{
comment|// get all relevant "cmap" subtables
name|CmapSubtable
index|[]
name|cmaps
init|=
name|cmapTable
operator|.
name|getCmaps
argument_list|()
decl_stmt|;
for|for
control|(
name|CmapSubtable
name|cmap
range|:
name|cmaps
control|)
block|{
if|if
condition|(
name|CmapTable
operator|.
name|PLATFORM_WINDOWS
operator|==
name|cmap
operator|.
name|getPlatformId
argument_list|()
condition|)
block|{
if|if
condition|(
name|CmapTable
operator|.
name|ENCODING_WIN_UNICODE_BMP
operator|==
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
condition|)
block|{
name|cmapWinUnicode
operator|=
name|cmap
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CmapTable
operator|.
name|ENCODING_WIN_SYMBOL
operator|==
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
condition|)
block|{
name|cmapWinSymbol
operator|=
name|cmap
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|CmapTable
operator|.
name|PLATFORM_MACINTOSH
operator|==
name|cmap
operator|.
name|getPlatformId
argument_list|()
operator|&&
name|CmapTable
operator|.
name|ENCODING_MAC_ROMAN
operator|==
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
condition|)
block|{
name|cmapMacRoman
operator|=
name|cmap
expr_stmt|;
block|}
block|}
block|}
name|cmapInitialized
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

