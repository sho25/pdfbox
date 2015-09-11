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
name|form
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
comment|/**  * The field tree.  */
end_comment

begin_class
specifier|public
class|class
name|PDFieldTree
implements|implements
name|Iterable
argument_list|<
name|PDField
argument_list|>
block|{
specifier|private
specifier|final
name|PDAcroForm
name|acroForm
decl_stmt|;
comment|/**      * Constructor for reading.      *      * @param root A page tree root.      */
specifier|public
name|PDFieldTree
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
if|if
condition|(
name|acroForm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"root cannot be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|acroForm
operator|=
name|acroForm
expr_stmt|;
block|}
comment|/**      * Returns an iterator which walks all pages in the tree, in order.      */
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|PDField
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|FieldIterator
argument_list|(
name|acroForm
argument_list|)
return|;
block|}
comment|/**      * Iterator which walks all pages in the tree, in order.      */
specifier|private
specifier|final
class|class
name|FieldIterator
implements|implements
name|Iterator
argument_list|<
name|PDField
argument_list|>
block|{
specifier|private
specifier|final
name|Queue
argument_list|<
name|PDField
argument_list|>
name|queue
init|=
operator|new
name|ArrayDeque
argument_list|<
name|PDField
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|FieldIterator
parameter_list|(
name|PDAcroForm
name|form
parameter_list|)
block|{
name|List
argument_list|<
name|PDField
argument_list|>
name|fields
init|=
name|form
operator|.
name|getFields
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|field
range|:
name|fields
control|)
block|{
name|enqueueKids
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDField
name|next
parameter_list|()
block|{
return|return
name|queue
operator|.
name|poll
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|private
name|void
name|enqueueKids
parameter_list|(
name|PDField
name|node
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|PDNonTerminalField
condition|)
block|{
name|List
argument_list|<
name|PDField
argument_list|>
name|kids
init|=
operator|(
operator|(
name|PDNonTerminalField
operator|)
name|node
operator|)
operator|.
name|getChildren
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|kid
range|:
name|kids
control|)
block|{
name|enqueueKids
argument_list|(
name|kid
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

