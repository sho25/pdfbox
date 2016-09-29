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
name|FileInputStream
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
name|InputStream
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
name|common
operator|.
name|PDRectangle
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
name|PDPageContentStream
operator|.
name|AppendMode
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
name|validate
import|;
end_import

begin_comment
comment|/**  * Unit tests for CCITTFactory  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|CCITTFactoryTest
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
comment|/**      * Tests CCITTFactory#createFromRandomAccess(PDDocument document,      * RandomAccess reader) with a single page TIFF      */
specifier|public
name|void
name|testCreateFromRandomAccessSingle
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|tiffG3Path
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg3.tif"
decl_stmt|;
name|String
name|tiffG4Path
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg4.tif"
decl_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDImageXObject
name|ximage3
init|=
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|document
argument_list|,
operator|new
name|File
argument_list|(
name|tiffG3Path
argument_list|)
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage3
argument_list|,
literal|1
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"tiff"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|bim3
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|tiffG3Path
argument_list|)
argument_list|)
decl_stmt|;
name|checkIdent
argument_list|(
name|bim3
argument_list|,
name|ximage3
operator|.
name|getOpaqueImage
argument_list|()
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
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
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage3
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ximage3
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage3
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
name|PDImageXObject
name|ximage4
init|=
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|document
argument_list|,
operator|new
name|File
argument_list|(
name|tiffG4Path
argument_list|)
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage4
argument_list|,
literal|1
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"tiff"
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|bim4
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|tiffG3Path
argument_list|)
argument_list|)
decl_stmt|;
name|checkIdent
argument_list|(
name|bim4
argument_list|,
name|ximage4
operator|.
name|getOpaqueImage
argument_list|()
argument_list|)
expr_stmt|;
name|page
operator|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
expr_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|contentStream
operator|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage4
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|testResultsDir
operator|+
literal|"/singletiff.pdf"
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
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"singletiff.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests CCITTFactory#createFromRandomAccess(PDDocument document,      * RandomAccess reader) with a multi page TIFF      */
specifier|public
name|void
name|testCreateFromRandomAccessMulti
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|tiffPath
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg4multi.tif"
decl_stmt|;
name|ImageInputStream
name|is
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|tiffPath
argument_list|)
argument_list|)
decl_stmt|;
name|ImageReader
name|imageReader
init|=
name|ImageIO
operator|.
name|getImageReaders
argument_list|(
name|is
argument_list|)
operator|.
name|next
argument_list|()
decl_stmt|;
name|imageReader
operator|.
name|setInput
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|int
name|countTiffImages
init|=
name|imageReader
operator|.
name|getNumImages
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|countTiffImages
operator|>
literal|1
argument_list|)
expr_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|int
name|pdfPageNum
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|PDImageXObject
name|ximage
init|=
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|document
argument_list|,
operator|new
name|File
argument_list|(
name|tiffPath
argument_list|)
argument_list|,
name|pdfPageNum
argument_list|)
decl_stmt|;
if|if
condition|(
name|ximage
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|BufferedImage
name|bim
init|=
name|imageReader
operator|.
name|read
argument_list|(
name|pdfPageNum
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage
argument_list|,
literal|1
argument_list|,
name|bim
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bim
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|"tiff"
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
name|bim
argument_list|,
name|ximage
operator|.
name|getOpaqueImage
argument_list|()
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|float
name|fX
init|=
name|ximage
operator|.
name|getWidth
argument_list|()
operator|/
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|fY
init|=
name|ximage
operator|.
name|getHeight
argument_list|()
operator|/
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|float
name|factor
init|=
name|Math
operator|.
name|max
argument_list|(
name|fX
argument_list|,
name|fY
argument_list|)
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
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ximage
operator|.
name|getWidth
argument_list|()
operator|/
name|factor
argument_list|,
name|ximage
operator|.
name|getHeight
argument_list|()
operator|/
name|factor
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
operator|++
name|pdfPageNum
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|countTiffImages
argument_list|,
name|pdfPageNum
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|testResultsDir
operator|+
literal|"/multitiff.pdf"
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
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"multitiff.pdf"
argument_list|)
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|countTiffImages
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|imageReader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateFromBufferedImage
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|tiffG4Path
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg4.tif"
decl_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|bim
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|tiffG4Path
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage3
init|=
name|CCITTFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|bim
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage3
argument_list|,
literal|1
argument_list|,
literal|344
argument_list|,
literal|287
argument_list|,
literal|"tiff"
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
name|bim
argument_list|,
name|ximage3
operator|.
name|getOpaqueImage
argument_list|()
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
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
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage3
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ximage3
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage3
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
name|document
operator|.
name|save
argument_list|(
name|testResultsDir
operator|+
literal|"/singletifffrombi.pdf"
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
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"singletifffrombi.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateFromBufferedChessImage
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
name|bim
init|=
operator|new
name|BufferedImage
argument_list|(
literal|343
argument_list|,
literal|287
argument_list|,
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
name|bim
operator|.
name|getWidth
argument_list|()
operator|/
literal|8
operator|)
operator|*
literal|8
operator|!=
name|bim
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
comment|// not mult of 8
name|int
name|col
init|=
literal|0
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
name|bim
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
name|bim
operator|.
name|getHeight
argument_list|()
condition|;
operator|++
name|y
control|)
block|{
name|bim
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|col
operator|&
literal|0xFFFFFF
argument_list|)
expr_stmt|;
name|col
operator|=
operator|~
name|col
expr_stmt|;
block|}
block|}
name|PDImageXObject
name|ximage3
init|=
name|CCITTFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|bim
argument_list|)
decl_stmt|;
name|validate
argument_list|(
name|ximage3
argument_list|,
literal|1
argument_list|,
literal|343
argument_list|,
literal|287
argument_list|,
literal|"tiff"
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
name|bim
argument_list|,
name|ximage3
operator|.
name|getOpaqueImage
argument_list|()
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
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
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage3
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ximage3
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage3
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
name|document
operator|.
name|save
argument_list|(
name|testResultsDir
operator|+
literal|"/singletifffromchessbi.pdf"
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
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"singletifffromchessbi.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests that CCITTFactory#createFromFile(PDDocument document, File file) doesn't lock the      * source file      */
specifier|public
name|void
name|testCreateFromFileLock
parameter_list|()
throws|throws
name|IOException
block|{
comment|// copy the source file to a temp directory, as we will be deleting it
name|String
name|tiffG3Path
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg3.tif"
decl_stmt|;
name|File
name|copiedTiffFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"ccittg3.tif"
argument_list|)
decl_stmt|;
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
name|tiffG3Path
argument_list|)
argument_list|,
name|copiedTiffFile
argument_list|)
expr_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|document
argument_list|,
name|copiedTiffFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copiedTiffFile
operator|.
name|delete
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that CCITTFactory#createFromFile(PDDocument document, File file, int number) doesn't      * lock the source file      */
specifier|public
name|void
name|testCreateFromFileNumberLock
parameter_list|()
throws|throws
name|IOException
block|{
comment|// copy the source file to a temp directory, as we will be deleting it
name|String
name|tiffG3Path
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/ccittg3.tif"
decl_stmt|;
name|File
name|copiedTiffFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"ccittg3n.tif"
argument_list|)
decl_stmt|;
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
name|tiffG3Path
argument_list|)
argument_list|,
name|copiedTiffFile
argument_list|)
expr_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|document
argument_list|,
name|copiedTiffFile
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copiedTiffFile
operator|.
name|delete
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|copyFile
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

