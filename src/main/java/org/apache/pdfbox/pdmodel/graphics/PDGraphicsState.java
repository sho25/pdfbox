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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Shape
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
name|util
operator|.
name|Matrix
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
name|text
operator|.
name|PDTextState
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
name|PDColorSpaceInstance
import|;
end_import

begin_comment
comment|/**  * This class will hold the current state of the graphics parameters when executing a  * content stream.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDGraphicsState
implements|implements
name|Cloneable
block|{
specifier|private
name|Matrix
name|currentTransformationMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
comment|//Here are some attributes of the Graphics state, but have not been created yet.
comment|//
comment|//clippingPath
specifier|private
name|PDColorSpaceInstance
name|strokingColorSpace
init|=
operator|new
name|PDColorSpaceInstance
argument_list|()
decl_stmt|;
specifier|private
name|PDColorSpaceInstance
name|nonStrokingColorSpace
init|=
operator|new
name|PDColorSpaceInstance
argument_list|()
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
name|double
name|lineWidth
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|lineCap
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|lineJoin
init|=
literal|0
decl_stmt|;
specifier|private
name|double
name|miterLimit
init|=
literal|0
decl_stmt|;
specifier|private
name|PDLineDashPattern
name|lineDashPattern
decl_stmt|;
specifier|private
name|String
name|renderingIntent
decl_stmt|;
specifier|private
name|boolean
name|strokeAdjustment
init|=
literal|false
decl_stmt|;
comment|//blend mode
comment|//soft mask
specifier|private
name|double
name|alphaConstants
init|=
literal|0
decl_stmt|;
specifier|private
name|boolean
name|alphaSource
init|=
literal|false
decl_stmt|;
comment|//DEVICE DEPENDENT parameters
specifier|private
name|boolean
name|overprint
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
comment|//transfer
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
specifier|private
name|GeneralPath
name|currentClippingPath
decl_stmt|;
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
name|double
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
name|double
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
name|double
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
name|double
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
comment|/**      * Get the value of the alpha constants property.      *      * @return The value of the alpha constants parameter.      */
specifier|public
name|double
name|getAlphaConstants
parameter_list|()
block|{
return|return
name|alphaConstants
return|;
block|}
comment|/**      * set the value of the alpha constants property.      *      * @param value The value of the alpha constants parameter.      */
specifier|public
name|void
name|setAlphaConstants
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|alphaConstants
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * get the value of the alpha source property.      *      * @return The value of the alpha source parameter.      */
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
name|String
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
name|String
name|value
parameter_list|)
block|{
name|renderingIntent
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Object
name|clone
parameter_list|()
block|{
name|PDGraphicsState
name|clone
init|=
literal|null
decl_stmt|;
try|try
block|{
name|clone
operator|=
operator|(
name|PDGraphicsState
operator|)
name|super
operator|.
name|clone
argument_list|()
expr_stmt|;
name|clone
operator|.
name|setTextState
argument_list|(
operator|(
name|PDTextState
operator|)
name|textState
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setCurrentTransformationMatrix
argument_list|(
name|currentTransformationMatrix
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setStrokingColorSpace
argument_list|(
operator|(
name|PDColorSpaceInstance
operator|)
name|strokingColorSpace
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setNonStrokingColorSpace
argument_list|(
operator|(
name|PDColorSpaceInstance
operator|)
name|nonStrokingColorSpace
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|lineDashPattern
operator|!=
literal|null
condition|)
block|{
name|clone
operator|.
name|setLineDashPattern
argument_list|(
operator|(
name|PDLineDashPattern
operator|)
name|lineDashPattern
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentClippingPath
operator|!=
literal|null
condition|)
block|{
name|clone
operator|.
name|setCurrentClippingPath
argument_list|(
operator|(
name|GeneralPath
operator|)
name|currentClippingPath
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|clone
return|;
block|}
comment|/**      * This will get the current stroking colorspace.      *      * @return The current stroking colorspace.      */
specifier|public
name|PDColorSpaceInstance
name|getStrokingColorSpace
parameter_list|()
block|{
return|return
name|strokingColorSpace
return|;
block|}
comment|/**      * This will set the current stroking colorspace.      *      * @param value The new stroking colorspace instance.      */
specifier|public
name|void
name|setStrokingColorSpace
parameter_list|(
name|PDColorSpaceInstance
name|value
parameter_list|)
block|{
name|strokingColorSpace
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will get the nonstroking color space instance.      *      * @return The colorspace instance.      */
specifier|public
name|PDColorSpaceInstance
name|getNonStrokingColorSpace
parameter_list|()
block|{
return|return
name|nonStrokingColorSpace
return|;
block|}
comment|/**      * This will set the non-stroking colorspace instance.      *      * @param value The non-stroking colorspace instance.      */
specifier|public
name|void
name|setNonStrokingColorSpace
parameter_list|(
name|PDColorSpaceInstance
name|value
parameter_list|)
block|{
name|nonStrokingColorSpace
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will set the current clipping path.      *      * @param pCurrentClippingPath The current clipping path.      *       */
specifier|public
name|void
name|setCurrentClippingPath
parameter_list|(
name|Shape
name|pCurrentClippingPath
parameter_list|)
block|{
if|if
condition|(
name|pCurrentClippingPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pCurrentClippingPath
operator|instanceof
name|GeneralPath
condition|)
block|{
name|currentClippingPath
operator|=
operator|(
name|GeneralPath
operator|)
name|pCurrentClippingPath
expr_stmt|;
block|}
else|else
block|{
name|currentClippingPath
operator|=
operator|new
name|GeneralPath
argument_list|()
expr_stmt|;
name|currentClippingPath
operator|.
name|append
argument_list|(
name|pCurrentClippingPath
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|currentClippingPath
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * This will get the current clipping path.      *      * @return The current clipping path.      */
specifier|public
name|Shape
name|getCurrentClippingPath
parameter_list|()
block|{
return|return
name|currentClippingPath
return|;
block|}
block|}
end_class

end_unit

