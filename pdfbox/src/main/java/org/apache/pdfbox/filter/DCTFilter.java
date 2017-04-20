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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|metadata
operator|.
name|IIOMetadataNode
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * Decompresses data encoded using a DCT (discrete cosine transform)  * technique based on the JPEG standard.  *  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|DCTFilter
extends|extends
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
annotation|@
name|Override
specifier|public
name|DecodeResult
name|decode
parameter_list|(
name|InputStream
name|encoded
parameter_list|,
name|OutputStream
name|decoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageReader
name|reader
init|=
name|findImageReader
argument_list|(
literal|"JPEG"
argument_list|,
literal|"a suitable JAI I/O image filter is not installed"
argument_list|)
decl_stmt|;
try|try
init|(
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|encoded
argument_list|)
init|)
block|{
comment|// skip one LF if there
if|if
condition|(
name|iis
operator|.
name|read
argument_list|()
operator|!=
literal|0x0A
condition|)
block|{
name|iis
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
name|String
name|numChannels
init|=
name|getNumChannels
argument_list|(
name|reader
argument_list|)
decl_stmt|;
comment|// get the raster using horrible JAI workarounds
name|ImageIO
operator|.
name|setUseCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Raster
name|raster
decl_stmt|;
comment|// Strategy: use read() for RGB or "can't get metadata"
comment|// use readRaster() for CMYK and gray and as fallback if read() fails
comment|// after "can't get metadata" because "no meta" file was CMYK
if|if
condition|(
literal|"3"
operator|.
name|equals
argument_list|(
name|numChannels
argument_list|)
operator|||
name|numChannels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
block|}
else|else
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
comment|// if we're using the Sun decoder then can be caused by either a YCCK
comment|// image or by a CMYK image which the decoder has problems reading
try|try
block|{
comment|// if this is Sun's decoder, use reflection to determine if the
comment|// color space is CMYK or YCCK
name|Field
name|field
init|=
name|reader
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"colorSpaceCode"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|int
name|colorSpaceCode
init|=
name|field
operator|.
name|getInt
argument_list|(
name|reader
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|colorSpaceCode
condition|)
block|{
case|case
literal|7
case|:
case|case
literal|8
case|:
case|case
literal|9
case|:
case|case
literal|11
case|:
comment|// YCCK
name|transform
operator|=
literal|2
expr_stmt|;
break|break;
case|case
literal|4
case|:
comment|// CMYK
name|transform
operator|=
literal|0
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected color space: "
operator|+
name|colorSpaceCode
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
decl||
name|IllegalAccessException
name|e1
parameter_list|)
block|{
comment|// error from non-Sun JPEG decoder
throw|throw
name|e
throw|;
block|}
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
comment|// already CMYK
break|break;
case|case
literal|1
case|:
comment|// TODO YCbCr
name|LOG
operator|.
name|warn
argument_list|(
literal|"YCbCr JPEGs not implemented"
argument_list|)
expr_stmt|;
break|break;
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
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown colorTransform"
argument_list|)
throw|;
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
name|decoded
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
finally|finally
block|{
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|DecodeResult
argument_list|(
name|parameters
argument_list|)
return|;
block|}
comment|// reads the APP14 Adobe transform tag and returns its value, or 0 if unknown
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
name|NodeList
name|app14AdobeNodeList
init|=
name|markerSequence
operator|.
name|getElementsByTagName
argument_list|(
literal|"app14Adobe"
argument_list|)
decl_stmt|;
if|if
condition|(
name|app14AdobeNodeList
operator|!=
literal|null
operator|&&
name|app14AdobeNodeList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Element
name|adobe
init|=
operator|(
name|Element
operator|)
name|app14AdobeNodeList
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
name|int
name|w3
init|=
name|width
operator|*
literal|3
decl_stmt|;
name|int
index|[]
name|tab
init|=
operator|new
name|int
index|[
name|w3
index|]
decl_stmt|;
comment|//BEWARE: handling the full image at a time is slower than one line at a time
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
name|raster
operator|.
name|getPixels
argument_list|(
literal|0
argument_list|,
name|y
argument_list|,
name|width
argument_list|,
literal|1
argument_list|,
name|tab
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|off
init|=
literal|0
init|;
name|off
operator|<
name|w3
condition|;
name|off
operator|+=
literal|3
control|)
block|{
name|int
name|tmp
init|=
name|tab
index|[
name|off
index|]
decl_stmt|;
name|tab
index|[
name|off
index|]
operator|=
name|tab
index|[
name|off
operator|+
literal|2
index|]
expr_stmt|;
name|tab
index|[
name|off
operator|+
literal|2
index|]
operator|=
name|tmp
expr_stmt|;
block|}
name|writableRaster
operator|.
name|setPixels
argument_list|(
literal|0
argument_list|,
name|y
argument_list|,
name|width
argument_list|,
literal|1
argument_list|,
name|tab
argument_list|)
expr_stmt|;
block|}
return|return
name|writableRaster
return|;
block|}
comment|// returns the number of channels as a string, or an empty string if there is an error getting the meta data
specifier|private
name|String
name|getNumChannels
parameter_list|(
name|ImageReader
name|reader
parameter_list|)
block|{
try|try
block|{
name|IIOMetadata
name|imageMetadata
init|=
name|reader
operator|.
name|getImageMetadata
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|imageMetadata
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|IIOMetadataNode
name|metaTree
init|=
operator|(
name|IIOMetadataNode
operator|)
name|imageMetadata
operator|.
name|getAsTree
argument_list|(
literal|"javax_imageio_1.0"
argument_list|)
decl_stmt|;
name|Element
name|numChannelsItem
init|=
operator|(
name|Element
operator|)
name|metaTree
operator|.
name|getElementsByTagName
argument_list|(
literal|"NumChannels"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|numChannelsItem
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|numChannelsItem
operator|.
name|getAttribute
argument_list|(
literal|"value"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
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
annotation|@
name|Override
specifier|protected
name|void
name|encode
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|encoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"DCTFilter encoding not implemented, use the JPEGFactory methods instead"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

