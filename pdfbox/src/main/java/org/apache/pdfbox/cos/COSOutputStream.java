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
name|ScratchFile
import|;
end_import

begin_comment
comment|/**  * An OutputStream which writes to an encoded COS stream.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|COSOutputStream
extends|extends
name|FilterOutputStream
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|parameters
decl_stmt|;
comment|// todo: this is an in-memory buffer, should use scratch file (if any) instead
specifier|private
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|/**      * Creates a new COSOutputStream writes to an encoded COS stream.      *       * @param filters Filters to apply.      * @param parameters Filter parameters.      * @param output Encoded stream.      * @param scratchFile Scratch file to use, or null.      */
name|COSOutputStream
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
name|OutputStream
name|output
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|super
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
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
name|buffer
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
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|buffer
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// apply filters in reverse order
for|for
control|(
name|int
name|i
init|=
name|filters
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
comment|// todo: this is an in-memory buffer, should use scratch file (if any) instead
name|ByteArrayInputStream
name|input
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|encode
argument_list|(
name|input
argument_list|,
name|buffer
argument_list|,
name|parameters
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// flush the entire stream
name|out
operator|.
name|write
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|buffer
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

