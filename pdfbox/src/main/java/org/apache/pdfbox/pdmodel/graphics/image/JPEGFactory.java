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
name|image
operator|.
name|BufferedImage
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSStream
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
comment|/**  * Factory for creating a PDImageXObject containing a JPEG compressed image.  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JPEGFactory
extends|extends
name|ImageFactory
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
comment|// copy stream
name|ByteArrayInputStream
name|byteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
argument_list|)
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
argument_list|)
decl_stmt|;
comment|// add DCT filter
name|pdImage
operator|.
name|getCOSStream
argument_list|()
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
comment|// set properties (width, height, depth, color space, etc.)
name|setPropertiesFromAWT
argument_list|(
name|awtImage
argument_list|,
name|pdImage
argument_list|)
expr_stmt|;
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
name|ImageInputStream
name|iis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|iis
operator|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
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
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
literal|"jpeg"
argument_list|,
name|baos
argument_list|,
name|dpi
argument_list|,
name|quality
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
argument_list|)
decl_stmt|;
comment|// add DCT filter
name|COSStream
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
name|DCT_DECODE
argument_list|)
expr_stmt|;
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
comment|// set properties (width, height, depth, color space, etc.)
name|setPropertiesFromAWT
argument_list|(
name|awtColorImage
argument_list|,
name|pdImage
argument_list|)
expr_stmt|;
return|return
name|pdImage
return|;
block|}
block|}
end_class

end_unit

