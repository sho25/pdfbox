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
name|ByteArrayInputStream
import|;
end_import

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
name|EOFException
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
comment|/**  * An interface into a data stream.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|MemoryTTFDataStream
extends|extends
name|TTFDataStream
block|{
specifier|private
name|byte
index|[]
name|data
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|currentPosition
init|=
literal|0
decl_stmt|;
comment|/**      * Constructor from a stream.       * @param is The stream of read from.      * @throws IOException If an error occurs while reading from the stream.      */
specifier|public
name|MemoryTTFDataStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|is
operator|.
name|available
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|data
operator|=
name|output
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
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
operator|(
call|(
name|long
call|)
argument_list|(
name|readSignedInt
argument_list|()
argument_list|)
operator|<<
literal|32
operator|)
operator|+
operator|(
name|readSignedInt
argument_list|()
operator|&
literal|0xFFFFFFFFL
operator|)
return|;
block|}
comment|/**      * Read a signed integer.      *       * @return A signed integer.      * @throws IOException If there is a problem reading the file.      */
specifier|public
name|int
name|readSignedInt
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|ch1
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|ch2
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|ch3
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|ch4
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|ch1
operator||
name|ch2
operator||
name|ch3
operator||
name|ch4
operator|)
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
return|return
operator|(
operator|(
name|ch1
operator|<<
literal|24
operator|)
operator|+
operator|(
name|ch2
operator|<<
literal|16
operator|)
operator|+
operator|(
name|ch3
operator|<<
literal|8
operator|)
operator|+
operator|(
name|ch4
operator|<<
literal|0
operator|)
operator|)
return|;
block|}
comment|/**      * Read an unsigned byte.      * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|currentPosition
operator|<
name|data
operator|.
name|length
condition|)
block|{
name|retval
operator|=
name|data
index|[
name|currentPosition
index|]
expr_stmt|;
block|}
name|currentPosition
operator|++
expr_stmt|;
return|return
operator|(
name|retval
operator|+
literal|256
operator|)
operator|%
literal|256
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
name|int
name|ch1
init|=
name|this
operator|.
name|read
argument_list|()
decl_stmt|;
name|int
name|ch2
init|=
name|this
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|ch1
operator||
name|ch2
operator|)
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
return|return
operator|(
name|ch1
operator|<<
literal|8
operator|)
operator|+
operator|(
name|ch2
operator|<<
literal|0
operator|)
return|;
block|}
comment|/**      * Read an signed short.      *       * @return An signed short.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|short
name|readSignedShort
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|ch1
init|=
name|this
operator|.
name|read
argument_list|()
decl_stmt|;
name|int
name|ch2
init|=
name|this
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|ch1
operator||
name|ch2
operator|)
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
return|return
call|(
name|short
call|)
argument_list|(
operator|(
name|ch1
operator|<<
literal|8
operator|)
operator|+
operator|(
name|ch2
operator|<<
literal|0
operator|)
argument_list|)
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
name|data
operator|=
literal|null
expr_stmt|;
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
name|currentPosition
operator|=
operator|(
name|int
operator|)
name|pos
expr_stmt|;
block|}
comment|/**      * @see java.io.InputStream#read( byte[], int, int )      *       * @param b The buffer to write to.      * @param off The offset into the buffer.      * @param len The length into the buffer.      *       * @return The number of bytes read, or -1 at the end of the stream      *       * @throws IOException If there is an error reading from the stream.      */
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
if|if
condition|(
name|currentPosition
operator|<
name|data
operator|.
name|length
condition|)
block|{
name|int
name|amountRead
init|=
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|data
operator|.
name|length
operator|-
name|currentPosition
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|currentPosition
argument_list|,
name|b
argument_list|,
name|off
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
name|currentPosition
operator|+=
name|amountRead
expr_stmt|;
return|return
name|amountRead
return|;
block|}
else|else
block|{
return|return
operator|-
literal|1
return|;
block|}
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
name|currentPosition
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
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
block|}
end_class

end_unit

