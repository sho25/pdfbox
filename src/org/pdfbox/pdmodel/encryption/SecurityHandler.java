begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|Set
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|encryption
operator|.
name|ARCFour
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * This class represents a security handler as described in the PDF specifications.  * A security handler is responsible of documents protection.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SecurityHandler
block|{
comment|/* ------------------------------------------------      * CONSTANTS      -------------------------------------------------- */
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_KEY_LENGTH
init|=
literal|40
decl_stmt|;
comment|/**      * The value of V field of the Encryption dictionary.      */
specifier|protected
name|int
name|version
decl_stmt|;
comment|/**      * The length of the secret key used to encrypt the document.      */
specifier|protected
name|int
name|keyLength
init|=
name|DEFAULT_KEY_LENGTH
decl_stmt|;
comment|/**      * The encryption key that will used to encrypt / decrypt.      */
specifier|protected
name|byte
index|[]
name|encryptionKey
decl_stmt|;
comment|/**      * The document whose security is handled by this security handler.      */
specifier|protected
name|PDDocument
name|document
decl_stmt|;
comment|/**      * The RC4 implementation used for cryptographic functions.      */
specifier|protected
name|ARCFour
name|rc4
init|=
operator|new
name|ARCFour
argument_list|()
decl_stmt|;
specifier|private
name|Set
name|objects
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|private
name|Set
name|potentialSignatures
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|/**      * The access permission granted to the current user for the document. These      * permissions are computed during decryption and are in read only mode.      */
specifier|protected
name|AccessPermission
name|currentAccessPermission
init|=
literal|null
decl_stmt|;
comment|/**      * Prepare the document for encryption.      *      * @param doc The document that will be encrypted.      *      * @throws CryptographyException If there is an error while preparing.      * @throws IOException If there is an error with the document.      */
specifier|public
specifier|abstract
name|void
name|prepareDocumentForEncryption
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|CryptographyException
throws|,
name|IOException
function_decl|;
comment|/**      * Prepare the document for decryption.      *      * @param doc The document to decrypt.      * @param mat Information required to decrypt the document.      * @throws CryptographyException If there is an error while preparing.      * @throws IOException If there is an error with the document.      */
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
name|CryptographyException
throws|,
name|IOException
function_decl|;
comment|/**      * This method must be called by an implementation of this class to really proceed      * to decryption.      *      * @throws IOException If there is an error in the decryption.      * @throws CryptographyException If there is an error in the decryption.      */
specifier|protected
name|void
name|proceedDecryption
parameter_list|()
throws|throws
name|IOException
throws|,
name|CryptographyException
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
comment|//We need to collect all the signature dictionaries, for some
comment|//reason the 'Contents' entry of signatures is not really encrypted
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
name|addDictionaryAndSubDictionary
argument_list|(
name|potentialSignatures
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
block|}
name|List
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
name|objectIter
init|=
name|allObjects
operator|.
name|iterator
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
name|decryptObject
argument_list|(
operator|(
name|COSObject
operator|)
name|objectIter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
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
name|set
parameter_list|,
name|COSDictionary
name|dic
parameter_list|)
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
literal|"Kids"
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
literal|"V"
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
comment|/**      * Encrypt a set of data.      *      * @param objectNumber The data object number.      * @param genNumber The data generation number.      * @param data The data to encrypt.      * @param output The output to write the encrypted data to.      *      * @throws CryptographyException If there is an error during the encryption.      * @throws IOException If there is an error reading the data.      */
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
name|CryptographyException
throws|,
name|IOException
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
comment|//PDF 1.4 reference pg 73
comment|//step 1
comment|//we have the reference
comment|//step 2
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
operator|(
name|objectNumber
operator|>>
literal|8
operator|)
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
operator|(
name|objectNumber
operator|>>
literal|16
operator|)
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
operator|(
name|genNumber
operator|>>
literal|8
operator|)
operator|&
literal|0xff
argument_list|)
expr_stmt|;
comment|//step 3
name|byte
index|[]
name|digestedKey
init|=
literal|null
decl_stmt|;
try|try
block|{
name|MessageDigest
name|md
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
decl_stmt|;
name|digestedKey
operator|=
name|md
operator|.
name|digest
argument_list|(
name|newKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptographyException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|//step 4
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
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will decrypt an object in the document.      *      * @param object The object to decrypt.      *      * @throws CryptographyException If there is an error decrypting the stream.      * @throws IOException If there is an error getting the stream data.      */
specifier|private
name|void
name|decryptObject
parameter_list|(
name|COSObject
name|object
parameter_list|)
throws|throws
name|CryptographyException
throws|,
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
comment|/**      * This will dispatch to the correct method.      *      * @param obj The object to decrypt.      * @param objNum The object number.      * @param genNum The object generation Number.      *      * @throws CryptographyException If there is an error decrypting the stream.      * @throws IOException If there is an error getting the stream data.      */
specifier|private
name|void
name|decrypt
parameter_list|(
name|Object
name|obj
parameter_list|,
name|long
name|objNum
parameter_list|,
name|long
name|genNum
parameter_list|)
throws|throws
name|CryptographyException
throws|,
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
comment|/**      * This will decrypt a stream.      *      * @param stream The stream to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws CryptographyException If there is an error getting the stream.      * @throws IOException If there is an error getting the stream data.      */
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
name|CryptographyException
throws|,
name|IOException
block|{
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
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decrypt a dictionary.      *      * @param dictionary The dictionary to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws CryptographyException If there is an error decrypting the document.      * @throws IOException If there is an error creating a new string.      */
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
name|CryptographyException
throws|,
name|IOException
block|{
name|Iterator
name|keys
init|=
name|dictionary
operator|.
name|keyList
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSName
name|key
init|=
operator|(
name|COSName
operator|)
name|keys
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|//if we are a signature dictionary and contain a Contents entry then
comment|//we don't decrypt it.
if|if
condition|(
operator|!
operator|(
name|key
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Contents"
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
comment|/**      * This will decrypt a string.      *      * @param string the string to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws CryptographyException If an error occurs during decryption.      * @throws IOException If an error occurs writing the new string.      */
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
name|CryptographyException
throws|,
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
comment|/**      * This will decrypt an array.      *      * @param array The array to decrypt.      * @param objNum The object number.      * @param genNum The object generation number.      *      * @throws CryptographyException If an error occurs during decryption.      * @throws IOException If there is an error accessing the data.      */
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
name|CryptographyException
throws|,
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
comment|/**      * Getter of the property<tt>keyLength</tt>.      * @return  Returns the keyLength.      * @uml.property  name="keyLength"      */
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
block|}
end_class

end_unit

