begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
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
name|pdmodel
operator|.
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|preflight
operator|.
name|PreflightConstants
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_ACTION_INVALID_TYPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_ACTION_MISING_KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
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
name|preflight
operator|.
name|PreflightContext
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
name|preflight
operator|.
name|ValidationResult
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
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|preflight
operator|.
name|exception
operator|.
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * ActionManager for the GoToRemote action. GoToRemoteAction is valid if the F entry is present.  */
end_comment

begin_class
specifier|public
class|class
name|GoToRemoteAction
extends|extends
name|GoToAction
block|{
comment|/**      *       * @param amFact Instance of ActionManagerFactory used to create ActionManager to check Next actions.      * @param adict the COSDictionary of the action wrapped by this class.      * @param ctx the preflight context.      * @param aaKey the name of the key which identify the action in a additional action dictionary.      */
specifier|public
name|GoToRemoteAction
parameter_list|(
name|ActionManagerFactory
name|amFact
parameter_list|,
name|COSDictionary
name|adict
parameter_list|,
name|PreflightContext
name|ctx
parameter_list|,
name|String
name|aaKey
parameter_list|)
block|{
name|super
argument_list|(
name|amFact
argument_list|,
name|adict
argument_list|,
name|ctx
argument_list|,
name|aaKey
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see AbstractActionManager#valid(java.util.List)      */
annotation|@
name|Override
specifier|protected
name|boolean
name|innerValid
parameter_list|()
throws|throws
name|ValidationException
block|{
name|COSBase
name|dest
init|=
name|this
operator|.
name|actionDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
decl_stmt|;
comment|// ---- D entry is mandatory
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_MISING_KEY
argument_list|,
literal|"/D entry is mandatory for the GoToActions"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|dest
operator|instanceof
name|COSName
operator|||
name|dest
operator|instanceof
name|COSString
operator|||
name|dest
operator|instanceof
name|COSArray
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_INVALID_TYPE
argument_list|,
literal|"Type "
operator|+
name|dest
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" of /D entry is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|COSBase
name|f
init|=
name|this
operator|.
name|actionDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_MISING_KEY
argument_list|,
literal|"/F entry is mandatory for the GoToRemoteActions"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|dest
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|ar
init|=
operator|(
name|COSArray
operator|)
name|dest
decl_stmt|;
if|if
condition|(
name|ar
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"Destination array must have at least 2 elements"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|ar
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|COSName
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"Second element of destination array must be a name"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|validateExplicitDestination
argument_list|(
name|ar
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|PDDestination
operator|.
name|create
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|validateExplicitDestination
parameter_list|(
name|COSArray
name|ar
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|COSNumber
operator|)
condition|)
block|{
comment|// "its first element shall be a page number"
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_INVALID_TYPE
argument_list|,
literal|"First element in /D array entry of GoToRemoteAction must be a page number, but is "
operator|+
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

