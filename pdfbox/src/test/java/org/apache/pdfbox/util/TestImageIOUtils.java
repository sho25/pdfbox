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
name|File
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
name|PDFRenderer
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
comment|/**      * Validate page rendering for all supported image formats (JDK5).      *       * @param file The file to validate      * @param outDir Name of the output directory      * @throws Exception when there is an exception      */
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
name|Exception
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
name|renderImage
argument_list|(
literal|0
argument_list|,
name|dpi
operator|/
literal|72f
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
comment|/**      * Test to validate image rendering of file set.      *       * @throws Exception when there is an exception      */
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
block|}
end_class

end_unit

