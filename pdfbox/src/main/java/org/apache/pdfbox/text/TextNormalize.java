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
name|java
operator|.
name|text
operator|.
name|Normalizer
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

begin_comment
comment|/**  * This class allows a caller to normalize text in various ways.  *   * @author Brian Carrier  */
end_comment

begin_class
specifier|public
class|class
name|TextNormalize
block|{
specifier|private
specifier|static
specifier|final
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|DIACRITICS
init|=
name|createDiacritics
argument_list|()
decl_stmt|;
comment|// Adds non-decomposing diacritics to the hash with their related combining character.
comment|// These are values that the unicode spec claims are equivalent but are not mapped in the form
comment|// NFKC normalization method. Determined by going through the Combining Diacritical Marks
comment|// section of the Unicode spec and identifying which characters are not  mapped to by the
comment|// normalization.
specifier|private
specifier|static
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createDiacritics
parameter_list|()
block|{
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0060
argument_list|,
literal|"\u0300"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02CB
argument_list|,
literal|"\u0300"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0027
argument_list|,
literal|"\u0301"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02B9
argument_list|,
literal|"\u0301"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02CA
argument_list|,
literal|"\u0301"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x005e
argument_list|,
literal|"\u0302"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02C6
argument_list|,
literal|"\u0302"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x007E
argument_list|,
literal|"\u0303"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02C9
argument_list|,
literal|"\u0304"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x00B0
argument_list|,
literal|"\u030A"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02BA
argument_list|,
literal|"\u030B"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02C7
argument_list|,
literal|"\u030C"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02C8
argument_list|,
literal|"\u030D"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0022
argument_list|,
literal|"\u030E"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02BB
argument_list|,
literal|"\u0312"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02BC
argument_list|,
literal|"\u0313"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0486
argument_list|,
literal|"\u0313"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x055A
argument_list|,
literal|"\u0313"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02BD
argument_list|,
literal|"\u0314"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0485
argument_list|,
literal|"\u0314"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x0559
argument_list|,
literal|"\u0314"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02D4
argument_list|,
literal|"\u031D"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02D5
argument_list|,
literal|"\u031E"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02D6
argument_list|,
literal|"\u031F"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02D7
argument_list|,
literal|"\u0320"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02B2
argument_list|,
literal|"\u0321"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02CC
argument_list|,
literal|"\u0329"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02B7
argument_list|,
literal|"\u032B"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x02CD
argument_list|,
literal|"\u0331"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x005F
argument_list|,
literal|"\u0332"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|0x204E
argument_list|,
literal|"\u0359"
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|private
name|String
name|outputEncoding
decl_stmt|;
comment|/**      *       * @param encoding The Encoding that the text will eventually be written as (or null)      */
specifier|public
name|TextNormalize
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|outputEncoding
operator|=
name|encoding
expr_stmt|;
block|}
comment|/**      * Normalize the presentation forms of characters in the string. For example, convert the      * single "fi" ligature to "f" and "i".      *       * @param str String to normalize      * @return Normalized string      */
specifier|public
name|String
name|normalizePresentationForm
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
name|str
operator|.
name|substring
argument_list|(
name|q
argument_list|,
name|q
operator|+
literal|1
argument_list|)
argument_list|,
name|Normalizer
operator|.
name|Form
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
comment|/**      * Normalize the diacritic, for example, convert non-combining diacritic characters to their      * combining counterparts.      *       * @param str String to normalize      * @return Normalized string      */
specifier|public
name|String
name|normalizeDiacritic
parameter_list|(
name|String
name|str
parameter_list|)
block|{
comment|// Unicode contains special combining forms of the diacritic characters which we want to use
if|if
condition|(
name|outputEncoding
operator|!=
literal|null
operator|&&
name|outputEncoding
operator|.
name|toUpperCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"UTF"
argument_list|)
condition|)
block|{
name|Integer
name|c
init|=
operator|(
name|int
operator|)
name|str
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// convert the characters not defined in the Unicode spec
if|if
condition|(
name|DIACRITICS
operator|.
name|containsKey
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
name|DIACRITICS
operator|.
name|get
argument_list|(
name|c
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Normalizer
operator|.
name|normalize
argument_list|(
name|str
argument_list|,
name|Normalizer
operator|.
name|Form
operator|.
name|NFKC
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
name|str
return|;
block|}
block|}
block|}
end_class

end_unit

