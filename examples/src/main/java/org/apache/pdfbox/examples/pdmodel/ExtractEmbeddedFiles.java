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
name|File
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
name|util
operator|.
name|Iterator
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
name|exceptions
operator|.
name|InvalidPasswordException
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

begin_comment
comment|/**  * This is an example on how to extract all embedded files from a PDF document.  *<p>  * Usage: java org.apache.pdfbox.examples.pdmodel.ExtractEmbeddedFiles&lt;input-pdf&gt;  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|ExtractEmbeddedFiles
block|{
specifier|private
name|ExtractEmbeddedFiles
parameter_list|()
block|{     }
comment|/**      * This is the main method.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
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
name|usage
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|String
name|filePath
init|=
name|pdfFile
operator|.
name|getParent
argument_list|()
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
decl_stmt|;
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
try|try
block|{
name|document
operator|.
name|decrypt
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The document is encrypted."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|CryptographyException
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
name|PDDocumentNameDictionary
name|namesDictionary
init|=
operator|new
name|PDDocumentNameDictionary
argument_list|(
name|document
operator|.
name|getDocumentCatalog
argument_list|()
argument_list|)
decl_stmt|;
name|PDEmbeddedFilesNameTreeNode
name|efTree
init|=
name|namesDictionary
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|efTree
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|names
init|=
name|efTree
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|namesKeys
init|=
name|names
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|namesKeys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|filename
init|=
name|namesKeys
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDComplexFileSpecification
name|fileSpec
init|=
operator|(
name|PDComplexFileSpecification
operator|)
name|names
operator|.
name|get
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|PDEmbeddedFile
name|embeddedFile
init|=
name|fileSpec
operator|.
name|getEmbeddedFile
argument_list|()
decl_stmt|;
name|String
name|embeddedFilename
init|=
name|filePath
operator|+
name|filename
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filePath
operator|+
name|filename
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing "
operator|+
name|embeddedFilename
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|embeddedFile
operator|.
name|getByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will print the usage for this program.      */
specifier|private
specifier|static
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
literal|"Usage: java "
operator|+
name|ExtractEmbeddedFiles
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

