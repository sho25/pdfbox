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
name|ByteArrayOutputStream
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|FilterManager
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
name|pdmodel
operator|.
name|PDDocument
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
name|pdmodel
operator|.
name|common
operator|.
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_comment
comment|/**  * A PDStream represents a stream in a PDF document.  Streams are tied to a single  * PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.17 $  */
end_comment

begin_class
specifier|public
class|class
name|PDStream
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSStream
name|stream
decl_stmt|;
comment|/**      * This will create a new PDStream object.      */
specifier|protected
name|PDStream
parameter_list|()
block|{
comment|//should only be called by PDMemoryStream
block|}
comment|/**      * This will create a new PDStream object.      *      * @param document The document that the stream will be part of.      */
specifier|public
name|PDStream
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|stream
operator|=
operator|new
name|COSStream
argument_list|(
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getScratchFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param str The stream parameter.      */
specifier|public
name|PDStream
parameter_list|(
name|COSStream
name|str
parameter_list|)
block|{
name|stream
operator|=
name|str
expr_stmt|;
block|}
comment|/**      * Constructor.  Reads all data from the input stream and embeds it into the      * document, this will close the InputStream.      *      * @param doc The document that will hold the stream.      * @param str The stream parameter.      * @throws IOException If there is an error creating the stream in the document.      */
specifier|public
name|PDStream
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|str
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|doc
argument_list|,
name|str
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.  Reads all data from the input stream and embeds it into the      * document, this will close the InputStream.      *      * @param doc The document that will hold the stream.      * @param str The stream parameter.      * @param filtered True if the stream already has a filter applied.      * @throws IOException If there is an error creating the stream in the document.      */
specifier|public
name|PDStream
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|str
parameter_list|,
name|boolean
name|filtered
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stream
operator|=
operator|new
name|COSStream
argument_list|(
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|getScratchFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|filtered
condition|)
block|{
name|output
operator|=
name|stream
operator|.
name|createFilteredStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|output
operator|=
name|stream
operator|.
name|createUnfilteredStream
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|amountRead
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|str
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|str
operator|!=
literal|null
condition|)
block|{
name|str
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * If there are not compression filters on the current stream then this      * will add a compression filter, flate compression for example.      */
specifier|public
name|void
name|addCompression
parameter_list|()
block|{
name|List
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
name|filters
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|filters
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
expr_stmt|;
name|setFilters
argument_list|(
name|filters
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a pd stream from either a regular COSStream on a COSArray of cos streams.      * @param base Either a COSStream or COSArray.      * @return A PDStream or null if base is null.      * @throws IOException If there is an error creating the PDStream.      */
specifier|public
specifier|static
name|PDStream
name|createFromCOS
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|PDStream
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
name|retval
operator|=
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|retval
operator|=
operator|new
name|PDStream
argument_list|(
operator|new
name|COSStreamArray
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Contents are unknown type:"
operator|+
name|base
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
comment|/**      * This will get a stream that can be written to.      *      * @return An output stream to write data to.      *      * @throws IOException If an IO error occurs during writing.      */
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|createUnfilteredStream
argument_list|()
return|;
block|}
comment|/**      * This will get a stream that can be read from.      *      * @return An input stream that can be read from.      *      * @throws IOException If an IO error occurs during reading.      */
specifier|public
name|InputStream
name|createInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
return|;
block|}
comment|/**      * This will get a stream with some filters applied but not others.  This is useful      * when doing images, ie filters = [flate,dct], we want to remove flate but leave dct      *      * @param stopFilters A list of filters to stop decoding at.      * @return A stream with decoded data.      * @throws IOException If there is an error processing the stream.      */
specifier|public
name|InputStream
name|getPartiallyFilteredStream
parameter_list|(
name|List
name|stopFilters
parameter_list|)
throws|throws
name|IOException
block|{
name|FilterManager
name|manager
init|=
name|stream
operator|.
name|getFilterManager
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|stream
operator|.
name|getFilteredStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|List
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
name|String
name|nextFilter
init|=
literal|null
decl_stmt|;
name|boolean
name|done
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
name|filters
operator|.
name|size
argument_list|()
operator|&&
operator|!
name|done
condition|;
name|i
operator|++
control|)
block|{
name|os
operator|.
name|reset
argument_list|()
expr_stmt|;
name|nextFilter
operator|=
operator|(
name|String
operator|)
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|stopFilters
operator|.
name|contains
argument_list|(
name|nextFilter
argument_list|)
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|Filter
name|filter
init|=
name|manager
operator|.
name|getFilter
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|nextFilter
argument_list|)
argument_list|)
decl_stmt|;
name|filter
operator|.
name|decode
argument_list|(
name|is
argument_list|,
name|os
argument_list|,
name|stream
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|is
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|is
return|;
block|}
comment|/**      * Get the cos stream associated with this object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSStream
name|getStream
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
comment|/**      * This will get the length of the filtered/compressed stream.  This is readonly in the      * PD Model and will be managed by this class.      *      * @return The length of the filtered stream.      */
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|stream
operator|.
name|getInt
argument_list|(
literal|"Length"
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will get the list of filters that are associated with this stream.  Or      * null if there are none.      * @return A list of all encoding filters to apply to this stream.      */
specifier|public
name|List
name|getFilters
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|filters
init|=
name|stream
operator|.
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
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|filters
decl_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|name
argument_list|,
name|stream
argument_list|,
name|COSName
operator|.
name|FILTER
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
name|retval
operator|=
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|filters
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the filters that are part of this stream.      *      * @param filters The filters that are part of this stream.      */
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
name|filters
parameter_list|)
block|{
name|COSBase
name|obj
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of decode parameters.  Each entry in the list will refer to      * an entry in the filters list.      *      * @return The list of decode parameters.      *      * @throws IOException if there is an error retrieving the parameters.      */
specifier|public
name|List
name|getDecodeParams
parameter_list|()
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|dp
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DECODE_PARMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|dp
operator|==
literal|null
condition|)
block|{
comment|//See PDF Ref 1.5 implementation note 7, the DP is sometimes used instead.
name|dp
operator|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dp
operator|instanceof
name|COSDictionary
condition|)
block|{
name|Map
name|map
init|=
name|COSDictionaryMap
operator|.
name|convertBasicTypesToMap
argument_list|(
operator|(
name|COSDictionary
operator|)
name|dp
argument_list|)
decl_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|map
argument_list|,
name|dp
argument_list|,
name|stream
argument_list|,
name|COSName
operator|.
name|DECODE_PARAMS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dp
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|dp
decl_stmt|;
name|List
name|actuals
init|=
operator|new
name|ArrayList
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
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|COSDictionaryMap
operator|.
name|convertBasicTypesToMap
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of decode params.      *      * @param decodeParams The list of decode params.      */
specifier|public
name|void
name|setDecodeParams
parameter_list|(
name|List
name|decodeParams
parameter_list|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE_PARAMS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|decodeParams
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the file specification for this stream.  This is only      * required for external files.      *      * @return The file specification.      *      * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|PDFileSpecification
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|f
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
decl_stmt|;
name|PDFileSpecification
name|retval
init|=
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|f
argument_list|)
decl_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Set the file specification.      * @param f The file specification.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|f
parameter_list|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of filters that are associated with this stream.  Or      * null if there are none.      * @return A list of all encoding filters to apply to this stream.      */
specifier|public
name|List
name|getFileFilters
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|filters
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F_FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|filters
decl_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|name
argument_list|,
name|stream
argument_list|,
name|COSName
operator|.
name|F_FILTER
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
name|retval
operator|=
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|filters
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the filters that are part of this stream.      *      * @param filters The filters that are part of this stream.      */
specifier|public
name|void
name|setFileFilters
parameter_list|(
name|List
name|filters
parameter_list|)
block|{
name|COSBase
name|obj
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F_FILTER
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of decode parameters.  Each entry in the list will refer to      * an entry in the filters list.      *      * @return The list of decode parameters.      *      * @throws IOException if there is an error retrieving the parameters.      */
specifier|public
name|List
name|getFileDecodeParams
parameter_list|()
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|dp
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F_DECODE_PARMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|dp
operator|instanceof
name|COSDictionary
condition|)
block|{
name|Map
name|map
init|=
name|COSDictionaryMap
operator|.
name|convertBasicTypesToMap
argument_list|(
operator|(
name|COSDictionary
operator|)
name|dp
argument_list|)
decl_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|map
argument_list|,
name|dp
argument_list|,
name|stream
argument_list|,
name|COSName
operator|.
name|F_DECODE_PARMS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dp
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|dp
decl_stmt|;
name|List
name|actuals
init|=
operator|new
name|ArrayList
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
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|COSDictionaryMap
operator|.
name|convertBasicTypesToMap
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of decode params.      *      * @param decodeParams The list of decode params.      */
specifier|public
name|void
name|setFileDecodeParams
parameter_list|(
name|List
name|decodeParams
parameter_list|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
literal|"FDecodeParams"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|decodeParams
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will copy the stream into a byte array.      *      * @return The byte array of the filteredStream      * @throws IOException When getFilteredStream did not work      */
specifier|public
name|byte
index|[]
name|getByteArray
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|createInputStream
argument_list|()
expr_stmt|;
name|int
name|amountRead
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|output
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/**      * A convenience method to get this stream as a string.  Uses      * the default system encoding.      *      * @return a String representation of this (input) stream, with the      * platform default encoding.      *      * @throws IOException if there is an error while converting the stream      *                     to a string.      */
specifier|public
name|String
name|getInputStreamAsString
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bStream
init|=
name|getByteArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|bStream
argument_list|)
return|;
block|}
comment|/**      * Get the metadata that is part of the document catalog.  This will      * return null if there is no meta data for this object.      *      * @return The metadata for this object.      */
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
name|PDMetadata
name|retval
init|=
literal|null
decl_stmt|;
name|COSStream
name|mdStream
init|=
operator|(
name|COSStream
operator|)
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|mdStream
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDMetadata
argument_list|(
name|mdStream
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the metadata for this object.  This can be null.      *      * @param meta The meta data for this object.      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the decoded stream length.      *      * @since Apache PDFBox 1.1.0      * @see<a href="https://issues.apache.org/jira/browse/PDFBOX-636">PDFBOX-636</a>      * @return the decoded stream length      */
specifier|public
name|int
name|getDecodedStreamLength
parameter_list|()
block|{
return|return
name|this
operator|.
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|DL
argument_list|)
return|;
block|}
comment|/**      * Set the decoded stream length.      *      * @since Apache PDFBox 1.1.0      * @see<a href="https://issues.apache.org/jira/browse/PDFBOX-636">PDFBOX-636</a>      * @param decodedStreamLength the decoded stream length      */
specifier|public
name|void
name|setDecodedStreamLength
parameter_list|(
name|int
name|decodedStreamLength
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|DL
argument_list|,
name|decodedStreamLength
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

