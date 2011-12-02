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
name|HashMap
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
name|Map
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
name|pdmodel
operator|.
name|font
operator|.
name|PDFont
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFontContainer
block|{
comment|/**    * PDFBox object which contains the Font Dictionary    */
specifier|protected
name|PDFont
name|font
init|=
literal|null
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|GlyphDetail
argument_list|>
name|cidKnownByFont
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|GlyphDetail
argument_list|>
argument_list|()
decl_stmt|;
comment|/**    * Boolean used to know if the Font Program is embedded.    */
specifier|protected
name|boolean
name|isFontProgramEmbedded
init|=
literal|true
decl_stmt|;
comment|/**    * Errors which occurs during the Font Validation    */
specifier|protected
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|/**    * The FontContainer Constructor. The type attribute is initialized according    * to the given PDFont object.    *     * @param fd    *          Font object of the PDFBox API. (Mandatory)    * @throws NullPointerException    *           If the fd is null.    */
specifier|public
name|AbstractFontContainer
parameter_list|(
name|PDFont
name|fd
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|fd
expr_stmt|;
block|}
comment|/**    * Return the PDFont object    *     * @return    */
specifier|public
name|PDFont
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
specifier|public
name|void
name|addCID
parameter_list|(
name|Integer
name|cid
parameter_list|,
name|GlyphDetail
name|details
parameter_list|)
block|{
name|this
operator|.
name|cidKnownByFont
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|details
argument_list|)
expr_stmt|;
block|}
comment|/**    * @return the isFontProgramEmbedded    */
specifier|public
name|boolean
name|isFontProgramEmbedded
parameter_list|()
block|{
return|return
name|isFontProgramEmbedded
return|;
block|}
comment|/**    * @param isFontProgramEmbedded    *          the isFontProgramEmbedded to set    */
specifier|public
name|void
name|setFontProgramEmbedded
parameter_list|(
name|boolean
name|isFontProgramEmbedded
parameter_list|)
block|{
name|this
operator|.
name|isFontProgramEmbedded
operator|=
name|isFontProgramEmbedded
expr_stmt|;
block|}
comment|/**    * Addition of a validation error.    *     * @param error    */
specifier|public
name|void
name|addError
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
name|this
operator|.
name|errors
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
comment|/**    * This method returns the validation state of the font.    *     * If the list of errors is empty, the validation is successful (State :    * VALID). If the size of the list is 1 and if the error is about EmbeddedFont,    * the state is "MAYBE" because the font can be valid if    * it isn't used (for Width error) or if the rendering mode is 3 (For not    * embedded font). Otherwise, the validation failed (State : INVALID)    *     * @return    */
specifier|public
name|State
name|isValid
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|State
operator|.
name|VALID
return|;
block|}
if|if
condition|(
operator|(
name|this
operator|.
name|errors
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|)
operator|&&
operator|!
name|this
operator|.
name|isFontProgramEmbedded
condition|)
block|{
return|return
name|State
operator|.
name|MAYBE
return|;
block|}
comment|// else more than one error, the validation failed
return|return
name|State
operator|.
name|INVALID
return|;
block|}
comment|/**    * @return the errors    */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
specifier|public
specifier|static
enum|enum
name|State
block|{
name|VALID
block|,
name|MAYBE
block|,
name|INVALID
block|;   }
comment|/**    * Check if the cid is present and consistent in the contained font.    * @param cid the cid    * @return true if cid is present and consistent, false otherwise    */
specifier|public
specifier|abstract
name|void
name|checkCID
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
function_decl|;
name|void
name|addKnownCidElement
parameter_list|(
name|GlyphDetail
name|glyphDetail
parameter_list|)
block|{
name|this
operator|.
name|cidKnownByFont
operator|.
name|put
argument_list|(
name|glyphDetail
operator|.
name|getCID
argument_list|()
argument_list|,
name|glyphDetail
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isAlreadyComputedCid
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
block|{
name|boolean
name|already
init|=
literal|false
decl_stmt|;
name|GlyphDetail
name|gdetail
init|=
name|this
operator|.
name|cidKnownByFont
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|gdetail
operator|!=
literal|null
condition|)
block|{
name|gdetail
operator|.
name|throwExceptionIfNotValid
argument_list|()
expr_stmt|;
name|already
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|already
return|;
block|}
specifier|protected
name|void
name|checkWidthsConsistency
parameter_list|(
name|int
name|cid
parameter_list|,
name|float
name|widthProvidedByPdfDictionary
parameter_list|,
name|float
name|widthInFontProgram
parameter_list|)
throws|throws
name|GlyphException
block|{
comment|// a delta of 1/1000 unit is allowed
specifier|final
name|float
name|epsilon
init|=
name|widthInFontProgram
operator|/
literal|1000
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|Math
operator|.
name|floor
argument_list|(
name|widthInFontProgram
operator|-
name|epsilon
argument_list|)
operator|<=
name|widthProvidedByPdfDictionary
operator|&&
name|Math
operator|.
name|round
argument_list|(
name|widthInFontProgram
operator|+
name|epsilon
argument_list|)
operator|>=
name|widthProvidedByPdfDictionary
operator|)
condition|)
block|{
name|GlyphException
name|e
init|=
operator|new
name|GlyphException
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_FONTS_METRICS
argument_list|,
name|cid
argument_list|,
literal|"Width of the character \""
operator|+
name|cid
operator|+
literal|"\" in the font program \""
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|"\"is inconsistent with the width in the PDF dictionary."
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

