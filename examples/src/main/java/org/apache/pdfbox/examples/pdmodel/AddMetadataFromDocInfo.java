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
name|xmpbox
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
name|xmpbox
operator|.
name|schema
operator|.
name|AdobePDFSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializer
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
name|IOException
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_comment
comment|/**  * This is an example on how to add metadata to a document.  *  * Usage: java org.apache.pdfbox.examples.pdmodel.AddMetadataToDocument&lt;input-pdf&gt;&lt;output-pdf&gt;  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AddMetadataFromDocInfo
block|{
specifier|private
name|AddMetadataFromDocInfo
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      * @throws TransformerException      */
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
name|IOException
throws|,
name|TransformerException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|usage
argument_list|()
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
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Cannot add metadata to encrypted document."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentInformation
name|info
init|=
name|document
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|XMPMetadata
name|metadata
init|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
decl_stmt|;
name|AdobePDFSchema
name|pdfSchema
init|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
name|pdfSchema
operator|.
name|setKeywords
argument_list|(
name|info
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|pdfSchema
operator|.
name|setProducer
argument_list|(
name|info
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|XMPBasicSchema
name|basicSchema
init|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
decl_stmt|;
name|basicSchema
operator|.
name|setModifyDate
argument_list|(
name|info
operator|.
name|getModificationDate
argument_list|()
argument_list|)
expr_stmt|;
name|basicSchema
operator|.
name|setCreateDate
argument_list|(
name|info
operator|.
name|getCreationDate
argument_list|()
argument_list|)
expr_stmt|;
name|basicSchema
operator|.
name|setCreatorTool
argument_list|(
name|info
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|basicSchema
operator|.
name|setMetadataDate
argument_list|(
operator|new
name|GregorianCalendar
argument_list|()
argument_list|)
expr_stmt|;
name|DublinCoreSchema
name|dcSchema
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|dcSchema
operator|.
name|setTitle
argument_list|(
name|info
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|dcSchema
operator|.
name|addCreator
argument_list|(
literal|"PDFBox"
argument_list|)
expr_stmt|;
name|dcSchema
operator|.
name|setDescription
argument_list|(
name|info
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|PDMetadata
name|metadataStream
init|=
operator|new
name|PDMetadata
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|catalog
operator|.
name|setMetadata
argument_list|(
name|metadataStream
argument_list|)
expr_stmt|;
name|XmpSerializer
name|serializer
init|=
operator|new
name|XmpSerializer
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|metadata
argument_list|,
name|baos
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|metadataStream
operator|.
name|importXMPMetadata
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
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
comment|/**      * This will print the usage for this document.      */
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.AddMetadataFromDocInfo "
operator|+
literal|"<input-pdf><output-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

