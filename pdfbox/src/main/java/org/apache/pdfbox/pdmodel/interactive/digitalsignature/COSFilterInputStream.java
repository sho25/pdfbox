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
name|pdmodel
operator|.
name|interactive
operator|.
name|digitalsignature
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
name|io
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * A filtered stream that includes the bytes that are in the (begin,length) intervals passed in the  * constructor.  */
end_comment

begin_class
specifier|public
class|class
name|COSFilterInputStream
extends|extends
name|FilterInputStream
block|{
comment|/**   * Log instance.    */
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
name|COSFilterInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|byteRange
decl_stmt|;
specifier|private
name|long
name|position
init|=
literal|0
decl_stmt|;
specifier|public
name|COSFilterInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|int
index|[]
name|byteRange
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|byteRange
operator|=
name|byteRange
expr_stmt|;
block|}
specifier|public
name|COSFilterInputStream
parameter_list|(
name|byte
index|[]
name|in
parameter_list|,
name|int
index|[]
name|byteRange
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|byteRange
operator|=
name|byteRange
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
name|nextAvailable
argument_list|()
expr_stmt|;
name|int
name|i
init|=
name|super
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|>
operator|-
literal|1
condition|)
block|{
operator|++
name|position
expr_stmt|;
block|}
return|return
name|i
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
return|return
name|read
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
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
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|c
init|=
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|b
index|[
name|off
index|]
operator|=
operator|(
name|byte
operator|)
name|c
expr_stmt|;
name|int
name|i
init|=
literal|1
decl_stmt|;
try|try
block|{
for|for
control|(
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|c
operator|=
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|b
index|[
name|off
operator|+
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|c
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ee
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"An exception occured while trying to fill byte[] - ignoring"
argument_list|,
name|ee
argument_list|)
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
specifier|private
name|boolean
name|inRange
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|pos
init|=
name|position
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|byteRange
operator|.
name|length
operator|/
literal|2
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|byteRange
index|[
name|i
operator|*
literal|2
index|]
operator|<=
name|pos
operator|&&
name|byteRange
index|[
name|i
operator|*
literal|2
index|]
operator|+
name|byteRange
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
operator|>
name|pos
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|nextAvailable
parameter_list|()
throws|throws
name|IOException
block|{
while|while
condition|(
operator|!
name|inRange
argument_list|()
condition|)
block|{
operator|++
name|position
expr_stmt|;
if|if
condition|(
name|super
operator|.
name|read
argument_list|()
operator|<
literal|0
condition|)
block|{
break|break;
block|}
block|}
block|}
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

