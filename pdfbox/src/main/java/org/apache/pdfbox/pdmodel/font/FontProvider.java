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
name|font
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * External font service provider interface.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|FontProvider
block|{
comment|/**      * Returns a string containing debugging information. This will be written to the log if no      * suitable fonts are found and no fallback fonts are available. May be null.      */
specifier|public
specifier|abstract
name|String
name|toDebugString
parameter_list|()
function_decl|;
comment|/**      * Returns a list of information about fonts on the system.      */
specifier|public
specifier|abstract
name|List
argument_list|<
name|?
extends|extends
name|FontInfo
argument_list|>
name|getFontInfo
parameter_list|()
function_decl|;
block|}
end_class

end_unit

