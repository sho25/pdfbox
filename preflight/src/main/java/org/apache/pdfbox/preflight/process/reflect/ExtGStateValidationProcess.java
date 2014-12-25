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
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_KEY
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
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
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
name|ERROR_TRANSPARENCY_EXT_GS_BLEND_MODE
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
name|ERROR_TRANSPARENCY_EXT_GS_CA
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
name|ERROR_TRANSPARENCY_EXT_GS_SOFT_MASK
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
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE_ENTRY_REGEX
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
name|TRANSPARENCY_DICTIONARY_KEY_BLEND_MODE
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
name|TRANSPARENCY_DICTIONARY_KEY_LOWER_CA
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
name|TRANSPARENCY_DICTIONARY_KEY_UPPER_CA
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
name|TRANSPARENCY_DICTIONARY_VALUE_BM_COMPATIBLE
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
name|TRANSPARENCY_DICTIONARY_VALUE_BM_NORMAL
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
name|TRANSPARENCY_DICTIONARY_VALUE_SOFT_MASK_NONE
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

begin_class
specifier|public
class|class
name|ExtGStateValidationProcess
extends|extends
name|AbstractProcess
block|{
comment|/**      * Validate the ExtGState dictionaries.      *       * @param context the context which contains the Resource dictionary.      * @throws ValidationException thrown if a the Extended Graphic State isn't valid.      */
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|context
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightPath
name|vPath
init|=
name|context
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
name|COSDictionary
operator|.
name|class
argument_list|)
condition|)
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
name|ERROR_GRAPHIC_XOBJECT_INVALID_TYPE
argument_list|,
literal|"ExtGState validation required at least a Resource dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSDictionary
name|extGStatesDict
init|=
operator|(
name|COSDictionary
operator|)
name|vPath
operator|.
name|peek
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|listOfExtGState
init|=
name|extractExtGStateDictionaries
argument_list|(
name|context
argument_list|,
name|extGStatesDict
argument_list|)
decl_stmt|;
name|validateTransparencyRules
argument_list|(
name|context
argument_list|,
name|listOfExtGState
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a list of ExtGState dictionaries using the given Resource dictionary and the COSDocument.      *       * @param context the context which contains the Resource dictionary.      * @param egsEntry a resource COSDictionary.      * @return the list of ExtGState dictionaries.      * @throws ValidationException thrown if a the Extended Graphic State isn't valid.      */
specifier|public
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|extractExtGStateDictionaries
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egsEntry
parameter_list|)
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|listOfExtGState
init|=
operator|new
name|ArrayList
argument_list|<
name|COSDictionary
argument_list|>
argument_list|(
literal|0
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
name|extGStates
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
name|extGStates
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|object
range|:
name|extGStates
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSName
name|key
init|=
operator|(
name|COSName
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|getName
argument_list|()
operator|.
name|matches
argument_list|(
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE_ENTRY_REGEX
argument_list|)
condition|)
block|{
name|COSBase
name|gsBase
init|=
name|extGStates
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|COSDictionary
name|gsDict
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|gsBase
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|gsDict
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"The Extended Graphics State dictionary is invalid"
argument_list|)
throw|;
block|}
name|listOfExtGState
operator|.
name|add
argument_list|(
name|gsDict
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|listOfExtGState
return|;
block|}
comment|/**      * Validate all ExtGState dictionaries of this container      *       * @param context the preflight context.      * @param listOfExtGState a list of ExtGState COSDictionaries.      *       */
specifier|protected
name|void
name|validateTransparencyRules
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|listOfExtGState
parameter_list|)
block|{
for|for
control|(
name|COSDictionary
name|egs
range|:
name|listOfExtGState
control|)
block|{
name|checkSoftMask
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
name|checkCA
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
name|checkBlendMode
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
name|checkTRKey
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
name|checkTR2Key
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks the SMask value of the ExtGState dictionary. The Soft Mask is optional but must be "None" if      * it is present.      *       * @param context the preflight context.      * @param egs the Graphic state to check      */
specifier|private
name|void
name|checkSoftMask
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
name|COSBase
name|smVal
init|=
name|egs
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|smVal
operator|!=
literal|null
condition|)
block|{
comment|// ---- Soft Mask is valid only if it is a COSName equals to None
if|if
condition|(
operator|!
operator|(
name|smVal
operator|instanceof
name|COSName
operator|&&
name|TRANSPARENCY_DICTIONARY_VALUE_SOFT_MASK_NONE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|smVal
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_TRANSPARENCY_EXT_GS_SOFT_MASK
argument_list|,
literal|"SoftMask must be null or None"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This method checks the BM value of the ExtGState dictionary. The Blend Mode is optional but must be "Normal" or      * "Compatible" if it is present.      *       * @param context the preflight context     * @param egs the graphic state to check      */
specifier|private
name|void
name|checkBlendMode
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
name|COSBase
name|bmVal
init|=
name|egs
operator|.
name|getItem
argument_list|(
name|TRANSPARENCY_DICTIONARY_KEY_BLEND_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|bmVal
operator|!=
literal|null
condition|)
block|{
comment|// ---- Blend Mode is valid only if it is equals to Normal or Compatible
if|if
condition|(
operator|!
operator|(
name|bmVal
operator|instanceof
name|COSName
operator|&&
operator|(
name|TRANSPARENCY_DICTIONARY_VALUE_BM_NORMAL
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|bmVal
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|TRANSPARENCY_DICTIONARY_VALUE_BM_COMPATIBLE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|bmVal
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_TRANSPARENCY_EXT_GS_BLEND_MODE
argument_list|,
literal|"BlendMode value isn't valid (only Normal and Compatible are authorized)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This method checks the "CA" and "ca" values of the ExtGState dictionary. They are optional but must be 1.0 if      * they are present.      *       * @param context the preflight context.      * @param egs the graphic state to check      */
specifier|private
name|void
name|checkCA
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
name|COSBase
name|uCA
init|=
name|egs
operator|.
name|getItem
argument_list|(
name|TRANSPARENCY_DICTIONARY_KEY_UPPER_CA
argument_list|)
decl_stmt|;
name|COSBase
name|lCA
init|=
name|egs
operator|.
name|getItem
argument_list|(
name|TRANSPARENCY_DICTIONARY_KEY_LOWER_CA
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
if|if
condition|(
name|uCA
operator|!=
literal|null
condition|)
block|{
comment|// ---- If CA is present only the value 1.0 is authorized
name|Float
name|fca
init|=
name|COSUtils
operator|.
name|getAsFloat
argument_list|(
name|uCA
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|Integer
name|ica
init|=
name|COSUtils
operator|.
name|getAsInteger
argument_list|(
name|uCA
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|fca
operator|!=
literal|null
operator|&&
name|fca
operator|==
literal|1.0f
operator|)
operator|&&
operator|!
operator|(
name|ica
operator|!=
literal|null
operator|&&
name|ica
operator|==
literal|1
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_TRANSPARENCY_EXT_GS_CA
argument_list|,
literal|"CA entry in a ExtGState is invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lCA
operator|!=
literal|null
condition|)
block|{
comment|// ---- If ca is present only the value 1.0 is authorized
name|Float
name|fca
init|=
name|COSUtils
operator|.
name|getAsFloat
argument_list|(
name|lCA
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|Integer
name|ica
init|=
name|COSUtils
operator|.
name|getAsInteger
argument_list|(
name|lCA
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|fca
operator|!=
literal|null
operator|&&
name|fca
operator|==
literal|1.0f
operator|)
operator|&&
operator|!
operator|(
name|ica
operator|!=
literal|null
operator|&&
name|ica
operator|==
literal|1
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_TRANSPARENCY_EXT_GS_CA
argument_list|,
literal|"ca entry in a ExtGState  is invalid."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Check the TR entry. A valid ExtGState hasn't TR entry.      *       * @param context the preflight context      * @param egs the graphic state to check      */
specifier|protected
name|void
name|checkTRKey
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
if|if
condition|(
name|egs
operator|.
name|getItem
argument_list|(
literal|"TR"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|,
literal|"No TR key expected in Extended graphics state"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check the TR2 entry. A valid ExtGState hasn't TR2 entry or a TR2 entry equals to "default".      *       * @param egs the graphic state to check      */
specifier|protected
name|void
name|checkTR2Key
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
if|if
condition|(
name|egs
operator|.
name|getItem
argument_list|(
literal|"TR2"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|egs
operator|.
name|getNameAsString
argument_list|(
literal|"TR2"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"Default"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"TR2 key only expect 'Default' value, not '"
operator|+
name|s
operator|+
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

