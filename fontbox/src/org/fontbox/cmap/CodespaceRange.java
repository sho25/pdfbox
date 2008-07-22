begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|cmap
package|;
end_package

begin_comment
comment|/**  * This represents a single entry in the codespace range.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
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
comment|/**      * Creates a new instance of CodespaceRange.      */
specifier|public
name|CodespaceRange
parameter_list|()
block|{     }
comment|/** Getter for property end.      * @return Value of property end.      *      */
specifier|public
name|byte
index|[]
name|getEnd
parameter_list|()
block|{
return|return
name|this
operator|.
name|end
return|;
block|}
comment|/** Setter for property end.      * @param endBytes New value of property end.      *      */
specifier|public
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
name|this
operator|.
name|start
return|;
block|}
comment|/** Setter for property start.      * @param startBytes New value of property start.      *      */
specifier|public
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
block|}
block|}
end_class

end_unit

