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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Test class for {@link COSNumber}  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TestCOSNumber
extends|extends
name|TestCOSBase
block|{
comment|/**      * Test floatValue() - test that the correct float value is returned.      */
specifier|public
specifier|abstract
name|void
name|testFloatValue
parameter_list|()
function_decl|;
comment|/**      * Test doubleValue() - test that the correct double value is returned.      */
specifier|public
specifier|abstract
name|void
name|testDoubleValue
parameter_list|()
function_decl|;
comment|/**      * Test intValue() - test that the correct int value is returned.      */
specifier|public
specifier|abstract
name|void
name|testIntValue
parameter_list|()
function_decl|;
comment|/**      * Test longValue() - test that the correct long value is returned.      */
specifier|public
specifier|abstract
name|void
name|testLongValue
parameter_list|()
function_decl|;
comment|/**      * Tests get() - tests a static constructor for COSNumber classes.      */
specifier|public
name|void
name|testGet
parameter_list|()
block|{
try|try
block|{
comment|// Ensure the basic static numbers are recognized
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|ONE
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|TWO
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|THREE
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test some arbitrary ints
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|100
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"100"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|256
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"256"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
operator|-
literal|1000
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"-1000"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Some arbitrary floats
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.1f
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"1.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|100f
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"100.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100.001f
argument_list|)
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|"-100.001"
argument_list|)
argument_list|)
expr_stmt|;
comment|// according to the specs the exponential shall not be used
comment|// but obviously there some
name|assertNotNull
argument_list|(
name|COSNumber
operator|.
name|get
argument_list|(
literal|"-2e-006"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|COSNumber
operator|.
name|get
argument_list|(
literal|"-8e+05"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"Null Value..."
argument_list|,
name|COSNumber
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failed to throw a NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// PASS
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Failed to convert a number "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

