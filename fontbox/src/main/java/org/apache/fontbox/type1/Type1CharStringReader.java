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
comment|/**  * Something which can read Type 1 CharStrings, namely Type 1 and CFF fonts.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|Type1CharStringReader
block|{
comment|/**      * Returns the Type 1 CharString for the character with the given name.      *      * @return Type 1 CharString      * @throws IOException if something went wrong      */
specifier|public
name|Type1CharString
name|getType1CharString
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

