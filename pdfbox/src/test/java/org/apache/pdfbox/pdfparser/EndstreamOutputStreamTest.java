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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|EndstreamOutputStreamTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testEndstreamOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|EndstreamOutputStream
name|feos
init|=
operator|new
name|EndstreamOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|byte
name|tab1
index|[]
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
decl_stmt|;
name|byte
name|tab2
index|[]
init|=
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|,
literal|'\n'
block|}
decl_stmt|;
name|byte
name|tab3
index|[]
init|=
block|{
literal|8
block|,
literal|9
block|,
literal|'\r'
block|,
literal|'\n'
block|}
decl_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab1
argument_list|,
literal|0
argument_list|,
name|tab1
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab2
argument_list|,
literal|0
argument_list|,
name|tab2
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab3
argument_list|,
literal|0
argument_list|,
name|tab3
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
name|expectedResult1
index|[]
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
literal|'\r'
block|,
literal|'\n'
block|,
literal|8
block|,
literal|9
block|}
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|expectedResult1
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|feos
operator|=
operator|new
name|EndstreamOutputStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|byte
name|tab4
index|[]
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
decl_stmt|;
name|byte
name|tab5
index|[]
init|=
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab6
index|[]
init|=
block|{
literal|8
block|,
literal|9
block|,
literal|'\n'
block|}
decl_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab4
argument_list|,
literal|0
argument_list|,
name|tab4
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab5
argument_list|,
literal|0
argument_list|,
name|tab5
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab6
argument_list|,
literal|0
argument_list|,
name|tab6
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
name|expectedResult2
index|[]
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
literal|'\r'
block|,
literal|8
block|,
literal|9
block|}
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|expectedResult2
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|feos
operator|=
operator|new
name|EndstreamOutputStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|byte
name|tab7
index|[]
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
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab8
index|[]
init|=
block|{
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\n'
block|}
decl_stmt|;
name|byte
name|tab9
index|[]
init|=
block|{
literal|8
block|,
literal|9
block|,
literal|'\r'
block|}
decl_stmt|;
comment|// final CR is not to be discarded
name|feos
operator|.
name|write
argument_list|(
name|tab7
argument_list|,
literal|0
argument_list|,
name|tab7
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab8
argument_list|,
literal|0
argument_list|,
name|tab8
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab9
argument_list|,
literal|0
argument_list|,
name|tab9
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
name|expectedResult3
index|[]
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
literal|'\r'
block|,
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\n'
block|,
literal|8
block|,
literal|9
block|,
literal|'\r'
block|}
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|expectedResult3
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|feos
operator|=
operator|new
name|EndstreamOutputStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|byte
name|tab10
index|[]
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
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab11
index|[]
init|=
block|{
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab12
index|[]
init|=
block|{
literal|8
block|,
literal|9
block|,
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab13
index|[]
init|=
block|{
literal|'\n'
block|}
decl_stmt|;
comment|// final CR LF across buffers
name|feos
operator|.
name|write
argument_list|(
name|tab10
argument_list|,
literal|0
argument_list|,
name|tab10
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab11
argument_list|,
literal|0
argument_list|,
name|tab11
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab12
argument_list|,
literal|0
argument_list|,
name|tab12
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab13
argument_list|,
literal|0
argument_list|,
name|tab13
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
name|expectedResult4
index|[]
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
literal|'\r'
block|,
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|,
literal|8
block|,
literal|9
block|}
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|expectedResult4
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|feos
operator|=
operator|new
name|EndstreamOutputStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|byte
name|tab14
index|[]
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
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab15
index|[]
init|=
block|{
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|}
decl_stmt|;
name|byte
name|tab16
index|[]
init|=
block|{
literal|8
block|,
literal|9
block|,
literal|'\n'
block|}
decl_stmt|;
name|byte
name|tab17
index|[]
init|=
block|{
literal|'\r'
block|}
decl_stmt|;
comment|// final CR is not to be discarded
name|feos
operator|.
name|write
argument_list|(
name|tab14
argument_list|,
literal|0
argument_list|,
name|tab14
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab15
argument_list|,
literal|0
argument_list|,
name|tab15
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab16
argument_list|,
literal|0
argument_list|,
name|tab16
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|write
argument_list|(
name|tab17
argument_list|,
literal|0
argument_list|,
name|tab17
operator|.
name|length
argument_list|)
expr_stmt|;
name|feos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
name|expectedResult5
index|[]
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
literal|'\r'
block|,
literal|'\n'
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|'\r'
block|,
literal|8
block|,
literal|9
block|,
literal|'\n'
block|,
literal|'\r'
block|}
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|expectedResult5
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

