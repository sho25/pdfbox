begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|integration
package|;
end_package

begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
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
name|parser
operator|.
name|PreflightParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractInvalidFileTester
block|{
comment|/**      * where result information are pushed      */
specifier|protected
name|OutputStream
name|outputResult
init|=
literal|null
decl_stmt|;
comment|/**      * carry the expected error with the current test      */
specifier|protected
specifier|final
name|String
name|expectedError
decl_stmt|;
comment|/**      * carry the path of the file validated during current test      */
specifier|protected
name|File
name|path
decl_stmt|;
specifier|protected
specifier|static
name|Logger
name|staticLogger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
specifier|protected
name|Logger
name|logger
init|=
literal|null
decl_stmt|;
comment|/**      * Prepare the test for one file      *       * @param path pdf/a file to test      * @param error expected error for this test      */
specifier|public
name|AbstractInvalidFileTester
parameter_list|(
name|File
name|path
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|expectedError
operator|=
name|error
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|()
specifier|public
specifier|final
name|void
name|validate
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"This is an empty test"
argument_list|)
expr_stmt|;
return|return;
block|}
name|PreflightDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|FileDataSource
name|bds
init|=
operator|new
name|FileDataSource
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|PreflightParser
name|parser
init|=
operator|new
name|PreflightParser
argument_list|(
name|bds
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|document
operator|=
name|parser
operator|.
name|getPreflightDocument
argument_list|()
expr_stmt|;
name|document
operator|.
name|validate
argument_list|()
expr_stmt|;
name|ValidationResult
name|result
init|=
name|document
operator|.
name|getResult
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|path
operator|+
literal|" : Isartor file should be invalid ("
operator|+
name|path
operator|+
literal|")"
argument_list|,
name|result
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|path
operator|+
literal|" : Should find at least one error"
argument_list|,
name|result
operator|.
name|getErrorsList
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// could contain more than one error
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|expectedError
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ValidationError
name|error
range|:
name|result
operator|.
name|getErrorsList
argument_list|()
control|)
block|{
if|if
condition|(
name|error
operator|.
name|getErrorCode
argument_list|()
operator|.
name|equals
argument_list|(
name|this
operator|.
name|expectedError
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|outputResult
operator|==
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|outputResult
operator|!=
literal|null
condition|)
block|{
name|String
name|log
init|=
name|path
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|".pdf"
argument_list|,
literal|""
argument_list|)
operator|+
literal|"#"
operator|+
name|error
operator|.
name|getErrorCode
argument_list|()
operator|+
literal|"#"
operator|+
name|error
operator|.
name|getDetails
argument_list|()
operator|+
literal|"\n"
decl_stmt|;
name|outputResult
operator|.
name|write
argument_list|(
name|log
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|.
name|getErrorsList
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|expectedError
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"File invalid as expected (no expected code) :"
operator|+
name|this
operator|.
name|path
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|message
operator|.
name|append
argument_list|(
name|path
argument_list|)
operator|.
name|append
argument_list|(
literal|" : Invalid error code returned. Expected "
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
name|this
operator|.
name|expectedError
argument_list|)
operator|.
name|append
argument_list|(
literal|", found "
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationError
name|error
range|:
name|result
operator|.
name|getErrorsList
argument_list|()
control|)
block|{
name|message
operator|.
name|append
argument_list|(
name|error
operator|.
name|getErrorCode
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|fail
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|path
operator|+
literal|" : Invalid error code returned."
argument_list|,
name|this
operator|.
name|expectedError
argument_list|,
name|result
operator|.
name|getErrorsList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|path
operator|+
literal|" :"
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
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|String
name|getResultFileKey
parameter_list|()
function_decl|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|irp
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|getResultFileKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|irp
operator|==
literal|null
condition|)
block|{
comment|// no log file defined, use system.err
name|outputResult
operator|=
name|System
operator|.
name|err
expr_stmt|;
block|}
else|else
block|{
name|outputResult
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|irp
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|after
parameter_list|()
throws|throws
name|Exception
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|outputResult
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

