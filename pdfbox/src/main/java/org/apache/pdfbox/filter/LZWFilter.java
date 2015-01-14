begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|OutputStream
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
name|Arrays
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
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|COSName
import|;
end_import

begin_comment
comment|/**  *  * This is the filter used for the LZWDecode filter.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|LZWFilter
extends|extends
name|Filter
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LZWFilter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The LZW clear table code.      */
specifier|public
specifier|static
specifier|final
name|long
name|CLEAR_TABLE
init|=
literal|256
decl_stmt|;
comment|/**      * The LZW end of data code.      */
specifier|public
specifier|static
specifier|final
name|long
name|EOD
init|=
literal|257
decl_stmt|;
comment|//BEWARE: codeTable must be local to each method, because there is only
comment|// one instance of each filter
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
specifier|final
name|DecodeResult
name|decode
parameter_list|(
name|InputStream
name|encoded
parameter_list|,
name|OutputStream
name|decoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|predictor
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|earlyChange
init|=
literal|1
decl_stmt|;
name|COSDictionary
name|decodeParams
init|=
name|getDecodeParams
argument_list|(
name|parameters
argument_list|,
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|decodeParams
operator|!=
literal|null
condition|)
block|{
name|predictor
operator|=
name|decodeParams
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|PREDICTOR
argument_list|)
expr_stmt|;
name|earlyChange
operator|=
name|decodeParams
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|EARLY_CHANGE
argument_list|,
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|earlyChange
operator|!=
literal|0
operator|&&
name|earlyChange
operator|!=
literal|1
condition|)
block|{
name|earlyChange
operator|=
literal|1
expr_stmt|;
block|}
block|}
if|if
condition|(
name|predictor
operator|>
literal|1
condition|)
block|{
name|int
name|colors
init|=
name|Math
operator|.
name|min
argument_list|(
name|decodeParams
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLORS
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|32
argument_list|)
decl_stmt|;
name|int
name|bitsPerPixel
init|=
name|decodeParams
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|int
name|columns
init|=
name|decodeParams
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|doLZWDecode
argument_list|(
name|encoded
argument_list|,
name|baos
argument_list|,
name|earlyChange
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Predictor
operator|.
name|decodePredictor
argument_list|(
name|predictor
argument_list|,
name|colors
argument_list|,
name|bitsPerPixel
argument_list|,
name|columns
argument_list|,
name|bais
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
name|decoded
operator|.
name|flush
argument_list|()
expr_stmt|;
name|baos
operator|.
name|reset
argument_list|()
expr_stmt|;
name|bais
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doLZWDecode
argument_list|(
name|encoded
argument_list|,
name|decoded
argument_list|,
name|earlyChange
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DecodeResult
argument_list|(
name|parameters
argument_list|)
return|;
block|}
specifier|private
name|void
name|doLZWDecode
parameter_list|(
name|InputStream
name|encoded
parameter_list|,
name|OutputStream
name|decoded
parameter_list|,
name|int
name|earlyChange
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|codeTable
init|=
literal|null
decl_stmt|;
name|int
name|chunk
init|=
literal|9
decl_stmt|;
name|MemoryCacheImageInputStream
name|in
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|long
name|nextCommand
decl_stmt|;
name|long
name|prevCommand
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|nextCommand
operator|=
name|in
operator|.
name|readBits
argument_list|(
name|chunk
argument_list|)
operator|)
operator|!=
name|EOD
condition|)
block|{
if|if
condition|(
name|nextCommand
operator|==
name|CLEAR_TABLE
condition|)
block|{
name|chunk
operator|=
literal|9
expr_stmt|;
name|codeTable
operator|=
name|createCodeTable
argument_list|()
expr_stmt|;
name|prevCommand
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|nextCommand
operator|<
name|codeTable
operator|.
name|size
argument_list|()
condition|)
block|{
name|byte
index|[]
name|data
init|=
name|codeTable
operator|.
name|get
argument_list|(
operator|(
name|int
operator|)
name|nextCommand
argument_list|)
decl_stmt|;
name|byte
name|firstByte
init|=
name|data
index|[
literal|0
index|]
decl_stmt|;
name|decoded
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
if|if
condition|(
name|prevCommand
operator|!=
operator|-
literal|1
condition|)
block|{
name|data
operator|=
name|codeTable
operator|.
name|get
argument_list|(
operator|(
name|int
operator|)
name|prevCommand
argument_list|)
expr_stmt|;
name|byte
index|[]
name|newData
init|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|data
argument_list|,
name|data
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|newData
index|[
name|data
operator|.
name|length
index|]
operator|=
name|firstByte
expr_stmt|;
name|codeTable
operator|.
name|add
argument_list|(
name|newData
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|byte
index|[]
name|data
init|=
name|codeTable
operator|.
name|get
argument_list|(
operator|(
name|int
operator|)
name|prevCommand
argument_list|)
decl_stmt|;
name|byte
index|[]
name|newData
init|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|data
argument_list|,
name|data
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|newData
index|[
name|data
operator|.
name|length
index|]
operator|=
name|data
index|[
literal|0
index|]
expr_stmt|;
name|decoded
operator|.
name|write
argument_list|(
name|newData
argument_list|)
expr_stmt|;
name|codeTable
operator|.
name|add
argument_list|(
name|newData
argument_list|)
expr_stmt|;
block|}
name|chunk
operator|=
name|calculateChunk
argument_list|(
name|codeTable
operator|.
name|size
argument_list|()
argument_list|,
name|earlyChange
argument_list|)
expr_stmt|;
name|prevCommand
operator|=
name|nextCommand
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Premature EOF in LZW stream, EOD code missing"
argument_list|)
expr_stmt|;
block|}
name|decoded
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|encoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|codeTable
init|=
name|createCodeTable
argument_list|()
decl_stmt|;
name|int
name|chunk
init|=
literal|9
decl_stmt|;
name|byte
index|[]
name|inputPattern
init|=
literal|null
decl_stmt|;
name|MemoryCacheImageOutputStream
name|out
init|=
operator|new
name|MemoryCacheImageOutputStream
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|out
operator|.
name|writeBits
argument_list|(
name|CLEAR_TABLE
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
name|int
name|foundCode
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|r
decl_stmt|;
while|while
condition|(
operator|(
name|r
operator|=
name|rawData
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|byte
name|by
init|=
operator|(
name|byte
operator|)
name|r
decl_stmt|;
if|if
condition|(
name|inputPattern
operator|==
literal|null
condition|)
block|{
name|inputPattern
operator|=
operator|new
name|byte
index|[]
block|{
name|by
block|}
expr_stmt|;
name|foundCode
operator|=
name|by
operator|&
literal|0xff
expr_stmt|;
block|}
else|else
block|{
name|inputPattern
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|inputPattern
argument_list|,
name|inputPattern
operator|.
name|length
operator|+
literal|1
argument_list|)
expr_stmt|;
name|inputPattern
index|[
name|inputPattern
operator|.
name|length
operator|-
literal|1
index|]
operator|=
name|by
expr_stmt|;
name|int
name|newFoundCode
init|=
name|findPatternCode
argument_list|(
name|codeTable
argument_list|,
name|inputPattern
argument_list|)
decl_stmt|;
if|if
condition|(
name|newFoundCode
operator|==
operator|-
literal|1
condition|)
block|{
comment|// use previous
name|chunk
operator|=
name|calculateChunk
argument_list|(
name|codeTable
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBits
argument_list|(
name|foundCode
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
comment|// create new table entry
name|codeTable
operator|.
name|add
argument_list|(
name|inputPattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|codeTable
operator|.
name|size
argument_list|()
operator|==
literal|4096
condition|)
block|{
comment|// code table is full
name|out
operator|.
name|writeBits
argument_list|(
name|CLEAR_TABLE
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
name|codeTable
operator|=
name|createCodeTable
argument_list|()
expr_stmt|;
block|}
name|inputPattern
operator|=
operator|new
name|byte
index|[]
block|{
name|by
block|}
expr_stmt|;
name|foundCode
operator|=
name|by
operator|&
literal|0xff
expr_stmt|;
block|}
else|else
block|{
name|foundCode
operator|=
name|newFoundCode
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|foundCode
operator|!=
operator|-
literal|1
condition|)
block|{
name|chunk
operator|=
name|calculateChunk
argument_list|(
name|codeTable
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBits
argument_list|(
name|foundCode
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
block|}
comment|// PPDFBOX-1977: the decoder wouldn't know that the encoder would output
comment|// an EOD as code, so he would have increased his own code table and
comment|// possibly adjusted the chunk. Therefore, the encoder must behave as
comment|// if the code table had just grown and thus it must be checked it is
comment|// needed to adjust the chunk, based on an increased table size parameter
name|chunk
operator|=
name|calculateChunk
argument_list|(
name|codeTable
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBits
argument_list|(
name|EOD
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
comment|// pad with 0
name|out
operator|.
name|writeBits
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|)
expr_stmt|;
comment|// must do or file will be empty :-(
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Find the longest matching pattern in the code table.      *      * @param codeTable The LZW code table.      * @param pattern The pattern to be searched for.      * @return The index of the longest matching pattern or -1 if nothing is      * found.      */
specifier|private
name|int
name|findPatternCode
parameter_list|(
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|codeTable
parameter_list|,
name|byte
index|[]
name|pattern
parameter_list|)
block|{
name|int
name|foundCode
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|foundLen
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|codeTable
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
operator|--
name|i
control|)
block|{
if|if
condition|(
name|i
operator|<=
name|EOD
condition|)
block|{
comment|// we're in the single byte area
if|if
condition|(
name|foundCode
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// we already found pattern with size> 1
return|return
name|foundCode
return|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|length
operator|>
literal|1
condition|)
block|{
comment|// we won't find anything here anyway
return|return
operator|-
literal|1
return|;
block|}
block|}
name|byte
index|[]
name|tryPattern
init|=
name|codeTable
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|foundCode
operator|!=
operator|-
literal|1
operator|||
name|tryPattern
operator|.
name|length
operator|>
name|foundLen
operator|)
operator|&&
name|Arrays
operator|.
name|equals
argument_list|(
name|tryPattern
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
name|foundCode
operator|=
name|i
expr_stmt|;
name|foundLen
operator|=
name|tryPattern
operator|.
name|length
expr_stmt|;
block|}
block|}
return|return
name|foundCode
return|;
block|}
comment|/**      * Init the code table with 1 byte entries and the EOD and CLEAR_TABLE      * markers.      */
specifier|private
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|createCodeTable
parameter_list|()
block|{
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|codeTable
init|=
operator|new
name|ArrayList
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|4096
argument_list|)
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
literal|256
condition|;
operator|++
name|i
control|)
block|{
name|codeTable
operator|.
name|add
argument_list|(
operator|new
name|byte
index|[]
block|{
call|(
name|byte
call|)
argument_list|(
name|i
operator|&
literal|0xFF
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
name|codeTable
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// 256 EOD
name|codeTable
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// 257 CLEAR_TABLE
return|return
name|codeTable
return|;
block|}
comment|/**      * Calculate the appropriate chunk size      *      * @param tabSize the size of the code table      * @param earlyChange 0 or 1 for early chunk increase      *      * @return a value between 9 and 12      */
specifier|private
name|int
name|calculateChunk
parameter_list|(
name|int
name|tabSize
parameter_list|,
name|int
name|earlyChange
parameter_list|)
block|{
if|if
condition|(
name|tabSize
operator|>=
literal|2048
operator|-
name|earlyChange
condition|)
block|{
return|return
literal|12
return|;
block|}
if|if
condition|(
name|tabSize
operator|>=
literal|1024
operator|-
name|earlyChange
condition|)
block|{
return|return
literal|11
return|;
block|}
if|if
condition|(
name|tabSize
operator|>=
literal|512
operator|-
name|earlyChange
condition|)
block|{
return|return
literal|10
return|;
block|}
return|return
literal|9
return|;
block|}
block|}
end_class

end_unit

