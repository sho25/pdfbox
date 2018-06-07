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
name|PDAnnotationText
operator|.
name|NAME_NOTE
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//TODO Comment, Key, Help, NewParagraph, Paragraph, Insert
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
name|boolean
name|hasBackground
init|=
name|contentStream
operator|.
name|setNonStrokingColorOnDemand
argument_list|(
name|getColor
argument_list|()
argument_list|)
decl_stmt|;
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
comment|//TODO find out what Adobe chooses if color is missing
name|PDRectangle
name|rect
init|=
name|getRectangle
argument_list|()
decl_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|annotation
operator|.
name|getNormalAppearanceStream
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
name|appearanceStream
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
argument_list|,
name|hasBackground
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
parameter_list|,
name|boolean
name|hasBackground
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
comment|// round edge
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
name|drawShape
argument_list|(
literal|1
argument_list|,
literal|true
argument_list|,
name|hasBackground
argument_list|)
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

