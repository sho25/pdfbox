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
name|color
operator|.
name|ICC_ColorSpace
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
name|ColorConvertOp
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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|IIOImage
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
name|ImageTypeSpecifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageWriter
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
name|plugins
operator|.
name|jpeg
operator|.
name|JPEGImageWriteParam
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
name|ImageOutputStream
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
name|filter
operator|.
name|MissingImageReaderException
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
name|PDDeviceCMYK
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
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * Factory for creating a PDImageXObject containing a JPEG compressed image.  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JPEGFactory
block|{
specifier|private
name|JPEGFactory
parameter_list|()
block|{     }
comment|/**      * Creates a new JPEG Image XObject from an input stream containing JPEG data.      *       * The input stream data will be preserved and embedded in the PDF file without modification.      * @param document the document where the image will be created      * @param stream a stream of JPEG data      * @return a new Image XObject      *       * @throws IOException if the input stream cannot be read      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromStream
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromByteArray
argument_list|(
name|document
argument_list|,
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a new JPEG Image XObject from a byte array containing JPEG data.      *      * @param document the document where the image will be created      * @param byteArray bytes of JPEG image      * @return a new Image XObject      *      * @throws IOException if the input stream cannot be read      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromByteArray
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|byte
index|[]
name|byteArray
parameter_list|)
throws|throws
name|IOException
block|{
comment|// copy stream
name|ByteArrayInputStream
name|byteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|byteArray
argument_list|)
decl_stmt|;
comment|// read image
name|BufferedImage
name|awtImage
init|=
name|readJPEG
argument_list|(
name|byteStream
argument_list|)
decl_stmt|;
name|byteStream
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// create Image XObject from stream
name|PDImageXObject
name|pdImage
init|=
operator|new
name|PDImageXObject
argument_list|(
name|document
argument_list|,
name|byteStream
argument_list|,
name|COSName
operator|.
name|DCT_DECODE
argument_list|,
name|awtImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|awtImage
operator|.
name|getHeight
argument_list|()
argument_list|,
name|awtImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|getComponentSize
argument_list|(
literal|0
argument_list|)
argument_list|,
name|getColorSpaceFromAWT
argument_list|(
name|awtImage
argument_list|)
argument_list|)
decl_stmt|;
comment|// no alpha
if|if
condition|(
name|awtImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|hasAlpha
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"alpha channel not implemented"
argument_list|)
throw|;
block|}
return|return
name|pdImage
return|;
block|}
specifier|private
specifier|static
name|BufferedImage
name|readJPEG
parameter_list|(
name|InputStream
name|stream
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
try|try
init|(
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|stream
argument_list|)
init|)
block|{
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
name|ImageIO
operator|.
name|setUseCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|reader
operator|.
name|read
argument_list|(
literal|0
argument_list|)
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
comment|/**      * Creates a new JPEG Image XObject from a Buffered Image.      * @param document the document where the image will be created      * @param image the buffered image to embed      * @return a new Image XObject      * @throws IOException if the JPEG data cannot be written      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|,
literal|0.75f
argument_list|)
return|;
block|}
comment|/**      * Creates a new JPEG Image XObject from a Buffered Image and a given quality.      * The image will be created at 72 DPI.      * @param document the document where the image will be created      * @param image the buffered image to embed      * @param quality the desired JPEG compression quality      * @return a new Image XObject      * @throws IOException if the JPEG data cannot be written      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|float
name|quality
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|,
name|quality
argument_list|,
literal|72
argument_list|)
return|;
block|}
comment|/**      * Creates a new JPEG Image XObject from a Buffered Image, a given quality and DPI.      * @param document the document where the image will be created      * @param image the buffered image to embed      * @param quality the desired JPEG compression quality      * @param dpi the desired DPI (resolution) of the JPEG      * @return a new Image XObject      * @throws IOException if the JPEG data cannot be written      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|float
name|quality
parameter_list|,
name|int
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createJPEG
argument_list|(
name|document
argument_list|,
name|image
argument_list|,
name|quality
argument_list|,
name|dpi
argument_list|)
return|;
block|}
comment|// returns the alpha channel of an image
specifier|private
specifier|static
name|BufferedImage
name|getAlphaImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|hasAlpha
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|image
operator|.
name|getTransparency
argument_list|()
operator|==
name|Transparency
operator|.
name|BITMASK
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"BITMASK Transparency JPEG compression is not"
operator|+
literal|" useful, use LosslessImageFactory instead"
argument_list|)
throw|;
block|}
name|WritableRaster
name|alphaRaster
init|=
name|image
operator|.
name|getAlphaRaster
argument_list|()
decl_stmt|;
if|if
condition|(
name|alphaRaster
operator|==
literal|null
condition|)
block|{
comment|// happens sometimes (PDFBOX-2654) despite colormodel claiming to have alpha
return|return
literal|null
return|;
block|}
name|BufferedImage
name|alphaImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_BYTE_GRAY
argument_list|)
decl_stmt|;
name|alphaImage
operator|.
name|setData
argument_list|(
name|alphaRaster
argument_list|)
expr_stmt|;
return|return
name|alphaImage
return|;
block|}
comment|// Creates an Image XObject from a Buffered Image using JAI Image I/O
specifier|private
specifier|static
name|PDImageXObject
name|createJPEG
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|float
name|quality
parameter_list|,
name|int
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
comment|// extract alpha channel (if any)
name|BufferedImage
name|awtColorImage
init|=
name|getColorImage
argument_list|(
name|image
argument_list|)
decl_stmt|;
name|BufferedImage
name|awtAlphaImage
init|=
name|getAlphaImage
argument_list|(
name|image
argument_list|)
decl_stmt|;
comment|// create XObject
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|encodeImageToJPEGStream
argument_list|(
name|awtColorImage
argument_list|,
name|quality
argument_list|,
name|dpi
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|byteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|PDImageXObject
name|pdImage
init|=
operator|new
name|PDImageXObject
argument_list|(
name|document
argument_list|,
name|byteStream
argument_list|,
name|COSName
operator|.
name|DCT_DECODE
argument_list|,
name|awtColorImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|awtColorImage
operator|.
name|getHeight
argument_list|()
argument_list|,
name|awtColorImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|getComponentSize
argument_list|(
literal|0
argument_list|)
argument_list|,
name|getColorSpaceFromAWT
argument_list|(
name|awtColorImage
argument_list|)
argument_list|)
decl_stmt|;
comment|// alpha -> soft mask
if|if
condition|(
name|awtAlphaImage
operator|!=
literal|null
condition|)
block|{
name|PDImage
name|xAlpha
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|awtAlphaImage
argument_list|,
name|quality
argument_list|)
decl_stmt|;
name|pdImage
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|,
name|xAlpha
argument_list|)
expr_stmt|;
block|}
return|return
name|pdImage
return|;
block|}
specifier|private
specifier|static
name|ImageWriter
name|getJPEGImageWriter
parameter_list|()
throws|throws
name|IOException
block|{
name|ImageWriter
name|writer
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|ImageWriter
argument_list|>
name|writers
init|=
name|ImageIO
operator|.
name|getImageWritersBySuffix
argument_list|(
literal|"jpeg"
argument_list|)
decl_stmt|;
while|while
condition|(
name|writers
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
name|writer
operator|=
name|writers
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
comment|// PDFBOX-3566: avoid CLibJPEGImageWriter, which is not a JPEGImageWriteParam
if|if
condition|(
name|writer
operator|.
name|getDefaultWriteParam
argument_list|()
operator|instanceof
name|JPEGImageWriteParam
condition|)
block|{
return|return
name|writer
return|;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No ImageWriter found for JPEG format"
argument_list|)
throw|;
block|}
specifier|private
specifier|static
name|void
name|encodeImageToJPEGStream
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|float
name|quality
parameter_list|,
name|int
name|dpi
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
comment|// encode to JPEG
name|ImageOutputStream
name|ios
init|=
literal|null
decl_stmt|;
name|ImageWriter
name|imageWriter
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// find JAI writer
name|imageWriter
operator|=
name|getJPEGImageWriter
argument_list|()
expr_stmt|;
name|ios
operator|=
name|ImageIO
operator|.
name|createImageOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|imageWriter
operator|.
name|setOutput
argument_list|(
name|ios
argument_list|)
expr_stmt|;
comment|// add compression
name|JPEGImageWriteParam
name|jpegParam
init|=
operator|(
name|JPEGImageWriteParam
operator|)
name|imageWriter
operator|.
name|getDefaultWriteParam
argument_list|()
decl_stmt|;
name|jpegParam
operator|.
name|setCompressionMode
argument_list|(
name|JPEGImageWriteParam
operator|.
name|MODE_EXPLICIT
argument_list|)
expr_stmt|;
name|jpegParam
operator|.
name|setCompressionQuality
argument_list|(
name|quality
argument_list|)
expr_stmt|;
comment|// add metadata
name|ImageTypeSpecifier
name|imageTypeSpecifier
init|=
operator|new
name|ImageTypeSpecifier
argument_list|(
name|image
argument_list|)
decl_stmt|;
name|IIOMetadata
name|data
init|=
name|imageWriter
operator|.
name|getDefaultImageMetadata
argument_list|(
name|imageTypeSpecifier
argument_list|,
name|jpegParam
argument_list|)
decl_stmt|;
name|Element
name|tree
init|=
operator|(
name|Element
operator|)
name|data
operator|.
name|getAsTree
argument_list|(
literal|"javax_imageio_jpeg_image_1.0"
argument_list|)
decl_stmt|;
name|Element
name|jfif
init|=
operator|(
name|Element
operator|)
name|tree
operator|.
name|getElementsByTagName
argument_list|(
literal|"app0JFIF"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jfif
operator|.
name|setAttribute
argument_list|(
literal|"Xdensity"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|dpi
argument_list|)
argument_list|)
expr_stmt|;
name|jfif
operator|.
name|setAttribute
argument_list|(
literal|"Ydensity"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|dpi
argument_list|)
argument_list|)
expr_stmt|;
name|jfif
operator|.
name|setAttribute
argument_list|(
literal|"resUnits"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// 1 = dots/inch
comment|// write
name|imageWriter
operator|.
name|write
argument_list|(
name|data
argument_list|,
operator|new
name|IIOImage
argument_list|(
name|image
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|jpegParam
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// clean up
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
name|ios
operator|!=
literal|null
condition|)
block|{
name|ios
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|imageWriter
operator|!=
literal|null
condition|)
block|{
name|imageWriter
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// returns a PDColorSpace for a given BufferedImage
specifier|private
specifier|static
name|PDColorSpace
name|getColorSpaceFromAWT
parameter_list|(
name|BufferedImage
name|awtImage
parameter_list|)
block|{
if|if
condition|(
name|awtImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|getNumComponents
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// 256 color (gray) JPEG
return|return
name|PDDeviceGray
operator|.
name|INSTANCE
return|;
block|}
name|ColorSpace
name|awtColorSpace
init|=
name|awtImage
operator|.
name|getColorModel
argument_list|()
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|awtColorSpace
operator|instanceof
name|ICC_ColorSpace
operator|&&
operator|!
name|awtColorSpace
operator|.
name|isCS_sRGB
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ICC color spaces not implemented"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|awtColorSpace
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ColorSpace
operator|.
name|TYPE_RGB
case|:
return|return
name|PDDeviceRGB
operator|.
name|INSTANCE
return|;
case|case
name|ColorSpace
operator|.
name|TYPE_GRAY
case|:
return|return
name|PDDeviceGray
operator|.
name|INSTANCE
return|;
case|case
name|ColorSpace
operator|.
name|TYPE_CMYK
case|:
return|return
name|PDDeviceCMYK
operator|.
name|INSTANCE
return|;
default|default:
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"color space not implemented: "
operator|+
name|awtColorSpace
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// returns the color channels of an image
specifier|private
specifier|static
name|BufferedImage
name|getColorImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|)
block|{
if|if
condition|(
operator|!
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|hasAlpha
argument_list|()
condition|)
block|{
return|return
name|image
return|;
block|}
if|if
condition|(
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getType
argument_list|()
operator|!=
name|ColorSpace
operator|.
name|TYPE_RGB
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"only RGB color spaces are implemented"
argument_list|)
throw|;
block|}
comment|// create an RGB image without alpha
comment|//BEWARE: the previous solution in the history
comment|// g.setComposite(AlphaComposite.Src) and g.drawImage()
comment|// didn't work properly for TYPE_4BYTE_ABGR.
comment|// alpha values of 0 result in a black dest pixel!!!
name|BufferedImage
name|rgbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_3BYTE_BGR
argument_list|)
decl_stmt|;
return|return
operator|new
name|ColorConvertOp
argument_list|(
literal|null
argument_list|)
operator|.
name|filter
argument_list|(
name|image
argument_list|,
name|rgbImage
argument_list|)
return|;
block|}
block|}
end_class

end_unit

