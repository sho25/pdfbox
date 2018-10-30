begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|cert
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
name|PublicKey
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
name|CertPathBuilder
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
name|CertPathBuilderException
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
name|CertStore
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
name|CollectionCertStoreParameters
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
name|PKIXBuilderParameters
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
name|PKIXCertPathBuilderResult
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
name|TrustAnchor
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
name|X509CertSelector
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
name|Date
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|validation
operator|.
name|OcspHelper
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
name|validation
operator|.
name|RevokedCertificateException
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
name|ASN1Sequence
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
name|DERTaggedObject
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
name|asn1
operator|.
name|x509
operator|.
name|GeneralName
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
name|X509ObjectIdentifiers
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
name|JcaX509ExtensionUtils
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
name|ocsp
operator|.
name|OCSPException
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
name|ocsp
operator|.
name|OCSPResp
import|;
end_import

begin_comment
comment|/**  * Copied from Apache CXF 2.4.9, initial version:  * https://svn.apache.org/repos/asf/cxf/tags/cxf-2.4.9/distribution/src/main/release/samples/sts_issue_operation/src/main/java/demo/sts/provider/cert/  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CertificateVerifier
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
name|CertificateVerifier
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CertificateVerifier
parameter_list|()
block|{      }
comment|/**      * Attempts to build a certification chain for given certificate and to      * verify it. Relies on a set of root CA certificates and intermediate      * certificates that will be used for building the certification chain. The      * verification process assumes that all self-signed certificates in the set      * are trusted root CA certificates and all other certificates in the set      * are intermediate certificates.      *      * @param cert - certificate for validation      * @param additionalCerts - set of trusted root CA certificates that will be      * used as "trust anchors" and intermediate CA certificates that will be      * used as part of the certification chain. All self-signed certificates are      * considered to be trusted root CA certificates. All the rest are      * considered to be intermediate CA certificates.      * @param verifySelfSignedCert true if a self-signed certificate is accepted, false if not.      * @param signDate the date when the signing took place      * @return the certification chain (if verification is successful)      * @throws CertificateVerificationException - if the certification is not      * successful (e.g. certification path cannot be built or some certificate      * in the chain is expired or CRL checks are failed)      */
specifier|public
specifier|static
name|PKIXCertPathBuilderResult
name|verifyCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|additionalCerts
parameter_list|,
name|boolean
name|verifySelfSignedCert
parameter_list|,
name|Date
name|signDate
parameter_list|)
throws|throws
name|CertificateVerificationException
block|{
try|try
block|{
comment|// Check for self-signed certificate
if|if
condition|(
operator|!
name|verifySelfSignedCert
operator|&&
name|isSelfSigned
argument_list|(
name|cert
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"The certificate is self-signed."
argument_list|)
throw|;
block|}
comment|// Prepare a set of trusted root CA certificates
comment|// and a set of intermediate certificates
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|trustedRootCerts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|intermediateCerts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|X509Certificate
name|issuerCert
init|=
literal|null
decl_stmt|;
for|for
control|(
name|X509Certificate
name|additionalCert
range|:
name|additionalCerts
control|)
block|{
if|if
condition|(
name|isSelfSigned
argument_list|(
name|additionalCert
argument_list|)
condition|)
block|{
name|trustedRootCerts
operator|.
name|add
argument_list|(
name|additionalCert
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|intermediateCerts
operator|.
name|add
argument_list|(
name|additionalCert
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cert
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|equals
argument_list|(
name|additionalCert
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
condition|)
block|{
name|issuerCert
operator|=
name|additionalCert
expr_stmt|;
block|}
block|}
comment|// Attempt to build the certification chain and verify it
name|PKIXCertPathBuilderResult
name|verifiedCertChain
init|=
name|verifyCertificate
argument_list|(
name|cert
argument_list|,
name|trustedRootCerts
argument_list|,
name|intermediateCerts
argument_list|,
name|signDate
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Certification chain verified successfully"
argument_list|)
expr_stmt|;
comment|// Try checking the certificate through OCSP (faster than CRL)
name|String
name|ocspURL
init|=
name|extractOCSPURL
argument_list|(
name|cert
argument_list|)
decl_stmt|;
if|if
condition|(
name|ocspURL
operator|!=
literal|null
condition|)
block|{
name|OcspHelper
name|ocspHelper
init|=
operator|new
name|OcspHelper
argument_list|(
name|cert
argument_list|,
name|issuerCert
argument_list|,
name|ocspURL
argument_list|)
decl_stmt|;
name|verifyOCSP
argument_list|(
name|ocspHelper
argument_list|,
name|signDate
argument_list|)
expr_stmt|;
return|return
name|verifiedCertChain
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"OCSP not available, will try CRL"
argument_list|)
expr_stmt|;
block|}
comment|// Check whether the certificate is revoked by the CRL
comment|// given in its CRL distribution point extension
name|CRLVerifier
operator|.
name|verifyCertificateCRLs
argument_list|(
name|cert
argument_list|,
name|signDate
argument_list|,
name|additionalCerts
argument_list|)
expr_stmt|;
return|return
name|verifiedCertChain
return|;
block|}
catch|catch
parameter_list|(
name|CertPathBuilderException
name|certPathEx
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"Error building certification path: "
operator|+
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|,
name|certPathEx
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CertificateVerificationException
name|cvex
parameter_list|)
block|{
throw|throw
name|cvex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"Error verifying the certificate: "
operator|+
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Checks whether given X.509 certificate is self-signed.      * @param cert The X.509 certificate to check.      * @return true if the certificate is self-signed, false if not.      * @throws java.security.GeneralSecurityException       */
specifier|public
specifier|static
name|boolean
name|isSelfSigned
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
try|try
block|{
comment|// Try to verify certificate signature with its own public key
name|PublicKey
name|key
init|=
name|cert
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
name|cert
operator|.
name|verify
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SignatureException
decl||
name|InvalidKeyException
name|sigEx
parameter_list|)
block|{
comment|// Invalid signature --> not self-signed
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't get signature information - returning false"
argument_list|,
name|sigEx
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Attempts to build a certification chain for given certificate and to      * verify it. Relies on a set of root CA certificates (trust anchors) and a      * set of intermediate certificates (to be used as part of the chain).      *      * @param cert - certificate for validation      * @param trustedRootCerts - set of trusted root CA certificates      * @param intermediateCerts - set of intermediate certificates      * @param signDate the date when the signing took place      * @return the certification chain (if verification is successful)      * @throws GeneralSecurityException - if the verification is not successful      * (e.g. certification path cannot be built or some certificate in the chain      * is expired)      */
specifier|private
specifier|static
name|PKIXCertPathBuilderResult
name|verifyCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|trustedRootCerts
parameter_list|,
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|intermediateCerts
parameter_list|,
name|Date
name|signDate
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
comment|// Create the selector that specifies the starting certificate
name|X509CertSelector
name|selector
init|=
operator|new
name|X509CertSelector
argument_list|()
decl_stmt|;
name|selector
operator|.
name|setCertificate
argument_list|(
name|cert
argument_list|)
expr_stmt|;
comment|// Create the trust anchors (set of root CA certificates)
name|Set
argument_list|<
name|TrustAnchor
argument_list|>
name|trustAnchors
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|X509Certificate
name|trustedRootCert
range|:
name|trustedRootCerts
control|)
block|{
name|trustAnchors
operator|.
name|add
argument_list|(
operator|new
name|TrustAnchor
argument_list|(
name|trustedRootCert
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Configure the PKIX certificate builder algorithm parameters
name|PKIXBuilderParameters
name|pkixParams
init|=
operator|new
name|PKIXBuilderParameters
argument_list|(
name|trustAnchors
argument_list|,
name|selector
argument_list|)
decl_stmt|;
comment|// Disable CRL checks (this is done manually as additional step)
name|pkixParams
operator|.
name|setRevocationEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|pkixParams
operator|.
name|setDate
argument_list|(
name|signDate
argument_list|)
expr_stmt|;
comment|// Specify a list of intermediate certificates
name|CertStore
name|intermediateCertStore
init|=
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|intermediateCerts
argument_list|)
argument_list|)
decl_stmt|;
name|pkixParams
operator|.
name|addCertStore
argument_list|(
name|intermediateCertStore
argument_list|)
expr_stmt|;
comment|// Build and verify the certification chain
comment|// If this doesn't work although it should, it can be debugged
comment|// by starting java with -Djava.security.debug=certpath
comment|// see also
comment|// https://docs.oracle.com/javase/8/docs/technotes/guides/security/troubleshooting-security.html
name|CertPathBuilder
name|builder
init|=
name|CertPathBuilder
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
decl_stmt|;
return|return
operator|(
name|PKIXCertPathBuilderResult
operator|)
name|builder
operator|.
name|build
argument_list|(
name|pkixParams
argument_list|)
return|;
block|}
comment|/**      * Extract the OCSP URL from an X.509 certificate if available.      *      * @param cert X.509 certificate      * @return the URL of the OCSP validation service      * @throws IOException       */
specifier|private
specifier|static
name|String
name|extractOCSPURL
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|authorityExtensionValue
init|=
name|cert
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
comment|// copied from CertInformationHelper.getAuthorityInfoExtensionValue()
comment|// DRY refactor should be done some day
name|ASN1Sequence
name|asn1Seq
init|=
operator|(
name|ASN1Sequence
operator|)
name|JcaX509ExtensionUtils
operator|.
name|parseExtensionValue
argument_list|(
name|authorityExtensionValue
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|asn1Seq
operator|.
name|getObjects
argument_list|()
decl_stmt|;
while|while
condition|(
name|objects
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
comment|// AccessDescription
name|ASN1Sequence
name|obj
init|=
operator|(
name|ASN1Sequence
operator|)
name|objects
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|ASN1ObjectIdentifier
name|oid
init|=
operator|(
name|ASN1ObjectIdentifier
operator|)
name|obj
operator|.
name|getObjectAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// accessLocation
name|DERTaggedObject
name|location
init|=
operator|(
name|DERTaggedObject
operator|)
name|obj
operator|.
name|getObjectAt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|oid
operator|.
name|equals
argument_list|(
name|X509ObjectIdentifiers
operator|.
name|id_ad_ocsp
argument_list|)
operator|&&
name|location
operator|.
name|getTagNo
argument_list|()
operator|==
name|GeneralName
operator|.
name|uniformResourceIdentifier
condition|)
block|{
name|DEROctetString
name|url
init|=
operator|(
name|DEROctetString
operator|)
name|location
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|String
name|ocspURL
init|=
operator|new
name|String
argument_list|(
name|url
operator|.
name|getOctets
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"OCSP URL: "
operator|+
name|ocspURL
argument_list|)
expr_stmt|;
return|return
name|ocspURL
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Verify whether the certificate has been revoked at signing date.      *      * @param ocspHelper the OCSP helper.      * @param signDate the signing date.      * @throws RevokedCertificateException      * @throws IOException      * @throws OCSPException      * @throws CertificateVerificationException      */
specifier|private
specifier|static
name|void
name|verifyOCSP
parameter_list|(
name|OcspHelper
name|ocspHelper
parameter_list|,
name|Date
name|signDate
parameter_list|)
throws|throws
name|RevokedCertificateException
throws|,
name|IOException
throws|,
name|OCSPException
throws|,
name|CertificateVerificationException
block|{
try|try
block|{
name|OCSPResp
name|basicResponse
init|=
name|ocspHelper
operator|.
name|getResponseOcsp
argument_list|()
decl_stmt|;
if|if
condition|(
name|basicResponse
operator|.
name|getStatus
argument_list|()
operator|!=
name|OCSPResp
operator|.
name|SUCCESSFUL
condition|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"OCSP check not successful, status: "
operator|+
name|basicResponse
operator|.
name|getStatus
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"OCSP check successful"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RevokedCertificateException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getRevocationTime
argument_list|()
operator|.
name|compareTo
argument_list|(
name|signDate
argument_list|)
operator|<=
literal|0
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"OCSP check successful: The certificate was revoked after signing on "
operator|+
name|ex
operator|.
name|getRevocationTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

