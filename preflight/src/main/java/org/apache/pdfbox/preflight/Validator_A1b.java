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
name|Version
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
name|parser
operator|.
name|PreflightParser
import|;
end_import

begin_comment
comment|/**  * This class is a simple main class used to check the validity of a pdf file.  *   * Usage : java net.awl.edoc.pdfa.Validator<file path>  *   * @author gbailleul  *   */
end_comment

begin_class
specifier|public
class|class
name|Validator_A1b
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Usage : java org.apache.pdfbox.preflight.Validator_A1b<file path>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Version : "
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|FileDataSource
name|fd
init|=
operator|new
name|FileDataSource
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|PreflightParser
name|parser
init|=
operator|new
name|PreflightParser
argument_list|(
name|fd
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
name|ValidationResult
name|result
init|=
name|document
operator|.
name|getResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The file "
operator|+
name|args
index|[
literal|0
index|]
operator|+
literal|" is a valid PDF/A-1b file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The file"
operator|+
name|args
index|[
literal|0
index|]
operator|+
literal|" is not valid, error(s) :"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|error
operator|.
name|getErrorCode
argument_list|()
operator|+
literal|" : "
operator|+
name|error
operator|.
name|getDetails
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

