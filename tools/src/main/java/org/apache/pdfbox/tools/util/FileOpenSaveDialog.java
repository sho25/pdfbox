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
name|util
package|;
end_package

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
name|FileOutputStream
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
name|JOptionPane
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
name|tools
operator|.
name|PDFDebugger
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A Customized class that helps to open and save file. It uses JFileChooser to operate.  */
end_comment

begin_class
specifier|public
class|class
name|FileOpenSaveDialog
block|{
name|PDFDebugger
name|mainUI
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|JFileChooser
name|fileChooser
init|=
operator|new
name|JFileChooser
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|approveSelection
parameter_list|()
block|{
name|File
name|selectedFile
init|=
name|getSelectedFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedFile
operator|.
name|exists
argument_list|()
operator|&&
name|getDialogType
argument_list|()
operator|==
name|JFileChooser
operator|.
name|SAVE_DIALOG
condition|)
block|{
name|int
name|result
init|=
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
name|this
argument_list|,
literal|"Do you want to overwrite?"
argument_list|,
literal|"File already exists"
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|YES_OPTION
condition|)
block|{
name|super
operator|.
name|approveSelection
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cancelSelection
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|approveSelection
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/**      * Constructor.      * @param parentUI the main UI (JFrame) on top of which File open/save dialog should open.      * @param fileFilter file Filter, null is allowed when no filter is applicable.      */
specifier|public
name|FileOpenSaveDialog
parameter_list|(
name|PDFDebugger
name|parentUI
parameter_list|,
name|ExtensionFileFilter
name|fileFilter
parameter_list|)
block|{
name|mainUI
operator|=
name|parentUI
expr_stmt|;
name|fileChooser
operator|.
name|setFileFilter
argument_list|(
name|fileFilter
argument_list|)
expr_stmt|;
block|}
comment|/**      * saves a file while user is prompted to chose the destination.      * @param bytes byte array of the saving file.      * @return true if file is saved successfully or false if failed.      * @throws IOException if there is error in creation of file.      */
specifier|public
name|boolean
name|saveFile
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|result
init|=
name|fileChooser
operator|.
name|showSaveDialog
argument_list|(
name|mainUI
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
name|File
name|selectedFile
init|=
name|fileChooser
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
name|FileOutputStream
name|outputStream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|selectedFile
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * open a file prompting user to select the file.      * @return the file opened.      * @throws IOException if there is error in opening the file.      */
specifier|public
name|File
name|openFile
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|result
init|=
name|fileChooser
operator|.
name|showOpenDialog
argument_list|(
name|mainUI
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
return|return
name|fileChooser
operator|.
name|getSelectedFile
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

