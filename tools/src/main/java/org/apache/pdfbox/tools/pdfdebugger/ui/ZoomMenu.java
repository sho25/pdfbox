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
name|util
operator|.
name|Arrays
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
name|JRadioButtonMenuItem
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A singleton class that provides zoom menu which can be used to show zoom menu in the menubar. To  * act upon the menu item selection user of the class must add ActionListener which will check for  * the action command and act accordingly.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ZoomMenu
extends|extends
name|MenuBase
block|{
specifier|private
specifier|static
class|class
name|ZoomMenuItem
extends|extends
name|JRadioButtonMenuItem
block|{
specifier|private
specifier|final
name|int
name|zoom
decl_stmt|;
name|ZoomMenuItem
parameter_list|(
name|String
name|text
parameter_list|,
name|int
name|zoom
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|this
operator|.
name|zoom
operator|=
name|zoom
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
specifier|final
name|String
name|ZOOM_100_PERCENT
init|=
literal|"100%"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|ZOOMS
init|=
operator|new
name|int
index|[]
block|{
literal|25
block|,
literal|50
block|,
literal|100
block|,
literal|200
block|,
literal|400
block|}
decl_stmt|;
specifier|private
specifier|static
name|ZoomMenu
name|instance
decl_stmt|;
specifier|private
specifier|final
name|JMenu
name|menu
decl_stmt|;
comment|/**      * Constructor.      */
specifier|private
name|ZoomMenu
parameter_list|()
block|{
name|menu
operator|=
operator|new
name|JMenu
argument_list|(
literal|"Zoom"
argument_list|)
expr_stmt|;
name|ButtonGroup
name|bg
init|=
operator|new
name|ButtonGroup
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|zoom
range|:
name|ZOOMS
control|)
block|{
name|ZoomMenuItem
name|zoomItem
init|=
operator|new
name|ZoomMenuItem
argument_list|(
name|zoom
operator|+
literal|"%"
argument_list|,
name|zoom
argument_list|)
decl_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|zoomItem
argument_list|)
expr_stmt|;
name|menu
operator|.
name|add
argument_list|(
name|zoomItem
argument_list|)
expr_stmt|;
block|}
name|setMenu
argument_list|(
name|menu
argument_list|)
expr_stmt|;
block|}
comment|/**      * Provides the ZoomMenu instance.      *      * @return ZoomMenu instance.      */
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
comment|/**      * Set the zoom selection.      *      * @param selection zoom menu string, e.g. "100%".      * @throws IllegalArgumentException if the parameter doesn't belong to a zoom menu item.      */
specifier|public
name|void
name|setZoomSelection
parameter_list|(
name|String
name|selection
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
name|JRadioButtonMenuItem
name|menuItem
init|=
operator|(
name|JRadioButtonMenuItem
operator|)
name|comp
decl_stmt|;
if|if
condition|(
name|menuItem
operator|.
name|getText
argument_list|()
operator|.
name|equals
argument_list|(
name|selection
argument_list|)
condition|)
block|{
name|menuItem
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no zoom menu item found for: "
operator|+
name|selection
argument_list|)
throw|;
block|}
comment|/**      * Tell whether the command belongs to the zoom menu.      *      * @param actionCommand a menu command string.      * @return true if the command is a zoom menu command, e.g. "100%", false if not.      */
specifier|public
specifier|static
name|boolean
name|isZoomMenu
parameter_list|(
name|String
name|actionCommand
parameter_list|)
block|{
if|if
condition|(
operator|!
name|actionCommand
operator|.
name|matches
argument_list|(
literal|"^\\d+%$"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|zoom
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|actionCommand
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|actionCommand
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|ZOOMS
argument_list|,
name|zoom
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/**      * Tell the current zoom scale.      *      * @return the current zoom scale.      * @throws IllegalStateException if no zoom menu item is selected.      */
specifier|public
specifier|static
name|float
name|getZoomScale
parameter_list|()
block|{
for|for
control|(
name|Component
name|comp
range|:
name|instance
operator|.
name|menu
operator|.
name|getMenuComponents
argument_list|()
control|)
block|{
name|ZoomMenuItem
name|menuItem
init|=
operator|(
name|ZoomMenuItem
operator|)
name|comp
decl_stmt|;
if|if
condition|(
name|menuItem
operator|.
name|isSelected
argument_list|()
condition|)
block|{
return|return
name|menuItem
operator|.
name|zoom
operator|/
literal|100f
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no zoom menu item is selected"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

