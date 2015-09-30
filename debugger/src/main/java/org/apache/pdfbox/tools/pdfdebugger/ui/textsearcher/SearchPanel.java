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
operator|.
name|textsearcher
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
name|Dimension
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
name|ComponentListener
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
name|KeyEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|AbstractAction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Action
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
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
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
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|KeyStroke
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
name|ChangeListener
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
name|DocumentListener
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
name|PDFDebugger
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that provides the search pane.  */
end_comment

begin_class
class|class
name|SearchPanel
block|{
specifier|private
specifier|final
name|Action
name|nextAction
decl_stmt|;
specifier|private
specifier|final
name|Action
name|previousAction
decl_stmt|;
specifier|private
name|JCheckBox
name|caseSensitive
decl_stmt|;
specifier|private
name|JTextField
name|searchField
decl_stmt|;
specifier|private
name|JLabel
name|counterLabel
decl_stmt|;
specifier|private
name|JPanel
name|panel
decl_stmt|;
specifier|private
name|Action
name|closeAction
init|=
operator|new
name|AbstractAction
argument_list|()
block|{
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
name|panel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|closeAction
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getParent
argument_list|()
operator|.
name|transferFocus
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|private
specifier|final
name|Action
name|findAction
init|=
operator|new
name|AbstractAction
argument_list|()
block|{
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
if|if
condition|(
operator|!
name|panel
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|panel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getParent
argument_list|()
operator|.
name|validate
argument_list|()
expr_stmt|;
return|return;
block|}
name|reFocus
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/**      * Constructor.      * @param documentListener DocumentListener instance.      * @param changeListener ChangeListener instance.      * @param compListener ComponentListener instance.      * @param nextAction Action instance for next find.      * @param previousAction Action instance for previous find.      */
name|SearchPanel
parameter_list|(
name|DocumentListener
name|documentListener
parameter_list|,
name|ChangeListener
name|changeListener
parameter_list|,
name|ComponentListener
name|compListener
parameter_list|,
name|Action
name|nextAction
parameter_list|,
name|Action
name|previousAction
parameter_list|)
block|{
name|this
operator|.
name|nextAction
operator|=
name|nextAction
expr_stmt|;
name|this
operator|.
name|previousAction
operator|=
name|previousAction
expr_stmt|;
name|initUI
argument_list|(
name|documentListener
argument_list|,
name|changeListener
argument_list|,
name|compListener
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initUI
parameter_list|(
name|DocumentListener
name|documentListener
parameter_list|,
name|ChangeListener
name|changeListener
parameter_list|,
name|ComponentListener
name|compListener
parameter_list|)
block|{
name|searchField
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|searchField
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
name|documentListener
argument_list|)
expr_stmt|;
name|counterLabel
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|counterLabel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|JButton
name|nextButton
init|=
operator|new
name|JButton
argument_list|()
decl_stmt|;
name|nextButton
operator|.
name|setAction
argument_list|(
name|nextAction
argument_list|)
expr_stmt|;
name|nextButton
operator|.
name|setText
argument_list|(
literal|"Next"
argument_list|)
expr_stmt|;
name|JButton
name|previousButton
init|=
operator|new
name|JButton
argument_list|()
decl_stmt|;
name|previousButton
operator|.
name|setAction
argument_list|(
name|previousAction
argument_list|)
expr_stmt|;
name|previousButton
operator|.
name|setText
argument_list|(
literal|"Previous"
argument_list|)
expr_stmt|;
name|caseSensitive
operator|=
operator|new
name|JCheckBox
argument_list|(
literal|"Match case"
argument_list|)
expr_stmt|;
name|caseSensitive
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|caseSensitive
operator|.
name|addChangeListener
argument_list|(
name|changeListener
argument_list|)
expr_stmt|;
name|caseSensitive
operator|.
name|setToolTipText
argument_list|(
literal|"Check for case sensitive search"
argument_list|)
expr_stmt|;
name|JButton
name|crossButton
init|=
operator|new
name|JButton
argument_list|()
decl_stmt|;
name|crossButton
operator|.
name|setAction
argument_list|(
name|closeAction
argument_list|)
expr_stmt|;
name|crossButton
operator|.
name|setText
argument_list|(
literal|"Done"
argument_list|)
expr_stmt|;
name|closeAction
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|panel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|panel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createMatteBorder
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|Color
operator|.
name|LIGHT_GRAY
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setBackground
argument_list|(
operator|new
name|Color
argument_list|(
literal|230
argument_list|,
literal|230
argument_list|,
literal|230
argument_list|)
argument_list|)
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
name|X_AXIS
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createHorizontalGlue
argument_list|()
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|searchField
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|counterLabel
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|previousButton
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|nextButton
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|caseSensitive
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createRigidArea
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|5
argument_list|,
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|crossButton
argument_list|)
expr_stmt|;
name|panel
operator|.
name|addComponentListener
argument_list|(
name|compListener
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|getInputMap
argument_list|(
name|JComponent
operator|.
name|WHEN_IN_FOCUSED_WINDOW
argument_list|)
operator|.
name|put
argument_list|(
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_ESCAPE
argument_list|,
literal|0
argument_list|)
argument_list|,
name|closeAction
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isCaseSensitive
parameter_list|()
block|{
return|return
name|caseSensitive
operator|.
name|isSelected
argument_list|()
return|;
block|}
name|String
name|getSearchWord
parameter_list|()
block|{
return|return
name|searchField
operator|.
name|getText
argument_list|()
return|;
block|}
name|void
name|reset
parameter_list|()
block|{
name|counterLabel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|void
name|updateCounterLabel
parameter_list|(
name|int
name|now
parameter_list|,
name|int
name|total
parameter_list|)
block|{
if|if
condition|(
operator|!
name|counterLabel
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|counterLabel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|total
operator|==
literal|0
condition|)
block|{
name|counterLabel
operator|.
name|setText
argument_list|(
literal|" No match found "
argument_list|)
expr_stmt|;
name|nextAction
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|counterLabel
operator|.
name|setText
argument_list|(
literal|" "
operator|+
name|now
operator|+
literal|" of "
operator|+
name|total
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
name|JPanel
name|getPanel
parameter_list|()
block|{
return|return
name|panel
return|;
block|}
specifier|public
name|void
name|reFocus
parameter_list|()
block|{
name|searchField
operator|.
name|requestFocus
argument_list|()
expr_stmt|;
name|String
name|searchKey
init|=
name|searchField
operator|.
name|getText
argument_list|()
decl_stmt|;
name|searchField
operator|.
name|setText
argument_list|(
name|searchKey
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setSelectionStart
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|searchField
operator|.
name|setSelectionEnd
argument_list|(
name|searchField
operator|.
name|getText
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|closeAction
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addMenuListeners
parameter_list|(
name|PDFDebugger
name|frame
parameter_list|)
block|{
name|frame
operator|.
name|getFindMenu
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindMenuItem
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|findAction
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindNextMenuItem
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|nextAction
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindPreviousMenuItem
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|previousAction
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeMenuListeners
parameter_list|(
name|PDFDebugger
name|frame
parameter_list|)
block|{
name|frame
operator|.
name|getFindMenu
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindMenuItem
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|findAction
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindNextMenuItem
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|nextAction
argument_list|)
expr_stmt|;
name|frame
operator|.
name|getFindPreviousMenuItem
argument_list|()
operator|.
name|removeActionListener
argument_list|(
name|previousAction
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

