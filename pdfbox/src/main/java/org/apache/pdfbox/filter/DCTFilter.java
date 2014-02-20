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
name|filter
package|;
end_package

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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|IIOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|metadata
operator|.
name|IIOMetadata
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

begin_comment
comment|/**  * Decompresses data encoded using a DCT (discrete cosine transform)  * technique based on the JPEG standard.  *  * This filter is called {@code DCTDecode} in the PDF Reference.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DCTFilter
implements|implements
name|Filter
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
name|DCTFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
comment|// find suitable image reader
name|Iterator
name|readers
init|=
name|ImageIO
operator|.
name|getImageReadersByFormatName
argument_list|(
literal|"JPEG"
argument_list|)
decl_stmt|;
name|ImageReader
name|reader
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|readers
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|reader
operator|=
operator|(
name|ImageReader
operator|)
name|readers
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|reader
operator|.
name|canReadRaster
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingImageReaderException
argument_list|(
literal|"Cannot read JPEG image: "
operator|+
literal|"a suitable JAI I/O image filter is not installed"
argument_list|)
throw|;
block|}
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
comment|// get the raster using horrible JAI workarounds
name|Raster
name|raster
decl_stmt|;
try|try
block|{
comment|// I'd like to use ImageReader#readRaster but it is buggy and can't read RGB correctly
name|BufferedImage
name|image
init|=
name|reader
operator|.
name|read
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|raster
operator|=
name|image
operator|.
name|getRaster
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IIOException
name|e
parameter_list|)
block|{
comment|// JAI can't read CMYK JPEGs using ImageReader#read or ImageIO.read but
comment|// fortunately ImageReader#readRaster isn't buggy when reading 4-channel files
name|raster
operator|=
name|reader
operator|.
name|readRaster
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// special handling for 4-component images
if|if
condition|(
name|raster
operator|.
name|getNumBands
argument_list|()
operator|==
literal|4
condition|)
block|{
comment|// get APP14 marker
name|Integer
name|transform
decl_stmt|;
try|try
block|{
name|transform
operator|=
name|getAdobeTransform
argument_list|(
name|reader
operator|.
name|getImageMetadata
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IIOException
name|e
parameter_list|)
block|{
comment|// catches the error "Inconsistent metadata read from stream"
comment|// which seems to be present indicate a YCCK image, but who knows?
name|LOG
operator|.
name|warn
argument_list|(
literal|"Inconsistent metadata read from JPEG stream"
argument_list|)
expr_stmt|;
name|transform
operator|=
literal|2
expr_stmt|;
comment|// YCCK
block|}
name|int
name|colorTransform
init|=
name|transform
operator|!=
literal|null
condition|?
name|transform
else|:
literal|0
decl_stmt|;
comment|// 0 = Unknown (RGB or CMYK), 1 = YCbCr, 2 = YCCK
switch|switch
condition|(
name|colorTransform
condition|)
block|{
case|case
literal|0
case|:
break|break;
comment|// already CMYK
case|case
literal|1
case|:
name|LOG
operator|.
name|warn
argument_list|(
literal|"YCbCr JPEGs not implemented"
argument_list|)
expr_stmt|;
break|break;
comment|// TODO YCbCr
case|case
literal|2
case|:
name|raster
operator|=
name|fromYCCKtoCMYK
argument_list|(
name|raster
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
elseif|else
if|if
condition|(
name|raster
operator|.
name|getNumBands
argument_list|()
operator|==
literal|3
condition|)
block|{
comment|// BGR to RGB
name|raster
operator|=
name|fromBGRtoRGB
argument_list|(
name|raster
argument_list|)
expr_stmt|;
block|}
name|DataBufferByte
name|dataBuffer
init|=
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
name|dataBuffer
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"DCTFilter#encode is not implemented yet, skipping this stream."
argument_list|)
expr_stmt|;
block|}
comment|// reads the APP14 Adobe transform tag
specifier|private
name|Integer
name|getAdobeTransform
parameter_list|(
name|IIOMetadata
name|metadata
parameter_list|)
block|{
name|Element
name|tree
init|=
operator|(
name|Element
operator|)
name|metadata
operator|.
name|getAsTree
argument_list|(
literal|"javax_imageio_jpeg_image_1.0"
argument_list|)
decl_stmt|;
name|Element
name|markerSequence
init|=
operator|(
name|Element
operator|)
name|tree
operator|.
name|getElementsByTagName
argument_list|(
literal|"markerSequence"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|markerSequence
operator|.
name|getElementsByTagName
argument_list|(
literal|"app14Adobe"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Element
name|adobe
init|=
operator|(
name|Element
operator|)
name|markerSequence
operator|.
name|getElementsByTagName
argument_list|(
literal|"app14Adobe"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|adobe
operator|.
name|getAttribute
argument_list|(
literal|"transform"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|0
return|;
comment|// Unknown
block|}
comment|// converts YCCK image to CMYK. YCCK is an equivalent encoding for
comment|// CMYK data, so no color management code is needed here, nor does the
comment|// PDF color space have to be consulted
specifier|private
name|WritableRaster
name|fromYCCKtoCMYK
parameter_list|(
name|Raster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
name|WritableRaster
name|writableRaster
init|=
name|raster
operator|.
name|createCompatibleWritableRaster
argument_list|()
decl_stmt|;
name|int
index|[]
name|value
init|=
operator|new
name|int
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
init|,
name|height
init|=
name|raster
operator|.
name|getHeight
argument_list|()
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
init|,
name|width
init|=
name|raster
operator|.
name|getWidth
argument_list|()
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
name|value
argument_list|)
expr_stmt|;
comment|// 4-channels 0..255
name|float
name|Y
init|=
name|value
index|[
literal|0
index|]
decl_stmt|;
name|float
name|Cb
init|=
name|value
index|[
literal|1
index|]
decl_stmt|;
name|float
name|Cr
init|=
name|value
index|[
literal|2
index|]
decl_stmt|;
name|float
name|K
init|=
name|value
index|[
literal|3
index|]
decl_stmt|;
comment|// YCCK to RGB, see http://software.intel.com/en-us/node/442744
name|int
name|r
init|=
name|clamp
argument_list|(
name|Y
operator|+
literal|1.402f
operator|*
name|Cr
operator|-
literal|179.456f
argument_list|)
decl_stmt|;
name|int
name|g
init|=
name|clamp
argument_list|(
name|Y
operator|-
literal|0.34414f
operator|*
name|Cb
operator|-
literal|0.71414f
operator|*
name|Cr
operator|+
literal|135.45984f
argument_list|)
decl_stmt|;
name|int
name|b
init|=
name|clamp
argument_list|(
name|Y
operator|+
literal|1.772f
operator|*
name|Cb
operator|-
literal|226.816f
argument_list|)
decl_stmt|;
comment|// naive RGB to CMYK
name|int
name|cyan
init|=
literal|255
operator|-
name|r
decl_stmt|;
name|int
name|magenta
init|=
literal|255
operator|-
name|g
decl_stmt|;
name|int
name|yellow
init|=
literal|255
operator|-
name|b
decl_stmt|;
comment|// update new raster
name|value
index|[
literal|0
index|]
operator|=
name|cyan
expr_stmt|;
name|value
index|[
literal|1
index|]
operator|=
name|magenta
expr_stmt|;
name|value
index|[
literal|2
index|]
operator|=
name|yellow
expr_stmt|;
name|value
index|[
literal|3
index|]
operator|=
operator|(
name|int
operator|)
name|K
expr_stmt|;
name|writableRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|writableRaster
return|;
block|}
comment|// converts from BGR to RGB
specifier|private
name|WritableRaster
name|fromBGRtoRGB
parameter_list|(
name|Raster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
name|WritableRaster
name|writableRaster
init|=
name|raster
operator|.
name|createCompatibleWritableRaster
argument_list|()
decl_stmt|;
name|int
index|[]
name|bgr
init|=
operator|new
name|int
index|[
literal|3
index|]
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
for|for
control|(
name|int
name|y
init|=
literal|0
init|,
name|height
init|=
name|raster
operator|.
name|getHeight
argument_list|()
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
init|,
name|width
init|=
name|raster
operator|.
name|getWidth
argument_list|()
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
name|bgr
argument_list|)
expr_stmt|;
name|rgb
index|[
literal|0
index|]
operator|=
name|bgr
index|[
literal|2
index|]
expr_stmt|;
name|rgb
index|[
literal|1
index|]
operator|=
name|bgr
index|[
literal|1
index|]
expr_stmt|;
name|rgb
index|[
literal|2
index|]
operator|=
name|bgr
index|[
literal|0
index|]
expr_stmt|;
name|writableRaster
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
name|writableRaster
return|;
block|}
comment|// clamps value to 0-255 range
specifier|private
name|int
name|clamp
parameter_list|(
name|float
name|value
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
operator|(
name|value
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
operator|(
operator|(
name|value
operator|>
literal|255
operator|)
condition|?
literal|255
else|:
name|value
operator|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

