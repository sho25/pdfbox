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
name|blend
operator|.
name|BlendMode
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
name|PDAnnotationHighlight
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
comment|/**  *   * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDHighlightAppearanceHandler
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
name|PDHighlightAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDHighlightAppearanceHandler
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
name|PDHighlightAppearanceHandler
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
name|PDAnnotationHighlight
name|annotation
init|=
operator|(
name|PDAnnotationHighlight
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
comment|// Adjust rectangle even if not empty, see PLPDF.com-MarkupAnnotations.pdf
comment|//TODO in a class structure this should be overridable
comment|// this is similar to polyline but different data type
comment|//TODO padding should consider the curves too; needs to know in advance where the curve is
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
comment|// get the delta used for curves and use it for padding
name|float
name|maxDelta
init|=
literal|0
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
literal|8
condition|;
operator|++
name|i
control|)
block|{
comment|// one of the two is 0, depending whether the rectangle is
comment|// horizontal or vertical
comment|// if it is diagonal then... uh...
name|float
name|delta
init|=
name|Math
operator|.
name|max
argument_list|(
operator|(
name|pathsArray
index|[
name|i
operator|+
literal|0
index|]
operator|-
name|pathsArray
index|[
name|i
operator|+
literal|4
index|]
operator|)
operator|/
literal|4
argument_list|,
operator|(
name|pathsArray
index|[
name|i
operator|+
literal|1
index|]
operator|-
name|pathsArray
index|[
name|i
operator|+
literal|5
index|]
operator|)
operator|/
literal|4
argument_list|)
decl_stmt|;
name|maxDelta
operator|=
name|Math
operator|.
name|max
argument_list|(
name|delta
argument_list|,
name|maxDelta
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
operator|-
name|maxDelta
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
operator|-
name|maxDelta
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
operator|+
name|maxDelta
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
operator|+
name|maxDelta
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
name|PDExtendedGraphicsState
name|r0
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|PDExtendedGraphicsState
name|r1
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|r0
operator|.
name|setAlphaSourceFlag
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r0
operator|.
name|setStrokingAlphaConstant
argument_list|(
name|annotation
operator|.
name|getConstantOpacity
argument_list|()
argument_list|)
expr_stmt|;
name|r0
operator|.
name|setNonStrokingAlphaConstant
argument_list|(
name|annotation
operator|.
name|getConstantOpacity
argument_list|()
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setAlphaSourceFlag
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setBlendMode
argument_list|(
name|BlendMode
operator|.
name|MULTIPLY
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setGraphicsStateParameters
argument_list|(
name|r0
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setGraphicsStateParameters
argument_list|(
name|r1
argument_list|)
expr_stmt|;
comment|//TODO replace with document.getDocument().createCOSStream()
comment|//     or call new PDFormXObject(document)
name|PDFormXObject
name|frm1
init|=
operator|new
name|PDFormXObject
argument_list|(
name|createCOSStream
argument_list|()
argument_list|)
decl_stmt|;
name|PDFormXObject
name|frm2
init|=
operator|new
name|PDFormXObject
argument_list|(
name|createCOSStream
argument_list|()
argument_list|)
decl_stmt|;
name|frm1
operator|.
name|setResources
argument_list|(
operator|new
name|PDResources
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|PDFormContentStream
name|mwfofrmCS
init|=
operator|new
name|PDFormContentStream
argument_list|(
name|frm1
argument_list|)
init|)
block|{
name|mwfofrmCS
operator|.
name|drawForm
argument_list|(
name|frm2
argument_list|)
expr_stmt|;
block|}
name|frm1
operator|.
name|setBBox
argument_list|(
name|annotation
operator|.
name|getRectangle
argument_list|()
argument_list|)
expr_stmt|;
name|COSDictionary
name|groupDict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|groupDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|COSName
operator|.
name|TRANSPARENCY
argument_list|)
expr_stmt|;
comment|//TODO PDFormXObject.setGroup() is missing
name|frm1
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|GROUP
argument_list|,
name|groupDict
argument_list|)
expr_stmt|;
name|cs
operator|.
name|drawForm
argument_list|(
name|frm1
argument_list|)
expr_stmt|;
name|frm2
operator|.
name|setBBox
argument_list|(
name|annotation
operator|.
name|getRectangle
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|PDFormContentStream
name|frm2CS
init|=
operator|new
name|PDFormContentStream
argument_list|(
name|frm2
argument_list|)
init|)
block|{
name|frm2CS
operator|.
name|setNonStrokingColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
name|int
name|of
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|of
operator|+
literal|7
operator|<
name|pathsArray
operator|.
name|length
condition|)
block|{
comment|// quadpoints spec sequence is incorrect, correct one is (4,5 0,1 2,3 6,7)
comment|// https://stackoverflow.com/questions/9855814/pdf-spec-vs-acrobat-creation-quadpoints
comment|// for "curvy" highlighting, two Bézier control points are used that seem to have a
comment|// distance of about 1/4 of the height.
comment|// note that curves won't appear if outside of the rectangle
name|float
name|delta
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// horizontal highlight
name|delta
operator|=
operator|(
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
operator|-
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
operator|)
operator|/
literal|4
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// vertical highlight
name|delta
operator|=
operator|(
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
operator|-
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
operator|)
operator|/
literal|4
expr_stmt|;
block|}
name|frm2CS
operator|.
name|moveTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// horizontal highlight
name|frm2CS
operator|.
name|curveTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// vertical highlight
name|frm2CS
operator|.
name|curveTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|4
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|5
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|frm2CS
operator|.
name|lineTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|0
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
name|frm2CS
operator|.
name|lineTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// horizontal highlight
name|frm2CS
operator|.
name|curveTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
operator|==
literal|0
condition|)
block|{
comment|// vertical highlight
name|frm2CS
operator|.
name|curveTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|2
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|3
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
operator|+
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
operator|-
name|delta
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|frm2CS
operator|.
name|lineTo
argument_list|(
name|pathsArray
index|[
name|of
operator|+
literal|6
index|]
argument_list|,
name|pathsArray
index|[
name|of
operator|+
literal|7
index|]
argument_list|)
expr_stmt|;
block|}
name|frm2CS
operator|.
name|fill
argument_list|()
expr_stmt|;
name|of
operator|+=
literal|8
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

