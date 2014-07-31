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
name|Transparency
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
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
name|ComponentColorModel
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
name|DataBuffer
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * AWT PaintContext for axial shading.  *   * Performance improvement done as part of GSoC2014, Tilman Hausherr is the  * mentor.  *  * @author Andreas Lehmkühler  * @author Shaola Ren   *  */
end_comment

begin_class
specifier|public
class|class
name|AxialShadingContext
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
name|ColorModel
name|outputColorModel
decl_stmt|;
specifier|private
name|PDColorSpace
name|shadingColorSpace
decl_stmt|;
specifier|private
name|PDShadingType2
name|shading
decl_stmt|;
specifier|private
name|float
index|[]
name|coords
decl_stmt|;
specifier|private
name|float
index|[]
name|domain
decl_stmt|;
specifier|private
name|float
index|[]
name|background
decl_stmt|;
specifier|private
name|int
name|rgbBackground
decl_stmt|;
specifier|private
name|boolean
index|[]
name|extend
decl_stmt|;
specifier|private
name|double
name|x1x0
decl_stmt|;
specifier|private
name|double
name|y1y0
decl_stmt|;
specifier|private
name|float
name|d1d0
decl_stmt|;
specifier|private
name|double
name|denom
decl_stmt|;
specifier|private
specifier|final
name|PDRectangle
name|bboxRect
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
specifier|private
specifier|final
name|double
name|axialLength
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|colorTable
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param ctm the transformation matrix      * @param pageHeight height of the current page      */
specifier|public
name|AxialShadingContext
parameter_list|(
name|PDShadingType2
name|shading
parameter_list|,
name|ColorModel
name|cm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeight
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|shading
operator|=
name|shading
expr_stmt|;
name|coords
operator|=
name|this
operator|.
name|shading
operator|.
name|getCoords
argument_list|()
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
name|bboxRect
operator|=
name|shading
operator|.
name|getBBox
argument_list|()
expr_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
name|bboxTab
index|[
literal|0
index|]
operator|=
name|bboxRect
operator|.
name|getLowerLeftX
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|1
index|]
operator|=
name|bboxRect
operator|.
name|getLowerLeftY
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|2
index|]
operator|=
name|bboxRect
operator|.
name|getUpperRightX
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|3
index|]
operator|=
name|bboxRect
operator|.
name|getUpperRightY
argument_list|()
expr_stmt|;
if|if
condition|(
name|ctm
operator|!=
literal|null
condition|)
block|{
comment|// transform the coords using the given matrix
name|ctm
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|transform
argument_list|(
name|bboxTab
argument_list|,
literal|0
argument_list|,
name|bboxTab
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
name|xform
operator|.
name|transform
argument_list|(
name|bboxTab
argument_list|,
literal|0
argument_list|,
name|bboxTab
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ctm
operator|!=
literal|null
condition|)
block|{
comment|// transform the coords using the given matrix
name|ctm
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|transform
argument_list|(
name|coords
argument_list|,
literal|0
argument_list|,
name|coords
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
name|xform
operator|.
name|transform
argument_list|(
name|coords
argument_list|,
literal|0
argument_list|,
name|coords
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// get the shading colorSpace
name|shadingColorSpace
operator|=
name|this
operator|.
name|shading
operator|.
name|getColorSpace
argument_list|()
expr_stmt|;
comment|// create the output colormodel using RGB+alpha as colorspace
name|ColorSpace
name|outputCS
init|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
decl_stmt|;
name|outputColorModel
operator|=
operator|new
name|ComponentColorModel
argument_list|(
name|outputCS
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|TRANSLUCENT
argument_list|,
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
expr_stmt|;
comment|// domain values
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
name|this
operator|.
name|shading
operator|.
name|getExtend
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
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
name|axialLength
operator|=
name|Math
operator|.
name|sqrt
argument_list|(
name|denom
argument_list|)
expr_stmt|;
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
name|rgbBackground
operator|=
name|convertToRGB
argument_list|(
name|background
argument_list|)
expr_stmt|;
block|}
name|colorTable
operator|=
name|calcColorTable
argument_list|()
expr_stmt|;
block|}
comment|/**      * Calculate the color on the axial line and store them in an array.      *      * @return an array, index denotes the relative position, the corresponding      * value is the color on the axial line      */
specifier|private
name|int
index|[]
name|calcColorTable
parameter_list|()
block|{
name|int
index|[]
name|map
init|=
operator|new
name|int
index|[
operator|(
name|int
operator|)
name|axialLength
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|axialLength
operator|==
literal|0
operator|||
name|d1d0
operator|==
literal|0
condition|)
block|{
try|try
block|{
name|float
index|[]
name|values
init|=
name|shading
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
name|axialLength
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
operator|(
name|float
operator|)
name|axialLength
decl_stmt|;
try|try
block|{
name|float
index|[]
name|values
init|=
name|shading
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
block|}
return|return
name|map
return|;
block|}
comment|// convert color to RGB color values
specifier|private
name|int
name|convertToRGB
parameter_list|(
name|float
index|[]
name|values
parameter_list|)
block|{
name|float
index|[]
name|rgbValues
decl_stmt|;
name|int
name|normRGBValues
init|=
literal|0
decl_stmt|;
try|try
block|{
name|rgbValues
operator|=
name|shadingColorSpace
operator|.
name|toRGB
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|normRGBValues
operator|=
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|0
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|normRGBValues
operator||=
operator|(
operator|(
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|1
index|]
operator|*
literal|255
argument_list|)
operator|)
operator|<<
literal|8
operator|)
expr_stmt|;
name|normRGBValues
operator||=
operator|(
operator|(
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|2
index|]
operator|*
literal|255
argument_list|)
operator|)
operator|<<
literal|16
operator|)
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
return|return
name|normRGBValues
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
condition|)
block|{
if|if
condition|(
name|currentY
argument_list|<
name|bboxTab
index|[
literal|3
index|]
operator|||
name|currentY
argument_list|>
name|bboxTab
index|[
literal|1
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
name|useBackground
operator|=
literal|false
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
name|axialLength
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
comment|/**      * Returns the coords values.      * @return the coords values as array      */
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
comment|/**      * Returns the domain values.      * @return the domain values as array      */
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
comment|/**      * Returns the extend values.      * @return the extend values as array      */
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
comment|/**      * Returns the function.      *      * @return the function      * @throws IOException if something goes wrong      */
specifier|public
name|PDFunction
name|getFunction
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|shading
operator|.
name|getFunction
argument_list|()
return|;
block|}
block|}
end_class

end_unit

