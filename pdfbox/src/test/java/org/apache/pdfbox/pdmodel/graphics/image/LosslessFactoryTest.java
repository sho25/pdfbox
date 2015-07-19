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
name|Graphics
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
name|GraphicsConfiguration
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
name|util
operator|.
name|Random
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
name|PDPage
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
name|PDPageContentStream
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
name|checkIdent
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

begin_comment
comment|/**  * Unit tests for LosslessFactory  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|LosslessFactoryTest
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
comment|/**      * Tests RGB LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image)      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromImageRGB
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"png.png"
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage1
init|=
name|LosslessFactory
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
name|ximage1
argument_list|,
literal|8
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
literal|"png"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|image
argument_list|,
name|ximage1
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a grayscale image
name|BufferedImage
name|grayImage
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
name|Graphics
name|g
init|=
name|grayImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|g
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
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|PDImageXObject
name|ximage2
init|=
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|grayImage
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage2
argument_list|,
literal|8
argument_list|,
name|grayImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|grayImage
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|grayImage
argument_list|,
name|ximage2
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a bitonal image
name|BufferedImage
name|bitonalImage
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
name|TYPE_BYTE_BINARY
argument_list|)
decl_stmt|;
comment|// avoid multiple of 8 to test padding
name|assertFalse
argument_list|(
name|bitonalImage
operator|.
name|getWidth
argument_list|()
operator|%
literal|8
operator|==
literal|0
argument_list|)
expr_stmt|;
name|g
operator|=
name|bitonalImage
operator|.
name|getGraphics
argument_list|()
expr_stmt|;
name|g
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
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|PDImageXObject
name|ximage3
init|=
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|bitonalImage
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage3
argument_list|,
literal|1
argument_list|,
name|bitonalImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bitonalImage
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|bitonalImage
argument_list|,
name|ximage3
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
comment|// This part isn't really needed because this test doesn't break
comment|// if the mask has the wrong colorspace (PDFBOX-2057), but it is still useful
comment|// if something goes wrong in the future and we want to have a PDF to open.
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage1
argument_list|,
literal|200
argument_list|,
literal|300
argument_list|,
name|ximage1
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|ximage1
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage2
argument_list|,
literal|200
argument_list|,
literal|450
argument_list|,
name|ximage2
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|ximage2
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage3
argument_list|,
literal|200
argument_list|,
literal|600
argument_list|,
name|ximage3
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|ximage3
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"misc.pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
operator|.
name|renderImage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests INT_ARGB LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image)      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromImageINT_ARGB
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"png.png"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create an ARGB image
name|int
name|w
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
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
name|w
argument_list|,
name|h
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
name|LosslessFactory
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
name|argbImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|argbImage
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdentRGB
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getOpaqueImage
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
name|argbImage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|argbImage
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|"png"
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
literal|"intargb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests INT_ARGB LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image) with BITMASK transparency      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromImageBITMASK_INT_ARGB
parameter_list|()
throws|throws
name|IOException
block|{
name|doBitmaskTransparencyTest
argument_list|(
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|,
literal|"bitmaskintargb.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests 4BYTE_ABGR LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image) with BITMASK transparency      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromImageBITMASK4BYTE_ABGR
parameter_list|()
throws|throws
name|IOException
block|{
name|doBitmaskTransparencyTest
argument_list|(
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|,
literal|"bitmask4babgr.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests 4BYTE_ABGR LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image)      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromImage4BYTE_ABGR
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"png.png"
argument_list|)
argument_list|)
decl_stmt|;
comment|// create an ARGB image
name|int
name|w
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
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
name|w
argument_list|,
name|h
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
name|LosslessFactory
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
name|w
argument_list|,
name|h
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdentRGB
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getOpaqueImage
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
name|w
argument_list|,
name|h
argument_list|,
literal|"png"
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
literal|"4babgr.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image) with transparent GIF      *      * @throws java.io.IOException      */
specifier|public
name|void
name|testCreateLosslessFromTransparentGIF
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"gif.gif"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Transparency
operator|.
name|BITMASK
argument_list|,
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getTransparency
argument_list|()
argument_list|)
expr_stmt|;
name|PDImageXObject
name|ximage
init|=
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
decl_stmt|;
name|int
name|w
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|8
argument_list|,
name|w
argument_list|,
name|h
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|image
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdentRGB
argument_list|(
name|image
argument_list|,
name|ximage
operator|.
name|getOpaqueImage
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
literal|1
argument_list|,
name|w
argument_list|,
name|h
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
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
literal|"gif.pdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check whether the RGB part of images are identical.      *      * @param expectedImage      * @param actualImage      */
specifier|private
name|void
name|checkIdentRGB
parameter_list|(
name|BufferedImage
name|expectedImage
parameter_list|,
name|BufferedImage
name|actualImage
parameter_list|)
block|{
name|String
name|errMsg
init|=
literal|""
decl_stmt|;
name|int
name|w
init|=
name|expectedImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
init|=
name|expectedImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|w
argument_list|,
name|actualImage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|h
argument_list|,
name|actualImage
operator|.
name|getHeight
argument_list|()
argument_list|)
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
if|if
condition|(
operator|(
name|expectedImage
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
operator|!=
operator|(
name|actualImage
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
condition|)
block|{
name|errMsg
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"(%d,%d) %06X != %06X"
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|expectedImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|,
name|actualImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|errMsg
argument_list|,
name|expectedImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|,
name|actualImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|doBitmaskTransparencyTest
parameter_list|(
name|int
name|imageType
parameter_list|,
name|String
name|pdfFilename
parameter_list|)
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
name|int
name|width
init|=
literal|257
decl_stmt|;
name|int
name|height
init|=
literal|256
decl_stmt|;
comment|// create an ARGB image
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
name|imageType
argument_list|)
decl_stmt|;
comment|// from there, create an image with Transparency.BITMASK
name|Graphics2D
name|g
init|=
name|argbImage
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|GraphicsConfiguration
name|gc
init|=
name|g
operator|.
name|getDeviceConfiguration
argument_list|()
decl_stmt|;
name|argbImage
operator|=
name|gc
operator|.
name|createCompatibleImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|Transparency
operator|.
name|BITMASK
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
comment|// create a red rectangle
name|g
operator|=
name|argbImage
operator|.
name|createGraphics
argument_list|()
expr_stmt|;
name|g
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|red
argument_list|)
expr_stmt|;
name|g
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|random
operator|.
name|setSeed
argument_list|(
literal|12345
argument_list|)
expr_stmt|;
comment|// create a transparency cross: only pixels in the
comment|// interval max/2 - max/8 ... max/2 + max/8 will be visible
name|int
name|startX
init|=
name|width
operator|/
literal|2
operator|-
name|width
operator|/
literal|8
decl_stmt|;
name|int
name|endX
init|=
name|width
operator|/
literal|2
operator|+
name|width
operator|/
literal|8
decl_stmt|;
name|int
name|startY
init|=
name|height
operator|/
literal|2
operator|-
name|height
operator|/
literal|8
decl_stmt|;
name|int
name|endY
init|=
name|height
operator|/
literal|2
operator|+
name|height
operator|/
literal|8
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
name|width
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
name|height
condition|;
operator|++
name|y
control|)
block|{
comment|// create pseudorandom alpha values, but those within the cross
comment|// must be>= 128 and those outside must be< 128
name|int
name|alpha
decl_stmt|;
if|if
condition|(
operator|(
name|x
operator|>=
name|startX
operator|&&
name|x
operator|<=
name|endX
operator|)
operator|||
name|y
operator|>=
name|startY
operator|&&
name|y
operator|<=
name|endY
condition|)
block|{
name|alpha
operator|=
literal|128
operator|+
call|(
name|int
call|)
argument_list|(
name|random
operator|.
name|nextFloat
argument_list|()
operator|*
literal|127
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|alpha
operator|>=
literal|128
argument_list|)
expr_stmt|;
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
name|alpha
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|255
argument_list|,
name|argbImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|>>>
literal|24
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|alpha
operator|=
call|(
name|int
call|)
argument_list|(
name|random
operator|.
name|nextFloat
argument_list|()
operator|*
literal|127
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|alpha
operator|<
literal|128
argument_list|)
expr_stmt|;
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
name|alpha
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|argbImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|>>>
literal|24
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|PDImageXObject
name|ximage
init|=
name|LosslessFactory
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
literal|"png"
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdent
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|checkIdentRGB
argument_list|(
name|argbImage
argument_list|,
name|ximage
operator|.
name|getOpaqueImage
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
literal|1
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|"png"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
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
argument_list|)
expr_stmt|;
comment|// check whether the mask is a b/w cross
name|BufferedImage
name|maskImage
init|=
name|ximage
operator|.
name|getSoftMask
argument_list|()
operator|.
name|getImage
argument_list|()
decl_stmt|;
comment|// avoid multiple of 8 to test padding
name|assertFalse
argument_list|(
name|maskImage
operator|.
name|getWidth
argument_list|()
operator|%
literal|8
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Transparency
operator|.
name|OPAQUE
argument_list|,
name|maskImage
operator|.
name|getTransparency
argument_list|()
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
name|width
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
name|height
condition|;
operator|++
name|y
control|)
block|{
if|if
condition|(
operator|(
name|x
operator|>=
name|startX
operator|&&
name|x
operator|<=
name|endX
operator|)
operator|||
name|y
operator|>=
name|startY
operator|&&
name|y
operator|<=
name|endY
condition|)
block|{
name|assertEquals
argument_list|(
literal|0xFFFFFF
argument_list|,
name|maskImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|maskImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|0xFFFFFF
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// This part isn't really needed because this test doesn't break
comment|// if the mask has the wrong colorspace (PDFBOX-2057), but it is still useful
comment|// if something goes wrong in the future and we want to have a PDF to open.
comment|// Create a rectangle
name|BufferedImage
name|rectImage
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
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|g
operator|=
name|rectImage
operator|.
name|createGraphics
argument_list|()
expr_stmt|;
name|g
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|blue
argument_list|)
expr_stmt|;
name|g
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|PDImageXObject
name|ximage2
init|=
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|rectImage
argument_list|)
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage2
argument_list|,
literal|150
argument_list|,
literal|300
argument_list|,
name|ximage2
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage2
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage
argument_list|,
literal|150
argument_list|,
literal|300
argument_list|,
name|ximage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
name|pdfFilename
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
operator|.
name|renderImage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

