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
name|pdfdebugger
operator|.
name|treestatus
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
name|tools
operator|.
name|pdfdebugger
operator|.
name|ui
operator|.
name|ArrayEntry
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
name|tools
operator|.
name|pdfdebugger
operator|.
name|ui
operator|.
name|MapEntry
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
name|tools
operator|.
name|pdfdebugger
operator|.
name|ui
operator|.
name|PageEntry
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TreeStatus
block|{
specifier|private
name|Object
name|rootNode
decl_stmt|;
specifier|private
name|TreeStatus
parameter_list|()
block|{     }
comment|/**      * Constructor.      *      * @param rootNode the root node of the tree which will be used to construct a treepath from a      * tree status string.      */
specifier|public
name|TreeStatus
parameter_list|(
name|Object
name|rootNode
parameter_list|)
block|{
name|this
operator|.
name|rootNode
operator|=
name|rootNode
expr_stmt|;
block|}
comment|/**      * Provides status string for a TreePath instance.      * @param path TreePath instance.      * @return pathString.      */
specifier|public
name|String
name|getStringForPath
parameter_list|(
name|TreePath
name|path
parameter_list|)
block|{
return|return
name|generatePathString
argument_list|(
name|path
argument_list|)
return|;
block|}
comment|/**      * Provides TreePath for a given status string. In case of invalid string returns null.      * @param statusString      * @return path.      */
specifier|public
name|TreePath
name|getPathForString
parameter_list|(
name|String
name|statusString
parameter_list|)
block|{
return|return
name|generatePath
argument_list|(
name|statusString
argument_list|)
return|;
block|}
comment|/**      * Constructs a status string from the path.      * @param path      * @return the status string.      */
specifier|private
name|String
name|generatePathString
parameter_list|(
name|TreePath
name|path
parameter_list|)
block|{
name|StringBuilder
name|pathStringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|getParentPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|object
init|=
name|path
operator|.
name|getLastPathComponent
argument_list|()
decl_stmt|;
name|pathStringBuilder
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"/"
operator|+
name|getObjectName
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|getParentPath
argument_list|()
expr_stmt|;
block|}
name|pathStringBuilder
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|pathStringBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Constructs TreePath from Status String.      * @param pathString      * @return a TreePath, or null if there is an error.      */
specifier|private
name|TreePath
name|generatePath
parameter_list|(
name|String
name|pathString
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|nodes
init|=
name|parsePathString
argument_list|(
name|pathString
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|obj
init|=
name|rootNode
decl_stmt|;
name|TreePath
name|treePath
init|=
operator|new
name|TreePath
argument_list|(
name|obj
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|node
range|:
name|nodes
control|)
block|{
name|obj
operator|=
name|searchNode
argument_list|(
name|obj
argument_list|,
name|node
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|treePath
operator|=
name|treePath
operator|.
name|pathByAddingChild
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
return|return
name|treePath
return|;
block|}
comment|/**      * Get the object name of a tree node. If the given node of the tree is a MapEntry, its key is      * used as node identifier; if it is an ArrayEntry, then its index is used as identifier.      *      * @param treeNode node of a tree.      * @return the name of the node.      * @throws IllegalArgumentException if there is an unknown treeNode type.      */
specifier|private
name|String
name|getObjectName
parameter_list|(
name|Object
name|treeNode
parameter_list|)
block|{
if|if
condition|(
name|treeNode
operator|instanceof
name|MapEntry
condition|)
block|{
name|MapEntry
name|entry
init|=
operator|(
name|MapEntry
operator|)
name|treeNode
decl_stmt|;
name|COSName
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
return|return
name|key
operator|.
name|getName
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|treeNode
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|ArrayEntry
name|entry
init|=
operator|(
name|ArrayEntry
operator|)
name|treeNode
decl_stmt|;
return|return
literal|"["
operator|+
name|entry
operator|.
name|getIndex
argument_list|()
operator|+
literal|"]"
return|;
block|}
elseif|else
if|if
condition|(
name|treeNode
operator|instanceof
name|PageEntry
condition|)
block|{
name|PageEntry
name|entry
init|=
operator|(
name|PageEntry
operator|)
name|treeNode
decl_stmt|;
return|return
name|entry
operator|.
name|getPath
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown treeNode type: "
operator|+
name|treeNode
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|/**      * Parses a string and lists all the nodes.      *      * @param path a tree path.      * @return a list of nodes, or null if there is an empty node.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|parsePathString
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|node
range|:
name|path
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
control|)
block|{
name|node
operator|=
name|node
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|node
operator|=
name|node
operator|.
name|replace
argument_list|(
literal|"]"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"["
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|node
operator|=
name|node
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
name|nodes
return|;
block|}
comment|/**      * An object is searched in the tree structure using the identifiers parsed earlier step.      * @param obj      * @param searchStr      * @return      */
specifier|private
name|Object
name|searchNode
parameter_list|(
name|Object
name|obj
parameter_list|,
name|String
name|searchStr
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|MapEntry
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|MapEntry
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|ArrayEntry
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|COSObject
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|obj
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|dic
operator|.
name|containsKey
argument_list|(
name|searchStr
argument_list|)
condition|)
block|{
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
name|COSName
operator|.
name|getPDFName
argument_list|(
name|searchStr
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|searchStr
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|dic
operator|.
name|getItem
argument_list|(
name|searchStr
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|entry
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSArray
condition|)
block|{
name|int
name|index
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|searchStr
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|index
operator|<=
name|array
operator|.
name|size
argument_list|()
operator|-
literal|1
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
name|array
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
name|array
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|entry
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

