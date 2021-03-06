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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
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
class|class
name|TestValidFiles
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESULTS_FILE
init|=
literal|"results.file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ISARTOR_FILES
init|=
literal|"valid.files"
decl_stmt|;
specifier|protected
specifier|static
name|OutputStream
name|isartorResultFile
init|=
literal|null
decl_stmt|;
specifier|protected
name|File
name|path
decl_stmt|;
specifier|protected
specifier|static
name|Log
name|staticLogger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
specifier|protected
name|Log
name|logger
init|=
literal|null
decl_stmt|;
specifier|public
name|TestValidFiles
parameter_list|(
name|File
name|path
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
name|logger
operator|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|path
operator|!=
literal|null
condition|?
name|path
operator|.
name|getName
argument_list|()
else|:
literal|"dummy"
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|stopIfExpected
parameter_list|()
throws|throws
name|Exception
block|{
comment|// throw new Exception("Test badly configured");
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|null
block|}
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
annotation|@
name|Parameters
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|initializeParameters
parameter_list|()
throws|throws
name|Exception
block|{
comment|// find isartor files
name|String
name|isartor
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|ISARTOR_FILES
argument_list|)
decl_stmt|;
if|if
condition|(
name|isartor
operator|==
literal|null
operator|||
name|isartor
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|staticLogger
operator|.
name|warn
argument_list|(
name|ISARTOR_FILES
operator|+
literal|" (where are isartor pdf files) is not defined."
argument_list|)
expr_stmt|;
return|return
name|stopIfExpected
argument_list|()
return|;
block|}
name|File
name|root
init|=
operator|new
name|File
argument_list|(
name|isartor
argument_list|)
decl_stmt|;
comment|// load expected errors
comment|// prepare config
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|files
init|=
name|FileUtils
operator|.
name|listFiles
argument_list|(
name|root
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"pdf"
block|}
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|files
control|)
block|{
name|File
name|file
init|=
operator|(
name|File
operator|)
name|object
decl_stmt|;
name|Object
index|[]
name|tmp
init|=
operator|new
name|Object
index|[]
block|{
name|file
block|}
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
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
name|RESULTS_FILE
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No result file defined, will use standard error"
argument_list|)
expr_stmt|;
name|isartorResultFile
operator|=
name|System
operator|.
name|err
expr_stmt|;
block|}
else|else
block|{
name|isartorResultFile
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
name|AfterClass
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|isartorResultFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|()
specifier|public
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
name|ValidationResult
name|result
init|=
name|PreflightParser
operator|.
name|validate
argument_list|(
name|path
argument_list|)
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
name|Assert
operator|.
name|fail
argument_list|(
literal|"File expected valid : "
operator|+
name|path
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

