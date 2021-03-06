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
name|pdmodel
operator|.
name|encryption
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * An implementation of the RC4 stream cipher.  *  * @author Ben Litchfield  */
end_comment

begin_class
class|class
name|RC4Cipher
block|{
specifier|private
specifier|final
name|int
index|[]
name|salt
decl_stmt|;
specifier|private
name|int
name|b
decl_stmt|;
specifier|private
name|int
name|c
decl_stmt|;
comment|/**      * Constructor.      */
name|RC4Cipher
parameter_list|()
block|{
name|salt
operator|=
operator|new
name|int
index|[
literal|256
index|]
expr_stmt|;
block|}
comment|/**      * This will reset the key to be used.      *      * @param key The RC4 key used during encryption.      */
specifier|public
name|void
name|setKey
parameter_list|(
name|byte
index|[]
name|key
parameter_list|)
block|{
name|b
operator|=
literal|0
expr_stmt|;
name|c
operator|=
literal|0
expr_stmt|;
if|if
condition|(
name|key
operator|.
name|length
argument_list|<
literal|1
operator|||
name|key
operator|.
name|length
argument_list|>
literal|32
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"number of bytes must be between 1 and 32"
argument_list|)
throw|;
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
name|salt
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|salt
index|[
name|i
index|]
operator|=
name|i
expr_stmt|;
block|}
name|int
name|keyIndex
init|=
literal|0
decl_stmt|;
name|int
name|saltIndex
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
name|salt
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|saltIndex
operator|=
operator|(
name|fixByte
argument_list|(
name|key
index|[
name|keyIndex
index|]
argument_list|)
operator|+
name|salt
index|[
name|i
index|]
operator|+
name|saltIndex
operator|)
operator|%
literal|256
expr_stmt|;
name|swap
argument_list|(
name|salt
argument_list|,
name|i
argument_list|,
name|saltIndex
argument_list|)
expr_stmt|;
name|keyIndex
operator|=
operator|(
name|keyIndex
operator|+
literal|1
operator|)
operator|%
name|key
operator|.
name|length
expr_stmt|;
block|}
block|}
comment|/**      * This will ensure that the value for a byte&gt;=0.      *      * @param aByte The byte to test against.      *      * @return A value&gt;=0 and&lt; 256      */
specifier|private
specifier|static
name|int
name|fixByte
parameter_list|(
name|byte
name|aByte
parameter_list|)
block|{
return|return
name|aByte
operator|<
literal|0
condition|?
literal|256
operator|+
name|aByte
else|:
name|aByte
return|;
block|}
comment|/**      * This will swap two values in an array.      *      * @param data The array to swap from.      * @param firstIndex The index of the first element to swap.      * @param secondIndex The index of the second element to swap.      */
specifier|private
specifier|static
name|void
name|swap
parameter_list|(
name|int
index|[]
name|data
parameter_list|,
name|int
name|firstIndex
parameter_list|,
name|int
name|secondIndex
parameter_list|)
block|{
name|int
name|tmp
init|=
name|data
index|[
name|firstIndex
index|]
decl_stmt|;
name|data
index|[
name|firstIndex
index|]
operator|=
name|data
index|[
name|secondIndex
index|]
expr_stmt|;
name|data
index|[
name|secondIndex
index|]
operator|=
name|tmp
expr_stmt|;
block|}
comment|/**      * This will encrypt and write the next byte.      *      * @param aByte The byte to encrypt.      * @param output The stream to write to.      *      * @throws IOException If there is an error writing to the output stream.      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
name|aByte
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|b
operator|=
operator|(
name|b
operator|+
literal|1
operator|)
operator|%
literal|256
expr_stmt|;
name|c
operator|=
operator|(
name|salt
index|[
name|b
index|]
operator|+
name|c
operator|)
operator|%
literal|256
expr_stmt|;
name|swap
argument_list|(
name|salt
argument_list|,
name|b
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|int
name|saltIndex
init|=
operator|(
name|salt
index|[
name|b
index|]
operator|+
name|salt
index|[
name|c
index|]
operator|)
operator|%
literal|256
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
name|aByte
operator|^
operator|(
name|byte
operator|)
name|salt
index|[
name|saltIndex
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will encrypt and write the data.      *      * @param data The data to encrypt.      * @param output The stream to write to.      *      * @throws IOException If there is an error writing to the output stream.      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|byte
name|aData
range|:
name|data
control|)
block|{
name|write
argument_list|(
name|aData
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will encrypt and write the data.      *      * @param data The data to encrypt.      * @param output The stream to write to.      *      * @throws IOException If there is an error writing to the output stream.      */
specifier|public
name|void
name|write
parameter_list|(
name|InputStream
name|data
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
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
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|data
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
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will encrypt and write the data.      *      * @param data The data to encrypt.      * @param offset The offset into the array to start reading data from.      * @param len The number of bytes to attempt to read.      * @param output The stream to write to.      *      * @throws IOException If there is an error writing to the output stream.      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|len
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
name|offset
init|;
name|i
operator|<
name|offset
operator|+
name|len
condition|;
name|i
operator|++
control|)
block|{
name|write
argument_list|(
name|data
index|[
name|i
index|]
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

