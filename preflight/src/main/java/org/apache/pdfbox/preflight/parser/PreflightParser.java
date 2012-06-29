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
name|parser
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|DataSource
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
name|io
operator|.
name|RandomAccess
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
name|pdfparser
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
name|PreflightConstants
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
name|PdfParseException
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
name|pdfbox
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
name|pdfbox
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

begin_class
specifier|public
class|class
name|PreflightParser
extends|extends
name|PDFParser
block|{
comment|/** 	 * Define a one byte encoding that hasn't specific encoding in UTF-8 charset. 	 * Avoid unexpected error when the encoding is Cp5816 	 */
specifier|public
specifier|static
specifier|final
name|Charset
name|encoding
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
specifier|protected
name|DataSource
name|originalDocument
decl_stmt|;
specifier|protected
name|ValidationResult
name|validationResult
decl_stmt|;
specifier|protected
name|PreflightDocument
name|document
decl_stmt|;
specifier|protected
name|PreflightContext
name|ctx
decl_stmt|;
specifier|public
name|PreflightParser
parameter_list|(
name|DataSource
name|input
parameter_list|,
name|RandomAccess
name|rafi
parameter_list|,
name|boolean
name|force
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|rafi
argument_list|,
name|force
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalDocument
operator|=
name|input
expr_stmt|;
block|}
specifier|public
name|PreflightParser
parameter_list|(
name|DataSource
name|input
parameter_list|,
name|RandomAccess
name|rafi
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|rafi
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalDocument
operator|=
name|input
expr_stmt|;
block|}
specifier|public
name|PreflightParser
parameter_list|(
name|DataSource
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalDocument
operator|=
name|input
expr_stmt|;
block|}
comment|/** 	 * Create an instance of ValidationResult. This object contains an instance of 	 * ValidationError. If the ParseException is an instance of PdfParseException, 	 * the embedded validation error is initialized with the error code of the 	 * exception, otherwise it is an UnknownError. 	 *  	 * @param e 	 * @return 	 */
specifier|protected
specifier|static
name|ValidationResult
name|createErrorResult
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|PdfParseException
condition|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ValidationResult
argument_list|(
operator|new
name|ValidationError
argument_list|(
operator|(
operator|(
name|PdfParseException
operator|)
name|e
operator|)
operator|.
name|getErrorCode
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ValidationResult
argument_list|(
operator|new
name|ValidationError
argument_list|(
operator|(
operator|(
name|PdfParseException
operator|)
name|e
operator|)
operator|.
name|getErrorCode
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ValidationResult
argument_list|(
operator|new
name|ValidationError
argument_list|(
operator|(
operator|(
name|PdfParseException
operator|)
name|e
operator|)
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|createUnknownErrorResult
argument_list|()
return|;
block|}
comment|/** 	 * Create an instance of ValidationResult with a 	 * ValidationError(UNKNOWN_ERROR) 	 *  	 * @return 	 */
specifier|protected
specifier|static
name|ValidationResult
name|createUnknownErrorResult
parameter_list|()
block|{
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_UNKOWN_ERROR
argument_list|)
decl_stmt|;
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|(
name|error
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
comment|/** 	 * Add the error to the ValidationResult. 	 * If the validationResult is null, an instance is created using the isWarning boolean of the  	 * ValidationError to know if the ValidationResult must be flagged as Valid. 	 * @param error 	 */
specifier|protected
name|void
name|addValidationError
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|validationResult
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|validationResult
operator|=
operator|new
name|ValidationResult
argument_list|(
name|error
operator|.
name|isWarning
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|validationResult
operator|.
name|addError
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addValidationErrors
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
for|for
control|(
name|ValidationError
name|error
range|:
name|errors
control|)
block|{
name|addValidationError
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
name|parse
argument_list|(
name|Format
operator|.
name|PDF_A1B
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Parse the given file and check if it is a confirming file according to the given format. 	 *  	 * @param format format that the document should follow (default {@link Format#PDF_A1B}) 	 * @throws IOException 	 */
specifier|public
name|void
name|parse
parameter_list|(
name|Format
name|format
parameter_list|)
throws|throws
name|IOException
block|{
name|parse
argument_list|(
name|format
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Parse the given file and check if it is a confirming file according to the given format. 	 *  	 * @param format format that the document should follow (default {@link Format#PDF_A1B}) 	 * @param config Configuration bean that will be used by the PreflightDocument.  	 * If null the format is used to determine the default configuration.  	 * @throws IOException 	 */
specifier|public
name|void
name|parse
parameter_list|(
name|Format
name|format
parameter_list|,
name|PreflightConfiguration
name|config
parameter_list|)
throws|throws
name|IOException
block|{
name|checkFileSyntax
argument_list|()
expr_stmt|;
comment|// run PDFBox Parser
name|super
operator|.
name|parse
argument_list|()
expr_stmt|;
name|Format
name|formatToUse
init|=
operator|(
name|format
operator|==
literal|null
condition|?
name|Format
operator|.
name|PDF_A1B
else|:
name|format
operator|)
decl_stmt|;
name|createPdfADocument
argument_list|(
name|formatToUse
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|createContext
argument_list|()
expr_stmt|;
name|extractTrailers
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Run the JavaCC parser to check the PDF syntax. 	 * @throws ValidationException 	 */
specifier|protected
name|void
name|checkFileSyntax
parameter_list|()
throws|throws
name|ValidationException
block|{
comment|// syntax (javacc) validation
try|try
block|{
name|InputStreamReader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|this
operator|.
name|originalDocument
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|javacc
operator|.
name|PDFParser
name|javaCCParser
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|javacc
operator|.
name|PDFParser
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|javaCCParser
operator|.
name|PDF
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|reader
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
name|this
operator|.
name|validationResult
operator|=
name|createErrorResult
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|createPdfADocument
parameter_list|(
name|Format
name|format
parameter_list|,
name|PreflightConfiguration
name|config
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|document
operator|=
operator|new
name|PreflightDocument
argument_list|(
name|getDocument
argument_list|()
argument_list|,
name|format
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createContext
parameter_list|()
block|{
name|this
operator|.
name|ctx
operator|=
operator|new
name|PreflightContext
argument_list|(
name|this
operator|.
name|originalDocument
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|setDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|.
name|setContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|extractTrailers
parameter_list|()
throws|throws
name|IOException
block|{
name|SimpleCharStream
name|scs
init|=
operator|new
name|SimpleCharStream
argument_list|(
name|this
operator|.
name|originalDocument
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
name|ctx
operator|.
name|setPdfExtractor
argument_list|(
name|extractor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDDocument
name|getPDDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|.
name|setResult
argument_list|(
name|validationResult
argument_list|)
expr_stmt|;
comment|// Add XMP MetaData
return|return
name|document
return|;
block|}
block|}
end_class

end_unit

