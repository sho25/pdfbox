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
comment|/**  * An interface allowing random access write operations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RandomAccessWrite
extends|extends
name|Closeable
block|{
comment|/**      * Write a byte to the stream.      *      * @param b The byte to write.      * @throws IOException If there is an IO error while writing.      */
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Write a buffer of data to the stream.      *      * @param b The buffer to get the data from.      * @throws IOException If there is an error while writing the data.      */
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Write a buffer of data to the stream.      *      * @param b The buffer to get the data from.      * @param offset An offset into the buffer to get the data from.      * @param length The length of data to write.      * @throws IOException If there is an error while writing the data.      */
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
function_decl|;
comment|/**      * Clears all data of the buffer.      */
name|void
name|clear
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

