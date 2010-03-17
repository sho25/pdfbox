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
name|graphics
operator|.
name|color
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
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
comment|/**  * Test cases for the {@link PDColorState} class.  */
end_comment

begin_class
specifier|public
class|class
name|PDColorStateTest
extends|extends
name|TestCase
block|{
comment|/**      * This will test setting field flags on the PDField.      *      * @throws IOException If there is an error creating the field.      */
specifier|public
name|void
name|testUnsupportedColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
name|PDColorState
name|state
init|=
operator|new
name|PDColorState
argument_list|()
decl_stmt|;
name|state
operator|.
name|setColorSpace
argument_list|(
operator|new
name|UnsupportedColorSpace
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|Color
operator|.
name|BLACK
argument_list|,
name|state
operator|.
name|getJavaColor
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: Check for the warning log message
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"PDFBOX-580: Use a dummy color instead of"
operator|+
literal|" failing with unsupported color spaces"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Dummy class used by the      * {@link PDColorStateTest#testUnsupportedColorSpace()} method.      */
specifier|private
specifier|static
class|class
name|UnsupportedColorSpace
extends|extends
name|PDColorSpace
block|{
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|1
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"unsupported color space"
return|;
block|}
specifier|protected
name|ColorSpace
name|createColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"unsupported color space"
argument_list|)
throw|;
block|}
specifier|public
name|ColorModel
name|createColorModel
parameter_list|(
name|int
name|bpc
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"unsupported color space"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

