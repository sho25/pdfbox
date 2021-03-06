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
name|pdmodel
operator|.
name|common
operator|.
name|function
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
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
name|cos
operator|.
name|COSStream
import|;
end_import

begin_comment
comment|/**  * Tests the {@link PDFunctionType4} class.  */
end_comment

begin_class
specifier|public
class|class
name|TestPDFunctionType4
extends|extends
name|TestCase
block|{
specifier|private
name|PDFunctionType4
name|createFunction
parameter_list|(
name|String
name|function
parameter_list|,
name|float
index|[]
name|domain
parameter_list|,
name|float
index|[]
name|range
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
operator|new
name|COSStream
argument_list|()
decl_stmt|;
name|stream
operator|.
name|setInt
argument_list|(
literal|"FunctionType"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|COSArray
name|domainArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|domainArray
operator|.
name|setFloatArray
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setItem
argument_list|(
literal|"Domain"
argument_list|,
name|domainArray
argument_list|)
expr_stmt|;
name|COSArray
name|rangeArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|rangeArray
operator|.
name|setFloatArray
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setItem
argument_list|(
literal|"Range"
argument_list|,
name|rangeArray
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|out
init|=
name|stream
operator|.
name|createOutputStream
argument_list|()
init|)
block|{
name|byte
index|[]
name|data
init|=
name|function
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|US_ASCII
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDFunctionType4
argument_list|(
name|stream
argument_list|)
return|;
block|}
comment|/**      * Checks the {@link PDFunctionType4}.      * @throws Exception if an error occurs      */
specifier|public
name|void
name|testFunctionSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|functionText
init|=
literal|"{ add }"
decl_stmt|;
comment|//Simply adds the two arguments and returns the result
name|PDFunctionType4
name|function
init|=
name|createFunction
argument_list|(
name|functionText
argument_list|,
operator|new
name|float
index|[]
block|{
operator|-
literal|1.0f
block|,
literal|1.0f
block|,
operator|-
literal|1.0f
block|,
literal|1.0f
block|}
argument_list|,
operator|new
name|float
index|[]
block|{
operator|-
literal|1.0f
block|,
literal|1.0f
block|}
argument_list|)
decl_stmt|;
name|float
index|[]
name|input
init|=
operator|new
name|float
index|[]
block|{
literal|0.8f
block|,
literal|0.1f
block|}
decl_stmt|;
name|float
index|[]
name|output
init|=
name|function
operator|.
name|eval
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|output
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.9f
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|,
literal|0.0001f
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|float
index|[]
block|{
literal|0.8f
block|,
literal|0.3f
block|}
expr_stmt|;
comment|//results in 1.1f being outside Range
name|output
operator|=
name|function
operator|.
name|eval
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|output
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1f
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|float
index|[]
block|{
literal|0.8f
block|,
literal|1.2f
block|}
expr_stmt|;
comment|//input argument outside Dimension
name|output
operator|=
name|function
operator|.
name|eval
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|output
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1f
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks the handling of the argument order for a {@link PDFunctionType4}.      * @throws Exception if an error occurs      */
specifier|public
name|void
name|testFunctionArgumentOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|functionText
init|=
literal|"{ pop }"
decl_stmt|;
comment|// pops an argument (2nd) and returns the next argument (1st)
name|PDFunctionType4
name|function
init|=
name|createFunction
argument_list|(
name|functionText
argument_list|,
operator|new
name|float
index|[]
block|{
operator|-
literal|1.0f
block|,
literal|1.0f
block|,
operator|-
literal|1.0f
block|,
literal|1.0f
block|}
argument_list|,
operator|new
name|float
index|[]
block|{
operator|-
literal|1.0f
block|,
literal|1.0f
block|}
argument_list|)
decl_stmt|;
name|float
index|[]
name|input
init|=
operator|new
name|float
index|[]
block|{
operator|-
literal|0.7f
block|,
literal|0.0f
block|}
decl_stmt|;
name|float
index|[]
name|output
init|=
name|function
operator|.
name|eval
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|output
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|0.7f
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|,
literal|0.0001f
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

