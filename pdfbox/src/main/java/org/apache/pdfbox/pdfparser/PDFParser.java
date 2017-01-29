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
name|pdfparser
package|;
end_package

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
name|COSNull
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
name|io
operator|.
name|RandomAccessRead
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
name|ScratchFile
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
name|DecryptionMaterial
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
name|InvalidPasswordException
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
name|PDEncryption
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
name|StandardDecryptionMaterial
import|;
end_import

begin_class
specifier|public
class|class
name|PDFParser
extends|extends
name|COSParser
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
name|PDFParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|password
init|=
literal|""
decl_stmt|;
specifier|private
name|InputStream
name|keyStoreInputStream
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|keyAlias
init|=
literal|null
decl_stmt|;
specifier|private
name|PDEncryption
name|encryption
init|=
literal|null
decl_stmt|;
specifier|private
name|AccessPermission
name|accessPermission
decl_stmt|;
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source source representing the pdf.      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
literal|""
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param scratchFile use a {@link ScratchFile} for temporary storage.      *       * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
literal|""
argument_list|,
name|scratchFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param scratchFile use a {@link ScratchFile} for temporary storage.      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|scratchFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param keyStore key store to be used for decryption when using public key security       * @param alias alias to be used for decryption when using public key security      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|InputStream
name|keyStore
parameter_list|,
name|String
name|alias
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
name|keyStore
argument_list|,
name|alias
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param keyStore key store to be used for decryption when using public key security       * @param alias alias to be used for decryption when using public key security      * @param scratchFile buffer handler for temporary storage; it will be closed on      *        {@link COSDocument#close()}      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|InputStream
name|keyStore
parameter_list|,
name|String
name|alias
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|fileLen
operator|=
name|source
operator|.
name|length
argument_list|()
expr_stmt|;
name|password
operator|=
name|decryptionPassword
expr_stmt|;
name|keyStoreInputStream
operator|=
name|keyStore
expr_stmt|;
name|keyAlias
operator|=
name|alias
expr_stmt|;
name|init
argument_list|(
name|scratchFile
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|eofLookupRangeStr
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYSPROP_EOFLOOKUPRANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|eofLookupRangeStr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|setEOFLookupRange
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|eofLookupRangeStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"System property "
operator|+
name|SYSPROP_EOFLOOKUPRANGE
operator|+
literal|" does not contain an integer value, but: '"
operator|+
name|eofLookupRangeStr
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
name|document
operator|=
operator|new
name|COSDocument
argument_list|(
name|scratchFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the PD document that was parsed.  When you are done with      * this document you must call close() on it to release resources.      *      * @return The document at the PD layer.      *      * @throws IOException If there is an error getting the document.      */
specifier|public
name|PDDocument
name|getPDDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|(
name|getDocument
argument_list|()
argument_list|,
name|source
argument_list|,
name|accessPermission
argument_list|)
decl_stmt|;
name|doc
operator|.
name|setEncryptionDictionary
argument_list|(
name|encryption
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
comment|/**      * The initial parse will first parse only the trailer, the xrefstart and all xref tables to have a pointer (offset)      * to all the pdf's objects. It can handle linearized pdfs, which will have an xref at the end pointing to an xref      * at the beginning of the file. Last the root object is parsed.      *       * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException If something went wrong.      */
specifier|protected
name|void
name|initialParse
parameter_list|()
throws|throws
name|InvalidPasswordException
throws|,
name|IOException
block|{
name|COSDictionary
name|trailer
init|=
literal|null
decl_stmt|;
comment|// parse startxref
name|long
name|startXRefOffset
init|=
name|getStartxrefOffset
argument_list|()
decl_stmt|;
name|boolean
name|rebuildTrailer
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|startXRefOffset
operator|>
operator|-
literal|1
condition|)
block|{
try|try
block|{
name|trailer
operator|=
name|parseXref
argument_list|(
name|startXRefOffset
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|isLenient
argument_list|()
condition|)
block|{
name|rebuildTrailer
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|exception
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|isLenient
argument_list|()
condition|)
block|{
name|rebuildTrailer
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|rebuildTrailer
condition|)
block|{
name|trailer
operator|=
name|rebuildTrailer
argument_list|()
expr_stmt|;
block|}
comment|// prepare decryption if necessary
name|prepareDecryption
argument_list|()
expr_stmt|;
name|COSBase
name|base
init|=
name|parseTrailerValuesDynamically
argument_list|(
name|trailer
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSDictionary
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected root dictionary, but got this: "
operator|+
name|base
argument_list|)
throw|;
block|}
name|COSDictionary
name|root
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
comment|// in some pdfs the type value "Catalog" is missing in the root object
if|if
condition|(
name|isLenient
argument_list|()
operator|&&
operator|!
name|root
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
condition|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|CATALOG
argument_list|)
expr_stmt|;
block|}
name|COSObject
name|catalogObj
init|=
name|document
operator|.
name|getCatalog
argument_list|()
decl_stmt|;
if|if
condition|(
name|catalogObj
operator|!=
literal|null
operator|&&
name|catalogObj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSDictionary
condition|)
block|{
name|parseDictObjects
argument_list|(
operator|(
name|COSDictionary
operator|)
name|catalogObj
operator|.
name|getObject
argument_list|()
argument_list|,
operator|(
name|COSName
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|COSBase
name|infoBase
init|=
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|INFO
argument_list|)
decl_stmt|;
if|if
condition|(
name|infoBase
operator|instanceof
name|COSDictionary
condition|)
block|{
name|parseDictObjects
argument_list|(
operator|(
name|COSDictionary
operator|)
name|infoBase
argument_list|,
operator|(
name|COSName
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
name|document
operator|.
name|setDecrypted
argument_list|()
expr_stmt|;
block|}
name|initialParseDone
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This will parse the stream and populate the COSDocument object.  This will close      * the keystore stream when it is done parsing.      *      * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException If there is an error reading from the stream or corrupt data      * is found.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|InvalidPasswordException
throws|,
name|IOException
block|{
comment|// set to false if all is processed
name|boolean
name|exceptionOccurred
init|=
literal|true
decl_stmt|;
try|try
block|{
comment|// PDFBOX-1922 read the version header and rewind
if|if
condition|(
operator|!
name|parsePDFHeader
argument_list|()
operator|&&
operator|!
name|parseFDFHeader
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Header doesn't contain versioninfo"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|initialParseDone
condition|)
block|{
name|initialParse
argument_list|()
expr_stmt|;
block|}
name|exceptionOccurred
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|keyStoreInputStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|exceptionOccurred
operator|&&
name|document
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Prepare for decryption.      *       * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException if something went wrong      */
specifier|private
name|void
name|prepareDecryption
parameter_list|()
throws|throws
name|InvalidPasswordException
throws|,
name|IOException
block|{
name|COSBase
name|trailerEncryptItem
init|=
name|document
operator|.
name|getTrailer
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCRYPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|trailerEncryptItem
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|trailerEncryptItem
operator|instanceof
name|COSNull
operator|)
condition|)
block|{
if|if
condition|(
name|trailerEncryptItem
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|trailerEncryptObj
init|=
operator|(
name|COSObject
operator|)
name|trailerEncryptItem
decl_stmt|;
name|parseDictionaryRecursive
argument_list|(
name|trailerEncryptObj
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|encryption
operator|=
operator|new
name|PDEncryption
argument_list|(
name|document
operator|.
name|getEncryptionDictionary
argument_list|()
argument_list|)
expr_stmt|;
name|DecryptionMaterial
name|decryptionMaterial
decl_stmt|;
if|if
condition|(
name|keyStoreInputStream
operator|!=
literal|null
condition|)
block|{
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"PKCS12"
argument_list|)
decl_stmt|;
name|ks
operator|.
name|load
argument_list|(
name|keyStoreInputStream
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|decryptionMaterial
operator|=
operator|new
name|PublicKeyDecryptionMaterial
argument_list|(
name|ks
argument_list|,
name|keyAlias
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|decryptionMaterial
operator|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
name|securityHandler
operator|=
name|encryption
operator|.
name|getSecurityHandler
argument_list|()
expr_stmt|;
name|securityHandler
operator|.
name|prepareForDecryption
argument_list|(
name|encryption
argument_list|,
name|document
operator|.
name|getDocumentID
argument_list|()
argument_list|,
name|decryptionMaterial
argument_list|)
expr_stmt|;
name|accessPermission
operator|=
name|securityHandler
operator|.
name|getCurrentAccessPermission
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error ("
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|") while creating security handler for decryption"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Resolves all not already parsed objects of a dictionary recursively.      *       * @param dictionaryObject dictionary to be parsed      * @throws IOException if something went wrong      *       */
specifier|private
name|void
name|parseDictionaryRecursive
parameter_list|(
name|COSObject
name|dictionaryObject
parameter_list|)
throws|throws
name|IOException
block|{
name|parseObjectDynamically
argument_list|(
name|dictionaryObject
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|COSDictionary
name|dictionary
init|=
operator|(
name|COSDictionary
operator|)
name|dictionaryObject
operator|.
name|getObject
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|value
range|:
name|dictionary
operator|.
name|getValues
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|object
init|=
operator|(
name|COSObject
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|object
operator|.
name|getObject
argument_list|()
operator|==
literal|null
condition|)
block|{
name|parseDictionaryRecursive
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

