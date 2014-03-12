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
name|examples
operator|.
name|pdmodel
package|;
end_package

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
name|IOException
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
name|RandomAccessFile
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
name|pdmodel
operator|.
name|graphics
operator|.
name|image
operator|.
name|CCITTFactory
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
name|image
operator|.
name|JPEGFactory
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
name|image
operator|.
name|PDImageXObject
import|;
end_import

begin_comment
comment|/**  * This is an example that creates a reads a document and adds an image to it..  *  * The example is taken from the pdf file format specification.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|AddImageToPDF
block|{
comment|/**      * Add an image to an existing PDF document.      *      * @param inputFile The input PDF to add the image to.      * @param image The filename of the image to put in the PDF.      * @param outputFile The file to write to the pdf to.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|void
name|createPDFFromImage
parameter_list|(
name|String
name|inputFile
parameter_list|,
name|String
name|image
parameter_list|,
name|String
name|outputFile
parameter_list|)
throws|throws
name|IOException
block|{
comment|// the document
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inputFile
argument_list|)
expr_stmt|;
comment|//we will add the image to the first page.
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
decl_stmt|;
if|if
condition|(
name|image
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jpg"
argument_list|)
condition|)
block|{
name|ximage
operator|=
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|doc
argument_list|,
operator|new
name|FileInputStream
argument_list|(
name|image
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|image
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".tif"
argument_list|)
operator|||
name|image
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".tiff"
argument_list|)
condition|)
block|{
name|ximage
operator|=
name|CCITTFactory
operator|.
name|createFromRandomAccess
argument_list|(
name|doc
argument_list|,
operator|new
name|RandomAccessFile
argument_list|(
operator|new
name|File
argument_list|(
name|image
argument_list|)
argument_list|,
literal|"r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//BufferedImage awtImage = ImageIO.read( new File( image ) );
comment|//ximage = new PDPixelMap(doc, awtImage);
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Image type not supported:"
operator|+
name|image
argument_list|)
throw|;
block|}
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawImage
argument_list|(
name|ximage
argument_list|,
literal|20
argument_list|,
literal|20
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
name|outputFile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will load a PDF document and add a single image on it.      *<br />      * see usage() for commandline      *      * @param args Command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|AddImageToPDF
name|app
init|=
operator|new
name|AddImageToPDF
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|app
operator|.
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|app
operator|.
name|createPDFFromImage
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|,
name|args
index|[
literal|1
index|]
argument_list|,
name|args
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
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
literal|"usage: "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf><image><output-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

