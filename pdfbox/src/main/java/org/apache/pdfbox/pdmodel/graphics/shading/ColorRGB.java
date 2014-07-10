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
name|pdmodel
operator|.
name|graphics
operator|.
name|shading
package|;
end_package

begin_comment
comment|/**  *  * @author Shaola  */
end_comment

begin_class
class|class
name|ColorRGB
block|{
specifier|protected
specifier|final
name|float
index|[]
name|color
decl_stmt|;
name|ColorRGB
parameter_list|(
name|float
index|[]
name|c
parameter_list|)
block|{
name|color
operator|=
name|c
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

