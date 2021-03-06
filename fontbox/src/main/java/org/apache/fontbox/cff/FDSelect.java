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
name|cff
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|FDSelect
block|{
specifier|protected
specifier|final
name|CFFCIDFont
name|owner
decl_stmt|;
comment|/**      * Constructor.      *      * @param owner the owner of the FDSelect data.      */
specifier|public
name|FDSelect
parameter_list|(
name|CFFCIDFont
name|owner
parameter_list|)
block|{
name|this
operator|.
name|owner
operator|=
name|owner
expr_stmt|;
block|}
comment|/**      * Returns the Font DICT index for the given GID.      *       * @param gid GID      */
specifier|public
specifier|abstract
name|int
name|getFDIndex
parameter_list|(
name|int
name|gid
parameter_list|)
function_decl|;
block|}
end_class

end_unit

