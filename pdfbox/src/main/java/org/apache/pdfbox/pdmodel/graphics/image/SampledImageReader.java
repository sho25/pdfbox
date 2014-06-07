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
name|image
package|;
end_package

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
name|Paint
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
name|DataBufferByte
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|io
operator|.
name|IOUtils
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
name|PDIndexed
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|ImageInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageInputStream
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
name|PDMemoryStream
import|;
end_import

begin_comment
comment|/**  * Reads a sampled image from a PDF file.  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|SampledImageReader
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
name|SampledImageReader
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Returns an ARGB image filled with the given paint and using the given image as a mask.      * @param paint the paint to fill the visible portions of the image with      * @return a masked image filled with the given paint      * @throws IOException if the image cannot be read      * @throws IllegalStateException if the image is not a stencil.      */
specifier|public
specifier|static
name|BufferedImage
name|getStencilImage
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|Paint
name|paint
parameter_list|)
throws|throws
name|IOException
block|{
comment|// get mask (this image)
name|BufferedImage
name|mask
init|=
name|getRGBImage
argument_list|(
name|pdImage
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// compose to ARGB
name|BufferedImage
name|masked
init|=
operator|new
name|BufferedImage
argument_list|(
name|mask
operator|.
name|getWidth
argument_list|()
argument_list|,
name|mask
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|Graphics2D
name|g
init|=
name|masked
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
comment|// draw the mask
comment|//g.drawImage(mask, 0, 0, null);
comment|// fill with paint using src-in
comment|//g.setComposite(AlphaComposite.SrcIn);
name|g
operator|.
name|setPaint
argument_list|(
name|paint
argument_list|)
expr_stmt|;
name|g
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|mask
operator|.
name|getWidth
argument_list|()
argument_list|,
name|mask
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
comment|// set the alpha
name|int
name|width
init|=
name|masked
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|masked
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|WritableRaster
name|raster
init|=
name|masked
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|WritableRaster
name|alpha
init|=
name|mask
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|float
index|[]
name|rgba
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
specifier|final
name|float
index|[]
name|transparent
init|=
operator|new
name|float
index|[
literal|4
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
name|rgba
argument_list|)
expr_stmt|;
if|if
condition|(
name|alpha
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
operator|(
name|float
index|[]
operator|)
literal|null
argument_list|)
index|[
literal|0
index|]
operator|==
literal|255
condition|)
block|{
name|raster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|transparent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|raster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgba
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|masked
return|;
block|}
comment|/**      * Returns the content of the given image as an AWT buffered image with an RGB color space.      * If a color key mask is provided then an ARGB image is returned instead.      * This method never returns null.      * @param pdImage the image to read      * @param colorKey an optional color key mask      * @return content of this image as an RGB buffered image      * @throws IOException if the image cannot be read      */
specifier|public
specifier|static
name|BufferedImage
name|getRGBImage
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|COSArray
name|colorKey
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdImage
operator|.
name|getStream
argument_list|()
operator|instanceof
name|PDMemoryStream
condition|)
block|{
comment|// for inline images
if|if
condition|(
name|pdImage
operator|.
name|getStream
argument_list|()
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Image stream is empty"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|pdImage
operator|.
name|getStream
argument_list|()
operator|.
name|getStream
argument_list|()
operator|.
name|getFilteredLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Image stream is empty"
argument_list|)
throw|;
block|}
comment|// get parameters, they must be valid or have been repaired
specifier|final
name|PDColorSpace
name|colorSpace
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
specifier|final
name|int
name|numComponents
init|=
name|colorSpace
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
specifier|final
name|int
name|width
init|=
name|pdImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|height
init|=
name|pdImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
specifier|final
name|int
name|bitsPerComponent
init|=
name|pdImage
operator|.
name|getBitsPerComponent
argument_list|()
decl_stmt|;
specifier|final
name|float
index|[]
name|decode
init|=
name|getDecodeArray
argument_list|(
name|pdImage
argument_list|)
decl_stmt|;
comment|//
comment|// An AWT raster must use 8/16/32 bits per component. Images with< 8bpc
comment|// will be unpacked into a byte-backed raster. Images with 16bpc will be reduced
comment|// in depth to 8bpc as they will be drawn to TYPE_INT_RGB images anyway. All code
comment|// in PDColorSpace#toRGBImage expects and 8-bit range, i.e. 0-255.
comment|//
name|WritableRaster
name|raster
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
name|numComponents
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
comment|// convert image, faster path for non-decoded, non-colormasked 8-bit images
specifier|final
name|float
index|[]
name|defaultDecode
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getDefaultDecode
argument_list|(
literal|8
argument_list|)
decl_stmt|;
if|if
condition|(
name|bitsPerComponent
operator|==
literal|8
operator|&&
name|Arrays
operator|.
name|equals
argument_list|(
name|decode
argument_list|,
name|defaultDecode
argument_list|)
operator|&&
name|colorKey
operator|==
literal|null
condition|)
block|{
return|return
name|from8bit
argument_list|(
name|pdImage
argument_list|,
name|raster
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|bitsPerComponent
operator|==
literal|1
operator|&&
name|colorKey
operator|==
literal|null
condition|)
block|{
return|return
name|from1Bit
argument_list|(
name|pdImage
argument_list|,
name|raster
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|fromAny
argument_list|(
name|pdImage
argument_list|,
name|raster
argument_list|,
name|colorKey
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
name|BufferedImage
name|from1Bit
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|PDColorSpace
name|colorSpace
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
specifier|final
name|int
name|width
init|=
name|pdImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|height
init|=
name|pdImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
specifier|final
name|float
index|[]
name|decode
init|=
name|getDecodeArray
argument_list|(
name|pdImage
argument_list|)
decl_stmt|;
name|byte
index|[]
name|output
init|=
operator|(
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|)
operator|.
name|getData
argument_list|()
decl_stmt|;
comment|// read bit stream
name|InputStream
name|iis
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// create stream
name|iis
operator|=
name|pdImage
operator|.
name|getStream
argument_list|()
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|isIndexed
init|=
name|colorSpace
operator|instanceof
name|PDIndexed
decl_stmt|;
name|int
name|rowLen
init|=
name|width
operator|/
literal|8
decl_stmt|;
if|if
condition|(
name|width
operator|%
literal|8
operator|>
literal|0
condition|)
block|{
name|rowLen
operator|++
expr_stmt|;
block|}
comment|// read stream
name|byte
name|value0
decl_stmt|;
name|byte
name|value1
decl_stmt|;
if|if
condition|(
name|isIndexed
operator|||
name|decode
index|[
literal|0
index|]
operator|<
name|decode
index|[
literal|1
index|]
condition|)
block|{
name|value0
operator|=
literal|0
expr_stmt|;
name|value1
operator|=
operator|(
name|byte
operator|)
literal|255
expr_stmt|;
block|}
else|else
block|{
name|value0
operator|=
operator|(
name|byte
operator|)
literal|255
expr_stmt|;
name|value1
operator|=
literal|0
expr_stmt|;
block|}
name|byte
index|[]
name|buff
init|=
operator|new
name|byte
index|[
name|rowLen
index|]
decl_stmt|;
name|int
name|idx
init|=
literal|0
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
name|int
name|x
init|=
literal|0
decl_stmt|;
name|iis
operator|.
name|read
argument_list|(
name|buff
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|r
init|=
literal|0
init|;
name|r
operator|<
name|rowLen
condition|;
name|r
operator|++
control|)
block|{
name|int
name|value
init|=
name|buff
index|[
name|r
index|]
decl_stmt|;
name|int
name|mask
init|=
literal|128
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
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|int
name|bit
init|=
name|value
operator|&
name|mask
decl_stmt|;
name|mask
operator|>>=
literal|1
expr_stmt|;
name|output
index|[
name|idx
operator|++
index|]
operator|=
name|bit
operator|==
literal|0
condition|?
name|value0
else|:
name|value1
expr_stmt|;
name|x
operator|++
expr_stmt|;
if|if
condition|(
name|x
operator|==
name|width
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
comment|// use the color space to convert the image to RGB
name|BufferedImage
name|rgbImage
init|=
name|colorSpace
operator|.
name|toRGBImage
argument_list|(
name|raster
argument_list|)
decl_stmt|;
return|return
name|rgbImage
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|iis
operator|!=
literal|null
condition|)
block|{
name|iis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// faster, 8-bit non-decoded, non-colormasked image conversion
specifier|private
specifier|static
name|BufferedImage
name|from8bit
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|input
init|=
name|pdImage
operator|.
name|getStream
argument_list|()
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
try|try
block|{
comment|// get the raster's underlying byte buffer
name|byte
index|[]
index|[]
name|banks
init|=
operator|(
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|)
operator|.
name|getBankData
argument_list|()
decl_stmt|;
name|byte
index|[]
name|source
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|input
argument_list|)
decl_stmt|;
specifier|final
name|int
name|width
init|=
name|pdImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|height
init|=
name|pdImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
specifier|final
name|int
name|numComponents
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numComponents
condition|;
name|c
operator|++
control|)
block|{
name|int
name|sourceOffset
init|=
name|c
decl_stmt|;
name|int
name|max
init|=
name|width
operator|*
name|height
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
name|max
condition|;
name|i
operator|++
control|)
block|{
name|banks
index|[
name|c
index|]
index|[
name|i
index|]
operator|=
name|source
index|[
name|sourceOffset
index|]
expr_stmt|;
name|sourceOffset
operator|+=
name|numComponents
expr_stmt|;
block|}
block|}
comment|// use the color space to convert the image to RGB
return|return
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|toRGBImage
argument_list|(
name|raster
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
comment|// slower, general-purpose image conversion from any image format
specifier|private
specifier|static
name|BufferedImage
name|fromAny
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|WritableRaster
name|raster
parameter_list|,
name|COSArray
name|colorKey
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|PDColorSpace
name|colorSpace
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
specifier|final
name|int
name|numComponents
init|=
name|colorSpace
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
specifier|final
name|int
name|width
init|=
name|pdImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|height
init|=
name|pdImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
specifier|final
name|int
name|bitsPerComponent
init|=
name|pdImage
operator|.
name|getBitsPerComponent
argument_list|()
decl_stmt|;
specifier|final
name|float
index|[]
name|decode
init|=
name|getDecodeArray
argument_list|(
name|pdImage
argument_list|)
decl_stmt|;
comment|// read bit stream
name|ImageInputStream
name|iis
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// create stream
name|iis
operator|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|pdImage
operator|.
name|getStream
argument_list|()
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|float
name|sampleMax
init|=
operator|(
name|float
operator|)
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bitsPerComponent
argument_list|)
operator|-
literal|1f
decl_stmt|;
specifier|final
name|boolean
name|isIndexed
init|=
name|colorSpace
operator|instanceof
name|PDIndexed
decl_stmt|;
comment|// init color key mask
name|float
index|[]
name|colorKeyRanges
init|=
literal|null
decl_stmt|;
name|BufferedImage
name|colorKeyMask
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorKey
operator|!=
literal|null
condition|)
block|{
name|colorKeyRanges
operator|=
name|colorKey
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
name|colorKeyMask
operator|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_BYTE_GRAY
argument_list|)
expr_stmt|;
block|}
comment|// calculate row padding
name|int
name|padding
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|width
operator|*
name|numComponents
operator|*
name|bitsPerComponent
operator|%
literal|8
operator|>
literal|0
condition|)
block|{
name|padding
operator|=
literal|8
operator|-
operator|(
name|width
operator|*
name|numComponents
operator|*
name|bitsPerComponent
operator|%
literal|8
operator|)
expr_stmt|;
block|}
comment|// read stream
name|byte
index|[]
name|srcColorValues
init|=
operator|new
name|byte
index|[
name|numComponents
index|]
decl_stmt|;
name|byte
index|[]
name|alpha
init|=
operator|new
name|byte
index|[
literal|1
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
name|boolean
name|isMasked
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numComponents
condition|;
name|c
operator|++
control|)
block|{
name|int
name|value
init|=
operator|(
name|int
operator|)
name|iis
operator|.
name|readBits
argument_list|(
name|bitsPerComponent
argument_list|)
decl_stmt|;
comment|// color key mask requires values before they are decoded
if|if
condition|(
name|colorKeyRanges
operator|!=
literal|null
condition|)
block|{
name|isMasked
operator|&=
name|value
operator|>=
name|colorKeyRanges
index|[
name|c
operator|*
literal|2
index|]
operator|&&
name|value
operator|<=
name|colorKeyRanges
index|[
name|c
operator|*
literal|2
operator|+
literal|1
index|]
expr_stmt|;
block|}
comment|// decode array
specifier|final
name|float
name|dMin
init|=
name|decode
index|[
name|c
operator|*
literal|2
index|]
decl_stmt|;
specifier|final
name|float
name|dMax
init|=
name|decode
index|[
operator|(
name|c
operator|*
literal|2
operator|)
operator|+
literal|1
index|]
decl_stmt|;
comment|// interpolate to domain
name|float
name|output
init|=
name|dMin
operator|+
operator|(
name|value
operator|*
operator|(
operator|(
name|dMax
operator|-
name|dMin
operator|)
operator|/
name|sampleMax
operator|)
operator|)
decl_stmt|;
if|if
condition|(
name|isIndexed
condition|)
block|{
comment|// indexed color spaces get the raw value, because the TYPE_BYTE
comment|// below cannot be reversed by the color space without it having
comment|// knowledge of the number of bits per component
name|srcColorValues
index|[
name|c
index|]
operator|=
operator|(
name|byte
operator|)
name|Math
operator|.
name|round
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// interpolate to TYPE_BYTE
name|int
name|outputByte
init|=
name|Math
operator|.
name|round
argument_list|(
operator|(
operator|(
name|output
operator|-
name|Math
operator|.
name|min
argument_list|(
name|dMin
argument_list|,
name|dMax
argument_list|)
operator|)
operator|/
name|Math
operator|.
name|abs
argument_list|(
name|dMax
operator|-
name|dMin
argument_list|)
operator|)
operator|*
literal|255f
argument_list|)
decl_stmt|;
name|srcColorValues
index|[
name|c
index|]
operator|=
operator|(
name|byte
operator|)
name|outputByte
expr_stmt|;
block|}
block|}
name|raster
operator|.
name|setDataElements
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|srcColorValues
argument_list|)
expr_stmt|;
comment|// set alpha channel in color key mask, if any
if|if
condition|(
name|colorKeyMask
operator|!=
literal|null
condition|)
block|{
name|alpha
index|[
literal|0
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|isMasked
condition|?
literal|255
else|:
literal|0
argument_list|)
expr_stmt|;
name|colorKeyMask
operator|.
name|getRaster
argument_list|()
operator|.
name|setDataElements
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|alpha
argument_list|)
expr_stmt|;
block|}
block|}
comment|// rows are padded to the nearest byte
name|iis
operator|.
name|readBits
argument_list|(
name|padding
argument_list|)
expr_stmt|;
block|}
comment|// use the color space to convert the image to RGB
name|BufferedImage
name|rgbImage
init|=
name|colorSpace
operator|.
name|toRGBImage
argument_list|(
name|raster
argument_list|)
decl_stmt|;
comment|// apply color mask, if any
if|if
condition|(
name|colorKeyMask
operator|!=
literal|null
condition|)
block|{
return|return
name|applyColorKeyMask
argument_list|(
name|rgbImage
argument_list|,
name|colorKeyMask
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|rgbImage
return|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|iis
operator|!=
literal|null
condition|)
block|{
name|iis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// color key mask: RGB + Binary -> ARGB
specifier|private
specifier|static
name|BufferedImage
name|applyColorKeyMask
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|BufferedImage
name|mask
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|width
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
comment|// compose to ARGB
name|BufferedImage
name|masked
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
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|WritableRaster
name|src
init|=
name|image
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|WritableRaster
name|dest
init|=
name|masked
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|WritableRaster
name|alpha
init|=
name|mask
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|float
index|[]
name|rgb
init|=
operator|new
name|float
index|[
literal|3
index|]
decl_stmt|;
name|float
index|[]
name|rgba
init|=
operator|new
name|float
index|[
literal|4
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
name|src
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgb
argument_list|)
expr_stmt|;
name|rgba
index|[
literal|0
index|]
operator|=
name|rgb
index|[
literal|0
index|]
expr_stmt|;
name|rgba
index|[
literal|1
index|]
operator|=
name|rgb
index|[
literal|1
index|]
expr_stmt|;
name|rgba
index|[
literal|2
index|]
operator|=
name|rgb
index|[
literal|2
index|]
expr_stmt|;
name|rgba
index|[
literal|3
index|]
operator|=
literal|255
operator|-
name|alpha
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
operator|(
name|float
index|[]
operator|)
literal|null
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
name|dest
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgba
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|masked
return|;
block|}
comment|// gets decode array from dictionary or returns default
specifier|private
specifier|static
name|float
index|[]
name|getDecodeArray
parameter_list|(
name|PDImage
name|pdImage
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|COSArray
name|cosDecode
init|=
name|pdImage
operator|.
name|getDecode
argument_list|()
decl_stmt|;
name|float
index|[]
name|decode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cosDecode
operator|!=
literal|null
condition|)
block|{
name|decode
operator|=
name|cosDecode
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
comment|// if ImageMask is true then decode must be [0 1] or [1 0]
if|if
condition|(
name|pdImage
operator|.
name|isStencil
argument_list|()
operator|&&
operator|(
name|decode
operator|.
name|length
operator|!=
literal|2
operator|||
name|decode
index|[
literal|0
index|]
operator|<
literal|0
operator|||
name|decode
index|[
literal|0
index|]
operator|>
literal|1
operator|||
name|decode
index|[
literal|1
index|]
operator|<
literal|0
operator|||
name|decode
index|[
literal|1
index|]
operator|>
literal|1
operator|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignored invalid decode array: not compatible with ImageMask"
argument_list|)
expr_stmt|;
name|decode
operator|=
literal|null
expr_stmt|;
block|}
comment|// otherwise, its length shall be twice the number of colour
comment|// components required by ColorSpace
name|int
name|n
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|decode
operator|!=
literal|null
operator|&&
name|decode
operator|.
name|length
operator|!=
name|n
operator|*
literal|2
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignored invalid decode array: not compatible with color space"
argument_list|)
expr_stmt|;
name|decode
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// use color space default
if|if
condition|(
name|decode
operator|==
literal|null
condition|)
block|{
return|return
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getDefaultDecode
argument_list|(
name|pdImage
operator|.
name|getBitsPerComponent
argument_list|()
argument_list|)
return|;
block|}
return|return
name|decode
return|;
block|}
block|}
end_class

end_unit

