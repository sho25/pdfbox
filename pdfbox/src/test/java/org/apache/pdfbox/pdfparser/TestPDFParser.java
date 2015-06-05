begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdfparser
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|FilenameFilter
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|io
operator|.
name|RandomAccessBufferedFileInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
specifier|public
class|class
name|TestPDFParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_OF_PDF
init|=
literal|"src/test/resources/input/yaddatest.pdf"
decl_stmt|;
specifier|private
specifier|static
name|File
name|tmpDirectory
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|int
name|numberOfTmpFiles
init|=
literal|0
decl_stmt|;
comment|/**      * Initialize the number of tmp file before the test      *       * @throws Exception      */
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|numberOfTmpFiles
operator|=
name|getNumberOfTempFile
argument_list|()
expr_stmt|;
block|}
comment|/**      * Count the number of temporary files      *       * @return      */
specifier|private
name|int
name|getNumberOfTempFile
parameter_list|()
block|{
name|int
name|result
init|=
literal|0
decl_stmt|;
name|File
index|[]
name|tmpPdfs
init|=
name|tmpDirectory
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|COSParser
operator|.
name|TMP_FILE_PREFIX
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|"pdf"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpPdfs
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|tmpPdfs
operator|.
name|length
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserFile
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|executeParserTest
argument_list|(
name|pdfParser
argument_list|)
expr_stmt|;
name|pdfParser
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|executeParserTest
argument_list|(
name|pdfParser
argument_list|)
expr_stmt|;
name|pdfParser
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserFileScratchFile
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|executeParserTest
argument_list|(
name|pdfParser
argument_list|)
expr_stmt|;
name|pdfParser
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserInputStreamScratchFile
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|executeParserTest
argument_list|(
name|pdfParser
argument_list|)
expr_stmt|;
name|pdfParser
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|executeParserTest
parameter_list|(
name|PDFParser
name|pdfParser
parameter_list|)
throws|throws
name|IOException
block|{
name|pdfParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|pdfParser
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
comment|// number tmp file must be the same
name|assertEquals
argument_list|(
name|numberOfTmpFiles
argument_list|,
name|getNumberOfTempFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

