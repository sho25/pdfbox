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
name|BasicStroke
import|;
end_import

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
name|Graphics2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|Printable
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
name|PrinterException
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
name|PrinterIOException
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
name|PDFRenderer
import|;
end_import

begin_comment
comment|/**  * Prints pages from a PDF document using any page size or scaling mode.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDFPrintable
implements|implements
name|Printable
block|{
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|PDFRenderer
name|renderer
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showPageBorder
decl_stmt|;
specifier|private
specifier|final
name|Scaling
name|scaling
decl_stmt|;
specifier|private
specifier|final
name|float
name|dpi
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|center
decl_stmt|;
comment|/**      * Creates a new PDFPrintable.      *      * @param document the document to print      */
specifier|public
name|PDFPrintable
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|Scaling
operator|.
name|SHRINK_TO_FIT
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrintable with the given page scaling.      *      * @param document the document to print      * @param scaling page scaling policy      */
specifier|public
name|PDFPrintable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|scaling
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrintable with the given page scaling and with optional page borders shown.      *      * @param document the document to print      * @param scaling page scaling policy      * @param showPageBorder true if page borders are to be printed      */
specifier|public
name|PDFPrintable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|scaling
argument_list|,
name|showPageBorder
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrintable with the given page scaling and with optional page borders shown.      * The image will be rasterized at the given DPI before being sent to the printer.      *      * @param document the document to print      * @param scaling page scaling policy      * @param showPageBorder true if page borders are to be printed      * @param dpi if non-zero then the image will be rasterized at the given DPI      */
specifier|public
name|PDFPrintable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|,
name|float
name|dpi
parameter_list|)
block|{
name|this
argument_list|(
name|document
argument_list|,
name|scaling
argument_list|,
name|showPageBorder
argument_list|,
name|dpi
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrintable with the given page scaling and with optional page borders shown.      * The image will be rasterized at the given DPI before being sent to the printer.      *      * @param document the document to print      * @param scaling page scaling policy      * @param showPageBorder true if page borders are to be printed      * @param dpi if non-zero then the image will be rasterized at the given DPI      * @param center true if the content is to be centered on the page (otherwise top-left).      */
specifier|public
name|PDFPrintable
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|,
name|float
name|dpi
parameter_list|,
name|boolean
name|center
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
name|renderer
operator|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|this
operator|.
name|scaling
operator|=
name|scaling
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
name|this
operator|.
name|center
operator|=
name|center
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|print
parameter_list|(
name|Graphics
name|graphics
parameter_list|,
name|PageFormat
name|pageFormat
parameter_list|,
name|int
name|pageIndex
parameter_list|)
throws|throws
name|PrinterException
block|{
if|if
condition|(
name|pageIndex
operator|<
literal|0
operator|||
name|pageIndex
operator|>=
name|document
operator|.
name|getNumberOfPages
argument_list|()
condition|)
block|{
return|return
name|NO_SUCH_PAGE
return|;
block|}
try|try
block|{
name|Graphics2D
name|graphics2D
init|=
operator|(
name|Graphics2D
operator|)
name|graphics
decl_stmt|;
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
name|cropBox
init|=
name|getRotatedCropBox
argument_list|(
name|page
argument_list|)
decl_stmt|;
comment|// the imageable area is the area within the page margins
specifier|final
name|double
name|imageableWidth
init|=
name|pageFormat
operator|.
name|getImageableWidth
argument_list|()
decl_stmt|;
specifier|final
name|double
name|imageableHeight
init|=
name|pageFormat
operator|.
name|getImageableHeight
argument_list|()
decl_stmt|;
name|double
name|scale
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|scaling
operator|!=
name|Scaling
operator|.
name|ACTUAL_SIZE
condition|)
block|{
comment|// scale to fit
name|double
name|scaleX
init|=
name|imageableWidth
operator|/
name|cropBox
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|double
name|scaleY
init|=
name|imageableHeight
operator|/
name|cropBox
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|scale
operator|=
name|Math
operator|.
name|min
argument_list|(
name|scaleX
argument_list|,
name|scaleY
argument_list|)
expr_stmt|;
comment|// only shrink to fit when enabled
if|if
condition|(
name|scale
operator|>
literal|1
operator|&&
name|scaling
operator|==
name|Scaling
operator|.
name|SHRINK_TO_FIT
condition|)
block|{
name|scale
operator|=
literal|1
expr_stmt|;
block|}
comment|// only stretch to fit when enabled
if|if
condition|(
name|scale
operator|<
literal|1
operator|&&
name|scaling
operator|==
name|Scaling
operator|.
name|STRETCH_TO_FIT
condition|)
block|{
name|scale
operator|=
literal|1
expr_stmt|;
block|}
block|}
comment|// set the graphics origin to the origin of the imageable area (i.e the margins)
name|graphics2D
operator|.
name|translate
argument_list|(
name|pageFormat
operator|.
name|getImageableX
argument_list|()
argument_list|,
name|pageFormat
operator|.
name|getImageableY
argument_list|()
argument_list|)
expr_stmt|;
comment|// center on page
if|if
condition|(
name|center
condition|)
block|{
name|graphics2D
operator|.
name|translate
argument_list|(
operator|(
name|imageableWidth
operator|-
name|cropBox
operator|.
name|getWidth
argument_list|()
operator|*
name|scale
operator|)
operator|/
literal|2
argument_list|,
operator|(
name|imageableHeight
operator|-
name|cropBox
operator|.
name|getHeight
argument_list|()
operator|*
name|scale
operator|)
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
comment|// rasterize to bitmap (optional)
name|Graphics2D
name|printerGraphics
init|=
literal|null
decl_stmt|;
name|BufferedImage
name|image
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dpi
operator|>
literal|0
condition|)
block|{
name|float
name|dpiScale
init|=
name|dpi
operator|/
literal|72
decl_stmt|;
name|image
operator|=
operator|new
name|BufferedImage
argument_list|(
call|(
name|int
call|)
argument_list|(
name|imageableWidth
operator|*
name|dpiScale
operator|/
name|scale
argument_list|)
argument_list|,
call|(
name|int
call|)
argument_list|(
name|imageableHeight
operator|*
name|dpiScale
operator|/
name|scale
argument_list|)
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
expr_stmt|;
name|printerGraphics
operator|=
name|graphics2D
expr_stmt|;
name|graphics2D
operator|=
name|image
operator|.
name|createGraphics
argument_list|()
expr_stmt|;
comment|// rescale
name|printerGraphics
operator|.
name|scale
argument_list|(
name|scale
operator|/
name|dpiScale
argument_list|,
name|scale
operator|/
name|dpiScale
argument_list|)
expr_stmt|;
name|scale
operator|=
name|dpiScale
expr_stmt|;
block|}
comment|// draw to graphics using PDFRender
name|AffineTransform
name|transform
init|=
operator|(
name|AffineTransform
operator|)
name|graphics2D
operator|.
name|getTransform
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|graphics2D
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|renderer
operator|.
name|renderPageToGraphics
argument_list|(
name|pageIndex
argument_list|,
name|graphics2D
argument_list|,
operator|(
name|float
operator|)
name|scale
argument_list|)
expr_stmt|;
comment|// draw crop box
if|if
condition|(
name|showPageBorder
condition|)
block|{
name|graphics2D
operator|.
name|setTransform
argument_list|(
name|transform
argument_list|)
expr_stmt|;
name|graphics2D
operator|.
name|setClip
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
name|int
operator|)
name|imageableWidth
argument_list|,
operator|(
name|int
operator|)
name|imageableHeight
argument_list|)
expr_stmt|;
name|graphics2D
operator|.
name|scale
argument_list|(
name|scale
argument_list|,
name|scale
argument_list|)
expr_stmt|;
name|graphics2D
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|GRAY
argument_list|)
expr_stmt|;
name|graphics2D
operator|.
name|setStroke
argument_list|(
operator|new
name|BasicStroke
argument_list|(
literal|0.5f
argument_list|)
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|drawRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
name|int
operator|)
name|cropBox
operator|.
name|getWidth
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|cropBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// draw rasterized bitmap (optional)
if|if
condition|(
name|printerGraphics
operator|!=
literal|null
condition|)
block|{
name|printerGraphics
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|printerGraphics
operator|.
name|clearRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|printerGraphics
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|graphics2D
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
return|return
name|PAGE_EXISTS
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PrinterIOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will find the CropBox with rotation applied, for this page by looking up the hierarchy      * until it finds them.      *      * @return The CropBox at this level in the hierarchy.      */
specifier|static
name|PDRectangle
name|getRotatedCropBox
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|PDRectangle
name|cropBox
init|=
name|page
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|int
name|rotationAngle
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|rotationAngle
operator|==
literal|90
operator|||
name|rotationAngle
operator|==
literal|270
condition|)
block|{
return|return
operator|new
name|PDRectangle
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
return|;
block|}
else|else
block|{
return|return
name|cropBox
return|;
block|}
block|}
comment|/**      * This will find the MediaBox with rotation applied, for this page by looking up the hierarchy      * until it finds them.      *      * @return The MediaBox at this level in the hierarchy.      */
specifier|static
name|PDRectangle
name|getRotatedMediaBox
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|PDRectangle
name|mediaBox
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|int
name|rotationAngle
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|rotationAngle
operator|==
literal|90
operator|||
name|rotationAngle
operator|==
literal|270
condition|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|mediaBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|mediaBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
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
return|;
block|}
else|else
block|{
return|return
name|mediaBox
return|;
block|}
block|}
block|}
end_class

end_unit

