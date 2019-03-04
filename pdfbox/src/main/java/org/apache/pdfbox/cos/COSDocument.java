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
name|HashMap
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
name|pdfparser
operator|.
name|PDFObjectStreamParser
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
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * List containing all streams which are created when creating a new pdf.       */
specifier|private
specifier|final
name|List
argument_list|<
name|COSStream
argument_list|>
name|streams
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Document trailer dictionary.      */
specifier|private
name|COSDictionary
name|trailer
decl_stmt|;
specifier|private
name|boolean
name|warnMissingClose
init|=
literal|true
decl_stmt|;
comment|/**       * Signal that document is already decrypted.       */
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
name|ScratchFile
name|scratchFile
decl_stmt|;
comment|/**      * Used for incremental saving, to avoid XRef object numbers from being reused.      */
specifier|private
name|long
name|highestXRefObjectNumber
decl_stmt|;
comment|/**      * Constructor. Uses main memory to buffer PDF streams.      */
specifier|public
name|COSDocument
parameter_list|()
block|{
name|this
argument_list|(
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will use the provide memory handler for storage of the      * PDF streams.      *      * @param scratchFile memory handler for buffering of PDF streams      *       */
specifier|public
name|COSDocument
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|this
operator|.
name|scratchFile
operator|=
name|scratchFile
expr_stmt|;
block|}
comment|/**      * Creates a new COSStream using the current configuration for scratch files.      *       * @return the new COSStream      */
specifier|public
name|COSStream
name|createCOSStream
parameter_list|()
block|{
name|COSStream
name|stream
init|=
operator|new
name|COSStream
argument_list|(
name|scratchFile
argument_list|)
decl_stmt|;
comment|// collect all COSStreams so that they can be closed when closing the COSDocument.
comment|// This is limited to newly created pdfs as all COSStreams of an existing pdf are
comment|// collected within the map objectPool
name|streams
operator|.
name|add
argument_list|(
name|stream
argument_list|)
expr_stmt|;
return|return
name|stream
return|;
block|}
comment|/**      * Creates a new COSStream using the current configuration for scratch files.      * Not for public use. Only COSParser should call this method.      *      * @param dictionary the corresponding dictionary      * @return the new COSStream      */
specifier|public
name|COSStream
name|createCOSStream
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|COSStream
name|stream
init|=
operator|new
name|COSStream
argument_list|(
name|scratchFile
argument_list|)
decl_stmt|;
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
name|stream
operator|.
name|setItem
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|stream
return|;
block|}
comment|/**      * This will get the first dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
specifier|public
name|COSObject
name|getObjectByType
parameter_list|(
name|COSName
name|type
parameter_list|)
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
comment|/**      * This will get all dictionary objects by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
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
comment|/**      * This will get a dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
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
block|{
name|List
argument_list|<
name|COSObject
argument_list|>
name|retval
init|=
operator|new
name|ArrayList
argument_list|<>
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
comment|/**      * Returns the COSObjectKey for a given COS object, or null if there is none.      * This lookup iterates over all objects in a PDF, which may be slow for large files.      *       * @param object COS object      * @return key      */
specifier|public
name|COSObjectKey
name|getKey
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSObjectKey
argument_list|,
name|COSObject
argument_list|>
name|entry
range|:
name|objectPool
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getObject
argument_list|()
operator|==
name|object
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
literal|null
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
comment|/**      * This will set the header version of this PDF document.      *      * @param versionValue The version of the PDF document.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|float
name|versionValue
parameter_list|)
block|{
name|version
operator|=
name|versionValue
expr_stmt|;
block|}
comment|/**      * This will get the version extracted from the header of this PDF document.      *      * @return The header version.      */
specifier|public
name|float
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**       * Signals that the document is decrypted completely.      */
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
comment|/**       * Indicates if a encrypted pdf is already decrypted after parsing.      *       *  @return true indicates that the pdf is decrypted.      */
specifier|public
name|boolean
name|isDecrypted
parameter_list|()
block|{
return|return
name|isDecrypted
return|;
block|}
comment|/**      * This will tell if this is an encrypted document.      *      * @return true If this document is encrypted.      */
specifier|public
name|boolean
name|isEncrypted
parameter_list|()
block|{
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
comment|/**      * This will get the document catalog.      *      * @return The catalog that is the root of the document; never null.      *      * @throws IOException If no catalog can be found.      */
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
comment|/**      * This will get a list of all available objects.      *      * @return A list of all objects, never null.      */
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
argument_list|<>
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
comment|/**      * Internal PDFBox use only. Get the object number of the highest XRef stream. This is needed to      * avoid reusing such a number in incremental saving.      *      * @return The object number of the highest XRef stream, or 0 if there was no XRef stream.      */
specifier|public
name|long
name|getHighestXRefObjectNumber
parameter_list|()
block|{
return|return
name|highestXRefObjectNumber
return|;
block|}
comment|/**      * Internal PDFBox use only. Sets the object number of the highest XRef stream. This is needed      * to avoid reusing such a number in incremental saving.      *      * @param highestXRefObjectNumber The object number of the highest XRef stream.      */
specifier|public
name|void
name|setHighestXRefObjectNumber
parameter_list|(
name|long
name|highestXRefObjectNumber
parameter_list|)
block|{
name|this
operator|.
name|highestXRefObjectNumber
operator|=
name|highestXRefObjectNumber
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
annotation|@
name|Override
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
comment|// Make sure that:
comment|// - first Exception is kept
comment|// - all COSStreams are closed
comment|// - ScratchFile is closed
comment|// - there's a way to see which errors occured
name|IOException
name|firstException
init|=
literal|null
decl_stmt|;
comment|// close all open I/O streams
for|for
control|(
name|COSObject
name|object
range|:
name|getObjects
argument_list|()
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
if|if
condition|(
name|cosObject
operator|instanceof
name|COSStream
condition|)
block|{
name|firstException
operator|=
name|IOUtils
operator|.
name|closeAndLogException
argument_list|(
operator|(
name|COSStream
operator|)
name|cosObject
argument_list|,
name|LOG
argument_list|,
literal|"COSStream"
argument_list|,
name|firstException
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|COSStream
name|stream
range|:
name|streams
control|)
block|{
name|firstException
operator|=
name|IOUtils
operator|.
name|closeAndLogException
argument_list|(
name|stream
argument_list|,
name|LOG
argument_list|,
literal|"COSStream"
argument_list|,
name|firstException
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scratchFile
operator|!=
literal|null
condition|)
block|{
name|firstException
operator|=
name|IOUtils
operator|.
name|closeAndLogException
argument_list|(
name|scratchFile
argument_list|,
name|LOG
argument_list|,
literal|"ScratchFile"
argument_list|,
name|firstException
argument_list|)
expr_stmt|;
block|}
name|closed
operator|=
literal|true
expr_stmt|;
comment|// rethrow first exception to keep method contract
if|if
condition|(
name|firstException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|firstException
throw|;
block|}
block|}
block|}
comment|/**      * Returns true if this document has been closed.      */
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
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
comment|// xrefTable stores negated objNr of objStream for objects in objStreams
operator|||
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
comment|/**      * This will get an object from the pool.      *      * @param key The object key.      *      * @return The object in the pool or a new one if it has not been parsed yet.      */
specifier|public
name|COSObject
name|getObjectFromPool
parameter_list|(
name|COSObjectKey
name|key
parameter_list|)
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
name|key
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setGenerationNumber
argument_list|(
name|key
operator|.
name|getGeneration
argument_list|()
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
comment|/**      * Sets isXRefStream to the given value. You need to take care that the version of your PDF is      * 1.5 or higher.      *      * @param isXRefStreamValue the new value for isXRefStream      */
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

