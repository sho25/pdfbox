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
name|exceptions
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
name|pdfviewer
operator|.
name|PageWrapper
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
name|pdfviewer
operator|.
name|ReaderBottomPanel
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
name|util
operator|.
name|ExtensionFileFilter
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
name|util
operator|.
name|ImageIOUtil
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
name|KeyStroke
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
name|awt
operator|.
name|print
operator|.
name|PrinterException
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
name|IOException
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

begin_comment
comment|/**  * An application to read PDF documents.  This will provide Acrobat Reader like  * funtionality.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFReader
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
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|saveAsImageMenuItem
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
name|printMenuItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenu
name|viewMenu
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|nextPageItem
decl_stmt|;
specifier|private
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
name|previousPageItem
decl_stmt|;
specifier|private
name|JPanel
name|documentPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
specifier|private
name|ReaderBottomPanel
name|bottomStatusPanel
init|=
operator|new
name|ReaderBottomPanel
argument_list|()
decl_stmt|;
specifier|private
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PDPage
argument_list|>
name|pages
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|currentPage
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|numberOfPages
init|=
literal|0
decl_stmt|;
specifier|private
name|String
name|currentFilename
init|=
literal|null
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
specifier|final
name|String
name|NONSEQ
init|=
literal|"-nonSeq"
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
name|PDFReader
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
name|saveAsImageMenuItem
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
name|printMenuItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|viewMenu
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenu
argument_list|()
expr_stmt|;
name|nextPageItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|previousPageItem
operator|=
operator|new
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
argument_list|()
expr_stmt|;
name|setTitle
argument_list|(
literal|"PDFBox - PDF Reader"
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
name|exitApplication
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|JScrollPane
name|documentScroller
init|=
operator|new
name|JScrollPane
argument_list|()
decl_stmt|;
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
name|documentScroller
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
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|bottomStatusPanel
argument_list|,
name|java
operator|.
name|awt
operator|.
name|BorderLayout
operator|.
name|SOUTH
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
name|printMenuItem
operator|.
name|setText
argument_list|(
literal|"Print"
argument_list|)
expr_stmt|;
name|printMenuItem
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
try|try
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
name|print
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PrinterException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|printMenuItem
argument_list|)
expr_stmt|;
name|saveAsImageMenuItem
operator|.
name|setText
argument_list|(
literal|"Save as image"
argument_list|)
expr_stmt|;
name|saveAsImageMenuItem
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
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|saveImage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|fileMenu
operator|.
name|add
argument_list|(
name|saveAsImageMenuItem
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
name|exitApplication
argument_list|()
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
name|viewMenu
operator|.
name|setText
argument_list|(
literal|"View"
argument_list|)
expr_stmt|;
name|nextPageItem
operator|.
name|setText
argument_list|(
literal|"Next page"
argument_list|)
expr_stmt|;
name|nextPageItem
operator|.
name|setAccelerator
argument_list|(
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
literal|'+'
argument_list|)
argument_list|)
expr_stmt|;
name|nextPageItem
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
name|nextPage
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|nextPageItem
argument_list|)
expr_stmt|;
name|previousPageItem
operator|.
name|setText
argument_list|(
literal|"Previous page"
argument_list|)
expr_stmt|;
name|previousPageItem
operator|.
name|setAccelerator
argument_list|(
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
literal|'-'
argument_list|)
argument_list|)
expr_stmt|;
name|previousPageItem
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
name|previousPage
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|previousPageItem
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|add
argument_list|(
name|viewMenu
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
specifier|private
name|void
name|updateTitle
parameter_list|()
block|{
name|setTitle
argument_list|(
literal|"PDFBox - "
operator|+
name|currentFilename
operator|+
literal|" ("
operator|+
operator|(
name|currentPage
operator|+
literal|1
operator|)
operator|+
literal|"/"
operator|+
name|numberOfPages
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|nextPage
parameter_list|()
block|{
if|if
condition|(
name|currentPage
operator|<
name|numberOfPages
operator|-
literal|1
condition|)
block|{
name|currentPage
operator|++
expr_stmt|;
name|updateTitle
argument_list|()
expr_stmt|;
name|showPage
argument_list|(
name|currentPage
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|previousPage
parameter_list|()
block|{
if|if
condition|(
name|currentPage
operator|>
literal|0
condition|)
block|{
name|currentPage
operator|--
expr_stmt|;
name|updateTitle
argument_list|()
expr_stmt|;
name|showPage
argument_list|(
name|currentPage
argument_list|)
expr_stmt|;
block|}
block|}
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
name|PDFReader
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
name|openPDFFile
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|exitApplication
parameter_list|()
block|{
try|try
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
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
comment|//do nothing because we are closing the application
block|}
name|this
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|dispose
argument_list|()
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
name|PDFReader
name|viewer
init|=
operator|new
name|PDFReader
argument_list|()
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|filename
init|=
literal|null
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
name|viewer
operator|.
name|openPDFFile
argument_list|(
name|filename
argument_list|,
name|password
argument_list|)
expr_stmt|;
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
name|openPDFFile
parameter_list|(
name|String
name|filename
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
name|documentPanel
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|parseDocument
argument_list|(
name|file
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|pages
operator|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
expr_stmt|;
name|numberOfPages
operator|=
name|pages
operator|.
name|size
argument_list|()
expr_stmt|;
name|currentFilename
operator|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
name|currentPage
operator|=
literal|0
expr_stmt|;
name|updateTitle
argument_list|()
expr_stmt|;
name|showPage
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|showPage
parameter_list|(
name|int
name|pageNumber
parameter_list|)
block|{
try|try
block|{
name|PageWrapper
name|wrapper
init|=
operator|new
name|PageWrapper
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|displayPage
argument_list|(
operator|(
name|PDPage
operator|)
name|pages
operator|.
name|get
argument_list|(
name|pageNumber
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|documentPanel
operator|.
name|getComponentCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|documentPanel
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|documentPanel
operator|.
name|add
argument_list|(
name|wrapper
operator|.
name|getPanel
argument_list|()
argument_list|)
expr_stmt|;
name|pack
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|saveImage
parameter_list|()
block|{
try|try
block|{
name|PDPage
name|pageToSave
init|=
operator|(
name|PDPage
operator|)
name|pages
operator|.
name|get
argument_list|(
name|currentPage
argument_list|)
decl_stmt|;
name|BufferedImage
name|pageAsImage
init|=
name|pageToSave
operator|.
name|convertToImage
argument_list|()
decl_stmt|;
name|String
name|imageFilename
init|=
name|currentFilename
decl_stmt|;
if|if
condition|(
name|imageFilename
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
condition|)
block|{
name|imageFilename
operator|=
name|imageFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|imageFilename
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|imageFilename
operator|+=
literal|"_"
operator|+
operator|(
name|currentPage
operator|+
literal|1
operator|)
expr_stmt|;
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|pageAsImage
argument_list|,
literal|"png"
argument_list|,
name|imageFilename
argument_list|,
name|BufferedImage
operator|.
name|TYPE_USHORT_565_RGB
argument_list|,
literal|300
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will parse a document.      *      * @param input The input stream for the document.      *      * @throws IOException If there is an error parsing the document.      */
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
name|document
operator|=
literal|null
expr_stmt|;
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
name|document
operator|.
name|decrypt
argument_list|(
name|password
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
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|CryptographyException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Get the bottom status panel.      *      * @return The bottom status panel.      */
specifier|public
name|ReaderBottomPanel
name|getBottomStatusPanel
parameter_list|()
block|{
return|return
name|bottomStatusPanel
return|;
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
literal|"usage: java -jar pdfbox-app-x.y.z.jar PDFReader [OPTIONS]<input-file>\n"
operator|+
literal|"  -password<password>      Password to decrypt the document\n"
operator|+
literal|"  -nonSeq                   Enables the new non-sequential parser\n"
operator|+
literal|"<input-file>              The PDF document to be loaded\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

