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
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
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
name|PDDocumentNameDictionary
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
name|PDEmbeddedFilesNameTreeNode
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
name|common
operator|.
name|COSObjectable
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
name|common
operator|.
name|filespecification
operator|.
name|PDComplexFileSpecification
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
name|common
operator|.
name|filespecification
operator|.
name|PDEmbeddedFile
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
name|StandardProtectionPolicy
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
name|graphics
operator|.
name|image
operator|.
name|ValidateXImage
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
name|rendering
operator|.
name|PDFRenderer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Tests for symmetric key encryption.  *  * IMPORTANT! When making changes in the encryption / decryption methods, do  * also check whether the six generated encrypted files (to be found in  * pdfbox/target/test-output/crypto and named *encrypted.pdf) can be opened with  * Adobe Reader by providing the owner password and the user password.  *  * @author Ralf Hauser  * @author Tilman Hausherr  *  */
end_comment

begin_class
specifier|public
class|class
name|TestSymmetricKeyEncryption
extends|extends
name|TestCase
block|{
comment|/**      * Logger instance.      */
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
name|TestSymmetricKeyEncryption
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|File
name|testResultsDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output/crypto"
argument_list|)
decl_stmt|;
specifier|private
name|AccessPermission
name|permission
decl_stmt|;
specifier|static
specifier|final
name|String
name|USERPASSWORD
init|=
literal|"1234567890abcdefghijk1234567890abcdefghijk"
decl_stmt|;
specifier|static
specifier|final
name|String
name|OWNERPASSWORD
init|=
literal|"abcdefghijk1234567890abcdefghijk1234567890"
decl_stmt|;
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|testResultsDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
name|Cipher
operator|.
name|getMaxAllowedKeyLength
argument_list|(
literal|"AES"
argument_list|)
operator|!=
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
comment|// we need strong encryption for these tests
name|fail
argument_list|(
literal|"JCE unlimited strength jurisdiction policy files are not installed"
argument_list|)
expr_stmt|;
block|}
name|permission
operator|=
operator|new
name|AccessPermission
argument_list|()
expr_stmt|;
name|permission
operator|.
name|setCanAssembleDocument
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanExtractContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanExtractForAccessibility
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanFillInForm
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanModify
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanModifyAnnotations
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setCanPrintDegraded
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|permission
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test that permissions work as intended: the user psw ("user") is enough      * to open the PDF with possibly restricted rights, the owner psw ("owner")      * gives full permissions. The 3 files of this test were created by Maruan      * Sayhoun, NOT with PDFBox, but with Adobe Acrobat to ensure "the gold      * standard". The restricted permissions prevent printing and text      * extraction. In the 128 and 256 bit encrypted files, AssembleDocument,      * ExtractForAccessibility and PrintDegraded are also disabled.      */
specifier|public
name|void
name|testPermissions
parameter_list|()
throws|throws
name|Exception
block|{
name|AccessPermission
name|fullAP
init|=
operator|new
name|AccessPermission
argument_list|()
decl_stmt|;
name|AccessPermission
name|restrAP
init|=
operator|new
name|AccessPermission
argument_list|()
decl_stmt|;
name|restrAP
operator|.
name|setCanPrint
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|restrAP
operator|.
name|setCanExtractContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|restrAP
operator|.
name|setCanModify
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|byte
index|[]
name|inputFileAsByteArray
init|=
name|getFileResourceAsByteArray
argument_list|(
literal|"PasswordSample-40bit.pdf"
argument_list|)
decl_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"owner"
argument_list|,
name|fullAP
argument_list|)
expr_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"user"
argument_list|,
name|restrAP
argument_list|)
expr_stmt|;
try|try
block|{
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"wrong password not detected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot decrypt PDF, the password is incorrect"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|restrAP
operator|.
name|setCanAssembleDocument
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|restrAP
operator|.
name|setCanExtractForAccessibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|restrAP
operator|.
name|setCanPrintDegraded
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|inputFileAsByteArray
operator|=
name|getFileResourceAsByteArray
argument_list|(
literal|"PasswordSample-128bit.pdf"
argument_list|)
expr_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"owner"
argument_list|,
name|fullAP
argument_list|)
expr_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"user"
argument_list|,
name|restrAP
argument_list|)
expr_stmt|;
try|try
block|{
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"wrong password not detected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot decrypt PDF, the password is incorrect"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|inputFileAsByteArray
operator|=
name|getFileResourceAsByteArray
argument_list|(
literal|"PasswordSample-256bit.pdf"
argument_list|)
expr_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"owner"
argument_list|,
name|fullAP
argument_list|)
expr_stmt|;
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|"user"
argument_list|,
name|restrAP
argument_list|)
expr_stmt|;
try|try
block|{
name|checkPerms
argument_list|(
name|inputFileAsByteArray
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"wrong password not detected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot decrypt PDF, the password is incorrect"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkPerms
parameter_list|(
name|byte
index|[]
name|inputFileAsByteArray
parameter_list|,
name|String
name|password
parameter_list|,
name|AccessPermission
name|expectedPermissions
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputFileAsByteArray
argument_list|)
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|AccessPermission
name|currentAccessPermission
init|=
name|doc
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
comment|// check permissions
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|isOwnerPermission
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|isOwnerPermission
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|isReadOnly
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canAssembleDocument
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canAssembleDocument
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canExtractContent
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canExtractContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canExtractForAccessibility
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canExtractForAccessibility
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canFillInForm
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canFillInForm
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canModify
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canModify
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canModifyAnnotations
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canModifyAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canPrint
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canPrint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPermissions
operator|.
name|canPrintDegraded
argument_list|()
argument_list|,
name|currentAccessPermission
operator|.
name|canPrintDegraded
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|PDFRenderer
argument_list|(
name|doc
argument_list|)
operator|.
name|renderImage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Protect a document with a key and try to reopen it with that key and      * compare.      *      * @throws Exception If there is an unexpected error during the test.      */
specifier|public
name|void
name|testProtection
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|inputFileAsByteArray
init|=
name|getFileResourceAsByteArray
argument_list|(
literal|"Acroform-PDFBOX-2333.pdf"
argument_list|)
decl_stmt|;
name|int
name|sizePriorToEncryption
init|=
name|inputFileAsByteArray
operator|.
name|length
decl_stmt|;
name|testSymmEncrForKeySize
argument_list|(
literal|40
argument_list|,
name|sizePriorToEncryption
argument_list|,
name|inputFileAsByteArray
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|,
name|permission
argument_list|)
expr_stmt|;
name|testSymmEncrForKeySize
argument_list|(
literal|128
argument_list|,
name|sizePriorToEncryption
argument_list|,
name|inputFileAsByteArray
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|,
name|permission
argument_list|)
expr_stmt|;
name|testSymmEncrForKeySize
argument_list|(
literal|256
argument_list|,
name|sizePriorToEncryption
argument_list|,
name|inputFileAsByteArray
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|,
name|permission
argument_list|)
expr_stmt|;
block|}
comment|/**      * Protect a document with an embedded PDF with a key and try to reopen it      * with that key and compare.      *      * @throws Exception If there is an unexpected error during the test.      */
specifier|public
name|void
name|testProtectionInnerAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testFileName
init|=
literal|"preEnc_20141025_105451.pdf"
decl_stmt|;
name|byte
index|[]
name|inputFileWithEmbeddedFileAsByteArray
init|=
name|getFileResourceAsByteArray
argument_list|(
name|testFileName
argument_list|)
decl_stmt|;
name|int
name|sizeOfFileWithEmbeddedFile
init|=
name|inputFileWithEmbeddedFileAsByteArray
operator|.
name|length
decl_stmt|;
name|File
name|extractedEmbeddedFile
init|=
name|extractEmbeddedFile
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputFileWithEmbeddedFileAsByteArray
argument_list|)
argument_list|,
literal|"innerFile.pdf"
argument_list|)
decl_stmt|;
name|testSymmEncrForKeySizeInner
argument_list|(
literal|40
argument_list|,
name|sizeOfFileWithEmbeddedFile
argument_list|,
name|inputFileWithEmbeddedFileAsByteArray
argument_list|,
name|extractedEmbeddedFile
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|)
expr_stmt|;
name|testSymmEncrForKeySizeInner
argument_list|(
literal|128
argument_list|,
name|sizeOfFileWithEmbeddedFile
argument_list|,
name|inputFileWithEmbeddedFileAsByteArray
argument_list|,
name|extractedEmbeddedFile
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|)
expr_stmt|;
name|testSymmEncrForKeySizeInner
argument_list|(
literal|256
argument_list|,
name|sizeOfFileWithEmbeddedFile
argument_list|,
name|inputFileWithEmbeddedFileAsByteArray
argument_list|,
name|extractedEmbeddedFile
argument_list|,
name|USERPASSWORD
argument_list|,
name|OWNERPASSWORD
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testSymmEncrForKeySize
parameter_list|(
name|int
name|keyLength
parameter_list|,
name|int
name|sizePriorToEncr
parameter_list|,
name|byte
index|[]
name|inputFileAsByteArray
parameter_list|,
name|String
name|userpassword
parameter_list|,
name|String
name|ownerpassword
parameter_list|,
name|AccessPermission
name|permission
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputFileAsByteArray
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
literal|"Simple-"
decl_stmt|;
name|int
name|numSrcPages
init|=
name|document
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|PDFRenderer
name|pdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|BufferedImage
argument_list|>
name|srcImgTab
init|=
operator|new
name|ArrayList
argument_list|<
name|BufferedImage
argument_list|>
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|ByteArrayOutputStream
argument_list|>
name|srcContentStreamTab
init|=
operator|new
name|ArrayList
argument_list|<
name|ByteArrayOutputStream
argument_list|>
argument_list|()
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
name|numSrcPages
condition|;
operator|++
name|i
control|)
block|{
name|srcImgTab
operator|.
name|add
argument_list|(
name|pdfRenderer
operator|.
name|renderImage
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|COSStream
name|contentStream
init|=
name|document
operator|.
name|getPage
argument_list|(
name|i
argument_list|)
operator|.
name|getContentStream
argument_list|()
decl_stmt|;
name|InputStream
name|unfilteredStream
init|=
name|contentStream
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|unfilteredStream
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|unfilteredStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|srcContentStreamTab
operator|.
name|add
argument_list|(
name|baos
argument_list|)
expr_stmt|;
block|}
name|PDDocument
name|encryptedDoc
init|=
name|encrypt
argument_list|(
name|keyLength
argument_list|,
name|sizePriorToEncr
argument_list|,
name|document
argument_list|,
name|prefix
argument_list|,
name|permission
argument_list|,
name|userpassword
argument_list|,
name|ownerpassword
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|numSrcPages
argument_list|,
name|encryptedDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|pdfRenderer
operator|=
operator|new
name|PDFRenderer
argument_list|(
name|encryptedDoc
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|encryptedDoc
operator|.
name|getNumberOfPages
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
comment|// compare rendering
name|BufferedImage
name|bim
init|=
name|pdfRenderer
operator|.
name|renderImage
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|ValidateXImage
operator|.
name|checkIdent
argument_list|(
name|bim
argument_list|,
name|srcImgTab
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
comment|// compare content streams
name|COSStream
name|contentStreamDecr
init|=
name|encryptedDoc
operator|.
name|getPage
argument_list|(
name|i
argument_list|)
operator|.
name|getContentStream
argument_list|()
decl_stmt|;
name|InputStream
name|unfilteredStream
init|=
name|contentStreamDecr
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|unfilteredStream
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|unfilteredStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
literal|"content stream of page "
operator|+
name|i
operator|+
literal|" not identical"
argument_list|,
name|srcContentStreamTab
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
name|prefix
operator|+
name|keyLength
operator|+
literal|"-bit-decrypted.pdf"
argument_list|)
decl_stmt|;
name|encryptedDoc
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|encryptedDoc
operator|.
name|save
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
name|encryptedDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// encrypt with keylength and permission, save, check sizes before and after encryption
comment|// reopen, decrypt and return document
specifier|private
name|PDDocument
name|encrypt
parameter_list|(
name|int
name|keyLength
parameter_list|,
name|int
name|sizePriorToEncr
parameter_list|,
name|PDDocument
name|doc
parameter_list|,
name|String
name|prefix
parameter_list|,
name|AccessPermission
name|permission
parameter_list|,
name|String
name|userpassword
parameter_list|,
name|String
name|ownerpassword
parameter_list|)
throws|throws
name|IOException
block|{
name|AccessPermission
name|ap
init|=
operator|new
name|AccessPermission
argument_list|()
decl_stmt|;
name|StandardProtectionPolicy
name|spp
init|=
operator|new
name|StandardProtectionPolicy
argument_list|(
name|ownerpassword
argument_list|,
name|userpassword
argument_list|,
name|ap
argument_list|)
decl_stmt|;
name|spp
operator|.
name|setEncryptionKeyLength
argument_list|(
name|keyLength
argument_list|)
expr_stmt|;
name|spp
operator|.
name|setPermissions
argument_list|(
name|permission
argument_list|)
expr_stmt|;
name|doc
operator|.
name|protect
argument_list|(
name|spp
argument_list|)
expr_stmt|;
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
name|prefix
operator|+
name|keyLength
operator|+
literal|"-bit-encrypted.pdf"
argument_list|)
decl_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
name|long
name|sizeEncrypted
init|=
name|pdfFile
operator|.
name|length
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|keyLength
operator|+
literal|"-bit encrypted pdf should not have same size as plain one"
argument_list|,
name|sizeEncrypted
operator|!=
name|sizePriorToEncr
argument_list|)
expr_stmt|;
name|PDDocument
name|encryptedDoc
decl_stmt|;
comment|// test with owner password => full permissions
name|encryptedDoc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|,
name|ownerpassword
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|encryptedDoc
operator|.
name|isEncrypted
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|encryptedDoc
operator|.
name|getCurrentAccessPermission
argument_list|()
operator|.
name|isOwnerPermission
argument_list|()
argument_list|)
expr_stmt|;
name|encryptedDoc
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// test with owner password => restricted permissions
name|encryptedDoc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|,
name|userpassword
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|encryptedDoc
operator|.
name|isEncrypted
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|encryptedDoc
operator|.
name|getCurrentAccessPermission
argument_list|()
operator|.
name|isOwnerPermission
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|permission
operator|.
name|getPermissionBytes
argument_list|()
argument_list|,
name|encryptedDoc
operator|.
name|getCurrentAccessPermission
argument_list|()
operator|.
name|getPermissionBytes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|encryptedDoc
return|;
block|}
comment|// extract the embedded file, saves it, and return the extracted saved file
specifier|private
name|File
name|extractEmbeddedFile
parameter_list|(
name|InputStream
name|pdfInputStream
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|docWithEmbeddedFile
decl_stmt|;
name|docWithEmbeddedFile
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfInputStream
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|docWithEmbeddedFile
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentNameDictionary
name|names
init|=
name|catalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|PDEmbeddedFilesNameTreeNode
name|embeddedFiles
init|=
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
name|embeddedFileNames
init|=
name|embeddedFiles
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|embeddedFileNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
name|entry
init|=
name|embeddedFileNames
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Processing embedded file "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|PDComplexFileSpecification
name|complexFileSpec
init|=
operator|(
name|PDComplexFileSpecification
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|PDEmbeddedFile
name|embeddedFile
init|=
name|complexFileSpec
operator|.
name|getEmbeddedFile
argument_list|()
decl_stmt|;
name|File
name|resultFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|resultFile
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|embeddedFile
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|fos
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  size: "
operator|+
name|embeddedFile
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|embeddedFile
operator|.
name|getSize
argument_list|()
argument_list|,
name|resultFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resultFile
return|;
block|}
specifier|private
name|void
name|testSymmEncrForKeySizeInner
parameter_list|(
name|int
name|keyLength
parameter_list|,
name|int
name|sizePriorToEncr
parameter_list|,
name|byte
index|[]
name|inputFileWithEmbeddedFileAsByteArray
parameter_list|,
name|File
name|embeddedFilePriorToEncryption
parameter_list|,
name|String
name|userpassword
parameter_list|,
name|String
name|ownerpassword
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|inputFileWithEmbeddedFileAsByteArray
argument_list|)
argument_list|)
decl_stmt|;
name|PDDocument
name|encryptedDoc
init|=
name|encrypt
argument_list|(
name|keyLength
argument_list|,
name|sizePriorToEncr
argument_list|,
name|document
argument_list|,
literal|"ContainsEmbedded-"
argument_list|,
name|permission
argument_list|,
name|userpassword
argument_list|,
name|ownerpassword
argument_list|)
decl_stmt|;
name|File
name|decryptedFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"DecryptedContainsEmbedded-"
operator|+
name|keyLength
operator|+
literal|"-bit.pdf"
argument_list|)
decl_stmt|;
name|encryptedDoc
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|encryptedDoc
operator|.
name|save
argument_list|(
name|decryptedFile
argument_list|)
expr_stmt|;
name|File
name|extractedEmbeddedFile
init|=
name|extractEmbeddedFile
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|decryptedFile
argument_list|)
argument_list|,
literal|"decryptedInnerFile-"
operator|+
name|keyLength
operator|+
literal|"-bit.pdf"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|keyLength
operator|+
literal|"-bit decrypted inner attachment pdf should have same size as plain one"
argument_list|,
name|embeddedFilePriorToEncryption
operator|.
name|length
argument_list|()
argument_list|,
name|extractedEmbeddedFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// compare the two embedded files
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|getFileAsByteArray
argument_list|(
name|embeddedFilePriorToEncryption
argument_list|)
argument_list|,
name|getFileAsByteArray
argument_list|(
name|extractedEmbeddedFile
argument_list|)
argument_list|)
expr_stmt|;
name|encryptedDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|byte
index|[]
name|getStreamAsByteArray
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|private
name|byte
index|[]
name|getFileResourceAsByteArray
parameter_list|(
name|String
name|testFileName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getStreamAsByteArray
argument_list|(
name|TestSymmetricKeyEncryption
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|testFileName
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|getFileAsByteArray
parameter_list|(
name|File
name|f
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getStreamAsByteArray
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

