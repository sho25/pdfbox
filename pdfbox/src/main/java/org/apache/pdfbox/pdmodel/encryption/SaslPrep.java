begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *   https://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|encryption
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|Normalizer
import|;
end_import

begin_comment
comment|/**  * Copied from https://github.com/tombentley/saslprep/blob/master/src/main/java/SaslPrep.java on  * 30.5.2019, commit 2e30daa.  *  * @author Tom Bentley  */
end_comment

begin_class
class|class
name|SaslPrep
block|{
comment|/**      * Return the {@code SASLPrep}-canonicalised version of the given {@code str} for use as a query      * string. This implements the {@code SASLPrep} algorithm defined in      *<a href="https://tools.ietf.org/html/rfc4013">RFC 4013</a>.      *      * @param str The string to canonicalise.      * @return The canonicalised string.      * @throws IllegalArgumentException if the string contained prohibited codepoints, or broke the      * requirements for bidirectional character handling.      * @see<a href="https://tools.ietf.org/html/rfc3454#section-7">RFC 3454, Section 7</a> for      * discussion of what a query string is.      */
specifier|static
name|String
name|saslPrepQuery
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|saslPrep
argument_list|(
name|str
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Return the {@code SASLPrep}-canonicalised version of the given      * @code str} for use as a stored string. This implements the {@code SASLPrep} algorithm defined      * in      *<a href="https://tools.ietf.org/html/rfc4013">RFC 4013</a>.      *      * @param str The string to canonicalise.      * @return The canonicalised string.      * @throws IllegalArgumentException if the string contained prohibited codepoints, or broke the      * requirements for bidirectional character handling.      * @see<a href="https://tools.ietf.org/html/rfc3454#section-7">RFC 3454, Section 7</a> for      * discussion of what a stored string is.      */
specifier|static
name|String
name|saslPrepStored
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|saslPrep
argument_list|(
name|str
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|saslPrep
parameter_list|(
name|String
name|str
parameter_list|,
name|boolean
name|allowUnassigned
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
name|str
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
comment|// 1. Map
comment|// non-ASCII space chars mapped to space
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|0
init|;
name|i
operator|<
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|nonAsciiSpace
argument_list|(
name|ch
argument_list|)
condition|)
block|{
name|chars
index|[
name|i
index|]
operator|=
literal|' '
expr_stmt|;
block|}
block|}
name|int
name|length
init|=
literal|0
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
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|mappedToNothing
argument_list|(
name|ch
argument_list|)
condition|)
block|{
name|chars
index|[
name|length
operator|++
index|]
operator|=
name|ch
expr_stmt|;
block|}
block|}
comment|// 2. Normalize
name|String
name|normalized
init|=
name|Normalizer
operator|.
name|normalize
argument_list|(
name|CharBuffer
operator|.
name|wrap
argument_list|(
name|chars
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
argument_list|,
name|Normalizer
operator|.
name|Form
operator|.
name|NFKC
argument_list|)
decl_stmt|;
name|boolean
name|containsRandALCat
init|=
literal|false
decl_stmt|;
name|boolean
name|containsLCat
init|=
literal|false
decl_stmt|;
name|boolean
name|initialRandALCat
init|=
literal|false
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
name|normalized
operator|.
name|length
argument_list|()
condition|;
control|)
block|{
specifier|final
name|int
name|codepoint
init|=
name|normalized
operator|.
name|codePointAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// 3. Prohibit
if|if
condition|(
name|prohibited
argument_list|(
name|codepoint
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Prohibited character '"
operator|+
name|Character
operator|.
name|getName
argument_list|(
name|codepoint
argument_list|)
operator|+
literal|"' at position "
operator|+
name|i
argument_list|)
throw|;
block|}
comment|// 4. Check bidi
specifier|final
name|byte
name|directionality
init|=
name|Character
operator|.
name|getDirectionality
argument_list|(
name|codepoint
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|isRandALcat
init|=
name|directionality
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT
operator|||
name|directionality
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
decl_stmt|;
name|containsRandALCat
operator||=
name|isRandALcat
expr_stmt|;
name|containsLCat
operator||=
name|directionality
operator|==
name|Character
operator|.
name|DIRECTIONALITY_LEFT_TO_RIGHT
expr_stmt|;
name|initialRandALCat
operator||=
name|i
operator|==
literal|0
operator|&&
name|isRandALcat
expr_stmt|;
if|if
condition|(
operator|!
name|allowUnassigned
operator|&&
operator|!
name|Character
operator|.
name|isDefined
argument_list|(
name|codepoint
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Character at position "
operator|+
name|i
operator|+
literal|" is unassigned"
argument_list|)
throw|;
block|}
name|i
operator|+=
name|Character
operator|.
name|charCount
argument_list|(
name|codepoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|initialRandALCat
operator|&&
name|i
operator|>=
name|normalized
operator|.
name|length
argument_list|()
operator|&&
operator|!
name|isRandALcat
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"First character is RandALCat, but last character is not"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|containsRandALCat
operator|&&
name|containsLCat
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Contains both RandALCat characters and LCat characters"
argument_list|)
throw|;
block|}
return|return
name|normalized
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is a prohibited character      * as defined by      *<a href="https://tools.ietf.org/html/rfc4013#section-2.3">RFC 4013,      * Section 2.3</a>.      */
specifier|static
name|boolean
name|prohibited
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
name|nonAsciiSpace
argument_list|(
operator|(
name|char
operator|)
name|codepoint
argument_list|)
operator|||
name|asciiControl
argument_list|(
operator|(
name|char
operator|)
name|codepoint
argument_list|)
operator|||
name|nonAsciiControl
argument_list|(
name|codepoint
argument_list|)
operator|||
name|privateUse
argument_list|(
name|codepoint
argument_list|)
operator|||
name|nonCharacterCodePoint
argument_list|(
name|codepoint
argument_list|)
operator|||
name|surrogateCodePoint
argument_list|(
name|codepoint
argument_list|)
operator|||
name|inappropriateForPlainText
argument_list|(
name|codepoint
argument_list|)
operator|||
name|inappropriateForCanonical
argument_list|(
name|codepoint
argument_list|)
operator|||
name|changeDisplayProperties
argument_list|(
name|codepoint
argument_list|)
operator|||
name|tagging
argument_list|(
name|codepoint
argument_list|)
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is a tagging character      * as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.9">RFC 3454,      * Appendix C.9</a>.      */
specifier|private
specifier|static
name|boolean
name|tagging
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
name|codepoint
operator|==
literal|0xE0001
operator|||
literal|0xE0020
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xE007F
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is change display properties      * or deprecated characters as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.8">RFC 3454,      * Appendix C.8</a>.      */
specifier|private
specifier|static
name|boolean
name|changeDisplayProperties
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
name|codepoint
operator|==
literal|0x0340
operator|||
name|codepoint
operator|==
literal|0x0341
operator|||
name|codepoint
operator|==
literal|0x200E
operator|||
name|codepoint
operator|==
literal|0x200F
operator|||
name|codepoint
operator|==
literal|0x202A
operator|||
name|codepoint
operator|==
literal|0x202B
operator|||
name|codepoint
operator|==
literal|0x202C
operator|||
name|codepoint
operator|==
literal|0x202D
operator|||
name|codepoint
operator|==
literal|0x202E
operator|||
name|codepoint
operator|==
literal|0x206A
operator|||
name|codepoint
operator|==
literal|0x206B
operator|||
name|codepoint
operator|==
literal|0x206C
operator|||
name|codepoint
operator|==
literal|0x206D
operator|||
name|codepoint
operator|==
literal|0x206E
operator|||
name|codepoint
operator|==
literal|0x206F
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is inappropriate for      * canonical representation characters as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.7">RFC 3454,      * Appendix C.7</a>.      */
specifier|private
specifier|static
name|boolean
name|inappropriateForCanonical
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
literal|0x2FF0
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x2FFB
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is inappropriate for plain      * text characters as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.6">RFC 3454,      * Appendix C.6</a>.      */
specifier|private
specifier|static
name|boolean
name|inappropriateForPlainText
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
name|codepoint
operator|==
literal|0xFFF9
operator|||
name|codepoint
operator|==
literal|0xFFFA
operator|||
name|codepoint
operator|==
literal|0xFFFB
operator|||
name|codepoint
operator|==
literal|0xFFFC
operator|||
name|codepoint
operator|==
literal|0xFFFD
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is a surrogate      * code point as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.5">RFC 3454,      * Appendix C.5</a>.      */
specifier|private
specifier|static
name|boolean
name|surrogateCodePoint
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
literal|0xD800
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xDFFF
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is a non-character      * code point as defined by      *<a href="https://tools.ietf.org/html/rfc3454#appendix-C.4">RFC 3454,      * Appendix C.4</a>.      */
specifier|private
specifier|static
name|boolean
name|nonCharacterCodePoint
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
literal|0xFDD0
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xFDEF
operator|||
literal|0xFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xFFFF
operator|||
literal|0x1FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x1FFFF
operator|||
literal|0x2FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x2FFFF
operator|||
literal|0x3FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x3FFFF
operator|||
literal|0x4FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x4FFFF
operator|||
literal|0x5FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x5FFFF
operator|||
literal|0x6FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x6FFFF
operator|||
literal|0x7FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x7FFFF
operator|||
literal|0x8FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x8FFFF
operator|||
literal|0x9FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x9FFFF
operator|||
literal|0xAFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xAFFFF
operator|||
literal|0xBFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xBFFFF
operator|||
literal|0xCFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xCFFFF
operator|||
literal|0xDFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xDFFFF
operator|||
literal|0xEFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xEFFFF
operator|||
literal|0xFFFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xFFFFF
operator|||
literal|0x10FFFE
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x10FFFF
return|;
block|}
comment|/**      * Return true if the given {@code codepoint} is a private use character      * as defined by<a href="https://tools.ietf.org/html/rfc3454#appendix-C.3">RFC 3454,      * Appendix C.3</a>.      */
specifier|private
specifier|static
name|boolean
name|privateUse
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
literal|0xE000
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xF8FF
operator|||
literal|0xF000
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xFFFFD
operator|||
literal|0x100000
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x10FFFD
return|;
block|}
comment|/**      * Return true if the given {@code ch} is a non-ASCII control character      * as defined by<a href="https://tools.ietf.org/html/rfc3454#appendix-C.2.2">RFC 3454,      * Appendix C.2.2</a>.      */
specifier|private
specifier|static
name|boolean
name|nonAsciiControl
parameter_list|(
name|int
name|codepoint
parameter_list|)
block|{
return|return
literal|0x0080
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x009F
operator|||
name|codepoint
operator|==
literal|0x06DD
operator|||
name|codepoint
operator|==
literal|0x070F
operator|||
name|codepoint
operator|==
literal|0x180E
operator|||
name|codepoint
operator|==
literal|0x200C
operator|||
name|codepoint
operator|==
literal|0x200D
operator|||
name|codepoint
operator|==
literal|0x2028
operator|||
name|codepoint
operator|==
literal|0x2029
operator|||
name|codepoint
operator|==
literal|0x2060
operator|||
name|codepoint
operator|==
literal|0x2061
operator|||
name|codepoint
operator|==
literal|0x2062
operator|||
name|codepoint
operator|==
literal|0x2063
operator|||
literal|0x206A
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x206F
operator|||
name|codepoint
operator|==
literal|0xFEFF
operator|||
literal|0xFFF9
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0xFFFC
operator|||
literal|0x1D173
operator|<=
name|codepoint
operator|&&
name|codepoint
operator|<=
literal|0x1D17A
return|;
block|}
comment|/**      * Return true if the given {@code ch} is an ASCII control character      * as defined by<a href="https://tools.ietf.org/html/rfc3454#appendix-C.2.1">RFC 3454,      * Appendix C.2.1</a>.      */
specifier|private
specifier|static
name|boolean
name|asciiControl
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
literal|'\u0000'
operator|<=
name|ch
operator|&&
name|ch
operator|<=
literal|'\u001F'
operator|||
name|ch
operator|==
literal|'\u007F'
return|;
block|}
comment|/**      * Return true if the given {@code ch} is a non-ASCII space character      * as defined by<a href="https://tools.ietf.org/html/rfc3454#appendix-C.1.2">RFC 3454,      * Appendix C.1.2</a>.      */
specifier|private
specifier|static
name|boolean
name|nonAsciiSpace
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|ch
operator|==
literal|'\u00A0'
operator|||
name|ch
operator|==
literal|'\u1680'
operator|||
literal|'\u2000'
operator|<=
name|ch
operator|&&
name|ch
operator|<=
literal|'\u200B'
operator|||
name|ch
operator|==
literal|'\u202F'
operator|||
name|ch
operator|==
literal|'\u205F'
operator|||
name|ch
operator|==
literal|'\u3000'
return|;
block|}
comment|/**      * Return true if the given {@code ch} is a "commonly mapped to nothing" character      * as defined by<a href="https://tools.ietf.org/html/rfc3454#appendix-B.1">RFC 3454,      * Appendix B.1</a>.      */
specifier|private
specifier|static
name|boolean
name|mappedToNothing
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|ch
operator|==
literal|'\u00AD'
operator|||
name|ch
operator|==
literal|'\u034F'
operator|||
name|ch
operator|==
literal|'\u1806'
operator|||
name|ch
operator|==
literal|'\u180B'
operator|||
name|ch
operator|==
literal|'\u180C'
operator|||
name|ch
operator|==
literal|'\u180D'
operator|||
name|ch
operator|==
literal|'\u200B'
operator|||
name|ch
operator|==
literal|'\u200C'
operator|||
name|ch
operator|==
literal|'\u200D'
operator|||
name|ch
operator|==
literal|'\u2060'
operator|||
literal|'\uFE00'
operator|<=
name|ch
operator|&&
name|ch
operator|<=
literal|'\uFE0F'
operator|||
name|ch
operator|==
literal|'\uFEFF'
return|;
block|}
block|}
end_class

end_unit

