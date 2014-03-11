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
name|Iterator
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
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|fail
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
comment|/**      * Validate page rendering for all supported image formats (JDK5).      * @param file The file to validate      * @param outDir Name of the output directory      * @throws IOException when there is an exception      */
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
literal|120
decl_stmt|;
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|file
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
name|RGB
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
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
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|imageFormat
argument_list|,
name|fileName
argument_list|,
name|Math
operator|.
name|round
argument_list|(
name|dpi
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test to validate image rendering of file set.      * @throws Exception when there is an exception      */
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
name|mkdir
argument_list|()
expr_stmt|;
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
comment|/**      * Get the compression of a TIFF file      *      * @param filename Filename      * @return the TIFF compression      *      * @throws IOException      */
name|String
name|getTiffCompression
parameter_list|(
name|String
name|filename
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
name|compression
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
return|return
name|compression
return|;
block|}
block|}
end_class

end_unit

