begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|tika
package|;
end_package

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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXTransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|TransformerHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|tika
operator|.
name|metadata
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|AutoDetectParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|ParseContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tika
operator|.
name|sax
operator|.
name|BodyContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_comment
comment|/**  * Test case for parsing pdf files.  */
end_comment

begin_class
specifier|public
class|class
name|PDFParserTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testPdfParsing
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDF.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|assertEquals
argument_list|(
literal|"application/pdf"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bertrand Delacr\u00e9taz"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|AUTHOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Apache Tika - Apache Tika"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|TITLE
argument_list|)
argument_list|)
expr_stmt|;
comment|// Can't reliably test dates yet - see TIKA-451
comment|//        assertEquals("Sat Sep 15 10:02:31 BST 2007", metadata.get(Metadata.CREATION_DATE));
comment|//        assertEquals("Sat Sep 15 10:02:31 BST 2007", metadata.get(Metadata.LAST_MODIFIED));
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"Apache Tika"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"Tika - Content Analysis Toolkit"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"incubator"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"Apache Software Foundation"
argument_list|)
argument_list|)
expr_stmt|;
comment|// testing how the end of one paragraph is separated from start of the next one
name|assertTrue
argument_list|(
literal|"should have word boundary after headline"
argument_list|,
operator|!
name|content
operator|.
name|contains
argument_list|(
literal|"ToolkitApache"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"should have word boundary between paragraphs"
argument_list|,
operator|!
name|content
operator|.
name|contains
argument_list|(
literal|"libraries.Apache"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCustomMetadata
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDF-custommetadata.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|assertEquals
argument_list|(
literal|"application/pdf"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Document author"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|AUTHOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Document title"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|TITLE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Custom Value"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
literal|"Custom Property"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Array Entry 1"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
literal|"Custom Array"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|metadata
operator|.
name|getValues
argument_list|(
literal|"Custom Array"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Array Entry 1"
argument_list|,
name|metadata
operator|.
name|getValues
argument_list|(
literal|"Custom Array"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Array Entry 2"
argument_list|,
name|metadata
operator|.
name|getValues
argument_list|(
literal|"Custom Array"
argument_list|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"Hello World!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFs can be "protected" with the default password. This means      *  they're encrypted (potentially both text and metadata),      *  but we can decrypt them easily.      */
specifier|public
name|void
name|testProtectedPDF
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDF_protected.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|assertEquals
argument_list|(
literal|"application/pdf"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Bank of England"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|AUTHOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Speeches by Andrew G Haldane"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Rethinking the Financial Network, Speech by Andrew G Haldane, Executive Director, Financial Stability delivered at the Financial Student Association, Amsterdam on 28 April 2009"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|TITLE
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"RETHINKING THE FINANCIAL NETWORK"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"On 16 November 2002"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"In many important respects"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTwoTextBoxes
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDFTwoTextBoxes.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|content
operator|.
name|contains
argument_list|(
literal|"Left column line 1 Left column line 2 Right column line 1 Right column line 2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testVarious
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDFVarious.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//content = content.replaceAll("\\s+"," ");
name|assertContains
argument_list|(
literal|"Footnote appears here"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"This is a footnote."
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"This is the header text."
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"This is the footer text."
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is a text box"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Bold"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"italic"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"underline"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"superscript"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"subscript"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is a citation:"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Figure 1 This is a caption for Figure 1"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"(Kramer)"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Row 1 Col 1 Row 1 Col 2 Row 1 Col 3 Row 2 Col 1 Row 2 Col 2 Row 2 Col 3"
argument_list|,
name|content
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Row 1 column 1 Row 2 column 1 Row 1 column 2 Row 2 column 2"
argument_list|,
name|content
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"This is a hyperlink"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is a list:"
argument_list|,
name|content
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<=
literal|3
condition|;
name|row
operator|++
control|)
block|{
comment|//assertContains("·\tBullet " + row, content);
comment|//assertContains("\u00b7\tBullet " + row, content);
name|assertContains
argument_list|(
literal|"Bullet "
operator|+
name|row
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
name|assertContains
argument_list|(
literal|"Here is a numbered list:"
argument_list|,
name|content
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<=
literal|3
condition|;
name|row
operator|++
control|)
block|{
comment|//assertContains(row + ")\tNumber bullet " + row, content);
name|assertContains
argument_list|(
name|row
operator|+
literal|") Number bullet "
operator|+
name|row
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<=
literal|2
condition|;
name|row
operator|++
control|)
block|{
for|for
control|(
name|int
name|col
init|=
literal|1
init|;
name|col
operator|<=
literal|3
condition|;
name|col
operator|++
control|)
block|{
name|assertContains
argument_list|(
literal|"Row "
operator|+
name|row
operator|+
literal|" Col "
operator|+
name|col
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
name|assertContains
argument_list|(
literal|"Keyword1 Keyword2"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Keyword1 Keyword2"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|KEYWORDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Subject is here"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subject is here"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|Metadata
operator|.
name|SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Suddenly some Japanese text:"
argument_list|,
name|content
argument_list|)
expr_stmt|;
comment|// Special version of (GHQ)
name|assertContains
argument_list|(
literal|"\uff08\uff27\uff28\uff31\uff09"
argument_list|,
name|content
argument_list|)
expr_stmt|;
comment|// 6 other characters
name|assertContains
argument_list|(
literal|"\u30be\u30eb\u30b2\u3068\u5c3e\u5d0e\u3001\u6de1\u3005\u3068\u6700\u671f"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"And then some Gothic text:"
argument_list|,
name|content
argument_list|)
expr_stmt|;
comment|// TODO: I saved the word doc as a PDF, but that
comment|// process somehow, apparently lost the gothic
comment|// chars, so we cannot test this here:
comment|//assertContains("\uD800\uDF32\uD800\uDF3f\uD800\uDF44\uD800\uDF39\uD800\uDF43\uD800\uDF3A", content);
block|}
specifier|public
name|void
name|testAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testAnnotations.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"[\\s\u00a0]+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is some text"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is a comment"
argument_list|,
name|content
argument_list|)
expr_stmt|;
comment|// Test w/ annotation text disabled:
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|()
decl_stmt|;
name|pdfParser
operator|.
name|setExtractAnnotationText
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|BodyContentHandler
argument_list|()
expr_stmt|;
name|metadata
operator|=
operator|new
name|Metadata
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|ParseContext
argument_list|()
expr_stmt|;
name|stream
operator|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testAnnotations.pdf"
argument_list|)
expr_stmt|;
try|try
block|{
name|pdfParser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|content
operator|=
name|handler
operator|.
name|toString
argument_list|()
expr_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"[\\s\u00a0]+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Here is some text"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|content
operator|.
name|indexOf
argument_list|(
literal|"Here is a comment"
argument_list|)
argument_list|)
expr_stmt|;
comment|// TIKA-738: make sure no extra</p> tags
name|String
name|xml
init|=
name|getXML
argument_list|(
literal|"testAnnotations.pdf"
argument_list|)
operator|.
name|xml
decl_stmt|;
name|assertEquals
argument_list|(
name|substringCount
argument_list|(
literal|"<p>"
argument_list|,
name|xml
argument_list|)
argument_list|,
name|substringCount
argument_list|(
literal|"</p>"
argument_list|,
name|xml
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|int
name|substringCount
parameter_list|(
name|String
name|needle
parameter_list|,
name|String
name|haystack
parameter_list|)
block|{
name|int
name|upto
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
specifier|final
name|int
name|next
init|=
name|haystack
operator|.
name|indexOf
argument_list|(
name|needle
argument_list|,
name|upto
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|count
operator|++
expr_stmt|;
name|upto
operator|=
name|next
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
specifier|public
name|void
name|testPageNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|getXML
argument_list|(
literal|"testPageNumber.pdf"
argument_list|)
operator|.
name|xml
decl_stmt|;
name|String
name|content
init|=
name|result
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertContains
argument_list|(
literal|"<p>1</p>"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDisableAutoSpace
parameter_list|()
throws|throws
name|Exception
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setEnableAutoSpace
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testExtraSpaces.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"[\\s\u00a0]+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
comment|// Text is correct when autoSpace is off:
name|assertContains
argument_list|(
literal|"Here is some formatted text"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setEnableAutoSpace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|BodyContentHandler
argument_list|()
expr_stmt|;
name|metadata
operator|=
operator|new
name|Metadata
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|ParseContext
argument_list|()
expr_stmt|;
name|stream
operator|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testExtraSpaces.pdf"
argument_list|)
expr_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|content
operator|=
name|handler
operator|.
name|toString
argument_list|()
expr_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"[\\s\u00a0]+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
comment|// Text is correct when autoSpace is off:
comment|// Text has extra spaces when autoSpace is on
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|content
operator|.
name|indexOf
argument_list|(
literal|"Here is some formatted text"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDuplicateOverlappingText
parameter_list|()
throws|throws
name|Exception
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|()
decl_stmt|;
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testOverlappingText.pdf"
argument_list|)
decl_stmt|;
comment|// Default is false (keep overlapping text):
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertContains
argument_list|(
literal|"Text the first timeText the second time"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setSuppressDuplicateOverlappingText
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|BodyContentHandler
argument_list|()
expr_stmt|;
name|metadata
operator|=
operator|new
name|Metadata
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|ParseContext
argument_list|()
expr_stmt|;
name|stream
operator|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testOverlappingText.pdf"
argument_list|)
expr_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|content
operator|=
name|handler
operator|.
name|toString
argument_list|()
expr_stmt|;
comment|// "Text the first" was dedup'd:
name|assertContains
argument_list|(
literal|"Text the first timesecond time"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSortByPosition
parameter_list|()
throws|throws
name|Exception
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setEnableAutoSpace
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ContentHandler
name|handler
init|=
operator|new
name|BodyContentHandler
argument_list|()
decl_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|ParseContext
name|context
init|=
operator|new
name|ParseContext
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDFTwoTextBoxes.pdf"
argument_list|)
decl_stmt|;
comment|// Default is false (do not sort):
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|String
name|content
init|=
name|handler
operator|.
name|toString
argument_list|()
decl_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Left column line 1 Left column line 2 Right column line 1 Right column line 2"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setSortByPosition
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|handler
operator|=
operator|new
name|BodyContentHandler
argument_list|()
expr_stmt|;
name|metadata
operator|=
operator|new
name|Metadata
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|ParseContext
argument_list|()
expr_stmt|;
name|stream
operator|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDFTwoTextBoxes.pdf"
argument_list|)
expr_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
name|context
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
name|content
operator|=
name|handler
operator|.
name|toString
argument_list|()
expr_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
comment|// Column text is now interleaved:
name|assertContains
argument_list|(
literal|"Left column line 1 Right column line 1 Left colu mn line 2 Right column line 2"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|XMLResult
block|{
specifier|public
specifier|final
name|String
name|xml
decl_stmt|;
specifier|public
specifier|final
name|Metadata
name|metadata
decl_stmt|;
specifier|public
name|XMLResult
parameter_list|(
name|String
name|xml
parameter_list|,
name|Metadata
name|metadata
parameter_list|)
block|{
name|this
operator|.
name|xml
operator|=
name|xml
expr_stmt|;
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
block|}
block|}
specifier|private
name|XMLResult
name|getXML
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|Exception
block|{
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|Parser
name|parser
init|=
operator|new
name|AutoDetectParser
argument_list|()
decl_stmt|;
comment|// Should auto-detect!
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|SAXTransformerFactory
name|factory
init|=
operator|(
name|SAXTransformerFactory
operator|)
name|SAXTransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|TransformerHandler
name|handler
init|=
name|factory
operator|.
name|newTransformerHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
name|handler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setResult
argument_list|(
operator|new
name|StreamResult
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
comment|// Try with a document containing various tables and formattings
name|InputStream
name|input
init|=
name|PDFParserTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|filename
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|input
argument_list|,
name|handler
argument_list|,
name|metadata
argument_list|,
operator|new
name|ParseContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|XMLResult
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|,
name|metadata
argument_list|)
return|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertContains
parameter_list|(
name|String
name|needle
parameter_list|,
name|String
name|haystack
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"\""
operator|+
name|needle
operator|+
literal|"\" not found in \""
operator|+
name|haystack
operator|+
literal|"\""
argument_list|,
name|haystack
operator|.
name|contains
argument_list|(
name|needle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

