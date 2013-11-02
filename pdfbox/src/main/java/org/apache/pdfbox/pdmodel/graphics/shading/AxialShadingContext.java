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
name|PDDeviceN
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
name|PDDeviceRGB
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
name|PDSeparation
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
comment|/**  * This class represents the PaintContext of an axial shading.  *   * @author lehmi  *   */
end_comment

begin_class
specifier|public
class|class
name|AxialShadingContext
implements|implements
name|PaintContext
block|{
specifier|private
name|ColorModel
name|colorModel
decl_stmt|;
specifier|private
name|ColorSpace
name|shadingColorSpace
decl_stmt|;
specifier|private
name|PDFunction
name|shadingTinttransform
decl_stmt|;
specifier|private
name|PDShadingType2
name|shadingType
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
name|int
index|[]
name|extend0Values
decl_stmt|;
specifier|private
name|int
index|[]
name|extend1Values
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
comment|/**      * Log instance.      */
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
comment|/**      * Constructor creates an instance to be used for fill operations.      *       * @param shadingType2 the shading type to be used      * @param colorModelValue the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      *       */
specifier|public
name|AxialShadingContext
parameter_list|(
name|PDShadingType2
name|shadingType2
parameter_list|,
name|ColorModel
name|colorModelValue
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
block|{
name|shadingType
operator|=
name|shadingType2
expr_stmt|;
name|coords
operator|=
name|shadingType
operator|.
name|getCoords
argument_list|()
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
if|if
condition|(
name|ctm
operator|!=
literal|null
condition|)
block|{
comment|// the shading is used in combination with the sh-operator
name|float
index|[]
name|coordsTemp
init|=
operator|new
name|float
index|[
name|coords
operator|.
name|length
index|]
decl_stmt|;
comment|// transform the coords from shading to user space
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
name|coordsTemp
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// move the 0,0-reference
name|coordsTemp
index|[
literal|1
index|]
operator|=
name|pageHeight
operator|-
name|coordsTemp
index|[
literal|1
index|]
expr_stmt|;
name|coordsTemp
index|[
literal|3
index|]
operator|=
name|pageHeight
operator|-
name|coordsTemp
index|[
literal|3
index|]
expr_stmt|;
comment|// transform the coords from user to device space
name|xform
operator|.
name|transform
argument_list|(
name|coordsTemp
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
else|else
block|{
comment|// the shading is used as pattern colorspace in combination
comment|// with a fill-, stroke- or showText-operator
name|float
name|translateY
init|=
operator|(
name|float
operator|)
name|xform
operator|.
name|getTranslateY
argument_list|()
decl_stmt|;
comment|// move the 0,0-reference including the y-translation from user to device space
name|coords
index|[
literal|1
index|]
operator|=
name|pageHeight
operator|+
name|translateY
operator|-
name|coords
index|[
literal|1
index|]
expr_stmt|;
name|coords
index|[
literal|3
index|]
operator|=
name|pageHeight
operator|+
name|translateY
operator|-
name|coords
index|[
literal|3
index|]
expr_stmt|;
block|}
comment|// colorSpace
try|try
block|{
name|PDColorSpace
name|cs
init|=
name|shadingType
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|cs
operator|instanceof
name|PDDeviceRGB
operator|)
condition|)
block|{
comment|// we have to create an instance of the shading colorspace if it isn't RGB
name|shadingColorSpace
operator|=
name|cs
operator|.
name|getJavaColorSpace
argument_list|()
expr_stmt|;
if|if
condition|(
name|cs
operator|instanceof
name|PDDeviceN
condition|)
block|{
name|shadingTinttransform
operator|=
operator|(
operator|(
name|PDDeviceN
operator|)
name|cs
operator|)
operator|.
name|getTintTransform
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cs
operator|instanceof
name|PDSeparation
condition|)
block|{
name|shadingTinttransform
operator|=
operator|(
operator|(
name|PDSeparation
operator|)
name|cs
operator|)
operator|.
name|getTintTransform
argument_list|()
expr_stmt|;
block|}
block|}
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
literal|"error while creating colorSpace"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
comment|// colorModel
if|if
condition|(
name|colorModelValue
operator|!=
literal|null
condition|)
block|{
name|colorModel
operator|=
name|colorModelValue
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
comment|// TODO bpc != 8 ??
name|colorModel
operator|=
name|shadingType
operator|.
name|getColorSpace
argument_list|()
operator|.
name|createColorModel
argument_list|(
literal|8
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
literal|"error while creating colorModel"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|// domain values
if|if
condition|(
name|shadingType
operator|.
name|getDomain
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|domain
operator|=
name|shadingType
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
name|shadingType
operator|.
name|getExtend
argument_list|()
decl_stmt|;
if|if
condition|(
name|shadingType
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
comment|// TODO take a possible Background value into account
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|colorModel
operator|=
literal|null
expr_stmt|;
name|shadingColorSpace
operator|=
literal|null
expr_stmt|;
name|shadingTinttransform
operator|=
literal|null
expr_stmt|;
name|shadingType
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|colorModel
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
literal|3
index|]
decl_stmt|;
name|boolean
name|saveExtend0
init|=
literal|false
decl_stmt|;
name|boolean
name|saveExtend1
init|=
literal|false
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
literal|3
decl_stmt|;
name|double
name|inputValue
init|=
name|x1x0
operator|*
operator|(
name|x
operator|+
name|i
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
name|y
operator|+
name|j
operator|-
name|coords
index|[
literal|1
index|]
operator|)
expr_stmt|;
name|inputValue
operator|/=
name|denom
expr_stmt|;
comment|// input value is out of range
if|if
condition|(
name|inputValue
operator|<
name|domain
index|[
literal|0
index|]
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
if|if
condition|(
name|extend0Values
operator|==
literal|null
condition|)
block|{
name|inputValue
operator|=
name|domain
index|[
literal|0
index|]
expr_stmt|;
name|saveExtend0
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// use the chached values
name|System
operator|.
name|arraycopy
argument_list|(
name|extend0Values
argument_list|,
literal|0
argument_list|,
name|data
argument_list|,
name|index
argument_list|,
literal|3
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
else|else
block|{
continue|continue;
block|}
block|}
comment|// input value is out of range
elseif|else
if|if
condition|(
name|inputValue
operator|>
name|domain
index|[
literal|1
index|]
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
if|if
condition|(
name|extend1Values
operator|==
literal|null
condition|)
block|{
name|inputValue
operator|=
name|domain
index|[
literal|1
index|]
expr_stmt|;
name|saveExtend1
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// use the chached values
name|System
operator|.
name|arraycopy
argument_list|(
name|extend1Values
argument_list|,
literal|0
argument_list|,
name|data
argument_list|,
name|index
argument_list|,
literal|3
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
else|else
block|{
continue|continue;
block|}
block|}
name|float
name|input
init|=
call|(
name|float
call|)
argument_list|(
name|domain
index|[
literal|0
index|]
operator|+
operator|(
name|d1d0
operator|*
name|inputValue
operator|)
argument_list|)
decl_stmt|;
name|float
index|[]
name|values
init|=
literal|null
decl_stmt|;
try|try
block|{
name|values
operator|=
name|shadingType
operator|.
name|evalFunction
argument_list|(
name|input
argument_list|)
expr_stmt|;
comment|// convert color values from shading colorspace to RGB
if|if
condition|(
name|shadingColorSpace
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|shadingTinttransform
operator|!=
literal|null
condition|)
block|{
name|values
operator|=
name|shadingTinttransform
operator|.
name|eval
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|saveExtend0
condition|)
block|{
comment|// chache values
name|extend0Values
operator|=
operator|new
name|int
index|[
literal|3
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|index
argument_list|,
name|extend0Values
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|saveExtend1
condition|)
block|{
comment|// chache values
name|extend1Values
operator|=
operator|new
name|int
index|[
literal|3
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|index
argument_list|,
name|extend1Values
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

