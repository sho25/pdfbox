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
comment|/**  * A tree model that uses a cos document.  *  *  * @author wurtz  * @author Ben Litchfield  */
end_comment

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
name|Collections
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeModelListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|COSDocument
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
name|cos
operator|.
name|COSObject
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
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * A class to model a PDF document as a tree structure.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFTreeModel
implements|implements
name|TreeModel
block|{
specifier|private
name|Object
name|root
decl_stmt|;
comment|/**      * constructor.      */
specifier|public
name|PDFTreeModel
parameter_list|()
block|{
comment|//default constructor
block|}
comment|/**      * Constructor to take a document.      *      * @param doc The document to display in the tree.      */
specifier|public
name|PDFTreeModel
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|root
operator|=
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor to take a document.      *      * @param docEntry The document to display in the tree.      */
specifier|public
name|PDFTreeModel
parameter_list|(
name|DocumentEntry
name|docEntry
parameter_list|)
block|{
name|root
operator|=
name|docEntry
expr_stmt|;
block|}
comment|/**      * Adds a listener for the<code>TreeModelEvent</code>      * posted after the tree changes.      *      * @param   l       the listener to add      * @see     #removeTreeModelListener      *      */
annotation|@
name|Override
specifier|public
name|void
name|addTreeModelListener
parameter_list|(
name|TreeModelListener
name|l
parameter_list|)
block|{
comment|//required for interface
block|}
comment|/**      * Returns the child of<code>parent</code> at index<code>index</code> in the parent's child      * array.<code>parent</code> must be a node previously obtained from this data source. This      * should not return<code>null</code> if<code>index</code> is a valid index for      *<code>parent</code> (that is<code>index&gt;= 0&&      * index&lt; getChildCount(parent</code>)).      *      * @param parent a node in the tree, obtained from this data source      * @param index The index into the parent object to location the child object.      * @return the child of<code>parent</code> at index<code>index</code>      * @throws IllegalArgumentException if an unknown unknown COS type is passed as parent      * parameter.      */
annotation|@
name|Override
specifier|public
name|Object
name|getChild
parameter_list|(
name|Object
name|parent
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|Object
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parent
operator|instanceof
name|COSArray
condition|)
block|{
name|ArrayEntry
name|entry
init|=
operator|new
name|ArrayEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
operator|(
operator|(
name|COSArray
operator|)
name|parent
operator|)
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setItem
argument_list|(
operator|(
operator|(
name|COSArray
operator|)
name|parent
operator|)
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|=
name|entry
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|parent
decl_stmt|;
name|List
argument_list|<
name|COSName
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|COSName
argument_list|>
argument_list|(
name|dict
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|COSName
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|COSBase
name|value
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|MapEntry
name|entry
init|=
operator|new
name|MapEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setItem
argument_list|(
name|dict
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|retval
operator|=
name|entry
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|MapEntry
condition|)
block|{
name|retval
operator|=
name|getChild
argument_list|(
operator|(
operator|(
name|MapEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|retval
operator|=
name|getChild
argument_list|(
operator|(
operator|(
name|ArrayEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDocument
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSDocument
operator|)
name|parent
operator|)
operator|.
name|getObjects
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|DocumentEntry
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|DocumentEntry
operator|)
name|parent
operator|)
operator|.
name|getPage
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|PageEntry
condition|)
block|{
name|retval
operator|=
name|getChild
argument_list|(
operator|(
operator|(
name|PageEntry
operator|)
name|parent
operator|)
operator|.
name|getDict
argument_list|()
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSObject
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|parent
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown COS type "
operator|+
name|parent
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/** Returns the number of children of<code>parent</code>.      * Returns 0 if the node      * is a leaf or if it has no children.<code>parent</code> must be a node      * previously obtained from this data source.      *      * @param   parent  a node in the tree, obtained from this data source      * @return  the number of children of the node<code>parent</code>      *      */
annotation|@
name|Override
specifier|public
name|int
name|getChildCount
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|parent
operator|instanceof
name|COSArray
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSArray
operator|)
name|parent
operator|)
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDictionary
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSDictionary
operator|)
name|parent
operator|)
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|MapEntry
condition|)
block|{
name|retval
operator|=
name|getChildCount
argument_list|(
operator|(
operator|(
name|MapEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|retval
operator|=
name|getChildCount
argument_list|(
operator|(
operator|(
name|ArrayEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDocument
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSDocument
operator|)
name|parent
operator|)
operator|.
name|getObjects
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|DocumentEntry
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|DocumentEntry
operator|)
name|parent
operator|)
operator|.
name|getPageCount
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|PageEntry
condition|)
block|{
name|retval
operator|=
name|getChildCount
argument_list|(
operator|(
operator|(
name|PageEntry
operator|)
name|parent
operator|)
operator|.
name|getDict
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSObject
condition|)
block|{
name|retval
operator|=
literal|1
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Returns the index of child in parent. If<code>parent</code> is<code>null</code> or      *<code>child</code> is<code>null</code>, returns -1.      *      * @param parent a node in the tree, obtained from this data source      * @param child the node we are interested in      * @return the index of the child in the parent, or -1 if either<code>child</code> or      *<code>parent</code> are<code>null</code>      * @throws IllegalArgumentException if an unknown unknown COS type is passed as parent parameter.      */
annotation|@
name|Override
specifier|public
name|int
name|getIndexOfChild
parameter_list|(
name|Object
name|parent
parameter_list|,
name|Object
name|child
parameter_list|)
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|parent
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|parent
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|ArrayEntry
name|arrayEntry
init|=
operator|(
name|ArrayEntry
operator|)
name|child
decl_stmt|;
name|retval
operator|=
name|arrayEntry
operator|.
name|getIndex
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|array
operator|.
name|indexOf
argument_list|(
operator|(
name|COSBase
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDictionary
condition|)
block|{
name|MapEntry
name|entry
init|=
operator|(
name|MapEntry
operator|)
name|child
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|parent
decl_stmt|;
name|List
argument_list|<
name|COSName
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|COSName
argument_list|>
argument_list|(
name|dict
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|retval
operator|==
operator|-
literal|1
operator|&&
name|i
operator|<
name|keys
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|keys
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|retval
operator|=
name|i
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|MapEntry
condition|)
block|{
name|retval
operator|=
name|getIndexOfChild
argument_list|(
operator|(
operator|(
name|MapEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|retval
operator|=
name|getIndexOfChild
argument_list|(
operator|(
operator|(
name|ArrayEntry
operator|)
name|parent
operator|)
operator|.
name|getValue
argument_list|()
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSDocument
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSDocument
operator|)
name|parent
operator|)
operator|.
name|getObjects
argument_list|()
operator|.
name|indexOf
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|DocumentEntry
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|DocumentEntry
operator|)
name|parent
operator|)
operator|.
name|indexOf
argument_list|(
operator|(
name|PageEntry
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|PageEntry
condition|)
block|{
name|retval
operator|=
name|getIndexOfChild
argument_list|(
operator|(
operator|(
name|PageEntry
operator|)
name|parent
operator|)
operator|.
name|getDict
argument_list|()
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|instanceof
name|COSObject
condition|)
block|{
name|retval
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown COS type "
operator|+
name|parent
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/** Returns the root of the tree.  Returns<code>null</code>      * only if the tree has no nodes.      *      * @return  the root of the tree      *      */
annotation|@
name|Override
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/** Returns<code>true</code> if<code>node</code> is a leaf.      * It is possible for this method to return<code>false</code>      * even if<code>node</code> has no children.      * A directory in a filesystem, for example,      * may contain no files; the node representing      * the directory is not a leaf, but it also has no children.      *      * @param   node  a node in the tree, obtained from this data source      * @return  true if<code>node</code> is a leaf      *      */
annotation|@
name|Override
specifier|public
name|boolean
name|isLeaf
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|boolean
name|isLeaf
init|=
operator|!
operator|(
name|node
operator|instanceof
name|COSDictionary
operator|||
name|node
operator|instanceof
name|COSArray
operator|||
name|node
operator|instanceof
name|COSDocument
operator|||
name|node
operator|instanceof
name|DocumentEntry
operator|||
name|node
operator|instanceof
name|PageEntry
operator|||
name|node
operator|instanceof
name|COSObject
operator|||
operator|(
name|node
operator|instanceof
name|MapEntry
operator|&&
operator|!
name|isLeaf
argument_list|(
operator|(
operator|(
name|MapEntry
operator|)
name|node
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|node
operator|instanceof
name|ArrayEntry
operator|&&
operator|!
name|isLeaf
argument_list|(
operator|(
operator|(
name|ArrayEntry
operator|)
name|node
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
operator|)
operator|)
decl_stmt|;
return|return
name|isLeaf
return|;
block|}
comment|/** Removes a listener previously added with      *<code>addTreeModelListener</code>.      *      * @see     #addTreeModelListener      * @param   l       the listener to remove      *      */
annotation|@
name|Override
specifier|public
name|void
name|removeTreeModelListener
parameter_list|(
name|TreeModelListener
name|l
parameter_list|)
block|{
comment|//required for interface
block|}
comment|/** Messaged when the user has altered the value for the item identified      * by<code>path</code> to<code>newValue</code>.      * If<code>newValue</code> signifies a truly new value      * the model should post a<code>treeNodesChanged</code> event.      *      * @param path path to the node that the user has altered      * @param newValue the new value from the TreeCellEditor      *      */
annotation|@
name|Override
specifier|public
name|void
name|valueForPathChanged
parameter_list|(
name|TreePath
name|path
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
comment|//required for interface
block|}
block|}
end_class

end_unit

