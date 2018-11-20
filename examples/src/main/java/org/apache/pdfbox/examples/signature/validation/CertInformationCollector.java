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
name|examples
operator|.
name|signature
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|io
operator|.
name|InputStream
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
name|net
operator|.
name|URL
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
name|SignatureException
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
name|CertificateFactory
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|examples
operator|.
name|signature
operator|.
name|cert
operator|.
name|CertificateVerifier
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
name|bouncycastle
operator|.
name|asn1
operator|.
name|DERSequence
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
name|Extension
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
name|JcaX509CertificateConverter
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
name|SignerInformation
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
name|tsp
operator|.
name|TimeStampToken
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
name|Selector
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

begin_comment
comment|/**  * This Class helps to extract Data/Information from a signature. The information is held in  * CertSignatureInformation. Some information is needed for validation processing of the  * participating certificates.  *  * @author Alexis Suter  *  */
end_comment

begin_class
specifier|public
class|class
name|CertInformationCollector
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CertInformationCollector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_CERTIFICATE_CHAIN_DEPTH
init|=
literal|5
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|BigInteger
argument_list|,
name|X509Certificate
argument_list|>
name|certificatesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|JcaX509CertificateConverter
name|certConverter
init|=
operator|new
name|JcaX509CertificateConverter
argument_list|()
decl_stmt|;
specifier|private
name|CertSignatureInformation
name|rootCertInfo
decl_stmt|;
comment|/**      * Gets the certificate information of a signature.      *       * @param signature the signature of the document.      * @param fileName of the document.      * @return the CertSignatureInformation containing all certificate information      * @throws CertificateProccessingException when there is an error processing the certificates      * @throws IOException on a data processing error      */
specifier|public
name|CertSignatureInformation
name|getLastCertInfo
parameter_list|(
name|PDSignature
name|signature
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|CertificateProccessingException
throws|,
name|IOException
block|{
try|try
init|(
name|FileInputStream
name|documentInput
init|=
operator|new
name|FileInputStream
argument_list|(
name|fileName
argument_list|)
init|)
block|{
name|byte
index|[]
name|signatureContent
init|=
name|signature
operator|.
name|getContents
argument_list|(
name|documentInput
argument_list|)
decl_stmt|;
return|return
name|getCertInfo
argument_list|(
name|signatureContent
argument_list|)
return|;
block|}
block|}
comment|/**      * Processes one signature and its including certificates.      *      * @param signatureContent the byte[]-Content of the signature      * @return the CertSignatureInformation for this signature      * @throws IOException      * @throws CertificateProccessingException      */
specifier|private
name|CertSignatureInformation
name|getCertInfo
parameter_list|(
name|byte
index|[]
name|signatureContent
parameter_list|)
throws|throws
name|CertificateProccessingException
throws|,
name|IOException
block|{
name|rootCertInfo
operator|=
operator|new
name|CertSignatureInformation
argument_list|()
expr_stmt|;
name|rootCertInfo
operator|.
name|signatureHash
operator|=
name|CertInformationHelper
operator|.
name|getSha1Hash
argument_list|(
name|signatureContent
argument_list|)
expr_stmt|;
try|try
block|{
name|CMSSignedData
name|signedData
init|=
operator|new
name|CMSSignedData
argument_list|(
name|signatureContent
argument_list|)
decl_stmt|;
name|Store
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certificatesStore
init|=
name|signedData
operator|.
name|getCertificates
argument_list|()
decl_stmt|;
name|SignerInformation
name|signerInformation
init|=
name|processSignerStore
argument_list|(
name|certificatesStore
argument_list|,
name|signedData
argument_list|,
name|rootCertInfo
argument_list|)
decl_stmt|;
name|addTimestampCerts
argument_list|(
name|signerInformation
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CMSException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error occurred getting Certificate Information from Signature"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CertificateProccessingException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|rootCertInfo
return|;
block|}
comment|/**      * Processes an embedded signed timestamp, that has been placed into a signature. The      * certificates and its chain(s) will be processed the same way as the signature itself.      *      * @param signerInformation of the signature, to get unsigned attributes from it.      * @throws IOException      * @throws CertificateProccessingException      */
specifier|private
name|void
name|addTimestampCerts
parameter_list|(
name|SignerInformation
name|signerInformation
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateProccessingException
block|{
name|AttributeTable
name|unsignedAttributes
init|=
name|signerInformation
operator|.
name|getUnsignedAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|unsignedAttributes
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Attribute
name|tsAttribute
init|=
name|signerInformation
operator|.
name|getUnsignedAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_aa_signatureTimeStampToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|tsAttribute
operator|.
name|getAttrValues
argument_list|()
operator|instanceof
name|DERSet
condition|)
block|{
name|DERSet
name|tsSet
init|=
operator|(
name|DERSet
operator|)
name|tsAttribute
operator|.
name|getAttrValues
argument_list|()
decl_stmt|;
name|tsSet
operator|.
name|getEncoded
argument_list|(
literal|"DER"
argument_list|)
expr_stmt|;
name|DERSequence
name|tsSeq
init|=
operator|(
name|DERSequence
operator|)
name|tsSet
operator|.
name|getObjectAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|TimeStampToken
name|tsToken
init|=
operator|new
name|TimeStampToken
argument_list|(
operator|new
name|CMSSignedData
argument_list|(
name|tsSeq
operator|.
name|getEncoded
argument_list|(
literal|"DER"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|rootCertInfo
operator|.
name|tsaCerts
operator|=
operator|new
name|CertSignatureInformation
argument_list|()
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Store
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certificatesStore
init|=
name|tsToken
operator|.
name|getCertificates
argument_list|()
decl_stmt|;
name|processSignerStore
argument_list|(
name|certificatesStore
argument_list|,
name|tsToken
operator|.
name|toCMSSignedData
argument_list|()
argument_list|,
name|rootCertInfo
operator|.
name|tsaCerts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TSPException
decl||
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error parsing timestamp token"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Processes a signer store and goes through the signers certificate-chain. Adds the found data      * to the certInfo. Handles only the first signer, although multiple would be possible, but is      * not yet practicable.      *      * @param certificatesStore To get the certificate information from. Certificates will be saved      * in certificatesMap.      * @param signedData data from which to get the SignerInformation      * @param certInfo where to add certificate information      * @return Signer Information of the processed certificatesStore for further usage.      * @throws IOException on data-processing error      * @throws CertificateProccessingException on a specific error with a certificate      */
specifier|private
name|SignerInformation
name|processSignerStore
parameter_list|(
name|Store
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certificatesStore
parameter_list|,
name|CMSSignedData
name|signedData
parameter_list|,
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateProccessingException
block|{
name|Collection
argument_list|<
name|SignerInformation
argument_list|>
name|signers
init|=
name|signedData
operator|.
name|getSignerInfos
argument_list|()
operator|.
name|getSigners
argument_list|()
decl_stmt|;
name|SignerInformation
name|signerInformation
init|=
name|signers
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Collection
argument_list|<
name|X509CertificateHolder
argument_list|>
name|matches
init|=
name|certificatesStore
operator|.
name|getMatches
argument_list|(
operator|(
name|Selector
argument_list|<
name|X509CertificateHolder
argument_list|>
operator|)
name|signerInformation
operator|.
name|getSID
argument_list|()
argument_list|)
decl_stmt|;
name|X509Certificate
name|certificate
init|=
name|getCertFromHolder
argument_list|(
name|matches
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|X509CertificateHolder
argument_list|>
name|allCerts
init|=
name|certificatesStore
operator|.
name|getMatches
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|addAllCerts
argument_list|(
name|allCerts
argument_list|)
expr_stmt|;
name|traverseChain
argument_list|(
name|certificate
argument_list|,
name|certInfo
argument_list|,
name|MAX_CERTIFICATE_CHAIN_DEPTH
argument_list|)
expr_stmt|;
return|return
name|signerInformation
return|;
block|}
comment|/**      * Traverse through the Cert-Chain of the given Certificate and add it to the CertInfo      * recursively.      *      * @param certificate Actual Certificate to be processed      * @param certInfo where to add the Certificate (and chain) information      * @param maxDepth Max depth from this point to go through CertChain (could be infinite)      * @throws IOException on data-processing error      * @throws CertificateProccessingException on a specific error with a certificate      */
specifier|private
name|void
name|traverseChain
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|CertSignatureInformation
name|certInfo
parameter_list|,
name|int
name|maxDepth
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateProccessingException
block|{
name|certInfo
operator|.
name|certificate
operator|=
name|certificate
expr_stmt|;
comment|// Certificate Authority Information Access
comment|// As described in https://tools.ietf.org/html/rfc3280.html#section-4.2.2.1
name|byte
index|[]
name|authorityExtensionValue
init|=
name|certificate
operator|.
name|getExtensionValue
argument_list|(
name|Extension
operator|.
name|authorityInfoAccess
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|authorityExtensionValue
operator|!=
literal|null
condition|)
block|{
name|CertInformationHelper
operator|.
name|getAuthorityInfoExtensionValue
argument_list|(
name|authorityExtensionValue
argument_list|,
name|certInfo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|certInfo
operator|.
name|issuerUrl
operator|!=
literal|null
condition|)
block|{
name|getAlternativeIssuerCertificate
argument_list|(
name|certInfo
argument_list|,
name|maxDepth
argument_list|)
expr_stmt|;
block|}
comment|// As described in https://tools.ietf.org/html/rfc3280.html#section-4.2.1.14
name|byte
index|[]
name|crlExtensionValue
init|=
name|certificate
operator|.
name|getExtensionValue
argument_list|(
name|Extension
operator|.
name|cRLDistributionPoints
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|crlExtensionValue
operator|!=
literal|null
condition|)
block|{
name|certInfo
operator|.
name|crlUrl
operator|=
name|CertInformationHelper
operator|.
name|getCrlUrlFromExtensionValue
argument_list|(
name|crlExtensionValue
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|certInfo
operator|.
name|isSelfSigned
operator|=
name|CertificateVerifier
operator|.
name|isSelfSigned
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateProccessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|maxDepth
operator|<=
literal|0
operator|||
name|certInfo
operator|.
name|isSelfSigned
condition|)
block|{
return|return;
block|}
for|for
control|(
name|X509Certificate
name|issuer
range|:
name|certificatesMap
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|certificate
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|equals
argument_list|(
name|issuer
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|certificate
operator|.
name|verify
argument_list|(
name|issuer
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateException
decl||
name|NoSuchAlgorithmException
decl||
name|InvalidKeyException
decl||
name|SignatureException
decl||
name|NoSuchProviderException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateProccessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Found the right Issuer Cert! for Cert: "
operator|+
name|certificate
operator|.
name|getSubjectX500Principal
argument_list|()
operator|+
literal|"\n"
operator|+
name|issuer
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
expr_stmt|;
name|certInfo
operator|.
name|issuerCertificate
operator|=
name|issuer
expr_stmt|;
name|certInfo
operator|.
name|certChain
operator|=
operator|new
name|CertSignatureInformation
argument_list|()
expr_stmt|;
name|traverseChain
argument_list|(
name|issuer
argument_list|,
name|certInfo
operator|.
name|certChain
argument_list|,
name|maxDepth
operator|-
literal|1
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|certInfo
operator|.
name|issuerCertificate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No Issuer Certificate found for Cert: "
operator|+
name|certificate
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get alternative certificate chain, from the Authority Information (a url). If the chain is      * not included in the signature, this is the main chain. Otherwise there might be a second      * chain. Exceptions which happen on this chain will be logged and ignored, because the cert      * might not be available at the time or other reasons.      *      * @param certInfo base Certificate Information, on which to put the alternative Certificate      * @param maxDepth Maximum depth to dig through the chain from here on.      * @throws CertificateProccessingException on a specific error with a certificate      */
specifier|private
name|void
name|getAlternativeIssuerCertificate
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|,
name|int
name|maxDepth
parameter_list|)
throws|throws
name|CertificateProccessingException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Get alternative issuer certificate from: "
operator|+
name|certInfo
operator|.
name|issuerUrl
argument_list|)
expr_stmt|;
try|try
block|{
name|URL
name|certUrl
init|=
operator|new
name|URL
argument_list|(
name|certInfo
operator|.
name|issuerUrl
argument_list|)
decl_stmt|;
name|CertificateFactory
name|certFactory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|certUrl
operator|.
name|openStream
argument_list|()
init|)
block|{
name|X509Certificate
name|altIssuerCert
init|=
operator|(
name|X509Certificate
operator|)
name|certFactory
operator|.
name|generateCertificate
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|addCertToCertificatesMap
argument_list|(
name|altIssuerCert
argument_list|)
expr_stmt|;
name|certInfo
operator|.
name|alternativeCertChain
operator|=
operator|new
name|CertSignatureInformation
argument_list|()
expr_stmt|;
name|traverseChain
argument_list|(
name|altIssuerCert
argument_list|,
name|certInfo
operator|.
name|alternativeCertChain
argument_list|,
name|maxDepth
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|CertificateException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error getting alternative issuer certificate from "
operator|+
name|certInfo
operator|.
name|issuerUrl
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds the given Certificate to the certificatesMap, if not yet containing.      *       * @param certificate to add to the certificatesMap      */
specifier|private
name|void
name|addCertToCertificatesMap
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|)
block|{
if|if
condition|(
operator|!
name|certificatesMap
operator|.
name|containsKey
argument_list|(
name|certificate
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
condition|)
block|{
name|certificatesMap
operator|.
name|put
argument_list|(
name|certificate
operator|.
name|getSerialNumber
argument_list|()
argument_list|,
name|certificate
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the X509Certificate out of the X509CertificateHolder and add it to certificatesMap.      *      * @param certificateHolder to get the certificate from      * @return a X509Certificate or<code>null</code> when there was an Error with the Certificate      * @throws CertificateProccessingException on failed conversion from X509CertificateHolder to X509Certificate      */
specifier|private
name|X509Certificate
name|getCertFromHolder
parameter_list|(
name|X509CertificateHolder
name|certificateHolder
parameter_list|)
throws|throws
name|CertificateProccessingException
block|{
comment|//TODO getCertFromHolder violates "do one thing" rule (adds to the map and returns a certificate)
if|if
condition|(
operator|!
name|certificatesMap
operator|.
name|containsKey
argument_list|(
name|certificateHolder
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|X509Certificate
name|certificate
init|=
name|certConverter
operator|.
name|getCertificate
argument_list|(
name|certificateHolder
argument_list|)
decl_stmt|;
name|certificatesMap
operator|.
name|put
argument_list|(
name|certificate
operator|.
name|getSerialNumber
argument_list|()
argument_list|,
name|certificate
argument_list|)
expr_stmt|;
return|return
name|certificate
return|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Certificate Exception getting Certificate from certHolder."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CertificateProccessingException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|certificatesMap
operator|.
name|get
argument_list|(
name|certificateHolder
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Adds multiple Certificates out of a Collection of X509CertificateHolder into certificatesMap.      *      * @param certHolders Collection of X509CertificateHolder      */
specifier|private
name|void
name|addAllCerts
parameter_list|(
name|Collection
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certHolders
parameter_list|)
block|{
for|for
control|(
name|X509CertificateHolder
name|certificateHolder
range|:
name|certHolders
control|)
block|{
try|try
block|{
name|getCertFromHolder
argument_list|(
name|certificateHolder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateProccessingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Certificate Exception getting Certificate from certHolder."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Gets a list of X509Certificate out of an array of X509CertificateHolder. The certificates      * will be added to certificatesMap.      *      * @param certHolders Array of X509CertificateHolder      * @throws CertificateProccessingException when one of the Certificates could not be parsed.      */
specifier|public
name|void
name|addAllCertsFromHolders
parameter_list|(
name|X509CertificateHolder
index|[]
name|certHolders
parameter_list|)
throws|throws
name|CertificateProccessingException
block|{
name|addAllCerts
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|certHolders
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Traverse the OCSP certificate.      *      * @param certHolder      * @return      * @throws CertificateProccessingException       */
name|CertSignatureInformation
name|getOCSPCertInfo
parameter_list|(
name|X509CertificateHolder
name|certHolder
parameter_list|)
throws|throws
name|CertificateProccessingException
block|{
try|try
block|{
name|CertSignatureInformation
name|certSignatureInformation
init|=
operator|new
name|CertSignatureInformation
argument_list|()
decl_stmt|;
name|traverseChain
argument_list|(
name|certConverter
operator|.
name|getCertificate
argument_list|(
name|certHolder
argument_list|)
argument_list|,
name|certSignatureInformation
argument_list|,
name|MAX_CERTIFICATE_CHAIN_DEPTH
argument_list|)
expr_stmt|;
return|return
name|certSignatureInformation
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|CertificateException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateProccessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get the map of all processed certificates until now.      *       * @return a map of serial numbers to certificates.      */
specifier|public
name|Map
argument_list|<
name|BigInteger
argument_list|,
name|X509Certificate
argument_list|>
name|getCertificatesMap
parameter_list|()
block|{
return|return
name|certificatesMap
return|;
block|}
comment|/**      * Data class to hold Signature, Certificate (and its chain(s)) and revocation Information      */
specifier|public
class|class
name|CertSignatureInformation
block|{
specifier|private
name|X509Certificate
name|certificate
decl_stmt|;
specifier|private
name|String
name|signatureHash
decl_stmt|;
specifier|private
name|boolean
name|isSelfSigned
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|ocspUrl
decl_stmt|;
specifier|private
name|String
name|crlUrl
decl_stmt|;
specifier|private
name|String
name|issuerUrl
decl_stmt|;
specifier|private
name|X509Certificate
name|issuerCertificate
decl_stmt|;
specifier|private
name|CertSignatureInformation
name|certChain
decl_stmt|;
specifier|private
name|CertSignatureInformation
name|tsaCerts
decl_stmt|;
specifier|private
name|CertSignatureInformation
name|alternativeCertChain
decl_stmt|;
specifier|public
name|String
name|getOcspUrl
parameter_list|()
block|{
return|return
name|ocspUrl
return|;
block|}
specifier|public
name|void
name|setOcspUrl
parameter_list|(
name|String
name|ocspUrl
parameter_list|)
block|{
name|this
operator|.
name|ocspUrl
operator|=
name|ocspUrl
expr_stmt|;
block|}
specifier|public
name|void
name|setIssuerUrl
parameter_list|(
name|String
name|issuerUrl
parameter_list|)
block|{
name|this
operator|.
name|issuerUrl
operator|=
name|issuerUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getCrlUrl
parameter_list|()
block|{
return|return
name|crlUrl
return|;
block|}
specifier|public
name|X509Certificate
name|getCertificate
parameter_list|()
block|{
return|return
name|certificate
return|;
block|}
specifier|public
name|boolean
name|isSelfSigned
parameter_list|()
block|{
return|return
name|isSelfSigned
return|;
block|}
specifier|public
name|X509Certificate
name|getIssuerCertificate
parameter_list|()
block|{
return|return
name|issuerCertificate
return|;
block|}
specifier|public
name|String
name|getSignatureHash
parameter_list|()
block|{
return|return
name|signatureHash
return|;
block|}
specifier|public
name|CertSignatureInformation
name|getCertChain
parameter_list|()
block|{
return|return
name|certChain
return|;
block|}
specifier|public
name|CertSignatureInformation
name|getTsaCerts
parameter_list|()
block|{
return|return
name|tsaCerts
return|;
block|}
specifier|public
name|CertSignatureInformation
name|getAlternativeCertChain
parameter_list|()
block|{
return|return
name|alternativeCertChain
return|;
block|}
block|}
block|}
end_class

end_unit

