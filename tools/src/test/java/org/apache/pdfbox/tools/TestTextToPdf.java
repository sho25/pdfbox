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
name|tools
package|;
end_package

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
name|StringReader
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
name|PDDocumentCatalog
import|;
end_import

begin_comment
comment|/**  * Test suite for TextToPDF.  */
end_comment

begin_class
specifier|public
class|class
name|TestTextToPdf
extends|extends
name|TestCase
block|{
comment|/**      * Test class constructor.      *      * @param name The name of the test class.      *      * @throws IOException If there is an error creating the test.      */
specifier|public
name|TestTextToPdf
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test ensures that a PDF created from an empty String is still readable by Adobe Reader      */
specifier|public
name|void
name|testCreateEmptyPdf
parameter_list|()
throws|throws
name|Exception
block|{
name|TextToPDF
name|pdfCreator
init|=
operator|new
name|TextToPDF
argument_list|()
decl_stmt|;
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|PDDocument
name|pdfDoc
init|=
name|pdfCreator
operator|.
name|createPDFFromText
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// In order for the PDF document to be openable by Adobe Reader, it needs
comment|// to have some pages in it. So we'll check that.
name|int
name|pageCount
init|=
name|pdfDoc
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"All Pages was unexpectedly zero."
argument_list|,
name|pageCount
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong number of pages."
argument_list|,
literal|1
argument_list|,
name|pageCount
argument_list|)
expr_stmt|;
name|pdfDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Set the tests in the suite for this test class.      *      * @return the Suite.      */
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
name|TestTextToPdf
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Command line execution.      *      * @param args Command line arguments.      */
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
name|TestTextToPdf
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
block|}
end_class

end_unit

