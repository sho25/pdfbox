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
name|PDFormContentStream
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
name|PDPatternContentStream
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
name|color
operator|.
name|PDColorSpace
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
name|graphics
operator|.
name|color
operator|.
name|PDPattern
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
name|form
operator|.
name|PDFormXObject
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
name|pattern
operator|.
name|PDTilingPattern
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
name|PDAnnotationSquiggly
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
name|PDSquigglyAppearanceHandler
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
name|PDSquigglyAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDSquigglyAppearanceHandler
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
specifier|public
name|PDSquigglyAppearanceHandler
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|,
name|PDDocument
name|document
parameter_list|)
block|{
name|super
argument_list|(
name|annotation
argument_list|,
name|document
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
name|PDAnnotationSquiggly
name|annotation
init|=
operator|(
name|PDAnnotationSquiggly
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
name|getQuadPoints
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
comment|// value found in adobe reader
name|ab
operator|.
name|width
operator|=
literal|1.5f
expr_stmt|;
block|}
comment|// Adjust rectangle even if not empty, see PLPDF.com-MarkupAnnotations.pdf
comment|//TODO in a class structure this should be overridable
comment|// this is similar to polyline but different data type
comment|// all coordinates (unlike painting) are used because I'm lazy
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
operator|/
literal|2
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
operator|/
literal|2
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
operator|/
literal|2
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
operator|/
literal|2
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
name|cs
operator|.
name|setStrokingColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
comment|//TODO we ignore dash pattern and line width for now. Do they have any effect?
comment|// quadpoints spec is incorrect
comment|// https://stackoverflow.com/questions/9855814/pdf-spec-vs-acrobat-creation-quadpoints
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
literal|8
condition|;
operator|++
name|i
control|)
block|{
comment|// Adobe uses a fixed pattern that assumes a height of 40, and it transforms to that height
comment|// horizontally and the same / 1.8 vertically.
comment|// translation apparently based on bottom left, but slightly different in Adobe
comment|//TODO what if the annotation is not horizontal?
name|float
name|height
init|=
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|1
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|5
index|]
decl_stmt|;
name|cs
operator|.
name|transform
argument_list|(
operator|new
name|Matrix
argument_list|(
name|height
operator|/
literal|40f
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|height
operator|/
literal|40f
operator|/
literal|1.8f
argument_list|,
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|4
index|]
argument_list|,
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|5
index|]
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create form, BBox is mostly fixed, except for the horizontal size which is
comment|// horizontal size divided by the horizontal transform factor from above
comment|// (almost)
name|PDFormXObject
name|form
init|=
operator|new
name|PDFormXObject
argument_list|(
name|createCOSStream
argument_list|()
argument_list|)
decl_stmt|;
name|form
operator|.
name|setBBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
operator|-
literal|0.5f
argument_list|,
operator|-
literal|0.5f
argument_list|,
operator|(
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|2
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
index|]
operator|)
operator|/
name|height
operator|*
literal|40f
operator|+
literal|0.5f
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|form
operator|.
name|setResources
argument_list|(
operator|new
name|PDResources
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|setMatrix
argument_list|(
name|AffineTransform
operator|.
name|getTranslateInstance
argument_list|(
literal|0.5f
argument_list|,
literal|0.5f
argument_list|)
argument_list|)
expr_stmt|;
name|cs
operator|.
name|drawForm
argument_list|(
name|form
argument_list|)
expr_stmt|;
try|try
init|(
name|PDFormContentStream
name|formCS
init|=
operator|new
name|PDFormContentStream
argument_list|(
name|form
argument_list|)
init|)
block|{
name|PDTilingPattern
name|pattern
init|=
operator|new
name|PDTilingPattern
argument_list|()
decl_stmt|;
name|pattern
operator|.
name|setBBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|pattern
operator|.
name|setXStep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|pattern
operator|.
name|setYStep
argument_list|(
literal|13
argument_list|)
expr_stmt|;
name|pattern
operator|.
name|setTilingType
argument_list|(
name|PDTilingPattern
operator|.
name|TILING_CONSTANT_SPACING_FASTER_TILING
argument_list|)
expr_stmt|;
name|pattern
operator|.
name|setPaintType
argument_list|(
name|PDTilingPattern
operator|.
name|PAINT_UNCOLORED
argument_list|)
expr_stmt|;
try|try
init|(
name|PDPatternContentStream
name|patternCS
init|=
operator|new
name|PDPatternContentStream
argument_list|(
name|pattern
argument_list|)
init|)
block|{
comment|// from Adobe
name|patternCS
operator|.
name|setLineCapStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|setLineJoinStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|setLineWidth
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|setMiterLimit
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|moveTo
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|lineTo
argument_list|(
literal|5
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|lineTo
argument_list|(
literal|10
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|patternCS
operator|.
name|stroke
argument_list|()
expr_stmt|;
block|}
name|COSName
name|patternName
init|=
name|form
operator|.
name|getResources
argument_list|()
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|PDColorSpace
name|patternColorSpace
init|=
operator|new
name|PDPattern
argument_list|(
literal|null
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|PDColor
name|patternColor
init|=
operator|new
name|PDColor
argument_list|(
name|color
operator|.
name|getComponents
argument_list|()
argument_list|,
name|patternName
argument_list|,
name|patternColorSpace
argument_list|)
decl_stmt|;
name|formCS
operator|.
name|setNonStrokingColor
argument_list|(
name|patternColor
argument_list|)
expr_stmt|;
comment|// With Adobe, the horizontal size is slightly different, don't know why
name|formCS
operator|.
name|addRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|2
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
index|]
operator|)
operator|/
name|height
operator|*
literal|40f
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|formCS
operator|.
name|fill
argument_list|()
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

