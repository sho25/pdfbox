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
name|contentstream
operator|.
name|PDContentStream
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
name|COSStreamArray
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * A page in a PDF document.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDPage
implements|implements
name|COSObjectable
implements|,
name|PDContentStream
block|{
comment|/**      * Log instance      */
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
specifier|private
specifier|final
name|COSDictionary
name|page
decl_stmt|;
specifier|private
name|PDResources
name|pageResources
decl_stmt|;
specifier|private
name|PDRectangle
name|mediaBox
decl_stmt|;
comment|/**      * Creates a new PDPage instance for embedding, with a size of U.S. Letter (8.5 x 11 inches).      */
specifier|public
name|PDPage
parameter_list|()
block|{
name|this
argument_list|(
name|PDRectangle
operator|.
name|LETTER
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance of PDPage for embedding.      *       * @param mediaBox The MediaBox of the page.      */
specifier|public
name|PDPage
parameter_list|(
name|PDRectangle
name|mediaBox
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
name|page
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MEDIA_BOX
argument_list|,
name|mediaBox
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance of PDPage for reading.      *       * @param pageDictionary A page dictionary in a PDF document.      */
specifier|public
name|PDPage
parameter_list|(
name|COSDictionary
name|pageDictionary
parameter_list|)
block|{
name|page
operator|=
name|pageDictionary
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *       * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|page
return|;
block|}
annotation|@
name|Override
specifier|public
name|COSStream
name|getContentStream
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|page
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|(
name|COSStream
operator|)
name|base
return|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|COSStreamArray
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A dictionary containing any resources required by the page.      */
annotation|@
name|Override
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
name|PDPageTree
operator|.
name|getInheritableAttribute
argument_list|(
name|page
argument_list|,
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
comment|// note: it's an error for resources to not be present
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
comment|/**      * This will get the key of this Page in the structural parent tree.      *       * @return the integer key of the page's entry in the structural parent tree      */
specifier|public
name|int
name|getStructParents
parameter_list|()
block|{
return|return
name|page
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENTS
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the key for this page in the structural parent tree.      *       * @param structParents The new key for this page.      */
specifier|public
name|void
name|setStructParents
parameter_list|(
name|int
name|structParents
parameter_list|)
block|{
name|page
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENTS
argument_list|,
name|structParents
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDRectangle
name|getBBox
parameter_list|()
block|{
return|return
name|getCropBox
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
comment|// todo: take into account user-space unit redefinition as scale?
return|return
operator|new
name|Matrix
argument_list|()
return|;
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the boundaries of the physical      * medium on which the page is intended to be displayed or printed.      */
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
name|PDPageTree
operator|.
name|getInheritableAttribute
argument_list|(
name|page
argument_list|,
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
if|if
condition|(
name|mediaBox
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Can't find MediaBox, will use U.S. Letter"
argument_list|)
expr_stmt|;
name|mediaBox
operator|=
name|PDRectangle
operator|.
name|LETTER
expr_stmt|;
block|}
return|return
name|mediaBox
return|;
block|}
comment|/**      * This will set the mediaBox for this page.      *       * @param mediaBox The new mediaBox for this page.      */
specifier|public
name|void
name|setMediaBox
parameter_list|(
name|PDRectangle
name|mediaBox
parameter_list|)
block|{
name|this
operator|.
name|mediaBox
operator|=
name|mediaBox
expr_stmt|;
if|if
condition|(
name|mediaBox
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
name|mediaBox
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the visible region of default      * user space. When the page is displayed or printed, its contents are to be clipped (cropped)      * to this rectangle.      */
specifier|public
name|PDRectangle
name|getCropBox
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|PDPageTree
operator|.
name|getInheritableAttribute
argument_list|(
name|page
argument_list|,
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
return|return
name|clipToMediaBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getMediaBox
argument_list|()
return|;
block|}
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
comment|/**      * A rectangle, expressed in default user space units, defining the region to which the contents      * of the page should be clipped when output in a production environment. The default is the      * CropBox.      *       * @return The BleedBox attribute.      */
specifier|public
name|PDRectangle
name|getBleedBox
parameter_list|()
block|{
name|PDRectangle
name|retval
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
name|clipToMediaBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|getCropBox
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the intended dimensions of the      * finished page after trimming. The default is the CropBox.      *       * @return The TrimBox attribute.      */
specifier|public
name|PDRectangle
name|getTrimBox
parameter_list|()
block|{
name|PDRectangle
name|retval
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
name|clipToMediaBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|getCropBox
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A rectangle, expressed in default user space units, defining the extent of the page's      * meaningful content (including potential white space) as intended by the page's creator The      * default is the CropBox.      *       * @return The ArtBox attribute.      */
specifier|public
name|PDRectangle
name|getArtBox
parameter_list|()
block|{
name|PDRectangle
name|retval
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
name|clipToMediaBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|getCropBox
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Clips the given box to the bounds of the media box.      */
specifier|private
name|PDRectangle
name|clipToMediaBox
parameter_list|(
name|PDRectangle
name|box
parameter_list|)
block|{
name|PDRectangle
name|mediaBox
init|=
name|getMediaBox
argument_list|()
decl_stmt|;
name|PDRectangle
name|result
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|result
operator|.
name|setLowerLeftX
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|mediaBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|box
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setLowerLeftY
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|mediaBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|box
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setUpperRightX
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|mediaBox
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
name|box
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setUpperRightY
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|mediaBox
operator|.
name|getUpperRightY
argument_list|()
argument_list|,
name|box
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * A value representing the rotation. This will be null if not set at this level The number of      * degrees by which the page should be rotated clockwise when displayed or printed. The value      * must be a multiple of 90.      */
specifier|public
name|int
name|getRotation
parameter_list|()
block|{
name|COSNumber
name|value
init|=
operator|(
name|COSNumber
operator|)
name|PDPageTree
operator|.
name|getInheritableAttribute
argument_list|(
name|page
argument_list|,
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
return|return
name|value
operator|.
name|intValue
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * This will set the rotation for this page.      *       * @param rotation The new rotation for this page in degrees.      */
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
comment|/**      * This will get the contents of the PDF Page, in the case that the contents of the page is an      * array then then the entire array of streams will be be wrapped and appear as a single stream.      *       * @return The page content stream.      * @throws IOException If there is an error obtaining the stream.      */
specifier|public
name|PDStream
name|getStream
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
comment|/**      * This will get a list of PDThreadBead objects, which are article threads in the document.      * This will return an empty list of there are no thread beads.      *       * @return A list of article threads on this page.      */
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
comment|/**      * Get the metadata that is part of the document catalog. This will return null if there is      * no meta data for this object.      *       * @return The metadata for this object.      */
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
comment|/**      * This will return a list of the Annotations for this page.      *       * @return List of the PDAnnotation objects.      * @throws IOException If there is an error while creating the annotations.      */
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
if|if
condition|(
name|item
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
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
comment|/**      * This will set the list of annotations.      *       * @param annotations The new list of annotations.      */
specifier|public
name|void
name|setAnnotations
parameter_list|(
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annotations
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
name|annotations
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|page
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

