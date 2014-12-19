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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|pdfwriter
operator|.
name|COSWriter
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

begin_class
specifier|public
class|class
name|VisualSignatureParser
extends|extends
name|BaseParser
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|VisualSignatureParser
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      *       * @param input the inputstream to be read.      *       * @throws IOException If something went wrong      */
specifier|public
name|VisualSignatureParser
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will parse the tokens making up the visual signature.      *      * @throws IOException If there is an error while parsing the visual signature.      */
specifier|public
name|void
name|parse
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
operator|!
name|wasLastParsedObjectEOF
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
comment|/*                      * Warning is sent to the PDFBox.log and to the Console that                      * we skipped over an object                      */
name|LOG
operator|.
name|warn
argument_list|(
literal|"Parsing Error, Skipping Object"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|skipToNextObj
argument_list|()
expr_stmt|;
block|}
name|skipSpaces
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
comment|/*              * PDF files may have random data after the EOF marker. Ignore errors if              * last object processed is EOF.              */
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
comment|/* Read a buffer of data each time to see if it starts with a          * known keyword. This is not the most efficient design, but we should          * rarely be needing this function. We could update this to use the          * circular buffer, like in readUntilEndStream().          */
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
argument_list|,
literal|"US-ASCII"
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
comment|/**      * This will read bytes until the end of line marker occurs.      *      * @param theString The next expected string in the stream.      *      * @return The characters between the current position and the end of the      * line.      *      * @throws IOException If there is an error reading from the stream or      * theString does not match what was read.      */
specifier|private
name|String
name|readExpectedStringUntilEOL
parameter_list|(
name|String
name|theString
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|c
init|=
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
while|while
condition|(
name|isWhitespace
argument_list|(
name|c
argument_list|)
operator|&&
name|c
operator|!=
operator|-
literal|1
condition|)
block|{
name|c
operator|=
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|theString
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|charsRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|isEOL
argument_list|(
name|c
argument_list|)
operator|&&
name|c
operator|!=
operator|-
literal|1
operator|&&
name|charsRead
operator|<
name|theString
operator|.
name|length
argument_list|()
condition|)
block|{
name|char
name|next
init|=
operator|(
name|char
operator|)
name|c
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|next
argument_list|)
expr_stmt|;
if|if
condition|(
name|theString
operator|.
name|charAt
argument_list|(
name|charsRead
argument_list|)
operator|==
name|next
condition|)
block|{
name|charsRead
operator|++
expr_stmt|;
block|}
else|else
block|{
name|pdfSource
operator|.
name|unread
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Expected to read '"
operator|+
name|theString
operator|+
literal|"' instead started reading '"
operator|+
name|buffer
operator|.
name|toString
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|c
operator|=
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
name|isEOL
argument_list|(
name|c
argument_list|)
operator|&&
name|c
operator|!=
operator|-
literal|1
condition|)
block|{
name|c
operator|=
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|!=
operator|-
literal|1
condition|)
block|{
name|pdfSource
operator|.
name|unread
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
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
comment|// end of file we will return a false and call it a day.
block|}
elseif|else
if|if
condition|(
name|peekedChar
operator|==
literal|'x'
condition|)
block|{
comment|//xref table. Note: The contents of the Xref table are currently ignored
return|return
literal|true
return|;
block|}
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
comment|// Note: startxref can occur in either a trailer section or by itself
if|if
condition|(
name|peekedChar
operator|==
literal|'t'
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|peekedChar
operator|==
literal|'s'
condition|)
block|{
name|skipToNextObj
argument_list|()
expr_stmt|;
comment|//verify that EOF exists
name|String
name|eof
init|=
name|readExpectedStringUntilEOL
argument_list|(
literal|"%%EOF"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|eof
operator|.
name|contains
argument_list|(
literal|"%%EOF"
argument_list|)
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
else|else
block|{
comment|//we are going to parse an normal object
name|long
name|number
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|genNum
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
name|readObjectNumber
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
name|readObjectNumber
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
name|readGenerationNumber
argument_list|()
expr_stmt|;
name|String
name|objectKey
init|=
name|readString
argument_list|(
literal|3
argument_list|)
decl_stmt|;
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
name|pb
operator|.
name|setNeedToBeUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|endObjectKey
operator|.
name|startsWith
argument_list|(
literal|"endobj"
argument_list|)
condition|)
block|{
comment|/*                      * Some PDF files don't contain a new line after endobj so we                      * need to make sure that the next object number is getting read separately                      * and not part of the endobj keyword. Ex. Some files would have "endobj28"                      * instead of "endobj"                      */
name|pdfSource
operator|.
name|unread
argument_list|(
name|endObjectKey
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|COSWriter
operator|.
name|SPACE
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
argument_list|,
name|e
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
comment|/**      * Returns the underlying COSDocument.      *       * @return the COSDocument      *       * @throws IOException If something went wrong      */
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
block|}
end_class

end_unit

