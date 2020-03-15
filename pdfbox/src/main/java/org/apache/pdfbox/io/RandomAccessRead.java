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
comment|/**  * An interface allowing random access read operations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RandomAccessRead
extends|extends
name|Closeable
block|{
comment|/**      * Read a single byte of data.      *      * @return The byte of data that is being read.      *      * @throws IOException If there is an error while reading the data.      */
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
comment|/**      * Returns offset of next byte to be returned by a read method.      *       * @return offset of next byte which will be returned with next {@link #read()}      *         (if no more bytes are left it returns a value&gt;= length of source)      *               * @throws IOException       */
name|long
name|getPosition
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Seek to a position in the data.      *      * @param position The position to seek to.      * @throws IOException If there is an error while seeking.      */
name|void
name|seek
parameter_list|(
name|long
name|position
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * The total number of bytes that are available.      *      * @return The number of bytes available.      *      * @throws IOException If there is an IO error while determining the      * length of the data stream.      */
name|long
name|length
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if this stream has been closed.      */
name|boolean
name|isClosed
parameter_list|()
function_decl|;
comment|/**      * This will peek at the next byte.      *      * @return The next byte on the stream, leaving it as available to read.      *      * @throws IOException If there is an error reading the next byte.      */
name|int
name|peek
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Seek backwards the given number of bytes.      *       * @param bytes the number of bytes to be seeked backwards      * @throws IOException If there is an error while seeking      */
name|void
name|rewind
parameter_list|(
name|int
name|bytes
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * A simple test to see if we are at the end of the data.      *      * @return true if we are at the end of the data.      *      * @throws IOException If there is an error reading the next byte.      */
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns an estimate of the number of bytes that can be read.      *      * @return the number of bytes that can be read      * @throws IOException if this random access has been closed      */
name|int
name|available
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

