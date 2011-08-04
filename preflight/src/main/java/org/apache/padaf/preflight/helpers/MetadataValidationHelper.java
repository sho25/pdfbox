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
operator|.
name|helpers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|IOUtils
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
name|DocumentHandler
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
name|ValidationConstants
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
name|ValidationException
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
name|ValidatorConfig
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
name|utils
operator|.
name|COSUtils
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
name|xmp
operator|.
name|PDFAIdentificationValidation
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
name|xmp
operator|.
name|RDFAboutAttributeConcordanceValidation
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
name|xmp
operator|.
name|RDFAboutAttributeConcordanceValidation
operator|.
name|DifferentRDFAboutException
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
name|xmp
operator|.
name|SynchronizedMetaDataValidation
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
name|xmp
operator|.
name|XpacketParsingException
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
name|xmp
operator|.
name|RDFAboutAttributeConcordanceValidation
operator|.
name|DifferentRDFAboutException
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
name|padaf
operator|.
name|xmpbox
operator|.
name|parser
operator|.
name|PDFAExtentionSchemaPreprocessor
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
name|xmpbox
operator|.
name|parser
operator|.
name|XMPDocumentBuilder
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpExpectedRdfAboutAttribute
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpParsingException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpPropertyFormatException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpRequiredPropertyException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpSchemaException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpUnexpectedNamespacePrefixException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpUnexpectedNamespaceURIException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpUnknownPropertyException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpUnknownSchemaException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpUnknownValueTypeException
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
name|xmpbox
operator|.
name|parser
operator|.
name|XmpXpacketEndException
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
name|xmpbox
operator|.
name|type
operator|.
name|BadFieldValueException
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
name|COSBase
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
name|COSDocument
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
name|cos
operator|.
name|COSObject
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
name|common
operator|.
name|PDStream
import|;
end_import

begin_class
specifier|public
class|class
name|MetadataValidationHelper
extends|extends
name|AbstractValidationHelper
block|{
specifier|public
name|MetadataValidationHelper
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
comment|/**    * Return the xpacket from the dictionary's stream    */
specifier|public
specifier|static
name|byte
index|[]
name|getXpacket
parameter_list|(
name|COSDocument
name|cdocument
parameter_list|)
throws|throws
name|IOException
throws|,
name|XpacketParsingException
block|{
name|COSObject
name|catalog
init|=
name|cdocument
operator|.
name|getCatalog
argument_list|()
decl_stmt|;
name|COSBase
name|cb
init|=
name|catalog
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|cb
operator|==
literal|null
condition|)
block|{
comment|// missing Metadata Key in catalog
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"Missing Metadata Key in catalog"
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|XpacketParsingException
argument_list|(
literal|"Failed while retrieving xpacket"
argument_list|,
name|error
argument_list|)
throw|;
block|}
comment|// no filter key
name|COSDictionary
name|metadataDictionnary
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|cb
argument_list|,
name|cdocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadataDictionnary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// should not be defined
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
argument_list|,
literal|"Filter specified in metadata dictionnary"
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|XpacketParsingException
argument_list|(
literal|"Failed while retrieving xpacket"
argument_list|,
name|error
argument_list|)
throw|;
block|}
name|PDStream
name|stream
init|=
name|PDStream
operator|.
name|createFromCOS
argument_list|(
name|metadataDictionnary
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|stream
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|innerValidate
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|PDDocument
name|document
init|=
name|handler
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|byte
index|[]
name|tmp
init|=
name|getXpacket
argument_list|(
name|document
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|XMPDocumentBuilder
name|builder
decl_stmt|;
try|try
block|{
name|builder
operator|=
operator|new
name|XMPDocumentBuilder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addPreprocessor
argument_list|(
operator|new
name|PDFAExtentionSchemaPreprocessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmpSchemaException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e1
argument_list|)
throw|;
block|}
name|XMPMetadata
name|metadata
decl_stmt|;
try|try
block|{
name|metadata
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setMetadata
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XmpSchemaException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Parser: Internal Problem (failed to instanciate Schema object)"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XmpXpacketEndException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to parse font metadata due to : "
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
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
comment|// 6.7.5 no deprecated attribute in xpacket processing instruction
if|if
condition|(
name|metadata
operator|.
name|getXpacketBytes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_XPACKET_DEPRECATED
argument_list|,
literal|"bytes attribute is forbidden"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metadata
operator|.
name|getXpacketEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_XPACKET_DEPRECATED
argument_list|,
literal|"encoding attribute is forbidden"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Call metadata synchronization checking
name|lve
operator|.
name|addAll
argument_list|(
operator|new
name|SynchronizedMetaDataValidation
argument_list|()
operator|.
name|validateMetadataSynchronization
argument_list|(
name|document
argument_list|,
name|metadata
argument_list|)
argument_list|)
expr_stmt|;
comment|// Call PDF/A Identifier checking
name|lve
operator|.
name|addAll
argument_list|(
operator|new
name|PDFAIdentificationValidation
argument_list|()
operator|.
name|validatePDFAIdentifer
argument_list|(
name|metadata
argument_list|)
argument_list|)
expr_stmt|;
comment|// Call rdf:about checking
try|try
block|{
operator|new
name|RDFAboutAttributeConcordanceValidation
argument_list|()
operator|.
name|validateRDFAboutAttributes
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DifferentRDFAboutException
name|e
parameter_list|)
block|{
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_RDF_ABOUT_ATTRIBUTE_INEQUAL_VALUE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XpacketParsingException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getError
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|lve
operator|.
name|add
argument_list|(
name|e
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_MAIN
argument_list|,
literal|"Unexpected error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpPropertyFormatException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_PROPERTY_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|BadFieldValueException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_CATEGORY_PROPERTY_INVALID
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpExpectedRdfAboutAttribute
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_RDF_ABOUT_ATTRIBUTE_MISSING
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnknownPropertyException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_PROPERTY_UNKNOWN
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnknownSchemaException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_ABSENT_DESCRIPTION_SCHEMA
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnexpectedNamespaceURIException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_WRONG_NS_URI
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnexpectedNamespacePrefixException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_ABSENT_DESCRIPTION_SCHEMA
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpRequiredPropertyException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_PROPERTY_MISSING
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnknownValueTypeException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_UNKNOWN_VALUETYPE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
block|}
catch|catch
parameter_list|(
name|XmpParsingException
name|e
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lve
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
name|lve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|lve
return|;
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
literal|"Failed while validating"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Check if metadata dictionary has no stream filter    *     * @param doc    * @return    */
specifier|protected
name|List
argument_list|<
name|ValidationError
argument_list|>
name|checkStreamFilterUsage
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
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
name|List
argument_list|<
name|?
argument_list|>
name|filters
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
operator|!
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_METADATA_MAIN
argument_list|,
literal|"Using stream filter on metadata dictionary is forbidden"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ve
return|;
block|}
block|}
end_class

end_unit

