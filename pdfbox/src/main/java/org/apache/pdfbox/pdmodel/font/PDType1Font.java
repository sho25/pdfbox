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
name|afm
operator|.
name|AFMParser
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
name|afm
operator|.
name|FontMetrics
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
name|Type1Equivalent
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
name|util
operator|.
name|ResourceLoader
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
implements|implements
name|PDType1Equivalent
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
comment|/**      * The static map of the default Adobe font metrics.      */
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|FontMetrics
argument_list|>
name|AFM_MAP
decl_stmt|;
static|static
block|{
name|AFM_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|FontMetrics
argument_list|>
argument_list|()
expr_stmt|;
name|addMetric
argument_list|(
literal|"Courier-Bold"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Courier-BoldOblique"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Courier"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Courier-Oblique"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Helvetica"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Helvetica-Bold"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Helvetica-BoldOblique"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Helvetica-Oblique"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Symbol"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Times-Bold"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Times-BoldItalic"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Times-Italic"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"Times-Roman"
argument_list|)
expr_stmt|;
name|addMetric
argument_list|(
literal|"ZapfDingbats"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addMetric
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|prefix
init|=
name|name
decl_stmt|;
comment|// todo: HACK
try|try
block|{
name|String
name|resource
init|=
literal|"org/apache/pdfbox/resources/afm/"
operator|+
name|prefix
operator|+
literal|".afm"
decl_stmt|;
name|InputStream
name|afmStream
init|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|afmStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|AFMParser
name|parser
init|=
operator|new
name|AFMParser
argument_list|(
name|afmStream
argument_list|)
decl_stmt|;
name|FontMetrics
name|metric
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
name|AFM_MAP
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|metric
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|afmStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Something went wrong when reading the adobe afm files"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
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
name|FontMetrics
name|afm
decl_stmt|;
comment|// for standard 14 fonts
specifier|private
specifier|final
name|Type1Font
name|type1font
decl_stmt|;
comment|// embedded font
specifier|private
specifier|final
name|Type1Equivalent
name|type1Equivalent
decl_stmt|;
comment|// embedded or system font for rendering
specifier|private
specifier|final
name|boolean
name|isEmbedded
decl_stmt|;
comment|/**      * Creates a Type 1 standard 14 font for embedding.      *      * @param baseFont One of the standard 14 PostScript names      */
specifier|private
name|PDType1Font
parameter_list|(
name|String
name|baseFont
parameter_list|)
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
name|afm
operator|=
name|getAFMFromBaseFont
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
if|if
condition|(
name|afm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No AFM for font "
operator|+
name|baseFont
argument_list|)
throw|;
block|}
comment|// todo: could load the PFB font here if we wanted to support Standard 14 embedding
name|type1font
operator|=
literal|null
expr_stmt|;
name|type1Equivalent
operator|=
literal|null
expr_stmt|;
name|isEmbedded
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Creates a new Type 1 font for embedding.      *      * @param doc PDF document to write to      * @param afmIn AFM file stream      * @param pfbIn PFB file stream      * @throws IOException      */
specifier|public
name|PDType1Font
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|afmIn
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
name|afmIn
argument_list|,
name|pfbIn
argument_list|)
decl_stmt|;
name|encoding
operator|=
name|embedder
operator|.
name|getFontEncoding
argument_list|()
expr_stmt|;
name|afm
operator|=
literal|null
expr_stmt|;
comment|// only used for standard 14 fonts, not AFM fonts as we already have the PFB
name|type1font
operator|=
name|embedder
operator|.
name|getType1Font
argument_list|()
expr_stmt|;
name|type1Equivalent
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
block|}
comment|/**      * Creates a Type 1 font from a Font dictionary in a PDF.      *       * @param fontDictionary font dictionary      */
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
if|if
condition|(
name|fd
operator|!=
literal|null
operator|&&
name|fd
operator|instanceof
name|PDFontDescriptorDictionary
condition|)
comment|//<-- todo: must be true
block|{
comment|// a Type1 font may contain a Type1C font
name|PDStream
name|fontFile3
init|=
operator|(
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fd
operator|)
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
operator|(
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fd
operator|)
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
comment|// the PFB embedded as two segments back-to-back
name|byte
index|[]
name|bytes
init|=
name|fontFile
operator|.
name|getByteArray
argument_list|()
decl_stmt|;
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
block|}
block|}
block|}
name|isEmbedded
operator|=
name|t1
operator|!=
literal|null
expr_stmt|;
comment|// try to find a suitable .pfb font to substitute
if|if
condition|(
name|t1
operator|==
literal|null
condition|)
block|{
name|t1
operator|=
name|ExternalFonts
operator|.
name|getType1Font
argument_list|(
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|type1font
operator|=
name|t1
expr_stmt|;
comment|// find a type 1-equivalent font to use for rendering, could even be a .ttf
if|if
condition|(
name|type1font
operator|!=
literal|null
condition|)
block|{
name|type1Equivalent
operator|=
name|type1font
expr_stmt|;
block|}
else|else
block|{
name|Type1Equivalent
name|t1Equiv
init|=
name|ExternalFonts
operator|.
name|getType1EquivalentFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|t1Equiv
operator|!=
literal|null
condition|)
block|{
name|type1Equivalent
operator|=
name|t1Equiv
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using fallback font for "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
name|type1Equivalent
operator|=
name|ExternalFonts
operator|.
name|getType1FallbackFont
argument_list|(
name|getFontDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// todo: for standard 14 only. todo: move this to a subclass "PDStandardType1Font" ?
name|afm
operator|=
name|getAFMFromBaseFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
comment|// may be null (it usually is)
name|readEncoding
argument_list|()
expr_stmt|;
block|}
comment|// todo: move this to a subclass?
specifier|private
name|FontMetrics
name|getAFMFromBaseFont
parameter_list|(
name|String
name|baseFont
parameter_list|)
block|{
if|if
condition|(
name|baseFont
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|baseFont
operator|.
name|contains
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
name|baseFont
operator|=
name|baseFont
operator|.
name|substring
argument_list|(
name|baseFont
operator|.
name|indexOf
argument_list|(
literal|'+'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|AFM_MAP
operator|.
name|get
argument_list|(
name|baseFont
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
name|PDFontDescriptor
name|fd
init|=
name|super
operator|.
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fd
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|afm
operator|!=
literal|null
condition|)
block|{
comment|// this is for embedding fonts into PDFs, rather than for reading, though it works.
name|fd
operator|=
operator|new
name|PDFontDescriptorAFM
argument_list|(
name|afm
argument_list|)
expr_stmt|;
name|setFontDescriptor
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fd
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
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|afm
operator|!=
literal|null
condition|)
block|{
return|return
name|afm
operator|.
name|getCharacterHeight
argument_list|(
name|name
argument_list|)
return|;
comment|// todo: isn't this the y-advance, not the height?
block|}
else|else
block|{
return|return
operator|(
name|float
operator|)
name|type1Equivalent
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
if|if
condition|(
name|afm
operator|!=
literal|null
condition|)
block|{
return|return
name|afm
operator|.
name|getCharacterWidth
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|type1Equivalent
operator|.
name|getWidth
argument_list|(
name|name
argument_list|)
return|;
block|}
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
name|afm
operator|!=
literal|null
condition|)
block|{
return|return
name|afm
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
name|afm
operator|!=
literal|null
condition|)
block|{
comment|// read from AFM
return|return
operator|new
name|Type1Encoding
argument_list|(
name|afm
argument_list|)
return|;
block|}
else|else
block|{
comment|// extract from Type1 font/substitute
if|if
condition|(
name|type1Equivalent
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|Type1Encoding
operator|.
name|fromFontBox
argument_list|(
name|type1Equivalent
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
comment|/**      * Returns the embedded or system font for rendering. This font is a Type 1-equivalent, but      * may not be a Type 1 font, it could be a CFF font or TTF font. If there is no suitable font      * then the fallback font will be returned: this method never returns null.      */
specifier|public
name|Type1Equivalent
name|getFontForRendering
parameter_list|()
block|{
return|return
name|type1Equivalent
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
name|type1Equivalent
operator|.
name|getFontBBox
argument_list|()
return|;
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
name|type1Equivalent
operator|.
name|hasGlyph
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|codeToName
parameter_list|(
name|int
name|code
parameter_list|)
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
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
return|return
literal|".notdef"
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
return|return
name|type1Equivalent
operator|.
name|getPath
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

