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
name|encryption
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
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|exceptions
operator|.
name|COSVisitorException
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
name|exceptions
operator|.
name|CryptographyException
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
name|encryption
operator|.
name|AccessPermission
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
name|PublicKeyDecryptionMaterial
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
name|PublicKeyProtectionPolicy
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
name|PublicKeyRecipient
import|;
end_import

begin_comment
comment|/**  * Tests for public key encryption.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|TestPublicKeyEncryption
extends|extends
name|TestCase
block|{
specifier|private
name|AccessPermission
name|permission1
decl_stmt|;
specifier|private
name|AccessPermission
name|permission2
decl_stmt|;
specifier|private
name|PublicKeyRecipient
name|recipient1
decl_stmt|;
specifier|private
name|PublicKeyRecipient
name|recipient2
decl_stmt|;
specifier|private
name|PublicKeyDecryptionMaterial
name|decryption1
decl_stmt|;
specifier|private
name|PublicKeyDecryptionMaterial
name|decryption2
decl_stmt|;
comment|/**      * Simple test document that gets encrypted by the test cases.      */
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|permission1
operator|=
operator|new
name|AccessPermission
argument_list|()
expr_stmt|;
name|permission1
operator|.
name|setCanAssembleDocument
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanExtractContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanExtractForAccessibility
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanFillInForm
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanModify
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanModifyAnnotations
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanPrint
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission1
operator|.
name|setCanPrintDegraded
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|=
operator|new
name|AccessPermission
argument_list|()
expr_stmt|;
name|permission2
operator|.
name|setCanAssembleDocument
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanExtractContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanExtractForAccessibility
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanFillInForm
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanModify
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanModifyAnnotations
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission2
operator|.
name|setCanPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// it is true now !
name|permission2
operator|.
name|setCanPrintDegraded
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|recipient1
operator|=
name|getRecipient
argument_list|(
literal|"test1.der"
argument_list|,
name|permission1
argument_list|)
expr_stmt|;
name|recipient2
operator|=
name|getRecipient
argument_list|(
literal|"test2.der"
argument_list|,
name|permission2
argument_list|)
expr_stmt|;
name|decryption1
operator|=
name|getDecryptionMaterial
argument_list|(
literal|"test1.pfx"
argument_list|,
literal|"test1"
argument_list|)
expr_stmt|;
name|decryption2
operator|=
name|getDecryptionMaterial
argument_list|(
literal|"test2.pfx"
argument_list|,
literal|"test2"
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
name|TestPublicKeyEncryption
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test.pdf"
argument_list|)
decl_stmt|;
try|try
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Protect a document with certificate 1 and try to open it with      * certificate 2 and catch the exception.      *      * @throws Exception If there is an unexpected error during the test.      */
specifier|public
name|void
name|testProtectionError
parameter_list|()
throws|throws
name|Exception
block|{
name|PublicKeyProtectionPolicy
name|policy
init|=
operator|new
name|PublicKeyProtectionPolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|addRecipient
argument_list|(
name|recipient1
argument_list|)
expr_stmt|;
name|document
operator|.
name|protect
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|PDDocument
name|encrypted
init|=
name|reload
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|encrypted
operator|.
name|isEncrypted
argument_list|()
argument_list|)
expr_stmt|;
name|encrypted
operator|.
name|openProtection
argument_list|(
name|decryption2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No exception when using an incorrect decryption key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CryptographyException
name|expected
parameter_list|)
block|{
comment|// do nothing
block|}
finally|finally
block|{
name|encrypted
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Protect a document with a public certificate and try to open it      * with the corresponding private certificate.      *      * @throws Exception If there is an unexpected error during the test.      */
specifier|public
name|void
name|testProtection
parameter_list|()
throws|throws
name|Exception
block|{
name|PublicKeyProtectionPolicy
name|policy
init|=
operator|new
name|PublicKeyProtectionPolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|addRecipient
argument_list|(
name|recipient1
argument_list|)
expr_stmt|;
name|document
operator|.
name|protect
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|PDDocument
name|encrypted
init|=
name|reload
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|encrypted
operator|.
name|isEncrypted
argument_list|()
argument_list|)
expr_stmt|;
name|encrypted
operator|.
name|openProtection
argument_list|(
name|decryption1
argument_list|)
expr_stmt|;
name|AccessPermission
name|permission
init|=
name|encrypted
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canAssembleDocument
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canExtractContent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|permission
operator|.
name|canExtractForAccessibility
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canFillInForm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModify
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModifyAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canPrint
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canPrintDegraded
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|encrypted
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Protect the document for 2 recipients and try to open it.      *      * @throws Exception If there is an error during the test.      */
specifier|public
name|void
name|testMultipleRecipients
parameter_list|()
throws|throws
name|Exception
block|{
name|PublicKeyProtectionPolicy
name|policy
init|=
operator|new
name|PublicKeyProtectionPolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|addRecipient
argument_list|(
name|recipient1
argument_list|)
expr_stmt|;
name|policy
operator|.
name|addRecipient
argument_list|(
name|recipient2
argument_list|)
expr_stmt|;
name|document
operator|.
name|protect
argument_list|(
name|policy
argument_list|)
expr_stmt|;
comment|// open first time
name|PDDocument
name|encrypted1
init|=
name|reload
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|encrypted1
operator|.
name|openProtection
argument_list|(
name|decryption1
argument_list|)
expr_stmt|;
name|AccessPermission
name|permission
init|=
name|encrypted1
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canAssembleDocument
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canExtractContent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|permission
operator|.
name|canExtractForAccessibility
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canFillInForm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModify
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModifyAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canPrint
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canPrintDegraded
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|encrypted1
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// open second time
name|PDDocument
name|encrypted2
init|=
name|reload
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|encrypted2
operator|.
name|openProtection
argument_list|(
name|decryption2
argument_list|)
expr_stmt|;
name|AccessPermission
name|permission
init|=
name|encrypted2
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canAssembleDocument
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canExtractContent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|permission
operator|.
name|canExtractForAccessibility
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canFillInForm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModify
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canModifyAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|permission
operator|.
name|canPrint
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|permission
operator|.
name|canPrintDegraded
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|encrypted2
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Reloads the given document by writing it to a temporary byte array      * and loading a fresh document from that byte array.      *      * @param document input document      * @return reloaded document      * @throws Exception if       */
specifier|private
name|PDDocument
name|reload
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
try|try
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
return|return
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected failure"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|COSVisitorException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected failure"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a recipient specification with the given access permissions      * and an X.509 certificate read from the given classpath resource.      *      * @param certificate X.509 certificate resource, relative to this class      * @param permission access permissions      * @return recipient specification      * @throws Exception if the certificate could not be read      */
specifier|private
name|PublicKeyRecipient
name|getRecipient
parameter_list|(
name|String
name|certificate
parameter_list|,
name|AccessPermission
name|permission
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|input
init|=
name|TestPublicKeyEncryption
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|certificate
argument_list|)
decl_stmt|;
try|try
block|{
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
name|PublicKeyRecipient
name|recipient
init|=
operator|new
name|PublicKeyRecipient
argument_list|()
decl_stmt|;
name|recipient
operator|.
name|setPermission
argument_list|(
name|permission
argument_list|)
expr_stmt|;
name|recipient
operator|.
name|setX509
argument_list|(
operator|(
name|X509Certificate
operator|)
name|factory
operator|.
name|generateCertificate
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|recipient
return|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|PublicKeyDecryptionMaterial
name|getDecryptionMaterial
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|input
init|=
name|TestPublicKeyEncryption
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
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
name|keystore
operator|.
name|load
argument_list|(
name|input
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|PublicKeyDecryptionMaterial
argument_list|(
name|keystore
argument_list|,
literal|null
argument_list|,
name|password
argument_list|)
return|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

