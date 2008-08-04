begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|BufferedInputStream
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

begin_import
import|import
name|org
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
name|fontbox
operator|.
name|afm
operator|.
name|FontMetric
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|encoding
operator|.
name|AFMEncoding
import|;
end_import

begin_import
import|import
name|org
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
comment|/**  * This is implementation of the Type1 Font  * with a afm and a pfb file.  *  * @author<a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDType1AfmPfbFont
extends|extends
name|PDType1Font
block|{
comment|/**      * the buffersize.      */
specifier|private
specifier|static
specifier|final
name|int
name|BUFFERSIZE
init|=
literal|0xffff
decl_stmt|;
comment|/**      * The font descriptor.      */
specifier|private
name|PDFontDescriptorDictionary
name|fd
decl_stmt|;
comment|/**      * The font metric.      */
specifier|private
name|FontMetric
name|metric
decl_stmt|;
comment|/**      * Create a new object.      * @param doc       The PDF document that will hold the embedded font.      * @param afmname   The font filename.      * @throws IOException If there is an error loading the data.      */
specifier|public
name|PDType1AfmPfbFont
parameter_list|(
specifier|final
name|PDDocument
name|doc
parameter_list|,
specifier|final
name|String
name|afmname
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
name|InputStream
name|afmin
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|afmname
argument_list|)
argument_list|,
name|BUFFERSIZE
argument_list|)
decl_stmt|;
name|String
name|pfbname
init|=
name|afmname
operator|.
name|replaceAll
argument_list|(
literal|".AFM"
argument_list|,
literal|""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|".afm"
argument_list|,
literal|""
argument_list|)
operator|+
literal|".pfb"
decl_stmt|;
name|InputStream
name|pfbin
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|pfbname
argument_list|)
argument_list|,
name|BUFFERSIZE
argument_list|)
decl_stmt|;
name|load
argument_list|(
name|doc
argument_list|,
name|afmin
argument_list|,
name|pfbin
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a new object.      * @param doc       The PDF document that will hold the embedded font.      * @param afm       The afm input.      * @param pfb       The pfb input.      * @throws IOException If there is an error loading the data.      */
specifier|public
name|PDType1AfmPfbFont
parameter_list|(
specifier|final
name|PDDocument
name|doc
parameter_list|,
specifier|final
name|InputStream
name|afm
parameter_list|,
specifier|final
name|InputStream
name|pfb
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|doc
argument_list|,
name|afm
argument_list|,
name|pfb
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will load a afm and pfb to be embedding into a document.      *       * @param doc   The PDF document that will hold the embedded font.       * @param afm   The afm input.      * @param pfb   The pfb input.      * @throws IOException If there is an error loading the data.      */
specifier|private
name|void
name|load
parameter_list|(
specifier|final
name|PDDocument
name|doc
parameter_list|,
specifier|final
name|InputStream
name|afm
parameter_list|,
specifier|final
name|InputStream
name|pfb
parameter_list|)
throws|throws
name|IOException
block|{
name|fd
operator|=
operator|new
name|PDFontDescriptorDictionary
argument_list|()
expr_stmt|;
name|setFontDescriptor
argument_list|(
name|fd
argument_list|)
expr_stmt|;
comment|// read the pfb
name|PfbParser
name|pfbparser
init|=
operator|new
name|PfbParser
argument_list|(
name|pfb
argument_list|)
decl_stmt|;
name|pfb
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDStream
name|fontStream
init|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|pfbparser
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
name|pfbparser
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
name|pfbparser
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
name|pfbparser
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
comment|// read the afm
name|AFMParser
name|parser
init|=
operator|new
name|AFMParser
argument_list|(
name|afm
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|metric
operator|=
name|parser
operator|.
name|getResult
argument_list|()
expr_stmt|;
name|setEncoding
argument_list|(
operator|new
name|AFMEncoding
argument_list|(
name|metric
argument_list|)
argument_list|)
expr_stmt|;
comment|// set the values
name|setBaseFont
argument_list|(
name|metric
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontName
argument_list|(
name|metric
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontFamily
argument_list|(
name|metric
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
name|metric
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
name|metric
operator|.
name|getItalicAngle
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setAscent
argument_list|(
name|metric
operator|.
name|getAscender
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setDescent
argument_list|(
name|metric
operator|.
name|getDescender
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setCapHeight
argument_list|(
name|metric
operator|.
name|getCapHeight
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setXHeight
argument_list|(
name|metric
operator|.
name|getXHeight
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setAverageWidth
argument_list|(
name|metric
operator|.
name|getAverageCharacterWidth
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setCharacterSet
argument_list|(
name|metric
operator|.
name|getCharacterSet
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
name|listmetric
init|=
name|metric
operator|.
name|getCharMetrics
argument_list|()
decl_stmt|;
name|int
name|maxWidths
init|=
literal|256
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
literal|0
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|listmetric
operator|.
name|iterator
argument_list|()
decl_stmt|;
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
operator|(
name|CharMetric
operator|)
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
name|float
name|width
init|=
name|m
operator|.
name|getWx
argument_list|()
decl_stmt|;
name|widths
operator|.
name|add
argument_list|(
operator|new
name|Float
argument_list|(
name|width
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widths
operator|.
name|add
argument_list|(
name|zero
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|setFirstChar
argument_list|(
name|firstchar
argument_list|)
expr_stmt|;
name|setLastChar
argument_list|(
name|lastchar
argument_list|)
expr_stmt|;
name|setWidths
argument_list|(
name|widths
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|fd
return|;
block|}
block|}
end_class

end_unit

