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
comment|/**  * This is the class that represents a rectangular or eliptical annotation  * Introduced in PDF 1.3 specification .  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationSquareCircle
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * Constant for a Rectangular type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_SQUARE
init|=
literal|"Square"
decl_stmt|;
comment|/**      * Constant for an Eliptical type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_CIRCLE
init|=
literal|"Circle"
decl_stmt|;
comment|/**      * Creates a Circle or Square annotation of the specified sub type.      *      * @param subType the subtype the annotation represents.          */
specifier|public
name|PDAnnotationSquareCircle
parameter_list|(
name|String
name|subType
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|setSubtype
argument_list|(
name|subType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Line annotation from a COSDictionary, expected to be a correct      * object definition.      *      * @param field      *            the PDF objet to represent as a field.      */
specifier|public
name|PDAnnotationSquareCircle
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set interior color of the drawn area      * color is in DeviceRGB colo rspace.      *      * @param ic color in the DeviceRGB color space.      *      */
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
comment|/**      * This will retrieve the interior color of the drawn area      * color is in DeviceRGB color space.      *      * @return  object representing the color.      */
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
comment|/**      * This will set the border effect dictionary, specifying effects to be applied      * when drawing the line.      *      * @param be The border effect dictionary to set.      *      */
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
literal|"BE"
argument_list|,
name|be
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border effect dictionary, specifying effects to be      * applied used in drawing the line.      *      * @return The border effect dictionary      */
specifier|public
name|PDBorderEffectDictionary
name|getBorderEffect
parameter_list|()
block|{
name|COSDictionary
name|be
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"BE"
argument_list|)
decl_stmt|;
if|if
condition|(
name|be
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDBorderEffectDictionary
argument_list|(
name|be
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the rectangle difference rectangle. Giving the difference      * between the annotations rectangle and where the drawing occurs.          * (To take account of any effects applied through the BE entry forexample)      *      * @param rd the rectangle difference      *      */
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
literal|"RD"
argument_list|,
name|rd
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the rectangle difference rectangle. Giving the difference      * between the annotations rectangle and where the drawing occurs.          * (To take account of any effects applied through the BE entry forexample)      *      * @return the rectangle difference      */
specifier|public
name|PDRectangle
name|getRectDifference
parameter_list|()
block|{
name|COSArray
name|rd
init|=
operator|(
name|COSArray
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"RD"
argument_list|)
decl_stmt|;
if|if
condition|(
name|rd
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|rd
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the sub type (and hence appearance, AP taking precedence) For      * this annotation. See the SUB_TYPE_XXX constants for valid values.      *      * @param subType The subtype of the annotation      */
specifier|public
name|void
name|setSubtype
parameter_list|(
name|String
name|subType
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|subType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the sub type (and hence appearance, AP taking precedence)      * For this annotation.      *      * @return The subtype of this annotation, see the SUB_TYPE_XXX constants.      */
annotation|@
name|Override
specifier|public
name|String
name|getSubtype
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
return|;
block|}
comment|/**      * This will set the border style dictionary, specifying the width and dash      * pattern used in drawing the line.      *      * @param bs the border style dictionary to set.      * TODO not all annotations may have a BS entry      *      */
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
literal|"BS"
argument_list|,
name|bs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border style dictionary, specifying the width and      * dash pattern used in drawing the line.      *      * @return the border style dictionary.      * TODO not all annotations may have a BS entry      */
specifier|public
name|PDBorderStyleDictionary
name|getBorderStyle
parameter_list|()
block|{
name|COSDictionary
name|bs
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|BS
argument_list|)
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDBorderStyleDictionary
argument_list|(
name|bs
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

