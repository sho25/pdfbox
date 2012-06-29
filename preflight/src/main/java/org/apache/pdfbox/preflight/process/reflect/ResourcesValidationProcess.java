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
name|graphics
operator|.
name|pattern
operator|.
name|PDPatternResources
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
name|PDTilingPatternResources
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
name|PDShadingResources
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
name|xobject
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
operator|&&
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
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Resources validation process needs at least one PDResources object"
argument_list|)
throw|;
block|}
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
comment|/** 	 * Check that fonts present in the Resources dictionary match with PDF/A-1 rules 	 * @param context 	 * @param resources 	 * @throws ValidationException 	 */
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
name|resources
operator|.
name|getFonts
argument_list|()
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
comment|/** 	 *  	 * @param context 	 * @param resources 	 * @throws ValidationException 	 */
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
name|getCOSDictionary
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
comment|/** 	 * This method check the Shading entry of the resource dictionary if exists. 	 * @param context 	 * @param resources 	 * @throws ValidationException 	 */
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
name|Map
argument_list|<
name|String
argument_list|,
name|PDShadingResources
argument_list|>
name|shadingResources
init|=
name|resources
operator|.
name|getShadings
argument_list|()
decl_stmt|;
if|if
condition|(
name|shadingResources
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
name|PDShadingResources
argument_list|>
name|entry
range|:
name|shadingResources
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
name|SHADDING_PATTERN_PROCESS
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
comment|/** 	 * This method check the Shading entry of the resource dictionary if exists. 	 * @param context 	 * @param resources 	 * @throws ValidationException 	 */
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
name|Map
argument_list|<
name|String
argument_list|,
name|PDPatternResources
argument_list|>
name|patternResources
init|=
name|resources
operator|.
name|getPatterns
argument_list|()
decl_stmt|;
if|if
condition|(
name|patternResources
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
name|PDPatternResources
argument_list|>
name|entry
range|:
name|patternResources
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|PDTilingPatternResources
condition|)
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
name|TILING_PATTERN_PROCESS
argument_list|)
expr_stmt|;
block|}
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
name|getCOSDictionary
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

