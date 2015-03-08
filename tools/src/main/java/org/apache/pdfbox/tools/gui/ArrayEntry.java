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
name|tools
operator|.
name|gui
package|;
end_package

begin_comment
comment|/**  * This is a simple class that will contain an index and a value.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|ArrayEntry
block|{
specifier|private
name|int
name|index
decl_stmt|;
specifier|private
name|Object
name|value
decl_stmt|;
comment|/**      * This will get the value for this entry.      *      * @return The value for this entry.      */
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * This will set the value for this entry.      *      * @param val the new value for this entry.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|val
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|val
expr_stmt|;
block|}
comment|/**      * This will get the index of the array entry.      *      * @return The 0-based index into the array      */
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
comment|/**      * This will set the index value.      *      * @param i The new index value.      */
specifier|public
name|void
name|setIndex
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|index
operator|=
name|i
expr_stmt|;
block|}
block|}
end_class

end_unit

