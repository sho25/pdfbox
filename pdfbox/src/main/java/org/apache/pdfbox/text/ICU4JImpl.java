begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|text
package|;
end_package

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|icu
operator|.
name|text
operator|.
name|Bidi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|icu
operator|.
name|text
operator|.
name|Normalizer
import|;
end_import

begin_comment
comment|/**  * This class is an implementation the the ICU4J class. TextNormalize   * will call this only if the ICU4J library exists in the classpath.  *  * @author Brian Carrier  */
end_comment

begin_class
specifier|public
class|class
name|ICU4JImpl
block|{
specifier|private
name|Bidi
name|bidi
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|ICU4JImpl
parameter_list|()
block|{
name|bidi
operator|=
operator|new
name|Bidi
argument_list|()
expr_stmt|;
comment|// We do not use bidi.setInverse() because that uses Bidi.REORDER_INVERSE_NUMBERS_AS_L,
comment|// which caused problems in some test files. For example, a file had a line of:
comment|// 0 1 / ARABIC  and the 0 and 1 were reversed in the end result.
comment|// REORDER_INVERSE_LIKE_DIRECT is the inverse Bidi mode that more closely reflects the
comment|// Unicode spec.
name|bidi
operator|.
name|setReorderingMode
argument_list|(
name|Bidi
operator|.
name|REORDER_INVERSE_LIKE_DIRECT
argument_list|)
expr_stmt|;
block|}
comment|/**      * Takes a line of text in presentation order and converts it to logical order.      *        * @param str String to convert      * @param isRtlDominant RTL (right-to-left) will be the dominant text direction      * @return The converted string      */
specifier|public
name|String
name|makeLineLogicalOrder
parameter_list|(
name|String
name|str
parameter_list|,
name|boolean
name|isRtlDominant
parameter_list|)
block|{
name|bidi
operator|.
name|setPara
argument_list|(
name|str
argument_list|,
name|isRtlDominant
condition|?
name|Bidi
operator|.
name|RTL
else|:
name|Bidi
operator|.
name|LTR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Set the mirror flag so that parentheses and other mirror symbols are properly reversed,
comment|// when needed.  With this removed, lines such as (CBA) in the PDF file will come out like
comment|// )ABC( in logical order.
return|return
name|bidi
operator|.
name|writeReordered
argument_list|(
name|Bidi
operator|.
name|DO_MIRRORING
argument_list|)
return|;
block|}
comment|/**      * Normalize presentation forms of characters to the separate parts.       * @see org.apache.pdfbox.text.TextNormalize#normalizePresentationForm(String)      *       * @param str String to normalize      * @return Normalized form      */
specifier|public
name|String
name|normalizePres
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
literal|null
decl_stmt|;
name|int
name|p
init|=
literal|0
decl_stmt|;
name|int
name|q
init|=
literal|0
decl_stmt|;
name|int
name|strLength
init|=
name|str
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|q
operator|<
name|strLength
condition|;
name|q
operator|++
control|)
block|{
comment|// We only normalize if the codepoint is in a given range.
comment|// Otherwise, NFKC converts too many things that would cause
comment|// confusion. For example, it converts the micro symbol in
comment|// extended Latin to the value in the Greek script. We normalize
comment|// the Unicode Alphabetic and Arabic A&B Presentation forms.
name|char
name|c
init|=
name|str
operator|.
name|charAt
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0xFB00
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|0xFDFF
operator|||
literal|0xFE70
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|0xFEFF
condition|)
block|{
if|if
condition|(
name|builder
operator|==
literal|null
condition|)
block|{
name|builder
operator|=
operator|new
name|StringBuilder
argument_list|(
name|strLength
operator|*
literal|2
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|str
operator|.
name|substring
argument_list|(
name|p
argument_list|,
name|q
argument_list|)
argument_list|)
expr_stmt|;
comment|// Some fonts map U+FDF2 differently than the Unicode spec.
comment|// They add an extra U+0627 character to compensate.
comment|// This removes the extra character for those fonts.
if|if
condition|(
name|c
operator|==
literal|0xFDF2
operator|&&
name|q
operator|>
literal|0
operator|&&
operator|(
name|str
operator|.
name|charAt
argument_list|(
name|q
operator|-
literal|1
argument_list|)
operator|==
literal|0x0627
operator|||
name|str
operator|.
name|charAt
argument_list|(
name|q
operator|-
literal|1
argument_list|)
operator|==
literal|0xFE8D
operator|)
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\u0644\u0644\u0647"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Trim because some decompositions have an extra space, such as U+FC5E
name|builder
operator|.
name|append
argument_list|(
name|Normalizer
operator|.
name|normalize
argument_list|(
name|c
argument_list|,
name|Normalizer
operator|.
name|NFKC
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|q
operator|+
literal|1
expr_stmt|;
block|}
block|}
if|if
condition|(
name|builder
operator|==
literal|null
condition|)
block|{
return|return
name|str
return|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|str
operator|.
name|substring
argument_list|(
name|p
argument_list|,
name|q
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**      * Decomposes Diacritic characters to their combining forms.      *       * @param str String to be Normalized      * @return A Normalized String      */
specifier|public
name|String
name|normalizeDiac
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|StringBuilder
name|retStr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|strLength
init|=
name|str
operator|.
name|length
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
name|strLength
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|type
init|=
name|Character
operator|.
name|getType
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Character
operator|.
name|NON_SPACING_MARK
operator|||
name|type
operator|==
name|Character
operator|.
name|MODIFIER_SYMBOL
operator|||
name|type
operator|==
name|Character
operator|.
name|MODIFIER_LETTER
condition|)
block|{
comment|// trim because some decompositions have an extra space, such as U+00B4
name|retStr
operator|.
name|append
argument_list|(
name|Normalizer
operator|.
name|normalize
argument_list|(
name|c
argument_list|,
name|Normalizer
operator|.
name|NFKC
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retStr
operator|.
name|append
argument_list|(
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retStr
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

