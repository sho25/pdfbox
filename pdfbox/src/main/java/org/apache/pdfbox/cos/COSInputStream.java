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
name|FilterInputStream
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
comment|/**  * An InputStream which reads from an encoded COS stream.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|COSInputStream
extends|extends
name|FilterInputStream
block|{
comment|/**      * Creates a new COSInputStream from an encoded input stream.      *      * @param filters Filters to be applied.      * @param parameters Filter parameters.      * @param in Encoded input stream.      * @param scratchFile Scratch file to use, or null.      * @return Decoded stream.      * @throws IOException If the stream could not be read.      */
specifier|static
name|COSInputStream
name|create
parameter_list|(
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|DecodeResult
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|DecodeResult
argument_list|>
argument_list|()
decl_stmt|;
name|InputStream
name|input
init|=
name|in
decl_stmt|;
if|if
condition|(
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|input
operator|=
name|in
expr_stmt|;
block|}
else|else
block|{
comment|// apply filters
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
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|scratchFile
operator|!=
literal|null
condition|)
block|{
comment|// scratch file
name|RandomAccess
name|buffer
init|=
name|scratchFile
operator|.
name|createBuffer
argument_list|()
decl_stmt|;
name|DecodeResult
name|result
init|=
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|decode
argument_list|(
name|input
argument_list|,
operator|new
name|RandomAccessOutputStream
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|parameters
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|RandomAccessInputStream
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// in-memory
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DecodeResult
name|result
init|=
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|decode
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|parameters
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|input
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|COSInputStream
argument_list|(
name|input
argument_list|,
name|results
argument_list|)
return|;
block|}
specifier|private
specifier|final
name|List
argument_list|<
name|DecodeResult
argument_list|>
name|decodeResults
decl_stmt|;
comment|/**      * Constructor.      *       * @param input decoded stream      * @param decodeResults results of decoding      */
specifier|private
name|COSInputStream
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|List
argument_list|<
name|DecodeResult
argument_list|>
name|decodeResults
parameter_list|)
block|{
name|super
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|this
operator|.
name|decodeResults
operator|=
name|decodeResults
expr_stmt|;
block|}
comment|/**      * Returns the result of the last filter, for use by repair mechanisms.      */
specifier|public
name|DecodeResult
name|getDecodeResult
parameter_list|()
block|{
if|if
condition|(
name|decodeResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|DecodeResult
operator|.
name|DEFAULT
return|;
block|}
else|else
block|{
return|return
name|decodeResults
operator|.
name|get
argument_list|(
name|decodeResults
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

