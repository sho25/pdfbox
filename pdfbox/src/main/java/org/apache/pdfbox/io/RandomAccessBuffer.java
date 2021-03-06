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
name|io
package|;
end_package

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * An implementation of the RandomAccess interface to store data in memory.  * The data will be stored in chunks organized in an ArrayList.  */
end_comment

begin_class
specifier|public
class|class
name|RandomAccessBuffer
implements|implements
name|RandomAccess
implements|,
name|Cloneable
block|{
comment|// default chunk size is 1kb
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CHUNK_SIZE
init|=
literal|1024
decl_stmt|;
comment|// use the default chunk size
specifier|private
name|int
name|chunkSize
init|=
name|DEFAULT_CHUNK_SIZE
decl_stmt|;
comment|// list containing all chunks
specifier|private
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|bufferList
init|=
literal|null
decl_stmt|;
comment|// current chunk
specifier|private
name|byte
index|[]
name|currentBuffer
decl_stmt|;
comment|// current pointer to the whole buffer
specifier|private
name|long
name|pointer
decl_stmt|;
comment|// current pointer for the current chunk
specifier|private
name|int
name|currentBufferPointer
decl_stmt|;
comment|// size of the whole buffer
specifier|private
name|long
name|size
decl_stmt|;
comment|// current chunk list index
specifier|private
name|int
name|bufferListIndex
decl_stmt|;
comment|// maximum chunk list index
specifier|private
name|int
name|bufferListMaxIndex
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|RandomAccessBuffer
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_CHUNK_SIZE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Default constructor.      */
specifier|private
name|RandomAccessBuffer
parameter_list|(
name|int
name|definedChunkSize
parameter_list|)
block|{
comment|// starting with one chunk
name|bufferList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|chunkSize
operator|=
name|definedChunkSize
expr_stmt|;
name|currentBuffer
operator|=
operator|new
name|byte
index|[
name|chunkSize
index|]
expr_stmt|;
name|bufferList
operator|.
name|add
argument_list|(
name|currentBuffer
argument_list|)
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
name|bufferListIndex
operator|=
literal|0
expr_stmt|;
name|bufferListMaxIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Create a random access buffer using the given byte array.      *       * @param input the byte array to be read      */
specifier|public
name|RandomAccessBuffer
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
block|{
comment|// this is a special case. The given byte array is used as the one
comment|// and only chunk.
name|bufferList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|chunkSize
operator|=
name|input
operator|.
name|length
expr_stmt|;
name|currentBuffer
operator|=
name|input
expr_stmt|;
name|bufferList
operator|.
name|add
argument_list|(
name|currentBuffer
argument_list|)
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
name|chunkSize
expr_stmt|;
name|bufferListIndex
operator|=
literal|0
expr_stmt|;
name|bufferListMaxIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Create a random access buffer of the given input stream by copying the data.      *       * @param input the input stream to be read      * @throws IOException if something went wrong while copying the data      */
specifier|public
name|RandomAccessBuffer
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|byte
index|[]
name|byteBuffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|bytesRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|input
operator|.
name|read
argument_list|(
name|byteBuffer
argument_list|)
operator|)
operator|>
operator|-
literal|1
condition|)
block|{
name|write
argument_list|(
name|byteBuffer
argument_list|,
literal|0
argument_list|,
name|bytesRead
argument_list|)
expr_stmt|;
block|}
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RandomAccessBuffer
name|clone
parameter_list|()
block|{
name|RandomAccessBuffer
name|copy
init|=
operator|new
name|RandomAccessBuffer
argument_list|(
name|chunkSize
argument_list|)
decl_stmt|;
name|copy
operator|.
name|bufferList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|bufferList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
index|[]
name|buffer
range|:
name|bufferList
control|)
block|{
name|byte
index|[]
name|newBuffer
init|=
operator|new
name|byte
index|[
name|buffer
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|newBuffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
expr_stmt|;
name|copy
operator|.
name|bufferList
operator|.
name|add
argument_list|(
name|newBuffer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentBuffer
operator|!=
literal|null
condition|)
block|{
name|copy
operator|.
name|currentBuffer
operator|=
name|copy
operator|.
name|bufferList
operator|.
name|get
argument_list|(
name|copy
operator|.
name|bufferList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|copy
operator|.
name|currentBuffer
operator|=
literal|null
expr_stmt|;
block|}
name|copy
operator|.
name|pointer
operator|=
name|pointer
expr_stmt|;
name|copy
operator|.
name|currentBufferPointer
operator|=
name|currentBufferPointer
expr_stmt|;
name|copy
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|copy
operator|.
name|bufferListIndex
operator|=
name|bufferListIndex
expr_stmt|;
name|copy
operator|.
name|bufferListMaxIndex
operator|=
name|bufferListMaxIndex
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|currentBuffer
operator|=
literal|null
expr_stmt|;
name|bufferList
operator|.
name|clear
argument_list|()
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
name|bufferListIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|bufferList
operator|.
name|clear
argument_list|()
expr_stmt|;
name|currentBuffer
operator|=
operator|new
name|byte
index|[
name|chunkSize
index|]
expr_stmt|;
name|bufferList
operator|.
name|add
argument_list|(
name|currentBuffer
argument_list|)
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
name|bufferListIndex
operator|=
literal|0
expr_stmt|;
name|bufferListMaxIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|seek
parameter_list|(
name|long
name|position
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid position "
operator|+
name|position
argument_list|)
throw|;
block|}
name|pointer
operator|=
name|position
expr_stmt|;
if|if
condition|(
name|pointer
operator|<
name|size
condition|)
block|{
comment|// calculate the chunk list index
name|bufferListIndex
operator|=
call|(
name|int
call|)
argument_list|(
name|pointer
operator|/
name|chunkSize
argument_list|)
expr_stmt|;
name|currentBufferPointer
operator|=
call|(
name|int
call|)
argument_list|(
name|pointer
operator|%
name|chunkSize
argument_list|)
expr_stmt|;
name|currentBuffer
operator|=
name|bufferList
operator|.
name|get
argument_list|(
name|bufferListIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// it is allowed to jump beyond the end of the file
comment|// jump to the end of the buffer
name|bufferListIndex
operator|=
name|bufferListMaxIndex
expr_stmt|;
name|currentBuffer
operator|=
name|bufferList
operator|.
name|get
argument_list|(
name|bufferListIndex
argument_list|)
expr_stmt|;
name|currentBufferPointer
operator|=
call|(
name|int
call|)
argument_list|(
name|size
operator|%
name|chunkSize
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|long
name|getPosition
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|pointer
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|pointer
operator|>=
name|this
operator|.
name|size
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|currentBufferPointer
operator|>=
name|chunkSize
condition|)
block|{
if|if
condition|(
name|bufferListIndex
operator|>=
name|bufferListMaxIndex
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
name|currentBuffer
operator|=
name|bufferList
operator|.
name|get
argument_list|(
operator|++
name|bufferListIndex
argument_list|)
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
block|}
block|}
name|pointer
operator|++
expr_stmt|;
return|return
name|currentBuffer
index|[
name|currentBufferPointer
operator|++
index|]
operator|&
literal|0xff
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|pointer
operator|>=
name|size
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|bytesRead
init|=
name|readRemainingBytes
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
while|while
condition|(
name|bytesRead
argument_list|<
name|length
operator|&&
name|available
operator|(
operator|)
argument_list|>
literal|0
condition|)
block|{
name|bytesRead
operator|+=
name|readRemainingBytes
argument_list|(
name|b
argument_list|,
name|offset
operator|+
name|bytesRead
argument_list|,
name|length
operator|-
name|bytesRead
argument_list|)
expr_stmt|;
if|if
condition|(
name|currentBufferPointer
operator|==
name|chunkSize
condition|)
block|{
name|nextBuffer
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|bytesRead
return|;
block|}
specifier|private
name|int
name|readRemainingBytes
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|pointer
operator|>=
name|size
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|maxLength
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|length
argument_list|,
name|size
operator|-
name|pointer
argument_list|)
decl_stmt|;
name|int
name|remainingBytes
init|=
name|chunkSize
operator|-
name|currentBufferPointer
decl_stmt|;
comment|// no more bytes left
if|if
condition|(
name|remainingBytes
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|maxLength
operator|>=
name|remainingBytes
condition|)
block|{
comment|// copy the remaining bytes from the current buffer
name|System
operator|.
name|arraycopy
argument_list|(
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
name|b
argument_list|,
name|offset
argument_list|,
name|remainingBytes
argument_list|)
expr_stmt|;
comment|// end of file reached
name|currentBufferPointer
operator|+=
name|remainingBytes
expr_stmt|;
name|pointer
operator|+=
name|remainingBytes
expr_stmt|;
return|return
name|remainingBytes
return|;
block|}
else|else
block|{
comment|// copy the remaining bytes from the whole buffer
name|System
operator|.
name|arraycopy
argument_list|(
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
name|b
argument_list|,
name|offset
argument_list|,
name|maxLength
argument_list|)
expr_stmt|;
comment|// end of file reached
name|currentBufferPointer
operator|+=
name|maxLength
expr_stmt|;
name|pointer
operator|+=
name|maxLength
expr_stmt|;
return|return
name|maxLength
return|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|size
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// end of buffer reached?
if|if
condition|(
name|currentBufferPointer
operator|>=
name|chunkSize
condition|)
block|{
if|if
condition|(
name|pointer
operator|+
name|chunkSize
operator|>=
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer overflow"
argument_list|)
throw|;
block|}
name|expandBuffer
argument_list|()
expr_stmt|;
block|}
name|currentBuffer
index|[
name|currentBufferPointer
operator|++
index|]
operator|=
operator|(
name|byte
operator|)
name|b
expr_stmt|;
name|pointer
operator|++
expr_stmt|;
if|if
condition|(
name|pointer
operator|>
name|this
operator|.
name|size
condition|)
block|{
name|this
operator|.
name|size
operator|=
name|pointer
expr_stmt|;
block|}
comment|// end of buffer reached now?
if|if
condition|(
name|currentBufferPointer
operator|>=
name|chunkSize
condition|)
block|{
if|if
condition|(
name|pointer
operator|+
name|chunkSize
operator|>=
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer overflow"
argument_list|)
throw|;
block|}
name|expandBuffer
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|long
name|newSize
init|=
name|pointer
operator|+
name|length
decl_stmt|;
name|int
name|remainingBytes
init|=
name|chunkSize
operator|-
name|currentBufferPointer
decl_stmt|;
if|if
condition|(
name|length
operator|>=
name|remainingBytes
condition|)
block|{
if|if
condition|(
name|newSize
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer overflow"
argument_list|)
throw|;
block|}
comment|// copy the first bytes to the current buffer
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
name|remainingBytes
argument_list|)
expr_stmt|;
name|int
name|newOffset
init|=
name|offset
operator|+
name|remainingBytes
decl_stmt|;
name|long
name|remainingBytes2Write
init|=
name|length
operator|-
name|remainingBytes
decl_stmt|;
comment|// determine how many buffers are needed for the remaining bytes
name|int
name|numberOfNewArrays
init|=
operator|(
name|int
operator|)
name|remainingBytes2Write
operator|/
name|chunkSize
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
name|numberOfNewArrays
condition|;
name|i
operator|++
control|)
block|{
name|expandBuffer
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
name|newOffset
argument_list|,
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
name|chunkSize
argument_list|)
expr_stmt|;
name|newOffset
operator|+=
name|chunkSize
expr_stmt|;
block|}
comment|// are there still some bytes to be written?
name|remainingBytes2Write
operator|-=
name|numberOfNewArrays
operator|*
operator|(
name|long
operator|)
name|chunkSize
expr_stmt|;
if|if
condition|(
name|remainingBytes2Write
operator|>=
literal|0
condition|)
block|{
name|expandBuffer
argument_list|()
expr_stmt|;
if|if
condition|(
name|remainingBytes2Write
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
name|newOffset
argument_list|,
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
operator|(
name|int
operator|)
name|remainingBytes2Write
argument_list|)
expr_stmt|;
block|}
name|currentBufferPointer
operator|=
operator|(
name|int
operator|)
name|remainingBytes2Write
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|currentBuffer
argument_list|,
name|currentBufferPointer
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|currentBufferPointer
operator|+=
name|length
expr_stmt|;
block|}
name|pointer
operator|+=
name|length
expr_stmt|;
if|if
condition|(
name|pointer
operator|>
name|this
operator|.
name|size
condition|)
block|{
name|this
operator|.
name|size
operator|=
name|pointer
expr_stmt|;
block|}
block|}
comment|/**      * create a new buffer chunk and adjust all pointers and indices.      */
specifier|private
name|void
name|expandBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|bufferListMaxIndex
operator|>
name|bufferListIndex
condition|)
block|{
comment|// there is already an existing chunk
name|nextBuffer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// create a new chunk and add it to the buffer
name|currentBuffer
operator|=
operator|new
name|byte
index|[
name|chunkSize
index|]
expr_stmt|;
name|bufferList
operator|.
name|add
argument_list|(
name|currentBuffer
argument_list|)
expr_stmt|;
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|bufferListMaxIndex
operator|++
expr_stmt|;
name|bufferListIndex
operator|++
expr_stmt|;
block|}
block|}
comment|/**      * switch to the next buffer chunk and reset the buffer pointer.      */
specifier|private
name|void
name|nextBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|bufferListIndex
operator|==
name|bufferListMaxIndex
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No more chunks available, end of buffer reached"
argument_list|)
throw|;
block|}
name|currentBufferPointer
operator|=
literal|0
expr_stmt|;
name|currentBuffer
operator|=
name|bufferList
operator|.
name|get
argument_list|(
operator|++
name|bufferListIndex
argument_list|)
expr_stmt|;
block|}
comment|/**      * Ensure that the RandomAccessBuffer is not closed      * @throws IOException      */
specifier|private
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|currentBuffer
operator|==
literal|null
condition|)
block|{
comment|// consider that the rab is closed if there is no current buffer
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer already closed"
argument_list|)
throw|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|currentBuffer
operator|==
literal|null
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|pointer
operator|>=
name|size
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|length
argument_list|()
operator|-
name|getPosition
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
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
name|rewind
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|rewind
parameter_list|(
name|int
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|seek
argument_list|(
name|getPosition
argument_list|()
operator|-
name|bytes
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|read
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
return|;
block|}
block|}
end_class

end_unit

