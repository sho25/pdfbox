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
comment|/**  * An interface to allow temp PDF data to be stored in a scratch  * file on the disk to reduce memory consumption.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|RandomAccessFile
implements|implements
name|RandomAccess
block|{
specifier|private
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
name|ras
decl_stmt|;
comment|/**      * Constructor.      *       * @param file The file to write the data to.      * @param mode The writing mode.      * @throws FileNotFoundException If the file cannot be created.      */
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
comment|/**      * {@inheritDoc}      */
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
name|ras
operator|.
name|seek
argument_list|(
name|position
argument_list|)
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
return|return
name|ras
operator|.
name|read
argument_list|()
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
comment|/**      * {@inheritDoc}      */
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ras
operator|.
name|length
argument_list|()
return|;
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
name|ras
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

