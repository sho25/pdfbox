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
name|ByteArrayOutputStream
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
name|ArrayList
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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
name|COSBoolean
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
name|cos
operator|.
name|COSNull
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
name|COSNumber
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
name|io
operator|.
name|RandomAccessRead
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
name|PDStream
import|;
end_import

begin_comment
comment|/**  * This will parse a PDF byte stream and extract operands and such.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFStreamParser
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
name|PDFStreamParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|streamObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
literal|100
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_BIN_CHAR_TEST_LENGTH
init|=
literal|10
decl_stmt|;
specifier|private
specifier|final
name|byte
index|[]
name|binCharTestArr
init|=
operator|new
name|byte
index|[
name|MAX_BIN_CHAR_TEST_LENGTH
index|]
decl_stmt|;
comment|/**      * Constructor.      *      * @param stream The stream to parse.      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFStreamParser
parameter_list|(
name|PDStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|stream
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param stream The stream to parse.      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFStreamParser
parameter_list|(
name|COSStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param input The random access read to parse.      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFStreamParser
parameter_list|(
name|RandomAccessRead
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
comment|/**      * This will parse the tokens in the stream.  This will close the      * stream when it is finished parsing.      *      * @throws IOException If there is an error while parsing the stream.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
name|Object
name|token
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|parseNextToken
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|streamObjects
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the tokens that were parsed from the stream.      *      * @return All of the tokens in the stream.      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getTokens
parameter_list|()
block|{
return|return
name|streamObjects
return|;
block|}
comment|/**      * This will parse the next token in the stream.      *      * @return The next token in the stream or null if there are no more tokens in the stream.      *      * @throws IOException If an io error occurs while parsing the stream.      */
specifier|public
name|Object
name|parseNextToken
parameter_list|()
throws|throws
name|IOException
block|{
name|Object
name|retval
decl_stmt|;
name|skipSpaces
argument_list|()
expr_stmt|;
name|int
name|nextByte
init|=
name|pdfSource
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|(
name|byte
operator|)
name|nextByte
operator|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
name|char
name|c
init|=
operator|(
name|char
operator|)
name|nextByte
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'<'
case|:
block|{
comment|// pull off first left bracket
name|int
name|leftBracket
init|=
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
comment|// check for second left bracket
name|c
operator|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
expr_stmt|;
comment|// put back first bracket
name|pdfSource
operator|.
name|rewind
argument_list|(
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'<'
condition|)
block|{
name|retval
operator|=
name|parseCOSDictionary
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|parseCOSString
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'['
case|:
block|{
comment|// array
name|retval
operator|=
name|parseCOSArray
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
literal|'('
case|:
comment|// string
name|retval
operator|=
name|parseCOSString
argument_list|()
expr_stmt|;
break|break;
case|case
literal|'/'
case|:
comment|// name
name|retval
operator|=
name|parseCOSName
argument_list|()
expr_stmt|;
break|break;
case|case
literal|'n'
case|:
block|{
comment|// null
name|String
name|nullString
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
name|nullString
operator|.
name|equals
argument_list|(
literal|"null"
argument_list|)
condition|)
block|{
name|retval
operator|=
name|COSNull
operator|.
name|NULL
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|nullString
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'t'
case|:
case|case
literal|'f'
case|:
block|{
name|String
name|next
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|retval
operator|=
name|COSBoolean
operator|.
name|TRUE
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|next
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
name|retval
operator|=
name|COSBoolean
operator|.
name|FALSE
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'R'
case|:
block|{
name|String
name|line
init|=
name|readString
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|equals
argument_list|(
literal|"R"
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|COSObject
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'0'
case|:
case|case
literal|'1'
case|:
case|case
literal|'2'
case|:
case|case
literal|'3'
case|:
case|case
literal|'4'
case|:
case|case
literal|'5'
case|:
case|case
literal|'6'
case|:
case|case
literal|'7'
case|:
case|case
literal|'8'
case|:
case|case
literal|'9'
case|:
case|case
literal|'-'
case|:
case|case
literal|'+'
case|:
case|case
literal|'.'
case|:
block|{
comment|/* We will be filling buf with the rest of the number.  Only                  * allow 1 "." and "-" and "+" at start of number. */
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|boolean
name|dotNotRead
init|=
name|c
operator|!=
literal|'.'
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|c
operator|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|peek
argument_list|()
argument_list|)
operator|||
name|dotNotRead
operator|&&
name|c
operator|==
literal|'.'
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|dotNotRead
operator|&&
name|c
operator|==
literal|'.'
condition|)
block|{
name|dotNotRead
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|retval
operator|=
name|COSNumber
operator|.
name|get
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|'B'
case|:
block|{
name|String
name|next
init|=
name|readString
argument_list|()
decl_stmt|;
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|next
argument_list|)
expr_stmt|;
if|if
condition|(
name|next
operator|.
name|equals
argument_list|(
literal|"BI"
argument_list|)
condition|)
block|{
name|Operator
name|beginImageOP
init|=
operator|(
name|Operator
operator|)
name|retval
decl_stmt|;
name|COSDictionary
name|imageParams
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|beginImageOP
operator|.
name|setImageParameters
argument_list|(
name|imageParams
argument_list|)
expr_stmt|;
name|Object
name|nextToken
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|nextToken
operator|=
name|parseNextToken
argument_list|()
operator|)
operator|instanceof
name|COSName
condition|)
block|{
name|Object
name|value
init|=
name|parseNextToken
argument_list|()
decl_stmt|;
name|imageParams
operator|.
name|setItem
argument_list|(
operator|(
name|COSName
operator|)
name|nextToken
argument_list|,
operator|(
name|COSBase
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
comment|//final token will be the image data, maybe??
name|Operator
name|imageData
init|=
operator|(
name|Operator
operator|)
name|nextToken
decl_stmt|;
name|beginImageOP
operator|.
name|setImageData
argument_list|(
name|imageData
operator|.
name|getImageData
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'I'
case|:
block|{
comment|//Special case for ID operator
name|String
name|id
init|=
literal|""
operator|+
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|read
argument_list|()
operator|+
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|equals
argument_list|(
literal|"ID"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Expected operator 'ID' actual='"
operator|+
name|id
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|ByteArrayOutputStream
name|imageData
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|isWhitespace
argument_list|()
condition|)
block|{
comment|//pull off the whitespace character
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|int
name|lastByte
init|=
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
name|int
name|currentByte
init|=
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
comment|// PDF spec is kinda unclear about this. Should a whitespace
comment|// always appear before EI? Not sure, so that we just read
comment|// until EI<whitespace>.
comment|// Be aware not all kind of whitespaces are allowed here. see PDFBOX-1561
while|while
condition|(
operator|!
operator|(
name|lastByte
operator|==
literal|'E'
operator|&&
name|currentByte
operator|==
literal|'I'
operator|&&
name|hasNextSpaceOrReturn
argument_list|()
operator|&&
name|hasNoFollowingBinData
argument_list|(
name|pdfSource
argument_list|)
operator|)
operator|&&
operator|!
name|pdfSource
operator|.
name|isEOF
argument_list|()
condition|)
block|{
name|imageData
operator|.
name|write
argument_list|(
name|lastByte
argument_list|)
expr_stmt|;
name|lastByte
operator|=
name|currentByte
expr_stmt|;
name|currentByte
operator|=
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
comment|// the EI operator isn't unread, as it won't be processed anyway
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
comment|// save the image data to the operator, so that it can be accessed later
operator|(
operator|(
name|Operator
operator|)
name|retval
operator|)
operator|.
name|setImageData
argument_list|(
name|imageData
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|']'
case|:
block|{
comment|// some ']' around without its previous '['
comment|// this means a PDF is somewhat corrupt but we will continue to parse.
name|pdfSource
operator|.
name|read
argument_list|()
expr_stmt|;
comment|// must be a better solution than null...
name|retval
operator|=
name|COSNull
operator|.
name|NULL
expr_stmt|;
break|break;
block|}
default|default:
block|{
comment|//we must be an operator
name|String
name|operator
init|=
name|readOperator
argument_list|()
decl_stmt|;
if|if
condition|(
name|operator
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|//we have a corrupt stream, stop reading here
name|retval
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|operator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Looks up an amount of bytes if they contain only ASCII characters (no      * control sequences etc.), and that these ASCII characters begin with a      * sequence of 1-3 non-blank characters between blanks      *      * @return<code>true</code> if next bytes are probably printable ASCII      * characters starting with a PDF operator, otherwise<code>false</code>      */
specifier|private
name|boolean
name|hasNoFollowingBinData
parameter_list|(
specifier|final
name|RandomAccessRead
name|pdfSource
parameter_list|)
throws|throws
name|IOException
block|{
comment|// as suggested in PDFBOX-1164
specifier|final
name|int
name|readBytes
init|=
name|pdfSource
operator|.
name|read
argument_list|(
name|binCharTestArr
argument_list|,
literal|0
argument_list|,
name|MAX_BIN_CHAR_TEST_LENGTH
argument_list|)
decl_stmt|;
name|boolean
name|noBinData
init|=
literal|true
decl_stmt|;
name|int
name|startOpIdx
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|endOpIdx
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|readBytes
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|bIdx
init|=
literal|0
init|;
name|bIdx
operator|<
name|readBytes
condition|;
name|bIdx
operator|++
control|)
block|{
specifier|final
name|byte
name|b
init|=
name|binCharTestArr
index|[
name|bIdx
index|]
decl_stmt|;
if|if
condition|(
name|b
argument_list|<
literal|0x09
operator|||
name|b
argument_list|>
literal|0x0a
operator|&&
name|b
operator|<
literal|0x20
operator|&&
name|b
operator|!=
literal|0x0d
condition|)
block|{
comment|// control character or> 0x7f -> we have binary data
name|noBinData
operator|=
literal|false
expr_stmt|;
break|break;
block|}
comment|// find the start of a PDF operator
if|if
condition|(
name|startOpIdx
operator|==
operator|-
literal|1
operator|&&
operator|!
operator|(
name|b
operator|==
literal|9
operator|||
name|b
operator|==
literal|0x20
operator|||
name|b
operator|==
literal|0x0a
operator|||
name|b
operator|==
literal|0x0d
operator|)
condition|)
block|{
name|startOpIdx
operator|=
name|bIdx
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|startOpIdx
operator|!=
operator|-
literal|1
operator|&&
name|endOpIdx
operator|==
operator|-
literal|1
operator|&&
operator|(
name|b
operator|==
literal|9
operator|||
name|b
operator|==
literal|0x20
operator|||
name|b
operator|==
literal|0x0a
operator|||
name|b
operator|==
literal|0x0d
operator|)
condition|)
block|{
name|endOpIdx
operator|=
name|bIdx
expr_stmt|;
block|}
block|}
comment|// only if not close to eof
if|if
condition|(
name|readBytes
operator|==
name|MAX_BIN_CHAR_TEST_LENGTH
condition|)
block|{
comment|// a PDF operator is 1-3 bytes long
if|if
condition|(
name|startOpIdx
operator|!=
operator|-
literal|1
operator|&&
name|endOpIdx
operator|==
operator|-
literal|1
condition|)
block|{
name|endOpIdx
operator|=
name|MAX_BIN_CHAR_TEST_LENGTH
expr_stmt|;
block|}
if|if
condition|(
name|endOpIdx
operator|!=
operator|-
literal|1
operator|&&
name|startOpIdx
operator|!=
operator|-
literal|1
operator|&&
name|endOpIdx
operator|-
name|startOpIdx
operator|>
literal|3
condition|)
block|{
name|noBinData
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|pdfSource
operator|.
name|rewind
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|noBinData
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"ignoring 'EI' assumed to be in the middle of inline image"
argument_list|)
expr_stmt|;
block|}
return|return
name|noBinData
return|;
block|}
comment|/**      * This will read an operator from the stream.      *      * @return The operator that was read from the stream.      *      * @throws IOException If there is an error reading from the stream.      */
specifier|protected
name|String
name|readOperator
parameter_list|()
throws|throws
name|IOException
block|{
name|skipSpaces
argument_list|()
expr_stmt|;
comment|//average string size is around 2 and the normal string buffer size is
comment|//about 16 so lets save some space.
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|int
name|nextChar
init|=
name|pdfSource
operator|.
name|peek
argument_list|()
decl_stmt|;
while|while
condition|(
name|nextChar
operator|!=
operator|-
literal|1
operator|&&
comment|// EOF
operator|!
name|isWhitespace
argument_list|(
name|nextChar
argument_list|)
operator|&&
operator|!
name|isClosing
argument_list|(
name|nextChar
argument_list|)
operator|&&
name|nextChar
operator|!=
literal|'['
operator|&&
name|nextChar
operator|!=
literal|'<'
operator|&&
name|nextChar
operator|!=
literal|'('
operator|&&
name|nextChar
operator|!=
literal|'/'
operator|&&
operator|(
name|nextChar
argument_list|<
literal|'0'
operator|||
name|nextChar
argument_list|>
literal|'9'
operator|)
condition|)
block|{
name|char
name|currentChar
init|=
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|read
argument_list|()
decl_stmt|;
name|nextChar
operator|=
name|pdfSource
operator|.
name|peek
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|currentChar
argument_list|)
expr_stmt|;
comment|// Type3 Glyph description has operators with a number in the name
if|if
condition|(
name|currentChar
operator|==
literal|'d'
operator|&&
operator|(
name|nextChar
operator|==
literal|'0'
operator|||
name|nextChar
operator|==
literal|'1'
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|pdfSource
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|nextChar
operator|=
name|pdfSource
operator|.
name|peek
argument_list|()
expr_stmt|;
block|}
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
name|isSpaceOrReturn
parameter_list|(
name|int
name|c
parameter_list|)
block|{
return|return
name|c
operator|==
literal|10
operator|||
name|c
operator|==
literal|13
operator|||
name|c
operator|==
literal|32
return|;
block|}
comment|/**      * Checks if the next char is a space or a return.      *       * @return true if the next char is a space or a return      * @throws IOException if something went wrong      */
specifier|private
name|boolean
name|hasNextSpaceOrReturn
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|isSpaceOrReturn
argument_list|(
name|pdfSource
operator|.
name|peek
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

