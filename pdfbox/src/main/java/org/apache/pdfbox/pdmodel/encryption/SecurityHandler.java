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
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SecurityHandler
operator|.
name|class
argument_list|)
decl_stmt|;
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
comment|/** The RC4 implementation used for cryptographic functions. */
specifier|protected
name|RC4Cipher
name|rc4
init|=
operator|new
name|RC4Cipher
argument_list|()
decl_stmt|;
comment|/** indicates if the Metadata have to be decrypted of not. */
specifier|private
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
comment|/**      * Set wether to decrypt meta data.      *      * @param decryptMetadata true if meta data has to be decrypted.      */
specifier|protected
name|void
name|setDecryptMetadata
parameter_list|(
name|boolean
name|decryptMetadata
parameter_list|)
block|{
name|this
operator|.
name|decryptMetadata
operator|=
name|decryptMetadata
expr_stmt|;
block|}
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
comment|/**      * Prepares everything to decrypt the document.      *       * @param encryption  encryption dictionary, can be retrieved via {@link PDDocument#getEncryption()}      * @param documentIDArray  document id which is returned via {@link org.apache.pdfbox.cos.COSDocument#getDocumentID()}      * @param decryptionMaterial Information used to decrypt the document.      *      * @throws IOException If there is an error accessing data.      */
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
comment|/**      * Encrypt or decrypt a set of data.      *      * @param objectNumber The data object number.      * @param genNumber The data generation number.      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      * @param decrypt true to decrypt the data, false to encrypt it.      *      * @throws IOException If there is an error reading the data.      */
specifier|private
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
name|encryptDataAES256
argument_list|(
name|data
argument_list|,
name|output
argument_list|,
name|decrypt
argument_list|)
expr_stmt|;
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
name|finalKey
init|=
name|calcFinalKey
argument_list|(
name|objectNumber
argument_list|,
name|genNumber
argument_list|)
decl_stmt|;
if|if
condition|(
name|useAES
condition|)
block|{
name|encryptDataAESother
argument_list|(
name|finalKey
argument_list|,
name|data
argument_list|,
name|output
argument_list|,
name|decrypt
argument_list|)
expr_stmt|;
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
comment|/**      * Calculate the key to be used for RC4 and AES-128.      *      * @param objectNumber The data object number.      * @param genNumber The data generation number.      * @return the calculated key.      */
specifier|private
name|byte
index|[]
name|calcFinalKey
parameter_list|(
name|long
name|objectNumber
parameter_list|,
name|long
name|genNumber
parameter_list|)
block|{
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
return|return
name|finalKey
return|;
block|}
comment|/**      * Encrypt or decrypt data with AES with key length other than 256 bits.      *      * @param finalKey The final key obtained with via {@link #calcFinalKey()}.      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      * @param decrypt true to decrypt the data, false to encrypt it.      *      * @throws IOException If there is an error reading the data.      */
specifier|private
name|void
name|encryptDataAESother
parameter_list|(
name|byte
index|[]
name|finalKey
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
name|int
name|ivSize
init|=
name|data
operator|.
name|read
argument_list|(
name|iv
argument_list|)
decl_stmt|;
if|if
condition|(
name|ivSize
operator|!=
name|iv
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"AES initialization vector not fully read: only "
operator|+
name|ivSize
operator|+
literal|" bytes read instead of "
operator|+
name|iv
operator|.
name|length
argument_list|)
throw|;
block|}
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
name|int
name|n
decl_stmt|;
while|while
condition|(
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
operator|!=
operator|-
literal|1
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
name|output
operator|.
name|write
argument_list|(
name|decryptCipher
operator|.
name|doFinal
argument_list|()
argument_list|)
expr_stmt|;
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
comment|/**      * Encrypt or decrypt data with AES256.      *      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      * @param decrypt true to decrypt the data, false to encrypt it.      *      * @throws IOException If there is an error reading the data.      */
specifier|private
name|void
name|encryptDataAES256
parameter_list|(
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
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|// starting with java 8 the JVM wraps an IOException around a GeneralSecurityException
comment|// it should be safe to swallow a GeneralSecurityException
if|if
condition|(
operator|!
operator|(
name|exception
operator|.
name|getCause
argument_list|()
operator|instanceof
name|GeneralSecurityException
operator|)
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"A GeneralSecurityException occured when decrypting some stream data"
argument_list|,
name|exception
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
comment|/**      * This will dispatch to the correct method.      *      * @param obj The object to decrypt.      * @param objNum The object number.      * @param genNum The object generation Number.      *      * @throws IOException If there is an error getting the stream data.      */
specifier|public
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
name|byte
index|[]
name|encrypted
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
operator|.
name|getFilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|encryptedStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|encrypted
argument_list|)
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
name|int
name|genNum
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|rawData
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
operator|.
name|getFilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|encryptedStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|rawData
argument_list|)
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
comment|// skip dictionary containing the signature
if|if
condition|(
operator|!
name|COSName
operator|.
name|SIG
operator|.
name|equals
argument_list|(
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
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
block|}
comment|/**      * This will decrypt a string.      *      * @param string the string to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If an error occurs writing the new string.      */
specifier|private
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
name|setValue
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
name|int
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
name|setValue
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decrypt an array.      *      * @param array The array to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws IOException If there is an error accessing the data.      */
specifier|private
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
comment|/**      * Returns whether a protection policy has been set.      *       * @return true if a protection policy has been set.      */
specifier|public
specifier|abstract
name|boolean
name|hasProtectionPolicy
parameter_list|()
function_decl|;
block|}
end_class

end_unit

