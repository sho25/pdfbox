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
name|fontbox
operator|.
name|ttf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|TimeZone
import|;
end_import

begin_comment
comment|/**  * An interface into a data stream.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|abstract
class|class
name|TTFDataStream
implements|implements
name|Closeable
block|{
name|TTFDataStream
parameter_list|()
block|{     }
comment|/**      * Read a 16.16 fixed value, where the first 16 bits are the decimal and the last 16 bits are the fraction.      *       * @return A 32 bit value.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|float
name|read32Fixed
parameter_list|()
throws|throws
name|IOException
block|{
name|float
name|retval
init|=
literal|0
decl_stmt|;
name|retval
operator|=
name|readSignedShort
argument_list|()
expr_stmt|;
name|retval
operator|+=
operator|(
name|readUnsignedShort
argument_list|()
operator|/
literal|65536.0
operator|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Read a fixed length ascii string.      *       * @param length The length of the string to read.      * @return A string of the desired length.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|String
name|readString
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readString
argument_list|(
name|length
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
return|;
block|}
comment|/**      * Read a fixed length string.      *       * @param length The length of the string to read in bytes.      * @param charset The expected character set of the string.      * @return A string of the desired length.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|String
name|readString
parameter_list|(
name|int
name|length
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
name|read
argument_list|(
name|length
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
name|charset
argument_list|)
return|;
block|}
comment|/**      * Read a fixed length string.      *       * @param length The length of the string to read in bytes.      * @param charset The expected character set of the string.      * @return A string of the desired length.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|String
name|readString
parameter_list|(
name|int
name|length
parameter_list|,
name|Charset
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
name|read
argument_list|(
name|length
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
name|charset
argument_list|)
return|;
block|}
comment|/**      * Read an unsigned byte.      *       * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
specifier|abstract
name|int
name|read
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Read an unsigned byte.      *       * @return An unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
specifier|abstract
name|long
name|readLong
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Read a signed byte.      *       * @return A signed byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
name|readSignedByte
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|signedByte
init|=
name|read
argument_list|()
decl_stmt|;
return|return
name|signedByte
operator|<=
literal|127
condition|?
name|signedByte
else|:
name|signedByte
operator|-
literal|256
return|;
block|}
comment|/**      * Read a unsigned byte. Similar to {@link #read()}, but throws an exception if EOF is unexpectedly reached.      *       * @return A unsigned byte.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
name|readUnsignedByte
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|unsignedByte
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|unsignedByte
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|(
literal|"premature EOF"
argument_list|)
throw|;
block|}
return|return
name|unsignedByte
return|;
block|}
comment|/**      * Read an unsigned integer.      *       * @return An unsigned integer.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|long
name|readUnsignedInt
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|byte1
init|=
name|read
argument_list|()
decl_stmt|;
name|long
name|byte2
init|=
name|read
argument_list|()
decl_stmt|;
name|long
name|byte3
init|=
name|read
argument_list|()
decl_stmt|;
name|long
name|byte4
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|byte4
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
return|return
operator|(
name|byte1
operator|<<
literal|24
operator|)
operator|+
operator|(
name|byte2
operator|<<
literal|16
operator|)
operator|+
operator|(
name|byte3
operator|<<
literal|8
operator|)
operator|+
operator|(
name|byte4
operator|<<
literal|0
operator|)
return|;
block|}
comment|/**      * Read an unsigned short.      *       * @return An unsigned short.      * @throws IOException If there is an error reading the data.      */
specifier|public
specifier|abstract
name|int
name|readUnsignedShort
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Read an unsigned byte array.      *       * @param length the length of the array to be read      * @return An unsigned byte array.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
index|[]
name|readUnsignedByteArray
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|int
index|[]
name|array
init|=
operator|new
name|int
index|[
name|length
index|]
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|array
index|[
name|i
index|]
operator|=
name|read
argument_list|()
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**      * Read an unsigned short array.      *       * @param length The length of the array to read.      * @return An unsigned short array.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|int
index|[]
name|readUnsignedShortArray
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|int
index|[]
name|array
init|=
operator|new
name|int
index|[
name|length
index|]
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|array
index|[
name|i
index|]
operator|=
name|readUnsignedShort
argument_list|()
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**      * Read an signed short.      *       * @return An signed short.      * @throws IOException If there is an error reading the data.      */
specifier|public
specifier|abstract
name|short
name|readSignedShort
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Read an eight byte international date.      *       * @return An signed short.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|Calendar
name|readInternationalDate
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|secondsSince1904
init|=
name|readLong
argument_list|()
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|1904
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|long
name|millisFor1904
init|=
name|cal
operator|.
name|getTimeInMillis
argument_list|()
decl_stmt|;
name|millisFor1904
operator|+=
operator|(
name|secondsSince1904
operator|*
literal|1000
operator|)
expr_stmt|;
name|cal
operator|.
name|setTimeInMillis
argument_list|(
name|millisFor1904
argument_list|)
expr_stmt|;
return|return
name|cal
return|;
block|}
comment|/**      * Reads a tag, an array of four uint8s used to identify a script, language system, feature,      * or baseline.      */
specifier|public
name|String
name|readTag
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|String
argument_list|(
name|read
argument_list|(
literal|4
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|US_ASCII
argument_list|)
return|;
block|}
comment|/**      * Seek into the datasource.      *       * @param pos The position to seek to.      * @throws IOException If there is an error seeking to that position.      */
specifier|public
specifier|abstract
name|void
name|seek
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Read a specific number of bytes from the stream.      *       * @param numberOfBytes The number of bytes to read.      * @return The byte buffer.      * @throws IOException If there is an error while reading.      */
specifier|public
name|byte
index|[]
name|read
parameter_list|(
name|int
name|numberOfBytes
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
name|numberOfBytes
index|]
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
name|int
name|totalAmountRead
init|=
literal|0
decl_stmt|;
comment|// read at most numberOfBytes bytes from the stream.
while|while
condition|(
name|totalAmountRead
operator|<
name|numberOfBytes
operator|&&
operator|(
name|amountRead
operator|=
name|read
argument_list|(
name|data
argument_list|,
name|totalAmountRead
argument_list|,
name|numberOfBytes
operator|-
name|totalAmountRead
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|totalAmountRead
operator|+=
name|amountRead
expr_stmt|;
block|}
if|if
condition|(
name|totalAmountRead
operator|==
name|numberOfBytes
condition|)
block|{
return|return
name|data
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected end of TTF stream reached"
argument_list|)
throw|;
block|}
block|}
comment|/**      * @see java.io.InputStream#read(byte[], int, int )      *       * @param b The buffer to write to.      * @param off The offset into the buffer.      * @param len The length into the buffer.      *       * @return The number of bytes read, or -1 at the end of the stream      *       * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|abstract
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Get the current position in the stream.      *       * @return The current position in the stream.      * @throws IOException If an error occurs while reading the stream.      */
specifier|public
specifier|abstract
name|long
name|getCurrentPosition
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * This will get the original data file that was used for this stream.      *       * @return The data that was read from.      * @throws IOException If there is an issue reading the data.      */
specifier|public
specifier|abstract
name|InputStream
name|getOriginalData
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * This will get the original data size that was used for this stream.      *       * @return The size of the original data.      * @throws IOException If there is an issue reading the data.      */
specifier|public
specifier|abstract
name|long
name|getOriginalDataSize
parameter_list|()
function_decl|;
block|}
end_class

end_unit

