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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|encryption
operator|.
name|InvalidPasswordException
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
name|util
operator|.
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * Convert a PDF document to an image.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFToImage
block|{
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
name|IMAGE_FORMAT
init|=
literal|"-imageType"
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
name|CROPBOX
init|=
literal|"-cropbox"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NONSEQ
init|=
literal|"-nonSeq"
decl_stmt|;
comment|/**      * private constructor.     */
specifier|private
name|PDFToImage
parameter_list|()
block|{
comment|//static class
block|}
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be one and a reference to a file.      *      * @throws Exception If there is an error parsing the document.      */
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
name|boolean
name|useNonSeqParser
init|=
literal|false
decl_stmt|;
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
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|PASSWORD
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|START_PAGE
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|END_PAGE
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|IMAGE_FORMAT
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|OUTPUT_PREFIX
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|COLOR
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|RESOLUTION
argument_list|)
condition|)
block|{
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
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|CROPBOX
argument_list|)
condition|)
block|{
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
operator|.
name|floatValue
argument_list|()
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
operator|.
name|floatValue
argument_list|()
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
operator|.
name|floatValue
argument_list|()
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
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|NONSEQ
argument_list|)
condition|)
block|{
name|useNonSeqParser
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
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
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|useNonSeqParser
condition|)
block|{
name|document
operator|=
name|PDDocument
operator|.
name|loadNonSeq
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
literal|null
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
try|try
block|{
name|document
operator|.
name|decrypt
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|4
condition|)
comment|//they supplied the wrong password
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The supplied password is incorrect."
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
else|else
block|{
comment|//they didn't supply a password and the default of "" was wrong.
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The document is encrypted."
argument_list|)
expr_stmt|;
name|usage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
name|ImageType
name|imageType
init|=
name|ImageType
operator|.
name|RGB
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
else|else
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
name|cropBoxLowerLeftX
operator|!=
literal|0
operator|||
name|cropBoxLowerLeftY
operator|!=
literal|0
operator|||
name|cropBoxUpperRightX
operator|!=
literal|0
operator|||
name|cropBoxUpperRightY
operator|!=
literal|0
condition|)
block|{
name|changeCropBoxes
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
comment|// render the pages
name|boolean
name|success
init|=
literal|true
decl_stmt|;
name|int
name|numPages
init|=
name|document
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
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
name|startPage
operator|-
literal|1
init|;
name|i
operator|<
name|endPage
operator|&&
name|i
operator|<
name|numPages
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
block|}
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: java -jar pdfbox-app-x.y.z.jar PDFToImage [OPTIONS]<PDF file>\n"
operator|+
literal|"  -password<password>          Password to decrypt document\n"
operator|+
literal|"  -imageType<image type>        ("
operator|+
name|getImageFormats
argument_list|()
operator|+
literal|")\n"
operator|+
literal|"  -outputPrefix<output prefix>  Filename prefix for image files\n"
operator|+
literal|"  -startPage<number>            The first page to start extraction(1 based)\n"
operator|+
literal|"  -endPage<number>              The last page to extract(inclusive)\n"
operator|+
literal|"  -color<string>                The color depth (valid: bilevel, indexed, gray, rgb, rgba)\n"
operator|+
literal|"  -resolution<number>           The bitmap resolution in dpi\n"
operator|+
literal|"  -cropbox<number><number><number><number> The page area to export\n"
operator|+
literal|"  -nonSeq                        Enables the new non-sequential parser\n"
operator|+
literal|"<PDF file>                     The PDF document to use\n"
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
name|StringBuffer
name|retval
init|=
operator|new
name|StringBuffer
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
literal|","
argument_list|)
expr_stmt|;
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
name|changeCropBoxes
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
name|List
name|pages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
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
name|pages
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
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
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|pages
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
name|setMediaBox
argument_list|(
name|rectangle
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

