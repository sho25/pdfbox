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
name|graphics
operator|.
name|state
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BasicStroke
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Composite
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
name|Area
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
name|PDLineDashPattern
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
name|BlendComposite
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
name|PDDeviceGray
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
comment|/**  * The current state of the graphics parameters when executing a content stream.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDGraphicsState
implements|implements
name|Cloneable
block|{
specifier|private
name|boolean
name|isClippingPathDirty
decl_stmt|;
specifier|private
name|Area
name|clippingPath
decl_stmt|;
specifier|private
name|Matrix
name|currentTransformationMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
specifier|private
name|PDColor
name|strokingColor
init|=
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getInitialColor
argument_list|()
decl_stmt|;
specifier|private
name|PDColor
name|nonStrokingColor
init|=
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getInitialColor
argument_list|()
decl_stmt|;
specifier|private
name|PDColorSpace
name|strokingColorSpace
init|=
name|PDDeviceGray
operator|.
name|INSTANCE
decl_stmt|;
specifier|private
name|PDColorSpace
name|nonStrokingColorSpace
init|=
name|PDDeviceGray
operator|.
name|INSTANCE
decl_stmt|;
specifier|private
name|PDTextState
name|textState
init|=
operator|new
name|PDTextState
argument_list|()
decl_stmt|;
specifier|private
name|float
name|lineWidth
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|lineCap
init|=
name|BasicStroke
operator|.
name|CAP_BUTT
decl_stmt|;
specifier|private
name|int
name|lineJoin
init|=
name|BasicStroke
operator|.
name|JOIN_MITER
decl_stmt|;
specifier|private
name|float
name|miterLimit
init|=
literal|10
decl_stmt|;
specifier|private
name|PDLineDashPattern
name|lineDashPattern
init|=
operator|new
name|PDLineDashPattern
argument_list|()
decl_stmt|;
specifier|private
name|RenderingIntent
name|renderingIntent
decl_stmt|;
specifier|private
name|boolean
name|strokeAdjustment
init|=
literal|false
decl_stmt|;
specifier|private
name|BlendMode
name|blendMode
init|=
name|BlendMode
operator|.
name|COMPATIBLE
decl_stmt|;
specifier|private
name|PDSoftMask
name|softMask
decl_stmt|;
specifier|private
name|double
name|alphaConstant
init|=
literal|1.0
decl_stmt|;
specifier|private
name|double
name|nonStrokingAlphaConstant
init|=
literal|1.0
decl_stmt|;
specifier|private
name|boolean
name|alphaSource
init|=
literal|false
decl_stmt|;
comment|// DEVICE-DEPENDENT parameters
specifier|private
name|boolean
name|overprint
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|nonStrokingOverprint
init|=
literal|false
decl_stmt|;
specifier|private
name|double
name|overprintMode
init|=
literal|0
decl_stmt|;
comment|//black generation
comment|//undercolor removal
specifier|private
name|COSBase
name|transfer
init|=
literal|null
decl_stmt|;
comment|//halftone
specifier|private
name|double
name|flatness
init|=
literal|1.0
decl_stmt|;
specifier|private
name|double
name|smoothness
init|=
literal|0
decl_stmt|;
comment|/**      * Constructor with a given page size to initialize the clipping path.      * @param page the size of the page      */
specifier|public
name|PDGraphicsState
parameter_list|(
name|PDRectangle
name|page
parameter_list|)
block|{
name|clippingPath
operator|=
operator|new
name|Area
argument_list|(
name|page
operator|.
name|toGeneralPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the value of the CTM.      *      * @return The current transformation matrix.      */
specifier|public
name|Matrix
name|getCurrentTransformationMatrix
parameter_list|()
block|{
return|return
name|currentTransformationMatrix
return|;
block|}
comment|/**      * Set the value of the CTM.      *      * @param value The current transformation matrix.      */
specifier|public
name|void
name|setCurrentTransformationMatrix
parameter_list|(
name|Matrix
name|value
parameter_list|)
block|{
name|currentTransformationMatrix
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the line width.      *      * @return The current line width.      */
specifier|public
name|float
name|getLineWidth
parameter_list|()
block|{
return|return
name|lineWidth
return|;
block|}
comment|/**      * set the value of the line width.      *      * @param value The current line width.      */
specifier|public
name|void
name|setLineWidth
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|lineWidth
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the line cap.      *      * @return The current line cap.      */
specifier|public
name|int
name|getLineCap
parameter_list|()
block|{
return|return
name|lineCap
return|;
block|}
comment|/**      * set the value of the line cap.      *      * @param value The current line cap.      */
specifier|public
name|void
name|setLineCap
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|lineCap
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the line join.      *      * @return The current line join value.      */
specifier|public
name|int
name|getLineJoin
parameter_list|()
block|{
return|return
name|lineJoin
return|;
block|}
comment|/**      * Get the value of the line join.      *      * @param value The current line join      */
specifier|public
name|void
name|setLineJoin
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|lineJoin
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the miter limit.      *      * @return The current miter limit.      */
specifier|public
name|float
name|getMiterLimit
parameter_list|()
block|{
return|return
name|miterLimit
return|;
block|}
comment|/**      * set the value of the miter limit.      *      * @param value The current miter limit.      */
specifier|public
name|void
name|setMiterLimit
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|miterLimit
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the stroke adjustment parameter.      *      * @return The current stroke adjustment.      */
specifier|public
name|boolean
name|isStrokeAdjustment
parameter_list|()
block|{
return|return
name|strokeAdjustment
return|;
block|}
comment|/**      * set the value of the stroke adjustment.      *      * @param value The value of the stroke adjustment parameter.      */
specifier|public
name|void
name|setStrokeAdjustment
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|strokeAdjustment
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the stroke alpha constant property.      *      * @return The value of the stroke alpha constant parameter.      */
specifier|public
name|double
name|getAlphaConstant
parameter_list|()
block|{
return|return
name|alphaConstant
return|;
block|}
comment|/**      * set the value of the stroke alpha constant property.      *      * @param value The value of the stroke alpha constant parameter.      */
specifier|public
name|void
name|setAlphaConstant
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|alphaConstant
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the value of the non-stroke alpha constant property.      *      * @return The value of the non-stroke alpha constant parameter.      */
specifier|public
name|double
name|getNonStrokeAlphaConstant
parameter_list|()
block|{
return|return
name|nonStrokingAlphaConstant
return|;
block|}
comment|/**      * set the value of the non-stroke alpha constant property.      *      * @param value The value of the non-stroke alpha constant parameter.      */
specifier|public
name|void
name|setNonStrokeAlphaConstant
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|nonStrokingAlphaConstant
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the stroke alpha source property.      *      * @return The value of the stroke alpha source parameter.      */
specifier|public
name|boolean
name|isAlphaSource
parameter_list|()
block|{
return|return
name|alphaSource
return|;
block|}
comment|/**      * set the value of the alpha source property.      *      * @param value The value of the alpha source parameter.      */
specifier|public
name|void
name|setAlphaSource
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|alphaSource
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * returns the current softmask      *      * @return softMask      */
specifier|public
name|PDSoftMask
name|getSoftMask
parameter_list|()
block|{
return|return
name|softMask
return|;
block|}
comment|/**      * Sets the current soft mask      *      * @param softMask      */
specifier|public
name|void
name|setSoftMask
parameter_list|(
name|PDSoftMask
name|softMask
parameter_list|)
block|{
name|this
operator|.
name|softMask
operator|=
name|softMask
expr_stmt|;
block|}
comment|/**      * Returns the current blend mode      *      * @return the current blend mode      */
specifier|public
name|BlendMode
name|getBlendMode
parameter_list|()
block|{
return|return
name|blendMode
return|;
block|}
comment|/**      * Sets the blend mode in the current graphics state      *      * @param blendMode      */
specifier|public
name|void
name|setBlendMode
parameter_list|(
name|BlendMode
name|blendMode
parameter_list|)
block|{
name|this
operator|.
name|blendMode
operator|=
name|blendMode
expr_stmt|;
block|}
comment|/**      * get the value of the overprint property.      *      * @return The value of the overprint parameter.      */
specifier|public
name|boolean
name|isOverprint
parameter_list|()
block|{
return|return
name|overprint
return|;
block|}
comment|/**      * set the value of the overprint property.      *      * @param value The value of the overprint parameter.      */
specifier|public
name|void
name|setOverprint
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|overprint
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the non stroking overprint property.      *      * @return The value of the non stroking overprint parameter.      */
specifier|public
name|boolean
name|isNonStrokingOverprint
parameter_list|()
block|{
return|return
name|nonStrokingOverprint
return|;
block|}
comment|/**      * set the value of the non stroking overprint property.      *      * @param value The value of the non stroking overprint parameter.      */
specifier|public
name|void
name|setNonStrokingOverprint
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|nonStrokingOverprint
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the overprint mode property.      *      * @return The value of the overprint mode parameter.      */
specifier|public
name|double
name|getOverprintMode
parameter_list|()
block|{
return|return
name|overprintMode
return|;
block|}
comment|/**      * set the value of the overprint mode property.      *      * @param value The value of the overprint mode parameter.      */
specifier|public
name|void
name|setOverprintMode
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|overprintMode
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the flatness property.      *      * @return The value of the flatness parameter.      */
specifier|public
name|double
name|getFlatness
parameter_list|()
block|{
return|return
name|flatness
return|;
block|}
comment|/**      * set the value of the flatness property.      *      * @param value The value of the flatness parameter.      */
specifier|public
name|void
name|setFlatness
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|flatness
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the smoothness property.      *      * @return The value of the smoothness parameter.      */
specifier|public
name|double
name|getSmoothness
parameter_list|()
block|{
return|return
name|smoothness
return|;
block|}
comment|/**      * set the value of the smoothness property.      *      * @param value The value of the smoothness parameter.      */
specifier|public
name|void
name|setSmoothness
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|smoothness
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will get the graphics text state.      *      * @return The graphics text state.      */
specifier|public
name|PDTextState
name|getTextState
parameter_list|()
block|{
return|return
name|textState
return|;
block|}
comment|/**      * This will set the graphics text state.      *      * @param value The graphics text state.      */
specifier|public
name|void
name|setTextState
parameter_list|(
name|PDTextState
name|value
parameter_list|)
block|{
name|textState
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will get the current line dash pattern.      *      * @return The line dash pattern.      */
specifier|public
name|PDLineDashPattern
name|getLineDashPattern
parameter_list|()
block|{
return|return
name|lineDashPattern
return|;
block|}
comment|/**      * This will set the current line dash pattern.      *      * @param value The new line dash pattern.      */
specifier|public
name|void
name|setLineDashPattern
parameter_list|(
name|PDLineDashPattern
name|value
parameter_list|)
block|{
name|lineDashPattern
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will get the rendering intent.      *      * @see PDExtendedGraphicsState      *      * @return The rendering intent      */
specifier|public
name|RenderingIntent
name|getRenderingIntent
parameter_list|()
block|{
return|return
name|renderingIntent
return|;
block|}
comment|/**      * This will set the rendering intent.      *      * @param value The new rendering intent.      */
specifier|public
name|void
name|setRenderingIntent
parameter_list|(
name|RenderingIntent
name|value
parameter_list|)
block|{
name|renderingIntent
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDGraphicsState
name|clone
parameter_list|()
block|{
try|try
block|{
name|PDGraphicsState
name|clone
init|=
operator|(
name|PDGraphicsState
operator|)
name|super
operator|.
name|clone
argument_list|()
decl_stmt|;
name|clone
operator|.
name|textState
operator|=
name|textState
operator|.
name|clone
argument_list|()
expr_stmt|;
name|clone
operator|.
name|currentTransformationMatrix
operator|=
name|currentTransformationMatrix
operator|.
name|clone
argument_list|()
expr_stmt|;
name|clone
operator|.
name|strokingColor
operator|=
name|strokingColor
expr_stmt|;
comment|// immutable
name|clone
operator|.
name|nonStrokingColor
operator|=
name|nonStrokingColor
expr_stmt|;
comment|// immutable
name|clone
operator|.
name|lineDashPattern
operator|=
name|lineDashPattern
expr_stmt|;
comment|// immutable
name|clone
operator|.
name|clippingPath
operator|=
name|clippingPath
expr_stmt|;
comment|// not cloned, see intersectClippingPath
name|clone
operator|.
name|isClippingPathDirty
operator|=
literal|false
expr_stmt|;
return|return
name|clone
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
comment|// should not happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns the stroking color.      *      * @return stroking color      */
specifier|public
name|PDColor
name|getStrokingColor
parameter_list|()
block|{
return|return
name|strokingColor
return|;
block|}
comment|/**      * Sets the stroking color.      *      * @param color The new stroking color      */
specifier|public
name|void
name|setStrokingColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
block|{
name|strokingColor
operator|=
name|color
expr_stmt|;
block|}
comment|/**      * Returns the non-stroking color.      *      * @return The non-stroking color      */
specifier|public
name|PDColor
name|getNonStrokingColor
parameter_list|()
block|{
return|return
name|nonStrokingColor
return|;
block|}
comment|/**      * Sets the non-stroking color.      *      * @param color The new non-stroking color      */
specifier|public
name|void
name|setNonStrokingColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
block|{
name|nonStrokingColor
operator|=
name|color
expr_stmt|;
block|}
comment|/**      * Returns the stroking color space.      *      * @return The stroking color space.      */
specifier|public
name|PDColorSpace
name|getStrokingColorSpace
parameter_list|()
block|{
return|return
name|strokingColorSpace
return|;
block|}
comment|/**      * Sets the stroking color space.      *      * @param colorSpace The new stroking color space.      */
specifier|public
name|void
name|setStrokingColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|strokingColorSpace
operator|=
name|colorSpace
expr_stmt|;
block|}
comment|/**      * Returns the non-stroking color space.      *      * @return The non-stroking color space.      */
specifier|public
name|PDColorSpace
name|getNonStrokingColorSpace
parameter_list|()
block|{
return|return
name|nonStrokingColorSpace
return|;
block|}
comment|/**      * Sets the non-stroking color space.      *      * @param colorSpace The new non-stroking color space.      */
specifier|public
name|void
name|setNonStrokingColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|nonStrokingColorSpace
operator|=
name|colorSpace
expr_stmt|;
block|}
comment|/**      * Modify the current clipping path by intersecting it with the given path.      * @param path path to intersect with the clipping path      */
specifier|public
name|void
name|intersectClippingPath
parameter_list|(
name|GeneralPath
name|path
parameter_list|)
block|{
name|intersectClippingPath
argument_list|(
operator|new
name|Area
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Modify the current clipping path by intersecting it with the given path.      * @param area area to intersect with the clipping path      */
specifier|public
name|void
name|intersectClippingPath
parameter_list|(
name|Area
name|area
parameter_list|)
block|{
comment|// lazy cloning of clipping path for performance
if|if
condition|(
operator|!
name|isClippingPathDirty
condition|)
block|{
comment|// deep copy (can't use clone() as it performs only a shallow copy)
name|Area
name|cloned
init|=
operator|new
name|Area
argument_list|()
decl_stmt|;
name|cloned
operator|.
name|add
argument_list|(
name|clippingPath
argument_list|)
expr_stmt|;
name|clippingPath
operator|=
name|cloned
expr_stmt|;
name|isClippingPathDirty
operator|=
literal|true
expr_stmt|;
block|}
comment|// intersection as usual
name|clippingPath
operator|.
name|intersect
argument_list|(
name|area
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the current clipping path. Do not modify this Area object!      *      * @return The current clipping path.      */
specifier|public
name|Area
name|getCurrentClippingPath
parameter_list|()
block|{
return|return
name|clippingPath
return|;
block|}
specifier|public
name|Composite
name|getStrokingJavaComposite
parameter_list|()
block|{
return|return
name|BlendComposite
operator|.
name|getInstance
argument_list|(
name|blendMode
argument_list|,
operator|(
name|float
operator|)
name|alphaConstant
argument_list|)
return|;
block|}
specifier|public
name|Composite
name|getNonStrokingJavaComposite
parameter_list|()
block|{
return|return
name|BlendComposite
operator|.
name|getInstance
argument_list|(
name|blendMode
argument_list|,
operator|(
name|float
operator|)
name|nonStrokingAlphaConstant
argument_list|)
return|;
block|}
comment|/**      * This will get the transfer function.      *      * @return The transfer function. According to the PDF specification, this is either a single      * function (which applies to all process colorants) or an array of four functions (which apply      * to the process colorants individually). The name Identity may be used to represent the      * identity function, and the name Default denotes the transfer function that was in effect at      * the start of the page.      */
specifier|public
name|COSBase
name|getTransfer
parameter_list|()
block|{
return|return
name|transfer
return|;
block|}
comment|/**      * This will set the transfer function.      *      * @param transfer The transfer function. According to the PDF specification, this is either a      * single function (which applies to all process colorants) or an array of four functions (which      * apply to the process colorants individually). The name Identity may be used to represent the      * identity function, and the name Default denotes the transfer function that was in effect at      * the start of the page.      */
specifier|public
name|void
name|setTransfer
parameter_list|(
name|COSBase
name|transfer
parameter_list|)
block|{
name|this
operator|.
name|transfer
operator|=
name|transfer
expr_stmt|;
block|}
block|}
end_class

end_unit

