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
name|utils
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
name|*
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
name|PreflightConfiguration
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
name|PreflightPath
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
name|process
operator|.
name|ValidationProcess
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

begin_class
specifier|public
class|class
name|ContextHelper
block|{
comment|/**      * Check that the element parameter isn't null before calling the      * {@link #callValidation(PreflightContext, Object, String)} method.      *       * @param context      * @param element      * @param processName      * @throws ValidationException      */
specifier|public
specifier|static
name|void
name|validateElement
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|Object
name|element
parameter_list|,
name|String
name|processName
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|element
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
name|ERROR_PDF_PROCESSING_MISSING
argument_list|,
literal|"Unable to process an element if it is null."
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callValidation
argument_list|(
name|context
argument_list|,
name|element
argument_list|,
name|processName
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Put the element to check on the top of the ValidationPath and call the validation method on the Process.      *       * @param context      *            (mandatory) the preflight context that contains all required information      * @param element      * @param processName      *            the process to instantiate and to compute      * @throws ValidationException      */
specifier|private
specifier|static
name|void
name|callValidation
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|Object
name|element
parameter_list|,
name|String
name|processName
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightPath
name|validationPath
init|=
name|context
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
name|boolean
name|needPop
init|=
name|validationPath
operator|.
name|pushObject
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|PreflightConfiguration
name|config
init|=
name|context
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|ValidationProcess
name|process
init|=
name|config
operator|.
name|getInstanceOfProcess
argument_list|(
name|processName
argument_list|)
decl_stmt|;
name|process
operator|.
name|validate
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|needPop
condition|)
block|{
name|validationPath
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * call directly the {@link #callValidation(PreflightContext, Object, String)}      *       * @param context      * @param processName      * @throws ValidationException      */
specifier|public
specifier|static
name|void
name|validateElement
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|String
name|processName
parameter_list|)
throws|throws
name|ValidationException
block|{
name|callValidation
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
name|processName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

