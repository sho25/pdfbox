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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
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
name|COSBase
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
name|COSInteger
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
name|COSObjectable
import|;
end_import

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
comment|/**  * The page tree, which defines the ordering of pages in the document in an efficient manner.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDPageTree
implements|implements
name|COSObjectable
implements|,
name|Iterable
argument_list|<
name|PDPage
argument_list|>
block|{
specifier|private
specifier|final
name|COSDictionary
name|root
decl_stmt|;
comment|/**      * Constructor for embedding.      */
specifier|public
name|PDPageTree
parameter_list|()
block|{
name|root
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|PAGES
argument_list|)
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
operator|new
name|COSArray
argument_list|()
argument_list|)
expr_stmt|;
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for reading.      *      * @param root A page tree root.      */
specifier|public
name|PDPageTree
parameter_list|(
name|COSDictionary
name|root
parameter_list|)
block|{
if|if
condition|(
name|root
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
name|root
operator|=
name|root
expr_stmt|;
block|}
comment|/**      * Returns the given attribute, inheriting from parent tree nodes if necessary.      *      * @param node page object      * @param key the key to look up      * @return COS value for the given key      */
specifier|public
specifier|static
name|COSBase
name|getInheritableAttribute
parameter_list|(
name|COSDictionary
name|node
parameter_list|,
name|COSName
name|key
parameter_list|)
block|{
name|COSBase
name|value
init|=
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
name|COSDictionary
name|parent
init|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|getInheritableAttribute
argument_list|(
name|parent
argument_list|,
name|key
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns an iterator which walks all pages in the tree, in order.      */
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|PDPage
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|PageIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
comment|/**      * Helper to get kids from malformed PDFs.      * @param node page tree node      * @return list of kids      */
specifier|private
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|getKids
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|COSDictionary
argument_list|>
argument_list|()
decl_stmt|;
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
comment|// probably a malformed PDF
return|return
name|result
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|size
init|=
name|kids
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|(
name|COSDictionary
operator|)
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Iterator which walks all pages in the tree, in order.      */
specifier|private
specifier|final
class|class
name|PageIterator
implements|implements
name|Iterator
argument_list|<
name|PDPage
argument_list|>
block|{
specifier|private
specifier|final
name|Queue
argument_list|<
name|COSDictionary
argument_list|>
name|queue
init|=
operator|new
name|ArrayDeque
argument_list|<
name|COSDictionary
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|PageIterator
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
name|enqueueKids
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|enqueueKids
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
if|if
condition|(
name|isPageTreeNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|COSDictionary
argument_list|>
name|kids
init|=
name|getKids
argument_list|(
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|COSDictionary
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
else|else
block|{
name|queue
operator|.
name|add
argument_list|(
name|node
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
name|PDPage
name|next
parameter_list|()
block|{
name|COSDictionary
name|next
init|=
name|queue
operator|.
name|poll
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|next
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
operator|!=
name|COSName
operator|.
name|PAGE
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected Page but got "
operator|+
name|next
argument_list|)
throw|;
block|}
return|return
operator|new
name|PDPage
argument_list|(
name|next
argument_list|)
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
block|}
comment|/**      * Returns the page at the given index.      *      * @param index zero-based index      */
specifier|public
name|PDPage
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|COSDictionary
name|dict
init|=
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|root
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|dict
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
operator|!=
name|COSName
operator|.
name|PAGE
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected Page but got "
operator|+
name|dict
argument_list|)
throw|;
block|}
return|return
operator|new
name|PDPage
argument_list|(
name|dict
argument_list|)
return|;
block|}
comment|/**      * Returns the given COS page using a depth-first search.      *      * @param pageNum 1-based page number      * @param node page tree node to search      * @param encountered number of pages encountered so far      * @return COS dictionary of the Page object      */
specifier|private
name|COSDictionary
name|get
parameter_list|(
name|int
name|pageNum
parameter_list|,
name|COSDictionary
name|node
parameter_list|,
name|int
name|encountered
parameter_list|)
block|{
if|if
condition|(
name|pageNum
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index out of bounds: "
operator|+
name|pageNum
argument_list|)
throw|;
block|}
if|if
condition|(
name|isPageTreeNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|int
name|count
init|=
name|node
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|pageNum
operator|<=
name|encountered
operator|+
name|count
condition|)
block|{
comment|// it's a kid of this node
for|for
control|(
name|COSDictionary
name|kid
range|:
name|getKids
argument_list|(
name|node
argument_list|)
control|)
block|{
comment|// which kid?
if|if
condition|(
name|isPageTreeNode
argument_list|(
name|kid
argument_list|)
condition|)
block|{
name|int
name|kidCount
init|=
name|kid
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|pageNum
operator|<=
name|encountered
operator|+
name|kidCount
condition|)
block|{
comment|// it's this kid
return|return
name|get
argument_list|(
name|pageNum
argument_list|,
name|kid
argument_list|,
name|encountered
argument_list|)
return|;
block|}
else|else
block|{
name|encountered
operator|+=
name|kidCount
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// single page
name|encountered
operator|++
expr_stmt|;
if|if
condition|(
name|pageNum
operator|==
name|encountered
condition|)
block|{
comment|// it's this page
return|return
name|get
argument_list|(
name|pageNum
argument_list|,
name|kid
argument_list|,
name|encountered
argument_list|)
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index out of bounds: "
operator|+
name|pageNum
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|encountered
operator|==
name|pageNum
condition|)
block|{
return|return
name|node
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
block|}
comment|/**      * Returns true if the node is a page tree node (i.e. and intermediate).      */
specifier|private
name|boolean
name|isPageTreeNode
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
comment|// some files such as PDFBOX-2250-229205.pdf don't have Pages set as the Type, so we have
comment|// to check for the presence of Kids too
return|return
name|node
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
operator|==
name|COSName
operator|.
name|PAGES
operator|||
name|node
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
return|;
block|}
comment|/**      * Returns the index of the given page, or -1 if it does not exist.      */
specifier|public
name|int
name|indexOf
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|int
name|num
init|=
literal|0
decl_stmt|;
name|COSDictionary
name|node
init|=
name|page
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
do|do
block|{
if|if
condition|(
name|isPageTreeNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|// count kids up until this node
for|for
control|(
name|COSDictionary
name|kid
range|:
name|getKids
argument_list|(
name|node
argument_list|)
control|)
block|{
if|if
condition|(
name|kid
operator|==
name|node
condition|)
block|{
break|break;
block|}
name|num
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
name|num
operator|++
expr_stmt|;
block|}
name|node
operator|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|node
operator|!=
literal|null
condition|)
do|;
return|return
name|num
operator|-
literal|1
return|;
block|}
comment|/**      * Returns the number of leaf nodes (page objects) that are descendants of this root within the      * page tree.      */
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|root
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
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Removes the page with the given index from the page tree.      * @param index zero-based page index      */
specifier|public
name|void
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|COSDictionary
name|node
init|=
name|get
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|root
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes the given page from the page tree.      */
specifier|public
name|void
name|remove
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|remove
argument_list|(
name|page
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes the given COS page.      */
specifier|private
name|void
name|remove
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
comment|// remove from parent's kids
name|COSDictionary
name|parent
init|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|parent
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
name|kids
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// update ancestor counts
do|do
block|{
name|node
operator|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
name|node
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|)
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|node
operator|!=
literal|null
condition|)
do|;
block|}
comment|/**      * Adds the given page to this page tree.      */
specifier|public
name|void
name|add
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
comment|// set parent
name|COSDictionary
name|node
init|=
name|page
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|root
argument_list|)
expr_stmt|;
comment|// todo: re-balance tree? (or at least group new pages into tree nodes of e.g. 20)
comment|// add to parent's kids
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
name|kids
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// update ancestor counts
do|do
block|{
name|node
operator|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT
argument_list|,
name|COSName
operator|.
name|P
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|,
name|node
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COUNT
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|node
operator|!=
literal|null
condition|)
do|;
block|}
block|}
end_class

end_unit

