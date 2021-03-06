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
name|interactive
operator|.
name|annotation
operator|.
name|handlers
operator|.
name|PDAppearanceHandler
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
name|handlers
operator|.
name|PDLineAppearanceHandler
import|;
end_import

begin_comment
comment|/**  * This is the class that represents a line annotation. Introduced in PDF 1.3 specification  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationLine
extends|extends
name|PDAnnotationMarkup
block|{
specifier|private
name|PDAppearanceHandler
name|customAppearanceHandler
decl_stmt|;
comment|/*      * The various values for intent (get/setIT, see the PDF 1.6 reference Table 8.22      */
comment|/**      * Constant for annotation intent of Arrow.      */
specifier|public
specifier|static
specifier|final
name|String
name|IT_LINE_ARROW
init|=
literal|"LineArrow"
decl_stmt|;
comment|/**      * Constant for annotation intent of a dimension line.      */
specifier|public
specifier|static
specifier|final
name|String
name|IT_LINE_DIMENSION
init|=
literal|"LineDimension"
decl_stmt|;
comment|/*      * The various values for line ending styles, see the PDF 1.6 reference Table 8.23      */
comment|/**      * Constant for a square line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_SQUARE
init|=
literal|"Square"
decl_stmt|;
comment|/**      * Constant for a circle line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_CIRCLE
init|=
literal|"Circle"
decl_stmt|;
comment|/**      * Constant for a diamond line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_DIAMOND
init|=
literal|"Diamond"
decl_stmt|;
comment|/**      * Constant for a open arrow line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_OPEN_ARROW
init|=
literal|"OpenArrow"
decl_stmt|;
comment|/**      * Constant for a closed arrow line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_CLOSED_ARROW
init|=
literal|"ClosedArrow"
decl_stmt|;
comment|/**      * Constant for no line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_NONE
init|=
literal|"None"
decl_stmt|;
comment|/**      * Constant for a butt line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_BUTT
init|=
literal|"Butt"
decl_stmt|;
comment|/**      * Constant for a reversed open arrow line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_R_OPEN_ARROW
init|=
literal|"ROpenArrow"
decl_stmt|;
comment|/**      * Constant for a reversed closed arrow line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_R_CLOSED_ARROW
init|=
literal|"RClosedArrow"
decl_stmt|;
comment|/**      * Constant for a slash line ending.      */
specifier|public
specifier|static
specifier|final
name|String
name|LE_SLASH
init|=
literal|"Slash"
decl_stmt|;
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Line"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationLine
parameter_list|()
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
name|SUB_TYPE
argument_list|)
expr_stmt|;
comment|// Dictionary value L is mandatory, fill in with arbitrary value
name|setLine
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Line annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationLine
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
comment|/**      * This will set start and end coordinates of the line (or leader line if LL entry is set).      *      * @param l array of 4 floats [x1, y1, x2, y2] line start and end points in default user space.      */
specifier|public
name|void
name|setLine
parameter_list|(
name|float
index|[]
name|l
parameter_list|)
block|{
name|COSArray
name|newL
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newL
operator|.
name|setFloatArray
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|L
argument_list|,
name|newL
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the start and end coordinates of the line (or leader line if LL entry is set).      *      * @return array of floats [x1, y1, x2, y2] line start and end points in default user space.      */
specifier|public
name|float
index|[]
name|getLine
parameter_list|()
block|{
name|COSArray
name|l
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
name|L
argument_list|)
decl_stmt|;
return|return
name|l
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
comment|/**      * This will set the line ending style for the start point, see the LE_ constants for the possible values.      *      * @param style The new style.      */
specifier|public
name|void
name|setStartPointEndingStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|String
name|actualStyle
init|=
name|style
operator|==
literal|null
condition|?
name|PDAnnotationLine
operator|.
name|LE_NONE
else|:
name|style
decl_stmt|;
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
name|LE
argument_list|)
decl_stmt|;
name|COSArray
name|array
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSArray
operator|)
operator|||
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|actualStyle
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_NONE
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
name|LE
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|=
operator|(
name|COSArray
operator|)
name|base
expr_stmt|;
name|array
operator|.
name|setName
argument_list|(
literal|0
argument_list|,
name|actualStyle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will retrieve the line ending style for the start point, possible values shown in the LE_ constants section.      *      * @return The ending style for the start point, LE_NONE if missing, never null.      */
specifier|public
name|String
name|getStartPointEndingStyle
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
name|LE
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
return|return
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|getName
argument_list|(
literal|0
argument_list|,
name|LE_NONE
argument_list|)
return|;
block|}
return|return
name|LE_NONE
return|;
block|}
comment|/**      * This will set the line ending style for the end point, see the LE_ constants for the possible values.      *      * @param style The new style.      */
specifier|public
name|void
name|setEndPointEndingStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|String
name|actualStyle
init|=
name|style
operator|==
literal|null
condition|?
name|PDAnnotationLine
operator|.
name|LE_NONE
else|:
name|style
decl_stmt|;
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
name|LE
argument_list|)
decl_stmt|;
name|COSArray
name|array
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSArray
operator|)
operator|||
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_NONE
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|actualStyle
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
name|LE
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|=
operator|(
name|COSArray
operator|)
name|base
expr_stmt|;
name|array
operator|.
name|setName
argument_list|(
literal|1
argument_list|,
name|actualStyle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will retrieve the line ending style for the end point, possible values shown in the LE_ constants section.      *      * @return The ending style for the end point, LE_NONE if missing, never null.      */
specifier|public
name|String
name|getEndPointEndingStyle
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
name|LE
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
return|return
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|getName
argument_list|(
literal|1
argument_list|,
name|LE_NONE
argument_list|)
return|;
block|}
return|return
name|LE_NONE
return|;
block|}
comment|/**      * This will set interior color of the line endings defined in the LE entry. color is in DeviceRGB color space.      *      * @param ic color in the DeviceRGB color space.      */
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
comment|/**      * This will retrieve the interior color of the line endings defined in the LE entry. color is in DeviceRGB color      * space.      *      * @return object representing the color.      */
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
comment|/**      * This will set if the contents are shown as a caption to the line.      *      * @param cap Boolean value.      */
specifier|public
name|void
name|setCaption
parameter_list|(
name|boolean
name|cap
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|CAP
argument_list|,
name|cap
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve whether the text specified by the /Contents or /RC entries shall be      * shown as a caption in the appearance of the line.      *      * @return boolean if the contents shall be shown as a caption (default: false).      */
specifier|public
name|boolean
name|hasCaption
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|CAP
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will retrieve the length of the leader line.      *       * @return the length of the leader line      */
specifier|public
name|float
name|getLeaderLineLength
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LL
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line.      *       * @param leaderLineLength length of the leader line      */
specifier|public
name|void
name|setLeaderLineLength
parameter_list|(
name|float
name|leaderLineLength
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LL
argument_list|,
name|leaderLineLength
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the length of the leader line extensions.      *       * @return the length of the leader line extensions      */
specifier|public
name|float
name|getLeaderLineExtensionLength
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LLE
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line extensions.      *       * @param leaderLineExtensionLength length of the leader line extensions      */
specifier|public
name|void
name|setLeaderLineExtensionLength
parameter_list|(
name|float
name|leaderLineExtensionLength
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LLE
argument_list|,
name|leaderLineExtensionLength
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the length of the leader line offset.      *       * @return the length of the leader line offset      */
specifier|public
name|float
name|getLeaderLineOffsetLength
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LLO
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line offset.      *       * @param leaderLineOffsetLength length of the leader line offset      */
specifier|public
name|void
name|setLeaderLineOffsetLength
parameter_list|(
name|float
name|leaderLineOffsetLength
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LLO
argument_list|,
name|leaderLineOffsetLength
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the caption positioning.      *       * @return the caption positioning      */
specifier|public
name|String
name|getCaptionPositioning
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|CP
argument_list|)
return|;
block|}
comment|/**      * This will set the caption positioning. Allowed values are: "Inline" and "Top"      *       * @param captionPositioning caption positioning      */
specifier|public
name|void
name|setCaptionPositioning
parameter_list|(
name|String
name|captionPositioning
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|CP
argument_list|,
name|captionPositioning
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the horizontal offset of the caption.      *       * @param offset the horizontal offset of the caption      */
specifier|public
name|void
name|setCaptionHorizontalOffset
parameter_list|(
name|float
name|offset
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CO
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
name|array
operator|.
name|setFloatArray
argument_list|(
operator|new
name|float
index|[]
block|{
name|offset
block|,
literal|0.f
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CO
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will retrieve the horizontal offset of the caption.      *       * @return the horizontal offset of the caption      */
specifier|public
name|float
name|getCaptionHorizontalOffset
parameter_list|()
block|{
name|float
name|retval
init|=
literal|0.f
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CO
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
name|array
operator|.
name|toFloatArray
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the vertical offset of the caption.      *       * @param offset vertical offset of the caption      */
specifier|public
name|void
name|setCaptionVerticalOffset
parameter_list|(
name|float
name|offset
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CO
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
name|array
operator|.
name|setFloatArray
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0.f
block|,
name|offset
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CO
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|set
argument_list|(
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will retrieve the vertical offset of the caption.      *       * @return the vertical offset of the caption      */
specifier|public
name|float
name|getCaptionVerticalOffset
parameter_list|()
block|{
name|float
name|retval
init|=
literal|0.f
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CO
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
name|array
operator|.
name|toFloatArray
argument_list|()
index|[
literal|1
index|]
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param appearanceHandler      */
specifier|public
name|void
name|setCustomAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|appearanceHandler
parameter_list|)
block|{
name|customAppearanceHandler
operator|=
name|appearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|()
block|{
name|this
operator|.
name|constructAppearances
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
if|if
condition|(
name|customAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDLineAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDLineAppearanceHandler
argument_list|(
name|this
argument_list|,
name|document
argument_list|)
decl_stmt|;
name|appearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|customAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

