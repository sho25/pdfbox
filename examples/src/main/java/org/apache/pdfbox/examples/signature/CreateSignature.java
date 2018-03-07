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
name|NoSuchAlgorithmException
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
name|CertificateException
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
name|ExternalSigningSupport
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

begin_comment
comment|/**  * An example for signing a PDF with bouncy castle.  * A keystore can be created with the java keytool, for example:  *  * {@code keytool -genkeypair -storepass 123456 -storetype pkcs12 -alias test -validity 365  *        -v -keyalg RSA -keystore keystore.p12 }  *  * @author Thomas Chojecki  * @author Vakhtang Koroghlishvili  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|CreateSignature
extends|extends
name|CreateSignatureBase
block|{
comment|/**      * Initialize the signature creator with a keystore and certficate password.      *      * @param keystore the pkcs12 keystore containing the signing certificate      * @param pin the password for recovering the key      * @throws KeyStoreException if the keystore has not been initialized (loaded)      * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found      * @throws UnrecoverableKeyException if the given password is wrong      * @throws CertificateException if the certificate is not valid as signing time      * @throws IOException if no certificate could be found      */
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
throws|throws
name|KeyStoreException
throws|,
name|UnrecoverableKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
throws|,
name|IOException
block|{
name|super
argument_list|(
name|keystore
argument_list|,
name|pin
argument_list|)
expr_stmt|;
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
comment|/**      * Signs the given PDF file.      * @param inFile input PDF file      * @param outFile output PDF file      * @param tsaUrl optional TSA url      * @throws IOException if the input file could not be read      */
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
name|String
name|tsaUrl
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
name|setTsaUrl
argument_list|(
name|tsaUrl
argument_list|)
expr_stmt|;
comment|// sign
try|try
init|(
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
init|;
name|PDDocument
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inFile
argument_list|)
init|)
block|{
name|signDetached
argument_list|(
name|doc
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
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
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|accessPermissions
init|=
name|SigUtils
operator|.
name|getMDPPermission
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessPermissions
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No changes to the document are permitted due to DocMDP transform parameters dictionary"
argument_list|)
throw|;
block|}
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
comment|// Optional: certify
if|if
condition|(
name|accessPermissions
operator|==
literal|0
condition|)
block|{
name|SigUtils
operator|.
name|setMDPPermission
argument_list|(
name|document
argument_list|,
name|signature
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isExternalSigning
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sign externally..."
argument_list|)
expr_stmt|;
name|document
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|)
expr_stmt|;
name|ExternalSigningSupport
name|externalSigning
init|=
name|document
operator|.
name|saveIncrementalForExternalSigning
argument_list|(
name|output
argument_list|)
decl_stmt|;
comment|// invoke external signature service
name|byte
index|[]
name|cmsSignature
init|=
name|sign
argument_list|(
name|externalSigning
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
comment|// set signature bytes received from the service
name|externalSigning
operator|.
name|setSignature
argument_list|(
name|cmsSignature
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SignatureOptions
name|signatureOptions
init|=
operator|new
name|SignatureOptions
argument_list|()
decl_stmt|;
comment|// Size can vary, but should be enough for purpose.
name|signatureOptions
operator|.
name|setPreferredSignatureSize
argument_list|(
name|SignatureOptions
operator|.
name|DEFAULT_SIGNATURE_SIZE
operator|*
literal|2
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
argument_list|,
name|signatureOptions
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
name|boolean
name|externalSig
init|=
literal|false
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
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
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
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-e"
argument_list|)
condition|)
block|{
name|externalSig
operator|=
literal|true
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
name|signing
operator|.
name|setExternalSigning
argument_list|(
name|externalSig
argument_list|)
expr_stmt|;
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
name|tsaUrl
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
literal|"  -tsa<url>    sign timestamp using the given TSA server\n"
operator|+
literal|"  -e            sign using external signature creation scenario"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

