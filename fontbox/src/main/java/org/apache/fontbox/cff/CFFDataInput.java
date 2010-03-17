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
name|cff
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
comment|/**  * This is specialized DataInput. It's used to parse a CFFFont.  *   * @author Villu Ruusmann  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|CFFDataInput
extends|extends
name|DataInput
block|{
comment|/**      * Constructor.      * @param buffer the buffer to be read       */
specifier|public
name|CFFDataInput
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
name|super
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Read one single Card8 value from the buffer.       * @return the card8 value      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readCard8
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|readUnsignedByte
argument_list|()
return|;
block|}
comment|/**      * Read one single Card16 value from the buffer.      * @return the card16 value      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readCard16
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|readUnsignedShort
argument_list|()
return|;
block|}
comment|/**      * Read the offset from the buffer.      * @param offSize the given offsize      * @return the offset      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readOffset
parameter_list|(
name|int
name|offSize
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|value
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
name|i
operator|<
name|offSize
condition|;
name|i
operator|++
control|)
block|{
name|value
operator|=
name|value
operator|<<
literal|8
operator||
name|readUnsignedByte
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Read the offsize from the buffer.      * @return the offsize      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readOffSize
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|readUnsignedByte
argument_list|()
return|;
block|}
comment|/**      * Read a SID from the buffer.      * @return the SID      * @throws IOException if an error occurs during reading      */
specifier|public
name|int
name|readSID
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|readUnsignedShort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

