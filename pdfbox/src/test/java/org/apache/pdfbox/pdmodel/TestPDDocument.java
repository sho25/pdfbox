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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Testcase introduced with PDFBOX-1581.  *   */
end_comment

begin_class
specifier|public
class|class
name|TestPDDocument
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|testResultsDir
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testResultsDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test document save/load using a stream.      */
specifier|public
name|void
name|testSaveLoadStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create PDF with one blank page
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Verify content
name|byte
index|[]
name|pdf
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|pdf
operator|.
name|length
operator|>
literal|200
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%PDF-1.4"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%%EOF\n"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
name|pdf
operator|.
name|length
operator|-
literal|6
argument_list|,
name|pdf
operator|.
name|length
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Load
name|PDDocument
name|loadDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|pdf
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|loadDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test document save/load using a file.      */
specifier|public
name|void
name|testSaveLoadFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create PDF with one blank page
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"pddocument-saveloadfile.pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|targetFile
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Verify content
name|assertTrue
argument_list|(
name|targetFile
operator|.
name|length
argument_list|()
operator|>
literal|200
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
name|byte
index|[]
name|pdf
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|pdf
operator|.
name|length
operator|>
literal|200
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%PDF-1.4"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%%EOF\n"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
name|pdf
operator|.
name|length
operator|-
literal|6
argument_list|,
name|pdf
operator|.
name|length
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Load
name|PDDocument
name|loadDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|loadDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test document save/loadNonSeq using a stream.      */
specifier|public
name|void
name|testSaveLoadNonSeqStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create PDF with one blank page
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Verify content
name|byte
index|[]
name|pdf
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|pdf
operator|.
name|length
operator|>
literal|200
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%PDF-1.4"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%%EOF\n"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
name|pdf
operator|.
name|length
operator|-
literal|6
argument_list|,
name|pdf
operator|.
name|length
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Load
name|PDDocument
name|loadDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|pdf
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|loadDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test document save/loadNonSeq using a file.      */
specifier|public
name|void
name|testSaveLoadNonSeqFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create PDF with one blank page
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"pddocument-saveloadnonseqfile.pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|targetFile
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Verify content
name|assertTrue
argument_list|(
name|targetFile
operator|.
name|length
argument_list|()
operator|>
literal|200
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
name|byte
index|[]
name|pdf
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|pdf
operator|.
name|length
operator|>
literal|200
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%PDF-1.4"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%%EOF\n"
argument_list|,
operator|new
name|String
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|pdf
argument_list|,
name|pdf
operator|.
name|length
operator|-
literal|6
argument_list|,
name|pdf
operator|.
name|length
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Load
name|PDDocument
name|loadDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|loadDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

