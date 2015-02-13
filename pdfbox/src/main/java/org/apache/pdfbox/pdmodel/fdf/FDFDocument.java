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
name|fdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|COSDictionary
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
name|COSDocument
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
name|pdfparser
operator|.
name|FDFParser
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
name|pdfwriter
operator|.
name|COSWriter
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * This is the in-memory representation of the FDF document.  You need to call  * close() on this object when you are done using it!!  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|FDFDocument
implements|implements
name|Closeable
block|{
specifier|private
name|COSDocument
name|document
decl_stmt|;
comment|/**      * Constructor, creates a new FDF document.      *      * @throws IOException If there is an error creating this document.      */
specifier|public
name|FDFDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|=
operator|new
name|COSDocument
argument_list|()
expr_stmt|;
name|document
operator|.
name|setVersion
argument_list|(
literal|1.2f
argument_list|)
expr_stmt|;
comment|//First we need a trailer
name|document
operator|.
name|setTrailer
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
expr_stmt|;
comment|//Next we need the root dictionary.
name|FDFCatalog
name|catalog
init|=
operator|new
name|FDFCatalog
argument_list|()
decl_stmt|;
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that uses an existing document.  The COSDocument that      * is passed in must be valid.      *      * @param doc The COSDocument that this document wraps.      */
specifier|public
name|FDFDocument
parameter_list|(
name|COSDocument
name|doc
parameter_list|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
block|}
comment|/**      * This will create an FDF document from an XFDF XML document.      *      * @param doc The XML document that contains the XFDF data.      * @throws IOException If there is an error reading from the dom.      */
specifier|public
name|FDFDocument
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|Element
name|xfdf
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|xfdf
operator|.
name|getNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"xfdf"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error while importing xfdf document, "
operator|+
literal|"root should be 'xfdf' and not '"
operator|+
name|xfdf
operator|.
name|getNodeName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|FDFCatalog
name|cat
init|=
operator|new
name|FDFCatalog
argument_list|(
name|xfdf
argument_list|)
decl_stmt|;
name|setCatalog
argument_list|(
name|cat
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will write this element as an XML document.      *      * @param output The stream to write the xml to.      *      * @throws IOException If there is an error writing the XML.      */
specifier|public
name|void
name|writeXML
parameter_list|(
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
literal|"<xfdf xmlns=\"http://ns.adobe.com/xfdf/\" xml:space=\"preserve\">\n"
argument_list|)
expr_stmt|;
name|getCatalog
argument_list|()
operator|.
name|writeXML
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
literal|"</xfdf>\n"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the low level document.      *      * @return The document that this layer sits on top of.      */
specifier|public
name|COSDocument
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
comment|/**      * This will get the FDF Catalog.  This is guaranteed to not return null.      *      * @return The documents /Root dictionary      */
specifier|public
name|FDFCatalog
name|getCatalog
parameter_list|()
block|{
name|FDFCatalog
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|trailer
init|=
name|document
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
name|COSDictionary
name|root
init|=
operator|(
name|COSDictionary
operator|)
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ROOT
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|FDFCatalog
argument_list|()
expr_stmt|;
name|setCatalog
argument_list|(
name|retval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|FDFCatalog
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the FDF catalog for this FDF document.      *      * @param cat The FDF catalog.      */
specifier|public
name|void
name|setCatalog
parameter_list|(
name|FDFCatalog
name|cat
parameter_list|)
block|{
name|COSDictionary
name|trailer
init|=
name|document
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
name|trailer
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ROOT
argument_list|,
name|cat
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will load a document from a file.      *      * @param filename The name of the file to load.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|load
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|FDFParser
name|parser
init|=
operator|new
name|FDFParser
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getFDFDocument
argument_list|()
return|;
block|}
comment|/**      * This will load a document from a file.      *      * @param file The name of the file to load.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|load
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|FDFParser
name|parser
init|=
operator|new
name|FDFParser
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getFDFDocument
argument_list|()
return|;
block|}
comment|/**      * This will load a document from an input stream.      *      * @param input The stream that contains the document.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|load
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|FDFParser
name|parser
init|=
operator|new
name|FDFParser
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getFDFDocument
argument_list|()
return|;
block|}
comment|/**      * This will load a document from a file.      *      * @param filename The name of the file to load.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|loadXFDF
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|loadXFDF
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|filename
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will load a document from a file.      *      * @param file The name of the file to load.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|loadXFDF
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|loadXFDF
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will load a document from an input stream.      *      * @param input The stream that contains the document.      *      * @return The document that was loaded.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|FDFDocument
name|loadXFDF
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|Document
name|doc
init|=
name|XMLUtil
operator|.
name|parse
argument_list|(
name|input
argument_list|)
decl_stmt|;
return|return
operator|new
name|FDFDocument
argument_list|(
name|doc
argument_list|)
return|;
block|}
comment|/**      * This will save this document to the filesystem.      *      * @param fileName The file to save as.      *      * @throws IOException If there is an error saving the document.      */
specifier|public
name|void
name|save
parameter_list|(
name|File
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|save
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will save this document to the filesystem.      *      * @param fileName The file to save as.      *      * @throws IOException If there is an error saving the document.      */
specifier|public
name|void
name|save
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|save
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will save the document to an output stream.      *      * @param output The stream to write to.      *      * @throws IOException If there is an error writing the document.      */
specifier|public
name|void
name|save
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|COSWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|writer
operator|=
operator|new
name|COSWriter
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will save this document to the filesystem.      *      * @param fileName The file to save as.      *      * @throws IOException If there is an error saving the document.      */
specifier|public
name|void
name|saveXFDF
parameter_list|(
name|File
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|saveXFDF
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|fileName
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will save this document to the filesystem.      *      * @param fileName The file to save as.      *      * @throws IOException If there is an error saving the document.      */
specifier|public
name|void
name|saveXFDF
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|saveXFDF
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|fileName
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will save the document to an output stream and close the stream.      *      * @param output The stream to write to.      *      * @throws IOException If there is an error writing the document.      */
specifier|public
name|void
name|saveXFDF
parameter_list|(
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|writeXML
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will close the underlying COSDocument object.      *      * @throws IOException If there is an error releasing resources.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

