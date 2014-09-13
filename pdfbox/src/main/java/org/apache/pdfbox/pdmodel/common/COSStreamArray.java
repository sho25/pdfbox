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
name|pdmodel
operator|.
name|common
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
name|io
operator|.
name|SequenceInputStream
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
name|java
operator|.
name|util
operator|.
name|Vector
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
name|COSArray
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
name|COSBase
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
name|COSStream
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
name|ICOSVisitor
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

begin_comment
comment|/**  * This will take an array of streams and sequence them together.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.10 $  */
end_comment

begin_class
specifier|public
class|class
name|COSStreamArray
extends|extends
name|COSStream
block|{
specifier|private
name|COSArray
name|streams
decl_stmt|;
comment|/**      * The first stream will be used to delegate some of the methods for this      * class.      */
specifier|private
name|COSStream
name|firstStream
decl_stmt|;
comment|/**      * Constructor.      *      * @param array The array of COSStreams to concatenate together.      */
specifier|public
name|COSStreamArray
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
expr_stmt|;
name|streams
operator|=
name|array
expr_stmt|;
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|firstStream
operator|=
operator|(
name|COSStream
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get a stream (or the reference to a stream) from the array.      *      * @param index The index of the requested stream      * @return The stream object or a reference to a stream      */
specifier|public
name|COSBase
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|streams
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * This will get the number of streams in the array.      *      * @return the number of streams      */
specifier|public
name|int
name|getStreamCount
parameter_list|()
block|{
return|return
name|streams
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * This will get an object from this streams dictionary.      *      * @param key The key to the object.      *      * @return The dictionary object with the key or null if one does not exist.      */
specifier|public
name|COSBase
name|getItem
parameter_list|(
name|COSName
name|key
parameter_list|)
block|{
return|return
name|firstStream
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * This will get an object from this streams dictionary and dereference it      * if necessary.      *      * @param key The key to the object.      *      * @return The dictionary object with the key or null if one does not exist.      */
specifier|public
name|COSBase
name|getDictionaryObject
parameter_list|(
name|COSName
name|key
parameter_list|)
block|{
return|return
name|firstStream
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSStream{}"
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
name|List
argument_list|<
name|Object
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|streams
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
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
name|retval
operator|=
name|parser
operator|.
name|getTokens
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the dictionary that is associated with this stream.      *      * @return the object that is associated with this stream.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|firstStream
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Not allowed to get filtered stream from array of streams."
argument_list|)
throw|;
block|}
comment|/**      * This will get the logical content stream with none of the filters.      *      * @return the bytes of the logical (decoded) stream      *      * @throws IOException when encoding/decoding causes an exception      */
specifier|public
name|InputStream
name|getUnfilteredStream
parameter_list|()
throws|throws
name|IOException
block|{
name|Vector
argument_list|<
name|InputStream
argument_list|>
name|inputStreams
init|=
operator|new
name|Vector
argument_list|<
name|InputStream
argument_list|>
argument_list|()
decl_stmt|;
name|byte
index|[]
name|inbetweenStreamBytes
init|=
literal|"\n"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
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
name|streams
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|streams
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|inputStreams
operator|.
name|add
argument_list|(
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
expr_stmt|;
comment|//handle the case where there is no whitespace in the
comment|//between streams in the contents array, without this
comment|//it is possible that two operators will get concatenated
comment|//together
name|inputStreams
operator|.
name|add
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|inbetweenStreamBytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SequenceInputStream
argument_list|(
name|inputStreams
operator|.
name|elements
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException if the output could not be written      */
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
name|streams
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
return|;
block|}
comment|/**      * This will return the filters to apply to the byte stream      * the method will return.      * - null if no filters are to be applied      * - a COSName if one filter is to be applied      * - a COSArray containing COSNames if multiple filters are to be applied      *      * @return the COSBase object representing the filters      */
specifier|public
name|COSBase
name|getFilters
parameter_list|()
block|{
return|return
name|firstStream
operator|.
name|getFilters
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
return|return
name|firstStream
operator|.
name|createFilteredStream
argument_list|()
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
return|return
name|firstStream
operator|.
name|createFilteredStream
argument_list|(
name|expectedLength
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
comment|//should this be allowed?  Should this
comment|//propagate to all streams in the array?
name|firstStream
operator|.
name|setFilters
argument_list|(
name|filters
argument_list|)
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
return|return
name|firstStream
operator|.
name|createUnfilteredStream
argument_list|()
return|;
block|}
comment|/**      * Appends a new stream to the array that represents this object's stream.      *      * @param streamToAppend The stream to append.      */
specifier|public
name|void
name|appendStream
parameter_list|(
name|COSStream
name|streamToAppend
parameter_list|)
block|{
name|streams
operator|.
name|add
argument_list|(
name|streamToAppend
argument_list|)
expr_stmt|;
block|}
comment|/**      * Insert the given stream at the beginning of the existing stream array.      * @param streamToBeInserted      */
specifier|public
name|void
name|insertCOSStream
parameter_list|(
name|PDStream
name|streamToBeInserted
parameter_list|)
block|{
name|COSArray
name|tmp
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|tmp
operator|.
name|add
argument_list|(
name|streamToBeInserted
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addAll
argument_list|(
name|streams
argument_list|)
expr_stmt|;
name|streams
operator|.
name|clear
argument_list|()
expr_stmt|;
name|streams
operator|=
name|tmp
expr_stmt|;
block|}
block|}
end_class

end_unit

