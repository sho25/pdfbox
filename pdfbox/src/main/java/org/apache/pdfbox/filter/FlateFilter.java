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
name|zip
operator|.
name|DataFormatException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Inflater
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
comment|/**  * Decompresses data encoded using the zlib/deflate compression method,  * reproducing the original text or binary data.  *  * @author Ben Litchfield  * @author Marcel Kammer  */
end_comment

begin_class
specifier|final
class|class
name|FlateFilter
extends|extends
name|Filter
block|{
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
name|FlateFilter
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
literal|16348
decl_stmt|;
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
name|int
name|predictor
init|=
operator|-
literal|1
decl_stmt|;
specifier|final
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
block|}
try|try
block|{
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
name|decompress
argument_list|(
name|encoded
argument_list|,
name|baos
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
name|decompress
argument_list|(
name|encoded
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|DataFormatException
name|e
parameter_list|)
block|{
comment|// if the stream is corrupt a DataFormatException may occur
name|LOG
operator|.
name|error
argument_list|(
literal|"FlateFilter: stop reading corrupt stream due to a DataFormatException"
argument_list|)
expr_stmt|;
comment|// re-throw the exception
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|DecodeResult
argument_list|(
name|parameters
argument_list|)
return|;
block|}
comment|// Use Inflater instead of InflateInputStream to avoid an EOFException due to a probably
comment|// missing Z_STREAM_END, see PDFBOX-1232 for details
specifier|private
name|void
name|decompress
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|DataFormatException
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|read
init|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
if|if
condition|(
name|read
operator|>
literal|0
condition|)
block|{
name|Inflater
name|inflater
init|=
operator|new
name|Inflater
argument_list|()
decl_stmt|;
name|inflater
operator|.
name|setInput
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
name|byte
index|[]
name|res
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
name|boolean
name|dataWritten
init|=
literal|false
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|resRead
init|=
literal|0
decl_stmt|;
try|try
block|{
name|resRead
operator|=
name|inflater
operator|.
name|inflate
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DataFormatException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|dataWritten
condition|)
block|{
comment|// some data could be read -> don't throw an exception
name|LOG
operator|.
name|warn
argument_list|(
literal|"FlateFilter: premature end of stream due to a DataFormatException"
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
comment|// nothing could be read -> re-throw exception
throw|throw
name|exception
throw|;
block|}
block|}
if|if
condition|(
name|resRead
operator|!=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|res
argument_list|,
literal|0
argument_list|,
name|resRead
argument_list|)
expr_stmt|;
name|dataWritten
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|inflater
operator|.
name|finished
argument_list|()
operator|||
name|inflater
operator|.
name|needsDictionary
argument_list|()
operator|||
name|in
operator|.
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
break|break;
block|}
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|inflater
operator|.
name|setInput
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
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
name|DeflaterOutputStream
name|out
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|int
name|amountRead
decl_stmt|;
name|int
name|mayRead
init|=
name|input
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|mayRead
operator|>
literal|0
condition|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|Math
operator|.
name|min
argument_list|(
name|mayRead
argument_list|,
name|BUFFER_SIZE
argument_list|)
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|mayRead
argument_list|,
name|BUFFER_SIZE
argument_list|)
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
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
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|encoded
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

