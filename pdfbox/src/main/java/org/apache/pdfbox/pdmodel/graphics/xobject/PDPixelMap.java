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
name|xobject
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|AlphaComposite
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
name|OutputStream
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
name|pdmodel
operator|.
name|PDDocument
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
name|PDStream
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
name|PDDeviceGray
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
name|PDICCBased
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
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * This class contains a PixelMap Image.  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author mathiak  * @version $Revision: 1.10 $  */
end_comment

begin_class
specifier|public
class|class
name|PDPixelMap
extends|extends
name|PDXObjectImage
block|{
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
name|PDPixelMap
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BufferedImage
name|image
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PNG
init|=
literal|"png"
decl_stmt|;
comment|/**      * Standard constructor. Basically does nothing.      * @param pdStream The stream that holds the pixel map.      */
specifier|public
name|PDPixelMap
parameter_list|(
name|PDStream
name|pdStream
parameter_list|)
block|{
name|super
argument_list|(
name|pdStream
argument_list|,
name|PNG
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a pixel map image from an AWT image.      *       *       * @param doc The PDF document to embed the image in.      * @param bi The image to read data from.      *      * @throws IOException If there is an error while embedding this image.      */
specifier|public
name|PDPixelMap
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|BufferedImage
name|bi
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|doc
argument_list|,
name|PNG
argument_list|)
expr_stmt|;
name|createImageStream
argument_list|(
name|doc
argument_list|,
name|bi
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createImageStream
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|BufferedImage
name|bi
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|alphaImage
init|=
literal|null
decl_stmt|;
name|BufferedImage
name|rgbImage
init|=
literal|null
decl_stmt|;
name|int
name|width
init|=
name|bi
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|bi
operator|.
name|getHeight
argument_list|()
decl_stmt|;
if|if
condition|(
name|bi
operator|.
name|getColorModel
argument_list|()
operator|.
name|hasAlpha
argument_list|()
condition|)
block|{
comment|// extract the alpha information
name|WritableRaster
name|alphaRaster
init|=
name|bi
operator|.
name|getAlphaRaster
argument_list|()
decl_stmt|;
name|ColorModel
name|cm
init|=
operator|new
name|ComponentColorModel
argument_list|(
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_GRAY
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|OPAQUE
argument_list|,
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
decl_stmt|;
name|alphaImage
operator|=
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|alphaRaster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// create a RGB image without alpha
name|rgbImage
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
name|TYPE_3BYTE_BGR
argument_list|)
expr_stmt|;
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
name|setComposite
argument_list|(
name|AlphaComposite
operator|.
name|Src
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|bi
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rgbImage
operator|=
name|bi
expr_stmt|;
block|}
name|java
operator|.
name|io
operator|.
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
try|try
block|{
name|int
name|numberOfComponents
init|=
name|rgbImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|getNumComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|numberOfComponents
operator|==
literal|3
condition|)
block|{
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|numberOfComponents
operator|==
literal|1
condition|)
block|{
name|setColorSpace
argument_list|(
operator|new
name|PDDeviceGray
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
name|byte
index|[]
name|outData
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|numberOfComponents
index|]
decl_stmt|;
name|rgbImage
operator|.
name|getData
argument_list|()
operator|.
name|getDataElements
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
name|outData
argument_list|)
expr_stmt|;
comment|// add FlateDecode compression
name|getPDStream
argument_list|()
operator|.
name|addCompression
argument_list|()
expr_stmt|;
name|os
operator|=
name|getCOSStream
argument_list|()
operator|.
name|createUnfilteredStream
argument_list|()
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|outData
argument_list|)
expr_stmt|;
name|COSDictionary
name|dic
init|=
name|getCOSStream
argument_list|()
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
expr_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|IMAGE
argument_list|)
expr_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XOBJECT
argument_list|)
expr_stmt|;
if|if
condition|(
name|alphaImage
operator|!=
literal|null
condition|)
block|{
name|PDPixelMap
name|smask
init|=
operator|new
name|PDPixelMap
argument_list|(
name|doc
argument_list|,
name|alphaImage
argument_list|)
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|,
name|smask
argument_list|)
expr_stmt|;
block|}
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns a {@link java.awt.image.BufferedImage} of the COSStream      * set in the constructor or null if the COSStream could not be encoded.      *      * @return {@inheritDoc}      *      * @throws IOException {@inheritDoc}      */
specifier|public
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
return|return
name|image
return|;
block|}
try|try
block|{
name|int
name|width
init|=
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|bpc
init|=
name|getBitsPerComponent
argument_list|()
decl_stmt|;
name|byte
index|[]
name|array
init|=
name|getPDStream
argument_list|()
operator|.
name|getByteArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Something went wrong ... the pixelmap doesn't contain any data."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// Get the ColorModel right
name|PDColorSpace
name|colorspace
init|=
name|getColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|colorspace
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"getColorSpace() returned NULL.  Predictor = "
operator|+
name|getPredictor
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|ColorModel
name|cm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorspace
operator|instanceof
name|PDIndexed
condition|)
block|{
name|PDIndexed
name|csIndexed
init|=
operator|(
name|PDIndexed
operator|)
name|colorspace
decl_stmt|;
comment|// the base color space uses 8 bit per component, as the indexed color values
comment|// of an indexed color space are always in a range from 0 to 255
name|ColorModel
name|baseColorModel
init|=
name|csIndexed
operator|.
name|getBaseColorSpace
argument_list|()
operator|.
name|createColorModel
argument_list|(
literal|8
argument_list|)
decl_stmt|;
comment|// number of possible color values in the target color space
name|int
name|numberOfColorValues
init|=
literal|1
operator|<<
name|bpc
decl_stmt|;
comment|// number of indexed color values
name|int
name|highValue
init|=
name|csIndexed
operator|.
name|getHighValue
argument_list|()
decl_stmt|;
comment|// choose the correct size, sometimes there are more indexed values than needed
comment|// and sometimes there are fewer indexed value than possible
name|int
name|size
init|=
name|Math
operator|.
name|min
argument_list|(
name|numberOfColorValues
operator|-
literal|1
argument_list|,
name|highValue
argument_list|)
decl_stmt|;
name|byte
index|[]
name|index
init|=
name|csIndexed
operator|.
name|getLookupData
argument_list|()
decl_stmt|;
name|boolean
name|hasAlpha
init|=
name|baseColorModel
operator|.
name|hasAlpha
argument_list|()
decl_stmt|;
name|COSBase
name|maskArray
init|=
name|getMask
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseColorModel
operator|.
name|getTransferType
argument_list|()
operator|!=
name|DataBuffer
operator|.
name|TYPE_BYTE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented"
argument_list|)
throw|;
block|}
comment|// the IndexColorModel uses RGB-based color values
comment|// which leads to 3 color components and a optional alpha channel
name|int
name|numberOfComponents
init|=
literal|3
operator|+
operator|(
name|hasAlpha
condition|?
literal|1
else|:
literal|0
operator|)
decl_stmt|;
name|int
name|buffersize
init|=
operator|(
name|size
operator|+
literal|1
operator|)
operator|*
name|numberOfComponents
decl_stmt|;
name|byte
index|[]
name|colorValues
init|=
operator|new
name|byte
index|[
name|buffersize
index|]
decl_stmt|;
name|byte
index|[]
name|inData
init|=
operator|new
name|byte
index|[
name|baseColorModel
operator|.
name|getNumComponents
argument_list|()
index|]
decl_stmt|;
name|int
name|bufferIndex
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|size
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|index
argument_list|,
name|i
operator|*
name|inData
operator|.
name|length
argument_list|,
name|inData
argument_list|,
literal|0
argument_list|,
name|inData
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// convert the indexed color values to RGB
name|colorValues
index|[
name|bufferIndex
index|]
operator|=
operator|(
name|byte
operator|)
name|baseColorModel
operator|.
name|getRed
argument_list|(
name|inData
argument_list|)
expr_stmt|;
name|colorValues
index|[
name|bufferIndex
operator|+
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
name|baseColorModel
operator|.
name|getGreen
argument_list|(
name|inData
argument_list|)
expr_stmt|;
name|colorValues
index|[
name|bufferIndex
operator|+
literal|2
index|]
operator|=
operator|(
name|byte
operator|)
name|baseColorModel
operator|.
name|getBlue
argument_list|(
name|inData
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasAlpha
condition|)
block|{
name|colorValues
index|[
name|bufferIndex
operator|+
literal|3
index|]
operator|=
operator|(
name|byte
operator|)
name|baseColorModel
operator|.
name|getAlpha
argument_list|(
name|inData
argument_list|)
expr_stmt|;
block|}
name|bufferIndex
operator|+=
name|numberOfComponents
expr_stmt|;
block|}
if|if
condition|(
name|maskArray
operator|!=
literal|null
operator|&&
name|maskArray
operator|instanceof
name|COSArray
condition|)
block|{
name|cm
operator|=
operator|new
name|IndexColorModel
argument_list|(
name|bpc
argument_list|,
name|size
operator|+
literal|1
argument_list|,
name|colorValues
argument_list|,
literal|0
argument_list|,
name|hasAlpha
argument_list|,
operator|(
operator|(
name|COSArray
operator|)
name|maskArray
operator|)
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cm
operator|=
operator|new
name|IndexColorModel
argument_list|(
name|bpc
argument_list|,
name|size
operator|+
literal|1
argument_list|,
name|colorValues
argument_list|,
literal|0
argument_list|,
name|hasAlpha
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|colorspace
operator|instanceof
name|PDSeparation
condition|)
block|{
name|PDSeparation
name|csSeparation
init|=
operator|(
name|PDSeparation
operator|)
name|colorspace
decl_stmt|;
name|int
name|numberOfComponents
init|=
name|csSeparation
operator|.
name|getAlternateColorSpace
argument_list|()
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|PDFunction
name|tintTransformFunc
init|=
name|csSeparation
operator|.
name|getTintTransform
argument_list|()
decl_stmt|;
name|COSArray
name|decode
init|=
name|getDecode
argument_list|()
decl_stmt|;
comment|// we have to invert the tint-values,
comment|// if the Decode array exists and consists of (1,0)
name|boolean
name|invert
init|=
name|decode
operator|!=
literal|null
operator|&&
name|decode
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
operator|==
literal|1
decl_stmt|;
comment|// TODO add interpolation for other decode values then 1,0
name|int
name|maxValue
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bpc
argument_list|)
operator|-
literal|1
decl_stmt|;
comment|// destination array
name|byte
index|[]
name|mappedData
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|numberOfComponents
index|]
decl_stmt|;
name|int
name|rowLength
init|=
name|width
operator|*
name|numberOfComponents
decl_stmt|;
name|float
index|[]
name|input
init|=
operator|new
name|float
index|[
literal|1
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
name|height
condition|;
name|i
operator|++
control|)
block|{
name|int
name|rowOffset
init|=
name|i
operator|*
name|rowLength
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
name|width
condition|;
name|j
operator|++
control|)
block|{
comment|// scale tint values to a range of 0...1
name|int
name|value
init|=
operator|(
name|array
index|[
name|i
operator|*
name|width
operator|+
name|j
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
decl_stmt|;
if|if
condition|(
name|invert
condition|)
block|{
name|input
index|[
literal|0
index|]
operator|=
literal|1
operator|-
operator|(
name|value
operator|/
name|maxValue
operator|)
expr_stmt|;
block|}
else|else
block|{
name|input
index|[
literal|0
index|]
operator|=
name|value
operator|/
name|maxValue
expr_stmt|;
block|}
name|float
index|[]
name|mappedColor
init|=
name|tintTransformFunc
operator|.
name|eval
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|int
name|columnOffset
init|=
name|j
operator|*
name|numberOfComponents
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|numberOfComponents
condition|;
name|k
operator|++
control|)
block|{
comment|// redo scaling for every single color value
name|float
name|mappedValue
init|=
name|mappedColor
index|[
name|k
index|]
decl_stmt|;
name|mappedData
index|[
name|rowOffset
operator|+
name|columnOffset
operator|+
name|k
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|mappedValue
operator|*
name|maxValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|array
operator|=
name|mappedData
expr_stmt|;
name|cm
operator|=
name|colorspace
operator|.
name|createColorModel
argument_list|(
name|bpc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bpc
operator|==
literal|1
condition|)
block|{
name|byte
index|[]
name|map
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorspace
operator|instanceof
name|PDDeviceGray
condition|)
block|{
name|COSArray
name|decode
init|=
name|getDecode
argument_list|()
decl_stmt|;
comment|// we have to invert the b/w-values,
comment|// if the Decode array exists and consists of (1,0)
if|if
condition|(
name|decode
operator|!=
literal|null
operator|&&
name|decode
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
operator|==
literal|1
condition|)
block|{
name|map
operator|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xff
block|}
expr_stmt|;
block|}
else|else
block|{
name|map
operator|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0xff
block|}
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|colorspace
operator|instanceof
name|PDICCBased
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|PDICCBased
operator|)
name|colorspace
operator|)
operator|.
name|getNumberOfComponents
argument_list|()
operator|==
literal|1
condition|)
block|{
name|map
operator|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xff
block|}
expr_stmt|;
block|}
else|else
block|{
name|map
operator|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0xff
block|}
expr_stmt|;
block|}
block|}
else|else
block|{
name|map
operator|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0xff
block|}
expr_stmt|;
block|}
name|cm
operator|=
operator|new
name|IndexColorModel
argument_list|(
name|bpc
argument_list|,
name|map
operator|.
name|length
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
name|Transparency
operator|.
name|OPAQUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|colorspace
operator|instanceof
name|PDICCBased
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|PDICCBased
operator|)
name|colorspace
operator|)
operator|.
name|getNumberOfComponents
argument_list|()
operator|==
literal|1
condition|)
block|{
name|byte
index|[]
name|map
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xff
block|}
decl_stmt|;
name|cm
operator|=
operator|new
name|IndexColorModel
argument_list|(
name|bpc
argument_list|,
literal|1
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
name|Transparency
operator|.
name|OPAQUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cm
operator|=
name|colorspace
operator|.
name|createColorModel
argument_list|(
name|bpc
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cm
operator|=
name|colorspace
operator|.
name|createColorModel
argument_list|(
name|bpc
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"ColorModel: "
operator|+
name|cm
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|WritableRaster
name|raster
init|=
name|cm
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|width
argument_list|,
name|height
argument_list|)
decl_stmt|;
name|DataBufferByte
name|buffer
init|=
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bufferData
init|=
name|buffer
operator|.
name|getData
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|bufferData
argument_list|,
literal|0
argument_list|,
operator|(
name|array
operator|.
name|length
operator|<
name|bufferData
operator|.
name|length
condition|?
name|array
operator|.
name|length
else|:
name|bufferData
operator|.
name|length
operator|)
argument_list|)
expr_stmt|;
name|image
operator|=
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|raster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|applyMasks
argument_list|(
name|image
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|exception
argument_list|,
name|exception
argument_list|)
expr_stmt|;
comment|//A NULL return is caught in pagedrawer.Invoke.process() so don't re-throw.
comment|//Returning the NULL falls through to Phillip Koch's TODO section.
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Writes the image as .png.      *      * {@inheritDoc}      */
specifier|public
name|void
name|write2OutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|getRGBImage
argument_list|()
expr_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|PNG
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * DecodeParms is an optional parameter for filters.      *      * It is provided if any of the filters has nondefault parameters. If there      * is only one filter it is a dictionary, if there are multiple filters it      * is an array with an entry for each filter. An array entry can hold a null      * value if only the default values are used or a dictionary with      * parameters.      *      * @return The decoding parameters.      *      */
specifier|public
name|COSDictionary
name|getDecodeParams
parameter_list|()
block|{
name|COSBase
name|decodeParms
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DECODE_PARMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|decodeParms
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|decodeParms
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|decodeParms
return|;
block|}
elseif|else
if|if
condition|(
name|decodeParms
operator|instanceof
name|COSArray
condition|)
block|{
comment|// not implemented yet, which index should we use?
return|return
literal|null
return|;
comment|//(COSDictionary)((COSArray)decodeParms).get(0);
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A code that selects the predictor algorithm.      *      *<ul>      *<li>1 No prediction (the default value)      *<li>2 TIFF Predictor 2      *<li>10 PNG prediction (on encoding, PNG None on all rows)      *<li>11 PNG prediction (on encoding, PNG Sub on all rows)      *<li>12 PNG prediction (on encoding, PNG Up on all rows)      *<li>13 PNG prediction (on encoding, PNG Average on all rows)      *<li>14 PNG prediction (on encoding, PNG Path on all rows)      *<li>15 PNG prediction (on encoding, PNG optimum)      *</ul>      *      * Default value: 1.      *      * @return predictor algorithm code      */
specifier|public
name|int
name|getPredictor
parameter_list|()
block|{
name|COSDictionary
name|decodeParms
init|=
name|getDecodeParams
argument_list|()
decl_stmt|;
if|if
condition|(
name|decodeParms
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|PREDICTOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

