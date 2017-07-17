begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2011 adam.  *   * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayOutputStream
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|rendering
operator|.
name|PDFRenderer
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *   * @author adam  */
end_comment

begin_class
specifier|public
class|class
name|PDFontTest
block|{
comment|/**      * Test of the error reported in PDFBOX-988      *      * @throws IOException      * @throws URISyntaxException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox988
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|PDFontTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"F001u_3_7j.pdf"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|renderer
operator|.
name|renderImage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// the allegation is that renderImage() will crash the JVM or hang
block|}
block|}
comment|/**      * PDFBOX-3747: Test that using "-" with Calibri in Windows 7 has "-" in text extraction and not      * \u2010, which was because of a wrong ToUnicode mapping because prior to the bugfix,      * CmapSubtable#getCharCodes provided values in random order.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3747
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"c:/windows/fonts"
argument_list|,
literal|"calibri.ttf"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"testPDFBox3747 skipped"
argument_list|)
expr_stmt|;
return|return;
block|}
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc
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
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDFont
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|file
argument_list|)
decl_stmt|;
try|try
init|(
name|PDPageContentStream
name|cs
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
init|)
block|{
name|cs
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|cs
operator|.
name|showText
argument_list|(
literal|"PDFBOX-3747"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
name|doc
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
init|)
block|{
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|stripper
operator|.
name|getText
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"PDFBOX-3747"
argument_list|,
name|text
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * PDFBOX-3826: Test ability to reuse a TrueTypeFont created from a file or a stream for several      * PDFs to avoid parsing it over and over again. Also check that full or partial embedding is      * done, and do render and text extraction.      *      * @throws IOException      * @throws URISyntaxException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3826
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|URL
name|url
init|=
name|PDFontTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/pdfbox/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
name|File
name|fontFile
init|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|TrueTypeFont
name|ttf1
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
name|fontFile
argument_list|)
init|)
block|{
name|testPDFBox3826checkFonts
argument_list|(
name|testPDFBox3826createDoc
argument_list|(
name|ttf1
argument_list|)
argument_list|,
name|fontFile
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|TrueTypeFont
name|ttf2
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|fontFile
argument_list|)
argument_list|)
init|)
block|{
name|testPDFBox3826checkFonts
argument_list|(
name|testPDFBox3826createDoc
argument_list|(
name|ttf2
argument_list|)
argument_list|,
name|fontFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testPDFBox3826checkFonts
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|,
name|File
name|fontFile
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|byteArray
argument_list|)
decl_stmt|;
name|PDPage
name|page2
init|=
name|doc
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// F1 = type0 subset
name|PDType0Font
name|fontF1
init|=
operator|(
name|PDType0Font
operator|)
name|page2
operator|.
name|getResources
argument_list|()
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"F1"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|fontF1
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"+"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|fontFile
operator|.
name|length
argument_list|()
operator|>
name|fontF1
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getFontFile2
argument_list|()
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// F2 = type0 full embed
name|PDType0Font
name|fontF2
init|=
operator|(
name|PDType0Font
operator|)
name|page2
operator|.
name|getResources
argument_list|()
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"F2"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|fontF2
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"+"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fontFile
operator|.
name|length
argument_list|()
argument_list|,
name|fontF2
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getFontFile2
argument_list|()
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// F3 = tt full embed
name|PDTrueTypeFont
name|fontF3
init|=
operator|(
name|PDTrueTypeFont
operator|)
name|page2
operator|.
name|getResources
argument_list|()
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"F3"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|fontF2
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"+"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fontFile
operator|.
name|length
argument_list|()
argument_list|,
name|fontF3
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getFontFile2
argument_list|()
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
operator|new
name|PDFRenderer
argument_list|(
name|doc
argument_list|)
operator|.
name|renderImage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|stripper
operator|.
name|setLineSeparator
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|stripper
operator|.
name|getText
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"testMultipleFontFileReuse1\ntestMultipleFontFileReuse2\ntestMultipleFontFileReuse3\n"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|byte
index|[]
name|testPDFBox3826createDoc
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc
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
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
comment|// type 0 subset embedding
name|PDFont
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|ttf
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PDPageContentStream
name|cs
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|cs
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|newLineAtOffset
argument_list|(
literal|10
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|cs
operator|.
name|showText
argument_list|(
literal|"testMultipleFontFileReuse1"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|endText
argument_list|()
expr_stmt|;
comment|// type 0 full embedding
name|font
operator|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|ttf
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cs
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|newLineAtOffset
argument_list|(
literal|10
argument_list|,
literal|650
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|cs
operator|.
name|showText
argument_list|(
literal|"testMultipleFontFileReuse2"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|endText
argument_list|()
expr_stmt|;
comment|// tt full embedding but only WinAnsiEncoding
name|font
operator|=
name|PDTrueTypeFont
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|ttf
argument_list|,
name|WinAnsiEncoding
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|cs
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|newLineAtOffset
argument_list|(
literal|10
argument_list|,
literal|600
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|cs
operator|.
name|showText
argument_list|(
literal|"testMultipleFontFileReuse3"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|endText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|close
argument_list|()
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

