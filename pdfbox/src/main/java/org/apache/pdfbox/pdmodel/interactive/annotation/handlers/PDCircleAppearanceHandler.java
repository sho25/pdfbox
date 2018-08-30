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
name|PDAnnotationCircle
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationMarkup
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

begin_comment
comment|/**  * Handler to generate the circle annotations appearance.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDCircleAppearanceHandler
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
name|PDCircleAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDCircleAppearanceHandler
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
name|PDAnnotationCircle
name|annotation
init|=
operator|(
name|PDAnnotationCircle
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
argument_list|,
name|annotation
operator|.
name|getBorder
argument_list|()
argument_list|)
expr_stmt|;
name|PDBorderEffectDictionary
name|borderEffect
init|=
name|annotation
operator|.
name|getBorderEffect
argument_list|()
decl_stmt|;
comment|// Acrobat applies a padding to each side of the bbox so the line is completely within
comment|// the bbox.
comment|// TODO: Needs validation for Circles as Adobe Reader seems to extend the bbox bei the rect differenve
comment|// for circle annotations.
name|PDRectangle
name|bbox
init|=
name|getRectangle
argument_list|()
decl_stmt|;
name|PDRectangle
name|borderEdge
init|=
name|getPaddedRectangle
argument_list|(
name|bbox
argument_list|,
name|lineWidth
operator|/
literal|2
argument_list|)
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
name|contentStream
argument_list|,
name|borderEffect
operator|.
name|getIntensity
argument_list|()
argument_list|,
name|lineWidth
argument_list|,
name|getRectangle
argument_list|()
argument_list|)
decl_stmt|;
name|cloudyBorder
operator|.
name|createCloudyEllipse
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
comment|// the differences rectangle
if|if
condition|(
name|lineWidth
operator|>
literal|0
condition|)
block|{
name|annotation
operator|.
name|setRectDifferences
argument_list|(
name|lineWidth
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
comment|// lower left corner
name|float
name|x0
init|=
name|borderEdge
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y0
init|=
name|borderEdge
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
comment|// upper right corner
name|float
name|x1
init|=
name|borderEdge
operator|.
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|y1
init|=
name|borderEdge
operator|.
name|getUpperRightY
argument_list|()
decl_stmt|;
comment|// mid points
name|float
name|xm
init|=
name|x0
operator|+
name|borderEdge
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
decl_stmt|;
name|float
name|ym
init|=
name|y0
operator|+
name|borderEdge
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
decl_stmt|;
comment|// see http://spencermortensen.com/articles/bezier-circle/
comment|// the below number was calculated from sampling content streams
comment|// generated using Adobe Reader
name|float
name|magic
init|=
literal|0.55555417f
decl_stmt|;
comment|// control point offsets
name|float
name|vOffset
init|=
name|borderEdge
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
operator|*
name|magic
decl_stmt|;
name|float
name|hOffset
init|=
name|borderEdge
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
operator|*
name|magic
decl_stmt|;
name|contentStream
operator|.
name|moveTo
argument_list|(
name|xm
argument_list|,
name|y1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
operator|(
name|xm
operator|+
name|hOffset
operator|)
argument_list|,
name|y1
argument_list|,
name|x1
argument_list|,
operator|(
name|ym
operator|+
name|vOffset
operator|)
argument_list|,
name|x1
argument_list|,
name|ym
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
name|x1
argument_list|,
operator|(
name|ym
operator|-
name|vOffset
operator|)
argument_list|,
operator|(
name|xm
operator|+
name|hOffset
operator|)
argument_list|,
name|y0
argument_list|,
name|xm
argument_list|,
name|y0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
operator|(
name|xm
operator|-
name|hOffset
operator|)
argument_list|,
name|y0
argument_list|,
name|x0
argument_list|,
operator|(
name|ym
operator|-
name|vOffset
operator|)
argument_list|,
name|x0
argument_list|,
name|ym
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|curveTo
argument_list|(
name|x0
argument_list|,
operator|(
name|ym
operator|+
name|vOffset
operator|)
argument_list|,
operator|(
name|xm
operator|-
name|hOffset
operator|)
argument_list|,
name|y1
argument_list|,
name|xm
argument_list|,
name|y1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
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
name|PDAnnotationMarkup
name|annotation
init|=
operator|(
name|PDAnnotationMarkup
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
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

