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
name|ui
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BorderLayout
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
name|Point
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Toolkit
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
name|KeyEvent
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
name|java
operator|.
name|util
operator|.
name|List
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
name|Icon
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
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JEditorPane
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
name|JTextPane
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
name|ScrollPaneConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|WindowConstants
import|;
end_import

begin_comment
comment|/**  * A dialog to display a runtime exception stack trace.  *  * @author Pinaki Poddar  *  * Modified from ErrorDialog.java and SwingHelper.java of the Apache OpenJPA  *<a href="https://svn.apache.org/repos/asf/openjpa/trunk/openjpa-examples/openbooks/src/main/java/jpa/tools/swing/">jpa.tools.swing  * package</a>.  *  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"serial"
block|,
literal|"squid:MaximumInheritanceDepth"
block|}
argument_list|)
specifier|public
class|class
name|ErrorDialog
extends|extends
name|JDialog
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|FILTERS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"java.awt."
argument_list|,
literal|"javax.swing."
argument_list|,
literal|"sun.reflect."
argument_list|,
literal|"java.util.concurrent."
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Dimension
name|MESSAGE_SIZE
init|=
operator|new
name|Dimension
argument_list|(
literal|800
argument_list|,
literal|200
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Dimension
name|STACKTRACE_SIZE
init|=
operator|new
name|Dimension
argument_list|(
literal|800
argument_list|,
literal|300
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Dimension
name|TOTAL_SIZE
init|=
operator|new
name|Dimension
argument_list|(
literal|800
argument_list|,
literal|500
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BORDER_SIZE
init|=
literal|20
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEWLINE
init|=
literal|"\r\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INDENT
init|=
literal|"    "
decl_stmt|;
specifier|private
name|boolean
name|showingDetails
decl_stmt|;
specifier|private
name|boolean
name|isFiltering
init|=
literal|true
decl_stmt|;
specifier|private
name|JComponent
name|message
decl_stmt|;
specifier|private
name|JComponent
name|main
decl_stmt|;
specifier|private
name|JScrollPane
name|details
decl_stmt|;
specifier|private
name|JTextPane
name|stacktrace
decl_stmt|;
specifier|private
specifier|final
name|Throwable
name|error
decl_stmt|;
comment|/**      * Creates a modal dialog to display the given exception message.      *      * @param t the exception to display      */
specifier|public
name|ErrorDialog
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a modal dialog to display the given exception message.      *      * @param owner if non-null, then the dialog is positioned (centered) w.r.t. this component      * @param t the exception to display      */
specifier|public
name|ErrorDialog
parameter_list|(
name|JComponent
name|owner
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|this
argument_list|(
name|owner
argument_list|,
literal|null
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a modal dialog to display the given exception message.      *      * @param owner if non-null, then the dialog is positioned (centered) w.r.t. this component      * @param icon the icon to display      * @param t the exception to display      */
specifier|public
name|ErrorDialog
parameter_list|(
name|JComponent
name|owner
parameter_list|,
name|Icon
name|icon
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|setTitle
argument_list|(
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|icon
operator|instanceof
name|ImageIcon
condition|)
block|{
name|setIconImage
argument_list|(
operator|(
operator|(
name|ImageIcon
operator|)
name|icon
operator|)
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|error
operator|=
name|t
expr_stmt|;
name|message
operator|=
name|createErrorMessage
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|main
operator|=
name|createContent
argument_list|()
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|main
argument_list|)
expr_stmt|;
name|pack
argument_list|()
expr_stmt|;
name|position
argument_list|(
name|this
argument_list|,
name|owner
argument_list|)
expr_stmt|;
block|}
comment|/**      * Position the given component at the center of the given parent component or physical screen.      *      * @param c the component to be positioned      * @param parent the component whose center will match the center of the given component. If      * null, the given component will match the screen center.      *      */
specifier|static
name|void
name|position
parameter_list|(
name|Component
name|c
parameter_list|,
name|Component
name|parent
parameter_list|)
block|{
name|Dimension
name|d
init|=
name|c
operator|.
name|getPreferredSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|Dimension
name|s
init|=
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getScreenSize
argument_list|()
decl_stmt|;
name|c
operator|.
name|setLocation
argument_list|(
name|s
operator|.
name|width
operator|/
literal|2
operator|-
name|d
operator|.
name|width
operator|/
literal|2
argument_list|,
name|s
operator|.
name|height
operator|/
literal|2
operator|-
name|d
operator|.
name|height
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Point
name|p
init|=
name|parent
operator|.
name|getLocationOnScreen
argument_list|()
decl_stmt|;
name|int
name|pw
init|=
name|parent
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|ph
init|=
name|parent
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|c
operator|.
name|setLocation
argument_list|(
name|p
operator|.
name|x
operator|+
name|pw
operator|/
literal|2
operator|-
name|d
operator|.
name|width
operator|/
literal|2
argument_list|,
name|p
operator|.
name|y
operator|+
name|ph
operator|/
literal|2
operator|-
name|d
operator|.
name|height
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates the display with the top-level exception message followed by a pane (that toggles)      * for detailed stack traces.      */
specifier|final
name|JComponent
name|createContent
parameter_list|()
block|{
specifier|final
name|JButton
name|showDetails
init|=
operator|new
name|JButton
argument_list|(
literal|"Show Details>>"
argument_list|)
decl_stmt|;
name|showDetails
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
if|if
condition|(
name|showingDetails
condition|)
block|{
name|main
operator|.
name|remove
argument_list|(
name|details
argument_list|)
expr_stmt|;
name|main
operator|.
name|validate
argument_list|()
expr_stmt|;
name|main
operator|.
name|setPreferredSize
argument_list|(
name|MESSAGE_SIZE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|details
operator|==
literal|null
condition|)
block|{
name|details
operator|=
name|createDetailedMessage
argument_list|()
expr_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|stacktrace
operator|.
name|setText
argument_list|(
name|generateStackTrace
argument_list|(
name|error
argument_list|,
name|buffer
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|stacktrace
operator|.
name|setCaretPosition
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|stacktrace
operator|.
name|setBackground
argument_list|(
name|main
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|stacktrace
operator|.
name|setPreferredSize
argument_list|(
name|STACKTRACE_SIZE
argument_list|)
expr_stmt|;
block|}
name|main
operator|.
name|add
argument_list|(
name|details
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|main
operator|.
name|validate
argument_list|()
expr_stmt|;
name|main
operator|.
name|setPreferredSize
argument_list|(
name|TOTAL_SIZE
argument_list|)
expr_stmt|;
block|}
name|showingDetails
operator|=
operator|!
name|showingDetails
expr_stmt|;
name|showDetails
operator|.
name|setText
argument_list|(
name|showingDetails
condition|?
literal|"<< Hide Details"
else|:
literal|"Show Details>>"
argument_list|)
expr_stmt|;
name|ErrorDialog
operator|.
name|this
operator|.
name|pack
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|JPanel
name|messagePanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
specifier|final
name|JCheckBox
name|filter
init|=
operator|new
name|JCheckBox
argument_list|(
literal|"Filter stack traces"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|setSelected
argument_list|(
name|isFiltering
argument_list|)
expr_stmt|;
name|filter
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|isFiltering
operator|=
name|filter
operator|.
name|isSelected
argument_list|()
expr_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|stacktrace
operator|.
name|setText
argument_list|(
name|generateStackTrace
argument_list|(
name|error
argument_list|,
name|buffer
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|stacktrace
operator|.
name|setCaretPosition
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|stacktrace
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBackground
argument_list|(
name|messagePanel
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|JPanel
name|buttonPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createHorizontalStrut
argument_list|(
name|BORDER_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|showDetails
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createHorizontalGlue
argument_list|()
argument_list|)
expr_stmt|;
name|messagePanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|messagePanel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
name|BORDER_SIZE
argument_list|,
name|BORDER_SIZE
argument_list|,
name|BORDER_SIZE
argument_list|,
name|BORDER_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|messagePanel
operator|.
name|add
argument_list|(
name|message
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|messagePanel
operator|.
name|add
argument_list|(
name|buttonPanel
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|messagePanel
operator|.
name|setPreferredSize
argument_list|(
name|MESSAGE_SIZE
argument_list|)
expr_stmt|;
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
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|messagePanel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
comment|// allow closing with ESC
name|ActionListener
name|actionListener
init|=
name|actionEvent
lambda|->
name|dispose
argument_list|()
decl_stmt|;
name|KeyStroke
name|stroke
init|=
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
decl_stmt|;
name|panel
operator|.
name|registerKeyboardAction
argument_list|(
name|actionListener
argument_list|,
name|stroke
argument_list|,
name|JComponent
operator|.
name|WHEN_IN_FOCUSED_WINDOW
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
comment|/**      * Creates a non-editable widget to display the error message.      *      */
specifier|final
name|JComponent
name|createErrorMessage
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|String
name|txt
init|=
name|t
operator|.
name|getLocalizedMessage
argument_list|()
decl_stmt|;
name|JEditorPane
name|msg
init|=
operator|new
name|JEditorPane
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
name|txt
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
comment|/**      * Creates a non-editable widget to display the detailed stack trace.      */
name|JScrollPane
name|createDetailedMessage
parameter_list|()
block|{
name|stacktrace
operator|=
operator|new
name|JTextPane
argument_list|()
expr_stmt|;
name|stacktrace
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
operator|new
name|JScrollPane
argument_list|(
name|stacktrace
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
return|;
block|}
comment|/**      * Recursively print the stack trace on the given buffer.      */
name|StringBuilder
name|generateStackTrace
parameter_list|(
name|Throwable
name|t
parameter_list|,
name|StringBuilder
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|NEWLINE
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|toString
argument_list|(
name|t
operator|.
name|getStackTrace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Throwable
name|cause
init|=
name|t
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
operator|&&
operator|!
name|cause
operator|.
name|equals
argument_list|(
name|t
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"Caused by: "
argument_list|)
expr_stmt|;
name|generateStackTrace
argument_list|(
name|cause
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
name|StringBuilder
name|toString
parameter_list|(
name|StackTraceElement
index|[]
name|traces
parameter_list|)
block|{
name|StringBuilder
name|err
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|StackTraceElement
name|e
range|:
name|traces
control|)
block|{
if|if
condition|(
operator|!
name|isFiltering
operator|||
operator|!
name|isSuppressed
argument_list|(
name|e
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|str
init|=
name|e
operator|.
name|toString
argument_list|()
decl_stmt|;
name|err
operator|.
name|append
argument_list|(
name|INDENT
argument_list|)
operator|.
name|append
argument_list|(
name|str
argument_list|)
operator|.
name|append
argument_list|(
name|NEWLINE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|err
return|;
block|}
comment|/**      * Affirms if the error messages from the given class name is to be suppressed.      */
specifier|private
name|boolean
name|isSuppressed
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|FILTERS
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|className
operator|::
name|startsWith
argument_list|)
return|;
block|}
block|}
end_class

end_unit

