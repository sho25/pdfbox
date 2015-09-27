begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *   Licensed to the Apache Software Foundation (ASF) under one or more  *   contributor license agreements.  See the NOTICE file distributed with  *   this work for additional information regarding copyright ownership.  *   The ASF licenses this file to You under the Apache License, Version 2.0  *   (the "License"); you may not use this file except in compliance with  *   the License.  You may obtain a copy of the License at  *  *        http://www.apache.org/licenses/LICENSE-2.0  *  *   Unless required by applicable law or agreed to in writing, software  *   distributed under the License is distributed on an "AS IS" BASIS,  *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *   See the License for the specific language governing permissions and  *   limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|tools
operator|.
name|pdfdebugger
operator|.
name|flagbitspane
package|;
end_package

begin_comment
comment|/**  * @author Khyrul Bashar  *  * An abstract class that provides Flag bits.  */
end_comment

begin_class
specifier|abstract
class|class
name|Flag
block|{
specifier|abstract
name|String
name|getFlagType
parameter_list|()
function_decl|;
specifier|abstract
name|String
name|getFlagValue
parameter_list|()
function_decl|;
specifier|abstract
name|Object
index|[]
index|[]
name|getFlagBits
parameter_list|()
function_decl|;
name|String
index|[]
name|getColumnNames
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"Bit Position"
block|,
literal|"Name"
block|,
literal|"Set"
block|}
return|;
block|}
block|}
end_class

end_unit

