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

begin_comment
comment|/**  *  * @author Khyrul Bashar  * @author Tilman Hausherr  *  * Base class.  */
end_comment

begin_class
specifier|abstract
class|class
name|MenuBase
block|{
specifier|private
name|JMenu
name|menu
init|=
literal|null
decl_stmt|;
comment|//    /**
comment|//     * Constructor.
comment|//     */
comment|//    protected MenuBase()
comment|//    {
comment|//        menu = createMenu();
comment|//    }
specifier|protected
name|void
name|setMenu
parameter_list|(
name|JMenu
name|menu
parameter_list|)
block|{
name|this
operator|.
name|menu
operator|=
name|menu
expr_stmt|;
block|}
comment|/**      * Provide the JMenu instance of the ZoomMenu.      *      * @return JMenu instance.      */
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
comment|/**      * Set if the menu should be enabled or disabled.      *      * @param isEnable boolean instance.      */
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
comment|/**      * Add the ActionListener for the menuitems.      *      * @param listener ActionListener.      */
specifier|public
name|void
name|addMenuListeners
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
block|}
end_class

end_unit

