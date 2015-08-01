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
name|ui
package|;
end_package

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
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ButtonGroup
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
name|JRadioButtonMenuItem
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A singleton class that provides zoom menu which can be used to show zoom menu in the menubar.  * To act upon the menu item selection user of the class must add ActionListener which will check for  * the action command and act accordingly.  */
end_comment

begin_class
specifier|public
class|class
name|ZoomMenu
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ZOOM_50_PERCENT
init|=
literal|"50%"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ZOOM_100_PERCENT
init|=
literal|"100%"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ZOOM_200_PERCENT
init|=
literal|"200%"
decl_stmt|;
specifier|private
specifier|static
name|ZoomMenu
name|instance
decl_stmt|;
specifier|private
name|JMenu
name|menu
init|=
literal|null
decl_stmt|;
specifier|private
name|JRadioButtonMenuItem
name|zoom50Item
decl_stmt|;
specifier|private
name|JRadioButtonMenuItem
name|zoom100Item
decl_stmt|;
specifier|private
name|JRadioButtonMenuItem
name|zoom200Item
decl_stmt|;
comment|/**      * Constructor.      */
specifier|private
name|ZoomMenu
parameter_list|()
block|{
name|menu
operator|=
name|createZoomMenu
argument_list|()
expr_stmt|;
block|}
comment|/**      * Provides the ZoomMenu instance.      * @return ZoomMenu instance.      */
specifier|public
specifier|static
name|ZoomMenu
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|ZoomMenu
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Provide the JMenu instance of the ZoomMenu.      * @return JMenu instance.      */
specifier|public
name|JMenu
name|getMenu
parameter_list|()
block|{
return|return
name|this
operator|.
name|menu
return|;
block|}
comment|/**      * Set if the menu should be enabled or disabled.      * @param isEnable boolean instance.      */
specifier|public
name|void
name|setEnableMenu
parameter_list|(
name|boolean
name|isEnable
parameter_list|)
block|{
name|menu
operator|.
name|setEnabled
argument_list|(
name|isEnable
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the ActionListener for the menuitems.      * @param listener ActionListener.      * @return the ZoomMenu instance.      */
specifier|public
name|ZoomMenu
name|menuListeners
parameter_list|(
name|ActionListener
name|listener
parameter_list|)
block|{
for|for
control|(
name|Component
name|comp
range|:
name|menu
operator|.
name|getMenuComponents
argument_list|()
control|)
block|{
name|JMenuItem
name|menuItem
init|=
operator|(
name|JMenuItem
operator|)
name|comp
decl_stmt|;
name|removeActionListeners
argument_list|(
name|menuItem
argument_list|)
expr_stmt|;
name|menuItem
operator|.
name|addActionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Set the zoom selection.      * @param selection String instance.      */
specifier|public
name|void
name|setZoomSelection
parameter_list|(
name|String
name|selection
parameter_list|)
block|{
if|if
condition|(
name|ZOOM_50_PERCENT
operator|.
name|equals
argument_list|(
name|selection
argument_list|)
condition|)
block|{
name|zoom50Item
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ZOOM_100_PERCENT
operator|.
name|equals
argument_list|(
name|selection
argument_list|)
condition|)
block|{
name|zoom100Item
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ZOOM_200_PERCENT
operator|.
name|equals
argument_list|(
name|selection
argument_list|)
condition|)
block|{
name|zoom200Item
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isZoomMenu
parameter_list|(
name|String
name|actionCommand
parameter_list|)
block|{
return|return
name|ZOOM_50_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|ZOOM_100_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|ZOOM_200_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|float
name|getZoomScale
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|.
name|zoom50Item
operator|.
name|isSelected
argument_list|()
condition|)
block|{
return|return
literal|0.5f
return|;
block|}
if|if
condition|(
name|instance
operator|.
name|zoom100Item
operator|.
name|isSelected
argument_list|()
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|instance
operator|.
name|zoom200Item
operator|.
name|isSelected
argument_list|()
condition|)
block|{
return|return
literal|2
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
specifier|public
specifier|static
name|float
name|getZoomScale
parameter_list|(
name|String
name|actionCommand
parameter_list|)
block|{
if|if
condition|(
name|ZOOM_50_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
condition|)
block|{
return|return
literal|0.5f
return|;
block|}
elseif|else
if|if
condition|(
name|ZOOM_100_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|ZOOM_200_PERCENT
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
condition|)
block|{
return|return
literal|2
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
specifier|private
name|void
name|removeActionListeners
parameter_list|(
name|JMenuItem
name|menuItem
parameter_list|)
block|{
for|for
control|(
name|ActionListener
name|listener
range|:
name|menuItem
operator|.
name|getActionListeners
argument_list|()
control|)
block|{
name|menuItem
operator|.
name|removeActionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|JMenu
name|createZoomMenu
parameter_list|()
block|{
name|menu
operator|=
operator|new
name|JMenu
argument_list|()
expr_stmt|;
name|menu
operator|.
name|setText
argument_list|(
literal|"Zoom"
argument_list|)
expr_stmt|;
name|zoom50Item
operator|=
operator|new
name|JRadioButtonMenuItem
argument_list|()
expr_stmt|;
name|zoom100Item
operator|=
operator|new
name|JRadioButtonMenuItem
argument_list|()
expr_stmt|;
name|zoom200Item
operator|=
operator|new
name|JRadioButtonMenuItem
argument_list|()
expr_stmt|;
name|zoom100Item
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ButtonGroup
name|bg
init|=
operator|new
name|ButtonGroup
argument_list|()
decl_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|zoom50Item
argument_list|)
expr_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|zoom100Item
argument_list|)
expr_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|zoom200Item
argument_list|)
expr_stmt|;
name|zoom50Item
operator|.
name|setText
argument_list|(
name|ZOOM_50_PERCENT
argument_list|)
expr_stmt|;
name|zoom100Item
operator|.
name|setText
argument_list|(
name|ZOOM_100_PERCENT
argument_list|)
expr_stmt|;
name|zoom200Item
operator|.
name|setText
argument_list|(
name|ZOOM_200_PERCENT
argument_list|)
expr_stmt|;
name|menu
operator|.
name|add
argument_list|(
name|zoom50Item
argument_list|)
expr_stmt|;
name|menu
operator|.
name|add
argument_list|(
name|zoom100Item
argument_list|)
expr_stmt|;
name|menu
operator|.
name|add
argument_list|(
name|zoom200Item
argument_list|)
expr_stmt|;
return|return
name|menu
return|;
block|}
block|}
end_class

end_unit

