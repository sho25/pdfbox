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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|CMAPTable
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
name|GlyphTable
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
name|HeaderTable
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
name|HorizontalHeaderTable
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
name|HorizontalMetricsTable
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
name|NameRecord
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
name|NamingTable
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
name|OS2WindowsMetricsTable
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
name|SystemFontManager
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
name|COSArrayList
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

begin_comment
comment|/**  * TrueType font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDTrueTypeFont
extends|extends
name|PDFont
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
comment|/**      * Loads a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param file a ttf file.      * @return a PDTrueTypeFont instance.      * @throws IOException If there is an error loading the data.      */
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
specifier|private
name|CMAPEncodingEntry
name|cmapWinUnicode
init|=
literal|null
decl_stmt|;
specifier|private
name|CMAPEncodingEntry
name|cmapWinSymbol
init|=
literal|null
decl_stmt|;
specifier|private
name|CMAPEncodingEntry
name|cmapMacintoshSymbol
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
name|TrueTypeFont
name|ttf
init|=
literal|null
decl_stmt|;
specifier|private
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
name|advanceWidths
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
argument_list|()
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
name|getTTFFont
argument_list|()
expr_stmt|;
comment|// load the font file
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
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|TRUE_TYPE
argument_list|)
expr_stmt|;
name|PDStream
name|stream
init|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|,
name|ttfStream
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|,
name|stream
operator|.
name|getByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// todo: wrong?
name|stream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
comment|// only support winansi encoding right now, should really
comment|// just use Identity-H with unicode mapping
name|Encoding
name|encoding
init|=
operator|new
name|WinAnsiEncoding
argument_list|()
decl_stmt|;
comment|// fixme: read encoding from TTF
name|this
operator|.
name|fontEncoding
operator|=
name|encoding
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|,
name|encoding
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// as the stream was close within the PDStream constructor, we have to recreate it
name|InputStream
name|stream2
init|=
literal|null
decl_stmt|;
name|PDFontDescriptorDictionary
name|fd
decl_stmt|;
try|try
block|{
name|stream2
operator|=
name|stream
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
name|ttf
operator|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parseTTF
argument_list|(
name|stream2
argument_list|)
expr_stmt|;
name|fd
operator|=
name|makeFontDescriptor
argument_list|(
name|ttf
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|stream2
argument_list|)
expr_stmt|;
block|}
name|fd
operator|.
name|setFontFile2
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|,
name|fd
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
if|if
condition|(
name|fontDescriptor
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|fd
init|=
operator|(
name|COSDictionary
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|)
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|fontDescriptor
operator|=
operator|new
name|PDFontDescriptorDictionary
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fontDescriptor
operator|=
name|makeFontDescriptor
argument_list|(
name|ttf
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fontDescriptor
return|;
block|}
comment|// creates a new font descriptor dictionary for the given TTF
specifier|private
name|PDFontDescriptorDictionary
name|makeFontDescriptor
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
block|{
name|PDFontDescriptorDictionary
name|fd
init|=
operator|new
name|PDFontDescriptorDictionary
argument_list|()
decl_stmt|;
name|NamingTable
name|naming
init|=
name|ttf
operator|.
name|getNaming
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|NameRecord
argument_list|>
name|records
init|=
name|naming
operator|.
name|getNameRecords
argument_list|()
decl_stmt|;
for|for
control|(
name|NameRecord
name|nr
range|:
name|records
control|)
block|{
if|if
condition|(
name|nr
operator|.
name|getNameId
argument_list|()
operator|==
name|NameRecord
operator|.
name|NAME_POSTSCRIPT_NAME
condition|)
block|{
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|nr
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontName
argument_list|(
name|nr
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nr
operator|.
name|getNameId
argument_list|()
operator|==
name|NameRecord
operator|.
name|NAME_FONT_FAMILY_NAME
condition|)
block|{
name|fd
operator|.
name|setFontFamily
argument_list|(
name|nr
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|OS2WindowsMetricsTable
name|os2
init|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
decl_stmt|;
name|boolean
name|isSymbolic
init|=
literal|false
decl_stmt|;
switch|switch
condition|(
name|os2
operator|.
name|getFamilyClass
argument_list|()
condition|)
block|{
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_SYMBOLIC
case|:
name|isSymbolic
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_SCRIPTS
case|:
name|fd
operator|.
name|setScript
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_CLAREDON_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_FREEFORM_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_MODERN_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_OLDSTYLE_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_SLAB_SERIFS
case|:
name|fd
operator|.
name|setSerif
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
switch|switch
condition|(
name|os2
operator|.
name|getWidthClass
argument_list|()
condition|)
block|{
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_ULTRA_CONDENSED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"UltraCondensed"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_EXTRA_CONDENSED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"ExtraCondensed"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_CONDENSED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"Condensed"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_SEMI_CONDENSED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"SemiCondensed"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_MEDIUM
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"Normal"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_SEMI_EXPANDED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"SemiExpanded"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_EXPANDED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"Expanded"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_EXTRA_EXPANDED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"ExtraExpanded"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|WIDTH_CLASS_ULTRA_EXPANDED
case|:
name|fd
operator|.
name|setFontStretch
argument_list|(
literal|"UltraExpanded"
argument_list|)
expr_stmt|;
break|break;
block|}
name|fd
operator|.
name|setFontWeight
argument_list|(
name|os2
operator|.
name|getWeightClass
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setSymbolic
argument_list|(
name|isSymbolic
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setNonSymbolic
argument_list|(
operator|!
name|isSymbolic
argument_list|)
expr_stmt|;
comment|// todo retval.setItalic
comment|// todo retval.setAllCap
comment|// todo retval.setSmallCap
comment|// todo retval.setForceBold
name|HeaderTable
name|header
init|=
name|ttf
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|float
name|scaling
init|=
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|header
operator|.
name|getXMin
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|header
operator|.
name|getYMin
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|header
operator|.
name|getXMax
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|header
operator|.
name|getYMax
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontBoundingBox
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|HorizontalHeaderTable
name|hHeader
init|=
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setAscent
argument_list|(
name|hHeader
operator|.
name|getAscender
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setDescent
argument_list|(
name|hHeader
operator|.
name|getDescender
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|GlyphTable
name|glyphTable
init|=
name|ttf
operator|.
name|getGlyph
argument_list|()
decl_stmt|;
name|GlyphData
index|[]
name|glyphs
init|=
name|glyphTable
operator|.
name|getGlyphs
argument_list|()
decl_stmt|;
name|PostScriptTable
name|ps
init|=
name|ttf
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setFixedPitch
argument_list|(
name|ps
operator|.
name|getIsFixedPitch
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setItalicAngle
argument_list|(
name|ps
operator|.
name|getItalicAngle
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|names
init|=
name|ps
operator|.
name|getGlyphNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// if we have a capital H then use that, otherwise use the tallest letter
if|if
condition|(
name|names
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"H"
argument_list|)
condition|)
block|{
name|fd
operator|.
name|setCapHeight
argument_list|(
name|glyphs
index|[
name|i
index|]
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getUpperRightY
argument_list|()
operator|/
name|scaling
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|names
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"x"
argument_list|)
condition|)
block|{
name|fd
operator|.
name|setXHeight
argument_list|(
name|glyphs
index|[
name|i
index|]
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getUpperRightY
argument_list|()
operator|/
name|scaling
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// hmm there does not seem to be a clear definition for StemV,
comment|// this is close enough and I am told it doesn't usually get used.
name|fd
operator|.
name|setStemV
argument_list|(
name|fd
operator|.
name|getFontBoundingBox
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|*
literal|.13f
argument_list|)
expr_stmt|;
name|CMAPTable
name|cmapTable
init|=
name|ttf
operator|.
name|getCMAP
argument_list|()
decl_stmt|;
name|CMAPEncodingEntry
index|[]
name|cmaps
init|=
name|cmapTable
operator|.
name|getCmaps
argument_list|()
decl_stmt|;
name|CMAPEncodingEntry
name|uniMap
init|=
literal|null
decl_stmt|;
for|for
control|(
name|CMAPEncodingEntry
name|cmap
range|:
name|cmaps
control|)
block|{
if|if
condition|(
name|cmap
operator|.
name|getPlatformId
argument_list|()
operator|==
name|CMAPTable
operator|.
name|PLATFORM_WINDOWS
condition|)
block|{
if|if
condition|(
name|CMAPTable
operator|.
name|ENCODING_WIN_UNICODE
operator|==
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
condition|)
block|{
name|uniMap
operator|=
name|cmap
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|this
operator|.
name|getFontEncoding
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// todo: calling this.getFontEncoding() doesn't work if the font is loaded
comment|//       from the local system, because it relies on the FontDescriptor!
comment|//       We make do for now by returning an incomplete descriptor pending further
comment|//       refactoring of PDFont#determineEncoding().
return|return
name|fd
return|;
block|}
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
name|this
operator|.
name|getFontEncoding
argument_list|()
operator|.
name|getCodeToNameMap
argument_list|()
decl_stmt|;
name|int
name|firstChar
init|=
name|Collections
operator|.
name|min
argument_list|(
name|codeToName
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|lastChar
init|=
name|Collections
operator|.
name|max
argument_list|(
name|codeToName
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|HorizontalMetricsTable
name|hMet
init|=
name|ttf
operator|.
name|getHorizontalMetrics
argument_list|()
decl_stmt|;
name|int
index|[]
name|widthValues
init|=
name|hMet
operator|.
name|getAdvanceWidth
argument_list|()
decl_stmt|;
comment|// some monospaced fonts provide only one value for the width
comment|// instead of an array containing the same value for every glyphid
name|boolean
name|isMonospaced
init|=
name|fd
operator|.
name|isFixedPitch
argument_list|()
decl_stmt|;
name|int
name|nWidths
init|=
name|lastChar
operator|-
name|firstChar
operator|+
literal|1
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|widths
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|nWidths
argument_list|)
decl_stmt|;
comment|// use the first width as default
comment|// proportional fonts -> width of the .notdef character
comment|// monospaced-fonts -> the first width
name|int
name|defaultWidth
init|=
name|Math
operator|.
name|round
argument_list|(
name|widthValues
index|[
literal|0
index|]
operator|*
name|scaling
argument_list|)
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
name|nWidths
condition|;
name|i
operator|++
control|)
block|{
name|widths
operator|.
name|add
argument_list|(
name|defaultWidth
argument_list|)
expr_stmt|;
block|}
comment|// Encoding singleton to have acces to the chglyph name to
comment|// unicode cpoint point mapping of Adobe's glyphlist.txt
name|Encoding
name|glyphlist
init|=
name|WinAnsiEncoding
operator|.
name|INSTANCE
decl_stmt|;
comment|// A character code is mapped to a glyph name via the provided font encoding
comment|// Afterwards, the glyph name is translated to a glyph ID.
comment|// For details, see PDFReference16.pdf, Section 5.5.5, p.401
comment|//
for|for
control|(
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|e
range|:
name|codeToName
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// pdf code to unicode by glyph list.
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
condition|)
block|{
name|String
name|c
init|=
name|glyphlist
operator|.
name|getCharacter
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|charCode
init|=
name|c
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|gid
init|=
name|uniMap
operator|.
name|getGlyphId
argument_list|(
name|charCode
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
name|isMonospaced
condition|)
block|{
name|widths
operator|.
name|set
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|-
name|firstChar
argument_list|,
name|defaultWidth
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widths
operator|.
name|set
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|-
name|firstChar
argument_list|,
name|Math
operator|.
name|round
argument_list|(
name|widthValues
index|[
name|gid
index|]
operator|*
name|scaling
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|widths
argument_list|)
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|,
name|firstChar
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|,
name|lastChar
argument_list|)
expr_stmt|;
return|return
name|fd
return|;
block|}
comment|/**      * Return the TTF font as TrueTypeFont.      *       * @return the TTF font      * @throws IOException If there is an error loading the data      */
specifier|public
name|TrueTypeFont
name|getTTFFont
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|ttf
operator|==
literal|null
condition|)
block|{
name|PDFontDescriptorDictionary
name|fd
init|=
operator|(
name|PDFontDescriptorDictionary
operator|)
name|super
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
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|ttf
operator|=
name|ttfParser
operator|.
name|parseTTF
argument_list|(
name|ff2Stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ttf
operator|==
literal|null
condition|)
block|{
comment|// check if there is a font mapping for an external font file
name|ttf
operator|=
name|SystemFontManager
operator|.
name|findTTFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ttf
operator|==
literal|null
condition|)
block|{
name|ttf
operator|=
name|PDFFontManager
operator|.
name|getTrueTypeFallbackFont
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|ttf
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getFontWidth
parameter_list|(
name|int
name|charCode
parameter_list|)
block|{
name|float
name|width
init|=
name|super
operator|.
name|getFontWidth
argument_list|(
name|charCode
argument_list|)
decl_stmt|;
if|if
condition|(
name|width
operator|<=
literal|0
condition|)
block|{
if|if
condition|(
name|advanceWidths
operator|.
name|containsKey
argument_list|(
name|charCode
argument_list|)
condition|)
block|{
name|width
operator|=
name|advanceWidths
operator|.
name|get
argument_list|(
name|charCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|TrueTypeFont
name|ttf
decl_stmt|;
try|try
block|{
name|ttf
operator|=
name|getTTFFont
argument_list|()
expr_stmt|;
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
name|int
name|code
init|=
name|getGIDForCharacterCode
argument_list|(
name|charCode
argument_list|)
decl_stmt|;
name|width
operator|=
name|ttf
operator|.
name|getAdvanceWidth
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|int
name|unitsPerEM
init|=
name|ttf
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
comment|// do we have to scale the width
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
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|width
operator|=
literal|250
expr_stmt|;
block|}
name|advanceWidths
operator|.
name|put
argument_list|(
name|charCode
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|width
return|;
block|}
comment|/**      * Returns the GID for the given character code.      *      * @param code character code      * @return GID (glyph index)      */
specifier|public
name|int
name|getGIDForCharacterCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|extractCmapTable
argument_list|()
expr_stmt|;
name|int
name|result
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|getFontEncoding
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|isSymbolicFont
argument_list|()
condition|)
block|{
try|try
block|{
name|String
name|characterName
init|=
name|getFontEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|characterName
operator|!=
literal|null
condition|)
block|{
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
name|Encoding
operator|.
name|getCharacterForName
argument_list|(
name|characterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|unicode
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|cmapWinUnicode
operator|.
name|getGlyphId
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmapMacintoshSymbol
operator|!=
literal|null
operator|&&
name|MacOSRomanEncoding
operator|.
name|INSTANCE
operator|.
name|hasCodeForName
argument_list|(
name|characterName
argument_list|)
condition|)
block|{
name|result
operator|=
name|MacOSRomanEncoding
operator|.
name|INSTANCE
operator|.
name|getCode
argument_list|(
name|characterName
argument_list|)
expr_stmt|;
name|result
operator|=
name|cmapMacintoshSymbol
operator|.
name|getGlyphId
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmapWinSymbol
operator|!=
literal|null
condition|)
block|{
comment|// fallback scenario if the glyph can't be found yet
comment|// maybe the 3,0 cmap provides a suitable mapping
comment|// see PDFBOX-2091
name|result
operator|=
name|cmapWinSymbol
operator|.
name|getGlyphId
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Caught an exception getGlyhcode: "
operator|+
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getFontEncoding
argument_list|()
operator|==
literal|null
operator|||
name|isSymbolicFont
argument_list|()
condition|)
block|{
if|if
condition|(
name|cmapWinSymbol
operator|!=
literal|null
condition|)
block|{
name|result
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
name|result
operator|==
literal|0
condition|)
block|{
comment|// F000 - F0FF
name|result
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
name|result
operator|==
literal|0
condition|)
block|{
comment|// F100 - F1FF
name|result
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
name|result
operator|==
literal|0
condition|)
block|{
comment|// F200 - F2FF
name|result
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
elseif|else
if|if
condition|(
name|cmapMacintoshSymbol
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|cmapMacintoshSymbol
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
name|result
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
name|result
return|;
block|}
comment|/**      * extract all useful "cmap" subtables.      */
specifier|private
name|void
name|extractCmapTable
parameter_list|()
block|{
if|if
condition|(
name|cmapInitialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|getTTFFont
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't read the true type font"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
name|CMAPTable
name|cmapTable
init|=
name|ttf
operator|.
name|getCMAP
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
name|CMAPEncodingEntry
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
name|CMAPEncodingEntry
name|cmap
range|:
name|cmaps
control|)
block|{
if|if
condition|(
name|CMAPTable
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
name|CMAPTable
operator|.
name|ENCODING_WIN_UNICODE
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
name|CMAPTable
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
name|CMAPTable
operator|.
name|PLATFORM_MACINTOSH
operator|==
name|cmap
operator|.
name|getPlatformId
argument_list|()
condition|)
block|{
if|if
condition|(
name|CMAPTable
operator|.
name|ENCODING_MAC_ROMAN
operator|==
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
condition|)
block|{
name|cmapMacintoshSymbol
operator|=
name|cmap
expr_stmt|;
block|}
block|}
block|}
block|}
name|cmapInitialized
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cmapWinUnicode
operator|=
literal|null
expr_stmt|;
name|cmapWinSymbol
operator|=
literal|null
expr_stmt|;
name|cmapMacintoshSymbol
operator|=
literal|null
expr_stmt|;
name|cmapInitialized
operator|=
literal|false
expr_stmt|;
name|ttf
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|advanceWidths
operator|!=
literal|null
condition|)
block|{
name|advanceWidths
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

