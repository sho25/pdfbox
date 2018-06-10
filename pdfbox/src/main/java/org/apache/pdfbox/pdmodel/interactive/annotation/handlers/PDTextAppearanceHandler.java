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
name|awt
operator|.
name|geom
operator|.
name|GeneralPath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|PathIterator
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
name|PDAnnotationText
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
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDTextAppearanceHandler
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
name|PDTextAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SUPPORTED_NAMES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|SUPPORTED_NAMES
operator|.
name|add
argument_list|(
name|PDAnnotationText
operator|.
name|NAME_NOTE
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMES
operator|.
name|add
argument_list|(
name|PDAnnotationText
operator|.
name|NAME_INSERT
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMES
operator|.
name|add
argument_list|(
name|PDAnnotationText
operator|.
name|NAME_CROSS
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMES
operator|.
name|add
argument_list|(
name|PDAnnotationText
operator|.
name|NAME_HELP
argument_list|)
expr_stmt|;
name|SUPPORTED_NAMES
operator|.
name|add
argument_list|(
name|PDAnnotationText
operator|.
name|NAME_CIRCLE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PDTextAppearanceHandler
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
name|PDAnnotationText
name|annotation
init|=
operator|(
name|PDAnnotationText
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|SUPPORTED_NAMES
operator|.
name|contains
argument_list|(
name|annotation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//TODO Comment, Key, NewParagraph, Paragraph
return|return;
block|}
try|try
init|(
name|PDAppearanceContentStream
name|contentStream
init|=
name|getNormalAppearanceAsContentStream
argument_list|()
init|)
block|{
name|PDColor
name|bgColor
init|=
name|getColor
argument_list|()
decl_stmt|;
if|if
condition|(
name|bgColor
operator|==
literal|null
condition|)
block|{
comment|// White is used by Adobe when /C entry is missing
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
literal|1f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|bgColor
argument_list|)
expr_stmt|;
block|}
comment|// stroking color is always black which is the PDF default
name|setOpacity
argument_list|(
name|contentStream
argument_list|,
name|annotation
operator|.
name|getConstantOpacity
argument_list|()
argument_list|)
expr_stmt|;
name|PDRectangle
name|rect
init|=
name|getRectangle
argument_list|()
decl_stmt|;
name|PDRectangle
name|bbox
init|=
name|rect
operator|.
name|createRetranslatedRectangle
argument_list|()
decl_stmt|;
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|setBBox
argument_list|(
name|bbox
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|annotation
operator|.
name|getName
argument_list|()
condition|)
block|{
case|case
name|PDAnnotationText
operator|.
name|NAME_NOTE
case|:
name|drawNote
argument_list|(
name|contentStream
argument_list|,
name|bbox
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationText
operator|.
name|NAME_CROSS
case|:
name|drawCross
argument_list|(
name|contentStream
argument_list|,
name|bbox
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationText
operator|.
name|NAME_CIRCLE
case|:
name|drawCircles
argument_list|(
name|contentStream
argument_list|,
name|bbox
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationText
operator|.
name|NAME_INSERT
case|:
name|drawInsert
argument_list|(
name|contentStream
argument_list|,
name|bbox
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDAnnotationText
operator|.
name|NAME_HELP
case|:
name|drawHelp
argument_list|(
name|contentStream
argument_list|,
name|bbox
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|drawNote
parameter_list|(
specifier|final
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|PDRectangle
name|bbox
parameter_list|)
throws|throws
name|IOException
block|{
name|contentStream
operator|.
name|setLineJoinStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// get round edge the easy way
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|0.61f
argument_list|)
expr_stmt|;
comment|// value from Adobe
name|contentStream
operator|.
name|addRect
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|bbox
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|4
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|*
literal|3
operator|/
literal|4
operator|-
literal|1
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|4
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|3
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|*
literal|3
operator|/
literal|4
operator|-
literal|1
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|3
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|4
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|*
literal|3
operator|/
literal|4
operator|-
literal|1
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|4
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|5
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|*
literal|3
operator|/
literal|4
operator|-
literal|1
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|7
operator|*
literal|5
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fillAndStroke
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|drawCircles
parameter_list|(
specifier|final
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|PDRectangle
name|bbox
parameter_list|)
throws|throws
name|IOException
block|{
comment|// strategy used by Adobe:
comment|// 1) add small circle in white using /ca /CA 0.6 and width 1
comment|// 2) fill
comment|// 3) add small circle in one direction
comment|// 4) add large circle in other direction
comment|// 5) stroke + fill
comment|// with square width 20 small r = 6.36, large r = 9.756
comment|// should be a square, but who knows...
name|float
name|min
init|=
name|Math
operator|.
name|min
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|smallR
init|=
name|min
operator|/
literal|20
operator|*
literal|6.36f
decl_stmt|;
name|float
name|largeR
init|=
name|min
operator|/
literal|20
operator|*
literal|9.756f
decl_stmt|;
name|contentStream
operator|.
name|setMiterLimit
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineJoinStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineCapStyle
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PDExtendedGraphicsState
name|gs
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|gs
operator|.
name|setAlphaSourceFlag
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setStrokingAlphaConstant
argument_list|(
literal|0.6f
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setNonStrokingAlphaConstant
argument_list|(
literal|0.6f
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setBlendMode
argument_list|(
name|BlendMode
operator|.
name|NORMAL
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setGraphicsStateParameters
argument_list|(
name|gs
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
literal|1f
argument_list|)
expr_stmt|;
name|drawCircle
argument_list|(
name|contentStream
argument_list|,
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
name|smallR
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fill
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|0.59f
argument_list|)
expr_stmt|;
comment|// value from Adobe
name|drawCircle
argument_list|(
name|contentStream
argument_list|,
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
name|smallR
argument_list|)
expr_stmt|;
name|drawCircle2
argument_list|(
name|contentStream
argument_list|,
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
name|largeR
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fillAndStroke
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|drawInsert
parameter_list|(
specifier|final
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|PDRectangle
name|bbox
parameter_list|)
throws|throws
name|IOException
block|{
name|contentStream
operator|.
name|setMiterLimit
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineJoinStyle
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineCapStyle
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|0.59f
argument_list|)
expr_stmt|;
comment|// value from Adobe
name|contentStream
operator|.
name|moveTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
operator|-
literal|1
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|closeAndFillAndStroke
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|drawCross
parameter_list|(
specifier|final
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|PDRectangle
name|bbox
parameter_list|)
throws|throws
name|IOException
block|{
comment|// should be a square, but who knows...
name|float
name|min
init|=
name|Math
operator|.
name|min
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
comment|// small = offset nearest bbox edge
comment|// large = offset second nearest bbox edge
name|float
name|small
init|=
name|min
operator|/
literal|10
decl_stmt|;
name|float
name|large
init|=
name|min
operator|/
literal|5
decl_stmt|;
name|contentStream
operator|.
name|setMiterLimit
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineJoinStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineCapStyle
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|0.59f
argument_list|)
expr_stmt|;
comment|// value from Adobe
name|contentStream
operator|.
name|moveTo
argument_list|(
name|small
argument_list|,
name|large
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|large
argument_list|,
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
operator|-
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|-
name|large
argument_list|,
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|-
name|small
argument_list|,
name|large
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|/
literal|2
operator|+
name|small
argument_list|,
name|min
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|-
name|small
argument_list|,
name|min
operator|-
name|large
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|-
name|large
argument_list|,
name|min
operator|-
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
operator|+
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|large
argument_list|,
name|min
operator|-
name|small
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|small
argument_list|,
name|min
operator|-
name|large
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|lineTo
argument_list|(
name|min
operator|/
literal|2
operator|-
name|small
argument_list|,
name|min
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|closeAndFillAndStroke
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|drawHelp
parameter_list|(
specifier|final
name|PDAppearanceContentStream
name|contentStream
parameter_list|,
name|PDRectangle
name|bbox
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|min
init|=
name|Math
operator|.
name|min
argument_list|(
name|bbox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|setMiterLimit
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineJoinStyle
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineCapStyle
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|0.59f
argument_list|)
expr_stmt|;
comment|// value from Adobe
comment|// Adobe first fills a white circle with CA ca 0.6, so do we
name|contentStream
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setLineWidth
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PDExtendedGraphicsState
name|gs
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|gs
operator|.
name|setAlphaSourceFlag
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setStrokingAlphaConstant
argument_list|(
literal|0.6f
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setNonStrokingAlphaConstant
argument_list|(
literal|0.6f
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setBlendMode
argument_list|(
name|BlendMode
operator|.
name|NORMAL
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setGraphicsStateParameters
argument_list|(
name|gs
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
literal|1f
argument_list|)
expr_stmt|;
name|drawCircle2
argument_list|(
name|contentStream
argument_list|,
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
operator|-
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fill
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
comment|// rescale so that "?" fits into circle and move "?" to circle center
comment|// values gathered by trial and error
name|contentStream
operator|.
name|transform
argument_list|(
name|Matrix
operator|.
name|getScaleInstance
argument_list|(
literal|0.001f
operator|*
name|min
operator|/
literal|2.25f
argument_list|,
literal|0.001f
operator|*
name|min
operator|/
literal|2.25f
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|transform
argument_list|(
name|Matrix
operator|.
name|getTranslateInstance
argument_list|(
literal|540
argument_list|,
literal|375
argument_list|)
argument_list|)
expr_stmt|;
comment|// we get the shape of an Helvetica "?" and use that one.
comment|// Adobe uses a different font (which one?), or created the shape from scratch.
name|GeneralPath
name|path
init|=
name|PDType1Font
operator|.
name|HELVETICA
operator|.
name|getPath
argument_list|(
literal|"question"
argument_list|)
decl_stmt|;
name|PathIterator
name|it
init|=
name|path
operator|.
name|getPathIterator
argument_list|(
operator|new
name|AffineTransform
argument_list|()
argument_list|)
decl_stmt|;
name|double
index|[]
name|coords
init|=
operator|new
name|double
index|[
literal|6
index|]
decl_stmt|;
while|while
condition|(
operator|!
name|it
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|int
name|type
init|=
name|it
operator|.
name|currentSegment
argument_list|(
name|coords
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|PathIterator
operator|.
name|SEG_CLOSE
case|:
name|contentStream
operator|.
name|closePath
argument_list|()
expr_stmt|;
break|break;
case|case
name|PathIterator
operator|.
name|SEG_CUBICTO
case|:
name|contentStream
operator|.
name|curveTo
argument_list|(
operator|(
name|float
operator|)
name|coords
index|[
literal|0
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|1
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|2
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|3
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|4
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|PathIterator
operator|.
name|SEG_QUADTO
case|:
name|contentStream
operator|.
name|curveTo1
argument_list|(
operator|(
name|float
operator|)
name|coords
index|[
literal|0
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|1
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|2
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
comment|// not sure whether curveTo1 or curveTo2 is to be used here
break|break;
case|case
name|PathIterator
operator|.
name|SEG_LINETO
case|:
name|contentStream
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|coords
index|[
literal|0
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|PathIterator
operator|.
name|SEG_MOVETO
case|:
name|contentStream
operator|.
name|moveTo
argument_list|(
operator|(
name|float
operator|)
name|coords
index|[
literal|0
index|]
argument_list|,
operator|(
name|float
operator|)
name|coords
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|contentStream
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
comment|// draw the outer circle counterclockwise to fill area between circle and "?"
name|drawCircle2
argument_list|(
name|contentStream
argument_list|,
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
argument_list|,
name|min
operator|/
literal|2
operator|-
literal|1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fillAndStroke
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

