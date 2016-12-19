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
name|io
operator|.
name|BufferedInputStream
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
name|DataInputStream
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
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
name|HashSet
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
name|Set
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
name|stream
operator|.
name|ImageInputStream
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|PDResources
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
name|PDXObject
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
name|form
operator|.
name|PDFormXObject
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
name|image
operator|.
name|PDImageXObject
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
name|rendering
operator|.
name|ImageType
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
name|rendering
operator|.
name|PDFRenderer
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
name|filetypedetector
operator|.
name|FileType
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
name|filetypedetector
operator|.
name|FileTypeDetector
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
name|Node
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
comment|/**  * Test suite for ImageIOUtil.  */
end_comment

begin_class
specifier|public
class|class
name|TestImageIOUtils
extends|extends
name|TestCase
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
name|TestImageIOUtils
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Check whether the resource images can be saved.      *       * @param resources      * @throws IOException       */
name|void
name|checkSaveResources
parameter_list|(
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|COSName
name|name
range|:
name|resources
operator|.
name|getXObjectNames
argument_list|()
control|)
block|{
name|PDXObject
name|xobject
init|=
name|resources
operator|.
name|getXObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|instanceof
name|PDImageXObject
condition|)
block|{
name|PDImageXObject
name|imageObject
init|=
operator|(
name|PDImageXObject
operator|)
name|xobject
decl_stmt|;
name|String
name|suffix
init|=
name|imageObject
operator|.
name|getSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
name|suffix
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"jpx"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|suffix
operator|=
literal|"JPEG2000"
expr_stmt|;
block|}
if|if
condition|(
literal|"jb2"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
comment|// jbig2 usually not available
name|suffix
operator|=
literal|"PNG"
expr_stmt|;
block|}
name|boolean
name|writeOK
init|=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|imageObject
operator|.
name|getImage
argument_list|()
argument_list|,
name|suffix
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|writeOK
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
name|checkSaveResources
argument_list|(
operator|(
operator|(
name|PDFormXObject
operator|)
name|xobject
operator|)
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Validate page rendering for all supported image formats (JDK5).      *      * @param file The file to validate      * @param outDir Name of the output directory      * @throws IOException when there is an exception      */
specifier|private
name|void
name|doTestFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|outDir
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
name|String
name|imageType
init|=
literal|"png"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Preparing to convert "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|float
name|dpi
init|=
literal|36
decl_stmt|;
comment|// low DPI so that rendering is FAST
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|// Save image resources of first page
name|checkSaveResources
argument_list|(
name|document
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
comment|// testing PNG
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
name|checkResolution
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
operator|(
name|int
operator|)
name|dpi
argument_list|)
expr_stmt|;
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|PNG
argument_list|)
expr_stmt|;
comment|// testing JPG/JPEG
name|imageType
operator|=
literal|"jpg"
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
name|checkResolution
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
operator|(
name|int
operator|)
name|dpi
argument_list|)
expr_stmt|;
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|JPEG
argument_list|)
expr_stmt|;
comment|// testing BMP
name|imageType
operator|=
literal|"bmp"
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
name|checkResolution
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
operator|(
name|int
operator|)
name|dpi
argument_list|)
expr_stmt|;
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|BMP
argument_list|)
expr_stmt|;
comment|// testing GIF
name|imageType
operator|=
literal|"gif"
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
comment|// no META data posible for GIF, thus no dpi test
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|GIF
argument_list|)
expr_stmt|;
comment|// testing WBMP
name|imageType
operator|=
literal|"wbmp"
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|,
name|ImageType
operator|.
name|BINARY
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
comment|// no META data posible for WBMP, thus no dpi test
comment|// testing TIFF
name|imageType
operator|=
literal|"tif"
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-bw-"
argument_list|,
name|ImageType
operator|.
name|BINARY
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
name|checkResolution
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-bw-1."
operator|+
name|imageType
argument_list|,
operator|(
name|int
operator|)
name|dpi
argument_list|)
expr_stmt|;
name|checkTiffCompression
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-bw-1."
operator|+
name|imageType
argument_list|,
literal|"CCITT T.6"
argument_list|)
expr_stmt|;
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-bw-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|TIFF
argument_list|)
expr_stmt|;
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-co-"
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
name|checkResolution
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-co-1."
operator|+
name|imageType
argument_list|,
operator|(
name|int
operator|)
name|dpi
argument_list|)
expr_stmt|;
name|checkTiffCompression
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-co-1."
operator|+
name|imageType
argument_list|,
literal|"LZW"
argument_list|)
expr_stmt|;
name|checkFileTypeByContent
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-co-1."
operator|+
name|imageType
argument_list|,
name|FileType
operator|.
name|TIFF
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Checks whether file image size and content are identical      *      * @param filename the filename where we just wrote to      * @param image the image that is to be checked      * @throws IOException if something goes wrong      */
specifier|private
name|void
name|checkImageFileSizeAndContent
parameter_list|(
name|String
name|filename
parameter_list|,
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|newImage
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"File '"
operator|+
name|filename
operator|+
literal|"' could not be read"
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
name|checkNotBlank
argument_list|(
name|filename
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
name|checkBufferedImageSize
argument_list|(
name|filename
argument_list|,
name|image
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|!=
name|newImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"\"File '"
operator|+
name|filename
operator|+
literal|"' has different pixel at ("
operator|+
name|x
operator|+
literal|","
operator|+
name|y
operator|+
literal|")"
argument_list|,
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
argument_list|,
operator|new
name|Color
argument_list|(
name|newImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Checks whether file image size is identical      *      * @param filename the filename where we just wrote to      * @param image the image that is to be checked      * @throws IOException if something goes wrong      */
specifier|private
name|void
name|checkImageFileSize
parameter_list|(
name|String
name|filename
parameter_list|,
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|newImage
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"File '"
operator|+
name|filename
operator|+
literal|"' could not be read"
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
name|checkNotBlank
argument_list|(
name|filename
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
name|checkBufferedImageSize
argument_list|(
name|filename
argument_list|,
name|image
argument_list|,
name|newImage
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkBufferedImageSize
parameter_list|(
name|String
name|filename
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|BufferedImage
name|newImage
parameter_list|)
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|"File '"
operator|+
name|filename
operator|+
literal|"' has different height after read"
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|,
name|newImage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"File '"
operator|+
name|filename
operator|+
literal|"' has different width after read"
argument_list|,
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|newImage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkNotBlank
parameter_list|(
name|String
name|filename
parameter_list|,
name|BufferedImage
name|newImage
parameter_list|)
block|{
comment|// http://stackoverflow.com/a/5253698/535646
name|Set
argument_list|<
name|Integer
argument_list|>
name|colors
init|=
operator|new
name|HashSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|w
init|=
name|newImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
init|=
name|newImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
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
name|x
operator|++
control|)
block|{
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
name|y
operator|++
control|)
block|{
name|colors
operator|.
name|add
argument_list|(
name|newImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|assertFalse
argument_list|(
literal|"File '"
operator|+
name|filename
operator|+
literal|"' has less than two colors"
argument_list|,
name|colors
operator|.
name|size
argument_list|()
operator|<
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|String
name|imageFormat
parameter_list|,
name|String
name|outputPrefix
parameter_list|,
name|ImageType
name|imageType
parameter_list|,
name|float
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
name|renderer
operator|.
name|renderImageWithDPI
argument_list|(
literal|0
argument_list|,
name|dpi
argument_list|,
name|imageType
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|outputPrefix
operator|+
literal|1
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Writing: "
operator|+
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|)
expr_stmt|;
comment|// for Maven (keep me!)
name|boolean
name|res
init|=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|,
name|Math
operator|.
name|round
argument_list|(
name|dpi
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ImageIOUtil.writeImage() failed for file "
operator|+
name|fileName
argument_list|,
name|res
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"jpg"
operator|.
name|equals
argument_list|(
name|imageFormat
argument_list|)
operator|||
literal|"gif"
operator|.
name|equals
argument_list|(
name|imageFormat
argument_list|)
condition|)
block|{
comment|// jpeg is lossy, gif has 256 colors,
comment|// so we can't check for content identity
name|checkImageFileSize
argument_list|(
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|,
name|image
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkImageFileSizeAndContent
argument_list|(
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|,
name|image
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test to validate image rendering of file set.      *      * @throws Exception when there is an exception      */
specifier|public
name|void
name|testRenderImage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|inDir
init|=
literal|"src/test/resources/input/ImageIOUtil"
decl_stmt|;
name|String
name|outDir
init|=
literal|"target/test-output/ImageIOUtil/"
decl_stmt|;
operator|new
name|File
argument_list|(
name|outDir
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|new
name|File
argument_list|(
name|outDir
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"could not create output directory"
argument_list|)
throw|;
block|}
name|File
index|[]
name|testFiles
init|=
operator|new
name|File
argument_list|(
name|inDir
argument_list|)
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".ai"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|testFiles
control|)
block|{
name|doTestFile
argument_list|(
name|file
argument_list|,
name|outDir
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_METADATA_FORMAT
init|=
literal|"javax_imageio_1.0"
decl_stmt|;
comment|/**      * checks whether the resolution of an image file is as expected.      *      * @param filename the name of the file      * @param expectedResolution the expected resolution      *      * @throws IOException if something goes wrong      */
specifier|private
name|void
name|checkResolution
parameter_list|(
name|String
name|filename
parameter_list|,
name|int
name|expectedResolution
parameter_list|)
throws|throws
name|IOException
block|{
name|assertFalse
argument_list|(
literal|"Empty file "
operator|+
name|filename
argument_list|,
operator|new
name|File
argument_list|(
name|filename
argument_list|)
operator|.
name|length
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|String
name|suffix
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
if|if
condition|(
literal|"BMP"
operator|.
name|equals
argument_list|(
name|suffix
operator|.
name|toUpperCase
argument_list|()
argument_list|)
condition|)
block|{
comment|// BMP reader doesn't work
name|checkBmpResolution
argument_list|(
name|filename
argument_list|,
name|expectedResolution
argument_list|)
expr_stmt|;
return|return;
block|}
name|Iterator
name|readers
init|=
name|ImageIO
operator|.
name|getImageReadersBySuffix
argument_list|(
name|suffix
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No image reader found for suffix "
operator|+
name|suffix
argument_list|,
name|readers
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
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
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No ImageInputStream created for file "
operator|+
name|filename
argument_list|,
name|iis
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
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
name|Element
name|root
init|=
operator|(
name|Element
operator|)
name|imageMetadata
operator|.
name|getAsTree
argument_list|(
name|STANDARD_METADATA_FORMAT
argument_list|)
decl_stmt|;
name|NodeList
name|dimensionNodes
init|=
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"Dimension"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No resolution found in image file "
operator|+
name|filename
argument_list|,
name|dimensionNodes
operator|.
name|getLength
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Element
name|dimensionElement
init|=
operator|(
name|Element
operator|)
name|dimensionNodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|NodeList
name|pixelSizeNodes
init|=
name|dimensionElement
operator|.
name|getElementsByTagName
argument_list|(
literal|"HorizontalPixelSize"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No X resolution found in image file "
operator|+
name|filename
argument_list|,
name|pixelSizeNodes
operator|.
name|getLength
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Node
name|pixelSizeNode
init|=
name|pixelSizeNodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|val
init|=
name|pixelSizeNode
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"value"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|int
name|actualResolution
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
literal|25.4
operator|/
name|Double
operator|.
name|parseDouble
argument_list|(
name|val
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"X resolution doesn't match in image file "
operator|+
name|filename
argument_list|,
name|expectedResolution
argument_list|,
name|actualResolution
argument_list|)
expr_stmt|;
name|pixelSizeNodes
operator|=
name|dimensionElement
operator|.
name|getElementsByTagName
argument_list|(
literal|"VerticalPixelSize"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No Y resolution found in image file "
operator|+
name|filename
argument_list|,
name|pixelSizeNodes
operator|.
name|getLength
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|pixelSizeNode
operator|=
name|pixelSizeNodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|val
operator|=
name|pixelSizeNode
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"value"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
expr_stmt|;
name|actualResolution
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
literal|25.4
operator|/
name|Double
operator|.
name|parseDouble
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y resolution doesn't match"
argument_list|,
name|expectedResolution
argument_list|,
name|actualResolution
argument_list|)
expr_stmt|;
name|iis
operator|.
name|close
argument_list|()
expr_stmt|;
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|/**      * checks whether the resolution of a BMP image file is as expected.      *      * @param filename the name of the BMP file      * @param expectedResolution the expected resolution      *      * @throws IOException if something goes wrong      */
specifier|private
name|void
name|checkBmpResolution
parameter_list|(
name|String
name|filename
parameter_list|,
name|int
name|expectedResolution
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
comment|// BMP format explained here:
comment|// http://www.javaworld.com/article/2077561/learn-java/java-tip-60--saving-bitmap-files-in-java.html
comment|// we skip 38 bytes and then read two 4 byte-integers and reverse the bytes
name|DataInputStream
name|dis
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|skipped
init|=
name|dis
operator|.
name|skipBytes
argument_list|(
literal|38
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't skip 38 bytes in image file "
operator|+
name|filename
argument_list|,
literal|38
argument_list|,
name|skipped
argument_list|)
expr_stmt|;
name|int
name|pixelsPerMeter
init|=
name|Integer
operator|.
name|reverseBytes
argument_list|(
name|dis
operator|.
name|readInt
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|actualResolution
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|pixelsPerMeter
operator|/
literal|100.0
operator|*
literal|2.54
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"X resolution doesn't match in image file "
operator|+
name|filename
argument_list|,
name|expectedResolution
argument_list|,
name|actualResolution
argument_list|)
expr_stmt|;
name|pixelsPerMeter
operator|=
name|Integer
operator|.
name|reverseBytes
argument_list|(
name|dis
operator|.
name|readInt
argument_list|()
argument_list|)
expr_stmt|;
name|actualResolution
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|pixelsPerMeter
operator|/
literal|100.0
operator|*
literal|2.54
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y resolution doesn't match in image file "
operator|+
name|filename
argument_list|,
name|expectedResolution
argument_list|,
name|actualResolution
argument_list|)
expr_stmt|;
name|dis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * checks whether the compression of a TIFF file is as expected.      *      * @param filename Filename      * @param expectedCompression expected TIFF compression      *      * @throws IOException if something goes wrong      */
name|void
name|checkTiffCompression
parameter_list|(
name|String
name|filename
parameter_list|,
name|String
name|expectedCompression
parameter_list|)
throws|throws
name|IOException
block|{
name|Iterator
name|readers
init|=
name|ImageIO
operator|.
name|getImageReadersBySuffix
argument_list|(
literal|"tiff"
argument_list|)
decl_stmt|;
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
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
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
name|Element
name|root
init|=
operator|(
name|Element
operator|)
name|imageMetadata
operator|.
name|getAsTree
argument_list|(
name|STANDARD_METADATA_FORMAT
argument_list|)
decl_stmt|;
name|Element
name|comprElement
init|=
operator|(
name|Element
operator|)
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"Compression"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Node
name|comprTypeNode
init|=
name|comprElement
operator|.
name|getElementsByTagName
argument_list|(
literal|"CompressionTypeName"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|actualCompression
init|=
name|comprTypeNode
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"value"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect TIFF compression in file "
operator|+
name|filename
argument_list|,
name|expectedCompression
argument_list|,
name|actualCompression
argument_list|)
expr_stmt|;
name|iis
operator|.
name|close
argument_list|()
expr_stmt|;
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|checkFileTypeByContent
parameter_list|(
name|String
name|filename
parameter_list|,
name|FileType
name|fileType
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedInputStream
name|bis
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|fileType
argument_list|,
name|FileTypeDetector
operator|.
name|detectFileType
argument_list|(
name|bis
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|bis
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

