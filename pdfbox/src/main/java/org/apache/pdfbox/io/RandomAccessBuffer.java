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
name|io
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

begin_comment
comment|/**  * An interface to allow PDF files to be stored completely in memory.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|RandomAccessBuffer
implements|implements
name|RandomAccess
block|{
specifier|private
name|byte
index|[]
name|buffer
decl_stmt|;
specifier|private
name|long
name|pointer
decl_stmt|;
specifier|private
name|long
name|size
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|RandomAccessBuffer
parameter_list|()
block|{
comment|// starting with a 16kb buffer
name|buffer
operator|=
operator|new
name|byte
index|[
literal|16384
index|]
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|buffer
operator|=
literal|null
expr_stmt|;
name|pointer
operator|=
literal|0
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|seek
parameter_list|(
name|long
name|position
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|pointer
operator|=
name|position
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|pointer
operator|>=
name|this
operator|.
name|size
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|buffer
index|[
operator|(
name|int
operator|)
name|pointer
operator|++
index|]
operator|&
literal|0xff
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pointer
operator|>=
name|this
operator|.
name|size
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|maxLength
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|length
argument_list|,
name|this
operator|.
name|size
operator|-
name|pointer
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
operator|(
name|int
operator|)
name|pointer
argument_list|,
name|b
argument_list|,
name|offset
argument_list|,
name|maxLength
argument_list|)
expr_stmt|;
name|pointer
operator|+=
name|maxLength
expr_stmt|;
return|return
name|maxLength
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|size
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
if|if
condition|(
name|pointer
operator|>=
name|buffer
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|pointer
operator|>=
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer overflow"
argument_list|)
throw|;
block|}
name|buffer
operator|=
name|expandBuffer
argument_list|(
name|buffer
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
literal|2L
operator|*
name|buffer
operator|.
name|length
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buffer
index|[
operator|(
name|int
operator|)
name|pointer
operator|++
index|]
operator|=
operator|(
name|byte
operator|)
name|b
expr_stmt|;
if|if
condition|(
name|pointer
operator|>
name|this
operator|.
name|size
condition|)
block|{
name|this
operator|.
name|size
operator|=
name|pointer
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|newSize
init|=
name|pointer
operator|+
name|length
decl_stmt|;
if|if
condition|(
name|newSize
operator|>=
name|buffer
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|newSize
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessBuffer overflow"
argument_list|)
throw|;
block|}
name|newSize
operator|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|2L
operator|*
name|buffer
operator|.
name|length
argument_list|,
name|newSize
argument_list|)
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|buffer
operator|=
name|expandBuffer
argument_list|(
name|buffer
argument_list|,
operator|(
name|int
operator|)
name|newSize
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|buffer
argument_list|,
operator|(
name|int
operator|)
name|pointer
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|pointer
operator|+=
name|length
expr_stmt|;
if|if
condition|(
name|pointer
operator|>
name|this
operator|.
name|size
condition|)
block|{
name|this
operator|.
name|size
operator|=
name|pointer
expr_stmt|;
block|}
block|}
comment|/**      * expand the given buffer to the new size.      *       * @param buffer the given buffer      * @param newSize the new size      * @return the expanded buffer      *       */
specifier|private
name|byte
index|[]
name|expandBuffer
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|newSize
parameter_list|)
block|{
name|byte
index|[]
name|expandedBuffer
init|=
operator|new
name|byte
index|[
name|newSize
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|expandedBuffer
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|expandedBuffer
return|;
block|}
block|}
end_class

end_unit

