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

begin_comment
comment|/**  * An interface allowing sequential read operations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SequentialRead
block|{
comment|/**      * Release resources that are being held.      *      * @throws IOException If there is an error closing this resource.      */
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
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
block|}
end_interface

end_unit

