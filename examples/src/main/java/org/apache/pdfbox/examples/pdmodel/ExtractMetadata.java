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
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
operator|.
name|XMPSchemaBasic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
operator|.
name|XMPSchemaDublinCore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
operator|.
name|XMPSchemaPDF
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
name|PDDocumentInformation
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
name|PDMetadata
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
name|encryption
operator|.
name|InvalidPasswordException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|encryption
operator|.
name|StandardDecryptionMaterial
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_comment
comment|/**  * This is an example on how to extract metadata from a PDF document.  *<p>  * Usage: java org.apache.pdfbox.examples.pdmodel.ExtractDocument&lt;input-pdf&gt;  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|ExtractMetadata
block|{
specifier|private
name|ExtractMetadata
parameter_list|()
block|{
comment|//utility class
block|}
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
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|args
index|[
literal|0
index|]
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
name|StandardDecryptionMaterial
name|sdm
init|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|document
operator|.
name|openProtection
argument_list|(
name|sdm
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
block|}
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDMetadata
name|meta
init|=
name|catalog
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|meta
operator|!=
literal|null
condition|)
block|{
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|documentBuilder
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|xmpDocument
init|=
name|documentBuilder
operator|.
name|parse
argument_list|(
name|meta
operator|.
name|createInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|XMPMetadata
name|metadata
init|=
operator|new
name|XMPMetadata
argument_list|(
name|xmpDocument
argument_list|)
decl_stmt|;
name|XMPSchemaDublinCore
name|dc
init|=
name|metadata
operator|.
name|getDublinCoreSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|dc
operator|!=
literal|null
condition|)
block|{
name|display
argument_list|(
literal|"Title:"
argument_list|,
name|dc
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Description:"
argument_list|,
name|dc
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|list
argument_list|(
literal|"Creators: "
argument_list|,
name|dc
operator|.
name|getCreators
argument_list|()
argument_list|)
expr_stmt|;
name|list
argument_list|(
literal|"Dates:"
argument_list|,
name|dc
operator|.
name|getDates
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|XMPSchemaPDF
name|pdf
init|=
name|metadata
operator|.
name|getPDFSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|pdf
operator|!=
literal|null
condition|)
block|{
name|display
argument_list|(
literal|"Keywords:"
argument_list|,
name|pdf
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"PDF Version:"
argument_list|,
name|pdf
operator|.
name|getPDFVersion
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"PDF Producer:"
argument_list|,
name|pdf
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|XMPSchemaBasic
name|basic
init|=
name|metadata
operator|.
name|getBasicSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|basic
operator|!=
literal|null
condition|)
block|{
name|display
argument_list|(
literal|"Create Date:"
argument_list|,
name|basic
operator|.
name|getCreateDate
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Modify Date:"
argument_list|,
name|basic
operator|.
name|getModifyDate
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Creator Tool:"
argument_list|,
name|basic
operator|.
name|getCreatorTool
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// The pdf doesn't contain any metadata, try to use the document information instead
name|PDDocumentInformation
name|information
init|=
name|document
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
if|if
condition|(
name|information
operator|!=
literal|null
condition|)
block|{
name|showDocumentInformation
argument_list|(
name|information
argument_list|)
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
specifier|private
specifier|static
name|void
name|showDocumentInformation
parameter_list|(
name|PDDocumentInformation
name|information
parameter_list|)
block|{
name|display
argument_list|(
literal|"Title:"
argument_list|,
name|information
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Subject:"
argument_list|,
name|information
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Author:"
argument_list|,
name|information
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Creator:"
argument_list|,
name|information
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
literal|"Producer:"
argument_list|,
name|information
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|list
parameter_list|(
name|String
name|title
parameter_list|,
name|List
name|list
parameter_list|)
block|{
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|Iterator
name|iter
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|o
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|format
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|format
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Calendar
condition|)
block|{
name|Calendar
name|cal
init|=
operator|(
name|Calendar
operator|)
name|o
decl_stmt|;
return|return
name|DateFormat
operator|.
name|getDateInstance
argument_list|()
operator|.
name|format
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|display
parameter_list|(
name|String
name|title
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|title
operator|+
literal|" "
operator|+
name|format
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
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
name|ExtractMetadata
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

