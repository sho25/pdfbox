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
name|Paint
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
name|COSInteger
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
name|edit
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
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * Unit tests for PDInlineImage  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDInlineImageTest
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
comment|/**      * Tests PDInlineImage#PDInlineImage(COSDictionary parameters, byte[] data,      * Map<String, PDColorSpace> colorSpaces)      */
specifier|public
name|void
name|testInlineImage
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|dict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dict
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|IM
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|int
name|width
init|=
literal|31
decl_stmt|;
name|int
name|height
init|=
literal|27
decl_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|width
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|H
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|BPC
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|int
name|rowbytes
init|=
name|width
operator|/
literal|8
decl_stmt|;
if|if
condition|(
name|rowbytes
operator|*
literal|8
operator|<
name|width
condition|)
block|{
comment|// PDF spec:
comment|// If the number of data bits per row is not a multiple of 8,
comment|// the end of the row is padded with extra bits to fill out the last byte.
operator|++
name|rowbytes
expr_stmt|;
block|}
comment|// draw a grid
name|int
name|datalen
init|=
name|rowbytes
operator|*
name|height
decl_stmt|;
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|datalen
index|]
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
name|datalen
condition|;
operator|++
name|i
control|)
block|{
name|data
index|[
name|i
index|]
operator|=
operator|(
name|i
operator|/
literal|4
operator|%
literal|2
operator|==
literal|0
operator|)
condition|?
operator|(
name|byte
operator|)
name|Integer
operator|.
name|parseInt
argument_list|(
literal|"10101010"
argument_list|,
literal|2
argument_list|)
else|:
literal|0
expr_stmt|;
block|}
name|PDInlineImage
name|inlineImage1
init|=
operator|new
name|PDInlineImage
argument_list|(
name|dict
argument_list|,
name|data
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|inlineImage1
operator|.
name|isStencil
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|inlineImage1
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|inlineImage1
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inlineImage1
operator|.
name|getBitsPerComponent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|length
argument_list|,
name|inlineImage1
operator|.
name|getStream
argument_list|()
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|COSDictionary
name|dict2
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dict2
operator|.
name|addAll
argument_list|(
name|dict
argument_list|)
expr_stmt|;
comment|// use decode array to revert in image2
name|COSArray
name|decodeArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|decodeArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ONE
argument_list|)
expr_stmt|;
name|decodeArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|dict2
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE
argument_list|,
name|decodeArray
argument_list|)
expr_stmt|;
name|PDInlineImage
name|inlineImage2
init|=
operator|new
name|PDInlineImage
argument_list|(
name|dict2
argument_list|,
name|data
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Paint
name|paint
init|=
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|BufferedImage
name|stencilImage
init|=
name|inlineImage1
operator|.
name|getStencilImage
argument_list|(
name|paint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|stencilImage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|stencilImage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|stencilImage2
init|=
name|inlineImage2
operator|.
name|getStencilImage
argument_list|(
name|paint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|stencilImage2
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|stencilImage2
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|image1
init|=
name|inlineImage1
operator|.
name|getImage
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|image1
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|image1
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|image2
init|=
name|inlineImage2
operator|.
name|getImage
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|image2
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|image2
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
comment|// write and read
name|boolean
name|writeOk
init|=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image1
argument_list|,
literal|"png"
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|testResultsDir
operator|+
literal|"/inline-grid1.png"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|writeOk
argument_list|)
expr_stmt|;
name|BufferedImage
name|bim1
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|testResultsDir
operator|+
literal|"/inline-grid1.png"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bim1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|bim1
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|bim1
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|writeOk
operator|=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image2
argument_list|,
literal|"png"
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|testResultsDir
operator|+
literal|"/inline-grid2.png"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|writeOk
argument_list|)
expr_stmt|;
name|BufferedImage
name|bim2
init|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|File
argument_list|(
name|testResultsDir
operator|+
literal|"/inline-grid2.png"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bim2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|width
argument_list|,
name|bim2
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|height
argument_list|,
name|bim2
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
comment|// compare: pixels with even coordinates are white (FF), all others are black (0)
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
name|x
operator|%
literal|2
operator|==
literal|0
operator|&&
name|y
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
literal|0xFFFFFF
argument_list|,
name|bim1
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
name|bim1
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
comment|// compare: pixels with odd coordinates are white (FF), all others are black (0)
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
name|x
operator|%
literal|2
operator|==
literal|0
operator|&&
name|y
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bim2
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
literal|0xFFFFFF
argument_list|,
name|bim2
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
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
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
name|drawInlineImage
argument_list|(
name|inlineImage1
argument_list|,
literal|150
argument_list|,
literal|400
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawInlineImage
argument_list|(
name|inlineImage1
argument_list|,
literal|150
argument_list|,
literal|500
argument_list|,
name|inlineImage1
operator|.
name|getWidth
argument_list|()
operator|*
literal|2
argument_list|,
name|inlineImage1
operator|.
name|getHeight
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawInlineImage
argument_list|(
name|inlineImage1
argument_list|,
literal|150
argument_list|,
literal|600
argument_list|,
name|inlineImage1
operator|.
name|getWidth
argument_list|()
operator|*
literal|4
argument_list|,
name|inlineImage1
operator|.
name|getHeight
argument_list|()
operator|*
literal|4
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawInlineImage
argument_list|(
name|inlineImage2
argument_list|,
literal|350
argument_list|,
literal|400
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawInlineImage
argument_list|(
name|inlineImage2
argument_list|,
literal|350
argument_list|,
literal|500
argument_list|,
name|inlineImage2
operator|.
name|getWidth
argument_list|()
operator|*
literal|2
argument_list|,
name|inlineImage2
operator|.
name|getHeight
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawInlineImage
argument_list|(
name|inlineImage2
argument_list|,
literal|350
argument_list|,
literal|600
argument_list|,
name|inlineImage2
operator|.
name|getWidth
argument_list|()
operator|*
literal|4
argument_list|,
name|inlineImage2
operator|.
name|getHeight
argument_list|()
operator|*
literal|4
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
literal|"inline.pdf"
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
name|loadNonSeq
argument_list|(
name|pdfFile
argument_list|,
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

