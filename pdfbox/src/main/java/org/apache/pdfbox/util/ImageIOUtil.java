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
name|util
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
name|FileOutputStream
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
name|ImageTypeSpecifier
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
name|metadata
operator|.
name|IIOInvalidTreeException
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
name|ImageOutputStream
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
comment|/**  * Handles some ImageIO operations.  */
end_comment

begin_class
specifier|public
class|class
name|ImageIOUtil
block|{
specifier|private
name|ImageIOUtil
parameter_list|()
block|{     }
comment|/**      * Writes a buffered image to a file using the given image format.      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param filename used to construct the filename for the individual images      * @param dpi the resolution in dpi (dots per inch)      * @return true if the images were produced, false if there was an error      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|String
name|formatName
parameter_list|,
name|String
name|filename
parameter_list|,
name|int
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|filename
operator|+
literal|"."
operator|+
name|formatName
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|writeImage
argument_list|(
name|image
argument_list|,
name|formatName
argument_list|,
name|output
argument_list|,
name|dpi
argument_list|)
return|;
block|}
finally|finally
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Writes a buffered image to a file using the given image format.      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @return true if the images were produced, false if there was an error      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|String
name|formatName
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|writeImage
argument_list|(
name|image
argument_list|,
name|formatName
argument_list|,
name|output
argument_list|,
literal|72
argument_list|)
return|;
block|}
comment|/**      * Writes a buffered image to a file using the given image format.      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @param dpi resolution to be used when writing the image      * @return true if the images were produced, false if there was an error      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|String
name|formatName
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|int
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|writeImage
argument_list|(
name|image
argument_list|,
name|formatName
argument_list|,
name|output
argument_list|,
name|dpi
argument_list|,
literal|1.0f
argument_list|)
return|;
block|}
comment|/**      * Writes a buffered image to a file using the given image format.      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @param dpi resolution to be used when writing the image      * @param quality quality to be used when compressing the image (0&lt; quality&lt; 1.0f)      * @return true if the images were produced, false if there was an error      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|String
name|formatName
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|int
name|dpi
parameter_list|,
name|float
name|quality
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageOutputStream
name|imageOutput
init|=
literal|null
decl_stmt|;
name|ImageWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// find suitable image writer
name|Iterator
argument_list|<
name|ImageWriter
argument_list|>
name|writers
init|=
name|ImageIO
operator|.
name|getImageWritersByFormatName
argument_list|(
name|formatName
argument_list|)
decl_stmt|;
name|ImageWriteParam
name|param
init|=
literal|null
decl_stmt|;
name|IIOMetadata
name|metadata
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|writers
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|writer
operator|=
name|writers
operator|.
name|next
argument_list|()
expr_stmt|;
name|param
operator|=
name|writer
operator|.
name|getDefaultWriteParam
argument_list|()
expr_stmt|;
name|metadata
operator|=
name|writer
operator|.
name|getDefaultImageMetadata
argument_list|(
operator|new
name|ImageTypeSpecifier
argument_list|(
name|image
argument_list|)
argument_list|,
name|param
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|isReadOnly
argument_list|()
operator|||
operator|!
name|metadata
operator|.
name|isStandardMetadataFormatSupported
argument_list|()
condition|)
block|{
name|writer
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// compression
if|if
condition|(
name|param
operator|.
name|canWriteCompressed
argument_list|()
condition|)
block|{
name|param
operator|.
name|setCompressionMode
argument_list|(
name|ImageWriteParam
operator|.
name|MODE_EXPLICIT
argument_list|)
expr_stmt|;
if|if
condition|(
name|formatName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"tif"
argument_list|)
condition|)
block|{
comment|// TIFF compression
name|TIFFUtil
operator|.
name|setCompressionType
argument_list|(
name|param
argument_list|,
name|image
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// JPEG, PNG compression
name|param
operator|.
name|setCompressionType
argument_list|(
name|param
operator|.
name|getCompressionTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|param
operator|.
name|setCompressionQuality
argument_list|(
name|quality
argument_list|)
expr_stmt|;
block|}
block|}
comment|// metadata
name|setDPI
argument_list|(
name|metadata
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
comment|// TIFF metadata
if|if
condition|(
name|formatName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"tif"
argument_list|)
condition|)
block|{
name|TIFFUtil
operator|.
name|updateMetadata
argument_list|(
name|metadata
argument_list|,
name|image
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
block|}
comment|// write
name|imageOutput
operator|=
name|ImageIO
operator|.
name|createImageOutputStream
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setOutput
argument_list|(
name|imageOutput
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|null
argument_list|,
operator|new
name|IIOImage
argument_list|(
name|image
argument_list|,
literal|null
argument_list|,
name|metadata
argument_list|)
argument_list|,
name|param
argument_list|)
expr_stmt|;
block|}
finally|finally
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
if|if
condition|(
name|imageOutput
operator|!=
literal|null
condition|)
block|{
name|imageOutput
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// sets the DPI metadata
specifier|private
specifier|static
name|void
name|setDPI
parameter_list|(
name|IIOMetadata
name|metadata
parameter_list|,
name|int
name|dpi
parameter_list|)
block|{
name|IIOMetadataNode
name|root
init|=
operator|new
name|IIOMetadataNode
argument_list|(
literal|"javax_imageio_1.0"
argument_list|)
decl_stmt|;
name|Element
name|dimension
init|=
operator|new
name|IIOMetadataNode
argument_list|(
literal|"Dimension"
argument_list|)
decl_stmt|;
name|Element
name|h
init|=
operator|new
name|IIOMetadataNode
argument_list|(
literal|"HorizontalPixelSize"
argument_list|)
decl_stmt|;
name|h
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
name|dpi
operator|/
literal|25.4
argument_list|)
argument_list|)
expr_stmt|;
name|dimension
operator|.
name|appendChild
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|Element
name|v
init|=
operator|new
name|IIOMetadataNode
argument_list|(
literal|"VerticalPixelSize"
argument_list|)
decl_stmt|;
name|v
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
name|dpi
operator|/
literal|25.4
argument_list|)
argument_list|)
expr_stmt|;
name|dimension
operator|.
name|appendChild
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
try|try
block|{
name|metadata
operator|.
name|mergeTree
argument_list|(
literal|"javax_imageio_1.0"
argument_list|,
name|root
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IIOInvalidTreeException
name|e
parameter_list|)
block|{
comment|// should never happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

