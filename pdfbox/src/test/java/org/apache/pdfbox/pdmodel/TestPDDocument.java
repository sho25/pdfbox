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
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|PrintWriter
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

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|fail
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
specifier|final
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
comment|/**      * Test document save/load using a stream.      * @throws IOException if something went wrong      */
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
comment|/**      * Test document save/load using a file.      * @throws IOException if something went wrong      */
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
comment|/**      * Test document save/loadNonSeq using a stream.      * @throws IOException if something went wrong      */
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
comment|/**      * Test document save/loadNonSeq using a file.      * @throws IOException if something went wrong      */
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
comment|/**      * Test get/setVersion.      * @throws IOException if something went wrong      */
specifier|public
name|void
name|testVersions
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
comment|// test default version
name|assertEquals
argument_list|(
literal|1.4f
argument_list|,
name|document
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.4f
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.4"
argument_list|,
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// force downgrading version (header)
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|setVersion
argument_list|(
literal|1.3f
argument_list|)
expr_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setVersion
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// test new version (header)
name|assertEquals
argument_list|(
literal|1.3f
argument_list|,
name|document
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.3f
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// check if version downgrade is denied
name|document
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|document
operator|.
name|setVersion
argument_list|(
literal|1.3f
argument_list|)
expr_stmt|;
comment|// all versions shall have their default value
name|assertEquals
argument_list|(
literal|1.4f
argument_list|,
name|document
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.4f
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.4"
argument_list|,
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// check version upgrade
name|document
operator|.
name|setVersion
argument_list|(
literal|1.5f
argument_list|)
expr_stmt|;
comment|// overall version has to be 1.5f
name|assertEquals
argument_list|(
literal|1.5f
argument_list|,
name|document
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// header version has to be unchanged
name|assertEquals
argument_list|(
literal|1.4f
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// catalog version version has to be 1.5
name|assertEquals
argument_list|(
literal|"1.5"
argument_list|,
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test whether a bad file can be deleted after load() failed.      *      * @throws java.io.FileNotFoundException      */
specifier|public
name|void
name|testDeleteBadFile
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
literal|"test.pdf"
argument_list|)
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
name|pw
operator|.
name|write
argument_list|(
literal|"<script language='JavaScript'>"
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"parsing should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
finally|finally
block|{
name|assertNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
name|boolean
name|deleted
init|=
name|f
operator|.
name|delete
argument_list|()
decl_stmt|;
comment|//TODO uncomment after bug is fixed!
comment|//assertTrue("delete bad file failed after failed load()", deleted);
block|}
comment|/**      * Test whether a good file can be deleted after load() and close() succeed.      *      * @throws java.io.FileNotFoundException      */
specifier|public
name|void
name|testDeleteGoodFile
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
literal|"test.pdf"
argument_list|)
decl_stmt|;
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDDocument
operator|.
name|load
argument_list|(
name|f
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|boolean
name|deleted
init|=
name|f
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"delete good file failed after successful load() and close()"
argument_list|,
name|deleted
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

