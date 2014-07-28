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
name|ttf
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
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
specifier|public
class|class
name|TestMemoryTTFDataStream
extends|extends
name|TestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEOF
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|byteArray
init|=
operator|new
name|byte
index|[
literal|10
index|]
decl_stmt|;
name|ByteArrayInputStream
name|inputStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|byteArray
argument_list|)
decl_stmt|;
name|MemoryTTFDataStream
name|dataStream
init|=
operator|new
name|MemoryTTFDataStream
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|int
name|value
init|=
name|dataStream
operator|.
name|read
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
name|value
operator|>
operator|-
literal|1
condition|)
block|{
name|value
operator|=
name|dataStream
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ArrayIndexOutOfBoundsException
name|exception
parameter_list|)
block|{
name|fail
argument_list|(
literal|"EOF not detected!"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|dataStream
operator|!=
literal|null
condition|)
block|{
name|dataStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

