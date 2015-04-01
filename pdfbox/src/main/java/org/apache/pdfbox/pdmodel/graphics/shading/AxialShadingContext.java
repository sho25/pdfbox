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
name|geom
operator|.
name|Point2D
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
name|cos
operator|.
name|COSBoolean
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
name|function
operator|.
name|PDFunction
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
comment|/**  * AWT PaintContext for axial shading.  *  * Performance improvement done as part of GSoC2014, Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
specifier|public
class|class
name|AxialShadingContext
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
name|AxialShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDShadingType2
name|axialShadingType
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|coords
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|domain
decl_stmt|;
specifier|private
specifier|final
name|boolean
index|[]
name|extend
decl_stmt|;
specifier|private
specifier|final
name|double
name|x1x0
decl_stmt|;
specifier|private
specifier|final
name|double
name|y1y0
decl_stmt|;
specifier|private
specifier|final
name|float
name|d1d0
decl_stmt|;
specifier|private
name|double
name|denom
decl_stmt|;
specifier|private
specifier|final
name|int
name|factor
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|colorTable
decl_stmt|;
specifier|private
name|AffineTransform
name|rat
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @param deviceBounds device bounds      * @throws java.io.IOException if there is an error getting the color space or doing color conversion.      */
specifier|public
name|AxialShadingContext
parameter_list|(
name|PDShadingType2
name|shading
parameter_list|,
name|ColorModel
name|colorModel
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|Rectangle
name|deviceBounds
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
name|matrix
argument_list|,
name|deviceBounds
argument_list|)
expr_stmt|;
name|this
operator|.
name|axialShadingType
operator|=
name|shading
expr_stmt|;
name|coords
operator|=
name|shading
operator|.
name|getCoords
argument_list|()
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
comment|// domain values
if|if
condition|(
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
comment|// set default values
name|domain
operator|=
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|}
expr_stmt|;
block|}
comment|// extend values
name|COSArray
name|extendValues
init|=
name|shading
operator|.
name|getExtend
argument_list|()
decl_stmt|;
if|if
condition|(
name|shading
operator|.
name|getExtend
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|extend
operator|=
operator|new
name|boolean
index|[
literal|2
index|]
expr_stmt|;
name|extend
index|[
literal|0
index|]
operator|=
operator|(
operator|(
name|COSBoolean
operator|)
name|extendValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|extend
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|COSBoolean
operator|)
name|extendValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// set default values
name|extend
operator|=
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|}
expr_stmt|;
block|}
comment|// calculate some constants to be used in getRaster
name|x1x0
operator|=
name|coords
index|[
literal|2
index|]
operator|-
name|coords
index|[
literal|0
index|]
expr_stmt|;
name|y1y0
operator|=
name|coords
index|[
literal|3
index|]
operator|-
name|coords
index|[
literal|1
index|]
expr_stmt|;
name|d1d0
operator|=
name|domain
index|[
literal|1
index|]
operator|-
name|domain
index|[
literal|0
index|]
expr_stmt|;
name|denom
operator|=
name|Math
operator|.
name|pow
argument_list|(
name|x1x0
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
name|y1y0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|double
name|longestDistance
init|=
name|Math
operator|.
name|sqrt
argument_list|(
name|denom
argument_list|)
decl_stmt|;
try|try
block|{
comment|// get inverse transform to be independent of current user / device space
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
comment|// transform the distance to actual pixel space
comment|// use transform, because xform.getScaleX() does not return correct scaling on 90° rotated matrix
name|Point2D
name|point
init|=
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|longestDistance
argument_list|,
name|longestDistance
argument_list|)
decl_stmt|;
name|matrix
operator|.
name|transform
argument_list|(
name|point
argument_list|)
expr_stmt|;
name|xform
operator|.
name|transform
argument_list|(
name|point
argument_list|,
name|point
argument_list|)
expr_stmt|;
name|factor
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|abs
argument_list|(
name|point
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|point
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|colorTable
operator|=
name|calcColorTable
argument_list|()
expr_stmt|;
block|}
comment|/**      * Calculate the color on the axial line and store them in an array.      *      * @return an array, index denotes the relative position, the corresponding      * value is the color on the axial line      * @throws IOException if the color conversion fails.      */
specifier|private
name|int
index|[]
name|calcColorTable
parameter_list|()
throws|throws
name|IOException
block|{
name|int
index|[]
name|map
init|=
operator|new
name|int
index|[
name|factor
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|factor
operator|==
literal|0
operator|||
name|d1d0
operator|==
literal|0
condition|)
block|{
name|float
index|[]
name|values
init|=
name|axialShadingType
operator|.
name|evalFunction
argument_list|(
name|domain
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|map
index|[
literal|0
index|]
operator|=
name|convertToRGB
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|factor
condition|;
name|i
operator|++
control|)
block|{
name|float
name|t
init|=
name|domain
index|[
literal|0
index|]
operator|+
name|d1d0
operator|*
name|i
operator|/
name|factor
decl_stmt|;
name|float
index|[]
name|values
init|=
name|axialShadingType
operator|.
name|evalFunction
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|map
index|[
name|i
index|]
operator|=
name|convertToRGB
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
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
name|axialShadingType
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
comment|// create writable raster
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
name|boolean
name|useBackground
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
name|double
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
operator|&&
operator|(
name|currentY
argument_list|<
name|minBBoxY
operator|||
name|currentY
argument_list|>
name|maxBBoxY
operator|)
condition|)
block|{
continue|continue;
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
name|double
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
operator|&&
operator|(
name|currentX
argument_list|<
name|minBBoxX
operator|||
name|currentX
argument_list|>
name|maxBBoxX
operator|)
condition|)
block|{
continue|continue;
block|}
name|useBackground
operator|=
literal|false
expr_stmt|;
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
name|currentX
operator|=
name|values
index|[
literal|0
index|]
expr_stmt|;
name|currentY
operator|=
name|values
index|[
literal|1
index|]
expr_stmt|;
name|double
name|inputValue
init|=
name|x1x0
operator|*
operator|(
name|currentX
operator|-
name|coords
index|[
literal|0
index|]
operator|)
decl_stmt|;
name|inputValue
operator|+=
name|y1y0
operator|*
operator|(
name|currentY
operator|-
name|coords
index|[
literal|1
index|]
operator|)
expr_stmt|;
comment|// TODO this happens if start == end, see PDFBOX-1442
if|if
condition|(
name|denom
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|background
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|useBackground
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|inputValue
operator|/=
name|denom
expr_stmt|;
block|}
comment|// input value is out of range
if|if
condition|(
name|inputValue
operator|<
literal|0
condition|)
block|{
comment|// the shading has to be extended if extend[0] == true
if|if
condition|(
name|extend
index|[
literal|0
index|]
condition|)
block|{
name|inputValue
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|background
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|useBackground
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// input value is out of range
elseif|else
if|if
condition|(
name|inputValue
operator|>
literal|1
condition|)
block|{
comment|// the shading has to be extended if extend[1] == true
if|if
condition|(
name|extend
index|[
literal|1
index|]
condition|)
block|{
name|inputValue
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|background
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|useBackground
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|int
name|value
decl_stmt|;
if|if
condition|(
name|useBackground
condition|)
block|{
comment|// use the given backgound color values
name|value
operator|=
name|rgbBackground
expr_stmt|;
block|}
else|else
block|{
name|int
name|key
init|=
call|(
name|int
call|)
argument_list|(
name|inputValue
operator|*
name|factor
argument_list|)
decl_stmt|;
name|value
operator|=
name|colorTable
index|[
name|key
index|]
expr_stmt|;
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
name|data
index|[
name|index
index|]
operator|=
name|value
operator|&
literal|255
expr_stmt|;
name|value
operator|>>=
literal|8
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|=
name|value
operator|&
literal|255
expr_stmt|;
name|value
operator|>>=
literal|8
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|=
name|value
operator|&
literal|255
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
comment|/**      * Returns the coords values.      */
specifier|public
name|float
index|[]
name|getCoords
parameter_list|()
block|{
return|return
name|coords
return|;
block|}
comment|/**      * Returns the domain values.      */
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
comment|/**      * Returns the extend values.      */
specifier|public
name|boolean
index|[]
name|getExtend
parameter_list|()
block|{
return|return
name|extend
return|;
block|}
comment|/**      * Returns the function.      *      * @throws java.io.IOException if we were not able to create the function.      */
specifier|public
name|PDFunction
name|getFunction
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|axialShadingType
operator|.
name|getFunction
argument_list|()
return|;
block|}
block|}
end_class

end_unit

