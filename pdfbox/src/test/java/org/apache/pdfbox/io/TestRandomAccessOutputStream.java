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
name|File
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * This is a unit test for RandomAccessOutputStream.  *   * @author Fredrik Kjellberg   */
end_comment

begin_class
specifier|public
class|class
name|TestRandomAccessOutputStream
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
name|File
name|testResultsDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testResultsDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testWrite
parameter_list|()
throws|throws
name|IOException
block|{
name|RandomAccessOutputStream
name|out
decl_stmt|;
name|byte
index|[]
name|buffer
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"raf-outputstream.bin"
argument_list|)
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
name|RandomAccessFile
name|raFile
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
decl_stmt|;
comment|// Test single byte writes
name|buffer
operator|=
name|createDataSequence
argument_list|(
literal|16
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|raFile
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|buffer
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|raFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|raFile
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test no write
name|out
operator|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|raFile
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|raFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|raFile
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test buffer writes
name|buffer
operator|=
name|createDataSequence
argument_list|(
literal|8
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|raFile
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|raFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|24
argument_list|,
name|raFile
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Test partial buffer writes
name|buffer
operator|=
name|createDataSequence
argument_list|(
literal|16
argument_list|,
literal|50
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|raFile
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|8
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|raFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|raFile
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Verify written data
name|buffer
operator|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|raFile
operator|.
name|length
argument_list|()
index|]
expr_stmt|;
name|raFile
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|buffer
operator|.
name|length
argument_list|,
name|raFile
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|buffer
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|buffer
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|buffer
index|[
literal|15
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|buffer
index|[
literal|16
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|31
argument_list|,
name|buffer
index|[
literal|17
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|37
argument_list|,
name|buffer
index|[
literal|23
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|58
argument_list|,
name|buffer
index|[
literal|24
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|59
argument_list|,
name|buffer
index|[
literal|25
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|buffer
index|[
literal|26
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|61
argument_list|,
name|buffer
index|[
literal|27
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|54
argument_list|,
name|buffer
index|[
literal|28
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|55
argument_list|,
name|buffer
index|[
literal|29
index|]
argument_list|)
expr_stmt|;
comment|// Cleanup
name|raFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|byte
index|[]
name|createDataSequence
parameter_list|(
name|int
name|length
parameter_list|,
name|int
name|firstByteValue
parameter_list|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
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
name|buffer
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|firstByteValue
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
block|}
end_class

end_unit

