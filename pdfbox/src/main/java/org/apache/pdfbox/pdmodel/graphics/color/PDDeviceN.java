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
name|color
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Point
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
name|BufferedImage
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|COSDictionary
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
name|COSName
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
name|COSNull
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
name|COSArrayList
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

begin_comment
comment|/**  * DeviceN colour spaces may contain an arbitrary number of colour components.  * DeviceN represents a colour space containing multiple components that correspond to colorants  * of some target device. As with Separation colour spaces, readers are able to approximate the  * colorants if they are not available on the current output device, such as a display  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDDeviceN
extends|extends
name|PDSpecialColorSpace
block|{
comment|// array indexes
specifier|private
specifier|static
specifier|final
name|int
name|COLORANT_NAMES
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ALTERNATE_CS
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|TINT_TRANSFORM
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEVICEN_ATTRIBUTES
init|=
literal|4
decl_stmt|;
comment|// fields
specifier|private
name|PDColorSpace
name|alternateColorSpace
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFunction
name|tintTransform
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDeviceNAttributes
name|attributes
decl_stmt|;
specifier|private
name|PDColor
name|initialColor
decl_stmt|;
comment|// color conversion cache
specifier|private
name|int
name|numColorants
decl_stmt|;
specifier|private
name|int
index|[]
name|colorantToComponent
decl_stmt|;
specifier|private
name|PDColorSpace
name|processColorSpace
decl_stmt|;
specifier|private
name|PDSeparation
index|[]
name|spotColorSpaces
decl_stmt|;
comment|/**      * Creates a new DeviceN color space.      */
specifier|public
name|PDDeviceN
parameter_list|()
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DEVICEN
argument_list|)
expr_stmt|;
comment|// empty placeholder
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new DeviceN color space from the given COS array.      * @param deviceN an array containing the color space information      */
specifier|public
name|PDDeviceN
parameter_list|(
name|COSArray
name|deviceN
parameter_list|)
throws|throws
name|IOException
block|{
name|array
operator|=
name|deviceN
expr_stmt|;
name|alternateColorSpace
operator|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
name|ALTERNATE_CS
argument_list|)
argument_list|)
expr_stmt|;
name|tintTransform
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
name|TINT_TRANSFORM
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|>
name|DEVICEN_ATTRIBUTES
condition|)
block|{
name|attributes
operator|=
operator|new
name|PDDeviceNAttributes
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|DEVICEN_ATTRIBUTES
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|initColorConversionCache
argument_list|()
expr_stmt|;
comment|// set initial color space
name|int
name|n
init|=
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|float
index|[]
name|initial
init|=
operator|new
name|float
index|[
name|n
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|initial
index|[
name|i
index|]
operator|=
literal|1
expr_stmt|;
block|}
name|initialColor
operator|=
operator|new
name|PDColor
argument_list|(
name|initial
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|// initializes the color conversion cache
specifier|private
name|void
name|initColorConversionCache
parameter_list|()
throws|throws
name|IOException
block|{
comment|// there's nothing to cache for non-attribute spaces
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// colorant names
name|List
argument_list|<
name|String
argument_list|>
name|colorantNames
init|=
name|getColorantNames
argument_list|()
decl_stmt|;
name|numColorants
operator|=
name|colorantNames
operator|.
name|size
argument_list|()
expr_stmt|;
comment|// process components
name|colorantToComponent
operator|=
operator|new
name|int
index|[
name|numColorants
index|]
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numColorants
condition|;
name|c
operator|++
control|)
block|{
name|colorantToComponent
index|[
name|c
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|attributes
operator|.
name|getProcess
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|components
init|=
name|attributes
operator|.
name|getProcess
argument_list|()
operator|.
name|getComponents
argument_list|()
decl_stmt|;
comment|// map each colorant to the corresponding process component (if any)
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numColorants
condition|;
name|c
operator|++
control|)
block|{
name|colorantToComponent
index|[
name|c
index|]
operator|=
name|components
operator|.
name|indexOf
argument_list|(
name|colorantNames
operator|.
name|get
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// process color space
name|processColorSpace
operator|=
name|attributes
operator|.
name|getProcess
argument_list|()
operator|.
name|getColorSpace
argument_list|()
expr_stmt|;
block|}
comment|// spot colorants
name|spotColorSpaces
operator|=
operator|new
name|PDSeparation
index|[
name|numColorants
index|]
expr_stmt|;
if|if
condition|(
name|attributes
operator|.
name|getColorants
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// spot color spaces
name|Map
argument_list|<
name|String
argument_list|,
name|PDSeparation
argument_list|>
name|spotColorants
init|=
name|attributes
operator|.
name|getColorants
argument_list|()
decl_stmt|;
comment|// map each colorant to the corresponding spot color space
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numColorants
condition|;
name|c
operator|++
control|)
block|{
name|String
name|name
init|=
name|colorantNames
operator|.
name|get
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|PDSeparation
name|spot
init|=
name|spotColorants
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|spot
operator|!=
literal|null
condition|)
block|{
comment|// spot colorant
name|spotColorSpaces
index|[
name|c
index|]
operator|=
name|spot
expr_stmt|;
comment|// spot colors may replace process colors with same name
comment|// providing that the subtype is not NChannel.
if|if
condition|(
operator|!
name|isNChannel
argument_list|()
condition|)
block|{
name|colorantToComponent
index|[
name|c
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// process colorant
name|spotColorSpaces
index|[
name|c
index|]
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|BufferedImage
name|toRGBImage
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
return|return
name|toRGBWithAttributes
argument_list|(
name|raster
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toRGBWithTintTransform
argument_list|(
name|raster
argument_list|)
return|;
block|}
block|}
comment|//
comment|// WARNING: this method is performance sensitive, modify with care!
comment|//
specifier|private
name|BufferedImage
name|toRGBWithAttributes
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|width
init|=
name|raster
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|raster
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|BufferedImage
name|rgbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|WritableRaster
name|rgbRaster
init|=
name|rgbImage
operator|.
name|getRaster
argument_list|()
decl_stmt|;
comment|// white background
name|Graphics2D
name|g
init|=
name|rgbImage
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|g
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|g
operator|.
name|clearRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
comment|// look up each colorant
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numColorants
condition|;
name|c
operator|++
control|)
block|{
name|PDColorSpace
name|componentColorSpace
decl_stmt|;
if|if
condition|(
name|colorantToComponent
index|[
name|c
index|]
operator|>=
literal|0
condition|)
block|{
comment|// process color
name|componentColorSpace
operator|=
name|processColorSpace
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|spotColorSpaces
index|[
name|c
index|]
operator|==
literal|null
condition|)
block|{
comment|// TODO this happens in the Altona Visual test, is there a better workaround?
comment|// missing spot color, fallback to using tintTransform
return|return
name|toRGBWithTintTransform
argument_list|(
name|raster
argument_list|)
return|;
block|}
else|else
block|{
comment|// spot color
name|componentColorSpace
operator|=
name|spotColorSpaces
index|[
name|c
index|]
expr_stmt|;
block|}
comment|// copy single-component to its own raster in the component color space
name|WritableRaster
name|componentRaster
init|=
name|Raster
operator|.
name|createBandedRaster
argument_list|(
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
name|componentColorSpace
operator|.
name|getNumberOfComponents
argument_list|()
argument_list|,
operator|new
name|Point
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|int
index|[]
name|samples
init|=
operator|new
name|int
index|[
name|numColorants
index|]
decl_stmt|;
name|int
index|[]
name|componentSamples
init|=
operator|new
name|int
index|[
name|componentColorSpace
operator|.
name|getNumberOfComponents
argument_list|()
index|]
decl_stmt|;
name|boolean
name|isProcessColorant
init|=
name|colorantToComponent
index|[
name|c
index|]
operator|>=
literal|0
decl_stmt|;
name|int
name|componentIndex
init|=
name|colorantToComponent
index|[
name|c
index|]
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
condition|;
name|y
operator|++
control|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|width
condition|;
name|x
operator|++
control|)
block|{
name|raster
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|samples
argument_list|)
expr_stmt|;
if|if
condition|(
name|isProcessColorant
condition|)
block|{
comment|// process color
name|componentSamples
index|[
name|componentIndex
index|]
operator|=
name|samples
index|[
name|c
index|]
expr_stmt|;
block|}
else|else
block|{
comment|// spot color
name|componentSamples
index|[
literal|0
index|]
operator|=
name|samples
index|[
name|c
index|]
expr_stmt|;
block|}
name|componentRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|componentSamples
argument_list|)
expr_stmt|;
block|}
block|}
comment|// convert single-component raster to RGB
name|BufferedImage
name|rgbComponentImage
init|=
name|componentColorSpace
operator|.
name|toRGBImage
argument_list|(
name|componentRaster
argument_list|)
decl_stmt|;
name|WritableRaster
name|rgbComponentRaster
init|=
name|rgbComponentImage
operator|.
name|getRaster
argument_list|()
decl_stmt|;
comment|// combine the RGB component with the RGB composite raster
name|int
index|[]
name|rgbChannel
init|=
operator|new
name|int
index|[
literal|3
index|]
decl_stmt|;
name|int
index|[]
name|rgbComposite
init|=
operator|new
name|int
index|[
literal|3
index|]
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
condition|;
name|y
operator|++
control|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|width
condition|;
name|x
operator|++
control|)
block|{
name|rgbComponentRaster
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgbChannel
argument_list|)
expr_stmt|;
name|rgbRaster
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgbComposite
argument_list|)
expr_stmt|;
comment|// multiply (blend mode)
name|rgbChannel
index|[
literal|0
index|]
operator|=
name|rgbChannel
index|[
literal|0
index|]
operator|*
name|rgbComposite
index|[
literal|0
index|]
operator|>>
literal|8
expr_stmt|;
name|rgbChannel
index|[
literal|1
index|]
operator|=
name|rgbChannel
index|[
literal|1
index|]
operator|*
name|rgbComposite
index|[
literal|1
index|]
operator|>>
literal|8
expr_stmt|;
name|rgbChannel
index|[
literal|2
index|]
operator|=
name|rgbChannel
index|[
literal|2
index|]
operator|*
name|rgbComposite
index|[
literal|2
index|]
operator|>>
literal|8
expr_stmt|;
name|rgbRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgbChannel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|rgbImage
return|;
block|}
comment|//
comment|// WARNING: this method is performance sensitive, modify with care!
comment|//
specifier|private
name|BufferedImage
name|toRGBWithTintTransform
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
comment|// map only in use if one color component
name|Map
argument_list|<
name|Float
argument_list|,
name|int
index|[]
argument_list|>
name|map1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|float
name|key
init|=
literal|0
decl_stmt|;
name|int
name|width
init|=
name|raster
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|raster
operator|.
name|getHeight
argument_list|()
decl_stmt|;
comment|// use the tint transform to convert the sample into
comment|// the alternate color space (this is usually 1:many)
name|BufferedImage
name|rgbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|WritableRaster
name|rgbRaster
init|=
name|rgbImage
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|int
index|[]
name|rgb
init|=
operator|new
name|int
index|[
literal|3
index|]
decl_stmt|;
name|int
name|numSrcComponents
init|=
name|getColorantNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|float
index|[]
name|src
init|=
operator|new
name|float
index|[
name|numSrcComponents
index|]
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
condition|;
name|y
operator|++
control|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|width
condition|;
name|x
operator|++
control|)
block|{
name|raster
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|src
argument_list|)
expr_stmt|;
if|if
condition|(
name|numSrcComponents
operator|==
literal|1
condition|)
block|{
name|int
index|[]
name|pxl
init|=
name|map1
operator|.
name|get
argument_list|(
name|src
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|pxl
operator|!=
literal|null
condition|)
block|{
name|rgbRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|pxl
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
comment|// need to remember key because src is modified
name|key
operator|=
name|src
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
comment|// scale to 0..1
for|for
control|(
name|int
name|s
init|=
literal|0
init|;
name|s
operator|<
name|numSrcComponents
condition|;
name|s
operator|++
control|)
block|{
name|src
index|[
name|s
index|]
operator|=
name|src
index|[
name|s
index|]
operator|/
literal|255
expr_stmt|;
block|}
comment|// convert to alternate color space via tint transform
name|float
index|[]
name|result
init|=
name|tintTransform
operator|.
name|eval
argument_list|(
name|src
argument_list|)
decl_stmt|;
comment|// convert from alternate color space to RGB
name|float
index|[]
name|rgbFloat
init|=
name|alternateColorSpace
operator|.
name|toRGB
argument_list|(
name|result
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|s
init|=
literal|0
init|;
name|s
operator|<
literal|3
condition|;
name|s
operator|++
control|)
block|{
comment|// scale to 0..255
name|rgb
index|[
name|s
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|rgbFloat
index|[
name|s
index|]
operator|*
literal|255f
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|numSrcComponents
operator|==
literal|1
condition|)
block|{
comment|// must clone because rgb is reused
name|map1
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|rgb
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|rgbRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgb
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rgbImage
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|toRGB
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
return|return
name|toRGBWithAttributes
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|toRGBWithTintTransform
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
specifier|private
name|float
index|[]
name|toRGBWithAttributes
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|float
index|[]
name|rgbValue
init|=
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|1
block|,
literal|1
block|}
decl_stmt|;
comment|// look up each colorant
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numColorants
condition|;
name|c
operator|++
control|)
block|{
name|PDColorSpace
name|componentColorSpace
decl_stmt|;
if|if
condition|(
name|colorantToComponent
index|[
name|c
index|]
operator|>=
literal|0
condition|)
block|{
comment|// process color
name|componentColorSpace
operator|=
name|processColorSpace
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|spotColorSpaces
index|[
name|c
index|]
operator|==
literal|null
condition|)
block|{
comment|// TODO this happens in the Altona Visual test, is there a better workaround?
comment|// missing spot color, fallback to using tintTransform
return|return
name|toRGBWithTintTransform
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
comment|// spot color
name|componentColorSpace
operator|=
name|spotColorSpaces
index|[
name|c
index|]
expr_stmt|;
block|}
comment|// get the single component
name|boolean
name|isProcessColorant
init|=
name|colorantToComponent
index|[
name|c
index|]
operator|>=
literal|0
decl_stmt|;
name|float
index|[]
name|componentSamples
init|=
operator|new
name|float
index|[
name|componentColorSpace
operator|.
name|getNumberOfComponents
argument_list|()
index|]
decl_stmt|;
name|int
name|componentIndex
init|=
name|colorantToComponent
index|[
name|c
index|]
decl_stmt|;
if|if
condition|(
name|isProcessColorant
condition|)
block|{
comment|// process color
name|componentSamples
index|[
name|componentIndex
index|]
operator|=
name|value
index|[
name|c
index|]
expr_stmt|;
block|}
else|else
block|{
comment|// spot color
name|componentSamples
index|[
literal|0
index|]
operator|=
name|value
index|[
name|c
index|]
expr_stmt|;
block|}
comment|// convert single component to RGB
name|float
index|[]
name|rgbComponent
init|=
name|componentColorSpace
operator|.
name|toRGB
argument_list|(
name|componentSamples
argument_list|)
decl_stmt|;
comment|// combine the RGB component value with the RGB composite value
comment|// multiply (blend mode)
name|rgbValue
index|[
literal|0
index|]
operator|*=
name|rgbComponent
index|[
literal|0
index|]
expr_stmt|;
name|rgbValue
index|[
literal|1
index|]
operator|*=
name|rgbComponent
index|[
literal|1
index|]
expr_stmt|;
name|rgbValue
index|[
literal|2
index|]
operator|*=
name|rgbComponent
index|[
literal|2
index|]
expr_stmt|;
block|}
return|return
name|rgbValue
return|;
block|}
specifier|private
name|float
index|[]
name|toRGBWithTintTransform
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
throws|throws
name|IOException
block|{
comment|// use the tint transform to convert the sample into
comment|// the alternate color space (this is usually 1:many)
name|float
index|[]
name|altValue
init|=
name|tintTransform
operator|.
name|eval
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// convert the alternate color space to RGB
return|return
name|alternateColorSpace
operator|.
name|toRGB
argument_list|(
name|altValue
argument_list|)
return|;
block|}
comment|/**      * Returns true if this color space has the NChannel subtype.      * @return true if subtype is NChannel      */
specifier|public
name|boolean
name|isNChannel
parameter_list|()
block|{
return|return
name|attributes
operator|!=
literal|null
operator|&&
name|attributes
operator|.
name|isNChannel
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|DEVICEN
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
name|int
name|getNumberOfComponents
parameter_list|()
block|{
return|return
name|getColorantNames
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|getDefaultDecode
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
block|{
name|int
name|n
init|=
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|float
index|[]
name|decode
init|=
operator|new
name|float
index|[
name|n
operator|*
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|decode
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
operator|=
literal|1
expr_stmt|;
block|}
return|return
name|decode
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDColor
name|getInitialColor
parameter_list|()
block|{
return|return
name|initialColor
return|;
block|}
comment|/**      * Returns the list of colorants.      * @return the list of colorants      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getColorantNames
parameter_list|()
block|{
name|COSArray
name|names
init|=
operator|(
name|COSArray
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|COLORANT_NAMES
argument_list|)
decl_stmt|;
return|return
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
name|names
argument_list|)
return|;
block|}
comment|/**      * Returns the attributes associated with the DeviceN color space.      * @return the DeviceN attributes      */
specifier|public
name|PDDeviceNAttributes
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
comment|/**      * Sets the list of colorants      * @param names the list of colorants      */
specifier|public
name|void
name|setColorantNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|COSArray
name|namesArray
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|names
argument_list|)
decl_stmt|;
name|array
operator|.
name|set
argument_list|(
name|COLORANT_NAMES
argument_list|,
name|namesArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the color space attributes.      * If null is passed in then all attribute will be removed.      * @param attributes the color space attributes, or null      */
specifier|public
name|void
name|setAttributes
parameter_list|(
name|PDDeviceNAttributes
name|attributes
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|array
operator|.
name|remove
argument_list|(
name|DEVICEN_ATTRIBUTES
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// make sure array is large enough
while|while
condition|(
name|array
operator|.
name|size
argument_list|()
operator|<=
name|DEVICEN_ATTRIBUTES
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
name|DEVICEN_ATTRIBUTES
argument_list|,
name|attributes
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the alternate color space for this separation.      *      * @return The alternate color space.      *      * @throws IOException If there is an error getting the alternate color      * space.      */
specifier|public
name|PDColorSpace
name|getAlternateColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|alternateColorSpace
operator|==
literal|null
condition|)
block|{
name|alternateColorSpace
operator|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
name|ALTERNATE_CS
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|alternateColorSpace
return|;
block|}
comment|/**      * This will set the alternate color space.      *      * @param cs The alternate color space.      */
specifier|public
name|void
name|setAlternateColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|alternateColorSpace
operator|=
name|cs
expr_stmt|;
name|COSBase
name|space
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|space
operator|=
name|cs
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
name|ALTERNATE_CS
argument_list|,
name|space
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the tint transform function.      *      * @return The tint transform function.      *      * @throws IOException if there is an error creating the function.      */
specifier|public
name|PDFunction
name|getTintTransform
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|tintTransform
operator|==
literal|null
condition|)
block|{
name|tintTransform
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
name|TINT_TRANSFORM
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tintTransform
return|;
block|}
comment|/**      * This will set the tint transform function.      *      * @param tint The tint transform function.      */
specifier|public
name|void
name|setTintTransform
parameter_list|(
name|PDFunction
name|tint
parameter_list|)
block|{
name|tintTransform
operator|=
name|tint
expr_stmt|;
name|array
operator|.
name|set
argument_list|(
name|TINT_TRANSFORM
argument_list|,
name|tint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|col
range|:
name|getColorantNames
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|col
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|alternateColorSpace
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|tintTransform
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

