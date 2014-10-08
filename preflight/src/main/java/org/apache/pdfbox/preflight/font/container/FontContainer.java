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
name|container
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
name|util
operator|.
name|FontLike
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
name|GlyphDetail
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

begin_class
specifier|public
specifier|abstract
class|class
name|FontContainer
block|{
comment|/**      * List of validation errors that occur during the font validation. If the font is used by an object in the PDF, all      * these errors will be appended to the Error list of the PreflightContext.      */
specifier|protected
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errorBuffer
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Boolean used to known if the font is embedded.      */
specifier|protected
name|boolean
name|embeddedFont
init|=
literal|true
decl_stmt|;
comment|/**      * Link CID to an Object that contain information about the Glyph state (Valid or no)      */
specifier|protected
name|Map
argument_list|<
name|Integer
argument_list|,
name|GlyphDetail
argument_list|>
name|computedCid
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
specifier|protected
name|boolean
name|errorsAleadyMerged
init|=
literal|false
decl_stmt|;
specifier|protected
name|FontLike
name|font
decl_stmt|;
specifier|public
name|FontContainer
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
operator|new
name|FontLike
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FontContainer
parameter_list|(
name|FontLike
name|font
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
specifier|public
name|void
name|push
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
name|this
operator|.
name|errorBuffer
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|push
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
name|this
operator|.
name|errorBuffer
operator|.
name|addAll
argument_list|(
name|errors
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getAllErrors
parameter_list|()
block|{
return|return
name|this
operator|.
name|errorBuffer
return|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|errorBuffer
operator|.
name|isEmpty
argument_list|()
operator|&&
name|isEmbeddedFont
argument_list|()
operator|)
return|;
block|}
specifier|public
name|boolean
name|errorsAleadyMerged
parameter_list|()
block|{
return|return
name|errorsAleadyMerged
return|;
block|}
specifier|public
name|void
name|setErrorsAleadyMerged
parameter_list|(
name|boolean
name|errorsAleadyMerged
parameter_list|)
block|{
name|this
operator|.
name|errorsAleadyMerged
operator|=
name|errorsAleadyMerged
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEmbeddedFont
parameter_list|()
block|{
return|return
name|embeddedFont
return|;
block|}
specifier|public
name|void
name|notEmbedded
parameter_list|()
block|{
name|this
operator|.
name|embeddedFont
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      *       * @param cid todo: not a CID, this is actually a char code!      * @throws GlyphException      */
specifier|public
name|void
name|checkGlyphWidth
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
block|{
if|if
condition|(
name|isAlreadyComputedCid
argument_list|(
name|cid
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
specifier|final
name|float
name|expectedWidth
init|=
name|this
operator|.
name|font
operator|.
name|getWidth
argument_list|(
name|cid
argument_list|)
decl_stmt|;
specifier|final
name|float
name|foundWidth
init|=
name|getFontProgramWidth
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|checkWidthsConsistency
argument_list|(
name|cid
argument_list|,
name|expectedWidth
argument_list|,
name|foundWidth
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|GlyphException
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_GLYPH
argument_list|,
name|cid
argument_list|,
literal|"Unexpected error during the width validation for the character CID("
operator|+
name|cid
operator|+
literal|") : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check if the given CID is already computed      *       * @param cid      *            the CID to check      * @return true if the CID has previously been marked as valid, false otherwise      * @throws GlyphException      *             if the CID has previously been marked as invalid // TODO useful ??      */
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
name|computedCid
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
comment|/**      * Extract the Glyph width for the given CID.      *       * @param cid      * @return The Glyph width in 'em' unit.      * @throws GlyphException       */
specifier|protected
specifier|abstract
name|float
name|getFontProgramWidth
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
function_decl|;
comment|/**      * Test if both width are consistent. At the end of this method, the CID is marked as valid or invalid.      *       * @param cid      * @param expectedWidth      * @param foundWidth      *            the glyph width found in the font program, a negative value if the CID is missing from the font.      * @throws GlyphException      */
specifier|protected
name|void
name|checkWidthsConsistency
parameter_list|(
name|int
name|cid
parameter_list|,
name|float
name|expectedWidth
parameter_list|,
name|float
name|foundWidth
parameter_list|)
throws|throws
name|GlyphException
block|{
if|if
condition|(
name|foundWidth
operator|<
literal|0
condition|)
block|{
name|GlyphException
name|e
init|=
operator|new
name|GlyphException
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_GLYPH_MISSING
argument_list|,
name|cid
argument_list|,
literal|"The character \""
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
literal|"\" is missing from the Charater Encoding."
argument_list|)
decl_stmt|;
name|markCIDAsInvalid
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
comment|// consistent is defined to be a difference of no more than 1/1000 unit.
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|foundWidth
operator|-
name|expectedWidth
argument_list|)
operator|>
literal|1
condition|)
block|{
name|GlyphException
name|e
init|=
operator|new
name|GlyphException
argument_list|(
name|PreflightConstants
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
literal|"\" is inconsistent with the width in the PDF dictionary."
argument_list|)
decl_stmt|;
name|markCIDAsInvalid
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
name|markCIDAsValid
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|markCIDAsValid
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
name|this
operator|.
name|computedCid
operator|.
name|put
argument_list|(
name|cid
argument_list|,
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|markCIDAsInvalid
parameter_list|(
name|int
name|cid
parameter_list|,
name|GlyphException
name|gex
parameter_list|)
block|{
name|this
operator|.
name|computedCid
operator|.
name|put
argument_list|(
name|cid
argument_list|,
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|gex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

