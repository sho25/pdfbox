begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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

begin_comment
comment|/**  * PushBackInputStream for byte arrays.   *   * The inheritance from PushBackInputStream is only to avoid the  * introduction of an interface with all PushBackInputStream  * methods. The parent PushBackInputStream is not used in any way and  * all methods are overridden. (Thus when adding new methods to PushBackInputStream  * override them in this class as well!)  * unread() is limited to the number of bytes already read from this stream (i.e.  * the current position in the array). This limitation usually poses no problem   * to a parser, but allows for some optimization since only one array has to  * be dealt with.  *   * Note: This class is not thread safe. Clients must provide synchronization  * if needed.  *   * Note: Calling unread() after mark() will cause (part of) the unread data to be   * read again after reset(). Thus do not call unread() between mark() and reset().  *   * @author Andreas Weiss (andreas.weiss@switzerland.org)  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|ByteArrayPushBackInputStream
extends|extends
name|PushBackInputStream
block|{
specifier|private
name|byte
index|[]
name|data
decl_stmt|;
specifier|private
name|int
name|datapos
decl_stmt|;
specifier|private
name|int
name|datalen
decl_stmt|;
specifier|private
name|int
name|save
decl_stmt|;
comment|// dummy for base class constructor
specifier|private
specifier|static
specifier|final
name|InputStream
name|DUMMY
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|""
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      * @param input Data to read from. Note that calls to unread() will      * modify this array! If this is not desired, pass a copy.      *       * @throws IOException If there is an IO error.      */
specifier|public
name|ByteArrayPushBackInputStream
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|DUMMY
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|data
operator|=
name|input
expr_stmt|;
name|datapos
operator|=
literal|0
expr_stmt|;
name|save
operator|=
name|datapos
expr_stmt|;
name|datalen
operator|=
name|input
operator|!=
literal|null
condition|?
name|input
operator|.
name|length
else|:
literal|0
expr_stmt|;
block|}
comment|/**      * This will peek at the next byte.      *      * @return The next byte on the stream, leaving it as available to read.      */
specifier|public
name|int
name|peek
parameter_list|()
block|{
try|try
block|{
comment|// convert negative values to 128..255
return|return
operator|(
name|data
index|[
name|datapos
index|]
operator|+
literal|0x100
operator|)
operator|&
literal|0xff
return|;
block|}
catch|catch
parameter_list|(
name|ArrayIndexOutOfBoundsException
name|ex
parameter_list|)
block|{
comment|// could check this before, but this is a rare case
comment|// and this method is called sufficiently often to justify this
comment|// optimization
return|return
operator|-
literal|1
return|;
block|}
block|}
comment|/**      * A simple test to see if we are at the end of the stream.      *      * @return true if we are at the end of the stream.      */
specifier|public
name|boolean
name|isEOF
parameter_list|()
block|{
return|return
name|datapos
operator|>=
name|datalen
return|;
block|}
comment|/**      * Save the state of this stream.      * @param readlimit Has no effect.      * @see InputStream#mark(int)      */
specifier|public
name|void
name|mark
parameter_list|(
name|int
name|readlimit
parameter_list|)
block|{
if|if
condition|(
literal|false
condition|)
block|{
operator|++
name|readlimit
expr_stmt|;
comment|// avoid unused param warning
block|}
name|save
operator|=
name|datapos
expr_stmt|;
block|}
comment|/**      * Check if mark is supported.      * @return Always true.      * @see InputStream#markSupported()      */
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Restore the state of this stream to the last saveState call.      * @see InputStream#reset()      */
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|datapos
operator|=
name|save
expr_stmt|;
block|}
comment|/** Available bytes.      * @see InputStream#available()      * @return Available bytes.      */
specifier|public
name|int
name|available
parameter_list|()
block|{
name|int
name|av
init|=
name|datalen
operator|-
name|datapos
decl_stmt|;
return|return
name|av
operator|>
literal|0
condition|?
name|av
else|:
literal|0
return|;
block|}
comment|/** Totally available bytes in the underlying array.      * @return Available bytes.      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|datalen
return|;
block|}
comment|/**      * Pushes back a byte.       * After this method returns, the next byte to be read will have the value (byte)by.      * @param by the int value whose low-order byte is to be pushed back.       * @throws IOException - If there is not enough room in the buffer for the byte.      * @see java.io.PushbackInputStream#unread(int)      */
specifier|public
name|void
name|unread
parameter_list|(
name|int
name|by
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|datapos
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"ByteArrayParserInputStream.unread(int): "
operator|+
literal|"cannot unread 1 byte at buffer position "
operator|+
name|datapos
argument_list|)
throw|;
block|}
operator|--
name|datapos
expr_stmt|;
name|data
index|[
name|datapos
index|]
operator|=
operator|(
name|byte
operator|)
name|by
expr_stmt|;
block|}
comment|/**      * Pushes back a portion of an array of bytes by copying it to the       * front of the pushback buffer. After this method returns, the next byte       * to be read will have the value b[off], the byte after that will have       * the value b[off+1], and so forth.      * @param buffer the byte array to push back.      * @param off the start offset of the data.      * @param len the number of bytes to push back.       * @throws IOException If there is not enough room in the pushback buffer       * for the specified number of bytes.      * @see java.io.PushbackInputStream#unread(byte[], int, int)      */
specifier|public
name|void
name|unread
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|len
operator|<=
literal|0
operator|||
name|off
operator|>=
name|buffer
operator|.
name|length
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|off
operator|<
literal|0
condition|)
block|{
name|off
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
name|buffer
operator|.
name|length
condition|)
block|{
name|len
operator|=
name|buffer
operator|.
name|length
expr_stmt|;
block|}
name|localUnread
argument_list|(
name|buffer
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pushes back a portion of an array of bytes by copying it to the       * front of the pushback buffer. After this method returns, the next byte       * to be read will have the value buffer[0], the byte after that will have       * the value buffer[1], and so forth.      * @param buffer the byte array to push back.      * @throws IOException If there is not enough room in the pushback buffer       * for the specified number of bytes.      * @see java.io.PushbackInputStream#unread(byte[])      */
specifier|public
name|void
name|unread
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
name|localUnread
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pushes back a portion of an array of bytes by copying it to the       * front of the pushback buffer. After this method returns, the next byte       * to be read will have the value buffer[off], the byte after that will have       * the value buffer[off+1], and so forth.      * Internal method that assumes off and len to be valid.      * @param buffer the byte array to push back.      * @param off the start offset of the data.      * @param len the number of bytes to push back.       * @throws IOException If there is not enough room in the pushback buffer       * for the specified number of bytes.      * @see java.io.PushbackInputStream#unread(byte[], int, int)      */
specifier|private
name|void
name|localUnread
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|datapos
operator|<
name|len
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"ByteArrayParserInputStream.unread(int): "
operator|+
literal|"cannot unread "
operator|+
name|len
operator|+
literal|" bytes at buffer position "
operator|+
name|datapos
argument_list|)
throw|;
block|}
name|datapos
operator|-=
name|len
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|off
argument_list|,
name|data
argument_list|,
name|datapos
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
comment|/**       * Read a byte.      * @see InputStream#read()      * @return Byte read or -1 if no more bytes are available.      */
specifier|public
name|int
name|read
parameter_list|()
block|{
try|try
block|{
comment|// convert negative values to 128..255
return|return
operator|(
name|data
index|[
name|datapos
operator|++
index|]
operator|+
literal|0x100
operator|)
operator|&
literal|0xff
return|;
block|}
catch|catch
parameter_list|(
name|ArrayIndexOutOfBoundsException
name|ex
parameter_list|)
block|{
comment|// could check this before, but this is a rare case
comment|// and this method is called sufficiently often to justify this
comment|// optimization
name|datapos
operator|=
name|datalen
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
block|}
comment|/**       * Read a number of bytes.      * @see InputStream#read(byte[])      * @param buffer the buffer into which the data is read.      * @return the total number of bytes read into the buffer, or -1 if there       * is no more data because the end of the stream has been reached.      */
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
return|return
name|localRead
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**       * Read a number of bytes.      * @see InputStream#read(byte[], int, int)      * @param buffer the buffer into which the data is read.      * @param off the start offset in array buffer at which the data is written.      * @param len the maximum number of bytes to read.      * @return the total number of bytes read into the buffer, or -1 if there       * is no more data because the end of the stream has been reached.      */
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
name|len
operator|<=
literal|0
operator|||
name|off
operator|>=
name|buffer
operator|.
name|length
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|off
operator|<
literal|0
condition|)
block|{
name|off
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
name|buffer
operator|.
name|length
condition|)
block|{
name|len
operator|=
name|buffer
operator|.
name|length
expr_stmt|;
block|}
return|return
name|localRead
argument_list|(
name|buffer
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
comment|/**       * Read a number of bytes. Internal method that assumes off and len to be       * valid.      * @see InputStream#read(byte[], int, int)      * @param buffer the buffer into which the data is read.      * @param off the start offset in array buffer at which the data is written.      * @param len the maximum number of bytes to read.      * @return the total number of bytes read into the buffer, or -1 if there       * is no more data because the end of the stream has been reached.      */
specifier|public
name|int
name|localRead
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
comment|// must return 0 even if at end!
block|}
elseif|else
if|if
condition|(
name|datapos
operator|>=
name|datalen
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
name|int
name|newpos
init|=
name|datapos
operator|+
name|len
decl_stmt|;
if|if
condition|(
name|newpos
operator|>
name|datalen
condition|)
block|{
name|newpos
operator|=
name|datalen
expr_stmt|;
name|len
operator|=
name|newpos
operator|-
name|datapos
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|datapos
argument_list|,
name|buffer
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|datapos
operator|=
name|newpos
expr_stmt|;
return|return
name|len
return|;
block|}
block|}
comment|/**      * Skips over and discards n bytes of data from this input stream.       * The skip method may, for a variety of reasons, end up skipping over some       * smaller number of bytes, possibly 0. This may result from any of a number       * of conditions; reaching end of file before n bytes have been skipped is       * only one possibility. The actual number of bytes skipped is returned.       * If n is negative, no bytes are skipped.      * @param num the number of bytes to be skipped.       * @return the actual number of bytes skipped.       * @see InputStream#skip(long)      */
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|num
parameter_list|)
block|{
if|if
condition|(
name|num
operator|<=
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
name|long
name|newpos
init|=
name|datapos
operator|+
name|num
decl_stmt|;
if|if
condition|(
name|newpos
operator|>=
name|datalen
condition|)
block|{
name|num
operator|=
name|datalen
operator|-
name|datapos
expr_stmt|;
name|datapos
operator|=
name|datalen
expr_stmt|;
block|}
else|else
block|{
name|datapos
operator|=
operator|(
name|int
operator|)
name|newpos
expr_stmt|;
block|}
return|return
name|num
return|;
block|}
block|}
comment|/** Position the stream at a given index. Positioning the stream      * at position size() will cause the next call to read() to return -1.      *       * @param newpos Position in the underlying array. A negative value will be       * interpreted as 0, a value greater than size() as size().      * @return old position.      */
specifier|public
name|int
name|seek
parameter_list|(
name|int
name|newpos
parameter_list|)
block|{
if|if
condition|(
name|newpos
operator|<
literal|0
condition|)
block|{
name|newpos
operator|=
literal|0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|newpos
operator|>
name|datalen
condition|)
block|{
name|newpos
operator|=
name|datalen
expr_stmt|;
block|}
name|int
name|oldpos
init|=
name|pos
decl_stmt|;
name|pos
operator|=
name|newpos
expr_stmt|;
return|return
name|oldpos
return|;
block|}
block|}
end_class

end_unit

