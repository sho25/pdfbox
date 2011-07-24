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
name|font
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
name|font
operator|.
name|AbstractFontContainer
operator|.
name|State
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
name|pdmodel
operator|.
name|common
operator|.
name|PDStream
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
name|font
operator|.
name|PDFont
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
name|font
operator|.
name|PDFontDescriptor
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
name|font
operator|.
name|PDFontFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFontValidator
implements|implements
name|FontValidator
implements|,
name|ValidationConstants
block|{
comment|/**    * DocumentHandler which contains all useful objects to validate a PDF/A ex :    * parser JavaCC    */
specifier|protected
name|DocumentHandler
name|handler
init|=
literal|null
decl_stmt|;
comment|/**    * The COSObject which is the starting point of the Font description in the    * PDF/A file. This object should be an insteance of COSDictionary    */
specifier|protected
name|COSObject
name|cObj
init|=
literal|null
decl_stmt|;
comment|/**    * The cObj casted as COSDictionary    */
specifier|protected
name|COSDictionary
name|fDictionary
init|=
literal|null
decl_stmt|;
comment|/**    * The PdfBox font dictionary wrapper.    */
specifier|protected
name|PDFont
name|pFont
init|=
literal|null
decl_stmt|;
comment|/**    * The Font Container contains the Font Validation state ( valid or not,    * why...) This Font Container is tested when the font is used as resource.    * According to the state of this font, the PDF File will be PDF/A conforming    * file or not. (Ex : if the FontContainer flags this font as not embedded,    * the PDF is a PDF/A only if the font is used in a Rendering Mode 3.)    */
specifier|protected
name|AbstractFontContainer
name|fontContainer
init|=
literal|null
decl_stmt|;
comment|/**    * Abstract Constructor    * @param handler the handled document    * @param cObj The cos object representing the font    * @throws ValidationException when object creation fails    */
specifier|public
name|AbstractFontValidator
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|,
name|COSObject
name|cObj
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|cObj
operator|=
name|cObj
expr_stmt|;
name|this
operator|.
name|fDictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|cObj
operator|.
name|getObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|pFont
operator|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|fDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|fontContainer
operator|=
name|instanciateContainer
argument_list|(
name|this
operator|.
name|pFont
argument_list|)
expr_stmt|;
name|this
operator|.
name|handler
operator|.
name|addFont
argument_list|(
name|this
operator|.
name|pFont
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|this
operator|.
name|fontContainer
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
literal|"Unable to instantiate a FontValidator object : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|AbstractFontContainer
name|instanciateContainer
parameter_list|(
name|PDFont
name|fd
parameter_list|)
block|{
name|String
name|subtype
init|=
name|fd
operator|.
name|getSubType
argument_list|()
decl_stmt|;
if|if
condition|(
name|FONT_DICTIONARY_VALUE_TRUETYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|TrueTypeFontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|FONT_DICTIONARY_VALUE_MMTYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|Type1FontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|FONT_DICTIONARY_VALUE_TYPE1
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|Type1FontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|FONT_DICTIONARY_VALUE_TYPE3
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|Type3FontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|FONT_DICTIONARY_VALUE_COMPOSITE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|CompositeFontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|UndefFontContainer
argument_list|(
name|fd
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|String
name|subSetPattern
init|=
literal|"^[A-Z]{6}\\+.*"
decl_stmt|;
specifier|public
specifier|static
name|boolean
name|isSubSet
parameter_list|(
name|String
name|fontName
parameter_list|)
block|{
return|return
name|fontName
operator|.
name|matches
argument_list|(
name|subSetPattern
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getSubSetPatternDelimiter
parameter_list|()
block|{
return|return
literal|"+"
return|;
block|}
comment|/*    * (non-Javadoc)    *     * @see net.awl.edoc.pdfa.validation.font.FontValidator#getState()    */
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|this
operator|.
name|fontContainer
operator|.
name|isValid
argument_list|()
return|;
block|}
comment|/*    * (non-Javadoc)    *     * @see net.awl.edoc.pdfa.validation.font.FontValidator#getValdiationErrors()    */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getValdiationErrors
parameter_list|()
block|{
return|return
name|this
operator|.
name|fontContainer
operator|.
name|getErrors
argument_list|()
return|;
block|}
comment|/**    * Type0, Type1 and TrueType FontValidatir call this method to check the    * FontFile meta data.    *     * @param fontDesc    *          The FontDescriptor which contains the FontFile stream    * @param fontFile    *          The font file stream to check    * @return true if the meta data is valid, false otherwise    * @throws ValidationException when checking fails    */
specifier|protected
name|boolean
name|checkFontFileMetaData
parameter_list|(
name|PDFontDescriptor
name|fontDesc
parameter_list|,
name|PDStream
name|fontFile
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDMetadata
name|metadata
init|=
name|fontFile
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
comment|// --- Filters are forbidden in a XMP stream
if|if
condition|(
name|metadata
operator|.
name|getFilters
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|metadata
operator|.
name|getFilters
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fontContainer
operator|.
name|addError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
argument_list|,
literal|"Filter specified in font file metadata dictionnary"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// --- extract the meta data content
name|byte
index|[]
name|mdAsBytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|metaDataContent
init|=
name|metadata
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyLarge
argument_list|(
name|metaDataContent
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|metaDataContent
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|bos
argument_list|)
expr_stmt|;
name|mdAsBytes
operator|=
name|bos
operator|.
name|toByteArray
argument_list|()
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
literal|"Unable to read font metadata due to : "
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
try|try
block|{
name|XMPDocumentBuilder
name|xmpBuilder
init|=
operator|new
name|XMPDocumentBuilder
argument_list|()
decl_stmt|;
name|XMPMetadata
name|xmpMeta
init|=
name|xmpBuilder
operator|.
name|parse
argument_list|(
name|mdAsBytes
argument_list|)
decl_stmt|;
name|FontMetaDataValidation
name|fontMDval
init|=
operator|new
name|FontMetaDataValidation
argument_list|()
decl_stmt|;
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
name|boolean
name|isVal
init|=
name|fontMDval
operator|.
name|analyseFontName
argument_list|(
name|xmpMeta
argument_list|,
name|fontDesc
argument_list|,
name|ve
argument_list|)
decl_stmt|;
name|isVal
operator|=
name|isVal
operator|&&
name|fontMDval
operator|.
name|analyseFontName
argument_list|(
name|xmpMeta
argument_list|,
name|fontDesc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationError
name|validationError
range|:
name|ve
control|)
block|{
name|fontContainer
operator|.
name|addError
argument_list|(
name|validationError
argument_list|)
expr_stmt|;
block|}
return|return
name|isVal
return|;
block|}
catch|catch
parameter_list|(
name|XmpUnknownValueTypeException
name|e
parameter_list|)
block|{
name|fontContainer
operator|.
name|addError
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
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|XmpParsingException
name|e
parameter_list|)
block|{
name|fontContainer
operator|.
name|addError
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
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|XmpSchemaException
name|e
parameter_list|)
block|{
name|fontContainer
operator|.
name|addError
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
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|XmpExpectedRdfAboutAttribute
name|e
parameter_list|)
block|{
name|fontContainer
operator|.
name|addError
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
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|BadFieldValueException
name|e
parameter_list|)
block|{
name|fontContainer
operator|.
name|addError
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
literal|false
return|;
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
block|}
comment|// --- No MetaData, valid
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

