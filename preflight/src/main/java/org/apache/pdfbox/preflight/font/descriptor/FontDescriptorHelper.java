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
name|HashSet
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
name|Set
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
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|MANDATORYFIELDS
decl_stmt|;
static|static
block|{
name|MANDATORYFIELDS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_FLAGS
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_ITALICANGLE
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_CAPHEIGHT
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_FONTBBOX
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_ASCENT
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_DESCENT
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|FONT_DICTIONARY_KEY_STEMV
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|FONT_NAME
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|MANDATORYFIELDS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|isStandard14
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|font
operator|instanceof
name|PDFont
condition|)
block|{
name|isStandard14
operator|=
operator|(
operator|(
name|PDFont
operator|)
name|font
operator|)
operator|.
name|isStandard14
argument_list|()
expr_stmt|;
block|}
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
operator|!
name|isStandard14
condition|)
block|{
name|checkMandatoryFields
argument_list|(
name|fontDescriptor
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": FontFile entry is missing from FontDescriptor"
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
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|+
literal|": They is more than one FontFile"
argument_list|)
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": FontDescriptor is null or is an AFM Descriptor"
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
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|StringBuilder
name|missingFields
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|field
range|:
name|MANDATORYFIELDS
control|)
block|{
if|if
condition|(
operator|!
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|field
argument_list|)
condition|)
block|{
if|if
condition|(
name|missingFields
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|missingFields
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|missingFields
operator|.
name|append
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|fDescriptor
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
condition|)
block|{
name|COSBase
name|type
init|=
name|fDescriptor
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSName
operator|.
name|FONT_DESC
operator|.
name|equals
argument_list|(
name|type
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
name|ERROR_FONTS_DESCRIPTOR_INVALID
argument_list|,
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": /Type in FontDescriptor must be /FontDescriptor, but is "
operator|+
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|missingFields
operator|.
name|length
argument_list|()
operator|>
literal|0
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
name|result
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|result
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
try|try
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
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Filter specified in font file metadata dictionnary"
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
argument_list|<>
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
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Unable to parse font metadata due to : "
operator|+
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
argument_list|,
name|e
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
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The Metadata entry doesn't reference a stream object"
argument_list|,
name|e
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
try|try
init|(
name|InputStream
name|metaDataContent
init|=
name|metadata
operator|.
name|createInputStream
argument_list|()
init|)
block|{
return|return
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|metaDataContent
argument_list|)
return|;
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
name|this
operator|.
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Unable to read font metadata due to : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
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
operator|!=
literal|null
operator|&&
name|fontName
operator|.
name|matches
argument_list|(
literal|"^[A-Z]{6}\\+.*"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

