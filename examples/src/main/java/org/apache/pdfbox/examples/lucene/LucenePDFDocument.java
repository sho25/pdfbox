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
name|lucene
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
name|io
operator|.
name|Reader
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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
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
name|Date
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|DateTools
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|FieldType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StringField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|TextField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexOptions
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
name|Loader
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
name|encryption
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
name|text
operator|.
name|PDFTextStripper
import|;
end_import

begin_comment
comment|/**  * This class is used to create a document for the lucene search engine. This should easily plug into the IndexPDFFiles  * that comes with the lucene project. This class will populate the following fields.  *<table>  *<caption></caption>  *<tr>  *<th>Lucene Field Name</th>  *<th>Description</th>  *</tr>  *<tr>  *<td>path</td>  *<td>File system path if loaded from a file</td>  *</tr>  *<tr>  *<td>url</td>  *<td>URL to PDF document</td>  *</tr>  *<tr>  *<td>contents</td>  *<td>Entire contents of PDF document, indexed but not stored</td>  *</tr>  *<tr>  *<td>summary</td>  *<td>First 500 characters of content</td>  *</tr>  *<tr>  *<td>modified</td>  *<td>The modified date/time according to the url or path</td>  *</tr>  *<tr>  *<td>uid</td>  *<td>A unique identifier for the Lucene document.</td>  *</tr>  *<tr>  *<td>CreationDate</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>Creator</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>Keywords</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>ModificationDate</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>Producer</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>Subject</td>  *<td>From PDF meta-data if available</td>  *</tr>  *<tr>  *<td>Trapped</td>  *<td>From PDF meta-data if available</td>  *</tr>  *</table>  *   * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|LucenePDFDocument
block|{
specifier|private
specifier|static
specifier|final
name|char
name|FILE_SEPARATOR
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// given caveat of increased search times when using
comment|// MICROSECOND, only use SECOND by default
specifier|private
specifier|static
specifier|final
name|DateTools
operator|.
name|Resolution
name|DATE_TIME_RES
init|=
name|DateTools
operator|.
name|Resolution
operator|.
name|SECOND
decl_stmt|;
specifier|private
name|PDFTextStripper
name|stripper
init|=
literal|null
decl_stmt|;
comment|/** not Indexed, tokenized, stored. */
specifier|public
specifier|static
specifier|final
name|FieldType
name|TYPE_STORED_NOT_INDEXED
init|=
operator|new
name|FieldType
argument_list|()
decl_stmt|;
static|static
block|{
name|TYPE_STORED_NOT_INDEXED
operator|.
name|setIndexOptions
argument_list|(
name|IndexOptions
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|TYPE_STORED_NOT_INDEXED
operator|.
name|setStored
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|TYPE_STORED_NOT_INDEXED
operator|.
name|setTokenized
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|TYPE_STORED_NOT_INDEXED
operator|.
name|freeze
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      */
specifier|public
name|LucenePDFDocument
parameter_list|()
block|{     }
comment|/**      * Set the text stripper that will be used during extraction.      *       * @param aStripper The new pdf text stripper.      */
specifier|public
name|void
name|setTextStripper
parameter_list|(
name|PDFTextStripper
name|aStripper
parameter_list|)
block|{
name|stripper
operator|=
name|aStripper
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|timeToString
parameter_list|(
name|long
name|time
parameter_list|)
block|{
return|return
name|DateTools
operator|.
name|timeToString
argument_list|(
name|time
argument_list|,
name|DATE_TIME_RES
argument_list|)
return|;
block|}
specifier|private
name|void
name|addKeywordField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|String
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
name|document
operator|.
name|add
argument_list|(
operator|new
name|StringField
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addTextField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|Reader
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
name|document
operator|.
name|add
argument_list|(
operator|new
name|TextField
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addTextField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|String
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
name|document
operator|.
name|add
argument_list|(
operator|new
name|TextField
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addTextField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|Date
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
name|addTextField
argument_list|(
name|document
argument_list|,
name|name
argument_list|,
name|DateTools
operator|.
name|dateToString
argument_list|(
name|value
argument_list|,
name|DATE_TIME_RES
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addTextField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|Calendar
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
name|addTextField
argument_list|(
name|document
argument_list|,
name|name
argument_list|,
name|value
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|addUnindexedField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|String
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
name|document
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|TYPE_STORED_NOT_INDEXED
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addUnstoredKeywordField
parameter_list|(
name|Document
name|document
parameter_list|,
name|String
name|name
parameter_list|,
name|String
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
name|document
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|TextField
operator|.
name|TYPE_NOT_STORED
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Convert the PDF stream to a lucene document.      *       * @param is The input stream.      * @return The input stream converted to a lucene document.      * @throws IOException If there is an error converting the PDF.      */
specifier|public
name|Document
name|convertDocument
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|Document
name|document
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|addContent
argument_list|(
name|document
argument_list|,
name|is
argument_list|,
literal|"<inputstream>"
argument_list|)
expr_stmt|;
return|return
name|document
return|;
block|}
comment|/**      * This will take a reference to a PDF document and create a lucene document.      *       * @param file A reference to a PDF document.      * @return The converted lucene document.      *       * @throws IOException If there is an exception while converting the document.      */
specifier|public
name|Document
name|convertDocument
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|Document
name|document
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
comment|// Add the url as a field named "url". Use an UnIndexed field, so
comment|// that the url is just stored with the document, but is not searchable.
name|addUnindexedField
argument_list|(
name|document
argument_list|,
literal|"path"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|addUnindexedField
argument_list|(
name|document
argument_list|,
literal|"url"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|replace
argument_list|(
name|FILE_SEPARATOR
argument_list|,
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add the last modified date of the file a field named "modified". Use a
comment|// Keyword field, so that it's searchable, but so that no attempt is made
comment|// to tokenize the field into words.
name|addKeywordField
argument_list|(
name|document
argument_list|,
literal|"modified"
argument_list|,
name|timeToString
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|uid
init|=
name|createUID
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// Add the uid as a field, so that index can be incrementally maintained.
comment|// This field is not stored with document, it is indexed, but it is not
comment|// tokenized prior to indexing.
name|addUnstoredKeywordField
argument_list|(
name|document
argument_list|,
literal|"uid"
argument_list|,
name|uid
argument_list|)
expr_stmt|;
try|try
init|(
name|FileInputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|addContent
argument_list|(
name|document
argument_list|,
name|input
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return the document
return|return
name|document
return|;
block|}
comment|/**      * Convert the document from a PDF to a lucene document.      *       * @param url A url to a PDF document.      * @return The PDF converted to a lucene document.      * @throws IOException If there is an error while converting the document.      */
specifier|public
name|Document
name|convertDocument
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|Document
name|document
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
comment|// Add the url as a field named "url". Use an UnIndexed field, so
comment|// that the url is just stored with the document, but is not searchable.
name|addUnindexedField
argument_list|(
name|document
argument_list|,
literal|"url"
argument_list|,
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add the last modified date of the file a field named "modified". Use a
comment|// Keyword field, so that it's searchable, but so that no attempt is made
comment|// to tokenize the field into words.
name|addKeywordField
argument_list|(
name|document
argument_list|,
literal|"modified"
argument_list|,
name|timeToString
argument_list|(
name|connection
operator|.
name|getLastModified
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|uid
init|=
name|createUID
argument_list|(
name|url
argument_list|,
name|connection
operator|.
name|getLastModified
argument_list|()
argument_list|)
decl_stmt|;
comment|// Add the uid as a field, so that index can be incrementally maintained.
comment|// This field is not stored with document, it is indexed, but it is not
comment|// tokenized prior to indexing.
name|addUnstoredKeywordField
argument_list|(
name|document
argument_list|,
literal|"uid"
argument_list|,
name|uid
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|connection
operator|.
name|getInputStream
argument_list|()
init|)
block|{
name|addContent
argument_list|(
name|document
argument_list|,
name|input
argument_list|,
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return the document
return|return
name|document
return|;
block|}
comment|/**      * This will get a lucene document from a PDF file.      *       * @param is The stream to read the PDF from.      *       * @return The lucene document.      *       * @throws IOException If there is an error parsing or indexing the document.      */
specifier|public
specifier|static
name|Document
name|getDocument
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|LucenePDFDocument
name|converter
init|=
operator|new
name|LucenePDFDocument
argument_list|()
decl_stmt|;
return|return
name|converter
operator|.
name|convertDocument
argument_list|(
name|is
argument_list|)
return|;
block|}
comment|/**      * This will get a lucene document from a PDF file.      *       * @param file The file to get the document for.      *       * @return The lucene document.      *       * @throws IOException If there is an error parsing or indexing the document.      */
specifier|public
specifier|static
name|Document
name|getDocument
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|LucenePDFDocument
name|converter
init|=
operator|new
name|LucenePDFDocument
argument_list|()
decl_stmt|;
return|return
name|converter
operator|.
name|convertDocument
argument_list|(
name|file
argument_list|)
return|;
block|}
comment|/**      * This will get a lucene document from a PDF file.      *       * @param url The file to get the document for.      *       * @return The lucene document.      *       * @throws IOException If there is an error parsing or indexing the document.      */
specifier|public
specifier|static
name|Document
name|getDocument
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|LucenePDFDocument
name|converter
init|=
operator|new
name|LucenePDFDocument
argument_list|()
decl_stmt|;
return|return
name|converter
operator|.
name|convertDocument
argument_list|(
name|url
argument_list|)
return|;
block|}
comment|/**      * This will add the contents to the lucene document.      *       * @param document The document to add the contents to.      * @param is The stream to get the contents from.      * @param documentLocation The location of the document, used just for debug messages.      *       * @throws IOException If there is an error parsing the document.      */
specifier|private
name|void
name|addContent
parameter_list|(
name|Document
name|document
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|String
name|documentLocation
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|pdfDocument
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
name|is
argument_list|)
init|)
block|{
comment|// create a writer where to append the text content.
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
if|if
condition|(
name|stripper
operator|==
literal|null
condition|)
block|{
name|stripper
operator|=
operator|new
name|PDFTextStripper
argument_list|()
expr_stmt|;
block|}
name|stripper
operator|.
name|writeText
argument_list|(
name|pdfDocument
argument_list|,
name|writer
argument_list|)
expr_stmt|;
comment|// Note: the buffer to string operation is costless;
comment|// the char array value of the writer buffer and the content string
comment|// is shared as long as the buffer content is not modified, which will
comment|// not occur here.
name|String
name|contents
init|=
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|contents
argument_list|)
decl_stmt|;
comment|// Add the tag-stripped contents as a Reader-valued Text field so it will
comment|// get tokenized and indexed.
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"contents"
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|PDDocumentInformation
name|info
init|=
name|pdfDocument
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Author"
argument_list|,
name|info
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"CreationDate"
argument_list|,
name|info
operator|.
name|getCreationDate
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Creator"
argument_list|,
name|info
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Keywords"
argument_list|,
name|info
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"ModificationDate"
argument_list|,
name|info
operator|.
name|getModificationDate
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Producer"
argument_list|,
name|info
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Subject"
argument_list|,
name|info
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Title"
argument_list|,
name|info
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|addTextField
argument_list|(
name|document
argument_list|,
literal|"Trapped"
argument_list|,
name|info
operator|.
name|getTrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|summarySize
init|=
name|Math
operator|.
name|min
argument_list|(
name|contents
operator|.
name|length
argument_list|()
argument_list|,
literal|500
argument_list|)
decl_stmt|;
name|String
name|summary
init|=
name|contents
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|summarySize
argument_list|)
decl_stmt|;
comment|// Add the summary as an UnIndexed field, so that it is stored and returned
comment|// with hit documents for display.
name|addUnindexedField
argument_list|(
name|document
argument_list|,
literal|"summary"
argument_list|,
name|summary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
comment|// they didn't suppply a password and the default of "" was wrong.
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: The document("
operator|+
name|documentLocation
operator|+
literal|") is encrypted and will not be indexed."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create an UID for the given file using the given time.      *       * @param url the file we have to create an UID for      * @param time the time to used to the UID      *       * @return the created UID      */
specifier|public
specifier|static
name|String
name|createUID
parameter_list|(
name|URL
name|url
parameter_list|,
name|long
name|time
parameter_list|)
block|{
return|return
name|url
operator|.
name|toExternalForm
argument_list|()
operator|.
name|replace
argument_list|(
name|FILE_SEPARATOR
argument_list|,
literal|'\u0000'
argument_list|)
operator|+
literal|"\u0000"
operator|+
name|timeToString
argument_list|(
name|time
argument_list|)
return|;
block|}
comment|/**      * Create an UID for the given file.      *       * @param file the file we have to create an UID for      *       * @return the created UID      */
specifier|public
specifier|static
name|String
name|createUID
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|replace
argument_list|(
name|FILE_SEPARATOR
argument_list|,
literal|'\u0000'
argument_list|)
operator|+
literal|"\u0000"
operator|+
name|timeToString
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

