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
name|fontbox
operator|.
name|util
operator|.
name|Charsets
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
name|operator
operator|.
name|Operator
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
name|COSObject
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|color
operator|.
name|PDDeviceCMYK
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
name|PDDeviceGray
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
name|PDAnnotationFreeText
import|;
end_import

begin_import
import|import static
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
operator|.
name|LE_NONE
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
name|PDBorderEffectDictionary
import|;
end_import

begin_import
import|import static
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
name|PDAbstractAppearanceHandler
operator|.
name|SHORT_STYLES
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

begin_class
specifier|public
class|class
name|PDFreeTextAppearanceHandler
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
name|PDFreeTextAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDFreeTextAppearanceHandler
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
name|PDAnnotationFreeText
name|annotation
init|=
operator|(
name|PDAnnotationFreeText
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
name|float
index|[]
name|pathsArray
init|=
operator|new
name|float
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
literal|"FreeTextCallout"
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getIntent
argument_list|()
argument_list|)
condition|)
block|{
name|pathsArray
operator|=
name|annotation
operator|.
name|getCallout
argument_list|()
expr_stmt|;
if|if
condition|(
name|pathsArray
operator|==
literal|null
operator|||
name|pathsArray
operator|.
name|length
operator|!=
literal|4
operator|&&
name|pathsArray
operator|.
name|length
operator|!=
literal|6
condition|)
block|{
name|pathsArray
operator|=
operator|new
name|float
index|[
literal|0
index|]
expr_stmt|;
block|}
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
comment|// width 0 = no border
comment|// pdf_commenting_new.pdf page 3
comment|// Root/Pages/Kids/[2]/Kids/[0]/Annots/[5]/BS/W
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|ab
operator|.
name|width
argument_list|,
literal|0
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|//TODO what happens if there is a callout?
comment|//TODO skip, don't return when we know how to make text
comment|// (maybe refactor the rectangle drawing segment)
return|return;
block|}
comment|//TODO how to set the text color? Apparently red is the default????
try|try
init|(
name|PDAppearanceContentStream
name|cs
init|=
name|getNormalAppearanceAsContentStream
argument_list|()
init|)
block|{
comment|// The fill color is the /C entry, there is no /IC entry defined
name|boolean
name|hasBackground
init|=
name|cs
operator|.
name|setNonStrokingColorOnDemand
argument_list|(
name|annotation
operator|.
name|getColor
argument_list|()
argument_list|)
decl_stmt|;
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
comment|// Adobe uses the last non stroking color from /DA as stroking color!
name|PDColor
name|strokingColor
init|=
name|extractNonStrokingColor
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
name|boolean
name|hasStroke
init|=
name|cs
operator|.
name|setStrokingColorOnDemand
argument_list|(
name|strokingColor
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
comment|// draw callout line(s)
comment|// must be done before retangle paint to avoid a line cutting through cloud
comment|// see CTAN-example-Annotations.pdf
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
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|SHORT_STYLES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getLineEndingStyle
argument_list|()
argument_list|)
condition|)
block|{
comment|// modify coordinate to shorten the segment
comment|// https://stackoverflow.com/questions/7740507/extend-a-line-segment-a-specific-distance
name|float
name|x1
init|=
name|pathsArray
index|[
literal|2
index|]
decl_stmt|;
name|float
name|y1
init|=
name|pathsArray
index|[
literal|3
index|]
decl_stmt|;
name|float
name|len
init|=
call|(
name|float
call|)
argument_list|(
name|Math
operator|.
name|sqrt
argument_list|(
name|Math
operator|.
name|pow
argument_list|(
name|x
operator|-
name|x1
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
name|y
operator|-
name|y1
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|len
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
name|x
operator|+=
operator|(
name|x1
operator|-
name|x
operator|)
operator|/
name|len
operator|*
name|ab
operator|.
name|width
expr_stmt|;
name|y
operator|+=
operator|(
name|y1
operator|-
name|y
operator|)
operator|/
name|len
operator|*
name|ab
operator|.
name|width
expr_stmt|;
block|}
block|}
name|cs
operator|.
name|moveTo
argument_list|(
name|x
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
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
block|}
name|cs
operator|.
name|stroke
argument_list|()
expr_stmt|;
comment|// do a transform so that first "arm" is imagined flat, like in line handler
comment|// the alternative would be to apply the transform to the LE shapes directly,
comment|// which would be more work and produce code difficult to understand
comment|// paint the styles here and after line(s) draw, to avoid line crossing a filled shape
if|if
condition|(
literal|"FreeTextCallout"
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getIntent
argument_list|()
argument_list|)
operator|&&
operator|!
name|LE_NONE
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getLineEndingStyle
argument_list|()
argument_list|)
operator|&&
name|pathsArray
operator|.
name|length
operator|>=
literal|4
condition|)
block|{
comment|// check only needed to avoid q cm Q if LE_NONE
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
name|saveGraphicsState
argument_list|()
expr_stmt|;
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
name|drawStyle
argument_list|(
name|annotation
operator|.
name|getLineEndingStyle
argument_list|()
argument_list|,
name|cs
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ab
operator|.
name|width
argument_list|,
name|hasStroke
argument_list|,
name|hasBackground
argument_list|)
expr_stmt|;
name|cs
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
block|}
comment|//TODO this segment was copied from square handler. Refactor?
name|PDBorderEffectDictionary
name|borderEffect
init|=
name|annotation
operator|.
name|getBorderEffect
argument_list|()
decl_stmt|;
if|if
condition|(
name|borderEffect
operator|!=
literal|null
operator|&&
name|borderEffect
operator|.
name|getStyle
argument_list|()
operator|.
name|equals
argument_list|(
name|PDBorderEffectDictionary
operator|.
name|STYLE_CLOUDY
argument_list|)
condition|)
block|{
name|CloudyBorder
name|cloudyBorder
init|=
operator|new
name|CloudyBorder
argument_list|(
name|cs
argument_list|,
name|borderEffect
operator|.
name|getIntensity
argument_list|()
argument_list|,
name|ab
operator|.
name|width
argument_list|,
name|getRectangle
argument_list|()
argument_list|)
decl_stmt|;
name|cloudyBorder
operator|.
name|createCloudyRectangle
argument_list|(
name|annotation
operator|.
name|getRectDifference
argument_list|()
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|setRectangle
argument_list|(
name|cloudyBorder
operator|.
name|getRectangle
argument_list|()
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|setRectDifference
argument_list|(
name|cloudyBorder
operator|.
name|getRectDifference
argument_list|()
argument_list|)
expr_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
decl_stmt|;
name|appearanceStream
operator|.
name|setBBox
argument_list|(
name|cloudyBorder
operator|.
name|getBBox
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceStream
operator|.
name|setMatrix
argument_list|(
name|cloudyBorder
operator|.
name|getMatrix
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// handle the border box
comment|//
comment|// There are two options. The handling is not part of the PDF specification but
comment|// implementation specific to Adobe Reader
comment|// - if /RD is set the border box is the /Rect entry inset by the respective
comment|//   border difference.
comment|// - if /RD is not set the border box is defined by the /Rect entry. The /RD entry will
comment|//   be set to be the line width and the /Rect is enlarged by the /RD amount
name|PDRectangle
name|borderBox
decl_stmt|;
name|float
index|[]
name|rectDifferences
init|=
name|annotation
operator|.
name|getRectDifferences
argument_list|()
decl_stmt|;
if|if
condition|(
name|rectDifferences
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|borderBox
operator|=
name|getPaddedRectangle
argument_list|(
name|getRectangle
argument_list|()
argument_list|,
name|ab
operator|.
name|width
operator|/
literal|2
argument_list|)
expr_stmt|;
comment|// the differences rectangle
name|annotation
operator|.
name|setRectDifferences
argument_list|(
name|ab
operator|.
name|width
operator|/
literal|2
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|setRectangle
argument_list|(
name|addRectDifferences
argument_list|(
name|getRectangle
argument_list|()
argument_list|,
name|annotation
operator|.
name|getRectDifferences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// when the normal appearance stream was generated BBox and Matrix have been set to the
comment|// values of the original /Rect. As the /Rect was changed that needs to be adjusted too.
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|setBBox
argument_list|(
name|getRectangle
argument_list|()
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
name|getRectangle
argument_list|()
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|-
name|getRectangle
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|setMatrix
argument_list|(
name|transform
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|borderBox
operator|=
name|applyRectDifferences
argument_list|(
name|getRectangle
argument_list|()
argument_list|,
name|rectDifferences
argument_list|)
expr_stmt|;
name|borderBox
operator|=
name|getPaddedRectangle
argument_list|(
name|borderBox
argument_list|,
name|ab
operator|.
name|width
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|addRect
argument_list|(
name|borderBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|borderBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|borderBox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|borderBox
operator|.
name|getHeight
argument_list|()
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
name|hasBackground
argument_list|)
expr_stmt|;
if|if
condition|(
name|pathsArray
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|PDRectangle
name|rect
init|=
name|getRectangle
argument_list|()
decl_stmt|;
comment|// Adjust rectangle
comment|// important to do this after the rectangle has been painted, because the
comment|// final rectangle will be bigger due to callout
comment|// CTAN-example-Annotations.pdf p1
comment|//TODO in a class structure this should be overridable
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
comment|// arrow length is 9 * width at about 30Â° => 10 * width seems to be enough
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
name|ab
operator|.
name|width
operator|*
literal|10
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
name|ab
operator|.
name|width
operator|*
literal|10
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
name|ab
operator|.
name|width
operator|*
literal|10
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
name|ab
operator|.
name|width
operator|*
literal|10
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
comment|// need to set the BBox too, because rectangle modification came later
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|setBBox
argument_list|(
name|getRectangle
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// get the last non stroking color from the /DA entry
specifier|private
name|PDColor
name|extractNonStrokingColor
parameter_list|(
name|PDAnnotationFreeText
name|annotation
parameter_list|)
block|{
comment|// It could also work with a regular expression, but that should be written so that
comment|// "/LucidaConsole 13.94766 Tf .392 .585 .93 rg" does not produce "2 .585 .93 rg" as result
comment|// Another alternative might be to create a PDDocument and a PDPage with /DA content as /Content,
comment|// process the whole thing and then get the non stroking color.
name|PDColor
name|strokingColor
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|}
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|String
name|defaultAppearance
init|=
name|annotation
operator|.
name|getDefaultAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultAppearance
operator|==
literal|null
condition|)
block|{
return|return
name|strokingColor
return|;
block|}
try|try
block|{
comment|// not sure if charset is correct, but we only need numbers and simple characters
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|defaultAppearance
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|US_ASCII
argument_list|)
argument_list|)
decl_stmt|;
name|COSArray
name|arguments
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|COSArray
name|colors
init|=
literal|null
decl_stmt|;
name|Operator
name|graphicOp
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|token
init|=
name|parser
operator|.
name|parseNextToken
argument_list|()
init|;
name|token
operator|!=
literal|null
condition|;
name|token
operator|=
name|parser
operator|.
name|parseNextToken
argument_list|()
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|COSObject
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|token
operator|)
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|Operator
condition|)
block|{
name|Operator
name|op
init|=
operator|(
name|Operator
operator|)
name|token
decl_stmt|;
name|String
name|name
init|=
name|op
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"g"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"rg"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"k"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|graphicOp
operator|=
name|op
expr_stmt|;
name|colors
operator|=
name|arguments
expr_stmt|;
block|}
name|arguments
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|arguments
operator|.
name|add
argument_list|(
operator|(
name|COSBase
operator|)
name|token
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|graphicOp
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|graphicOp
operator|.
name|getName
argument_list|()
condition|)
block|{
case|case
literal|"g"
case|:
name|strokingColor
operator|=
operator|new
name|PDColor
argument_list|(
name|colors
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"rg"
case|:
name|strokingColor
operator|=
operator|new
name|PDColor
argument_list|(
name|colors
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"k"
case|:
name|strokingColor
operator|=
operator|new
name|PDColor
argument_list|(
name|colors
argument_list|,
name|PDDeviceCMYK
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
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
name|warn
argument_list|(
literal|"Problem parsing /DA, will use default black"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|strokingColor
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateRolloverAppearance
parameter_list|()
block|{
comment|// TODO to be implemented
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateDownAppearance
parameter_list|()
block|{
comment|// TODO to be implemented
block|}
block|}
end_class

end_unit

