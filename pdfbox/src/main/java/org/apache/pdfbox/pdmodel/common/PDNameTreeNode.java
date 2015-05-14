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
name|common
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
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * This class represents a node in a name tree.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDNameTreeNode
parameter_list|<
name|T
extends|extends
name|COSObjectable
parameter_list|>
implements|implements
name|COSObjectable
block|{
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
name|PDNameTreeNode
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|node
decl_stmt|;
specifier|private
name|PDNameTreeNode
name|parent
decl_stmt|;
comment|/**      * Constructor.      */
specifier|protected
name|PDNameTreeNode
parameter_list|()
block|{
name|node
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The dictionary that holds the name information.      */
specifier|protected
name|PDNameTreeNode
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|node
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|node
return|;
block|}
comment|/**      * Returns the parent node.      *       * @return parent node      */
specifier|public
name|PDNameTreeNode
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/**      * Sets the parent to the given node.      *       * @param parentNode the node to be set as parent      */
specifier|public
name|void
name|setParent
parameter_list|(
name|PDNameTreeNode
name|parentNode
parameter_list|)
block|{
name|parent
operator|=
name|parentNode
expr_stmt|;
name|calculateLimits
argument_list|()
expr_stmt|;
block|}
comment|/**      * Determines if this is a root node or not.      *       * @return true if this is a root node      */
specifier|public
name|boolean
name|isRootNode
parameter_list|()
block|{
return|return
name|parent
operator|==
literal|null
return|;
block|}
comment|/**      * Return the children of this node.  This list will contain PDNameTreeNode objects.      *      * @return The list of children or null if there are no children.      */
specifier|public
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|getKids
parameter_list|()
block|{
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|retval
init|=
literal|null
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
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|pdObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
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
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|pdObjects
operator|.
name|add
argument_list|(
name|createChildNode
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
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|(
name|pdObjects
argument_list|,
name|kids
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the children of this named tree.      *      * @param kids The children of this named tree.      */
specifier|public
name|void
name|setKids
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|kids
parameter_list|)
block|{
if|if
condition|(
name|kids
operator|!=
literal|null
operator|&&
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|PDNameTreeNode
name|kidsNode
range|:
name|kids
control|)
block|{
name|kidsNode
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|kids
argument_list|)
argument_list|)
expr_stmt|;
comment|// root nodes with kids don't have Names
if|if
condition|(
name|isRootNode
argument_list|()
condition|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// remove kids
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// remove Limits
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|calculateLimits
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|calculateLimits
parameter_list|()
block|{
if|if
condition|(
name|isRootNode
argument_list|()
condition|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
operator|&&
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|PDNameTreeNode
name|firstKid
init|=
name|kids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDNameTreeNode
name|lastKid
init|=
name|kids
operator|.
name|get
argument_list|(
name|kids
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|lowerLimit
init|=
name|firstKid
operator|.
name|getLowerLimit
argument_list|()
decl_stmt|;
name|setLowerLimit
argument_list|(
name|lowerLimit
argument_list|)
expr_stmt|;
name|String
name|upperLimit
init|=
name|lastKid
operator|.
name|getUpperLimit
argument_list|()
decl_stmt|;
name|setUpperLimit
argument_list|(
name|upperLimit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|names
init|=
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
operator|&&
name|names
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|strings
init|=
name|names
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|String
index|[]
name|keys
init|=
name|strings
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|strings
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|String
name|lowerLimit
init|=
name|keys
index|[
literal|0
index|]
decl_stmt|;
name|setLowerLimit
argument_list|(
name|lowerLimit
argument_list|)
expr_stmt|;
name|String
name|upperLimit
init|=
name|keys
index|[
name|keys
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|setUpperLimit
argument_list|(
name|upperLimit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Error while calculating the Limits of a PageNameTreeNode:"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * The name to retrieve.      *      * @param name The name in the tree.      * @return The value of the name in the tree.      * @throws IOException If an there is a problem creating the destinations.      */
specifier|public
name|T
name|getValue
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|T
name|retval
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|names
init|=
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|names
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|kids
operator|.
name|size
argument_list|()
operator|&&
name|retval
operator|==
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
name|childNode
init|=
name|kids
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|childNode
operator|.
name|getLowerLimit
argument_list|()
operator|.
name|compareTo
argument_list|(
name|name
argument_list|)
operator|<=
literal|0
operator|&&
name|childNode
operator|.
name|getUpperLimit
argument_list|()
operator|.
name|compareTo
argument_list|(
name|name
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|retval
operator|=
name|childNode
operator|.
name|getValue
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"NameTreeNode does not have \"names\" nor \"kids\" objects."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will return a map of names. The key will be a string, and the      * value will depend on where this class is being used.      *      * @return ordered map of cos objects or<code>null</code> if dictionary      *         contains no 'Names' entry      * @throws IOException If there is an error while creating the sub types.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|getNames
parameter_list|()
throws|throws
name|IOException
block|{
name|COSArray
name|namesArray
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
name|NAMES
argument_list|)
decl_stmt|;
if|if
condition|(
name|namesArray
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|names
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|()
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
name|namesArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|COSString
name|key
init|=
operator|(
name|COSString
operator|)
name|namesArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSBase
name|cosValue
init|=
name|namesArray
operator|.
name|getObject
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|names
operator|.
name|put
argument_list|(
name|key
operator|.
name|getString
argument_list|()
argument_list|,
name|convertCOSToPD
argument_list|(
name|cosValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|names
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Method to convert the COS value in the name tree to the PD Model object. The      * default implementation will simply return the given COSBase object.      * Subclasses should do something specific.      *      * @param base The COS object to convert.      * @return The converted PD Model object.      * @throws IOException If there is an error during creation.      */
specifier|protected
specifier|abstract
name|T
name|convertCOSToPD
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Create a child node object.      *      * @param dic The dictionary for the child node object to refer to.      * @return The new child node object.      */
specifier|protected
specifier|abstract
name|PDNameTreeNode
argument_list|<
name|T
argument_list|>
name|createChildNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
function_decl|;
comment|/**      * Set the names of for this node.  The keys should be java.lang.String and the      * values must be a COSObjectable.  This method will set the appropriate upper and lower      * limits based on the keys in the map.      *      * @param names map of names to objects, or<code>null</code>      */
specifier|public
name|void
name|setNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|names
parameter_list|)
block|{
if|if
condition|(
name|names
operator|==
literal|null
condition|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|,
operator|(
name|COSObjectable
operator|)
literal|null
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
operator|(
name|COSObjectable
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|names
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
name|String
name|key
range|:
name|keys
control|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|names
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAMES
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|calculateLimits
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Get the highest value for a key in the name map.      *      * @return The highest value for a key in the map.      */
specifier|public
name|String
name|getUpperLimit
parameter_list|()
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|arr
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
name|LIMITS
argument_list|)
decl_stmt|;
if|if
condition|(
name|arr
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|arr
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the highest value for the key in the map.      *      * @param upper The new highest value for a key in the map.      */
specifier|private
name|void
name|setUpperLimit
parameter_list|(
name|String
name|upper
parameter_list|)
block|{
name|COSArray
name|arr
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
name|LIMITS
argument_list|)
decl_stmt|;
if|if
condition|(
name|arr
operator|==
literal|null
condition|)
block|{
name|arr
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|arr
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|arr
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
name|arr
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|upper
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the lowest value for a key in the name map.      *      * @return The lowest value for a key in the map.      */
specifier|public
name|String
name|getLowerLimit
parameter_list|()
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|arr
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
name|LIMITS
argument_list|)
decl_stmt|;
if|if
condition|(
name|arr
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|arr
operator|.
name|getString
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the lowest value for the key in the map.      *      * @param lower The new lowest value for a key in the map.      */
specifier|private
name|void
name|setLowerLimit
parameter_list|(
name|String
name|lower
parameter_list|)
block|{
name|COSArray
name|arr
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
name|LIMITS
argument_list|)
decl_stmt|;
if|if
condition|(
name|arr
operator|==
literal|null
condition|)
block|{
name|arr
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|arr
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|arr
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LIMITS
argument_list|,
name|arr
argument_list|)
expr_stmt|;
block|}
name|arr
operator|.
name|setString
argument_list|(
literal|0
argument_list|,
name|lower
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

