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
name|HashMap
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
name|io
operator|.
name|RandomAccess
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
name|RandomAccessFile
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This is the in-memory representation of the PDF document.  You need to call  * close() on this object when you are done using it!!  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.28 $  */
end_comment

begin_class
specifier|public
class|class
name|COSDocument
extends|extends
name|COSBase
block|{
specifier|private
name|float
name|version
decl_stmt|;
comment|/**      * Maps ObjectKeys to a COSObject. Note that references to these objects      * are also stored in COSDictionary objects that map a name to a specific object.       */
specifier|private
name|Map
name|objectPool
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * Maps object and generation ids to object byte offsets      */
specifier|private
name|Map
name|xrefTable
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * Document trailer dictionary.      */
specifier|private
name|COSDictionary
name|trailer
decl_stmt|;
comment|/**      * This file will store the streams in order to conserve memory.      */
specifier|private
name|RandomAccess
name|scratchFile
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|tmpFile
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|headerString
init|=
literal|"%PDF-1.4"
decl_stmt|;
comment|/**      * Constructor.  Uses the java.io.tmpdir value to create a file      * to store the streams.      *      *  @throws IOException If there is an error creating the tmp file.      */
specifier|public
name|COSDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will create a create a scratch file in the      * following directory.      *      * @param scratchDir The directory to store a scratch file.      *      *  @throws IOException If there is an error creating the tmp file.      */
specifier|public
name|COSDocument
parameter_list|(
name|File
name|scratchDir
parameter_list|)
throws|throws
name|IOException
block|{
name|tmpFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"pdfbox"
argument_list|,
literal|"tmp"
argument_list|,
name|scratchDir
argument_list|)
expr_stmt|;
name|scratchFile
operator|=
operator|new
name|RandomAccessFile
argument_list|(
name|tmpFile
argument_list|,
literal|"rw"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that will use the following random access file for storage      * of the PDF streams.  The client of this method is responsible for deleting      * the storage if necessary that this file will write to.  The close method      * will close the file though.      *      * @param file The random access file to use for storage.      */
specifier|public
name|COSDocument
parameter_list|(
name|RandomAccess
name|file
parameter_list|)
block|{
name|scratchFile
operator|=
name|file
expr_stmt|;
block|}
comment|/**      * This will get the scratch file for this document.      *      * @return The scratch file.      */
specifier|public
name|RandomAccess
name|getScratchFile
parameter_list|()
block|{
return|return
name|scratchFile
return|;
block|}
comment|/**      * This will get the first dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
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
comment|/**      * This will get the first dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
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
name|COSObject
name|retval
init|=
literal|null
decl_stmt|;
name|Iterator
name|iter
init|=
name|objectPool
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
operator|&&
name|retval
operator|==
literal|null
condition|)
block|{
name|COSObject
name|object
init|=
operator|(
name|COSObject
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|COSName
name|objectType
init|=
operator|(
name|COSName
operator|)
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
name|objectType
operator|!=
literal|null
operator|&&
name|objectType
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|retval
operator|=
name|object
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|logger
argument_list|()
operator|.
name|warning
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|+
literal|"\n at\n"
operator|+
name|FullStackTrace
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get all dictionary objects by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
specifier|public
name|List
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
comment|/**      * This will get a dictionary object by type.      *      * @param type The type of the object.      *      * @return This will return an object with the specified type.      */
specifier|public
name|List
name|getObjectsByType
parameter_list|(
name|COSName
name|type
parameter_list|)
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|iter
init|=
name|objectPool
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSObject
name|object
init|=
operator|(
name|COSObject
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|COSName
name|objectType
init|=
operator|(
name|COSName
operator|)
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
name|objectType
operator|!=
literal|null
operator|&&
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
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|logger
argument_list|()
operator|.
name|warning
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|+
literal|"\n at\n"
operator|+
name|FullStackTrace
argument_list|(
name|e
argument_list|)
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
name|Iterator
name|iter
init|=
name|objectPool
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSObject
name|object
init|=
operator|(
name|COSObject
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
literal|"Encrypt"
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
name|getPDFName
argument_list|(
literal|"Encrypt"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the encryption dictionary, this should only be called when      * encypting the document.      *      * @param encDictionary The encryption dictionary.      */
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
name|getPDFName
argument_list|(
literal|"Encrypt"
argument_list|)
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
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ID"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"ID"
argument_list|)
argument_list|,
name|id
argument_list|)
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
name|getObjects
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
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
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws COSVisitorException If an error occurs while visiting this object.      */
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|COSVisitorException
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
name|scratchFile
operator|!=
literal|null
condition|)
block|{
name|scratchFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|scratchFile
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|tmpFile
operator|!=
literal|null
condition|)
block|{
name|tmpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tmpFile
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * The sole purpose of this is to inform a client of PDFBox that they      * did not close the document.      */
specifier|protected
name|void
name|finalize
parameter_list|()
block|{
if|if
condition|(
name|tmpFile
operator|!=
literal|null
operator|||
name|scratchFile
operator|!=
literal|null
condition|)
block|{
name|Throwable
name|t
init|=
operator|new
name|Throwable
argument_list|(
literal|"Warning: You did not close the PDF Document"
argument_list|)
decl_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
name|Iterator
name|objStm
init|=
name|getObjectsByType
argument_list|(
literal|"ObjStm"
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|objStm
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSObject
name|objStream
init|=
operator|(
name|COSObject
operator|)
name|objStm
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|Iterator
name|compressedObjects
init|=
name|parser
operator|.
name|getObjects
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|compressedObjects
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSObject
name|next
init|=
operator|(
name|COSObject
operator|)
name|compressedObjects
operator|.
name|next
argument_list|()
decl_stmt|;
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
name|next
argument_list|)
decl_stmt|;
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
operator|(
name|COSObject
operator|)
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
operator|new
name|COSInteger
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
operator|new
name|COSInteger
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
comment|/**      * Used to populate the XRef HashMap. Will add an Xreftable entry      * that maps ObjectKeys to byte offsets in the file.       * @param objKey The objkey, with id and gen numbers      * @param currOffset The byte offset in this file      */
specifier|public
name|void
name|setXRef
parameter_list|(
name|COSObjectKey
name|objKey
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|xrefTable
operator|.
name|put
argument_list|(
name|objKey
argument_list|,
operator|new
name|Integer
argument_list|(
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the xrefTable which is a mapping of ObjectKeys      * to byte offsets in the file.       * @return      */
specifier|public
name|Map
name|getXrefTable
parameter_list|()
block|{
return|return
name|xrefTable
return|;
block|}
block|}
end_class

end_unit

