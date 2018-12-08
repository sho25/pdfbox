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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|HeadlessException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Toolkit
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
name|tools
operator|.
name|imageio
operator|.
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * Convert a PDF document to an image.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDFToImage
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
name|PDFToImage
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|START_PAGE
init|=
literal|"-startPage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|END_PAGE
init|=
literal|"-endPage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PAGE
init|=
literal|"-page"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IMAGE_TYPE
init|=
literal|"-imageType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT
init|=
literal|"-format"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_PREFIX
init|=
literal|"-outputPrefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"-prefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COLOR
init|=
literal|"-color"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESOLUTION
init|=
literal|"-resolution"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DPI
init|=
literal|"-dpi"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUALITY
init|=
literal|"-quality"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CROPBOX
init|=
literal|"-cropbox"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TIME
init|=
literal|"-time"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUBSAMPLING
init|=
literal|"-subsampling"
decl_stmt|;
comment|/**      * private constructor.     */
specifier|private
name|PDFToImage
parameter_list|()
block|{
comment|//static class
block|}
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be one and a reference to a file.      *      * @throws IOException If there is an error parsing the document.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|String
name|outputPrefix
init|=
literal|null
decl_stmt|;
name|String
name|imageFormat
init|=
literal|"jpg"
decl_stmt|;
name|int
name|startPage
init|=
literal|1
decl_stmt|;
name|int
name|endPage
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|String
name|color
init|=
literal|"rgb"
decl_stmt|;
name|int
name|dpi
decl_stmt|;
name|float
name|quality
init|=
literal|1.0f
decl_stmt|;
name|float
name|cropBoxLowerLeftX
init|=
literal|0
decl_stmt|;
name|float
name|cropBoxLowerLeftY
init|=
literal|0
decl_stmt|;
name|float
name|cropBoxUpperRightX
init|=
literal|0
decl_stmt|;
name|float
name|cropBoxUpperRightY
init|=
literal|0
decl_stmt|;
name|boolean
name|showTime
init|=
literal|false
decl_stmt|;
name|boolean
name|subsampling
init|=
literal|false
decl_stmt|;
try|try
block|{
name|dpi
operator|=
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getScreenResolution
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HeadlessException
name|e
parameter_list|)
block|{
name|dpi
operator|=
literal|96
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|args
index|[
name|i
index|]
condition|)
block|{
case|case
name|PASSWORD
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|START_PAGE
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|startPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|END_PAGE
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|endPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|PAGE
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|startPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|endPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|IMAGE_TYPE
case|:
case|case
name|FORMAT
case|:
name|i
operator|++
expr_stmt|;
name|imageFormat
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|OUTPUT_PREFIX
case|:
case|case
name|PREFIX
case|:
name|i
operator|++
expr_stmt|;
name|outputPrefix
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|COLOR
case|:
name|i
operator|++
expr_stmt|;
name|color
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|RESOLUTION
case|:
case|case
name|DPI
case|:
name|i
operator|++
expr_stmt|;
name|dpi
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|QUALITY
case|:
name|i
operator|++
expr_stmt|;
name|quality
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|CROPBOX
case|:
name|i
operator|++
expr_stmt|;
name|cropBoxLowerLeftX
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
name|cropBoxLowerLeftY
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
name|cropBoxUpperRightX
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
name|cropBoxUpperRightY
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
name|TIME
case|:
name|showTime
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|SUBSAMPLING
case|:
name|subsampling
operator|=
literal|true
expr_stmt|;
break|break;
default|default:
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|outputPrefix
operator|==
literal|null
condition|)
block|{
name|outputPrefix
operator|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
name|password
argument_list|)
init|)
block|{
name|ImageType
name|imageType
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"bilevel"
operator|.
name|equalsIgnoreCase
argument_list|(
name|color
argument_list|)
condition|)
block|{
name|imageType
operator|=
name|ImageType
operator|.
name|BINARY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"gray"
operator|.
name|equalsIgnoreCase
argument_list|(
name|color
argument_list|)
condition|)
block|{
name|imageType
operator|=
name|ImageType
operator|.
name|GRAY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rgb"
operator|.
name|equalsIgnoreCase
argument_list|(
name|color
argument_list|)
condition|)
block|{
name|imageType
operator|=
name|ImageType
operator|.
name|RGB
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rgba"
operator|.
name|equalsIgnoreCase
argument_list|(
name|color
argument_list|)
condition|)
block|{
name|imageType
operator|=
name|ImageType
operator|.
name|ARGB
expr_stmt|;
block|}
if|if
condition|(
name|imageType
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Invalid color."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
comment|//if a CropBox has been specified, update the CropBox:
comment|//changeCropBoxes(PDDocument document,float a, float b, float c,float d)
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|cropBoxLowerLeftX
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|cropBoxLowerLeftY
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|cropBoxUpperRightX
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|cropBoxUpperRightY
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
condition|)
block|{
name|changeCropBox
argument_list|(
name|document
argument_list|,
name|cropBoxLowerLeftX
argument_list|,
name|cropBoxLowerLeftY
argument_list|,
name|cropBoxUpperRightX
argument_list|,
name|cropBoxUpperRightY
argument_list|)
expr_stmt|;
block|}
name|long
name|startTime
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
comment|// render the pages
name|boolean
name|success
init|=
literal|true
decl_stmt|;
name|endPage
operator|=
name|Math
operator|.
name|min
argument_list|(
name|endPage
argument_list|,
name|document
operator|.
name|getNumberOfPages
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
name|renderer
operator|.
name|setSubsamplingAllowed
argument_list|(
name|subsampling
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startPage
operator|-
literal|1
init|;
name|i
operator|<
name|endPage
condition|;
name|i
operator|++
control|)
block|{
name|BufferedImage
name|image
init|=
name|renderer
operator|.
name|renderImageWithDPI
argument_list|(
name|i
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
operator|(
name|i
operator|+
literal|1
operator|)
operator|+
literal|"."
operator|+
name|imageFormat
decl_stmt|;
name|success
operator|&=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|fileName
argument_list|,
name|dpi
argument_list|,
name|quality
argument_list|)
expr_stmt|;
block|}
comment|// performance stats
name|long
name|endTime
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|long
name|duration
init|=
name|endTime
operator|-
name|startTime
decl_stmt|;
name|int
name|count
init|=
literal|1
operator|+
name|endPage
operator|-
name|startPage
decl_stmt|;
if|if
condition|(
name|showTime
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|printf
argument_list|(
literal|"Rendered %d page%s in %dms\n"
argument_list|,
name|count
argument_list|,
name|count
operator|==
literal|1
condition|?
literal|""
else|:
literal|"s"
argument_list|,
name|duration
operator|/
literal|1000000
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: no writer found for image format '"
operator|+
name|imageFormat
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar PDFToImage [options]<inputfile>\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -password<password>            : Password to decrypt document\n"
operator|+
literal|"  -format<string>                 : Image format: "
operator|+
name|getImageFormats
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"  -prefix<string>                 : Filename prefix for image files\n"
operator|+
literal|"  -page<number>                   : The only page to extract (1-based)\n"
operator|+
literal|"  -startPage<int>                 : The first page to start extraction (1-based)\n"
operator|+
literal|"  -endPage<int>                   : The last page to extract(inclusive)\n"
operator|+
literal|"  -color<int>                     : The color depth (valid: bilevel, gray, rgb (default), rgba)\n"
operator|+
literal|"  -dpi<int>                       : The DPI of the output image, default: screen resolution or 96 if unknown\n"
operator|+
literal|"  -quality<float>                 : The quality to be used when compressing the image (0< quality<= 1 (default))\n"
operator|+
literal|"  -cropbox<int><int><int><int> : The page area to export\n"
operator|+
literal|"  -time                            : Prints timing information to stdout\n"
operator|+
literal|"  -subsampling                     : Activate subsampling (for PDFs with huge images)\n"
operator|+
literal|"<inputfile>                      : The PDF document to use\n"
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getImageFormats
parameter_list|()
block|{
name|StringBuilder
name|retval
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|formats
init|=
name|ImageIO
operator|.
name|getReaderFormatNames
argument_list|()
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
name|formats
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|formats
index|[
name|i
index|]
operator|.
name|equalsIgnoreCase
argument_list|(
name|formats
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
name|formats
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|formats
operator|.
name|length
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|changeCropBox
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|float
name|a
parameter_list|,
name|float
name|b
parameter_list|,
name|float
name|c
parameter_list|,
name|float
name|d
parameter_list|)
block|{
for|for
control|(
name|PDPage
name|page
range|:
name|document
operator|.
name|getPages
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"resizing page"
argument_list|)
expr_stmt|;
name|PDRectangle
name|rectangle
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rectangle
operator|.
name|setLowerLeftX
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|rectangle
operator|.
name|setLowerLeftY
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|rectangle
operator|.
name|setUpperRightX
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|rectangle
operator|.
name|setUpperRightY
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|page
operator|.
name|setCropBox
argument_list|(
name|rectangle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

