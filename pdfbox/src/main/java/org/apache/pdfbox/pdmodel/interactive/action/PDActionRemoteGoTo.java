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
name|pdmodel
operator|.
name|interactive
operator|.
name|action
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
comment|/**  * This represents a remote go-to action that can be executed in a PDF document.  *  * @author Ben Litchfield  * @author Panagiotis Toumasis  */
end_comment

begin_class
specifier|public
class|class
name|PDActionRemoteGoTo
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
literal|"GoToR"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDActionRemoteGoTo
parameter_list|()
block|{
name|setSubType
argument_list|(
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDActionRemoteGoTo
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
comment|/**      * This will get the file in which the destination is located.      *      * @return The F entry of the specific remote go-to action dictionary.      *      * @throws IOException If there is an error creating the file spec.      */
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
name|action
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the file in which the destination is located.      *      * @param fs The file specification.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|fs
parameter_list|)
block|{
name|action
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the destination to jump to.      * If the value is an array defining an explicit destination,      * its first element must be a page number within the remote      * document rather than an indirect reference to a page object      * in the current document. The first page is numbered 0.      *      * @return The D entry of the specific remote go-to action dictionary.      */
comment|// Array or String.
specifier|public
name|COSBase
name|getD
parameter_list|()
block|{
return|return
name|action
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
return|;
block|}
comment|/**      * This will set the destination to jump to.      * If the value is an array defining an explicit destination,      * its first element must be a page number within the remote      * document rather than an indirect reference to a page object      * in the current document. The first page is numbered 0.      *      * @param d The destination.      */
comment|// In case the value is an array.
specifier|public
name|void
name|setD
parameter_list|(
name|COSBase
name|d
parameter_list|)
block|{
name|action
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will specify whether to open the destination document in a new window, in the same      * window, or behave in accordance with the current user preference.      *      * @return A flag specifying how to open the destination document.      */
specifier|public
name|OpenMode
name|getOpenInNewWindow
parameter_list|()
block|{
if|if
condition|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|)
operator|instanceof
name|COSBoolean
condition|)
block|{
name|COSBoolean
name|b
init|=
operator|(
name|COSBoolean
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|)
decl_stmt|;
return|return
name|b
operator|.
name|getValue
argument_list|()
condition|?
name|OpenMode
operator|.
name|NEW_WINDOW
else|:
name|OpenMode
operator|.
name|SAME_WINDOW
return|;
block|}
return|return
name|OpenMode
operator|.
name|USER_PREFERENCE
return|;
block|}
comment|/**      * This will specify whether to open the destination document in a new window.      *      * @param value The flag value.      */
specifier|public
name|void
name|setOpenInNewWindow
parameter_list|(
name|OpenMode
name|value
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|value
condition|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|)
expr_stmt|;
return|return;
block|}
switch|switch
condition|(
name|value
condition|)
block|{
case|case
name|USER_PREFERENCE
case|:
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|)
expr_stmt|;
break|break;
case|case
name|SAME_WINDOW
case|:
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|,
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|NEW_WINDOW
case|:
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|NEW_WINDOW
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// shouldn't happen unless the enum type is changed
break|break;
block|}
block|}
block|}
end_class

end_unit

