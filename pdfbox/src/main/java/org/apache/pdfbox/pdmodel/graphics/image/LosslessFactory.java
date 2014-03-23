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

begin_import
import|import static
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
operator|.
name|ImageFactory
operator|.
name|getColorImage
import|;
end_import

begin_comment
comment|/**  * Factory for creating a PDImageXObject containing a lossless compressed image.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|LosslessFactory
block|{
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
comment|// extract color channel
name|BufferedImage
name|awtColorImage
init|=
name|getColorImage
argument_list|(
name|image
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
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
operator|||
name|image
operator|.
name|getType
argument_list|()
operator|==
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
operator|)
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
name|int
name|h
init|=
name|awtColorImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|w
init|=
name|awtColorImage
operator|.
name|getWidth
argument_list|()
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
init|;
name|x
operator|<
name|w
condition|;
operator|++
name|x
control|)
block|{
name|mcios
operator|.
name|writeBits
argument_list|(
name|awtColorImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|,
name|bpc
argument_list|)
expr_stmt|;
block|}
block|}
name|mcios
operator|.
name|writeBits
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|)
expr_stmt|;
comment|// padding
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
name|int
name|h
init|=
name|awtColorImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|w
init|=
name|awtColorImage
operator|.
name|getWidth
argument_list|()
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
init|;
name|x
operator|<
name|w
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
name|awtColorImage
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
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
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
name|ByteArrayOutputStream
name|bos2
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|filter
operator|.
name|encode
argument_list|(
name|bais
argument_list|,
name|bos2
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
name|bos2
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
name|filteredByteStream
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
name|pdImage
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
name|dict
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
name|pdImage
operator|.
name|setColorSpace
argument_list|(
name|deviceColorSpace
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setBitsPerComponent
argument_list|(
name|bpc
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setHeight
argument_list|(
name|awtColorImage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setWidth
argument_list|(
name|awtColorImage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
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
name|dict
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
comment|/**      * Creates a grayscale PDImageXObject from the alpha channel of an image.      *      * @param document the document where the image will be created.      * @param image an ARGB image.      *      * @return the alpha channel of an image as a grayscale image.      *      * @throws IOException if something goes wrong      */
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
comment|//TODO: optimize this to lessen the memory footprint.
comment|// possible idea? Derive an inputStream that reads from the raster.
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
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
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
name|ByteArrayOutputStream
name|bos2
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|filter
operator|.
name|encode
argument_list|(
name|bais
argument_list|,
name|bos2
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
name|bos2
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
name|filteredByteStream
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
name|pdImage
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
name|dict
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
name|pdImage
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setHeight
argument_list|(
name|image
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setWidth
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pdImage
return|;
block|}
block|}
end_class

end_unit

