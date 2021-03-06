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
name|rendering
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Loader
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
name|ParallelParameterized
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_comment
comment|/**  * Test suite for rendering.  *  * FILE SET VALIDATION  *  * This test is designed to test PDFToImage using a set of PDF files and known good output for  * each. The default mode is to process all *.pdf and *.ai files in  * "src/test/resources/input/rendering". An output file is created in "target/test-output/rendering"  * with the same name as the PDF file, plus an additional page number and ".png" suffix.  *  * The output file is then tested against a known good result file from the input directory (again,  * with the same name as the tested PDF file, but with the additional page number and ".png"  * suffix).  *  * If the two aren't identical, a graphical .diff.png file is created. If they are identical, the  * output .png file is deleted. If a "good result" file doesn't exist, the output .png file is left  * there for human inspection.  *  * Errors are flagged by creating empty files with appropriate names in the target directory.  *  * @author Daniel Wilson  * @author Ben Litchfield  * @author Tilman Hausherr  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ParallelParameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|TestPDFToImage
block|{
comment|/**      * Logger instance.      */
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
name|TestPDFToImage
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
name|String
name|inDir
init|=
literal|"src/test/resources/input/rendering"
decl_stmt|;
specifier|static
name|String
name|outDir
init|=
literal|"target/test-output/rendering/"
decl_stmt|;
name|String
name|filename
decl_stmt|;
annotation|@
name|Parameterized
operator|.
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|data
parameter_list|()
block|{
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
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
operator|||
name|name
operator|.
name|toLowerCase
argument_list|()
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
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|testFiles
control|)
block|{
name|params
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
name|file
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|params
return|;
block|}
comment|/**      * Test class constructor.      *      * @param filename The name of the test class.      *      * @throws IOException If there is an error creating the test.      */
specifier|public
name|TestPDFToImage
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
block|}
comment|/**      * Test to validate image rendering of file.      *      * @throws IOException when there is an exception      */
annotation|@
name|Test
specifier|public
name|void
name|testRenderImage
parameter_list|()
throws|throws
name|IOException
block|{
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
name|doTestFile
argument_list|(
operator|new
name|File
argument_list|(
name|inDir
argument_list|,
name|filename
argument_list|)
argument_list|,
name|inDir
argument_list|,
name|outDir
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"failure, see test log for details"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create an image; the part between the smaller and the larger image is painted black, the rest      * in white      *      * @param minWidth width of the smaller image      * @param minHeight width of the smaller image      * @param maxWidth height of the larger image      * @param maxHeight height of the larger image      *      * @return      */
specifier|private
name|BufferedImage
name|createEmptyDiffImage
parameter_list|(
name|int
name|minWidth
parameter_list|,
name|int
name|minHeight
parameter_list|,
name|int
name|maxWidth
parameter_list|,
name|int
name|maxHeight
parameter_list|)
block|{
name|BufferedImage
name|bim3
init|=
operator|new
name|BufferedImage
argument_list|(
name|maxWidth
argument_list|,
name|maxHeight
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|Graphics
name|graphics
init|=
name|bim3
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
if|if
condition|(
name|minWidth
operator|!=
name|maxWidth
operator|||
name|minHeight
operator|!=
name|maxHeight
condition|)
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|maxWidth
argument_list|,
name|maxHeight
argument_list|)
expr_stmt|;
block|}
name|graphics
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|minWidth
argument_list|,
name|minHeight
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|bim3
return|;
block|}
comment|/**      * Get the difference between two images, identical colors are set to white, differences are      * xored, the highest bit of each color is reset to avoid colors that are too light.      *      * @param bim1      * @param bim2      * @return If the images are different, the function returns a diff image. If the images are      * identical, the function returns null. If the size is different, a black border on the bottom      * at the right is created.      *      * @throws IOException      */
specifier|private
name|BufferedImage
name|diffImages
parameter_list|(
name|BufferedImage
name|bim1
parameter_list|,
name|BufferedImage
name|bim2
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|minWidth
init|=
name|Math
operator|.
name|min
argument_list|(
name|bim1
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bim2
operator|.
name|getWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|minHeight
init|=
name|Math
operator|.
name|min
argument_list|(
name|bim1
operator|.
name|getHeight
argument_list|()
argument_list|,
name|bim2
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|maxWidth
init|=
name|Math
operator|.
name|max
argument_list|(
name|bim1
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bim2
operator|.
name|getWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|maxHeight
init|=
name|Math
operator|.
name|max
argument_list|(
name|bim1
operator|.
name|getHeight
argument_list|()
argument_list|,
name|bim2
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|BufferedImage
name|bim3
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|minWidth
operator|!=
name|maxWidth
operator|||
name|minHeight
operator|!=
name|maxHeight
condition|)
block|{
name|bim3
operator|=
name|createEmptyDiffImage
argument_list|(
name|minWidth
argument_list|,
name|minHeight
argument_list|,
name|maxWidth
argument_list|,
name|maxHeight
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|minWidth
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
name|minHeight
condition|;
operator|++
name|y
control|)
block|{
name|int
name|rgb1
init|=
name|bim1
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|int
name|rgb2
init|=
name|bim2
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
if|if
condition|(
name|rgb1
operator|!=
name|rgb2
comment|// don't bother about small differences
operator|&&
operator|(
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|rgb1
operator|&
literal|0xFF
operator|)
operator|-
operator|(
name|rgb2
operator|&
literal|0xFF
operator|)
argument_list|)
operator|>
literal|3
operator|||
name|Math
operator|.
name|abs
argument_list|(
operator|(
operator|(
name|rgb1
operator|>>
literal|8
operator|)
operator|&
literal|0xFF
operator|)
operator|-
operator|(
operator|(
name|rgb2
operator|>>
literal|8
operator|)
operator|&
literal|0xFF
operator|)
argument_list|)
operator|>
literal|3
operator|||
name|Math
operator|.
name|abs
argument_list|(
operator|(
operator|(
name|rgb1
operator|>>
literal|16
operator|)
operator|&
literal|0xFF
operator|)
operator|-
operator|(
operator|(
name|rgb2
operator|>>
literal|16
operator|)
operator|&
literal|0xFF
operator|)
argument_list|)
operator|>
literal|3
operator|)
condition|)
block|{
if|if
condition|(
name|bim3
operator|==
literal|null
condition|)
block|{
name|bim3
operator|=
name|createEmptyDiffImage
argument_list|(
name|minWidth
argument_list|,
name|minHeight
argument_list|,
name|maxWidth
argument_list|,
name|maxHeight
argument_list|)
expr_stmt|;
block|}
name|int
name|r
init|=
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|rgb1
operator|&
literal|0xFF
operator|)
operator|-
operator|(
name|rgb2
operator|&
literal|0xFF
operator|)
argument_list|)
decl_stmt|;
name|int
name|g
init|=
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|rgb1
operator|&
literal|0xFF00
operator|)
operator|-
operator|(
name|rgb2
operator|&
literal|0xFF00
operator|)
argument_list|)
decl_stmt|;
name|int
name|b
init|=
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|rgb1
operator|&
literal|0xFF0000
operator|)
operator|-
operator|(
name|rgb2
operator|&
literal|0xFF0000
operator|)
argument_list|)
decl_stmt|;
name|bim3
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
literal|0xFFFFFF
operator|-
operator|(
name|r
operator||
name|g
operator||
name|b
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|bim3
operator|!=
literal|null
condition|)
block|{
name|bim3
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|Color
operator|.
name|WHITE
operator|.
name|getRGB
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|bim3
return|;
block|}
comment|/**      * Validate the renderings of a single file.      *      * @param file The file to validate      * @param inDir Name of the input directory      * @param outDir Name of the output directory      * @return false if the test failed (not identical or other problem), true if the test succeeded      * (all identical)      * @throws IOException when there is an exception      */
specifier|public
name|boolean
name|doTestFile
parameter_list|(
specifier|final
name|File
name|file
parameter_list|,
name|String
name|inDir
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
name|boolean
name|failed
init|=
literal|false
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Opening: "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".parseerror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|document
operator|=
name|Loader
operator|.
name|loadPDF
argument_list|(
name|file
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
name|String
name|outputPrefix
init|=
name|outDir
operator|+
literal|'/'
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
decl_stmt|;
name|int
name|numPages
init|=
name|document
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
if|if
condition|(
name|numPages
operator|<
literal|1
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"file "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" has< 1 page"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".parseerror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Rendering: "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
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
name|numPages
condition|;
name|i
operator|++
control|)
block|{
name|String
name|fileName
init|=
name|outputPrefix
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
operator|+
literal|".png"
decl_stmt|;
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|fileName
operator|+
literal|".rendererror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|BufferedImage
name|image
init|=
name|renderer
operator|.
name|renderImageWithDPI
argument_list|(
name|i
argument_list|,
literal|96
argument_list|)
decl_stmt|;
comment|// Windows native DPI
operator|new
name|File
argument_list|(
name|fileName
operator|+
literal|".rendererror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Writing: "
operator|+
name|fileName
argument_list|)
expr_stmt|;
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|fileName
operator|+
literal|".writeerror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|ImageIO
operator|.
name|write
argument_list|(
name|image
argument_list|,
literal|"PNG"
argument_list|,
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
name|fileName
operator|+
literal|".writeerror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|// test to see whether file is destroyed in pdfbox
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".saveerror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|File
name|tmpFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"pdfbox"
argument_list|,
literal|".pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|tmpFile
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".saveerror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".reloaderror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|Loader
operator|.
name|loadPDF
argument_list|(
name|tmpFile
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".reloaderror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tmpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Error converting file "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Comparing: "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//Now check the resulting files ... did we get identical PNG(s)?
try|try
block|{
operator|new
name|File
argument_list|(
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".cmperror"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
name|File
index|[]
name|outFiles
init|=
operator|new
name|File
argument_list|(
name|outDir
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
literal|".png"
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|)
operator|&&
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|".png-diff.png"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|outFiles
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"*** TEST FAILURE *** Output missing for file: "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|File
name|outFile
range|:
name|outFiles
control|)
block|{
operator|new
name|File
argument_list|(
name|outFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"-diff.png"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
comment|// delete diff file from a previous run
name|File
name|inFile
init|=
operator|new
name|File
argument_list|(
name|inDir
operator|+
literal|'/'
operator|+
name|outFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|inFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"*** TEST FAILURE *** Input missing for file: "
operator|+
name|inFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|filesAreIdentical
argument_list|(
name|outFile
argument_list|,
name|inFile
argument_list|)
condition|)
block|{
comment|// different files might still have identical content
comment|// save the difference (if any) into a diff image
name|BufferedImage
name|bim3
init|=
name|diffImages
argument_list|(
name|ImageIO
operator|.
name|read
argument_list|(
name|inFile
argument_list|)
argument_list|,
name|ImageIO
operator|.
name|read
argument_list|(
name|outFile
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|bim3
operator|!=
literal|null
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"*** TEST FAILURE *** Input and output not identical for file: "
operator|+
name|inFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ImageIO
operator|.
name|write
argument_list|(
name|bim3
argument_list|,
literal|"png"
argument_list|,
operator|new
name|File
argument_list|(
name|outFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"-diff.png"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Files differ: "
operator|+
name|inFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"              "
operator|+
name|outFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"*** TEST OK *** for file: "
operator|+
name|inFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting: "
operator|+
name|outFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|outFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"*** TEST OK *** for file: "
operator|+
name|inFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting: "
operator|+
name|outFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|outFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".cmperror"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|failed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Error comparing file output for "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
name|failed
return|;
block|}
specifier|private
name|boolean
name|filesAreIdentical
parameter_list|(
name|File
name|left
parameter_list|,
name|File
name|right
parameter_list|)
throws|throws
name|IOException
block|{
comment|//http://forum.java.sun.com/thread.jspa?threadID=688105&messageID=4003259
comment|//http://web.archive.org/web/20060515173719/http://forum.java.sun.com/thread.jspa?threadID=688105&messageID=4003259
comment|/* -- I reworked ASSERT's into IF statement -- dwilson          assert left != null;          assert right != null;          assert left.exists();          assert right.exists();          */
if|if
condition|(
name|left
operator|!=
literal|null
operator|&&
name|right
operator|!=
literal|null
operator|&&
name|left
operator|.
name|exists
argument_list|()
operator|&&
name|right
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|left
operator|.
name|length
argument_list|()
operator|!=
name|right
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FileInputStream
name|lin
init|=
operator|new
name|FileInputStream
argument_list|(
name|left
argument_list|)
decl_stmt|;
name|FileInputStream
name|rin
init|=
operator|new
name|FileInputStream
argument_list|(
name|right
argument_list|)
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|lbuffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|byte
index|[]
name|rbuffer
init|=
operator|new
name|byte
index|[
name|lbuffer
operator|.
name|length
index|]
decl_stmt|;
name|int
name|lcount
decl_stmt|;
while|while
condition|(
operator|(
name|lcount
operator|=
name|lin
operator|.
name|read
argument_list|(
name|lbuffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|bytesRead
init|=
literal|0
decl_stmt|;
name|int
name|rcount
decl_stmt|;
while|while
condition|(
operator|(
name|rcount
operator|=
name|rin
operator|.
name|read
argument_list|(
name|rbuffer
argument_list|,
name|bytesRead
argument_list|,
name|lcount
operator|-
name|bytesRead
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|bytesRead
operator|+=
name|rcount
expr_stmt|;
block|}
for|for
control|(
name|int
name|byteIndex
init|=
literal|0
init|;
name|byteIndex
operator|<
name|lcount
condition|;
name|byteIndex
operator|++
control|)
block|{
if|if
condition|(
name|lbuffer
index|[
name|byteIndex
index|]
operator|!=
name|rbuffer
index|[
name|byteIndex
index|]
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|lin
operator|.
name|close
argument_list|()
expr_stmt|;
name|rin
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

