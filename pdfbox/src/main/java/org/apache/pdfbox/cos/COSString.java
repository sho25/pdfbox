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
name|io
operator|.
name|OutputStream
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
name|encoding
operator|.
name|PdfDocEncoding
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
name|CryptographyException
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
name|COSHEXTable
import|;
end_import

begin_comment
comment|/**  * This represents a string object in a PDF document.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.30 $  */
end_comment

begin_class
specifier|public
class|class
name|COSString
extends|extends
name|COSBase
block|{
comment|/**      * One of the open string tokens.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|STRING_OPEN
init|=
operator|new
name|byte
index|[]
block|{
literal|40
block|}
decl_stmt|;
comment|// "(".getBytes();
comment|/**      * One of the close string tokens.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|STRING_CLOSE
init|=
operator|new
name|byte
index|[]
block|{
literal|41
block|}
decl_stmt|;
comment|// ")".getBytes( "ISO-8859-1" );
comment|/**      * One of the open string tokens.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|HEX_STRING_OPEN
init|=
operator|new
name|byte
index|[]
block|{
literal|60
block|}
decl_stmt|;
comment|// "<".getBytes( "ISO-8859-1" );
comment|/**      * One of the close string tokens.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|HEX_STRING_CLOSE
init|=
operator|new
name|byte
index|[]
block|{
literal|62
block|}
decl_stmt|;
comment|// ">".getBytes( "ISO-8859-1" );
comment|/**      * the escape character in strings.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|}
decl_stmt|;
comment|// "\\".getBytes( "ISO-8859-1" );
comment|/**      * CR escape characters.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|CR_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|,
literal|114
block|}
decl_stmt|;
comment|// "\\r".getBytes( "ISO-8859-1" );
comment|/**      * LF escape characters.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|LF_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|,
literal|110
block|}
decl_stmt|;
comment|// "\\n".getBytes( "ISO-8859-1" );
comment|/**      * HT escape characters.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|HT_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|,
literal|116
block|}
decl_stmt|;
comment|// "\\t".getBytes( "ISO-8859-1" );
comment|/**      * BS escape characters.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|BS_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|,
literal|98
block|}
decl_stmt|;
comment|// "\\b".getBytes( "ISO-8859-1" );
comment|/**      * FF escape characters.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|FF_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|92
block|,
literal|102
block|}
decl_stmt|;
comment|// "\\f".getBytes( "ISO-8859-1" );
specifier|private
name|ByteArrayOutputStream
name|out
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|str
init|=
literal|null
decl_stmt|;
comment|/**      * Forces the string to be serialized in hex form but not literal form, the default is to stream in literal form.      */
specifier|private
name|boolean
name|forceHexForm
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isDictionary
init|=
literal|false
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|COSString
parameter_list|()
block|{
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
comment|/**       * Constructor.      *       * @param isDictionaryValue determines if this string represents a dictionary      */
specifier|public
name|COSString
parameter_list|(
name|boolean
name|isDictionaryValue
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|isDictionary
operator|=
name|isDictionaryValue
expr_stmt|;
block|}
comment|/**      * Explicit constructor for ease of manual PDF construction.      *       * @param value      *            The string value of the object.      */
specifier|public
name|COSString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|boolean
name|unicode16
init|=
literal|false
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|value
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|chars
operator|.
name|length
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
operator|++
control|)
block|{
if|if
condition|(
name|chars
index|[
name|i
index|]
operator|>
literal|255
condition|)
block|{
name|unicode16
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|unicode16
condition|)
block|{
name|byte
index|[]
name|data
init|=
name|value
operator|.
name|getBytes
argument_list|(
literal|"UTF-16BE"
argument_list|)
decl_stmt|;
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|data
operator|.
name|length
operator|+
literal|2
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFE
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFF
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|data
init|=
name|value
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{
name|ignore
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|// should never happen
block|}
block|}
comment|/**      * Explicit constructor for ease of manual PDF construction.      *       * @param value      *            The string value of the object.      */
specifier|public
name|COSString
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
try|try
block|{
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|value
operator|.
name|length
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{
name|ignore
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|// should never happen
block|}
block|}
comment|/**      * Forces the string to be written in literal form instead of hexadecimal form.      *       * @param v      *            if v is true the string will be written in literal form, otherwise it will be written in hexa if      *            necessary.      */
specifier|public
name|void
name|setForceLiteralForm
parameter_list|(
name|boolean
name|v
parameter_list|)
block|{
name|forceHexForm
operator|=
operator|!
name|v
expr_stmt|;
block|}
comment|/**      * Forces the string to be written in hexadecimal form instead of literal form.      *       * @param v      *            if v is true the string will be written in hexadecimal form otherwise it will be written in literal if      *            necessary.      */
specifier|public
name|void
name|setForceHexForm
parameter_list|(
name|boolean
name|v
parameter_list|)
block|{
name|forceHexForm
operator|=
name|v
expr_stmt|;
block|}
comment|/**      * This will create a COS string from a string of hex characters.      *       * @param hex      *            A hex string.      * @return A cos string with the hex characters converted to their actual bytes.      * @throws IOException      *             If there is an error with the hex string.      */
specifier|public
specifier|static
name|COSString
name|createFromHexString
parameter_list|(
name|String
name|hex
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromHexString
argument_list|(
name|hex
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates a COS string from a string of hex characters, optionally ignoring malformed input.      *       * @param hex      *            A hex string.      * @param force      *            flag to ignore malformed input      * @return A cos string with the hex characters converted to their actual bytes.      * @throws IOException      *             If there is an error with the hex string.      */
specifier|public
specifier|static
name|COSString
name|createFromHexString
parameter_list|(
name|String
name|hex
parameter_list|,
name|boolean
name|force
parameter_list|)
throws|throws
name|IOException
block|{
name|COSString
name|retval
init|=
operator|new
name|COSString
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
name|retval
operator|.
name|append
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
name|force
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IOException
name|exception
init|=
operator|new
name|IOException
argument_list|(
literal|"Invalid hex string: "
operator|+
name|hex
argument_list|)
decl_stmt|;
name|exception
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take this string and create a hex representation of the bytes that make the string.      *       * @return A hex string representing the bytes in this string.      */
specifier|public
name|String
name|getHexString
parameter_list|()
block|{
name|StringBuilder
name|retval
init|=
operator|new
name|StringBuilder
argument_list|(
name|out
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|getBytes
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|data
operator|.
name|length
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
operator|++
control|)
block|{
name|retval
operator|.
name|append
argument_list|(
name|COSHEXTable
operator|.
name|HEX_TABLE
index|[
operator|(
name|data
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * This will get the string that this object wraps.      *       * @return The wrapped string.      */
specifier|public
name|String
name|getString
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|str
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|str
return|;
block|}
name|String
name|retval
decl_stmt|;
name|String
name|encoding
init|=
literal|"ISO-8859-1"
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|getBytes
argument_list|()
decl_stmt|;
name|int
name|start
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|length
operator|>
literal|2
condition|)
block|{
if|if
condition|(
name|data
index|[
literal|0
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFF
operator|&&
name|data
index|[
literal|1
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFE
condition|)
block|{
name|encoding
operator|=
literal|"UTF-16LE"
expr_stmt|;
name|start
operator|=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|data
index|[
literal|0
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFE
operator|&&
name|data
index|[
literal|1
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFF
condition|)
block|{
name|encoding
operator|=
literal|"UTF-16BE"
expr_stmt|;
name|start
operator|=
literal|2
expr_stmt|;
block|}
block|}
try|try
block|{
if|if
condition|(
name|isDictionary
operator|&&
name|encoding
operator|.
name|equals
argument_list|(
literal|"ISO-8859-1"
argument_list|)
condition|)
block|{
name|byte
index|[]
name|tmp
init|=
name|getBytes
argument_list|()
decl_stmt|;
name|PdfDocEncoding
name|pde
init|=
operator|new
name|PdfDocEncoding
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|tmp
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|tmp
control|)
block|{
specifier|final
name|String
name|character
init|=
name|pde
operator|.
name|getCharacter
argument_list|(
operator|(
name|b
operator|+
literal|256
operator|)
operator|%
literal|256
argument_list|)
decl_stmt|;
if|if
condition|(
name|character
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|character
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|String
argument_list|(
name|getBytes
argument_list|()
argument_list|,
name|start
argument_list|,
name|data
operator|.
name|length
operator|-
name|start
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// should never happen
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|retval
operator|=
operator|new
name|String
argument_list|(
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|str
operator|=
name|retval
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * This will append a byte[] to the string.      *       * @param data      *            The byte[] to add to this string.      *       * @throws IOException      *             If an IO error occurs while writing the byte.      */
specifier|public
name|void
name|append
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|this
operator|.
name|str
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will append a byte to the string.      *       * @param in      *            The byte to add to this string.      *       * @throws IOException      *             If an IO error occurs while writing the byte.      */
specifier|public
name|void
name|append
parameter_list|(
name|int
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|str
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will reset the internal buffer.      */
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|this
operator|.
name|str
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will get the bytes of the string.      *       * @return A byte array that represents the string.      */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
name|this
operator|.
name|getString
argument_list|()
operator|+
literal|"}"
return|;
block|}
comment|/**      * This will output this string as a PDF object.      *       * @param output      *            The stream to write to.      * @throws IOException      *             If there is an error writing to the stream.      */
specifier|public
name|void
name|writePDF
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|outsideASCII
init|=
literal|false
decl_stmt|;
comment|// Lets first check if we need to escape this string.
name|byte
index|[]
name|bytes
init|=
name|getBytes
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|bytes
operator|.
name|length
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
operator|&&
operator|!
name|outsideASCII
condition|;
name|i
operator|++
control|)
block|{
comment|// if the byte is negative then it is an eight bit byte and is
comment|// outside the ASCII range.
name|outsideASCII
operator|=
name|bytes
index|[
name|i
index|]
operator|<
literal|0
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|outsideASCII
operator|&&
operator|!
name|forceHexForm
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|STRING_OPEN
argument_list|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|b
init|=
operator|(
name|bytes
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
decl_stmt|;
switch|switch
condition|(
name|b
condition|)
block|{
case|case
literal|'('
case|:
case|case
literal|')'
case|:
case|case
literal|'\\'
case|:
block|{
name|output
operator|.
name|write
argument_list|(
name|ESCAPE
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|10
case|:
comment|// LF
block|{
name|output
operator|.
name|write
argument_list|(
name|LF_ESCAPE
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|13
case|:
comment|// CR
block|{
name|output
operator|.
name|write
argument_list|(
name|CR_ESCAPE
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|'\t'
case|:
block|{
name|output
operator|.
name|write
argument_list|(
name|HT_ESCAPE
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|'\b'
case|:
block|{
name|output
operator|.
name|write
argument_list|(
name|BS_ESCAPE
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|'\f'
case|:
block|{
name|output
operator|.
name|write
argument_list|(
name|FF_ESCAPE
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
block|{
name|output
operator|.
name|write
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|output
operator|.
name|write
argument_list|(
name|STRING_CLOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|write
argument_list|(
name|HEX_STRING_OPEN
argument_list|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|output
operator|.
name|write
argument_list|(
name|COSHEXTable
operator|.
name|TABLE
index|[
operator|(
name|bytes
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
index|]
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|write
argument_list|(
name|HEX_STRING_CLOSE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * visitor pattern double dispatch method.      *       * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException If an error occurs while visiting this object.      * @throws CryptographyException If an error occurs while visiting this object.      */
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
throws|,
name|CryptographyException
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
comment|/**      * {@inheritDoc}      */
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
name|this
operator|.
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
name|this
operator|.
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
comment|/**      * {@inheritDoc}      */
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
name|getString
argument_list|()
operator|.
name|hashCode
argument_list|()
decl_stmt|;
return|return
name|result
operator|+=
name|forceHexForm
condition|?
literal|17
else|:
literal|0
return|;
block|}
block|}
end_class

end_unit

