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
name|io
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * Decodes image data that has been encoded using either Group 3 or Group 4  * CCITT facsimile (fax) encoding, and encodes image data to Group 4.  *  * @author Ben Litchfield  * @author Marcel Kammer  * @author Paul King  */
end_comment

begin_class
specifier|final
class|class
name|CCITTFaxFilter
extends|extends
name|Filter
block|{
annotation|@
name|Override
specifier|public
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
name|getDecodeParams
argument_list|(
name|parameters
argument_list|,
name|index
argument_list|)
decl_stmt|;
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
comment|// PDFBOX-771, PDFBOX-3727: rows in DecodeParms sometimes contains an incorrect value
name|rows
operator|=
name|height
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
comment|// TODO possible options??
name|byte
index|[]
name|decompressed
init|=
operator|new
name|byte
index|[
name|arraySize
index|]
decl_stmt|;
name|CCITTFaxDecoderStream
name|s
decl_stmt|;
name|int
name|type
decl_stmt|;
name|long
name|tiffOptions
decl_stmt|;
if|if
condition|(
name|k
operator|==
literal|0
condition|)
block|{
name|tiffOptions
operator|=
name|encodedByteAlign
condition|?
name|TIFFExtension
operator|.
name|GROUP3OPT_BYTEALIGNED
else|:
literal|0
expr_stmt|;
name|type
operator|=
name|TIFFExtension
operator|.
name|COMPRESSION_CCITT_MODIFIED_HUFFMAN_RLE
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|k
operator|>
literal|0
condition|)
block|{
name|tiffOptions
operator|=
name|encodedByteAlign
condition|?
name|TIFFExtension
operator|.
name|GROUP3OPT_BYTEALIGNED
else|:
literal|0
expr_stmt|;
name|tiffOptions
operator||=
name|TIFFExtension
operator|.
name|GROUP3OPT_2DENCODING
expr_stmt|;
name|type
operator|=
name|TIFFExtension
operator|.
name|COMPRESSION_CCITT_T4
expr_stmt|;
block|}
else|else
block|{
comment|// k< 0
name|tiffOptions
operator|=
name|encodedByteAlign
condition|?
name|TIFFExtension
operator|.
name|GROUP4OPT_BYTEALIGNED
else|:
literal|0
expr_stmt|;
name|type
operator|=
name|TIFFExtension
operator|.
name|COMPRESSION_CCITT_T6
expr_stmt|;
block|}
block|}
name|s
operator|=
operator|new
name|CCITTFaxDecoderStream
argument_list|(
name|encoded
argument_list|,
name|cols
argument_list|,
name|type
argument_list|,
name|TIFFExtension
operator|.
name|FILL_LEFT_TO_RIGHT
argument_list|,
name|tiffOptions
argument_list|)
expr_stmt|;
name|readFromDecoderStream
argument_list|(
name|s
argument_list|,
name|decompressed
argument_list|)
expr_stmt|;
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
name|void
name|readFromDecoderStream
parameter_list|(
name|CCITTFaxDecoderStream
name|decoderStream
parameter_list|,
name|byte
index|[]
name|result
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|pos
init|=
literal|0
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|decoderStream
operator|.
name|read
argument_list|(
name|result
argument_list|,
name|pos
argument_list|,
name|result
operator|.
name|length
operator|-
name|pos
argument_list|)
operator|)
operator|>
operator|-
literal|1
condition|)
block|{
name|pos
operator|+=
name|read
expr_stmt|;
if|if
condition|(
name|pos
operator|>=
name|result
operator|.
name|length
condition|)
block|{
break|break;
block|}
block|}
name|decoderStream
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|int
name|cols
init|=
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|)
decl_stmt|;
name|int
name|rows
init|=
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|)
decl_stmt|;
name|CCITTFaxEncoderStream
name|ccittFaxEncoderStream
init|=
operator|new
name|CCITTFaxEncoderStream
argument_list|(
name|encoded
argument_list|,
name|cols
argument_list|,
name|rows
argument_list|,
name|TIFFExtension
operator|.
name|FILL_LEFT_TO_RIGHT
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|ccittFaxEncoderStream
argument_list|)
expr_stmt|;
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

