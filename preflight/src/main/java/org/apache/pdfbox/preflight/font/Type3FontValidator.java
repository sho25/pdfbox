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
name|Collections
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
name|COSInteger
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
name|font
operator|.
name|PDType3CharProc
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
name|common
operator|.
name|COSArrayList
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
name|pdmodel
operator|.
name|font
operator|.
name|PDType3Font
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
name|PreflightType3Stream
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
name|PDType3Font
name|font
decl_stmt|;
specifier|protected
name|COSDictionary
name|fontDictionary
decl_stmt|;
specifier|protected
name|COSDocument
name|cosDocument
decl_stmt|;
specifier|public
name|Type3FontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDType3Font
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
name|font
operator|.
name|getCOSObject
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Some required fields are missing from the Font dictionary."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check that the FontBBox element has the right format as declared in the      * PDF reference document.      */
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The FontBBox element isn't an array"
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The FontBBox element is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": An element of FontBBox isn't a number"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The FontMatrix element isn't an array"
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The FontMatrix element is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": An element of FontMatrix isn't a number"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|/**      * For a Type3 font, the mapping between the Character Code and the      * Character name is entirely defined in the /Encoding entry. The /Encoding      * Entry can be a Name (For the 5 predefined encodings) or a dictionary. If      * it is a dictionary, the /Differences array contains the correspondence      * between a character code and a set of character name which are different      * from the encoding entry of the dictionary.      *      * This method checks that the encoding is :      *<UL>      *<li>An existing encoding name.      *<li>A dictionary with an existing encoding name (the name is optional)      * and a well formed "Differences" array (the array is optional)      *</UL>      *      * At the end of this method, if the validation succeed the Font encoding is      * kept in the {@link #encoding} attribute      */
annotation|@
name|Override
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
name|checkEncodingAsString
argument_list|(
name|enc
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
name|checkEncodingAsDictionary
argument_list|(
name|encodingDictionary
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The Encoding entry doesn't have the right type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method is called by the CheckEncoding method if the /Encoding entry is a String. In this case, the String      * must be an existing encoding name. (WinAnsi, MacRoman...)      *       * @param enc The name of the encoding.      */
specifier|private
name|void
name|checkEncodingAsString
parameter_list|(
name|String
name|enc
parameter_list|)
block|{
comment|// Encoding is a name, check if it is an existing encoding
name|Encoding
name|encodingInstance
init|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|enc
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodingInstance
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
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The encoding '"
operator|+
name|enc
operator|+
literal|"' doesn't exist"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method is called by the CheckEncoding method if the /Encoding entry      * is an instance of COSDictionary. If that dictionary has a /BaseEncoding      * entry, the name is checked. In any case, the /Differences array is      * validated.      *      * @param encodingDictionary the encoding dictionary.      */
specifier|private
name|void
name|checkEncodingAsDictionary
parameter_list|(
name|COSDictionary
name|encodingDictionary
parameter_list|)
block|{
if|if
condition|(
name|encodingDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
condition|)
block|{
name|checkEncodingAsString
argument_list|(
name|encodingDictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|COSBase
name|diff
init|=
name|encodingDictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DIFFERENCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isArray
argument_list|(
name|diff
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
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The differences element of the encoding dictionary isn't an array"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// ---- The DictionaryEncoding object doesn't throw exception if the
comment|// Differences isn't well formed.
comment|// So check if the array has the right format.
name|COSArray
name|diffArray
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|diff
argument_list|,
name|cosDocument
argument_list|)
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
name|diffArray
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|COSBase
name|item
init|=
name|diffArray
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
name|item
operator|instanceof
name|COSInteger
operator|||
name|item
operator|instanceof
name|COSName
operator|)
condition|)
block|{
comment|// ---- Error, the Differences array is invalid
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
literal|"Differences Array should contain COSInt or COSName, no other type"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
comment|/**      * CharProcs is a dictionary where the key is a character name and the value is a Stream which contains the glyph      * representation of the key.      *       * This method checks that all characters codes defined in the Widths Array exist in the CharProcs dictionary. If      * the CharProcs doesn't know the Character, it is mapped with the .notdef one.      *       * For each character, the Glyph width must be the same as the Width value declared in the Widths array.      */
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
name|getWidths
argument_list|(
name|font
argument_list|)
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The Witdhs array is unreachable"
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CharProcs element isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|fc
init|=
name|font
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|lc
init|=
name|font
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|/*          * wArr length = (lc - fc) + 1 and it is an array of int.           * If FirstChar is greater than LastChar, the validation          * will fail because of the array will have an expected size&lt;= 0.          */
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The length of Witdhs array is invalid. Expected : \""
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
name|code
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
name|PDType3CharProc
name|charProc
init|=
name|getCharProc
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|charProc
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|float
name|fontProgramWidth
init|=
name|getWidthFromCharProc
argument_list|(
name|charProc
argument_list|)
decl_stmt|;
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|width
operator|-
name|fontProgramWidth
argument_list|)
operator|<
literal|0.001f
condition|)
block|{
comment|// Glyph is OK, we keep the CID.
comment|// PDF/A-1b only states that the width "shall be consistent".
comment|// For PDF/A-2,3 the description has been enhanced and is now requesting
comment|// "consistent is defined to be a difference of no more than 1/1000 unit"
comment|// We interpret this as clarification of the PDF/A-1b requirement.
name|this
operator|.
name|fontContainer
operator|.
name|markAsValid
argument_list|(
name|code
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
name|code
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The character with CID "
operator|+
name|code
operator|+
literal|" should have a width equals to "
operator|+
name|width
operator|+
literal|", but has "
operator|+
name|fontProgramWidth
argument_list|)
decl_stmt|;
name|this
operator|.
name|fontContainer
operator|.
name|markAsInvalid
argument_list|(
name|code
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
name|e
operator|.
name|getErrorCode
argument_list|()
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CharProcs references an element which can't be read"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|Float
argument_list|>
name|getWidths
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|List
argument_list|<
name|Float
argument_list|>
name|widths
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|font
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|widths
operator|=
name|COSArrayList
operator|.
name|convertFloatCOSArrayToList
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widths
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
return|return
name|widths
return|;
block|}
specifier|private
name|PDType3CharProc
name|getCharProc
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDType3CharProc
name|charProc
init|=
name|font
operator|.
name|getCharProc
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|charProc
operator|==
literal|null
condition|)
block|{
comment|// There are no character description, we declare the Glyph as Invalid. If the character
comment|// is used in a Stream, the GlyphDetail will throw an exception.
name|GlyphException
name|glyphEx
init|=
operator|new
name|GlyphException
argument_list|(
name|ERROR_FONTS_METRICS
argument_list|,
name|code
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CharProcs \""
operator|+
name|font
operator|.
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
operator|+
literal|"\" doesn't exist"
argument_list|)
decl_stmt|;
name|this
operator|.
name|fontContainer
operator|.
name|markAsInvalid
argument_list|(
name|code
argument_list|,
name|glyphEx
argument_list|)
expr_stmt|;
block|}
return|return
name|charProc
return|;
block|}
comment|/**      * Parse the Glyph description to obtain the Width      *       * @return the width of the character      */
specifier|private
name|float
name|getWidthFromCharProc
parameter_list|(
name|PDType3CharProc
name|charProc
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
name|PreflightType3Stream
name|parser
init|=
operator|new
name|PreflightType3Stream
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
argument_list|,
name|charProc
argument_list|)
decl_stmt|;
name|parser
operator|.
name|showType3Character
argument_list|(
name|charProc
argument_list|)
expr_stmt|;
return|return
name|parser
operator|.
name|getWidth
argument_list|()
return|;
block|}
comment|/**      * If the Resources entry is present, this method checks its content. Only fonts and images are checked because this      * resource describes glyphs.      */
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The Resources element isn't a dictionary"
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
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The Resources dictionary of type 3 font contains invalid font"
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
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_DAMAGED
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Unable to valid the Type3 : "
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
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

