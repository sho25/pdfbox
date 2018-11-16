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
name|ByteArrayInputStream
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
name|PublicKey
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
name|CRLException
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
name|X509CRL
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
name|X509CRLEntry
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|DirContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|InitialDirContext
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
name|DERIA5String
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
name|x509
operator|.
name|CRLDistPoint
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
name|DistributionPoint
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
name|DistributionPointName
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
name|GeneralNames
import|;
end_import

begin_comment
comment|/**  * Copied from Apache CXF 2.4.9, initial version:  * https://svn.apache.org/repos/asf/cxf/tags/cxf-2.4.9/distribution/src/main/release/samples/sts_issue_operation/src/main/java/demo/sts/provider/cert/  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CRLVerifier
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
name|CRLVerifier
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CRLVerifier
parameter_list|()
block|{     }
comment|/**      * Extracts the CRL distribution points from the certificate (if available)      * and checks the certificate revocation status against the CRLs coming from      * the distribution points. Supports HTTP, HTTPS, FTP and LDAP based URLs.      *      * @param cert the certificate to be checked for revocation      * @param signDate the date when the signing took place      * @param additionalCerts set of trusted root CA certificates that will be      * used as "trust anchors" and intermediate CA certificates that will be      * used as part of the certification chain.      * @throws CertificateVerificationException if the certificate is revoked      */
specifier|public
specifier|static
name|void
name|verifyCertificateCRLs
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|Date
name|signDate
parameter_list|,
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|additionalCerts
parameter_list|)
throws|throws
name|CertificateVerificationException
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|crlDistributionPointsURLs
init|=
name|getCrlDistributionPoints
argument_list|(
name|cert
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|crlDistributionPointsURL
range|:
name|crlDistributionPointsURLs
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Checking distribution point URL: "
operator|+
name|crlDistributionPointsURL
argument_list|)
expr_stmt|;
comment|//TODO catch connection errors and try the next one
name|X509CRL
name|crl
init|=
name|downloadCRL
argument_list|(
name|crlDistributionPointsURL
argument_list|)
decl_stmt|;
comment|// Verify CRL, see wikipedia:
comment|// "To validate a specific CRL prior to relying on it,
comment|//  the certificate of its corresponding CA is needed"
name|PublicKey
name|issuerKey
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
name|crl
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
name|issuerKey
operator|=
name|additionalCert
operator|.
name|getPublicKey
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|issuerKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"Certificate for "
operator|+
name|crl
operator|.
name|getIssuerX500Principal
argument_list|()
operator|+
literal|"not found in certificate chain, so the CRL at "
operator|+
name|crlDistributionPointsURL
operator|+
literal|" could not be verified"
argument_list|)
throw|;
block|}
name|crl
operator|.
name|verify
argument_list|(
name|issuerKey
argument_list|)
expr_stmt|;
name|checkRevocation
argument_list|(
name|crl
argument_list|,
name|cert
argument_list|,
name|signDate
argument_list|,
name|crlDistributionPointsURL
argument_list|)
expr_stmt|;
comment|// https://tools.ietf.org/html/rfc5280#section-4.2.1.13
comment|// If the DistributionPointName contains multiple values,
comment|// each name describes a different mechanism to obtain the same
comment|// CRL.  For example, the same CRL could be available for
comment|// retrieval through both LDAP and HTTP.
comment|//
comment|// => thus no need to check several protocols
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|CertificateVerificationException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
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
literal|"Cannot verify CRL for certificate: "
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
comment|/**      * Check whether the certificate was revoked at signing time.      *      * @param crl certificate revocation list      * @param cert certificate to be checked      * @param signDate date the certificate was used for signing      * @param crlDistributionPointsURL URL for log message or exception text      * @throws CertificateVerificationException if the certificate was revoked at signing time      */
specifier|public
specifier|static
name|void
name|checkRevocation
parameter_list|(
name|X509CRL
name|crl
parameter_list|,
name|X509Certificate
name|cert
parameter_list|,
name|Date
name|signDate
parameter_list|,
name|String
name|crlDistributionPointsURL
parameter_list|)
throws|throws
name|CertificateVerificationException
block|{
comment|//TODO this should throw a RevokedCertificateException
name|X509CRLEntry
name|revokedCRLEntry
init|=
name|crl
operator|.
name|getRevokedCertificate
argument_list|(
name|cert
argument_list|)
decl_stmt|;
if|if
condition|(
name|revokedCRLEntry
operator|!=
literal|null
operator|&&
name|revokedCRLEntry
operator|.
name|getRevocationDate
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
operator|new
name|CertificateVerificationException
argument_list|(
literal|"The certificate was revoked by CRL "
operator|+
name|crlDistributionPointsURL
operator|+
literal|" on "
operator|+
name|revokedCRLEntry
operator|.
name|getRevocationDate
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|revokedCRLEntry
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The certificate was revoked after signing by CRL "
operator|+
name|crlDistributionPointsURL
operator|+
literal|" on "
operator|+
name|revokedCRLEntry
operator|.
name|getRevocationDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The certificate was not revoked by CRL "
operator|+
name|crlDistributionPointsURL
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Downloads CRL from given URL. Supports http, https, ftp and ldap based URLs.      */
specifier|private
specifier|static
name|X509CRL
name|downloadCRL
parameter_list|(
name|String
name|crlURL
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
throws|,
name|CRLException
throws|,
name|CertificateVerificationException
throws|,
name|NamingException
block|{
if|if
condition|(
name|crlURL
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|crlURL
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
operator|||
name|crlURL
operator|.
name|startsWith
argument_list|(
literal|"ftp://"
argument_list|)
condition|)
block|{
return|return
name|downloadCRLFromWeb
argument_list|(
name|crlURL
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|crlURL
operator|.
name|startsWith
argument_list|(
literal|"ldap://"
argument_list|)
condition|)
block|{
return|return
name|downloadCRLFromLDAP
argument_list|(
name|crlURL
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"Can not download CRL from certificate "
operator|+
literal|"distribution point: "
operator|+
name|crlURL
argument_list|)
throw|;
block|}
block|}
comment|/**      * Downloads a CRL from given LDAP url, e.g.      * ldap://ldap.infonotary.com/dc=identity-ca,dc=infonotary,dc=com      */
specifier|private
specifier|static
name|X509CRL
name|downloadCRLFromLDAP
parameter_list|(
name|String
name|ldapURL
parameter_list|)
throws|throws
name|CertificateException
throws|,
name|NamingException
throws|,
name|CRLException
throws|,
name|CertificateVerificationException
block|{
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|env
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
literal|"com.sun.jndi.ldap.LdapCtxFactory"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|PROVIDER_URL
argument_list|,
name|ldapURL
argument_list|)
expr_stmt|;
name|DirContext
name|ctx
init|=
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|Attributes
name|avals
init|=
name|ctx
operator|.
name|getAttributes
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|Attribute
name|aval
init|=
name|avals
operator|.
name|get
argument_list|(
literal|"certificateRevocationList;binary"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|val
init|=
operator|(
name|byte
index|[]
operator|)
name|aval
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
operator|||
name|val
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
literal|"Can not download CRL from: "
operator|+
name|ldapURL
argument_list|)
throw|;
block|}
else|else
block|{
name|InputStream
name|inStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|val
argument_list|)
decl_stmt|;
name|CertificateFactory
name|cf
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
return|return
operator|(
name|X509CRL
operator|)
name|cf
operator|.
name|generateCRL
argument_list|(
name|inStream
argument_list|)
return|;
block|}
block|}
comment|/**      * Downloads a CRL from given HTTP/HTTPS/FTP URL, e.g.      * http://crl.infonotary.com/crl/identity-ca.crl      */
specifier|public
specifier|static
name|X509CRL
name|downloadCRLFromWeb
parameter_list|(
name|String
name|crlURL
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
throws|,
name|CRLException
block|{
try|try
init|(
name|InputStream
name|crlStream
init|=
operator|new
name|URL
argument_list|(
name|crlURL
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
return|return
operator|(
name|X509CRL
operator|)
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
operator|.
name|generateCRL
argument_list|(
name|crlStream
argument_list|)
return|;
block|}
block|}
comment|/**      * Extracts all CRL distribution point URLs from the "CRL Distribution      * Point" extension in a X.509 certificate. If CRL distribution point      * extension is unavailable, returns an empty list.      * @param cert      * @return List of CRL distribution point URLs.      * @throws java.io.IOException      */
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getCrlDistributionPoints
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|crldpExt
init|=
name|cert
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
name|crldpExt
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
name|ASN1InputStream
name|oAsnInStream
init|=
operator|new
name|ASN1InputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|crldpExt
argument_list|)
argument_list|)
decl_stmt|;
name|ASN1Primitive
name|derObjCrlDP
init|=
name|oAsnInStream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|DEROctetString
name|dosCrlDP
init|=
operator|(
name|DEROctetString
operator|)
name|derObjCrlDP
decl_stmt|;
name|byte
index|[]
name|crldpExtOctets
init|=
name|dosCrlDP
operator|.
name|getOctets
argument_list|()
decl_stmt|;
name|ASN1InputStream
name|oAsnInStream2
init|=
operator|new
name|ASN1InputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|crldpExtOctets
argument_list|)
argument_list|)
decl_stmt|;
name|ASN1Primitive
name|derObj2
init|=
name|oAsnInStream2
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|CRLDistPoint
name|distPoint
init|=
name|CRLDistPoint
operator|.
name|getInstance
argument_list|(
name|derObj2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|crlUrls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DistributionPoint
name|dp
range|:
name|distPoint
operator|.
name|getDistributionPoints
argument_list|()
control|)
block|{
name|DistributionPointName
name|dpn
init|=
name|dp
operator|.
name|getDistributionPoint
argument_list|()
decl_stmt|;
comment|// Look for URIs in fullName
if|if
condition|(
name|dpn
operator|!=
literal|null
operator|&&
name|dpn
operator|.
name|getType
argument_list|()
operator|==
name|DistributionPointName
operator|.
name|FULL_NAME
condition|)
block|{
comment|// Look for an URI
for|for
control|(
name|GeneralName
name|genName
range|:
name|GeneralNames
operator|.
name|getInstance
argument_list|(
name|dpn
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getNames
argument_list|()
control|)
block|{
if|if
condition|(
name|genName
operator|.
name|getTagNo
argument_list|()
operator|==
name|GeneralName
operator|.
name|uniformResourceIdentifier
condition|)
block|{
name|String
name|url
init|=
name|DERIA5String
operator|.
name|getInstance
argument_list|(
name|genName
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getString
argument_list|()
decl_stmt|;
name|crlUrls
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|crlUrls
return|;
block|}
block|}
end_class

end_unit

