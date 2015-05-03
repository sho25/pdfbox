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
comment|/**  * Test for power user creation of a custom default CMYK color space.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDDeviceCMYKTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCMYK
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDeviceCMYK
operator|.
name|INSTANCE
operator|=
operator|new
name|CustomDeviceCMYK
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|CustomDeviceCMYK
extends|extends
name|PDDeviceCMYK
block|{
specifier|protected
name|CustomDeviceCMYK
parameter_list|()
throws|throws
name|IOException
block|{         }
block|}
block|}
end_class

end_unit

