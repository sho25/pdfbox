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
name|COSNumber
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
name|PDAnnotationSquare
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
name|PDSquareAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|float
name|lineWidth
init|=
name|getLineWidth
argument_list|()
decl_stmt|;
try|try
block|{
name|PDAnnotationSquare
name|annotation
init|=
operator|(
name|PDAnnotationSquare
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
name|boolean
name|hasStroke
init|=
name|contentStream
operator|.
name|setStrokingColorOnDemand
argument_list|(
name|getColor
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|hasBackground
init|=
name|contentStream
operator|.
name|setNonStrokingColorOnDemand
argument_list|(
name|annotation
operator|.
name|getInteriorColor
argument_list|()
argument_list|)
decl_stmt|;
name|handleOpacity
argument_list|(
name|annotation
operator|.
name|getConstantOpacity
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setBorderLine
argument_list|(
name|lineWidth
argument_list|,
name|annotation
operator|.
name|getBorderStyle
argument_list|()
argument_list|)
expr_stmt|;
comment|// the differences rectangle
comment|// TODO: this only works for border effect solid. Cloudy needs a different approach.
name|setRectDifference
argument_list|(
name|lineWidth
argument_list|)
expr_stmt|;
comment|// Acrobat applies a padding to each side of the bbox so the line is completely within
comment|// the bbox.
name|PDRectangle
name|borderEdge
init|=
name|getPaddedRectangle
argument_list|(
name|getRectangle
argument_list|()
argument_list|,
name|lineWidth
operator|/
literal|2
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|addRect
argument_list|(
name|borderEdge
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|borderEdge
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|borderEdge
operator|.
name|getWidth
argument_list|()
argument_list|,
name|borderEdge
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawShape
argument_list|(
name|lineWidth
argument_list|,
name|hasStroke
argument_list|,
name|hasBackground
argument_list|)
expr_stmt|;
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
comment|/**      * Get the line with of the border.      *       * Get the width of the line used to draw a border around the annotation.      * This may either be specified by the annotation dictionaries Border      * setting or by the W entry in the BS border style dictionary. If both are      * missing the default width is 1.      *       * @return the line width      */
comment|// TODO: according to the PDF spec the use of the BS entry is annotation
comment|// specific
comment|// so we will leave that to be implemented by individual handlers.
comment|// If at the end all annotations support the BS entry this can be handled
comment|// here and removed from the individual handlers.
name|float
name|getLineWidth
parameter_list|()
block|{
name|PDAnnotationSquare
name|annotation
init|=
operator|(
name|PDAnnotationSquare
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
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
name|COSBase
name|base
init|=
name|borderCharacteristics
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|base
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

