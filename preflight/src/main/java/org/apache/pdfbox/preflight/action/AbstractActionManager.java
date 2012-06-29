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
name|ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTION
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractActionManager
block|{
comment|/**    * ActionManager factory used to create new ActionManager    */
specifier|protected
name|ActionManagerFactory
name|actionFact
init|=
literal|null
decl_stmt|;
comment|/**    * Action name in a Additional Action dictionary    */
specifier|protected
name|String
name|aaKey
init|=
literal|null
decl_stmt|;
comment|/**    * The action dictionary checked by this class    */
specifier|protected
name|COSDictionary
name|actionDictionnary
init|=
literal|null
decl_stmt|;
comment|/**    * The validation context    */
specifier|protected
name|PreflightContext
name|context
init|=
literal|null
decl_stmt|;
comment|/**    *     * @param amFact    *          Instance of ActionManagerFactory used to create ActionManager to    *          check Next actions.    * @param adict    *          the COSDictionary of the action wrapped by this class.    * @param ctx the validation context .    * @param aaKey    *          The name of the key which identify the action in a additional    *          action dictionary.    */
name|AbstractActionManager
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
name|this
operator|.
name|actionFact
operator|=
name|amFact
expr_stmt|;
name|this
operator|.
name|actionDictionnary
operator|=
name|adict
expr_stmt|;
name|this
operator|.
name|aaKey
operator|=
name|aaKey
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|ctx
expr_stmt|;
block|}
comment|/**    * @return the isAdditionalAction    */
specifier|public
name|boolean
name|isAdditionalAction
parameter_list|()
block|{
return|return
name|this
operator|.
name|aaKey
operator|!=
literal|null
return|;
block|}
comment|/**    * @return the actionDictionnary    */
specifier|public
name|COSDictionary
name|getActionDictionnary
parameter_list|()
block|{
return|return
name|actionDictionnary
return|;
block|}
comment|/**    * @return the aaKey    */
specifier|public
name|String
name|getAdditionalActionKey
parameter_list|()
block|{
return|return
name|aaKey
return|;
block|}
comment|/**    * This method create a list of Action Managers which represent actions in the    * Next entry of the current action dictionary. For each Next Action, the    * innerValid is called and the method returns false if a validation fails.    *     * @return True if all Next Action are valid, false otherwise.    * @throws ValidationException    */
specifier|protected
name|boolean
name|validNextActions
parameter_list|()
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|AbstractActionManager
argument_list|>
name|lActions
init|=
name|this
operator|.
name|actionFact
operator|.
name|getNextActions
argument_list|(
name|this
operator|.
name|context
argument_list|,
name|this
operator|.
name|actionDictionnary
argument_list|)
decl_stmt|;
for|for
control|(
name|AbstractActionManager
name|nAction
range|:
name|lActions
control|)
block|{
if|if
condition|(
operator|!
name|nAction
operator|.
name|innerValid
argument_list|()
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
comment|/**    * Call the valid(boolean, List) method with the additonalActionAuth set to    * false.    *     * @param error    *          the validation error list to updated if the validation fails.    * @return    * @throws ValidationException    */
specifier|public
name|boolean
name|valid
parameter_list|()
throws|throws
name|ValidationException
block|{
return|return
name|valid
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/**    * Validate an Action dictionary.    *     * Return false if the dictionary is invalid (ex : missing key). If the    * ActionManager represents an AdditionalAction, this method returns false and    * updates the error list when the additonalActionAuth parameter is set to    * false.    *     * This method call the innerValid method to process specific checks according    * to the action type.    *     * If innerValid successes, all actions contained in the Next entry of the    * Action dictionary are validated.    *     * @param additonalActionAuth    *          boolean to know if an additional action is authorized.    * @return     * @throws ValidationException    */
specifier|public
name|boolean
name|valid
parameter_list|(
name|boolean
name|additonalActionAuth
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|isAdditionalAction
argument_list|()
operator|&&
operator|!
name|additonalActionAuth
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTION
argument_list|,
literal|"Additional Action are forbidden"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|innerValid
argument_list|()
condition|)
block|{
return|return
name|validNextActions
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**    * This method must be implemented by inherited classes to process specific    * validation.    *     * @return True if the action is valid, false otherwise.    */
specifier|protected
specifier|abstract
name|boolean
name|innerValid
parameter_list|()
function_decl|;
block|}
end_class

end_unit

