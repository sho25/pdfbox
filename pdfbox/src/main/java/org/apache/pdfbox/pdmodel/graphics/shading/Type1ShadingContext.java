begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|shading
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|PaintContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
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
name|NoninvertibleTransformException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|Raster
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|WritableRaster
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * AWT PaintContext for function-based (Type 1) shading.  * @author Andreas Lehmkühler  * @author Tilman Hausherr  */
end_comment

begin_class
class|class
name|Type1ShadingContext
extends|extends
name|ShadingContext
implements|implements
name|PaintContext
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
name|Type1ShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDShadingType1
name|shading
decl_stmt|;
specifier|private
name|AffineTransform
name|rat
decl_stmt|;
specifier|private
name|float
index|[]
name|domain
decl_stmt|;
specifier|private
name|Matrix
name|matrix
decl_stmt|;
specifier|private
name|float
index|[]
name|background
decl_stmt|;
specifier|private
name|float
index|[]
name|bboxTab
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @param dBounds device bounds       */
specifier|public
name|Type1ShadingContext
parameter_list|(
name|PDShadingType1
name|shading
parameter_list|,
name|ColorModel
name|colorModel
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeight
parameter_list|,
name|Rectangle
name|dBounds
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|shading
argument_list|,
name|colorModel
argument_list|,
name|xform
argument_list|,
name|ctm
argument_list|,
name|pageHeight
argument_list|,
name|dBounds
argument_list|)
expr_stmt|;
name|this
operator|.
name|shading
operator|=
name|shading
expr_stmt|;
comment|// spec p.308
comment|// (Optional) An array of four numbers [ xmin xmax ymin ymax ]
comment|// specifying the rectangular domain of coordinates over which the
comment|// color function(s) are defined. Default value: [ 0.0 1.0 0.0 1.0 ].
if|if
condition|(
name|this
operator|.
name|shading
operator|.
name|getDomain
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|domain
operator|=
name|this
operator|.
name|shading
operator|.
name|getDomain
argument_list|()
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|domain
operator|=
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|0
block|,
literal|1
block|}
expr_stmt|;
block|}
name|matrix
operator|=
name|this
operator|.
name|shading
operator|.
name|getMatrix
argument_list|()
expr_stmt|;
if|if
condition|(
name|matrix
operator|==
literal|null
condition|)
block|{
name|matrix
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// get inverse transform to be independent of
comment|// shading matrix and current user / device space
comment|// when handling actual pixels in getRaster()
name|rat
operator|=
name|matrix
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|createInverse
argument_list|()
expr_stmt|;
name|rat
operator|.
name|concatenate
argument_list|(
name|ctm
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|createInverse
argument_list|()
argument_list|)
expr_stmt|;
name|rat
operator|.
name|concatenate
argument_list|(
name|xform
operator|.
name|createInverse
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoninvertibleTransformException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
comment|// get background values if available
name|COSArray
name|bg
init|=
name|shading
operator|.
name|getBackground
argument_list|()
decl_stmt|;
if|if
condition|(
name|bg
operator|!=
literal|null
condition|)
block|{
name|background
operator|=
name|bg
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|outputColorModel
operator|=
literal|null
expr_stmt|;
name|shadingColorSpace
operator|=
literal|null
expr_stmt|;
name|shading
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|outputColorModel
return|;
block|}
annotation|@
name|Override
specifier|public
name|Raster
name|getRaster
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|w
parameter_list|,
name|int
name|h
parameter_list|)
block|{
name|WritableRaster
name|raster
init|=
name|getColorModel
argument_list|()
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
decl_stmt|;
name|int
index|[]
name|data
init|=
operator|new
name|int
index|[
name|w
operator|*
name|h
operator|*
literal|4
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|h
condition|;
name|j
operator|++
control|)
block|{
name|int
name|currentY
init|=
name|y
operator|+
name|j
decl_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|currentY
argument_list|<
name|bboxTab
index|[
literal|1
index|]
operator|||
name|currentY
argument_list|>
name|bboxTab
index|[
literal|3
index|]
condition|)
block|{
continue|continue;
block|}
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
name|w
condition|;
name|i
operator|++
control|)
block|{
name|int
name|currentX
init|=
name|x
operator|+
name|i
decl_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|currentX
argument_list|<
name|bboxTab
index|[
literal|0
index|]
operator|||
name|currentX
argument_list|>
name|bboxTab
index|[
literal|2
index|]
condition|)
block|{
continue|continue;
block|}
block|}
name|int
name|index
init|=
operator|(
name|j
operator|*
name|w
operator|+
name|i
operator|)
operator|*
literal|4
decl_stmt|;
name|boolean
name|useBackground
init|=
literal|false
decl_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[]
block|{
name|x
operator|+
name|i
block|,
name|y
operator|+
name|j
block|}
decl_stmt|;
name|rat
operator|.
name|transform
argument_list|(
name|values
argument_list|,
literal|0
argument_list|,
name|values
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|values
index|[
literal|0
index|]
operator|<
name|domain
index|[
literal|0
index|]
operator|||
name|values
index|[
literal|0
index|]
operator|>
name|domain
index|[
literal|1
index|]
operator|||
name|values
index|[
literal|1
index|]
operator|<
name|domain
index|[
literal|2
index|]
operator|||
name|values
index|[
literal|1
index|]
operator|>
name|domain
index|[
literal|3
index|]
condition|)
block|{
if|if
condition|(
name|background
operator|!=
literal|null
condition|)
block|{
name|useBackground
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
continue|continue;
block|}
block|}
comment|// evaluate function
if|if
condition|(
name|useBackground
condition|)
block|{
name|values
operator|=
name|background
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|values
operator|=
name|shading
operator|.
name|evalFunction
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while processing a function"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|// convert color values from shading color space to RGB
if|if
condition|(
name|shadingColorSpace
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|values
operator|=
name|shadingColorSpace
operator|.
name|toRGB
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error processing color space"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
name|data
index|[
name|index
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|0
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|1
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|2
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|3
index|]
operator|=
literal|255
expr_stmt|;
block|}
block|}
name|raster
operator|.
name|setPixels
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|w
argument_list|,
name|h
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return
name|raster
return|;
block|}
specifier|public
name|float
index|[]
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
block|}
end_class

end_unit

