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
name|cert
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|CertificateParsingException
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
name|Calendar
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
name|Random
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
name|SigUtils
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
name|encryption
operator|.
name|SecurityProvider
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
name|DLSequence
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
name|ocsp
operator|.
name|OCSPObjectIdentifiers
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
name|ocsp
operator|.
name|OCSPResponseStatus
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
name|ocsp
operator|.
name|ResponderID
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
name|oiw
operator|.
name|OIWObjectIdentifiers
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
name|x500
operator|.
name|X500Name
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
name|Extensions
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
name|SubjectPublicKeyInfo
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
name|cert
operator|.
name|jcajce
operator|.
name|JcaX509CertificateHolder
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
name|BasicOCSPResp
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
name|CertificateID
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
name|CertificateStatus
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
name|OCSPReq
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
name|OCSPReqBuilder
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
name|RevokedStatus
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
name|SingleResp
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
name|ContentVerifierProvider
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
name|DigestCalculator
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
name|JcaContentVerifierProviderBuilder
import|;
end_import

begin_comment
comment|/**  * Helper Class for OCSP-Operations with bouncy castle.  *   * @author Alexis Suter  */
end_comment

begin_class
specifier|public
class|class
name|OcspHelper
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
name|OcspHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|X509Certificate
name|issuerCertificate
decl_stmt|;
specifier|private
specifier|final
name|Date
name|signDate
decl_stmt|;
specifier|private
specifier|final
name|X509Certificate
name|certificateToCheck
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|additionalCerts
decl_stmt|;
specifier|private
specifier|final
name|String
name|ocspUrl
decl_stmt|;
specifier|private
name|DEROctetString
name|encodedNonce
decl_stmt|;
specifier|private
name|X509Certificate
name|ocspResponderCertificate
decl_stmt|;
specifier|private
specifier|final
name|JcaX509CertificateConverter
name|certificateConverter
init|=
operator|new
name|JcaX509CertificateConverter
argument_list|()
decl_stmt|;
comment|/**      * @param checkCertificate Certificate to be OCSP-checked      * @param signDate the date when the signing took place      * @param issuerCertificate Certificate of the issuer      * @param additionalCerts Set of trusted root CA certificates that will be used as "trust      * anchors" and intermediate CA certificates that will be used as part of the certification      * chain. All self-signed certificates are considered to be trusted root CA certificates. All      * the rest are considered to be intermediate CA certificates.      * @param ocspUrl where to fetch for OCSP      */
specifier|public
name|OcspHelper
parameter_list|(
name|X509Certificate
name|checkCertificate
parameter_list|,
name|Date
name|signDate
parameter_list|,
name|X509Certificate
name|issuerCertificate
parameter_list|,
name|Set
argument_list|<
name|X509Certificate
argument_list|>
name|additionalCerts
parameter_list|,
name|String
name|ocspUrl
parameter_list|)
block|{
name|this
operator|.
name|certificateToCheck
operator|=
name|checkCertificate
expr_stmt|;
name|this
operator|.
name|signDate
operator|=
name|signDate
expr_stmt|;
name|this
operator|.
name|issuerCertificate
operator|=
name|issuerCertificate
expr_stmt|;
name|this
operator|.
name|additionalCerts
operator|=
name|additionalCerts
expr_stmt|;
name|this
operator|.
name|ocspUrl
operator|=
name|ocspUrl
expr_stmt|;
block|}
comment|/**      * Performs and verifies the OCSP-Request      *      * @return the OCSPResp, when the request was successful, else a corresponding exception will be      * thrown. Never returns null.      *      * @throws IOException      * @throws OCSPException      * @throws RevokedCertificateException      */
specifier|public
name|OCSPResp
name|getResponseOcsp
parameter_list|()
throws|throws
name|IOException
throws|,
name|OCSPException
throws|,
name|RevokedCertificateException
block|{
name|OCSPResp
name|ocspResponse
init|=
name|performRequest
argument_list|()
decl_stmt|;
name|verifyOcspResponse
argument_list|(
name|ocspResponse
argument_list|)
expr_stmt|;
return|return
name|ocspResponse
return|;
block|}
comment|/**      * Get responder certificate. This is available after {@link #getResponseOcsp()} has been called.      *      * @return The certificate of the responder.      */
specifier|public
name|X509Certificate
name|getOcspResponderCertificate
parameter_list|()
block|{
return|return
name|ocspResponderCertificate
return|;
block|}
comment|/**      * Verifies the status and the response itself (including nonce), but not the signature.      *       * @param ocspResponse to be verified      * @throws OCSPException      * @throws RevokedCertificateException      * @throws IOException if the default security provider can't be instantiated      */
specifier|private
name|void
name|verifyOcspResponse
parameter_list|(
name|OCSPResp
name|ocspResponse
parameter_list|)
throws|throws
name|OCSPException
throws|,
name|RevokedCertificateException
throws|,
name|IOException
block|{
name|verifyRespStatus
argument_list|(
name|ocspResponse
argument_list|)
expr_stmt|;
name|BasicOCSPResp
name|basicResponse
init|=
operator|(
name|BasicOCSPResp
operator|)
name|ocspResponse
operator|.
name|getResponseObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|basicResponse
operator|!=
literal|null
condition|)
block|{
name|ResponderID
name|responderID
init|=
name|basicResponse
operator|.
name|getResponderId
argument_list|()
operator|.
name|toASN1Primitive
argument_list|()
decl_stmt|;
comment|// https://tools.ietf.org/html/rfc6960#section-4.2.2.3
comment|// The basic response type contains:
comment|// (...)
comment|// either the name of the responder or a hash of the responder's
comment|// public key as the ResponderID
comment|// (...)
comment|// The responder MAY include certificates in the certs field of
comment|// BasicOCSPResponse that help the OCSP client verify the responder's
comment|// signature.
name|X500Name
name|name
init|=
name|responderID
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|findResponderCertificateByName
argument_list|(
name|basicResponse
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|keyHash
init|=
name|responderID
operator|.
name|getKeyHash
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyHash
operator|!=
literal|null
condition|)
block|{
name|findResponderCertificateByKeyHash
argument_list|(
name|basicResponse
argument_list|,
name|keyHash
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ocspResponderCertificate
operator|==
literal|null
condition|)
block|{
comment|// DO NOT use the certificate found in additionalCerts first. One file had a
comment|// responder certificate in the PDF itself with SHA1withRSA algorithm, but
comment|// the responder delivered a different (newer, more secure) certificate
comment|// with SHA256withRSA (tried with QV_RCA1_RCA3_CPCPS_V4_11.pdf)
comment|// https://www.quovadisglobal.com/~/media/Files/Repository/QV_RCA1_RCA3_CPCPS_V4_11.ashx
for|for
control|(
name|X509Certificate
name|cert
range|:
name|additionalCerts
control|)
block|{
name|X500Name
name|certSubjectName
init|=
operator|new
name|X500Name
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|certSubjectName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ocspResponderCertificate
operator|=
name|cert
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|ocspResponderCertificate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: certificate for responder "
operator|+
name|name
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
try|try
block|{
name|SigUtils
operator|.
name|checkResponderCertificateUsage
argument_list|(
name|ocspResponderCertificate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateParsingException
name|ex
parameter_list|)
block|{
comment|// unlikely to happen because the certificate existed as an object
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|checkOcspSignature
argument_list|(
name|ocspResponderCertificate
argument_list|,
name|basicResponse
argument_list|)
expr_stmt|;
name|boolean
name|nonceChecked
init|=
name|checkNonce
argument_list|(
name|basicResponse
argument_list|)
decl_stmt|;
name|SingleResp
index|[]
name|responses
init|=
name|basicResponse
operator|.
name|getResponses
argument_list|()
decl_stmt|;
if|if
condition|(
name|responses
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: Received "
operator|+
name|responses
operator|.
name|length
operator|+
literal|" responses instead of 1!"
argument_list|)
throw|;
block|}
name|SingleResp
name|resp
init|=
name|responses
index|[
literal|0
index|]
decl_stmt|;
name|Object
name|status
init|=
name|resp
operator|.
name|getCertStatus
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|nonceChecked
condition|)
block|{
comment|// https://tools.ietf.org/html/rfc5019
comment|// fall back to validating the OCSPResponse based on time
name|checkOcspResponseFresh
argument_list|(
name|resp
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|status
operator|instanceof
name|RevokedStatus
condition|)
block|{
name|RevokedStatus
name|revokedStatus
init|=
operator|(
name|RevokedStatus
operator|)
name|status
decl_stmt|;
if|if
condition|(
name|revokedStatus
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
operator|new
name|RevokedCertificateException
argument_list|(
literal|"OCSP: Certificate is revoked since "
operator|+
name|revokedStatus
operator|.
name|getRevocationTime
argument_list|()
argument_list|,
name|revokedStatus
operator|.
name|getRevocationTime
argument_list|()
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"The certificate was revoked after signing by OCSP "
operator|+
name|ocspUrl
operator|+
literal|" on "
operator|+
name|revokedStatus
operator|.
name|getRevocationTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|status
operator|!=
name|CertificateStatus
operator|.
name|GOOD
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: Status of Cert is unknown"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|findResponderCertificateByKeyHash
parameter_list|(
name|BasicOCSPResp
name|basicResponse
parameter_list|,
name|byte
index|[]
name|keyHash
parameter_list|)
throws|throws
name|IOException
block|{
comment|// https://tools.ietf.org/html/rfc2560#section-4.2.1
comment|// KeyHash ::= OCTET STRING -- SHA-1 hash of responder's public key
comment|//         -- (i.e., the SHA-1 hash of the value of the
comment|//         -- BIT STRING subjectPublicKey [excluding
comment|//         -- the tag, length, and number of unused
comment|//         -- bits] in the responder's certificate)
comment|// code below inspired by org.bouncycastle.cert.ocsp.CertificateID.createCertID()
comment|// tested with SO52757037-Signed3-OCSP-with-KeyHash.pdf
name|X509CertificateHolder
index|[]
name|certHolders
init|=
name|basicResponse
operator|.
name|getCerts
argument_list|()
decl_stmt|;
for|for
control|(
name|X509CertificateHolder
name|certHolder
range|:
name|certHolders
control|)
block|{
name|SHA1DigestCalculator
name|digCalc
init|=
operator|new
name|SHA1DigestCalculator
argument_list|()
decl_stmt|;
name|SubjectPublicKeyInfo
name|info
init|=
name|certHolder
operator|.
name|getSubjectPublicKeyInfo
argument_list|()
decl_stmt|;
try|try
init|(
name|OutputStream
name|dgOut
init|=
name|digCalc
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|dgOut
operator|.
name|write
argument_list|(
name|info
operator|.
name|getPublicKeyData
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|digest
init|=
name|digCalc
operator|.
name|getDigest
argument_list|()
decl_stmt|;
if|if
condition|(
name|Arrays
operator|.
name|equals
argument_list|(
name|keyHash
argument_list|,
name|digest
argument_list|)
condition|)
block|{
try|try
block|{
name|ocspResponderCertificate
operator|=
name|certificateConverter
operator|.
name|getCertificate
argument_list|(
name|certHolder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|ex
parameter_list|)
block|{
comment|// unlikely to happen because the certificate existed as an object
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
specifier|private
name|void
name|findResponderCertificateByName
parameter_list|(
name|BasicOCSPResp
name|basicResponse
parameter_list|,
name|X500Name
name|name
parameter_list|)
block|{
name|X509CertificateHolder
index|[]
name|certHolders
init|=
name|basicResponse
operator|.
name|getCerts
argument_list|()
decl_stmt|;
for|for
control|(
name|X509CertificateHolder
name|certHolder
range|:
name|certHolders
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|certHolder
operator|.
name|getSubject
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|ocspResponderCertificate
operator|=
name|certificateConverter
operator|.
name|getCertificate
argument_list|(
name|certHolder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateException
name|ex
parameter_list|)
block|{
comment|// unlikely to happen because the certificate existed as an object
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
specifier|private
name|void
name|checkOcspResponseFresh
parameter_list|(
name|SingleResp
name|resp
parameter_list|)
throws|throws
name|OCSPException
block|{
comment|// https://tools.ietf.org/html/rfc5019
comment|// Clients MUST check for the existence of the nextUpdate field and MUST
comment|// ensure the current time, expressed in GMT time as described in
comment|// Section 2.2.4, falls between the thisUpdate and nextUpdate times.  If
comment|// the nextUpdate field is absent, the client MUST reject the response.
name|Date
name|curDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|Date
name|thisUpdate
init|=
name|resp
operator|.
name|getThisUpdate
argument_list|()
decl_stmt|;
if|if
condition|(
name|thisUpdate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: thisUpdate field is missing in response (RFC 5019 2.2.4.)"
argument_list|)
throw|;
block|}
name|Date
name|nextUpdate
init|=
name|resp
operator|.
name|getNextUpdate
argument_list|()
decl_stmt|;
if|if
condition|(
name|nextUpdate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: nextUpdate field is missing in response (RFC 5019 2.2.4.)"
argument_list|)
throw|;
block|}
if|if
condition|(
name|curDate
operator|.
name|compareTo
argument_list|(
name|thisUpdate
argument_list|)
operator|<
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|curDate
operator|+
literal|"< "
operator|+
name|thisUpdate
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: current date< thisUpdate field (RFC 5019 2.2.4.)"
argument_list|)
throw|;
block|}
if|if
condition|(
name|curDate
operator|.
name|compareTo
argument_list|(
name|nextUpdate
argument_list|)
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|curDate
operator|+
literal|"> "
operator|+
name|nextUpdate
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP: current date> nextUpdate field (RFC 5019 2.2.4.)"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"OCSP response is fresh"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks whether the OCSP response is signed by the given certificate.      *       * @param certificate the certificate to check the signature      * @param basicResponse OCSP response containing the signature      * @throws OCSPException when the signature is invalid or could not be checked      * @throws IOException if the default security provider can't be instantiated      */
specifier|private
name|void
name|checkOcspSignature
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|BasicOCSPResp
name|basicResponse
parameter_list|)
throws|throws
name|OCSPException
throws|,
name|IOException
block|{
try|try
block|{
name|ContentVerifierProvider
name|verifier
init|=
operator|new
name|JcaContentVerifierProviderBuilder
argument_list|()
operator|.
name|setProvider
argument_list|(
name|SecurityProvider
operator|.
name|getProvider
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|certificate
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|basicResponse
operator|.
name|isSignatureValid
argument_list|(
name|verifier
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP-Signature is not valid!"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|OperatorCreationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"Error checking Ocsp-Signature"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Checks if the nonce in the response matches.      *       * @param basicResponse Response to be checked      * @return true if the nonce is present and matches, false if nonce is missing.      * @throws OCSPException if the nonce is different      */
specifier|private
name|boolean
name|checkNonce
parameter_list|(
name|BasicOCSPResp
name|basicResponse
parameter_list|)
throws|throws
name|OCSPException
block|{
name|Extension
name|nonceExt
init|=
name|basicResponse
operator|.
name|getExtension
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_nonce
argument_list|)
decl_stmt|;
if|if
condition|(
name|nonceExt
operator|!=
literal|null
condition|)
block|{
name|DEROctetString
name|responseNonceString
init|=
operator|(
name|DEROctetString
operator|)
name|nonceExt
operator|.
name|getExtnValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|responseNonceString
operator|.
name|equals
argument_list|(
name|encodedNonce
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"Different nonce found in response!"
argument_list|)
throw|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Nonce is good"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// https://tools.ietf.org/html/rfc5019
comment|// Clients that opt to include a nonce in the
comment|// request SHOULD NOT reject a corresponding OCSPResponse solely on the
comment|// basis of the nonexistent expected nonce, but MUST fall back to
comment|// validating the OCSPResponse based on time.
return|return
literal|false
return|;
block|}
comment|/**      * Performs the OCSP-Request, with given data.      *       * @return the OCSPResp, that has been fetched from the ocspUrl      * @throws IOException      * @throws OCSPException      */
specifier|private
name|OCSPResp
name|performRequest
parameter_list|()
throws|throws
name|IOException
throws|,
name|OCSPException
block|{
name|OCSPReq
name|request
init|=
name|generateOCSPRequest
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|ocspUrl
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|httpConnection
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|httpConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/ocsp-request"
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/ocsp-response"
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|out
init|=
name|httpConnection
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|out
operator|.
name|write
argument_list|(
name|request
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpConnection
operator|.
name|getResponseCode
argument_list|()
operator|!=
literal|200
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"OCSP: Could not access url, ResponseCode: "
operator|+
name|httpConnection
operator|.
name|getResponseCode
argument_list|()
argument_list|)
throw|;
block|}
comment|// Get response
try|try
init|(
name|InputStream
name|in
init|=
operator|(
name|InputStream
operator|)
name|httpConnection
operator|.
name|getContent
argument_list|()
init|)
block|{
return|return
operator|new
name|OCSPResp
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
name|httpConnection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Helper method to verify response status.      *       * @param resp OCSP response      * @throws OCSPException if the response status is not ok      */
specifier|public
name|void
name|verifyRespStatus
parameter_list|(
name|OCSPResp
name|resp
parameter_list|)
throws|throws
name|OCSPException
block|{
name|String
name|statusInfo
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|resp
operator|!=
literal|null
condition|)
block|{
name|int
name|status
init|=
name|resp
operator|.
name|getStatus
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|status
condition|)
block|{
case|case
name|OCSPResponseStatus
operator|.
name|INTERNAL_ERROR
case|:
name|statusInfo
operator|=
literal|"INTERNAL_ERROR"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"An internal error occurred in the OCSP Server!"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCSPResponseStatus
operator|.
name|MALFORMED_REQUEST
case|:
comment|// This happened when the "critical" flag was used for extensions
comment|// on a responder known by the committer of this comment.
name|statusInfo
operator|=
literal|"MALFORMED_REQUEST"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Your request did not fit the RFC 2560 syntax!"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCSPResponseStatus
operator|.
name|SIG_REQUIRED
case|:
name|statusInfo
operator|=
literal|"SIG_REQUIRED"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Your request was not signed!"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCSPResponseStatus
operator|.
name|TRY_LATER
case|:
name|statusInfo
operator|=
literal|"TRY_LATER"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"The server was too busy to answer you!"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCSPResponseStatus
operator|.
name|UNAUTHORIZED
case|:
name|statusInfo
operator|=
literal|"UNAUTHORIZED"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"The server could not authenticate you!"
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCSPResponseStatus
operator|.
name|SUCCESSFUL
case|:
break|break;
default|default:
name|statusInfo
operator|=
literal|"UNKNOWN"
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Unknown OCSPResponse status code! "
operator|+
name|status
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|resp
operator|==
literal|null
operator|||
name|resp
operator|.
name|getStatus
argument_list|()
operator|!=
name|OCSPResponseStatus
operator|.
name|SUCCESSFUL
condition|)
block|{
throw|throw
operator|new
name|OCSPException
argument_list|(
literal|"OCSP response unsuccessful, status: "
operator|+
name|statusInfo
argument_list|)
throw|;
block|}
block|}
comment|/**      * Generates an OCSP request and generates the<code>CertificateID</code>.      *      * @return OCSP request, ready to fetch data      * @throws OCSPException      * @throws IOException      */
specifier|private
name|OCSPReq
name|generateOCSPRequest
parameter_list|()
throws|throws
name|OCSPException
throws|,
name|IOException
block|{
name|Security
operator|.
name|addProvider
argument_list|(
name|SecurityProvider
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
comment|// Generate the ID for the certificate we are looking for
name|CertificateID
name|certId
decl_stmt|;
try|try
block|{
name|certId
operator|=
operator|new
name|CertificateID
argument_list|(
operator|new
name|SHA1DigestCalculator
argument_list|()
argument_list|,
operator|new
name|JcaX509CertificateHolder
argument_list|(
name|issuerCertificate
argument_list|)
argument_list|,
name|certificateToCheck
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Error creating CertificateID with the Certificate encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// https://tools.ietf.org/html/rfc2560#section-4.1.2
comment|// Support for any specific extension is OPTIONAL. The critical flag
comment|// SHOULD NOT be set for any of them.
name|Extension
name|responseExtension
init|=
operator|new
name|Extension
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_response
argument_list|,
literal|false
argument_list|,
operator|new
name|DLSequence
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_basic
argument_list|)
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|Random
name|rand
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|byte
index|[]
name|nonce
init|=
operator|new
name|byte
index|[
literal|16
index|]
decl_stmt|;
name|rand
operator|.
name|nextBytes
argument_list|(
name|nonce
argument_list|)
expr_stmt|;
name|encodedNonce
operator|=
operator|new
name|DEROctetString
argument_list|(
operator|new
name|DEROctetString
argument_list|(
name|nonce
argument_list|)
argument_list|)
expr_stmt|;
name|Extension
name|nonceExtension
init|=
operator|new
name|Extension
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_nonce
argument_list|,
literal|false
argument_list|,
name|encodedNonce
argument_list|)
decl_stmt|;
name|OCSPReqBuilder
name|builder
init|=
operator|new
name|OCSPReqBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRequestExtensions
argument_list|(
operator|new
name|Extensions
argument_list|(
operator|new
name|Extension
index|[]
block|{
name|responseExtension
block|,
name|nonceExtension
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addRequest
argument_list|(
name|certId
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Class to create SHA-1 Digest, used for creation of CertificateID.      */
specifier|private
specifier|static
class|class
name|SHA1DigestCalculator
implements|implements
name|DigestCalculator
block|{
specifier|private
specifier|final
name|ByteArrayOutputStream
name|bOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|AlgorithmIdentifier
name|getAlgorithmIdentifier
parameter_list|()
block|{
return|return
operator|new
name|AlgorithmIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|idSHA1
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
block|{
return|return
name|bOut
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getDigest
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|bOut
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|bOut
operator|.
name|reset
argument_list|()
expr_stmt|;
try|try
block|{
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
return|return
name|md
operator|.
name|digest
argument_list|(
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"SHA-1 Algorithm not found"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

