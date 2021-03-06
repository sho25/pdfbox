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
name|ERROR_SYNTAX_COMMON
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
name|ERROR_SYNTAX_NUMERIC_RANGE
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
name|MAX_NEGATIVE_FLOAT
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
name|MAX_POSITIVE_FLOAT
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
name|COSFloat
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
name|cos
operator|.
name|COSNumber
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
name|ContextHelper
import|;
end_import

begin_class
specifier|public
class|class
name|ExtGStateValidationProcess
extends|extends
name|AbstractProcess
block|{
comment|/**      * Validate the ExtGState dictionaries.      *       * @param context the context which contains the Resource dictionary.      * @throws ValidationException thrown if an Extended Graphic State isn't valid.      */
annotation|@
name|Override
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
name|validateFonts
argument_list|(
name|context
argument_list|,
name|listOfExtGState
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a list of ExtGState dictionaries using the given Resource dictionary and the COSDocument.      *       * @param extGStates a resource COSDictionary.      * @return the list of ExtGState dictionaries.      * @throws ValidationException thrown if an Extended Graphic State isn't valid.      */
specifier|public
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|extractExtGStateDictionaries
parameter_list|(
name|COSDictionary
name|extGStates
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
argument_list|<>
argument_list|(
literal|0
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
name|COSName
name|key
range|:
name|extGStates
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSDictionary
name|gsDict
init|=
name|extGStates
operator|.
name|getCOSDictionary
argument_list|(
name|key
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
return|return
name|listOfExtGState
return|;
block|}
comment|/**      * Validate transparency rules in all ExtGState dictionaries of this container.      *       * @param context the preflight context.      * @param listOfExtGState a list of ExtGState COSDictionaries.      *       */
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
name|checkUpperCA
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
name|checkLowerCA
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
comment|/**      * Validate fonts in all ExtGState dictionaries of this container.      *       * @param context the preflight context.      * @param listOfExtGState a list of ExtGState COSDictionaries.      * @throws ValidationException      *       */
specifier|protected
name|void
name|validateFonts
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
throws|throws
name|ValidationException
block|{
for|for
control|(
name|COSDictionary
name|egs
range|:
name|listOfExtGState
control|)
block|{
name|checkFont
argument_list|(
name|context
argument_list|,
name|egs
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks a Font array in the ExtGState dictionary.      *       * @param context the preflight context.      * @param egs the Graphic state to check      * @throws ValidationException      */
specifier|private
name|void
name|checkFont
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSBase
name|base
init|=
name|egs
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
name|base
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSArray
operator|)
operator|||
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_COMMON
argument_list|,
literal|"/Font entry in /ExtGState must be an array with 2 elements"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSArray
name|ar
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
name|COSBase
name|base0
init|=
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base0
operator|instanceof
name|COSObject
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
name|ERROR_SYNTAX_COMMON
argument_list|,
literal|"1st element in /Font entry in /ExtGState must be an indirect object"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSBase
name|base1
init|=
name|ar
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base1
operator|instanceof
name|COSNumber
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
name|ERROR_SYNTAX_COMMON
argument_list|,
literal|"2nd element in /Font entry in /ExtGState must be a number"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSNumber
name|fontSize
init|=
operator|(
name|COSNumber
operator|)
name|ar
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontSize
operator|.
name|floatValue
argument_list|()
operator|>
name|MAX_POSITIVE_FLOAT
operator|||
name|fontSize
operator|.
name|floatValue
argument_list|()
operator|<
name|MAX_NEGATIVE_FLOAT
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_NUMERIC_RANGE
argument_list|,
literal|"invalid float range in 2nd element in /Font entry in /ExtGState"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ar
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|fontDict
init|=
operator|(
name|COSDictionary
operator|)
name|ar
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|PDFont
name|newFont
init|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|fontDict
argument_list|)
decl_stmt|;
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|newFont
argument_list|,
name|FONT_PROCESS
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
name|fontDict
argument_list|,
name|context
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|getDictionaryObject
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
operator|&&
operator|!
operator|(
name|smVal
operator|instanceof
name|COSName
operator|&&
name|COSName
operator|.
name|NONE
operator|.
name|equals
argument_list|(
name|smVal
argument_list|)
operator|)
condition|)
block|{
comment|// ---- Soft Mask is valid only if it is a COSName equals to None
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
name|COSName
name|bmVal
init|=
name|egs
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|BM
argument_list|)
decl_stmt|;
comment|// ---- Blend Mode is valid only if it is equals to Normal or Compatible
if|if
condition|(
name|bmVal
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|COSName
operator|.
name|NORMAL
operator|.
name|equals
argument_list|(
name|bmVal
argument_list|)
operator|||
name|COSName
operator|.
name|COMPATIBLE
operator|.
name|equals
argument_list|(
name|bmVal
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
name|ERROR_TRANSPARENCY_EXT_GS_BLEND_MODE
argument_list|,
literal|"BlendMode value isn't valid (only Normal and Compatible are authorized)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks the "CA" value of the ExtGState dictionary. It is optional but must be 1.0      * if present.      *      * @param context the preflight context.      * @param egs the graphic state to check      */
specifier|private
name|void
name|checkUpperCA
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
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CA
argument_list|)
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
name|uCA
operator|instanceof
name|COSFloat
condition|?
operator|(
operator|(
name|COSFloat
operator|)
name|uCA
operator|)
operator|.
name|floatValue
argument_list|()
else|:
literal|null
decl_stmt|;
name|Integer
name|ica
init|=
name|uCA
operator|instanceof
name|COSInteger
condition|?
operator|(
operator|(
name|COSInteger
operator|)
name|uCA
operator|)
operator|.
name|intValue
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|fca
operator|!=
literal|null
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|fca
argument_list|,
literal|1.0f
argument_list|)
operator|==
literal|0
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
block|}
comment|/**      * This method checks the "ca" value of the ExtGState dictionary. It is optional but must be 1.0      * if present.      *      * @param context the preflight context.      * @param egs the graphic state to check      */
specifier|private
name|void
name|checkLowerCA
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|egs
parameter_list|)
block|{
name|COSBase
name|lCA
init|=
name|egs
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CA_NS
argument_list|)
decl_stmt|;
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
name|lCA
operator|instanceof
name|COSFloat
condition|?
operator|(
operator|(
name|COSFloat
operator|)
name|lCA
operator|)
operator|.
name|floatValue
argument_list|()
else|:
literal|null
decl_stmt|;
name|Integer
name|ica
init|=
name|lCA
operator|instanceof
name|COSInteger
condition|?
operator|(
operator|(
name|COSInteger
operator|)
name|lCA
operator|)
operator|.
name|intValue
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|fca
operator|!=
literal|null
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|fca
argument_list|,
literal|1.0f
argument_list|)
operator|==
literal|0
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
literal|"ca entry in a ExtGState is invalid"
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
name|COSName
operator|.
name|TR
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
comment|/**      * Check the TR2 entry. A valid ExtGState hasn't TR2 entry or a TR2 entry equals to "default".      *       * @param context the preflight context      * @param egs the graphic state to check      */
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

