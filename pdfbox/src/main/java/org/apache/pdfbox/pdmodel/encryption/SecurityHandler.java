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
name|pdmodel
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
name|InvalidAlgorithmParameterException
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
name|SecureRandom
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
name|Iterator
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
name|Map
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
name|crypto
operator|.
name|BadPaddingException
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
name|javax
operator|.
name|crypto
operator|.
name|CipherInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|IllegalBlockSizeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|NoSuchPaddingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|IvParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|COSDocument
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

begin_comment
comment|/**  * A security handler as described in the PDF specifications.  * A security handler is responsible of documents protection.  *  * @author Ben Litchfield  * @author Benoit Guillon  * @author Manuel Kasper  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SecurityHandler
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_KEY_LENGTH
init|=
literal|40
decl_stmt|;
comment|// see 7.6.2, page 58, PDF 32000-1:2008
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|AES_SALT
init|=
block|{
operator|(
name|byte
operator|)
literal|0x73
block|,
operator|(
name|byte
operator|)
literal|0x41
block|,
operator|(
name|byte
operator|)
literal|0x6c
block|,
operator|(
name|byte
operator|)
literal|0x54
block|}
decl_stmt|;
comment|/** The value of V field of the Encryption dictionary. */
specifier|protected
name|int
name|version
decl_stmt|;
comment|/** The length of the secret key used to encrypt the document. */
specifier|protected
name|int
name|keyLength
init|=
name|DEFAULT_KEY_LENGTH
decl_stmt|;
comment|/** The encryption key that will used to encrypt / decrypt.*/
specifier|protected
name|byte
index|[]
name|encryptionKey
decl_stmt|;
comment|/** The document whose security is handled by this security handler.*/
specifier|protected
name|PDDocument
name|document
decl_stmt|;
comment|/** The RC4 implementation used for cryptographic functions. */
specifier|protected
name|RC4Cipher
name|rc4
init|=
operator|new
name|RC4Cipher
argument_list|()
decl_stmt|;
comment|/** indicates if the Metadata have to be decrypted of not */
specifier|protected
name|boolean
name|decryptMetadata
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|COSBase
argument_list|>
name|objects
init|=
operator|new
name|HashSet
argument_list|<
name|COSBase
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|COSDictionary
argument_list|>
name|potentialSignatures
init|=
operator|new
name|HashSet
argument_list|<
name|COSDictionary
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|useAES
decl_stmt|;
comment|/**      * The access permission granted to the current user for the document. These      * permissions are computed during decryption and are in read only mode.      */
specifier|protected
name|AccessPermission
name|currentAccessPermission
init|=
literal|null
decl_stmt|;
comment|/**      * Prepare the document for encryption.      *      * @param doc The document that will be encrypted.      *      * @throws IOException If there is an error with the document.      */
specifier|public
specifier|abstract
name|void
name|prepareDocumentForEncryption
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Prepares everything to decrypt the document.      *       * If {@link #decryptDocument(PDDocument, DecryptionMaterial)} is used, this method is      * called from there. Only if decryption of single objects is needed this should be called instead.      *      * @param encryption  encryption dictionary, can be retrieved via {@link PDDocument#getEncryption()}      * @param documentIDArray  document id which is returned via {@link COSDocument#getDocumentID()}      * @param decryptionMaterial Information used to decrypt the document.      *      * @throws IOException If there is an error accessing data.      */
specifier|public
specifier|abstract
name|void
name|prepareForDecryption
parameter_list|(
name|PDEncryption
name|encryption
parameter_list|,
name|COSArray
name|documentIDArray
parameter_list|,
name|DecryptionMaterial
name|decryptionMaterial
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Prepare the document for decryption.      *      * @param doc The document to decrypt.      * @param mat Information required to decrypt the document.      * @throws IOException If there is an error with the document.      */
specifier|public
specifier|abstract
name|void
name|decryptDocument
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|DecryptionMaterial
name|mat
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * This method must be called by an implementation of this class to really proceed      * to decryption.      *      * @throws IOException If there is an error in the decryption.      */
specifier|protected
name|void
name|proceedDecryption
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|trailer
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|trailer
operator|.
name|getObjectFromPath
argument_list|(
literal|"Root/AcroForm/Fields"
argument_list|)
decl_stmt|;
comment|// We need to collect all the signature dictionaries, for some
comment|// reason the 'Contents' entry of signatures is not really encrypted
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|field
init|=
operator|(
name|COSDictionary
operator|)
name|fields
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|!=
literal|null
condition|)
block|{
name|addDictionaryAndSubDictionary
argument_list|(
name|potentialSignatures
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not decypt document, object not found."
argument_list|)
throw|;
block|}
block|}
block|}
name|List
argument_list|<
name|COSObject
argument_list|>
name|allObjects
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getObjects
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|COSObject
argument_list|>
name|objectIter
init|=
name|allObjects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|COSDictionary
name|encryptionDict
init|=
name|document
operator|.
name|getEncryption
argument_list|()
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
while|while
condition|(
name|objectIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSObject
name|nextObj
init|=
name|objectIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|COSBase
name|nextCOSBase
init|=
name|nextObj
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|boolean
name|isSignatureDictionary
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|nextCOSBase
operator|instanceof
name|COSDictionary
condition|)
block|{
name|isSignatureDictionary
operator|=
name|COSName
operator|.
name|SIG
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|COSDictionary
operator|)
name|nextCOSBase
operator|)
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isSignatureDictionary
operator|&&
name|nextCOSBase
operator|!=
name|encryptionDict
condition|)
block|{
name|decryptObject
argument_list|(
name|nextObj
argument_list|)
expr_stmt|;
block|}
block|}
name|document
operator|.
name|setEncryptionDictionary
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addDictionaryAndSubDictionary
parameter_list|(
name|Set
argument_list|<
name|COSDictionary
argument_list|>
name|set
parameter_list|,
name|COSDictionary
name|dic
parameter_list|)
block|{
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
comment|// in case dictionary is part of object stream we have null value here
block|{
name|set
operator|.
name|add
argument_list|(
name|dic
argument_list|)
expr_stmt|;
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|kids
operator|!=
literal|null
operator|&&
name|i
operator|<
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|addDictionaryAndSubDictionary
argument_list|(
name|set
argument_list|,
operator|(
name|COSDictionary
operator|)
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|COSBase
name|value
init|=
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|COSDictionary
condition|)
block|{
name|addDictionaryAndSubDictionary
argument_list|(
name|set
argument_list|,
operator|(
name|COSDictionary
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Encrypt a set of data.      *      * @param objectNumber The data object number.      * @param genNumber The data generation number.      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      * @throws IOException If there is an error reading the data.      * @deprecated While this works fine for RC4 encryption, it will never decrypt AES data      *             You should use encryptData(objectNumber, genNumber, data, output, decrypt)      *             which can do everything.  This function is just here for compatibility      *             reasons and will be removed in the future.      */
specifier|public
name|void
name|encryptData
parameter_list|(
name|long
name|objectNumber
parameter_list|,
name|long
name|genNumber
parameter_list|,
name|InputStream
name|data
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
comment|// default to encrypting since the function is named "encryptData"
name|encryptData
argument_list|(
name|objectNumber
argument_list|,
name|genNumber
argument_list|,
name|data
argument_list|,
name|output
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Encrypt a set of data.      *      * @param objectNumber The data object number.      * @param genNumber The data generation number.      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      * @param decrypt true to decrypt the data, false to encrypt it      *      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|encryptData
parameter_list|(
name|long
name|objectNumber
parameter_list|,
name|long
name|genNumber
parameter_list|,
name|InputStream
name|data
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|boolean
name|decrypt
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Determine whether we're using Algorithm 1 (for RC4 and AES-128), or 1.A (for AES-256)
if|if
condition|(
name|useAES
operator|&&
name|encryptionKey
operator|.
name|length
operator|==
literal|32
condition|)
block|{
name|byte
index|[]
name|iv
init|=
operator|new
name|byte
index|[
literal|16
index|]
decl_stmt|;
if|if
condition|(
name|decrypt
condition|)
block|{
comment|// read IV from stream
name|data
operator|.
name|read
argument_list|(
name|iv
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// generate random IV and write to stream
name|SecureRandom
name|rnd
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
name|rnd
operator|.
name|nextBytes
argument_list|(
name|iv
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|iv
argument_list|)
expr_stmt|;
block|}
name|Cipher
name|cipher
decl_stmt|;
try|try
block|{
name|cipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES/CBC/PKCS5Padding"
argument_list|)
expr_stmt|;
name|SecretKeySpec
name|keySpec
init|=
operator|new
name|SecretKeySpec
argument_list|(
name|encryptionKey
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|IvParameterSpec
name|ivSpec
init|=
operator|new
name|IvParameterSpec
argument_list|(
name|iv
argument_list|)
decl_stmt|;
name|cipher
operator|.
name|init
argument_list|(
name|decrypt
condition|?
name|Cipher
operator|.
name|DECRYPT_MODE
else|:
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|keySpec
argument_list|,
name|ivSpec
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
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
name|CipherInputStream
name|cis
init|=
operator|new
name|CipherInputStream
argument_list|(
name|data
argument_list|,
name|cipher
argument_list|)
decl_stmt|;
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|cis
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|useAES
operator|&&
operator|!
name|decrypt
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AES encryption with key length other than 256 bits is not yet implemented."
argument_list|)
throw|;
block|}
name|byte
index|[]
name|newKey
init|=
operator|new
name|byte
index|[
name|encryptionKey
operator|.
name|length
operator|+
literal|5
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|encryptionKey
argument_list|,
literal|0
argument_list|,
name|newKey
argument_list|,
literal|0
argument_list|,
name|encryptionKey
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// PDF 1.4 reference pg 73
comment|// step 1
comment|// we have the reference
comment|// step 2
name|newKey
index|[
name|newKey
operator|.
name|length
operator|-
literal|5
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|objectNumber
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|newKey
index|[
name|newKey
operator|.
name|length
operator|-
literal|4
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|objectNumber
operator|>>
literal|8
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|newKey
index|[
name|newKey
operator|.
name|length
operator|-
literal|3
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|objectNumber
operator|>>
literal|16
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|newKey
index|[
name|newKey
operator|.
name|length
operator|-
literal|2
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|genNumber
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|newKey
index|[
name|newKey
operator|.
name|length
operator|-
literal|1
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|genNumber
operator|>>
literal|8
operator|&
literal|0xff
argument_list|)
expr_stmt|;
comment|// step 3
name|MessageDigest
name|md
init|=
name|MessageDigests
operator|.
name|getMD5
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|newKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|useAES
condition|)
block|{
name|md
operator|.
name|update
argument_list|(
name|AES_SALT
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|digestedKey
init|=
name|md
operator|.
name|digest
argument_list|()
decl_stmt|;
comment|// step 4
name|int
name|length
init|=
name|Math
operator|.
name|min
argument_list|(
name|newKey
operator|.
name|length
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|byte
index|[]
name|finalKey
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|digestedKey
argument_list|,
literal|0
argument_list|,
name|finalKey
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|useAES
condition|)
block|{
name|byte
index|[]
name|iv
init|=
operator|new
name|byte
index|[
literal|16
index|]
decl_stmt|;
name|data
operator|.
name|read
argument_list|(
name|iv
argument_list|)
expr_stmt|;
try|try
block|{
name|Cipher
name|decryptCipher
decl_stmt|;
try|try
block|{
name|decryptCipher
operator|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES/CBC/PKCS5Padding"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// should never happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|SecretKey
name|aesKey
init|=
operator|new
name|SecretKeySpec
argument_list|(
name|finalKey
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|IvParameterSpec
name|ips
init|=
operator|new
name|IvParameterSpec
argument_list|(
name|iv
argument_list|)
decl_stmt|;
name|decryptCipher
operator|.
name|init
argument_list|(
name|decrypt
condition|?
name|Cipher
operator|.
name|DECRYPT_MODE
else|:
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|aesKey
argument_list|,
name|ips
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
operator|-
literal|1
operator|!=
operator|(
name|n
operator|=
name|data
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
condition|;
control|)
block|{
if|if
condition|(
name|data
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|decryptCipher
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|write
argument_list|(
name|decryptCipher
operator|.
name|doFinal
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
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
catch|catch
parameter_list|(
name|InvalidAlgorithmParameterException
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
catch|catch
parameter_list|(
name|NoSuchPaddingException
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
catch|catch
parameter_list|(
name|IllegalBlockSizeException
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
catch|catch
parameter_list|(
name|BadPaddingException
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
else|else
block|{
name|rc4
operator|.
name|setKey
argument_list|(
name|finalKey
argument_list|)
expr_stmt|;
name|rc4
operator|.
name|write
argument_list|(
name|data
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will decrypt an object in the document.      *      * @param object The object to decrypt.      *      * @throws IOException If there is an error getting the stream data.      */
specifier|private
name|void
name|decryptObject
parameter_list|(
name|COSObject
name|object
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|objNum
init|=
name|object
operator|.
name|getObjectNumber
argument_list|()
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|long
name|genNum
init|=
name|object
operator|.
name|getGenerationNumber
argument_list|()
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|COSBase
name|base
init|=
name|object
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|decrypt
argument_list|(
name|base
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will dispatch to the correct method.      *      * @param obj The object to decrypt.      * @param objNum The object number.      * @param genNum The object generation Number.      *      * @throws IOException If there is an error getting the stream data.      */
specifier|private
name|void
name|decrypt
parameter_list|(
name|COSBase
name|obj
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|objects
operator|.
name|contains
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|objects
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSString
condition|)
block|{
name|decryptString
argument_list|(
operator|(
name|COSString
operator|)
name|obj
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSStream
condition|)
block|{
name|decryptStream
argument_list|(
operator|(
name|COSStream
operator|)
name|obj
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSDictionary
condition|)
block|{
name|decryptDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|obj
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSArray
condition|)
block|{
name|decryptArray
argument_list|(
operator|(
name|COSArray
operator|)
name|obj
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will decrypt a stream.      *      * @param stream The stream to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If there is an error getting the stream data.      */
specifier|public
name|void
name|decryptStream
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|decryptMetadata
operator|&&
name|COSName
operator|.
name|METADATA
operator|.
name|equals
argument_list|(
name|stream
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// "The cross-reference stream shall not be encrypted"
if|if
condition|(
name|COSName
operator|.
name|XREF
operator|.
name|equals
argument_list|(
name|stream
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
name|decryptDictionary
argument_list|(
name|stream
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
name|InputStream
name|encryptedStream
init|=
name|stream
operator|.
name|getFilteredStream
argument_list|()
decl_stmt|;
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|encryptedStream
argument_list|,
name|stream
operator|.
name|createFilteredStream
argument_list|()
argument_list|,
literal|true
comment|/* decrypt */
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will encrypt a stream, but not the dictionary as the dictionary is      * encrypted by visitFromString() in COSWriter and we don't want to encrypt      * it twice.      *      * @param stream The stream to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If there is an error getting the stream data.      */
specifier|public
name|void
name|encryptStream
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|encryptedStream
init|=
name|stream
operator|.
name|getFilteredStream
argument_list|()
decl_stmt|;
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|encryptedStream
argument_list|,
name|stream
operator|.
name|createFilteredStream
argument_list|()
argument_list|,
literal|false
comment|/* encrypt */
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decrypt a dictionary.      *      * @param dictionary The dictionary to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If there is an error creating a new string.      */
specifier|private
name|void
name|decryptDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|dictionary
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|COSBase
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// within a dictionary only the following kind of COS objects have to be decrypted
if|if
condition|(
name|value
operator|instanceof
name|COSString
operator|||
name|value
operator|instanceof
name|COSStream
operator|||
name|value
operator|instanceof
name|COSArray
operator|||
name|value
operator|instanceof
name|COSDictionary
condition|)
block|{
comment|// if we are a signature dictionary and contain a Contents entry then
comment|// we don't decrypt it.
if|if
condition|(
operator|!
operator|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
operator|&&
name|value
operator|instanceof
name|COSString
operator|&&
name|potentialSignatures
operator|.
name|contains
argument_list|(
name|dictionary
argument_list|)
operator|)
condition|)
block|{
name|decrypt
argument_list|(
name|value
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will decrypt a string.      *      * @param string the string to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If an error occurs writing the new string.      */
specifier|public
name|void
name|decryptString
parameter_list|(
name|COSString
name|string
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|data
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|string
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|data
argument_list|,
name|buffer
argument_list|,
literal|true
comment|/* decrypt */
argument_list|)
expr_stmt|;
name|string
operator|.
name|reset
argument_list|()
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will encrypt a string.      *      * @param string the string to encrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If an error occurs writing the new string.      */
specifier|public
name|void
name|encryptString
parameter_list|(
name|COSString
name|string
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|data
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|string
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|data
argument_list|,
name|buffer
argument_list|,
literal|false
comment|/* decrypt */
argument_list|)
expr_stmt|;
name|string
operator|.
name|reset
argument_list|()
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decrypt an array.      *      * @param array The array to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|void
name|decryptArray
parameter_list|(
name|COSArray
name|array
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|decrypt
argument_list|(
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|objNum
argument_list|,
name|genNum
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Getter of the property<tt>keyLength</tt>.      * @return  Returns the keyLength.      */
specifier|public
name|int
name|getKeyLength
parameter_list|()
block|{
return|return
name|keyLength
return|;
block|}
comment|/**      * Setter of the property<tt>keyLength</tt>.      *      * @param keyLen  The keyLength to set.      */
specifier|public
name|void
name|setKeyLength
parameter_list|(
name|int
name|keyLen
parameter_list|)
block|{
name|this
operator|.
name|keyLength
operator|=
name|keyLen
expr_stmt|;
block|}
comment|/**      * Returns the access permissions that were computed during document decryption.      * The returned object is in read only mode.      *      * @return the access permissions or null if the document was not decrypted.      */
specifier|public
name|AccessPermission
name|getCurrentAccessPermission
parameter_list|()
block|{
return|return
name|currentAccessPermission
return|;
block|}
comment|/**      * True if AES is used for encryption and decryption.      *       * @return true if AEs is used       */
specifier|public
name|boolean
name|isAES
parameter_list|()
block|{
return|return
name|useAES
return|;
block|}
comment|/**      * Set to true if AES for encryption and decryption should be used.      *       * @param aesValue if true AES will be used       *       */
specifier|public
name|void
name|setAES
parameter_list|(
name|boolean
name|aesValue
parameter_list|)
block|{
name|useAES
operator|=
name|aesValue
expr_stmt|;
block|}
block|}
end_class

end_unit

