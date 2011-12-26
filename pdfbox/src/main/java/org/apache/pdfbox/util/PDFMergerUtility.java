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
name|util
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
name|FileInputStream
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
name|COSInteger
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
name|COSNumber
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
name|PDDocumentInformation
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
name|PDPage
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
name|COSArrayList
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
name|PDStream
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
name|documentnavigation
operator|.
name|outline
operator|.
name|PDDocumentOutline
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
name|documentnavigation
operator|.
name|outline
operator|.
name|PDOutlineItem
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
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDField
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
name|form
operator|.
name|PDFieldFactory
import|;
end_import

begin_comment
comment|/**  * This class will take a list of pdf documents and merge them, saving the result  * in a new document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFMergerUtility
block|{
specifier|private
name|List
argument_list|<
name|InputStream
argument_list|>
name|sources
decl_stmt|;
specifier|private
name|String
name|destinationFileName
decl_stmt|;
specifier|private
name|OutputStream
name|destinationStream
decl_stmt|;
specifier|private
name|boolean
name|ignoreAcroFormErrors
init|=
literal|false
decl_stmt|;
comment|/**      * Instantiate a new PDFMergerUtility.      */
specifier|public
name|PDFMergerUtility
parameter_list|()
block|{
name|sources
operator|=
operator|new
name|ArrayList
argument_list|<
name|InputStream
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get the name of the destination file.      * @return Returns the destination.      */
specifier|public
name|String
name|getDestinationFileName
parameter_list|()
block|{
return|return
name|destinationFileName
return|;
block|}
comment|/**      * Set the name of the destination file.      * @param destination      *            The destination to set.      */
specifier|public
name|void
name|setDestinationFileName
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destinationFileName
operator|=
name|destination
expr_stmt|;
block|}
comment|/**      * Get the destination OutputStream.      * @return Returns the destination OutputStream.      */
specifier|public
name|OutputStream
name|getDestinationStream
parameter_list|()
block|{
return|return
name|destinationStream
return|;
block|}
comment|/**      * Set the destination OutputStream.      * @param destStream The destination to set.      */
specifier|public
name|void
name|setDestinationStream
parameter_list|(
name|OutputStream
name|destStream
parameter_list|)
block|{
name|destinationStream
operator|=
name|destStream
expr_stmt|;
block|}
comment|/**      * Add a source file to the list of files to merge.      *      * @param source Full path and file name of source document.      */
specifier|public
name|void
name|addSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
try|try
block|{
name|sources
operator|.
name|add
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|source
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Add a source file to the list of files to merge.      *      * @param source File representing source document      */
specifier|public
name|void
name|addSource
parameter_list|(
name|File
name|source
parameter_list|)
block|{
try|try
block|{
name|sources
operator|.
name|add
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Add a source to the list of documents to merge.      *      * @param source InputStream representing source document      */
specifier|public
name|void
name|addSource
parameter_list|(
name|InputStream
name|source
parameter_list|)
block|{
name|sources
operator|.
name|add
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a list of sources to the list of documents to merge.      *      * @param sourcesList List of InputStream objects representing source documents      */
specifier|public
name|void
name|addSources
parameter_list|(
name|List
argument_list|<
name|InputStream
argument_list|>
name|sourcesList
parameter_list|)
block|{
name|this
operator|.
name|sources
operator|.
name|addAll
argument_list|(
name|sources
argument_list|)
expr_stmt|;
block|}
comment|/**      * Merge the list of source documents, saving the result in the destination file.      *      * @throws IOException If there is an error saving the document.      * @throws COSVisitorException If an error occurs while saving the destination file.      */
specifier|public
name|void
name|mergeDocuments
parameter_list|()
throws|throws
name|IOException
throws|,
name|COSVisitorException
block|{
name|PDDocument
name|destination
init|=
literal|null
decl_stmt|;
name|InputStream
name|sourceFile
decl_stmt|;
name|PDDocument
name|source
decl_stmt|;
if|if
condition|(
name|sources
operator|!=
literal|null
operator|&&
name|sources
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|java
operator|.
name|util
operator|.
name|Vector
argument_list|<
name|PDDocument
argument_list|>
name|tobeclosed
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|Vector
argument_list|<
name|PDDocument
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|Iterator
argument_list|<
name|InputStream
argument_list|>
name|sit
init|=
name|sources
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|sourceFile
operator|=
name|sit
operator|.
name|next
argument_list|()
expr_stmt|;
name|destination
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|sourceFile
argument_list|)
expr_stmt|;
while|while
condition|(
name|sit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sourceFile
operator|=
name|sit
operator|.
name|next
argument_list|()
expr_stmt|;
name|source
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|sourceFile
argument_list|)
expr_stmt|;
name|tobeclosed
operator|.
name|add
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|appendDocument
argument_list|(
name|destination
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destinationStream
operator|==
literal|null
condition|)
block|{
name|destination
operator|.
name|save
argument_list|(
name|destinationFileName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|.
name|save
argument_list|(
name|destinationStream
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
name|destination
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|PDDocument
name|doc
range|:
name|tobeclosed
control|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * append all pages from source to destination.      *      * @param destination the document to receive the pages      * @param source the document originating the new pages      *      * @throws IOException If there is an error accessing data from either document.      */
specifier|public
name|void
name|appendDocument
parameter_list|(
name|PDDocument
name|destination
parameter_list|,
name|PDDocument
name|source
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|destination
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: destination PDF is encrypted, can't append encrypted PDF documents."
argument_list|)
throw|;
block|}
if|if
condition|(
name|source
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: source PDF is encrypted, can't append encrypted PDF documents."
argument_list|)
throw|;
block|}
name|PDDocumentInformation
name|destInfo
init|=
name|destination
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|PDDocumentInformation
name|srcInfo
init|=
name|source
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|destInfo
operator|.
name|getDictionary
argument_list|()
operator|.
name|mergeInto
argument_list|(
name|srcInfo
operator|.
name|getDictionary
argument_list|()
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|destCatalog
init|=
name|destination
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentCatalog
name|srcCatalog
init|=
name|source
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
comment|// use the highest version number for the resulting pdf
name|float
name|destVersion
init|=
name|destination
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|float
name|srcVersion
init|=
name|source
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|destVersion
operator|<
name|srcVersion
condition|)
block|{
name|destination
operator|.
name|getDocument
argument_list|()
operator|.
name|setVersion
argument_list|(
name|srcVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destCatalog
operator|.
name|getOpenAction
argument_list|()
operator|==
literal|null
condition|)
block|{
name|destCatalog
operator|.
name|setOpenAction
argument_list|(
name|srcCatalog
operator|.
name|getOpenAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// maybe there are some shared resources for all pages
name|COSDictionary
name|srcPages
init|=
operator|(
name|COSDictionary
operator|)
name|srcCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGES
argument_list|)
decl_stmt|;
name|COSDictionary
name|srcResources
init|=
operator|(
name|COSDictionary
operator|)
name|srcPages
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
name|COSDictionary
name|destPages
init|=
operator|(
name|COSDictionary
operator|)
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGES
argument_list|)
decl_stmt|;
name|COSDictionary
name|destResources
init|=
operator|(
name|COSDictionary
operator|)
name|destPages
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcResources
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|destResources
operator|!=
literal|null
condition|)
block|{
name|destResources
operator|.
name|mergeInto
argument_list|(
name|srcResources
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destPages
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|,
name|srcResources
argument_list|)
expr_stmt|;
block|}
block|}
name|PDFCloneUtility
name|cloner
init|=
operator|new
name|PDFCloneUtility
argument_list|(
name|destination
argument_list|)
decl_stmt|;
try|try
block|{
name|PDAcroForm
name|destAcroForm
init|=
name|destCatalog
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDAcroForm
name|srcAcroForm
init|=
name|srcCatalog
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
if|if
condition|(
name|destAcroForm
operator|==
literal|null
condition|)
block|{
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|srcAcroForm
argument_list|)
expr_stmt|;
name|destCatalog
operator|.
name|setAcroForm
argument_list|(
name|srcAcroForm
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|srcAcroForm
operator|!=
literal|null
condition|)
block|{
name|mergeAcroForm
argument_list|(
name|cloner
argument_list|,
name|destAcroForm
argument_list|,
name|srcAcroForm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// if we are not ignoring exceptions, we'll re-throw this
if|if
condition|(
operator|!
name|ignoreAcroFormErrors
condition|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|e
throw|;
block|}
block|}
name|COSArray
name|destThreads
init|=
operator|(
name|COSArray
operator|)
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|)
decl_stmt|;
name|COSArray
name|srcThreads
init|=
operator|(
name|COSArray
operator|)
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|destThreads
operator|==
literal|null
condition|)
block|{
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|,
name|srcThreads
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destThreads
operator|.
name|addAll
argument_list|(
name|srcThreads
argument_list|)
expr_stmt|;
block|}
name|PDDocumentNameDictionary
name|destNames
init|=
name|destCatalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
name|PDDocumentNameDictionary
name|srcNames
init|=
name|srcCatalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcNames
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|destNames
operator|==
literal|null
condition|)
block|{
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|,
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|srcNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cloner
operator|.
name|cloneMerge
argument_list|(
name|srcNames
argument_list|,
name|destNames
argument_list|)
expr_stmt|;
block|}
block|}
name|PDDocumentOutline
name|destOutline
init|=
name|destCatalog
operator|.
name|getDocumentOutline
argument_list|()
decl_stmt|;
name|PDDocumentOutline
name|srcOutline
init|=
name|srcCatalog
operator|.
name|getDocumentOutline
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcOutline
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|destOutline
operator|==
literal|null
condition|)
block|{
name|PDDocumentOutline
name|cloned
init|=
operator|new
name|PDDocumentOutline
argument_list|(
operator|(
name|COSDictionary
operator|)
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|srcOutline
argument_list|)
argument_list|)
decl_stmt|;
name|destCatalog
operator|.
name|setDocumentOutline
argument_list|(
name|cloned
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDOutlineItem
name|first
init|=
name|srcOutline
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|first
operator|!=
literal|null
condition|)
block|{
name|PDOutlineItem
name|clonedFirst
init|=
operator|new
name|PDOutlineItem
argument_list|(
operator|(
name|COSDictionary
operator|)
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|first
argument_list|)
argument_list|)
decl_stmt|;
name|destOutline
operator|.
name|appendChild
argument_list|(
name|clonedFirst
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|destPageMode
init|=
name|destCatalog
operator|.
name|getPageMode
argument_list|()
decl_stmt|;
name|String
name|srcPageMode
init|=
name|srcCatalog
operator|.
name|getPageMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|destPageMode
operator|==
literal|null
condition|)
block|{
name|destCatalog
operator|.
name|setPageMode
argument_list|(
name|srcPageMode
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|destLabels
init|=
operator|(
name|COSDictionary
operator|)
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGE_LABELS
argument_list|)
decl_stmt|;
name|COSDictionary
name|srcLabels
init|=
operator|(
name|COSDictionary
operator|)
name|srcCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGE_LABELS
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcLabels
operator|!=
literal|null
condition|)
block|{
name|int
name|destPageCount
init|=
name|destination
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|COSArray
name|destNums
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|destLabels
operator|==
literal|null
condition|)
block|{
name|destLabels
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|destNums
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|destLabels
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|,
name|destNums
argument_list|)
expr_stmt|;
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PAGE_LABELS
argument_list|,
name|destLabels
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destNums
operator|=
operator|(
name|COSArray
operator|)
name|destLabels
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|)
expr_stmt|;
block|}
name|COSArray
name|srcNums
init|=
operator|(
name|COSArray
operator|)
name|srcLabels
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcNums
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
name|srcNums
operator|.
name|size
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|COSNumber
name|labelIndex
init|=
operator|(
name|COSNumber
operator|)
name|srcNums
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|long
name|labelIndexValue
init|=
name|labelIndex
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|destNums
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|labelIndexValue
operator|+
name|destPageCount
argument_list|)
argument_list|)
expr_stmt|;
name|destNums
operator|.
name|add
argument_list|(
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|srcNums
operator|.
name|getObject
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|COSStream
name|destMetadata
init|=
operator|(
name|COSStream
operator|)
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
decl_stmt|;
name|COSStream
name|srcMetadata
init|=
operator|(
name|COSStream
operator|)
name|srcCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|destMetadata
operator|==
literal|null
operator|&&
name|srcMetadata
operator|!=
literal|null
condition|)
block|{
name|PDStream
name|newStream
init|=
operator|new
name|PDStream
argument_list|(
name|destination
argument_list|,
name|srcMetadata
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|newStream
operator|.
name|getStream
argument_list|()
operator|.
name|mergeInto
argument_list|(
name|srcMetadata
argument_list|)
expr_stmt|;
name|newStream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
name|destCatalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|,
name|newStream
argument_list|)
expr_stmt|;
block|}
comment|//finally append the pages
name|List
argument_list|<
name|PDPage
argument_list|>
name|pages
init|=
name|srcCatalog
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|PDPage
argument_list|>
name|pageIter
init|=
name|pages
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|pageIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PDPage
name|page
init|=
name|pageIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDPage
name|newPage
init|=
operator|new
name|PDPage
argument_list|(
operator|(
name|COSDictionary
operator|)
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|page
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|newPage
operator|.
name|setCropBox
argument_list|(
name|page
operator|.
name|findCropBox
argument_list|()
argument_list|)
expr_stmt|;
name|newPage
operator|.
name|setMediaBox
argument_list|(
name|page
operator|.
name|findMediaBox
argument_list|()
argument_list|)
expr_stmt|;
name|newPage
operator|.
name|setRotation
argument_list|(
name|page
operator|.
name|findRotation
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|addPage
argument_list|(
name|newPage
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|nextFieldNum
init|=
literal|1
decl_stmt|;
comment|/**      * Merge the contents of the source form into the destination form      * for the destination file.      *      * @param cloner the object cloner for the destination document      * @param destAcroForm the destination form      * @param srcAcroForm the source form      * @throws IOException If an error occurs while adding the field.      */
specifier|private
name|void
name|mergeAcroForm
parameter_list|(
name|PDFCloneUtility
name|cloner
parameter_list|,
name|PDAcroForm
name|destAcroForm
parameter_list|,
name|PDAcroForm
name|srcAcroForm
parameter_list|)
throws|throws
name|IOException
block|{
name|List
name|destFields
init|=
name|destAcroForm
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|List
name|srcFields
init|=
name|srcAcroForm
operator|.
name|getFields
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcFields
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|destFields
operator|==
literal|null
condition|)
block|{
name|destFields
operator|=
operator|new
name|COSArrayList
argument_list|()
expr_stmt|;
name|destAcroForm
operator|.
name|setFields
argument_list|(
name|destFields
argument_list|)
expr_stmt|;
block|}
name|Iterator
name|srcFieldsIterator
init|=
name|srcFields
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|srcFieldsIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PDField
name|srcField
init|=
operator|(
name|PDField
operator|)
name|srcFieldsIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDField
name|destField
init|=
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|destAcroForm
argument_list|,
operator|(
name|COSDictionary
operator|)
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|srcField
operator|.
name|getDictionary
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// if the form already has a field with this name then we need to rename this field
comment|// to prevent merge conflicts.
if|if
condition|(
name|destAcroForm
operator|.
name|getField
argument_list|(
name|destField
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|destField
operator|.
name|setPartialName
argument_list|(
literal|"dummyFieldName"
operator|+
operator|(
name|nextFieldNum
operator|++
operator|)
argument_list|)
expr_stmt|;
block|}
name|destFields
operator|.
name|add
argument_list|(
name|destField
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isIgnoreAcroFormErrors
parameter_list|()
block|{
return|return
name|ignoreAcroFormErrors
return|;
block|}
specifier|public
name|void
name|setIgnoreAcroFormErrors
parameter_list|(
name|boolean
name|ignoreAcroFormErrors
parameter_list|)
block|{
name|this
operator|.
name|ignoreAcroFormErrors
operator|=
name|ignoreAcroFormErrors
expr_stmt|;
block|}
block|}
end_class

end_unit

