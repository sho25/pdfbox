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
name|interactive
operator|.
name|action
operator|.
name|type
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
comment|/**  * This class represents the acroform of a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.21 $  */
end_comment

begin_class
specifier|public
class|class
name|PDDocumentCatalog
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|root
decl_stmt|;
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
init|=
literal|null
decl_stmt|;
comment|/**      * Page mode where neither the outline nor the thumbnails      * are displayed.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_USE_NONE
init|=
literal|"UseNone"
decl_stmt|;
comment|/**      * Show bookmarks when pdf is opened.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_USE_OUTLINES
init|=
literal|"UseOutlines"
decl_stmt|;
comment|/**      * Show thumbnails when pdf is opened.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_USE_THUMBS
init|=
literal|"UseThumbs"
decl_stmt|;
comment|/**      * Full screen mode with no menu bar, window controls.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_FULL_SCREEN
init|=
literal|"FullScreen"
decl_stmt|;
comment|/**      * Optional content group panel is visible when opened.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_USE_OPTIONAL_CONTENT
init|=
literal|"UseOC"
decl_stmt|;
comment|/**      * Attachments panel is visible.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_MODE_USE_ATTACHMENTS
init|=
literal|"UseAttachments"
decl_stmt|;
comment|/**      * Display one page at a time.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_SINGLE_PAGE
init|=
literal|"SinglePage"
decl_stmt|;
comment|/**      * Display the pages in one column.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_ONE_COLUMN
init|=
literal|"OneColumn"
decl_stmt|;
comment|/**      * Display the pages in two columns, with odd numbered pagse on the left.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_TWO_COLUMN_LEFT
init|=
literal|"TwoColumnLeft"
decl_stmt|;
comment|/**      * Display the pages in two columns, with odd numbered pagse on the right.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_TWO_COLUMN_RIGHT
init|=
literal|"TwoColumnRight"
decl_stmt|;
comment|/**      * Display the pages two at a time, with odd-numbered pages on the left.      * @since PDF Version 1.5      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_TWO_PAGE_LEFT
init|=
literal|"TwoPageLeft"
decl_stmt|;
comment|/**      * Display the pages two at a time, with odd-numbered pages on the right.      * @since PDF Version 1.5      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_LAYOUT_TWO_PAGE_RIGHT
init|=
literal|"TwoPageRight"
decl_stmt|;
comment|/**      * Constructor.      *      * @param doc The document that this catalog is part of.      */
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
operator|new
name|COSString
argument_list|(
literal|"Catalog"
argument_list|)
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
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * This will get the documents acroform.  This will return null if      * no acroform is part of the document.      *      * @return The documents acroform.      */
specifier|public
name|PDAcroForm
name|getAcroForm
parameter_list|()
block|{
if|if
condition|(
name|acroForm
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|acroFormDic
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
if|if
condition|(
name|acroFormDic
operator|!=
literal|null
condition|)
block|{
name|acroForm
operator|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|,
name|acroFormDic
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|acroForm
return|;
block|}
comment|/**      * Set the acro form for this catalog.      *      * @param acro The new acro form.      */
specifier|public
name|void
name|setAcroForm
parameter_list|(
name|PDAcroForm
name|acro
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
name|acro
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the root node for the pages.      *      * @return The parent page node.      */
specifier|public
name|PDPageNode
name|getPages
parameter_list|()
block|{
return|return
operator|new
name|PDPageNode
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
comment|/**      * The PDF document contains a hierarchical structure of PDPageNode and PDPages, which      * is mostly just a way to store this information.  This method will return a flat list      * of all PDPage objects in this document.      *      * @return A list of PDPage objects.      */
specifier|public
name|List
name|getAllPages
parameter_list|()
block|{
name|List
name|retval
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|PDPageNode
name|rootNode
init|=
name|getPages
argument_list|()
decl_stmt|;
comment|//old (slower):
comment|//getPageObjects( rootNode, retval );
name|rootNode
operator|.
name|getAllKids
argument_list|(
name|retval
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Get the viewer preferences associated with this document or null if they      * do not exist.      *      * @return The document's viewer preferences.      */
specifier|public
name|PDViewerPreferences
name|getViewerPreferences
parameter_list|()
block|{
name|PDViewerPreferences
name|retval
init|=
literal|null
decl_stmt|;
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
literal|"ViewerPreferences"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDViewerPreferences
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the viewer preferences.      *      * @param prefs The new viewer preferences.      */
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
literal|"ViewerPreferences"
argument_list|,
name|prefs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the outline associated with this document or null if it      * does not exist.      *      * @return The document's outline.      */
specifier|public
name|PDDocumentOutline
name|getDocumentOutline
parameter_list|()
block|{
name|PDDocumentOutline
name|retval
init|=
literal|null
decl_stmt|;
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
literal|"Outlines"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDDocumentOutline
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the document outlines.      *      * @param outlines The new document outlines.      */
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
literal|"Outlines"
argument_list|,
name|outlines
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list threads for this pdf document.      *      * @return A list of PDThread objects.      */
specifier|public
name|List
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
literal|"Threads"
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
literal|"Threads"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
name|List
name|pdObjects
init|=
operator|new
name|ArrayList
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
argument_list|(
name|pdObjects
argument_list|,
name|array
argument_list|)
return|;
block|}
comment|/**      * Set the list of threads for this pdf document.      *      * @param threads The list of threads, or null to clear it.      */
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
literal|"Threads"
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
comment|/**      * Get the metadata that is part of the document catalog.  This will      * return null if there is no meta data for this object.      *      * @return The metadata for this object.      */
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
name|PDMetadata
name|retval
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
literal|"Metadata"
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDMetadata
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the metadata for this object.  This can be null.      *      * @param meta The meta data for this object.      */
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
literal|"Metadata"
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the Document Open Action for this object.      *      * @param action The action you want to perform.      */
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
name|getPDFName
argument_list|(
literal|"OpenAction"
argument_list|)
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the Document Open Action for this object.      *      * @return The action to perform when the document is opened.      *      * @throws IOException If there is an error creating the destination      * or action.      */
specifier|public
name|PDDestinationOrAction
name|getOpenAction
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDestinationOrAction
name|action
init|=
literal|null
decl_stmt|;
name|COSBase
name|actionObj
init|=
name|root
operator|.
name|getDictionaryObject
argument_list|(
literal|"OpenAction"
argument_list|)
decl_stmt|;
if|if
condition|(
name|actionObj
operator|==
literal|null
condition|)
block|{
comment|//no op
block|}
elseif|else
if|if
condition|(
name|actionObj
operator|instanceof
name|COSDictionary
condition|)
block|{
name|action
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
operator|(
name|COSDictionary
operator|)
name|actionObj
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|actionObj
operator|instanceof
name|COSArray
condition|)
block|{
name|action
operator|=
name|PDDestination
operator|.
name|create
argument_list|(
name|actionObj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown OpenAction "
operator|+
name|actionObj
argument_list|)
throw|;
block|}
return|return
name|action
return|;
block|}
comment|/**      * @return The Additional Actions for this Document      */
specifier|public
name|PDDocumentCatalogAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSDictionary
name|addAct
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
literal|"AA"
argument_list|)
decl_stmt|;
if|if
condition|(
name|addAct
operator|==
literal|null
condition|)
block|{
name|addAct
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
literal|"AA"
argument_list|,
name|addAct
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDDocumentCatalogAdditionalActions
argument_list|(
name|addAct
argument_list|)
return|;
block|}
comment|/**      * Set the additional actions for the document.      *      * @param actions The actions that are associated with this document.      */
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
literal|"AA"
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
name|PDDocumentNameDictionary
name|nameDic
init|=
literal|null
decl_stmt|;
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
literal|"Names"
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|nameDic
operator|=
operator|new
name|PDDocumentNameDictionary
argument_list|(
name|this
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
return|return
name|nameDic
return|;
block|}
comment|/**      * Set the names dictionary for the document.      *      * @param names The names dictionary that is associated with this document.      */
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
literal|"Names"
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get info about doc's usage of tagged features.  This will return      * null if there is no information.      *      * @return The new mark info.      */
specifier|public
name|PDMarkInfo
name|getMarkInfo
parameter_list|()
block|{
name|PDMarkInfo
name|retval
init|=
literal|null
decl_stmt|;
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
literal|"MarkInfo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDMarkInfo
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
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
literal|"MarkInfo"
argument_list|,
name|markInfo
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the page display mode, see the PAGE_MODE_XXX constants.      * @return A string representing the page mode.      */
specifier|public
name|String
name|getPageMode
parameter_list|()
block|{
return|return
name|root
operator|.
name|getNameAsString
argument_list|(
literal|"PageMode"
argument_list|,
name|PAGE_MODE_USE_NONE
argument_list|)
return|;
block|}
comment|/**      * Set the page mode.  See the PAGE_MODE_XXX constants for valid values.      * @param mode The new page mode.      */
specifier|public
name|void
name|setPageMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|root
operator|.
name|setName
argument_list|(
literal|"PageMode"
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the page layout, see the PAGE_LAYOUT_XXX constants.      * @return A string representing the page layout.      */
specifier|public
name|String
name|getPageLayout
parameter_list|()
block|{
return|return
name|root
operator|.
name|getNameAsString
argument_list|(
literal|"PageLayout"
argument_list|,
name|PAGE_LAYOUT_SINGLE_PAGE
argument_list|)
return|;
block|}
comment|/**      * Set the page layout.  See the PAGE_LAYOUT_XXX constants for valid values.      * @param layout The new page layout.      */
specifier|public
name|void
name|setPageLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|root
operator|.
name|setName
argument_list|(
literal|"PageLayout"
argument_list|,
name|layout
argument_list|)
expr_stmt|;
block|}
comment|/**      * Document level information in the URI.      * @return Document level URI.      */
specifier|public
name|PDURIDictionary
name|getURI
parameter_list|()
block|{
name|PDURIDictionary
name|retval
init|=
literal|null
decl_stmt|;
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
literal|"URI"
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDURIDictionary
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the document level uri.      * @param uri The new document level uri.      */
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
literal|"URI"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the document's structure tree root.      *      * @return The document's structure tree root or null if none exists.      */
specifier|public
name|PDStructureTreeRoot
name|getStructureTreeRoot
parameter_list|()
block|{
name|PDStructureTreeRoot
name|treeRoot
init|=
literal|null
decl_stmt|;
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
literal|"StructTreeRoot"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|treeRoot
operator|=
operator|new
name|PDStructureTreeRoot
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|treeRoot
return|;
block|}
comment|/**      * Set the document's structure tree root.      *      * @param treeRoot The new structure tree.      */
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
literal|"StructTreeRoot"
argument_list|,
name|treeRoot
argument_list|)
expr_stmt|;
block|}
comment|/**      * The language for the document.      *      * @return The language for the document.      */
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
literal|"Lang"
argument_list|)
return|;
block|}
comment|/**      * Set the Language for the document.      *      * @param language The new document language.      */
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
literal|"Lang"
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

