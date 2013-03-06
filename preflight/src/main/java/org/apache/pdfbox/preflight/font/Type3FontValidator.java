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
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
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
name|ERROR_FONTS_ENCODING
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
name|ERROR_FONTS_METRICS
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
name|ERROR_FONTS_TYPE3_DAMAGED
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
name|encoding
operator|.
name|DictionaryEncoding
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
name|encoding
operator|.
name|Encoding
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
name|encoding
operator|.
name|EncodingManager
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
name|PDPage
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
name|PDResources
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
name|PDFontFactory
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
name|PreflightPath
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
name|content
operator|.
name|ContentStreamException
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
name|Type3Container
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
name|GlyphException
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
name|PDFAType3StreamParser
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
name|preflight
operator|.
name|utils
operator|.
name|ContextHelper
import|;
end_import

begin_class
specifier|public
class|class
name|Type3FontValidator
extends|extends
name|FontValidator
argument_list|<
name|Type3Container
argument_list|>
block|{
specifier|protected
name|COSDictionary
name|fontDictionary
decl_stmt|;
specifier|protected
name|COSDocument
name|cosDocument
decl_stmt|;
specifier|protected
name|Encoding
name|encoding
decl_stmt|;
specifier|public
name|Type3FontValidator
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
argument_list|,
operator|new
name|Type3Container
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|cosDocument
operator|=
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
name|fontDictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|checkMandatoryField
argument_list|()
expr_stmt|;
name|checkFontBBox
argument_list|()
expr_stmt|;
name|checkFontMatrix
argument_list|()
expr_stmt|;
name|checkEncoding
argument_list|()
expr_stmt|;
name|checkResources
argument_list|()
expr_stmt|;
name|checkCharProcsAndMetrics
argument_list|()
expr_stmt|;
name|checkToUnicode
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|checkMandatoryField
parameter_list|()
block|{
name|boolean
name|areFieldsPResent
init|=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FONT_BBOX
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
name|FONT_MATRIX
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
name|CHAR_PROCS
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
name|areFieldsPResent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
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
name|LAST_CHAR
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
name|WIDTHS
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
literal|"Some required fields are missing from the Font dictionary."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check that the FontBBox element has the right format as declared in the PDF reference document.      */
specifier|private
name|void
name|checkFontBBox
parameter_list|()
block|{
name|COSBase
name|fontBBox
init|=
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FONT_BBOX
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isArray
argument_list|(
name|fontBBox
argument_list|,
name|cosDocument
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"The FontBBox element isn't an array"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|/*          * check the content of the FontBBox. Should be an array with 4 numbers          */
name|COSArray
name|bbox
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|fontBBox
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|bbox
operator|.
name|size
argument_list|()
operator|!=
literal|4
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
literal|"The FontBBox element is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|elt
init|=
name|bbox
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|COSUtils
operator|.
name|isFloat
argument_list|(
name|elt
argument_list|,
name|cosDocument
argument_list|)
operator|||
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|elt
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"An element of FontBBox isn't a number"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
comment|/**      * Check that the FontMatrix element has the right format as declared in the PDF reference document.      */
specifier|private
name|void
name|checkFontMatrix
parameter_list|()
block|{
name|COSBase
name|fontMatrix
init|=
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FONT_MATRIX
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isArray
argument_list|(
name|fontMatrix
argument_list|,
name|cosDocument
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"The FontMatrix element isn't an array"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|/*          * Check the content of the FontMatrix. Should be an array with 6 numbers          */
name|COSArray
name|matrix
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|fontMatrix
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|matrix
operator|.
name|size
argument_list|()
operator|!=
literal|6
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
literal|"The FontMatrix element is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|elt
init|=
name|matrix
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|COSUtils
operator|.
name|isFloat
argument_list|(
name|elt
argument_list|,
name|cosDocument
argument_list|)
operator|||
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|elt
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"An element of FontMatrix isn't a number"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
annotation|@
name|Override
comment|/**      * For a Type3 font, the mapping between the Character Code and the Character      * name is entirely defined in the Encoding Entry. The Encoding Entry can be a      * Name (For the 5 predefined Encoding) or a Dictionary. If it is a      * dictionary, the "Differences" array contains the correspondence between a      * character code and a set of character name which are different from the      * encoding entry of the dictionary.      *       * This method checks that the encoding is :      *<UL>      *<li>An existing encoding name.      *<li>A dictionary with an existing encoding name (the name is optional) and      * a well formed "Differences" array (the array is optional)      *</UL>      *       * At the end of this method, if the validation succeed the Font encoding is kept in the {@link #encoding} attribute      * @return      */
specifier|protected
name|void
name|checkEncoding
parameter_list|()
block|{
name|COSBase
name|fontEncoding
init|=
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSUtils
operator|.
name|isString
argument_list|(
name|fontEncoding
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|checkEncodingAsString
argument_list|(
name|fontEncoding
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|fontEncoding
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|checkEncodingAsDictionary
argument_list|(
name|fontEncoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the encoding entry is invalid
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The Encoding entry doesn't have the right type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method is called by the CheckEncoding method if the Encoding entry is a String. In this case, the String      * must be an existing encoding name. (WinAnsi, MacRoman...)      *       * @param fontEncoding      */
specifier|private
name|void
name|checkEncodingAsString
parameter_list|(
name|COSBase
name|fontEncoding
parameter_list|)
block|{
name|EncodingManager
name|emng
init|=
operator|new
name|EncodingManager
argument_list|()
decl_stmt|;
comment|// Encoding is a Name, check if it is an Existing Encoding
name|String
name|enc
init|=
name|COSUtils
operator|.
name|getAsString
argument_list|(
name|fontEncoding
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
try|try
block|{
name|this
operator|.
name|encoding
operator|=
name|emng
operator|.
name|getEncoding
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|enc
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
comment|// the encoding doesn't exist
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_ENCODING
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/**      * This method is called by the CheckEncoding method if the Encoding entry is an instance of COSDictionary. In this      * case, a new instance of {@link DictionaryEncoding} is created. If an IOException is thrown by the      * DictionaryEncoding constructor the {@link PreflightConstants.ERROR_FONTS_ENCODING} is pushed in the      * FontContainer.      *       * Differences entry validation is implicitly done by the DictionaryEncoding constructor.      *       * @param fontEncoding      */
specifier|private
name|void
name|checkEncodingAsDictionary
parameter_list|(
name|COSBase
name|fontEncoding
parameter_list|)
block|{
name|COSDictionary
name|encodingDictionary
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|fontEncoding
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
try|try
block|{
name|this
operator|.
name|encoding
operator|=
operator|new
name|DictionaryEncoding
argument_list|(
name|encodingDictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// the encoding doesn't exist
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_ENCODING
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/**      * CharProcs is a dictionary where the key is a character name and the value is a Stream which contains the glyph      * representation of the key.      *       * This method checks that all characters codes defined in the Widths Array exist in the CharProcs dictionary. If      * the CharProcs doesn't know the Character, it is mapped with the .notdef one.      *       * For each character, the Glyph width must be the same as the Width value declared in the Widths array.      *       * @param errors      * @return      */
specifier|private
name|void
name|checkCharProcsAndMetrics
parameter_list|()
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|Float
argument_list|>
name|widths
init|=
name|font
operator|.
name|getWidths
argument_list|()
decl_stmt|;
if|if
condition|(
name|widths
operator|==
literal|null
operator|||
name|widths
operator|.
name|isEmpty
argument_list|()
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
literal|"The Witdhs array is unreachable"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSDictionary
name|charProcs
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CHAR_PROCS
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|charProcs
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"The CharProcs element isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|fc
init|=
name|this
operator|.
name|font
operator|.
name|getFirstChar
argument_list|()
decl_stmt|;
name|int
name|lc
init|=
name|this
operator|.
name|font
operator|.
name|getLastChar
argument_list|()
decl_stmt|;
comment|/*          * wArr length = (lc - fc) + 1 and it is an array of int. If FirstChar is greater than LastChar, the validation          * will fail because of the array will have an expected size<= 0.          */
name|int
name|expectedLength
init|=
operator|(
name|lc
operator|-
name|fc
operator|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|widths
operator|.
name|size
argument_list|()
operator|!=
name|expectedLength
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
literal|"The length of Witdhs array is invalid. Expected : \""
operator|+
name|expectedLength
operator|+
literal|"\" Current : \""
operator|+
name|widths
operator|.
name|size
argument_list|()
operator|+
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Check width consistency
name|PDResources
name|pResources
init|=
name|getPDResources
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedLength
condition|;
name|i
operator|++
control|)
block|{
name|int
name|cid
init|=
name|fc
operator|+
name|i
decl_stmt|;
name|float
name|width
init|=
name|widths
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSStream
name|charStream
init|=
name|getCharacterStreamDescription
argument_list|(
name|cid
argument_list|,
name|charProcs
argument_list|)
decl_stmt|;
if|if
condition|(
name|charStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|float
name|fontProgamWidth
init|=
name|getWidthFromCharacterStream
argument_list|(
name|pResources
argument_list|,
name|charStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|width
operator|==
name|fontProgamWidth
condition|)
block|{
comment|// Glyph is OK, we keep the CID.
name|this
operator|.
name|fontContainer
operator|.
name|markCIDAsValid
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|GlyphException
name|glyphEx
init|=
operator|new
name|GlyphException
argument_list|(
name|ERROR_FONTS_METRICS
argument_list|,
name|cid
argument_list|,
literal|"The character with CID\""
operator|+
name|cid
operator|+
literal|"\" should have a width equals to "
operator|+
name|width
argument_list|)
decl_stmt|;
name|this
operator|.
name|fontContainer
operator|.
name|markCIDAsInvalid
argument_list|(
name|cid
argument_list|,
name|glyphEx
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ContentStreamException
name|e
parameter_list|)
block|{
comment|// TODO spaces/isartor-6-2-3-3-t02-fail-h.pdf --> si ajout de l'erreur dans le container le test
comment|// echoue... pourquoi si la font est utilisée ca devrait planter???
name|this
operator|.
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
operator|(
operator|(
name|ContentStreamException
operator|)
name|e
operator|)
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
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
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The CharProcs references an element which can't be read"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
specifier|private
name|PDResources
name|getPDResources
parameter_list|()
block|{
name|COSBase
name|res
init|=
name|this
operator|.
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
name|PDResources
name|pResources
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|resAsDict
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|res
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|resAsDict
operator|!=
literal|null
condition|)
block|{
name|pResources
operator|=
operator|new
name|PDResources
argument_list|(
name|resAsDict
argument_list|)
expr_stmt|;
block|}
return|return
name|pResources
return|;
block|}
specifier|private
name|COSStream
name|getCharacterStreamDescription
parameter_list|(
name|int
name|cid
parameter_list|,
name|COSDictionary
name|charProcs
parameter_list|)
throws|throws
name|ValidationException
block|{
name|String
name|charName
init|=
name|getCharNameFromEncoding
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|COSBase
name|item
init|=
name|charProcs
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|charName
argument_list|)
argument_list|)
decl_stmt|;
name|COSStream
name|charStream
init|=
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|item
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|charStream
operator|==
literal|null
condition|)
block|{
comment|/*              * There are no character description, we declare the Glyph as Invalid. If the character is used in a              * Stream, the GlyphDetail will throw an exception.              */
name|GlyphException
name|glyphEx
init|=
operator|new
name|GlyphException
argument_list|(
name|ERROR_FONTS_METRICS
argument_list|,
name|cid
argument_list|,
literal|"The CharProcs \""
operator|+
name|charName
operator|+
literal|"\" doesn't exist"
argument_list|)
decl_stmt|;
name|this
operator|.
name|fontContainer
operator|.
name|markCIDAsInvalid
argument_list|(
name|cid
argument_list|,
name|glyphEx
argument_list|)
expr_stmt|;
block|}
return|return
name|charStream
return|;
block|}
specifier|private
name|String
name|getCharNameFromEncoding
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
return|return
name|this
operator|.
name|encoding
operator|.
name|getName
argument_list|(
name|cid
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// shouldn't occur
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to check Widths consistency"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Parse the Glyph description to obtain the Width      *       * @return the width of the character      */
specifier|private
name|float
name|getWidthFromCharacterStream
parameter_list|(
name|PDResources
name|resources
parameter_list|,
name|COSStream
name|charStream
parameter_list|)
throws|throws
name|IOException
block|{
name|PreflightPath
name|vPath
init|=
name|context
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
name|PDFAType3StreamParser
name|parser
init|=
operator|new
name|PDFAType3StreamParser
argument_list|(
name|context
argument_list|,
name|vPath
operator|.
name|getClosestPathElement
argument_list|(
name|PDPage
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|resetEngine
argument_list|()
expr_stmt|;
name|parser
operator|.
name|processSubStream
argument_list|(
literal|null
argument_list|,
name|resources
argument_list|,
name|charStream
argument_list|)
expr_stmt|;
return|return
name|parser
operator|.
name|getWidth
argument_list|()
return|;
block|}
comment|/**      * If the Resources entry is present, this method check its content. Only fonts and Images are checked because this      * resource describes glyphs.      *       * @return      */
specifier|private
name|void
name|checkResources
parameter_list|()
throws|throws
name|ValidationException
block|{
name|COSBase
name|resources
init|=
name|this
operator|.
name|fontDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|COSDictionary
name|dictionary
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|resources
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionary
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
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
literal|"The Resources element isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// process Fonts and XObjects
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
operator|new
name|PDResources
argument_list|(
name|dictionary
argument_list|)
argument_list|,
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
name|COSBase
name|cbFont
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
name|cbFont
operator|!=
literal|null
condition|)
block|{
comment|/*                  * Check that all referenced object are present in the PDF file                  */
name|COSDictionary
name|dicFonts
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|cbFont
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|COSName
argument_list|>
name|keyList
init|=
name|dicFonts
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|keyList
control|)
block|{
name|COSBase
name|item
init|=
name|dicFonts
operator|.
name|getItem
argument_list|(
operator|(
name|COSName
operator|)
name|key
argument_list|)
decl_stmt|;
name|COSDictionary
name|xObjFont
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|item
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
try|try
block|{
name|PDFont
name|aFont
init|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|xObjFont
argument_list|)
decl_stmt|;
name|FontContainer
name|aContainer
init|=
name|this
operator|.
name|context
operator|.
name|getFontContainer
argument_list|(
name|aFont
operator|.
name|getCOSObject
argument_list|()
argument_list|)
decl_stmt|;
comment|// another font is used in the Type3, check if the font is valid.
if|if
condition|(
operator|!
name|aContainer
operator|.
name|isValid
argument_list|()
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
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The Resources dictionary of type 3 font contains invalid font"
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
literal|"Unable to valid the Type3 : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

