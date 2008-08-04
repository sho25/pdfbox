begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|action
operator|.
name|type
package|;
end_package

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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_comment
comment|/**  * This represents a launch action that can be executed in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Panagiotis Toumasis (ptoumasis@mail.gr)  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDActionLaunch
extends|extends
name|PDAction
block|{
comment|/**      * This type of action this object represents.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Launch"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDActionLaunch
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setSubType
argument_list|(
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDActionLaunch
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the application to be launched or the document      * to be opened or printed. It is required if none of the entries      * Win, Mac or Unix is present. If this entry is absent and the      * viewer application does not understand any of the alternative      * entries it should do nothing.      *      * @return The F entry of the specific launch action dictionary.      *       * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|PDFileSpecification
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"F"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the application to be launched or the document      * to be opened or printed. It is required if none of the entries      * Win, Mac or Unix is present. If this entry is absent and the      * viewer application does not understand any of the alternative      * entries it should do nothing.      *      * @param fs The file specification.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|fs
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"F"
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a dictionary containing Windows-specific launch parameters.      *      * @return The Win entry of of the specific launch action dictionary.      */
specifier|public
name|PDWindowsLaunchParams
name|getWinLaunchParams
parameter_list|()
block|{
name|COSDictionary
name|win
init|=
operator|(
name|COSDictionary
operator|)
name|action
operator|.
name|getDictionaryObject
argument_list|(
literal|"Win"
argument_list|)
decl_stmt|;
name|PDWindowsLaunchParams
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|win
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDWindowsLaunchParams
argument_list|(
name|win
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a dictionary containing Windows-specific launch parameters.      *      * @param win The action to be performed.      */
specifier|public
name|void
name|setWinLaunchParams
parameter_list|(
name|PDWindowsLaunchParams
name|win
parameter_list|)
block|{
name|action
operator|.
name|setItem
argument_list|(
literal|"Win"
argument_list|,
name|win
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the file name to be launched or the document to be opened      * or printed, in standard Windows pathname format. If the name string includes      * a backslash character (\), the backslash must itself be preceded by a backslash.      * This value must be a single string; it is not a file specification.      *      * @return The F entry of the specific Windows launch parameter dictionary.      */
specifier|public
name|String
name|getF
parameter_list|()
block|{
return|return
name|action
operator|.
name|getString
argument_list|(
literal|"F"
argument_list|)
return|;
block|}
comment|/**      * This will set the file name to be launched or the document to be opened      * or printed, in standard Windows pathname format. If the name string includes      * a backslash character (\), the backslash must itself be preceded by a backslash.      * This value must be a single string; it is not a file specification.      *      * @param f The file name to be launched.      */
specifier|public
name|void
name|setF
parameter_list|(
name|String
name|f
parameter_list|)
block|{
name|action
operator|.
name|setString
argument_list|(
literal|"F"
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the string specifying the default directory in standard DOS syntax.      *      * @return The D entry of the specific Windows launch parameter dictionary.      */
specifier|public
name|String
name|getD
parameter_list|()
block|{
return|return
name|action
operator|.
name|getString
argument_list|(
literal|"D"
argument_list|)
return|;
block|}
comment|/**      * This will set the string specifying the default directory in standard DOS syntax.      *      * @param d The default directory.      */
specifier|public
name|void
name|setD
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|action
operator|.
name|setString
argument_list|(
literal|"D"
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the string specifying the operation to perform:      * open to open a document      * print to print a document      * If the F entry designates an application instead of a document, this entry      * is ignored and the application is launched. Default value: open.      *      * @return The O entry of the specific Windows launch parameter dictionary.      */
specifier|public
name|String
name|getO
parameter_list|()
block|{
return|return
name|action
operator|.
name|getString
argument_list|(
literal|"O"
argument_list|)
return|;
block|}
comment|/**      * This will set the string specifying the operation to perform:      * open to open a document      * print to print a document      * If the F entry designates an application instead of a document, this entry      * is ignored and the application is launched. Default value: open.      *      * @param o The operation to perform.      */
specifier|public
name|void
name|setO
parameter_list|(
name|String
name|o
parameter_list|)
block|{
name|action
operator|.
name|setString
argument_list|(
literal|"O"
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a parameter string to be passed to the application designated by the F entry.      * This entry should be omitted if F designates a document.      *      * @return The P entry of the specific Windows launch parameter dictionary.      */
specifier|public
name|String
name|getP
parameter_list|()
block|{
return|return
name|action
operator|.
name|getString
argument_list|(
literal|"P"
argument_list|)
return|;
block|}
comment|/**      * This will set a parameter string to be passed to the application designated by the F entry.      * This entry should be omitted if F designates a document.      *      * @param p The parameter string.      */
specifier|public
name|void
name|setP
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|action
operator|.
name|setString
argument_list|(
literal|"P"
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will specify whether to open the destination document in a new window.      * If this flag is false, the destination document will replace the current      * document in the same window. If this entry is absent, the viewer application      * should behave in accordance with the current user preference. This entry is      * ignored if the file designated by the F entry is not a PDF document.      *      * @return A flag specifying whether to open the destination document in a new window.      */
specifier|public
name|boolean
name|shouldOpenInNewWindow
parameter_list|()
block|{
return|return
name|action
operator|.
name|getBoolean
argument_list|(
literal|"NewWindow"
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * This will specify the destination document to open in a new window.      *      * @param value The flag value.      */
specifier|public
name|void
name|setOpenInNewWindow
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|action
operator|.
name|setBoolean
argument_list|(
literal|"NewWindow"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

