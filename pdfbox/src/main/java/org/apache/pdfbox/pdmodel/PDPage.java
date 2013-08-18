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
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
name|PDRectangle
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
name|action
operator|.
name|PDPageAdditionalActions
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
name|annotation
operator|.
name|PDAnnotation
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
name|PDThreadBead
import|;
end_import

begin_comment
comment|/**  * This represents a single page in a PDF document.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDPage
implements|implements
name|COSObjectable
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
name|PDPage
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Default DPI in user space.      */
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_USER_SPACE_UNIT_DPI
init|=
literal|72
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|float
name|MM_TO_UNITS
init|=
literal|1
operator|/
operator|(
literal|10
operator|*
literal|2.54f
operator|)
operator|*
name|DEFAULT_USER_SPACE_UNIT_DPI
decl_stmt|;
specifier|private
name|COSDictionary
name|page
decl_stmt|;
specifier|private
name|PDResources
name|pageResources
decl_stmt|;
comment|/**      * A page size of LETTER or 8.5x11.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_LETTER
init|=
operator|new
name|PDRectangle
argument_list|(
literal|8.5f
operator|*
name|DEFAULT_USER_SPACE_UNIT_DPI
argument_list|,
literal|11f
operator|*
name|DEFAULT_USER_SPACE_UNIT_DPI
argument_list|)
decl_stmt|;
comment|/**      * A page size of A0 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A0
init|=
operator|new
name|PDRectangle
argument_list|(
literal|841
operator|*
name|MM_TO_UNITS
argument_list|,
literal|1189
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A1 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A1
init|=
operator|new
name|PDRectangle
argument_list|(
literal|594
operator|*
name|MM_TO_UNITS
argument_list|,
literal|841
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A2 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A2
init|=
operator|new
name|PDRectangle
argument_list|(
literal|420
operator|*
name|MM_TO_UNITS
argument_list|,
literal|594
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A3 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A3
init|=
operator|new
name|PDRectangle
argument_list|(
literal|297
operator|*
name|MM_TO_UNITS
argument_list|,
literal|420
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A4 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A4
init|=
operator|new
name|PDRectangle
argument_list|(
literal|210
operator|*
name|MM_TO_UNITS
argument_list|,
literal|297
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A5 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A5
init|=
operator|new
name|PDRectangle
argument_list|(
literal|148
operator|*
name|MM_TO_UNITS
argument_list|,
literal|210
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * A page size of A6 Paper.      */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|PAGE_SIZE_A6
init|=
operator|new
name|PDRectangle
argument_list|(
literal|105
operator|*
name|MM_TO_UNITS
argument_list|,
literal|148
operator|*
name|MM_TO_UNITS
argument_list|)
decl_stmt|;
comment|/**      * Creates a new instance of PDPage with a size of 8.5x11.      */
specifier|public
name|PDPage
parameter_list|()
block|{
name|page
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|PAGE
argument_list|)
expr_stmt|;
name|setMediaBox
argument_list|(
name|PAGE_SIZE_LETTER
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance of PDPage.      *       * @param size The MediaBox or the page.      */
specifier|public
name|PDPage
parameter_list|(
name|PDRectangle
name|size
parameter_list|)
block|{
name|page
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|PAGE
argument_list|)
expr_stmt|;
name|setMediaBox
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance of PDPage.      *       * @param pageDic The existing page dictionary.      */
specifier|public
name|PDPage
parameter_list|(
name|COSDictionary
name|pageDic
parameter_list|)
block|{
name|page
operator|=
name|pageDic
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *       * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * This will get the underlying dictionary that this class acts on.      *       * @return The underlying dictionary for this class.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * This is the parent page node. The parent is a required element of the page. This will be null until this page is      * added to the document.      *       * @return The parent to this page.      */
specifier|public
name|PDPageNode
name|getParent
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|parentDic
init|=
operator|(
name|COSDictionary
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentDic
operator|!=
literal|null
condition|)
block|{
name|parent
operator|=
operator|new
name|PDPageNode
argument_list|(
name|parentDic
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|parent
return|;
block|}
specifier|private
name|PDPageNode
name|parent
init|=
literal|null
decl_stmt|;
comment|/**      * This will set the parent of this page.      *       * @param parentNode The parent to this page node.      */
specifier|public
name|void
name|setParent
parameter_list|(
name|PDPageNode
name|parentNode
parameter_list|)
block|{
name|parent
operator|=
name|parentNode
expr_stmt|;
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|parent
operator|.
name|getDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will update the last modified time for the page object.      */
specifier|public
name|void
name|updateLastModified
parameter_list|()
block|{
name|page
operator|.
name|setDate
argument_list|(
name|COSName
operator|.
name|LAST_MODIFIED
argument_list|,
operator|new
name|GregorianCalendar
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the date that the content stream was last modified. This may return null.      *       * @return The date the content stream was last modified.      *       * @throws IOException If there is an error accessing the date information.      */
specifier|public
name|Calendar
name|getLastModified
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|page
operator|.
name|getDate
argument_list|(
name|COSName
operator|.
name|LAST_MODIFIED
argument_list|)
return|;
block|}
comment|/**      * This will get the resources at this page and not look up the hierarchy. This attribute is inheritable, and      * findResources() should probably used. This will return null if no resources are available at this level.      *       * @return The resources at this level in the hierarchy.      */
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
if|if
condition|(
name|pageResources
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|resources
init|=
operator|(
name|COSDictionary
operator|)
name|page
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
name|resources
operator|!=
literal|null
condition|)
block|{
name|pageResources
operator|=
operator|new
name|PDResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|pageResources
return|;
block|}
comment|/**      * This will find the resources for this page by looking up the hierarchy until it finds them.      *       * @return The resources at this level in the hierarchy.      */
specifier|public
name|PDResources
name|findResources
parameter_list|()
block|{
name|PDResources
name|retval
init|=
name|getResources
argument_list|()
decl_stmt|;
name|PDPageNode
name|parentNode
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|parentNode
operator|.
name|findResources
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the resources for this page.      *       * @param resources The new resources for this page.      */
specifier|public
name|void
name|setResources
parameter_list|(
name|PDResources
name|resources
parameter_list|)
block|{
name|pageResources
operator|=
name|resources
expr_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the boundaries of the physical medium on which the      * page is intended to be displayed or printed      *       * This will get the MediaBox at this page and not look up the hierarchy. This attribute is inheritable, and      * findMediaBox() should probably used. This will return null if no MediaBox are available at this level.      *       * @return The MediaBox at this level in the hierarchy.      */
specifier|public
name|PDRectangle
name|getMediaBox
parameter_list|()
block|{
if|if
condition|(
name|mediaBox
operator|==
literal|null
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MEDIA_BOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|mediaBox
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mediaBox
return|;
block|}
specifier|private
name|PDRectangle
name|mediaBox
init|=
literal|null
decl_stmt|;
comment|/**      * This will find the MediaBox for this page by looking up the hierarchy until it finds them.      *       * @return The MediaBox at this level in the hierarchy.      */
specifier|public
name|PDRectangle
name|findMediaBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
name|getMediaBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
operator|&&
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|getParent
argument_list|()
operator|.
name|findMediaBox
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Can't find MediaBox, using LETTER as default pagesize!"
argument_list|)
expr_stmt|;
name|retval
operator|=
name|PDPage
operator|.
name|PAGE_SIZE_LETTER
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the mediaBox for this page.      *       * @param mediaBoxValue The new mediaBox for this page.      */
specifier|public
name|void
name|setMediaBox
parameter_list|(
name|PDRectangle
name|mediaBoxValue
parameter_list|)
block|{
name|this
operator|.
name|mediaBox
operator|=
name|mediaBoxValue
expr_stmt|;
if|if
condition|(
name|mediaBoxValue
operator|==
literal|null
condition|)
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|MEDIA_BOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MEDIA_BOX
argument_list|,
name|mediaBoxValue
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the visible region of default user space. When the      * page is displayed or printed, its contents are to be clipped (cropped) to this rectangle and then imposed on the      * output medium in some implementationdefined manner      *       * This will get the CropBox at this page and not look up the hierarchy. This attribute is inheritable, and      * findCropBox() should probably used. This will return null if no CropBox is available at this level.      *       * @return The CropBox at this level in the hierarchy.      */
specifier|public
name|PDRectangle
name|getCropBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CROP_BOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will find the CropBox for this page by looking up the hierarchy until it finds them.      *       * @return The CropBox at this level in the hierarchy.      */
specifier|public
name|PDRectangle
name|findCropBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
name|getCropBox
argument_list|()
decl_stmt|;
name|PDPageNode
name|parentNode
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
operator|&&
name|parentNode
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|findParentCropBox
argument_list|(
name|parentNode
argument_list|)
expr_stmt|;
block|}
comment|// default value for cropbox is the media box
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|findMediaBox
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will search for a crop box in the parent and return null if it is not found. It will NOT default to the      * media box if it cannot be found.      *       * @param node The node      */
specifier|private
name|PDRectangle
name|findParentCropBox
parameter_list|(
name|PDPageNode
name|node
parameter_list|)
block|{
name|PDRectangle
name|rect
init|=
name|node
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|PDPageNode
name|parentNode
init|=
name|node
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|rect
operator|==
literal|null
operator|&&
name|parentNode
operator|!=
literal|null
condition|)
block|{
name|rect
operator|=
name|findParentCropBox
argument_list|(
name|parentNode
argument_list|)
expr_stmt|;
block|}
return|return
name|rect
return|;
block|}
comment|/**      * This will set the CropBox for this page.      *       * @param cropBox The new CropBox for this page.      */
specifier|public
name|void
name|setCropBox
parameter_list|(
name|PDRectangle
name|cropBox
parameter_list|)
block|{
if|if
condition|(
name|cropBox
operator|==
literal|null
condition|)
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|CROP_BOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CROP_BOX
argument_list|,
name|cropBox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the region to which the contents of the page should      * be clipped when output in a production environment. The default is the CropBox.      *       * @return The BleedBox attribute.      */
specifier|public
name|PDRectangle
name|getBleedBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BLEED_BOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|findCropBox
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the BleedBox for this page.      *       * @param bleedBox The new BleedBox for this page.      */
specifier|public
name|void
name|setBleedBox
parameter_list|(
name|PDRectangle
name|bleedBox
parameter_list|)
block|{
if|if
condition|(
name|bleedBox
operator|==
literal|null
condition|)
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|BLEED_BOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BLEED_BOX
argument_list|,
name|bleedBox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the intended dimensions of the finished page after      * trimming. The default is the CropBox.      *       * @return The TrimBox attribute.      */
specifier|public
name|PDRectangle
name|getTrimBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TRIM_BOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|findCropBox
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the TrimBox for this page.      *       * @param trimBox The new TrimBox for this page.      */
specifier|public
name|void
name|setTrimBox
parameter_list|(
name|PDRectangle
name|trimBox
parameter_list|)
block|{
if|if
condition|(
name|trimBox
operator|==
literal|null
condition|)
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|TRIM_BOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TRIM_BOX
argument_list|,
name|trimBox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the extent of the page's meaningful content      * (including potential white space) as intended by the page's creator The default isthe CropBox.      *       * @return The ArtBox attribute.      */
specifier|public
name|PDRectangle
name|getArtBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ART_BOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|findCropBox
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the ArtBox for this page.      *       * @param artBox The new ArtBox for this page.      */
specifier|public
name|void
name|setArtBox
parameter_list|(
name|PDRectangle
name|artBox
parameter_list|)
block|{
if|if
condition|(
name|artBox
operator|==
literal|null
condition|)
block|{
name|page
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|ART_BOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ART_BOX
argument_list|,
name|artBox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// todo BoxColorInfo
comment|// todo Contents
comment|/**      * A value representing the rotation. This will be null if not set at this level The number of degrees by which the      * page should be rotated clockwise when displayed or printed. The value must be a multiple of 90.      *       * This will get the rotation at this page and not look up the hierarchy. This attribute is inheritable, and      * findRotation() should probably used. This will return null if no rotation is available at this level.      *       * @return The rotation at this level in the hierarchy.      */
specifier|public
name|Integer
name|getRotation
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|value
init|=
operator|(
name|COSNumber
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ROTATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|value
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will find the rotation for this page by looking up the hierarchy until it finds them.      *       * @return The rotation at this level in the hierarchy.      */
specifier|public
name|int
name|findRotation
parameter_list|()
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
name|Integer
name|rotation
init|=
name|getRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|rotation
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|rotation
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|PDPageNode
name|parentNode
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentNode
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|parentNode
operator|.
name|findRotation
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the rotation for this page.      *       * @param rotation The new rotation for this page.      */
specifier|public
name|void
name|setRotation
parameter_list|(
name|int
name|rotation
parameter_list|)
block|{
name|page
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|ROTATE
argument_list|,
name|rotation
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the contents of the PDF Page, in the case that the contents of the page is an array then then the      * entire array of streams will be be wrapped and appear as a single stream.      *       * @return The page content stream.      *       * @throws IOException If there is an error obtaining the stream.      */
specifier|public
name|PDStream
name|getContents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDStream
operator|.
name|createFromCOS
argument_list|(
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the contents of this page.      *       * @param contents The new contents of the page.      */
specifier|public
name|void
name|setContents
parameter_list|(
name|PDStream
name|contents
parameter_list|)
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|,
name|contents
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a list of PDThreadBead objects, which are article threads in the document. This will return an      * empty list of there are no thread beads.      *       * @return A list of article threads on this page.      */
specifier|public
name|List
argument_list|<
name|PDThreadBead
argument_list|>
name|getThreadBeads
parameter_list|()
block|{
name|COSArray
name|beads
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|B
argument_list|)
decl_stmt|;
if|if
condition|(
name|beads
operator|==
literal|null
condition|)
block|{
name|beads
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|PDThreadBead
argument_list|>
name|pdObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|PDThreadBead
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
name|beads
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|beadDic
init|=
operator|(
name|COSDictionary
operator|)
name|beads
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|PDThreadBead
name|bead
init|=
literal|null
decl_stmt|;
comment|// in some cases the bead is null
if|if
condition|(
name|beadDic
operator|!=
literal|null
condition|)
block|{
name|bead
operator|=
operator|new
name|PDThreadBead
argument_list|(
name|beadDic
argument_list|)
expr_stmt|;
block|}
name|pdObjects
operator|.
name|add
argument_list|(
name|bead
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|COSArrayList
argument_list|<
name|PDThreadBead
argument_list|>
argument_list|(
name|pdObjects
argument_list|,
name|beads
argument_list|)
return|;
block|}
comment|/**      * This will set the list of thread beads.      *       * @param beads A list of PDThreadBead objects or null.      */
specifier|public
name|void
name|setThreadBeads
parameter_list|(
name|List
argument_list|<
name|PDThreadBead
argument_list|>
name|beads
parameter_list|)
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|B
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|beads
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the metadata that is part of the document catalog. This will return null if there is no meta data for this      * object.      *       * @return The metadata for this object.      */
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
name|page
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
comment|/**      * Set the metadata for this object. This can be null.      *       * @param meta The meta data for this object.      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
name|page
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
comment|/**      * Get the page actions.      *       * @return The Actions for this Page      */
specifier|public
name|PDPageAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSDictionary
name|addAct
init|=
operator|(
name|COSDictionary
operator|)
name|page
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
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|addAct
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDPageAdditionalActions
argument_list|(
name|addAct
argument_list|)
return|;
block|}
comment|/**      * Set the page actions.      *       * @param actions The actions for the page.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDPageAdditionalActions
name|actions
parameter_list|)
block|{
name|page
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
comment|/**      * This will return a list of the Annotations for this page.      *       * @return List of the PDAnnotation objects.      *       * @throws IOException If there is an error while creating the annotations.      */
specifier|public
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|getAnnotations
parameter_list|()
throws|throws
name|IOException
block|{
name|COSArrayList
argument_list|<
name|PDAnnotation
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|annots
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ANNOTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|annots
operator|==
literal|null
condition|)
block|{
name|annots
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ANNOTS
argument_list|,
name|annots
argument_list|)
expr_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDAnnotation
argument_list|>
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|PDAnnotation
argument_list|>
argument_list|()
argument_list|,
name|annots
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|PDAnnotation
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
name|annots
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|item
init|=
name|annots
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|actuals
operator|.
name|add
argument_list|(
name|PDAnnotation
operator|.
name|createAnnotation
argument_list|(
name|item
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDAnnotation
argument_list|>
argument_list|(
name|actuals
argument_list|,
name|annots
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of annotations.      *       * @param annots The new list of annotations.      */
specifier|public
name|void
name|setAnnotations
parameter_list|(
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annots
parameter_list|)
block|{
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ANNOTS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|annots
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
return|return
name|other
operator|instanceof
name|PDPage
operator|&&
operator|(
operator|(
name|PDPage
operator|)
name|other
operator|)
operator|.
name|getCOSObject
argument_list|()
operator|==
name|this
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

