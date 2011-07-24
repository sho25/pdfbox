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
name|padaf
operator|.
name|preflight
operator|.
name|helpers
package|;
end_package

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
name|padaf
operator|.
name|preflight
operator|.
name|DocumentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidatorConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
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
name|padaf
operator|.
name|preflight
operator|.
name|actions
operator|.
name|ActionManagerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|annotation
operator|.
name|AnnotationValidatorFactory
import|;
end_import

begin_comment
comment|/**  * Abstract class for ValidationHelper. A validation helper is an object which  * check the PDF/A-1x compliance of a specific PDF element.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractValidationHelper
implements|implements
name|ValidationConstants
block|{
specifier|protected
name|ValidatorConfig
name|valConfig
init|=
literal|null
decl_stmt|;
specifier|protected
name|AnnotationValidatorFactory
name|annotFact
init|=
literal|null
decl_stmt|;
specifier|protected
name|ActionManagerFactory
name|actionFact
init|=
literal|null
decl_stmt|;
specifier|public
name|AbstractValidationHelper
parameter_list|(
name|ValidatorConfig
name|cfg
parameter_list|)
throws|throws
name|ValidationException
block|{
name|valConfig
operator|=
name|cfg
expr_stmt|;
try|try
block|{
name|this
operator|.
name|actionFact
operator|=
name|cfg
operator|.
name|getActionFact
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to instatiate action factory : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to instatiate action factory : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|this
operator|.
name|annotFact
operator|=
name|cfg
operator|.
name|getAnnotFact
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|this
operator|.
name|annotFact
operator|.
name|setActionFact
argument_list|(
name|this
operator|.
name|actionFact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to instatiate annotation factory : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to instatiate annotation factory : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Validation process of all inherited classes.    *     * @param handler    *          the object which contains the PDF Document    * @return A list of validation error. If there are no error, the list is    *         empty.    * @throws ValidationException    */
specifier|public
specifier|abstract
name|List
argument_list|<
name|ValidationError
argument_list|>
name|innerValidate
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**    * Process the validation of specific elements contained in the PDF document.    *     * @param handler    *          the object which contains the PDF Document    * @return A list of validation error. If there are no error, the list is    *         empty.    * @throws ValidationException    */
specifier|public
specifier|final
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validate
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|)
throws|throws
name|ValidationException
block|{
name|checkHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|innerValidate
argument_list|(
name|handler
argument_list|)
return|;
block|}
comment|/**    * Check if the Handler isn't null and if it is complete.    *     * @param handler    * @throws ValidationException    */
specifier|protected
name|void
name|checkHandler
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"DocumentHandler can't be null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|handler
operator|.
name|isComplete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"DocumentHandler error : missing element"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

