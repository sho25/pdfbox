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
name|examples
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|exceptions
operator|.
name|COSVisitorException
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
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|edit
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
name|PDFont
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
name|PDType1Font
import|;
end_import

begin_comment
comment|/**  * This is an example that creates a simple document and embeds a file into it..  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|EmbeddedFiles
block|{
comment|/**      * Constructor.      */
specifier|public
name|EmbeddedFiles
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * create the second sample document from the PDF file format specification.      *      * @param file The file to write the PDF to.      *      * @throws IOException If there is an error writing the data.      * @throws COSVisitorException If there is an error writing the PDF.      */
specifier|public
name|void
name|doIt
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|COSVisitorException
block|{
comment|// the document
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
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
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|100
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"Go to Document->File Attachments to View Embedded Files"
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//embedded files are stored in a named tree
name|PDEmbeddedFilesNameTreeNode
name|efTree
init|=
operator|new
name|PDEmbeddedFilesNameTreeNode
argument_list|()
decl_stmt|;
comment|//first create the file specification, which holds the embedded file
name|PDComplexFileSpecification
name|fs
init|=
operator|new
name|PDComplexFileSpecification
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setFile
argument_list|(
literal|"Test.txt"
argument_list|)
expr_stmt|;
comment|//create a dummy file stream, this would probably normally be a FileInputStream
name|byte
index|[]
name|data
init|=
literal|"This is the contents of the embedded file"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|fakeFile
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|PDEmbeddedFile
name|ef
init|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|doc
argument_list|,
name|fakeFile
argument_list|)
decl_stmt|;
comment|//now lets some of the optional parameters
name|ef
operator|.
name|setSubtype
argument_list|(
literal|"test/plain"
argument_list|)
expr_stmt|;
name|ef
operator|.
name|setSize
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|ef
operator|.
name|setCreationDate
argument_list|(
operator|new
name|GregorianCalendar
argument_list|()
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setEmbeddedFile
argument_list|(
name|ef
argument_list|)
expr_stmt|;
comment|//now add the entry to the embedded file tree and set in the document.
name|efTree
operator|.
name|setNames
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"My first attachment"
argument_list|,
name|fs
argument_list|)
argument_list|)
expr_stmt|;
name|PDDocumentNameDictionary
name|names
init|=
operator|new
name|PDDocumentNameDictionary
argument_list|(
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
argument_list|)
decl_stmt|;
name|names
operator|.
name|setEmbeddedFiles
argument_list|(
name|efTree
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will create a hello world PDF document with an embedded file.      *<br />      * see usage() for commandline      *      * @param args Command line arguments.      */
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
name|EmbeddedFiles
name|app
init|=
operator|new
name|EmbeddedFiles
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|app
operator|.
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|app
operator|.
name|doIt
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"<output-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

