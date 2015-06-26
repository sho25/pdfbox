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
name|fontbox
operator|.
name|encoding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A font's built-in encoding. This is a custom encoding embedded in a font.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|BuiltInEncoding
extends|extends
name|Encoding
block|{
comment|/**      * Constructor.      *       * @param codeToName the given code to name mapping      */
specifier|public
name|BuiltInEncoding
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|codeToName
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|addCharacterEncoding
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

