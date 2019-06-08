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
name|process
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|cos
operator|.
name|COSStream
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
name|PDDocumentCatalog
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
name|PDMetadata
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
name|metadata
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
name|pdfbox
operator|.
name|preflight
operator|.
name|metadata
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
name|pdfbox
operator|.
name|preflight
operator|.
name|metadata
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
name|pdfbox
operator|.
name|preflight
operator|.
name|metadata
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
name|pdfbox
operator|.
name|preflight
operator|.
name|metadata
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
name|pdfbox
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
name|pdfbox
operator|.
name|util
operator|.
name|Hex
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
name|BadFieldValueException
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
name|ThumbnailType
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
name|xml
operator|.
name|DomXmpParser
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
name|xml
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
name|xmpbox
operator|.
name|xml
operator|.
name|XmpParsingException
operator|.
name|ErrorType
import|;
end_import

begin_class
specifier|public
class|class
name|MetadataValidationProcess
extends|extends
name|AbstractProcess
block|{
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|PDDocument
name|document
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|getXpacket
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|DomXmpParser
name|builder
init|=
operator|new
name|DomXmpParser
argument_list|()
decl_stmt|;
name|XMPMetadata
name|metadata
init|=
name|builder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|setMetadata
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
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
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
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
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_XPACKET_DEPRECATED
argument_list|,
literal|"encoding attribute is forbidden"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkThumbnails
argument_list|(
name|ctx
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Call metadata synchronization checking
name|addValidationErrors
argument_list|(
name|ctx
argument_list|,
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
name|addValidationErrors
argument_list|(
name|ctx
argument_list|,
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
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_RDF_ABOUT_ATTRIBUTE_INEQUAL_VALUE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XpacketParsingException
name|e
parameter_list|)
block|{
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
name|addValidationError
argument_list|(
name|ctx
argument_list|,
name|e
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_MAIN
argument_list|,
literal|"Unexpected error"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XmpParsingException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|NoValueType
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_UNKNOWN_VALUETYPE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|RequiredProperty
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_PROPERTY_MISSING
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|InvalidPrefix
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_ABSENT_DESCRIPTION_SCHEMA
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|InvalidType
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_PROPERTY_UNKNOWN
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|XpacketBadEnd
condition|)
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
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|NoSchema
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_ABSENT_DESCRIPTION_SCHEMA
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|ErrorType
operator|.
name|InvalidPdfaSchema
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_WRONG_NS_URI
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// check thumbnails. See Bavaria Testsuite file PDFA_Conference_2009_nc.pdf for an example.
specifier|private
name|void
name|checkThumbnails
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|getXMPBasicSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|xmp
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|ThumbnailType
argument_list|>
name|tbProp
decl_stmt|;
try|try
block|{
name|tbProp
operator|=
name|xmp
operator|.
name|getThumbnailsProperty
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadFieldValueException
name|e
parameter_list|)
block|{
comment|// should not happen here because it would have happened in XmpParser already
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|tbProp
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ThumbnailType
name|tb
range|:
name|tbProp
control|)
block|{
name|checkThumbnail
argument_list|(
name|tb
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkThumbnail
parameter_list|(
name|ThumbnailType
name|tb
parameter_list|,
name|PreflightContext
name|ctx
parameter_list|)
block|{
name|byte
index|[]
name|binImage
decl_stmt|;
try|try
block|{
name|binImage
operator|=
name|Hex
operator|.
name|decodeBase64
argument_list|(
name|tb
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"xapGImg:image is not correct base64 encoding"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|hasJpegMagicNumber
argument_list|(
name|binImage
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"xapGImg:image decoded base64 content is not in JPEG format"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|BufferedImage
name|bim
decl_stmt|;
try|try
block|{
name|bim
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|binImage
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
literal|"JPEG"
operator|.
name|equals
argument_list|(
name|tb
operator|.
name|getFormat
argument_list|()
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"xapGImg:format must be 'JPEG'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bim
operator|.
name|getHeight
argument_list|()
operator|!=
name|tb
operator|.
name|getHeight
argument_list|()
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"xapGImg:height does not match the actual base64-encoded thumbnail image data"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bim
operator|.
name|getWidth
argument_list|()
operator|!=
name|tb
operator|.
name|getWidth
argument_list|()
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"xapGImg:witdh does not match the actual base64-encoded thumbnail image data"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasJpegMagicNumber
parameter_list|(
name|byte
index|[]
name|binImage
parameter_list|)
block|{
if|if
condition|(
name|binImage
operator|.
name|length
operator|<
literal|4
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
name|binImage
index|[
literal|0
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFF
operator|&&
name|binImage
index|[
literal|1
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xD8
operator|&&
name|binImage
index|[
name|binImage
operator|.
name|length
operator|-
literal|2
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xFF
operator|&&
name|binImage
index|[
name|binImage
operator|.
name|length
operator|-
literal|1
index|]
operator|==
operator|(
name|byte
operator|)
literal|0xD9
operator|)
return|;
block|}
comment|/**      * Return the xpacket from the dictionary's stream      */
specifier|private
specifier|static
name|InputStream
name|getXpacket
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
throws|,
name|XpacketParsingException
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDMetadata
name|metadata
init|=
name|catalog
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadata
operator|==
literal|null
condition|)
block|{
name|COSBase
name|metaObject
init|=
name|catalog
operator|.
name|getCOSObject
argument_list|()
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
operator|!
operator|(
name|metaObject
operator|instanceof
name|COSStream
operator|)
condition|)
block|{
comment|// the Metadata object isn't a stream
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_FORMAT
argument_list|,
literal|"Metadata is not a stream"
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
comment|// missing Metadata Key in catalog
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
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
if|if
condition|(
name|metadata
operator|.
name|getFilters
argument_list|()
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
name|PreflightConstants
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
return|return
name|metadata
operator|.
name|exportXMPMetadata
argument_list|()
return|;
block|}
comment|/**      * Check if metadata dictionary has no stream filter      *       * @param doc the document to check.      * @return the list of validation errors.      */
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
argument_list|<>
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
name|PreflightConstants
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

