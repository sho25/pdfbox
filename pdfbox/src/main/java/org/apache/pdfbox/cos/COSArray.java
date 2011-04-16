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
name|cos
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
name|Collection
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
name|List
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
name|exceptions
operator|.
name|COSVisitorException
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

begin_comment
comment|/**  * An array of PDFBase objects as part of the PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.24 $  */
end_comment

begin_class
specifier|public
class|class
name|COSArray
extends|extends
name|COSBase
implements|implements
name|Iterable
argument_list|<
name|COSBase
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|COSBase
argument_list|>
name|objects
init|=
operator|new
name|ArrayList
argument_list|<
name|COSBase
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|COSArray
parameter_list|()
block|{
comment|//default constructor
block|}
comment|/**      * This will add an object to the array.      *      * @param object The object to add to the array.      */
specifier|public
name|void
name|add
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
name|objects
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add an object to the array.      *      * @param object The object to add to the array.      */
specifier|public
name|void
name|add
parameter_list|(
name|COSObjectable
name|object
parameter_list|)
block|{
name|objects
operator|.
name|add
argument_list|(
name|object
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the specified object at the ith location and push the rest to the      * right.      *      * @param i The index to add at.      * @param object The object to add at that index.      */
specifier|public
name|void
name|add
parameter_list|(
name|int
name|i
parameter_list|,
name|COSBase
name|object
parameter_list|)
block|{
name|objects
operator|.
name|add
argument_list|(
name|i
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will remove all of the objects in the collection.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|objects
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will remove all of the objects in the collection.      *      * @param objectsList The list of objects to remove from the collection.      */
specifier|public
name|void
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|COSBase
argument_list|>
name|objectsList
parameter_list|)
block|{
name|objects
operator|.
name|removeAll
argument_list|(
name|objectsList
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retain all of the objects in the collection.      *      * @param objectsList The list of objects to retain from the collection.      */
specifier|public
name|void
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|COSBase
argument_list|>
name|objectsList
parameter_list|)
block|{
name|objects
operator|.
name|retainAll
argument_list|(
name|objectsList
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add an object to the array.      *      * @param objectsList The object to add to the array.      */
specifier|public
name|void
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|COSBase
argument_list|>
name|objectsList
parameter_list|)
block|{
name|objects
operator|.
name|addAll
argument_list|(
name|objectsList
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add all objects to this array.      *      * @param objectList The objects to add.      */
specifier|public
name|void
name|addAll
parameter_list|(
name|COSArray
name|objectList
parameter_list|)
block|{
if|if
condition|(
name|objectList
operator|!=
literal|null
condition|)
block|{
name|objects
operator|.
name|addAll
argument_list|(
name|objectList
operator|.
name|objects
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add the specified object at the ith location and push the rest to the      * right.      *      * @param i The index to add at.      * @param objectList The object to add at that index.      */
specifier|public
name|void
name|addAll
parameter_list|(
name|int
name|i
parameter_list|,
name|Collection
argument_list|<
name|COSBase
argument_list|>
name|objectList
parameter_list|)
block|{
name|objects
operator|.
name|addAll
argument_list|(
name|i
argument_list|,
name|objectList
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set an object at a specific index.      *      * @param index zero based index into array.      * @param object The object to set.      */
specifier|public
name|void
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|COSBase
name|object
parameter_list|)
block|{
name|objects
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set an object at a specific index.      *      * @param index zero based index into array.      * @param intVal The object to set.      */
specifier|public
name|void
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|intVal
parameter_list|)
block|{
name|objects
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
name|intVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set an object at a specific index.      *      * @param index zero based index into array.      * @param object The object to set.      */
specifier|public
name|void
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|COSObjectable
name|object
parameter_list|)
block|{
name|COSBase
name|base
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|base
operator|=
name|object
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|objects
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get an object from the array.  This will dereference the object.      * If the object is COSNull then null will be returned.      *      * @param index The index into the array to get the object.      *      * @return The object at the requested index.      */
specifier|public
name|COSBase
name|getObject
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Object
name|obj
init|=
name|objects
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
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
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|COSNull
condition|)
block|{
name|obj
operator|=
literal|null
expr_stmt|;
block|}
return|return
operator|(
name|COSBase
operator|)
name|obj
return|;
block|}
comment|/**      * This will get an object from the array.  This will NOT derefernce      * the COS object.      *      * @param index The index into the array to get the object.      *      * @return The object at the requested index.      */
specifier|public
name|COSBase
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|objects
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * Get the value of the array as an integer.      *      * @param index The index into the list.      *      * @return The value at that index or -1 if it is null.      */
specifier|public
name|int
name|getInt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|getInt
argument_list|(
name|index
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Get the value of the array as an integer, return the default if it does      * not exist.      *      * @param index The value of the array.      * @param defaultValue The value to return if the value is null.      * @return The value at the index or the defaultValue.      */
specifier|public
name|int
name|getInt
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|int
name|retval
init|=
name|defaultValue
decl_stmt|;
if|if
condition|(
name|index
operator|<
name|size
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|objects
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSNumber
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the value in the array as an integer.      *      * @param index The index into the array.      * @param value The value to set.      */
specifier|public
name|void
name|setInt
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|set
argument_list|(
name|index
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the value in the array as a name.      * @param index The index into the array.      * @param name The name to set in the array.      */
specifier|public
name|void
name|setName
parameter_list|(
name|int
name|index
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|set
argument_list|(
name|index
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the value of the array as a string.      *      * @param index The index into the array.      * @return The name converted to a string or null if it does not exist.      */
specifier|public
name|String
name|getName
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|getName
argument_list|(
name|index
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Get an entry in the array that is expected to be a COSName.      * @param index The index into the array.      * @param defaultValue The value to return if it is null.      * @return The value at the index or defaultValue if none is found.      */
specifier|public
name|String
name|getName
parameter_list|(
name|int
name|index
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|retval
init|=
name|defaultValue
decl_stmt|;
if|if
condition|(
name|index
operator|<
name|size
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|objects
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSName
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSName
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the value in the array as a string.      * @param index The index into the array.      * @param string The string to set in the array.      */
specifier|public
name|void
name|setString
parameter_list|(
name|int
name|index
parameter_list|,
name|String
name|string
parameter_list|)
block|{
name|set
argument_list|(
name|index
argument_list|,
operator|new
name|COSString
argument_list|(
name|string
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the value of the array as a string.      *      * @param index The index into the array.      * @return The string or null if it does not exist.      */
specifier|public
name|String
name|getString
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|getString
argument_list|(
name|index
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Get an entry in the array that is expected to be a COSName.      * @param index The index into the array.      * @param defaultValue The value to return if it is null.      * @return The value at the index or defaultValue if none is found.      */
specifier|public
name|String
name|getString
parameter_list|(
name|int
name|index
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|retval
init|=
name|defaultValue
decl_stmt|;
if|if
condition|(
name|index
operator|<
name|size
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|objects
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSString
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSString
operator|)
name|obj
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the size of this array.      *      * @return The number of elements in the array.      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|objects
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * This will remove an element from the array.      *      * @param i The index of the object to remove.      *      * @return The object that was removed.      */
specifier|public
name|COSBase
name|remove
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|objects
operator|.
name|remove
argument_list|(
name|i
argument_list|)
return|;
block|}
comment|/**      * This will remove an element from the array.      *      * @param o The object to remove.      *      * @return<code>true</code> if the object was removed,<code>false</code>      *  otherwise      */
specifier|public
name|boolean
name|remove
parameter_list|(
name|COSBase
name|o
parameter_list|)
block|{
return|return
name|objects
operator|.
name|remove
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * This will remove an element from the array.      * This method will also remove a reference to the object.      *      * @param o The object to remove.      * @return<code>true</code> if the object was removed,<code>false</code>      *  otherwise      */
specifier|public
name|boolean
name|removeObject
parameter_list|(
name|COSBase
name|o
parameter_list|)
block|{
name|boolean
name|removed
init|=
name|this
operator|.
name|remove
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|removed
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
name|this
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|entry
init|=
name|this
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|objEntry
init|=
operator|(
name|COSObject
operator|)
name|entry
decl_stmt|;
if|if
condition|(
name|objEntry
operator|.
name|getObject
argument_list|()
operator|.
name|equals
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
return|;
block|}
block|}
block|}
block|}
return|return
name|removed
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSArray{"
operator|+
name|objects
operator|+
literal|"}"
return|;
block|}
comment|/**      * Get access to the list.      *      * @return an iterator over the array elements      */
specifier|public
name|Iterator
argument_list|<
name|COSBase
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|objects
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * This will return the index of the entry or -1 if it is not found.      *      * @param object The object to search for.      * @return The index of the object or -1.      */
specifier|public
name|int
name|indexOf
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|retval
operator|<
literal|0
operator|&&
name|i
operator|<
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|retval
operator|=
name|i
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will return the index of the entry or -1 if it is not found.      * This method will also find references to indirect objects.      *      * @param object The object to search for.      * @return The index of the object or -1.      */
specifier|public
name|int
name|indexOfObject
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|retval
operator|<
literal|0
operator|&&
name|i
operator|<
name|this
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|item
init|=
name|this
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|retval
operator|=
name|i
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|item
operator|instanceof
name|COSObject
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|COSObject
operator|)
name|item
operator|)
operator|.
name|getObject
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|retval
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will add null values until the size of the array is at least      * as large as the parameter.  If the array is already larger than the      * parameter then nothing is done.      *      * @param size The desired size of the array.      */
specifier|public
name|void
name|growToSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|growToSize
argument_list|(
name|size
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add the object until the size of the array is at least      * as large as the parameter.  If the array is already larger than the      * parameter then nothing is done.      *      * @param size The desired size of the array.      * @param object The object to fill the array with.      */
specifier|public
name|void
name|growToSize
parameter_list|(
name|int
name|size
parameter_list|,
name|COSBase
name|object
parameter_list|)
block|{
while|while
condition|(
name|size
argument_list|()
operator|<
name|size
condition|)
block|{
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws COSVisitorException If an error occurs while visiting this object.      */
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|COSVisitorException
block|{
return|return
name|visitor
operator|.
name|visitFromArray
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * This will take an COSArray of numbers and convert it to a float[].      *      * @return This COSArray as an array of float numbers.      */
specifier|public
name|float
index|[]
name|toFloatArray
parameter_list|()
block|{
name|float
index|[]
name|retval
init|=
operator|new
name|float
index|[
name|size
argument_list|()
index|]
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
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|retval
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Clear the current contents of the COSArray and set it with the float[].      *      * @param value The new value of the float array.      */
specifier|public
name|void
name|setFloatArray
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
block|{
name|this
operator|.
name|clear
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
name|value
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|value
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *  Return contents of COSArray as a Java List.      *      *  @return the COSArray as List      */
specifier|public
name|List
argument_list|<
name|COSBase
argument_list|>
name|toList
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|COSBase
argument_list|>
name|retList
init|=
operator|new
name|ArrayList
argument_list|<
name|COSBase
argument_list|>
argument_list|(
name|size
argument_list|()
argument_list|)
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
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|retList
operator|.
name|add
argument_list|(
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retList
return|;
block|}
block|}
end_class

end_unit

