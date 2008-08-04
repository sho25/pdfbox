begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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

