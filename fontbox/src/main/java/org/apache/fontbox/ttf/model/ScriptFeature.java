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
name|model
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|FeatureRecord
import|;
end_import

begin_comment
comment|/**  * Models a {@link FeatureRecord}  *   * @author Palash Ray  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ScriptFeature
block|{
name|String
name|getName
parameter_list|()
function_decl|;
name|Set
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|getAllGlyphIdsForSubstitution
parameter_list|()
function_decl|;
name|boolean
name|canReplaceGlyphs
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
parameter_list|)
function_decl|;
name|Integer
name|getReplacementForGlyphs
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

