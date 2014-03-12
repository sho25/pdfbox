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
name|InputStream
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
name|OutputStream
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
name|*
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
name|pdfparser
operator|.
name|PDFStreamParser
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

begin_comment
comment|/**  * This class represents a stream object in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  */
end_comment

begin_class
specifier|public
class|class
name|COSStream
extends|extends
name|COSDictionary
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
name|COSStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|16384
decl_stmt|;
specifier|private
name|RandomAccess
name|file
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
name|RandomAccess
name|clone
parameter_list|(
name|RandomAccess
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|file
operator|instanceof
name|RandomAccessFile
condition|)
block|{
return|return
name|file
return|;
block|}
else|else
block|{
return|return
operator|(
operator|(
name|RandomAccessBuffer
operator|)
name|file
operator|)
operator|.
name|clone
argument_list|()
return|;
block|}
block|}
comment|/**      * Constructor.  Creates a new stream with an empty dictionary.      *      * @param storage The intermediate storage for the stream.      */
specifier|public
name|COSStream
parameter_list|(
name|RandomAccess
name|storage
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|file
operator|=
name|clone
argument_list|(
name|storage
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dictionary The dictionary that is associated with this stream.      * @param storage The intermediate storage for the stream.      */
specifier|public
name|COSStream
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|,
name|RandomAccess
name|storage
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
name|file
operator|=
name|clone
argument_list|(
name|storage
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will replace this object with the data from the new object.  This      * is used to easily maintain referential integrity when changing references      * to new objects.      *      * @param stream The stream that have the new values in it.      */
specifier|public
name|void
name|replaceWithStream
parameter_list|(
name|COSStream
name|stream
parameter_list|)
block|{
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|addAll
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|file
operator|=
name|stream
operator|.
name|file
expr_stmt|;
name|filteredStream
operator|=
name|stream
operator|.
name|filteredStream
expr_stmt|;
name|unFilteredStream
operator|=
name|stream
operator|.
name|unFilteredStream
expr_stmt|;
block|}
comment|/**      * This will get the scratch file associated with this stream.      *      * @return The scratch file where this stream is being stored.      */
specifier|public
name|RandomAccess
name|getScratchFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
comment|/**      * This will get all the tokens in the stream.      *      * @return All of the tokens in the stream.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getStreamTokens
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getTokens
argument_list|()
return|;
block|}
comment|/**      * This will get the stream with all of the filters applied.      *      * @return the bytes of the physical (endoced) stream      *      * @throws IOException when encoding/decoding causes an exception      */
specifier|public
name|InputStream
name|getFilteredStream
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
name|getLength
argument_list|()
decl_stmt|;
name|RandomAccessFileInputStream
name|input
init|=
operator|new
name|RandomAccessFileInputStream
argument_list|(
name|file
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
decl_stmt|;
return|return
operator|new
name|BufferedInputStream
argument_list|(
name|input
argument_list|,
name|BUFFER_SIZE
argument_list|)
return|;
block|}
comment|/**      * This will get the length of the encoded stream.      *       * @return the length of the encoded stream as long      *      * @throws IOException       */
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
comment|/**      * This will get the logical content stream with none of the filters.      *      * @return the bytes of the logical (decoded) stream      *      * @throws IOException when encoding/decoding causes an exception      */
specifier|public
name|InputStream
name|getUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|retval
init|=
literal|null
decl_stmt|;
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
comment|//if unFilteredStream is still null then this stream has not been
comment|//created yet, so we should return null.
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
name|getLength
argument_list|()
decl_stmt|;
name|RandomAccessFileInputStream
name|input
init|=
operator|new
name|RandomAccessFileInputStream
argument_list|(
name|file
argument_list|,
name|position
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|retval
operator|=
operator|new
name|BufferedInputStream
argument_list|(
name|input
argument_list|,
name|BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// We should check if the COSStream contains data, maybe it
comment|// has been created with a RandomAccessFile - which is not
comment|// necessary empty.
comment|// In this case, the creation was been done as an input, this should
comment|// be the unfiltered file, since no filter has been applied yet.
comment|//            if ( (file != null)&&
comment|//                    (file.length()> 0) )
comment|//            {
comment|//                retval = new RandomAccessFileInputStream( file,
comment|//                                                          0,
comment|//                                                          file.length() );
comment|//            }
comment|//            else
comment|//            {
comment|//if there is no stream data then simply return an empty stream.
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
comment|//            }
block|}
return|return
name|retval
return|;
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Stream was not read"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|decodeResult
return|;
block|}
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      */
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
comment|// FIXME: We shouldn't keep the same reference?
name|unFilteredStream
operator|=
name|filteredStream
expr_stmt|;
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
comment|//then do nothing
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
name|doDecode
argument_list|(
operator|(
name|COSName
operator|)
name|filters
argument_list|,
literal|0
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
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * This will decode applying a single filter on the stream.      *      * @param filterName The name of the filter.      * @param filterIndex The index of the current filter.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|private
name|void
name|doDecode
parameter_list|(
name|COSName
name|filterName
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
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
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|IOException
name|exception
init|=
literal|null
decl_stmt|;
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
name|getLength
argument_list|()
decl_stmt|;
comment|// in case we need it later
name|long
name|writtenLength
init|=
name|unFilteredStream
operator|.
name|getLengthWritten
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|0
condition|)
block|{
comment|//if the length is zero then don't bother trying to decode
comment|//some filters don't work when attempting to decode
comment|//with a zero length stream.  See zlib_error_01.pdf
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
name|file
argument_list|)
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|//ok this is a simple hack, sometimes we read a couple extra
comment|//bytes that shouldn't be there, so we encounter an error we will just
comment|//try again with one less byte.
for|for
control|(
name|int
name|tryCount
init|=
literal|0
init|;
operator|!
name|done
operator|&&
name|tryCount
operator|<
literal|5
condition|;
name|tryCount
operator|++
control|)
block|{
name|InputStream
name|input
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
name|RandomAccessFileInputStream
argument_list|(
name|file
argument_list|,
name|position
argument_list|,
name|length
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
name|unFilteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|file
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
name|done
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
name|length
operator|--
expr_stmt|;
name|exception
operator|=
name|io
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
block|}
if|if
condition|(
operator|!
name|done
condition|)
block|{
comment|//if no good stream was found then lets try again but with the
comment|//length of data that was actually read and not length
comment|//defined in the dictionary
name|length
operator|=
name|writtenLength
expr_stmt|;
for|for
control|(
name|int
name|tryCount
init|=
literal|0
init|;
operator|!
name|done
operator|&&
name|tryCount
operator|<
literal|5
condition|;
name|tryCount
operator|++
control|)
block|{
name|InputStream
name|input
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
name|RandomAccessFileInputStream
argument_list|(
name|file
argument_list|,
name|position
argument_list|,
name|length
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
name|unFilteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|file
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
name|done
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
name|length
operator|--
expr_stmt|;
name|exception
operator|=
name|io
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
block|}
block|}
block|}
if|if
condition|(
operator|!
name|done
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
block|}
comment|/**      * This will encode the logical byte stream applying all of the filters to the stream.      *      * @throws IOException If there is an error applying a filter to the stream.      */
specifier|private
name|void
name|doEncode
parameter_list|()
throws|throws
name|IOException
block|{
name|filteredStream
operator|=
name|unFilteredStream
expr_stmt|;
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
comment|//there is no filter to apply
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|doEncode
argument_list|(
operator|(
name|COSName
operator|)
name|filters
argument_list|,
literal|0
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will encode applying a single filter on the stream.      *      * @param filterName The name of the filter.      * @param filterIndex The index to the filter.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|private
name|void
name|doEncode
parameter_list|(
name|COSName
name|filterName
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
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
name|RandomAccessFileInputStream
argument_list|(
name|file
argument_list|,
name|filteredStream
operator|.
name|getPosition
argument_list|()
argument_list|,
name|filteredStream
operator|.
name|getLength
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
name|filteredStream
operator|=
operator|new
name|RandomAccessFileOutputStream
argument_list|(
name|file
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
name|file
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
name|file
argument_list|)
expr_stmt|;
name|filteredStream
operator|.
name|setExpectedLength
argument_list|(
name|expectedLength
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
name|file
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
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|file
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception occured when closing the file."
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|filteredStream
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|filteredStream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unFilteredStream
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|unFilteredStream
argument_list|)
expr_stmt|;
block|}
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

