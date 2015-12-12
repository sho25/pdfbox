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
name|cmap
package|;
end_package

begin_comment
comment|/**  * This represents a single entry in the codespace range.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|CodespaceRange
block|{
specifier|private
name|byte
index|[]
name|start
decl_stmt|;
specifier|private
name|byte
index|[]
name|end
decl_stmt|;
specifier|private
name|int
name|codeLength
init|=
literal|0
decl_stmt|;
comment|/**      * Creates a new instance of CodespaceRange.      */
specifier|public
name|CodespaceRange
parameter_list|()
block|{     }
comment|/**      * Returns the length of the codes of the codespace.      *       * @return the code length      */
specifier|public
name|int
name|getCodeLength
parameter_list|()
block|{
return|return
name|codeLength
return|;
block|}
comment|/** Getter for property end.      * @return Value of property end.      *      */
specifier|public
name|byte
index|[]
name|getEnd
parameter_list|()
block|{
return|return
name|end
return|;
block|}
comment|/** Setter for property end.      * @param endBytes New value of property end.      *      */
name|void
name|setEnd
parameter_list|(
name|byte
index|[]
name|endBytes
parameter_list|)
block|{
name|end
operator|=
name|endBytes
expr_stmt|;
block|}
comment|/** Getter for property start.      * @return Value of property start.      *      */
specifier|public
name|byte
index|[]
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
comment|/** Setter for property start.      * @param startBytes New value of property start.      *      */
name|void
name|setStart
parameter_list|(
name|byte
index|[]
name|startBytes
parameter_list|)
block|{
name|start
operator|=
name|startBytes
expr_stmt|;
name|codeLength
operator|=
name|start
operator|.
name|length
expr_stmt|;
block|}
comment|/**      * Returns true if the given code bytes match this codespace range.      */
specifier|public
name|boolean
name|matches
parameter_list|(
name|byte
index|[]
name|code
parameter_list|)
block|{
return|return
name|isFullMatch
argument_list|(
name|code
argument_list|,
name|code
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**      * Returns true if the given code bytes match this codespace range.      */
specifier|public
name|boolean
name|isFullMatch
parameter_list|(
name|byte
index|[]
name|code
parameter_list|,
name|int
name|codeLen
parameter_list|)
block|{
comment|// code must be the same length as the bounding codes
if|if
condition|(
name|codeLen
operator|==
name|codeLength
condition|)
block|{
comment|// each of it bytes must lie between the corresponding bytes of the upper& lower bounds
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|codeLen
condition|;
name|i
operator|++
control|)
block|{
name|int
name|startNum
init|=
name|start
index|[
name|i
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|endNum
init|=
name|end
index|[
name|i
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|codeNum
init|=
name|code
index|[
name|i
index|]
operator|&
literal|0xff
decl_stmt|;
if|if
condition|(
name|codeNum
operator|>
name|endNum
operator|||
name|codeNum
operator|<
name|startNum
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if the given byte matches the byte at the given index of this codespace range.      */
specifier|public
name|boolean
name|isPartialMatch
parameter_list|(
name|byte
name|b
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|==
name|codeLength
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|startNum
init|=
name|start
index|[
name|index
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|endNum
init|=
name|end
index|[
name|index
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|codeNum
init|=
name|b
operator|&
literal|0xff
decl_stmt|;
return|return
operator|!
operator|(
name|codeNum
operator|>
name|endNum
operator|||
name|codeNum
operator|<
name|startNum
operator|)
return|;
block|}
block|}
end_class

end_unit

