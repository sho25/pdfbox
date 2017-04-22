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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cmap
operator|.
name|CMap
operator|.
name|toInt
import|;
end_import

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
name|startInt
decl_stmt|;
specifier|private
name|int
name|endInt
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
name|endInt
operator|=
name|toInt
argument_list|(
name|endBytes
argument_list|,
name|endBytes
operator|.
name|length
argument_list|)
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
name|startInt
operator|=
name|toInt
argument_list|(
name|startBytes
argument_list|,
name|startBytes
operator|.
name|length
argument_list|)
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
name|int
name|value
init|=
name|toInt
argument_list|(
name|code
argument_list|,
name|codeLen
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|>=
name|startInt
operator|&&
name|value
operator|<=
name|endInt
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
block|}
end_class

end_unit

