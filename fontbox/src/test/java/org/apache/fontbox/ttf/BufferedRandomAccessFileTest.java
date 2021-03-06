begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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

begin_comment
comment|/*  * Copyright 2016 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

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
name|FileOutputStream
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
name|OutputStream
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
comment|/**  * @author Cameron Rollhieser  */
end_comment

begin_class
specifier|public
class|class
name|BufferedRandomAccessFileTest
block|{
comment|/**      * Before solving PDFBOX-3605, this test never ended.      *       * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|ensureReadFinishes
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|File
name|file
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"apache-pdfbox"
argument_list|,
literal|".dat"
argument_list|)
decl_stmt|;
try|try
init|(
name|OutputStream
name|outputStream
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
init|)
block|{
specifier|final
name|String
name|content
init|=
literal|"1234567890"
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|content
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|final
name|byte
index|[]
name|readBuffer
init|=
operator|new
name|byte
index|[
literal|2
index|]
decl_stmt|;
specifier|final
name|BufferedRandomAccessFile
name|buffer
init|=
operator|new
name|BufferedRandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"r"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|int
name|amountRead
decl_stmt|;
name|int
name|totalAmountRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|buffer
operator|.
name|read
argument_list|(
name|readBuffer
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|totalAmountRead
operator|+=
name|amountRead
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|totalAmountRead
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

