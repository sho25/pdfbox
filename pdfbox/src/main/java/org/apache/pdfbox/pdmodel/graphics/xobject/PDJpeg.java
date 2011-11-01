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
name|util
operator|.
name|ArrayList
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
name|Locale
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
name|IIOException
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
name|ImageWriteParam
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
name|PDSeparation
import|;
end_import

begin_comment
comment|/**  * An image class for JPegs.  *  * @author mathiak  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDJpeg
extends|extends
name|PDXObjectImage
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JPG
init|=
literal|"jpg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DCT_FILTERS
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|float
name|DEFAULT_COMPRESSION_LEVEL
init|=
literal|0.75f
decl_stmt|;
static|static
block|{
name|DCT_FILTERS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DCT_FILTERS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DCT_DECODE_ABBREVIATION
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constructor.      *      * @param jpeg The COSStream from which to extract the JPeg      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDStream
name|jpeg
parameter_list|)
block|{
name|super
argument_list|(
name|jpeg
argument_list|,
name|JPG
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct from a stream.      *      * @param doc The document to create the image as part of.      * @param is The stream that contains the jpeg data.      * @throws IOException If there is an error reading the jpeg data.      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|is
argument_list|,
literal|true
argument_list|)
argument_list|,
name|JPG
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
name|DCT_DECODE
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
name|BufferedImage
name|image
init|=
name|getRGBImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
name|image
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Construct from a buffered image.      * The default compression level of 0.75 will be used.      *      * @param doc The document to create the image as part of.      * @param bi The image to convert to a jpeg      * @throws IOException If there is an error processing the jpeg data.      */
specifier|public
name|PDJpeg
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
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
argument_list|,
name|JPG
argument_list|)
expr_stmt|;
name|createImageStream
argument_list|(
name|doc
argument_list|,
name|bi
argument_list|,
name|DEFAULT_COMPRESSION_LEVEL
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct from a buffered image.      *      * @param doc The document to create the image as part of.      * @param bi The image to convert to a jpeg      * @param compressionQuality The quality level which is used to compress the image      * @throws IOException If there is an error processing the jpeg data.      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|BufferedImage
name|bi
parameter_list|,
name|float
name|compressionQuality
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
argument_list|,
name|JPG
argument_list|)
expr_stmt|;
name|createImageStream
argument_list|(
name|doc
argument_list|,
name|bi
argument_list|,
name|compressionQuality
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
parameter_list|,
name|float
name|compressionQuality
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|alpha
init|=
literal|null
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
name|alpha
operator|=
operator|new
name|BufferedImage
argument_list|(
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
name|BufferedImage
operator|.
name|TYPE_BYTE_GRAY
argument_list|)
expr_stmt|;
name|Graphics2D
name|g
init|=
name|alpha
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|g
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawRect
argument_list|(
literal|0
argument_list|,
literal|0
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
argument_list|)
expr_stmt|;
name|g
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|WHITE
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
name|int
name|alphaHeight
init|=
name|alpha
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|alphaWidth
init|=
name|alpha
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|whiteRGB
init|=
operator|(
name|Color
operator|.
name|WHITE
operator|)
operator|.
name|getRGB
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
name|alphaHeight
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
name|alphaWidth
condition|;
name|x
operator|++
control|)
block|{
name|Color
name|color
init|=
operator|new
name|Color
argument_list|(
name|alpha
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|color
operator|.
name|getRed
argument_list|()
operator|!=
literal|0
operator|&&
name|color
operator|.
name|getGreen
argument_list|()
operator|!=
literal|0
operator|&&
name|color
operator|.
name|getBlue
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|alpha
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|whiteRGB
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|BufferedImage
name|image
init|=
operator|new
name|BufferedImage
argument_list|(
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
name|BufferedImage
operator|.
name|TYPE_USHORT_565_RGB
argument_list|)
decl_stmt|;
name|g
operator|=
name|image
operator|.
name|createGraphics
argument_list|()
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
name|bi
operator|=
name|image
expr_stmt|;
block|}
name|java
operator|.
name|io
operator|.
name|OutputStream
name|os
init|=
name|getCOSStream
argument_list|()
operator|.
name|createFilteredStream
argument_list|()
decl_stmt|;
try|try
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
name|iter
init|=
name|ImageIO
operator|.
name|getImageWritersByFormatName
argument_list|(
name|JPG
argument_list|)
decl_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|writer
operator|=
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|ImageOutputStream
name|ios
init|=
name|ImageIO
operator|.
name|createImageOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|writer
operator|.
name|setOutput
argument_list|(
name|ios
argument_list|)
expr_stmt|;
comment|// Set the compression quality
name|JPEGImageWriteParam
name|iwparam
init|=
operator|new
name|JPEGImageWriteParam
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
decl_stmt|;
name|iwparam
operator|.
name|setCompressionMode
argument_list|(
name|ImageWriteParam
operator|.
name|MODE_EXPLICIT
argument_list|)
expr_stmt|;
name|iwparam
operator|.
name|setCompressionQuality
argument_list|(
name|compressionQuality
argument_list|)
expr_stmt|;
comment|// Write the image
name|writer
operator|.
name|write
argument_list|(
literal|null
argument_list|,
operator|new
name|IIOImage
argument_list|(
name|bi
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|iwparam
argument_list|)
expr_stmt|;
name|writer
operator|.
name|dispose
argument_list|()
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
name|DCT_DECODE
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
name|PDXObjectImage
name|alphaPdImage
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alpha
operator|!=
literal|null
condition|)
block|{
name|alphaPdImage
operator|=
operator|new
name|PDJpeg
argument_list|(
name|doc
argument_list|,
name|alpha
argument_list|,
name|compressionQuality
argument_list|)
expr_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|,
name|alphaPdImage
argument_list|)
expr_stmt|;
block|}
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
if|if
condition|(
name|bi
operator|.
name|getColorModel
argument_list|()
operator|.
name|getNumComponents
argument_list|()
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
name|bi
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
name|setHeight
argument_list|(
name|bi
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|bi
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns an image of the JPeg, or null if JPegs are not supported. (They should be. )      * {@inheritDoc}      */
specifier|public
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
block|{
comment|//TODO PKOCH
name|BufferedImage
name|bi
init|=
literal|null
decl_stmt|;
name|boolean
name|readError
init|=
literal|false
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|write2OutputStream
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|byte
index|[]
name|img
init|=
name|os
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|PDColorSpace
name|cs
init|=
name|getColorSpace
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cs
operator|instanceof
name|PDDeviceCMYK
operator|||
operator|(
name|cs
operator|instanceof
name|PDICCBased
operator|&&
name|cs
operator|.
name|getNumberOfComponents
argument_list|()
operator|==
literal|4
operator|)
condition|)
block|{
comment|// create BufferedImage based on the converted color values
name|bi
operator|=
name|convertCMYK2RGB
argument_list|(
name|readImage
argument_list|(
name|img
argument_list|)
argument_list|,
name|cs
argument_list|)
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
comment|// create BufferedImage based on the converted color values
name|bi
operator|=
name|processSeparation
argument_list|(
name|readImage
argument_list|(
name|img
argument_list|)
argument_list|,
name|cs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ByteArrayInputStream
name|bai
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|img
argument_list|)
decl_stmt|;
name|bi
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|bai
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IIOException
name|exception
parameter_list|)
block|{
name|readError
operator|=
literal|true
expr_stmt|;
block|}
comment|// 2. try to read jpeg again. some jpegs have some strange header containing
comment|//    "Adobe " at some place. so just replace the header with a valid jpeg header.
comment|// TODO : not sure if it works for all cases
if|if
condition|(
name|bi
operator|==
literal|null
operator|&&
name|readError
condition|)
block|{
name|byte
index|[]
name|newImage
init|=
name|replaceHeader
argument_list|(
name|img
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bai
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|newImage
argument_list|)
decl_stmt|;
name|bi
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|bai
argument_list|)
expr_stmt|;
block|}
comment|// If there is a 'soft mask' image then we use that as a transparency mask.
name|PDXObjectImage
name|smask
init|=
name|getSMaskImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|smask
operator|!=
literal|null
condition|)
block|{
name|BufferedImage
name|smaskBI
init|=
name|smask
operator|.
name|getRGBImage
argument_list|()
decl_stmt|;
name|COSArray
name|decodeArray
init|=
name|smask
operator|.
name|getDecode
argument_list|()
decl_stmt|;
name|CompositeImage
name|compositeImage
init|=
operator|new
name|CompositeImage
argument_list|(
name|bi
argument_list|,
name|smaskBI
argument_list|)
decl_stmt|;
name|BufferedImage
name|rgbImage
init|=
name|compositeImage
operator|.
name|createMaskedImage
argument_list|(
name|decodeArray
argument_list|)
decl_stmt|;
return|return
name|rgbImage
return|;
block|}
else|else
block|{
comment|// But if there is no soft mask, use the unaltered image.
return|return
name|bi
return|;
block|}
block|}
comment|/**      * This writes the JPeg to out.      * {@inheritDoc}      */
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
name|InputStream
name|data
init|=
name|getPDStream
argument_list|()
operator|.
name|getPartiallyFilteredStream
argument_list|(
name|DCT_FILTERS
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|amountRead
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|data
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|getHeaderEndPos
parameter_list|(
name|byte
index|[]
name|image
parameter_list|)
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
name|image
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|byte
name|b
init|=
name|image
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|b
operator|==
operator|(
name|byte
operator|)
literal|0xDB
condition|)
block|{
comment|// TODO : check for ff db
return|return
name|i
operator|-
literal|2
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
specifier|private
name|byte
index|[]
name|replaceHeader
parameter_list|(
name|byte
index|[]
name|image
parameter_list|)
block|{
comment|// get end position of wrong header respectively startposition of "real jpeg data"
name|int
name|pos
init|=
name|getHeaderEndPos
argument_list|(
name|image
argument_list|)
decl_stmt|;
comment|// simple correct header
name|byte
index|[]
name|header
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0xD8
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0xE0
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x10
block|,
operator|(
name|byte
operator|)
literal|0x4A
block|,
operator|(
name|byte
operator|)
literal|0x46
block|,
operator|(
name|byte
operator|)
literal|0x49
block|,
operator|(
name|byte
operator|)
literal|0x46
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x60
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x60
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
decl_stmt|;
comment|// concat
name|byte
index|[]
name|newImage
init|=
operator|new
name|byte
index|[
name|image
operator|.
name|length
operator|-
name|pos
operator|+
name|header
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|header
argument_list|,
literal|0
argument_list|,
name|newImage
argument_list|,
literal|0
argument_list|,
name|header
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|image
argument_list|,
name|pos
operator|+
literal|1
argument_list|,
name|newImage
argument_list|,
name|header
operator|.
name|length
argument_list|,
name|image
operator|.
name|length
operator|-
name|pos
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|newImage
return|;
block|}
specifier|private
name|Raster
name|readImage
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageInputStream
name|input
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ImageReader
argument_list|>
name|readers
init|=
name|ImageIO
operator|.
name|getImageReaders
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|readers
operator|==
literal|null
operator|||
operator|!
name|readers
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No ImageReaders found"
argument_list|)
throw|;
block|}
comment|// read the raster information only
comment|// avoid to access the meta information
name|ImageReader
name|reader
init|=
operator|(
name|ImageReader
operator|)
name|readers
operator|.
name|next
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setInput
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|Raster
name|raster
init|=
name|reader
operator|.
name|readRaster
argument_list|(
literal|0
argument_list|,
name|reader
operator|.
name|getDefaultReadParam
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|raster
return|;
block|}
comment|// CMYK jpegs are not supported by JAI, so that we have to do the conversion on our own
specifier|private
name|BufferedImage
name|convertCMYK2RGB
parameter_list|(
name|Raster
name|raster
parameter_list|,
name|PDColorSpace
name|colorspace
parameter_list|)
throws|throws
name|IOException
block|{
comment|// create a java color space to be used for conversion
name|ColorSpace
name|cs
init|=
name|colorspace
operator|.
name|getJavaColorSpace
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
name|byte
index|[]
name|rgb
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
literal|3
index|]
decl_stmt|;
name|int
name|rgbIndex
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
operator|<
name|height
condition|;
name|i
operator|++
control|)
block|{
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
comment|// get the source color values
name|float
index|[]
name|srcColorValues
init|=
name|raster
operator|.
name|getPixel
argument_list|(
name|j
argument_list|,
name|i
argument_list|,
operator|(
name|float
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
comment|// convert values from 0..255 to 0..1
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
literal|4
condition|;
name|k
operator|++
control|)
block|{
name|srcColorValues
index|[
name|k
index|]
operator|/=
literal|255f
expr_stmt|;
block|}
comment|// convert CMYK to RGB
name|float
index|[]
name|rgbValues
init|=
name|cs
operator|.
name|toRGB
argument_list|(
name|srcColorValues
argument_list|)
decl_stmt|;
comment|// convert values from 0..1 to 0..255
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
literal|3
condition|;
name|k
operator|++
control|)
block|{
name|rgb
index|[
name|rgbIndex
operator|+
name|k
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|rgbValues
index|[
name|k
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
block|}
name|rgbIndex
operator|+=
literal|3
expr_stmt|;
block|}
block|}
comment|// create a RGB color model
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
name|CS_sRGB
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
comment|// create the target raster
name|WritableRaster
name|writeableRaster
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
comment|// get the data buffer of the raster
name|DataBufferByte
name|buffer
init|=
operator|(
name|DataBufferByte
operator|)
name|writeableRaster
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
comment|// copy all the converted data to the raster buffer
name|System
operator|.
name|arraycopy
argument_list|(
name|rgb
argument_list|,
literal|0
argument_list|,
name|bufferData
argument_list|,
literal|0
argument_list|,
name|rgb
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// create an image using the converted color values
return|return
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|writeableRaster
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|// a separation colorspace uses a tint transform function to convert color values
specifier|private
name|BufferedImage
name|processSeparation
parameter_list|(
name|Raster
name|raster
parameter_list|,
name|PDColorSpace
name|colorspace
parameter_list|)
throws|throws
name|IOException
block|{
name|PDSeparation
name|csSeparation
init|=
operator|(
name|PDSeparation
operator|)
name|colorspace
decl_stmt|;
name|PDFunction
name|function
init|=
name|csSeparation
operator|.
name|getTintTransform
argument_list|()
decl_stmt|;
name|int
name|numberOfInputValues
init|=
name|function
operator|.
name|getNumberOfInputParameters
argument_list|()
decl_stmt|;
name|int
name|numberOfOutputValues
init|=
name|function
operator|.
name|getNumberOfInputParameters
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
name|byte
index|[]
name|sourceBuffer
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|numberOfOutputValues
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
operator|<
name|height
condition|;
name|i
operator|++
control|)
block|{
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
comment|// get the source color values
name|float
index|[]
name|srcColorValues
init|=
name|raster
operator|.
name|getPixel
argument_list|(
name|j
argument_list|,
name|i
argument_list|,
operator|(
name|float
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
comment|// convert values from 0..255 to 0..1
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|numberOfInputValues
condition|;
name|k
operator|++
control|)
block|{
name|srcColorValues
index|[
name|k
index|]
operator|/=
literal|255f
expr_stmt|;
block|}
comment|// transform the color values using the tint function
name|float
index|[]
name|convertedValues
init|=
name|function
operator|.
name|eval
argument_list|(
name|srcColorValues
argument_list|)
decl_stmt|;
comment|// convert values from 0..1 to 0..255
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|numberOfOutputValues
condition|;
name|k
operator|++
control|)
block|{
name|sourceBuffer
index|[
name|bufferIndex
operator|+
name|k
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|convertedValues
index|[
name|k
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
block|}
name|bufferIndex
operator|+=
name|numberOfOutputValues
expr_stmt|;
block|}
block|}
comment|// create a target color model
name|ColorModel
name|cm
init|=
operator|new
name|ComponentColorModel
argument_list|(
name|colorspace
operator|.
name|getJavaColorSpace
argument_list|()
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
comment|// create the target raster
name|WritableRaster
name|writeableRaster
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
comment|// get the data buffer of the raster
name|DataBufferByte
name|buffer
init|=
operator|(
name|DataBufferByte
operator|)
name|writeableRaster
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
comment|// copy all the converted data to the raster buffer
name|System
operator|.
name|arraycopy
argument_list|(
name|sourceBuffer
argument_list|,
literal|0
argument_list|,
name|bufferData
argument_list|,
literal|0
argument_list|,
name|sourceBuffer
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// create an image using the converted color values
return|return
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|writeableRaster
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

