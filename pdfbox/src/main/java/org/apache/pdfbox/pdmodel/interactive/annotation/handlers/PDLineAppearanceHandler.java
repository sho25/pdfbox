begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|font
operator|.
name|PDType1Font
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PDLineAppearanceHandler
extends|extends
name|PDAbstractAppearanceHandler
block|{
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
name|PDLineAppearanceHandler
operator|.
name|class
argument_list|)
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
specifier|static
specifier|final
name|int
name|FONT_SIZE
init|=
literal|9
decl_stmt|;
comment|/**      * styles where the line has to be drawn shorter (minus line width).      */
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SHORT_STYLES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * styles where there is an interior color.      */
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|INTERIOR_COLOR_STYLES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|SHORT_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_OPEN_ARROW
argument_list|)
expr_stmt|;
name|SHORT_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|SHORT_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_SQUARE
argument_list|)
expr_stmt|;
name|SHORT_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CIRCLE
argument_list|)
expr_stmt|;
name|SHORT_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_DIAMOND
argument_list|)
expr_stmt|;
name|INTERIOR_COLOR_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|INTERIOR_COLOR_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_CIRCLE
argument_list|)
expr_stmt|;
name|INTERIOR_COLOR_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_DIAMOND
argument_list|)
expr_stmt|;
name|INTERIOR_COLOR_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
argument_list|)
expr_stmt|;
name|INTERIOR_COLOR_STYLES
operator|.
name|add
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_SQUARE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PDLineAppearanceHandler
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|)
block|{
name|super
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateAppearanceStreams
parameter_list|()
block|{
name|generateNormalAppearance
argument_list|()
expr_stmt|;
name|generateRolloverAppearance
argument_list|()
expr_stmt|;
name|generateDownAppearance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateNormalAppearance
parameter_list|()
block|{
name|PDAnnotationLine
name|annotation
init|=
operator|(
name|PDAnnotationLine
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
name|PDRectangle
name|rect
init|=
name|annotation
operator|.
name|getRectangle
argument_list|()
decl_stmt|;
name|float
index|[]
name|pathsArray
init|=
name|annotation
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathsArray
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|AnnotationBorder
name|ab
init|=
name|AnnotationBorder
operator|.
name|getAnnotationBorder
argument_list|(
name|annotation
argument_list|,
name|annotation
operator|.
name|getBorderStyle
argument_list|()
argument_list|)
decl_stmt|;
name|PDColor
name|color
init|=
name|annotation
operator|.
name|getColor
argument_list|()
decl_stmt|;
if|if
condition|(
name|color
operator|==
literal|null
operator|||
name|color
operator|.
name|getComponents
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|float
name|ll
init|=
name|annotation
operator|.
name|getLeaderLineLength
argument_list|()
decl_stmt|;
name|float
name|lle
init|=
name|annotation
operator|.
name|getLeaderLineExtensionLength
argument_list|()
decl_stmt|;
name|float
name|llo
init|=
name|annotation
operator|.
name|getLeaderLineOffsetLength
argument_list|()
decl_stmt|;
comment|// Adjust rectangle even if not empty, see PLPDF.com-MarkupAnnotations.pdf
name|float
name|minX
init|=
name|Float
operator|.
name|MAX_VALUE
decl_stmt|;
name|float
name|minY
init|=
name|Float
operator|.
name|MAX_VALUE
decl_stmt|;
name|float
name|maxX
init|=
name|Float
operator|.
name|MIN_VALUE
decl_stmt|;
name|float
name|maxY
init|=
name|Float
operator|.
name|MIN_VALUE
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
name|pathsArray
operator|.
name|length
operator|/
literal|2
condition|;
operator|++
name|i
control|)
block|{
name|float
name|x
init|=
name|pathsArray
index|[
name|i
operator|*
literal|2
index|]
decl_stmt|;
name|float
name|y
init|=
name|pathsArray
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
decl_stmt|;
name|minX
operator|=
name|Math
operator|.
name|min
argument_list|(
name|minX
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|minY
operator|=
name|Math
operator|.
name|min
argument_list|(
name|minY
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|maxX
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxX
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|maxY
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxY
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
comment|// Leader lines
if|if
condition|(
name|ll
operator|<
literal|0
condition|)
block|{
comment|// /LLO and /LLE go in the same direction as /LL
name|llo
operator|=
operator|-
name|llo
expr_stmt|;
name|lle
operator|=
operator|-
name|lle
expr_stmt|;
block|}
comment|// add/substract with, font height, and arrows
comment|// arrow length is 9 * width at about 30° => 10 * width seems to be enough
comment|// but need to consider /LL, /LLE and /LLO too
comment|//TODO find better way to calculate padding
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|minX
operator|-
name|Math
operator|.
name|max
argument_list|(
name|ab
operator|.
name|width
operator|*
literal|10
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
argument_list|)
argument_list|,
name|rect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|minY
operator|-
name|Math
operator|.
name|max
argument_list|(
name|ab
operator|.
name|width
operator|*
literal|10
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
argument_list|)
argument_list|,
name|rect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|maxX
operator|+
name|Math
operator|.
name|max
argument_list|(
name|ab
operator|.
name|width
operator|*
literal|10
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
argument_list|)
argument_list|,
name|rect
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|maxY
operator|+
name|Math
operator|.
name|max
argument_list|(
name|ab
operator|.
name|width
operator|*
literal|10
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
argument_list|)
argument_list|,
name|rect
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
try|try
block|{
try|try
init|(
name|PDAppearanceContentStream
name|cs
init|=
name|getNormalAppearanceAsContentStream
argument_list|()
init|)
block|{
name|setOpacity
argument_list|(
name|cs
argument_list|,
name|annotation
operator|.
name|getConstantOpacity
argument_list|()
argument_list|)
expr_stmt|;
comment|// Tested with Adobe Reader:
comment|// text is written first (TODO)
comment|// width 0 is used by Adobe as such (but results in a visible line in rendering)
comment|// empty color array results in an invisible line ("n" operator) but the rest is visible
comment|// empty content is like no caption
name|boolean
name|hasStroke
init|=
name|cs
operator|.
name|setStrokingColorOnDemand
argument_list|(
name|color
argument_list|)
decl_stmt|;
if|if
condition|(
name|ab
operator|.
name|dashArray
operator|!=
literal|null
condition|)
block|{
name|cs
operator|.
name|setLineDashPattern
argument_list|(
name|ab
operator|.
name|dashArray
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|setLineWidth
argument_list|(
name|ab
operator|.
name|width
argument_list|)
expr_stmt|;
name|float
name|x1
init|=
name|pathsArray
index|[
literal|0
index|]
decl_stmt|;
name|float
name|y1
init|=
name|pathsArray
index|[
literal|1
index|]
decl_stmt|;
name|float
name|x2
init|=
name|pathsArray
index|[
literal|2
index|]
decl_stmt|;
name|float
name|y2
init|=
name|pathsArray
index|[
literal|3
index|]
decl_stmt|;
comment|// if there are leader lines, then the /L coordinates represent
comment|// the endpoints of the leader lines rather than the endpoints of the line itself.
comment|// so for us, llo + ll is the vertical offset for the line.
name|float
name|y
init|=
name|llo
operator|+
name|ll
decl_stmt|;
name|String
name|contents
init|=
name|annotation
operator|.
name|getContents
argument_list|()
decl_stmt|;
if|if
condition|(
name|contents
operator|==
literal|null
condition|)
block|{
name|contents
operator|=
literal|""
expr_stmt|;
block|}
name|double
name|angle
init|=
name|Math
operator|.
name|atan2
argument_list|(
name|y2
operator|-
name|y1
argument_list|,
name|x2
operator|-
name|x1
argument_list|)
decl_stmt|;
name|cs
operator|.
name|transform
argument_list|(
name|Matrix
operator|.
name|getRotateInstance
argument_list|(
name|angle
argument_list|,
name|x1
argument_list|,
name|y1
argument_list|)
argument_list|)
expr_stmt|;
name|float
name|lineLength
init|=
operator|(
name|float
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
operator|(
operator|(
name|x2
operator|-
name|x1
operator|)
operator|*
operator|(
name|x2
operator|-
name|x1
operator|)
operator|)
operator|+
operator|(
operator|(
name|y2
operator|-
name|y1
operator|)
operator|*
operator|(
name|y2
operator|-
name|y1
operator|)
operator|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|.
name|hasCaption
argument_list|()
operator|&&
operator|!
name|contents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PDType1Font
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
comment|// TODO: support newlines!!!!!
comment|// see https://www.pdfill.com/example/pdf_commenting_new.pdf
name|float
name|contentLength
init|=
literal|0
decl_stmt|;
try|try
block|{
name|contentLength
operator|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|annotation
operator|.
name|getContents
argument_list|()
argument_list|)
operator|/
literal|1000
operator|*
name|FONT_SIZE
expr_stmt|;
comment|//TODO How to decide the size of the font?
comment|// 9 seems to be standard, but if the text doesn't fit, a scaling is done
comment|// see AnnotationSample.Standard.pdf, diagonal line
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// Adobe Reader displays placeholders instead
name|LOG
operator|.
name|error
argument_list|(
literal|"line text '"
operator|+
name|annotation
operator|.
name|getContents
argument_list|()
operator|+
literal|"' can't be shown"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|float
name|xOffset
init|=
operator|(
name|lineLength
operator|-
name|contentLength
operator|)
operator|/
literal|2
decl_stmt|;
name|float
name|yOffset
decl_stmt|;
comment|// Leader lines
name|cs
operator|.
name|moveTo
argument_list|(
literal|0
argument_list|,
name|llo
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
literal|0
argument_list|,
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
expr_stmt|;
name|cs
operator|.
name|moveTo
argument_list|(
name|lineLength
argument_list|,
name|llo
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
argument_list|,
name|llo
operator|+
name|ll
operator|+
name|lle
argument_list|)
expr_stmt|;
name|String
name|captionPositioning
init|=
name|annotation
operator|.
name|getCaptionPositioning
argument_list|()
decl_stmt|;
comment|// draw the line horizontally, using the rotation CTM to get to correct final position
comment|// that's the easiest way to calculate the positions for the line before and after inline caption
if|if
condition|(
name|SHORT_STYLES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|moveTo
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cs
operator|.
name|moveTo
argument_list|(
literal|0
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"Top"
operator|.
name|equals
argument_list|(
name|captionPositioning
argument_list|)
condition|)
block|{
comment|// this arbitrary number is from Adobe
name|yOffset
operator|=
literal|1.908f
expr_stmt|;
block|}
else|else
block|{
comment|// Inline
comment|// this arbitrary number is from Adobe
name|yOffset
operator|=
operator|-
literal|2.6f
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|xOffset
operator|-
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|moveTo
argument_list|(
name|lineLength
operator|-
name|xOffset
operator|+
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|SHORT_STYLES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
operator|-
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// /CO entry (caption offset)
name|float
name|captionHorizontalOffset
init|=
name|annotation
operator|.
name|getCaptionHorizontalOffset
argument_list|()
decl_stmt|;
name|float
name|captionVerticalOffset
init|=
name|annotation
operator|.
name|getCaptionVerticalOffset
argument_list|()
decl_stmt|;
comment|// check contentLength so we don't show if there was trouble before
if|if
condition|(
name|contentLength
operator|>
literal|0
condition|)
block|{
comment|//prepareResources(cs);
name|cs
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|cs
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
name|FONT_SIZE
argument_list|)
expr_stmt|;
name|cs
operator|.
name|newLineAtOffset
argument_list|(
name|xOffset
operator|+
name|captionHorizontalOffset
argument_list|,
name|y
operator|+
name|yOffset
operator|+
name|captionVerticalOffset
argument_list|)
expr_stmt|;
name|cs
operator|.
name|showText
argument_list|(
name|annotation
operator|.
name|getContents
argument_list|()
argument_list|)
expr_stmt|;
name|cs
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|captionVerticalOffset
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
comment|// Adobe paints vertical bar to the caption
name|cs
operator|.
name|moveTo
argument_list|(
literal|0
operator|+
name|lineLength
operator|/
literal|2
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
literal|0
operator|+
name|lineLength
operator|/
literal|2
argument_list|,
name|y
operator|+
name|captionVerticalOffset
argument_list|)
expr_stmt|;
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|SHORT_STYLES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|moveTo
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cs
operator|.
name|moveTo
argument_list|(
literal|0
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|SHORT_STYLES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
operator|-
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// do this here and not before showing the text, or the text would appear in the
comment|// interior color
name|boolean
name|hasBackground
init|=
name|cs
operator|.
name|setNonStrokingColorOnDemand
argument_list|(
name|annotation
operator|.
name|getInteriorColor
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
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
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|,
name|ab
operator|.
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
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
literal|0
argument_list|,
name|y
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
literal|0
argument_list|,
name|y
operator|+
name|ab
operator|.
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
literal|0
argument_list|,
name|y
argument_list|,
name|ab
operator|.
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
literal|0
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|,
name|y
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|,
name|ab
operator|.
name|width
operator|*
literal|6
argument_list|,
name|ab
operator|.
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
literal|0
argument_list|,
name|y
argument_list|,
name|ab
operator|.
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
name|drawArrow
argument_list|(
name|cs
argument_list|,
operator|-
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|,
operator|-
name|ab
operator|.
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
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
name|ab
operator|.
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
name|ab
operator|.
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
name|ab
operator|.
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
name|ab
operator|.
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
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
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
name|annotation
operator|.
name|getStartPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
comment|// need to do this separately, because sometimes /IC is set anyway
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
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
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|lineLength
operator|-
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|,
operator|-
name|ab
operator|.
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
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
name|lineLength
argument_list|,
name|y
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|lineLength
argument_list|,
name|y
operator|+
name|ab
operator|.
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
name|lineLength
argument_list|,
name|y
argument_list|,
name|ab
operator|.
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
name|lineLength
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|,
name|y
operator|-
name|ab
operator|.
name|width
operator|*
literal|3
argument_list|,
name|ab
operator|.
name|width
operator|*
literal|6
argument_list|,
name|ab
operator|.
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
name|lineLength
argument_list|,
name|y
argument_list|,
name|ab
operator|.
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
name|drawArrow
argument_list|(
name|cs
argument_list|,
name|lineLength
operator|+
name|ab
operator|.
name|width
argument_list|,
name|y
argument_list|,
name|ab
operator|.
name|width
operator|*
literal|9
argument_list|)
expr_stmt|;
if|if
condition|(
name|PDAnnotationLine
operator|.
name|LE_R_CLOSED_ARROW
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
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
name|lineLength
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
name|ab
operator|.
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
name|ab
operator|.
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
name|lineLength
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
name|ab
operator|.
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
name|ab
operator|.
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
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
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
name|annotation
operator|.
name|getEndPointEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
comment|// need to do this separately, because sometimes /IC is set anyway
name|cs
operator|.
name|drawShape
argument_list|(
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add the two arms of a horizontal arrow.      *       * @param cs Content stream      * @param x      * @param y      * @param len The arm length. Positive goes to the right, negative goes to the left.      *       * @throws IOException If the content stream could not be written      */
specifier|private
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
specifier|private
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
specifier|private
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
annotation|@
name|Override
specifier|public
name|void
name|generateRolloverAppearance
parameter_list|()
block|{
comment|// No rollover appearance generated
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateDownAppearance
parameter_list|()
block|{
comment|// No down appearance generated
block|}
block|}
end_class

end_unit

