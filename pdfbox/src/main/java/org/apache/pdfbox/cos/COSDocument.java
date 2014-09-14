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
name|cos
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|IOException
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|pdfparser
operator|.
name|NonSequentialPDFParser
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
name|pdfparser
operator|.
name|PDFObjectStreamParser
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This is the in-memory representation of the PDF document.  You need to call  * close() on this object when you are done using it!!  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|COSDocument
extends|extends
name|COSBase
implements|implements
name|Closeable
block|{
comment|/**      * Log instance.      */
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
name|COSDocument
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|float
name|version
init|=
literal|1.4f
decl_stmt|;
comment|/**      * Maps ObjectKeys to a COSObject. Note that references to these objects      * are also stored in COSDictionary objects that map a name to a specific object.      */
specifier|private
specifier|final
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|COSObject
argument_list|>
name|objectPool
init|=
operator|new
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|COSObject
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Maps object and generation id to object byte offsets.      */
specifier|private
specifier|final
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|xrefTable
init|=
operator|new
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Document trailer dictionary.      */
specifier|private
name|COSDictionary
name|trailer
decl_stmt|;
comment|/**      * Signature interface.      */
specifier|private
name|SignatureInterface
name|signatureInterface
decl_stmt|;
specifier|private
name|String
name|headerString
init|=
literal|"%PDF-"
operator|+
name|version
decl_stmt|;
specifier|private
name|boolean
name|warnMissingClose
init|=
literal|true
decl_stmt|;
comment|/** signal that document is already decrypted, e.g. with {@link NonSequentialPDFParser} */
specifier|private
name|boolean
name|isDecrypted
init|=
literal|false
decl_stmt|;
specifier|private
name|long
name|startXref
decl_stmt|;
specifier|private
name|boolean
name|closed
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isXRefStream
decl_stmt|;
specifier|private
specifier|final
name|File
name|scratchDirectory
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|useScratchFiles
decl_stmt|;
comment|/**      * Flag to skip malformed or otherwise unparseable input where possible.      */
specifier|private
specifier|final
name|boolean
name|forceParsing
decl_stmt|;
comment|/**      * Constructor that will use the given random access file for storage      * of the PDF streams. The client of this method is responsible for      * deleting the storage if necessary that this file will write to. The      * close method will close the file though.      *      * @param scratchFileValue the random access file to use for storage      * @param forceParsingValue flag to skip malformed or otherwise unparseable      *                     document content where possible      */
specifier|public
name|COSDocument
parameter_list|(
name|boolean
name|forceParsingValue
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|forceParsingValue
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will use the given random access file for storage      * of the PDF streams. The client of this method is responsible for      * deleting the storage if necessary that this file will write to. The      * close method will close the file though.      *      * @param scratchFileValue the random access file to use for storage      * @param forceParsingValue flag to skip malformed or otherwise unparseable      *                     document content where possible      */
specifier|public
name|COSDocument
parameter_list|(
name|boolean
name|forceParsingValue
parameter_list|,
name|boolean
name|useScratchFiles
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|forceParsingValue
argument_list|,
name|useScratchFiles
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will use a temporary file in the given directory      * for storage of the PDF streams. The temporary file is automatically      * removed when this document gets closed.      *      * @param scratchDir directory for the temporary file,      *                   or<code>null</code> to use the system default      * @param forceParsingValue flag to skip malformed or otherwise unparseable      *                     document content where possible      */
specifier|public
name|COSDocument
parameter_list|(
name|File
name|scratchDir
parameter_list|,
name|boolean
name|forceParsingValue
parameter_list|)
block|{
name|this
argument_list|(
name|scratchDir
argument_list|,
name|forceParsingValue
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will use a temporary file in the given directory      * for storage of the PDF streams. The temporary file is automatically      * removed when this document gets closed.      *      * @param scratchDir directory for the temporary file,      *                   or<code>null</code> to use the system default      * @param forceParsingValue flag to skip malformed or otherwise unparseable      *                     document content where possible      */
specifier|public
name|COSDocument
parameter_list|(
name|File
name|scratchDir
parameter_list|,
name|boolean
name|forceParsingValue
parameter_list|,
name|boolean
name|useScratchFiles
parameter_list|)
block|{
name|forceParsing
operator|=
name|forceParsingValue
expr_stmt|;
name|scratchDirectory
operator|=
name|scratchDir
expr_stmt|;
name|this
operator|.
name|useScratchFiles
operator|=
name|useScratchFiles
expr_stmt|;
block|}
comment|/**      * Constructor.  Uses memory to store stream.      */
specifier|public
name|COSDocument
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will create a create a scratch file in the      * following directory.      *      * @param scratchDir The directory to store a scratch file.      *      * @throws IOException If there is an error creating the tmp file.      */
specifier|public
name|COSDocument
parameter_list|(
name|File
name|scratchDir
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|scratchDir
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a new COSStream using the underlying scratch file.      *       * @return the new COSStream      */
specifier|public
name|COSStream
name|createCOSStream
parameter_list|()
block|{
return|return
operator|new
name|COSStream
argument_list|(
name|useScratchFiles
argument_list|,
name|scratchDirectory
argument_list|)
return|;
block|}
comment|/**      * Create a new COSStream using the underlying scratch file.      *      * @param dictionary the corresponding dictionary      *       * @return the new COSStream      */
specifier|public
name|COSStream
name|createCOSStream
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
return|return
operator|new
name|COSStream
argument_list|(
name|dictionary
argument_list|,
name|useScratchFiles
argument_list|,
name|scratchDirectory
argument_list|)
return|;
block|}
comment|/**      * This will get the first dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      * @throws IOException If there is an error getting the object      */
specifier|public
name|COSObject
name|getObjectByType
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getObjectByType
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will get the first dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      * @throws IOException If there is an error getting the object      */
specifier|public
name|COSObject
name|getObjectByType
parameter_list|(
name|COSName
name|type
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|COSObject
name|object
range|:
name|objectPool
operator|.
name|values
argument_list|()
control|)
block|{
name|COSBase
name|realObject
init|=
name|object
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|realObject
operator|instanceof
name|COSDictionary
condition|)
block|{
try|try
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|realObject
decl_stmt|;
name|COSBase
name|typeItem
init|=
name|dic
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeItem
operator|!=
literal|null
operator|&&
name|typeItem
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|objectType
init|=
operator|(
name|COSName
operator|)
name|typeItem
decl_stmt|;
if|if
condition|(
name|objectType
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|object
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|typeItem
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Expected a /Name object after /Type, got '"
operator|+
name|typeItem
operator|+
literal|"' instead"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will get all dictionary objects by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      * @throws IOException If there is an error getting the object      */
specifier|public
name|List
argument_list|<
name|COSObject
argument_list|>
name|getObjectsByType
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getObjectsByType
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will get a dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      * @throws IOException If there is an error getting the object      */
specifier|public
name|List
argument_list|<
name|COSObject
argument_list|>
name|getObjectsByType
parameter_list|(
name|COSName
name|type
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|COSObject
argument_list|>
name|retval
init|=
operator|new
name|ArrayList
argument_list|<
name|COSObject
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|COSObject
name|object
range|:
name|objectPool
operator|.
name|values
argument_list|()
control|)
block|{
name|COSBase
name|realObject
init|=
name|object
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|realObject
operator|instanceof
name|COSDictionary
condition|)
block|{
try|try
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|realObject
decl_stmt|;
name|COSBase
name|typeItem
init|=
name|dic
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeItem
operator|!=
literal|null
operator|&&
name|typeItem
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|objectType
init|=
operator|(
name|COSName
operator|)
name|typeItem
decl_stmt|;
if|if
condition|(
name|objectType
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retval
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|typeItem
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Expected a /Name object after /Type, got '"
operator|+
name|typeItem
operator|+
literal|"' instead"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will print contents to stdout.      */
specifier|public
name|void
name|print
parameter_list|()
block|{
for|for
control|(
name|COSObject
name|object
range|:
name|objectPool
operator|.
name|values
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will set the version of this PDF document.      *      * @param versionValue The version of the PDF document.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|float
name|versionValue
parameter_list|)
block|{
comment|// update header string
if|if
condition|(
name|versionValue
operator|!=
name|version
condition|)
block|{
name|headerString
operator|=
name|headerString
operator|.
name|replaceFirst
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|version
argument_list|)
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|versionValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|version
operator|=
name|versionValue
expr_stmt|;
block|}
comment|/**      * This will get the version of this PDF document.      *      * @return This documents version.      */
specifier|public
name|float
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/** Signals that the document is decrypted completely.      *  Needed e.g. by {@link NonSequentialPDFParser} to circumvent      *  additional decryption later on. */
specifier|public
name|void
name|setDecrypted
parameter_list|()
block|{
name|isDecrypted
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This will tell if this is an encrypted document.      *      * @return true If this document is encrypted.      */
specifier|public
name|boolean
name|isEncrypted
parameter_list|()
block|{
if|if
condition|(
name|isDecrypted
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|encrypted
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|trailer
operator|!=
literal|null
condition|)
block|{
name|encrypted
operator|=
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCRYPT
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
return|return
name|encrypted
return|;
block|}
comment|/**      * This will get the encryption dictionary if the document is encrypted or null      * if the document is not encrypted.      *      * @return The encryption dictionary.      */
specifier|public
name|COSDictionary
name|getEncryptionDictionary
parameter_list|()
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCRYPT
argument_list|)
return|;
block|}
comment|/**      * This will return the signature interface.      * @return the signature interface       */
specifier|public
name|SignatureInterface
name|getSignatureInterface
parameter_list|()
block|{
return|return
name|signatureInterface
return|;
block|}
comment|/**      * This will set the encryption dictionary, this should only be called when      * encrypting the document.      *      * @param encDictionary The encryption dictionary.      */
specifier|public
name|void
name|setEncryptionDictionary
parameter_list|(
name|COSDictionary
name|encDictionary
parameter_list|)
block|{
name|trailer
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ENCRYPT
argument_list|,
name|encDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a list of signature dictionaries as COSDictionary.      *      * @return list of signature dictionaries as COSDictionary      * @throws IOException if no document catalog can be found      */
specifier|public
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|getSignatureDictionaries
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|signatureFields
init|=
name|getSignatureFields
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|signatures
init|=
operator|new
name|LinkedList
argument_list|<
name|COSDictionary
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|COSDictionary
name|dict
range|:
name|signatureFields
control|)
block|{
name|COSBase
name|dictionaryObject
init|=
name|dict
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
name|dictionaryObject
operator|!=
literal|null
condition|)
block|{
name|signatures
operator|.
name|add
argument_list|(
operator|(
name|COSDictionary
operator|)
name|dictionaryObject
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|signatures
return|;
block|}
comment|/**      * This will return a list of signature fields.      *      * @return list of signature dictionaries as COSDictionary      * @throws IOException if no document catalog can be found      */
specifier|public
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|getSignatureFields
parameter_list|(
name|boolean
name|onlyEmptyFields
parameter_list|)
throws|throws
name|IOException
block|{
name|COSObject
name|documentCatalog
init|=
name|getCatalog
argument_list|()
decl_stmt|;
if|if
condition|(
name|documentCatalog
operator|!=
literal|null
condition|)
block|{
name|COSDictionary
name|acroForm
init|=
operator|(
name|COSDictionary
operator|)
name|documentCatalog
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ACRO_FORM
argument_list|)
decl_stmt|;
if|if
condition|(
name|acroForm
operator|!=
literal|null
condition|)
block|{
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|acroForm
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FIELDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
comment|// Some fields may contain twice references to a single field.
comment|// This will prevent such double entries.
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|COSDictionary
argument_list|>
name|signatures
init|=
operator|new
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|COSDictionary
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|fields
control|)
block|{
name|COSObject
name|dict
init|=
operator|(
name|COSObject
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|SIG
operator|.
name|equals
argument_list|(
name|dict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
argument_list|)
condition|)
block|{
name|COSBase
name|dictionaryObject
init|=
name|dict
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
name|dictionaryObject
operator|==
literal|null
operator|||
operator|(
name|dictionaryObject
operator|!=
literal|null
operator|&&
operator|!
name|onlyEmptyFields
operator|)
condition|)
block|{
name|signatures
operator|.
name|put
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
name|dict
argument_list|)
argument_list|,
operator|(
name|COSDictionary
operator|)
name|dict
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|LinkedList
argument_list|<
name|COSDictionary
argument_list|>
argument_list|(
name|signatures
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * This will get the document ID.      *      * @return The document id.      */
specifier|public
name|COSArray
name|getDocumentID
parameter_list|()
block|{
return|return
operator|(
name|COSArray
operator|)
name|getTrailer
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ID
argument_list|)
return|;
block|}
comment|/**      * This will set the document ID.      *      * @param id The document id.      */
specifier|public
name|void
name|setDocumentID
parameter_list|(
name|COSArray
name|id
parameter_list|)
block|{
name|getTrailer
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the signature interface to the given value.      * @param sigInterface the signature interface      */
specifier|public
name|void
name|setSignatureInterface
parameter_list|(
name|SignatureInterface
name|sigInterface
parameter_list|)
block|{
name|signatureInterface
operator|=
name|sigInterface
expr_stmt|;
block|}
comment|/**      * This will get the document catalog.      *      * Maybe this should move to an object at PDFEdit level      *      * @return catalog is the root of all document activities      *      * @throws IOException If no catalog can be found.      */
specifier|public
name|COSObject
name|getCatalog
parameter_list|()
throws|throws
name|IOException
block|{
name|COSObject
name|catalog
init|=
name|getObjectByType
argument_list|(
name|COSName
operator|.
name|CATALOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Catalog cannot be found"
argument_list|)
throw|;
block|}
return|return
name|catalog
return|;
block|}
comment|/**      * This will get a list of all available objects.      *      * @return A list of all objects.      */
specifier|public
name|List
argument_list|<
name|COSObject
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|COSObject
argument_list|>
argument_list|(
name|objectPool
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will get the document trailer.      *      * @return the document trailer dict      */
specifier|public
name|COSDictionary
name|getTrailer
parameter_list|()
block|{
return|return
name|trailer
return|;
block|}
comment|/**      * // MIT added, maybe this should not be supported as trailer is a persistence construct.      * This will set the document trailer.      *      * @param newTrailer the document trailer dictionary      */
specifier|public
name|void
name|setTrailer
parameter_list|(
name|COSDictionary
name|newTrailer
parameter_list|)
block|{
name|trailer
operator|=
name|newTrailer
expr_stmt|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException If an error occurs while visiting this object.      */
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|visitor
operator|.
name|visitFromDocument
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * This will close all storage and delete the tmp files.      *      *  @throws IOException If there is an error close resources.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
if|if
condition|(
name|trailer
operator|!=
literal|null
condition|)
block|{
name|trailer
operator|.
name|clear
argument_list|()
expr_stmt|;
name|trailer
operator|=
literal|null
expr_stmt|;
block|}
comment|// Clear object pool
name|List
argument_list|<
name|COSObject
argument_list|>
name|list
init|=
name|getObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|COSObject
name|object
range|:
name|list
control|)
block|{
name|COSBase
name|cosObject
init|=
name|object
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// clear the resources of the pooled objects
if|if
condition|(
name|cosObject
operator|instanceof
name|COSStream
condition|)
block|{
operator|(
operator|(
name|COSStream
operator|)
name|cosObject
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObject
operator|instanceof
name|COSDictionary
condition|)
block|{
operator|(
operator|(
name|COSDictionary
operator|)
name|cosObject
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObject
operator|instanceof
name|COSArray
condition|)
block|{
operator|(
operator|(
name|COSArray
operator|)
name|cosObject
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// TODO are there other kind of COSObjects to be cleared?
block|}
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Warn the user in the finalizer if he didn't close the PDF document. The method also      * closes the document just in case, to avoid abandoned temporary files. It's still a good      * idea for the user to close the PDF document at the earliest possible to conserve resources.      * @throws IOException if an error occurs while closing the temporary files      */
annotation|@
name|Override
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
if|if
condition|(
name|warnMissingClose
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Warning: You did not close a PDF Document"
argument_list|)
expr_stmt|;
block|}
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Controls whether this instance shall issue a warning if the PDF document wasn't closed      * properly through a call to the {@link #close()} method. If the PDF document is held in      * a cache governed by soft references it is impossible to reliably close the document      * before the warning is raised. By default, the warning is enabled.      * @param warn true enables the warning, false disables it.      */
specifier|public
name|void
name|setWarnMissingClose
parameter_list|(
name|boolean
name|warn
parameter_list|)
block|{
name|this
operator|.
name|warnMissingClose
operator|=
name|warn
expr_stmt|;
block|}
comment|/**      * @return Returns the headerString.      */
specifier|public
name|String
name|getHeaderString
parameter_list|()
block|{
return|return
name|headerString
return|;
block|}
comment|/**      * @param header The headerString to set.      */
specifier|public
name|void
name|setHeaderString
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|headerString
operator|=
name|header
expr_stmt|;
block|}
comment|/**      * This method will search the list of objects for types of ObjStm.  If it finds      * them then it will parse out all of the objects from the stream that is contains.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|public
name|void
name|dereferenceObjectStreams
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
name|COSObject
name|objStream
range|:
name|getObjectsByType
argument_list|(
name|COSName
operator|.
name|OBJ_STM
argument_list|)
control|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|objStream
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|PDFObjectStreamParser
name|parser
init|=
operator|new
name|PDFObjectStreamParser
argument_list|(
name|stream
argument_list|,
name|this
argument_list|,
name|forceParsing
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
for|for
control|(
name|COSObject
name|next
range|:
name|parser
operator|.
name|getObjects
argument_list|()
control|)
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectPool
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|null
operator|||
name|objectPool
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getObject
argument_list|()
operator|==
literal|null
operator|||
comment|// xrefTable stores negated objNr of objStream for objects in objStreams
operator|(
name|xrefTable
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
name|xrefTable
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
operator|-
name|objStream
operator|.
name|getObjectNumber
argument_list|()
operator|.
name|longValue
argument_list|()
operator|)
condition|)
block|{
name|COSObject
name|obj
init|=
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setObject
argument_list|(
name|next
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will get an object from the pool.      *      * @param key The object key.      *      * @return The object in the pool or a new one if it has not been parsed yet.      *      * @throws IOException If there is an error getting the proxy object.      */
specifier|public
name|COSObject
name|getObjectFromPool
parameter_list|(
name|COSObjectKey
name|key
parameter_list|)
throws|throws
name|IOException
block|{
name|COSObject
name|obj
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|obj
operator|=
name|objectPool
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
comment|// this was a forward reference, make "proxy" object
name|obj
operator|=
operator|new
name|COSObject
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|obj
operator|.
name|setObjectNumber
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|key
operator|.
name|getNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|key
operator|.
name|getGeneration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|objectPool
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|obj
return|;
block|}
comment|/**      * Removes an object from the object pool.      * @param key the object key      * @return the object that was removed or null if the object was not found      */
specifier|public
name|COSObject
name|removeObject
parameter_list|(
name|COSObjectKey
name|key
parameter_list|)
block|{
return|return
name|objectPool
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Populate XRef HashMap with given values.      * Each entry maps ObjectKeys to byte offsets in the file.      * @param xrefTableValues  xref table entries to be added      */
specifier|public
name|void
name|addXRefTable
parameter_list|(
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|xrefTableValues
parameter_list|)
block|{
name|xrefTable
operator|.
name|putAll
argument_list|(
name|xrefTableValues
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the xrefTable which is a mapping of ObjectKeys      * to byte offsets in the file.      * @return mapping of ObjectsKeys to byte offsets      */
specifier|public
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|getXrefTable
parameter_list|()
block|{
return|return
name|xrefTable
return|;
block|}
comment|/**      * This method set the startxref value of the document. This will only       * be needed for incremental updates.      *       * @param startXrefValue the value for startXref      */
specifier|public
name|void
name|setStartXref
parameter_list|(
name|long
name|startXrefValue
parameter_list|)
block|{
name|startXref
operator|=
name|startXrefValue
expr_stmt|;
block|}
comment|/**      * Return the startXref Position of the parsed document. This will only be needed for incremental updates.      *       * @return a long with the old position of the startxref      */
specifier|public
name|long
name|getStartXref
parameter_list|()
block|{
return|return
name|startXref
return|;
block|}
comment|/**      * Determines if the trailer is a XRef stream or not.      *       * @return true if the trailer is a XRef stream      */
specifier|public
name|boolean
name|isXRefStream
parameter_list|()
block|{
return|return
name|isXRefStream
return|;
block|}
comment|/**      * Sets isXRefStream to the given value.      *       * @param isXRefStreamValue the new value for isXRefStream      */
specifier|public
name|void
name|setIsXRefStream
parameter_list|(
name|boolean
name|isXRefStreamValue
parameter_list|)
block|{
name|isXRefStream
operator|=
name|isXRefStreamValue
expr_stmt|;
block|}
block|}
end_class

end_unit

