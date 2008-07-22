begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.fontbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of fontbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.fontbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|ttf
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
name|FileNotFoundException
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
name|RandomAccessFile
import|;
end_import

begin_comment
comment|/**  * An implementation of the TTFDataStream that goes against a RAF.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|RAFDataStream
extends|extends
name|TTFDataStream
block|{
specifier|private
name|RandomAccessFile
name|raf
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|ttfFile
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *       * @param name The raf file.      * @param mode The mode to open the RAF.      *       * @throws FileNotFoundException If there is a problem creating the RAF.      *       * @see RandomAccessFile#RandomAccessFile( String, String )      */
specifier|public
name|RAFDataStream
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param file The raf file.      * @param mode The mode to open the RAF.      *       * @throws FileNotFoundException If there is a problem creating the RAF.      *       * @see RandomAccessFile#RandomAccessFile( File, String )      */
specifier|public
name|RAFDataStream
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|raf
operator|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|ttfFile
operator|=
name|file
expr_stmt|;
block|}
comment|/**      * Read an signed short.      *       * @return An signed short.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|short
name|readSignedShort
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|raf
operator|.
name|readShort
argument_list|()
return|;
block|}
comment|/**      * Get the current position in the stream.      * @return The current position in the stream.      * @throws IOException If an error occurs while reading the stream.      */
specifier|public
name|long
name|getCurrentPosition
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|raf
operator|.
name|getFilePointer
argument_list|()
return|;
block|}
comment|/**      * Close the underlying resources.      *       * @throws IOException If there is an error closing the resources.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
name|raf
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Read an unsigned byte.      * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|raf
operator|.
name|read
argument_list|()
return|;
block|}
comment|/**      * Read an unsigned short.      *       * @return An unsigned short.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
name|readUnsignedShort
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|raf
operator|.
name|readUnsignedShort
argument_list|()
return|;
block|}
comment|/**      * Read an unsigned byte.      * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|long
name|readLong
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|raf
operator|.
name|readLong
argument_list|()
return|;
block|}
comment|/**      * Seek into the datasource.      *       * @param pos The position to seek to.      * @throws IOException If there is an error seeking to that position.      */
specifier|public
name|void
name|seek
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|IOException
block|{
name|raf
operator|.
name|seek
argument_list|(
name|pos
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see java.io.InputStream#read( byte[], int, int )      *       * @param b The buffer to write to.      * @param off The offset into the buffer.      * @param len The length into the buffer.      *       * @return The number of bytes read.      *       * @throws IOException If there is an error reading from the stream.      */
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
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
return|return
name|raf
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|InputStream
name|getOriginalData
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
name|ttfFile
argument_list|)
return|;
block|}
block|}
end_class

end_unit

