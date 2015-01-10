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
name|documentinterchange
operator|.
name|logicalstructure
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|COSDictionaryMap
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
name|PDNameTreeNode
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
name|PDNumberTreeNode
import|;
end_import

begin_comment
comment|/**  * A root of a structure tree.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>,<a  * href="mailto:Johannes%20Koch%20%3Ckoch@apache.org%3E">Johannes Koch</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDStructureTreeRoot
extends|extends
name|PDStructureNode
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDStructureTreeRoot
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"StructTreeRoot"
decl_stmt|;
comment|/**      * Default Constructor.      *       */
specifier|public
name|PDStructureTreeRoot
parameter_list|()
block|{
name|super
argument_list|(
name|TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for an existing structure element.      *       * @param dic The existing dictionary.      */
specifier|public
name|PDStructureTreeRoot
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|super
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the K array entry.      *       * @return the K array entry      */
specifier|public
name|COSArray
name|getKArray
parameter_list|()
block|{
name|COSBase
name|k
init|=
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|k
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|kdict
init|=
operator|(
name|COSDictionary
operator|)
name|k
decl_stmt|;
name|k
operator|=
name|kdict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
name|COSArray
operator|)
name|k
return|;
block|}
block|}
else|else
block|{
return|return
operator|(
name|COSArray
operator|)
name|k
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the K entry.      *       * @return the K entry      */
specifier|public
name|COSBase
name|getK
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
return|;
block|}
comment|/**      * Sets the K entry.      *       * @param k the K value      */
specifier|public
name|void
name|setK
parameter_list|(
name|COSBase
name|k
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the ID tree.      *       * @return the ID tree      */
specifier|public
name|PDNameTreeNode
name|getIDTree
parameter_list|()
block|{
name|COSDictionary
name|idTreeDic
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ID_TREE
argument_list|)
decl_stmt|;
if|if
condition|(
name|idTreeDic
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDNameTreeNode
argument_list|(
name|idTreeDic
argument_list|,
name|PDStructureElement
operator|.
name|class
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the ID tree.      *       * @param idTree the ID tree      */
specifier|public
name|void
name|setIDTree
parameter_list|(
name|PDNameTreeNode
name|idTree
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ID_TREE
argument_list|,
name|idTree
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the parent tree.      *       * @return the parent tree      */
specifier|public
name|PDNumberTreeNode
name|getParentTree
parameter_list|()
block|{
name|COSDictionary
name|parentTreeDic
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PARENT_TREE
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentTreeDic
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDNumberTreeNode
argument_list|(
name|parentTreeDic
argument_list|,
name|COSBase
operator|.
name|class
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the parent tree.      *       * @param parentTree the parent tree      */
specifier|public
name|void
name|setParentTree
parameter_list|(
name|PDNumberTreeNode
name|parentTree
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PARENT_TREE
argument_list|,
name|parentTree
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the next key in the parent tree.      *       * @return the next key in the parent tree      */
specifier|public
name|int
name|getParentTreeNextKey
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|PARENT_TREE_NEXT_KEY
argument_list|)
return|;
block|}
comment|/**      * Sets the next key in the parent tree.      *       * @param parentTreeNextkey the next key in the parent tree.      */
specifier|public
name|void
name|setParentTreeNextKey
parameter_list|(
name|int
name|parentTreeNextkey
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PARENT_TREE_NEXT_KEY
argument_list|,
name|parentTreeNextkey
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the role map.      *       * @return the role map      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRoleMap
parameter_list|()
block|{
name|COSBase
name|rm
init|=
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ROLE_MAP
argument_list|)
decl_stmt|;
if|if
condition|(
name|rm
operator|instanceof
name|COSDictionary
condition|)
block|{
try|try
block|{
return|return
name|COSDictionaryMap
operator|.
name|convertBasicTypesToMap
argument_list|(
operator|(
name|COSDictionary
operator|)
name|rm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Sets the role map.      *       * @param roleMap the role map      */
specifier|public
name|void
name|setRoleMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|roleMap
parameter_list|)
block|{
name|COSDictionary
name|rmDic
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|roleMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|rmDic
operator|.
name|setName
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ROLE_MAP
argument_list|,
name|rmDic
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

