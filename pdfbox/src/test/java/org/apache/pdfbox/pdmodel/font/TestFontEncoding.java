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
name|ByteArrayOutputStream
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
name|pdfparser
operator|.
name|PDFParser
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
name|pdmodel
operator|.
name|font
operator|.
name|encoding
operator|.
name|MacRomanEncoding
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
name|text
operator|.
name|PDFTextStripper
import|;
end_import

begin_comment
comment|/**  * Tests font encoding.  *  */
end_comment

begin_class
specifier|public
class|class
name|TestFontEncoding
extends|extends
name|TestCase
block|{
comment|/**      * Test the add method of a font encoding.      */
specifier|public
name|void
name|testAdd
parameter_list|()
throws|throws
name|Exception
block|{
comment|// see PDFDBOX-3332
name|int
name|codeForSpace
init|=
name|WinAnsiEncoding
operator|.
name|INSTANCE
operator|.
name|getNameToCodeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"space"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|32
argument_list|,
name|codeForSpace
argument_list|)
expr_stmt|;
name|codeForSpace
operator|=
name|MacRomanEncoding
operator|.
name|INSTANCE
operator|.
name|getNameToCodeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"space"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|32
argument_list|,
name|codeForSpace
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOverwrite
parameter_list|()
throws|throws
name|Exception
block|{
comment|// see PDFDBOX-3332
name|COSDictionary
name|dictEncodingDict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dictEncodingDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|dictEncodingDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|,
name|COSName
operator|.
name|WIN_ANSI_ENCODING
argument_list|)
expr_stmt|;
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
name|get
argument_list|(
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|differences
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|dictEncodingDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DIFFERENCES
argument_list|,
name|differences
argument_list|)
expr_stmt|;
name|DictionaryEncoding
name|dictEncoding
init|=
operator|new
name|DictionaryEncoding
argument_list|(
name|dictEncodingDict
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|dictEncoding
operator|.
name|getNameToCodeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"space"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|32
argument_list|,
name|dictEncoding
operator|.
name|getNameToCodeMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3826: Some unicodes are reached by several names in glyphlist.txt, e.g. tilde and      * ilde.      *      * @throws IOException      */
specifier|public
name|void
name|testPDFBox3884
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
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
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
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
name|setFont
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|,
literal|20
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
literal|100
argument_list|,
literal|700
argument_list|)
expr_stmt|;
comment|// first tilde is "asciitilde" (from the keyboard), 2nd tilde is "tilde"
comment|// using ˜ would bring IllegalArgumentException prior to bugfix
name|cs
operator|.
name|showText
argument_list|(
literal|"~˜"
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
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// verify
name|doc
operator|=
name|PDFParser
operator|.
name|load
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"~˜"
argument_list|,
name|text
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

