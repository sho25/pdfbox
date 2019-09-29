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
name|util
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Base64
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

begin_comment
comment|/**  * Utility functions for hex encoding.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Hex
block|{
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
name|Hex
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * for hex conversion.      *       * https://stackoverflow.com/questions/2817752/java-code-to-convert-byte-to-hexadecimal      *      */
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|HEX_BYTES
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|,
literal|'E'
block|,
literal|'F'
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|HEX_CHARS
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|,
literal|'E'
block|,
literal|'F'
block|}
decl_stmt|;
specifier|private
name|Hex
parameter_list|()
block|{}
comment|/**      * Returns a hex string of the given byte.      */
specifier|public
specifier|static
name|String
name|getString
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[]
block|{
name|HEX_CHARS
index|[
name|getHighNibble
argument_list|(
name|b
argument_list|)
index|]
block|,
name|HEX_CHARS
index|[
name|getLowNibble
argument_list|(
name|b
argument_list|)
index|]
block|}
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|chars
argument_list|)
return|;
block|}
comment|/**      * Returns a hex string of the given byte array.      */
specifier|public
specifier|static
name|String
name|getString
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|StringBuilder
name|string
init|=
operator|new
name|StringBuilder
argument_list|(
name|bytes
operator|.
name|length
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|string
operator|.
name|append
argument_list|(
name|HEX_CHARS
index|[
name|getHighNibble
argument_list|(
name|b
argument_list|)
index|]
argument_list|)
operator|.
name|append
argument_list|(
name|HEX_CHARS
index|[
name|getLowNibble
argument_list|(
name|b
argument_list|)
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|string
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the bytes corresponding to the ASCII hex encoding of the given byte.      */
specifier|public
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
return|return
operator|new
name|byte
index|[]
block|{
name|HEX_BYTES
index|[
name|getHighNibble
argument_list|(
name|b
argument_list|)
index|]
block|,
name|HEX_BYTES
index|[
name|getLowNibble
argument_list|(
name|b
argument_list|)
index|]
block|}
return|;
block|}
comment|/**      * Returns the bytes corresponding to the ASCII hex encoding of the given bytes.      */
specifier|public
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|byte
index|[]
name|asciiBytes
init|=
operator|new
name|byte
index|[
name|bytes
operator|.
name|length
operator|*
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|asciiBytes
index|[
name|i
operator|*
literal|2
index|]
operator|=
name|HEX_BYTES
index|[
name|getHighNibble
argument_list|(
name|bytes
index|[
name|i
index|]
argument_list|)
index|]
expr_stmt|;
name|asciiBytes
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
operator|=
name|HEX_BYTES
index|[
name|getLowNibble
argument_list|(
name|bytes
index|[
name|i
index|]
argument_list|)
index|]
expr_stmt|;
block|}
return|return
name|asciiBytes
return|;
block|}
comment|/**       * Returns the characters corresponding to the ASCII hex encoding of the given short.      */
specifier|public
specifier|static
name|char
index|[]
name|getChars
parameter_list|(
name|short
name|num
parameter_list|)
block|{
name|char
index|[]
name|hex
init|=
operator|new
name|char
index|[
literal|4
index|]
decl_stmt|;
name|hex
index|[
literal|0
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|num
operator|>>
literal|12
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
literal|1
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|num
operator|>>
literal|8
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
literal|2
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|num
operator|>>
literal|4
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
literal|3
index|]
operator|=
name|HEX_CHARS
index|[
name|num
operator|&
literal|0x0F
index|]
expr_stmt|;
return|return
name|hex
return|;
block|}
comment|/**      * Takes the characters in the given string, convert it to bytes in UTF16-BE format      * and build a char array that corresponds to the ASCII hex encoding of the resulting      * bytes.      *      * Example:      *<pre>      *   getCharsUTF16BE("ab") == new char[]{'0','0','6','1','0','0','6','2'}      *</pre>      *      * @param text The string to convert      * @return The string converted to hex      */
specifier|public
specifier|static
name|char
index|[]
name|getCharsUTF16BE
parameter_list|(
name|String
name|text
parameter_list|)
block|{
comment|// Note that the internal representation of string in Java is already UTF-16. Therefore
comment|// we do not need to use an encoder to convert the string to its byte representation.
name|char
index|[]
name|hex
init|=
operator|new
name|char
index|[
name|text
operator|.
name|length
argument_list|()
operator|*
literal|4
index|]
decl_stmt|;
for|for
control|(
name|int
name|stringIdx
init|=
literal|0
init|,
name|charIdx
init|=
literal|0
init|;
name|stringIdx
operator|<
name|text
operator|.
name|length
argument_list|()
condition|;
name|stringIdx
operator|++
control|)
block|{
name|char
name|c
init|=
name|text
operator|.
name|charAt
argument_list|(
name|stringIdx
argument_list|)
decl_stmt|;
name|hex
index|[
name|charIdx
operator|++
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|c
operator|>>
literal|12
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
name|charIdx
operator|++
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|c
operator|>>
literal|8
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
name|charIdx
operator|++
index|]
operator|=
name|HEX_CHARS
index|[
operator|(
name|c
operator|>>
literal|4
operator|)
operator|&
literal|0x0F
index|]
expr_stmt|;
name|hex
index|[
name|charIdx
operator|++
index|]
operator|=
name|HEX_CHARS
index|[
name|c
operator|&
literal|0x0F
index|]
expr_stmt|;
block|}
return|return
name|hex
return|;
block|}
comment|/**      * Writes the given byte as hex value to the given output stream.      * @param b the byte to be written      * @param output the output stream to be written to      * @throws IOException exception if anything went wrong      */
specifier|public
specifier|static
name|void
name|writeHexByte
parameter_list|(
name|byte
name|b
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
name|HEX_BYTES
index|[
name|getHighNibble
argument_list|(
name|b
argument_list|)
index|]
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|HEX_BYTES
index|[
name|getLowNibble
argument_list|(
name|b
argument_list|)
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**       * Writes the given byte array as hex value to the given output stream.      * @param bytes the byte array to be written      * @param output the output stream to be written to      * @throws IOException exception if anything went wrong      */
specifier|public
specifier|static
name|void
name|writeHexBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|writeHexByte
argument_list|(
name|b
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the high nibble of the given byte.      *       * @param b the given byte      * @return the high nibble      */
specifier|private
specifier|static
name|int
name|getHighNibble
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
return|return
operator|(
name|b
operator|&
literal|0xF0
operator|)
operator|>>
literal|4
return|;
block|}
comment|/**      * Get the low nibble of the given byte.      *       * @param b the given byte      * @return the low nibble      */
specifier|private
specifier|static
name|int
name|getLowNibble
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
return|return
name|b
operator|&
literal|0x0F
return|;
block|}
comment|/**      * Decode a base64 String.      *      * @param base64Value a base64 encoded String.      *      * @return the decoded String as a byte array.      *      * @throws IllegalArgumentException if this isn't a base64 encoded string.      */
specifier|public
specifier|static
name|byte
index|[]
name|decodeBase64
parameter_list|(
name|String
name|base64Value
parameter_list|)
block|{
return|return
name|Base64
operator|.
name|getDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|base64Value
operator|.
name|replaceAll
argument_list|(
literal|"\\s"
argument_list|,
literal|""
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Decodes a hex String into a byte array.      *      * @param s A String with ASCII hex.      * @return decoded byte array.      * @throws IOException      */
specifier|public
specifier|static
name|byte
index|[]
name|decodeHex
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'\n'
operator|||
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'\r'
condition|)
block|{
operator|++
name|i
expr_stmt|;
block|}
else|else
block|{
name|String
name|hexByte
init|=
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
try|try
block|{
name|baos
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|hexByte
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
comment|// Byte.parseByte won't work with "9C"
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't parse "
operator|+
name|hexByte
operator|+
literal|", aborting decode"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
break|break;
block|}
name|i
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

