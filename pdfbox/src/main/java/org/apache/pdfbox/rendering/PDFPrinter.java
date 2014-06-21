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
name|printing
operator|.
name|Orientation
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
name|printing
operator|.
name|Scaling
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|PrintRequestAttributeSet
import|;
end_import

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
name|Dimension
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
name|Pageable
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
name|awt
operator|.
name|print
operator|.
name|PrinterJob
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

begin_comment
comment|/**  * Prints a PDF document using AWT.  * This class may be overridden in order to perform custom printing.  *  * @author Andreas Lehmkühler  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDFPrinter
block|{
specifier|protected
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|protected
specifier|final
name|PDFRenderer
name|renderer
decl_stmt|;
specifier|protected
specifier|final
name|PrinterJob
name|printerJob
decl_stmt|;
specifier|protected
specifier|final
name|Scaling
name|scaling
decl_stmt|;
specifier|protected
specifier|final
name|Orientation
name|orientation
decl_stmt|;
specifier|protected
specifier|final
name|boolean
name|showPageBorder
decl_stmt|;
specifier|protected
specifier|final
name|Paper
name|paper
decl_stmt|;
comment|// may be null
specifier|protected
specifier|final
name|float
name|dpi
decl_stmt|;
comment|/**      * Creates a new PDFPrinter using the system's default printer.      * @param document the document to print      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|PrinterException
block|{
name|this
argument_list|(
name|document
argument_list|,
name|PrinterJob
operator|.
name|getPrinterJob
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrinter using the given printer.      * @param document the document to print      * @param printerJob the printer job to use      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|PrinterJob
name|printerJob
parameter_list|)
throws|throws
name|PrinterException
block|{
name|this
argument_list|(
name|document
argument_list|,
name|printerJob
argument_list|,
name|Scaling
operator|.
name|SHRINK_TO_FIT
argument_list|,
name|Orientation
operator|.
name|AUTO
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrinter using the system's default printer,      * with the given page scaling and orientation.      *      * @param document the document to print      * @param scaling page scaling policy      * @param orientation page orientation policy      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|Orientation
name|orientation
parameter_list|)
throws|throws
name|PrinterException
block|{
name|this
argument_list|(
name|document
argument_list|,
name|PrinterJob
operator|.
name|getPrinterJob
argument_list|()
argument_list|,
name|scaling
argument_list|,
name|orientation
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrinter using the system's default printer,      * with the given page scaling and orientation.      *      * @param document the document to print      * @param scaling page scaling policy      * @param orientation page orientation policy      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|Orientation
name|orientation
parameter_list|,
name|Paper
name|paper
parameter_list|)
throws|throws
name|PrinterException
block|{
name|this
argument_list|(
name|document
argument_list|,
name|PrinterJob
operator|.
name|getPrinterJob
argument_list|()
argument_list|,
name|scaling
argument_list|,
name|orientation
argument_list|,
name|paper
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrinter using the system's default printer,      * with the given page scaling and orientation.      *      * @param document the document to print      * @param scaling page scaling policy      * @param orientation page orientation policy      * @param dpi if non-zero then the image will be rasterized at the given DPI      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|Orientation
name|orientation
parameter_list|,
name|Paper
name|paper
parameter_list|,
name|float
name|dpi
parameter_list|)
throws|throws
name|PrinterException
block|{
name|this
argument_list|(
name|document
argument_list|,
name|PrinterJob
operator|.
name|getPrinterJob
argument_list|()
argument_list|,
name|scaling
argument_list|,
name|orientation
argument_list|,
name|paper
argument_list|,
literal|false
argument_list|,
name|dpi
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new PDFPrinter using the given printer, the given page scaling and orientation,      * and with optional page borders shown.      *      * @param document the document to print      * @param printerJob the printer job to use      * @param scaling page scaling policy      * @param orientation page orientation policy      * @param showPageBorder true if page borders are to be printed      * @param dpi if non-zero then the image will be rasterized at the given DPI      * @throws PrinterException      */
specifier|public
name|PDFPrinter
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|PrinterJob
name|printerJob
parameter_list|,
name|Scaling
name|scaling
parameter_list|,
name|Orientation
name|orientation
parameter_list|,
name|Paper
name|paper
parameter_list|,
name|boolean
name|showPageBorder
parameter_list|,
name|float
name|dpi
parameter_list|)
throws|throws
name|PrinterException
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"document"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|printerJob
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"printerJob"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|document
operator|.
name|getCurrentAccessPermission
argument_list|()
operator|.
name|canPrint
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PrinterException
argument_list|(
literal|"You do not have permission to print this document"
argument_list|)
throw|;
block|}
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
name|printerJob
operator|=
name|printerJob
expr_stmt|;
name|this
operator|.
name|scaling
operator|=
name|scaling
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
name|paper
operator|=
name|paper
expr_stmt|;
name|this
operator|.
name|dpi
operator|=
name|dpi
expr_stmt|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * @throws java.awt.print.PrinterException if the document cannot be printed      */
specifier|public
name|void
name|silentPrint
parameter_list|()
throws|throws
name|PrinterException
block|{
name|silentPrint
argument_list|(
name|printerJob
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * @param attributes application supplied attributes      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|boolean
name|silentPrint
parameter_list|(
name|PrintRequestAttributeSet
name|attributes
parameter_list|)
throws|throws
name|PrinterException
block|{
return|return
name|print
argument_list|(
name|printerJob
argument_list|,
name|attributes
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * @param printerJob a printer job definition      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|void
name|silentPrint
parameter_list|(
name|PrinterJob
name|printerJob
parameter_list|)
throws|throws
name|PrinterException
block|{
name|print
argument_list|(
name|printerJob
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * The image is generated using {@link PageDrawer}.      * This is a convenience method to create the java.awt.print.PrinterJob.      * Advanced printing tasks can be performed using {@link #getPageable()} instead.      *      * @return true if the user does not cancel the dialog      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|boolean
name|print
parameter_list|()
throws|throws
name|PrinterException
block|{
return|return
name|print
argument_list|(
name|printerJob
argument_list|)
return|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * The image is generated using {@link PageDrawer}.      * This is a convenience method to create the java.awt.print.PrinterJob.      * Advanced printing tasks can be performed using {@link #getPageable()} instead.      *      * @param attributes application supplied attributes      * @return true if the user does not cancel the dialog      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|boolean
name|print
parameter_list|(
name|PrintRequestAttributeSet
name|attributes
parameter_list|)
throws|throws
name|PrinterException
block|{
return|return
name|print
argument_list|(
name|printerJob
argument_list|,
name|attributes
argument_list|)
return|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * @param printerJob the printer job.      * @return true if the user does not cancel the dialog      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|boolean
name|print
parameter_list|(
name|PrinterJob
name|printerJob
parameter_list|)
throws|throws
name|PrinterException
block|{
return|return
name|print
argument_list|(
name|printerJob
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Prints the given document using the default printer without prompting the user.      * @param printerJob the printer job.      * @param attributes application supplied attributes      * @return true if the user does not cancel the dialog      * @throws PrinterException if the document cannot be printed      */
specifier|public
name|boolean
name|print
parameter_list|(
name|PrinterJob
name|printerJob
parameter_list|,
name|PrintRequestAttributeSet
name|attributes
parameter_list|)
throws|throws
name|PrinterException
block|{
return|return
name|print
argument_list|(
name|printerJob
argument_list|,
name|attributes
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|// prints a document
specifier|private
name|boolean
name|print
parameter_list|(
name|PrinterJob
name|job
parameter_list|,
name|PrintRequestAttributeSet
name|attributes
parameter_list|,
name|boolean
name|isSilent
parameter_list|)
throws|throws
name|PrinterException
block|{
if|if
condition|(
name|job
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"job cannot be null"
argument_list|)
throw|;
block|}
else|else
block|{
name|job
operator|.
name|setPageable
argument_list|(
name|getPageable
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSilent
condition|)
block|{
name|job
operator|.
name|print
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|job
operator|.
name|printDialog
argument_list|()
condition|)
block|{
name|job
operator|.
name|print
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|job
operator|.
name|printDialog
argument_list|(
name|attributes
argument_list|)
condition|)
block|{
name|job
operator|.
name|print
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the Pageable instance used in this class. Can be overridden by subclasses.      */
specifier|public
name|PDFPageable
name|getPageable
parameter_list|()
block|{
return|return
operator|new
name|PDFPageable
argument_list|()
return|;
block|}
specifier|protected
class|class
name|PDFPageable
implements|implements
name|Pageable
block|{
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
annotation|@
name|Override
specifier|public
name|PageFormat
name|getPageFormat
parameter_list|(
name|int
name|pageIndex
parameter_list|)
throws|throws
name|IndexOutOfBoundsException
block|{
comment|// note: PDFPrintable#print() is responsible for fitting the current page to
comment|//       the printer's actual paper size, so this method must return the full
comment|//       physical, printable size of the actual paper in the printer.
name|PageFormat
name|format
init|=
name|printerJob
operator|.
name|defaultPage
argument_list|()
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
comment|// auto portrait/landscape
if|if
condition|(
name|orientation
operator|==
name|Orientation
operator|.
name|AUTO
condition|)
block|{
name|Dimension
name|cropBox
init|=
name|page
operator|.
name|findRotatedCropBox
argument_list|()
operator|.
name|createDimension
argument_list|()
decl_stmt|;
if|if
condition|(
name|cropBox
operator|.
name|getWidth
argument_list|()
operator|>
name|cropBox
operator|.
name|getHeight
argument_list|()
condition|)
block|{
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|LANDSCAPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|PORTRAIT
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|orientation
operator|==
name|Orientation
operator|.
name|LANDSCAPE
condition|)
block|{
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|LANDSCAPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orientation
operator|==
name|Orientation
operator|.
name|PORTRAIT
condition|)
block|{
name|format
operator|.
name|setOrientation
argument_list|(
name|PageFormat
operator|.
name|PORTRAIT
argument_list|)
expr_stmt|;
block|}
comment|// custom paper
if|if
condition|(
name|paper
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setPaper
argument_list|(
name|paper
argument_list|)
expr_stmt|;
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
throws|throws
name|IndexOutOfBoundsException
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
argument_list|()
return|;
block|}
block|}
specifier|protected
class|class
name|PDFPrintable
implements|implements
name|Printable
block|{
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
name|page
operator|.
name|findRotatedCropBox
argument_list|()
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
argument_list|)
argument_list|,
call|(
name|int
call|)
argument_list|(
name|imageableHeight
operator|*
name|dpiScale
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
block|}
block|}
end_class

end_unit

