begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
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
name|math
operator|.
name|BigInteger
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
name|COSDocument
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
name|exceptions
operator|.
name|InvalidPasswordException
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

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|encryption
operator|.
name|PDStandardEncryption
import|;
end_import

begin_comment
comment|/**  * This class will deal with encrypting/decrypting a document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.13 $  *   * @deprecated use the new security API instead.  *   * @see org.pdfbox.pdmodel.encryption.StandardSecurityHandler  */
end_comment

begin_class
specifier|public
class|class
name|DocumentEncryption
block|{
specifier|private
name|PDDocument
name|pdDocument
init|=
literal|null
decl_stmt|;
specifier|private
name|COSDocument
name|document
init|=
literal|null
decl_stmt|;
specifier|private
name|byte
index|[]
name|encryptionKey
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFEncryption
name|encryption
init|=
operator|new
name|PDFEncryption
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
comment|/**      * A set that contains potential signature dictionaries.  This is used      * because the Contents entry of the signature is not encrypted.      */
specifier|private
name|Set
name|potentialSignatures
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      *      * @param doc The document to decrypt.      */
specifier|public
name|DocumentEncryption
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|pdDocument
operator|=
name|doc
expr_stmt|;
name|document
operator|=
name|doc
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param doc The document to decrypt.      */
specifier|public
name|DocumentEncryption
parameter_list|(
name|COSDocument
name|doc
parameter_list|)
block|{
name|pdDocument
operator|=
operator|new
name|PDDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|document
operator|=
name|doc
expr_stmt|;
block|}
comment|/**      * This will encrypt the given document, given the owner password and user password.      * The encryption method used is the standard filter.      *      * @throws CryptographyException If an error occurs during encryption.      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|void
name|initForEncryption
parameter_list|()
throws|throws
name|CryptographyException
throws|,
name|IOException
block|{
name|String
name|ownerPassword
init|=
name|pdDocument
operator|.
name|getOwnerPasswordForEncryption
argument_list|()
decl_stmt|;
name|String
name|userPassword
init|=
name|pdDocument
operator|.
name|getUserPasswordForEncryption
argument_list|()
decl_stmt|;
if|if
condition|(
name|ownerPassword
operator|==
literal|null
condition|)
block|{
name|ownerPassword
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|userPassword
operator|==
literal|null
condition|)
block|{
name|userPassword
operator|=
literal|""
expr_stmt|;
block|}
name|PDStandardEncryption
name|encParameters
init|=
operator|(
name|PDStandardEncryption
operator|)
name|pdDocument
operator|.
name|getEncryptionDictionary
argument_list|()
decl_stmt|;
name|int
name|permissionInt
init|=
name|encParameters
operator|.
name|getPermissions
argument_list|()
decl_stmt|;
name|int
name|revision
init|=
name|encParameters
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|encParameters
operator|.
name|getLength
argument_list|()
operator|/
literal|8
decl_stmt|;
name|COSArray
name|idArray
init|=
name|document
operator|.
name|getDocumentID
argument_list|()
decl_stmt|;
comment|//check if the document has an id yet.  If it does not then
comment|//generate one
if|if
condition|(
name|idArray
operator|==
literal|null
operator|||
name|idArray
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
name|idArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
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
name|BigInteger
name|time
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|time
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|ownerPassword
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|userPassword
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|document
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|id
init|=
name|md
operator|.
name|digest
argument_list|(
name|this
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|COSString
name|idString
init|=
operator|new
name|COSString
argument_list|()
decl_stmt|;
name|idString
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|idArray
operator|.
name|add
argument_list|(
name|idString
argument_list|)
expr_stmt|;
name|idArray
operator|.
name|add
argument_list|(
name|idString
argument_list|)
expr_stmt|;
name|document
operator|.
name|setDocumentID
argument_list|(
name|idArray
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
block|}
name|COSString
name|id
init|=
operator|(
name|COSString
operator|)
name|idArray
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|encryption
operator|=
operator|new
name|PDFEncryption
argument_list|()
expr_stmt|;
name|byte
index|[]
name|o
init|=
name|encryption
operator|.
name|computeOwnerPassword
argument_list|(
name|ownerPassword
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|userPassword
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|byte
index|[]
name|u
init|=
name|encryption
operator|.
name|computeUserPassword
argument_list|(
name|userPassword
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|o
argument_list|,
name|permissionInt
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|encryptionKey
operator|=
name|encryption
operator|.
name|computeEncryptedKey
argument_list|(
name|userPassword
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|o
argument_list|,
name|permissionInt
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|encParameters
operator|.
name|setOwnerKey
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|encParameters
operator|.
name|setUserKey
argument_list|(
name|u
argument_list|)
expr_stmt|;
name|document
operator|.
name|setEncryptionDictionary
argument_list|(
name|encParameters
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decrypt the document.      *      * @param password The password for the document.      *      * @throws CryptographyException If there is an error decrypting the document.      * @throws IOException If there is an error getting the stream data.      * @throws InvalidPasswordException If the password is not a user or owner password.      */
specifier|public
name|void
name|decryptDocument
parameter_list|(
name|String
name|password
parameter_list|)
throws|throws
name|CryptographyException
throws|,
name|IOException
throws|,
name|InvalidPasswordException
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
literal|""
expr_stmt|;
block|}
name|PDStandardEncryption
name|encParameters
init|=
operator|(
name|PDStandardEncryption
operator|)
name|pdDocument
operator|.
name|getEncryptionDictionary
argument_list|()
decl_stmt|;
name|int
name|permissions
init|=
name|encParameters
operator|.
name|getPermissions
argument_list|()
decl_stmt|;
name|int
name|revision
init|=
name|encParameters
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|encParameters
operator|.
name|getLength
argument_list|()
operator|/
literal|8
decl_stmt|;
name|COSString
name|id
init|=
operator|(
name|COSString
operator|)
name|document
operator|.
name|getDocumentID
argument_list|()
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|byte
index|[]
name|u
init|=
name|encParameters
operator|.
name|getUserKey
argument_list|()
decl_stmt|;
name|byte
index|[]
name|o
init|=
name|encParameters
operator|.
name|getOwnerKey
argument_list|()
decl_stmt|;
name|boolean
name|isUserPassword
init|=
name|encryption
operator|.
name|isUserPassword
argument_list|(
name|password
operator|.
name|getBytes
argument_list|()
argument_list|,
name|u
argument_list|,
name|o
argument_list|,
name|permissions
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|boolean
name|isOwnerPassword
init|=
name|encryption
operator|.
name|isOwnerPassword
argument_list|(
name|password
operator|.
name|getBytes
argument_list|()
argument_list|,
name|u
argument_list|,
name|o
argument_list|,
name|permissions
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUserPassword
condition|)
block|{
name|encryptionKey
operator|=
name|encryption
operator|.
name|computeEncryptedKey
argument_list|(
name|password
operator|.
name|getBytes
argument_list|()
argument_list|,
name|o
argument_list|,
name|permissions
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isOwnerPassword
condition|)
block|{
name|byte
index|[]
name|computedUserPassword
init|=
name|encryption
operator|.
name|getUserPassword
argument_list|(
name|password
operator|.
name|getBytes
argument_list|()
argument_list|,
name|o
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|encryptionKey
operator|=
name|encryption
operator|.
name|computeEncryptedKey
argument_list|(
name|computedUserPassword
argument_list|,
name|o
argument_list|,
name|permissions
argument_list|,
name|id
operator|.
name|getBytes
argument_list|()
argument_list|,
name|revision
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPasswordException
argument_list|(
literal|"Error: The supplied password does not match "
operator|+
literal|"either the owner or user password in the document."
argument_list|)
throw|;
block|}
name|COSDictionary
name|trailer
init|=
name|document
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
specifier|public
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
specifier|private
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
name|encryption
operator|.
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|encryptionKey
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
name|encryption
operator|.
name|encryptData
argument_list|(
name|objNum
argument_list|,
name|genNum
argument_list|,
name|encryptionKey
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
block|}
end_class

end_unit

