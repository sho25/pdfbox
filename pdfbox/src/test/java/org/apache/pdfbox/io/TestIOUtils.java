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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * This is a unit test for {@link IOUtils}.  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|TestIOUtils
extends|extends
name|TestCase
block|{
comment|/**      * Tests {@link IOUtils#populateBuffer(java.io.InputStream, byte[]).      * @throws IOException if an I/O error occursn      */
specifier|public
name|void
name|testPopulateBuffer
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
literal|"Hello World!"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|data
operator|.
name|length
index|]
decl_stmt|;
name|long
name|count
init|=
name|IOUtils
operator|.
name|populateBuffer
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|,
name|buffer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|buffer
operator|=
operator|new
name|byte
index|[
name|data
operator|.
name|length
operator|-
literal|2
index|]
expr_stmt|;
comment|//Buffer too small
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|count
operator|=
name|IOUtils
operator|.
name|populateBuffer
argument_list|(
name|in
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|byte
index|[]
name|leftOver
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|leftOver
operator|.
name|length
argument_list|)
expr_stmt|;
name|buffer
operator|=
operator|new
name|byte
index|[
name|data
operator|.
name|length
operator|+
literal|2
index|]
expr_stmt|;
comment|//Buffer too big
name|in
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|count
operator|=
name|IOUtils
operator|.
name|populateBuffer
argument_list|(
name|in
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
comment|//EOD reached
block|}
block|}
end_class

end_unit

