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
name|ccitt
operator|.
name|TIFFFaxDecoder
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
name|filter
operator|.
name|ccitt
operator|.
name|CCITTFaxG31DDecodeInputStream
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
name|ccitt
operator|.
name|FillOrderChangeInputStream
import|;
end_import

begin_comment
comment|/**  * Decodes image data that has been encoded using either Group 3 or Group 4  * CCITT facsimile (fax) encoding.  *  * @author Ben Litchfield  * @author Marcel Kammer  * @author Paul King  */
end_comment

begin_class
specifier|final
class|class
name|CCITTFaxFilter
extends|extends
name|Filter
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CCITTFaxFilter
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
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
parameter_list|)
throws|throws
name|IOException
block|{
name|DecodeResult
name|result
init|=
operator|new
name|DecodeResult
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|getParameters
argument_list|()
operator|.
name|addAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
comment|// get decode parameters
name|COSDictionary
name|decodeParms
init|=
operator|(
name|COSDictionary
operator|)
name|parameters
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DECODE_PARMS
argument_list|,
name|COSName
operator|.
name|DP
argument_list|)
decl_stmt|;
comment|// get compressed data
name|int
name|length
init|=
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|byte
index|[]
name|compressed
decl_stmt|;
if|if
condition|(
name|length
operator|!=
operator|-
literal|1
condition|)
block|{
name|compressed
operator|=
operator|new
name|byte
index|[
name|length
index|]
expr_stmt|;
name|long
name|written
init|=
name|IOUtils
operator|.
name|populateBuffer
argument_list|(
name|encoded
argument_list|,
name|compressed
argument_list|)
decl_stmt|;
if|if
condition|(
name|written
operator|!=
name|compressed
operator|.
name|length
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Buffer for compressed data did not match the length"
operator|+
literal|" of the actual compressed data"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// inline images don't provide the length of the stream so that
comment|// we have to read until the end of the stream to find out the length
comment|// the streams inline images are stored in are mostly small ones
name|compressed
operator|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|encoded
argument_list|)
expr_stmt|;
block|}
comment|// parse dimensions
name|int
name|cols
init|=
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|,
literal|1728
argument_list|)
decl_stmt|;
name|int
name|rows
init|=
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|int
name|height
init|=
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|HEIGHT
argument_list|,
name|COSName
operator|.
name|H
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|rows
operator|>
literal|0
operator|&&
name|height
operator|>
literal|0
condition|)
block|{
comment|// ensure that rows doesn't contain implausible data, see PDFBOX-771
name|rows
operator|=
name|Math
operator|.
name|min
argument_list|(
name|rows
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// at least one of the values has to have a valid value
name|rows
operator|=
name|Math
operator|.
name|max
argument_list|(
name|rows
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|// decompress data
name|int
name|k
init|=
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|boolean
name|encodedByteAlign
init|=
name|decodeParms
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|ENCODED_BYTE_ALIGN
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|int
name|arraySize
init|=
operator|(
name|cols
operator|+
literal|7
operator|)
operator|/
literal|8
operator|*
name|rows
decl_stmt|;
name|TIFFFaxDecoder
name|faxDecoder
init|=
operator|new
name|TIFFFaxDecoder
argument_list|(
literal|1
argument_list|,
name|cols
argument_list|,
name|rows
argument_list|)
decl_stmt|;
comment|// TODO possible options??
name|long
name|tiffOptions
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|decompressed
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|k
operator|==
literal|0
condition|)
block|{
name|InputStream
name|in
init|=
operator|new
name|CCITTFaxG31DDecodeInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|compressed
argument_list|)
argument_list|,
name|cols
argument_list|)
decl_stmt|;
name|in
operator|=
operator|new
name|FillOrderChangeInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|//Decorate to change fill order
name|decompressed
operator|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|>
literal|0
condition|)
block|{
name|decompressed
operator|=
operator|new
name|byte
index|[
name|arraySize
index|]
expr_stmt|;
name|faxDecoder
operator|.
name|decode2D
argument_list|(
name|decompressed
argument_list|,
name|compressed
argument_list|,
literal|0
argument_list|,
name|rows
argument_list|,
name|tiffOptions
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|<
literal|0
condition|)
block|{
name|decompressed
operator|=
operator|new
name|byte
index|[
name|arraySize
index|]
expr_stmt|;
name|faxDecoder
operator|.
name|decodeT6
argument_list|(
name|decompressed
argument_list|,
name|compressed
argument_list|,
literal|0
argument_list|,
name|rows
argument_list|,
name|tiffOptions
argument_list|,
name|encodedByteAlign
argument_list|)
expr_stmt|;
block|}
comment|// invert bitmap
name|boolean
name|blackIsOne
init|=
name|decodeParms
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|BLACK_IS_1
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|blackIsOne
condition|)
block|{
comment|// Inverting the bitmap
comment|// Note the previous approach with starting from an IndexColorModel didn't work
comment|// reliably. In some cases the image wouldn't be painted for some reason.
comment|// So a safe but slower approach was taken.
name|invertBitmap
argument_list|(
name|decompressed
argument_list|)
expr_stmt|;
block|}
comment|// repair missing color space
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|)
condition|)
block|{
name|result
operator|.
name|getParameters
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|,
name|COSName
operator|.
name|DEVICEGRAY
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|decoded
operator|.
name|write
argument_list|(
name|decompressed
argument_list|)
expr_stmt|;
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
name|invertBitmap
parameter_list|(
name|byte
index|[]
name|bufferData
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|c
init|=
name|bufferData
operator|.
name|length
init|;
name|i
operator|<
name|c
condition|;
name|i
operator|++
control|)
block|{
name|bufferData
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
operator|~
name|bufferData
index|[
name|i
index|]
operator|&
literal|0xFF
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|encode
parameter_list|(
name|InputStream
name|input
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
name|log
operator|.
name|warn
argument_list|(
literal|"CCITTFaxDecode.encode is not implemented yet, skipping this stream."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

