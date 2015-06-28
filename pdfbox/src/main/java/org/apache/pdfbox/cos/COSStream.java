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
name|cos
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

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
name|Closeable
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|DecodeResult
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterFactory
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
name|io
operator|.
name|IOUtils
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
name|io
operator|.
name|RandomAccess
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
name|io
operator|.
name|RandomAccessBuffer
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
name|io
operator|.
name|RandomAccessReadInputStream
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
name|io
operator|.
name|RandomAccessFileOutputStream
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
name|io
operator|.
name|RandomAccessRead
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
name|io
operator|.
name|RandomAccessReadWrapper
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
name|io
operator|.
name|ScratchFile
import|;
end_import

begin_comment
comment|/**  * This class represents a stream object in a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|COSStream
extends|extends
name|COSDictionary
implements|implements
name|Closeable
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|16384
decl_stmt|;
comment|/**      * internal buffer, either held in memory or within a scratch file.      */
specifier|private
name|RandomAccess
name|tempBuffer
decl_stmt|;
specifier|private
name|RandomAccess
name|filteredBuffer
decl_stmt|;
specifier|private
name|RandomAccess
name|unfilteredBuffer
decl_stmt|;
comment|/**      * The stream with all of the filters applied.      */
specifier|private
name|RandomAccessFileOutputStream
name|filteredStream
decl_stmt|;
comment|/**      * The stream with no filters, this contains the useful data.      */
specifier|private
name|RandomAccessFileOutputStream
name|unFilteredStream
decl_stmt|;
specifier|private
name|DecodeResult
name|decodeResult
decl_stmt|;
specifier|private
specifier|final
name|ScratchFile
name|scratchFile
decl_stmt|;
comment|/**      * Constructor.  Creates a new stream with an empty dictionary.      *      */
specifier|public
name|COSStream
parameter_list|( )
block|{
name|this
argument_list|(
operator|(
name|ScratchFile
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dictionary The dictionary that is associated with this stream.      *      */
specifier|public
name|COSStream
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
argument_list|(
name|dictionary
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.  Creates a new stream with an empty dictionary.      *      * @param scratchFile scratch file to use.      *      */
specifier|public
name|COSStream
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|scratchFile
operator|=
name|scratchFile
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dictionary The dictionary that is associated with this stream.      * @param scratchFile The scratch file to use.      *      */
specifier|public
name|COSStream
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|scratchFile
operator|=
name|scratchFile
expr_stmt|;
block|}
specifier|private
name|RandomAccess
name|createBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|scratchFile
operator|!=
literal|null
condition|)
block|{
return|return
name|scratchFile
operator|.
name|createBuffer
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|RandomAccessBuffer
argument_list|()
return|;
block|}
block|}
comment|/**      * This will get the stream with all of the filters applied.      *      * @return the bytes of the physical (encoded) stream      *      * @throws IOException when encoding causes an exception      */
specifier|public
name|InputStream
name|getFilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|checkFilteredBuffer
argument_list|()
expr_stmt|;
name|InputStream
name|retval
decl_stmt|;
if|if
condition|(
name|filteredStream
operator|!=
literal|null
condition|)
block|{
name|long
name|position
init|=
name|filteredStream
operator|.
name|getPosition
argument_list|()
decl_stmt|;
name|long
name|length
init|=
name|filteredStream
operator|.
name|getLengthWritten
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|RandomAccessReadInputStream
argument_list|(
name|getFilteredBuffer
argument_list|()
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
argument_list|,
name|BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the data with all of the filters applied.      *      * @return the data of the physical (encoded) stream      *      * @throws IOException when encoding causes an exception      */
specifier|public
name|RandomAccessRead
name|getFilteredRandomAccess
parameter_list|()
throws|throws
name|IOException
block|{
name|checkFilteredBuffer
argument_list|()
expr_stmt|;
name|RandomAccessRead
name|retval
decl_stmt|;
if|if
condition|(
name|filteredStream
operator|!=
literal|null
condition|)
block|{
name|long
name|position
init|=
name|filteredStream
operator|.
name|getPosition
argument_list|()
decl_stmt|;
name|long
name|length
init|=
name|filteredStream
operator|.
name|getLengthWritten
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|RandomAccessReadWrapper
argument_list|(
name|getFilteredBuffer
argument_list|()
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|RandomAccessBuffer
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|void
name|checkFilteredBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getFilteredBuffer
argument_list|()
operator|!=
literal|null
operator|&&
name|getFilteredBuffer
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"COSStream has been closed and cannot be read. "
operator|+
literal|"Perhaps its enclosing PDDocument has been closed?"
argument_list|)
throw|;
block|}
if|if
condition|(
name|filteredStream
operator|==
literal|null
condition|)
block|{
name|doEncode
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will get the length of the encoded stream.      *      * @return the length of the encoded stream as long      *      * @throws IOException if something went wrong      */
specifier|public
name|long
name|getFilteredLength
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|filteredStream
operator|==
literal|null
condition|)
block|{
name|doEncode
argument_list|()
expr_stmt|;
block|}
return|return
name|filteredStream
operator|.
name|getLength
argument_list|()
return|;
block|}
comment|/**      * This will get the logical content stream with none of the filters.      *      * @return the bytes of the logical (decoded) stream      *      * @throws IOException when decoding causes an exception      */
specifier|public
name|InputStream
name|getUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|checkUnfilteredBuffer
argument_list|()
expr_stmt|;
name|InputStream
name|retval
decl_stmt|;
if|if
condition|(
name|unFilteredStream
operator|!=
literal|null
condition|)
block|{
name|long
name|position
init|=
name|unFilteredStream
operator|.
name|getPosition
argument_list|()
decl_stmt|;
name|long
name|length
init|=
name|unFilteredStream
operator|.
name|getLengthWritten
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|RandomAccessReadInputStream
argument_list|(
name|getUnfilteredBuffer
argument_list|()
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
argument_list|,
name|BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the logical content with none of the filters.      *      * @return the bytes of the logical (decoded) stream      *      * @throws IOException when decoding causes an exception      */
specifier|public
name|RandomAccessRead
name|getUnfilteredRandomAccess
parameter_list|()
throws|throws
name|IOException
block|{
name|checkUnfilteredBuffer
argument_list|()
expr_stmt|;
name|RandomAccessRead
name|retval
decl_stmt|;
if|if
condition|(
name|unFilteredStream
operator|!=
literal|null
condition|)
block|{
name|long
name|position
init|=
name|unFilteredStream
operator|.
name|getPosition
argument_list|()
decl_stmt|;
name|long
name|length
init|=
name|unFilteredStream
operator|.
name|getLengthWritten
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|RandomAccessReadWrapper
argument_list|(
name|getUnfilteredBuffer
argument_list|()
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|RandomAccessBuffer
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|void
name|checkUnfilteredBuffer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getUnfilteredBuffer
argument_list|()
operator|!=
literal|null
operator|&&
name|getUnfilteredBuffer
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"COSStream has been closed and cannot be read. "
operator|+
literal|"Perhaps its enclosing PDDocument has been closed?"
argument_list|)
throw|;
block|}
if|if
condition|(
name|unFilteredStream
operator|==
literal|null
condition|)
block|{
name|doDecode
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the repaired stream parameters dictionary.      *      * @return the repaired stream parameters dictionary      * @throws IOException when encoding/decoding causes an exception      */
specifier|public
name|DecodeResult
name|getDecodeResult
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|unFilteredStream
operator|==
literal|null
condition|)
block|{
name|doDecode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|unFilteredStream
operator|==
literal|null
operator|||
name|decodeResult
operator|==
literal|null
condition|)
block|{
name|StringBuilder
name|filterInfo
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|COSBase
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|!=
literal|null
condition|)
block|{
name|filterInfo
operator|.
name|append
argument_list|(
literal|" - filter: "
argument_list|)
expr_stmt|;
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|filterInfo
operator|.
name|append
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|filters
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|filterArray
init|=
operator|(
name|COSArray
operator|)
name|filters
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
name|filterArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|filterArray
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|filterInfo
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|filterInfo
operator|.
name|append
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|filterArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|subtype
init|=
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|subtype
operator|+
literal|" stream was not read"
operator|+
name|filterInfo
argument_list|)
throw|;
block|}
return|return
name|decodeResult
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|visitor
operator|.
name|visitFromStream
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * This will decode the physical byte stream applying all of the filters to the stream.      *      * @throws IOException If there is an error applying a filter to the stream.      */
specifier|private
name|void
name|doDecode
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|==
literal|null
condition|)
block|{
comment|// do nothing but copying the data
name|unFilteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|getUnfilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|getFilteredStream
argument_list|()
argument_list|,
name|unFilteredStream
argument_list|)
expr_stmt|;
name|decodeResult
operator|=
name|DecodeResult
operator|.
name|DEFAULT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|copyBuffer
argument_list|(
name|getFilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
name|filteredStream
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|doDecode
argument_list|(
operator|(
name|COSName
operator|)
name|filters
argument_list|,
literal|0
argument_list|,
name|getFilteredBuffer
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
name|getUnfilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|copyBuffer
argument_list|(
name|getFilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
name|filteredStream
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|COSArray
name|filterArray
init|=
operator|(
name|COSArray
operator|)
name|filters
decl_stmt|;
name|int
name|filterArraysize
init|=
name|filterArray
operator|.
name|size
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
name|filterArraysize
condition|;
name|i
operator|++
control|)
block|{
name|COSName
name|filterName
init|=
operator|(
name|COSName
operator|)
name|filterArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|doDecode
argument_list|(
name|filterName
argument_list|,
name|i
argument_list|,
name|getFilteredBuffer
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
name|getUnfilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|filterArraysize
operator|-
literal|1
condition|)
block|{
name|copyBuffer
argument_list|(
name|getUnfilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|tempBuffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown filter type:"
operator|+
name|filters
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|copyBuffer
parameter_list|(
name|RandomAccess
name|srcBuffer
parameter_list|,
name|long
name|position
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|length
init|=
call|(
name|int
call|)
argument_list|(
name|srcBuffer
operator|.
name|length
argument_list|()
operator|-
name|position
argument_list|)
decl_stmt|;
name|byte
index|[]
name|byteBuffer
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|srcBuffer
operator|.
name|seek
argument_list|(
name|position
argument_list|)
expr_stmt|;
name|srcBuffer
operator|.
name|read
argument_list|(
name|byteBuffer
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|tempBuffer
operator|==
literal|null
condition|)
block|{
name|tempBuffer
operator|=
operator|new
name|RandomAccessBuffer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|tempBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|tempBuffer
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|tempBuffer
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will decode applying a single filter on the stream.      *      * @param filterName The name of the filter.      * @param filterIndex The index of the current filter.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|private
name|RandomAccess
name|doDecode
parameter_list|(
name|COSName
name|filterName
parameter_list|,
name|int
name|filterIndex
parameter_list|,
name|long
name|length
parameter_list|,
name|RandomAccess
name|destBuffer
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccess
name|result
init|=
name|destBuffer
decl_stmt|;
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|filterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|createBuffer
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|attemptDecode
argument_list|(
name|filter
argument_list|,
name|filterIndex
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// attempts to decode the stream at the given position and length
specifier|private
name|RandomAccess
name|attemptDecode
parameter_list|(
name|Filter
name|filter
parameter_list|,
name|int
name|filterIndex
parameter_list|,
name|RandomAccess
name|destBuffer
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
name|RandomAccess
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|RandomAccessReadInputStream
argument_list|(
name|tempBuffer
argument_list|,
literal|0
argument_list|,
name|tempBuffer
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|BUFFER_SIZE
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|unFilteredStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|destBuffer
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|createBuffer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|destBuffer
expr_stmt|;
block|}
name|unFilteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|decodeResult
operator|=
name|filter
operator|.
name|decode
argument_list|(
name|input
argument_list|,
name|unFilteredStream
argument_list|,
name|this
argument_list|,
name|filterIndex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * This will encode the logical byte stream applying all of the filters to the stream.      *      * @throws IOException If there is an error applying a filter to the stream.      */
specifier|private
name|void
name|doEncode
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|==
literal|null
condition|)
block|{
comment|// there is no filter to apply
comment|// do nothing but copying the data
name|filteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|getFilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|getUnfilteredStream
argument_list|()
argument_list|,
name|filteredStream
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|copyBuffer
argument_list|(
name|getUnfilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
name|unFilteredStream
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
name|doEncode
argument_list|(
operator|(
name|COSName
operator|)
name|filters
argument_list|,
literal|0
argument_list|,
name|getFilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|copyBuffer
argument_list|(
name|getUnfilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
name|unFilteredStream
operator|.
name|getPosition
argument_list|()
argument_list|)
expr_stmt|;
comment|// apply filters in reverse order
name|COSArray
name|filterArray
init|=
operator|(
name|COSArray
operator|)
name|filters
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|filterArray
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
name|i
operator|--
control|)
block|{
name|COSName
name|filterName
init|=
operator|(
name|COSName
operator|)
name|filterArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|doEncode
argument_list|(
name|filterName
argument_list|,
name|i
argument_list|,
name|getFilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|copyBuffer
argument_list|(
name|getFilteredBuffer
argument_list|(
literal|false
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
name|tempBuffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will encode applying a single filter on the stream.      *      * @param filterName The name of the filter.      * @param filterIndex The index to the filter.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|private
name|RandomAccess
name|doEncode
parameter_list|(
name|COSName
name|filterName
parameter_list|,
name|int
name|filterIndex
parameter_list|,
name|RandomAccess
name|destBuffer
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccess
name|result
init|=
literal|null
decl_stmt|;
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|filterName
argument_list|)
decl_stmt|;
name|InputStream
name|input
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|RandomAccessReadInputStream
argument_list|(
name|tempBuffer
argument_list|,
literal|0
argument_list|,
name|tempBuffer
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|BUFFER_SIZE
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|filteredStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|destBuffer
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|createBuffer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|destBuffer
expr_stmt|;
block|}
name|filteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|filter
operator|.
name|encode
argument_list|(
name|input
argument_list|,
name|filteredStream
argument_list|,
name|this
argument_list|,
name|filterIndex
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * This will return the filters to apply to the byte stream.      * The method will return      * - null if no filters are to be applied      * - a COSName if one filter is to be applied      * - a COSArray containing COSNames if multiple filters are to be applied      *      * @return the COSBase object representing the filters      */
specifier|public
name|COSBase
name|getFilters
parameter_list|()
block|{
return|return
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
return|;
block|}
comment|/**      * Returns the contents of the stream as a text string.      */
specifier|public
name|String
name|getString
parameter_list|()
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
name|getUnfilteredStream
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
name|COSString
name|string
init|=
operator|new
name|COSString
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|string
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * This will create a new stream for which filtered byte should be      * written to.  You probably don't want this but want to use the      * createUnfilteredStream, which is used to write raw bytes to.      *      * @return A stream that can be written to.      *      * @throws IOException If there is an error creating the stream.      */
specifier|public
name|OutputStream
name|createFilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|unFilteredStream
argument_list|)
expr_stmt|;
name|unFilteredStream
operator|=
literal|null
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|filteredStream
argument_list|)
expr_stmt|;
name|filteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|getFilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|BufferedOutputStream
argument_list|(
name|filteredStream
argument_list|,
name|BUFFER_SIZE
argument_list|)
return|;
block|}
comment|/**      * This will create a new stream for which filtered byte should be      * written to.  You probably don't want this but want to use the      * createUnfilteredStream, which is used to write raw bytes to.      *      * @param expectedLength An entry where a length is expected.      *      * @return A stream that can be written to.      *      * @throws IOException If there is an error creating the stream.      */
specifier|public
name|OutputStream
name|createFilteredStream
parameter_list|(
name|COSBase
name|expectedLength
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|out
init|=
name|createFilteredStream
argument_list|()
decl_stmt|;
name|filteredStream
operator|.
name|setExpectedLength
argument_list|(
name|expectedLength
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
comment|/**      * set the filters to be applied to the stream.      *      * @param filters The filters to set on this stream.      *      * @throws IOException If there is an error clearing the old filters.      */
specifier|public
name|void
name|setFilters
parameter_list|(
name|COSBase
name|filters
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|unFilteredStream
operator|==
literal|null
condition|)
block|{
comment|// don't lose stream contents
name|doDecode
argument_list|()
expr_stmt|;
block|}
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|filters
argument_list|)
expr_stmt|;
comment|// kill cached filtered streams
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|filteredStream
argument_list|)
expr_stmt|;
name|filteredStream
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will create an output stream that can be written to.      *      * @return An output stream which raw data bytes should be written to.      *      * @throws IOException If there is an error creating the stream.      */
specifier|public
name|OutputStream
name|createUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|filteredStream
argument_list|)
expr_stmt|;
name|filteredStream
operator|=
literal|null
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|unFilteredStream
argument_list|)
expr_stmt|;
name|unFilteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|getUnfilteredBuffer
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|BufferedOutputStream
argument_list|(
name|unFilteredStream
argument_list|,
name|BUFFER_SIZE
argument_list|)
return|;
block|}
specifier|private
name|RandomAccess
name|getFilteredBuffer
parameter_list|()
block|{
return|return
name|filteredBuffer
return|;
block|}
specifier|private
name|RandomAccess
name|getFilteredBuffer
parameter_list|(
name|boolean
name|clear
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|filteredBuffer
operator|==
literal|null
condition|)
block|{
name|filteredBuffer
operator|=
name|createBuffer
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clear
condition|)
block|{
name|filteredBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|filteredBuffer
return|;
block|}
specifier|private
name|RandomAccess
name|getUnfilteredBuffer
parameter_list|()
block|{
return|return
name|unfilteredBuffer
return|;
block|}
specifier|private
name|RandomAccess
name|getUnfilteredBuffer
parameter_list|(
name|boolean
name|clear
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|unfilteredBuffer
operator|==
literal|null
condition|)
block|{
name|unfilteredBuffer
operator|=
name|createBuffer
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clear
condition|)
block|{
name|unfilteredBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|unfilteredBuffer
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|filteredStream
operator|!=
literal|null
condition|)
block|{
name|filteredStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|unFilteredStream
operator|!=
literal|null
condition|)
block|{
name|unFilteredStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|unfilteredBuffer
operator|!=
literal|null
condition|)
block|{
name|unfilteredBuffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filteredBuffer
operator|!=
literal|null
condition|)
block|{
name|filteredBuffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

