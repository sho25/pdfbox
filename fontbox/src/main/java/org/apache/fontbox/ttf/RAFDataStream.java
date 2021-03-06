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
comment|/**  * An implementation of the TTFDataStream that goes against a RAF.  *   * @author Ben Litchfield  */
end_comment

begin_class
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
specifier|private
specifier|static
specifier|final
name|int
name|BUFFERSIZE
init|=
literal|16384
decl_stmt|;
comment|/**      * Constructor.      *       * @param name The raf file.      * @param mode The mode to open the RAF.      *       * @throws IOException If there is a problem creating the RAF.      *       * @see RandomAccessFile#RandomAccessFile( String, String )      */
name|RAFDataStream
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|IOException
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
comment|/**      * Constructor.      *       * @param file The raf file.      * @param mode The mode to open the RAF.      *       * @throws IOException If there is a problem creating the RAF.      *       * @see RandomAccessFile#RandomAccessFile( File, String )      */
name|RAFDataStream
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|IOException
block|{
name|raf
operator|=
operator|new
name|BufferedRandomAccessFile
argument_list|(
name|file
argument_list|,
name|mode
argument_list|,
name|BUFFERSIZE
argument_list|)
expr_stmt|;
name|ttfFile
operator|=
name|file
expr_stmt|;
block|}
comment|/**      * Read a signed short.      *       * @return An signed short.      * @throws IOException If there is an error reading the data.      * @see RandomAccessFile#readShort()      */
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|raf
operator|!=
literal|null
condition|)
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
block|}
comment|/**      * Read an unsigned byte.      * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      * @see RandomAccessFile#read()      */
annotation|@
name|Override
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
comment|/**      * Read an unsigned short.      *       * @return An unsigned short.      * @throws IOException If there is an error reading the data.      * @see RandomAccessFile#readUnsignedShort()      */
annotation|@
name|Override
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
comment|/**      * Read a signed 64-bit integer.      *       * @return eight bytes interpreted as a long.      * @throws IOException If there is an error reading the data.      * @see RandomAccessFile#readLong()          */
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|long
name|getOriginalDataSize
parameter_list|()
block|{
return|return
name|ttfFile
operator|.
name|length
argument_list|()
return|;
block|}
block|}
end_class

end_unit

