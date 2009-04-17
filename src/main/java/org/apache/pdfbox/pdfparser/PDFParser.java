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
name|pdfparser
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
name|rmi
operator|.
name|server
operator|.
name|LogStream
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|COSBase
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
name|COSObject
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
name|COSStream
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
name|LoggingObject
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
name|WrappedIOException
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
name|RandomAccess
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
name|fdf
operator|.
name|FDFDocument
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This class will handle the parsing of the PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.53 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFParser
extends|extends
name|BaseParser
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SPACE_BYTE
init|=
literal|32
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PDF_HEADER
init|=
literal|"%PDF-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FDF_HEADER
init|=
literal|"%FDF-"
decl_stmt|;
specifier|private
name|COSDocument
name|document
decl_stmt|;
specifier|private
name|boolean
name|forceParsing
init|=
literal|false
decl_stmt|;
comment|/**      * Temp file directory.      */
specifier|private
name|File
name|tempDirectory
init|=
literal|null
decl_stmt|;
specifier|private
name|RandomAccess
name|raf
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      * @param input The input stream that contains the PDF document.      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFParser
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|input
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor to allow control over RandomAccessFile.      * @param input The input stream that contains the PDF document.      * @param rafi The RandomAccessFile to be used in internal COSDocument      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFParser
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|RandomAccess
name|rafi
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|this
operator|.
name|raf
operator|=
name|rafi
expr_stmt|;
block|}
comment|/**      * Constructor to allow control over RandomAccessFile.      * Also enables parser to skip corrupt objects to try and force parsing      * @param input The input stream that contains the PDF document.      * @param rafi The RandomAccessFile to be used in internal COSDocument      * @param force When true, the parser will skip corrupt pdf objects and       * will continue parsing at the next object in the file      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFParser
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|RandomAccess
name|rafi
parameter_list|,
name|boolean
name|force
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|this
operator|.
name|raf
operator|=
name|rafi
expr_stmt|;
name|this
operator|.
name|forceParsing
operator|=
name|force
expr_stmt|;
block|}
comment|/**      * This is the directory where pdfbox will create a temporary file      * for storing pdf document stream in.  By default this directory will      * be the value of the system property java.io.tmpdir.      *      * @param tmpDir The directory to create scratch files needed to store      *        pdf document streams.      */
specifier|public
name|void
name|setTempDirectory
parameter_list|(
name|File
name|tmpDir
parameter_list|)
block|{
name|tempDirectory
operator|=
name|tmpDir
expr_stmt|;
block|}
comment|/**      * This will parse the stream and populate the COSDocument object.  This will close      * the stream when it is done parsing.      *      * @throws IOException If there is an error reading from the stream or corrupt data      * is found.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|raf
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|tempDirectory
operator|!=
literal|null
condition|)
block|{
name|document
operator|=
operator|new
name|COSDocument
argument_list|(
name|tempDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
operator|new
name|COSDocument
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|document
operator|=
operator|new
name|COSDocument
argument_list|(
name|raf
argument_list|)
expr_stmt|;
block|}
name|setDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|parseHeader
argument_list|()
expr_stmt|;
comment|//Some PDF files have garbage between the header and the
comment|//first object
name|skipToNextObj
argument_list|()
expr_stmt|;
name|boolean
name|wasLastParsedObjectEOF
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
break|break;
block|}
try|try
block|{
name|wasLastParsedObjectEOF
operator|=
name|parseObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|forceParsing
condition|)
block|{
comment|/*                              * Warning is sent to the PDFBox.log and to the Console that                              * we skipped over an object                              */
name|logger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Parsing Error, Skipping Object"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|skipToNextObj
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
block|}
comment|//Test if we saw a trailer section. If not, look for an XRef Stream (Cross-Reference Stream)
comment|//For PDF 1.5 and above
if|if
condition|(
name|document
operator|.
name|getTrailer
argument_list|()
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|trailer
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|Iterator
name|xrefIter
init|=
name|document
operator|.
name|getObjectsByType
argument_list|(
literal|"XRef"
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|xrefIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSStream
name|next
init|=
call|(
name|COSStream
call|)
argument_list|(
operator|(
name|COSObject
operator|)
name|xrefIter
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|trailer
operator|.
name|addAll
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
name|document
operator|.
name|setTrailer
argument_list|(
name|trailer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
name|document
operator|.
name|dereferenceObjectStreams
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|/*                  * PDF files may have random data after the EOF marker. Ignore errors if                  * last object processed is EOF.                   */
if|if
condition|(
operator|!
name|wasLastParsedObjectEOF
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//so if the PDF is corrupt then close the document and clear
comment|//all resources to it
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
if|if
condition|(
name|t
operator|instanceof
name|IOException
condition|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|t
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|WrappedIOException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|pdfSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Skip to the start of the next object.  This is used to recover      * from a corrupt object. This should handle all cases that parseObject      * supports. This assumes that the next object will      * start on its own line.      *       * @throws IOException       */
specifier|private
name|void
name|skipToNextObj
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|16
index|]
decl_stmt|;
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\d+\\s+\\d+\\s+obj.*"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
decl_stmt|;
comment|/* Read a buffer of data each time to see if it starts with a          * known keyword. This is not the most efficient design, but we should          * rarely be needing this function. We could update this to use the           * circular buffer, like in readUntilEndStream().          */
while|while
condition|(
operator|!
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
name|int
name|l
init|=
name|pdfSource
operator|.
name|read
argument_list|(
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|<
literal|1
condition|)
block|{
break|break;
block|}
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"trailer"
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"xref"
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"startxref"
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"stream"
argument_list|)
operator|||
name|p
operator|.
name|matcher
argument_list|(
name|s
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|pdfSource
operator|.
name|unread
argument_list|(
name|b
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
name|pdfSource
operator|.
name|unread
argument_list|(
name|b
argument_list|,
literal|1
argument_list|,
name|l
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|parseHeader
parameter_list|()
throws|throws
name|IOException
block|{
comment|// read first line
name|String
name|header
init|=
name|readLine
argument_list|()
decl_stmt|;
comment|// some pdf-documents are broken and the pdf-version is in one of the following lines
if|if
condition|(
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|PDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
operator|&&
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|FDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
name|header
operator|=
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|PDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
operator|&&
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|FDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
comment|// if a line starts with a digit, it has to be the first one with data in it
if|if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|header
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
break|break ;
name|header
operator|=
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
comment|// nothing found
if|if
condition|(
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|PDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
operator|&&
operator|(
name|header
operator|.
name|indexOf
argument_list|(
name|FDF_HEADER
argument_list|)
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Header doesn't contain versioninfo"
argument_list|)
throw|;
block|}
comment|//sometimes there are some garbage bytes in the header before the header
comment|//actually starts, so lets try to find the header first.
name|int
name|headerStart
init|=
name|header
operator|.
name|indexOf
argument_list|(
name|PDF_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerStart
operator|==
operator|-
literal|1
condition|)
name|headerStart
operator|=
name|header
operator|.
name|indexOf
argument_list|(
name|FDF_HEADER
argument_list|)
expr_stmt|;
comment|//greater than zero because if it is zero then
comment|//there is no point of trimming
if|if
condition|(
name|headerStart
operator|>
literal|0
condition|)
block|{
comment|//trim off any leading characters
name|header
operator|=
name|header
operator|.
name|substring
argument_list|(
name|headerStart
argument_list|,
name|header
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*          * This is used if there is garbage after the header on the same line          */
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|PDF_HEADER
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|header
operator|.
name|matches
argument_list|(
name|PDF_HEADER
operator|+
literal|"\\d.\\d"
argument_list|)
condition|)
block|{
name|String
name|headerGarbage
init|=
name|header
operator|.
name|substring
argument_list|(
name|PDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|,
name|header
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|header
operator|=
name|header
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|PDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
name|headerGarbage
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|header
operator|.
name|matches
argument_list|(
name|FDF_HEADER
operator|+
literal|"\\d.\\d"
argument_list|)
condition|)
block|{
name|String
name|headerGarbage
init|=
name|header
operator|.
name|substring
argument_list|(
name|FDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|,
name|header
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|header
operator|=
name|header
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|FDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
name|headerGarbage
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|document
operator|.
name|setHeaderString
argument_list|(
name|header
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|PDF_HEADER
argument_list|)
condition|)
block|{
name|float
name|pdfVersion
init|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|header
operator|.
name|substring
argument_list|(
name|PDF_HEADER
operator|.
name|length
argument_list|()
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|header
operator|.
name|length
argument_list|()
argument_list|,
name|PDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|document
operator|.
name|setVersion
argument_list|(
name|pdfVersion
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|float
name|pdfVersion
init|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|header
operator|.
name|substring
argument_list|(
name|FDF_HEADER
operator|.
name|length
argument_list|()
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|header
operator|.
name|length
argument_list|()
argument_list|,
name|FDF_HEADER
operator|.
name|length
argument_list|()
operator|+
literal|3
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|document
operator|.
name|setVersion
argument_list|(
name|pdfVersion
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error getting pdf version:"
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will get the document that was parsed.  parse() must be called before this is called.      * When you are done with this document you must call close() on it to release      * resources.      *      * @return The document that was parsed.      *      * @throws IOException If there is an error getting the document.      */
specifier|public
name|COSDocument
name|getDocument
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"You must call parse() before calling getDocument()"
argument_list|)
throw|;
block|}
return|return
name|document
return|;
block|}
comment|/**      * This will get the PD document that was parsed.  When you are done with      * this document you must call close() on it to release resources.      *      * @return The document at the PD layer.      *      * @throws IOException If there is an error getting the document.      */
specifier|public
name|PDDocument
name|getPDDocument
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDDocument
argument_list|(
name|getDocument
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will get the FDF document that was parsed.  When you are done with      * this document you must call close() on it to release resources.      *      * @return The document at the PD layer.      *      * @throws IOException If there is an error getting the document.      */
specifier|public
name|FDFDocument
name|getFDFDocument
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|FDFDocument
argument_list|(
name|getDocument
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will parse the next object from the stream and add it to       * the local state.       *      * @return Returns true if the processed object had an endOfFile marker      *      * @throws IOException If an IO error occurs.      */
specifier|private
name|boolean
name|parseObject
parameter_list|()
throws|throws
name|IOException
block|{
name|boolean
name|isEndOfFile
init|=
literal|false
decl_stmt|;
name|skipSpaces
argument_list|()
expr_stmt|;
comment|//peek at the next character to determine the type of object we are parsing
name|char
name|peekedChar
init|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
decl_stmt|;
comment|//ignore endobj and endstream sections.
while|while
condition|(
name|peekedChar
operator|==
literal|'e'
condition|)
block|{
comment|//there are times when there are multiple endobj, so lets
comment|//just read them and move on.
name|readString
argument_list|()
expr_stmt|;
name|skipSpaces
argument_list|()
expr_stmt|;
name|peekedChar
operator|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
comment|//"Skipping because of EOF" );
comment|//end of file we will return a false and call it a day.
block|}
comment|//xref table. Note: The contents of the Xref table are currently ignored
elseif|else
if|if
condition|(
name|peekedChar
operator|==
literal|'x'
condition|)
block|{
name|parseXrefTable
argument_list|()
expr_stmt|;
block|}
comment|// Note: startxref can occur in either a trailer section or by itself
elseif|else
if|if
condition|(
name|peekedChar
operator|==
literal|'t'
operator|||
name|peekedChar
operator|==
literal|'s'
condition|)
block|{
if|if
condition|(
name|peekedChar
operator|==
literal|'t'
condition|)
block|{
name|parseTrailer
argument_list|()
expr_stmt|;
name|peekedChar
operator|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|peekedChar
operator|==
literal|'s'
condition|)
block|{
name|parseStartXref
argument_list|()
expr_stmt|;
comment|//verify that EOF exists
name|String
name|eof
init|=
name|readExpectedString
argument_list|(
literal|"%%EOF"
argument_list|)
decl_stmt|;
if|if
condition|(
name|eof
operator|.
name|indexOf
argument_list|(
literal|"%%EOF"
argument_list|)
operator|==
operator|-
literal|1
operator|&&
operator|!
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"expected='%%EOF' actual='"
operator|+
name|eof
operator|+
literal|"' next="
operator|+
name|readString
argument_list|()
operator|+
literal|" next="
operator|+
name|readString
argument_list|()
argument_list|)
throw|;
block|}
name|isEndOfFile
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|//we are going to parse an normal object
else|else
block|{
name|int
name|number
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|genNum
init|=
operator|-
literal|1
decl_stmt|;
name|String
name|objectKey
init|=
literal|null
decl_stmt|;
name|boolean
name|missingObjectNumber
init|=
literal|false
decl_stmt|;
try|try
block|{
name|char
name|peeked
init|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|peeked
operator|==
literal|'<'
condition|)
block|{
name|missingObjectNumber
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|number
operator|=
name|readInt
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ok for some reason "GNU Ghostscript 5.10" puts two endobj
comment|//statements after an object, of course this is nonsense
comment|//but because we want to support as many PDFs as possible
comment|//we will simply try again
name|number
operator|=
name|readInt
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|missingObjectNumber
condition|)
block|{
name|skipSpaces
argument_list|()
expr_stmt|;
name|genNum
operator|=
name|readInt
argument_list|()
expr_stmt|;
name|objectKey
operator|=
name|readString
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|//System.out.println( "parseObject() num=" + number +
comment|//" genNumber=" + genNum + " key='" + objectKey + "'" );
if|if
condition|(
operator|!
name|objectKey
operator|.
name|equals
argument_list|(
literal|"obj"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"expected='obj' actual='"
operator|+
name|objectKey
operator|+
literal|"' "
operator|+
name|pdfSource
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|number
operator|=
operator|-
literal|1
expr_stmt|;
name|genNum
operator|=
operator|-
literal|1
expr_stmt|;
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
name|COSBase
name|pb
init|=
name|parseDirObject
argument_list|()
decl_stmt|;
name|String
name|endObjectKey
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
name|endObjectKey
operator|.
name|equals
argument_list|(
literal|"stream"
argument_list|)
condition|)
block|{
name|pdfSource
operator|.
name|unread
argument_list|(
name|endObjectKey
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|pb
operator|instanceof
name|COSDictionary
condition|)
block|{
name|pb
operator|=
name|parseCOSStream
argument_list|(
operator|(
name|COSDictionary
operator|)
name|pb
argument_list|,
name|getDocument
argument_list|()
operator|.
name|getScratchFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// this is not legal
comment|// the combination of a dict and the stream/endstream forms a complete stream object
throw|throw
operator|new
name|IOException
argument_list|(
literal|"stream not preceded by dictionary"
argument_list|)
throw|;
block|}
name|endObjectKey
operator|=
name|readString
argument_list|()
expr_stmt|;
block|}
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
name|number
argument_list|,
name|genNum
argument_list|)
decl_stmt|;
name|COSObject
name|pdfObject
init|=
name|document
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|pdfObject
operator|.
name|setObject
argument_list|(
name|pb
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|endObjectKey
operator|.
name|equals
argument_list|(
literal|"endobj"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
try|try
block|{
comment|//It is possible that the endobj  is missing, there
comment|//are several PDFs out there that do that so skip it and move on.
name|Float
operator|.
name|parseFloat
argument_list|(
name|endObjectKey
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
name|SPACE_BYTE
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
name|endObjectKey
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|//we will try again incase there was some garbage which
comment|//some writers will leave behind.
name|String
name|secondEndObjectKey
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|secondEndObjectKey
operator|.
name|equals
argument_list|(
literal|"endobj"
argument_list|)
condition|)
block|{
if|if
condition|(
name|isClosing
argument_list|()
condition|)
block|{
comment|//found a case with 17506.pdf object 41 that was like this
comment|//41 0 obj [/Pattern /DeviceGray] ] endobj
comment|//notice the second array close, here we are reading it
comment|//and ignoring and attempting to continue
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
name|String
name|thirdPossibleEndObj
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|thirdPossibleEndObj
operator|.
name|equals
argument_list|(
literal|"endobj"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"expected='endobj' firstReadAttempt='"
operator|+
name|endObjectKey
operator|+
literal|"' "
operator|+
literal|"secondReadAttempt='"
operator|+
name|secondEndObjectKey
operator|+
literal|"' "
operator|+
name|pdfSource
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
block|}
return|return
name|isEndOfFile
return|;
block|}
comment|/**      * This will parse the startxref section from the stream.      * The startxref value is ignored.      *                  * @return false on parsing error       * @throws IOException If an IO error occurs.      */
specifier|private
name|boolean
name|parseStartXref
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdfSource
operator|.
name|peek
argument_list|()
operator|!=
literal|'s'
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|nextLine
init|=
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|nextLine
operator|.
name|equals
argument_list|(
literal|"startxref"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
comment|/* This integer is the byte offset of the first object referenced by the xref or xref stream          * Not needed for PDFbox          */
name|readInt
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * This will parse the xref table from the stream and add it to the state      * The XrefTable contents are ignored.      *                  * @return false on parsing error       * @throws IOException If an IO error occurs.      */
specifier|private
name|boolean
name|parseXrefTable
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdfSource
operator|.
name|peek
argument_list|()
operator|!=
literal|'x'
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|nextLine
init|=
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|nextLine
operator|.
name|equals
argument_list|(
literal|"xref"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|/*          * Xref tables can have multiple sections.           * Each starts with a starting object id and a count.          */
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|start
init|=
name|readInt
argument_list|()
decl_stmt|;
comment|// first obj id
name|int
name|count
init|=
name|readInt
argument_list|()
decl_stmt|;
comment|// the number of objects in the xref table
name|skipSpaces
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|pdfSource
operator|.
name|isEOF
argument_list|()
operator|||
name|isEndOfName
argument_list|(
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|pdfSource
operator|.
name|peek
argument_list|()
operator|==
literal|'t'
condition|)
block|{
break|break;
block|}
comment|//Ignore table contents
name|readLine
argument_list|()
expr_stmt|;
name|skipSpaces
argument_list|()
expr_stmt|;
block|}
name|addXref
argument_list|(
operator|new
name|PDFXref
argument_list|(
name|start
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
name|skipSpaces
argument_list|()
expr_stmt|;
name|char
name|c
init|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
argument_list|<
literal|'0'
operator|||
name|c
argument_list|>
literal|'9'
condition|)
block|{
break|break;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * This will parse the trailer from the stream and add it to the state      *                  * @return false on parsing error      * @throws IOException If an IO error occurs.      */
specifier|private
name|boolean
name|parseTrailer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdfSource
operator|.
name|peek
argument_list|()
operator|!=
literal|'t'
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|//read "trailer"
name|String
name|nextLine
init|=
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|nextLine
operator|.
name|equals
argument_list|(
literal|"trailer"
argument_list|)
condition|)
block|{
comment|// in some cases the EOL is missing and the trailer immediately continues with "<<" or with a blank character
comment|// even if this does not comply with PDF reference we want to support as many PDFs as possible
comment|// Acrobat reader can also deal with this.
if|if
condition|(
name|nextLine
operator|.
name|startsWith
argument_list|(
literal|"trailer"
argument_list|)
condition|)
block|{
name|byte
index|[]
name|b
init|=
name|nextLine
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|int
name|len
init|=
literal|"trailer"
operator|.
name|length
argument_list|()
decl_stmt|;
name|pdfSource
operator|.
name|unread
argument_list|(
name|b
argument_list|,
name|len
argument_list|,
name|b
operator|.
name|length
operator|-
name|len
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// in some cases the EOL is missing and the trailer continues with "<<"
comment|// even if this does not comply with PDF reference we want to support as many PDFs as possible
comment|// Acrobat reader can also deal with this.
name|skipSpaces
argument_list|()
expr_stmt|;
name|COSDictionary
name|parsedTrailer
init|=
name|parseCOSDictionary
argument_list|()
decl_stmt|;
name|COSDictionary
name|docTrailer
init|=
name|document
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
if|if
condition|(
name|docTrailer
operator|==
literal|null
condition|)
block|{
name|document
operator|.
name|setTrailer
argument_list|(
name|parsedTrailer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|docTrailer
operator|.
name|addAll
argument_list|(
name|parsedTrailer
argument_list|)
expr_stmt|;
block|}
name|skipSpaces
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

