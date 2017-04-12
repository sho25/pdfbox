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
name|afm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * This class represents composite character data.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|Composite
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CompositePart
argument_list|>
name|parts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** Getter for property name.      * @return Value of property name.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/** Setter for property name.      * @param nameValue New value of property name.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|nameValue
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|nameValue
expr_stmt|;
block|}
comment|/**      * This will add a composite part.      *      * @param part The composite part to add.      */
specifier|public
name|void
name|addPart
parameter_list|(
name|CompositePart
name|part
parameter_list|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
comment|/** Getter for property parts.      * @return Value of property parts.      */
specifier|public
name|List
argument_list|<
name|CompositePart
argument_list|>
name|getParts
parameter_list|()
block|{
return|return
name|parts
return|;
block|}
comment|/** Setter for property parts.      * @param partsList New value of property parts.      */
specifier|public
name|void
name|setParts
parameter_list|(
name|List
argument_list|<
name|CompositePart
argument_list|>
name|partsList
parameter_list|)
block|{
name|this
operator|.
name|parts
operator|=
name|partsList
expr_stmt|;
block|}
block|}
end_class

end_unit

