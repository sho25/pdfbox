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
name|encoding
operator|.
name|conversion
package|;
end_package

begin_import
import|import
name|org
operator|.
name|fontbox
operator|.
name|cmap
operator|.
name|CMap
import|;
end_import

begin_comment
comment|/**  *  EncodingConverter converts string or characters in one encoding, which is specified in PDF  *  file, to another string with respective java charset. The mapping from  *  PDF encoding name to java charset name is maintained by EncodingConversionManager  */
end_comment

begin_interface
specifier|public
interface|interface
name|EncodingConverter
block|{
comment|/**         *  Convert a string         */
specifier|public
name|String
name|convertString
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
comment|/** 	    *  Convert bytes to a string 	    */
specifier|public
name|String
name|convertBytes
parameter_list|(
name|byte
index|[]
name|c
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|,
name|CMap
name|cmap
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

