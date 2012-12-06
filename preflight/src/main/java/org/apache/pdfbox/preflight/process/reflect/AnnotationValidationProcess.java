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
name|process
operator|.
name|reflect
package|;
end_package

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
name|annotation
operator|.
name|AnnotationValidator
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
name|annotation
operator|.
name|AnnotationValidatorFactory
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
name|AbstractProcess
import|;
end_import

begin_class
specifier|public
class|class
name|AnnotationValidationProcess
extends|extends
name|AbstractProcess
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|context
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightPath
name|vPath
init|=
name|context
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|vPath
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|vPath
operator|.
name|isExpectedType
argument_list|(
name|COSDictionary
operator|.
name|class
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Annotation validation process needs at least one COSDictionary object"
argument_list|)
throw|;
block|}
name|COSDictionary
name|annotDict
init|=
operator|(
name|COSDictionary
operator|)
name|vPath
operator|.
name|peek
argument_list|()
decl_stmt|;
name|PreflightConfiguration
name|config
init|=
name|context
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|AnnotationValidatorFactory
name|factory
init|=
name|config
operator|.
name|getAnnotFact
argument_list|()
decl_stmt|;
name|AnnotationValidator
name|annotValidator
init|=
name|factory
operator|.
name|getAnnotationValidator
argument_list|(
name|context
argument_list|,
name|annotDict
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotValidator
operator|!=
literal|null
condition|)
block|{
name|annotValidator
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

