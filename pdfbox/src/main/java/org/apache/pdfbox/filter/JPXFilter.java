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
name|DataBufferUShort
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
name|IndexColorModel
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
name|MultiPixelPackedSampleModel
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
name|javax
operator|.
name|imageio
operator|.
name|ImageReadParam
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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDJPXColorSpace
import|;
end_import

begin_comment
comment|/**  * Decompress data encoded using the wavelet-based JPEG 2000 standard,  * reproducing the original data.  *  * Requires the Java Advanced Imaging (JAI) Image I/O Tools to be installed from java.net, see  *<a href="http://download.java.net/media/jai-imageio/builds/release/1.1/">jai-imageio</a>.  * Alternatively you can build from the source available in the  *<a href="https://java.net/projects/jai-imageio-core/">jai-imageio-core svn repo</a>.  *  * Mac OS X users should download the tar.gz file for linux and unpack it to obtain the  * required jar files. The .so file can be safely ignored.  *  * @author John Hewson  * @author Timo Boehme  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JPXFilter
extends|extends
name|Filter
block|{
comment|/**      * {@inheritDoc}      */
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
parameter_list|,
name|DecodeOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|DecodeResult
name|result
init|=
operator|new
name|DecodeResult
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|getParameters
argument_list|()
operator|.
name|addAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
init|=
name|readJPX
argument_list|(
name|encoded
argument_list|,
name|options
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|Raster
name|raster
init|=
name|image
operator|.
name|getRaster
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|.
name|getDataType
argument_list|()
condition|)
block|{
case|case
name|DataBuffer
operator|.
name|TYPE_BYTE
case|:
name|DataBufferByte
name|byteBuffer
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
name|byteBuffer
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
case|case
name|DataBuffer
operator|.
name|TYPE_USHORT
case|:
name|DataBufferUShort
name|wordBuffer
init|=
operator|(
name|DataBufferUShort
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|short
name|w
range|:
name|wordBuffer
operator|.
name|getData
argument_list|()
control|)
block|{
name|decoded
operator|.
name|write
argument_list|(
name|w
operator|>>
literal|8
argument_list|)
expr_stmt|;
name|decoded
operator|.
name|write
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
case|case
name|DataBuffer
operator|.
name|TYPE_INT
case|:
comment|// not yet used (as of October 2018) but works as fallback
comment|// if we decide to convert to BufferedImage.TYPE_INT_RGB
name|int
index|[]
name|ar
init|=
operator|new
name|int
index|[
name|raster
operator|.
name|getNumBands
argument_list|()
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
name|image
operator|.
name|getHeight
argument_list|()
condition|;
operator|++
name|y
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
name|image
operator|.
name|getWidth
argument_list|()
condition|;
operator|++
name|x
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
name|ar
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ar
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|decoded
operator|.
name|write
argument_list|(
name|ar
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Data type "
operator|+
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|.
name|getDataType
argument_list|()
operator|+
literal|" not implemented"
argument_list|)
throw|;
block|}
block|}
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
return|return
name|decode
argument_list|(
name|encoded
argument_list|,
name|decoded
argument_list|,
name|parameters
argument_list|,
name|index
argument_list|,
name|DecodeOptions
operator|.
name|DEFAULT
argument_list|)
return|;
block|}
comment|// try to read using JAI Image I/O
specifier|private
name|BufferedImage
name|readJPX
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|DecodeOptions
name|options
parameter_list|,
name|DecodeResult
name|result
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageReader
name|reader
init|=
name|findImageReader
argument_list|(
literal|"JPEG2000"
argument_list|,
literal|"Java Advanced Imaging (JAI) Image I/O Tools are not installed"
argument_list|)
decl_stmt|;
comment|// PDFBOX-4121: ImageIO.createImageInputStream() is much slower
try|try
init|(
name|ImageInputStream
name|iis
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|input
argument_list|)
init|)
block|{
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ImageReadParam
name|irp
init|=
name|reader
operator|.
name|getDefaultReadParam
argument_list|()
decl_stmt|;
name|irp
operator|.
name|setSourceRegion
argument_list|(
name|options
operator|.
name|getSourceRegion
argument_list|()
argument_list|)
expr_stmt|;
name|irp
operator|.
name|setSourceSubsampling
argument_list|(
name|options
operator|.
name|getSubsamplingX
argument_list|()
argument_list|,
name|options
operator|.
name|getSubsamplingY
argument_list|()
argument_list|,
name|options
operator|.
name|getSubsamplingOffsetX
argument_list|()
argument_list|,
name|options
operator|.
name|getSubsamplingOffsetY
argument_list|()
argument_list|)
expr_stmt|;
name|options
operator|.
name|setFilterSubsampled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
decl_stmt|;
try|try
block|{
name|image
operator|=
name|reader
operator|.
name|read
argument_list|(
literal|0
argument_list|,
name|irp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// wrap and rethrow any exceptions
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not read JPEG 2000 (JPX) image"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|COSDictionary
name|parameters
init|=
name|result
operator|.
name|getParameters
argument_list|()
decl_stmt|;
comment|// "If the image stream uses the JPXDecode filter, this entry is optional
comment|// and shall be ignored if present"
comment|//
comment|// note that indexed color spaces make the BPC logic tricky, see PDFBOX-2204
name|int
name|bpc
init|=
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
operator|/
name|image
operator|.
name|getRaster
argument_list|()
operator|.
name|getNumBands
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
name|bpc
argument_list|)
expr_stmt|;
comment|// "Decode shall be ignored, except in the case where the image is treated as a mask"
if|if
condition|(
operator|!
name|parameters
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|IMAGE_MASK
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// override dimensions, see PDFBOX-1735
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|WIDTH
argument_list|,
name|reader
operator|.
name|getWidth
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|HEIGHT
argument_list|,
name|reader
operator|.
name|getHeight
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// extract embedded color space
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|)
condition|)
block|{
if|if
condition|(
name|image
operator|.
name|getSampleModel
argument_list|()
operator|instanceof
name|MultiPixelPackedSampleModel
operator|&&
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
operator|==
literal|1
operator|&&
name|image
operator|.
name|getRaster
argument_list|()
operator|.
name|getNumBands
argument_list|()
operator|==
literal|1
operator|&&
name|image
operator|.
name|getColorModel
argument_list|()
operator|instanceof
name|IndexColorModel
condition|)
block|{
comment|// PDFBOX-4326:
comment|// force CS_GRAY colorspace because colorspace in IndexColorModel
comment|// has 3 colors despite that there is only 1 color per pixel
comment|// in raster
name|result
operator|.
name|setColorSpace
argument_list|(
operator|new
name|PDJPXColorSpace
argument_list|(
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_GRAY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|setColorSpace
argument_list|(
operator|new
name|PDJPXColorSpace
argument_list|(
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getColorSpace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|image
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
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
literal|"JPX encoding not implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

