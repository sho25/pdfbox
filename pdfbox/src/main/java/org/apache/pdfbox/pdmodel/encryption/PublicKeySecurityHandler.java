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
name|math
operator|.
name|BigInteger
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
name|InvalidKeyException
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
name|PrivateKey
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
name|cert
operator|.
name|CertificateEncodingException
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
name|BadPaddingException
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
name|IllegalBlockSizeException
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
name|NoSuchPaddingException
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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
import|;
end_import

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
name|COSString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
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
name|ASN1ObjectIdentifier
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
name|ASN1Primitive
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
name|ASN1Set
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
name|cert
operator|.
name|X509CertificateHolder
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
name|KeyTransRecipientId
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
name|RecipientId
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
name|cms
operator|.
name|jcajce
operator|.
name|JceKeyTransEnvelopedRecipient
import|;
end_import

begin_comment
comment|/**  * This class implements the public key security handler described in the PDF specification.  *  * @see PublicKeyProtectionPolicy to see how to protect document with this security handler.  * @author Benoit Guillon  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PublicKeySecurityHandler
extends|extends
name|SecurityHandler
block|{
comment|/** The filter name. */
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
comment|/**      * Constructor used for encryption.      *      * @param p The protection policy.      */
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
comment|/**      * Prepares everything to decrypt the document.      *      * @param encryption encryption dictionary, can be retrieved via      * {@link PDDocument#getEncryption()}      * @param documentIDArray document id which is returned via      * {@link org.apache.pdfbox.cos.COSDocument#getDocumentID()} (not used by      * this handler)      * @param decryptionMaterial Information used to decrypt the document.      *      * @throws IOException If there is an error accessing data. If verbose mode      * is enabled, the exception message will provide more details why the      * match wasn't successful.      */
annotation|@
name|Override
specifier|public
name|void
name|prepareForDecryption
parameter_list|(
name|PDEncryption
name|encryption
parameter_list|,
name|COSArray
name|documentIDArray
parameter_list|,
name|DecryptionMaterial
name|decryptionMaterial
parameter_list|)
throws|throws
name|IOException
block|{
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
name|IOException
argument_list|(
literal|"Provided decryption material is not compatible with the document"
argument_list|)
throw|;
block|}
name|setDecryptMetadata
argument_list|(
name|encryption
operator|.
name|isEncryptMetaData
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|encryption
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
name|encryption
operator|.
name|getLength
argument_list|()
expr_stmt|;
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
name|encryption
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
name|int
name|i
init|=
literal|0
decl_stmt|;
name|StringBuilder
name|extraInfo
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|encryption
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
name|encryption
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
argument_list|<
name|?
argument_list|>
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
name|int
name|j
init|=
literal|0
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
name|X509Certificate
name|certificate
init|=
name|material
operator|.
name|getCertificate
argument_list|()
decl_stmt|;
name|X509CertificateHolder
name|materialCert
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|certificate
condition|)
block|{
name|materialCert
operator|=
operator|new
name|X509CertificateHolder
argument_list|(
name|certificate
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RecipientId
name|rid
init|=
name|ri
operator|.
name|getRID
argument_list|()
decl_stmt|;
if|if
condition|(
name|rid
operator|.
name|match
argument_list|(
name|materialCert
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|material
operator|.
name|getPrivateKey
argument_list|()
decl_stmt|;
name|envelopedData
operator|=
name|ri
operator|.
name|getContent
argument_list|(
operator|new
name|JceKeyTransEnvelopedRecipient
argument_list|(
name|privateKey
argument_list|)
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|j
operator|++
expr_stmt|;
if|if
condition|(
name|certificate
operator|!=
literal|null
condition|)
block|{
name|extraInfo
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
name|j
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
if|if
condition|(
name|rid
operator|instanceof
name|KeyTransRecipientId
condition|)
block|{
name|appendCertInfo
argument_list|(
name|extraInfo
argument_list|,
operator|(
name|KeyTransRecipientId
operator|)
name|rid
argument_list|,
name|certificate
argument_list|,
name|materialCert
argument_list|)
expr_stmt|;
block|}
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
name|IOException
argument_list|(
literal|"The certificate matches none of "
operator|+
name|i
operator|+
literal|" recipient entries"
operator|+
name|extraInfo
operator|.
name|toString
argument_list|()
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
name|IOException
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
name|AccessPermission
name|currentAccessPermission
init|=
operator|new
name|AccessPermission
argument_list|(
name|accessBytes
argument_list|)
decl_stmt|;
name|currentAccessPermission
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
name|setCurrentAccessPermission
argument_list|(
name|currentAccessPermission
argument_list|)
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
name|byte
index|[]
name|recipientFieldsByte
range|:
name|recipientFieldsBytes
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|recipientFieldsByte
argument_list|,
literal|0
argument_list|,
name|sha1Input
argument_list|,
name|sha1InputOffset
argument_list|,
name|recipientFieldsByte
operator|.
name|length
argument_list|)
expr_stmt|;
name|sha1InputOffset
operator|+=
name|recipientFieldsByte
operator|.
name|length
expr_stmt|;
block|}
name|MessageDigest
name|md
init|=
name|MessageDigests
operator|.
name|getSHA1
argument_list|()
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
block|}
catch|catch
parameter_list|(
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
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
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|appendCertInfo
parameter_list|(
name|StringBuilder
name|extraInfo
parameter_list|,
name|KeyTransRecipientId
name|ktRid
parameter_list|,
name|X509Certificate
name|certificate
parameter_list|,
name|X509CertificateHolder
name|materialCert
parameter_list|)
block|{
name|BigInteger
name|ridSerialNumber
init|=
name|ktRid
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|ridSerialNumber
operator|!=
literal|null
condition|)
block|{
name|String
name|certSerial
init|=
literal|"unknown"
decl_stmt|;
name|BigInteger
name|certSerialNumber
init|=
name|certificate
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|certSerialNumber
operator|!=
literal|null
condition|)
block|{
name|certSerial
operator|=
name|certSerialNumber
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
expr_stmt|;
block|}
name|extraInfo
operator|.
name|append
argument_list|(
literal|"serial-#: rid "
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
name|ridSerialNumber
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
literal|" vs. cert "
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
name|certSerial
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
literal|" issuer: rid \'"
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
name|ktRid
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
literal|"\' vs. cert \'"
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
name|materialCert
operator|==
literal|null
condition|?
literal|"null"
else|:
name|materialCert
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|extraInfo
operator|.
name|append
argument_list|(
literal|"\' "
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Prepare the document for encryption.      *      * @param doc The document that will be encrypted.      *      * @throws IOException If there is an error while encrypting.      */
annotation|@
name|Override
specifier|public
name|void
name|prepareDocumentForEncryption
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|keyLength
operator|==
literal|256
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"256 bit key length is not supported yet for public key security"
argument_list|)
throw|;
block|}
try|try
block|{
name|PDEncryption
name|dictionary
init|=
name|doc
operator|.
name|getEncryption
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictionary
operator|==
literal|null
condition|)
block|{
name|dictionary
operator|=
operator|new
name|PDEncryption
argument_list|()
expr_stmt|;
block|}
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
comment|// remove CF, StmF, and StrF entries that may be left from a previous encryption
name|dictionary
operator|.
name|removeV45filters
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setSubFilter
argument_list|(
name|SUBFILTER
argument_list|)
expr_stmt|;
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
decl_stmt|;
try|try
block|{
name|key
operator|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// should never happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
comment|// create the 20 bytes seed
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
name|byte
index|[]
index|[]
name|recipientsField
init|=
name|computeRecipientsField
argument_list|(
name|seed
argument_list|)
decl_stmt|;
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
name|sha1
init|=
name|MessageDigests
operator|.
name|getSHA1
argument_list|()
decl_stmt|;
name|byte
index|[]
name|mdResult
init|=
name|sha1
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
name|getCOSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|byte
index|[]
index|[]
name|computeRecipientsField
parameter_list|(
name|byte
index|[]
name|seed
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
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
name|getNumberOfRecipients
argument_list|()
index|]
index|[]
decl_stmt|;
name|Iterator
argument_list|<
name|PublicKeyRecipient
argument_list|>
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
comment|// put this seed in the pkcs7 input
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
name|ASN1Primitive
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
name|derOS
init|=
operator|new
name|DEROutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|derOS
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
return|return
name|recipientsField
return|;
block|}
specifier|private
name|ASN1Primitive
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
name|algorithm
init|=
literal|"1.2.840.113549.3.2"
decl_stmt|;
name|AlgorithmParameterGenerator
name|apg
decl_stmt|;
name|KeyGenerator
name|keygen
decl_stmt|;
name|Cipher
name|cipher
decl_stmt|;
try|try
block|{
name|apg
operator|=
name|AlgorithmParameterGenerator
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|keygen
operator|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|cipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// happens when using the command line app .jar file
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not find a suitable javax.crypto provider for algorithm "
operator|+
name|algorithm
operator|+
literal|"; possible reason: using an unsigned .jar file"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchPaddingException
name|e
parameter_list|)
block|{
comment|// should never happen, if this happens throw IOException instead
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find a suitable javax.crypto provider"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|AlgorithmParameters
name|parameters
init|=
name|apg
operator|.
name|generateParameters
argument_list|()
decl_stmt|;
name|ASN1InputStream
name|input
init|=
operator|new
name|ASN1InputStream
argument_list|(
name|parameters
operator|.
name|getEncoded
argument_list|(
literal|"ASN.1"
argument_list|)
argument_list|)
decl_stmt|;
name|ASN1Primitive
name|object
init|=
name|input
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|secretkey
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
literal|1
argument_list|,
name|secretkey
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|cipher
operator|.
name|doFinal
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|KeyTransRecipientInfo
name|recipientInfo
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
name|set
init|=
operator|new
name|DERSet
argument_list|(
operator|new
name|RecipientInfo
argument_list|(
name|recipientInfo
argument_list|)
argument_list|)
decl_stmt|;
name|AlgorithmIdentifier
name|algorithmId
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|algorithm
argument_list|)
argument_list|,
name|object
argument_list|)
decl_stmt|;
name|EncryptedContentInfo
name|encryptedInfo
init|=
operator|new
name|EncryptedContentInfo
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|data
argument_list|,
name|algorithmId
argument_list|,
operator|new
name|DEROctetString
argument_list|(
name|bytes
argument_list|)
argument_list|)
decl_stmt|;
name|EnvelopedData
name|enveloped
init|=
operator|new
name|EnvelopedData
argument_list|(
literal|null
argument_list|,
name|set
argument_list|,
name|encryptedInfo
argument_list|,
operator|(
name|ASN1Set
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ContentInfo
name|contentInfo
init|=
operator|new
name|ContentInfo
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|envelopedData
argument_list|,
name|enveloped
argument_list|)
decl_stmt|;
return|return
name|contentInfo
operator|.
name|toASN1Primitive
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
name|IOException
throws|,
name|CertificateEncodingException
throws|,
name|InvalidKeyException
throws|,
name|BadPaddingException
throws|,
name|IllegalBlockSizeException
block|{
name|ASN1InputStream
name|input
init|=
operator|new
name|ASN1InputStream
argument_list|(
name|x509certificate
operator|.
name|getTBSCertificate
argument_list|()
argument_list|)
decl_stmt|;
name|TBSCertificateStructure
name|certificate
init|=
name|TBSCertificateStructure
operator|.
name|getInstance
argument_list|(
name|input
operator|.
name|readObject
argument_list|()
argument_list|)
decl_stmt|;
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
name|AlgorithmIdentifier
name|algorithmId
init|=
name|certificate
operator|.
name|getSubjectPublicKeyInfo
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|IssuerAndSerialNumber
name|serial
init|=
operator|new
name|IssuerAndSerialNumber
argument_list|(
name|certificate
operator|.
name|getIssuer
argument_list|()
argument_list|,
name|certificate
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
decl_stmt|;
try|try
block|{
name|cipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithmId
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// should never happen, if this happens throw IOException instead
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find a suitable javax.crypto provider"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchPaddingException
name|e
parameter_list|)
block|{
comment|// should never happen, if this happens throw IOException instead
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find a suitable javax.crypto provider"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|octets
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
name|recipientId
init|=
operator|new
name|RecipientIdentifier
argument_list|(
name|serial
argument_list|)
decl_stmt|;
return|return
operator|new
name|KeyTransRecipientInfo
argument_list|(
name|recipientId
argument_list|,
name|algorithmId
argument_list|,
name|octets
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|hasProtectionPolicy
parameter_list|()
block|{
return|return
name|policy
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

