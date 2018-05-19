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
name|fdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * This represents a Line FDF annotation.  *  * @author Ben Litchfield  * @author Johanneke Lamberink  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationLine
extends|extends
name|FDFAnnotation
block|{
comment|/**      * COS Model value for SubType entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBTYPE
init|=
literal|"Line"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFAnnotationLine
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a An existing FDF Annotation.      */
specifier|public
name|FDFAnnotationLine
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param element An XFDF element.      *      * @throws IOException If there is an error extracting information from the element.      */
specifier|public
name|FDFAnnotationLine
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
name|String
name|startCoords
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"start"
argument_list|)
decl_stmt|;
if|if
condition|(
name|startCoords
operator|==
literal|null
operator|||
name|startCoords
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: missing attribute 'start'"
argument_list|)
throw|;
block|}
name|String
name|endCoords
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"end"
argument_list|)
decl_stmt|;
if|if
condition|(
name|endCoords
operator|==
literal|null
operator|||
name|endCoords
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: missing attribute 'end'"
argument_list|)
throw|;
block|}
name|String
name|line
init|=
name|startCoords
operator|+
literal|","
operator|+
name|endCoords
decl_stmt|;
name|String
index|[]
name|lineValues
init|=
name|line
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|lineValues
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: wrong amount of line coordinates"
argument_list|)
throw|;
block|}
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
literal|4
index|]
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
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|lineValues
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|setLine
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|String
name|leaderLine
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"leaderLength"
argument_list|)
decl_stmt|;
if|if
condition|(
name|leaderLine
operator|!=
literal|null
operator|&&
operator|!
name|leaderLine
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setLeaderLength
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|leaderLine
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|leaderLineExtension
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"leaderExtend"
argument_list|)
decl_stmt|;
if|if
condition|(
name|leaderLineExtension
operator|!=
literal|null
operator|&&
operator|!
name|leaderLineExtension
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setLeaderExtend
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|leaderLineExtension
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|leaderLineOffset
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"leaderOffset"
argument_list|)
decl_stmt|;
if|if
condition|(
name|leaderLineOffset
operator|!=
literal|null
operator|&&
operator|!
name|leaderLineOffset
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setLeaderOffset
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|leaderLineOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|startStyle
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"head"
argument_list|)
decl_stmt|;
if|if
condition|(
name|startStyle
operator|!=
literal|null
operator|&&
operator|!
name|startStyle
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setStartPointEndingStyle
argument_list|(
name|startStyle
argument_list|)
expr_stmt|;
block|}
name|String
name|endStyle
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"tail"
argument_list|)
decl_stmt|;
if|if
condition|(
name|endStyle
operator|!=
literal|null
operator|&&
operator|!
name|endStyle
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setEndPointEndingStyle
argument_list|(
name|endStyle
argument_list|)
expr_stmt|;
block|}
name|String
name|color
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"interior-color"
argument_list|)
decl_stmt|;
if|if
condition|(
name|color
operator|!=
literal|null
operator|&&
name|color
operator|.
name|length
argument_list|()
operator|==
literal|7
operator|&&
name|color
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'#'
condition|)
block|{
name|int
name|colorValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|color
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|setInteriorColor
argument_list|(
operator|new
name|Color
argument_list|(
name|colorValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|caption
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"caption"
argument_list|)
decl_stmt|;
if|if
condition|(
name|caption
operator|!=
literal|null
operator|&&
operator|!
name|caption
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setCaption
argument_list|(
literal|"yes"
operator|.
name|equals
argument_list|(
name|caption
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|captionH
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"caption-offset-h"
argument_list|)
decl_stmt|;
if|if
condition|(
name|captionH
operator|!=
literal|null
operator|&&
operator|!
name|captionH
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setCaptionHorizontalOffset
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|captionH
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|captionV
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"caption-offset-v"
argument_list|)
decl_stmt|;
if|if
condition|(
name|captionV
operator|!=
literal|null
operator|&&
operator|!
name|captionV
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setCaptionVerticalOffset
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|captionV
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|captionStyle
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"caption-style"
argument_list|)
decl_stmt|;
if|if
condition|(
name|captionStyle
operator|!=
literal|null
operator|&&
operator|!
name|captionStyle
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setCaptionStyle
argument_list|(
name|captionStyle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will set start and end coordinates of the line (or leader line if LL entry is set).      *      * @param line array of 4 floats [x1, y1, x2, y2] line start and end points in default user space.      */
specifier|public
specifier|final
name|void
name|setLine
parameter_list|(
name|float
index|[]
name|line
parameter_list|)
block|{
name|COSArray
name|newLine
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newLine
operator|.
name|setFloatArray
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|L
argument_list|,
name|newLine
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
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|L
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
name|array
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
comment|// Should never happen as this is a required item
block|}
block|}
comment|/**      * This will set the line ending style for the start point, see the LE_ constants for the possible values.      *      * @param style The new style.      */
specifier|public
specifier|final
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
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
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
name|annot
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
comment|/**      * This will retrieve the line ending style for the start point, possible values shown in the LE_ constants section.      *      * @return The ending style for the start point.      */
specifier|public
name|String
name|getStartPointEndingStyle
parameter_list|()
block|{
name|String
name|retval
init|=
name|PDAnnotationLine
operator|.
name|LE_NONE
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
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
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|array
operator|.
name|getName
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the line ending style for the end point, see the LE_ constants for the possible values.      *      * @param style The new style.      */
specifier|public
specifier|final
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
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
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
name|annot
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
comment|/**      * This will retrieve the line ending style for the end point, possible values shown in the LE_ constants section.      *      * @return The ending style for the end point.      */
specifier|public
name|String
name|getEndPointEndingStyle
parameter_list|()
block|{
name|String
name|retval
init|=
name|PDAnnotationLine
operator|.
name|LE_NONE
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
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
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|array
operator|.
name|getName
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set interior color of the line endings defined in the LE entry.      *      * @param color The interior color of the line endings.      */
specifier|public
specifier|final
name|void
name|setInteriorColor
parameter_list|(
name|Color
name|color
parameter_list|)
block|{
name|COSArray
name|array
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
name|float
index|[]
name|colors
init|=
name|color
operator|.
name|getRGBColorComponents
argument_list|(
literal|null
argument_list|)
decl_stmt|;
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
name|colors
argument_list|)
expr_stmt|;
block|}
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|IC
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the interior color of the line endings defined in the LE entry.      *      * @return object representing the color.      */
specifier|public
name|Color
name|getInteriorColor
parameter_list|()
block|{
name|Color
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
name|annot
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|IC
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|float
index|[]
name|rgb
init|=
name|array
operator|.
name|toFloatArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|rgb
operator|.
name|length
operator|>=
literal|3
condition|)
block|{
name|retval
operator|=
operator|new
name|Color
argument_list|(
name|rgb
index|[
literal|0
index|]
argument_list|,
name|rgb
index|[
literal|1
index|]
argument_list|,
name|rgb
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set if the contents are shown as a caption to the line.      *      * @param cap Boolean value.      */
specifier|public
specifier|final
name|void
name|setCaption
parameter_list|(
name|boolean
name|cap
parameter_list|)
block|{
name|annot
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
comment|/**      * This will retrieve if the contents are shown as a caption or not.      *      * @return boolean if the content is shown as a caption.      */
specifier|public
name|boolean
name|getCaption
parameter_list|()
block|{
return|return
name|annot
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
name|getLeaderLength
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LL
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line.      *       * @param leaderLength length of the leader line      */
specifier|public
specifier|final
name|void
name|setLeaderLength
parameter_list|(
name|float
name|leaderLength
parameter_list|)
block|{
name|annot
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LL
argument_list|,
name|leaderLength
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the length of the leader line extensions.      *       * @return the length of the leader line extensions      */
specifier|public
name|float
name|getLeaderExtend
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LLE
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line extensions.      *       * @param leaderExtend length of the leader line extensions      */
specifier|public
specifier|final
name|void
name|setLeaderExtend
parameter_list|(
name|float
name|leaderExtend
parameter_list|)
block|{
name|annot
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LLE
argument_list|,
name|leaderExtend
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the length of the leader line offset.      *       * @return the length of the leader line offset      */
specifier|public
name|float
name|getLeaderOffset
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|LLO
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the leader line offset.      *       * @param leaderOffset length of the leader line offset      */
specifier|public
specifier|final
name|void
name|setLeaderOffset
parameter_list|(
name|float
name|leaderOffset
parameter_list|)
block|{
name|annot
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|LLO
argument_list|,
name|leaderOffset
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the caption positioning.      *       * @return the caption positioning      */
specifier|public
name|String
name|getCaptionStyle
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|CP
argument_list|)
return|;
block|}
comment|/**      * This will set the caption positioning. Allowed values are: "Inline" and "Top"      *       * @param captionStyle caption positioning      */
specifier|public
specifier|final
name|void
name|setCaptionStyle
parameter_list|(
name|String
name|captionStyle
parameter_list|)
block|{
name|annot
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|CP
argument_list|,
name|captionStyle
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the horizontal offset of the caption.      *       * @param offset the horizontal offset of the caption      */
specifier|public
specifier|final
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
name|annot
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
name|annot
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
name|annot
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
specifier|final
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
name|annot
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
name|annot
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
name|annot
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
block|}
end_class

end_unit

