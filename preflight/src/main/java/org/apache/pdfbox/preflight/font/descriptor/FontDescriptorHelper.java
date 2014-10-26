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
name|font
operator|.
name|descriptor
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
name|ERROR_FONTS_DESCRIPTOR_INVALID
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
name|ERROR_FONTS_FONT_FILEX_INVALID
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
name|ERROR_METADATA_FORMAT
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
name|ERROR_METADATA_FORMAT_STREAM
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
name|ERROR_METADATA_FORMAT_UNKOWN
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
name|ERROR_METADATA_FORMAT_XPACKET
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
name|ERROR_METADATA_UNKNOWN_VALUETYPE
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
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
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
name|FONT_DICTIONARY_KEY_ASCENT
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
name|FONT_DICTIONARY_KEY_CAPHEIGHT
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
name|FONT_DICTIONARY_KEY_DESCENT
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
name|FONT_DICTIONARY_KEY_FLAGS
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
name|FONT_DICTIONARY_KEY_FONTBBOX
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
name|FONT_DICTIONARY_KEY_FONTNAME
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
name|FONT_DICTIONARY_KEY_ITALICANGLE
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
name|FONT_DICTIONARY_KEY_STEMV
import|;
end_import

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
name|PDFontLike
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
name|font
operator|.
name|container
operator|.
name|FontContainer
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
name|font
operator|.
name|util
operator|.
name|FontMetaDataValidation
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
specifier|abstract
class|class
name|FontDescriptorHelper
parameter_list|<
name|T
extends|extends
name|FontContainer
parameter_list|>
block|{
specifier|protected
name|T
name|fContainer
decl_stmt|;
specifier|protected
name|PreflightContext
name|context
decl_stmt|;
specifier|protected
name|PDFontLike
name|font
decl_stmt|;
specifier|protected
name|PDFontDescriptor
name|fontDescriptor
decl_stmt|;
specifier|public
name|FontDescriptorHelper
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFontLike
name|font
parameter_list|,
name|T
name|fontContainer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|fContainer
operator|=
name|fontContainer
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|PDFontDescriptor
name|fd
init|=
name|this
operator|.
name|font
operator|.
name|getFontDescriptor
argument_list|()
decl_stmt|;
comment|// Only a PDFontDescriptorDictionary provides a way to embedded the font program.
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|fontDescriptor
operator|=
name|fd
expr_stmt|;
if|if
condition|(
name|checkMandatoryFields
argument_list|(
name|fontDescriptor
operator|.
name|getCOSObject
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|hasOnlyOneFontFile
argument_list|(
name|fontDescriptor
argument_list|)
condition|)
block|{
name|PDStream
name|fontFile
init|=
name|extractFontFile
argument_list|(
name|fontDescriptor
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontFile
operator|!=
literal|null
condition|)
block|{
name|processFontFile
argument_list|(
name|fontDescriptor
argument_list|,
name|fontFile
argument_list|)
expr_stmt|;
name|checkFontFileMetaData
argument_list|(
name|fontDescriptor
argument_list|,
name|fontFile
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|fontFileNotEmbedded
argument_list|(
name|fontDescriptor
argument_list|)
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
literal|"FontFile entry is missing from FontDescriptor for "
operator|+
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|fContainer
operator|.
name|notEmbedded
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
literal|"They are more than one FontFile for "
operator|+
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DESCRIPTOR_INVALID
argument_list|,
literal|"FontDescriptor is null or is a AFM Descriptor"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|fContainer
operator|.
name|notEmbedded
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|checkMandatoryFields
parameter_list|(
name|COSDictionary
name|fDescriptor
parameter_list|)
block|{
name|String
name|missingFields
init|=
literal|""
decl_stmt|;
name|boolean
name|areFieldsPresent
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_FONTNAME
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|areFieldsPresent
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_FONTNAME
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|flags
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_FLAGS
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|flags
expr_stmt|;
if|if
condition|(
operator|!
name|flags
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_FLAGS
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|italicAngle
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_ITALICANGLE
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|italicAngle
expr_stmt|;
if|if
condition|(
operator|!
name|italicAngle
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_ITALICANGLE
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|capHeight
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_CAPHEIGHT
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|capHeight
expr_stmt|;
if|if
condition|(
operator|!
name|capHeight
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_CAPHEIGHT
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|fontBox
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_FONTBBOX
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|fontBox
expr_stmt|;
if|if
condition|(
operator|!
name|fontBox
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_FONTBBOX
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|ascent
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_ASCENT
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|ascent
expr_stmt|;
if|if
condition|(
operator|!
name|ascent
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_ASCENT
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|descent
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_DESCENT
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|descent
expr_stmt|;
if|if
condition|(
operator|!
name|descent
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_DESCENT
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|stemV
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|FONT_DICTIONARY_KEY_STEMV
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|stemV
expr_stmt|;
if|if
condition|(
operator|!
name|stemV
condition|)
block|{
name|missingFields
operator|+=
name|FONT_DICTIONARY_KEY_STEMV
operator|+
literal|", "
expr_stmt|;
block|}
name|boolean
name|name
init|=
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FONT_NAME
argument_list|)
decl_stmt|;
name|areFieldsPresent
operator|&=
name|name
expr_stmt|;
if|if
condition|(
operator|!
name|name
condition|)
block|{
name|missingFields
operator|+=
name|COSName
operator|.
name|FONT_NAME
operator|+
literal|", "
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|areFieldsPresent
condition|)
block|{
if|if
condition|(
name|missingFields
operator|.
name|endsWith
argument_list|(
literal|", "
argument_list|)
condition|)
block|{
name|missingFields
operator|=
name|missingFields
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|missingFields
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DESCRIPTOR_INVALID
argument_list|,
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": some mandatory fields are missing from the FontDescriptor: "
operator|+
name|missingFields
operator|+
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|areFieldsPresent
return|;
block|}
specifier|public
specifier|abstract
name|PDStream
name|extractFontFile
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
function_decl|;
comment|/**      * Return true if the FontDescriptor has only one FontFile entry.      *       * @param fontDescriptor      * @return true if the FontDescriptor has only one FontFile entry.      */
specifier|protected
name|boolean
name|hasOnlyOneFontFile
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|PDStream
name|ff1
init|=
name|fontDescriptor
operator|.
name|getFontFile
argument_list|()
decl_stmt|;
name|PDStream
name|ff2
init|=
name|fontDescriptor
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
name|PDStream
name|ff3
init|=
name|fontDescriptor
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
return|return
operator|(
name|ff1
operator|!=
literal|null
operator|^
name|ff2
operator|!=
literal|null
operator|^
name|ff3
operator|!=
literal|null
operator|)
return|;
block|}
specifier|protected
name|boolean
name|fontFileNotEmbedded
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|PDStream
name|ff1
init|=
name|fontDescriptor
operator|.
name|getFontFile
argument_list|()
decl_stmt|;
name|PDStream
name|ff2
init|=
name|fontDescriptor
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
name|PDStream
name|ff3
init|=
name|fontDescriptor
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
return|return
operator|(
name|ff1
operator|==
literal|null
operator|&&
name|ff2
operator|==
literal|null
operator|&&
name|ff3
operator|==
literal|null
operator|)
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|processFontFile
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|,
name|PDStream
name|fontFile
parameter_list|)
function_decl|;
comment|/**      * Type0, Type1 and TrueType FontValidator call this method to check the FontFile meta data.      *       * @param fontDescriptor      *            The FontDescriptor which contains the FontFile stream      * @param fontFile      *            The font file stream to check      */
specifier|protected
name|void
name|checkFontFileMetaData
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|,
name|PDStream
name|fontFile
parameter_list|)
block|{
name|PDMetadata
name|metadata
init|=
literal|null
decl_stmt|;
try|try
block|{
name|metadata
operator|=
name|fontFile
operator|.
name|getMetadata
argument_list|()
expr_stmt|;
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
comment|// Filters are forbidden in a XMP stream
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
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
argument_list|,
literal|"Filter specified in font file metadata dictionnary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|byte
index|[]
name|mdAsBytes
init|=
name|getMetaDataStreamAsBytes
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
try|try
block|{
name|DomXmpParser
name|xmpBuilder
init|=
operator|new
name|DomXmpParser
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
name|fontMDval
operator|.
name|analyseFontName
argument_list|(
name|xmpMeta
argument_list|,
name|fontDescriptor
argument_list|,
name|ve
argument_list|)
expr_stmt|;
name|fontMDval
operator|.
name|analyseRights
argument_list|(
name|xmpMeta
argument_list|,
name|fontDescriptor
argument_list|,
name|ve
argument_list|)
expr_stmt|;
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
name|ve
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_UNKNOWN_VALUETYPE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
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
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_FORMAT_XPACKET
argument_list|,
literal|"Unable to parse font metadata due to : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_FORMAT
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_FORMAT_UNKOWN
argument_list|,
literal|"The Metadata entry doesn't reference a stream object"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
name|byte
index|[]
name|getMetaDataStreamAsBytes
parameter_list|(
name|PDMetadata
name|metadata
parameter_list|)
block|{
name|byte
index|[]
name|result
init|=
literal|null
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
literal|null
decl_stmt|;
name|InputStream
name|metaDataContent
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|metaDataContent
operator|=
name|metadata
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|copyLarge
argument_list|(
name|metaDataContent
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|result
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
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_METADATA_FORMAT_STREAM
argument_list|,
literal|"Unable to read font metadata due to : "
operator|+
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
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

