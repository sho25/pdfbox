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
name|ByteArrayOutputStream
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
comment|/**  * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
class|class
name|DataOutput
block|{
specifier|private
name|ByteArrayOutputStream
name|outputBuffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|private
name|String
name|outputEncoding
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|DataOutput
parameter_list|()
block|{
name|this
argument_list|(
literal|"ISO-8859-1"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor with a given encoding.      * @param encoding the encoding to be used for writing      */
specifier|public
name|DataOutput
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|outputEncoding
operator|=
name|encoding
expr_stmt|;
block|}
comment|/**      * Returns the written data buffer as byte array.      * @return the data buffer as byte array      */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|outputBuffer
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/**      * Write an int value to the buffer.      * @param value the given value      */
specifier|public
name|void
name|write
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|outputBuffer
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write a byte array to the buffer.      * @param buffer the given byte array      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
name|outputBuffer
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write a part of a byte array to the buffer.      * @param buffer the given byte buffer      * @param offset the offset where to start       * @param length the amount of bytes to be written from the array      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|outputBuffer
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the given string to the buffer using the given encoding.      * @param string the given string      * @throws IOException If an error occurs during writing the data to the buffer      */
specifier|public
name|void
name|print
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|string
operator|.
name|getBytes
argument_list|(
name|outputEncoding
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the given string to the buffer using the given encoding.      * A newline is added after the given string      * @param string the given string      * @throws IOException If an error occurs during writing the data to the buffer      */
specifier|public
name|void
name|println
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|string
operator|.
name|getBytes
argument_list|(
name|outputEncoding
argument_list|)
argument_list|)
expr_stmt|;
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a newline to the given string.      */
specifier|public
name|void
name|println
parameter_list|()
block|{
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

