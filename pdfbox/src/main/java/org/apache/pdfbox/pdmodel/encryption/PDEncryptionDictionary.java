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
name|encryption
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_comment
comment|/**  * @deprecated Use {@link PDEncryption} instead  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|PDEncryptionDictionary
extends|extends
name|PDEncryption
block|{
comment|/**      * @deprecated Use {@link PDEncryption()} instead      */
specifier|public
name|PDEncryptionDictionary
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * @deprecated Use {@link PDEncryption(COSDictionary)} instead      * @param dictionary a COS encryption dictionary      */
specifier|public
name|PDEncryptionDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

