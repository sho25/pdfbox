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
name|PDShading
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
name|PDShadingType3
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
comment|/**  * This example creates a PDF with type 2 (axial) and 3 (radial) shadings with a  * type 2 (exponential) function.  *  * @author Tilman Hausherr  */
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
comment|// type 2 (exponential) function with attributes
comment|// can be used by both shadings
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
comment|// axial shading with attributes
name|PDShadingType2
name|axialShading
init|=
operator|new
name|PDShadingType2
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|axialShading
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|axialShading
operator|.
name|setShadingType
argument_list|(
name|PDShading
operator|.
name|SHADING_TYPE2
argument_list|)
expr_stmt|;
name|COSArray
name|coords1
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|coords1
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
name|coords1
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
name|coords1
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
name|coords1
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
name|axialShading
operator|.
name|setCoords
argument_list|(
name|coords1
argument_list|)
expr_stmt|;
name|axialShading
operator|.
name|setFunction
argument_list|(
name|func
argument_list|)
expr_stmt|;
comment|// radial shading with attributes
name|PDShadingType3
name|radialShading
init|=
operator|new
name|PDShadingType3
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|radialShading
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|radialShading
operator|.
name|setShadingType
argument_list|(
name|PDShading
operator|.
name|SHADING_TYPE3
argument_list|)
expr_stmt|;
name|COSArray
name|coords2
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|coords2
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
name|coords2
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
name|coords2
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
comment|// radius1
name|coords2
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
name|coords2
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
name|coords2
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|150
argument_list|)
argument_list|)
expr_stmt|;
comment|// radius2
name|radialShading
operator|.
name|setCoords
argument_list|(
name|coords2
argument_list|)
expr_stmt|;
name|radialShading
operator|.
name|setFunction
argument_list|(
name|func
argument_list|)
expr_stmt|;
comment|// invoke shading from content stream
comment|// compress parameter is set to false so that you can see the stream in a text editor
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
name|shadingFill
argument_list|(
name|axialShading
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|shadingFill
argument_list|(
name|radialShading
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
comment|// render the PDF and save it into a PNG file
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|BufferedImage
name|bim
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
operator|.
name|renderImageWithDPI
argument_list|(
literal|0
argument_list|,
literal|300
argument_list|)
decl_stmt|;
name|ImageIO
operator|.
name|write
argument_list|(
name|bim
argument_list|,
literal|"png"
argument_list|,
operator|new
name|File
argument_list|(
name|file
operator|+
literal|".png"
argument_list|)
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
literal|"usage: java o"
operator|+
name|CreateGradientShadingPDF
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<outputfile.pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

