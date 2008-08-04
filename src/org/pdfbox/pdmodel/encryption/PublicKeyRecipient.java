begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_comment
comment|/**  * Represents a recipient in the public key protection policy.  *   * @see PublicKeyProtectionPolicy  *   * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *   * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PublicKeyRecipient
block|{
specifier|private
name|X509Certificate
name|x509
decl_stmt|;
specifier|private
name|AccessPermission
name|permission
decl_stmt|;
comment|/**      * Returns the X509 certificate of the recipient.      *       * @return The X509 certificate      */
specifier|public
name|X509Certificate
name|getX509
parameter_list|()
block|{
return|return
name|x509
return|;
block|}
comment|/**      * Set the X509 certificate of the recipient.      *       * @param aX509 The X509 certificate      */
specifier|public
name|void
name|setX509
parameter_list|(
name|X509Certificate
name|aX509
parameter_list|)
block|{
name|this
operator|.
name|x509
operator|=
name|aX509
expr_stmt|;
block|}
comment|/**      * Returns the access permission granted to the recipient.      *       * @return The access permission object.      */
specifier|public
name|AccessPermission
name|getPermission
parameter_list|()
block|{
return|return
name|permission
return|;
block|}
comment|/**      * Set the access permission granted to the recipient.      *       * @param permissions The permission to set.      */
specifier|public
name|void
name|setPermission
parameter_list|(
name|AccessPermission
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|permission
operator|=
name|permissions
expr_stmt|;
block|}
block|}
end_class

end_unit

