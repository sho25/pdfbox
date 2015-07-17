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
name|JComboBox
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
name|RandomAccessBuffer
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
name|streampane
operator|.
name|tooltip
operator|.
name|ToolTipController
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that shows the COSStream.  */
end_comment

begin_class
specifier|public
class|class
name|StreamPane
implements|implements
name|ActionListener
block|{
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
comment|/**      * Constructor.      * @param cosStream COSStream instance.      * @param streamKey COSName instance. This is the type .      * @param resourcesDic COSDictionary instance that holds the resource dictionary for the stream.      */
specifier|public
name|StreamPane
parameter_list|(
name|COSStream
name|cosStream
parameter_list|,
name|COSName
name|streamKey
parameter_list|,
name|COSDictionary
name|resourcesDic
parameter_list|)
block|{
name|isContentStream
operator|=
name|COSName
operator|.
name|CONTENTS
operator|.
name|equals
argument_list|(
name|streamKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|stream
operator|=
operator|new
name|Stream
argument_list|(
name|cosStream
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
if|if
condition|(
name|stream
operator|.
name|isImage
argument_list|()
condition|)
block|{
name|view
operator|=
operator|new
name|StreamPaneView
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
expr_stmt|;
name|requestImageShowing
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|view
operator|=
operator|new
name|StreamPaneView
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
expr_stmt|;
name|requestStreamText
argument_list|(
name|Stream
operator|.
name|UNFILTERED
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|JPanel
name|getPanel
parameter_list|()
block|{
return|return
name|view
operator|.
name|getStreamPanel
argument_list|()
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
block|}
specifier|private
name|void
name|requestImageShowing
parameter_list|()
block|{
if|if
condition|(
name|stream
operator|.
name|isImage
argument_list|()
operator|&&
name|resources
operator|!=
literal|null
condition|)
block|{
name|view
operator|.
name|showStreamImage
argument_list|(
name|stream
operator|.
name|getImage
argument_list|(
name|resources
argument_list|)
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
name|String
name|data
init|=
name|getStringOfStream
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|StyledDocument
name|document
init|=
operator|new
name|DefaultStyledDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|document
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
return|return
name|document
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
operator|new
name|RandomAccessBuffer
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
try|try
block|{
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
if|if
condition|(
name|obj
operator|instanceof
name|Operator
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
operator|(
operator|(
name|Operator
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
operator|+
literal|"\n"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|str
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSName
condition|)
block|{
name|str
operator|=
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
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"[ "
argument_list|)
decl_stmt|;
for|for
control|(
name|COSBase
name|base
range|:
operator|(
name|COSArray
operator|)
name|obj
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|getCOSVlaue
argument_list|(
name|base
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|(
name|COSArray
operator|)
name|obj
operator|)
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|lastIndexOf
argument_list|(
literal|","
argument_list|)
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
name|str
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|str
operator|=
name|getCOSVlaue
argument_list|(
name|obj
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
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|docu
return|;
block|}
specifier|private
name|String
name|getCOSVlaue
parameter_list|(
name|Object
name|obj
parameter_list|)
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
if|if
condition|(
name|obj
operator|instanceof
name|COSString
condition|)
block|{
name|str
operator|=
literal|"("
operator|+
name|str
operator|+
literal|")"
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
block|}
block|}
end_class

end_unit

