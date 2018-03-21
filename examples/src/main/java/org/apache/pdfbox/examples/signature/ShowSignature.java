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
name|File
import|;
end_import

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
name|text
operator|.
name|SimpleDateFormat
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
name|COSInputStream
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
name|COSObject
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
name|io
operator|.
name|IOUtils
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
name|CMSProcessable
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
name|CMSProcessableByteArray
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
name|cms
operator|.
name|jcajce
operator|.
name|JcaSimpleSignerInfoVerifierBuilder
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
name|Store
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
name|StoreException
import|;
end_import

begin_comment
comment|/**  * This will read a document from the filesystem, decrypt it and do something with the signature.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ShowSignature
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
name|ShowSignature
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"dd.MM.yyyy HH:mm:ss"
argument_list|)
decl_stmt|;
specifier|private
name|ShowSignature
parameter_list|()
block|{     }
comment|/**      * This is the entry point for the application.      *      * @param args The command-line arguments.      *      * @throws IOException If there is an error reading the file.      * @throws CertificateException      * @throws java.security.NoSuchAlgorithmException      * @throws java.security.NoSuchProviderException      * @throws org.bouncycastle.tsp.TSPException      */
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
throws|,
name|CertificateException
throws|,
name|NoSuchAlgorithmException
throws|,
name|NoSuchProviderException
throws|,
name|TSPException
block|{
name|ShowSignature
name|show
init|=
operator|new
name|ShowSignature
argument_list|()
decl_stmt|;
name|show
operator|.
name|showSignature
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|showSignature
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
throws|,
name|NoSuchAlgorithmException
throws|,
name|NoSuchProviderException
throws|,
name|TSPException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|password
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|File
name|infile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
try|try
init|(
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|infile
argument_list|,
name|password
argument_list|)
init|)
block|{
for|for
control|(
name|PDSignature
name|sig
range|:
name|document
operator|.
name|getSignatureDictionaries
argument_list|()
control|)
block|{
name|COSDictionary
name|sigDict
init|=
name|sig
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSString
name|contents
init|=
operator|(
name|COSString
operator|)
name|sigDict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
decl_stmt|;
comment|// download the signed content
name|byte
index|[]
name|buf
decl_stmt|;
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|infile
argument_list|)
init|)
block|{
name|buf
operator|=
name|sig
operator|.
name|getSignedContent
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature found"
argument_list|)
expr_stmt|;
name|int
index|[]
name|byteRange
init|=
name|sig
operator|.
name|getByteRange
argument_list|()
decl_stmt|;
if|if
condition|(
name|byteRange
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Signature byteRange must have 4 items"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|long
name|fileLen
init|=
name|infile
operator|.
name|length
argument_list|()
decl_stmt|;
name|long
name|rangeMax
init|=
name|byteRange
index|[
literal|2
index|]
operator|+
operator|(
name|long
operator|)
name|byteRange
index|[
literal|3
index|]
decl_stmt|;
comment|// multiply content length with 2 (because it is in hex in the PDF) and add 2 for< and>
name|int
name|contentLen
init|=
name|contents
operator|.
name|getString
argument_list|()
operator|.
name|length
argument_list|()
operator|*
literal|2
operator|+
literal|2
decl_stmt|;
if|if
condition|(
name|fileLen
operator|!=
name|rangeMax
operator|||
name|byteRange
index|[
literal|0
index|]
operator|!=
literal|0
operator|||
name|byteRange
index|[
literal|1
index|]
operator|+
name|contentLen
operator|!=
name|byteRange
index|[
literal|2
index|]
condition|)
block|{
comment|// a false result doesn't necessarily mean that the PDF is a fake
comment|// see this answer why:
comment|// https://stackoverflow.com/a/48185913/535646
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature does not cover whole document"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature covers whole document"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sig
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Name:     "
operator|+
name|sig
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sig
operator|.
name|getSignDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Modified: "
operator|+
name|sdf
operator|.
name|format
argument_list|(
name|sig
operator|.
name|getSignDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|subFilter
init|=
name|sig
operator|.
name|getSubFilter
argument_list|()
decl_stmt|;
if|if
condition|(
name|subFilter
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|subFilter
condition|)
block|{
case|case
literal|"adbe.pkcs7.detached"
case|:
case|case
literal|"ETSI.CAdES.detached"
case|:
name|verifyPKCS7
argument_list|(
name|buf
argument_list|,
name|contents
argument_list|,
name|sig
argument_list|)
expr_stmt|;
comment|//TODO check certificate chain, revocation lists, timestamp...
break|break;
case|case
literal|"adbe.pkcs7.sha1"
case|:
block|{
comment|// example: PDFBOX-1452.pdf
name|byte
index|[]
name|certData
init|=
name|contents
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|certStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|certData
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Certificate
argument_list|>
name|certs
init|=
name|factory
operator|.
name|generateCertificates
argument_list|(
name|certStream
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certs="
operator|+
name|certs
argument_list|)
expr_stmt|;
name|byte
index|[]
name|hash
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA1"
argument_list|)
operator|.
name|digest
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|verifyPKCS7
argument_list|(
name|hash
argument_list|,
name|contents
argument_list|,
name|sig
argument_list|)
expr_stmt|;
comment|//TODO check certificate chain, revocation lists, timestamp...
break|break;
block|}
case|case
literal|"adbe.x509.rsa_sha1"
case|:
block|{
comment|// example: PDFBOX-2693.pdf
name|COSString
name|certString
init|=
operator|(
name|COSString
operator|)
name|sigDict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CERT
argument_list|)
decl_stmt|;
if|if
condition|(
name|certString
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"The /Cert certificate string is missing in the signature dictionary"
argument_list|)
expr_stmt|;
return|return;
block|}
name|byte
index|[]
name|certData
init|=
name|certString
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|certStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|certData
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Certificate
argument_list|>
name|certs
init|=
name|factory
operator|.
name|generateCertificates
argument_list|(
name|certStream
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certs="
operator|+
name|certs
argument_list|)
expr_stmt|;
comment|//TODO verify signature
break|break;
block|}
case|case
literal|"ETSI.RFC3161"
case|:
name|TimeStampToken
name|timeStampToken
init|=
operator|new
name|TimeStampToken
argument_list|(
operator|new
name|CMSSignedData
argument_list|(
name|contents
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Time stamp gen time: "
operator|+
name|timeStampToken
operator|.
name|getTimeStampInfo
argument_list|()
operator|.
name|getGenTime
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Time stamp tsa name: "
operator|+
name|timeStampToken
operator|.
name|getTimeStampInfo
argument_list|()
operator|.
name|getTsa
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|certStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|contents
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Certificate
argument_list|>
name|certs
init|=
name|factory
operator|.
name|generateCertificates
argument_list|(
name|certStream
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certs="
operator|+
name|certs
argument_list|)
expr_stmt|;
comment|//TODO verify signature
break|break;
default|default:
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown certificate type: "
operator|+
name|subFilter
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing subfilter for cert dictionary"
argument_list|)
throw|;
block|}
block|}
name|analyseDSS
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CMSException
decl||
name|OperatorCreationException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Analyzed: "
operator|+
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Verify a PKCS7 signature.      *      * @param byteArray the byte sequence that has been signed      * @param contents the /Contents field as a COSString      * @param sig the PDF signature (the /V dictionary)      * @throws CertificateException      * @throws CMSException      * @throws StoreException      * @throws OperatorCreationException      */
specifier|private
name|void
name|verifyPKCS7
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|,
name|COSString
name|contents
parameter_list|,
name|PDSignature
name|sig
parameter_list|)
throws|throws
name|CMSException
throws|,
name|CertificateException
throws|,
name|StoreException
throws|,
name|OperatorCreationException
throws|,
name|NoSuchAlgorithmException
throws|,
name|NoSuchProviderException
block|{
comment|// inspiration:
comment|// http://stackoverflow.com/a/26702631/535646
comment|// http://stackoverflow.com/a/9261365/535646
name|CMSProcessable
name|signedContent
init|=
operator|new
name|CMSProcessableByteArray
argument_list|(
name|byteArray
argument_list|)
decl_stmt|;
name|CMSSignedData
name|signedData
init|=
operator|new
name|CMSSignedData
argument_list|(
name|signedContent
argument_list|,
name|contents
operator|.
name|getBytes
argument_list|()
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
name|signerInformation
operator|.
name|getSID
argument_list|()
argument_list|)
decl_stmt|;
name|X509CertificateHolder
name|certificateHolder
init|=
name|matches
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|X509Certificate
name|certFromSignedData
init|=
operator|new
name|JcaX509CertificateConverter
argument_list|()
operator|.
name|getCertificate
argument_list|(
name|certificateHolder
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certFromSignedData: "
operator|+
name|certFromSignedData
argument_list|)
expr_stmt|;
name|certFromSignedData
operator|.
name|checkValidity
argument_list|(
name|sig
operator|.
name|getSignDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSelfSigned
argument_list|(
name|certFromSignedData
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Certificate is self-signed, LOL!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Certificate is not self-signed"
argument_list|)
expr_stmt|;
comment|// todo rest of chain
block|}
if|if
condition|(
name|signerInformation
operator|.
name|verify
argument_list|(
operator|new
name|JcaSimpleSignerInfoVerifierBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|certFromSignedData
argument_list|)
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature verified"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature verification failed"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Analyzes the DSS-Dictionary (Document Security Store) of the document. Which is used for signature validation.      * The DSS is defined in PAdES Part 4 - Long Term Validation.      *       * @param document PDDocument, to get the DSS from      */
specifier|private
name|void
name|analyseDSS
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|COSBase
name|dssElement
init|=
name|catalog
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"DSS"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dssElement
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|dss
init|=
operator|(
name|COSDictionary
operator|)
name|dssElement
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"DSS Dictionary: "
operator|+
name|dss
argument_list|)
expr_stmt|;
name|COSBase
name|certsElement
init|=
name|dss
operator|.
name|getDictionaryObject
argument_list|(
literal|"Certs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|certsElement
operator|instanceof
name|COSArray
condition|)
block|{
name|printStreamsFromArray
argument_list|(
operator|(
name|COSArray
operator|)
name|certsElement
argument_list|,
literal|"Cert"
argument_list|)
expr_stmt|;
block|}
name|COSBase
name|ocspsElement
init|=
name|dss
operator|.
name|getDictionaryObject
argument_list|(
literal|"OCSPs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ocspsElement
operator|instanceof
name|COSArray
condition|)
block|{
name|printStreamsFromArray
argument_list|(
operator|(
name|COSArray
operator|)
name|ocspsElement
argument_list|,
literal|"Ocsp"
argument_list|)
expr_stmt|;
block|}
name|COSBase
name|crlElement
init|=
name|dss
operator|.
name|getDictionaryObject
argument_list|(
literal|"CRLs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|crlElement
operator|instanceof
name|COSArray
condition|)
block|{
name|printStreamsFromArray
argument_list|(
operator|(
name|COSArray
operator|)
name|crlElement
argument_list|,
literal|"CRL"
argument_list|)
expr_stmt|;
block|}
comment|// TODO: go through VRIs (which indirectly point to the DSS-Data)
block|}
block|}
comment|/**      * Go through the elements of a COSArray containing each an COSStream to print in Hex.      *       * @param elements COSArray of elements containing a COS Stream      * @param description to append on Print      * @throws IOException      */
specifier|private
name|void
name|printStreamsFromArray
parameter_list|(
name|COSArray
name|elements
parameter_list|,
name|String
name|description
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|COSBase
name|baseElem
range|:
name|elements
control|)
block|{
name|COSObject
name|streamObj
init|=
operator|(
name|COSObject
operator|)
name|baseElem
decl_stmt|;
if|if
condition|(
name|streamObj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|streamObj
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|COSInputStream
name|input
init|=
name|cosStream
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|streamBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|description
operator|+
literal|" ("
operator|+
name|elements
operator|.
name|indexOf
argument_list|(
name|streamObj
argument_list|)
operator|+
literal|"): "
operator|+
name|Hex
operator|.
name|getString
argument_list|(
name|streamBytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// https://svn.apache.org/repos/asf/cxf/tags/cxf-2.4.1/distribution/src/main/release/samples/sts_issue_operation/src/main/java/demo/sts/provider/cert/CertificateVerifier.java
comment|/**      * Checks whether given X.509 certificate is self-signed.      */
specifier|private
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
decl||
name|InvalidKeyException
name|sigEx
parameter_list|)
block|{
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
comment|/**      * This will print a usage message.      */
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
name|ShowSignature
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<password><inputfile>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

