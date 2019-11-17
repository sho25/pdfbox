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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
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
name|GeneralSecurityException
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
name|X509Certificate
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
name|cos
operator|.
name|COSStream
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
name|COSUpdateInfo
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
name|examples
operator|.
name|signature
operator|.
name|cert
operator|.
name|CRLVerifier
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
name|CertificateVerificationException
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
name|cert
operator|.
name|RevokedCertificateException
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
name|CertInformationCollector
operator|.
name|CertSignatureInformation
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
name|PDDocumentCatalog
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
name|util
operator|.
name|Hex
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
comment|/**  * An example for adding Validation Information to a signed PDF, inspired by ETSI TS 102 778-4  * V1.1.2 (2009-12), Part 4: PAdES Long Term - PAdES-LTV Profile. This procedure appends the  * Validation Information of the last signature (more precise its signer(s)) to a copy of the  * document. The signature and the signed data will not be touched and stay valid.  *  * @author Alexis Suter  */
end_comment

begin_class
specifier|public
class|class
name|AddValidationInformation
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
name|AddValidationInformation
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CertInformationCollector
name|certInformationHelper
decl_stmt|;
specifier|private
name|COSArray
name|correspondingOCSPs
decl_stmt|;
specifier|private
name|COSArray
name|correspondingCRLs
decl_stmt|;
specifier|private
name|COSDictionary
name|vriBase
decl_stmt|;
specifier|private
name|COSArray
name|ocsps
decl_stmt|;
specifier|private
name|COSArray
name|crls
decl_stmt|;
specifier|private
name|COSArray
name|certs
decl_stmt|;
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|BigInteger
argument_list|>
name|foundRevocationInformation
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Calendar
name|signDate
decl_stmt|;
comment|/**      * Signs the given PDF file.      *       * @param inFile input PDF file      * @param outFile output PDF file      * @throws IOException if the input file could not be read      */
specifier|public
name|void
name|validateSignature
parameter_list|(
name|File
name|inFile
parameter_list|,
name|File
name|outFile
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|inFile
operator|==
literal|null
operator|||
operator|!
name|inFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Document for signing does not exist"
argument_list|)
throw|;
block|}
try|try
init|(
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inFile
argument_list|)
init|;
name|FileOutputStream
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
init|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
name|doValidation
argument_list|(
name|inFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Fetches certificate information from the last signature of the document and appends a DSS      * with the validation information to the document.      *      * @param document containing the Signature      * @param filename in file to extract signature      * @param output where to write the changed document      * @throws IOException      */
specifier|private
name|void
name|doValidation
parameter_list|(
name|String
name|filename
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|certInformationHelper
operator|=
operator|new
name|CertInformationCollector
argument_list|()
expr_stmt|;
name|CertSignatureInformation
name|certInfo
init|=
literal|null
decl_stmt|;
try|try
block|{
name|PDSignature
name|signature
init|=
name|SigUtils
operator|.
name|getLastRelevantSignature
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|signature
operator|!=
literal|null
condition|)
block|{
name|certInfo
operator|=
name|certInformationHelper
operator|.
name|getLastCertInfo
argument_list|(
name|signature
argument_list|,
name|filename
argument_list|)
expr_stmt|;
name|signDate
operator|=
name|signature
operator|.
name|getSignDate
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CertificateProccessingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"An Error occurred processing the Signature"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|certInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No Certificate information or signature found in the given document"
argument_list|)
throw|;
block|}
name|PDDocumentCatalog
name|docCatalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|COSDictionary
name|catalog
init|=
name|docCatalog
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|catalog
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|COSDictionary
name|dss
init|=
name|getOrCreateDictionaryEntry
argument_list|(
name|COSDictionary
operator|.
name|class
argument_list|,
name|catalog
argument_list|,
literal|"DSS"
argument_list|)
decl_stmt|;
name|addExtensions
argument_list|(
name|docCatalog
argument_list|)
expr_stmt|;
name|vriBase
operator|=
name|getOrCreateDictionaryEntry
argument_list|(
name|COSDictionary
operator|.
name|class
argument_list|,
name|dss
argument_list|,
literal|"VRI"
argument_list|)
expr_stmt|;
name|ocsps
operator|=
name|getOrCreateDictionaryEntry
argument_list|(
name|COSArray
operator|.
name|class
argument_list|,
name|dss
argument_list|,
literal|"OCSPs"
argument_list|)
expr_stmt|;
name|crls
operator|=
name|getOrCreateDictionaryEntry
argument_list|(
name|COSArray
operator|.
name|class
argument_list|,
name|dss
argument_list|,
literal|"CRLs"
argument_list|)
expr_stmt|;
name|certs
operator|=
name|getOrCreateDictionaryEntry
argument_list|(
name|COSArray
operator|.
name|class
argument_list|,
name|dss
argument_list|,
literal|"Certs"
argument_list|)
expr_stmt|;
name|addRevocationData
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
name|addAllCertsToCertArray
argument_list|()
expr_stmt|;
comment|// write incremental
name|document
operator|.
name|saveIncremental
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets or creates a dictionary entry. If existing checks for the type and sets need to be      * updated.      *      * @param clazz the class of the dictionary entry, must implement COSUpdateInfo      * @param parent where to find the element      * @param name of the element      * @return a Element of given class, new or existing      * @throws IOException when the type of the element is wrong      */
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|COSBase
operator|&
name|COSUpdateInfo
parameter_list|>
name|T
name|getOrCreateDictionaryEntry
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|COSDictionary
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|T
name|result
decl_stmt|;
name|COSBase
name|element
init|=
name|parent
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
operator|&&
name|clazz
operator|.
name|isInstance
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|result
operator|=
name|clazz
operator|.
name|cast
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|result
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Element "
operator|+
name|name
operator|+
literal|" from dictionary is not of type "
operator|+
name|clazz
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
try|try
block|{
name|result
operator|=
name|clazz
operator|.
name|getDeclaredConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ReflectiveOperationException
decl||
name|SecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to create new instance of "
operator|+
name|clazz
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|result
operator|.
name|setDirect
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|parent
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Fetches and adds revocation information based on the certInfo to the DSS.      *      * @param certInfo Certificate information from CertInformationHelper containing certificate      * chains.      * @throws IOException      */
specifier|private
name|void
name|addRevocationData
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
block|{
name|COSDictionary
name|vri
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|vriBase
operator|.
name|setItem
argument_list|(
name|certInfo
operator|.
name|getSignatureHash
argument_list|()
argument_list|,
name|vri
argument_list|)
expr_stmt|;
name|updateVRI
argument_list|(
name|certInfo
argument_list|,
name|vri
argument_list|)
expr_stmt|;
if|if
condition|(
name|certInfo
operator|.
name|getTsaCerts
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Don't add RevocationInfo from tsa to VRI's
name|correspondingOCSPs
operator|=
literal|null
expr_stmt|;
name|correspondingCRLs
operator|=
literal|null
expr_stmt|;
name|addRevocationDataRecursive
argument_list|(
name|certInfo
operator|.
name|getTsaCerts
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tries to get Revocation Data (first OCSP, else CRL) from the given Certificate Chain.      *      * @param certInfo from which to fetch revocation data. Will work recursively through its      * chains.      * @throws IOException when failed to fetch an revocation data.      */
specifier|private
name|void
name|addRevocationDataRecursive
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|certInfo
operator|.
name|isSelfSigned
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// To avoid getting same revocation information twice.
name|boolean
name|isRevocationInfoFound
init|=
name|foundRevocationInformation
operator|.
name|contains
argument_list|(
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isRevocationInfoFound
condition|)
block|{
if|if
condition|(
name|certInfo
operator|.
name|getOcspUrl
argument_list|()
operator|!=
literal|null
operator|&&
name|certInfo
operator|.
name|getIssuerCertificate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|isRevocationInfoFound
operator|=
name|fetchOcspData
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isRevocationInfoFound
operator|&&
name|certInfo
operator|.
name|getCrlUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fetchCrlData
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
name|isRevocationInfoFound
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|certInfo
operator|.
name|getOcspUrl
argument_list|()
operator|==
literal|null
operator|&&
name|certInfo
operator|.
name|getCrlUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No revocation information for cert "
operator|+
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isRevocationInfoFound
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not fetch Revocation Info for Cert: "
operator|+
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getSubjectX500Principal
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|certInfo
operator|.
name|getAlternativeCertChain
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addRevocationDataRecursive
argument_list|(
name|certInfo
operator|.
name|getAlternativeCertChain
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|certInfo
operator|.
name|getCertChain
argument_list|()
operator|!=
literal|null
operator|&&
name|certInfo
operator|.
name|getCertChain
argument_list|()
operator|.
name|getCertificate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addRevocationDataRecursive
argument_list|(
name|certInfo
operator|.
name|getCertChain
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tries to fetch and add OCSP Data to its containers.      *      * @param certInfo the certificate info, for it to check OCSP data.      * @return true when the OCSP data has successfully been fetched and added      * @throws IOException when Certificate is revoked.      */
specifier|private
name|boolean
name|fetchOcspData
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|addOcspData
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|OCSPException
decl||
name|CertificateProccessingException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed fetching Ocsp"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|RevokedCertificateException
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
comment|/**      * Tries to fetch and add CRL Data to its containers.      *      * @param certInfo the certificate info, for it to check CRL data.      * @throws IOException when failed to fetch, because no validation data could be fetched for      * data.      */
specifier|private
name|void
name|fetchCrlData
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|addCrlRevocationInfo
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
decl||
name|IOException
decl||
name|RevokedCertificateException
decl||
name|CertificateVerificationException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed fetching CRL"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Fetches and adds OCSP data to storage for the given Certificate.      *       * @param certInfo the certificate info, for it to check OCSP data.      * @throws IOException      * @throws OCSPException      * @throws CertificateProccessingException      * @throws RevokedCertificateException      */
specifier|private
name|void
name|addOcspData
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
throws|,
name|OCSPException
throws|,
name|CertificateProccessingException
throws|,
name|RevokedCertificateException
block|{
name|OcspHelper
name|ocspHelper
init|=
operator|new
name|OcspHelper
argument_list|(
name|certInfo
operator|.
name|getCertificate
argument_list|()
argument_list|,
name|signDate
operator|.
name|getTime
argument_list|()
argument_list|,
name|certInfo
operator|.
name|getIssuerCertificate
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|certInformationHelper
operator|.
name|getCertificateSet
argument_list|()
argument_list|)
argument_list|,
name|certInfo
operator|.
name|getOcspUrl
argument_list|()
argument_list|)
decl_stmt|;
name|OCSPResp
name|ocspResp
init|=
name|ocspHelper
operator|.
name|getResponseOcsp
argument_list|()
decl_stmt|;
name|BasicOCSPResp
name|basicResponse
init|=
operator|(
name|BasicOCSPResp
operator|)
name|ocspResp
operator|.
name|getResponseObject
argument_list|()
decl_stmt|;
name|X509Certificate
name|ocspResponderCertificate
init|=
name|ocspHelper
operator|.
name|getOcspResponderCertificate
argument_list|()
decl_stmt|;
name|certInformationHelper
operator|.
name|addAllCertsFromHolders
argument_list|(
name|basicResponse
operator|.
name|getCerts
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|signatureHash
decl_stmt|;
try|try
block|{
name|signatureHash
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
operator|.
name|digest
argument_list|(
name|basicResponse
operator|.
name|getSignature
argument_list|()
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
name|CertificateProccessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|String
name|signatureHashHex
init|=
name|Hex
operator|.
name|getString
argument_list|(
name|signatureHash
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|vriBase
operator|.
name|containsKey
argument_list|(
name|signatureHashHex
argument_list|)
condition|)
block|{
name|COSArray
name|savedCorrespondingOCSPs
init|=
name|correspondingOCSPs
decl_stmt|;
name|COSArray
name|savedCorrespondingCRLs
init|=
name|correspondingCRLs
decl_stmt|;
name|COSDictionary
name|vri
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|vriBase
operator|.
name|setItem
argument_list|(
name|signatureHashHex
argument_list|,
name|vri
argument_list|)
expr_stmt|;
name|CertSignatureInformation
name|ocspCertInfo
init|=
name|certInformationHelper
operator|.
name|getCertInfo
argument_list|(
name|ocspResponderCertificate
argument_list|)
decl_stmt|;
name|updateVRI
argument_list|(
name|ocspCertInfo
argument_list|,
name|vri
argument_list|)
expr_stmt|;
name|correspondingOCSPs
operator|=
name|savedCorrespondingOCSPs
expr_stmt|;
name|correspondingCRLs
operator|=
name|savedCorrespondingCRLs
expr_stmt|;
block|}
name|byte
index|[]
name|ocspData
init|=
name|ocspResp
operator|.
name|getEncoded
argument_list|()
decl_stmt|;
name|COSStream
name|ocspStream
init|=
name|writeDataToStream
argument_list|(
name|ocspData
argument_list|)
decl_stmt|;
name|ocsps
operator|.
name|add
argument_list|(
name|ocspStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|correspondingOCSPs
operator|!=
literal|null
condition|)
block|{
name|correspondingOCSPs
operator|.
name|add
argument_list|(
name|ocspStream
argument_list|)
expr_stmt|;
block|}
name|foundRevocationInformation
operator|.
name|add
argument_list|(
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fetches and adds CRL data to storage for the given Certificate.      *       * @param certInfo the certificate info, for it to check CRL data.      * @throws IOException      * @throws RevokedCertificateException      * @throws GeneralSecurityException      * @throws CertificateVerificationException       */
specifier|private
name|void
name|addCrlRevocationInfo
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
throws|,
name|RevokedCertificateException
throws|,
name|GeneralSecurityException
throws|,
name|CertificateVerificationException
block|{
name|X509CRL
name|crl
init|=
name|CRLVerifier
operator|.
name|downloadCRLFromWeb
argument_list|(
name|certInfo
operator|.
name|getCrlUrl
argument_list|()
argument_list|)
decl_stmt|;
name|X509Certificate
name|issuerCertificate
init|=
name|certInfo
operator|.
name|getIssuerCertificate
argument_list|()
decl_stmt|;
comment|// find the issuer certificate (usually issuer of signature certificate)
for|for
control|(
name|X509Certificate
name|certificate
range|:
name|certInformationHelper
operator|.
name|getCertificateSet
argument_list|()
control|)
block|{
if|if
condition|(
name|certificate
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|equals
argument_list|(
name|crl
operator|.
name|getIssuerX500Principal
argument_list|()
argument_list|)
condition|)
block|{
name|issuerCertificate
operator|=
name|certificate
expr_stmt|;
break|break;
block|}
block|}
name|crl
operator|.
name|verify
argument_list|(
name|issuerCertificate
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|SecurityProvider
operator|.
name|getProvider
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|CRLVerifier
operator|.
name|checkRevocation
argument_list|(
name|crl
argument_list|,
name|certInfo
operator|.
name|getCertificate
argument_list|()
argument_list|,
name|signDate
operator|.
name|getTime
argument_list|()
argument_list|,
name|certInfo
operator|.
name|getCrlUrl
argument_list|()
argument_list|)
expr_stmt|;
name|COSStream
name|crlStream
init|=
name|writeDataToStream
argument_list|(
name|crl
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|crls
operator|.
name|add
argument_list|(
name|crlStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|correspondingCRLs
operator|!=
literal|null
condition|)
block|{
name|correspondingCRLs
operator|.
name|add
argument_list|(
name|crlStream
argument_list|)
expr_stmt|;
name|byte
index|[]
name|signatureHash
decl_stmt|;
try|try
block|{
name|signatureHash
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
operator|.
name|digest
argument_list|(
name|crl
operator|.
name|getSignature
argument_list|()
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
name|CertificateVerificationException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|String
name|signatureHashHex
init|=
name|Hex
operator|.
name|getString
argument_list|(
name|signatureHash
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|vriBase
operator|.
name|containsKey
argument_list|(
name|signatureHashHex
argument_list|)
condition|)
block|{
name|COSArray
name|savedCorrespondingOCSPs
init|=
name|correspondingOCSPs
decl_stmt|;
name|COSArray
name|savedCorrespondingCRLs
init|=
name|correspondingCRLs
decl_stmt|;
name|COSDictionary
name|vri
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|vriBase
operator|.
name|setItem
argument_list|(
name|signatureHashHex
argument_list|,
name|vri
argument_list|)
expr_stmt|;
name|CertSignatureInformation
name|crlCertInfo
decl_stmt|;
try|try
block|{
name|crlCertInfo
operator|=
name|certInformationHelper
operator|.
name|getCertInfo
argument_list|(
name|issuerCertificate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateProccessingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CertificateVerificationException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|updateVRI
argument_list|(
name|crlCertInfo
argument_list|,
name|vri
argument_list|)
expr_stmt|;
name|correspondingOCSPs
operator|=
name|savedCorrespondingOCSPs
expr_stmt|;
name|correspondingCRLs
operator|=
name|savedCorrespondingCRLs
expr_stmt|;
block|}
block|}
name|foundRevocationInformation
operator|.
name|add
argument_list|(
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateVRI
parameter_list|(
name|CertSignatureInformation
name|certInfo
parameter_list|,
name|COSDictionary
name|vri
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|certInfo
operator|.
name|getCertificate
argument_list|()
operator|.
name|getExtensionValue
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_nocheck
operator|.
name|getId
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|correspondingOCSPs
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|correspondingCRLs
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|addRevocationDataRecursive
argument_list|(
name|certInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|correspondingOCSPs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|vri
operator|.
name|setItem
argument_list|(
literal|"OCSP"
argument_list|,
name|correspondingOCSPs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|correspondingCRLs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|vri
operator|.
name|setItem
argument_list|(
literal|"CRL"
argument_list|,
name|correspondingCRLs
argument_list|)
expr_stmt|;
block|}
block|}
name|COSArray
name|correspondingCerts
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|CertSignatureInformation
name|ci
init|=
name|certInfo
decl_stmt|;
do|do
block|{
name|X509Certificate
name|cert
init|=
name|ci
operator|.
name|getCertificate
argument_list|()
decl_stmt|;
try|try
block|{
name|COSStream
name|certStream
init|=
name|writeDataToStream
argument_list|(
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|correspondingCerts
operator|.
name|add
argument_list|(
name|certStream
argument_list|)
expr_stmt|;
name|certs
operator|.
name|add
argument_list|(
name|certStream
argument_list|)
expr_stmt|;
comment|// may lead to duplicate certificates. Important?
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|ex
parameter_list|)
block|{
comment|// should not happen because these are existing certificates
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
if|if
condition|(
name|cert
operator|.
name|getExtensionValue
argument_list|(
name|OCSPObjectIdentifiers
operator|.
name|id_pkix_ocsp_nocheck
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
name|ci
operator|=
name|ci
operator|.
name|getCertChain
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|ci
operator|!=
literal|null
condition|)
do|;
name|vri
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CERT
argument_list|,
name|correspondingCerts
argument_list|)
expr_stmt|;
name|vri
operator|.
name|setDate
argument_list|(
name|COSName
operator|.
name|TU
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds all certs to the certs-array. Make sure, all certificates are inside the      * certificateStore of certInformationHelper      *      * @throws IOException      */
specifier|private
name|void
name|addAllCertsToCertArray
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
for|for
control|(
name|X509Certificate
name|cert
range|:
name|certInformationHelper
operator|.
name|getCertificateSet
argument_list|()
control|)
block|{
name|COSStream
name|stream
init|=
name|writeDataToStream
argument_list|(
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|certs
operator|.
name|add
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Creates a Flate encoded<code>COSStream</code> object with the given data.      *       * @param data to write into the COSStream      * @return COSStream a COSStream object that can be added to the document      * @throws IOException      */
specifier|private
name|COSStream
name|writeDataToStream
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
name|stream
operator|.
name|createOutputStream
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
init|)
block|{
name|os
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|stream
return|;
block|}
comment|/**      * Adds Extensions to the document catalog. So that the use of DSS is identified. Described in      * PAdES Part 4, Chapter 4.4.      *      * @param catalog to add Extensions into      */
specifier|private
name|void
name|addExtensions
parameter_list|(
name|PDDocumentCatalog
name|catalog
parameter_list|)
block|{
name|COSDictionary
name|dssExtensions
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dssExtensions
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|catalog
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"Extensions"
argument_list|,
name|dssExtensions
argument_list|)
expr_stmt|;
name|COSDictionary
name|adbeExtension
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|adbeExtension
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dssExtensions
operator|.
name|setItem
argument_list|(
literal|"ADBE"
argument_list|,
name|adbeExtension
argument_list|)
expr_stmt|;
name|adbeExtension
operator|.
name|setName
argument_list|(
literal|"BaseVersion"
argument_list|,
literal|"1.7"
argument_list|)
expr_stmt|;
name|adbeExtension
operator|.
name|setInt
argument_list|(
literal|"ExtensionLevel"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|catalog
operator|.
name|setVersion
argument_list|(
literal|"1.7"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// register BouncyCastle provider, needed for "exotic" algorithms
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
comment|// add ocspInformation
name|AddValidationInformation
name|addOcspInformation
init|=
operator|new
name|AddValidationInformation
argument_list|()
decl_stmt|;
name|File
name|inFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|inFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|substring
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|inFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|substring
operator|+
literal|"_ocsp.pdf"
argument_list|)
decl_stmt|;
name|addOcspInformation
operator|.
name|validateSignature
argument_list|(
name|inFile
argument_list|,
name|outFile
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: java "
operator|+
name|AddValidationInformation
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
literal|"<pdf_to_add_ocsp>\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

