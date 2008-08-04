begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
name|FilterOutputStream
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

begin_comment
comment|/**  * This class represents an ASCII85 output stream.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|ASCII85OutputStream
extends|extends
name|FilterOutputStream
block|{
specifier|private
name|int
name|lineBreak
decl_stmt|;
specifier|private
name|int
name|count
decl_stmt|;
specifier|private
name|byte
index|[]
name|indata
decl_stmt|;
specifier|private
name|byte
index|[]
name|outdata
decl_stmt|;
comment|/**      * Function produces five ASCII printing characters from      * four bytes of binary data.      */
specifier|private
name|int
name|maxline
decl_stmt|;
specifier|private
name|boolean
name|flushed
decl_stmt|;
specifier|private
name|char
name|terminator
decl_stmt|;
comment|/**      * Constructor.      *      * @param out The output stream to write to.      */
specifier|public
name|ASCII85OutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|super
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|lineBreak
operator|=
literal|36
operator|*
literal|2
expr_stmt|;
name|maxline
operator|=
literal|36
operator|*
literal|2
expr_stmt|;
name|count
operator|=
literal|0
expr_stmt|;
name|indata
operator|=
operator|new
name|byte
index|[
literal|4
index|]
expr_stmt|;
name|outdata
operator|=
operator|new
name|byte
index|[
literal|5
index|]
expr_stmt|;
name|flushed
operator|=
literal|true
expr_stmt|;
name|terminator
operator|=
literal|'~'
expr_stmt|;
block|}
comment|/**      * This will set the terminating character.      *      * @param term The terminating character.      */
specifier|public
name|void
name|setTerminator
parameter_list|(
name|char
name|term
parameter_list|)
block|{
if|if
condition|(
name|term
argument_list|<
literal|118
operator|||
name|term
argument_list|>
literal|126
operator|||
name|term
operator|==
literal|'z'
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Terminator must be 118-126 excluding z"
argument_list|)
throw|;
block|}
name|terminator
operator|=
name|term
expr_stmt|;
block|}
comment|/**      * This will get the terminating character.      *      * @return The terminating character.      */
specifier|public
name|char
name|getTerminator
parameter_list|()
block|{
return|return
name|terminator
return|;
block|}
comment|/**      * This will set the line length that will be used.      *      * @param l The length of the line to use.      */
specifier|public
name|void
name|setLineLength
parameter_list|(
name|int
name|l
parameter_list|)
block|{
if|if
condition|(
name|lineBreak
operator|>
name|l
condition|)
block|{
name|lineBreak
operator|=
name|l
expr_stmt|;
block|}
name|maxline
operator|=
name|l
expr_stmt|;
block|}
comment|/**      * This will get the length of the line.      *      * @return The line length attribute.      */
specifier|public
name|int
name|getLineLength
parameter_list|()
block|{
return|return
name|maxline
return|;
block|}
comment|/**      * This will transform the next four ascii bytes.      */
specifier|private
specifier|final
name|void
name|transformASCII85
parameter_list|()
block|{
name|long
name|word
decl_stmt|;
name|word
operator|=
operator|(
operator|(
operator|(
operator|(
name|indata
index|[
literal|0
index|]
operator|<<
literal|8
operator|)
operator||
operator|(
name|indata
index|[
literal|1
index|]
operator|&
literal|0xFF
operator|)
operator|)
operator|<<
literal|16
operator|)
operator||
operator|(
operator|(
name|indata
index|[
literal|2
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|8
operator|)
operator||
operator|(
name|indata
index|[
literal|3
index|]
operator|&
literal|0xFF
operator|)
operator|)
operator|&
literal|0xFFFFFFFFL
expr_stmt|;
comment|// System.out.println("word=0x"+Long.toString(word,16)+" "+word);
if|if
condition|(
name|word
operator|==
literal|0
condition|)
block|{
name|outdata
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
literal|'z'
expr_stmt|;
name|outdata
index|[
literal|1
index|]
operator|=
literal|0
expr_stmt|;
return|return;
block|}
name|long
name|x
decl_stmt|;
name|x
operator|=
name|word
operator|/
operator|(
literal|85L
operator|*
literal|85L
operator|*
literal|85L
operator|*
literal|85L
operator|)
expr_stmt|;
comment|// System.out.println("x0="+x);
name|outdata
index|[
literal|0
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|x
operator|+
literal|'!'
argument_list|)
expr_stmt|;
name|word
operator|-=
name|x
operator|*
literal|85L
operator|*
literal|85L
operator|*
literal|85L
operator|*
literal|85L
expr_stmt|;
name|x
operator|=
name|word
operator|/
operator|(
literal|85L
operator|*
literal|85L
operator|*
literal|85L
operator|)
expr_stmt|;
comment|// System.out.println("x1="+x);
name|outdata
index|[
literal|1
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|x
operator|+
literal|'!'
argument_list|)
expr_stmt|;
name|word
operator|-=
name|x
operator|*
literal|85L
operator|*
literal|85L
operator|*
literal|85L
expr_stmt|;
name|x
operator|=
name|word
operator|/
operator|(
literal|85L
operator|*
literal|85L
operator|)
expr_stmt|;
comment|// System.out.println("x2="+x);
name|outdata
index|[
literal|2
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|x
operator|+
literal|'!'
argument_list|)
expr_stmt|;
name|word
operator|-=
name|x
operator|*
literal|85L
operator|*
literal|85L
expr_stmt|;
name|x
operator|=
name|word
operator|/
literal|85L
expr_stmt|;
comment|// System.out.println("x3="+x);
name|outdata
index|[
literal|3
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|x
operator|+
literal|'!'
argument_list|)
expr_stmt|;
comment|// word-=x*85L;
comment|// System.out.println("x4="+(word % 85L));
name|outdata
index|[
literal|4
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
operator|(
name|word
operator|%
literal|85L
operator|)
operator|+
literal|'!'
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will write a single byte.      *      * @param b The byte to write.      *      * @throws IOException If there is an error writing to the stream.      */
specifier|public
specifier|final
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|flushed
operator|=
literal|false
expr_stmt|;
name|indata
index|[
name|count
operator|++
index|]
operator|=
operator|(
name|byte
operator|)
name|b
expr_stmt|;
if|if
condition|(
name|count
operator|<
literal|4
condition|)
block|{
return|return;
block|}
name|transformASCII85
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|outdata
index|[
name|i
index|]
operator|==
literal|0
condition|)
block|{
break|break;
block|}
name|out
operator|.
name|write
argument_list|(
name|outdata
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
operator|--
name|lineBreak
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|lineBreak
operator|=
name|maxline
expr_stmt|;
block|}
block|}
name|count
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * This will write a chunk of data to the stream.      *      * @param b The byte buffer to read from.      * @param off The offset into the buffer.      * @param sz The number of bytes to read from the buffer.      *      * @throws IOException If there is an error writing to the underlying stream.      */
specifier|public
specifier|final
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|sz
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|sz
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|count
operator|<
literal|3
condition|)
block|{
name|indata
index|[
name|count
operator|++
index|]
operator|=
name|b
index|[
name|off
operator|+
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|write
argument_list|(
name|b
index|[
name|off
operator|+
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will flush the data to the stream.      *      * @throws IOException If there is an error writing the data to the stream.      */
specifier|public
specifier|final
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|flushed
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
name|count
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|indata
index|[
name|i
index|]
operator|=
literal|0
expr_stmt|;
block|}
name|transformASCII85
argument_list|()
expr_stmt|;
if|if
condition|(
name|outdata
index|[
literal|0
index|]
operator|==
literal|'z'
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
comment|// expand 'z',
block|{
name|outdata
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
literal|'!'
expr_stmt|;
block|}
block|}
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
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|outdata
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
operator|--
name|lineBreak
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|lineBreak
operator|=
name|maxline
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|--
name|lineBreak
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
name|terminator
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|count
operator|=
literal|0
expr_stmt|;
name|lineBreak
operator|=
name|maxline
expr_stmt|;
name|flushed
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will close the stream.      *      * @throws IOException If there is an error closing the wrapped stream.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|indata
operator|=
name|outdata
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * This will flush the stream.      *      * @throws Throwable If there is an error.      */
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
try|try
block|{
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

