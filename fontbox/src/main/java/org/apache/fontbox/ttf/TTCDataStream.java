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
name|fontbox
operator|.
name|ttf
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

begin_comment
comment|/**  * A wrapper for a TTF stream inside a TTC file, does not close the underlying shared stream.  *  * @author John Hewson  */
end_comment

begin_class
class|class
name|TTCDataStream
extends|extends
name|TTFDataStream
block|{
specifier|private
specifier|final
name|TTFDataStream
name|stream
decl_stmt|;
name|TTCDataStream
parameter_list|(
name|TTFDataStream
name|stream
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|readLong
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|readLong
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|readUnsignedShort
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|readUnsignedShort
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|short
name|readSignedShort
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|readSignedShort
argument_list|()
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
comment|// don't close the underlying stream, as it is shared by all fonts from the same TTC
comment|// TrueTypeCollection.close() must be called instead
block|}
annotation|@
name|Override
specifier|public
name|void
name|seek
parameter_list|(
name|long
name|pos
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|seek
argument_list|(
name|pos
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
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
return|return
name|stream
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getCurrentPosition
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|getCurrentPosition
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getOriginalData
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|getOriginalData
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getOriginalDataSize
parameter_list|()
block|{
return|return
name|stream
operator|.
name|getOriginalDataSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

