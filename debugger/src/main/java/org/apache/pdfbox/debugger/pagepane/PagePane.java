begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|pagepane
package|;
end_package

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
name|debugger
operator|.
name|PDFDebugger
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
name|debugger
operator|.
name|ui
operator|.
name|ImageUtil
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
name|debugger
operator|.
name|ui
operator|.
name|RotationMenu
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
name|debugger
operator|.
name|ui
operator|.
name|ViewMenu
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
name|debugger
operator|.
name|ui
operator|.
name|ZoomMenu
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
name|rendering
operator|.
name|PDFRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BoxLayout
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingWorker
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|AncestorEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|AncestorListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GraphicsEnvironment
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseMotionListener
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|debugger
operator|.
name|ui
operator|.
name|HighResolutionImageIcon
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
name|debugger
operator|.
name|ui
operator|.
name|TextDialog
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
name|interactive
operator|.
name|action
operator|.
name|PDAction
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
name|action
operator|.
name|PDActionGoTo
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
name|action
operator|.
name|PDActionURI
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
name|PDAnnotation
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
name|PDAnnotationLink
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
name|PDAnnotationWidget
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDNamedDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
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
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDField
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

begin_comment
comment|/**  * Display the page number and a page rendering.  *   * @author Tilman Hausherr  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PagePane
implements|implements
name|ActionListener
implements|,
name|AncestorListener
implements|,
name|MouseMotionListener
implements|,
name|MouseListener
block|{
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|JLabel
name|statuslabel
decl_stmt|;
specifier|private
specifier|final
name|PDPage
name|page
decl_stmt|;
specifier|private
name|JPanel
name|panel
decl_stmt|;
specifier|private
name|int
name|pageIndex
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|JLabel
name|label
decl_stmt|;
specifier|private
name|ZoomMenu
name|zoomMenu
decl_stmt|;
specifier|private
name|RotationMenu
name|rotationMenu
decl_stmt|;
specifier|private
name|ViewMenu
name|viewMenu
decl_stmt|;
specifier|private
name|String
name|labelText
init|=
literal|""
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|PDRectangle
argument_list|,
name|String
argument_list|>
name|rectMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AffineTransform
name|defaultTransform
init|=
name|GraphicsEnvironment
operator|.
name|getLocalGraphicsEnvironment
argument_list|()
operator|.
name|getDefaultScreenDevice
argument_list|()
operator|.
name|getDefaultConfiguration
argument_list|()
operator|.
name|getDefaultTransform
argument_list|()
decl_stmt|;
specifier|public
name|PagePane
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|pageDict
parameter_list|,
name|JLabel
name|statuslabel
parameter_list|)
block|{
name|page
operator|=
operator|new
name|PDPage
argument_list|(
name|pageDict
argument_list|)
expr_stmt|;
name|pageIndex
operator|=
name|document
operator|.
name|getPages
argument_list|()
operator|.
name|indexOf
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|statuslabel
operator|=
name|statuslabel
expr_stmt|;
name|initUI
argument_list|()
expr_stmt|;
name|initRectMap
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initRectMap
parameter_list|()
block|{
try|try
block|{
name|collectFieldLocations
argument_list|()
expr_stmt|;
name|collectLinkLocations
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|private
name|void
name|collectLinkLocations
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
name|PDAnnotation
name|annotation
range|:
name|page
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|PDAnnotationLink
condition|)
block|{
name|PDAnnotationLink
name|linkAnnotation
init|=
operator|(
name|PDAnnotationLink
operator|)
name|annotation
decl_stmt|;
name|PDAction
name|action
init|=
name|linkAnnotation
operator|.
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|PDActionURI
condition|)
block|{
name|PDActionURI
name|uriAction
init|=
operator|(
name|PDActionURI
operator|)
name|action
decl_stmt|;
name|rectMap
operator|.
name|put
argument_list|(
name|annotation
operator|.
name|getRectangle
argument_list|()
argument_list|,
literal|"URI: "
operator|+
name|uriAction
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|PDDestination
name|destination
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|PDActionGoTo
condition|)
block|{
name|PDActionGoTo
name|goToAction
init|=
operator|(
name|PDActionGoTo
operator|)
name|action
decl_stmt|;
name|destination
operator|=
name|goToAction
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
name|linkAnnotation
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|instanceof
name|PDNamedDestination
condition|)
block|{
name|destination
operator|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|findNamedDestinationPage
argument_list|(
operator|(
name|PDNamedDestination
operator|)
name|destination
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|instanceof
name|PDPageDestination
condition|)
block|{
name|PDPageDestination
name|pageDestination
init|=
operator|(
name|PDPageDestination
operator|)
name|destination
decl_stmt|;
name|int
name|pageNum
init|=
name|pageDestination
operator|.
name|retrievePageNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|pageNum
operator|!=
operator|-
literal|1
condition|)
block|{
name|rectMap
operator|.
name|put
argument_list|(
name|annotation
operator|.
name|getRectangle
argument_list|()
argument_list|,
literal|"Page destination: "
operator|+
operator|(
name|pageNum
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|collectFieldLocations
parameter_list|()
block|{
name|PDAcroForm
name|acroForm
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
if|if
condition|(
name|acroForm
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|PDField
name|field
range|:
name|acroForm
operator|.
name|getFieldTree
argument_list|()
control|)
block|{
name|String
name|fullyQualifiedName
init|=
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|field
operator|.
name|getWidgets
argument_list|()
control|)
block|{
if|if
condition|(
name|page
operator|.
name|equals
argument_list|(
name|widget
operator|.
name|getPage
argument_list|()
argument_list|)
condition|)
block|{
name|rectMap
operator|.
name|put
argument_list|(
name|widget
operator|.
name|getRectangle
argument_list|()
argument_list|,
literal|"Field name: "
operator|+
name|fullyQualifiedName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|initUI
parameter_list|()
block|{
name|panel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|panel
operator|.
name|setLayout
argument_list|(
operator|new
name|BoxLayout
argument_list|(
name|panel
argument_list|,
name|BoxLayout
operator|.
name|Y_AXIS
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|pageLabelText
init|=
name|pageIndex
operator|<
literal|0
condition|?
literal|"Page number not found"
else|:
literal|"Page "
operator|+
operator|(
name|pageIndex
operator|+
literal|1
operator|)
decl_stmt|;
comment|// append PDF page label, if available
name|String
name|lbl
init|=
name|PDFDebugger
operator|.
name|getPageLabel
argument_list|(
name|document
argument_list|,
name|pageIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|lbl
operator|!=
literal|null
condition|)
block|{
name|pageLabelText
operator|+=
literal|" - "
operator|+
name|lbl
expr_stmt|;
block|}
name|JLabel
name|pageLabel
init|=
operator|new
name|JLabel
argument_list|(
name|pageLabelText
argument_list|)
decl_stmt|;
name|pageLabel
operator|.
name|setAlignmentX
argument_list|(
name|Component
operator|.
name|CENTER_ALIGNMENT
argument_list|)
expr_stmt|;
name|pageLabel
operator|.
name|setFont
argument_list|(
operator|new
name|Font
argument_list|(
name|Font
operator|.
name|MONOSPACED
argument_list|,
name|Font
operator|.
name|PLAIN
argument_list|,
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|pageLabel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|5
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|pageLabel
argument_list|)
expr_stmt|;
name|label
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|label
operator|.
name|addMouseMotionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|label
operator|.
name|addMouseListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|label
operator|.
name|setAlignmentX
argument_list|(
name|Component
operator|.
name|CENTER_ALIGNMENT
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|panel
operator|.
name|addAncestorListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|zoomMenu
operator|=
name|ZoomMenu
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|zoomMenu
operator|.
name|changeZoomSelection
argument_list|(
name|zoomMenu
operator|.
name|getPageZoomScale
argument_list|()
argument_list|)
expr_stmt|;
name|startRendering
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the main panel that hold all the UI elements.      *      * @return JPanel instance      */
specifier|public
name|Component
name|getPanel
parameter_list|()
block|{
return|return
name|panel
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|actionEvent
parameter_list|)
block|{
name|String
name|actionCommand
init|=
name|actionEvent
operator|.
name|getActionCommand
argument_list|()
decl_stmt|;
if|if
condition|(
name|ZoomMenu
operator|.
name|isZoomMenu
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|RotationMenu
operator|.
name|isRotationMenu
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|ViewMenu
operator|.
name|isRenderingOptions
argument_list|(
name|actionCommand
argument_list|)
condition|)
block|{
name|startRendering
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ViewMenu
operator|.
name|isExtractText
argument_list|(
name|actionEvent
argument_list|)
condition|)
block|{
name|startExtracting
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|startExtracting
parameter_list|()
block|{
name|TextDialog
name|textDialog
init|=
name|TextDialog
operator|.
name|instance
argument_list|()
decl_stmt|;
name|textDialog
operator|.
name|setSize
argument_list|(
literal|800
argument_list|,
literal|400
argument_list|)
expr_stmt|;
name|textDialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|textDialog
operator|.
name|setLocation
argument_list|(
name|getPanel
argument_list|()
operator|.
name|getLocationOnScreen
argument_list|()
operator|.
name|x
operator|+
name|getPanel
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|getPanel
argument_list|()
operator|.
name|getLocationOnScreen
argument_list|()
operator|.
name|y
operator|+
name|getPanel
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|stripper
operator|.
name|setStartPage
argument_list|(
name|pageIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setEndPage
argument_list|(
name|pageIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|textDialog
operator|.
name|setText
argument_list|(
name|stripper
operator|.
name|getText
argument_list|(
name|document
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|startRendering
parameter_list|()
block|{
comment|// render in a background thread: rendering is read-only, so this should be ok, despite
comment|// the fact that PDDocument is not officially thread safe
operator|new
name|RenderWorker
argument_list|(
name|ZoomMenu
operator|.
name|getZoomScale
argument_list|()
argument_list|,
name|RotationMenu
operator|.
name|getRotationDegrees
argument_list|()
argument_list|,
name|ViewMenu
operator|.
name|isShowTextStripper
argument_list|()
argument_list|,
name|ViewMenu
operator|.
name|isShowTextStripperBeads
argument_list|()
argument_list|,
name|ViewMenu
operator|.
name|isShowFontBBox
argument_list|()
argument_list|,
name|ViewMenu
operator|.
name|isShowGlyphBounds
argument_list|()
argument_list|,
name|ViewMenu
operator|.
name|isAllowSubsampling
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|zoomMenu
operator|.
name|setPageZoomScale
argument_list|(
name|ZoomMenu
operator|.
name|getZoomScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|ancestorAdded
parameter_list|(
name|AncestorEvent
name|ancestorEvent
parameter_list|)
block|{
name|zoomMenu
operator|.
name|addMenuListeners
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|zoomMenu
operator|.
name|setEnableMenu
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rotationMenu
operator|=
name|RotationMenu
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|rotationMenu
operator|.
name|addMenuListeners
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|rotationMenu
operator|.
name|setEnableMenu
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|viewMenu
operator|=
name|ViewMenu
operator|.
name|getInstance
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|JMenu
name|menuInstance
init|=
name|viewMenu
operator|.
name|getMenu
argument_list|()
decl_stmt|;
name|int
name|itemCount
init|=
name|menuInstance
operator|.
name|getItemCount
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
name|itemCount
condition|;
name|i
operator|++
control|)
block|{
name|JMenuItem
name|item
init|=
name|menuInstance
operator|.
name|getItem
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|!=
literal|null
condition|)
block|{
name|item
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|item
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|ancestorRemoved
parameter_list|(
name|AncestorEvent
name|ancestorEvent
parameter_list|)
block|{
name|boolean
name|isFirstEntrySkipped
init|=
literal|false
decl_stmt|;
name|zoomMenu
operator|.
name|setEnableMenu
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rotationMenu
operator|.
name|setEnableMenu
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|JMenu
name|menuInstance
init|=
name|viewMenu
operator|.
name|getMenu
argument_list|()
decl_stmt|;
name|int
name|itemCount
init|=
name|menuInstance
operator|.
name|getItemCount
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
name|itemCount
condition|;
name|i
operator|++
control|)
block|{
name|JMenuItem
name|item
init|=
name|menuInstance
operator|.
name|getItem
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// skip the first JMenuItem as this shall always be shown
if|if
condition|(
name|item
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isFirstEntrySkipped
condition|)
block|{
name|isFirstEntrySkipped
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|item
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|item
operator|.
name|removeActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|ancestorMoved
parameter_list|(
name|AncestorEvent
name|ancestorEvent
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|mouseDragged
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{     }
comment|/**      * Catch mouse event to display cursor position in PDF coordinates in the status bar.      *      * @param e mouse event with position      */
annotation|@
name|Override
specifier|public
name|void
name|mouseMoved
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|float
name|height
init|=
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|float
name|width
init|=
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|offsetX
init|=
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|offsetY
init|=
name|page
operator|.
name|getCropBox
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|zoomScale
init|=
name|zoomMenu
operator|.
name|getPageZoomScale
argument_list|()
decl_stmt|;
name|float
name|x
init|=
name|e
operator|.
name|getX
argument_list|()
operator|/
name|zoomScale
operator|*
operator|(
name|float
operator|)
name|defaultTransform
operator|.
name|getScaleX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
name|e
operator|.
name|getY
argument_list|()
operator|/
name|zoomScale
operator|*
operator|(
name|float
operator|)
name|defaultTransform
operator|.
name|getScaleY
argument_list|()
decl_stmt|;
name|int
name|x1
decl_stmt|,
name|y1
decl_stmt|;
switch|switch
condition|(
operator|(
name|RotationMenu
operator|.
name|getRotationDegrees
argument_list|()
operator|+
name|page
operator|.
name|getRotation
argument_list|()
operator|)
operator|%
literal|360
condition|)
block|{
case|case
literal|90
case|:
name|x1
operator|=
call|(
name|int
call|)
argument_list|(
name|y
operator|+
name|offsetX
argument_list|)
expr_stmt|;
name|y1
operator|=
call|(
name|int
call|)
argument_list|(
name|x
operator|+
name|offsetY
argument_list|)
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|x1
operator|=
call|(
name|int
call|)
argument_list|(
name|width
operator|-
name|x
operator|+
name|offsetX
argument_list|)
expr_stmt|;
name|y1
operator|=
call|(
name|int
call|)
argument_list|(
name|y
operator|-
name|offsetY
argument_list|)
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|x1
operator|=
call|(
name|int
call|)
argument_list|(
name|width
operator|-
name|y
operator|+
name|offsetX
argument_list|)
expr_stmt|;
name|y1
operator|=
call|(
name|int
call|)
argument_list|(
name|height
operator|-
name|x
operator|+
name|offsetY
argument_list|)
expr_stmt|;
break|break;
case|case
literal|0
case|:
default|default:
name|x1
operator|=
call|(
name|int
call|)
argument_list|(
name|x
operator|+
name|offsetX
argument_list|)
expr_stmt|;
name|y1
operator|=
call|(
name|int
call|)
argument_list|(
name|height
operator|-
name|y
operator|+
name|offsetY
argument_list|)
expr_stmt|;
break|break;
block|}
name|String
name|text
init|=
literal|"x: "
operator|+
name|x1
operator|+
literal|", y: "
operator|+
name|y1
decl_stmt|;
comment|// are we in a field widget?
for|for
control|(
name|Entry
argument_list|<
name|PDRectangle
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|rectMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|contains
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|)
condition|)
block|{
name|text
operator|+=
literal|", "
operator|+
name|rectMap
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|statuslabel
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseClicked
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|mousePressed
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|mouseReleased
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|mouseEntered
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|mouseExited
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|statuslabel
operator|.
name|setText
argument_list|(
name|labelText
argument_list|)
expr_stmt|;
block|}
comment|/**      * Note that PDDocument is not officially thread safe, caution advised.      */
specifier|private
specifier|final
class|class
name|RenderWorker
extends|extends
name|SwingWorker
argument_list|<
name|BufferedImage
argument_list|,
name|Integer
argument_list|>
block|{
specifier|private
specifier|final
name|float
name|scale
decl_stmt|;
specifier|private
specifier|final
name|int
name|rotation
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showTextStripper
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showTextStripperBeads
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showFontBBox
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|showGlyphBounds
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|allowSubsampling
decl_stmt|;
specifier|private
name|RenderWorker
parameter_list|(
name|float
name|scale
parameter_list|,
name|int
name|rotation
parameter_list|,
name|boolean
name|showTextStripper
parameter_list|,
name|boolean
name|showTextStripperBeads
parameter_list|,
name|boolean
name|showFontBBox
parameter_list|,
name|boolean
name|showGlyphBounds
parameter_list|,
name|boolean
name|allowSubsampling
parameter_list|)
block|{
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
name|this
operator|.
name|rotation
operator|=
name|rotation
expr_stmt|;
name|this
operator|.
name|showTextStripper
operator|=
name|showTextStripper
expr_stmt|;
name|this
operator|.
name|showTextStripperBeads
operator|=
name|showTextStripperBeads
expr_stmt|;
name|this
operator|.
name|showFontBBox
operator|=
name|showFontBBox
expr_stmt|;
name|this
operator|.
name|showGlyphBounds
operator|=
name|showGlyphBounds
expr_stmt|;
name|this
operator|.
name|allowSubsampling
operator|=
name|allowSubsampling
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|BufferedImage
name|doInBackground
parameter_list|()
throws|throws
name|IOException
block|{
name|label
operator|.
name|setIcon
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|labelText
operator|=
literal|"Rendering..."
expr_stmt|;
name|label
operator|.
name|setText
argument_list|(
name|labelText
argument_list|)
expr_stmt|;
name|statuslabel
operator|.
name|setText
argument_list|(
name|labelText
argument_list|)
expr_stmt|;
name|PDFRenderer
name|renderer
init|=
operator|new
name|DebugPDFRenderer
argument_list|(
name|document
argument_list|,
name|this
operator|.
name|showGlyphBounds
argument_list|)
decl_stmt|;
name|renderer
operator|.
name|setSubsamplingAllowed
argument_list|(
name|allowSubsampling
argument_list|)
expr_stmt|;
name|long
name|t0
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|renderer
operator|.
name|renderImage
argument_list|(
name|pageIndex
argument_list|,
name|scale
argument_list|)
decl_stmt|;
name|long
name|t1
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|long
name|ms
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|t1
operator|-
name|t0
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|labelText
operator|=
literal|"Rendered in "
operator|+
name|ms
operator|+
literal|" ms"
expr_stmt|;
name|statuslabel
operator|.
name|setText
argument_list|(
name|labelText
argument_list|)
expr_stmt|;
comment|// debug overlays
name|DebugTextOverlay
name|debugText
init|=
operator|new
name|DebugTextOverlay
argument_list|(
name|document
argument_list|,
name|pageIndex
argument_list|,
name|scale
argument_list|,
name|showTextStripper
argument_list|,
name|showTextStripperBeads
argument_list|,
name|showFontBBox
argument_list|)
decl_stmt|;
name|Graphics2D
name|g
init|=
name|image
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|debugText
operator|.
name|renderTo
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|ImageUtil
operator|.
name|getRotatedImage
argument_list|(
name|image
argument_list|,
name|rotation
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|done
parameter_list|()
block|{
try|try
block|{
name|BufferedImage
name|image
init|=
name|get
argument_list|()
decl_stmt|;
comment|// We cannot use "label.setIcon(new ImageIcon(get()))" here
comment|// because of blurry upscaling in JDK9. Instead, the label is now created with
comment|// a smaller size than the image to compensate that the
comment|// image is scaled up with some screen configurations (e.g. 125% on windows).
comment|// See PDFBOX-3665 for more sample code and discussion.
name|label
operator|.
name|setSize
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
operator|/
name|defaultTransform
operator|.
name|getScaleX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|image
operator|.
name|getHeight
argument_list|()
operator|/
name|defaultTransform
operator|.
name|getScaleY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|label
operator|.
name|setIcon
argument_list|(
operator|new
name|HighResolutionImageIcon
argument_list|(
name|image
argument_list|,
name|label
operator|.
name|getWidth
argument_list|()
argument_list|,
name|label
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|label
operator|.
name|setText
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
name|label
operator|.
name|setText
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
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

