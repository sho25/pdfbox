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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AlgorithmParameterGenerator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AlgorithmParameters
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|MessageDigest
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
name|NoSuchProviderException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|KeyGenerator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|DERObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|DERObjectIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|DEROctetString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|DEROutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|DERSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|ContentInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|EncryptedContentInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|EnvelopedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|IssuerAndSerialNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|KeyTransRecipientInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|RecipientIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|RecipientInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|pkcs
operator|.
name|PKCSObjectIdentifiers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|AlgorithmIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|TBSCertificateStructure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSEnvelopedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|RecipientInformation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|CryptographyException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * This class implements the public key security handler   * described in the PDF specification.  *   * @see PDF Spec 1.6 p104  *   * @see PublicKeyProtectionPolicy to see how to protect document with this security handler.  *   * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PublicKeySecurityHandler
extends|extends
name|SecurityHandler
block|{
comment|/**      * The filter name.      */
specifier|public
specifier|static
specifier|final
name|String
name|FILTER
init|=
literal|"Adobe.PubSec"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUBFILTER
init|=
literal|"adbe.pkcs7.s4"
decl_stmt|;
specifier|private
name|PublicKeyProtectionPolicy
name|policy
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PublicKeySecurityHandler
parameter_list|()
block|{     }
comment|/**      * Constructor used for encryption.      *       * @param p The protection policy.      */
specifier|public
name|PublicKeySecurityHandler
parameter_list|(
name|PublicKeyProtectionPolicy
name|p
parameter_list|)
block|{
name|policy
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|keyLength
operator|=
name|policy
operator|.
name|getEncryptionKeyLength
argument_list|()
expr_stmt|;
block|}
comment|/**      * Decrypt the document.      *       * @param doc The document to decrypt.      * @param decryptionMaterial The data used to decrypt the document.      *       * @throws CryptographyException If there is an error during decryption.      * @throws IOException If there is an error accessing data.      */
specifier|public
name|void
name|decryptDocument
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|DecryptionMaterial
name|decryptionMaterial
parameter_list|)
throws|throws
name|CryptographyException
throws|,
name|IOException
block|{
name|this
operator|.
name|document
operator|=
name|doc
expr_stmt|;
name|PDEncryptionDictionary
name|dictionary
init|=
name|doc
operator|.
name|getEncryptionDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictionary
operator|.
name|getLength
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|this
operator|.
name|keyLength
operator|=
name|dictionary
operator|.
name|getLength
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|decryptionMaterial
operator|instanceof
name|PublicKeyDecryptionMaterial
operator|)
condition|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
literal|"Provided decryption material is not compatible with the document"
argument_list|)
throw|;
block|}
name|PublicKeyDecryptionMaterial
name|material
init|=
operator|(
name|PublicKeyDecryptionMaterial
operator|)
name|decryptionMaterial
decl_stmt|;
try|try
block|{
name|boolean
name|foundRecipient
init|=
literal|false
decl_stmt|;
comment|// the decrypted content of the enveloped data that match
comment|// the certificate in the decryption material provided
name|byte
index|[]
name|envelopedData
init|=
literal|null
decl_stmt|;
comment|// the bytes of each recipient in the recipients array
name|byte
index|[]
index|[]
name|recipientFieldsBytes
init|=
operator|new
name|byte
index|[
name|dictionary
operator|.
name|getRecipientsLength
argument_list|()
index|]
index|[]
decl_stmt|;
name|int
name|recipientFieldsLength
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dictionary
operator|.
name|getRecipientsLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSString
name|recipientFieldString
init|=
name|dictionary
operator|.
name|getRecipientStringAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|byte
index|[]
name|recipientBytes
init|=
name|recipientFieldString
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|CMSEnvelopedData
name|data
init|=
operator|new
name|CMSEnvelopedData
argument_list|(
name|recipientBytes
argument_list|)
decl_stmt|;
name|Iterator
name|recipCertificatesIt
init|=
name|data
operator|.
name|getRecipientInfos
argument_list|()
operator|.
name|getRecipients
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|recipCertificatesIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|RecipientInformation
name|ri
init|=
operator|(
name|RecipientInformation
operator|)
name|recipCertificatesIt
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Impl: if a matching certificate was previously found it is an error,
comment|// here we just don't care about it
if|if
condition|(
name|ri
operator|.
name|getRID
argument_list|()
operator|.
name|match
argument_list|(
name|material
operator|.
name|getCertificate
argument_list|()
argument_list|)
operator|&&
operator|!
name|foundRecipient
condition|)
block|{
name|foundRecipient
operator|=
literal|true
expr_stmt|;
name|envelopedData
operator|=
name|ri
operator|.
name|getContent
argument_list|(
name|material
operator|.
name|getPrivateKey
argument_list|()
argument_list|,
literal|"BC"
argument_list|)
expr_stmt|;
block|}
block|}
name|recipientFieldsBytes
index|[
name|i
index|]
operator|=
name|recipientBytes
expr_stmt|;
name|recipientFieldsLength
operator|+=
name|recipientBytes
operator|.
name|length
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|foundRecipient
operator|||
name|envelopedData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
literal|"The certificate matches no recipient entry"
argument_list|)
throw|;
block|}
if|if
condition|(
name|envelopedData
operator|.
name|length
operator|!=
literal|24
condition|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
literal|"The enveloped data does not contain 24 bytes"
argument_list|)
throw|;
block|}
comment|// now envelopedData contains:
comment|// - the 20 bytes seed
comment|// - the 4 bytes of permission for the current user
name|byte
index|[]
name|accessBytes
init|=
operator|new
name|byte
index|[
literal|4
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|envelopedData
argument_list|,
literal|20
argument_list|,
name|accessBytes
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|currentAccessPermission
operator|=
operator|new
name|AccessPermission
argument_list|(
name|accessBytes
argument_list|)
expr_stmt|;
name|currentAccessPermission
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
comment|// what we will put in the SHA1 = the seed + each byte contained in the recipients array
name|byte
index|[]
name|sha1Input
init|=
operator|new
name|byte
index|[
name|recipientFieldsLength
operator|+
literal|20
index|]
decl_stmt|;
comment|// put the seed in the sha1 input
name|System
operator|.
name|arraycopy
argument_list|(
name|envelopedData
argument_list|,
literal|0
argument_list|,
name|sha1Input
argument_list|,
literal|0
argument_list|,
literal|20
argument_list|)
expr_stmt|;
comment|// put each bytes of the recipients array in the sha1 input
name|int
name|sha1InputOffset
init|=
literal|20
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|recipientFieldsBytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|recipientFieldsBytes
index|[
name|i
index|]
argument_list|,
literal|0
argument_list|,
name|sha1Input
argument_list|,
name|sha1InputOffset
argument_list|,
name|recipientFieldsBytes
index|[
name|i
index|]
operator|.
name|length
argument_list|)
expr_stmt|;
name|sha1InputOffset
operator|+=
name|recipientFieldsBytes
index|[
name|i
index|]
operator|.
name|length
expr_stmt|;
block|}
name|MessageDigest
name|md
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|mdResult
init|=
name|md
operator|.
name|digest
argument_list|(
name|sha1Input
argument_list|)
decl_stmt|;
comment|// we have the encryption key ...
name|encryptionKey
operator|=
operator|new
name|byte
index|[
name|this
operator|.
name|keyLength
operator|/
literal|8
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|mdResult
argument_list|,
literal|0
argument_list|,
name|encryptionKey
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|keyLength
operator|/
literal|8
argument_list|)
expr_stmt|;
name|proceedDecryption
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Prepare the document for encryption.      *       * @param doc The document that will be encrypted.      *       * @throws CryptographyException If there is an error while encrypting.      */
specifier|public
name|void
name|prepareDocumentForEncryption
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|CryptographyException
block|{
try|try
block|{
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
name|PDEncryptionDictionary
name|dictionary
init|=
name|doc
operator|.
name|getEncryptionDictionary
argument_list|()
decl_stmt|;
name|dictionary
operator|.
name|setFilter
argument_list|(
name|FILTER
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setLength
argument_list|(
name|this
operator|.
name|keyLength
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setVersion
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setSubFilter
argument_list|(
name|SUBFILTER
argument_list|)
expr_stmt|;
name|byte
index|[]
index|[]
name|recipientsField
init|=
operator|new
name|byte
index|[
name|policy
operator|.
name|getRecipientsNumber
argument_list|()
index|]
index|[]
decl_stmt|;
comment|// create the 20 bytes seed
name|byte
index|[]
name|seed
init|=
operator|new
name|byte
index|[
literal|20
index|]
decl_stmt|;
name|KeyGenerator
name|key
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|key
operator|.
name|init
argument_list|(
literal|192
argument_list|,
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|SecretKey
name|sk
init|=
name|key
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|sk
operator|.
name|getEncoded
argument_list|()
argument_list|,
literal|0
argument_list|,
name|seed
argument_list|,
literal|0
argument_list|,
literal|20
argument_list|)
expr_stmt|;
comment|// create the 20 bytes seed
name|Iterator
name|it
init|=
name|policy
operator|.
name|getRecipientsIterator
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PublicKeyRecipient
name|recipient
init|=
operator|(
name|PublicKeyRecipient
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|X509Certificate
name|certificate
init|=
name|recipient
operator|.
name|getX509
argument_list|()
decl_stmt|;
name|int
name|permission
init|=
name|recipient
operator|.
name|getPermission
argument_list|()
operator|.
name|getPermissionBytesForPublicKey
argument_list|()
decl_stmt|;
name|byte
index|[]
name|pkcs7input
init|=
operator|new
name|byte
index|[
literal|24
index|]
decl_stmt|;
name|byte
name|one
init|=
call|(
name|byte
call|)
argument_list|(
name|permission
argument_list|)
decl_stmt|;
name|byte
name|two
init|=
call|(
name|byte
call|)
argument_list|(
name|permission
operator|>>>
literal|8
argument_list|)
decl_stmt|;
name|byte
name|three
init|=
call|(
name|byte
call|)
argument_list|(
name|permission
operator|>>>
literal|16
argument_list|)
decl_stmt|;
name|byte
name|four
init|=
call|(
name|byte
call|)
argument_list|(
name|permission
operator|>>>
literal|24
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|seed
argument_list|,
literal|0
argument_list|,
name|pkcs7input
argument_list|,
literal|0
argument_list|,
literal|20
argument_list|)
expr_stmt|;
comment|// put this seed in the pkcs7 input
name|pkcs7input
index|[
literal|20
index|]
operator|=
name|four
expr_stmt|;
name|pkcs7input
index|[
literal|21
index|]
operator|=
name|three
expr_stmt|;
name|pkcs7input
index|[
literal|22
index|]
operator|=
name|two
expr_stmt|;
name|pkcs7input
index|[
literal|23
index|]
operator|=
name|one
expr_stmt|;
name|DERObject
name|obj
init|=
name|createDERForRecipient
argument_list|(
name|pkcs7input
argument_list|,
name|certificate
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DEROutputStream
name|k
init|=
operator|new
name|DEROutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|k
operator|.
name|writeObject
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|recipientsField
index|[
name|i
index|]
operator|=
name|baos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|dictionary
operator|.
name|setRecipients
argument_list|(
name|recipientsField
argument_list|)
expr_stmt|;
name|int
name|sha1InputLength
init|=
name|seed
operator|.
name|length
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|dictionary
operator|.
name|getRecipientsLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|COSString
name|string
init|=
name|dictionary
operator|.
name|getRecipientStringAt
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|sha1InputLength
operator|+=
name|string
operator|.
name|getBytes
argument_list|()
operator|.
name|length
expr_stmt|;
block|}
name|byte
index|[]
name|sha1Input
init|=
operator|new
name|byte
index|[
name|sha1InputLength
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|seed
argument_list|,
literal|0
argument_list|,
name|sha1Input
argument_list|,
literal|0
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|int
name|sha1InputOffset
init|=
literal|20
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|dictionary
operator|.
name|getRecipientsLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|COSString
name|string
init|=
name|dictionary
operator|.
name|getRecipientStringAt
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|string
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|sha1Input
argument_list|,
name|sha1InputOffset
argument_list|,
name|string
operator|.
name|getBytes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|sha1InputOffset
operator|+=
name|string
operator|.
name|getBytes
argument_list|()
operator|.
name|length
expr_stmt|;
block|}
name|MessageDigest
name|md
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|mdResult
init|=
name|md
operator|.
name|digest
argument_list|(
name|sha1Input
argument_list|)
decl_stmt|;
name|this
operator|.
name|encryptionKey
operator|=
operator|new
name|byte
index|[
name|this
operator|.
name|keyLength
operator|/
literal|8
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|mdResult
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|encryptionKey
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|keyLength
operator|/
literal|8
argument_list|)
expr_stmt|;
name|doc
operator|.
name|setEncryptionDictionary
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|setEncryptionDictionary
argument_list|(
name|dictionary
operator|.
name|encryptionDictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|DERObject
name|createDERForRecipient
parameter_list|(
name|byte
index|[]
name|in
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|IOException
throws|,
name|GeneralSecurityException
block|{
name|String
name|s
init|=
literal|"1.2.840.113549.3.2"
decl_stmt|;
name|AlgorithmParameterGenerator
name|algorithmparametergenerator
init|=
name|AlgorithmParameterGenerator
operator|.
name|getInstance
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|AlgorithmParameters
name|algorithmparameters
init|=
name|algorithmparametergenerator
operator|.
name|generateParameters
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|bytearrayinputstream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|algorithmparameters
operator|.
name|getEncoded
argument_list|(
literal|"ASN.1"
argument_list|)
argument_list|)
decl_stmt|;
name|ASN1InputStream
name|asn1inputstream
init|=
operator|new
name|ASN1InputStream
argument_list|(
name|bytearrayinputstream
argument_list|)
decl_stmt|;
name|DERObject
name|derobject
init|=
name|asn1inputstream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|KeyGenerator
name|keygenerator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|keygenerator
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|secretkey
init|=
name|keygenerator
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|Cipher
name|cipher
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
literal|1
argument_list|,
name|secretkey
argument_list|,
name|algorithmparameters
argument_list|)
expr_stmt|;
name|byte
index|[]
name|abyte1
init|=
name|cipher
operator|.
name|doFinal
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|DEROctetString
name|deroctetstring
init|=
operator|new
name|DEROctetString
argument_list|(
name|abyte1
argument_list|)
decl_stmt|;
name|KeyTransRecipientInfo
name|keytransrecipientinfo
init|=
name|computeRecipientInfo
argument_list|(
name|cert
argument_list|,
name|secretkey
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|DERSet
name|derset
init|=
operator|new
name|DERSet
argument_list|(
operator|new
name|RecipientInfo
argument_list|(
name|keytransrecipientinfo
argument_list|)
argument_list|)
decl_stmt|;
name|AlgorithmIdentifier
name|algorithmidentifier
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
operator|new
name|DERObjectIdentifier
argument_list|(
name|s
argument_list|)
argument_list|,
name|derobject
argument_list|)
decl_stmt|;
name|EncryptedContentInfo
name|encryptedcontentinfo
init|=
operator|new
name|EncryptedContentInfo
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|data
argument_list|,
name|algorithmidentifier
argument_list|,
name|deroctetstring
argument_list|)
decl_stmt|;
name|EnvelopedData
name|env
init|=
operator|new
name|EnvelopedData
argument_list|(
literal|null
argument_list|,
name|derset
argument_list|,
name|encryptedcontentinfo
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ContentInfo
name|contentinfo
init|=
operator|new
name|ContentInfo
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|envelopedData
argument_list|,
name|env
argument_list|)
decl_stmt|;
return|return
name|contentinfo
operator|.
name|getDERObject
argument_list|()
return|;
block|}
specifier|private
name|KeyTransRecipientInfo
name|computeRecipientInfo
parameter_list|(
name|X509Certificate
name|x509certificate
parameter_list|,
name|byte
index|[]
name|abyte0
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|ASN1InputStream
name|asn1inputstream
init|=
operator|new
name|ASN1InputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|x509certificate
operator|.
name|getTBSCertificate
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|TBSCertificateStructure
name|tbscertificatestructure
init|=
name|TBSCertificateStructure
operator|.
name|getInstance
argument_list|(
name|asn1inputstream
operator|.
name|readObject
argument_list|()
argument_list|)
decl_stmt|;
name|AlgorithmIdentifier
name|algorithmidentifier
init|=
name|tbscertificatestructure
operator|.
name|getSubjectPublicKeyInfo
argument_list|()
operator|.
name|getAlgorithmId
argument_list|()
decl_stmt|;
name|IssuerAndSerialNumber
name|issuerandserialnumber
init|=
operator|new
name|IssuerAndSerialNumber
argument_list|(
name|tbscertificatestructure
operator|.
name|getIssuer
argument_list|()
argument_list|,
name|tbscertificatestructure
operator|.
name|getSerialNumber
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|Cipher
name|cipher
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithmidentifier
operator|.
name|getObjectId
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
literal|1
argument_list|,
name|x509certificate
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
name|DEROctetString
name|deroctetstring
init|=
operator|new
name|DEROctetString
argument_list|(
name|cipher
operator|.
name|doFinal
argument_list|(
name|abyte0
argument_list|)
argument_list|)
decl_stmt|;
name|RecipientIdentifier
name|recipId
init|=
operator|new
name|RecipientIdentifier
argument_list|(
name|issuerandserialnumber
argument_list|)
decl_stmt|;
return|return
operator|new
name|KeyTransRecipientInfo
argument_list|(
name|recipId
argument_list|,
name|algorithmidentifier
argument_list|,
name|deroctetstring
argument_list|)
return|;
block|}
block|}
end_class

end_unit

