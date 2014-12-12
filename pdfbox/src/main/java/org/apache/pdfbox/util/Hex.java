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
name|util
package|;
end_package

begin_comment
comment|/**  * Utility functions for hex encoding.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|Hex
block|{
specifier|private
name|Hex
parameter_list|()
block|{}
comment|/**      * Returns a hex string of the given byte.      */
specifier|public
specifier|static
name|String
name|getString
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|toHexString
argument_list|(
literal|0x100
operator||
name|b
operator|&
literal|0xff
argument_list|)
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
comment|/**      * Returns the bytes corresponding to the ASCII hex encoding of the given byte.      */
specifier|public
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
return|return
name|getString
argument_list|(
name|b
argument_list|)
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|US_ASCII
argument_list|)
return|;
block|}
block|}
end_class

end_unit

