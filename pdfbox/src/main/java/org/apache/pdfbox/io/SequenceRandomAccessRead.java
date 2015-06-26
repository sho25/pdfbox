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
comment|/**  * A<code>SequenceRandomAccessRead</code> represents the logical concatenation of a couple of RandomAccessRead  * instances.  *  */
end_comment

begin_class
specifier|public
class|class
name|SequenceRandomAccessRead
implements|implements
name|RandomAccessRead
block|{
specifier|private
name|boolean
name|isClosed
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|RandomAccessRead
argument_list|>
name|source
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Long
argument_list|>
name|sourceLength
decl_stmt|;
specifier|private
specifier|final
name|long
name|bufferLength
decl_stmt|;
specifier|private
name|RandomAccessRead
name|currentBuffer
decl_stmt|;
specifier|private
name|long
name|currentPosition
decl_stmt|;
specifier|private
name|long
name|currentBufferPosition
decl_stmt|;
specifier|private
name|long
name|currentBufferLength
decl_stmt|;
specifier|private
name|int
name|currentIndex
decl_stmt|;
specifier|private
name|int
name|maxIndex
decl_stmt|;
comment|/**      * Create a read only wrapper for a RandomAccessRead instance.      *      * @param list a list containing all instances of RandomAccessRead to be read.      *       * @throws IOException if something went wrong while accessing the given list of RandomAccessRead.      */
specifier|public
name|SequenceRandomAccessRead
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|RandomAccessRead
argument_list|>
name|list
parameter_list|)
throws|throws
name|IOException
block|{
name|source
operator|=
name|list
expr_stmt|;
name|maxIndex
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
name|sourceLength
operator|=
operator|new
name|ArrayList
argument_list|<
name|Long
argument_list|>
argument_list|(
name|maxIndex
argument_list|)
expr_stmt|;
name|long
name|sumLength
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RandomAccessRead
name|input
range|:
name|list
control|)
block|{
name|long
name|inputLength
init|=
name|input
operator|.
name|length
argument_list|()
decl_stmt|;
name|input
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sourceLength
operator|.
name|add
argument_list|(
name|inputLength
argument_list|)
expr_stmt|;
name|sumLength
operator|+=
name|inputLength
expr_stmt|;
block|}
name|bufferLength
operator|=
name|sumLength
expr_stmt|;
name|currentBuffer
operator|=
name|source
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|currentBufferLength
operator|=
name|sourceLength
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|currentBufferPosition
operator|=
literal|0
expr_stmt|;
name|currentPosition
operator|=
literal|0
expr_stmt|;
name|currentIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Ensure that the RandomAccessFile is not closed.      *       * @throws IOException if the buffer is already closed      */
specifier|private
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|isClosed
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessFile already closed"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Switch to the next buffer.      *       * @return true if another buffer is available      *       * @throws IOException if something went wrong when switching to the next buffer      */
specifier|private
name|boolean
name|nextBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|currentIndex
operator|<
name|maxIndex
condition|)
block|{
name|switchBuffer
argument_list|(
name|currentIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Switch to buffer with the given index.      *       * @param index the index of the buffer to be switched to      *       * @throws IOException if the given index exceeds the available number of buffers      */
specifier|private
name|void
name|switchBuffer
parameter_list|(
name|int
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|currentIndex
operator|=
name|index
expr_stmt|;
name|currentBuffer
operator|=
name|source
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|currentBufferPosition
operator|=
literal|0
expr_stmt|;
name|currentBufferLength
operator|=
name|sourceLength
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|currentBuffer
operator|.
name|seek
argument_list|(
name|currentBufferPosition
argument_list|)
expr_stmt|;
block|}
comment|/** Returns offset in file at which next byte would be read. */
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
name|currentPosition
return|;
block|}
comment|/**      * Seeks to new position. If new position is outside of current page the new page is either taken from cache or read      * from file and added to cache.      *      * @param newPosition the position to seek to.      * @throws java.io.IOException if something went wrong.      */
annotation|@
name|Override
specifier|public
name|void
name|seek
parameter_list|(
specifier|final
name|long
name|newPosition
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
comment|// new position beyond EOF
if|if
condition|(
name|newPosition
operator|>=
name|bufferLength
condition|)
block|{
name|switchBuffer
argument_list|(
name|maxIndex
argument_list|)
expr_stmt|;
name|currentBufferPosition
operator|=
name|sourceLength
operator|.
name|get
argument_list|(
name|currentIndex
argument_list|)
expr_stmt|;
name|currentPosition
operator|=
name|newPosition
expr_stmt|;
name|currentBuffer
operator|.
name|seek
argument_list|(
name|currentBufferPosition
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
name|long
name|position
init|=
name|sourceLength
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
while|while
condition|(
name|newPosition
operator|>=
name|position
condition|)
block|{
name|position
operator|+=
name|sourceLength
operator|.
name|get
argument_list|(
operator|++
name|index
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentIndex
operator|!=
name|index
condition|)
block|{
name|switchBuffer
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|long
name|startPostion
init|=
literal|0
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
name|index
condition|;
name|i
operator|++
control|)
block|{
name|startPostion
operator|+=
name|sourceLength
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|currentBufferPosition
operator|=
name|newPosition
operator|-
name|startPostion
expr_stmt|;
block|}
else|else
block|{
name|currentBufferPosition
operator|+=
name|newPosition
operator|-
name|currentPosition
expr_stmt|;
block|}
name|currentBuffer
operator|.
name|seek
argument_list|(
name|currentBufferPosition
argument_list|)
expr_stmt|;
name|currentPosition
operator|=
name|newPosition
expr_stmt|;
block|}
block|}
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
name|int
name|returnValue
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|currentPosition
operator|<
name|bufferLength
condition|)
block|{
if|if
condition|(
name|currentBufferPosition
operator|<
name|currentBufferLength
condition|)
block|{
name|returnValue
operator|=
name|currentBuffer
operator|.
name|read
argument_list|()
expr_stmt|;
name|currentPosition
operator|++
expr_stmt|;
name|currentBufferPosition
operator|++
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|nextBuffer
argument_list|()
condition|)
block|{
name|returnValue
operator|=
name|currentBuffer
operator|.
name|read
argument_list|()
expr_stmt|;
name|currentPosition
operator|++
expr_stmt|;
name|currentBufferPosition
operator|++
expr_stmt|;
block|}
block|}
block|}
return|return
name|returnValue
return|;
block|}
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
name|checkClosed
argument_list|()
expr_stmt|;
name|int
name|bytesReadTotal
init|=
name|readBytes
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
name|int
name|bytesRead
init|=
name|bytesReadTotal
decl_stmt|;
while|while
condition|(
name|bytesReadTotal
argument_list|<
name|len
operator|&&
name|bytesRead
argument_list|>
literal|0
condition|)
block|{
name|bytesRead
operator|=
name|read
argument_list|(
name|b
argument_list|,
name|off
operator|+
name|bytesRead
argument_list|,
name|len
operator|-
name|bytesRead
argument_list|)
expr_stmt|;
name|bytesReadTotal
operator|+=
name|bytesRead
expr_stmt|;
block|}
return|return
name|bytesReadTotal
return|;
block|}
specifier|private
name|int
name|readBytes
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
comment|// end of current buffer reached?
if|if
condition|(
name|currentBufferPosition
operator|==
name|currentBufferLength
condition|)
block|{
name|nextBuffer
argument_list|()
expr_stmt|;
block|}
name|int
name|bytesRead
init|=
name|currentBuffer
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
name|currentBufferPosition
operator|+=
name|bytesRead
expr_stmt|;
name|currentPosition
operator|+=
name|bytesRead
expr_stmt|;
return|return
name|bytesRead
return|;
block|}
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
name|bufferLength
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
annotation|@
name|Override
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|bufferLength
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
comment|// don't close the underlying random access
name|isClosed
operator|=
literal|true
expr_stmt|;
name|currentBuffer
operator|=
literal|null
expr_stmt|;
name|source
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|isClosed
operator|||
name|source
operator|==
literal|null
return|;
block|}
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
name|seek
argument_list|(
name|getPosition
argument_list|()
operator|-
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|readFully
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|int
name|bytesRead
init|=
name|read
argument_list|(
name|b
argument_list|)
decl_stmt|;
while|while
condition|(
name|bytesRead
operator|<
name|length
condition|)
block|{
name|bytesRead
operator|+=
name|read
argument_list|(
name|b
argument_list|,
name|bytesRead
argument_list|,
name|length
operator|-
name|bytesRead
argument_list|)
expr_stmt|;
block|}
return|return
name|b
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|peek
argument_list|()
operator|==
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

