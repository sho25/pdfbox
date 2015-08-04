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
name|ByteArrayInputStream
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
name|pfb
operator|.
name|PfbParser
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
name|Type1Encoding
import|;
end_import

begin_comment
comment|/**  * Embedded PDType1Font builder. Helper class to populate a PDType1Font from a PFB and AFM.  *  * @author Michael Niedermair  */
end_comment

begin_class
class|class
name|PDType1FontEmbedder
block|{
specifier|private
specifier|final
name|Encoding
name|fontEncoding
decl_stmt|;
specifier|private
specifier|final
name|Type1Font
name|type1
decl_stmt|;
comment|/**      * This will load a PFB to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param dict The Font dictionary to write to.      * @param pfbStream The pfb input.      * @throws IOException If there is an error loading the data.      */
name|PDType1FontEmbedder
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|InputStream
name|pfbStream
parameter_list|,
name|Encoding
name|encoding
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
name|TYPE1
argument_list|)
expr_stmt|;
comment|// read the pfb
name|byte
index|[]
name|pfbBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|pfbStream
argument_list|)
decl_stmt|;
name|PfbParser
name|pfbParser
init|=
operator|new
name|PfbParser
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|pfbBytes
argument_list|)
argument_list|)
decl_stmt|;
name|type1
operator|=
name|Type1Font
operator|.
name|createWithPFB
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|pfbBytes
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|fontEncoding
operator|=
name|Type1Encoding
operator|.
name|fromFontBox
argument_list|(
name|type1
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fontEncoding
operator|=
name|encoding
expr_stmt|;
block|}
comment|// build font descriptor
name|PDFontDescriptor
name|fd
init|=
name|buildFontDescriptor
argument_list|(
name|type1
argument_list|)
decl_stmt|;
name|PDStream
name|fontStream
init|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|pfbParser
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|fontStream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
literal|"Length"
argument_list|,
name|pfbParser
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pfbParser
operator|.
name|getLengths
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|fontStream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
literal|"Length"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|,
name|pfbParser
operator|.
name|getLengths
argument_list|()
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|fd
operator|.
name|setFontFile
argument_list|(
name|fontStream
argument_list|)
expr_stmt|;
comment|// set the values
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
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|type1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// widths
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
literal|256
argument_list|)
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
literal|255
condition|;
name|code
operator|++
control|)
block|{
name|String
name|name
init|=
name|fontEncoding
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|int
name|width
init|=
name|Math
operator|.
name|round
argument_list|(
name|type1
operator|.
name|getWidth
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|widths
operator|.
name|add
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|,
literal|0
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
literal|255
argument_list|)
expr_stmt|;
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
block|}
comment|/**      * Returns a PDFontDescriptor for the given PFB.      */
specifier|static
name|PDFontDescriptor
name|buildFontDescriptor
parameter_list|(
name|Type1Font
name|type1
parameter_list|)
block|{
name|boolean
name|isSymbolic
init|=
name|type1
operator|.
name|getEncoding
argument_list|()
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|encoding
operator|.
name|BuiltInEncoding
decl_stmt|;
name|PDFontDescriptor
name|fd
init|=
operator|new
name|PDFontDescriptor
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setFontName
argument_list|(
name|type1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontFamily
argument_list|(
name|type1
operator|.
name|getFamilyName
argument_list|()
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
name|fd
operator|.
name|setSymbolic
argument_list|(
name|isSymbolic
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontBoundingBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|type1
operator|.
name|getFontBBox
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setItalicAngle
argument_list|(
name|type1
operator|.
name|getItalicAngle
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setAscent
argument_list|(
name|type1
operator|.
name|getFontBBox
argument_list|()
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setDescent
argument_list|(
name|type1
operator|.
name|getFontBBox
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setCapHeight
argument_list|(
name|type1
operator|.
name|getBlueValues
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setStemV
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// for PDF/A
return|return
name|fd
return|;
block|}
comment|/**      * Returns a PDFontDescriptor for the given AFM. Used only for Standard 14 fonts.      *      * @param metrics AFM      */
specifier|static
name|PDFontDescriptor
name|buildFontDescriptor
parameter_list|(
name|FontMetrics
name|metrics
parameter_list|)
block|{
name|boolean
name|isSymbolic
init|=
name|metrics
operator|.
name|getEncodingScheme
argument_list|()
operator|.
name|equals
argument_list|(
literal|"FontSpecific"
argument_list|)
decl_stmt|;
name|PDFontDescriptor
name|fd
init|=
operator|new
name|PDFontDescriptor
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setFontName
argument_list|(
name|metrics
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontFamily
argument_list|(
name|metrics
operator|.
name|getFamilyName
argument_list|()
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
name|fd
operator|.
name|setSymbolic
argument_list|(
name|isSymbolic
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontBoundingBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|metrics
operator|.
name|getFontBBox
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setItalicAngle
argument_list|(
name|metrics
operator|.
name|getItalicAngle
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setAscent
argument_list|(
name|metrics
operator|.
name|getAscender
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setDescent
argument_list|(
name|metrics
operator|.
name|getDescender
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setCapHeight
argument_list|(
name|metrics
operator|.
name|getCapHeight
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setXHeight
argument_list|(
name|metrics
operator|.
name|getXHeight
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setAverageWidth
argument_list|(
name|metrics
operator|.
name|getAverageCharacterWidth
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setCharacterSet
argument_list|(
name|metrics
operator|.
name|getCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setStemV
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// for PDF/A
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
comment|/**      * Returns the font's glyph list.      */
specifier|public
name|GlyphList
name|getGlyphList
parameter_list|()
block|{
return|return
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
return|;
block|}
comment|/**      * Returns the Type 1 font.      */
specifier|public
name|Type1Font
name|getType1Font
parameter_list|()
block|{
return|return
name|type1
return|;
block|}
block|}
end_class

end_unit

