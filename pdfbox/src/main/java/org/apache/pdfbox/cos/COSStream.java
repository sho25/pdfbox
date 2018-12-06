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
name|FilterOutputStream
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
name|DecodeOptions
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
name|RandomAccessInputStream
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
name|RandomAccessOutputStream
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
name|RandomAccess
name|randomAccess
decl_stmt|;
comment|// backing store, in-memory or on-disk
specifier|private
specifier|final
name|ScratchFile
name|scratchFile
decl_stmt|;
comment|// used as a temp buffer during decoding
specifier|private
name|boolean
name|isWriting
decl_stmt|;
comment|// true if there's an open OutputStream
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
comment|/**      * Creates a new stream with an empty dictionary.      *<p>      * Try to avoid using this constructor because it creates a new scratch file in memory. Instead,      * use {@link COSDocument#createCOSStream() document.getDocument().createCOSStream()} which will      * use the existing scratch file (in memory or in temp file) of the document.      *</p>      */
specifier|public
name|COSStream
parameter_list|()
block|{
name|this
argument_list|(
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new stream with an empty dictionary. Data is stored in the given scratch file.      *      * @param scratchFile Scratch file for writing stream data.      */
specifier|public
name|COSStream
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|scratchFile
operator|=
name|scratchFile
operator|!=
literal|null
condition|?
name|scratchFile
else|:
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
expr_stmt|;
block|}
comment|/**      * Throws if the random access backing store has been closed. Helpful for catching cases where      * a user tries to use a COSStream which has outlived its COSDocument.      */
specifier|private
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|randomAccess
operator|!=
literal|null
operator|&&
name|randomAccess
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
comment|// Tip for debugging: look at the destination file with an editor, you'll see an
comment|// incomplete stream at the bottom.
block|}
block|}
comment|/**      * This will get the stream with all of the filters applied.      *      * @return the bytes of the physical (encoded) stream      * @throws IOException when encoding causes an exception      * @deprecated Use {@link #createRawInputStream()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|InputStream
name|getFilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createRawInputStream
argument_list|()
return|;
block|}
comment|/**      * Ensures {@link #randomAccess} is not<code>null</code> by creating a      * buffer from {@link #scratchFile} if needed.      *       * @param forInputStream  if<code>true</code> and {@link #randomAccess} is<code>null</code>      *                        a debug message is logged - input stream should be retrieved after      *                        data being written to stream      * @throws IOException      */
specifier|private
name|void
name|ensureRandomAccessExists
parameter_list|(
name|boolean
name|forInputStream
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|randomAccess
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|forInputStream
operator|&&
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// no data written to stream - maybe this should be an exception
name|LOG
operator|.
name|debug
argument_list|(
literal|"Create InputStream called without data being written before to stream."
argument_list|)
expr_stmt|;
block|}
name|randomAccess
operator|=
name|scratchFile
operator|.
name|createBuffer
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns a new InputStream which reads the encoded PDF stream data. Experts only!      *       * @return InputStream containing raw, encoded PDF stream data.      * @throws IOException If the stream could not be read.      */
specifier|public
name|InputStream
name|createRawInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|isWriting
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot read while there is an open stream writer"
argument_list|)
throw|;
block|}
name|ensureRandomAccessExists
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|new
name|RandomAccessInputStream
argument_list|(
name|randomAccess
argument_list|)
return|;
block|}
comment|/**      * This will get the logical content stream with none of the filters.      *      * @return the bytes of the logical (decoded) stream      * @throws IOException when decoding causes an exception      * @deprecated Use {@link #createInputStream()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|InputStream
name|getUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createInputStream
argument_list|()
return|;
block|}
comment|/**      * Returns a new InputStream which reads the decoded stream data.      *       * @return InputStream containing decoded stream data.      * @throws IOException If the stream could not be read.      */
specifier|public
name|COSInputStream
name|createInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createInputStream
argument_list|(
name|DecodeOptions
operator|.
name|DEFAULT
argument_list|)
return|;
block|}
specifier|public
name|COSInputStream
name|createInputStream
parameter_list|(
name|DecodeOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|isWriting
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot read while there is an open stream writer"
argument_list|)
throw|;
block|}
name|ensureRandomAccessExists
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
operator|new
name|RandomAccessInputStream
argument_list|(
name|randomAccess
argument_list|)
decl_stmt|;
return|return
name|COSInputStream
operator|.
name|create
argument_list|(
name|getFilterList
argument_list|()
argument_list|,
name|this
argument_list|,
name|input
argument_list|,
name|scratchFile
argument_list|,
name|options
argument_list|)
return|;
block|}
comment|/**      * This will create an output stream that can be written to.      *      * @return An output stream which raw data bytes should be written to.      * @throws IOException If there is an error creating the stream.      * @deprecated Use {@link #createOutputStream()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|OutputStream
name|createUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createOutputStream
argument_list|()
return|;
block|}
comment|/**      * Returns a new OutputStream for writing stream data, using the current filters.      *      * @return OutputStream for un-encoded stream data.      * @throws IOException If the output stream could not be created.      */
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createOutputStream
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a new OutputStream for writing stream data, using and the given filters.      *       * @param filters COSArray or COSName of filters to be used.      * @return OutputStream for un-encoded stream data.      * @throws IOException If the output stream could not be created.      */
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|(
name|COSBase
name|filters
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|isWriting
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot have more than one open stream writer."
argument_list|)
throw|;
block|}
comment|// apply filters, if any
if|if
condition|(
name|filters
operator|!=
literal|null
condition|)
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
block|}
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|randomAccess
argument_list|)
expr_stmt|;
name|randomAccess
operator|=
name|scratchFile
operator|.
name|createBuffer
argument_list|()
expr_stmt|;
name|OutputStream
name|randomOut
init|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|randomAccess
argument_list|)
decl_stmt|;
name|OutputStream
name|cosOut
init|=
operator|new
name|COSOutputStream
argument_list|(
name|getFilterList
argument_list|()
argument_list|,
name|this
argument_list|,
name|randomOut
argument_list|,
name|scratchFile
argument_list|)
decl_stmt|;
name|isWriting
operator|=
literal|true
expr_stmt|;
return|return
operator|new
name|FilterOutputStream
argument_list|(
name|cosOut
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
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
block|{
name|this
operator|.
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
operator|(
name|int
operator|)
name|randomAccess
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|isWriting
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * This will create a new stream for which filtered byte should be      * written to. You probably don't want this but want to use the      * createUnfilteredStream, which is used to write raw bytes to.      *      * @return A stream that can be written to.      * @throws IOException If there is an error creating the stream.      * @deprecated Use {@link #createRawOutputStream()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|OutputStream
name|createFilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createRawOutputStream
argument_list|()
return|;
block|}
comment|/**      * Returns a new OutputStream for writing encoded PDF data. Experts only!      *       * @return OutputStream for raw PDF stream data.      * @throws IOException If the output stream could not be created.      */
specifier|public
name|OutputStream
name|createRawOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|isWriting
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot have more than one open stream writer."
argument_list|)
throw|;
block|}
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|randomAccess
argument_list|)
expr_stmt|;
name|randomAccess
operator|=
name|scratchFile
operator|.
name|createBuffer
argument_list|()
expr_stmt|;
name|OutputStream
name|out
init|=
operator|new
name|RandomAccessOutputStream
argument_list|(
name|randomAccess
argument_list|)
decl_stmt|;
name|isWriting
operator|=
literal|true
expr_stmt|;
return|return
operator|new
name|FilterOutputStream
argument_list|(
name|out
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
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
block|{
name|this
operator|.
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
operator|(
name|int
operator|)
name|randomAccess
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|isWriting
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Returns the list of filters.      */
specifier|private
name|List
argument_list|<
name|Filter
argument_list|>
name|getFilterList
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Filter
argument_list|>
name|filterList
init|=
operator|new
name|ArrayList
argument_list|<>
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
operator|instanceof
name|COSName
condition|)
block|{
name|filterList
operator|.
name|add
argument_list|(
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
operator|(
name|COSName
operator|)
name|filters
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
name|filterList
operator|.
name|add
argument_list|(
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|filterName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filterList
return|;
block|}
comment|/**      * Returns the length of the encoded stream.      *      * @return length in bytes      */
specifier|public
name|long
name|getLength
parameter_list|()
block|{
if|if
condition|(
name|isWriting
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"There is an open OutputStream associated with "
operator|+
literal|"this COSStream. It must be closed before querying"
operator|+
literal|"length of this COSStream."
argument_list|)
throw|;
block|}
return|return
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|0
argument_list|)
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
comment|/**      * Sets the filters to be applied when encoding or decoding the stream.      *      * @param filters The filters to set on this stream.      * @throws IOException If there is an error clearing the old filters.      * @deprecated Use {@link #createOutputStream(COSBase)} instead.      */
annotation|@
name|Deprecated
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
block|}
comment|/**      * Returns the contents of the stream as a text string.      *       * @deprecated Use {@link #toTextString()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|String
name|getString
parameter_list|()
block|{
return|return
name|toTextString
argument_list|()
return|;
block|}
comment|/**      * Returns the contents of the stream as a PDF "text string".      */
specifier|public
name|String
name|toTextString
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
name|createInputStream
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"An exception occured trying to get the content - returning empty string instead"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
comment|// marks the scratch file pages as free
if|if
condition|(
name|randomAccess
operator|!=
literal|null
condition|)
block|{
name|randomAccess
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

