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
package|;
end_package

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
name|encryption
operator|.
name|InvalidPasswordException
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
name|gui
operator|.
name|PDFTreeModel
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
name|gui
operator|.
name|PDFTreeCellRenderer
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
name|gui
operator|.
name|ArrayEntry
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
name|gui
operator|.
name|MapEntry
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
name|cos
operator|.
name|COSBoolean
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
name|COSFloat
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
name|COSInteger
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
name|cos
operator|.
name|COSNull
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
name|COSStream
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
name|COSString
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFileChooser
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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|UIManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|encryption
operator|.
name|StandardDecryptionMaterial
import|;
end_import

begin_comment
comment|/**  *  * @author  wurtz  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFDebugger
extends|extends
name|javax
operator|.
name|swing
operator|.
name|JFrame
block|{
specifier|private
name|File
name|currentDir
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
specifier|private
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NONSEQ
init|=
literal|"-nonSeq"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|useNonSeqParser
init|=
literal|false
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDFDebugger
parameter_list|()
block|{
name|initComponents
argument_list|()
expr_stmt|;
block|}
comment|/**      * This method is called from within the constructor to      * initialize the form.      * WARNING: Do NOT modify this code. The content of this method is      * always regenerated by the Form Editor.      */
specifier|private
name|void
name|initComponents
parameter_list|()
block|{
name|jSplitPane1
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JSplitPane
argument_list|()
expr_stmt|;
name|jScrollPane1
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
argument_list|()
expr_stmt|;
name|jTree1
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JTree
argument_list|()
expr_stmt|;
name|jScrollPane2
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
argument_list|()
expr_stmt|;
name|jTextPane1
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JTextPane
argument_list|()
expr_stmt|;
name|menuBar
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuBar
argument_list|()
expr_stmt|;
name|fileMenu
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenu
argument_list|()
expr_stmt|;
name|openMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|saveMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|saveAsMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|exitMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|editMenu
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenu
argument_list|()
expr_stmt|;
name|cutMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|copyMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|pasteMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|deleteMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|helpMenu
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenu
argument_list|()
expr_stmt|;
name|contentsMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|aboutMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|jTree1
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|PDFTreeCellRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|jTree1
operator|.
name|setModel
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"PDFBox - PDF Viewer"
argument_list|)
expr_stmt|;
name|addWindowListener
argument_list|(
operator|new
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|WindowAdapter
argument_list|()
block|{
specifier|public
name|void
name|windowClosing
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|WindowEvent
name|evt
parameter_list|)
block|{
name|exitForm
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|jScrollPane1
operator|.
name|setBorder
argument_list|(
operator|new
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|BevelBorder
argument_list|(
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|BevelBorder
operator|.
name|RAISED
argument_list|)
argument_list|)
expr_stmt|;
name|jScrollPane1
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|java
operator|.
name|awt
operator|.
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|jTree1
operator|.
name|addTreeSelectionListener
argument_list|(
operator|new
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeSelectionListener
argument_list|()
block|{
specifier|public
name|void
name|valueChanged
parameter_list|(
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeSelectionEvent
name|evt
parameter_list|)
block|{
name|jTree1ValueChanged
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|jScrollPane1
operator|.
name|setViewportView
argument_list|(
name|jTree1
argument_list|)
expr_stmt|;
name|jSplitPane1
operator|.
name|setRightComponent
argument_list|(
name|jScrollPane2
argument_list|)
expr_stmt|;
name|jScrollPane2
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|java
operator|.
name|awt
operator|.
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|jScrollPane2
operator|.
name|setViewportView
argument_list|(
name|jTextPane1
argument_list|)
expr_stmt|;
name|jSplitPane1
operator|.
name|setLeftComponent
argument_list|(
name|jScrollPane1
argument_list|)
expr_stmt|;
name|JScrollPane
name|documentScroller
init|=
operator|new
name|JScrollPane
argument_list|()
decl_stmt|;
comment|//documentScroller.setPreferredSize( new Dimension( 300, 500 ) );
name|documentScroller
operator|.
name|setViewportView
argument_list|(
name|documentPanel
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|jSplitPane1
argument_list|,
name|java
operator|.
name|awt
operator|.
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|setText
argument_list|(
literal|"File"
argument_list|)
expr_stmt|;
name|openMenuItem
operator|.
name|setText
argument_list|(
literal|"Open"
argument_list|)
expr_stmt|;
name|openMenuItem
operator|.
name|setToolTipText
argument_list|(
literal|"Open PDF file"
argument_list|)
expr_stmt|;
name|openMenuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
name|evt
parameter_list|)
block|{
name|openMenuItemActionPerformed
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|openMenuItem
argument_list|)
expr_stmt|;
name|saveMenuItem
operator|.
name|setText
argument_list|(
literal|"Save"
argument_list|)
expr_stmt|;
name|saveAsMenuItem
operator|.
name|setText
argument_list|(
literal|"Save As ..."
argument_list|)
expr_stmt|;
name|exitMenuItem
operator|.
name|setText
argument_list|(
literal|"Exit"
argument_list|)
expr_stmt|;
name|exitMenuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
name|evt
parameter_list|)
block|{
name|exitMenuItemActionPerformed
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|exitMenuItem
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|fileMenu
argument_list|)
expr_stmt|;
name|editMenu
operator|.
name|setText
argument_list|(
literal|"Edit"
argument_list|)
expr_stmt|;
name|cutMenuItem
operator|.
name|setText
argument_list|(
literal|"Cut"
argument_list|)
expr_stmt|;
name|editMenu
operator|.
name|add
argument_list|(
name|cutMenuItem
argument_list|)
expr_stmt|;
name|copyMenuItem
operator|.
name|setText
argument_list|(
literal|"Copy"
argument_list|)
expr_stmt|;
name|editMenu
operator|.
name|add
argument_list|(
name|copyMenuItem
argument_list|)
expr_stmt|;
name|pasteMenuItem
operator|.
name|setText
argument_list|(
literal|"Paste"
argument_list|)
expr_stmt|;
name|editMenu
operator|.
name|add
argument_list|(
name|pasteMenuItem
argument_list|)
expr_stmt|;
name|deleteMenuItem
operator|.
name|setText
argument_list|(
literal|"Delete"
argument_list|)
expr_stmt|;
name|editMenu
operator|.
name|add
argument_list|(
name|deleteMenuItem
argument_list|)
expr_stmt|;
name|helpMenu
operator|.
name|setText
argument_list|(
literal|"Help"
argument_list|)
expr_stmt|;
name|contentsMenuItem
operator|.
name|setText
argument_list|(
literal|"Contents"
argument_list|)
expr_stmt|;
name|helpMenu
operator|.
name|add
argument_list|(
name|contentsMenuItem
argument_list|)
expr_stmt|;
name|aboutMenuItem
operator|.
name|setText
argument_list|(
literal|"About"
argument_list|)
expr_stmt|;
name|helpMenu
operator|.
name|add
argument_list|(
name|aboutMenuItem
argument_list|)
expr_stmt|;
name|setJMenuBar
argument_list|(
name|menuBar
argument_list|)
expr_stmt|;
name|java
operator|.
name|awt
operator|.
name|Dimension
name|screenSize
init|=
name|java
operator|.
name|awt
operator|.
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getScreenSize
argument_list|()
decl_stmt|;
name|setBounds
argument_list|(
operator|(
name|screenSize
operator|.
name|width
operator|-
literal|700
operator|)
operator|/
literal|2
argument_list|,
operator|(
name|screenSize
operator|.
name|height
operator|-
literal|600
operator|)
operator|/
literal|2
argument_list|,
literal|700
argument_list|,
literal|600
argument_list|)
expr_stmt|;
block|}
comment|//GEN-END:initComponents
specifier|private
name|void
name|openMenuItemActionPerformed
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
name|evt
parameter_list|)
block|{
name|JFileChooser
name|chooser
init|=
operator|new
name|JFileChooser
argument_list|()
decl_stmt|;
name|chooser
operator|.
name|setCurrentDirectory
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
name|ExtensionFileFilter
name|pdfFilter
init|=
operator|new
name|ExtensionFileFilter
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"pdf"
block|,
literal|"PDF"
block|}
argument_list|,
literal|"PDF Files"
argument_list|)
decl_stmt|;
name|chooser
operator|.
name|setFileFilter
argument_list|(
name|pdfFilter
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|chooser
operator|.
name|showOpenDialog
argument_list|(
name|PDFDebugger
operator|.
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|String
name|name
init|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|currentDir
operator|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
try|try
block|{
name|readPDFFile
argument_list|(
name|name
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
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
comment|//GEN-LAST:event_openMenuItemActionPerformed
specifier|private
name|void
name|jTree1ValueChanged
parameter_list|(
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeSelectionEvent
name|evt
parameter_list|)
block|{
name|TreePath
name|path
init|=
name|jTree1
operator|.
name|getSelectionPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Object
name|selectedNode
init|=
name|path
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
name|String
name|data
init|=
name|convertToString
argument_list|(
name|selectedNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|jTextPane1
operator|.
name|setText
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jTextPane1
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
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
comment|//GEN-LAST:event_jTree1ValueChanged
specifier|private
name|String
name|convertToString
parameter_list|(
name|Object
name|selectedNode
parameter_list|)
block|{
name|String
name|data
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSBoolean
condition|)
block|{
name|data
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSBoolean
operator|)
name|selectedNode
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSFloat
condition|)
block|{
name|data
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSFloat
operator|)
name|selectedNode
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSNull
condition|)
block|{
name|data
operator|=
literal|"null"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSInteger
condition|)
block|{
name|data
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSInteger
operator|)
name|selectedNode
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSName
condition|)
block|{
name|data
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSName
operator|)
name|selectedNode
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSString
condition|)
block|{
name|data
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSString
operator|)
name|selectedNode
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|COSStream
condition|)
block|{
try|try
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|selectedNode
decl_stmt|;
name|InputStream
name|ioStream
init|=
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|byteArray
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|ioStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|byteArray
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|data
operator|=
name|byteArray
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|MapEntry
condition|)
block|{
name|data
operator|=
name|convertToString
argument_list|(
operator|(
operator|(
name|MapEntry
operator|)
name|selectedNode
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|selectedNode
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|data
operator|=
name|convertToString
argument_list|(
operator|(
operator|(
name|ArrayEntry
operator|)
name|selectedNode
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
specifier|private
name|void
name|exitMenuItemActionPerformed
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
name|evt
parameter_list|)
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Exit the Application.      */
specifier|private
name|void
name|exitForm
parameter_list|(
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|WindowEvent
name|evt
parameter_list|)
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param args the command line arguments      *      * @throws Exception If anything goes wrong.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|UIManager
operator|.
name|setLookAndFeel
argument_list|(
name|UIManager
operator|.
name|getSystemLookAndFeelClassName
argument_list|()
argument_list|)
expr_stmt|;
name|PDFDebugger
name|viewer
init|=
operator|new
name|PDFDebugger
argument_list|()
decl_stmt|;
name|String
name|filename
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|""
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|PASSWORD
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|NONSEQ
argument_list|)
condition|)
block|{
name|useNonSeqParser
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|filename
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
if|if
condition|(
name|filename
operator|!=
literal|null
condition|)
block|{
name|viewer
operator|.
name|readPDFFile
argument_list|(
name|filename
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
name|viewer
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readPDFFile
parameter_list|(
name|String
name|file
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|parseDocument
argument_list|(
name|f
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|TreeModel
name|model
init|=
operator|new
name|PDFTreeModel
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|jTree1
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"PDFBox - "
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will parse a document.      *      * @param file The file addressing the document.      *      * @throws IOException If there is an error parsing the document.      */
specifier|private
name|void
name|parseDocument
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|useNonSeqParser
condition|)
block|{
name|document
operator|=
name|PDDocument
operator|.
name|loadNonSeq
argument_list|(
name|file
argument_list|,
literal|null
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
try|try
block|{
name|StandardDecryptionMaterial
name|sdm
init|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|document
operator|.
name|openProtection
argument_list|(
name|sdm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The document is encrypted."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will print out a message telling how to use this utility.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: java -jar pdfbox-app-x.y.z.jar PDFDebugger [OPTIONS]<input-file>\n"
operator|+
literal|"  -password<password>      Password to decrypt the document\n"
operator|+
literal|"  -nonSeq                   Enables the new non-sequential parser\n"
operator|+
literal|"<input-file>              The PDF document to be loaded\n"
argument_list|)
expr_stmt|;
block|}
comment|// Variables declaration - do not modify//GEN-BEGIN:variables
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|aboutMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|contentsMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|copyMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|cutMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|deleteMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenu
name|editMenu
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|exitMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenu
name|fileMenu
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenu
name|helpMenu
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
name|jScrollPane1
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
name|jScrollPane2
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JSplitPane
name|jSplitPane1
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JTextPane
name|jTextPane1
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JTree
name|jTree1
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuBar
name|menuBar
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|openMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|pasteMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|saveAsMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|saveMenuItem
decl_stmt|;
specifier|private
name|JPanel
name|documentPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
comment|// End of variables declaration//GEN-END:variables
block|}
end_class

end_unit

