begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * This class represents the protection policy to use to protect  * a document with the public key security handler as described  * in the PDF specification 1.6 p104.  *  * PDF documents are encrypted so that they can be decrypted by  * one or more recipients. Each recipient have its own access permission.  *  * The following code sample shows how to protect a document using  * the public key security handler. In this code sample,<code>doc</code> is  * a<code>PDDocument</code> object.  *  *<pre>  * PublicKeyProtectionPolicy policy = new PublicKeyProtectionPolicy();  * PublicKeyRecipient recip = new PublicKeyRecipient();  * AccessPermission ap = new AccessPermission();  * ap.setCanModify(false);  * recip.setPermission(ap);  *  * // load the recipient's certificate  * InputStream inStream = new FileInputStream(certificate_path);  * CertificateFactory cf = CertificateFactory.getInstance("X.509");  * X509Certificate certificate = (X509Certificate)cf.generateCertificate(inStream);  * inStream.close();  *  * recip.setX509(certificate); // set the recipient's certificate  * policy.addRecipient(recip);  * policy.setEncryptionKeyLength(128); // the document will be encrypted with 128 bits secret key  * doc.protect(policy);  * doc.save(out);  *</pre>  *  *  * @see org.pdfbox.pdmodel.PDDocument#protect(ProtectionPolicy)  * @see AccessPermission  * @see PublicKeyRecipient  *  * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PublicKeyProtectionPolicy
extends|extends
name|ProtectionPolicy
block|{
comment|/**      * The list of recipients.      */
specifier|private
name|ArrayList
name|recipients
init|=
literal|null
decl_stmt|;
comment|/**      * The X509 certificate used to decrypt the current document.      */
specifier|private
name|X509Certificate
name|decryptionCertificate
decl_stmt|;
comment|/**      * Constructor for encryption. Just creates an empty recipients list.      */
specifier|public
name|PublicKeyProtectionPolicy
parameter_list|()
block|{
name|recipients
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
comment|/**      * Adds a new recipient to the recipients list.      *      * @param r A new recipient.      */
specifier|public
name|void
name|addRecipient
parameter_list|(
name|PublicKeyRecipient
name|r
parameter_list|)
block|{
name|recipients
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a recipient from the recipients list.      *      * @param r The recipient to remove.      *      * @return true If a recipient was found and removed.      */
specifier|public
name|boolean
name|removeRecipient
parameter_list|(
name|PublicKeyRecipient
name|r
parameter_list|)
block|{
return|return
name|recipients
operator|.
name|remove
argument_list|(
name|r
argument_list|)
return|;
block|}
comment|/**      * Returns an iterator to browse the list of recipients. Object      * found in this iterator are<code>PublicKeyRecipient</code>.      *      * @return The recipients list iterator.      */
specifier|public
name|Iterator
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
comment|/**      * Getter of the property<tt>decryptionCertificate</tt>.      *      * @return  Returns the decryptionCertificate.      */
specifier|public
name|X509Certificate
name|getDecryptionCertificate
parameter_list|()
block|{
return|return
name|decryptionCertificate
return|;
block|}
comment|/**      * Setter of the property<tt>decryptionCertificate</tt>.      *      * @param aDecryptionCertificate The decryption certificate to set.      */
specifier|public
name|void
name|setDecryptionCertificate
parameter_list|(
name|X509Certificate
name|aDecryptionCertificate
parameter_list|)
block|{
name|this
operator|.
name|decryptionCertificate
operator|=
name|aDecryptionCertificate
expr_stmt|;
block|}
comment|/**      * Returns the number of recipients.      *      * @return The number of recipients.      */
specifier|public
name|int
name|getRecipientsNumber
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

