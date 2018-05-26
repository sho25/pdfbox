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
name|interactive
operator|.
name|annotation
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
name|COSFloat
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
name|graphics
operator|.
name|color
operator|.
name|PDColor
import|;
end_import

begin_comment
comment|/**  * This is the class that represents a rectangular or elliptical annotation introduced in PDF 1.3  * specification .  *  * @author Paul King  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAnnotationSquareCircle
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * Creates a Circle or Square annotation of the specified sub type.      *      * @param subType the subtype the annotation represents.      */
specifier|protected
name|PDAnnotationSquareCircle
parameter_list|(
name|String
name|subType
parameter_list|)
block|{
name|setSubtype
argument_list|(
name|subType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The annotations dictionary.      */
specifier|protected
name|PDAnnotationSquareCircle
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|super
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|constructAppearances
parameter_list|()
function_decl|;
comment|/**      * This will set interior color of the drawn area color is in DeviceRGB colorspace.      *      * @param ic color in the DeviceRGB color space.      *      */
specifier|public
name|void
name|setInteriorColor
parameter_list|(
name|PDColor
name|ic
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|IC
argument_list|,
name|ic
operator|.
name|toCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the interior color of the drawn area color is in DeviceRGB color space.      *      * @return object representing the color.      */
specifier|public
name|PDColor
name|getInteriorColor
parameter_list|()
block|{
return|return
name|getColor
argument_list|(
name|COSName
operator|.
name|IC
argument_list|)
return|;
block|}
comment|/**      * This will set the border effect dictionary, specifying effects to be applied when drawing the      * line.      *      * @param be The border effect dictionary to set.      *      */
specifier|public
name|void
name|setBorderEffect
parameter_list|(
name|PDBorderEffectDictionary
name|be
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BE
argument_list|,
name|be
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border effect dictionary, specifying effects to be applied used in      * drawing the line.      *      * @return The border effect dictionary      */
specifier|public
name|PDBorderEffectDictionary
name|getBorderEffect
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BE
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDBorderEffectDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the rectangle difference rectangle. Giving the difference between the      * annotations rectangle and where the drawing occurs. (To take account of any effects applied      * through the BE entry for example)      *      * @param rd the rectangle difference      *      */
specifier|public
name|void
name|setRectDifference
parameter_list|(
name|PDRectangle
name|rd
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|,
name|rd
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the rectangle difference rectangle. Giving the difference between the      * annotations rectangle and where the drawing occurs. (To take account of any effects applied      * through the BE entry for example)      *      * @return the rectangle difference      */
specifier|public
name|PDRectangle
name|getRectDifference
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RD
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the border style dictionary, specifying the width and dash pattern used in      * drawing the line.      *      * @param bs the border style dictionary to set. TODO not all annotations may have a BS entry      *      */
annotation|@
name|Override
specifier|public
name|void
name|setBorderStyle
parameter_list|(
name|PDBorderStyleDictionary
name|bs
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BS
argument_list|,
name|bs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border style dictionary, specifying the width and dash pattern used in      * drawing the line.      *      * @return the border style dictionary. TODO not all annotations may have a BS entry      */
annotation|@
name|Override
specifier|public
name|PDBorderStyleDictionary
name|getBorderStyle
parameter_list|()
block|{
name|COSBase
name|bs
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BS
argument_list|)
decl_stmt|;
if|if
condition|(
name|bs
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDBorderStyleDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|bs
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the difference between the annotations "outer" rectangle defined by /Rect and      * the border.      *      *<p>      * This will set an equal difference for all sides</p>      *      * @param difference from the annotations /Rect entry      */
specifier|public
name|void
name|setRectDifferences
parameter_list|(
name|float
name|difference
parameter_list|)
block|{
name|setRectDifferences
argument_list|(
name|difference
argument_list|,
name|difference
argument_list|,
name|difference
argument_list|,
name|difference
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the difference between the annotations "outer" rectangle defined by      * /Rect and the border.      *       * @param differenceLeft left difference from the annotations /Rect entry      * @param differenceTop top difference from the annotations /Rect entry      * @param differenceRight right difference from  the annotations /Rect entry      * @param differenceBottom bottom difference from the annotations /Rect entry      *       */
specifier|public
name|void
name|setRectDifferences
parameter_list|(
name|float
name|differenceLeft
parameter_list|,
name|float
name|differenceTop
parameter_list|,
name|float
name|differenceRight
parameter_list|,
name|float
name|differenceBottom
parameter_list|)
block|{
name|COSArray
name|margins
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceLeft
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceTop
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceRight
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceBottom
argument_list|)
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|,
name|margins
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the differences between the annotations "outer" rectangle defined by      * /Rect and the border.      *       * @return the differences. If the entry hasn't been set am empty array is returned.      */
specifier|public
name|float
index|[]
name|getRectDifferences
parameter_list|()
block|{
name|COSBase
name|margin
init|=
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|)
decl_stmt|;
if|if
condition|(
name|margin
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
operator|(
name|COSArray
operator|)
name|margin
operator|)
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
return|return
operator|new
name|float
index|[]
block|{}
return|;
block|}
block|}
end_class

end_unit

