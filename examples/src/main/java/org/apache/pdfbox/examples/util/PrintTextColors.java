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
name|util
package|;
end_package

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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingColorN
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingColorSpace
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingDeviceCMYKColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingDeviceGrayColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetNonStrokingDeviceRGBColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingColorN
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingColorSpace
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingDeviceCMYKColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingDeviceGrayColor
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
name|contentstream
operator|.
name|operator
operator|.
name|color
operator|.
name|SetStrokingDeviceRGBColor
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
name|pdfparser
operator|.
name|PDFParser
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
name|PDColor
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
name|state
operator|.
name|RenderingMode
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
name|text
operator|.
name|PDFTextStripper
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
name|text
operator|.
name|TextPosition
import|;
end_import

begin_comment
comment|/**  * This is an example on how to get the colors of text. Note that this will not tell the background,  * and will only work properly if the text is not overwritten later, and only if the text rendering  * modes are 0, 1 or 2. In the PDF 32000 specification, please read 9.3.6 "Text Rendering Mode" to  * know more. Mode 0 (FILL) is the default. Mode 1 (STROKE) will make glyphs look "hollow". Mode 2  * (FILL_STROKE) will make glyphs look "fat".  *  * @author Ben Litchfield  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PrintTextColors
extends|extends
name|PDFTextStripper
block|{
comment|/**      * Instantiate a new PDFTextStripper object.      *      * @throws IOException If there is an error loading the properties.      */
specifier|public
name|PrintTextColors
parameter_list|()
throws|throws
name|IOException
block|{
name|addOperator
argument_list|(
operator|new
name|SetStrokingColorSpace
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingColorSpace
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetStrokingDeviceCMYKColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingDeviceCMYKColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingDeviceRGBColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetStrokingDeviceRGBColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingDeviceGrayColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetStrokingDeviceGrayColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetStrokingColorN
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetNonStrokingColorN
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      */
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
try|try
init|(
name|PDDocument
name|document
init|=
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
init|)
block|{
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PrintTextColors
argument_list|()
decl_stmt|;
name|stripper
operator|.
name|setSortByPosition
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setStartPage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setEndPage
argument_list|(
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|Writer
name|dummy
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|dummy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|super
operator|.
name|processTextPosition
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|PDColor
name|strokingColor
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
decl_stmt|;
name|PDColor
name|nonStrokingColor
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
decl_stmt|;
name|String
name|unicode
init|=
name|text
operator|.
name|getUnicode
argument_list|()
decl_stmt|;
name|RenderingMode
name|renderingMode
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getRenderingMode
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unicode:            "
operator|+
name|unicode
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Rendering mode:     "
operator|+
name|renderingMode
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stroking color:     "
operator|+
name|strokingColor
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Non-Stroking color: "
operator|+
name|nonStrokingColor
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Non-Stroking color: "
operator|+
name|nonStrokingColor
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// See the PrintTextLocations for more attributes
block|}
comment|/**      * This will print the usage for this document.      */
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
literal|"Usage: java "
operator|+
name|PrintTextColors
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

