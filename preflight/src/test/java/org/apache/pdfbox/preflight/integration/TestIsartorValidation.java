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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|TestIsartorValidation
extends|extends
name|AbstractInvalidFileTester
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
name|EXPECTED_ERRORS
init|=
literal|"isartor.errors"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ISARTOR_FILES
init|=
literal|"isartor.files"
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
specifier|public
name|TestIsartorValidation
parameter_list|(
name|File
name|path
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|super
argument_list|(
name|path
argument_list|,
name|error
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
block|,
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
name|String
name|expectedPath
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|EXPECTED_ERRORS
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedPath
operator|==
literal|null
condition|)
block|{
name|staticLogger
operator|.
name|warn
argument_list|(
literal|"'expected.errors' not defined, so cannot execute tests"
argument_list|)
expr_stmt|;
return|return
name|stopIfExpected
argument_list|()
return|;
block|}
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|expectedPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expectedFile
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|expectedFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|staticLogger
operator|.
name|warn
argument_list|(
literal|"'expected.errors' does not reference valid file, so cannot execute tests : "
operator|+
name|expectedFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|stopIfExpected
argument_list|()
return|;
block|}
name|InputStream
name|expected
init|=
operator|new
name|FileInputStream
argument_list|(
name|expectedPath
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|expected
argument_list|)
expr_stmt|;
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
name|String
name|fn
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|.
name|getProperty
argument_list|(
name|fn
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|error
init|=
operator|new
name|StringTokenizer
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|fn
argument_list|)
argument_list|,
literal|"//"
argument_list|)
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
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
block|,
name|error
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
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No expected result for this file, will not try to validate : "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|data
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getResultFileKey
parameter_list|()
block|{
return|return
name|RESULTS_FILE
return|;
block|}
block|}
end_class

end_unit

