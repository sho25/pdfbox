begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|Type1CharString
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

begin_comment
comment|/**  * A high-level mapping from character codes to glyphs.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|Type1Mapping
block|{
comment|/**      * Returns the Type 1 CharString for the character.      *      * @return the Type 1 CharString      * @throws java.io.IOException if an error occurs during reading      */
specifier|public
name|Type1CharString
name|getType1CharString
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Gets the value for the code.      *      * @return the code      */
specifier|public
name|int
name|getCode
parameter_list|()
function_decl|;
comment|/**      * Gets the value for the name.      *      * @return the name      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Gets the value for the bytes.      *      * @return the bytes      */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

