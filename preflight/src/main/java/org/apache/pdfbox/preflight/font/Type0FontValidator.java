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
name|ERROR_FONTS_CIDKEYED_CMAP_INVALID_OR_MISSING
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
name|ERROR_FONTS_CIDKEYED_INVALID
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
name|ERROR_FONTS_CIDKEYED_SYSINFO
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
name|ERROR_FONTS_CID_DAMAGED
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
name|ERROR_FONTS_CID_CMAP_DAMAGED
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
name|ERROR_FONTS_DICTIONARY_INVALID
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
name|FONT_DICTIONARY_DEFAULT_CMAP_WMODE
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
name|FONT_DICTIONARY_KEY_CMAP_NAME
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
name|FONT_DICTIONARY_KEY_CMAP_USECMAP
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
name|FONT_DICTIONARY_KEY_CMAP_WMODE
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
name|FONT_DICTIONARY_VALUE_CMAP_IDENTITY_H
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
name|FONT_DICTIONARY_VALUE_CMAP_IDENTITY_V
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
name|FONT_DICTIONARY_VALUE_TYPE0
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
name|FONT_DICTIONARY_VALUE_TYPE2
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
name|FONT_DICTIONARY_VALUE_TYPE_CMAP
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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cmap
operator|.
name|CMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cmap
operator|.
name|CMapParser
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
name|COSArray
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
name|font
operator|.
name|PDCIDFontType0
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
name|PDCIDFontType2
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
name|PDType0Font
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
name|container
operator|.
name|Type0Container
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

