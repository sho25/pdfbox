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
name|cff
package|;
end_package

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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
comment|/**  * This class contains some functionality to read a byte buffer.  *   * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
class|class
name|DataInput
block|{
specifier|private
name|byte
index|[]
name|inputBuffer
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|bufferPosition
init|=
literal|0
decl_stmt|;
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
name|DataInput
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      * @param buffer the buffer to be read      */
specifier|public
name|DataInput
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
name|inputBuffer
operator|=
name|buffer
expr_stmt|;
block|}
comment|/**      * Determines if there are any bytes left to read or not.       * @return true if there are any bytes left to read      */
specifier|public
name|boolean
name|hasRemaining
parameter_list|()
block|{
return|return
name|bufferPosition
operator|<
name|inputBuffer
operator|.
name|length
return|;
block|}
comment|/**      * Returns the current position.      * @return current position      */
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|bufferPosition
return|;
block|}
comment|/**      * Sets the current position to the given value.      * @param position the given position      */
specifier|public
name|void
name|setPosition
parameter_list|(
name|int
name|position
parameter_list|)
block|{
name|bufferPosition
operator|=
name|position
expr_stmt|;
block|}
comment|/**       * Returns the buffer as an ISO-8859-1 string.      * @return the buffer as string      * @throws IOException if an error occurs during reading      */
specifier|public
name|String
name|getString
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|String
argument_list|(
name|inputBuffer
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
return|;
block|}
comment|/**      * Read one single byte from the buffer.      * @return the byte      * @throws IOException if an error occurs during reading      */
specifier|public
name|byte
name|readByte
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|byte
name|value
init|=
name|inputBuffer
index|[
name|bufferPosition
index|]
decl_stmt|;
name|bufferPosition
operator|++
expr_stmt|;
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"An error occured reading a byte - returning -1"
argument_list|,
name|re
argument_list|)
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
block|}
comment|/**      * Read one single unsigned byte from the buffer.      * @return the unsigned byte as int      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readUnsignedByte
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|b
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
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
name|b
return|;
block|}
comment|/**      * Peeks one single unsigned byte from the buffer.      * @return the unsigned byte as int      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|peekUnsignedByte
parameter_list|(
name|int
name|offset
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|b
init|=
name|peek
argument_list|(
name|offset
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
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
name|b
return|;
block|}
comment|/**      * Read one single short value from the buffer.      * @return the short value      * @throws IOException if an error occurs during reading      */
specifier|public
name|short
name|readShort
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|short
operator|)
name|readUnsignedShort
argument_list|()
return|;
block|}
comment|/**      * Read one single unsigned short (2 bytes) value from the buffer.      * @return the unsigned short value as int      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readUnsignedShort
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|b1
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|b2
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|b1
operator||
name|b2
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
name|b1
operator|<<
literal|8
operator||
name|b2
return|;
block|}
comment|/**      * Read one single int (4 bytes) from the buffer.      * @return the int value      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readInt
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|b1
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|b2
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|b3
init|=
name|read
argument_list|()
decl_stmt|;
name|int
name|b4
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|b1
operator||
name|b2
operator||
name|b3
operator||
name|b4
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
name|b1
operator|<<
literal|24
operator||
name|b2
operator|<<
literal|16
operator||
name|b3
operator|<<
literal|8
operator||
name|b4
return|;
block|}
comment|/**      * Read a number of single byte values from the buffer.      * @param length the number of bytes to be read      * @return an array with containing the bytes from the buffer       * @throws IOException if an error occurs during reading      */
specifier|public
name|byte
index|[]
name|readBytes
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|inputBuffer
operator|.
name|length
operator|-
name|bufferPosition
operator|<
name|length
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|inputBuffer
argument_list|,
name|bufferPosition
argument_list|,
name|bytes
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|bufferPosition
operator|+=
name|length
expr_stmt|;
return|return
name|bytes
return|;
block|}
specifier|private
name|int
name|read
parameter_list|()
block|{
try|try
block|{
name|int
name|value
init|=
name|inputBuffer
index|[
name|bufferPosition
index|]
operator|&
literal|0xff
decl_stmt|;
name|bufferPosition
operator|++
expr_stmt|;
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"An error occured reading an int - returning -1"
argument_list|,
name|re
argument_list|)
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|private
name|int
name|peek
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
try|try
block|{
return|return
name|inputBuffer
index|[
name|bufferPosition
operator|+
name|offset
index|]
operator|&
literal|0xff
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"An error occured peeking at offset "
operator|+
name|offset
operator|+
literal|" - returning -1"
argument_list|,
name|re
argument_list|)
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|public
name|int
name|length
parameter_list|()
block|{
return|return
name|inputBuffer
operator|.
name|length
return|;
block|}
block|}
end_class

end_unit

