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
name|Arrays
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

begin_comment
comment|/**  * Embedded PDTrueTypeFont builder. Helper class to populate a PDTrueTypeFont from a TTF.  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
class|class
name|PDTrueTypeFontEmbedder
block|{
specifier|private
specifier|final
name|Encoding
name|fontEncoding
decl_stmt|;
specifier|private
specifier|final
name|TrueTypeFont
name|ttf
decl_stmt|;
comment|/**      * Creates a new TrueType font for embedding.      */
name|PDTrueTypeFontEmbedder
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|dict
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
name|PDFontDescriptor
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
name|parse
argument_list|(
name|stream2
argument_list|)
expr_stmt|;
name|fd
operator|=
name|createFontDescriptor
argument_list|(
name|dict
argument_list|,
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
comment|// creates a new font descriptor dictionary for the given TTF
specifier|private
name|PDFontDescriptor
name|createFontDescriptor
parameter_list|(
name|COSDictionary
name|dict
parameter_list|,
name|TrueTypeFont
name|ttf
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFontDescriptor
name|fd
init|=
operator|new
name|PDFontDescriptor
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
name|CmapTable
name|cmapTable
init|=
name|ttf
operator|.
name|getCmap
argument_list|()
decl_stmt|;
name|CmapSubtable
name|uniMap
init|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_UNICODE
argument_list|,
name|CmapTable
operator|.
name|ENCODING_UNICODE_2_0_FULL
argument_list|)
decl_stmt|;
if|if
condition|(
name|uniMap
operator|==
literal|null
condition|)
block|{
name|uniMap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_UNICODE
argument_list|,
name|CmapTable
operator|.
name|ENCODING_UNICODE_2_0_BMP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uniMap
operator|==
literal|null
condition|)
block|{
name|uniMap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_WINDOWS
argument_list|,
name|CmapTable
operator|.
name|ENCODING_WIN_UNICODE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uniMap
operator|==
literal|null
condition|)
block|{
comment|// Microsoft's "Recommendations for OpenType Fonts" says that "Symbol" encoding
comment|// actually means "Unicode, non-standard character set"
name|uniMap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_WINDOWS
argument_list|,
name|CmapTable
operator|.
name|ENCODING_WIN_SYMBOL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uniMap
operator|==
literal|null
condition|)
block|{
comment|// there should always be a usable cmap, if this happens we haven't tried hard enough
comment|// to find one. Furthermore, if we loaded the font from disk then we should've checked
comment|// first to see that it had a suitable cmap before calling createFontDescriptor
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ttf: no suitable cmap for font '"
operator|+
name|ttf
operator|.
name|getNaming
argument_list|()
operator|.
name|getFontFamily
argument_list|()
operator|+
literal|"', found: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|cmapTable
operator|.
name|getCmaps
argument_list|()
argument_list|)
argument_list|)
throw|;
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
operator|||
name|widthValues
operator|.
name|length
operator|==
literal|1
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
comment|// A character code is mapped to a glyph name via the provided font encoding
comment|// Afterwards, the glyph name is translated to a glyph ID.
comment|// For details, see PDFReference16.pdf, Section 5.5.5, p.401
comment|//
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
name|GlyphList
operator|.
name|DEFAULT
operator|.
name|toUnicode
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
comment|/**      * Returns the font's encoding.      */
specifier|public
name|Encoding
name|getFontEncoding
parameter_list|()
block|{
return|return
name|fontEncoding
return|;
block|}
comment|/**      * Returns the FontBox font.      */
specifier|public
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|()
block|{
return|return
name|ttf
return|;
block|}
block|}
end_class

end_unit

