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
name|pdfparser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
comment|/**  * A SequentialSource provides access to sequential data for parsing.  */
end_comment

begin_interface
interface|interface
name|SequentialSource
extends|extends
name|Closeable
block|{
comment|/**      * Read a single byte of data.      *      * @return The byte of data that is being read.      * @throws IOException If there is an error while reading the data.      */
name|int
name|read
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Read a buffer of data.      *      * @param b The buffer to write the data to.      * @return The number of bytes that were actually read.      * @throws IOException If there was an error while reading the data.      */
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Read a buffer of data.      *      * @param b The buffer to write the data to.      * @param offset Offset into the buffer to start writing.      * @param length The amount of data to attempt to read.      * @return The number of bytes that were actually read.      * @throws IOException If there was an error while reading the data.      */
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
function_decl|;
comment|/**      * Returns offset of next byte to be returned by a read method.      *      * @return offset of next byte which will be returned with next {@link #read()} (if no more       * bytes are left it returns a value&gt;= length of source).      * @throws IOException If there was an error while reading the data.      */
name|long
name|getPosition
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * This will peek at the next byte.      *      * @return The next byte on the stream, leaving it as available to read.      * @throws IOException If there is an error reading the next byte.      */
name|int
name|peek
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Unreads a single byte.      *      * @param b byte array to push back      * @throws IOException if there is an error while unreading      */
name|void
name|unread
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Unreads an array of bytes.      *      * @param bytes byte array to be unread      * @throws IOException if there is an error while unreading      */
name|void
name|unread
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Unreads a portion of an array of bytes.      *      * @param bytes byte array to be unread      * @param start start index      * @param len number of bytes to be unread      * @throws IOException if there is an error while unreading      */
name|void
name|unread
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Reads a given number of bytes in its entirety.      *      * @param length the number of bytes to be read      * @return a byte array containing the bytes just read      * @throws IOException if an I/O error occurs while reading data      */
name|byte
index|[]
name|readFully
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if the end of the data source has been reached.      *      * @return true if we are at the end of the data.      * @throws IOException If there is an error reading the next byte.      */
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

