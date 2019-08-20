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
name|tools
operator|.
name|imageio
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
name|File
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * Handles some ImageIO operations.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ImageIOUtil
block|{
comment|/**      * Log instance      */
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
name|ImageIOUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ImageIOUtil
parameter_list|()
block|{     }
comment|/**      * Writes a buffered image to a file using the given image format. See           * {@link #writeImage(BufferedImage image, String formatName,       * OutputStream output, int dpi, float compressionQuality)} for more details.      *      * @param image the image to be written      * @param filename used to construct the filename for the individual image.      * Its suffix will be used as the image format.      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
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
return|return
name|writeImage
argument_list|(
name|image
argument_list|,
name|filename
argument_list|,
name|dpi
argument_list|,
literal|1.0f
argument_list|)
return|;
block|}
comment|/**      * Writes a buffered image to a file using the given image format.      * See {@link #writeImage(BufferedImage image, String formatName,      * OutputStream output, int dpi, float compressionQuality)} for more details.      *      * @param image the image to be written      * @param filename used to construct the filename for the individual image. Its suffix will be      * used as the image format.      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @param compressionQuality quality to be used when compressing the image (0&lt;      * compressionQuality&lt; 1.0f)      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
specifier|public
specifier|static
name|boolean
name|writeImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|String
name|filename
parameter_list|,
name|int
name|dpi
parameter_list|,
name|float
name|compressionQuality
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
try|try
init|(
name|FileOutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|String
name|formatName
init|=
name|filename
operator|.
name|substring
argument_list|(
name|filename
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
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
name|compressionQuality
argument_list|)
return|;
block|}
block|}
comment|/**      * Writes a buffered image to a file using the given image format. See            * {@link #writeImage(BufferedImage image, String formatName,       * OutputStream output, int dpi, float compressionQuality)} for more details.      *      * @param image the image to be written      * @param formatName the target format (ex. "png") which is also the suffix      * for the filename      * @param filename used to construct the filename for the individual image.      * The formatName parameter will be used as the suffix.      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      * @deprecated use      * {@link #writeImage(BufferedImage image, String filename, int dpi)}, which      * uses the full filename instead of just the prefix.      */
annotation|@
name|Deprecated
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
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
operator|+
literal|"."
operator|+
name|formatName
argument_list|)
decl_stmt|;
try|try
init|(
name|FileOutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
init|)
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
block|}
comment|/**      * Writes a buffered image to a file using the given image format. See            * {@link #writeImage(BufferedImage image, String formatName,       * OutputStream output, int dpi, float compressionQuality)} for more details.      *      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
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
comment|/**      * Writes a buffered image to a file using the given image format. See            * {@link #writeImage(BufferedImage image, String formatName,       * OutputStream output, int dpi, float compressionQuality)} for more details.      *      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
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
comment|/**      * Writes a buffered image to a file using the given image format.      * Compression is fixed for PNG, GIF, BMP and WBMP, dependent of the compressionQuality      * parameter for JPG, and dependent of bit count for TIFF (a bitonal image      * will be compressed with CCITT G4, a color image with LZW). Creating a      * TIFF image is only supported if the jai_imageio library (or equivalent)      * is in the class path.      *      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @param compressionQuality quality to be used when compressing the image (0&lt;      * compressionQuality&lt; 1.0f)      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
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
name|compressionQuality
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
name|compressionQuality
argument_list|,
literal|""
argument_list|)
return|;
block|}
comment|/**      * Writes a buffered image to a file using the given image format.      * Compression is fixed for PNG, GIF, BMP and WBMP, dependent of the compressionQuality      * parameter for JPG, and dependent of bit count for TIFF (a bitonal image      * will be compressed with CCITT G4, a color image with LZW). Creating a      * TIFF image is only supported if the jai_imageio library is in the class      * path.      *      * @param image the image to be written      * @param formatName the target format (ex. "png")      * @param output the output stream to be used for writing      * @param dpi the resolution in dpi (dots per inch) to be used in metadata      * @param compressionQuality quality to be used when compressing the image      * (0&lt; compressionQuality&lt; 1.0f)      * @param compressionType Advanced users only, and only relevant for TIFF      * files: If null, save uncompressed; if empty string, use logic explained      * above; other valid values are found in the javadoc of      *<a href="https://download.java.net/media/jai-imageio/javadoc/1.1/com/sun/media/imageio/plugins/tiff/TIFFImageWriteParam.html">TIFFImageWriteParam</a>.      * @return true if the image file was produced, false if there was an error.      * @throws IOException if an I/O error occurs      */
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
name|compressionQuality
parameter_list|,
name|String
name|compressionType
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
comment|// Loop until we get the best driver, i.e. one that supports
comment|// setting dpi in the standard metadata format; however we'd also
comment|// accept a driver that can't, if a better one can't be found
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
operator|!=
literal|null
condition|)
block|{
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
operator|!=
literal|null
operator|&&
operator|!
name|metadata
operator|.
name|isReadOnly
argument_list|()
operator|&&
name|metadata
operator|.
name|isStandardMetadataFormatSupported
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
block|}
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"No ImageWriter found for '"
operator|+
name|formatName
operator|+
literal|"' format"
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|writerFormatNames
init|=
name|ImageIO
operator|.
name|getWriterFormatNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|fmt
range|:
name|writerFormatNames
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|fmt
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|error
argument_list|(
literal|"Supported formats: "
operator|+
name|sb
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// compression
if|if
condition|(
name|param
operator|!=
literal|null
operator|&&
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
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|compressionType
argument_list|)
condition|)
block|{
comment|// default logic
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
name|param
operator|.
name|setCompressionType
argument_list|(
name|compressionType
argument_list|)
expr_stmt|;
if|if
condition|(
name|compressionType
operator|!=
literal|null
condition|)
block|{
name|param
operator|.
name|setCompressionQuality
argument_list|(
name|compressionQuality
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
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
name|compressionQuality
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// TIFF metadata
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
elseif|else
if|if
condition|(
literal|"jpeg"
operator|.
name|equalsIgnoreCase
argument_list|(
name|formatName
argument_list|)
operator|||
literal|"jpg"
operator|.
name|equalsIgnoreCase
argument_list|(
name|formatName
argument_list|)
condition|)
block|{
comment|// This segment must be run before other meta operations,
comment|// or else "IIOInvalidTreeException: Invalid node: app0JFIF"
comment|// The other (general) "meta" methods may not be used, because
comment|// this will break the reading of the meta data in tests
name|JPEGUtil
operator|.
name|updateMetadata
argument_list|(
name|metadata
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// write metadata is possible
if|if
condition|(
name|metadata
operator|!=
literal|null
operator|&&
operator|!
name|metadata
operator|.
name|isReadOnly
argument_list|()
operator|&&
name|metadata
operator|.
name|isStandardMetadataFormatSupported
argument_list|()
condition|)
block|{
name|setDPI
argument_list|(
name|metadata
argument_list|,
name|dpi
argument_list|,
name|formatName
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Gets the named child node, or creates and attaches it.      *      * @param parentNode the parent node      * @param name name of the child node      *      * @return the existing or just created child node      */
specifier|private
specifier|static
name|IIOMetadataNode
name|getOrCreateChildNode
parameter_list|(
name|IIOMetadataNode
name|parentNode
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|NodeList
name|nodeList
init|=
name|parentNode
operator|.
name|getElementsByTagName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|(
name|IIOMetadataNode
operator|)
name|nodeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
return|;
block|}
name|IIOMetadataNode
name|childNode
init|=
operator|new
name|IIOMetadataNode
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|parentNode
operator|.
name|appendChild
argument_list|(
name|childNode
argument_list|)
expr_stmt|;
return|return
name|childNode
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
parameter_list|,
name|String
name|formatName
parameter_list|)
throws|throws
name|IIOInvalidTreeException
block|{
name|IIOMetadataNode
name|root
init|=
operator|(
name|IIOMetadataNode
operator|)
name|metadata
operator|.
name|getAsTree
argument_list|(
name|MetaUtil
operator|.
name|STANDARD_METADATA_FORMAT
argument_list|)
decl_stmt|;
name|IIOMetadataNode
name|dimension
init|=
name|getOrCreateChildNode
argument_list|(
name|root
argument_list|,
literal|"Dimension"
argument_list|)
decl_stmt|;
comment|// PNG writer doesn't conform to the spec which is
comment|// "The width of a pixel, in millimeters"
comment|// but instead counts the pixels per millimeter
name|float
name|res
init|=
literal|"PNG"
operator|.
name|equalsIgnoreCase
argument_list|(
name|formatName
argument_list|)
condition|?
name|dpi
operator|/
literal|25.4f
else|:
literal|25.4f
operator|/
name|dpi
decl_stmt|;
name|IIOMetadataNode
name|child
decl_stmt|;
name|child
operator|=
name|getOrCreateChildNode
argument_list|(
name|dimension
argument_list|,
literal|"HorizontalPixelSize"
argument_list|)
expr_stmt|;
name|child
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
name|res
argument_list|)
argument_list|)
expr_stmt|;
name|child
operator|=
name|getOrCreateChildNode
argument_list|(
name|dimension
argument_list|,
literal|"VerticalPixelSize"
argument_list|)
expr_stmt|;
name|child
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
name|res
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|mergeTree
argument_list|(
name|MetaUtil
operator|.
name|STANDARD_METADATA_FORMAT
argument_list|,
name|root
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

