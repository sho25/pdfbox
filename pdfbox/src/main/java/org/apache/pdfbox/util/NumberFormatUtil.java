begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * This class contains methods to format numbers.  *  * @author Michael Doswald  */
end_comment

begin_class
specifier|public
class|class
name|NumberFormatUtil
block|{
comment|/**      * Maximum number of fraction digits supported by the format methods      */
specifier|private
specifier|static
specifier|final
name|int
name|MAX_FRACTION_DIGITS
init|=
literal|5
decl_stmt|;
comment|/**      * Contains the power of ten values for fast lookup in the format methods      */
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|POWER_OF_TENS
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|POWER_OF_TENS_INT
decl_stmt|;
static|static
block|{
name|POWER_OF_TENS
operator|=
operator|new
name|long
index|[
literal|19
index|]
expr_stmt|;
name|POWER_OF_TENS
index|[
literal|0
index|]
operator|=
literal|1
expr_stmt|;
for|for
control|(
name|int
name|exp
init|=
literal|1
init|;
name|exp
operator|<
name|POWER_OF_TENS
operator|.
name|length
condition|;
name|exp
operator|++
control|)
block|{
name|POWER_OF_TENS
index|[
name|exp
index|]
operator|=
name|POWER_OF_TENS
index|[
name|exp
operator|-
literal|1
index|]
operator|*
literal|10
expr_stmt|;
block|}
name|POWER_OF_TENS_INT
operator|=
operator|new
name|int
index|[
literal|10
index|]
expr_stmt|;
name|POWER_OF_TENS_INT
index|[
literal|0
index|]
operator|=
literal|1
expr_stmt|;
for|for
control|(
name|int
name|exp
init|=
literal|1
init|;
name|exp
operator|<
name|POWER_OF_TENS_INT
operator|.
name|length
condition|;
name|exp
operator|++
control|)
block|{
name|POWER_OF_TENS_INT
index|[
name|exp
index|]
operator|=
name|POWER_OF_TENS_INT
index|[
name|exp
operator|-
literal|1
index|]
operator|*
literal|10
expr_stmt|;
block|}
block|}
comment|/**      * Fast variant to format a floating point value to a ASCII-string. The format will fail if the      * value is greater than {@link Long#MAX_VALUE}, smaller or equal to {@link Long#MIN_VALUE}, is      * {@link Float#NaN}, infinite or the number of requested fraction digits is greater than      * {@link #MAX_FRACTION_DIGITS}.      *       * When the number contains more fractional digits than {@code maxFractionDigits} the value will      * be rounded. Rounding is done to the nearest possible value, with the tie breaking rule of       * rounding away from zero.      *       * @param value The float value to format      * @param maxFractionDigits The maximum number of fraction digits used      * @param asciiBuffer The output buffer to write the formatted value to      *      * @return The number of bytes used in the buffer or {@code -1} if formatting failed      */
specifier|public
specifier|static
name|int
name|formatFloatFast
parameter_list|(
name|float
name|value
parameter_list|,
name|int
name|maxFractionDigits
parameter_list|,
name|byte
index|[]
name|asciiBuffer
parameter_list|)
block|{
if|if
condition|(
name|Float
operator|.
name|isNaN
argument_list|(
name|value
argument_list|)
operator|||
name|Float
operator|.
name|isInfinite
argument_list|(
name|value
argument_list|)
operator|||
name|value
operator|>
name|Long
operator|.
name|MAX_VALUE
operator|||
name|value
operator|<=
name|Long
operator|.
name|MIN_VALUE
operator|||
name|maxFractionDigits
operator|>
name|MAX_FRACTION_DIGITS
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|offset
init|=
literal|0
decl_stmt|;
name|long
name|integerPart
init|=
operator|(
name|long
operator|)
name|value
decl_stmt|;
comment|//handle sign
if|if
condition|(
name|value
operator|<
literal|0
condition|)
block|{
name|asciiBuffer
index|[
name|offset
operator|++
index|]
operator|=
literal|'-'
expr_stmt|;
name|integerPart
operator|=
operator|-
name|integerPart
expr_stmt|;
block|}
comment|//extract fraction part
name|long
name|fractionPart
init|=
call|(
name|long
call|)
argument_list|(
operator|(
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|double
operator|)
name|value
argument_list|)
operator|-
name|integerPart
operator|)
operator|*
name|POWER_OF_TENS
index|[
name|maxFractionDigits
index|]
operator|+
literal|0.5d
argument_list|)
decl_stmt|;
comment|//Check for rounding to next integer
if|if
condition|(
name|fractionPart
operator|>=
name|POWER_OF_TENS
index|[
name|maxFractionDigits
index|]
condition|)
block|{
name|integerPart
operator|++
expr_stmt|;
name|fractionPart
operator|-=
name|POWER_OF_TENS
index|[
name|maxFractionDigits
index|]
expr_stmt|;
block|}
comment|//format integer part
name|offset
operator|=
name|formatPositiveNumber
argument_list|(
name|integerPart
argument_list|,
name|getExponent
argument_list|(
name|integerPart
argument_list|)
argument_list|,
literal|false
argument_list|,
name|asciiBuffer
argument_list|,
name|offset
argument_list|)
expr_stmt|;
if|if
condition|(
name|fractionPart
operator|>
literal|0
operator|&&
name|maxFractionDigits
operator|>
literal|0
condition|)
block|{
name|asciiBuffer
index|[
name|offset
operator|++
index|]
operator|=
literal|'.'
expr_stmt|;
name|offset
operator|=
name|formatPositiveNumber
argument_list|(
name|fractionPart
argument_list|,
name|maxFractionDigits
operator|-
literal|1
argument_list|,
literal|true
argument_list|,
name|asciiBuffer
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
return|return
name|offset
return|;
block|}
comment|/**      * Formats a positive integer number starting with the digit at {@code 10^exp}.      *      * @param number The number to format      * @param exp The start digit      * @param omitTrailingZeros Whether the formatting should stop if only trailing zeros are left.      * This is needed e.g. when formatting fractions of a number.      * @param asciiBuffer The buffer to write the ASCII digits to      * @param startOffset The start offset into the buffer to start writing      *      * @return The offset into the buffer which contains the first byte that was not filled by the      * method      */
specifier|private
specifier|static
name|int
name|formatPositiveNumber
parameter_list|(
name|long
name|number
parameter_list|,
name|int
name|exp
parameter_list|,
name|boolean
name|omitTrailingZeros
parameter_list|,
name|byte
index|[]
name|asciiBuffer
parameter_list|,
name|int
name|startOffset
parameter_list|)
block|{
name|int
name|offset
init|=
name|startOffset
decl_stmt|;
name|long
name|remaining
init|=
name|number
decl_stmt|;
while|while
condition|(
name|remaining
operator|>
name|Integer
operator|.
name|MAX_VALUE
operator|&&
operator|(
operator|!
name|omitTrailingZeros
operator|||
name|remaining
operator|>
literal|0
operator|)
condition|)
block|{
name|long
name|digit
init|=
name|remaining
operator|/
name|POWER_OF_TENS
index|[
name|exp
index|]
decl_stmt|;
name|remaining
operator|-=
operator|(
name|digit
operator|*
name|POWER_OF_TENS
index|[
name|exp
index|]
operator|)
expr_stmt|;
name|asciiBuffer
index|[
name|offset
operator|++
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
literal|'0'
operator|+
name|digit
argument_list|)
expr_stmt|;
name|exp
operator|--
expr_stmt|;
block|}
comment|//If the remaining fits into an integer, use int arithmetic as it is faster
name|int
name|remainingInt
init|=
operator|(
name|int
operator|)
name|remaining
decl_stmt|;
while|while
condition|(
name|exp
operator|>=
literal|0
operator|&&
operator|(
operator|!
name|omitTrailingZeros
operator|||
name|remainingInt
operator|>
literal|0
operator|)
condition|)
block|{
name|int
name|digit
init|=
name|remainingInt
operator|/
name|POWER_OF_TENS_INT
index|[
name|exp
index|]
decl_stmt|;
name|remainingInt
operator|-=
operator|(
name|digit
operator|*
name|POWER_OF_TENS_INT
index|[
name|exp
index|]
operator|)
expr_stmt|;
name|asciiBuffer
index|[
name|offset
operator|++
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
literal|'0'
operator|+
name|digit
argument_list|)
expr_stmt|;
name|exp
operator|--
expr_stmt|;
block|}
return|return
name|offset
return|;
block|}
comment|/**      * Returns the highest exponent of 10 where {@code 10^exp< number} for numbers> 0      */
specifier|private
specifier|static
name|int
name|getExponent
parameter_list|(
name|long
name|number
parameter_list|)
block|{
for|for
control|(
name|int
name|exp
init|=
literal|0
init|;
name|exp
operator|<
operator|(
name|POWER_OF_TENS
operator|.
name|length
operator|-
literal|1
operator|)
condition|;
name|exp
operator|++
control|)
block|{
if|if
condition|(
name|number
operator|<
name|POWER_OF_TENS
index|[
name|exp
operator|+
literal|1
index|]
condition|)
block|{
return|return
name|exp
return|;
block|}
block|}
return|return
name|POWER_OF_TENS
operator|.
name|length
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit
