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
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|UnrecoverableKeyException
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_comment
comment|/**  * This class holds necessary information to decrypt a PDF document  * protected by the public key security handler.  *  * To decrypt such a document, we need:  *<ul>  *<li>a valid X509 certificate which correspond to one of the recipient of the document</li>  *<li>the private key corresponding to this certificate  *<li>the password to decrypt the private key if necessary</li>  *</ul>  *  * @author Benoit Guillon  *   */
end_comment

begin_class
specifier|public
class|class
name|PublicKeyDecryptionMaterial
extends|extends
name|DecryptionMaterial
block|{
specifier|private
name|String
name|password
init|=
literal|null
decl_stmt|;
specifier|private
name|KeyStore
name|keyStore
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|alias
init|=
literal|null
decl_stmt|;
comment|/**      * Create a new public key decryption material.      *      * @param keystore The keystore were the private key and the certificate are      * @param a The alias of the private key and the certificate.      *   If the keystore contains only 1 entry, this parameter can be left null.      * @param pwd The password to extract the private key from the keystore.      */
specifier|public
name|PublicKeyDecryptionMaterial
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|String
name|a
parameter_list|,
name|String
name|pwd
parameter_list|)
block|{
name|keyStore
operator|=
name|keystore
expr_stmt|;
name|alias
operator|=
name|a
expr_stmt|;
name|password
operator|=
name|pwd
expr_stmt|;
block|}
comment|/**      * Returns the certificate contained in the keystore.      *      * @return The certificate that will be used to try to open the document.      *      * @throws KeyStoreException If there is an error accessing the certificate.      */
specifier|public
name|X509Certificate
name|getCertificate
parameter_list|()
throws|throws
name|KeyStoreException
block|{
if|if
condition|(
name|keyStore
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keyStore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|String
name|keyStoreAlias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
return|return
operator|(
name|X509Certificate
operator|)
name|keyStore
operator|.
name|getCertificate
argument_list|(
name|keyStoreAlias
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|keyStore
operator|.
name|containsAlias
argument_list|(
name|alias
argument_list|)
condition|)
block|{
return|return
operator|(
name|X509Certificate
operator|)
name|keyStore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
return|;
block|}
throw|throw
operator|new
name|KeyStoreException
argument_list|(
literal|"the keystore does not contain the given alias"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns the password given by the user and that will be used      * to open the private key.      *      * @return The password.      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * returns The private key that will be used to open the document protection.      * @return The private key.      * @throws KeyStoreException If there is an error accessing the key.      */
specifier|public
name|Key
name|getPrivateKey
parameter_list|()
throws|throws
name|KeyStoreException
block|{
try|try
block|{
if|if
condition|(
name|keyStore
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keyStore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|String
name|keyStoreAlias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
return|return
name|keyStore
operator|.
name|getKey
argument_list|(
name|keyStoreAlias
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|keyStore
operator|.
name|containsAlias
argument_list|(
name|alias
argument_list|)
condition|)
block|{
return|return
name|keyStore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|KeyStoreException
argument_list|(
literal|"the keystore does not contain the given alias"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|UnrecoverableKeyException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|KeyStoreException
argument_list|(
literal|"the private key is not recoverable"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|KeyStoreException
argument_list|(
literal|"the algorithm necessary to recover the key is not available"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

