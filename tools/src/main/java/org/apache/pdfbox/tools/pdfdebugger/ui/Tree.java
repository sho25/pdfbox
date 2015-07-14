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
name|datatransfer
operator|.
name|Clipboard
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|StringSelection
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
name|event
operator|.
name|MouseEvent
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
name|util
operator|.
name|ArrayList
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
name|JMenuItem
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPopupMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTree
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
name|pdmodel
operator|.
name|common
operator|.
name|PDStream
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
name|tools
operator|.
name|pdfdebugger
operator|.
name|treestatus
operator|.
name|TreeStatus
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
name|FileOpenSaveDialog
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A customized tree for PDFDebugger.  */
end_comment

begin_class
specifier|public
class|class
name|Tree
extends|extends
name|JTree
block|{
specifier|private
specifier|final
name|JPopupMenu
name|treePopupMenu
decl_stmt|;
specifier|private
specifier|final
name|Component
name|parent
decl_stmt|;
specifier|private
specifier|final
name|Object
name|rootNode
decl_stmt|;
comment|/**      * Constructor.      * @param parentComponent the main UI where the Tree resides.      */
specifier|public
name|Tree
parameter_list|(
name|Component
name|parentComponent
parameter_list|)
block|{
name|treePopupMenu
operator|=
operator|new
name|JPopupMenu
argument_list|()
expr_stmt|;
name|setComponentPopupMenu
argument_list|(
name|treePopupMenu
argument_list|)
expr_stmt|;
name|parent
operator|=
name|parentComponent
expr_stmt|;
name|rootNode
operator|=
name|this
operator|.
name|getModel
argument_list|()
operator|.
name|getRoot
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Point
name|getPopupLocation
parameter_list|(
name|MouseEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
name|TreePath
name|path
init|=
name|getClosestPathForLocation
argument_list|(
name|event
operator|.
name|getX
argument_list|()
argument_list|,
name|event
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|setSelectionPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|treePopupMenu
operator|.
name|removeAll
argument_list|()
expr_stmt|;
for|for
control|(
name|JMenuItem
name|menuItem
range|:
name|getPopupMenuItems
argument_list|(
name|path
argument_list|)
control|)
block|{
name|treePopupMenu
operator|.
name|add
argument_list|(
name|menuItem
argument_list|)
expr_stmt|;
block|}
return|return
name|event
operator|.
name|getPoint
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Produce the popup menu items depending on the node of a certain TreePath.      * @param nodePath is instance of TreePath of the specified Node.      * @return the JMenuItem list for the node      */
specifier|private
name|List
argument_list|<
name|JMenuItem
argument_list|>
name|getPopupMenuItems
parameter_list|(
name|TreePath
name|nodePath
parameter_list|)
block|{
name|Object
name|obj
init|=
name|nodePath
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|JMenuItem
argument_list|>
name|menuItems
init|=
operator|new
name|ArrayList
argument_list|<
name|JMenuItem
argument_list|>
argument_list|()
decl_stmt|;
name|menuItems
operator|.
name|add
argument_list|(
name|getTreePathMenuItem
argument_list|(
name|nodePath
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|MapEntry
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|MapEntry
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|ArrayEntry
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|obj
decl_stmt|;
name|menuItems
operator|.
name|add
argument_list|(
name|getUnFilteredStreamSaveMenu
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|stream
operator|.
name|getFilters
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|stream
operator|.
name|getFilters
argument_list|()
operator|instanceof
name|COSArray
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|stream
operator|.
name|getFilters
argument_list|()
operator|)
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
for|for
control|(
name|JMenuItem
name|menuItem
range|:
name|getPartiallyFilteredStreamSaveMenu
argument_list|(
name|stream
argument_list|)
control|)
block|{
name|menuItems
operator|.
name|add
argument_list|(
name|menuItem
argument_list|)
expr_stmt|;
block|}
block|}
name|menuItems
operator|.
name|add
argument_list|(
name|getFilteredStreamSaveMenu
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|menuItems
return|;
block|}
comment|/**      * Produce the JMenuItem that gives way to copy tree path string to clipboard      * @param path the TreePath instance      * @return Menu Item      */
specifier|private
name|JMenuItem
name|getTreePathMenuItem
parameter_list|(
specifier|final
name|TreePath
name|path
parameter_list|)
block|{
name|JMenuItem
name|copyPathMenuItem
init|=
operator|new
name|JMenuItem
argument_list|(
literal|"Copy Tree Path"
argument_list|)
decl_stmt|;
name|copyPathMenuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
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
name|Clipboard
name|clipboard
init|=
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getSystemClipboard
argument_list|()
decl_stmt|;
name|clipboard
operator|.
name|setContents
argument_list|(
operator|new
name|StringSelection
argument_list|(
operator|new
name|TreeStatus
argument_list|(
name|rootNode
argument_list|)
operator|.
name|getStringForPath
argument_list|(
name|path
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|copyPathMenuItem
return|;
block|}
comment|/**      * Produce JMenuItem that saves filtered stream      * @param cosStream stream to save      * @return JMenuItem for saving filtered stream      */
specifier|private
name|JMenuItem
name|getFilteredStreamSaveMenu
parameter_list|(
specifier|final
name|COSStream
name|cosStream
parameter_list|)
block|{
name|JMenuItem
name|saveMenuItem
init|=
operator|new
name|JMenuItem
argument_list|(
literal|"Save Filtered Stream ("
operator|+
name|getFilters
argument_list|(
name|cosStream
argument_list|)
operator|+
literal|")..."
argument_list|)
decl_stmt|;
name|saveMenuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
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
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|cosStream
operator|.
name|getFilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|saveStream
argument_list|(
name|bytes
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
argument_list|)
expr_stmt|;
return|return
name|saveMenuItem
return|;
block|}
comment|/**      * Returns the filters used by the given stream.      */
specifier|private
name|String
name|getFilters
parameter_list|(
name|COSStream
name|cosStream
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|COSBase
name|filters
init|=
name|cosStream
operator|.
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|filters
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|filterArray
init|=
operator|(
name|COSArray
operator|)
name|filters
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
name|filterArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|filterArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Produce JMenuItem that saves unfiltered stream      * @param cosStream stream to save      * @return JMenuItem for saving unfiltered stream      */
specifier|private
name|JMenuItem
name|getUnFilteredStreamSaveMenu
parameter_list|(
specifier|final
name|COSStream
name|cosStream
parameter_list|)
block|{
name|JMenuItem
name|saveMenuItem
init|=
operator|new
name|JMenuItem
argument_list|(
literal|"Save Unfiltered Stream..."
argument_list|)
decl_stmt|;
name|saveMenuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
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
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|cosStream
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|saveStream
argument_list|(
name|bytes
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
argument_list|)
expr_stmt|;
return|return
name|saveMenuItem
return|;
block|}
comment|/**      * produce possible partially filtered stream saving menu items      * @param cosStream stream to save      * @return JMenuItems for saving partially filtered streams      */
specifier|private
name|List
argument_list|<
name|JMenuItem
argument_list|>
name|getPartiallyFilteredStreamSaveMenu
parameter_list|(
specifier|final
name|COSStream
name|cosStream
parameter_list|)
block|{
name|List
argument_list|<
name|JMenuItem
argument_list|>
name|menuItems
init|=
operator|new
name|ArrayList
argument_list|<
name|JMenuItem
argument_list|>
argument_list|()
decl_stmt|;
name|PDStream
name|stream
init|=
operator|new
name|PDStream
argument_list|(
name|cosStream
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|COSName
argument_list|>
name|filters
init|=
name|stream
operator|.
name|getFilters
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|filters
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|1
condition|;
name|i
operator|--
control|)
block|{
name|menuItems
operator|.
name|add
argument_list|(
name|getPartialStreamSavingMenuItem
argument_list|(
name|i
argument_list|,
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|menuItems
return|;
block|}
specifier|private
name|JMenuItem
name|getPartialStreamSavingMenuItem
parameter_list|(
specifier|final
name|int
name|indexOfStopFilter
parameter_list|,
specifier|final
name|PDStream
name|stream
parameter_list|)
block|{
name|List
argument_list|<
name|COSName
argument_list|>
name|filters
init|=
name|stream
operator|.
name|getFilters
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|stopFilters
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|stopFilters
operator|.
name|add
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|indexOfStopFilter
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|nameListBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|indexOfStopFilter
init|;
name|i
operator|<
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|nameListBuilder
operator|.
name|append
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"& "
argument_list|)
expr_stmt|;
block|}
name|nameListBuilder
operator|.
name|delete
argument_list|(
name|nameListBuilder
operator|.
name|lastIndexOf
argument_list|(
literal|"&"
argument_list|)
argument_list|,
name|nameListBuilder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|JMenuItem
name|menuItem
init|=
operator|new
name|JMenuItem
argument_list|(
literal|"Keep "
operator|+
name|nameListBuilder
operator|.
name|toString
argument_list|()
operator|+
literal|"..."
argument_list|)
decl_stmt|;
name|menuItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
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
try|try
block|{
name|InputStream
name|data
init|=
name|stream
operator|.
name|getPartiallyFilteredStream
argument_list|(
name|stopFilters
argument_list|)
decl_stmt|;
name|saveStream
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|data
argument_list|)
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
argument_list|)
expr_stmt|;
return|return
name|menuItem
return|;
block|}
comment|/**      * Save the stream.      * @param bytes byte array of the stream.      * @throws IOException if there is an error in creation of the file.      */
specifier|private
name|void
name|saveStream
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOpenSaveDialog
name|saveDialog
init|=
operator|new
name|FileOpenSaveDialog
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|saveDialog
operator|.
name|saveFile
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

