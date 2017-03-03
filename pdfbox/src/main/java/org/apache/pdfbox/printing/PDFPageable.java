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
name|printing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|Book
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|PageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|Paper
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|Printable
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

begin_comment
comment|/**  * Prints a PDF document using its original paper size.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDFPageable
extends|extends
name|Book
block|{
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showPageBorder
decl_stmt|;
specifier|private
specifier|final
name|float
name|dpi
decl_stmt|;
specifier|private
specifier|final
name|Orientation
name|orientation
decl_stmt|;
comment|/**      * Creates a new PDFPageable.      *      * @param document the document to print      */
specifier|public
name|PDFPageable
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|Orientation
operator|.
name|AUTO
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPageable with the given page orientation.      *      * @param document the document to print      * @param orientation page orientation policy      */
specifier|public
name|PDFPageable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Orientation
name|orientation
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|orientation
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPageable with the given page orientation and with optional page borders      * shown. The image will be rasterized at the given DPI before being sent to the printer.      *      * @param document the document to print      * @param orientation page orientation policy      * @param showPageBorder true if page borders are to be printed      */
specifier|public
name|PDFPageable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Orientation
name|orientation
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|orientation
argument_list|,
name|showPageBorder
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPageable with the given page orientation and with optional page borders      * shown. The image will be rasterized at the given DPI before being sent to the printer.      *      * @param document the document to print      * @param orientation page orientation policy      * @param showPageBorder true if page borders are to be printed      * @param dpi if non-zero then the image will be rasterized at the given DPI      */
specifier|public
name|PDFPageable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Orientation
name|orientation
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|,
name|float
name|dpi
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|orientation
operator|=
name|orientation
expr_stmt|;
name|this
operator|.
name|showPageBorder
operator|=
name|showPageBorder
expr_stmt|;
name|this
operator|.
name|dpi
operator|=
name|dpi
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getNumberOfPages
parameter_list|()
block|{
return|return
name|document
operator|.
name|getNumberOfPages
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      *       * Returns the actual physical size of the pages in the PDF file. May not fit the local printer.      */
annotation|@
name|Override
specifier|public
name|PageFormat
name|getPageFormat
parameter_list|(
name|int
name|pageIndex
parameter_list|)
block|{
name|PDPage
name|page
init|=
name|document
operator|.
name|getPage
argument_list|(
name|pageIndex
argument_list|)
decl_stmt|;
name|PDRectangle
name|mediaBox
init|=
name|PDFPrintable
operator|.
name|getRotatedMediaBox
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|PDRectangle
name|cropBox
init|=
name|PDFPrintable
operator|.
name|getRotatedCropBox
argument_list|(
name|page
argument_list|)
decl_stmt|;
comment|// Java does not seem to understand landscape paper sizes, i.e. where width> height, it
comment|// always crops the imageable area as if the page were in portrait. I suspect that this is
comment|// a JDK bug but it might be by design, see PDFBOX-2922.
comment|//
comment|// As a workaround, we normalise all Page(s) to be portrait, then flag them as landscape in
comment|// the PageFormat.
name|Paper
name|paper
decl_stmt|;
name|boolean
name|isLandscape
decl_stmt|;
if|if
condition|(
name|mediaBox
operator|.
name|getWidth
argument_list|()
operator|>
name|mediaBox
operator|.
name|getHeight
argument_list|()
condition|)
block|{
comment|// rotate
name|paper
operator|=
operator|new
name|Paper
argument_list|()
expr_stmt|;
name|paper
operator|.
name|setSize
argument_list|(
name|mediaBox
operator|.
name|getHeight
argument_list|()
argument_list|,
name|mediaBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|paper
operator|.
name|setImageableArea
argument_list|(
name|cropBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getHeight
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|isLandscape
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|paper
operator|=
operator|new
name|Paper
argument_list|()
expr_stmt|;
name|paper
operator|.
name|setSize
argument_list|(
name|mediaBox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|mediaBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|paper
operator|.
name|setImageableArea
argument_list|(
name|cropBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|isLandscape
operator|=
literal|false
expr_stmt|;
block|}
name|PageFormat
name|format
init|=
operator|new
name|PageFormat
argument_list|()
decl_stmt|;
name|format
operator|.
name|setPaper
argument_list|(
name|paper
argument_list|)
expr_stmt|;
comment|// auto portrait/landscape
switch|switch
condition|(
name|orientation
condition|)
block|{
case|case
name|AUTO
case|:
name|format
operator|.
name|setOrientation
argument_list|(
name|isLandscape
condition|?
name|PageFormat
operator|.
name|LANDSCAPE
else|:
name|PageFormat
operator|.
name|PORTRAIT
argument_list|)
expr_stmt|;
break|break;
case|case
name|LANDSCAPE
case|:
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|LANDSCAPE
argument_list|)
expr_stmt|;
break|break;
case|case
name|PORTRAIT
case|:
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|PORTRAIT
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|format
return|;
block|}
annotation|@
name|Override
specifier|public
name|Printable
name|getPrintable
parameter_list|(
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|>=
name|getNumberOfPages
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
name|i
operator|+
literal|">= "
operator|+
name|getNumberOfPages
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|PDFPrintable
argument_list|(
name|document
argument_list|,
name|Scaling
operator|.
name|ACTUAL_SIZE
argument_list|,
name|showPageBorder
argument_list|,
name|dpi
argument_list|)
return|;
block|}
block|}
end_class

end_unit

