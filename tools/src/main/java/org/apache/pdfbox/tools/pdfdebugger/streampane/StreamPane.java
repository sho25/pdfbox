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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
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
name|ByteArrayOutputStream
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
name|UnsupportedEncodingException
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
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|JComboBox
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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTabbedPane
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
name|text
operator|.
name|BadLocationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|DefaultStyledDocument
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Style
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyleConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyleContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyledDocument
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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
name|COSArray
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
name|COSBase
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
name|COSNumber
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|io
operator|.
name|IOUtils
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|PDResources
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
name|hexviewer
operator|.
name|HexView
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
name|streampane
operator|.
name|tooltip
operator|.
name|ToolTipController
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
name|Charsets
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A class that shows the COSStream.  */
end_comment

begin_class
specifier|public
class|class
name|StreamPane
implements|implements
name|ActionListener
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_TEXT_OBJECT
init|=
literal|"BT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|END_TEXT_OBJECT
init|=
literal|"ET"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAVE_GRAPHICS_STATE
init|=
literal|"q"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESTORE_GRAPHICS_STATE
init|=
literal|"Q"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INLINE_IMAGE_BEGIN
init|=
literal|"BI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMAGE_DATA
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INLINE_IMAGE_END
init|=
literal|"EI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_MARKED_CONTENT1
init|=
literal|"BMC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_MARKED_CONTENT2
init|=
literal|"BDC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|END_MARKED_CONTENT
init|=
literal|"EMC"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|StyleContext
name|CONTEXT
init|=
name|StyleContext
operator|.
name|getDefaultStyleContext
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|OPERATOR_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"operator"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|NUMBER_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"number"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|STRING_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"string"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|ESCAPE_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"escape"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|NAME_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"name"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Style
name|INLINE_IMAGE_STYLE
init|=
name|CONTEXT
operator|.
name|addStyle
argument_list|(
literal|"inline_image"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
static|static
block|{
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|OPERATOR_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|25
argument_list|,
literal|55
argument_list|,
literal|156
argument_list|)
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|NUMBER_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|51
argument_list|,
literal|86
argument_list|,
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|STRING_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|128
argument_list|,
literal|35
argument_list|,
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|ESCAPE_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|179
argument_list|,
literal|49
argument_list|,
literal|36
argument_list|)
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|NAME_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|140
argument_list|,
literal|38
argument_list|,
literal|145
argument_list|)
argument_list|)
expr_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|INLINE_IMAGE_STYLE
argument_list|,
operator|new
name|Color
argument_list|(
literal|116
argument_list|,
literal|113
argument_list|,
literal|39
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|JPanel
name|panel
decl_stmt|;
specifier|private
specifier|final
name|HexView
name|hexView
decl_stmt|;
specifier|private
specifier|final
name|JTabbedPane
name|tabbedPane
decl_stmt|;
specifier|private
specifier|final
name|StreamPaneView
name|view
decl_stmt|;
specifier|private
specifier|final
name|Stream
name|stream
decl_stmt|;
specifier|private
name|ToolTipController
name|tTController
decl_stmt|;
specifier|private
name|PDResources
name|resources
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isContentStream
decl_stmt|;
comment|/**      * Constructor.      *      * @param cosStream COSStream instance.      * @param isContentStream boolean instance. This says if a stream is content stream or not.      * @param isThumb This says if a stream is an thumbnail image or not.      * @param resourcesDic COSDictionary instance that holds the resource dictionary for the stream.      * @throws IOException if there is an I/O error during internal data transfer.      */
specifier|public
name|StreamPane
parameter_list|(
name|COSStream
name|cosStream
parameter_list|,
name|boolean
name|isContentStream
parameter_list|,
name|boolean
name|isThumb
parameter_list|,
name|COSDictionary
name|resourcesDic
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|isContentStream
operator|=
name|isContentStream
expr_stmt|;
name|this
operator|.
name|stream
operator|=
operator|new
name|Stream
argument_list|(
name|cosStream
argument_list|,
name|isThumb
argument_list|)
expr_stmt|;
if|if
condition|(
name|resourcesDic
operator|!=
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|(
name|resourcesDic
argument_list|)
expr_stmt|;
name|tTController
operator|=
operator|new
name|ToolTipController
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
name|panel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|panel
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|500
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
name|Y_AXIS
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|StreamPaneView
argument_list|()
expr_stmt|;
name|hexView
operator|=
operator|new
name|HexView
argument_list|()
expr_stmt|;
if|if
condition|(
name|stream
operator|.
name|isImage
argument_list|()
condition|)
block|{
name|panel
operator|.
name|add
argument_list|(
name|createHeaderPanel
argument_list|(
name|stream
operator|.
name|getFilterList
argument_list|()
argument_list|,
name|Stream
operator|.
name|IMAGE
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|requestImageShowing
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|panel
operator|.
name|add
argument_list|(
name|createHeaderPanel
argument_list|(
name|stream
operator|.
name|getFilterList
argument_list|()
argument_list|,
name|Stream
operator|.
name|UNFILTERED
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|requestStreamText
argument_list|(
name|Stream
operator|.
name|UNFILTERED
argument_list|)
expr_stmt|;
block|}
name|tabbedPane
operator|=
operator|new
name|JTabbedPane
argument_list|()
expr_stmt|;
name|tabbedPane
operator|.
name|add
argument_list|(
literal|"Text view"
argument_list|,
name|view
operator|.
name|getStreamPanel
argument_list|()
argument_list|)
expr_stmt|;
name|tabbedPane
operator|.
name|add
argument_list|(
literal|"Hex view"
argument_list|,
name|hexView
operator|.
name|getPane
argument_list|()
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|tabbedPane
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JComponent
name|getPanel
parameter_list|()
block|{
return|return
name|panel
return|;
block|}
specifier|private
name|JPanel
name|createHeaderPanel
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|availableFilters
parameter_list|,
name|String
name|i
parameter_list|,
name|ActionListener
name|actionListener
parameter_list|)
block|{
name|JComboBox
name|filters
init|=
operator|new
name|JComboBox
argument_list|(
name|availableFilters
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
name|filters
operator|.
name|setSelectedItem
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|filters
operator|.
name|addActionListener
argument_list|(
name|actionListener
argument_list|)
expr_stmt|;
name|JPanel
name|headerPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|()
argument_list|)
decl_stmt|;
name|headerPanel
operator|.
name|add
argument_list|(
name|filters
argument_list|)
expr_stmt|;
return|return
name|headerPanel
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
if|if
condition|(
name|actionEvent
operator|.
name|getActionCommand
argument_list|()
operator|.
name|equals
argument_list|(
literal|"comboBoxChanged"
argument_list|)
condition|)
block|{
name|JComboBox
name|comboBox
init|=
operator|(
name|JComboBox
operator|)
name|actionEvent
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|String
name|currentFilter
init|=
operator|(
name|String
operator|)
name|comboBox
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|currentFilter
operator|.
name|equals
argument_list|(
name|Stream
operator|.
name|IMAGE
argument_list|)
condition|)
block|{
name|requestImageShowing
argument_list|()
expr_stmt|;
return|return;
block|}
name|requestStreamText
argument_list|(
name|currentFilter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
name|requestImageShowing
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|stream
operator|.
name|isImage
argument_list|()
condition|)
block|{
name|BufferedImage
name|image
decl_stmt|;
synchronized|synchronized
init|(
name|stream
init|)
block|{
name|image
operator|=
name|stream
operator|.
name|getImage
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|showStreamImage
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ImageIO
operator|.
name|write
argument_list|(
name|image
argument_list|,
literal|"jpg"
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|baos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|baos
operator|.
name|close
argument_list|()
expr_stmt|;
name|hexView
operator|.
name|changeData
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|requestStreamText
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|IOException
block|{
operator|new
name|DocumentCreator
argument_list|(
name|command
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|stream
init|)
block|{
name|hexView
operator|.
name|changeData
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
operator|.
name|getStream
argument_list|(
name|command
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A SwingWorker extended class that convert the stream to text loads in a document.      */
specifier|private
specifier|final
class|class
name|DocumentCreator
extends|extends
name|SwingWorker
argument_list|<
name|StyledDocument
argument_list|,
name|Integer
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|filterKey
decl_stmt|;
specifier|private
name|int
name|indent
decl_stmt|;
specifier|private
name|boolean
name|needIndent
decl_stmt|;
specifier|private
name|DocumentCreator
parameter_list|(
name|String
name|filterKey
parameter_list|)
block|{
name|this
operator|.
name|filterKey
operator|=
name|filterKey
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|StyledDocument
name|doInBackground
parameter_list|()
block|{
synchronized|synchronized
init|(
name|stream
init|)
block|{
name|InputStream
name|inputStream
init|=
name|stream
operator|.
name|getStream
argument_list|(
name|filterKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|isContentStream
operator|&&
name|Stream
operator|.
name|UNFILTERED
operator|.
name|equals
argument_list|(
name|filterKey
argument_list|)
condition|)
block|{
name|StyledDocument
name|document
init|=
name|getContentStreamDocument
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
return|return
name|document
return|;
block|}
return|return
name|getDocument
argument_list|(
name|stream
operator|.
name|getStream
argument_list|(
name|filterKey
argument_list|)
argument_list|)
return|;
block|}
return|return
name|getDocument
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
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
name|view
operator|.
name|showStreamText
argument_list|(
name|get
argument_list|()
argument_list|,
name|tTController
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
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
specifier|private
name|String
name|getStringOfStream
parameter_list|(
name|InputStream
name|ioStream
parameter_list|)
block|{
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
decl_stmt|;
try|try
block|{
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
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
name|byteArray
operator|.
name|toString
argument_list|(
literal|"ISO-8859-1"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|StyledDocument
name|getDocument
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|StyledDocument
name|docu
init|=
operator|new
name|DefaultStyledDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputStream
operator|!=
literal|null
condition|)
block|{
name|String
name|data
init|=
name|getStringOfStream
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
try|try
block|{
name|docu
operator|.
name|insertString
argument_list|(
literal|0
argument_list|,
name|data
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
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
return|return
name|docu
return|;
block|}
specifier|private
name|StyledDocument
name|getContentStreamDocument
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|StyledDocument
name|docu
init|=
operator|new
name|DefaultStyledDocument
argument_list|()
decl_stmt|;
name|PDFStreamParser
name|parser
decl_stmt|;
try|try
block|{
name|parser
operator|=
operator|new
name|PDFStreamParser
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|Object
name|obj
range|:
name|parser
operator|.
name|getTokens
argument_list|()
control|)
block|{
name|writeToken
argument_list|(
name|obj
argument_list|,
name|docu
argument_list|)
expr_stmt|;
block|}
return|return
name|docu
return|;
block|}
specifier|private
name|void
name|writeToken
parameter_list|(
name|Object
name|obj
parameter_list|,
name|StyledDocument
name|docu
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Operator
condition|)
block|{
name|addOperators
argument_list|(
name|obj
argument_list|,
name|docu
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeOperand
argument_list|(
name|obj
argument_list|,
name|docu
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BadLocationException
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
specifier|private
name|void
name|writeOperand
parameter_list|(
name|Object
name|obj
parameter_list|,
name|StyledDocument
name|docu
parameter_list|)
throws|throws
name|BadLocationException
block|{
name|writeIndent
argument_list|(
name|docu
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSName
condition|)
block|{
name|String
name|str
init|=
literal|"/"
operator|+
operator|(
operator|(
name|COSName
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
operator|+
literal|" "
argument_list|,
name|NAME_STYLE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSBoolean
condition|)
block|{
name|String
name|str
init|=
name|obj
operator|.
name|toString
argument_list|()
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
operator|+
literal|" "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSArray
condition|)
block|{
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"[ "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|COSBase
name|elem
range|:
operator|(
name|COSArray
operator|)
name|obj
control|)
block|{
name|writeOperand
argument_list|(
name|elem
argument_list|,
name|docu
argument_list|)
expr_stmt|;
block|}
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"] "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSString
condition|)
block|{
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"("
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|(
operator|(
name|COSString
operator|)
name|obj
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|int
name|chr
init|=
name|b
operator|&
literal|0xff
decl_stmt|;
if|if
condition|(
name|chr
argument_list|<
literal|0x20
operator|||
name|chr
argument_list|>
literal|0x7e
condition|)
block|{
comment|// non-printable ASCII is shown as an octal escape
name|String
name|str
init|=
name|String
operator|.
name|format
argument_list|(
literal|"\\%03o"
argument_list|,
name|chr
argument_list|)
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
argument_list|,
name|ESCAPE_STYLE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|chr
operator|==
literal|'('
operator|||
name|chr
operator|==
literal|')'
operator|||
name|chr
operator|==
literal|'\n'
operator|||
name|chr
operator|==
literal|'\r'
operator|||
name|chr
operator|==
literal|'\t'
operator|||
name|chr
operator|==
literal|'\b'
operator|||
name|chr
operator|==
literal|'\f'
operator|||
name|chr
operator|==
literal|'\\'
condition|)
block|{
comment|// PDF reserved characters must be escaped
name|String
name|str
init|=
literal|"\\"
operator|+
operator|(
name|char
operator|)
name|chr
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
argument_list|,
name|ESCAPE_STYLE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|str
init|=
literal|""
operator|+
operator|(
name|char
operator|)
name|chr
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
argument_list|,
name|STRING_STYLE
argument_list|)
expr_stmt|;
block|}
block|}
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|") "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSNumber
condition|)
block|{
name|String
name|str
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSFloat
condition|)
block|{
name|str
operator|=
name|Float
operator|.
name|toString
argument_list|(
operator|(
operator|(
name|COSFloat
operator|)
name|obj
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|str
operator|=
name|Integer
operator|.
name|toString
argument_list|(
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
operator|+
literal|" "
argument_list|,
name|NUMBER_STYLE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSDictionary
condition|)
block|{
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"<< "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|obj
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|dict
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|writeOperand
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|docu
argument_list|)
expr_stmt|;
name|writeOperand
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|docu
argument_list|)
expr_stmt|;
block|}
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|">> "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|str
init|=
name|obj
operator|.
name|toString
argument_list|()
decl_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
name|str
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
operator|+
literal|1
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|str
operator|+
literal|" "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addOperators
parameter_list|(
name|Object
name|obj
parameter_list|,
name|StyledDocument
name|docu
parameter_list|)
throws|throws
name|BadLocationException
block|{
name|Operator
name|op
init|=
operator|(
name|Operator
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|END_TEXT_OBJECT
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|RESTORE_GRAPHICS_STATE
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|END_MARKED_CONTENT
argument_list|)
condition|)
block|{
name|indent
operator|--
expr_stmt|;
block|}
name|writeIndent
argument_list|(
name|docu
argument_list|)
expr_stmt|;
if|if
condition|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|INLINE_IMAGE_BEGIN
argument_list|)
condition|)
block|{
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|INLINE_IMAGE_BEGIN
operator|+
literal|"\n"
argument_list|,
name|OPERATOR_STYLE
argument_list|)
expr_stmt|;
name|COSDictionary
name|dic
init|=
name|op
operator|.
name|getImageParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|key
range|:
name|dic
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"/"
operator|+
name|key
operator|.
name|getName
argument_list|()
operator|+
literal|" "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|writeToken
argument_list|(
name|value
argument_list|,
name|docu
argument_list|)
expr_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"\n"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
name|imageString
init|=
operator|new
name|String
argument_list|(
name|op
operator|.
name|getImageData
argument_list|()
argument_list|,
name|Charsets
operator|.
name|ISO_8859_1
argument_list|)
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|IMAGE_DATA
operator|+
literal|"\n"
argument_list|,
name|INLINE_IMAGE_STYLE
argument_list|)
expr_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|imageString
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"\n"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|INLINE_IMAGE_END
operator|+
literal|"\n"
argument_list|,
name|OPERATOR_STYLE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|operator
init|=
operator|(
operator|(
name|Operator
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
name|operator
operator|+
literal|"\n"
argument_list|,
name|OPERATOR_STYLE
argument_list|)
expr_stmt|;
comment|// nested opening operators
if|if
condition|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|BEGIN_TEXT_OBJECT
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|SAVE_GRAPHICS_STATE
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|BEGIN_MARKED_CONTENT1
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|BEGIN_MARKED_CONTENT2
argument_list|)
condition|)
block|{
name|indent
operator|++
expr_stmt|;
block|}
block|}
name|needIndent
operator|=
literal|true
expr_stmt|;
block|}
name|void
name|writeIndent
parameter_list|(
name|StyledDocument
name|docu
parameter_list|)
throws|throws
name|BadLocationException
block|{
if|if
condition|(
name|needIndent
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indent
condition|;
name|i
operator|++
control|)
block|{
name|docu
operator|.
name|insertString
argument_list|(
name|docu
operator|.
name|getLength
argument_list|()
argument_list|,
literal|"  "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|needIndent
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

