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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|HashMap
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

begin_comment
comment|/**  * This class represents a PDF Number tree. See the PDF Reference 1.7 section  * 7.9.7 for more details.  *  * @author Ben Litchfield,  * @author Igor Podolskiy  */
end_comment

begin_class
specifier|public
class|class
name|PDNumberTreeNode
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
name|PDNumberTreeNode
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
name|Class
argument_list|<
name|?
extends|extends
name|COSObjectable
argument_list|>
name|valueType
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      * @param valueClass The PD Model type of object that is the value.      */
specifier|public
name|PDNumberTreeNode
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|COSObjectable
argument_list|>
name|valueClass
parameter_list|)
block|{
name|node
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|valueType
operator|=
name|valueClass
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The dictionary that holds the name information.      * @param valueClass The PD Model type of object that is the value.      */
specifier|public
name|PDNumberTreeNode
parameter_list|(
name|COSDictionary
name|dict
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|COSObjectable
argument_list|>
name|valueClass
parameter_list|)
block|{
name|node
operator|=
name|dict
expr_stmt|;
name|valueType
operator|=
name|valueClass
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
comment|/**      * Return the children of this node.  This list will contain PDNumberTreeNode objects.      *      * @return The list of children or null if there are no children.      */
specifier|public
name|List
argument_list|<
name|PDNumberTreeNode
argument_list|>
name|getKids
parameter_list|()
block|{
name|List
argument_list|<
name|PDNumberTreeNode
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
name|PDNumberTreeNode
argument_list|>
name|pdObjects
init|=
operator|new
name|ArrayList
argument_list|<>
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
argument_list|<>
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
comment|/**      * Set the children of this number tree.      *      * @param kids The children of this number tree.      */
specifier|public
name|void
name|setKids
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|PDNumberTreeNode
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
operator|!
name|kids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PDNumberTreeNode
name|firstKid
init|=
name|kids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDNumberTreeNode
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
name|Integer
name|lowerLimit
init|=
name|firstKid
operator|.
name|getLowerLimit
argument_list|()
decl_stmt|;
name|this
operator|.
name|setLowerLimit
argument_list|(
name|lowerLimit
argument_list|)
expr_stmt|;
name|Integer
name|upperLimit
init|=
name|lastKid
operator|.
name|getUpperLimit
argument_list|()
decl_stmt|;
name|this
operator|.
name|setUpperLimit
argument_list|(
name|upperLimit
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// Remove limits if there are no kids and no numbers set.
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
block|}
comment|/**      * Returns the value corresponding to an index in the number tree.      *      * @param index The index in the number tree.      *      * @return The value corresponding to the index.      *      * @throws IOException If there is a problem creating the values.      */
specifier|public
name|Object
name|getValue
parameter_list|(
name|Integer
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|COSObjectable
argument_list|>
name|names
init|=
name|getNumbers
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
return|return
name|names
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
name|Object
name|retval
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|PDNumberTreeNode
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
name|PDNumberTreeNode
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
name|index
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
name|index
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
name|index
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
literal|"NumberTreeNode does not have \"nums\" nor \"kids\" objects."
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will return a map of numbers.  The key will be a java.lang.Integer, the value will      * depend on where this class is being used.      *      * @return A map of COS objects.      *      * @throws IOException If there is a problem creating the values.      */
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|COSObjectable
argument_list|>
name|getNumbers
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|COSObjectable
argument_list|>
name|indices
init|=
literal|null
decl_stmt|;
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
name|NUMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|namesArray
operator|!=
literal|null
condition|)
block|{
name|indices
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
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
name|COSInteger
name|key
init|=
operator|(
name|COSInteger
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
name|COSObjectable
name|pdValue
init|=
name|convertCOSToPD
argument_list|(
name|cosValue
argument_list|)
decl_stmt|;
name|indices
operator|.
name|put
argument_list|(
name|key
operator|.
name|intValue
argument_list|()
argument_list|,
name|pdValue
argument_list|)
expr_stmt|;
block|}
name|indices
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|indices
argument_list|)
expr_stmt|;
block|}
return|return
name|indices
return|;
block|}
comment|/**      * Method to convert the COS value in the name tree to the PD Model object.  The      * default implementation will simply use reflection to create the correct object      * type.  Subclasses can do whatever they want.      *      * @param base The COS object to convert.      * @return The converted PD Model object.      * @throws IOException If there is an error during creation.      */
specifier|protected
name|COSObjectable
name|convertCOSToPD
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
return|return
name|valueType
operator|.
name|getDeclaredConstructor
argument_list|(
name|base
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|(
name|base
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ReflectiveOperationException
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error while trying to create value in number tree:"
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create a child node object.      *      * @param dic The dictionary for the child node object to refer to.      * @return The new child node object.      */
specifier|protected
name|PDNumberTreeNode
name|createChildNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
return|return
operator|new
name|PDNumberTreeNode
argument_list|(
name|dic
argument_list|,
name|valueType
argument_list|)
return|;
block|}
comment|/**      * Set the names of for this node.  The keys should be java.lang.String and the      * values must be a COSObjectable.  This method will set the appropriate upper and lower      * limits based on the keys in the map.      *      * @param numbers The map of names to objects.      */
specifier|public
name|void
name|setNumbers
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|?
extends|extends
name|COSObjectable
argument_list|>
name|numbers
parameter_list|)
block|{
if|if
condition|(
name|numbers
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
name|NUMS
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
name|List
argument_list|<
name|Integer
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|numbers
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
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|key
range|:
name|keys
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|COSObjectable
name|obj
init|=
name|numbers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|Integer
name|lower
init|=
literal|null
decl_stmt|;
name|Integer
name|upper
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|keys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|lower
operator|=
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|upper
operator|=
name|keys
operator|.
name|get
argument_list|(
name|keys
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|setUpperLimit
argument_list|(
name|upper
argument_list|)
expr_stmt|;
name|setLowerLimit
argument_list|(
name|lower
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the highest value for a key in the name map.      *      * @return The highest value for a key in the map.      */
specifier|public
name|Integer
name|getUpperLimit
parameter_list|()
block|{
name|Integer
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
operator|&&
name|arr
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|arr
operator|.
name|getInt
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
name|Integer
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
if|if
condition|(
name|upper
operator|!=
literal|null
condition|)
block|{
name|arr
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|upper
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|arr
operator|.
name|set
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the lowest value for a key in the name map.      *      * @return The lowest value for a key in the map.      */
specifier|public
name|Integer
name|getLowerLimit
parameter_list|()
block|{
name|Integer
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
operator|&&
name|arr
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|arr
operator|.
name|getInt
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
name|Integer
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
if|if
condition|(
name|lower
operator|!=
literal|null
condition|)
block|{
name|arr
operator|.
name|setInt
argument_list|(
literal|0
argument_list|,
name|lower
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|arr
operator|.
name|set
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

