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
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This is the used for the LZWDecode filter.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
class|class
name|LZWNode
block|{
specifier|private
specifier|final
name|long
name|code
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Byte
argument_list|,
name|LZWNode
argument_list|>
name|subNodes
init|=
operator|new
name|HashMap
argument_list|<
name|Byte
argument_list|,
name|LZWNode
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|LZWNode
parameter_list|(
name|long
name|codeValue
parameter_list|)
block|{
name|code
operator|=
name|codeValue
expr_stmt|;
block|}
comment|/**      * This will get the number of children.      *      * @return The number of children.      */
specifier|public
name|int
name|childCount
parameter_list|()
block|{
return|return
name|subNodes
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * This will set the node for a particular byte.      *      * @param b The byte for that node.      * @param node The node to add.      */
specifier|public
name|void
name|setNode
parameter_list|(
name|byte
name|b
parameter_list|,
name|LZWNode
name|node
parameter_list|)
block|{
name|subNodes
operator|.
name|put
argument_list|(
name|b
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the node that is a direct sub node of this node.      *      * @param data The byte code to the node.      *      * @return The node at that value if it exists.      */
specifier|public
name|LZWNode
name|getNode
parameter_list|(
name|byte
name|data
parameter_list|)
block|{
return|return
name|subNodes
operator|.
name|get
argument_list|(
name|data
argument_list|)
return|;
block|}
comment|/**      * This will traverse the tree until it gets to the sub node.      * This will return null if the node does not exist.      *      * @param data The path to the node.      *      * @return The node that resides at the data path.      */
specifier|public
name|LZWNode
name|getNode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|LZWNode
name|current
init|=
name|this
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
name|data
operator|.
name|length
operator|&&
name|current
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|current
operator|=
name|current
operator|.
name|getNode
argument_list|(
name|data
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|current
return|;
block|}
comment|/** Getter for property code.      * @return Value of property code.      */
specifier|public
name|long
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
block|}
end_class

end_unit

