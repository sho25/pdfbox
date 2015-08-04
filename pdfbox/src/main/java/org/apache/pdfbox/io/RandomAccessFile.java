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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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

begin_comment
comment|/**  * A RandomAccess implementation which allows data to be stored in a scratch file on the disk to  * reduce memory consumption.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|RandomAccessFile
implements|implements
name|RandomAccess
block|{
specifier|private
specifier|final
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
name|ras
decl_stmt|;
specifier|private
name|boolean
name|isClosed
decl_stmt|;
comment|/**      * Constructor.      *      * @param file The file to write the data to.      * @param mode The writing mode.      * @throws FileNotFoundException If the file cannot be created.      */
specifier|public
name|RandomAccessFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|ras
operator|=
operator|new
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
name|mode
argument_list|)
expr_stmt|;
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
name|ras
operator|.
name|close
argument_list|()
expr_stmt|;
name|isClosed
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|ras
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ras
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|checkClosed
argument_list|()
expr_stmt|;
name|ras
operator|.
name|seek
argument_list|(
name|position
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getPosition
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|ras
operator|.
name|getFilePointer
argument_list|()
return|;
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
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|ras
operator|.
name|read
argument_list|()
return|;
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
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|ras
operator|.
name|read
argument_list|(
name|b
argument_list|)
return|;
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
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|ras
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|ras
operator|.
name|length
argument_list|()
return|;
block|}
comment|/**      * Ensure that the RandomAccessFile is not closed      *       * @throws IOException      */
specifier|private
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|isClosed
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"RandomAccessFile already closed"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|isClosed
return|;
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
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|ras
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|offset
argument_list|,
name|length
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
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
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
name|checkClosed
argument_list|()
expr_stmt|;
name|ras
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
name|int
name|peek
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|result
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
operator|-
literal|1
condition|)
block|{
name|rewind
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|rewind
parameter_list|(
name|int
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|ras
operator|.
name|seek
argument_list|(
name|ras
operator|.
name|getFilePointer
argument_list|()
operator|-
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|readFully
parameter_list|(
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|length
index|]
decl_stmt|;
name|ras
operator|.
name|readFully
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEOF
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|peek
argument_list|()
operator|==
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|ras
operator|.
name|length
argument_list|()
operator|-
name|getPosition
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

