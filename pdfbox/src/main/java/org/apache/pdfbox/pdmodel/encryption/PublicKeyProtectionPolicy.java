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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * The protection policy to use to protect a document with the public key security handler.  *  * PDF documents are encrypted so that they can be decrypted by  * one or more recipients. Each recipient have its own access permission.  *  * The following code sample shows how to protect a document using  * the public key security handler. In this code sample,<code>doc</code> is  * a<code>PDDocument</code> object.  *  *<pre>  * PublicKeyProtectionPolicy policy = new PublicKeyProtectionPolicy();  * PublicKeyRecipient recip = new PublicKeyRecipient();  * AccessPermission ap = new AccessPermission();  * ap.setCanModify(false);  * recip.setPermission(ap);  *  * // load the recipient's certificate  * InputStream inStream = new FileInputStream(certificate_path);  * CertificateFactory cf = CertificateFactory.getInstance("X.509");  * X509Certificate certificate = (X509Certificate)cf.generateCertificate(inStream);  * inStream.close();  *  * recip.setX509(certificate); // set the recipient's certificate  * policy.addRecipient(recip);  * policy.setEncryptionKeyLength(128); // the document will be encrypted with 128 bits secret key  * doc.protect(policy);  * doc.save(out);  *</pre>  *  * @see org.apache.pdfbox.pdmodel.PDDocument#protect(ProtectionPolicy)  * @see AccessPermission  * @see PublicKeyRecipient  * @author Benoit Guillon  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PublicKeyProtectionPolicy
extends|extends
name|ProtectionPolicy
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|PublicKeyRecipient
argument_list|>
name|recipients
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|X509Certificate
name|decryptionCertificate
decl_stmt|;
comment|/**      * Creates a new PublicKeyProtectionPolicy with an empty recipients list.      */
specifier|public
name|PublicKeyProtectionPolicy
parameter_list|()
block|{     }
comment|/**      * Adds a new recipient to the recipients list.      * @param recipient A new recipient.      */
specifier|public
name|void
name|addRecipient
parameter_list|(
name|PublicKeyRecipient
name|recipient
parameter_list|)
block|{
name|recipients
operator|.
name|add
argument_list|(
name|recipient
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a recipient from the recipients list.      * @param recipient The recipient to remove.      * @return true If a recipient was found and removed.      */
specifier|public
name|boolean
name|removeRecipient
parameter_list|(
name|PublicKeyRecipient
name|recipient
parameter_list|)
block|{
return|return
name|recipients
operator|.
name|remove
argument_list|(
name|recipient
argument_list|)
return|;
block|}
comment|/**      * Returns an iterator to browse the list of recipients.      * Object found in this iterator are<code>PublicKeyRecipient</code>.      * @return The recipients list iterator.      */
specifier|public
name|Iterator
argument_list|<
name|PublicKeyRecipient
argument_list|>
name|getRecipientsIterator
parameter_list|()
block|{
return|return
name|recipients
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * Returns the decryption certificate.      * @return the decryption certificate      */
specifier|public
name|X509Certificate
name|getDecryptionCertificate
parameter_list|()
block|{
return|return
name|decryptionCertificate
return|;
block|}
comment|/**      * Sets the decryption certificate      * @param decryptionCertificate the new decryption certificate.      */
specifier|public
name|void
name|setDecryptionCertificate
parameter_list|(
name|X509Certificate
name|decryptionCertificate
parameter_list|)
block|{
name|this
operator|.
name|decryptionCertificate
operator|=
name|decryptionCertificate
expr_stmt|;
block|}
comment|/**      * Returns the number of recipients      * @return the number of recipients      */
specifier|public
name|int
name|getNumberOfRecipients
parameter_list|()
block|{
return|return
name|recipients
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

