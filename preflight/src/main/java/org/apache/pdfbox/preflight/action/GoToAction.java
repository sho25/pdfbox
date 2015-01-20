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
name|COSDocument
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
name|COSObject
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
name|utils
operator|.
name|COSUtils
import|;
end_import

begin_comment
comment|/**  * ActionManager for the GoTo action GoToAction is valid if the D entry is present.  */
end_comment

begin_class
specifier|public
class|class
name|GoToAction
extends|extends
name|AbstractActionManager
block|{
comment|/**      *       * @param amFact Instance of ActionManagerFactory used to create ActionManager to check Next actions.      * @param adict the COSDictionary of the action wrapped by this class.      * @param ctx the preflight context.      * @param aaKey the name of the key which identifies the action in an additional action dictionary.      */
specifier|public
name|GoToAction
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
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.actions.AbstractActionManager#valid(java.util .List)      */
annotation|@
name|Override
specifier|protected
name|boolean
name|innerValid
parameter_list|()
block|{
name|COSBase
name|d
init|=
name|this
operator|.
name|actionDictionnary
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
name|d
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
literal|"D entry is mandatory for the GoToActions"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|COSDocument
name|cosDocument
init|=
name|this
operator|.
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|d
operator|instanceof
name|COSName
operator|||
name|COSUtils
operator|.
name|isString
argument_list|(
name|d
argument_list|,
name|cosDocument
argument_list|)
operator|||
name|COSUtils
operator|.
name|isArray
argument_list|(
name|d
argument_list|,
name|cosDocument
argument_list|)
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
literal|"Type of D entry is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|d
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
name|d
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
name|ValidationError
argument_list|(
name|ERROR_ACTION_INVALID_TYPE
argument_list|,
literal|"/D entry of type array must have at least 2 elements"
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
name|validateExplicitDestination
argument_list|(
name|ar
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|validateExplicitDestination
parameter_list|(
name|COSArray
name|ar
parameter_list|)
block|{
comment|// "In each case, page is an indirect reference to a page object."
if|if
condition|(
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|ob
init|=
operator|(
name|COSObject
operator|)
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSBase
name|type
init|=
name|ob
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|PAGE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_INVALID_TYPE
argument_list|,
literal|"First element in /D array entry of GoToAction must be an indirect reference to a dictionary of /Type /Page, but is "
operator|+
name|ar
operator|.
name|getName
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
block|}
end_class

end_unit

