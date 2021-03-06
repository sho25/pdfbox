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
operator|.
name|gsub
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
comment|/**  * This class is responsible for replacing GlyphIDs with new ones according to the GSUB tables. Each language should  * have an implementation of this.  *   * @author Palash Ray  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|GsubWorker
block|{
comment|/**      * Applies language-specific transforms including GSUB and any other pre or post-processing necessary for displaying      * Glyphs correctly.      *       */
name|List
argument_list|<
name|Integer
argument_list|>
name|applyTransforms
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|originalGlyphIds
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

