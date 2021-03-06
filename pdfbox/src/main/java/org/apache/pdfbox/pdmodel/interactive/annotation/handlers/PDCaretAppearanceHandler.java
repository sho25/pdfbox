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
name|PDAnnotationCaret
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * Handler to generate the caret annotations appearance.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDCaretAppearanceHandler
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
name|PDCaretAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDCaretAppearanceHandler
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
name|PDCaretAppearanceHandler
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
name|PDAnnotationCaret
name|annotation
init|=
operator|(
name|PDAnnotationCaret
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
try|try
init|(
name|PDAppearanceContentStream
name|contentStream
init|=
name|getNormalAppearanceAsContentStream
argument_list|()
init|)
block|{
name|contentStream
operator|.
name|setStrokingColor
argument_list|(
name|getColor
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|getColor
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|PDRectangle
argument_list|(
name|rect
operator|.
name|getWidth
argument_list|()
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|annotation
operator|.
name|getCOSObject
argument_list|()
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|RD
argument_list|)
condition|)
block|{
comment|// Adobe creates the /RD entry with a number that is decided
comment|// by dividing the height by 10, with a maximum result of 5.
comment|// That number is then used to enlarge the bbox and the rectangle and added to the
comment|// translation values in the matrix and also used for the line width
comment|// (not here because it has no effect, see comment near fill() ).
comment|// The curves are based on the original rectangle.
name|float
name|rd
init|=
name|Math
operator|.
name|min
argument_list|(
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|10
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|setRectDifferences
argument_list|(
name|rd
argument_list|)
expr_stmt|;
name|bbox
operator|=
operator|new
name|PDRectangle
argument_list|(
operator|-
name|rd
argument_list|,
operator|-
name|rd
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
operator|+
literal|2
operator|*
name|rd
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|+
literal|2
operator|*
name|rd
argument_list|)
expr_stmt|;
name|Matrix
name|matrix
init|=
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|getMatrix
argument_list|()
decl_stmt|;
name|matrix
operator|.
name|transformPoint
argument_list|(
name|rd
argument_list|,
name|rd
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|setMatrix
argument_list|(
name|matrix
operator|.
name|createAffineTransform
argument_list|()
argument_list|)
expr_stmt|;
name|PDRectangle
name|rect2
init|=
operator|new
name|PDRectangle
argument_list|(
name|rect
operator|.
name|getLowerLeftX
argument_list|()
operator|-
name|rd
argument_list|,
name|rect
operator|.
name|getLowerLeftY
argument_list|()
operator|-
name|rd
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
operator|+
literal|2
operator|*
name|rd
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|+
literal|2
operator|*
name|rd
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|setRectangle
argument_list|(
name|rect2
argument_list|)
expr_stmt|;
block|}
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
name|float
name|halfX
init|=
name|rect
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
decl_stmt|;
name|float
name|halfY
init|=
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
decl_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
name|halfX
argument_list|,
literal|0
argument_list|,
name|halfX
argument_list|,
name|halfY
argument_list|,
name|halfX
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
name|halfX
argument_list|,
name|halfY
argument_list|,
name|halfX
argument_list|,
literal|0
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|closePath
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|fill
argument_list|()
expr_stmt|;
comment|// Adobe has an additional stroke, but it has no effect
comment|// because fill "consumes" the path.
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

