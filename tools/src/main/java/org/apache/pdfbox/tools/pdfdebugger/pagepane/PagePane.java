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
name|tools
operator|.
name|pdfdebugger
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
name|Color
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
name|concurrent
operator|.
name|ExecutionException
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
name|ImageIcon
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|tools
operator|.
name|pdfdebugger
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
name|tools
operator|.
name|pdfdebugger
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
name|tools
operator|.
name|util
operator|.
name|ImageUtil
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
block|{
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
specifier|final
name|PDDocument
name|document
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
specifier|public
name|PagePane
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|page
parameter_list|)
block|{
name|PDPage
name|pdPage
init|=
operator|new
name|PDPage
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|pageIndex
operator|=
name|document
operator|.
name|getPages
argument_list|()
operator|.
name|indexOf
argument_list|(
name|pdPage
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|initUI
argument_list|()
expr_stmt|;
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
name|BOLD
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|pageLabel
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|GREEN
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
name|setBackground
argument_list|(
name|panel
operator|.
name|getBackground
argument_list|()
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
name|label
operator|.
name|setText
argument_list|(
literal|"Loading..."
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
comment|// render in a background thread: rendering is read-only, so this should be ok, despite
comment|// the fact that PDDocument is not officially thread safe
operator|new
name|RenderWorker
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
operator|.
name|execute
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
condition|)
block|{
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
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
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
operator|=
name|ZoomMenu
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|zoomMenu
operator|.
name|addMenuListeners
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|zoomMenu
operator|.
name|setZoomSelection
argument_list|(
name|ZoomMenu
operator|.
name|ZOOM_100_PERCENT
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
name|setRotationSelection
argument_list|(
name|RotationMenu
operator|.
name|ROTATE_0_DEGREES
argument_list|)
expr_stmt|;
name|rotationMenu
operator|.
name|setEnableMenu
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|RenderWorker
parameter_list|(
name|float
name|scale
parameter_list|,
name|int
name|rotation
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
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|BufferedImage
name|bim
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
return|return
name|ImageUtil
operator|.
name|getRotatedImage
argument_list|(
name|bim
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
name|label
operator|.
name|setIcon
argument_list|(
operator|new
name|ImageIcon
argument_list|(
name|get
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
catch|catch
parameter_list|(
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

