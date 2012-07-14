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
package|;
end_package

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
name|padaf
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
name|TestValidDirectory
block|{
specifier|protected
name|File
name|target
init|=
literal|null
decl_stmt|;
specifier|public
name|TestValidDirectory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|file
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidationResult
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|PreflightParser
name|parser
init|=
operator|new
name|PreflightParser
argument_list|(
operator|new
name|FileDataSource
argument_list|(
name|target
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|PreflightDocument
name|document
init|=
operator|(
name|PreflightDocument
operator|)
name|parser
operator|.
name|getPDDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|validate
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Validation of "
operator|+
name|target
argument_list|,
name|document
operator|.
name|getResult
argument_list|()
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|closePdf
argument_list|()
expr_stmt|;
block|}
block|}
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
comment|// check directory
name|File
name|directory
init|=
literal|null
decl_stmt|;
name|String
name|pdfPath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"pdfa.valid"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"${user.pdfa.valid}"
operator|.
name|equals
argument_list|(
name|pdfPath
argument_list|)
condition|)
block|{
name|pdfPath
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|pdfPath
operator|!=
literal|null
condition|)
block|{
name|directory
operator|=
operator|new
name|File
argument_list|(
name|pdfPath
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|directory
operator|.
name|exists
argument_list|()
condition|)
throw|throw
operator|new
name|Exception
argument_list|(
literal|"directory does not exists : "
operator|+
name|directory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
if|if
condition|(
operator|!
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
throw|throw
operator|new
name|Exception
argument_list|(
literal|"not a directory : "
operator|+
name|directory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"System property 'pdfa.valid' not defined, will not run TestValidaDirectory"
argument_list|)
expr_stmt|;
block|}
comment|// create list
if|if
condition|(
name|directory
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
name|File
index|[]
name|files
init|=
name|directory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|(
name|files
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|data
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
name|file
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|data
return|;
block|}
block|}
block|}
end_class

end_unit

