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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|javacc
operator|.
name|PDFParser
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
name|javacc
operator|.
name|ParseException
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
name|javacc
operator|.
name|extractor
operator|.
name|ExtractorTokenManager
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
name|javacc
operator|.
name|extractor
operator|.
name|SimpleCharStream
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
name|helpers
operator|.
name|AbstractValidationHelper
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

begin_class
specifier|public
class|class
name|PdfA1bValidator
extends|extends
name|AbstractValidator
block|{
specifier|public
name|PdfA1bValidator
parameter_list|(
name|ValidatorConfig
name|cfg
parameter_list|)
throws|throws
name|ValidationException
block|{
name|super
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
comment|/* 	 * (non-Javadoc) 	 *  	 * @see 	 * net.awl.edoc.pdfa.validation.PdfAValidator#validate(javax.activation.DataSource 	 * ) 	 */
specifier|public
specifier|synchronized
name|ValidationResult
name|validate
parameter_list|(
name|DataSource
name|source
parameter_list|)
throws|throws
name|ValidationException
block|{
name|DocumentHandler
name|handler
init|=
name|createDocumentHandler
argument_list|(
name|source
argument_list|)
decl_stmt|;
try|try
block|{
comment|// syntax (javacc) validation
try|try
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|(
name|source
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|parser
operator|.
name|PDF
argument_list|()
expr_stmt|;
name|handler
operator|.
name|setParser
argument_list|(
name|parser
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Failed to parse datasource due to : "
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
name|ParseException
name|e
parameter_list|)
block|{
return|return
name|createErrorResult
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|// if here is reached, validate with helpers
comment|// init PDF Box document
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|handler
operator|.
name|getSource
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"PDFBox failed to parse datasource"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// init PDF Extractor
try|try
block|{
name|SimpleCharStream
name|scs
init|=
operator|new
name|SimpleCharStream
argument_list|(
name|source
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|ExtractorTokenManager
name|extractor
init|=
operator|new
name|ExtractorTokenManager
argument_list|(
name|scs
argument_list|)
decl_stmt|;
name|extractor
operator|.
name|parse
argument_list|()
expr_stmt|;
name|handler
operator|.
name|setPdfExtractor
argument_list|(
name|extractor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"PDF ExtractorTokenMng failed to parse datasource"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// call all helpers
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
name|allErrors
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
comment|// Execute priority helpers.
for|for
control|(
name|AbstractValidationHelper
name|helper
range|:
name|priorHelpers
control|)
block|{
name|runValidation
argument_list|(
name|handler
argument_list|,
name|helper
argument_list|,
name|allErrors
argument_list|)
expr_stmt|;
block|}
comment|// Execute other helpers.
for|for
control|(
name|AbstractValidationHelper
name|helper
range|:
name|standHelpers
control|)
block|{
name|runValidation
argument_list|(
name|handler
argument_list|,
name|helper
argument_list|,
name|allErrors
argument_list|)
expr_stmt|;
block|}
comment|// check result
name|ValidationResult
name|valRes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|allErrors
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|valRes
operator|=
operator|new
name|ValidationResult
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there are some errors
name|valRes
operator|=
operator|new
name|ValidationResult
argument_list|(
name|allErrors
argument_list|)
expr_stmt|;
block|}
comment|// addition of the some objects to avoid a second file parsing
name|valRes
operator|.
name|setPdf
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|valRes
operator|.
name|setXmpMetaData
argument_list|(
name|handler
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|valRes
return|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
comment|// ---- Close all open resources if an error occurs.
name|handler
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

