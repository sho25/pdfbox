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
name|ArrayList
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
name|SignerInformationStore
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

begin_comment
comment|/**  * An example for singing a PDF with bouncy castle.  * A keystore can be created with the java keytool, for example:  *  * {@code keytool -genkeypair -storepass 123456 -storetype pkcs12 -alias test -validity 365  *        -v -keyalg RSA -keystore keystore.p12 }  *  * @author Thomas Chojecki  * @author Vakhtang Koroghlishvili  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|CreateSignature
extends|extends
name|CreateSignatureBase
block|{
comment|/**      * Initialize the signature creator with a keystore and certficate password.      * @param keystore the pkcs12 keystore containing the signing certificate      * @param password the password for recovering the key      * @throws KeyStoreException if the keystore has not been initialized (loaded)      * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found      * @throws UnrecoverableKeyException if the given password is wrong      * @throws CertificateException if the certificate is not valid as signing time      */
specifier|public
name|CreateSignature
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|UnrecoverableKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
block|{
comment|// grabs the first alias from the keystore and get the private key. An
comment|// TODO alternative method or constructor could be used for setting a specific
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
name|KeyStoreException
argument_list|(
literal|"Keystore is empty"
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
name|password
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
comment|/**      * Signs the given PDF file. Alters the original file on disk.      * @param file the PDF file to sign      * @throws IOException if the file could not be read or written      */
specifier|public
name|void
name|signDetached
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|signDetached
argument_list|(
name|file
argument_list|,
name|file
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Signs the given PDF file.      * @param inFile input PDF file      * @param outFile output PDF file      * @throws IOException if the input file could not be read      */
specifier|public
name|void
name|signDetached
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
name|signDetached
argument_list|(
name|inFile
argument_list|,
name|outFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Signs the given PDF file.      * @param inFile input PDF file      * @param outFile output PDF file      * @param tsaClient optional TSA client      * @throws IOException if the input file could not be read      */
specifier|public
name|void
name|signDetached
parameter_list|(
name|File
name|inFile
parameter_list|,
name|File
name|outFile
parameter_list|,
name|TSAClient
name|tsaClient
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
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
comment|// sign
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inFile
argument_list|)
decl_stmt|;
name|signDetached
argument_list|(
name|doc
argument_list|,
name|fos
argument_list|,
name|tsaClient
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|signDetached
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|OutputStream
name|output
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
literal|"Example User"
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setLocation
argument_list|(
literal|"Los Angeles, CA"
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setReason
argument_list|(
literal|"Testing"
argument_list|)
expr_stmt|;
comment|// TODO extract the above details from the signing certificate? Reason as a parameter?
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
name|document
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// write incremental (only for signing purpose)
name|document
operator|.
name|saveIncremental
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * We just extend CMS signed Data      *      * @param signedData -Generated CMS signed data      * @return CMSSignedData - Extended CMS signed data      */
annotation|@
name|Override
specifier|protected
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
argument_list|<
name|SignerInformation
argument_list|>
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
name|GeneralSecurityException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<
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
comment|// load the keystore
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"PKCS12"
argument_list|)
decl_stmt|;
name|char
index|[]
name|password
init|=
name|args
index|[
literal|1
index|]
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
comment|// TODO use Java 6 java.io.Console.readPassword
name|keystore
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|password
argument_list|)
expr_stmt|;
comment|// TODO alias command line argument
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
comment|// sign PDF
name|CreateSignature
name|signing
init|=
operator|new
name|CreateSignature
argument_list|(
name|keystore
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|File
name|inFile
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
literal|"_signed.pdf"
argument_list|)
decl_stmt|;
name|signing
operator|.
name|signDetached
argument_list|(
name|inFile
argument_list|,
name|outFile
argument_list|,
name|tsaClient
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
name|CreateSignature
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
literal|"<pkcs12_keystore><password><pdf_to_sign>\n"
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

