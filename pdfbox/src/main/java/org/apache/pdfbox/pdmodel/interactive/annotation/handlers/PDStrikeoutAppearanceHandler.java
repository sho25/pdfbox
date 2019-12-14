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
name|PDAnnotationStrikeout
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PDStrikeoutAppearanceHandler
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
name|PDStrikeoutAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDStrikeoutAppearanceHandler
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
name|PDStrikeoutAppearanceHandler
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
name|PDAnnotationStrikeout
name|annotation
init|=
operator|(
name|PDAnnotationStrikeout
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
comment|// spec is incorrect
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
comment|// get mid point between bounds, subtract the line width to approximate what Adobe is doing
comment|// See e.g. CTAN-example-Annotations.pdf and PLPDF.com-MarkupAnnotations.pdf
comment|// and https://bugs.ghostscript.com/show_bug.cgi?id=693664
comment|// do the math for diagonal annotations with this weird old trick:
comment|// https://stackoverflow.com/questions/7740507/extend-a-line-segment-a-specific-distance
name|float
name|len0
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
name|pathsArray
index|[
name|i
operator|*
literal|8
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|4
index|]
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
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
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|float
name|x0
init|=
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|4
index|]
decl_stmt|;
name|float
name|y0
init|=
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|5
index|]
decl_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|len0
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
comment|// only if both coordinates are not identical to avoid divide by zero
name|x0
operator|+=
operator|(
name|pathsArray
index|[
name|i
operator|*
literal|8
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|4
index|]
operator|)
operator|/
name|len0
operator|*
operator|(
name|len0
operator|/
literal|2
operator|-
name|ab
operator|.
name|width
operator|)
expr_stmt|;
name|y0
operator|+=
operator|(
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
operator|)
operator|/
name|len0
operator|*
operator|(
name|len0
operator|/
literal|2
operator|-
name|ab
operator|.
name|width
operator|)
expr_stmt|;
block|}
name|float
name|len1
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
operator|+
literal|6
index|]
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|3
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|7
index|]
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|float
name|x1
init|=
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|6
index|]
decl_stmt|;
name|float
name|y1
init|=
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|7
index|]
decl_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|len1
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
comment|// only if both coordinates are not identical to avoid divide by zero
name|x1
operator|+=
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
operator|+
literal|6
index|]
operator|)
operator|/
name|len1
operator|*
operator|(
name|len1
operator|/
literal|2
operator|-
name|ab
operator|.
name|width
operator|)
expr_stmt|;
name|y1
operator|+=
operator|(
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|3
index|]
operator|-
name|pathsArray
index|[
name|i
operator|*
literal|8
operator|+
literal|7
index|]
operator|)
operator|/
name|len1
operator|*
operator|(
name|len1
operator|/
literal|2
operator|-
name|ab
operator|.
name|width
operator|)
expr_stmt|;
block|}
name|cs
operator|.
name|moveTo
argument_list|(
name|x0
argument_list|,
name|y0
argument_list|)
expr_stmt|;
name|cs
operator|.
name|lineTo
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|stroke
argument_list|()
expr_stmt|;
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

