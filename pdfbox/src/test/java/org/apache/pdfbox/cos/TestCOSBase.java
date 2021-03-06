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
name|cos
package|;
end_package

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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Test class for {@link COSBase}.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TestCOSBase
extends|extends
name|TestCase
block|{
comment|/** The COSBase abstraction of the object being tested. */
specifier|protected
name|COSBase
name|testCOSBase
decl_stmt|;
comment|/**      * Tests getCOSObject() - tests that the underlying object is returned.      */
specifier|public
name|void
name|testGetCOSObject
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|testCOSBase
argument_list|,
name|testCOSBase
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test accept() - tests the interface for visiting a document at the COS level.      */
specifier|public
specifier|abstract
name|void
name|testAccept
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Tests isDirect() and setDirect() - tests the getter/setter methods.      */
specifier|public
name|void
name|testIsSetDirect
parameter_list|()
block|{
name|testCOSBase
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|testCOSBase
operator|.
name|isDirect
argument_list|()
argument_list|)
expr_stmt|;
name|testCOSBase
operator|.
name|setDirect
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testCOSBase
operator|.
name|isDirect
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * A simple utility function to compare two byte arrays.      * @param byteArr1 the expected byte array      * @param byteArr2 the byte array being compared      */
specifier|protected
name|void
name|testByteArrays
parameter_list|(
name|byte
index|[]
name|byteArr1
parameter_list|,
name|byte
index|[]
name|byteArr2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|byteArr1
operator|.
name|length
argument_list|,
name|byteArr1
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|byteArr1
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|byteArr1
index|[
name|i
index|]
argument_list|,
name|byteArr2
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

