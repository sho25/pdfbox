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
name|tools
operator|.
name|pdfdebugger
operator|.
name|streampane
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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
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
name|javax
operator|.
name|swing
operator|.
name|Box
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
name|JComponent
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|LineBorder
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
comment|/**  * @author Khyrul Bashar  *  * A class that provides the container for the image in case of image showing in stream pane.  */
end_comment

begin_class
class|class
name|StreamImageView
implements|implements
name|ActionListener
implements|,
name|AncestorListener
block|{
specifier|private
specifier|final
name|BufferedImage
name|image
decl_stmt|;
specifier|private
name|JScrollPane
name|scrollPane
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
comment|/**      * constructor.      * @param image instance of BufferedImage.      */
name|StreamImageView
parameter_list|(
name|BufferedImage
name|image
parameter_list|)
block|{
name|this
operator|.
name|image
operator|=
name|image
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
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
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
name|label
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|label
operator|.
name|setBorder
argument_list|(
operator|new
name|LineBorder
argument_list|(
name|Color
operator|.
name|BLACK
argument_list|)
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
name|setIcon
argument_list|(
operator|new
name|ImageIcon
argument_list|(
name|image
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createVerticalGlue
argument_list|()
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
name|add
argument_list|(
name|Box
operator|.
name|createVerticalGlue
argument_list|()
argument_list|)
expr_stmt|;
name|scrollPane
operator|=
operator|new
name|JScrollPane
argument_list|()
expr_stmt|;
name|scrollPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|400
argument_list|)
argument_list|)
expr_stmt|;
name|scrollPane
operator|.
name|addAncestorListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|scrollPane
operator|.
name|setViewportView
argument_list|(
name|panel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the view i.e container containing image.      * @return A JComponent instance.      */
name|JComponent
name|getView
parameter_list|()
block|{
return|return
name|scrollPane
return|;
block|}
specifier|private
name|Image
name|zoomImage
parameter_list|(
name|BufferedImage
name|origin
parameter_list|,
name|float
name|scale
parameter_list|,
name|int
name|rotation
parameter_list|)
block|{
name|BufferedImage
name|rotatedImage
init|=
name|ImageUtil
operator|.
name|getRotatedImage
argument_list|(
name|origin
argument_list|,
name|rotation
argument_list|)
decl_stmt|;
name|int
name|resizedWidth
init|=
call|(
name|int
call|)
argument_list|(
name|rotatedImage
operator|.
name|getWidth
argument_list|()
operator|*
name|scale
argument_list|)
decl_stmt|;
name|int
name|resizedHeight
init|=
call|(
name|int
call|)
argument_list|(
name|rotatedImage
operator|.
name|getHeight
argument_list|()
operator|*
name|scale
argument_list|)
decl_stmt|;
return|return
name|rotatedImage
operator|.
name|getScaledInstance
argument_list|(
name|resizedWidth
argument_list|,
name|resizedHeight
argument_list|,
name|BufferedImage
operator|.
name|SCALE_SMOOTH
argument_list|)
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
name|addImage
argument_list|(
name|zoomImage
argument_list|(
name|image
argument_list|,
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
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addImage
parameter_list|(
name|Image
name|img
parameter_list|)
block|{
name|label
operator|.
name|setIcon
argument_list|(
operator|new
name|ImageIcon
argument_list|(
name|img
argument_list|)
argument_list|)
expr_stmt|;
name|label
operator|.
name|revalidate
argument_list|()
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
block|}
end_class

end_unit

