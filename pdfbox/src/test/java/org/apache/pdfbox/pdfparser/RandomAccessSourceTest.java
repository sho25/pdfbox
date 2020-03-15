begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|pdfbox
operator|.
name|io
operator|.
name|RandomAccessBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * Unittest for org.apache.pdfbox.pdfparser.RandomAccessSource  */
end_comment

begin_class
specifier|public
class|class
name|RandomAccessSourceTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPositionSkip
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|inputValues
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|}
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputValues
argument_list|)
decl_stmt|;
name|RandomAccessSource
name|randomAccessSource
init|=
operator|new
name|RandomAccessSource
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|bais
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|skip
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPositionRead
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|inputValues
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|}
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputValues
argument_list|)
decl_stmt|;
name|RandomAccessSource
name|randomAccessSource
init|=
operator|new
name|RandomAccessSource
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|bais
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPositionReadBytes
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|inputValues
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|}
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputValues
argument_list|)
decl_stmt|;
name|RandomAccessSource
name|randomAccessSource
init|=
operator|new
name|RandomAccessSource
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|bais
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|4
index|]
decl_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPositionPeek
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|inputValues
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|}
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputValues
argument_list|)
decl_stmt|;
name|RandomAccessSource
name|randomAccessSource
init|=
operator|new
name|RandomAccessSource
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|bais
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|skip
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|peek
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPositionUnreadBytes
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|inputValues
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|}
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputValues
argument_list|)
decl_stmt|;
name|RandomAccessSource
name|randomAccessSource
init|=
operator|new
name|RandomAccessSource
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|bais
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|byte
index|[]
name|readBytes
init|=
operator|new
name|byte
index|[
literal|6
index|]
decl_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|unread
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|read
argument_list|(
name|readBytes
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|unread
argument_list|(
name|readBytes
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|randomAccessSource
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|randomAccessSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

