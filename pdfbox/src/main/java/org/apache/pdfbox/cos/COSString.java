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
name|cos
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|util
operator|.
name|Hex
import|;
end_import

begin_comment
comment|/**  * A string object, which may be a text string, a PDFDocEncoded string, ASCII string, or byte string.  *  *<p>Text strings are used for character strings that contain information intended to be  * human-readable, such as text annotations, bookmark names, article names, document information,  * and so forth.  *  *<p>PDFDocEncoded strings are used for characters that are represented in a single byte.  *  *<p>ASCII strings are used for characters that are represented in a single byte using ASCII  * encoding.  *  *<p>Byte strings are used for binary data represented as a series of bytes, but the encoding is  * not known. The bytes of the string need not represent characters.  *   * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|COSString
extends|extends
name|COSBase
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
name|COSString
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|byte
index|[]
name|bytes
decl_stmt|;
specifier|private
name|boolean
name|forceHexForm
decl_stmt|;
comment|// legacy behaviour for old PDFParser
specifier|public
specifier|static
specifier|final
name|boolean
name|FORCE_PARSING
init|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"org.apache.pdfbox.forceParsing"
argument_list|)
decl_stmt|;
comment|/**      * Creates a new PDF string from a byte array. This method can be used to read a string from      * an existing PDF file, or to create a new byte string.      *      * @param bytes The raw bytes of the PDF text string or byte string.      */
specifier|public
name|COSString
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|setValue
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new<i>text string</i> from a Java String.      *      * @param text The string value of the object.      */
specifier|public
name|COSString
parameter_list|(
name|String
name|text
parameter_list|)
block|{
comment|// check whether the string uses only characters available in PDFDocEncoding
name|boolean
name|isOnlyPDFDocEncoding
init|=
literal|true
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|text
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|PDFDocEncoding
operator|.
name|containsChar
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|isOnlyPDFDocEncoding
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|isOnlyPDFDocEncoding
condition|)
block|{
comment|// PDFDocEncoded string
name|bytes
operator|=
name|PDFDocEncoding
operator|.
name|getBytes
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// UTF-16BE encoded string with a leading byte order marker
name|byte
index|[]
name|data
init|=
name|text
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_16BE
argument_list|)
decl_stmt|;
name|bytes
operator|=
operator|new
name|byte
index|[
name|data
operator|.
name|length
operator|+
literal|2
index|]
expr_stmt|;
name|bytes
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
literal|0xFE
expr_stmt|;
name|bytes
index|[
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
literal|0xFF
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|bytes
argument_list|,
literal|2
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will create a COS string from a string of hex characters.      *      * @param hex A hex string.      * @return A cos string with the hex characters converted to their actual bytes.      * @throws IOException If there is an error with the hex string.      */
specifier|public
specifier|static
name|COSString
name|parseHex
parameter_list|(
name|String
name|hex
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StringBuilder
name|hexBuffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|hex
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
comment|// if odd number then the last hex digit is assumed to be 0
if|if
condition|(
name|hexBuffer
operator|.
name|length
argument_list|()
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
name|hexBuffer
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|int
name|length
init|=
name|hexBuffer
operator|.
name|length
argument_list|()
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
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
try|try
block|{
name|bytes
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|hexBuffer
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|2
argument_list|)
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
if|if
condition|(
name|FORCE_PARSING
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Encountered a malformed hex string"
argument_list|)
expr_stmt|;
name|bytes
operator|.
name|write
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
comment|// todo: what does Acrobat do? Any example PDFs?
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid hex string: "
operator|+
name|hex
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
operator|new
name|COSString
argument_list|(
name|bytes
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets the raw value of this string.      *      * @param value The raw bytes of the PDF text string or byte string.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
name|bytes
operator|=
name|value
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets whether or not to force the string is to be written in hex form.      * This is needed when signing PDF files.      *      * @param value True to force hex.      */
specifier|public
name|void
name|setForceHexForm
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|forceHexForm
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Returns true if the string is to be written in hex form.      */
specifier|public
name|boolean
name|getForceHexForm
parameter_list|()
block|{
return|return
name|forceHexForm
return|;
block|}
comment|/**      * Returns the content of this string as a PDF<i>text string</i>.      */
specifier|public
name|String
name|getString
parameter_list|()
block|{
comment|// text string - BOM indicates Unicode
if|if
condition|(
name|bytes
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
if|if
condition|(
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xff
operator|)
operator|==
literal|0xFE
operator|&&
operator|(
name|bytes
index|[
literal|1
index|]
operator|&
literal|0xff
operator|)
operator|==
literal|0xFF
condition|)
block|{
comment|// UTF-16BE
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|2
argument_list|,
name|bytes
operator|.
name|length
operator|-
literal|2
argument_list|,
name|StandardCharsets
operator|.
name|UTF_16BE
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xff
operator|)
operator|==
literal|0xFF
operator|&&
operator|(
name|bytes
index|[
literal|1
index|]
operator|&
literal|0xff
operator|)
operator|==
literal|0xFE
condition|)
block|{
comment|// UTF-16LE - not in the PDF spec!
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|2
argument_list|,
name|bytes
operator|.
name|length
operator|-
literal|2
argument_list|,
name|StandardCharsets
operator|.
name|UTF_16LE
argument_list|)
return|;
block|}
block|}
comment|// otherwise use PDFDocEncoding
return|return
name|PDFDocEncoding
operator|.
name|toString
argument_list|(
name|bytes
argument_list|)
return|;
block|}
comment|/**      * Returns the content of this string as a PDF<i>ASCII string</i>.      */
specifier|public
name|String
name|getASCII
parameter_list|()
block|{
comment|// ASCII string
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|StandardCharsets
operator|.
name|US_ASCII
argument_list|)
return|;
block|}
comment|/**      * Returns the raw bytes of the string. Best used with a PDF<i>byte string</i>.      */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|bytes
return|;
block|}
comment|/**      * This will take this string and create a hex representation of the bytes that make the string.      *      * @return A hex string representing the bytes in this string.      */
specifier|public
name|String
name|toHexString
parameter_list|()
block|{
return|return
name|Hex
operator|.
name|getString
argument_list|(
name|bytes
argument_list|)
return|;
block|}
comment|/**      * Visitor pattern double dispatch method.      *       * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException If an error occurs while visiting this object.      */
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|visitor
operator|.
name|visitFromString
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|COSString
condition|)
block|{
name|COSString
name|strObj
init|=
operator|(
name|COSString
operator|)
name|obj
decl_stmt|;
return|return
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|strObj
operator|.
name|getString
argument_list|()
argument_list|)
operator|&&
name|forceHexForm
operator|==
name|strObj
operator|.
name|forceHexForm
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|Arrays
operator|.
name|hashCode
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
return|return
name|result
operator|+
operator|(
name|forceHexForm
condition|?
literal|17
else|:
literal|0
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSString{"
operator|+
name|getString
argument_list|()
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

