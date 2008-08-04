begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|io
operator|.
name|PushbackInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StreamCorruptedException
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|io
operator|.
name|NBitInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|io
operator|.
name|NBitOutputStream
import|;
end_import

begin_comment
comment|/**  * This is the used for the LZWDecode filter.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.15 $  */
end_comment

begin_class
specifier|public
class|class
name|LZWFilter
implements|implements
name|Filter
block|{
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
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|compressedData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
comment|//log.debug("decode( )");
name|NBitInputStream
name|in
init|=
literal|null
decl_stmt|;
name|in
operator|=
operator|new
name|NBitInputStream
argument_list|(
name|compressedData
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|LZWDictionary
name|dic
init|=
operator|new
name|LZWDictionary
argument_list|()
decl_stmt|;
name|byte
name|firstByte
init|=
literal|0
decl_stmt|;
name|long
name|nextCommand
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|nextCommand
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
name|EOD
condition|)
block|{
comment|// log.debug( "decode - nextCommand=" + nextCommand + ", bitsInChunk: " + in.getBitsInChunk());
if|if
condition|(
name|nextCommand
operator|==
name|CLEAR_TABLE
condition|)
block|{
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|dic
operator|=
operator|new
name|LZWDictionary
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|data
init|=
name|dic
operator|.
name|getData
argument_list|(
name|nextCommand
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
name|dic
operator|.
name|visit
argument_list|(
name|firstByte
argument_list|)
expr_stmt|;
name|data
operator|=
name|dic
operator|.
name|getData
argument_list|(
name|nextCommand
argument_list|)
expr_stmt|;
name|dic
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|StreamCorruptedException
argument_list|(
literal|"Error: data is null"
argument_list|)
throw|;
block|}
name|dic
operator|.
name|visit
argument_list|(
name|data
argument_list|)
expr_stmt|;
comment|//log.debug( "decode - dic.getNextCode(): " + dic.getNextCode());
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|2047
condition|)
block|{
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|12
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|1023
condition|)
block|{
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|511
condition|)
block|{
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|.
name|setBitsInChunk
argument_list|(
literal|9
argument_list|)
expr_stmt|;
block|}
comment|/**                 if( in.getBitsInChunk() != dic.getCodeSize() )                 {                     in.unread( nextCommand );                     in.setBitsInChunk( dic.getCodeSize() );                     System.out.print( "Switching " + nextCommand + " to " );                     nextCommand = in.read();                     System.out.println( "" +  nextCommand );                     data = dic.getData( nextCommand );                 }**/
name|firstByte
operator|=
name|data
index|[
literal|0
index|]
expr_stmt|;
name|result
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
comment|//log.debug("encode( )");
name|PushbackInputStream
name|input
init|=
operator|new
name|PushbackInputStream
argument_list|(
name|rawData
argument_list|,
literal|4096
argument_list|)
decl_stmt|;
name|LZWDictionary
name|dic
init|=
operator|new
name|LZWDictionary
argument_list|()
decl_stmt|;
name|NBitOutputStream
name|out
init|=
operator|new
name|NBitOutputStream
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBitsInChunk
argument_list|(
literal|9
argument_list|)
expr_stmt|;
comment|//initially nine
name|out
operator|.
name|write
argument_list|(
name|CLEAR_TABLE
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|byteRead
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|byteRead
operator|=
name|input
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
comment|//log.debug( "byteRead = '" + (char)byteRead + "' (0x" + Integer.toHexString(byteRead) + "), i=" + i);
name|buffer
operator|.
name|write
argument_list|(
name|byteRead
argument_list|)
expr_stmt|;
name|dic
operator|.
name|visit
argument_list|(
operator|(
name|byte
operator|)
name|byteRead
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBitsInChunk
argument_list|(
name|dic
operator|.
name|getCodeSize
argument_list|()
argument_list|)
expr_stmt|;
comment|//log.debug( "Getting node '" + new String( buffer.toByteArray() ) + "', buffer.size = " + buffer.size() );
name|LZWNode
name|node
init|=
name|dic
operator|.
name|getNode
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|nextByte
init|=
name|input
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|nextByte
operator|!=
operator|-
literal|1
condition|)
block|{
comment|//log.debug( "nextByte = '" + (char)nextByte + "' (0x" + Integer.toHexString(nextByte) + ")");
name|LZWNode
name|next
init|=
name|node
operator|.
name|getNode
argument_list|(
operator|(
name|byte
operator|)
name|nextByte
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|==
literal|null
condition|)
block|{
comment|//log.debug("encode - No next node, writing node and resetting buffer (" +
comment|//          " node.getCode: " + node.getCode() + ")" +
comment|//          " bitsInChunk: " + out.getBitsInChunk() +
comment|//          ")");
name|out
operator|.
name|write
argument_list|(
name|node
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
name|input
operator|.
name|unread
argument_list|(
name|nextByte
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//log.debug("encode - EOF on lookahead: writing node, resetting buffer, and terminating read loop (" +
comment|//          " node.getCode: " + node.getCode() + ")" +
comment|//          " bitsInChunk: " + out.getBitsInChunk() +
comment|//          ")");
name|out
operator|.
name|write
argument_list|(
name|node
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|==
literal|4096
condition|)
block|{
comment|//log.debug("encode - Clearing dictionary and unreading pending buffer data (" +
comment|//          " bitsInChunk: " + out.getBitsInChunk() +
comment|//          ")");
name|out
operator|.
name|write
argument_list|(
name|CLEAR_TABLE
argument_list|)
expr_stmt|;
name|dic
operator|=
operator|new
name|LZWDictionary
argument_list|()
expr_stmt|;
name|input
operator|.
name|unread
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Fix the code size based on the fact that we are writing the EOD
comment|//
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|2047
condition|)
block|{
name|out
operator|.
name|setBitsInChunk
argument_list|(
literal|12
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|1023
condition|)
block|{
name|out
operator|.
name|setBitsInChunk
argument_list|(
literal|11
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dic
operator|.
name|getNextCode
argument_list|()
operator|>=
literal|511
condition|)
block|{
name|out
operator|.
name|setBitsInChunk
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|setBitsInChunk
argument_list|(
literal|9
argument_list|)
expr_stmt|;
block|}
comment|//log.debug("encode - Writing EOD (" +
comment|//          " bitsInChunk: " + out.getBitsInChunk() +
comment|//          ")");
name|out
operator|.
name|write
argument_list|(
name|EOD
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|result
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

