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
name|awt
operator|.
name|geom
operator|.
name|Point2D
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
name|Arrays
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
name|EncodedFont
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
name|type1
operator|.
name|DamagedFontException
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
name|type1
operator|.
name|Type1Font
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
name|StandardEncoding
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
name|Matrix
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
name|pdmodel
operator|.
name|font
operator|.
name|UniUtil
operator|.
name|getUniNameOfCodePoint
import|;
end_import

begin_comment
comment|/**  * A PostScript Type 1 Font.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDType1Font
extends|extends
name|PDSimpleFont
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
name|PDType1Font
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// alternative names for glyphs which are commonly encountered
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ALT_NAMES
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"ff"
argument_list|,
literal|"f_f"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"ffi"
argument_list|,
literal|"f_f_i"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"ffl"
argument_list|,
literal|"f_f_l"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"fi"
argument_list|,
literal|"f_i"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"fl"
argument_list|,
literal|"f_l"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"st"
argument_list|,
literal|"s_t"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"IJ"
argument_list|,
literal|"I_J"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"ij"
argument_list|,
literal|"i_j"
argument_list|)
expr_stmt|;
name|ALT_NAMES
operator|.
name|put
argument_list|(
literal|"ellipsis"
argument_list|,
literal|"elipsis"
argument_list|)
expr_stmt|;
comment|// misspelled in ArialMT
block|}
specifier|private
specifier|static
specifier|final
name|int
name|PFB_START_MARKER
init|=
literal|0x80
decl_stmt|;
comment|// todo: replace with enum? or getters?
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_ROMAN
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Roman"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Bold"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_ITALIC
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Italic"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_BOLD_ITALIC
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-BoldItalic"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-Bold"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-Oblique"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_BOLD_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-BoldOblique"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-Bold"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-Oblique"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_BOLD_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-BoldOblique"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|SYMBOL
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Symbol"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|ZAPF_DINGBATS
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"ZapfDingbats"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Type1Font
name|type1font
decl_stmt|;
comment|// embedded font
specifier|private
specifier|final
name|FontBoxFont
name|genericFont
decl_stmt|;
comment|// embedded or system font for rendering
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
name|Matrix
name|fontMatrix
decl_stmt|;
specifier|private
specifier|final
name|AffineTransform
name|fontMatrixTransform
decl_stmt|;
comment|/**      * Creates a Type 1 standard 14 font for embedding.      *      * @param baseFont One of the standard 14 PostScript names      */
specifier|private
name|PDType1Font
parameter_list|(
name|String
name|baseFont
parameter_list|)
block|{
name|super
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
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
name|TYPE1
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|baseFont
argument_list|)
expr_stmt|;
name|encoding
operator|=
operator|new
name|WinAnsiEncoding
argument_list|()
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|,
name|COSName
operator|.
name|WIN_ANSI_ENCODING
argument_list|)
expr_stmt|;
comment|// todo: could load the PFB font here if we wanted to support Standard 14 embedding
name|type1font
operator|=
literal|null
expr_stmt|;
name|FontMapping
argument_list|<
name|FontBoxFont
argument_list|>
name|mapping
init|=
name|FontMappers
operator|.
name|instance
argument_list|()
operator|.
name|getFontBoxFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|,
name|getFontDescriptor
argument_list|()
argument_list|)
decl_stmt|;
name|genericFont
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
name|String
name|fontName
decl_stmt|;
try|try
block|{
name|fontName
operator|=
name|genericFont
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fontName
operator|=
literal|"?"
expr_stmt|;
block|}
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using fallback font "
operator|+
name|fontName
operator|+
literal|" for base font "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|isEmbedded
operator|=
literal|false
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
name|fontMatrixTransform
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a new Type 1 font for embedding.      *      * @param doc PDF document to write to      * @param pfbIn PFB file stream      * @throws IOException      */
specifier|public
name|PDType1Font
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|pfbIn
parameter_list|)
throws|throws
name|IOException
block|{
name|PDType1FontEmbedder
name|embedder
init|=
operator|new
name|PDType1FontEmbedder
argument_list|(
name|doc
argument_list|,
name|dict
argument_list|,
name|pfbIn
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|encoding
operator|=
name|embedder
operator|.
name|getFontEncoding
argument_list|()
expr_stmt|;
name|glyphList
operator|=
name|embedder
operator|.
name|getGlyphList
argument_list|()
expr_stmt|;
name|type1font
operator|=
name|embedder
operator|.
name|getType1Font
argument_list|()
expr_stmt|;
name|genericFont
operator|=
name|embedder
operator|.
name|getType1Font
argument_list|()
expr_stmt|;
name|isEmbedded
operator|=
literal|true
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
name|fontMatrixTransform
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a new Type 1 font for embedding.      *      * @param doc PDF document to write to      * @param pfbIn PFB file stream      * @param encoding      * @throws IOException      */
specifier|public
name|PDType1Font
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|pfbIn
parameter_list|,
name|Encoding
name|encoding
parameter_list|)
throws|throws
name|IOException
block|{
name|PDType1FontEmbedder
name|embedder
init|=
operator|new
name|PDType1FontEmbedder
argument_list|(
name|doc
argument_list|,
name|dict
argument_list|,
name|pfbIn
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
name|glyphList
operator|=
name|embedder
operator|.
name|getGlyphList
argument_list|()
expr_stmt|;
name|type1font
operator|=
name|embedder
operator|.
name|getType1Font
argument_list|()
expr_stmt|;
name|genericFont
operator|=
name|embedder
operator|.
name|getType1Font
argument_list|()
expr_stmt|;
name|isEmbedded
operator|=
literal|true
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
name|fontMatrixTransform
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a Type 1 font from a Font dictionary in a PDF.      *       * @param fontDictionary font dictionary.      * @throws IOException if there was an error initializing the font.      * @throws IllegalArgumentException if /FontFile3 was used.      */
specifier|public
name|PDType1Font
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
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
name|Type1Font
name|t1
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
name|fd
operator|!=
literal|null
condition|)
block|{
comment|// a Type1 font may contain a Type1C font
name|PDStream
name|fontFile3
init|=
name|fd
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontFile3
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Use PDType1CFont for FontFile3"
argument_list|)
throw|;
block|}
comment|// or it may contain a PFB
name|PDStream
name|fontFile
init|=
name|fd
operator|.
name|getFontFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|COSStream
name|stream
init|=
name|fontFile
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|int
name|length1
init|=
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|)
decl_stmt|;
name|int
name|length2
init|=
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH2
argument_list|)
decl_stmt|;
comment|// repair Length1 if necessary
name|byte
index|[]
name|bytes
init|=
name|fontFile
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|length1
operator|=
name|repairLength1
argument_list|(
name|bytes
argument_list|,
name|length1
argument_list|)
expr_stmt|;
if|if
condition|(
name|bytes
operator|.
name|length
operator|>
literal|0
operator|&&
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xff
operator|)
operator|==
name|PFB_START_MARKER
condition|)
block|{
comment|// some bad files embed the entire PFB, see PDFBOX-2607
name|t1
operator|=
name|Type1Font
operator|.
name|createWithPFB
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the PFB embedded as two segments back-to-back
name|byte
index|[]
name|segment1
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|length1
argument_list|)
decl_stmt|;
name|byte
index|[]
name|segment2
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|bytes
argument_list|,
name|length1
argument_list|,
name|length1
operator|+
name|length2
argument_list|)
decl_stmt|;
comment|// empty streams are simply ignored
if|if
condition|(
name|length1
operator|>
literal|0
operator|&&
name|length2
operator|>
literal|0
condition|)
block|{
name|t1
operator|=
name|Type1Font
operator|.
name|createWithSegments
argument_list|(
name|segment1
argument_list|,
name|segment2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|DamagedFontException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't read damaged embedded Type1 font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
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
name|error
argument_list|(
literal|"Can't read the embedded Type1 font "
operator|+
name|fd
operator|.
name|getFontName
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
name|t1
operator|!=
literal|null
expr_stmt|;
name|isDamaged
operator|=
name|fontIsDamaged
expr_stmt|;
name|type1font
operator|=
name|t1
expr_stmt|;
comment|// find a generic font to use for rendering, could be a .pfb, but might be a .ttf
if|if
condition|(
name|type1font
operator|!=
literal|null
condition|)
block|{
name|genericFont
operator|=
name|type1font
expr_stmt|;
block|}
else|else
block|{
name|FontMapping
argument_list|<
name|FontBoxFont
argument_list|>
name|mapping
init|=
name|FontMappers
operator|.
name|instance
argument_list|()
operator|.
name|getFontBoxFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|,
name|fd
argument_list|)
decl_stmt|;
name|genericFont
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
literal|"Using fallback font "
operator|+
name|genericFont
operator|.
name|getName
argument_list|()
operator|+
literal|" for "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|readEncoding
argument_list|()
expr_stmt|;
name|fontMatrixTransform
operator|=
name|getFontMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
expr_stmt|;
name|fontMatrixTransform
operator|.
name|scale
argument_list|(
literal|1000
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
comment|/**      * Some Type 1 fonts have an invalid Length1, which causes the binary segment of the font      * to be truncated, see PDFBOX-2350.      *      * @param bytes Type 1 stream bytes      * @param length1 Length1 from the Type 1 stream      * @return repaired Length1 value      */
specifier|private
name|int
name|repairLength1
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|length1
parameter_list|)
block|{
comment|// scan backwards from the end of the first segment to find 'exec'
name|int
name|offset
init|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|length1
operator|-
literal|4
argument_list|)
decl_stmt|;
if|if
condition|(
name|offset
operator|<=
literal|0
operator|||
name|offset
operator|>
name|bytes
operator|.
name|length
operator|-
literal|4
condition|)
block|{
name|offset
operator|=
name|bytes
operator|.
name|length
operator|-
literal|4
expr_stmt|;
block|}
while|while
condition|(
name|offset
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|bytes
index|[
name|offset
operator|+
literal|0
index|]
operator|==
literal|'e'
operator|&&
name|bytes
index|[
name|offset
operator|+
literal|1
index|]
operator|==
literal|'x'
operator|&&
name|bytes
index|[
name|offset
operator|+
literal|2
index|]
operator|==
literal|'e'
operator|&&
name|bytes
index|[
name|offset
operator|+
literal|3
index|]
operator|==
literal|'c'
condition|)
block|{
name|offset
operator|+=
literal|4
expr_stmt|;
comment|// skip additional CR LF space characters
while|while
condition|(
name|offset
operator|<
name|length1
operator|&&
operator|(
name|bytes
index|[
name|offset
index|]
operator|==
literal|'\r'
operator|||
name|bytes
index|[
name|offset
index|]
operator|==
literal|'\n'
operator|||
name|bytes
index|[
name|offset
index|]
operator|==
literal|' '
operator|)
condition|)
block|{
name|offset
operator|++
expr_stmt|;
block|}
break|break;
block|}
name|offset
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|length1
operator|-
name|offset
operator|!=
literal|0
operator|&&
name|offset
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignored invalid Length1 "
operator|+
name|length1
operator|+
literal|" for Type 1 font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|offset
return|;
block|}
return|return
name|length1
return|;
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
name|String
name|name
init|=
name|codeToName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|getStandard14AFM
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|afmName
init|=
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
return|return
name|getStandard14AFM
argument_list|()
operator|.
name|getCharacterHeight
argument_list|(
name|afmName
argument_list|)
return|;
comment|// todo: isn't this the y-advance, not the height?
block|}
else|else
block|{
comment|// todo: should be scaled by font matrix
return|return
operator|(
name|float
operator|)
name|genericFont
operator|.
name|getPath
argument_list|(
name|name
argument_list|)
operator|.
name|getBounds
argument_list|()
operator|.
name|getHeight
argument_list|()
return|;
block|}
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
name|unicode
operator|>
literal|0xff
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
literal|"Can't encode U+%04X in font %s. "
operator|+
literal|"Type 1 fonts only support 8-bit code points"
argument_list|,
name|unicode
argument_list|,
name|getName
argument_list|()
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
if|if
condition|(
operator|!
name|encoding
operator|.
name|contains
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
literal|"U+%04X is not available in this font's encoding: %s"
argument_list|,
name|unicode
argument_list|,
name|encoding
operator|.
name|getEncodingName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|nameInFont
init|=
name|getNameInFont
argument_list|(
name|name
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
name|encoding
operator|.
name|getNameToCodeMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|nameInFont
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
operator|||
operator|!
name|genericFont
operator|.
name|hasGlyph
argument_list|(
name|nameInFont
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
name|String
name|name
init|=
name|codeToName
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// width of .notdef is ignored for substitutes, see PDFBOX-1900
if|if
condition|(
operator|!
name|isEmbedded
operator|&&
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
condition|)
block|{
return|return
literal|250
return|;
block|}
name|float
name|width
init|=
name|genericFont
operator|.
name|getWidth
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Point2D
name|p
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|width
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|fontMatrixTransform
operator|.
name|transform
argument_list|(
name|p
argument_list|,
name|p
argument_list|)
expr_stmt|;
return|return
operator|(
name|float
operator|)
name|p
operator|.
name|getX
argument_list|()
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
name|float
name|getAverageFontWidth
parameter_list|()
block|{
if|if
condition|(
name|getStandard14AFM
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getStandard14AFM
argument_list|()
operator|.
name|getAverageCharacterWidth
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getAverageFontWidth
argument_list|()
return|;
block|}
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
comment|// extract from Type1 font/substitute
if|if
condition|(
name|genericFont
operator|instanceof
name|EncodedFont
condition|)
block|{
return|return
name|Type1Encoding
operator|.
name|fromFontBox
argument_list|(
operator|(
operator|(
name|EncodedFont
operator|)
name|genericFont
operator|)
operator|.
name|getEncoding
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// default (only happens with TTFs)
return|return
name|StandardEncoding
operator|.
name|INSTANCE
return|;
block|}
block|}
block|}
comment|/**      * Returns the embedded or substituted Type 1 font, or null if there is none.      */
specifier|public
name|Type1Font
name|getType1Font
parameter_list|()
block|{
return|return
name|type1font
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
name|genericFont
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
operator|.
name|getLowerLeftX
argument_list|()
operator|!=
literal|0
operator|||
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
operator|!=
literal|0
operator|||
name|bbox
operator|.
name|getUpperRightX
argument_list|()
operator|!=
literal|0
operator|||
name|bbox
operator|.
name|getUpperRightY
argument_list|()
operator|!=
literal|0
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
name|genericFont
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
comment|//@Override
specifier|public
name|String
name|codeToName
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
return|return
name|getNameInFont
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Maps a PostScript glyph name to the name in the underlying font, for example when      * using a TTF font we might map "W" to "uni0057".      */
specifier|private
name|String
name|getNameInFont
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isEmbedded
argument_list|()
operator|||
name|genericFont
operator|.
name|hasGlyph
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
comment|// try alternative name
name|String
name|altName
init|=
name|ALT_NAMES
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|altName
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
operator|&&
name|genericFont
operator|.
name|hasGlyph
argument_list|(
name|altName
argument_list|)
condition|)
block|{
return|return
name|altName
return|;
block|}
else|else
block|{
comment|// try unicode name
name|String
name|unicodes
init|=
name|getGlyphList
argument_list|()
operator|.
name|toUnicode
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicodes
operator|!=
literal|null
operator|&&
name|unicodes
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|String
name|uniName
init|=
name|getUniNameOfCodePoint
argument_list|(
name|unicodes
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|genericFont
operator|.
name|hasGlyph
argument_list|(
name|uniName
argument_list|)
condition|)
block|{
return|return
name|uniName
return|;
block|}
block|}
block|}
block|}
return|return
literal|".notdef"
return|;
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
comment|// Acrobat does not draw .notdef for Type 1 fonts, see PDFBOX-2421
comment|// I suspect that it does do this for embedded fonts though, but this is untested
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
operator|&&
operator|!
name|isEmbedded
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
name|genericFont
operator|.
name|getPath
argument_list|(
name|getNameInFont
argument_list|(
name|name
argument_list|)
argument_list|)
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
return|return
name|genericFont
operator|.
name|hasGlyph
argument_list|(
name|getNameInFont
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
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
comment|// PDF specified that Type 1 fonts use a 1000upem matrix, but some fonts specify
comment|// their own custom matrix anyway, for example PDFBOX-2298
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
init|=
literal|null
decl_stmt|;
try|try
block|{
name|numbers
operator|=
name|genericFont
operator|.
name|getFontMatrix
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fontMatrix
operator|=
name|DEFAULT_FONT_MATRIX
expr_stmt|;
block|}
if|if
condition|(
name|numbers
operator|!=
literal|null
operator|&&
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|6
condition|)
block|{
name|fontMatrix
operator|=
operator|new
name|Matrix
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
block|}
return|return
name|fontMatrix
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
block|}
end_class

end_unit

