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
name|CharMetric
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
name|COSArray
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
name|COSInteger
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
name|DictionaryEncoding
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
name|Iterator
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
name|FontMetrics
name|metrics
decl_stmt|;
specifier|private
specifier|final
name|Type1Font
name|type1
decl_stmt|;
comment|/**      * This will load a afm and pfb to be embedding into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param dict The Font dictionary to write to.      * @param afmStream The afm input.      * @param pfbStream The pfb input.      * @throws IOException If there is an error loading the data.      */
specifier|public
name|PDType1FontEmbedder
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|InputStream
name|afmStream
parameter_list|,
name|InputStream
name|pfbStream
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
comment|// read the afm
name|AFMParser
name|afmParser
init|=
operator|new
name|AFMParser
argument_list|(
name|afmStream
argument_list|)
decl_stmt|;
name|metrics
operator|=
name|afmParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|this
operator|.
name|fontEncoding
operator|=
name|encodingFromAFM
argument_list|(
name|metrics
argument_list|)
expr_stmt|;
comment|// build font descriptor
name|PDFontDescriptor
name|fd
init|=
name|buildFontDescriptor
argument_list|(
name|metrics
argument_list|)
decl_stmt|;
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
name|fontStream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
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
name|metrics
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
comment|// get firstchar, lastchar
name|int
name|firstchar
init|=
literal|255
decl_stmt|;
name|int
name|lastchar
init|=
literal|0
decl_stmt|;
comment|// widths
name|List
argument_list|<
name|CharMetric
argument_list|>
name|listmetric
init|=
name|metrics
operator|.
name|getCharMetrics
argument_list|()
decl_stmt|;
name|Encoding
name|encoding
init|=
name|getFontEncoding
argument_list|()
decl_stmt|;
name|int
name|maxWidths
init|=
literal|256
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
name|maxWidths
argument_list|)
decl_stmt|;
name|int
name|zero
init|=
literal|250
decl_stmt|;
name|Iterator
argument_list|<
name|CharMetric
argument_list|>
name|iter
init|=
name|listmetric
operator|.
name|iterator
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
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CharMetric
name|m
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|int
name|n
init|=
name|m
operator|.
name|getCharacterCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|firstchar
operator|=
name|Math
operator|.
name|min
argument_list|(
name|firstchar
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|lastchar
operator|=
name|Math
operator|.
name|max
argument_list|(
name|lastchar
argument_list|,
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|getWx
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|width
init|=
name|Math
operator|.
name|round
argument_list|(
name|m
operator|.
name|getWx
argument_list|()
argument_list|)
decl_stmt|;
name|widths
operator|.
name|set
argument_list|(
name|n
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Returns a PDFontDescriptor for the given AFM.      *      * @param metrics AFM      */
specifier|static
name|PDFontDescriptor
name|buildFontDescriptor
parameter_list|(
name|FontMetrics
name|metrics
parameter_list|)
block|{
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
literal|true
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
return|return
name|fd
return|;
block|}
comment|// This will generate a Encoding from the AFM-Encoding, because the AFM-Enconding isn't exported
comment|// to the pdf and consequently the StandardEncoding is used so that any special character is
comment|// missing I've copied the code from the pdfbox-forum posted by V0JT4 and made some additions
comment|// concerning german umlauts see also https://sourceforge.net/forum/message.php?msg_id=4705274
specifier|private
name|DictionaryEncoding
name|encodingFromAFM
parameter_list|(
name|FontMetrics
name|metrics
parameter_list|)
throws|throws
name|IOException
block|{
name|Type1Encoding
name|encoding
init|=
operator|new
name|Type1Encoding
argument_list|(
name|metrics
argument_list|)
decl_stmt|;
name|COSArray
name|differences
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|differences
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
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
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|differences
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|encoding
operator|.
name|getName
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// my AFMPFB-Fonts has no character-codes for german umlauts
comment|// so that I've to add them here by hand
name|differences
operator|.
name|set
argument_list|(
literal|0337
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"germandbls"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0344
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"adieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0366
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"odieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0374
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"udieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0304
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Adieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0326
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Odieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|set
argument_list|(
literal|0334
operator|+
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Udieresis"
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|DictionaryEncoding
argument_list|(
name|COSName
operator|.
name|STANDARD_ENCODING
argument_list|,
name|differences
argument_list|)
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
comment|/**      * Returns the font's metrics.      */
specifier|public
name|FontMetrics
name|getFontMetrics
parameter_list|()
block|{
return|return
name|metrics
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

