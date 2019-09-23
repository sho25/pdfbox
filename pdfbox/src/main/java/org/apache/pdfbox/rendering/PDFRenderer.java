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
name|DisplayMode
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
name|GraphicsConfiguration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GraphicsDevice
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|pdmodel
operator|.
name|graphics
operator|.
name|blend
operator|.
name|BlendMode
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
name|optionalcontent
operator|.
name|PDOptionalContentGroup
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
name|optionalcontent
operator|.
name|PDOptionalContentProperties
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
name|PDExtendedGraphicsState
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
name|interactive
operator|.
name|annotation
operator|.
name|AnnotationFilter
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
name|PDFRenderer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|PDDocument
name|document
decl_stmt|;
comment|// TODO keep rendering state such as caches here
comment|/**     * Default annotations filter, returns all annotations     */
specifier|private
name|AnnotationFilter
name|annotationFilter
init|=
name|annotation
lambda|->
literal|true
decl_stmt|;
specifier|private
name|boolean
name|subsamplingAllowed
init|=
literal|false
decl_stmt|;
specifier|private
name|RenderDestination
name|defaultDestination
decl_stmt|;
specifier|private
name|RenderingHints
name|renderingHints
init|=
literal|null
decl_stmt|;
specifier|private
name|BufferedImage
name|pageImage
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|kcmsLogged
init|=
literal|false
decl_stmt|;
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
if|if
condition|(
operator|!
name|kcmsLogged
condition|)
block|{
name|suggestKCMS
argument_list|()
expr_stmt|;
name|kcmsLogged
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Return the AnnotationFilter.      *       * @return the AnnotationFilter      */
specifier|public
name|AnnotationFilter
name|getAnnotationsFilter
parameter_list|()
block|{
return|return
name|annotationFilter
return|;
block|}
comment|/**      * Set the AnnotationFilter.      *       *<p>Allows to only render annotation accepted by the filter.      *       * @param annotationsFilter the AnnotationFilter      */
specifier|public
name|void
name|setAnnotationsFilter
parameter_list|(
name|AnnotationFilter
name|annotationsFilter
parameter_list|)
block|{
name|this
operator|.
name|annotationFilter
operator|=
name|annotationsFilter
expr_stmt|;
block|}
comment|/**      * Value indicating if the renderer is allowed to subsample images before drawing, according to      * image dimensions and requested scale.      *      * Subsampling may be faster and less memory-intensive in some cases, but it may also lead to      * loss of quality, especially in images with high spatial frequency.      *      * @return true if subsampling of images is allowed, false otherwise.      */
specifier|public
name|boolean
name|isSubsamplingAllowed
parameter_list|()
block|{
return|return
name|subsamplingAllowed
return|;
block|}
comment|/**      * Sets a value instructing the renderer whether it is allowed to subsample images before      * drawing. The subsampling frequency is determined according to image size and requested scale.      *      * Subsampling may be faster and less memory-intensive in some cases, but it may also lead to      * loss of quality, especially in images with high spatial frequency.      *      * @param subsamplingAllowed The new value indicating if subsampling is allowed.      */
specifier|public
name|void
name|setSubsamplingAllowed
parameter_list|(
name|boolean
name|subsamplingAllowed
parameter_list|)
block|{
name|this
operator|.
name|subsamplingAllowed
operator|=
name|subsamplingAllowed
expr_stmt|;
block|}
comment|/**      * @return the defaultDestination      */
specifier|public
name|RenderDestination
name|getDefaultDestination
parameter_list|()
block|{
return|return
name|defaultDestination
return|;
block|}
comment|/**      * @param defaultDestination the defaultDestination to set      */
specifier|public
name|void
name|setDefaultDestination
parameter_list|(
name|RenderDestination
name|defaultDestination
parameter_list|)
block|{
name|this
operator|.
name|defaultDestination
operator|=
name|defaultDestination
expr_stmt|;
block|}
comment|/**      * Get the rendering hints.      *      * @return the rendering hints or null if none are set.      */
specifier|public
name|RenderingHints
name|getRenderingHints
parameter_list|()
block|{
return|return
name|renderingHints
return|;
block|}
comment|/**      * Set the rendering hints. Use this to influence rendering quality and speed. If you don't set      * them yourself or pass null, PDFBox will decide<b><u>at runtime</u></b> depending on the      * destination.      *      * @param renderingHints      */
specifier|public
name|void
name|setRenderingHints
parameter_list|(
name|RenderingHints
name|renderingHints
parameter_list|)
block|{
name|this
operator|.
name|renderingHints
operator|=
name|renderingHints
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
return|return
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
name|scale
argument_list|,
name|imageType
argument_list|,
name|defaultDestination
operator|==
literal|null
condition|?
name|RenderDestination
operator|.
name|EXPORT
else|:
name|defaultDestination
argument_list|)
return|;
block|}
comment|/**      * Returns the given page as an RGB or ARGB image at the given scale.      * @param pageIndex the zero-based index of the page to be converted      * @param scale the scaling factor, where 1 = 72 DPI      * @param imageType the type of image to return      * @param destination controlling visibility of optional content groups      * @return the rendered page image      * @throws IOException if the PDF cannot be read      */
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
parameter_list|,
name|RenderDestination
name|destination
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
comment|// PDFBOX-4306 avoid single blank pixel line on the right or on the bottom
name|int
name|widthPx
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|floor
argument_list|(
name|widthPt
operator|*
name|scale
argument_list|)
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|int
name|heightPx
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|floor
argument_list|(
name|heightPt
operator|*
name|scale
argument_list|)
argument_list|,
literal|1
argument_list|)
decl_stmt|;
comment|// PDFBOX-4518 the maximum size (w*h) of a buffered image is limited to Integer.MAX_VALUE
if|if
condition|(
operator|(
name|long
operator|)
name|widthPx
operator|*
operator|(
name|long
operator|)
name|heightPx
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Maximum size of image exceeded (w * h * scale) = "
comment|//
operator|+
name|widthPt
operator|+
literal|" * "
operator|+
name|heightPt
operator|+
literal|" * "
operator|+
name|scale
operator|+
literal|"> "
operator|+
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
throw|;
block|}
name|int
name|rotationAngle
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
name|int
name|bimType
init|=
name|imageType
operator|.
name|toBufferedImageType
argument_list|()
decl_stmt|;
if|if
condition|(
name|imageType
operator|!=
name|ImageType
operator|.
name|ARGB
operator|&&
name|hasBlendMode
argument_list|(
name|page
argument_list|)
condition|)
block|{
comment|// PDFBOX-4095: if the PDF has blending on the top level, draw on transparent background
comment|// Inpired from PDF.js: if a PDF page uses any blend modes other than Normal,
comment|// PDF.js renders everything on a fully transparent RGBA canvas.
comment|// Finally when the page has been rendered, PDF.js draws the RGBA canvas on a white canvas.
name|bimType
operator|=
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
expr_stmt|;
block|}
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
name|bimType
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
name|bimType
argument_list|)
expr_stmt|;
block|}
name|pageImage
operator|=
name|image
expr_stmt|;
comment|// use a transparent background if the image type supports alpha
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
name|image
operator|.
name|getType
argument_list|()
operator|==
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
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
name|g
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
name|transform
argument_list|(
name|g
argument_list|,
name|page
argument_list|,
name|scale
argument_list|,
name|scale
argument_list|)
expr_stmt|;
comment|// the end-user may provide a custom PageDrawer
name|RenderingHints
name|actualRenderingHints
init|=
name|renderingHints
operator|==
literal|null
condition|?
name|createDefaultRenderingHints
argument_list|(
name|g
argument_list|)
else|:
name|renderingHints
decl_stmt|;
name|PageDrawerParameters
name|parameters
init|=
operator|new
name|PageDrawerParameters
argument_list|(
name|this
argument_list|,
name|page
argument_list|,
name|subsamplingAllowed
argument_list|,
name|destination
argument_list|,
name|actualRenderingHints
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
name|g
argument_list|,
name|page
operator|.
name|getCropBox
argument_list|()
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
if|if
condition|(
name|image
operator|.
name|getType
argument_list|()
operator|!=
name|imageType
operator|.
name|toBufferedImageType
argument_list|()
condition|)
block|{
comment|// PDFBOX-4095: draw temporary transparent image on white background
name|BufferedImage
name|newImage
init|=
operator|new
name|BufferedImage
argument_list|(
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
name|imageType
operator|.
name|toBufferedImageType
argument_list|()
argument_list|)
decl_stmt|;
name|Graphics2D
name|dstGraphics
init|=
name|newImage
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|dstGraphics
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
name|dstGraphics
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
name|dstGraphics
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
name|dstGraphics
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|image
operator|=
name|newImage
expr_stmt|;
block|}
return|return
name|image
return|;
block|}
comment|/**      * Renders a given page to an AWT Graphics2D instance at 72 DPI.      *<p>      * Read {@link #renderPageToGraphics(int, java.awt.Graphics2D, float, float, org.apache.pdfbox.rendering.RenderDestination) renderPageToGraphics(int, Graphics2D, float, float, RenderDestination)}      * before using this.      *      * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @throws IOException if the PDF cannot be read      */
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
comment|/**      * Renders a given page to an AWT Graphics2D instance.      *<p>      * Read {@link #renderPageToGraphics(int, java.awt.Graphics2D, float, float, org.apache.pdfbox.rendering.RenderDestination) renderPageToGraphics(int, Graphics2D, float, float, RenderDestination)}      * before using this.      *      * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @param scale the scaling factor, where 1 = 72 DPI      * @throws IOException if the PDF cannot be read      */
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
name|renderPageToGraphics
argument_list|(
name|pageIndex
argument_list|,
name|graphics
argument_list|,
name|scale
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
comment|/**      * Renders a given page to an AWT Graphics2D instance.      *<p>      * Read {@link #renderPageToGraphics(int, java.awt.Graphics2D, float, float, org.apache.pdfbox.rendering.RenderDestination) renderPageToGraphics(int, Graphics2D, float, float, RenderDestination)}      * before using this.      *      * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @param scaleX the scale to draw the page at for the x-axis, where 1 = 72 DPI      * @param scaleY the scale to draw the page at for the y-axis, where 1 = 72 DPI      * @throws IOException if the PDF cannot be read      */
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
name|scaleX
parameter_list|,
name|float
name|scaleY
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
name|scaleX
argument_list|,
name|scaleY
argument_list|,
name|defaultDestination
operator|==
literal|null
condition|?
name|RenderDestination
operator|.
name|VIEW
else|:
name|defaultDestination
argument_list|)
expr_stmt|;
block|}
comment|/**      * Renders a given page to an AWT Graphics2D instance.      *<p>      * Known problems:      *<ul>      *<li>rendering of PDF files with transparencies is not supported on Ubuntu, see      *<a href="https://issues.apache.org/jira/browse/PDFBOX-4581">PDFBOX-4581</a> and      *<a href="https://bugs.openjdk.java.net/browse/JDK-6689349">JDK-6689349</a>. Rendering will      * not abort, but the pages will be rendered incorrectly.</li>      *<li>Clipping the Graphics2D will not work properly, see      *<a href="https://issues.apache.org/jira/browse/PDFBOX-4583">PDFBOX-4583</a>.</li>      *</ul>      * If you encounter these problems, then you should render into an image by using the      * {@link #renderImage(int) renderImage} methods.      *       * @param pageIndex the zero-based index of the page to be converted      * @param graphics the Graphics2D on which to draw the page      * @param scaleX the scale to draw the page at for the x-axis, where 1 = 72 DPI      * @param scaleY the scale to draw the page at for the y-axis, where 1 = 72 DPI      * @param destination controlling visibility of optional content groups      * @throws IOException if the PDF cannot be read      */
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
name|scaleX
parameter_list|,
name|float
name|scaleY
parameter_list|,
name|RenderDestination
name|destination
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
name|transform
argument_list|(
name|graphics
argument_list|,
name|page
argument_list|,
name|scaleX
argument_list|,
name|scaleY
argument_list|)
expr_stmt|;
name|PDRectangle
name|cropBox
init|=
name|page
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|graphics
operator|.
name|clearRect
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
comment|// the end-user may provide a custom PageDrawer
name|RenderingHints
name|actualRenderingHints
init|=
name|renderingHints
operator|==
literal|null
condition|?
name|createDefaultRenderingHints
argument_list|(
name|graphics
argument_list|)
else|:
name|renderingHints
decl_stmt|;
name|PageDrawerParameters
name|parameters
init|=
operator|new
name|PageDrawerParameters
argument_list|(
name|this
argument_list|,
name|page
argument_list|,
name|subsamplingAllowed
argument_list|,
name|destination
argument_list|,
name|actualRenderingHints
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
comment|/**      * Indicates whether an optional content group is enabled.      * @param group the group      * @return true if the group is enabled      */
specifier|public
name|boolean
name|isGroupEnabled
parameter_list|(
name|PDOptionalContentGroup
name|group
parameter_list|)
block|{
name|PDOptionalContentProperties
name|ocProperties
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getOCProperties
argument_list|()
decl_stmt|;
return|return
name|ocProperties
operator|==
literal|null
operator|||
name|ocProperties
operator|.
name|isGroupEnabled
argument_list|(
name|group
argument_list|)
return|;
block|}
comment|// scale rotate translate
specifier|private
name|void
name|transform
parameter_list|(
name|Graphics2D
name|graphics
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|float
name|scaleX
parameter_list|,
name|float
name|scaleY
parameter_list|)
block|{
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
name|int
name|rotationAngle
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
name|PDRectangle
name|cropBox
init|=
name|page
operator|.
name|getCropBox
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
default|default:
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
name|Math
operator|.
name|toRadians
argument_list|(
name|rotationAngle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isBitonal
parameter_list|(
name|Graphics2D
name|graphics
parameter_list|)
block|{
name|GraphicsConfiguration
name|deviceConfiguration
init|=
name|graphics
operator|.
name|getDeviceConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|deviceConfiguration
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|GraphicsDevice
name|device
init|=
name|deviceConfiguration
operator|.
name|getDevice
argument_list|()
decl_stmt|;
if|if
condition|(
name|device
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DisplayMode
name|displayMode
init|=
name|device
operator|.
name|getDisplayMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|displayMode
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|displayMode
operator|.
name|getBitDepth
argument_list|()
operator|==
literal|1
return|;
block|}
specifier|private
name|RenderingHints
name|createDefaultRenderingHints
parameter_list|(
name|Graphics2D
name|graphics
parameter_list|)
block|{
name|RenderingHints
name|r
init|=
operator|new
name|RenderingHints
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|r
operator|.
name|put
argument_list|(
name|RenderingHints
operator|.
name|KEY_INTERPOLATION
argument_list|,
name|isBitonal
argument_list|(
name|graphics
argument_list|)
condition|?
name|RenderingHints
operator|.
name|VALUE_INTERPOLATION_NEAREST_NEIGHBOR
else|:
name|RenderingHints
operator|.
name|VALUE_INTERPOLATION_BICUBIC
argument_list|)
expr_stmt|;
name|r
operator|.
name|put
argument_list|(
name|RenderingHints
operator|.
name|KEY_RENDERING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_RENDER_QUALITY
argument_list|)
expr_stmt|;
name|r
operator|.
name|put
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|isBitonal
argument_list|(
name|graphics
argument_list|)
condition|?
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_OFF
else|:
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_ON
argument_list|)
expr_stmt|;
return|return
name|r
return|;
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
name|PageDrawer
name|pageDrawer
init|=
operator|new
name|PageDrawer
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|pageDrawer
operator|.
name|setAnnotationFilter
argument_list|(
name|annotationFilter
argument_list|)
expr_stmt|;
return|return
name|pageDrawer
return|;
block|}
specifier|private
name|boolean
name|hasBlendMode
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
comment|// check the current resources for blend modes
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|COSName
name|name
range|:
name|resources
operator|.
name|getExtGStateNames
argument_list|()
control|)
block|{
name|PDExtendedGraphicsState
name|extGState
init|=
name|resources
operator|.
name|getExtGState
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|extGState
operator|==
literal|null
condition|)
block|{
comment|// can happen if key exists but no value
comment|// see PDFBOX-3950-23EGDHXSBBYQLKYOKGZUOVYVNE675PRD.pdf
continue|continue;
block|}
name|BlendMode
name|blendMode
init|=
name|extGState
operator|.
name|getBlendMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|blendMode
operator|!=
name|BlendMode
operator|.
name|NORMAL
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the image to which the current page is being rendered.      * May be null if the page is rendered to a Graphics2D object      * instead of a BufferedImage.      */
name|BufferedImage
name|getPageImage
parameter_list|()
block|{
return|return
name|pageImage
return|;
block|}
specifier|private
specifier|static
name|void
name|suggestKCMS
parameter_list|()
block|{
name|String
name|cmmProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"sun.java2d.cmm"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isMinJdk8
argument_list|()
operator|&&
operator|!
literal|"sun.java2d.cmm.kcms.KcmsServiceProvider"
operator|.
name|equals
argument_list|(
name|cmmProperty
argument_list|)
condition|)
block|{
try|try
block|{
comment|// Make sure that class exists
name|Class
operator|.
name|forName
argument_list|(
literal|"sun.java2d.cmm.kcms.KcmsServiceProvider"
argument_list|)
expr_stmt|;
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
decl_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|isGoodVersion
argument_list|(
name|version
argument_list|,
literal|"1.8.0_(\\d+)"
argument_list|,
literal|191
argument_list|)
operator|||
name|isGoodVersion
argument_list|(
name|version
argument_list|,
literal|"9.0.(\\d+)"
argument_list|,
literal|4
argument_list|)
condition|)
block|{
return|return;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Your current java version is: "
operator|+
name|version
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"To get higher rendering speed on old java 1.8 or 9 versions,"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  update to the latest 1.8 or 9 version (>= 1.8.0_191 or>= 9.0.4),"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  or"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  use the option -Dsun.java2d.cmm=sun.java2d.cmm.kcms.KcmsServiceProvider"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  or call System.setProperty(\"sun.java2d.cmm\", \"sun.java2d.cmm.kcms.KcmsServiceProvider\")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// KCMS not available
block|}
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isGoodVersion
parameter_list|(
name|String
name|version
parameter_list|,
name|String
name|regex
parameter_list|,
name|int
name|min
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
operator|&&
name|matcher
operator|.
name|groupCount
argument_list|()
operator|>=
literal|1
condition|)
block|{
try|try
block|{
name|int
name|v
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|>=
name|min
condition|)
block|{
comment|// LCMS no longer bad
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isMinJdk8
parameter_list|()
block|{
comment|// strategy from lucene-solr/lucene/core/src/java/org/apache/lucene/util/Constants.java
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
decl_stmt|;
specifier|final
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|version
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|major
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|minor
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|minor
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|major
operator|>
literal|1
operator|||
operator|(
name|major
operator|==
literal|1
operator|&&
name|minor
operator|>=
literal|8
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
comment|// maybe some new numbering scheme in the 22nd century
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

