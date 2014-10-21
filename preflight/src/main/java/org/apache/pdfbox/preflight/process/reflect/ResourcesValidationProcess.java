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
operator|.
name|reflect
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
name|EXTGSTATE_PROCESS
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
name|PreflightConfiguration
operator|.
name|FONT_PROCESS
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
name|PreflightConfiguration
operator|.
name|GRAPHIC_PROCESS
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
name|PreflightConfiguration
operator|.
name|SHADDING_PATTERN_PROCESS
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
name|PreflightConfiguration
operator|.
name|TILING_PATTERN_PROCESS
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
name|ERROR_GRAPHIC_INVALID_PATTERN_DEFINITION
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
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|pdmodel
operator|.
name|graphics
operator|.
name|pattern
operator|.
name|PDAbstractPattern
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
name|graphics
operator|.
name|pattern
operator|.
name|PDTilingPattern
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
name|graphics
operator|.
name|shading
operator|.
name|PDShading
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
name|graphics
operator|.
name|PDXObject
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
name|process
operator|.
name|AbstractProcess
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
name|ResourcesValidationProcess
extends|extends
name|AbstractProcess
block|{
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
name|PreflightPath
name|vPath
init|=
name|ctx
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|vPath
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
operator|!
name|vPath
operator|.
name|isExpectedType
argument_list|(
name|PDResources
operator|.
name|class
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
name|ERROR_PDF_PROCESSING_MISSING
argument_list|,
literal|"Resources validation process needs at least one PDResources object"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDResources
name|resources
init|=
operator|(
name|PDResources
operator|)
name|vPath
operator|.
name|peek
argument_list|()
decl_stmt|;
name|validateFonts
argument_list|(
name|ctx
argument_list|,
name|resources
argument_list|)
expr_stmt|;
name|validateExtGStates
argument_list|(
name|ctx
argument_list|,
name|resources
argument_list|)
expr_stmt|;
name|validateShadingPattern
argument_list|(
name|ctx
argument_list|,
name|resources
argument_list|)
expr_stmt|;
name|validateTilingPattern
argument_list|(
name|ctx
argument_list|,
name|resources
argument_list|)
expr_stmt|;
name|validateXObjects
argument_list|(
name|ctx
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check that fonts present in the Resources dictionary match with PDF/A-1 rules      *       * @param context      * @param resources      * @throws ValidationException      */
specifier|protected
name|void
name|validateFonts
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|ValidationException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|mapOfFonts
init|=
name|getFonts
argument_list|(
name|resources
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapOfFonts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|entry
range|:
name|mapOfFonts
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|FONT_PROCESS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will get the map of fonts. This will never return null.      *      * @return The map of fonts.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|getFonts
parameter_list|(
name|COSDictionary
name|resources
parameter_list|,
name|PreflightContext
name|context
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|fonts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
argument_list|()
decl_stmt|;
name|COSDictionary
name|fontsDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontsDictionary
operator|==
literal|null
condition|)
block|{
name|fontsDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|,
name|fontsDictionary
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|COSName
name|fontName
range|:
name|fontsDictionary
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|font
init|=
name|fontsDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|fontName
argument_list|)
decl_stmt|;
comment|// data-000174.pdf contains a font that is a COSArray, looks to be an error in the
comment|// PDF, we will just ignore entries that are not dictionaries.
if|if
condition|(
name|font
operator|instanceof
name|COSDictionary
condition|)
block|{
name|PDFont
name|newFont
init|=
literal|null
decl_stmt|;
try|try
block|{
name|newFont
operator|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
operator|(
name|COSDictionary
operator|)
name|font
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addFontError
argument_list|(
operator|(
name|COSDictionary
operator|)
name|font
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newFont
operator|!=
literal|null
condition|)
block|{
name|fonts
operator|.
name|put
argument_list|(
name|fontName
operator|.
name|getName
argument_list|()
argument_list|,
name|newFont
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|fonts
return|;
block|}
comment|/**      * PDFont loads embedded fonts in its constructor so we have to handle IOExceptions      * from PDFont and translate them into validation errors.      */
specifier|private
name|void
name|addFontError
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|,
name|PreflightContext
name|context
parameter_list|)
block|{
name|COSName
name|type
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSName
operator|.
name|FONT
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_UNKNOWN_FONT_TYPE
argument_list|,
literal|"Expected 'Font' dictionary but found '"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|fontName
init|=
literal|"Unknown"
decl_stmt|;
if|if
condition|(
name|dictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
condition|)
block|{
name|fontName
operator|=
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
expr_stmt|;
block|}
name|COSName
name|subType
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|TYPE1
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_TYPE1_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|MM_TYPE1
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_TYPE1_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TRUE_TYPE
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_TRUETYPE_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TYPE3
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_TYPE3_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TYPE0
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_CID_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE0
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_UNKNOWN_FONT_TYPE
argument_list|,
literal|"Unexpected CIDFontType0 descendant font for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE2
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_UNKNOWN_FONT_TYPE
argument_list|,
literal|"Unexpected CIDFontType2 descendant font for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_UNKNOWN_FONT_TYPE
argument_list|,
literal|"Unknown font type for "
operator|+
name|fontName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *       * @param context      * @param resources      * @throws ValidationException      */
specifier|protected
name|void
name|validateExtGStates
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSBase
name|egsEntry
init|=
name|resources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE
argument_list|)
decl_stmt|;
name|COSDocument
name|cosDocument
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSDictionary
name|extGState
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|egsEntry
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|egsEntry
operator|!=
literal|null
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|extGState
argument_list|,
name|EXTGSTATE_PROCESS
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method check the Shading entry of the resource dictionary if exists.      *       * @param context      * @param resources      * @throws ValidationException      */
specifier|protected
name|void
name|validateShadingPattern
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
for|for
control|(
name|COSName
name|name
range|:
name|resources
operator|.
name|getShadingNames
argument_list|()
control|)
block|{
name|PDShading
name|shading
init|=
name|resources
operator|.
name|getShading
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|shading
argument_list|,
name|SHADDING_PATTERN_PROCESS
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
name|ERROR_GRAPHIC_INVALID_PATTERN_DEFINITION
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
comment|/**      * This method check the Shading entry of the resource dictionary if exists.      *       * @param context      * @param resources      * @throws ValidationException      */
specifier|protected
name|void
name|validateTilingPattern
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
for|for
control|(
name|COSName
name|name
range|:
name|resources
operator|.
name|getPatternNames
argument_list|()
control|)
block|{
name|PDAbstractPattern
name|pattern
init|=
name|resources
operator|.
name|getPattern
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|instanceof
name|PDTilingPattern
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|pattern
argument_list|,
name|TILING_PATTERN_PROCESS
argument_list|)
expr_stmt|;
block|}
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
name|ERROR_GRAPHIC_INVALID_PATTERN_DEFINITION
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
specifier|protected
name|void
name|validateXObjects
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSDocument
name|cosDocument
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSDictionary
name|mapOfXObj
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|resources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapOfXObj
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|mapOfXObj
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|COSBase
name|xobj
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|xobj
operator|!=
literal|null
operator|&&
name|COSUtils
operator|.
name|isStream
argument_list|(
name|xobj
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
try|try
block|{
name|COSStream
name|stream
init|=
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|xobj
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|PDXObject
name|pdXObject
init|=
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|stream
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|resources
argument_list|)
decl_stmt|;
if|if
condition|(
name|pdXObject
operator|!=
literal|null
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|pdXObject
argument_list|,
name|GRAPHIC_PROCESS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|stream
argument_list|,
name|GRAPHIC_PROCESS
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
block|}
block|}
block|}
block|}
end_class

end_unit

