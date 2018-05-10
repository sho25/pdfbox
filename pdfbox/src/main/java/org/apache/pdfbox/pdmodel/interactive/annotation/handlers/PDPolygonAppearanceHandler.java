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
name|PDAnnotationPolygon
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
comment|/**  * Handler to generate the polygon annotations appearance.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDPolygonAppearanceHandler
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
name|PDPolygonAppearanceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|PDPolygonAppearanceHandler
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
name|PDAnnotationPolygon
name|annotation
init|=
operator|(
name|PDAnnotationPolygon
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
name|float
name|lineWidth
init|=
name|getLineWidth
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
comment|// Adjust rectangle even if not empty
comment|// CTAN-example-Annotations.pdf p2
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
name|float
index|[]
index|[]
name|pathArray
init|=
name|getPathArray
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathArray
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pathArray
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|pathArray
index|[
name|i
index|]
operator|.
name|length
operator|/
literal|2
condition|;
operator|++
name|j
control|)
block|{
name|float
name|x
init|=
name|pathArray
index|[
name|i
index|]
index|[
name|j
operator|*
literal|2
index|]
decl_stmt|;
name|float
name|y
init|=
name|pathArray
index|[
name|i
index|]
index|[
name|j
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
name|lineWidth
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
name|lineWidth
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
name|lineWidth
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
name|lineWidth
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
block|{
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
comment|//TODO find better way to do this. Either pass border array to
comment|// setBorderLine(), or use AnnotationBorder class
if|if
condition|(
name|annotation
operator|.
name|getBorderStyle
argument_list|()
operator|==
literal|null
condition|)
block|{
name|COSArray
name|border
init|=
name|annotation
operator|.
name|getBorder
argument_list|()
decl_stmt|;
if|if
condition|(
name|border
operator|.
name|size
argument_list|()
operator|>
literal|3
operator|&&
name|border
operator|.
name|getObject
argument_list|(
literal|3
argument_list|)
operator|instanceof
name|COSArray
condition|)
block|{
name|contentStream
operator|.
name|setLineDashPattern
argument_list|(
operator|(
operator|(
name|COSArray
operator|)
name|border
operator|.
name|getObject
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|toFloatArray
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
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
name|createCloudyPolygon
argument_list|(
name|pathArray
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
name|setRectDifference
argument_list|(
name|lineWidth
argument_list|)
expr_stmt|;
comment|// Acrobat applies a padding to each side of the bbox so the line is
comment|// completely within the bbox.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pathArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|float
index|[]
name|pointsArray
init|=
name|pathArray
index|[
name|i
index|]
decl_stmt|;
comment|// first array shall be of size 2 and specify the moveto operator
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
name|pointsArray
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|contentStream
operator|.
name|moveTo
argument_list|(
name|pointsArray
index|[
literal|0
index|]
argument_list|,
name|pointsArray
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// entries of length 2 shall be treated as lineto operator
if|if
condition|(
name|pointsArray
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|contentStream
operator|.
name|lineTo
argument_list|(
name|pointsArray
index|[
literal|0
index|]
argument_list|,
name|pointsArray
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pointsArray
operator|.
name|length
operator|==
literal|6
condition|)
block|{
name|contentStream
operator|.
name|curveTo
argument_list|(
name|pointsArray
index|[
literal|0
index|]
argument_list|,
name|pointsArray
index|[
literal|1
index|]
argument_list|,
name|pointsArray
index|[
literal|2
index|]
argument_list|,
name|pointsArray
index|[
literal|3
index|]
argument_list|,
name|pointsArray
index|[
literal|4
index|]
argument_list|,
name|pointsArray
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//TODO line endings (LE) are missing
comment|// How it could be done by reusing some of the code from
comment|// the Line and StrikeOut handlers
comment|// 1) if the LE is contained in SHORT_STYLES,
comment|// shorten the first + last arms with "this weird old trick"
comment|// used in the StrikeOut handler
comment|// and paint
comment|// 2) do a transform so that first and last arms are imagined flat
comment|// (like in Line handler)
comment|// 3) refactor + reuse the line handler code that draws the ending shapes
comment|// the alternative would be to apply the transform to the LE shapes directly,
comment|// which would be more work and produce code difficult to understand
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
specifier|private
name|float
index|[]
index|[]
name|getPathArray
parameter_list|(
name|PDAnnotationPolygon
name|annotation
parameter_list|)
block|{
comment|// PDF 2.0: Path takes priority over Vertices
name|float
index|[]
index|[]
name|pathArray
init|=
name|annotation
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathArray
operator|==
literal|null
condition|)
block|{
comment|// convert PDF 1.* array to PDF 2.0 array
name|float
index|[]
name|verticesArray
init|=
name|annotation
operator|.
name|getVertices
argument_list|()
decl_stmt|;
if|if
condition|(
name|verticesArray
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|points
init|=
name|verticesArray
operator|.
name|length
operator|/
literal|2
decl_stmt|;
name|pathArray
operator|=
operator|new
name|float
index|[
name|points
index|]
index|[
literal|2
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|points
condition|;
operator|++
name|i
control|)
block|{
name|pathArray
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|verticesArray
index|[
name|i
operator|*
literal|2
index|]
expr_stmt|;
name|pathArray
index|[
name|i
index|]
index|[
literal|1
index|]
operator|=
name|verticesArray
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
expr_stmt|;
block|}
block|}
return|return
name|pathArray
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateRolloverAppearance
parameter_list|()
block|{
comment|// No rollover appearance generated for a polygon annotation
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateDownAppearance
parameter_list|()
block|{
comment|// No down appearance generated for a polygon annotation
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
name|PDAnnotationPolygon
name|annotation
init|=
operator|(
name|PDAnnotationPolygon
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

