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
name|common
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
name|UnsupportedEncodingException
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
name|PDDocumentNameDictionary
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
name|PDEmbeddedFilesNameTreeNode
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
name|filespecification
operator|.
name|PDComplexFileSpecification
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
name|filespecification
operator|.
name|PDEmbeddedFile
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|TestEmbeddedFiles
extends|extends
name|TestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNullEmbeddedFile
parameter_list|()
throws|throws
name|IOException
block|{
name|PDEmbeddedFile
name|embeddedFile
init|=
literal|null
decl_stmt|;
name|boolean
name|ok
init|=
literal|false
decl_stmt|;
try|try
block|{
name|PDDocument
name|doc
init|=
name|PDFParser
operator|.
name|load
argument_list|(
name|TestEmbeddedFiles
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"null_PDComplexFileSpecification.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentNameDictionary
name|names
init|=
name|catalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"expected two files"
argument_list|,
literal|2
argument_list|,
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
operator|.
name|getNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PDEmbeddedFilesNameTreeNode
name|embeddedFiles
init|=
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
name|PDComplexFileSpecification
name|spec
init|=
name|embeddedFiles
operator|.
name|getNames
argument_list|()
operator|.
name|get
argument_list|(
literal|"non-existent-file.docx"
argument_list|)
decl_stmt|;
if|if
condition|(
name|spec
operator|!=
literal|null
condition|)
block|{
name|embeddedFile
operator|=
name|spec
operator|.
name|getEmbeddedFile
argument_list|()
expr_stmt|;
name|ok
operator|=
literal|true
expr_stmt|;
block|}
comment|//now test for actual attachment
name|spec
operator|=
name|embeddedFiles
operator|.
name|getNames
argument_list|()
operator|.
name|get
argument_list|(
literal|"My first attachment"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"one attachment actually exists"
argument_list|,
name|spec
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"existing file length"
argument_list|,
literal|17660
argument_list|,
name|spec
operator|.
name|getEmbeddedFile
argument_list|()
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|spec
operator|=
name|embeddedFiles
operator|.
name|getNames
argument_list|()
operator|.
name|get
argument_list|(
literal|"non-existent-file.docx"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"null pointer exception"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Was able to get file without exception"
argument_list|,
name|ok
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"EmbeddedFile was correctly null"
argument_list|,
name|embeddedFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOSSpecificAttachments
parameter_list|()
throws|throws
name|IOException
block|{
name|PDEmbeddedFile
name|nonOSFile
init|=
literal|null
decl_stmt|;
name|PDEmbeddedFile
name|macFile
init|=
literal|null
decl_stmt|;
name|PDEmbeddedFile
name|dosFile
init|=
literal|null
decl_stmt|;
name|PDEmbeddedFile
name|unixFile
init|=
literal|null
decl_stmt|;
name|PDDocument
name|doc
init|=
name|PDFParser
operator|.
name|load
argument_list|(
name|TestEmbeddedFiles
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"testPDF_multiFormatEmbFiles.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentNameDictionary
name|names
init|=
name|catalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|PDEmbeddedFilesNameTreeNode
name|treeNode
init|=
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|PDComplexFileSpecification
argument_list|>
argument_list|>
name|kids
init|=
name|treeNode
operator|.
name|getKids
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNameTreeNode
argument_list|<
name|PDComplexFileSpecification
argument_list|>
name|kid
range|:
name|kids
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDComplexFileSpecification
argument_list|>
name|tmpNames
init|=
name|kid
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|COSObjectable
name|obj
init|=
name|tmpNames
operator|.
name|get
argument_list|(
literal|"My first attachment"
argument_list|)
decl_stmt|;
name|PDComplexFileSpecification
name|spec
init|=
operator|(
name|PDComplexFileSpecification
operator|)
name|obj
decl_stmt|;
name|nonOSFile
operator|=
name|spec
operator|.
name|getEmbeddedFile
argument_list|()
expr_stmt|;
name|macFile
operator|=
name|spec
operator|.
name|getEmbeddedFileMac
argument_list|()
expr_stmt|;
name|dosFile
operator|=
name|spec
operator|.
name|getEmbeddedFileDos
argument_list|()
expr_stmt|;
name|unixFile
operator|=
name|spec
operator|.
name|getEmbeddedFileUnix
argument_list|()
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"non os specific"
argument_list|,
name|byteArrayContainsLC
argument_list|(
literal|"non os specific"
argument_list|,
name|nonOSFile
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"mac"
argument_list|,
name|byteArrayContainsLC
argument_list|(
literal|"mac embedded"
argument_list|,
name|macFile
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"dos"
argument_list|,
name|byteArrayContainsLC
argument_list|(
literal|"dos embedded"
argument_list|,
name|dosFile
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unix"
argument_list|,
name|byteArrayContainsLC
argument_list|(
literal|"unix embedded"
argument_list|,
name|unixFile
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|byteArrayContainsLC
parameter_list|(
name|String
name|target
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
return|return
name|s
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
name|target
argument_list|)
return|;
block|}
block|}
end_class

end_unit

