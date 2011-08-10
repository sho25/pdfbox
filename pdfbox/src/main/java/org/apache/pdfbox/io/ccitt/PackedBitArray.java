begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/* $Id$ */
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
operator|.
name|ccitt
package|;
end_package

begin_comment
comment|/**  * Represents an array of bits packed in a byte array of fixed size.  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|PackedBitArray
block|{
specifier|private
name|int
name|bitCount
decl_stmt|;
specifier|private
name|byte
index|[]
name|data
decl_stmt|;
comment|/**      * Constructs a new bit array.      * @param bitCount the number of bits to maintain      */
specifier|public
name|PackedBitArray
parameter_list|(
name|int
name|bitCount
parameter_list|)
block|{
name|this
operator|.
name|bitCount
operator|=
name|bitCount
expr_stmt|;
name|int
name|byteCount
init|=
operator|(
name|bitCount
operator|+
literal|7
operator|)
operator|/
literal|8
decl_stmt|;
name|this
operator|.
name|data
operator|=
operator|new
name|byte
index|[
name|byteCount
index|]
expr_stmt|;
block|}
specifier|private
name|int
name|byteOffset
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
return|return
name|offset
operator|/
literal|8
return|;
block|}
specifier|private
name|int
name|bitOffset
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
return|return
name|offset
operator|%
literal|8
return|;
block|}
comment|/**      * Sets a bit at the given offset.      * @param offset the offset      */
specifier|public
name|void
name|set
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
name|int
name|byteOffset
init|=
name|byteOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|this
operator|.
name|data
index|[
name|byteOffset
index|]
operator||=
literal|1
operator|<<
name|bitOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clears a bit at the given offset.      * @param offset the offset      */
specifier|public
name|void
name|clear
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
name|int
name|byteOffset
init|=
name|byteOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|int
name|bitOffset
init|=
name|bitOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|this
operator|.
name|data
index|[
name|byteOffset
index|]
operator|&=
operator|~
operator|(
literal|1
operator|<<
name|bitOffset
operator|)
expr_stmt|;
block|}
comment|/**      * Sets a run of bits at the given offset to either 1 or 0.      * @param offset the offset      * @param length the number of bits to set      * @param bit 1 to set the bit, 0 to clear it      */
specifier|public
name|void
name|setBits
parameter_list|(
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|,
name|int
name|bit
parameter_list|)
block|{
if|if
condition|(
name|bit
operator|==
literal|0
condition|)
block|{
name|clearBits
argument_list|(
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBits
argument_list|(
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets a run of bits at the given offset to either 1.      * @param offset the offset      * @param length the number of bits to set      */
specifier|public
name|void
name|setBits
parameter_list|(
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|length
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|int
name|startBitOffset
init|=
name|bitOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|int
name|firstByte
init|=
name|byteOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|int
name|lastBitOffset
init|=
name|offset
operator|+
name|length
decl_stmt|;
if|if
condition|(
name|lastBitOffset
operator|>
name|getBitCount
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"offset + length> bit count"
argument_list|)
throw|;
block|}
name|int
name|lastByte
init|=
name|byteOffset
argument_list|(
name|lastBitOffset
argument_list|)
decl_stmt|;
name|int
name|endBitOffset
init|=
name|bitOffset
argument_list|(
name|lastBitOffset
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstByte
operator|==
name|lastByte
condition|)
block|{
comment|//Only one byte affected
name|int
name|mask
init|=
operator|(
literal|1
operator|<<
name|endBitOffset
operator|)
operator|-
operator|(
literal|1
operator|<<
name|startBitOffset
operator|)
decl_stmt|;
name|this
operator|.
name|data
index|[
name|firstByte
index|]
operator||=
name|mask
expr_stmt|;
block|}
else|else
block|{
comment|//Bits spanning multiple bytes
name|this
operator|.
name|data
index|[
name|firstByte
index|]
operator||=
literal|0xFF
operator|<<
name|startBitOffset
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|firstByte
operator|+
literal|1
init|;
name|i
operator|<
name|lastByte
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|data
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
literal|0xFF
expr_stmt|;
block|}
if|if
condition|(
name|endBitOffset
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|data
index|[
name|lastByte
index|]
operator||=
literal|0xFF
operator|>>
operator|(
literal|8
operator|-
name|endBitOffset
operator|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Clears a run of bits at the given offset.      * @param offset the offset      * @param length the number of bits to clear      */
specifier|public
name|void
name|clearBits
parameter_list|(
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|length
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|int
name|startBitOffset
init|=
name|offset
operator|%
literal|8
decl_stmt|;
name|int
name|firstByte
init|=
name|byteOffset
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|int
name|lastBitOffset
init|=
name|offset
operator|+
name|length
decl_stmt|;
name|int
name|lastByte
init|=
name|byteOffset
argument_list|(
name|lastBitOffset
argument_list|)
decl_stmt|;
name|int
name|endBitOffset
init|=
name|lastBitOffset
operator|%
literal|8
decl_stmt|;
if|if
condition|(
name|firstByte
operator|==
name|lastByte
condition|)
block|{
comment|//Only one byte affected
name|int
name|mask
init|=
operator|(
literal|1
operator|<<
name|endBitOffset
operator|)
operator|-
operator|(
literal|1
operator|<<
name|startBitOffset
operator|)
decl_stmt|;
name|this
operator|.
name|data
index|[
name|firstByte
index|]
operator|&=
operator|~
name|mask
expr_stmt|;
block|}
else|else
block|{
comment|//Bits spanning multiple bytes
name|this
operator|.
name|data
index|[
name|firstByte
index|]
operator|&=
operator|~
operator|(
literal|0xFF
operator|<<
name|startBitOffset
operator|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|firstByte
operator|+
literal|1
init|;
name|i
operator|<
name|lastByte
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|data
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
literal|0x00
expr_stmt|;
block|}
if|if
condition|(
name|endBitOffset
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|data
index|[
name|lastByte
index|]
operator|&=
operator|~
operator|(
literal|0xFF
operator|>>
operator|(
literal|8
operator|-
name|endBitOffset
operator|)
operator|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Clear all bits in the array.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|clearBits
argument_list|(
literal|0
argument_list|,
name|getBitCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the number of bits maintained by this array.      * @return the number of bits      */
specifier|public
name|int
name|getBitCount
parameter_list|()
block|{
return|return
name|this
operator|.
name|bitCount
return|;
block|}
comment|/**      * Returns the size of the byte buffer for this array.      * @return the size of the byte buffer      */
specifier|public
name|int
name|getByteCount
parameter_list|()
block|{
return|return
name|this
operator|.
name|data
operator|.
name|length
return|;
block|}
comment|/**      * Returns the underlying byte buffer.      *<p>      * Note: the actual buffer is returned. If it's manipulated      * the content of the bit array changes.      * @return the underlying data buffer      */
specifier|public
name|byte
index|[]
name|getData
parameter_list|()
block|{
return|return
name|this
operator|.
name|data
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toBitString
argument_list|(
name|this
operator|.
name|data
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|this
operator|.
name|bitCount
argument_list|)
return|;
block|}
comment|/**      * Converts a byte to a "binary" String of 0s and 1s.      * @param data the value to convert      * @return the binary string      */
specifier|public
specifier|static
name|String
name|toBitString
parameter_list|(
name|byte
name|data
parameter_list|)
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[]
block|{
name|data
block|}
decl_stmt|;
return|return
name|toBitString
argument_list|(
name|buf
argument_list|)
return|;
block|}
comment|/**      * Converts a series of bytes to a "binary" String of 0s and 1s.      * @param data the data      * @return the binary string      */
specifier|public
specifier|static
name|String
name|toBitString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|toBitString
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**      * Converts a series of bytes to a "binary" String of 0s and 1s.      * @param data the data      * @param start the start offset      * @param len the number of bytes to convert      * @return the binary string      */
specifier|public
specifier|static
name|String
name|toBitString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
name|start
init|,
name|end
init|=
name|start
operator|+
name|len
init|;
name|x
operator|<
name|end
condition|;
name|x
operator|++
control|)
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
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|int
name|mask
init|=
literal|1
operator|<<
name|i
decl_stmt|;
name|int
name|value
init|=
name|data
index|[
name|x
index|]
operator|&
name|mask
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|value
operator|!=
literal|0
condition|?
literal|'1'
else|:
literal|'0'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

