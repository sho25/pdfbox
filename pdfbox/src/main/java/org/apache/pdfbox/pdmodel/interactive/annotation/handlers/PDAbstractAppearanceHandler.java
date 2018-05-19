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
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|state
operator|.
name|PDExtendedGraphicsState
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
name|annotation
operator|.
name|PDAnnotationSquareCircle
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
name|PDAppearanceContentStream
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
name|PDAppearanceDictionary
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
name|PDAppearanceEntry
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
name|PDAppearanceStream
import|;
end_import

begin_comment
comment|/**  * Generic handler to generate the fields appearance.  *   * Individual handler will provide specific implementations for different field  * types.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAbstractAppearanceHandler
implements|implements
name|PDAppearanceHandler
block|{
specifier|private
specifier|final
name|PDAnnotation
name|annotation
decl_stmt|;
comment|/**      * Line ending styles where the line has to be drawn shorter (minus line width).      */
specifier|protected
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SHORT_STYLES
init|=
name|createShortStyles
argument_list|()
decl_stmt|;
specifier|static
specifier|final
name|double
name|ARROW_ANGLE
init|=
name|Math
operator|.
name|toRadians
argument_list|(
literal|30
argument_list|)
decl_stmt|;
comment|/**      * Line ending styles where there is an interior color.      */
specifier|protected
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|INTERIOR_COLOR_STYLES
init|=
name|createInteriorColorStyles
argument_list|()
decl_stmt|;
specifier|public
name|PDAbstractAppearanceHandler
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|)
block|{
name|this
operator|.
name|annotation
operator|=
name|annotation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateNormalAppearance
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateRolloverAppearance
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateDownAppearance
parameter_list|()
function_decl|;
name|PDAnnotation
name|getAnnotation
parameter_list|()
block|{
return|return
name|annotation
return|;
block|}
name|PDColor
name|getColor
parameter_list|()
block|{
return|return
name|annotation
operator|.
name|getColor
argument_list|()
return|;
block|}
name|PDRectangle
name|getRectangle
parameter_list|()
block|{
return|return
name|annotation
operator|.
name|getRectangle
argument_list|()
return|;
block|}
comment|/**      * Get the annotations appearance dictionary.      *       *<p>      * This will get the annotations appearance dictionary. If this is not      * existent an empty appearance dictionary will be created.      *       * @return the annotations appearance dictionary      */
name|PDAppearanceDictionary
name|getAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|annotation
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceDictionary
operator|==
literal|null
condition|)
block|{
name|appearanceDictionary
operator|=
operator|new
name|PDAppearanceDictionary
argument_list|()
expr_stmt|;
name|annotation
operator|.
name|setAppearance
argument_list|(
name|appearanceDictionary
argument_list|)
expr_stmt|;
block|}
return|return
name|appearanceDictionary
return|;
block|}
comment|/**      * Get the annotations normal appearance content stream.      *       *<p>      * This will get the annotations normal appearance content stream,      * to 'draw' to.      *       * @return the appearance entry representing the normal appearance.      * @throws IOException       */
name|PDAppearanceContentStream
name|getNormalAppearanceAsContentStream
parameter_list|()
throws|throws
name|IOException
block|{
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|getNormalAppearance
argument_list|()
decl_stmt|;
return|return
name|getAppearanceEntryAsContentStream
argument_list|(
name|appearanceEntry
argument_list|)
return|;
block|}
comment|/**      * Get the annotations down appearance.      *       *<p>      * This will get the annotations down appearance. If this is not existent an      * empty appearance entry will be created.      *       * @return the appearance entry representing the down appearance.      */
name|PDAppearanceEntry
name|getDownAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|downAppearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getDownAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|downAppearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
comment|//TODO replace with "document.getDocument().createCOSStream()"
name|downAppearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setDownAppearance
argument_list|(
name|downAppearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|downAppearanceEntry
return|;
block|}
comment|/**      * Get the annotations rollover appearance.      *       *<p>      * This will get the annotations rollover appearance. If this is not      * existent an empty appearance entry will be created.      *       * @return the appearance entry representing the rollover appearance.      */
name|PDAppearanceEntry
name|getRolloverAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|rolloverAppearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getRolloverAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|rolloverAppearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
comment|//TODO replace with "document.getDocument().createCOSStream()"
name|rolloverAppearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setRolloverAppearance
argument_list|(
name|rolloverAppearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|rolloverAppearanceEntry
return|;
block|}
comment|/**      * Set the differences rectangle.      */
name|void
name|setRectDifference
parameter_list|(
name|float
name|lineWidth
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|PDAnnotationSquareCircle
operator|&&
name|lineWidth
operator|>
literal|0
condition|)
block|{
name|PDRectangle
name|differences
init|=
operator|new
name|PDRectangle
argument_list|(
name|lineWidth
operator|/
literal|2
argument_list|,
name|lineWidth
operator|/
literal|2
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
operator|(
operator|(
name|PDAnnotationSquareCircle
operator|)
name|annotation
operator|)
operator|.
name|setRectDifference
argument_list|(
name|differences
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get a padded rectangle.      *       *<p>Creates a new rectangle with padding applied to each side.      * .      * @param rectangle the rectangle.      * @param padding the padding to apply.      * @return the padded rectangle.      */
name|PDRectangle
name|getPaddedRectangle
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|,
name|float
name|padding
parameter_list|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|rectangle
operator|.
name|getLowerLeftX
argument_list|()
operator|+
name|padding
argument_list|,
name|rectangle
operator|.
name|getLowerLeftY
argument_list|()
operator|+
name|padding
argument_list|,
name|rectangle
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|,
name|rectangle
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|)
return|;
block|}
comment|/**      * Get a rectangle enlarged by the differences.      *       *<p>Creates a new rectangle with differences added to each side.      * .      * @param rectangle the rectangle.      * @param differences the differences to apply.      * @return the padded rectangle.      */
name|PDRectangle
name|addRectDifferences
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|,
name|float
index|[]
name|differences
parameter_list|)
block|{
if|if
condition|(
name|differences
operator|==
literal|null
operator|||
name|differences
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
return|return
name|rectangle
return|;
block|}
return|return
operator|new
name|PDRectangle
argument_list|(
name|rectangle
operator|.
name|getLowerLeftX
argument_list|()
operator|-
name|differences
index|[
literal|0
index|]
argument_list|,
name|rectangle
operator|.
name|getLowerLeftY
argument_list|()
operator|-
name|differences
index|[
literal|1
index|]
argument_list|,
name|rectangle
operator|.
name|getWidth
argument_list|()
operator|+
name|differences
index|[
literal|0
index|]
operator|+
name|differences
index|[
literal|2
index|]
argument_list|,
name|rectangle
operator|.
name|getHeight
argument_list|()
operator|+
name|differences
index|[
literal|1
index|]
operator|+
name|differences
index|[
literal|3
index|]
argument_list|)
return|;
block|}
comment|/**      * Get a rectangle with the differences applied to each side.      *       *<p>Creates a new rectangle with differences added to each side.      * .      * @param rectangle the rectangle.      * @param differences the differences to apply.      * @return the padded rectangle.      */
name|PDRectangle
name|applyRectDifferences
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|,
name|float
index|[]
name|differences
parameter_list|)
block|{
if|if
condition|(
name|differences
operator|==
literal|null
operator|||
name|differences
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
return|return
name|rectangle
return|;
block|}
return|return
operator|new
name|PDRectangle
argument_list|(
name|rectangle
operator|.
name|getLowerLeftX
argument_list|()
operator|+
name|differences
index|[
literal|0
index|]
argument_list|,
name|rectangle
operator|.
name|getLowerLeftY
argument_list|()
operator|+
name|differences
index|[
literal|1
index|]
argument_list|,
name|rectangle
operator|.
name|getWidth
argument_list|()
operator|-
name|differences
index|[
literal|0
index|]
operator|-
name|differences
index|[
literal|2
index|]
argument_list|,
name|rectangle
operator|.
name|getHeight
argument_list|()
operator|-
name|differences
index|[
literal|1
index|]
operator|-
name|differences
index|[
literal|3
index|]
argument_list|)
return|;
block|}
name|void
name|setOpacity
parameter_list|(
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|float
name|opacity
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|opacity
operator|<
literal|1
condition|)
block|{
name|PDExtendedGraphicsState
name|gs
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|gs
operator|.
name|setStrokingAlphaConstant
argument_list|(
name|opacity
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setNonStrokingAlphaConstant
argument_list|(
name|opacity
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setGraphicsStateParameters
argument_list|(
name|gs
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|drawStyle
parameter_list|(
name|String
name|style
parameter_list|,
specifier|final
name|PDAppearanceContentStream
name|cs
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|width
parameter_list|,
name|boolean
name|hasStroke
parameter_list|,
name|boolean
name|hasBackground
parameter_list|)
throws|throws
name|IOException
block|{
switch|switch
condition|(
name|style
condition|)
block|{
case|case
name|PDAnnotationLine
operator|.
name|LE_OPEN_ARROW
case|:
case|case
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
case|:
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|x
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
comment|// ending
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|x
operator|-
name|width
argument_list|,
name|y
argument_list|,
operator|-
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// start
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|width
argument_list|,
name|y
argument_list|,
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|cs
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_BUTT
case|:
name|cs
operator|.
name|moveTo
argument_list|(
name|x
argument_list|,
name|y
operator|-
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
argument_list|,
name|y
operator|+
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_DIAMOND
case|:
name|drawDiamond
argument_list|(
name|cs
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_SQUARE
case|:
name|cs
operator|.
name|addRect
argument_list|(
name|x
operator|-
name|width
operator|*
literal|3
argument_list|,
name|y
operator|-
name|width
operator|*
literal|3
argument_list|,
name|width
operator|*
literal|6
argument_list|,
name|width
operator|*
literal|6
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_CIRCLE
case|:
name|addCircle
argument_list|(
name|cs
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_R_OPEN_ARROW
case|:
case|case
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
case|:
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|x
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
comment|// ending
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|x
operator|+
name|width
argument_list|,
name|y
argument_list|,
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// start
name|drawArrow
argument_list|(
name|cs
argument_list|,
operator|-
name|width
argument_list|,
name|y
argument_list|,
operator|-
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|cs
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|PDAnnotationLine
operator|.
name|LE_SLASH
case|:
comment|// the line is 18 x linewidth at an angle of 60°
name|cs
operator|.
name|moveTo
argument_list|(
name|x
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|cos
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
literal|60
argument_list|)
argument_list|)
operator|*
name|width
operator|*
literal|9
argument_list|)
argument_list|,
name|y
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|sin
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
literal|60
argument_list|)
argument_list|)
operator|*
name|width
operator|*
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|cos
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
literal|240
argument_list|)
argument_list|)
operator|*
name|width
operator|*
literal|9
argument_list|)
argument_list|,
name|y
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|sin
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
literal|240
argument_list|)
argument_list|)
operator|*
name|width
operator|*
literal|9
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
if|if
condition|(
name|INTERIOR_COLOR_STYLES
operator|.
name|contains
argument_list|(
name|style
argument_list|)
condition|)
block|{
name|cs
operator|.
name|drawShape
argument_list|(
name|width
argument_list|,
name|hasStroke
argument_list|,
name|hasBackground
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|PDAnnotationLine
operator|.
name|LE_NONE
operator|.
name|equals
argument_list|(
name|style
argument_list|)
condition|)
block|{
comment|// need to do this separately, because sometimes /IC is set anyway
name|cs
operator|.
name|drawShape
argument_list|(
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add the two arms of a horizontal arrow.      *       * @param cs Content stream      * @param x      * @param y      * @param len The arm length. Positive goes to the right, negative goes to the left.      *       * @throws IOException If the content stream could not be written      */
name|void
name|drawArrow
parameter_list|(
name|PDAppearanceContentStream
name|cs
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|len
parameter_list|)
throws|throws
name|IOException
block|{
comment|// strategy for arrows: angle 30°, arrow arm length = 9 * line width
comment|// cos(angle) = x position
comment|// sin(angle) = y position
comment|// this comes very close to what Adobe is doing
name|cs
operator|.
name|moveTo
argument_list|(
name|x
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|cos
argument_list|(
name|ARROW_ANGLE
argument_list|)
operator|*
name|len
argument_list|)
argument_list|,
name|y
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|sin
argument_list|(
name|ARROW_ANGLE
argument_list|)
operator|*
name|len
argument_list|)
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
operator|+
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|cos
argument_list|(
name|ARROW_ANGLE
argument_list|)
operator|*
name|len
argument_list|)
argument_list|,
name|y
operator|-
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|sin
argument_list|(
name|ARROW_ANGLE
argument_list|)
operator|*
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a square diamond shape (corner on top) to the path.      *      * @param cs Content stream      * @param x      * @param y      * @param r Radius (to a corner)      *       * @throws IOException If the content stream could not be written      */
name|void
name|drawDiamond
parameter_list|(
name|PDAppearanceContentStream
name|cs
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|r
parameter_list|)
throws|throws
name|IOException
block|{
name|cs
operator|.
name|moveTo
argument_list|(
name|x
operator|-
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
argument_list|,
name|y
operator|+
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
operator|+
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x
argument_list|,
name|y
operator|-
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
comment|/**      * Add a circle shape to the path.      *      * @param cs Content stream      * @param x      * @param y      * @param r Radius      *       * @throws IOException If the content stream could not be written      */
name|void
name|addCircle
parameter_list|(
name|PDAppearanceContentStream
name|cs
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|r
parameter_list|)
throws|throws
name|IOException
block|{
comment|// http://stackoverflow.com/a/2007782/535646
name|float
name|magic
init|=
name|r
operator|*
literal|0.551784f
decl_stmt|;
name|cs
operator|.
name|moveTo
argument_list|(
name|x
argument_list|,
name|y
operator|+
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|+
name|magic
argument_list|,
name|y
operator|+
name|r
argument_list|,
name|x
operator|+
name|r
argument_list|,
name|y
operator|+
name|magic
argument_list|,
name|x
operator|+
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|+
name|r
argument_list|,
name|y
operator|-
name|magic
argument_list|,
name|x
operator|+
name|magic
argument_list|,
name|y
operator|-
name|r
argument_list|,
name|x
argument_list|,
name|y
operator|-
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|-
name|magic
argument_list|,
name|y
operator|-
name|r
argument_list|,
name|x
operator|-
name|r
argument_list|,
name|y
operator|-
name|magic
argument_list|,
name|x
operator|-
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|-
name|r
argument_list|,
name|y
operator|+
name|magic
argument_list|,
name|x
operator|-
name|magic
argument_list|,
name|y
operator|+
name|r
argument_list|,
name|x
argument_list|,
name|y
operator|+
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|createShortStyles
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|shortStyles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|shortStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_OPEN_ARROW
argument_list|)
expr_stmt|;
name|shortStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|shortStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_SQUARE
argument_list|)
expr_stmt|;
name|shortStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CIRCLE
argument_list|)
expr_stmt|;
name|shortStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_DIAMOND
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|shortStyles
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|createInteriorColorStyles
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|interiorColorStyles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|interiorColorStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|interiorColorStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CIRCLE
argument_list|)
expr_stmt|;
name|interiorColorStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_DIAMOND
argument_list|)
expr_stmt|;
name|interiorColorStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|interiorColorStyles
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_SQUARE
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|interiorColorStyles
argument_list|)
return|;
block|}
comment|/**      * Get the annotations normal appearance.      *       *<p>      * This will get the annotations normal appearance. If this is not existent      * an empty appearance entry will be created.      *       * @return the appearance entry representing the normal appearance.      */
specifier|private
name|PDAppearanceEntry
name|getNormalAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|normalAppearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|normalAppearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
comment|//TODO replace with "document.getDocument().createCOSStream()"
name|normalAppearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setNormalAppearance
argument_list|(
name|normalAppearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|normalAppearanceEntry
return|;
block|}
specifier|private
name|PDAppearanceContentStream
name|getAppearanceEntryAsContentStream
parameter_list|(
name|PDAppearanceEntry
name|appearanceEntry
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAppearanceStream
name|appearanceStream
init|=
name|appearanceEntry
operator|.
name|getAppearanceStream
argument_list|()
decl_stmt|;
name|setTransformationMatrix
argument_list|(
name|appearanceStream
argument_list|)
expr_stmt|;
comment|// ensure there are resources
name|PDResources
name|resources
init|=
name|appearanceStream
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|appearanceStream
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDAppearanceContentStream
argument_list|(
name|appearanceStream
argument_list|)
return|;
block|}
specifier|private
name|void
name|setTransformationMatrix
parameter_list|(
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
block|{
name|PDRectangle
name|bbox
init|=
name|getRectangle
argument_list|()
decl_stmt|;
name|appearanceStream
operator|.
name|setBBox
argument_list|(
name|bbox
argument_list|)
expr_stmt|;
name|AffineTransform
name|transform
init|=
name|AffineTransform
operator|.
name|getTranslateInstance
argument_list|(
operator|-
name|bbox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|-
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
decl_stmt|;
name|appearanceStream
operator|.
name|setMatrix
argument_list|(
name|transform
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

