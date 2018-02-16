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
name|ttf
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
comment|/**  * An interface that abstracts the cid&lt;-&gt; codepoint lookup functionality of cmap.  *  * @author Aaron Madlon-Kay  */
end_comment

begin_interface
specifier|public
interface|interface
name|CmapLookup
block|{
comment|/**      * Returns the GlyphId linked with the given character code.      *      * @param codePointAt the given character code to be mapped      * @return glyphId the corresponding glyph id for the given character code      */
name|int
name|getGlyphId
parameter_list|(
name|int
name|codePointAt
parameter_list|)
function_decl|;
comment|/**      * Returns all possible character codes for the given gid, or null if there is none.      *      * @param gid glyph id      * @return a list with all character codes the given gid maps to      */
name|List
argument_list|<
name|Integer
argument_list|>
name|getCharCodes
parameter_list|(
name|int
name|gid
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

