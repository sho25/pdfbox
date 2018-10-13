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
name|CertificateVerifier
parameter_list|()
block|{      }
comment|/**      * Attempts to build a certification chain for given certificate and to      * verify it. Relies on a set of root CA certificates and intermediate      * certificates that will be used for building the certification chain. The      * verification process assumes that all self-signed certificates in the set      * are trusted root CA certificates and all other certificates in the set      * are intermediate certificates.      *      * @param cert - certificate for validation      * @param additionalCerts - set of trusted root CA certificates that will be      * used as "trust anchors" and intermediate CA certificates that will be      * used as part of the certification chain. All self-signed certificates are      * considered to be trusted root CA certificates. All the rest are      * considered to be intermediate CA certificates.      * @return the certification chain (if verification is successful)      * @throws CertificateVerificationException - if the certification is not      * successful (e.g. certification path cannot be built or some certificate      * in the chain is expired or CRL checks are failed)      */
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
argument_list|<
name|X509Certificate
argument_list|>
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
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
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
name|verifySelfSignedCert
argument_list|)
decl_stmt|;
comment|// Check whether the certificate is revoked by the CRL
comment|// given in its CRL distribution point extension
name|CRLVerifier
operator|.
name|verifyCertificateCRLs
argument_list|(
name|cert
argument_list|)
expr_stmt|;
comment|// The chain is built and verified. Return it as a result
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
comment|/**      * Checks whether given X.509 certificate is self-signed.      */
specifier|public
specifier|static
name|boolean
name|isSelfSigned
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|CertificateException
throws|,
name|NoSuchAlgorithmException
throws|,
name|NoSuchProviderException
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
name|sigEx
parameter_list|)
block|{
comment|// Invalid signature --> not self-signed
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|keyEx
parameter_list|)
block|{
comment|// Invalid key --> not self-signed
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Attempts to build a certification chain for given certificate and to      * verify it. Relies on a set of root CA certificates (trust anchors) and a      * set of intermediate certificates (to be used as part of the chain).      *      * @param cert - certificate for validation      * @param trustedRootCerts - set of trusted root CA certificates      * @param intermediateCerts - set of intermediate certificates      * @return the certification chain (if verification is successful)      * @throws GeneralSecurityException - if the verification is not successful      * (e.g. certification path cannot be built or some certificate in the chain      * is expired)      */
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
name|boolean
name|verifySelfSignedCert
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
argument_list|<
name|TrustAnchor
argument_list|>
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
name|PKIXCertPathBuilderResult
name|result
init|=
operator|(
name|PKIXCertPathBuilderResult
operator|)
name|builder
operator|.
name|build
argument_list|(
name|pkixParams
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

