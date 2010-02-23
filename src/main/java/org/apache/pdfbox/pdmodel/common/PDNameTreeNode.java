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
comment|/**  * This class represends a PDF Name tree.  See the PDF Reference 1.5 section 3.8.5  * for more details.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDNameTreeNode
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|node
decl_stmt|;
specifier|private
name|Class
name|valueType
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      * @param valueClass The PD Model type of object that is the value.      */
specifier|public
name|PDNameTreeNode
parameter_list|(
name|Class
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
name|PDNameTreeNode
parameter_list|(
name|COSDictionary
name|dict
parameter_list|,
name|Class
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
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|node
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|node
return|;
block|}
comment|/**      * Return the children of this node.  This list will contain PDNameTreeNode objects.      *      * @return The list of children or null if there are no children.      */
specifier|public
name|List
name|getKids
parameter_list|()
block|{
name|List
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
name|pdObjects
init|=
operator|new
name|ArrayList
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
name|kids
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
literal|"Kids"
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
comment|/**      * The name to retrieve.      *      * @param name The name in the tree.      *      * @return The value of the name in the tree.      *      * @throws IOException If an there is a problem creating the destinations.      */
specifier|public
name|Object
name|getValue
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|retval
init|=
literal|null
decl_stmt|;
name|Map
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
name|kids
init|=
name|getKids
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
name|childNode
init|=
operator|(
name|PDNameTreeNode
operator|)
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
return|return
name|retval
return|;
block|}
comment|/**      * This will return a map of names.  The key will be a java.lang.String the value will      * depend on where this class is being used.      *      * @return A map of cos objects.      *      * @throws IOException If there is an error while creating the sub types.      */
specifier|public
name|Map
name|getNames
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
name|names
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
name|names
operator|=
operator|new
name|HashMap
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
name|Object
name|pdValue
init|=
name|convertCOSToPD
argument_list|(
name|cosValue
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
name|pdValue
argument_list|)
expr_stmt|;
block|}
name|names
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
comment|/**      * Method to convert the COS value in the name tree to the PD Model object.  The      * default implementation will simply use reflection to create the correct object      * type.  Subclasses can do whatever they want.      *      * @param base The COS object to convert.      * @return The converted PD Model object.      * @throws IOException If there is an error during creation.      */
specifier|protected
name|Object
name|convertCOSToPD
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|retval
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Constructor
name|ctor
init|=
name|valueType
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|base
operator|.
name|getClass
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|retval
operator|=
name|ctor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|base
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error while trying to create value in named tree:"
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Create a child node object.      *      * @param dic The dictionary for the child node object to refer to.      * @return The new child node object.      */
specifier|protected
name|PDNameTreeNode
name|createChildNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
return|return
operator|new
name|PDNameTreeNode
argument_list|(
name|dic
argument_list|,
name|valueType
argument_list|)
return|;
block|}
comment|/**      * Set the names of for this node.  The keys should be java.lang.String and the      * values must be a COSObjectable.  This method will set the appropriate upper and lower      * limits based on the keys in the map.      *      * @param names The map of names to objects.      */
specifier|public
name|void
name|setNames
parameter_list|(
name|Map
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
literal|"Names"
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
name|keys
init|=
operator|new
name|ArrayList
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
name|COSArray
name|array
init|=
operator|new
name|COSArray
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
name|keys
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
name|COSObjectable
name|obj
init|=
operator|(
name|COSObjectable
operator|)
name|names
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
name|String
name|lower
init|=
literal|null
decl_stmt|;
name|String
name|upper
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keys
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|lower
operator|=
operator|(
name|String
operator|)
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|upper
operator|=
operator|(
name|String
operator|)
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
literal|"Names"
argument_list|,
name|array
argument_list|)
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

