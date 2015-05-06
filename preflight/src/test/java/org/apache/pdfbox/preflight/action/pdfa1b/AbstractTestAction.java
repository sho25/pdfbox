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
operator|.
name|pdfa1b
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
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
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
name|PDDocument
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
name|action
operator|.
name|PDAction
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
name|Format
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
name|PreflightDocument
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
name|action
operator|.
name|AbstractActionManager
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
name|action
operator|.
name|ActionManagerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTestAction
block|{
comment|/**      * Read a simple PDF/A to create a valid Context      *       * @return      * @throws Exception      */
specifier|protected
name|PreflightContext
name|createContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSource
name|ds
init|=
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/resources/pdfa-with-annotations-square.pdf"
argument_list|)
decl_stmt|;
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|PreflightDocument
name|preflightDocument
init|=
operator|new
name|PreflightDocument
argument_list|(
name|doc
operator|.
name|getDocument
argument_list|()
argument_list|,
name|Format
operator|.
name|PDF_A1B
argument_list|)
decl_stmt|;
name|PreflightContext
name|ctx
init|=
operator|new
name|PreflightContext
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|setDocument
argument_list|(
name|preflightDocument
argument_list|)
expr_stmt|;
name|preflightDocument
operator|.
name|setContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
comment|/**      * Run the Action validation and check the result.      *       * @param action      *            action to check      * @param valid      *            true if the Action must be valid, false if the action contains mistakes      * @throws Exception      */
specifier|protected
name|void
name|valid
parameter_list|(
name|PDAction
name|action
parameter_list|,
name|boolean
name|valid
parameter_list|)
throws|throws
name|Exception
block|{
name|valid
argument_list|(
name|action
argument_list|,
name|valid
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|valid
parameter_list|(
name|COSDictionary
name|action
parameter_list|,
name|boolean
name|valid
parameter_list|)
throws|throws
name|Exception
block|{
name|valid
argument_list|(
name|action
argument_list|,
name|valid
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Run the Action validation and check the result.      *       * @param action      *            action to check      * @param valid      *            true if the Action must be valid, false if the action contains mistakes      * @param expectedCode      *            the expected error code (can be null)      * @throws Exception      */
specifier|protected
name|void
name|valid
parameter_list|(
name|PDAction
name|action
parameter_list|,
name|boolean
name|valid
parameter_list|,
name|String
name|expectedCode
parameter_list|)
throws|throws
name|Exception
block|{
name|valid
argument_list|(
name|action
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|valid
argument_list|,
name|expectedCode
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|valid
parameter_list|(
name|COSDictionary
name|action
parameter_list|,
name|boolean
name|valid
parameter_list|,
name|String
name|expectedCode
parameter_list|)
throws|throws
name|Exception
block|{
name|ActionManagerFactory
name|fact
init|=
operator|new
name|ActionManagerFactory
argument_list|()
decl_stmt|;
name|PreflightContext
name|ctx
init|=
name|createContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|setConfig
argument_list|(
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|COSDictionary
name|dict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|,
name|action
argument_list|)
expr_stmt|;
comment|// process the action validation
name|List
argument_list|<
name|AbstractActionManager
argument_list|>
name|actions
init|=
name|fact
operator|.
name|getActionManagers
argument_list|(
name|ctx
argument_list|,
name|dict
argument_list|)
decl_stmt|;
for|for
control|(
name|AbstractActionManager
name|abstractActionManager
range|:
name|actions
control|)
block|{
name|abstractActionManager
operator|.
name|valid
argument_list|()
expr_stmt|;
block|}
comment|// check the result
if|if
condition|(
operator|!
name|valid
condition|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getResult
argument_list|()
operator|.
name|getErrorsList
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|errors
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedCode
operator|!=
literal|null
operator|||
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|expectedCode
argument_list|)
condition|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ValidationError
name|err
range|:
name|errors
control|)
block|{
if|if
condition|(
name|err
operator|.
name|getErrorCode
argument_list|()
operator|.
name|equals
argument_list|(
name|expectedCode
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getResult
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getResult
argument_list|()
operator|.
name|getErrorsList
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|errors
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

