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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|PDPage
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
name|PDPageContentStream
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
name|text
operator|.
name|PDFTextStripper
import|;
end_import

begin_comment
comment|/**  * Tests font embedding.  *  * @author John Hewson  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|TestFontEmbedding
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|File
name|OUT_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * Embed a TTF as CIDFontType2.      */
specifier|public
name|void
name|testCIDFontType2
parameter_list|()
throws|throws
name|Exception
block|{
name|validateCIDFontType2
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Embed a TTF as CIDFontType2 with subsetting.      */
specifier|public
name|void
name|testCIDFontType2Subset
parameter_list|()
throws|throws
name|Exception
block|{
name|validateCIDFontType2
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateCIDFontType2
parameter_list|(
name|boolean
name|useSubset
parameter_list|)
throws|throws
name|Exception
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
name|TestFontEmbedding
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/pdfbox/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
name|PDType0Font
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|document
argument_list|,
name|input
argument_list|,
name|useSubset
argument_list|)
decl_stmt|;
name|PDPageContentStream
name|stream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|stream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|stream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|String
name|text
init|=
literal|"Unicode русский язык Tiếng Việt"
decl_stmt|;
name|stream
operator|.
name|newLineAtOffset
argument_list|(
literal|50
argument_list|,
literal|600
argument_list|)
expr_stmt|;
name|stream
operator|.
name|showText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|stream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|stream
operator|.
name|close
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"CIDFontType2.pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// check that the extracted text matches what we wrote
name|String
name|extracted
init|=
name|getUnicodeText
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|extracted
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Embed a TTF as vertical CIDFontType2 with subsetting.      *       * @throws IOException       */
specifier|public
name|void
name|testCIDFontType2VerticalSubset
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|text
init|=
literal|"「ABC」"
decl_stmt|;
name|String
name|expectedExtractedtext
init|=
literal|"「\nA\nB\nC\n」"
decl_stmt|;
name|File
name|pdf
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"CIDFontType2V.pdf"
argument_list|)
decl_stmt|;
try|try
init|(
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|File
name|ipafont
init|=
operator|new
name|File
argument_list|(
literal|"target/fonts/ipag00303"
argument_list|,
literal|"ipag.ttf"
argument_list|)
decl_stmt|;
name|PDType0Font
name|vfont
init|=
name|PDType0Font
operator|.
name|loadVertical
argument_list|(
name|document
argument_list|,
name|ipafont
argument_list|)
decl_stmt|;
try|try
init|(
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
init|)
block|{
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|vfont
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|newLineAtOffset
argument_list|(
literal|50
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
comment|// Check the font substitution
name|byte
index|[]
name|encode
init|=
name|vfont
operator|.
name|encode
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|int
name|cid
init|=
operator|(
operator|(
name|encode
index|[
literal|0
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|8
operator|)
operator|+
operator|(
name|encode
index|[
literal|1
index|]
operator|&
literal|0xFF
operator|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7392
argument_list|,
name|cid
argument_list|)
expr_stmt|;
comment|// it's 441 without substitution
comment|// Check the dictionaries
name|COSDictionary
name|fontDict
init|=
name|vfont
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|IDENTITY_V
argument_list|,
name|fontDict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|COSDictionary
name|descFontDict
init|=
name|vfont
operator|.
name|getDescendantFont
argument_list|()
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSArray
name|dw2
init|=
operator|(
name|COSArray
operator|)
name|descFontDict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DW2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|880
argument_list|,
name|dw2
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1000
argument_list|,
name|dw2
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|pdf
argument_list|)
expr_stmt|;
block|}
comment|// Check text extraction
name|String
name|extracted
init|=
name|getUnicodeText
argument_list|(
name|pdf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedExtractedtext
argument_list|,
name|extracted
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|""
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getUnicodeText
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
return|return
name|stripper
operator|.
name|getText
argument_list|(
name|document
argument_list|)
return|;
block|}
block|}
end_class

end_unit