begin_class
specifier|public
class|class
name|Type0FontValidator
extends|extends
name|FontValidator
argument_list|<
name|Type0Container
argument_list|>
block|{
specifier|protected
name|PDFont
name|font
decl_stmt|;
specifier|protected
name|COSDocument
name|cosDocument
init|=
literal|null
decl_stmt|;
specifier|public
name|Type0FontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
operator|.
name|getCOSObject
argument_list|()
argument_list|,
operator|new
name|Type0Container
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
name|cosDocument
operator|=
name|this
operator|.
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
expr_stmt|;
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|checkMandatoryFields
argument_list|()
expr_stmt|;
name|processDescendantFont
argument_list|()
expr_stmt|;
name|checkEncoding
argument_list|()
expr_stmt|;
name|checkToUnicode
argument_list|()
expr_stmt|;
block|}
comment|/**      * This methods extracts from the Font dictionary all mandatory fields. If a mandatory field is missing, the list of      * ValidationError in the FontContainer is updated.      */
specifier|protected
name|void
name|checkMandatoryFields
parameter_list|()
block|{
name|COSDictionary
name|fontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|boolean
name|areFieldsPResent
init|=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|DESCENDANT_FONTS
argument_list|)
expr_stmt|;
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|areFieldsPResent
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Some keys are missing from composite font dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extract the single CIDFont from the Descendant array. Create a FontValidator for this CODFont and launch its      * validation.      */
specifier|protected
name|void
name|processDescendantFont
parameter_list|()
throws|throws
name|ValidationException
block|{
name|COSDictionary
name|fontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
comment|// a CIDFont is contained in the DescendantFonts array
name|COSArray
name|array
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DESCENDANT_FONTS
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
operator|||
name|array
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
comment|/*              * in PDF 1.4, this array must contain only one element, because of a PDF/A should be a PDF 1.4, this method              * returns an error if the array has more than one element.              */
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": CIDFont is missing from the DescendantFonts array or the size of array is greater than 1"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSDictionary
name|cidFont
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidFont
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The DescendantFonts array should have one element with is a dictionary."
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|FontValidator
argument_list|<
name|?
extends|extends
name|FontContainer
argument_list|>
name|cidFontValidator
init|=
name|createDescendantValidator
argument_list|(
name|cidFont
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidFontValidator
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|setDelegateFontContainer
argument_list|(
name|cidFontValidator
operator|.
name|getFontContainer
argument_list|()
argument_list|)
expr_stmt|;
name|cidFontValidator
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|FontValidator
argument_list|<
name|?
extends|extends
name|FontContainer
argument_list|>
name|createDescendantValidator
parameter_list|(
name|COSDictionary
name|cidFont
parameter_list|)
block|{
name|String
name|subtype
init|=
name|cidFont
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
name|FontValidator
argument_list|<
name|?
extends|extends
name|FontContainer
argument_list|>
name|cidFontValidator
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|FONT_DICTIONARY_VALUE_TYPE0
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|cidFontValidator
operator|=
name|createCIDType0FontValidator
argument_list|(
name|cidFont
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|FONT_DICTIONARY_VALUE_TYPE2
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|cidFontValidator
operator|=
name|createCIDType2FontValidator
argument_list|(
name|cidFont
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Type and/or Subtype keys are missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cidFontValidator
return|;
block|}
comment|/**      * Create the validation object for CIDType0 Font      */
specifier|protected
name|FontValidator
argument_list|<
name|?
extends|extends
name|FontContainer
argument_list|>
name|createCIDType0FontValidator
parameter_list|(
name|COSDictionary
name|fDict
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|CIDType0FontValidator
argument_list|(
name|context
argument_list|,
operator|new
name|PDCIDFontType0
argument_list|(
name|fDict
argument_list|,
operator|(
name|PDType0Font
operator|)
name|font
argument_list|)
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
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CID_DAMAGED
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CIDType0 font is damaged"
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
comment|/**      * Create the validation object for CIDType2 Font      */
specifier|protected
name|FontValidator
argument_list|<
name|?
extends|extends
name|FontContainer
argument_list|>
name|createCIDType2FontValidator
parameter_list|(
name|COSDictionary
name|fDict
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|CIDType2FontValidator
argument_list|(
name|context
argument_list|,
operator|new
name|PDCIDFontType2
argument_list|(
name|fDict
argument_list|,
operator|(
name|PDType0Font
operator|)
name|font
argument_list|)
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
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CID_DAMAGED
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CIDType2 font is damaged"
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
comment|/**      * Check the CMap entry.      *       * The CMap entry must be a dictionary in a PDF/A. This entry can be a String only if the String value is Identity-H      * or Identity-V      */
specifier|protected
name|void
name|checkEncoding
parameter_list|()
block|{
name|COSBase
name|encoding
init|=
operator|(
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
operator|)
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|checkCMapEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|checkCMapEncoding
parameter_list|(
name|COSBase
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|COSUtils
operator|.
name|isString
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
comment|// if encoding is a string, only 2 values are allowed
name|String
name|str
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|FONT_DICTIONARY_VALUE_CMAP_IDENTITY_V
operator|.
name|equals
argument_list|(
name|str
argument_list|)
operator|||
name|FONT_DICTIONARY_VALUE_CMAP_IDENTITY_H
operator|.
name|equals
argument_list|(
name|str
argument_list|)
operator|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CMap is a string but it isn't an Identity-H/V"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
elseif|else
if|if
condition|(
name|COSUtils
operator|.
name|isStream
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
comment|/*              * If the CMap is a stream, some fields are mandatory and the CIDSytemInfo must be compared with the              * CIDSystemInfo entry of the CIDFont.              */
name|processCMapAsStream
argument_list|(
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|encoding
argument_list|,
name|cosDocument
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// CMap type is invalid
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CMAP_INVALID_OR_MISSING
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CMap type is invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Standard information of a stream element will be checked by the StreamValidationProcess.      *       * This method checks mandatory fields of the CMap stream. This method checks too if the CMap stream is damaged      * using the CMapParser of the fontbox api.      *       * @param aCMap      */
specifier|private
name|void
name|processCMapAsStream
parameter_list|(
name|COSStream
name|aCMap
parameter_list|)
block|{
name|COSBase
name|sysinfo
init|=
name|aCMap
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|)
decl_stmt|;
name|checkCIDSystemInfo
argument_list|(
name|sysinfo
argument_list|)
expr_stmt|;
try|try
block|{
comment|// extract information from the CMap stream
name|CMap
name|fontboxCMap
init|=
operator|new
name|CMapParser
argument_list|()
operator|.
name|parse
argument_list|(
name|aCMap
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|wmValue
init|=
name|fontboxCMap
operator|.
name|getWMode
argument_list|()
decl_stmt|;
name|String
name|cmnValue
init|=
name|fontboxCMap
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/*              * According to the getInt javadoc, -1 is returned if there are no result. In the PDF Reference v1.7 p449,              * we can read that Default value is 0.              */
name|int
name|wmode
init|=
name|aCMap
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|FONT_DICTIONARY_KEY_CMAP_WMODE
argument_list|)
argument_list|,
name|FONT_DICTIONARY_DEFAULT_CMAP_WMODE
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|aCMap
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|String
name|cmapName
init|=
name|aCMap
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|FONT_DICTIONARY_KEY_CMAP_NAME
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmapName
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|cmapName
argument_list|)
operator|||
name|wmode
operator|>
literal|1
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CMAP_INVALID_OR_MISSING
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Some elements in the CMap dictionary are missing or invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|wmValue
operator|==
name|wmode
operator|&&
name|cmapName
operator|.
name|equals
argument_list|(
name|cmnValue
argument_list|)
operator|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CMAP_INVALID_OR_MISSING
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": CMapName or WMode is inconsistent"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|FONT_DICTIONARY_VALUE_TYPE_CMAP
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CMAP_INVALID_OR_MISSING
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CMap type is invalid"
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
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CID_CMAP_DAMAGED
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CMap type is damaged"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|cmapUsed
init|=
operator|(
name|COSDictionary
operator|)
name|aCMap
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|FONT_DICTIONARY_KEY_CMAP_USECMAP
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmapUsed
operator|!=
literal|null
condition|)
block|{
name|checkCMapEncoding
argument_list|(
name|cmapUsed
argument_list|)
expr_stmt|;
block|}
name|compareCIDSystemInfo
argument_list|(
name|aCMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the content of the CIDSystemInfo dictionary. A CIDSystemInfo dictionary must contain :      *<UL>      *<li>a Name - Registry      *<li>a Name - Ordering      *<li>a Integer - Supplement      *</UL>      *       * @param sysinfo      * @return the validation result.      */
specifier|protected
name|boolean
name|checkCIDSystemInfo
parameter_list|(
name|COSBase
name|sysinfo
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|COSDictionary
name|cidSysInfo
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|sysinfo
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidSysInfo
operator|!=
literal|null
condition|)
block|{
name|COSBase
name|reg
init|=
name|cidSysInfo
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|)
decl_stmt|;
name|COSBase
name|ord
init|=
name|cidSysInfo
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|)
decl_stmt|;
name|COSBase
name|sup
init|=
name|cidSysInfo
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|SUPPLEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|COSUtils
operator|.
name|isString
argument_list|(
name|reg
argument_list|,
name|cosDocument
argument_list|)
operator|&&
name|COSUtils
operator|.
name|isString
argument_list|(
name|ord
argument_list|,
name|cosDocument
argument_list|)
operator|&&
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|sup
argument_list|,
name|cosDocument
argument_list|)
operator|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_SYSINFO
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_SYSINFO
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
comment|/**      * The CIDSystemInfo must have the same Registry and Ordering for CMap and CIDFont. This control is useless if CMap      * is Identity-H or Identity-V so this method is called by the checkCMap method.      *       * @param cmap      */
specifier|private
name|void
name|compareCIDSystemInfo
parameter_list|(
name|COSDictionary
name|cmap
parameter_list|)
block|{
name|COSDictionary
name|fontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSArray
name|array
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DESCENDANT_FONTS
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
operator|&&
name|array
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|COSDictionary
name|cidFont
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|COSDictionary
name|cmsi
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|cmap
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|COSDictionary
name|cfsi
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|cidFont
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|String
name|regCM
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|cmsi
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|String
name|ordCM
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|cmsi
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|String
name|regCF
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|cfsi
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|String
name|ordCF
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|cfsi
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|regCF
operator|.
name|equals
argument_list|(
name|regCM
argument_list|)
operator|||
operator|!
name|ordCF
operator|.
name|equals
argument_list|(
name|ordCM
argument_list|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_SYSINFO
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CIDSystemInfo is inconsistent"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

