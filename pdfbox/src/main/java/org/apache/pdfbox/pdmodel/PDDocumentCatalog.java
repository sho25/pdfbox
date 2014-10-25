begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements. See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License. You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|PDDestinationOrAction
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
name|PDMetadata
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
name|PDPageLabels
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDMarkInfo
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDStructureTreeRoot
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
name|color
operator|.
name|PDOutputIntent
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
name|optionalcontent
operator|.
name|PDOptionalContentProperties
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
name|action
operator|.
name|PDActionFactory
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
name|action
operator|.
name|PDDocumentCatalogAdditionalActions
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
name|action
operator|.
name|PDURIDictionary
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
name|destination
operator|.
name|PDDestination
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
name|pagenavigation
operator|.
name|PDThread
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
name|viewerpreferences
operator|.
name|PDViewerPreferences
import|;
end_import

begin_comment
comment|/**  * The Document Catalog of a PDF.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDDocumentCatalog
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|root
decl_stmt|;
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|cachedAcroForm
decl_stmt|;
comment|/**      * Constructor. AcroForm.      *      * @param doc The document that this catalog is part of.      */
specifier|public
name|PDDocumentCatalog
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
name|root
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
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
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ROOT
argument_list|,
name|root
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param doc The document that this catalog is part of.      * @param rootDictionary The root dictionary that this object wraps.      */
specifier|public
name|PDDocumentCatalog
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|COSDictionary
name|rootDictionary
parameter_list|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
name|root
operator|=
name|rootDictionary
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Get the documents AcroForm. This will return null if no AcroForm is part of the document.      *      * @return The document's AcroForm.      */
specifier|public
name|PDAcroForm
name|getAcroForm
parameter_list|()
block|{
if|if
condition|(
name|cachedAcroForm
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ACRO_FORM
argument_list|)
decl_stmt|;
name|cachedAcroForm
operator|=
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|,
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|cachedAcroForm
return|;
block|}
comment|/**      * Sets the AcroForm for this catalog.      *      * @param acroForm The new AcroForm.      */
specifier|public
name|void
name|setAcroForm
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ACRO_FORM
argument_list|,
name|acroForm
argument_list|)
expr_stmt|;
name|cachedAcroForm
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Returns all pages in the document, as a page tree.      */
specifier|public
name|PDPageTree
name|getPages
parameter_list|()
block|{
comment|// todo: cache me?
return|return
operator|new
name|PDPageTree
argument_list|(
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGES
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Get the viewer preferences associated with this document or null if they do not exist.      *      * @return The document's viewer preferences.      */
specifier|public
name|PDViewerPreferences
name|getViewerPreferences
parameter_list|()
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|VIEWER_PREFERENCES
argument_list|)
decl_stmt|;
return|return
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDViewerPreferences
argument_list|(
name|dict
argument_list|)
return|;
block|}
comment|/**      * Sets the viewer preferences.      *      * @param prefs The new viewer preferences.      */
specifier|public
name|void
name|setViewerPreferences
parameter_list|(
name|PDViewerPreferences
name|prefs
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|VIEWER_PREFERENCES
argument_list|,
name|prefs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the outline associated with this document or null if it does not exist.      *      * @return The document's outline.      */
specifier|public
name|PDDocumentOutline
name|getDocumentOutline
parameter_list|()
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OUTLINES
argument_list|)
decl_stmt|;
return|return
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDDocumentOutline
argument_list|(
name|dict
argument_list|)
return|;
block|}
comment|/**      * Sets the document outlines.      *      * @param outlines The new document outlines.      */
specifier|public
name|void
name|setDocumentOutline
parameter_list|(
name|PDDocumentOutline
name|outlines
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OUTLINES
argument_list|,
name|outlines
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the document?s article threads.      */
specifier|public
name|List
argument_list|<
name|PDThread
argument_list|>
name|getThreads
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|PDThread
argument_list|>
name|pdObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|PDThread
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
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|pdObjects
operator|.
name|add
argument_list|(
operator|new
name|PDThread
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|COSArrayList
argument_list|<
name|PDThread
argument_list|>
argument_list|(
name|pdObjects
argument_list|,
name|array
argument_list|)
return|;
block|}
comment|/**      * Sets the list of threads for this pdf document.      *      * @param threads The list of threads, or null to clear it.      */
specifier|public
name|void
name|setThreads
parameter_list|(
name|List
name|threads
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|THREADS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|threads
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the metadata that is part of the document catalog. This will return null if there is no      * meta data for this object.      *      * @return The metadata for this object.      */
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
name|COSBase
name|metaObj
init|=
name|root
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
name|metaObj
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|new
name|PDMetadata
argument_list|(
operator|(
name|COSStream
operator|)
name|metaObj
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the metadata for this object. This can be null.      *      * @param meta The meta data for this object.      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the Document Open Action for this object.      *      * @param action The action you want to perform.      */
specifier|public
name|void
name|setOpenAction
parameter_list|(
name|PDDestinationOrAction
name|action
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPEN_ACTION
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the Document Open Action for this object.      *      * @return The action to perform when the document is opened.      * @throws IOException If there is an error creating the destination or action.      */
specifier|public
name|PDDestinationOrAction
name|getOpenAction
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|openAction
init|=
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OPEN_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|openAction
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|openAction
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
name|PDActionFactory
operator|.
name|createAction
argument_list|(
operator|(
name|COSDictionary
operator|)
name|openAction
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|openAction
operator|instanceof
name|COSArray
condition|)
block|{
return|return
name|PDDestination
operator|.
name|create
argument_list|(
name|openAction
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown OpenAction "
operator|+
name|openAction
argument_list|)
throw|;
block|}
block|}
comment|/**      * @return The Additional Actions for this Document      */
specifier|public
name|PDDocumentCatalogAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSDictionary
name|addAction
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AA
argument_list|)
decl_stmt|;
if|if
condition|(
name|addAction
operator|==
literal|null
condition|)
block|{
name|addAction
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|addAction
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDDocumentCatalogAdditionalActions
argument_list|(
name|addAction
argument_list|)
return|;
block|}
comment|/**      * Sets the additional actions for the document.      *      * @param actions The actions that are associated with this document.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDDocumentCatalogAdditionalActions
name|actions
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|actions
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The names dictionary for this document or null if none exist.      */
specifier|public
name|PDDocumentNameDictionary
name|getNames
parameter_list|()
block|{
name|COSDictionary
name|names
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|)
decl_stmt|;
return|return
name|names
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDDocumentNameDictionary
argument_list|(
name|this
argument_list|,
name|names
argument_list|)
return|;
block|}
comment|/**      * Sets the names dictionary for the document.      *      * @param names The names dictionary that is associated with this document.      */
specifier|public
name|void
name|setNames
parameter_list|(
name|PDDocumentNameDictionary
name|names
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get info about doc's usage of tagged features. This will return null if there is no      * information.      *      * @return The new mark info.      */
specifier|public
name|PDMarkInfo
name|getMarkInfo
parameter_list|()
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MARK_INFO
argument_list|)
decl_stmt|;
return|return
name|dic
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDMarkInfo
argument_list|(
name|dic
argument_list|)
return|;
block|}
comment|/**      * Set information about the doc's usage of tagged features.      *      * @param markInfo The new MarkInfo data.      */
specifier|public
name|void
name|setMarkInfo
parameter_list|(
name|PDMarkInfo
name|markInfo
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MARK_INFO
argument_list|,
name|markInfo
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of OutputIntents defined in the document.      *      * @return The list of PDOutputIntent      */
specifier|public
name|List
argument_list|<
name|PDOutputIntent
argument_list|>
name|getOutputIntent
parameter_list|()
block|{
name|List
argument_list|<
name|PDOutputIntent
argument_list|>
name|retval
init|=
operator|new
name|ArrayList
argument_list|<
name|PDOutputIntent
argument_list|>
argument_list|()
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|root
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OUTPUT_INTENTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|COSBase
name|cosBase
range|:
name|array
control|)
block|{
name|PDOutputIntent
name|oi
init|=
operator|new
name|PDOutputIntent
argument_list|(
operator|(
name|COSStream
operator|)
name|cosBase
argument_list|)
decl_stmt|;
name|retval
operator|.
name|add
argument_list|(
name|oi
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Add an OutputIntent to the list.  If there is not OutputIntent, the list is created and the      * first  element added.      *      * @param outputIntent the OutputIntent to add.      */
specifier|public
name|void
name|addOutputIntent
parameter_list|(
name|PDOutputIntent
name|outputIntent
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|root
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OUTPUT_INTENTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OUTPUT_INTENTS
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
name|array
operator|.
name|add
argument_list|(
name|outputIntent
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Replace the list of OutputIntents of the document.      *      * @param outputIntents the list of OutputIntents, if the list is empty all OutputIntents are      * removed.      */
specifier|public
name|void
name|setOutputIntents
parameter_list|(
name|List
argument_list|<
name|PDOutputIntent
argument_list|>
name|outputIntents
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDOutputIntent
name|intent
range|:
name|outputIntents
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|intent
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OUTPUT_INTENTS
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page display mode.      */
specifier|public
name|PageMode
name|getPageMode
parameter_list|()
block|{
name|String
name|mode
init|=
name|root
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|PAGE_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
return|return
name|PageMode
operator|.
name|fromString
argument_list|(
name|mode
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|PageMode
operator|.
name|USE_NONE
return|;
block|}
block|}
comment|/**      * Sets the page mode.      *      * @param mode The new page mode.      */
specifier|public
name|void
name|setPageMode
parameter_list|(
name|PageMode
name|mode
parameter_list|)
block|{
name|root
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|PAGE_MODE
argument_list|,
name|mode
operator|.
name|stringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page layout.      */
specifier|public
name|PageLayout
name|getPageLayout
parameter_list|()
block|{
name|String
name|mode
init|=
name|root
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|PAGE_LAYOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
return|return
name|PageLayout
operator|.
name|fromString
argument_list|(
name|mode
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|PageLayout
operator|.
name|SINGLE_PAGE
return|;
block|}
block|}
comment|/**      * Sets the page layout.      *      * @param layout The new page layout.      */
specifier|public
name|void
name|setPageLayout
parameter_list|(
name|PageLayout
name|layout
parameter_list|)
block|{
name|root
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|PAGE_LAYOUT
argument_list|,
name|layout
operator|.
name|stringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the document-level URI.      */
specifier|public
name|PDURIDictionary
name|getURI
parameter_list|()
block|{
name|COSDictionary
name|uri
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|URI
argument_list|)
decl_stmt|;
return|return
name|uri
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDURIDictionary
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Sets the document level URI.      *      * @param uri The new document level URI.      */
specifier|public
name|void
name|setURI
parameter_list|(
name|PDURIDictionary
name|uri
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|URI
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the document's structure tree root, or null if none exists.      */
specifier|public
name|PDStructureTreeRoot
name|getStructureTreeRoot
parameter_list|()
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|STRUCT_TREE_ROOT
argument_list|)
decl_stmt|;
return|return
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDStructureTreeRoot
argument_list|(
name|dict
argument_list|)
return|;
block|}
comment|/**      * Sets the document's structure tree root.      *      * @param treeRoot The new structure tree.      */
specifier|public
name|void
name|setStructureTreeRoot
parameter_list|(
name|PDStructureTreeRoot
name|treeRoot
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|STRUCT_TREE_ROOT
argument_list|,
name|treeRoot
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the language for the document, or null.      */
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|root
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|LANG
argument_list|)
return|;
block|}
comment|/**      * Sets the Language for the document.      *      * @param language The new document language.      */
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|root
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|LANG
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the PDF specification version this document conforms to.      *      * @return the PDF version (e.g. "1.4")      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|root
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|VERSION
argument_list|)
return|;
block|}
comment|/**      * Sets the PDF specification version this document conforms to.      *      * @param version the PDF version (e.g. "1.4")      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|root
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|VERSION
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page labels descriptor of the document.      *      * @return the page labels descriptor of the document.      * @throws IOException If there is a problem retrieving the page labels.      */
specifier|public
name|PDPageLabels
name|getPageLabels
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PAGE_LABELS
argument_list|)
decl_stmt|;
return|return
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDPageLabels
argument_list|(
name|document
argument_list|,
name|dict
argument_list|)
return|;
block|}
comment|/**      * Sets the page label descriptor for the document.      *      * @param labels the new page label descriptor to set.      */
specifier|public
name|void
name|setPageLabels
parameter_list|(
name|PDPageLabels
name|labels
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PAGE_LABELS
argument_list|,
name|labels
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the optional content properties dictionary associated with this document.      *      * @return the optional properties dictionary or null if it is not present      */
specifier|public
name|PDOptionalContentProperties
name|getOCProperties
parameter_list|()
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OCPROPERTIES
argument_list|)
decl_stmt|;
return|return
name|dict
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|PDOptionalContentProperties
argument_list|(
name|dict
argument_list|)
return|;
block|}
comment|/**      * Sets the optional content properties dictionary.      *      * @param ocProperties the optional properties dictionary      */
specifier|public
name|void
name|setOCProperties
parameter_list|(
name|PDOptionalContentProperties
name|ocProperties
parameter_list|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OCPROPERTIES
argument_list|,
name|ocProperties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

