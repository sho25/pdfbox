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
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
name|cos
operator|.
name|COSString
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
name|PDFStreamParser
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
name|COSObjectable
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
name|fdf
operator|.
name|FDFDocument
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAppearanceStream
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
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
name|interactive
operator|.
name|form
operator|.
name|PDAcroForm
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
name|interactive
operator|.
name|form
operator|.
name|PDFieldTreeNode
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
name|interactive
operator|.
name|form
operator|.
name|PDRadioButton
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
name|interactive
operator|.
name|form
operator|.
name|PDTextField
import|;
end_import

begin_comment
comment|/**  * This will test the FDF algorithms in PDFBox.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|TestFDF
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PDF_FDEB
init|=
literal|"target/test-input-ext/fdeb.pdf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PDF_LOTSOFFIELDS
init|=
literal|"target/test-input-ext/pdf_with_lots_of_fields.pdf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PDF_FREEDOM
init|=
literal|"target/test-input-ext/FreedomExpressions.pdf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FDF_FREEDOM
init|=
literal|"target/test-input-ext/FreedomExpressions.fdf"
decl_stmt|;
comment|/**      * Constructor.      *      * @param name The name of the test to run.      */
specifier|public
name|TestFDF
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suite of test that this class holds.      *      * @return All of the tests that this class holds.      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestSuite
argument_list|(
name|TestFDF
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * infamous main method.      *      * @param args The command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
index|[]
name|arg
init|=
block|{
name|TestFDF
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
name|junit
operator|.
name|textui
operator|.
name|TestRunner
operator|.
name|main
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will test some simple field setting.      *      * @throws Exception If there is an exception while encrypting.      */
specifier|public
name|void
name|testFDFfdeb
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|filePDF
init|=
operator|new
name|File
argument_list|(
name|PDF_FDEB
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePDF
operator|.
name|exists
argument_list|()
condition|)
block|{
name|PDDocument
name|fdeb
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fdeb
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|filePDF
argument_list|)
expr_stmt|;
name|PDAcroForm
name|form
init|=
name|fdeb
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"f67_1"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"/Tx BMC "
operator|+
literal|"BT "
operator|+
literal|"/Helv 9 Tf "
operator|+
literal|" 0 g "
operator|+
literal|" 2 1.985585 Td "
operator|+
literal|"2.07698 0 Td "
operator|+
literal|"(2) Tj "
operator|+
literal|"ET "
operator|+
literal|"EMC"
decl_stmt|;
name|testContentStreams
argument_list|(
name|fdeb
argument_list|,
name|field
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fdeb
operator|!=
literal|null
condition|)
block|{
name|fdeb
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will test a pdf with lots of fields.      *      * @throws Exception If there is an exception while encrypting.      */
specifier|public
name|void
name|testFDFPDFWithLotsOfFields
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|filePDF
init|=
operator|new
name|File
argument_list|(
name|PDF_LOTSOFFIELDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePDF
operator|.
name|exists
argument_list|()
condition|)
block|{
name|PDDocument
name|fdeb
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fdeb
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|filePDF
argument_list|)
expr_stmt|;
name|PDAcroForm
name|form
init|=
name|fdeb
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDTextField
name|feld2
init|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"Feld.2"
argument_list|)
decl_stmt|;
name|feld2
operator|.
name|setValue
argument_list|(
literal|"Benjamin"
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"1 1 0.8000000119 rg "
operator|+
literal|" 0 0 127.5 19.8299999237 re "
operator|+
literal|" f "
operator|+
literal|" 0 0 0 RG "
operator|+
literal|" 1 w "
operator|+
literal|" 0.5 0.5 126.5 18.8299999237 re "
operator|+
literal|" S "
operator|+
literal|" 0.5 g "
operator|+
literal|" 1 1 m "
operator|+
literal|" 1 18.8299999237 l "
operator|+
literal|" 126.5 18.8299999237 l "
operator|+
literal|" 125.5 17.8299999237 l "
operator|+
literal|" 2 17.8299999237 l "
operator|+
literal|" 2 2 l "
operator|+
literal|" 1 1 l "
operator|+
literal|" f "
operator|+
literal|" 0.75 g "
operator|+
literal|" 1 1 m "
operator|+
literal|" 126.5 1 l "
operator|+
literal|" 126.5 18.8299999237 l "
operator|+
literal|" 125.5 17.8299999237 l "
operator|+
literal|" 125.5 2 l "
operator|+
literal|" 2 2 l "
operator|+
literal|" 1 1 l "
operator|+
literal|" f "
operator|+
literal|" /Tx BMC  "
operator|+
literal|"BT "
operator|+
literal|"/Helv 14 Tf "
operator|+
literal|" 0 0 0 rg "
operator|+
literal|" 4 4.721 Td "
operator|+
literal|"(Benjamin) Tj "
operator|+
literal|"ET "
operator|+
literal|"EMC"
decl_stmt|;
name|testContentStreams
argument_list|(
name|fdeb
argument_list|,
name|feld2
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|PDRadioButton
name|feld3
init|=
operator|(
name|PDRadioButton
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"Feld.3"
argument_list|)
decl_stmt|;
name|feld3
operator|.
name|setValue
argument_list|(
literal|"RB1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RB1"
argument_list|,
name|feld3
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fdeb
operator|!=
literal|null
condition|)
block|{
name|fdeb
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will test the Freedom pdf.      *      * @throws Exception If there is an error while testing.      */
specifier|public
name|void
name|testFDFFreedomExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|filePDF
init|=
operator|new
name|File
argument_list|(
name|PDF_FREEDOM
argument_list|)
decl_stmt|;
name|File
name|fileFDF
init|=
operator|new
name|File
argument_list|(
name|FDF_FREEDOM
argument_list|)
decl_stmt|;
if|if
condition|(
name|filePDF
operator|.
name|exists
argument_list|()
operator|&&
name|fileFDF
operator|.
name|exists
argument_list|()
condition|)
block|{
name|PDDocument
name|freedom
init|=
literal|null
decl_stmt|;
name|FDFDocument
name|fdf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|freedom
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|filePDF
argument_list|)
expr_stmt|;
name|fdf
operator|=
name|FDFDocument
operator|.
name|load
argument_list|(
name|fileFDF
argument_list|)
expr_stmt|;
name|PDAcroForm
name|form
init|=
name|freedom
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|form
operator|.
name|importFDF
argument_list|(
name|fdf
argument_list|)
expr_stmt|;
name|PDTextField
name|feld2
init|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"eeFirstName"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|COSObjectable
argument_list|>
name|kids
init|=
name|feld2
operator|.
name|getKids
argument_list|()
decl_stmt|;
name|PDFieldTreeNode
name|firstKid
init|=
operator|(
name|PDFieldTreeNode
operator|)
name|kids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDFieldTreeNode
name|secondKid
init|=
operator|(
name|PDFieldTreeNode
operator|)
name|kids
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|testContentStreamContains
argument_list|(
name|freedom
argument_list|,
name|firstKid
argument_list|,
literal|"Steve"
argument_list|)
expr_stmt|;
name|testContentStreamContains
argument_list|(
name|freedom
argument_list|,
name|secondKid
argument_list|,
literal|"Steve"
argument_list|)
expr_stmt|;
comment|//the appearance stream is suppose to be null because there
comment|//is an F action in the AA dictionary that populates that field.
name|PDFieldTreeNode
name|totalAmt
init|=
name|form
operator|.
name|getField
argument_list|(
literal|"eeSuppTotalAmt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|totalAmt
operator|.
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|freedom
operator|!=
literal|null
condition|)
block|{
name|freedom
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fdf
operator|!=
literal|null
condition|)
block|{
name|fdf
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
name|testContentStreamContains
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|PDFieldTreeNode
name|field
parameter_list|,
name|String
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|PDAnnotationWidget
name|widget
init|=
name|field
operator|.
name|getWidget
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|normalAppearance
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|normalAppearance
operator|.
name|get
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|COSStream
name|actual
init|=
name|appearanceStream
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|actualTokens
init|=
name|getStreamTokens
argument_list|(
name|doc
argument_list|,
name|actual
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|actualTokens
operator|.
name|contains
argument_list|(
operator|new
name|COSString
argument_list|(
name|expected
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testContentStreams
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|PDFieldTreeNode
name|field
parameter_list|,
name|String
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|PDAnnotationWidget
name|widget
init|=
name|field
operator|.
name|getWidget
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|normalAppearance
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|normalAppearance
operator|.
name|get
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|COSStream
name|actual
init|=
name|appearanceStream
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|actualTokens
init|=
name|getStreamTokens
argument_list|(
name|doc
argument_list|,
name|actual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|expectedTokens
init|=
name|getStreamTokens
argument_list|(
name|doc
argument_list|,
name|expected
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|actualTokens
operator|.
name|size
argument_list|()
argument_list|,
name|expectedTokens
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
name|actualTokens
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|actualToken
init|=
name|actualTokens
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|expectedToken
init|=
name|expectedTokens
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|actualToken
argument_list|,
name|expectedToken
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|getStreamTokens
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFStreamParser
name|parser
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|string
operator|!=
literal|null
condition|)
block|{
name|ByteArrayInputStream
name|stream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|string
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|parser
operator|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|tokens
operator|=
name|parser
operator|.
name|getTokens
argument_list|()
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|getStreamTokens
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|COSStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFStreamParser
name|parser
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|parser
operator|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|tokens
operator|=
name|parser
operator|.
name|getTokens
argument_list|()
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
block|}
end_class

end_unit

