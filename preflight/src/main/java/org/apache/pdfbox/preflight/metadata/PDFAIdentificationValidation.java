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
name|metadata
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_METADATA_INVALID_PDFA_CONFORMANCE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_METADATA_INVALID_PDFA_VERSION_ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_METADATA_PDFA_ID_MISSING
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_METADATA_WRONG_NS_PREFIX
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
name|List
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
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|PDFAIdentificationSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|StructuredType
import|;
end_import

begin_comment
comment|/**  * Class which check if PDF/A Identification Schema contains good information  *   * @author Germain Costenobel  *   */
end_comment

begin_class
specifier|public
class|class
name|PDFAIdentificationValidation
block|{
comment|/**      * Check if PDFAIdentification is valid      *       * @param metadata the XMP MetaData.      * @return the list of validation errors.      * @throws ValidationException      */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validatePDFAIdentifer
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|PDFAIdentificationSchema
name|id
init|=
name|metadata
operator|.
name|getPDFIdentificationSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_PDFA_ID_MISSING
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ve
return|;
block|}
comment|// According to the PDF/A specification, the prefix must be pdfaid for this schema.
name|StructuredType
name|stBasic
init|=
name|XMPBasicSchema
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
name|StructuredType
name|stPdfaIdent
init|=
name|PDFAIdentificationSchema
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|getPrefix
argument_list|()
operator|.
name|equals
argument_list|(
name|stPdfaIdent
operator|.
name|preferedPrefix
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|metadata
operator|.
name|getSchema
argument_list|(
name|stPdfaIdent
operator|.
name|preferedPrefix
argument_list|()
argument_list|,
name|stBasic
operator|.
name|namespace
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unexpectedPrefixFoundError
argument_list|(
name|id
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|stPdfaIdent
operator|.
name|preferedPrefix
argument_list|()
argument_list|,
name|PDFAIdentificationSchema
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|id
operator|=
operator|(
name|PDFAIdentificationSchema
operator|)
name|metadata
operator|.
name|getSchema
argument_list|(
name|stPdfaIdent
operator|.
name|preferedPrefix
argument_list|()
argument_list|,
name|stPdfaIdent
operator|.
name|namespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|checkConformanceLevel
argument_list|(
name|ve
argument_list|,
name|id
operator|.
name|getConformance
argument_list|()
argument_list|)
expr_stmt|;
name|checkPartNumber
argument_list|(
name|ve
argument_list|,
name|id
operator|.
name|getPart
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ve
return|;
block|}
comment|/**      * Return a validationError formatted when a schema has not the expected prefix      *       * @param prefFound      * @param prefExpected      * @param schema      * @return the validation error.      */
specifier|protected
name|ValidationError
name|unexpectedPrefixFoundError
parameter_list|(
name|String
name|prefFound
parameter_list|,
name|String
name|prefExpected
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|80
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|schema
argument_list|)
operator|.
name|append
argument_list|(
literal|" found but prefix used is '"
argument_list|)
operator|.
name|append
argument_list|(
name|prefFound
argument_list|)
operator|.
name|append
argument_list|(
literal|"', prefix '"
argument_list|)
operator|.
name|append
argument_list|(
name|prefExpected
argument_list|)
operator|.
name|append
argument_list|(
literal|"' is expected."
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_WRONG_NS_PREFIX
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|void
name|checkConformanceLevel
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|value
operator|.
name|equals
argument_list|(
literal|"A"
argument_list|)
operator|||
name|value
operator|.
name|equals
argument_list|(
literal|"B"
argument_list|)
operator|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_INVALID_PDFA_CONFORMANCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|checkPartNumber
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|,
name|int
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|1
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_INVALID_PDFA_VERSION_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

