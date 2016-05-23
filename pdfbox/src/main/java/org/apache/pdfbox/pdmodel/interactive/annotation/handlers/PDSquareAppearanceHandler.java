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
name|interactive
operator|.
name|annotation
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
name|PDBorderStyleDictionary
import|;
end_import

begin_comment
comment|/**  * Handler to generate the square annotations appearance.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDSquareAppearanceHandler
extends|extends
name|PDAppearanceHandler
block|{
specifier|public
name|PDSquareAppearanceHandler
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
name|generateNormalAppearance
parameter_list|()
block|{
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|getNormalAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|appearanceEntry
operator|.
name|getAppearanceStream
argument_list|()
decl_stmt|;
name|float
name|lineWidth
init|=
name|getLineWidth
argument_list|()
decl_stmt|;
try|try
block|{
name|PDAppearanceContentStream
name|contentStream
init|=
operator|new
name|PDAppearanceContentStream
argument_list|(
name|appearanceStream
argument_list|)
decl_stmt|;
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
name|contentStream
operator|.
name|setStrokingColor
argument_list|(
name|getColor
argument_list|()
operator|.
name|getComponents
argument_list|()
argument_list|)
expr_stmt|;
comment|// Acrobat doesn't write a line width command
comment|// for a line width of 1 as this is default.
comment|// Will do the same.
if|if
condition|(
operator|!
operator|(
name|Math
operator|.
name|abs
argument_list|(
name|lineWidth
operator|-
literal|1
argument_list|)
operator|<
literal|1e-6
operator|)
condition|)
block|{
name|contentStream
operator|.
name|setLineWidth
argument_list|(
name|lineWidth
argument_list|)
expr_stmt|;
block|}
name|contentStream
operator|.
name|addRect
argument_list|(
name|bbox
operator|.
name|getLowerLeftX
argument_list|()
operator|+
name|lineWidth
argument_list|,
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
operator|+
name|lineWidth
argument_list|,
name|bbox
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|lineWidth
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
operator|*
name|lineWidth
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|stroke
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
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
comment|/**      * Get the line with of the border.      *       * Get the width of the line used to draw a border around      * the annotation. This may either be specified by the annotation      * dictionaries Border setting or by the W entry in the BS border      * style dictionary. If both are missing the default width is 1.      *       * @return the line width      */
comment|// TODO: according to the PDF spec the use of the BS entry is annotation specific
comment|//       so we will leave that to be implemented by individual handlers.
comment|//       If at the end all annotations support the BS entry this can be handled
comment|//       here and removed from the individual handlers.
specifier|public
name|float
name|getLineWidth
parameter_list|()
block|{
name|PDAnnotationSquareCircle
name|annotation
init|=
operator|(
name|PDAnnotationSquareCircle
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
name|PDBorderStyleDictionary
name|bs
init|=
name|annotation
operator|.
name|getBorderStyle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
return|return
name|bs
operator|.
name|getWidth
argument_list|()
return|;
block|}
else|else
block|{
name|COSArray
name|borderCharacteristics
init|=
name|annotation
operator|.
name|getBorder
argument_list|()
decl_stmt|;
if|if
condition|(
name|borderCharacteristics
operator|!=
literal|null
operator|&&
name|borderCharacteristics
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
return|return
name|borderCharacteristics
operator|.
name|getInt
argument_list|(
literal|3
argument_list|)
return|;
block|}
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

