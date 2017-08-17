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
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * This class contains some helper methods handling Type1-Fonts.  *  * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Type1FontUtil
block|{
specifier|private
name|Type1FontUtil
parameter_list|()
block|{     }
comment|/**      * Converts a byte-array into a string with the corresponding hex value.       * @param bytes the byte array      * @return the string with the hex value      */
specifier|public
specifier|static
name|String
name|hexEncode
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|aByte
range|:
name|bytes
control|)
block|{
name|String
name|string
init|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|aByte
operator|&
literal|0xff
argument_list|)
decl_stmt|;
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|string
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Converts a string representing a hex value into a byte array.      * @param string the string representing the hex value      * @return the hex value as byte array      */
specifier|public
specifier|static
name|byte
index|[]
name|hexDecode
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
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
name|string
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
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
name|string
operator|.
name|length
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|bytes
index|[
name|i
operator|/
literal|2
index|]
operator|=
operator|(
name|byte
operator|)
name|Integer
operator|.
name|parseInt
argument_list|(
name|string
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|2
argument_list|)
argument_list|,
literal|16
argument_list|)
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
comment|/**      * Encrypt eexec.      * @param buffer the given data      * @return the encrypted data      */
specifier|public
specifier|static
name|byte
index|[]
name|eexecEncrypt
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
return|return
name|encrypt
argument_list|(
name|buffer
argument_list|,
literal|55665
argument_list|,
literal|4
argument_list|)
return|;
block|}
comment|/**      * Encrypt charstring.      * @param buffer the given data      * @param n blocksize?      * @return the encrypted data      */
specifier|public
specifier|static
name|byte
index|[]
name|charstringEncrypt
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|n
parameter_list|)
block|{
return|return
name|encrypt
argument_list|(
name|buffer
argument_list|,
literal|4330
argument_list|,
name|n
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|encrypt
parameter_list|(
name|byte
index|[]
name|plaintextBytes
parameter_list|,
name|int
name|r
parameter_list|,
name|int
name|n
parameter_list|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|plaintextBytes
operator|.
name|length
operator|+
name|n
index|]
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
name|n
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
literal|0
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|plaintextBytes
argument_list|,
literal|0
argument_list|,
name|buffer
argument_list|,
name|n
argument_list|,
name|buffer
operator|.
name|length
operator|-
name|n
argument_list|)
expr_stmt|;
name|int
name|c1
init|=
literal|52845
decl_stmt|;
name|int
name|c2
init|=
literal|22719
decl_stmt|;
name|byte
index|[]
name|ciphertextBytes
init|=
operator|new
name|byte
index|[
name|buffer
operator|.
name|length
index|]
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
name|buffer
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|plain
init|=
name|buffer
index|[
name|i
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|cipher
init|=
name|plain
operator|^
name|r
operator|>>
literal|8
decl_stmt|;
name|ciphertextBytes
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|cipher
expr_stmt|;
name|r
operator|=
operator|(
name|cipher
operator|+
name|r
operator|)
operator|*
name|c1
operator|+
name|c2
operator|&
literal|0xffff
expr_stmt|;
block|}
return|return
name|ciphertextBytes
return|;
block|}
comment|/**      * Decrypt eexec.      * @param buffer the given encrypted data      * @return the decrypted data      */
specifier|public
specifier|static
name|byte
index|[]
name|eexecDecrypt
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
return|return
name|decrypt
argument_list|(
name|buffer
argument_list|,
literal|55665
argument_list|,
literal|4
argument_list|)
return|;
block|}
comment|/**      * Decrypt charstring.      * @param buffer the given encrypted data      * @param n blocksize?      * @return the decrypted data      */
specifier|public
specifier|static
name|byte
index|[]
name|charstringDecrypt
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|n
parameter_list|)
block|{
return|return
name|decrypt
argument_list|(
name|buffer
argument_list|,
literal|4330
argument_list|,
name|n
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|decrypt
parameter_list|(
name|byte
index|[]
name|ciphertextBytes
parameter_list|,
name|int
name|r
parameter_list|,
name|int
name|n
parameter_list|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|ciphertextBytes
operator|.
name|length
index|]
decl_stmt|;
name|int
name|c1
init|=
literal|52845
decl_stmt|;
name|int
name|c2
init|=
literal|22719
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
name|ciphertextBytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|cipher
init|=
name|ciphertextBytes
index|[
name|i
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|plain
init|=
name|cipher
operator|^
name|r
operator|>>
literal|8
decl_stmt|;
name|buffer
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|plain
expr_stmt|;
name|r
operator|=
operator|(
name|cipher
operator|+
name|r
operator|)
operator|*
name|c1
operator|+
name|c2
operator|&
literal|0xffff
expr_stmt|;
block|}
name|byte
index|[]
name|plaintextBytes
init|=
operator|new
name|byte
index|[
name|ciphertextBytes
operator|.
name|length
operator|-
name|n
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|n
argument_list|,
name|plaintextBytes
argument_list|,
literal|0
argument_list|,
name|plaintextBytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|plaintextBytes
return|;
block|}
block|}
end_class

end_unit

