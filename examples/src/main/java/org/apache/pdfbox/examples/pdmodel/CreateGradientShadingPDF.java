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
name|COSFloat
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
name|PDResources
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
name|function
operator|.
name|PDFunctionType2
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
name|color
operator|.
name|PDDeviceRGB
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
name|shading
operator|.
name|PDShadingType2
import|;
end_import

begin_comment
comment|/**  * This example creates a PDF with type 2 (axial) shading with a type 2  * (exponential) function.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|CreateGradientShadingPDF
block|{
comment|/**      * This will create the PDF and write the contents to a file.      *      * @param file The name of the file to write to.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|void
name|create
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
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
comment|// function attributes
name|COSDictionary
name|fdict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|fdict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FUNCTION_TYPE
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|COSArray
name|domain
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|domain
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|COSArray
name|c0
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|COSArray
name|c1
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"0.5"
argument_list|)
argument_list|)
expr_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"0.5"
argument_list|)
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|,
name|domain
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C0
argument_list|,
name|c0
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C1
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|PDFunctionType2
name|func
init|=
operator|new
name|PDFunctionType2
argument_list|(
name|fdict
argument_list|)
decl_stmt|;
name|PDShadingType2
name|shading
init|=
operator|new
name|PDShadingType2
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
comment|// shading attributes
name|shading
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|shading
operator|.
name|setShadingType
argument_list|(
name|PDShadingType2
operator|.
name|SHADING_TYPE2
argument_list|)
expr_stmt|;
name|COSArray
name|coords
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|coords
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|coords
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|400
argument_list|)
argument_list|)
expr_stmt|;
name|coords
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|400
argument_list|)
argument_list|)
expr_stmt|;
name|coords
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|600
argument_list|)
argument_list|)
expr_stmt|;
name|shading
operator|.
name|setCoords
argument_list|(
name|coords
argument_list|)
expr_stmt|;
name|shading
operator|.
name|setFunction
argument_list|(
name|func
argument_list|)
expr_stmt|;
comment|// create and add to shading resources
name|page
operator|.
name|setResources
argument_list|(
operator|new
name|PDResources
argument_list|()
argument_list|)
expr_stmt|;
name|page
operator|.
name|getResources
argument_list|()
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"sh1"
argument_list|)
argument_list|,
name|shading
argument_list|)
expr_stmt|;
comment|// invoke shading from content stream
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
name|appendRawCommands
argument_list|(
literal|"/sh1 sh\n"
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
name|file
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
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
comment|/**      * This will create a blank document.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error writing the document data.      */
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
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|CreateGradientShadingPDF
name|creator
init|=
operator|new
name|CreateGradientShadingPDF
argument_list|()
decl_stmt|;
name|creator
operator|.
name|create
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will print the usage of this class.      */
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
literal|"usage: java org.apache.pdfbox.examples.pdmodel.CreateGradientShadingPDF<outputfile.pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

