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
name|ERROR_ACTION_FORBIDDEN_ACTIONS_UNDEF
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

begin_comment
comment|/**  * ActionManager for Undefined Actions. An undefined action is an action which isn't declared in the PDF Reference Third  * Edition. This kind of actions are forbidden to avoid wrong result due to new features which can't be consistent with  * the PDF/A-1 format  */
end_comment

begin_class
specifier|public
class|class
name|UndefAction
extends|extends
name|AbstractActionManager
block|{
specifier|private
name|String
name|actionName
init|=
literal|null
decl_stmt|;
comment|/**      *       * @param amFact      *            Instance of ActionManagerFactory used to create ActionManager to check Next actions.      * @param adict      *            the COSDictionary of the action wrapped by this class.      * @param ctx      *            the COSDocument from which the action comes from.      * @param aaKey      *            The name of the key which identify the action in a additional action dictionary.      * @param name      *            the action type      */
specifier|public
name|UndefAction
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
parameter_list|,
name|String
name|name
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
name|this
operator|.
name|actionName
operator|=
name|name
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.preflight.action.AbstractActionManager#valid(java.util .List)      */
annotation|@
name|Override
specifier|protected
name|boolean
name|innerValid
parameter_list|()
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ACTIONS_UNDEF
argument_list|,
literal|"The action "
operator|+
name|actionName
operator|+
literal|" is undefined"
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

