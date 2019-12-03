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
name|ByteArrayInputStream
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertFalse
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
name|assertNotNull
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
name|assertNull
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
name|assertTrue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|PNGConverterTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
block|{
comment|//noinspection ResultOfMethodCallIgnored
name|parentDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * This "test" just dumps the list of constants for the PNGConverter CHUNK_??? types, so that      * it can just be copy&pasted into the PNGConverter class.      */
comment|//@Test
specifier|public
name|void
name|dumpChunkTypes
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|chunkTypes
init|=
block|{
literal|"IHDR"
block|,
literal|"IDAT"
block|,
literal|"PLTE"
block|,
literal|"IEND"
block|,
literal|"tRNS"
block|,
literal|"cHRM"
block|,
literal|"gAMA"
block|,
literal|"iCCP"
block|,
literal|"sBIT"
block|,
literal|"sRGB"
block|,
literal|"tEXt"
block|,
literal|"zTXt"
block|,
literal|"iTXt"
block|,
literal|"kBKG"
block|,
literal|"hIST"
block|,
literal|"pHYs"
block|,
literal|"sPLT"
block|,
literal|"tIME"
block|}
decl_stmt|;
for|for
control|(
name|String
name|chunkType
range|:
name|chunkTypes
control|)
block|{
name|byte
index|[]
name|bytes
init|=
name|chunkType
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"\tprivate static final int CHUNK_"
operator|+
name|chunkType
operator|+
literal|" = 0x%02X%02X%02X%02X; // %s: %d %d %d %d"
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|1
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|2
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|3
index|]
operator|&
literal|0xFF
argument_list|,
name|chunkType
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|1
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|2
index|]
operator|&
literal|0xFF
argument_list|,
operator|(
name|int
operator|)
name|bytes
index|[
literal|3
index|]
operator|&
literal|0xFF
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGB
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBGamma
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_rgb_gamma.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGB16BitICC
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_rgb_romm_16bit.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBIndexed
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_indexed.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBIndexedAlpha1Bit
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_indexed_1bit_alpha.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBIndexedAlpha2Bit
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_indexed_2bit_alpha.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBIndexedAlpha4Bit
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_indexed_4bit_alpha.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBIndexedAlpha8Bit
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvert
argument_list|(
literal|"png_indexed_8bit_alpha.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionRGBAlpha
parameter_list|()
throws|throws
name|IOException
block|{
comment|// We can't handle Alpha RGB
name|checkImageConvertFail
argument_list|(
literal|"png_alpha_rgb.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionGrayAlpha
parameter_list|()
throws|throws
name|IOException
block|{
comment|// We can't handle Alpha RGB
name|checkImageConvertFail
argument_list|(
literal|"png_alpha_gray.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionGray
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvertFail
argument_list|(
literal|"png_gray.png"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImageConversionGrayGamma
parameter_list|()
throws|throws
name|IOException
block|{
name|checkImageConvertFail
argument_list|(
literal|"png_gray_with_gama.png"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|File
name|parentDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output/graphics/graphics"
argument_list|)
decl_stmt|;
specifier|private
name|void
name|checkImageConvertFail
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|byte
index|[]
name|imageBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|PNGConverterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|pdImageXObject
init|=
name|PNGConverter
operator|.
name|convertPNGImage
argument_list|(
name|doc
argument_list|,
name|imageBytes
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|pdImageXObject
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkImageConvert
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|byte
index|[]
name|imageBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|PNGConverterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|pdImageXObject
init|=
name|PNGConverter
operator|.
name|convertPNGImage
argument_list|(
name|doc
argument_list|,
name|imageBytes
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pdImageXObject
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|doc
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
name|doc
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|PINK
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|addRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getWidth
argument_list|()
argument_list|,
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|fill
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|pdImageXObject
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|pdImageXObject
operator|.
name|getWidth
argument_list|()
argument_list|,
name|pdImageXObject
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
name|doc
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|parentDir
argument_list|,
name|name
operator|+
literal|".pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
init|=
name|pdImageXObject
operator|.
name|getImage
argument_list|()
decl_stmt|;
name|checkIdent
argument_list|(
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|imageBytes
argument_list|)
argument_list|)
argument_list|,
name|image
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCheckConverterState
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|PNGConverter
operator|.
name|PNGConverterState
name|state
init|=
operator|new
name|PNGConverter
operator|.
name|PNGConverterState
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|PNGConverter
operator|.
name|Chunk
name|invalidChunk
init|=
operator|new
name|PNGConverter
operator|.
name|Chunk
argument_list|()
decl_stmt|;
name|invalidChunk
operator|.
name|bytes
operator|=
operator|new
name|byte
index|[
literal|0
index|]
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|invalidChunk
argument_list|)
argument_list|)
expr_stmt|;
comment|// Valid Dummy Chunk
name|PNGConverter
operator|.
name|Chunk
name|validChunk
init|=
operator|new
name|PNGConverter
operator|.
name|Chunk
argument_list|()
decl_stmt|;
name|validChunk
operator|.
name|bytes
operator|=
operator|new
name|byte
index|[
literal|16
index|]
expr_stmt|;
name|validChunk
operator|.
name|start
operator|=
literal|4
expr_stmt|;
name|validChunk
operator|.
name|length
operator|=
literal|8
expr_stmt|;
name|validChunk
operator|.
name|crc
operator|=
literal|2077607535
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|validChunk
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IHDR
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IDATs
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|validChunk
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IHDR
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IDATs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IDATs
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|validChunk
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|PLTE
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|PLTE
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|cHRM
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|cHRM
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|tRNS
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|tRNS
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|iCCP
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|iCCP
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|sRGB
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|sRGB
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|gAMA
operator|=
name|invalidChunk
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|gAMA
operator|=
name|validChunk
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|IDATs
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|validChunk
argument_list|,
name|invalidChunk
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkConverterState
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testChunkSane
parameter_list|()
block|{
name|PNGConverter
operator|.
name|Chunk
name|chunk
init|=
operator|new
name|PNGConverter
operator|.
name|Chunk
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|bytes
operator|=
literal|"IHDRsomedummyvaluesDummyValuesAtEnd"
operator|.
name|getBytes
argument_list|()
expr_stmt|;
name|chunk
operator|.
name|length
operator|=
literal|19
expr_stmt|;
name|assertEquals
argument_list|(
name|chunk
operator|.
name|bytes
operator|.
name|length
argument_list|,
literal|35
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"IHDRsomedummyvalues"
argument_list|,
operator|new
name|String
argument_list|(
name|chunk
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|start
operator|=
literal|4
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somedummyvaluesDumm"
argument_list|,
operator|new
name|String
argument_list|(
name|chunk
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|crc
operator|=
operator|-
literal|1729802258
expr_stmt|;
name|assertTrue
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|start
operator|=
literal|6
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|length
operator|=
literal|60
expr_stmt|;
name|assertFalse
argument_list|(
name|PNGConverter
operator|.
name|checkChunkSane
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCRCImpl
parameter_list|()
block|{
name|byte
index|[]
name|b1
init|=
literal|"Hello World!"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|472456355
argument_list|,
name|PNGConverter
operator|.
name|crc
argument_list|(
name|b1
argument_list|,
literal|0
argument_list|,
name|b1
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|632335482
argument_list|,
name|PNGConverter
operator|.
name|crc
argument_list|(
name|b1
argument_list|,
literal|2
argument_list|,
name|b1
operator|.
name|length
operator|-
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapPNGRenderIntent
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|COSName
operator|.
name|PERCEPTUAL
argument_list|,
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|RELATIVE_COLORIMETRIC
argument_list|,
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|SATURATION
argument_list|,
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|ABSOLUTE_COLORIMETRIC
argument_list|,
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|PNGConverter
operator|.
name|mapPNGRenderIntent
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

