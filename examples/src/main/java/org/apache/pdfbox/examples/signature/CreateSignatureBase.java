begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|examples
operator|.
name|signature
package|;
end_package

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
name|io
operator|.
name|InputStream
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
name|PrivateKey
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
name|Certificate
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
name|CertificateException
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
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|COSBase
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
name|COSDictionary
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
name|COSName
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
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|digitalsignature
operator|.
name|PDSignature
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
name|interactive
operator|.
name|digitalsignature
operator|.
name|SignatureInterface
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
name|ASN1Encodable
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
name|ASN1EncodableVector
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
name|Attribute
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
name|AttributeTable
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
name|Attributes
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
name|cert
operator|.
name|jcajce
operator|.
name|JcaCertStore
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
name|CMSSignedData
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
name|CMSSignedDataGenerator
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
name|SignerInformation
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
name|SignerInformationStore
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
name|JcaSignerInfoGeneratorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|ContentSigner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OperatorCreationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|jcajce
operator|.
name|JcaContentSignerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|jcajce
operator|.
name|JcaDigestCalculatorProviderBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TSPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|Store
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|CreateSignatureBase
implements|implements
name|SignatureInterface
block|{
specifier|private
name|PrivateKey
name|privateKey
decl_stmt|;
specifier|private
name|Certificate
name|certificate
decl_stmt|;
specifier|private
name|TSAClient
name|tsaClient
decl_stmt|;
specifier|private
name|boolean
name|externalSigning
decl_stmt|;
comment|/**      * Initialize the signature creator with a keystore (pkcs12) and pin that should be used for the      * signature.      *      * @param keystore is a pkcs12 keystore.      * @param pin is the pin for the keystore / private key      * @throws KeyStoreException if the keystore has not been initialized (loaded)      * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found      * @throws UnrecoverableKeyException if the given password is wrong      * @throws CertificateException if the certificate is not valid as signing time      * @throws IOException if no certificate could be found      */
specifier|public
name|CreateSignatureBase
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|char
index|[]
name|pin
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|UnrecoverableKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|IOException
throws|,
name|CertificateException
block|{
comment|// grabs the first alias from the keystore and get the private key. An
comment|// alternative method or constructor could be used for setting a specific
comment|// alias that should be used.
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|String
name|alias
decl_stmt|;
name|Certificate
name|cert
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|alias
operator|=
name|aliases
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|setPrivateKey
argument_list|(
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|pin
argument_list|)
argument_list|)
expr_stmt|;
name|Certificate
index|[]
name|certChain
init|=
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|certChain
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|cert
operator|=
name|certChain
index|[
literal|0
index|]
expr_stmt|;
name|setCertificate
argument_list|(
name|cert
argument_list|)
expr_stmt|;
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
comment|// avoid expired certificate
operator|(
operator|(
name|X509Certificate
operator|)
name|cert
operator|)
operator|.
name|checkValidity
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not find certificate"
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|final
name|void
name|setPrivateKey
parameter_list|(
name|PrivateKey
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setCertificate
parameter_list|(
name|Certificate
name|certificate
parameter_list|)
block|{
name|this
operator|.
name|certificate
operator|=
name|certificate
expr_stmt|;
block|}
specifier|public
name|void
name|setTsaClient
parameter_list|(
name|TSAClient
name|tsaClient
parameter_list|)
block|{
name|this
operator|.
name|tsaClient
operator|=
name|tsaClient
expr_stmt|;
block|}
specifier|public
name|TSAClient
name|getTsaClient
parameter_list|()
block|{
return|return
name|tsaClient
return|;
block|}
comment|/**      * We just extend CMS signed Data      *      * @param signedData ´Generated CMS signed data      * @return CMSSignedData Extended CMS signed data      * @throws IOException      * @throws org.bouncycastle.tsp.TSPException      */
specifier|private
name|CMSSignedData
name|signTimeStamps
parameter_list|(
name|CMSSignedData
name|signedData
parameter_list|)
throws|throws
name|IOException
throws|,
name|TSPException
block|{
name|SignerInformationStore
name|signerStore
init|=
name|signedData
operator|.
name|getSignerInfos
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|SignerInformation
argument_list|>
name|newSigners
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SignerInformation
name|signer
range|:
name|signerStore
operator|.
name|getSigners
argument_list|()
control|)
block|{
name|newSigners
operator|.
name|add
argument_list|(
name|signTimeStamp
argument_list|(
name|signer
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO do we have to return a new store?
return|return
name|CMSSignedData
operator|.
name|replaceSigners
argument_list|(
name|signedData
argument_list|,
operator|new
name|SignerInformationStore
argument_list|(
name|newSigners
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * We are extending CMS Signature      *      * @param signer information about signer      * @return information about SignerInformation      */
specifier|private
name|SignerInformation
name|signTimeStamp
parameter_list|(
name|SignerInformation
name|signer
parameter_list|)
throws|throws
name|IOException
throws|,
name|TSPException
block|{
name|AttributeTable
name|unsignedAttributes
init|=
name|signer
operator|.
name|getUnsignedAttributes
argument_list|()
decl_stmt|;
name|ASN1EncodableVector
name|vector
init|=
operator|new
name|ASN1EncodableVector
argument_list|()
decl_stmt|;
if|if
condition|(
name|unsignedAttributes
operator|!=
literal|null
condition|)
block|{
name|vector
operator|=
name|unsignedAttributes
operator|.
name|toASN1EncodableVector
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|token
init|=
name|getTsaClient
argument_list|()
operator|.
name|getTimeStampToken
argument_list|(
name|signer
operator|.
name|getSignature
argument_list|()
argument_list|)
decl_stmt|;
name|ASN1ObjectIdentifier
name|oid
init|=
name|PKCSObjectIdentifiers
operator|.
name|id_aa_signatureTimeStampToken
decl_stmt|;
name|ASN1Encodable
name|signatureTimeStamp
init|=
operator|new
name|Attribute
argument_list|(
name|oid
argument_list|,
operator|new
name|DERSet
argument_list|(
name|ASN1Primitive
operator|.
name|fromByteArray
argument_list|(
name|token
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|vector
operator|.
name|add
argument_list|(
name|signatureTimeStamp
argument_list|)
expr_stmt|;
name|Attributes
name|signedAttributes
init|=
operator|new
name|Attributes
argument_list|(
name|vector
argument_list|)
decl_stmt|;
name|SignerInformation
name|newSigner
init|=
name|SignerInformation
operator|.
name|replaceUnsignedAttributes
argument_list|(
name|signer
argument_list|,
operator|new
name|AttributeTable
argument_list|(
name|signedAttributes
argument_list|)
argument_list|)
decl_stmt|;
comment|// TODO can this actually happen?
if|if
condition|(
name|newSigner
operator|==
literal|null
condition|)
block|{
return|return
name|signer
return|;
block|}
return|return
name|newSigner
return|;
block|}
comment|/**      * SignatureInterface implementation.      *      * This method will be called from inside of the pdfbox and create the PKCS #7 signature.      * The given InputStream contains the bytes that are given by the byte range.      *      * This method is for internal use only.      *      * Use your favorite cryptographic library to implement PKCS #7 signature creation.      */
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|(
name|InputStream
name|content
parameter_list|)
throws|throws
name|IOException
block|{
comment|//TODO this method should be private
try|try
block|{
name|List
argument_list|<
name|Certificate
argument_list|>
name|certList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|certList
operator|.
name|add
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
name|Store
name|certs
init|=
operator|new
name|JcaCertStore
argument_list|(
name|certList
argument_list|)
decl_stmt|;
name|CMSSignedDataGenerator
name|gen
init|=
operator|new
name|CMSSignedDataGenerator
argument_list|()
decl_stmt|;
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|Certificate
name|cert
init|=
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|Certificate
operator|.
name|getInstance
argument_list|(
name|ASN1Primitive
operator|.
name|fromByteArray
argument_list|(
name|certificate
operator|.
name|getEncoded
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ContentSigner
name|sha1Signer
init|=
operator|new
name|JcaContentSignerBuilder
argument_list|(
literal|"SHA256WithRSA"
argument_list|)
operator|.
name|build
argument_list|(
name|privateKey
argument_list|)
decl_stmt|;
name|gen
operator|.
name|addSignerInfoGenerator
argument_list|(
operator|new
name|JcaSignerInfoGeneratorBuilder
argument_list|(
operator|new
name|JcaDigestCalculatorProviderBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|sha1Signer
argument_list|,
operator|new
name|X509CertificateHolder
argument_list|(
name|cert
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|gen
operator|.
name|addCertificates
argument_list|(
name|certs
argument_list|)
expr_stmt|;
name|CMSProcessableInputStream
name|msg
init|=
operator|new
name|CMSProcessableInputStream
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|CMSSignedData
name|signedData
init|=
name|gen
operator|.
name|generate
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|tsaClient
operator|!=
literal|null
condition|)
block|{
name|signedData
operator|=
name|signTimeStamps
argument_list|(
name|signedData
argument_list|)
expr_stmt|;
block|}
return|return
name|signedData
operator|.
name|getEncoded
argument_list|()
return|;
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
name|TSPException
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
name|OperatorCreationException
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
comment|/**      * Set if external signing scenario should be used.      * If {@code false}, SignatureInterface would be used for signing.      *<p>      *     Default: {@code false}      *</p>      * @param externalSigning {@code true} if external signing should be performed      */
specifier|public
name|void
name|setExternalSigning
parameter_list|(
name|boolean
name|externalSigning
parameter_list|)
block|{
name|this
operator|.
name|externalSigning
operator|=
name|externalSigning
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExternalSigning
parameter_list|()
block|{
return|return
name|externalSigning
return|;
block|}
comment|/**      * Get the access permissions granted for this document in the DocMDP transform parameters      * dictionary. Details are described in the table "Entries in the DocMDP transform parameters      * dictionary" in the PDF specification.      *      * @param doc document.      * @return the permission value. 0 means no DocMDP transform parameters dictionary exists. Other      * return values are 1, 2 or 3. 2 is also returned if the DocMDP transform parameters dictionary      * is found but did not contain a /P entry, or if the value is outside the valid range.      */
specifier|public
name|int
name|getMDPPermission
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|COSBase
name|base
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PERMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|permsDict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|base
operator|=
name|permsDict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DOCMDP
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|signatureDict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|base
operator|=
name|signatureDict
operator|.
name|getDictionaryObject
argument_list|(
literal|"Reference"
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|refArray
init|=
operator|(
name|COSArray
operator|)
name|base
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
name|refArray
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|base
operator|=
name|refArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|sigRefDict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|DOCMDP
operator|.
name|equals
argument_list|(
name|sigRefDict
operator|.
name|getDictionaryObject
argument_list|(
literal|"TransformMethod"
argument_list|)
argument_list|)
condition|)
block|{
name|base
operator|=
name|sigRefDict
operator|.
name|getDictionaryObject
argument_list|(
literal|"TransformParams"
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|transformDict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|int
name|accessPermissions
init|=
name|transformDict
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessPermissions
argument_list|<
literal|1
operator|||
name|accessPermissions
argument_list|>
literal|3
condition|)
block|{
name|accessPermissions
operator|=
literal|2
expr_stmt|;
block|}
return|return
name|accessPermissions
return|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
return|return
literal|0
return|;
block|}
specifier|public
name|void
name|setMDPPermission
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|PDSignature
name|signature
parameter_list|,
name|int
name|accessPermissions
parameter_list|)
block|{
name|COSDictionary
name|sigDict
init|=
name|signature
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
comment|// DocMDP specific stuff
name|COSDictionary
name|transformParameters
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|transformParameters
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"TransformParams"
argument_list|)
argument_list|)
expr_stmt|;
name|transformParameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
name|accessPermissions
argument_list|)
expr_stmt|;
name|transformParameters
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
literal|"1.2"
argument_list|)
expr_stmt|;
name|transformParameters
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|COSDictionary
name|referenceDict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|referenceDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"SigRef"
argument_list|)
argument_list|)
expr_stmt|;
name|referenceDict
operator|.
name|setItem
argument_list|(
literal|"TransformMethod"
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"DocMDP"
argument_list|)
argument_list|)
expr_stmt|;
name|referenceDict
operator|.
name|setItem
argument_list|(
literal|"DigestMethod"
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"SHA1"
argument_list|)
argument_list|)
expr_stmt|;
name|referenceDict
operator|.
name|setItem
argument_list|(
literal|"TransformParams"
argument_list|,
name|transformParameters
argument_list|)
expr_stmt|;
name|referenceDict
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|COSArray
name|referenceArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|referenceArray
operator|.
name|add
argument_list|(
name|referenceDict
argument_list|)
expr_stmt|;
name|sigDict
operator|.
name|setItem
argument_list|(
literal|"Reference"
argument_list|,
name|referenceArray
argument_list|)
expr_stmt|;
name|referenceArray
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Catalog
name|COSDictionary
name|catalogDict
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSDictionary
name|permsDict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|catalogDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PERMS
argument_list|,
name|permsDict
argument_list|)
expr_stmt|;
name|permsDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOCMDP
argument_list|,
name|signature
argument_list|)
expr_stmt|;
name|catalogDict
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|permsDict
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

