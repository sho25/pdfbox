begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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

begin_comment
comment|/**  * A simple subclass that adds a few convience methods.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PushBackInputStream
extends|extends
name|java
operator|.
name|io
operator|.
name|PushbackInputStream
block|{
comment|/**      * Constructor.      *      * @param input The input stream.      * @param size The size of the push back buffer.      *       * @throws IOException If there is an error with the stream.       */
specifier|public
name|PushBackInputStream
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
argument_list|,
name|size
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: input was null"
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will peek at the next byte.      *      * @return The next byte on the stream, leaving it as available to read.      *      * @throws IOException If there is an error reading the next byte.      */
specifier|public
name|int
name|peek
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|result
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
operator|-
literal|1
condition|)
block|{
name|unread
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * A simple test to see if we are at the end of the stream.      *      * @return true if we are at the end of the stream.      *      * @throws IOException If there is an error reading the next byte.      */
specifier|public
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|peek
init|=
name|peek
argument_list|()
decl_stmt|;
return|return
name|peek
operator|==
operator|-
literal|1
return|;
block|}
comment|/**      * This is a method used to fix PDFBox issue 974661, the PDF parsing code needs      * to know if there is at least x amount of data left in the stream, but the available()      * method returns how much data will be available without blocking.  PDFBox is willing to      * block to read the data, so we will first fill the internal buffer.      *       * @throws IOException If there is an error filling the buffer.      */
specifier|public
name|void
name|fillBuffer
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|bufferLength
init|=
name|buf
operator|.
name|length
decl_stmt|;
name|byte
index|[]
name|tmpBuffer
init|=
operator|new
name|byte
index|[
name|bufferLength
index|]
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
name|int
name|totalAmountRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|amountRead
operator|!=
operator|-
literal|1
operator|&&
name|totalAmountRead
operator|<
name|bufferLength
condition|)
block|{
name|amountRead
operator|=
name|this
operator|.
name|read
argument_list|(
name|tmpBuffer
argument_list|,
name|totalAmountRead
argument_list|,
name|bufferLength
operator|-
name|totalAmountRead
argument_list|)
expr_stmt|;
if|if
condition|(
name|amountRead
operator|!=
operator|-
literal|1
condition|)
block|{
name|totalAmountRead
operator|+=
name|amountRead
expr_stmt|;
block|}
block|}
name|this
operator|.
name|unread
argument_list|(
name|tmpBuffer
argument_list|,
literal|0
argument_list|,
name|totalAmountRead
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

