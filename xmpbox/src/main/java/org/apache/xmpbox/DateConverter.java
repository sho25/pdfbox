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
name|xmpbox
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SimpleTimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * This class is used to convert dates to strings and back using the PDF date standards. Date are described in  * PDFReference1.4 section 3.8.2  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author<a href="mailto:chris@oezbek.net">Christopher Oezbek</a>  *   * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|DateConverter
block|{
comment|// The Date format is supposed to be the PDF_DATE_FORMAT, but not all PDF
comment|// documents
comment|// will use that date, so I have added a couple other potential formats
comment|// to try if the original one does not work.
specifier|private
specifier|static
specifier|final
name|SimpleDateFormat
index|[]
name|POTENTIAL_FORMATS
init|=
operator|new
name|SimpleDateFormat
index|[]
block|{
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEEE, dd MMM yyyy hh:mm:ss a"
argument_list|)
block|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEEE, MMM dd, yyyy hh:mm:ss a"
argument_list|)
block|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss'Z'"
argument_list|)
block|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ssz"
argument_list|)
block|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss"
argument_list|)
block|,
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.S"
argument_list|)
block|}
decl_stmt|;
comment|/**      * According to check-style, Utility classes should not have a public or default constructor.      */
specifier|protected
name|DateConverter
parameter_list|()
block|{     }
empty_stmt|;
comment|/**      * This will convert a string to a calendar.      *       * @param date      *            The string representation of the calendar.      *       * @return The calendar that this string represents.      *       * @throws IOException      *             If the date string is not in the correct format.      */
specifier|public
specifier|static
name|Calendar
name|toCalendar
parameter_list|(
name|String
name|date
parameter_list|)
throws|throws
name|IOException
block|{
name|Calendar
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|date
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|date
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
comment|// these are the default values
name|int
name|year
init|=
literal|0
decl_stmt|;
name|int
name|month
init|=
literal|1
decl_stmt|;
name|int
name|day
init|=
literal|1
decl_stmt|;
name|int
name|hour
init|=
literal|0
decl_stmt|;
name|int
name|minute
init|=
literal|0
decl_stmt|;
name|int
name|second
init|=
literal|0
decl_stmt|;
comment|// first string off the prefix if it exists
try|try
block|{
name|SimpleTimeZone
name|zone
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Pattern
operator|.
name|matches
argument_list|(
literal|"^\\d{4}-\\d{2}-\\d{2}T.*"
argument_list|,
name|date
argument_list|)
condition|)
block|{
comment|// Assuming ISO860 date string
return|return
name|fromISO8601
argument_list|(
name|date
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|date
operator|.
name|startsWith
argument_list|(
literal|"D:"
argument_list|)
condition|)
block|{
name|date
operator|=
name|date
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
name|date
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|date
operator|=
name|date
operator|.
name|replaceAll
argument_list|(
literal|"[-:T]"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|<
literal|4
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Invalid date format '"
operator|+
name|date
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|year
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
literal|6
condition|)
block|{
name|month
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|4
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
literal|8
condition|)
block|{
name|day
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|6
argument_list|,
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
literal|10
condition|)
block|{
name|hour
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|8
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
literal|12
condition|)
block|{
name|minute
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|10
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|int
name|timeZonePos
init|=
literal|12
decl_stmt|;
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|12
operator|>
literal|5
operator|||
operator|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|12
operator|==
literal|3
operator|&&
name|date
operator|.
name|endsWith
argument_list|(
literal|"Z"
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
literal|14
condition|)
block|{
name|second
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|12
argument_list|,
literal|14
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|timeZonePos
operator|=
literal|14
expr_stmt|;
block|}
else|else
block|{
name|second
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
operator|(
name|timeZonePos
operator|+
literal|1
operator|)
condition|)
block|{
name|char
name|sign
init|=
name|date
operator|.
name|charAt
argument_list|(
name|timeZonePos
argument_list|)
decl_stmt|;
if|if
condition|(
name|sign
operator|==
literal|'Z'
condition|)
block|{
name|zone
operator|=
operator|new
name|SimpleTimeZone
argument_list|(
literal|0
argument_list|,
literal|"Unknown"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|hours
init|=
literal|0
decl_stmt|;
name|int
name|minutes
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
operator|(
name|timeZonePos
operator|+
literal|3
operator|)
condition|)
block|{
if|if
condition|(
name|sign
operator|==
literal|'+'
condition|)
block|{
comment|// parseInt cannot handle the + sign
name|hours
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
operator|(
name|timeZonePos
operator|+
literal|1
operator|)
argument_list|,
operator|(
name|timeZonePos
operator|+
literal|3
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hours
operator|=
operator|-
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
name|timeZonePos
argument_list|,
operator|(
name|timeZonePos
operator|+
literal|2
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sign
operator|==
literal|'+'
condition|)
block|{
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
operator|(
name|timeZonePos
operator|+
literal|5
operator|)
condition|)
block|{
name|minutes
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
operator|(
name|timeZonePos
operator|+
literal|3
operator|)
argument_list|,
operator|(
name|timeZonePos
operator|+
literal|5
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|date
operator|.
name|length
argument_list|()
operator|>=
operator|(
name|timeZonePos
operator|+
literal|4
operator|)
condition|)
block|{
name|minutes
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
operator|(
name|timeZonePos
operator|+
literal|2
operator|)
argument_list|,
operator|(
name|timeZonePos
operator|+
literal|4
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|zone
operator|=
operator|new
name|SimpleTimeZone
argument_list|(
name|hours
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000
operator|+
name|minutes
operator|*
literal|60
operator|*
literal|1000
argument_list|,
literal|"Unknown"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|zone
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|GregorianCalendar
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|GregorianCalendar
argument_list|(
name|zone
argument_list|)
expr_stmt|;
block|}
name|retval
operator|.
name|clear
argument_list|()
expr_stmt|;
name|retval
operator|.
name|set
argument_list|(
name|year
argument_list|,
name|month
operator|-
literal|1
argument_list|,
name|day
argument_list|,
name|hour
argument_list|,
name|minute
argument_list|,
name|second
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// remove the arbitrary : in the timezone. SimpleDateFormat
comment|// can't handle it
if|if
condition|(
name|date
operator|.
name|substring
argument_list|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|,
name|date
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
operator|.
name|equals
argument_list|(
literal|":"
argument_list|)
operator|&&
operator|(
name|date
operator|.
name|substring
argument_list|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|6
argument_list|,
name|date
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
operator|.
name|equals
argument_list|(
literal|"+"
argument_list|)
operator|||
name|date
operator|.
name|substring
argument_list|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|6
argument_list|,
name|date
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
operator|.
name|equals
argument_list|(
literal|"-"
argument_list|)
operator|)
condition|)
block|{
comment|// thats a timezone string, remove the :
name|date
operator|=
name|date
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|date
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
operator|+
name|date
operator|.
name|substring
argument_list|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|retval
operator|==
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|POTENTIAL_FORMATS
operator|.
name|length
operator|)
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Date
name|utilDate
init|=
name|POTENTIAL_FORMATS
index|[
name|i
index|]
operator|.
name|parse
argument_list|(
name|date
argument_list|)
decl_stmt|;
name|retval
operator|=
operator|new
name|GregorianCalendar
argument_list|()
expr_stmt|;
name|retval
operator|.
name|setTime
argument_list|(
name|utilDate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|pe
parameter_list|)
block|{
comment|// ignore and move to next potential format
block|}
block|}
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
comment|// we didn't find a valid date format so throw an exception
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error converting date:"
operator|+
name|date
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Append Zero to String Buffer.      *       * This will append zero before number&lt; 10 ('1' become '01')      *       * @param out The String buffer      * @param number The concerned number      */
specifier|private
specifier|static
name|void
name|zeroAppend
parameter_list|(
name|StringBuffer
name|out
parameter_list|,
name|int
name|number
parameter_list|)
block|{
if|if
condition|(
name|number
operator|<
literal|10
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|number
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert the date to iso 8601 string format.      *       * @param cal      *            The date to convert.      * @return The date represented as an ISO 8601 string.      */
specifier|public
specifier|static
name|String
name|toISO8601
parameter_list|(
name|Calendar
name|cal
parameter_list|)
block|{
name|StringBuffer
name|retval
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|retval
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|zeroAppend
argument_list|(
name|retval
argument_list|,
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|zeroAppend
argument_list|(
name|retval
argument_list|,
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|"T"
argument_list|)
expr_stmt|;
name|zeroAppend
argument_list|(
name|retval
argument_list|,
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|zeroAppend
argument_list|(
name|retval
argument_list|,
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|zeroAppend
argument_list|(
name|retval
argument_list|,
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|timeZone
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|ZONE_OFFSET
argument_list|)
operator|+
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DST_OFFSET
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeZone
operator|<
literal|0
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|.
name|append
argument_list|(
literal|"+"
argument_list|)
expr_stmt|;
block|}
name|timeZone
operator|=
name|Math
operator|.
name|abs
argument_list|(
name|timeZone
argument_list|)
expr_stmt|;
comment|// milliseconds/1000 = seconds = seconds / 60 = minutes = minutes/60 =
comment|// hours
name|int
name|hours
init|=
name|timeZone
operator|/
literal|1000
operator|/
literal|60
operator|/
literal|60
decl_stmt|;
name|int
name|minutes
init|=
operator|(
name|timeZone
operator|-
operator|(
name|hours
operator|*
literal|1000
operator|*
literal|60
operator|*
literal|60
operator|)
operator|)
operator|/
literal|1000
operator|/
literal|1000
decl_stmt|;
if|if
condition|(
name|hours
operator|<
literal|10
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
block|}
name|retval
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|hours
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|minutes
operator|<
literal|10
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
block|}
name|retval
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|minutes
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Get a Calendar from an ISO8601 date string.      *       * @param dateString      * @return the Calendar instance.      */
specifier|private
specifier|static
name|Calendar
name|fromISO8601
parameter_list|(
name|String
name|dateString
parameter_list|)
block|{
comment|// Pattern to test for a time zone string
name|Pattern
name|timeZonePattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[\\d-]*T?[\\d-\\.]([A-Z]{1,4})$|(.*\\d*)([A-Z][a-z]+\\/[A-Z][a-z]+)$"
argument_list|)
decl_stmt|;
name|Matcher
name|timeZoneMatcher
init|=
name|timeZonePattern
operator|.
name|matcher
argument_list|(
name|dateString
argument_list|)
decl_stmt|;
name|String
name|timeZoneString
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|timeZoneMatcher
operator|.
name|find
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|timeZoneMatcher
operator|.
name|groupCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|timeZoneMatcher
operator|.
name|group
argument_list|(
name|i
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|timeZoneString
operator|=
name|timeZoneMatcher
operator|.
name|group
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|timeZoneString
operator|!=
literal|null
condition|)
block|{
name|Calendar
name|cal
init|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
name|dateString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dateString
operator|.
name|indexOf
argument_list|(
name|timeZoneString
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|TimeZone
name|z
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
name|timeZoneString
argument_list|)
decl_stmt|;
name|cal
operator|.
name|setTimeZone
argument_list|(
name|z
argument_list|)
expr_stmt|;
return|return
name|cal
return|;
block|}
else|else
block|{
return|return
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
name|dateString
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

