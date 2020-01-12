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
name|Graphics
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
name|ByteArrayOutputStream
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
name|util
operator|.
name|Arrays
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
name|pdfparser
operator|.
name|PDFParser
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
name|ValidateXImage
operator|.
name|colorCount
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
name|ValidateXImage
operator|.
name|doWritePDF
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
name|ValidateXImage
operator|.
name|validate
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_comment
comment|/**  * Unit tests for JPEGFactory  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|JPEGFactoryTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
name|File
name|testResultsDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output/graphics"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testResultsDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests JPEGFactory#createFromStream(PDDocument document, InputStream      * stream) with color JPEG file      */
specifier|public
name|void
name|testCreateFromStream
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|document
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpegrgbstream.pdf"
argument_list|)
expr_stmt|;
name|checkJpegStream
argument_list|(
name|testResultsDir
argument_list|,
literal|"jpegrgbstream.pdf"
argument_list|,
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Tests JPEGFactory#createFromStream(PDDocument document, InputStream      * stream) with CMYK color JPEG file      */
specifier|public
name|void
name|testCreateFromStreamCMYK
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpegcmyk.jpg"
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|document
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
literal|343
argument_list|,
literal|287
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceCMYK
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpegcmykstream.pdf"
argument_list|)
expr_stmt|;
name|checkJpegStream
argument_list|(
name|testResultsDir
argument_list|,
literal|"jpegcmykstream.pdf"
argument_list|,
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpegcmyk.jpg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests JPEGFactory#createFromStream(PDDocument document, InputStream      * stream) with gray JPEG file      */
specifier|public
name|void
name|testCreateFromStream256
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg256.jpg"
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|document
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpeg256stream.pdf"
argument_list|)
expr_stmt|;
name|checkJpegStream
argument_list|(
name|testResultsDir
argument_list|,
literal|"jpeg256stream.pdf"
argument_list|,
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg256.jpg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests RGB JPEGFactory#createFromImage(PDDocument document, BufferedImage      * image) with color JPEG image      */
specifier|public
name|void
name|testCreateFromImageRGB
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getNumComponents
argument_list|()
argument_list|)
expr_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpegrgb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests RGB JPEGFactory#createFromImage(PDDocument document, BufferedImage      * image) with gray JPEG image      */
specifier|public
name|void
name|testCreateFromImage256
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg256.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getNumComponents
argument_list|()
argument_list|)
expr_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpeg256.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests ARGB JPEGFactory#createFromImage(PDDocument document, BufferedImage      * image)      */
specifier|public
name|void
name|testCreateFromImageINT_ARGB
parameter_list|()
throws|throws
name|IOException
block|{
comment|// workaround Open JDK bug
comment|// http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7044758
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.runtime.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"OpenJDK Runtime Environment"
argument_list|)
operator|&&
operator|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.6"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.7"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.8"
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create an ARGB image
name|int
name|width
init|=
name|image
operator|.
name|getWidth
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
name|BufferedImage
name|argbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|Graphics
name|ag
init|=
name|argbImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|ag
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ag
operator|.
name|dispose
argument_list|()
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
name|argbImage
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
name|argbImage
operator|.
name|getHeight
argument_list|()
condition|;
operator|++
name|y
control|)
block|{
name|argbImage
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
operator|(
name|argbImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
operator|)
operator||
operator|(
operator|(
name|y
operator|/
literal|10
operator|*
literal|10
operator|)
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|argbImage
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
argument_list|,
literal|8
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|colorCount
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
operator|.
name|getImage
argument_list|()
argument_list|)
operator|>
name|image
operator|.
name|getHeight
argument_list|()
operator|/
literal|10
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpeg-intargb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests ARGB JPEGFactory#createFromImage(PDDocument document, BufferedImage      * image)      */
specifier|public
name|void
name|testCreateFromImage4BYTE_ABGR
parameter_list|()
throws|throws
name|IOException
block|{
comment|// workaround Open JDK bug
comment|// http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7044758
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.runtime.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"OpenJDK Runtime Environment"
argument_list|)
operator|&&
operator|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.6"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.7"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.8"
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create an ARGB image
name|int
name|width
init|=
name|image
operator|.
name|getWidth
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
name|BufferedImage
name|argbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_4BYTE_ABGR
argument_list|)
decl_stmt|;
name|Graphics
name|ag
init|=
name|argbImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|ag
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ag
operator|.
name|dispose
argument_list|()
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
name|argbImage
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
name|argbImage
operator|.
name|getHeight
argument_list|()
condition|;
operator|++
name|y
control|)
block|{
name|argbImage
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
operator|(
name|argbImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
operator|)
operator||
operator|(
operator|(
name|y
operator|/
literal|10
operator|*
literal|10
operator|)
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|argbImage
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
argument_list|,
literal|8
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|colorCount
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
operator|.
name|getImage
argument_list|()
argument_list|)
operator|>
name|image
operator|.
name|getHeight
argument_list|()
operator|/
literal|10
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpeg-4bargb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests USHORT_555_RGB JPEGFactory#createFromImage(PDDocument document, BufferedImage      * image), see also PDFBOX-4674.      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateFromImageUSHORT_555_RGB
parameter_list|()
throws|throws
name|IOException
block|{
comment|// workaround Open JDK bug
comment|// http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7044758
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.runtime.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"OpenJDK Runtime Environment"
argument_list|)
operator|&&
operator|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.6"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.7"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.8"
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"jpeg.jpg"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create an USHORT_555_RGB image
name|int
name|width
init|=
name|image
operator|.
name|getWidth
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
name|BufferedImage
name|rgbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_USHORT_555_RGB
argument_list|)
decl_stmt|;
name|Graphics
name|ag
init|=
name|rgbImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|ag
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ag
operator|.
name|dispose
argument_list|()
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
name|rgbImage
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
name|rgbImage
operator|.
name|getHeight
argument_list|()
condition|;
operator|++
name|y
control|)
block|{
name|rgbImage
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
operator|(
name|rgbImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
operator|)
operator||
operator|(
operator|(
name|y
operator|/
literal|10
operator|*
literal|10
operator|)
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|rgbImage
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"jpg"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ximage
operator|.
name|getSoftMask
argument_list|()
argument_list|)
expr_stmt|;
name|doWritePDF
argument_list|(
name|document
argument_list|,
name|ximage
argument_list|,
name|testResultsDir
argument_list|,
literal|"jpeg-ushort555rgb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|// check whether it is possible to extract the jpeg stream exactly
comment|// as it was passed to createFromStream
specifier|private
name|void
name|checkJpegStream
parameter_list|(
name|File
name|testResultsDir
parameter_list|,
name|String
name|filename
parameter_list|,
name|InputStream
name|resourceStream
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
name|filename
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|img
init|=
operator|(
name|PDImageXObject
operator|)
name|doc
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getResources
argument_list|()
operator|.
name|getXObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Im1"
argument_list|)
argument_list|)
decl_stmt|;
name|InputStream
name|dctStream
init|=
name|img
operator|.
name|createInputStream
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos1
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos2
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|resourceStream
argument_list|,
name|baos1
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|dctStream
argument_list|,
name|baos2
argument_list|)
expr_stmt|;
name|resourceStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|dctStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|baos1
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|baos2
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

