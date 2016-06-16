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
name|Calendar
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
name|SignatureOptions
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
name|visible
operator|.
name|PDVisibleSigProperties
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
name|visible
operator|.
name|PDVisibleSignDesigner
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

begin_comment
comment|/**  * This is an example for visual signing a pdf with bouncy castle.   * @see CreateSignature  * @author Vakhtang Koroghlishvili  */
end_comment

begin_class
specifier|public
class|class
name|CreateVisibleSignature
extends|extends
name|CreateSignatureBase
block|{
specifier|private
specifier|static
specifier|final
name|BouncyCastleProvider
name|BCPROVIDER
init|=
operator|new
name|BouncyCastleProvider
argument_list|()
decl_stmt|;
specifier|private
name|SignatureOptions
name|signatureOptions
decl_stmt|;
specifier|private
name|PDVisibleSignDesigner
name|visibleSignDesigner
decl_stmt|;
specifier|private
specifier|final
name|PDVisibleSigProperties
name|visibleSignatureProperties
init|=
operator|new
name|PDVisibleSigProperties
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setvisibleSignDesigner
parameter_list|(
name|String
name|filename
parameter_list|,
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|zoomPercent
parameter_list|,
name|FileInputStream
name|image
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|visibleSignDesigner
operator|=
operator|new
name|PDVisibleSignDesigner
argument_list|(
name|filename
argument_list|,
name|image
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|visibleSignDesigner
operator|.
name|xAxis
argument_list|(
name|x
argument_list|)
operator|.
name|yAxis
argument_list|(
name|y
argument_list|)
operator|.
name|zoom
argument_list|(
name|zoomPercent
argument_list|)
operator|.
name|signatureFieldName
argument_list|(
literal|"signature"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setVisibleSignatureProperties
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|reason
parameter_list|,
name|int
name|preferredSize
parameter_list|,
name|int
name|page
parameter_list|,
name|boolean
name|visualSignEnabled
parameter_list|)
throws|throws
name|IOException
block|{
name|visibleSignatureProperties
operator|.
name|signerName
argument_list|(
name|name
argument_list|)
operator|.
name|signerLocation
argument_list|(
name|location
argument_list|)
operator|.
name|signatureReason
argument_list|(
name|reason
argument_list|)
operator|.
name|preferredSize
argument_list|(
name|preferredSize
argument_list|)
operator|.
name|page
argument_list|(
name|page
argument_list|)
operator|.
name|visualSignEnabled
argument_list|(
name|visualSignEnabled
argument_list|)
operator|.
name|setPdVisibleSignature
argument_list|(
name|visibleSignDesigner
argument_list|)
operator|.
name|buildSignature
argument_list|()
expr_stmt|;
block|}
comment|/**      * Initialize the signature creator with a keystore (pkcs12) and pin that      * should be used for the signature.      *      * @param keystore is a pkcs12 keystore.      * @param pin is the pin for the keystore / private key      * @throws KeyStoreException if the keystore has not been initialized (loaded)      * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found      * @throws UnrecoverableKeyException if the given password is wrong      * @throws CertificateException if the certificate is not valid as signing time      */
specifier|public
name|CreateVisibleSignature
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
init|=
literal|null
decl_stmt|;
if|if
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
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not find alias"
argument_list|)
throw|;
block|}
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
name|cert
init|=
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
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
block|}
comment|/**      * Sign pdf file and create new file that ends with "_signed.pdf".      *      * @param inputFile The source pdf document file.      * @param signedFile The file to be signed.      * @param tsaClient optional TSA client      * @throws IOException      */
specifier|public
name|void
name|signPDF
parameter_list|(
name|File
name|inputFile
parameter_list|,
name|File
name|signedFile
parameter_list|,
name|TSAClient
name|tsaClient
parameter_list|)
throws|throws
name|IOException
block|{
name|setTsaClient
argument_list|(
name|tsaClient
argument_list|)
expr_stmt|;
if|if
condition|(
name|inputFile
operator|==
literal|null
operator|||
operator|!
name|inputFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Document for signing does not exist"
argument_list|)
throw|;
block|}
comment|// creating output document and prepare the IO streams.
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|signedFile
argument_list|)
decl_stmt|;
comment|// load document
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
comment|// create signature dictionary
name|PDSignature
name|signature
init|=
operator|new
name|PDSignature
argument_list|()
decl_stmt|;
comment|// default filter
name|signature
operator|.
name|setFilter
argument_list|(
name|PDSignature
operator|.
name|FILTER_ADOBE_PPKLITE
argument_list|)
expr_stmt|;
comment|// subfilter for basic and PAdES Part 2 signatures
name|signature
operator|.
name|setSubFilter
argument_list|(
name|PDSignature
operator|.
name|SUBFILTER_ADBE_PKCS7_DETACHED
argument_list|)
expr_stmt|;
if|if
condition|(
name|visibleSignatureProperties
operator|!=
literal|null
condition|)
block|{
name|signature
operator|.
name|setName
argument_list|(
name|visibleSignatureProperties
operator|.
name|getSignerName
argument_list|()
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setLocation
argument_list|(
name|visibleSignatureProperties
operator|.
name|getSignerLocation
argument_list|()
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setReason
argument_list|(
name|visibleSignatureProperties
operator|.
name|getSignatureReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// the signing date, needed for valid signature
name|signature
operator|.
name|setSignDate
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
comment|// register signature dictionary and sign interface
if|if
condition|(
name|visibleSignatureProperties
operator|!=
literal|null
operator|&&
name|visibleSignatureProperties
operator|.
name|isVisualSignEnabled
argument_list|()
condition|)
block|{
name|signatureOptions
operator|=
operator|new
name|SignatureOptions
argument_list|()
expr_stmt|;
name|signatureOptions
operator|.
name|setVisualSignature
argument_list|(
name|visibleSignatureProperties
operator|.
name|getVisibleSignature
argument_list|()
argument_list|)
expr_stmt|;
name|signatureOptions
operator|.
name|setPage
argument_list|(
name|visibleSignatureProperties
operator|.
name|getPage
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|,
name|this
argument_list|,
name|signatureOptions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|// write incremental (only for signing purpose)
name|doc
operator|.
name|saveIncremental
argument_list|(
name|fos
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// do not close options before saving, because some COSStream objects within options
comment|// are transferred to the signed document.
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|signatureOptions
argument_list|)
expr_stmt|;
block|}
comment|/**      * Arguments are      * [0] key store      * [1] pin      * [2] document that will be signed      * [3] image of visible signature      * @param args      * @throws java.security.KeyStoreException      * @throws java.security.cert.CertificateException      * @throws java.io.IOException      * @throws java.security.NoSuchAlgorithmException      * @throws java.security.UnrecoverableKeyException      */
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
name|KeyStoreException
throws|,
name|CertificateException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
throws|,
name|UnrecoverableKeyException
block|{
comment|// generate with
comment|// keytool -storepass 123456 -storetype PKCS12 -keystore file.p12 -genkey -alias client -keyalg RSA
if|if
condition|(
name|args
operator|.
name|length
operator|<
literal|4
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
name|String
name|tsaUrl
init|=
literal|null
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-tsa"
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|tsaUrl
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
name|File
name|ksFile
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
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"PKCS12"
argument_list|,
name|BCPROVIDER
argument_list|)
decl_stmt|;
name|char
index|[]
name|pin
init|=
name|args
index|[
literal|1
index|]
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|ksFile
argument_list|)
argument_list|,
name|pin
argument_list|)
expr_stmt|;
comment|// TSA client
name|TSAClient
name|tsaClient
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tsaUrl
operator|!=
literal|null
condition|)
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-256"
argument_list|)
decl_stmt|;
name|tsaClient
operator|=
operator|new
name|TSAClient
argument_list|(
operator|new
name|URL
argument_list|(
name|tsaUrl
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|digest
argument_list|)
expr_stmt|;
block|}
name|File
name|documentFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|CreateVisibleSignature
name|signing
init|=
operator|new
name|CreateVisibleSignature
argument_list|(
name|keystore
argument_list|,
name|pin
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
name|FileInputStream
name|image
init|=
operator|new
name|FileInputStream
argument_list|(
name|args
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|documentFile
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
name|signedDocumentFile
init|=
operator|new
name|File
argument_list|(
name|documentFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|substring
operator|+
literal|"_signed.pdf"
argument_list|)
decl_stmt|;
comment|// page is 1-based here
name|int
name|page
init|=
literal|1
decl_stmt|;
name|signing
operator|.
name|setvisibleSignDesigner
argument_list|(
name|args
index|[
literal|2
index|]
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
operator|-
literal|50
argument_list|,
name|image
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|signing
operator|.
name|setVisibleSignatureProperties
argument_list|(
literal|"name"
argument_list|,
literal|"location"
argument_list|,
literal|"Security"
argument_list|,
literal|0
argument_list|,
name|page
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|signing
operator|.
name|signPDF
argument_list|(
name|documentFile
argument_list|,
name|signedDocumentFile
argument_list|,
name|tsaClient
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the usage for this program.      */
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
literal|"Usage: java "
operator|+
name|CreateVisibleSignature
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<pkcs12-keystore-file><pin><input-pdf><sign-image>\n"
operator|+
literal|""
operator|+
literal|"options:\n"
operator|+
literal|"  -tsa<url>    sign timestamp using the given TSA server"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

