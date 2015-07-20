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
operator|.
name|graphics
operator|.
name|pattern
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
name|PDResources
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

begin_comment
comment|/**  * A tiling pattern dictionary.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDTilingPattern
extends|extends
name|PDAbstractPattern
implements|implements
name|PDContentStream
block|{
comment|/** paint type 1 = colored tiling pattern. */
specifier|public
specifier|static
specifier|final
name|int
name|PAINT_COLORED
init|=
literal|1
decl_stmt|;
comment|/** paint type 2 = uncolored tiling pattern. */
specifier|public
specifier|static
specifier|final
name|int
name|PAINT_UNCOLORED
init|=
literal|2
decl_stmt|;
comment|/** tiling type 1 = constant spacing.*/
specifier|public
specifier|static
specifier|final
name|int
name|TILING_CONSTANT_SPACING
init|=
literal|1
decl_stmt|;
comment|/**  tiling type 2 = no distortion. */
specifier|public
specifier|static
specifier|final
name|int
name|TILING_NO_DISTORTION
init|=
literal|2
decl_stmt|;
comment|/** tiling type 3 = constant spacing and faster tiling. */
specifier|public
specifier|static
specifier|final
name|int
name|TILING_CONSTANT_SPACING_FASTER_TILING
init|=
literal|3
decl_stmt|;
comment|/**      * Creates a new tiling pattern.      */
specifier|public
name|PDTilingPattern
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PATTERN_TYPE
argument_list|,
name|PDAbstractPattern
operator|.
name|TYPE_TILING_PATTERN
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new tiling pattern from the given COS dictionary.      * @param resourceDictionary The COSDictionary for this pattern resource.      */
specifier|public
name|PDTilingPattern
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getPatternType
parameter_list|()
block|{
return|return
name|PDAbstractPattern
operator|.
name|TYPE_TILING_PATTERN
return|;
block|}
comment|/**      * This will set the length of the content stream.      * @param length The new stream length.      */
annotation|@
name|Override
specifier|public
name|void
name|setLength
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the length of the content stream.      * @return The length of the content stream      */
annotation|@
name|Override
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the paint type.      * @param paintType The new paint type.      */
annotation|@
name|Override
specifier|public
name|void
name|setPaintType
parameter_list|(
name|int
name|paintType
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PAINT_TYPE
argument_list|,
name|paintType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the paint type.      * @return The paint type      */
specifier|public
name|int
name|getPaintType
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|PAINT_TYPE
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the tiling type.      * @param tilingType The new tiling type.      */
specifier|public
name|void
name|setTilingType
parameter_list|(
name|int
name|tilingType
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|TILING_TYPE
argument_list|,
name|tilingType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the tiling type.      * @return The tiling type      */
specifier|public
name|int
name|getTilingType
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|TILING_TYPE
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the XStep value.      * @param xStep The new XStep value.      */
specifier|public
name|void
name|setXStep
parameter_list|(
name|float
name|xStep
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|X_STEP
argument_list|,
name|xStep
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the XStep value.      * @return The XStep value      */
specifier|public
name|float
name|getXStep
parameter_list|()
block|{
comment|// ignores invalid values, see PDFBOX-1094-065514-XStep32767.pdf
name|float
name|xStep
init|=
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|X_STEP
argument_list|,
literal|0
argument_list|)
decl_stmt|;
return|return
name|xStep
operator|==
name|Short
operator|.
name|MAX_VALUE
condition|?
literal|0
else|:
name|xStep
return|;
block|}
comment|/**      * This will set the YStep value.      * @param yStep The new YStep value.      */
specifier|public
name|void
name|setYStep
parameter_list|(
name|float
name|yStep
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|Y_STEP
argument_list|,
name|yStep
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the YStep value.      * @return The YStep value      */
specifier|public
name|float
name|getYStep
parameter_list|()
block|{
comment|// ignores invalid values, see PDFBOX-1094-065514-XStep32767.pdf
name|float
name|yStep
init|=
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|Y_STEP
argument_list|,
literal|0
argument_list|)
decl_stmt|;
return|return
name|yStep
operator|==
name|Short
operator|.
name|MAX_VALUE
condition|?
literal|0
else|:
name|yStep
return|;
block|}
specifier|public
name|PDStream
name|getContentStream
parameter_list|()
block|{
return|return
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|getCOSObject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getContents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
operator|(
name|COSStream
operator|)
name|getCOSObject
argument_list|()
operator|)
operator|.
name|getUnfilteredStream
argument_list|()
return|;
block|}
comment|/**      * This will get the resources for this pattern.      * This will return null if no resources are available at this level.      * @return The resources for this pattern.      */
annotation|@
name|Override
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
name|PDResources
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|resources
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
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
name|retval
operator|=
operator|new
name|PDResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the resources for this pattern.      * @param resources The new resources for this pattern.      */
specifier|public
name|void
name|setResources
parameter_list|(
name|PDResources
name|resources
parameter_list|)
block|{
name|getCOSObject
argument_list|()
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
comment|/**      * An array of four numbers in the form coordinate system (see      * below), giving the coordinates of the left, bottom, right, and top edges,      * respectively, of the pattern's bounding box.      *      * @return The BBox of the pattern.      */
annotation|@
name|Override
specifier|public
name|PDRectangle
name|getBBox
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
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BBOX
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
comment|/**      * This will set the BBox (bounding box) for this Pattern.      * @param bbox The new BBox for this Pattern.      */
specifier|public
name|void
name|setBBox
parameter_list|(
name|PDRectangle
name|bbox
parameter_list|)
block|{
if|if
condition|(
name|bbox
operator|==
literal|null
condition|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|,
name|bbox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

