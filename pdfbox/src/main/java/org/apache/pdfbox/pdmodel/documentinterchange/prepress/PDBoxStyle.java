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
name|documentinterchange
operator|.
name|prepress
package|;
end_package

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
name|graphics
operator|.
name|PDLineDashPattern
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
name|PDColor
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
name|PDDeviceRGB
import|;
end_import

begin_comment
comment|/**  * The Box Style specifies visual characteristics for displaying box areas.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDBoxStyle
implements|implements
name|COSObjectable
block|{
comment|/**      * Style for guideline.      */
specifier|public
specifier|static
specifier|final
name|String
name|GUIDELINE_STYLE_SOLID
init|=
literal|"S"
decl_stmt|;
comment|/**      * Style for guideline.      */
specifier|public
specifier|static
specifier|final
name|String
name|GUIDELINE_STYLE_DASHED
init|=
literal|"D"
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default Constructor.      *      */
specifier|public
name|PDBoxStyle
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for an existing BoxStyle element.      *      * @param dic The existing dictionary.      */
specifier|public
name|PDBoxStyle
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|dictionary
operator|=
name|dic
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Get the RGB color to be used for the guidelines.  This is guaranteed to      * not return null. The default color is [0,0,0].      *      *@return The guideline color.      */
specifier|public
name|PDColor
name|getGuidelineColor
parameter_list|()
block|{
name|COSArray
name|colorValues
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|C
argument_list|)
decl_stmt|;
if|if
condition|(
name|colorValues
operator|==
literal|null
condition|)
block|{
name|colorValues
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|colorValues
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|colorValues
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|colorValues
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|colorValues
argument_list|)
expr_stmt|;
block|}
name|PDColor
name|color
init|=
operator|new
name|PDColor
argument_list|(
name|colorValues
operator|.
name|toFloatArray
argument_list|()
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
return|return
name|color
return|;
block|}
comment|/**      * Set the color space instance for this box style.  This must be a      * PDDeviceRGB!      *      * @param color The new colorspace value.      */
specifier|public
name|void
name|setGuideLineColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
block|{
name|COSArray
name|values
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|color
operator|!=
literal|null
condition|)
block|{
name|values
operator|=
name|color
operator|.
name|toCOSArray
argument_list|()
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the width of the of the guideline in default user space units.      * The default is 1.      *      * @return The width of the guideline.      */
specifier|public
name|float
name|getGuidelineWidth
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the guideline width.      *      * @param width The width in default user space units.      */
specifier|public
name|void
name|setGuidelineWidth
parameter_list|(
name|float
name|width
parameter_list|)
block|{
name|dictionary
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the style for the guideline.  The default is "S" for solid.      *      * @return The guideline style.      * @see PDBoxStyle#GUIDELINE_STYLE_DASHED      * @see PDBoxStyle#GUIDELINE_STYLE_SOLID      */
specifier|public
name|String
name|getGuidelineStyle
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|GUIDELINE_STYLE_SOLID
argument_list|)
return|;
block|}
comment|/**      * Set the style for the box.      *      * @param style The style for the box line.      * @see PDBoxStyle#GUIDELINE_STYLE_DASHED      * @see PDBoxStyle#GUIDELINE_STYLE_SOLID      */
specifier|public
name|void
name|setGuidelineStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|dictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the line dash pattern for this box style.  This is guaranteed to not      * return null.  The default is [3],0.      *      * @return The line dash pattern.      */
specifier|public
name|PDLineDashPattern
name|getLineDashPattern
parameter_list|()
block|{
name|PDLineDashPattern
name|pattern
decl_stmt|;
name|COSArray
name|d
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|d
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|THREE
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
name|COSArray
name|lineArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|lineArray
operator|.
name|add
argument_list|(
name|d
argument_list|)
expr_stmt|;
comment|//dash phase is not specified and assumed to be zero.
name|pattern
operator|=
operator|new
name|PDLineDashPattern
argument_list|(
name|lineArray
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|pattern
return|;
block|}
comment|/**      * Set the line dash pattern associated with this box style.      *      * @param dashArray The patter for this box style.      */
specifier|public
name|void
name|setLineDashPattern
parameter_list|(
name|COSArray
name|dashArray
parameter_list|)
block|{
name|COSArray
name|array
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dashArray
operator|!=
literal|null
condition|)
block|{
name|array
operator|=
name|dashArray
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

