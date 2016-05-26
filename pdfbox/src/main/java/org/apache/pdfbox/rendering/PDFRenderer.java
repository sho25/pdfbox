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
name|Graphics2D
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

begin_comment
comment|/**  * Renders a PDF document to an AWT BufferedImage.  * This class may be overridden in order to perform custom rendering.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDFRenderer
block|{
specifier|protected
specifier|final
name|PDDocument
name|document
decl_stmt|;
comment|// TODO keep rendering state such as caches here
comment|/**      * Creates a new PDFRenderer.      * @param document the document to render      */
specifier|public
name|PDFRenderer
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
comment|/**      * Returns the given page as an RGB image at 72 DPI      * @param pageIndex the zero-based index of the page to be converted.      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
specifier|public
name|BufferedImage
name|renderImage
parameter_list|(
name|int
name|pageIndex
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * Returns the given page as an RGB image at the given scale.      * A scale of 1 will render at 72 DPI.      * @param pageIndex the zero-based index of the page to be converted      * @param scale the scaling factor, where 1 = 72 DPI      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
specifier|public
name|BufferedImage
name|renderImage
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|float
name|scale
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
name|scale
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|)
return|;
block|}
comment|/**      * Returns the given page as an RGB image at the given DPI.      * @param pageIndex the zero-based index of the page to be converted      * @param dpi the DPI (dots per inch) to render at      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
specifier|public
name|BufferedImage
name|renderImageWithDPI
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|float
name|dpi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
name|dpi
operator|/
literal|72f
argument_list|,
name|ImageType
operator|.
name|RGB
argument_list|)
return|;
block|}
comment|/**      * Returns the given page as an RGB image at the given DPI.      * @param pageIndex the zero-based index of the page to be converted      * @param dpi the DPI (dots per inch) to render at      * @param imageType the type of image to return      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
specifier|public
name|BufferedImage
name|renderImageWithDPI
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|float
name|dpi
parameter_list|,
name|ImageType
name|imageType
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
name|dpi
operator|/
literal|72f
argument_list|,
name|imageType
argument_list|)
return|;
block|}
comment|/**      * Returns the given page as an RGB or ARGB image at the given scale.      * @param pageIndex the zero-based index of the page to be converted      * @param scale the scaling factor, where 1 = 72 DPI      * @param imageType the type of image to return      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
specifier|public
name|BufferedImage
name|renderImage
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|float
name|scale
parameter_list|,
name|ImageType
name|imageType
parameter_list|)
throws|throws
name|IOException
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
name|cropbBox
init|=
name|page
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|float
name|widthPt
init|=
name|cropbBox
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|heightPt
init|=
name|cropbBox
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|widthPx
init|=
name|Math
operator|.
name|round
argument_list|(
name|widthPt
operator|*
name|scale
argument_list|)
decl_stmt|;
name|int
name|heightPx
init|=
name|Math
operator|.
name|round
argument_list|(
name|heightPt
operator|*
name|scale
argument_list|)
decl_stmt|;
name|int
name|rotationAngle
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
comment|// swap width and height
name|BufferedImage
name|image
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
name|image
operator|=
operator|new
name|BufferedImage
argument_list|(
name|heightPx
argument_list|,
name|widthPx
argument_list|,
name|imageType
operator|.
name|toBufferedImageType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|image
operator|=
operator|new
name|BufferedImage
argument_list|(
name|widthPx
argument_list|,
name|heightPx
argument_list|,
name|imageType
operator|.
name|toBufferedImageType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// use a transparent background if the imageType supports alpha
name|Graphics2D
name|g
init|=
name|image
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
if|if
condition|(
name|imageType
operator|==
name|ImageType
operator|.
name|ARGB
condition|)
block|{
name|g
operator|.
name|setBackground
argument_list|(
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|g
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
block|}
name|renderPage
argument_list|(
name|page
argument_list|,
name|g
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
argument_list|,
name|scale
argument_list|,
name|scale
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|image
return|;
block|}
comment|/**      * Renders a given page to an AWT Graphics2D instance.      * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @throws IOException if the PDF cannot be read      */
specifier|public
name|void
name|renderPageToGraphics
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|Graphics2D
name|graphics
parameter_list|)
throws|throws
name|IOException
block|{
name|renderPageToGraphics
argument_list|(
name|pageIndex
argument_list|,
name|graphics
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Renders a given page to an AWT Graphics2D instance.      * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @param scale the scale to draw the page at      * @throws IOException if the PDF cannot be read      */
specifier|public
name|void
name|renderPageToGraphics
parameter_list|(
name|int
name|pageIndex
parameter_list|,
name|Graphics2D
name|graphics
parameter_list|,
name|float
name|scale
parameter_list|)
throws|throws
name|IOException
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
comment|// TODO need width/wight calculations? should these be in PageDrawer?
name|PDRectangle
name|adjustedCropBox
init|=
name|page
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|renderPage
argument_list|(
name|page
argument_list|,
name|graphics
argument_list|,
operator|(
name|int
operator|)
name|adjustedCropBox
operator|.
name|getWidth
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|adjustedCropBox
operator|.
name|getHeight
argument_list|()
argument_list|,
name|scale
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
comment|// renders a page to the given graphics
specifier|private
name|void
name|renderPage
parameter_list|(
name|PDPage
name|page
parameter_list|,
name|Graphics2D
name|graphics
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|float
name|scaleX
parameter_list|,
name|float
name|scaleY
parameter_list|)
throws|throws
name|IOException
block|{
name|graphics
operator|.
name|clearRect
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|scale
argument_list|(
name|scaleX
argument_list|,
name|scaleY
argument_list|)
expr_stmt|;
comment|// TODO should we be passing the scale to PageDrawer rather than messing with Graphics?
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
operator|!=
literal|0
condition|)
block|{
name|float
name|translateX
init|=
literal|0
decl_stmt|;
name|float
name|translateY
init|=
literal|0
decl_stmt|;
switch|switch
condition|(
name|rotationAngle
condition|)
block|{
case|case
literal|90
case|:
name|translateX
operator|=
name|cropBox
operator|.
name|getHeight
argument_list|()
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|translateY
operator|=
name|cropBox
operator|.
name|getWidth
argument_list|()
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|translateX
operator|=
name|cropBox
operator|.
name|getWidth
argument_list|()
expr_stmt|;
name|translateY
operator|=
name|cropBox
operator|.
name|getHeight
argument_list|()
expr_stmt|;
break|break;
block|}
name|graphics
operator|.
name|translate
argument_list|(
name|translateX
argument_list|,
name|translateY
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|rotate
argument_list|(
operator|(
name|float
operator|)
name|Math
operator|.
name|toRadians
argument_list|(
name|rotationAngle
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// the end-user may provide a custom PageDrawer
name|PageDrawerParameters
name|parameters
init|=
operator|new
name|PageDrawerParameters
argument_list|(
name|this
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|PageDrawer
name|drawer
init|=
name|createPageDrawer
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|drawer
operator|.
name|drawPage
argument_list|(
name|graphics
argument_list|,
name|cropBox
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a new PageDrawer instance, using the given parameters. May be overridden.      */
specifier|protected
name|PageDrawer
name|createPageDrawer
parameter_list|(
name|PageDrawerParameters
name|parameters
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PageDrawer
argument_list|(
name|parameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

