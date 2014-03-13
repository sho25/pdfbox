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
name|CMSSignedGenerator
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
comment|/**  * An example for singing a PDF with bouncy castle.  * A keystore can be created with the java keytool, for example:  *  * {@code keytool -genkeypair -storepass 123456 -storetype pkcs12 -alias test -validity 365  *        -v -keyalg RSA -keystore keystore.p12 }  *  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|CreateSignature
implements|implements
name|SignatureInterface
block|{
specifier|private
specifier|static
name|BouncyCastleProvider
name|provider
init|=
operator|new
name|BouncyCastleProvider
argument_list|()
decl_stmt|;
specifier|private
name|PrivateKey
name|privKey
decl_stmt|;
specifier|private
name|Certificate
index|[]
name|cert
decl_stmt|;
specifier|private
name|SignatureOptions
name|options
decl_stmt|;
comment|/**      * Initialize the signature creator with a keystore (pkcs12) and pin that      * should be used for the signature.      *      * @param keystore is a pkcs12 keystore.      * @param pin is the pin for the keystore / private key      */
specifier|public
name|CreateSignature
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|char
index|[]
name|pin
parameter_list|)
block|{
try|try
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
name|RuntimeException
argument_list|(
literal|"Could not find alias"
argument_list|)
throw|;
block|}
name|privKey
operator|=
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
expr_stmt|;
name|cert
operator|=
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnrecoverableKeyException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Could not extract private key."
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown algorithm."
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Signs the given pdf file.      *      * @param document is the pdf document      * @return the signed pdf document      * @throws IOException      */
specifier|public
name|File
name|signPDF
parameter_list|(
name|File
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8
operator|*
literal|1024
index|]
decl_stmt|;
if|if
condition|(
name|document
operator|==
literal|null
operator|||
operator|!
name|document
operator|.
name|exists
argument_list|()
condition|)
block|{
operator|new
name|RuntimeException
argument_list|(
literal|"Document for signing does not exist"
argument_list|)
expr_stmt|;
block|}
comment|// creating output document and prepare the IO streams.
name|String
name|name
init|=
name|document
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
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|outputDocument
init|=
operator|new
name|File
argument_list|(
name|document
operator|.
name|getParent
argument_list|()
argument_list|,
name|substring
operator|+
literal|"_signed.pdf"
argument_list|)
decl_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outputDocument
argument_list|)
decl_stmt|;
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|outputDocument
argument_list|)
expr_stmt|;
comment|// load document
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|document
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
name|signature
operator|.
name|setFilter
argument_list|(
name|PDSignature
operator|.
name|FILTER_ADOBE_PPKLITE
argument_list|)
expr_stmt|;
comment|// default filter
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
name|signature
operator|.
name|setName
argument_list|(
literal|"signer name"
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setLocation
argument_list|(
literal|"signer location"
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setReason
argument_list|(
literal|"reason for signature"
argument_list|)
expr_stmt|;
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
name|options
operator|==
literal|null
condition|)
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
else|else
block|{
name|doc
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|,
name|this
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
comment|// write incremental (only for signing purpose)
name|doc
operator|.
name|saveIncremental
argument_list|(
name|fis
argument_list|,
name|fos
argument_list|)
expr_stmt|;
return|return
name|outputDocument
return|;
block|}
comment|/**      * SignatureInterface implementation.      *      * This method will be called from inside of the pdfbox and create the pkcs7 signature.      * The given InputStream contains the bytes that are given by the byte range.      *      * This method is for internal use only.<-- TODO this method should be private      *      * Use your favorite cryptographic library to implement pkcs7 signature creation.      */
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
name|CMSProcessableInputStream
name|input
init|=
operator|new
name|CMSProcessableInputStream
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|CMSSignedDataGenerator
name|gen
init|=
operator|new
name|CMSSignedDataGenerator
argument_list|()
decl_stmt|;
comment|// CertificateChain
name|List
argument_list|<
name|Certificate
argument_list|>
name|certList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|cert
argument_list|)
decl_stmt|;
name|CertStore
name|certStore
init|=
literal|null
decl_stmt|;
try|try
block|{
name|certStore
operator|=
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|certList
argument_list|)
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|gen
operator|.
name|addSigner
argument_list|(
name|privKey
argument_list|,
operator|(
name|X509Certificate
operator|)
name|certList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|CMSSignedGenerator
operator|.
name|DIGEST_SHA256
argument_list|)
expr_stmt|;
name|gen
operator|.
name|addCertificatesAndCRLs
argument_list|(
name|certStore
argument_list|)
expr_stmt|;
name|CMSSignedData
name|signedData
init|=
name|gen
operator|.
name|generate
argument_list|(
name|input
argument_list|,
literal|false
argument_list|,
name|provider
argument_list|)
decl_stmt|;
return|return
name|signedData
operator|.
name|getEncoded
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// should be handled
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error while creating pkcs7 signature."
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Problem while preparing signature"
argument_list|)
throw|;
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
name|KeyStoreException
throws|,
name|CertificateException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
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
else|else
block|{
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
name|provider
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
name|File
name|document
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
name|CreateSignature
name|signing
init|=
operator|new
name|CreateSignature
argument_list|(
name|keystore
argument_list|,
name|pin
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
name|signing
operator|.
name|signPDF
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
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
literal|"Usage: java "
operator|+
name|CreateSignature
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<pkcs12-keystore-file><pin><input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

