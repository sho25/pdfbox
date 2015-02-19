begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Color
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
name|IOException
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
name|MemoryCacheImageOutputStream
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
name|filter
operator|.
name|Filter
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
name|FilterFactory
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
name|PDDeviceColorSpace
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

begin_comment
comment|/**  * Factory for creating a PDImageXObject containing a lossless compressed image.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|LosslessFactory
block|{
specifier|private
name|LosslessFactory
parameter_list|()
block|{     }
comment|/**      * Creates a new lossless encoded Image XObject from a Buffered Image.      *      * @param document the document where the image will be created      * @param image the buffered image to embed      * @return a new Image XObject      * @throws IOException if something goes wrong      */
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
name|int
name|bpc
decl_stmt|;
name|PDDeviceColorSpace
name|deviceColorSpace
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
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
name|int
name|width
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|image
operator|.
name|getType
argument_list|()
operator|==
name|BufferedImage
operator|.
name|TYPE_BYTE_GRAY
operator|&&
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
operator|<=
literal|8
operator|)
operator|||
operator|(
name|image
operator|.
name|getType
argument_list|()
operator|==
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
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
operator|)
condition|)
block|{
name|MemoryCacheImageOutputStream
name|mcios
init|=
operator|new
name|MemoryCacheImageOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
comment|// grayscale images need one color per sample
name|bpc
operator|=
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
expr_stmt|;
name|deviceColorSpace
operator|=
name|PDDeviceGray
operator|.
name|INSTANCE
expr_stmt|;
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
name|width
condition|;
operator|++
name|x
control|)
block|{
name|mcios
operator|.
name|writeBits
argument_list|(
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFF
argument_list|,
name|bpc
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|mcios
operator|.
name|getBitOffset
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|mcios
operator|.
name|writeBit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|mcios
operator|.
name|flush
argument_list|()
expr_stmt|;
name|mcios
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// RGB
name|bpc
operator|=
literal|8
expr_stmt|;
name|deviceColorSpace
operator|=
name|PDDeviceRGB
operator|.
name|INSTANCE
expr_stmt|;
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
name|width
condition|;
operator|++
name|x
control|)
block|{
name|Color
name|color
init|=
operator|new
name|Color
argument_list|(
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
decl_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|color
operator|.
name|getRed
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|color
operator|.
name|getGreen
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|color
operator|.
name|getBlue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|PDImageXObject
name|pdImage
init|=
name|prepareImageXObject
argument_list|(
name|document
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
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
name|bpc
argument_list|,
name|deviceColorSpace
argument_list|)
decl_stmt|;
comment|// alpha -> soft mask
name|PDImage
name|xAlpha
init|=
name|createAlphaFromARGBImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
decl_stmt|;
if|if
condition|(
name|xAlpha
operator|!=
literal|null
condition|)
block|{
name|pdImage
operator|.
name|getCOSStream
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
comment|/**      * Creates a grayscale Flate encoded PDImageXObject from the alpha channel      * of an image.      *      * @param document the document where the image will be created.      * @param image an ARGB image.      *      * @return the alpha channel of an image as a grayscale image.      *      * @throws IOException if something goes wrong      */
specifier|private
specifier|static
name|PDImageXObject
name|createAlphaFromARGBImage
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
comment|// this implementation makes the assumption that the raster uses
comment|// SinglePixelPackedSampleModel, i.e. the values can be used 1:1 for
comment|// the stream.
comment|// Sadly the type of the databuffer is TYPE_INT and not TYPE_BYTE.
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
comment|// extract the alpha information
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
name|createAlphaFromARGBImage2
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
return|;
block|}
name|int
index|[]
name|pixels
init|=
name|alphaRaster
operator|.
name|getPixels
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|alphaRaster
operator|.
name|getSampleModel
argument_list|()
operator|.
name|getWidth
argument_list|()
argument_list|,
name|alphaRaster
operator|.
name|getSampleModel
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|,
operator|(
name|int
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|bpc
decl_stmt|;
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
name|bpc
operator|=
literal|1
expr_stmt|;
name|MemoryCacheImageOutputStream
name|mcios
init|=
operator|new
name|MemoryCacheImageOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|int
name|width
init|=
name|alphaRaster
operator|.
name|getSampleModel
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|p
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|pixel
range|:
name|pixels
control|)
block|{
name|mcios
operator|.
name|writeBit
argument_list|(
name|pixel
argument_list|)
expr_stmt|;
operator|++
name|p
expr_stmt|;
if|if
condition|(
name|p
operator|%
name|width
operator|==
literal|0
condition|)
block|{
while|while
condition|(
name|mcios
operator|.
name|getBitOffset
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|mcios
operator|.
name|writeBit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|mcios
operator|.
name|flush
argument_list|()
expr_stmt|;
name|mcios
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bpc
operator|=
literal|8
expr_stmt|;
for|for
control|(
name|int
name|pixel
range|:
name|pixels
control|)
block|{
name|bos
operator|.
name|write
argument_list|(
name|pixel
argument_list|)
expr_stmt|;
block|}
block|}
name|PDImageXObject
name|pdImage
init|=
name|prepareImageXObject
argument_list|(
name|document
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
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
name|bpc
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
return|return
name|pdImage
return|;
block|}
comment|// create alpha image the hard way: get the alpha through getRGB()
specifier|private
specifier|static
name|PDImageXObject
name|createAlphaFromARGBImage2
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|bi
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|bpc
decl_stmt|;
if|if
condition|(
name|bi
operator|.
name|getTransparency
argument_list|()
operator|==
name|Transparency
operator|.
name|BITMASK
condition|)
block|{
name|bpc
operator|=
literal|1
expr_stmt|;
name|MemoryCacheImageOutputStream
name|mcios
init|=
operator|new
name|MemoryCacheImageOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|,
name|h
init|=
name|bi
operator|.
name|getHeight
argument_list|()
init|;
name|y
operator|<
name|h
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
init|,
name|w
init|=
name|bi
operator|.
name|getWidth
argument_list|()
init|;
name|x
operator|<
name|w
condition|;
operator|++
name|x
control|)
block|{
name|int
name|alpha
init|=
name|bi
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|>>>
literal|24
decl_stmt|;
name|mcios
operator|.
name|writeBit
argument_list|(
name|alpha
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|mcios
operator|.
name|getBitOffset
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|mcios
operator|.
name|writeBit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|mcios
operator|.
name|flush
argument_list|()
expr_stmt|;
name|mcios
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bpc
operator|=
literal|8
expr_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|,
name|h
init|=
name|bi
operator|.
name|getHeight
argument_list|()
init|;
name|y
operator|<
name|h
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
init|,
name|w
init|=
name|bi
operator|.
name|getWidth
argument_list|()
init|;
name|x
operator|<
name|w
condition|;
operator|++
name|x
control|)
block|{
name|int
name|alpha
init|=
name|bi
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|>>>
literal|24
decl_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|alpha
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|PDImageXObject
name|pdImage
init|=
name|prepareImageXObject
argument_list|(
name|document
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|bi
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bi
operator|.
name|getHeight
argument_list|()
argument_list|,
name|bpc
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
return|return
name|pdImage
return|;
block|}
comment|/**      * Create a PDImageXObject while making a decision whether not to       * compress, use Flate filter only, or Flate and LZW filters.      *       * @param document The document.      * @param byteArray array with data.      * @param width the image width      * @param height the image height      * @param bitsPerComponent the bits per component      * @param initColorSpace the color space      * @return the newly created PDImageXObject with the data compressed.      * @throws IOException       */
specifier|private
specifier|static
name|PDImageXObject
name|prepareImageXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|byte
index|[]
name|byteArray
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|int
name|bitsPerComponent
parameter_list|,
name|PDColorSpace
name|initColorSpace
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|filter
operator|.
name|encode
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|byteArray
argument_list|)
argument_list|,
name|baos
argument_list|,
operator|new
name|COSDictionary
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|filteredByteStream
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
return|return
operator|new
name|PDImageXObject
argument_list|(
name|document
argument_list|,
name|filteredByteStream
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
name|bitsPerComponent
argument_list|,
name|initColorSpace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

