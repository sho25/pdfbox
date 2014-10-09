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
name|FileOutputStream
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
name|SyntaxValidationException
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
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|utils
operator|.
name|ByteArrayDataSource
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|TestIsartor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FILTER_FILE
init|=
literal|"isartor.filter"
decl_stmt|;
specifier|private
specifier|static
name|FileOutputStream
name|isartorResultFile
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TestIsartor
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
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
name|String
name|filter
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|FILTER_FILE
argument_list|)
decl_stmt|;
comment|// load expected errors
name|File
name|f
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/expected_errors.txt"
argument_list|)
decl_stmt|;
name|InputStream
name|expected
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
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
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|File
name|isartor
init|=
operator|new
name|File
argument_list|(
literal|"target/pdfs/Isartor testsuite/PDFA-1b"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isartor
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|pdfFiles
init|=
name|FileUtils
operator|.
name|listFiles
argument_list|(
name|isartor
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"pdf"
block|,
literal|"PDF"
block|}
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|pdfFile
range|:
name|pdfFiles
control|)
block|{
name|String
name|fn
init|=
operator|(
operator|(
name|File
operator|)
name|pdfFile
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|fn
operator|.
name|contains
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|fn
argument_list|)
decl_stmt|;
name|String
name|error
init|=
operator|new
name|StringTokenizer
argument_list|(
name|path
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
name|data
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|(
name|File
operator|)
name|pdfFile
block|,
name|error
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Isartor data set has not been downloaded! Try running Maven?"
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
literal|"isartor.results.path"
argument_list|)
decl_stmt|;
if|if
condition|(
name|irp
operator|!=
literal|null
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|irp
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
name|isartorResultFile
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|isartorResultFile
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid result file : "
operator|+
name|irp
argument_list|)
throw|;
block|}
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
if|if
condition|(
name|isartorResultFile
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|isartorResultFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|final
name|String
name|expectedError
decl_stmt|;
specifier|private
specifier|final
name|File
name|file
decl_stmt|;
specifier|public
name|TestIsartor
parameter_list|(
name|File
name|path
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|path
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|expectedError
operator|=
name|error
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
name|PreflightDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|InputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|ValidationResult
name|result
decl_stmt|;
try|try
block|{
name|PreflightParser
name|parser
init|=
operator|new
name|PreflightParser
argument_list|(
operator|new
name|ByteArrayDataSource
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|document
operator|=
operator|(
name|PreflightDocument
operator|)
name|parser
operator|.
name|getPDDocument
argument_list|()
expr_stmt|;
comment|// to speeds up tests, skip validation of page count is over the limit
if|if
condition|(
name|document
operator|.
name|getNumberOfPages
argument_list|()
operator|<
literal|8191
condition|)
block|{
name|document
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
name|result
operator|=
name|document
operator|.
name|getResult
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SyntaxValidationException
name|e
parameter_list|)
block|{
name|result
operator|=
name|e
operator|.
name|getResult
argument_list|()
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" : Isartor file should be invalid (expected "
operator|+
name|this
operator|.
name|expectedError
operator|+
literal|")"
argument_list|,
name|result
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|getName
argument_list|()
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
name|isartorResultFile
operator|==
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|isartorResultFile
operator|!=
literal|null
condition|)
block|{
name|String
name|log
init|=
name|file
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
name|isartorResultFile
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
literal|1
condition|)
block|{
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
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s : Invalid error code returned. Expected %s, found [%s]"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|expectedError
argument_list|,
name|message
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// if one of the error code of the list is the expected one, we consider test valid
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|file
operator|.
name|getName
argument_list|()
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
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s : %s raised , message=%s"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

