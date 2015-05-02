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
name|documentnavigation
operator|.
name|outline
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
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
name|cos
operator|.
name|COSName
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
name|pdmodel
operator|.
name|common
operator|.
name|PDDictionaryWrapper
import|;
end_import

begin_comment
comment|/**  * Base class for a node in the outline of a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDOutlineNode
extends|extends
name|PDDictionaryWrapper
block|{
comment|/**      * Default Constructor.      */
specifier|public
name|PDOutlineNode
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * @param dict The dictionary storage.      */
specifier|public
name|PDOutlineNode
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|super
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The parent of this node or null if there is no parent.      */
name|PDOutlineNode
name|getParent
parameter_list|()
block|{
name|COSDictionary
name|item
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|COSName
operator|.
name|OUTLINES
operator|.
name|equals
argument_list|(
name|item
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDDocumentOutline
argument_list|(
name|item
argument_list|)
return|;
block|}
return|return
operator|new
name|PDOutlineItem
argument_list|(
name|item
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
name|void
name|setParent
parameter_list|(
name|PDOutlineNode
name|parent
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given node to the bottom of the children list.      *      * @param newChild The node to add.      * @throws IllegalArgumentException if the given node is part of a list (i.e. if it has a previous or a next      * sibling)      */
specifier|public
name|void
name|addLast
parameter_list|(
name|PDOutlineItem
name|newChild
parameter_list|)
block|{
name|requireSingleNode
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
name|updateParentOpenCountForAddedChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given node to the top of the children list.      *       * @param newChild The node to add.      * @throws IllegalArgumentException if the given node is part of a list (i.e. if it has a previous or a next      * sibling)      */
specifier|public
name|void
name|addFirst
parameter_list|(
name|PDOutlineItem
name|newChild
parameter_list|)
block|{
name|requireSingleNode
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
name|prepend
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
name|updateParentOpenCountForAddedChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param node      * @throws IllegalArgumentException if the given node is part of a list (i.e. if it has a previous or a next      * sibling)      */
name|void
name|requireSingleNode
parameter_list|(
name|PDOutlineItem
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getNextSibling
argument_list|()
operator|!=
literal|null
operator|||
name|node
operator|.
name|getPreviousSibling
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A single node with no siblings is required"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Appends the child to the linked list of children. This method only adjust pointers but doesn't take care of the      * Count key in the parent hierarchy.      *       * @param newChild      */
specifier|private
name|void
name|append
parameter_list|(
name|PDOutlineItem
name|newChild
parameter_list|)
block|{
name|newChild
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasChildren
argument_list|()
condition|)
block|{
name|setFirstChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDOutlineItem
name|previousLastChild
init|=
name|getLastChild
argument_list|()
decl_stmt|;
name|previousLastChild
operator|.
name|setNextSibling
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
name|newChild
operator|.
name|setPreviousSibling
argument_list|(
name|previousLastChild
argument_list|)
expr_stmt|;
block|}
name|setLastChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prepends the child to the linked list of children. This method only adjust pointers but doesn't take care of the      * Count key in the parent hierarchy.      *       * @param newChild      */
specifier|private
name|void
name|prepend
parameter_list|(
name|PDOutlineItem
name|newChild
parameter_list|)
block|{
name|newChild
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasChildren
argument_list|()
condition|)
block|{
name|setLastChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDOutlineItem
name|previousFirstChild
init|=
name|getFirstChild
argument_list|()
decl_stmt|;
name|newChild
operator|.
name|setNextSibling
argument_list|(
name|previousFirstChild
argument_list|)
expr_stmt|;
name|previousFirstChild
operator|.
name|setPreviousSibling
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
name|setFirstChild
argument_list|(
name|newChild
argument_list|)
expr_stmt|;
block|}
name|void
name|updateParentOpenCountForAddedChild
parameter_list|(
name|PDOutlineItem
name|newChild
parameter_list|)
block|{
name|int
name|delta
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|newChild
operator|.
name|isNodeOpen
argument_list|()
condition|)
block|{
name|delta
operator|+=
name|newChild
operator|.
name|getOpenCount
argument_list|()
expr_stmt|;
block|}
name|newChild
operator|.
name|updateParentOpenCount
argument_list|(
name|delta
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the node has at least one child      */
specifier|public
name|boolean
name|hasChildren
parameter_list|()
block|{
return|return
name|getFirstChild
argument_list|()
operator|!=
literal|null
return|;
block|}
name|PDOutlineItem
name|getOutlineItem
parameter_list|(
name|COSName
name|name
parameter_list|)
block|{
name|COSDictionary
name|item
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDOutlineItem
argument_list|(
name|item
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @return The first child or null if there is no child.      */
specifier|public
name|PDOutlineItem
name|getFirstChild
parameter_list|()
block|{
return|return
name|getOutlineItem
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|)
return|;
block|}
comment|/**      * Set the first child, this will be maintained by this class.      *      * @param outlineNode The new first child.      */
name|void
name|setFirstChild
parameter_list|(
name|PDOutlineNode
name|outlineNode
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|,
name|outlineNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The last child or null if there is no child.      */
specifier|public
name|PDOutlineItem
name|getLastChild
parameter_list|()
block|{
return|return
name|getOutlineItem
argument_list|(
name|COSName
operator|.
name|LAST
argument_list|)
return|;
block|}
comment|/**      * Set the last child, this will be maintained by this class.      *      * @param outlineNode The new last child.      */
name|void
name|setLastChild
parameter_list|(
name|PDOutlineNode
name|outlineNode
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LAST
argument_list|,
name|outlineNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the number of open nodes or a negative number if this node is closed.      * See PDF Reference 32000-1:2008 table 152 and 153 for more details. This      * value is updated as you append children and siblings.      *      * @return The Count attribute of the outline dictionary.      */
specifier|public
name|int
name|getOpenCount
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Set the open count. This number is automatically managed for you when you add items to the outline.      *      * @param openCount The new open count.      */
name|void
name|setOpenCount
parameter_list|(
name|int
name|openCount
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
name|openCount
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set this node to be open when it is shown in the viewer. By default, when a new node is created it will      * be closed. This will do nothing if the node is already open.      */
specifier|public
name|void
name|openNode
parameter_list|()
block|{
comment|//if the node is already open then do nothing.
if|if
condition|(
operator|!
name|isNodeOpen
argument_list|()
condition|)
block|{
name|switchNodeCount
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Close this node.      *      */
specifier|public
name|void
name|closeNode
parameter_list|()
block|{
if|if
condition|(
name|isNodeOpen
argument_list|()
condition|)
block|{
name|switchNodeCount
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|switchNodeCount
parameter_list|()
block|{
name|int
name|openCount
init|=
name|getOpenCount
argument_list|()
decl_stmt|;
name|setOpenCount
argument_list|(
operator|-
name|openCount
argument_list|)
expr_stmt|;
name|updateParentOpenCount
argument_list|(
operator|-
name|openCount
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if this node count is greater than zero, false otherwise.      */
specifier|public
name|boolean
name|isNodeOpen
parameter_list|()
block|{
return|return
name|getOpenCount
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      * The count parameter needs to be updated when you add, remove, open or close outline items.      *      * @param delta The amount to update by.      */
name|void
name|updateParentOpenCount
parameter_list|(
name|int
name|delta
parameter_list|)
block|{
name|PDOutlineNode
name|parent
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|parent
operator|.
name|isNodeOpen
argument_list|()
condition|)
block|{
name|parent
operator|.
name|setOpenCount
argument_list|(
name|parent
operator|.
name|getOpenCount
argument_list|()
operator|+
name|delta
argument_list|)
expr_stmt|;
name|parent
operator|.
name|updateParentOpenCount
argument_list|(
name|delta
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parent
operator|.
name|setOpenCount
argument_list|(
name|parent
operator|.
name|getOpenCount
argument_list|()
operator|-
name|delta
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @return An {@link Iterable} view of the items children      */
specifier|public
name|Iterable
argument_list|<
name|PDOutlineItem
argument_list|>
name|children
parameter_list|()
block|{
return|return
operator|new
name|Iterable
argument_list|<
name|PDOutlineItem
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|PDOutlineItem
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|PDOutlineItemIterator
argument_list|(
name|getFirstChild
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

