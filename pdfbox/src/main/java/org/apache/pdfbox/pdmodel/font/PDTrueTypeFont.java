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
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FontFormatException
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
name|Properties
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

begin_comment
comment|/**  * This is the TrueType implementation of fonts.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.17 $  */
end_comment

begin_class
specifier|public
class|class
name|PDTrueTypeFont
extends|extends
name|PDSimpleFont
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
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
comment|/**      * This is the key to a property in the PDFBox_External_Fonts.properties      * file to load a Font when a mapping does not exist for the current font.      */
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN_FONT
init|=
literal|"UNKNOWN_FONT"
decl_stmt|;
specifier|private
name|Font
name|awtFont
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
name|Properties
name|externalFonts
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|TrueTypeFont
argument_list|>
name|loadedExternalFonts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TrueTypeFont
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
try|try
block|{
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
literal|"org/apache/pdfbox/resources/PDFBox_External_Fonts.properties"
argument_list|,
name|externalFonts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error loading font resources"
argument_list|,
name|io
argument_list|)
throw|;
block|}
block|}
comment|/**      * Constructor.      */
specifier|public
name|PDTrueTypeFont
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|font
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
block|}
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
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
name|ensureFontDescriptor
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will load a TTF font from a font file.      *      * @param doc The PDF document that will hold the embedded font.      * @param file The file on the filesystem that holds the font file.      * @return A true type font.      * @throws IOException If there is an error loading the file data.      */
specifier|public
specifier|static
name|PDTrueTypeFont
name|loadTTF
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|String
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|loadTTF
argument_list|(
name|doc
argument_list|,
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will load a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param file a ttf file.      * @return a PDTrueTypeFont instance.      * @throws IOException If there is an error loading the data.      */
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
name|loadTTF
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
comment|/**      * This will load a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param stream a ttf input stream.      * @return a PDTrueTypeFont instance.      * @throws IOException If there is an error loading the data.      */
specifier|public
specifier|static
name|PDTrueTypeFont
name|loadTTF
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|PDTrueTypeFont
name|retval
init|=
operator|new
name|PDTrueTypeFont
argument_list|()
decl_stmt|;
name|PDFontDescriptorDictionary
name|fd
init|=
operator|new
name|PDFontDescriptorDictionary
argument_list|()
decl_stmt|;
name|retval
operator|.
name|setFontDescriptor
argument_list|(
name|fd
argument_list|)
expr_stmt|;
name|PDStream
name|fontStream
init|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|stream
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fontStream
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
name|fontStream
operator|.
name|getByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|fontStream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
name|fd
operator|.
name|setFontFile2
argument_list|(
name|fontStream
argument_list|)
expr_stmt|;
comment|// As the stream was close within the PDStream constructor, we have to recreate it
name|stream
operator|=
name|fontStream
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
try|try
block|{
name|retval
operator|.
name|loadDescriptorDictionary
argument_list|(
name|fd
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|//only support winansi encoding right now, should really
comment|//just use Identity-H with unicode mapping
name|retval
operator|.
name|setFontEncoding
argument_list|(
operator|new
name|WinAnsiEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setEncoding
argument_list|(
name|COSName
operator|.
name|WIN_ANSI_ENCODING
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
specifier|private
name|void
name|ensureFontDescriptor
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getFontDescriptor
argument_list|()
operator|==
literal|null
condition|)
block|{
name|PDFontDescriptorDictionary
name|fdd
init|=
operator|new
name|PDFontDescriptorDictionary
argument_list|()
decl_stmt|;
name|setFontDescriptor
argument_list|(
name|fdd
argument_list|)
expr_stmt|;
name|InputStream
name|ttfData
init|=
name|getExternalTTFData
argument_list|()
decl_stmt|;
if|if
condition|(
name|ttfData
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|loadDescriptorDictionary
argument_list|(
name|fdd
argument_list|,
name|ttfData
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ttfData
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|loadDescriptorDictionary
parameter_list|(
name|PDFontDescriptorDictionary
name|fd
parameter_list|,
name|InputStream
name|ttfData
parameter_list|)
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|ttf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|TTFParser
name|parser
init|=
operator|new
name|TTFParser
argument_list|()
decl_stmt|;
name|ttf
operator|=
name|parser
operator|.
name|parseTTF
argument_list|(
name|ttfData
argument_list|)
expr_stmt|;
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|records
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|NameRecord
name|nr
init|=
name|records
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
name|setBaseFont
argument_list|(
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
name|fd
operator|.
name|setNonSymbolic
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|fd
operator|.
name|setSymbolic
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setNonSymbolic
argument_list|(
literal|false
argument_list|)
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
default|default:
comment|//do nothing
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
default|default:
comment|//do nothing
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
comment|//todo retval.setFixedPitch
comment|//todo retval.setNonSymbolic
comment|//todo retval.setItalic
comment|//todo retval.setAllCap
comment|//todo retval.setSmallCap
comment|//todo retval.setForceBold
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
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|header
operator|.
name|getXMin
argument_list|()
operator|*
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
comment|//if we have a capital H then use that, otherwise use the
comment|//tallest letter
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
operator|(
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
operator|*
literal|1000f
operator|)
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
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
operator|(
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
operator|*
literal|1000f
operator|)
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//hmm there does not seem to be a clear definition for StemV,
comment|//this is close enough and I am told it doesn't usually get used.
name|fd
operator|.
name|setStemV
argument_list|(
operator|(
name|fd
operator|.
name|getFontBoundingBox
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|*
literal|.13f
operator|)
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
name|int
index|[]
name|glyphToCCode
init|=
literal|null
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
name|cmaps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|cmaps
index|[
name|i
index|]
operator|.
name|getPlatformId
argument_list|()
operator|==
name|CMAPTable
operator|.
name|PLATFORM_WINDOWS
operator|&&
name|cmaps
index|[
name|i
index|]
operator|.
name|getPlatformEncodingId
argument_list|()
operator|==
name|CMAPTable
operator|.
name|ENCODING_UNICODE
condition|)
block|{
name|glyphToCCode
operator|=
name|cmaps
index|[
name|i
index|]
operator|.
name|getGlyphIdToCharacterCode
argument_list|()
expr_stmt|;
block|}
block|}
name|int
name|firstChar
init|=
literal|0
decl_stmt|;
name|int
name|maxWidths
init|=
name|glyphToCCode
operator|.
name|length
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
name|List
name|widths
init|=
operator|new
name|ArrayList
argument_list|(
name|maxWidths
argument_list|)
decl_stmt|;
name|Integer
name|zero
init|=
operator|new
name|Integer
argument_list|(
literal|250
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
name|maxWidths
condition|;
name|i
operator|++
control|)
block|{
name|widths
operator|.
name|add
argument_list|(
name|zero
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|widthValues
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|glyphToCCode
index|[
name|i
index|]
operator|-
name|firstChar
operator|<
name|widths
operator|.
name|size
argument_list|()
operator|&&
name|glyphToCCode
index|[
name|i
index|]
operator|-
name|firstChar
operator|>=
literal|0
operator|&&
name|widths
operator|.
name|get
argument_list|(
name|glyphToCCode
index|[
name|i
index|]
operator|-
name|firstChar
argument_list|)
operator|==
name|zero
condition|)
block|{
name|widths
operator|.
name|set
argument_list|(
name|glyphToCCode
index|[
name|i
index|]
operator|-
name|firstChar
argument_list|,
operator|new
name|Integer
argument_list|(
call|(
name|int
call|)
argument_list|(
name|widthValues
index|[
name|i
index|]
operator|*
literal|1000f
argument_list|)
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|setWidths
argument_list|(
name|widths
argument_list|)
expr_stmt|;
name|setFirstChar
argument_list|(
name|firstChar
argument_list|)
expr_stmt|;
name|setLastChar
argument_list|(
name|firstChar
operator|+
name|widths
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
name|ttf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Font
name|getawtFont
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFontDescriptorDictionary
name|fd
init|=
operator|(
name|PDFontDescriptorDictionary
operator|)
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|awtFont
operator|==
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
try|try
block|{
comment|// create a font with the embedded data
name|awtFont
operator|=
name|Font
operator|.
name|createFont
argument_list|(
name|Font
operator|.
name|TRUETYPE_FONT
argument_list|,
name|ff2Stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FontFormatException
name|f
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't read the embedded font "
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
name|awtFont
operator|==
literal|null
condition|)
block|{
name|awtFont
operator|=
name|FontManager
operator|.
name|getAwtFont
argument_list|(
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|awtFont
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using font "
operator|+
name|awtFont
operator|.
name|getName
argument_list|()
operator|+
literal|" instead"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// check if the font is part of our environment
name|awtFont
operator|=
name|FontManager
operator|.
name|getAwtFont
argument_list|(
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't find the specified font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
comment|// check if there is a font mapping for an external font file
name|TrueTypeFont
name|ttf
init|=
name|getExternalFontFile2
argument_list|(
name|fd
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|awtFont
operator|=
name|Font
operator|.
name|createFont
argument_list|(
name|Font
operator|.
name|TRUETYPE_FONT
argument_list|,
name|ttf
operator|.
name|getOriginalData
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FontFormatException
name|f
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't read the external fontfile "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
comment|// we can't find anything, so we have to use the standard font
name|awtFont
operator|=
name|FontManager
operator|.
name|getStandardFont
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using font "
operator|+
name|awtFont
operator|.
name|getName
argument_list|()
operator|+
literal|" instead"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|awtFont
return|;
block|}
specifier|private
name|InputStream
name|getExternalTTFData
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|ttfResource
init|=
name|externalFonts
operator|.
name|getProperty
argument_list|(
name|UNKNOWN_FONT
argument_list|)
decl_stmt|;
name|String
name|baseFont
init|=
name|getBaseFont
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseFont
operator|!=
literal|null
operator|&&
name|externalFonts
operator|.
name|containsKey
argument_list|(
name|baseFont
argument_list|)
condition|)
block|{
name|ttfResource
operator|=
name|externalFonts
operator|.
name|getProperty
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ttfResource
operator|!=
literal|null
condition|?
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|ttfResource
argument_list|)
else|:
literal|null
operator|)
return|;
block|}
comment|/**      * Permit to load an external TTF Font program file      *      * Created by Pascal Allain      * Vertical7 Inc.      *      * @param fd The font descriptor currently used      * @return A PDStream with the Font File program, null if fd is null      * @throws IOException If the font is not found      */
specifier|private
name|TrueTypeFont
name|getExternalFontFile2
parameter_list|(
name|PDFontDescriptorDictionary
name|fd
parameter_list|)
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|retval
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
name|String
name|baseFont
init|=
name|getBaseFont
argument_list|()
decl_stmt|;
name|String
name|fontResource
init|=
name|externalFonts
operator|.
name|getProperty
argument_list|(
name|UNKNOWN_FONT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|baseFont
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|externalFonts
operator|.
name|containsKey
argument_list|(
name|baseFont
argument_list|)
operator|)
condition|)
block|{
name|fontResource
operator|=
name|externalFonts
operator|.
name|getProperty
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fontResource
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|(
name|TrueTypeFont
operator|)
name|loadedExternalFonts
operator|.
name|get
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|()
decl_stmt|;
name|InputStream
name|fontStream
init|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|fontResource
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error missing font resource '"
operator|+
name|externalFonts
operator|.
name|get
argument_list|(
name|baseFont
argument_list|)
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|retval
operator|=
name|ttfParser
operator|.
name|parseTTF
argument_list|(
name|fontStream
argument_list|)
expr_stmt|;
name|loadedExternalFonts
operator|.
name|put
argument_list|(
name|baseFont
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

