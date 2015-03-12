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
name|COSFloat
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
name|COSNull
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
name|COSNumber
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
name|java
operator|.
name|util
operator|.
name|ListIterator
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

begin_comment
comment|/**  * This is an implementation of a List that will sync its contents to a COSArray.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|COSArrayList
parameter_list|<
name|E
parameter_list|>
implements|implements
name|List
argument_list|<
name|E
argument_list|>
block|{
specifier|private
specifier|final
name|COSArray
name|array
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|actual
decl_stmt|;
specifier|private
name|COSDictionary
name|parentDict
decl_stmt|;
specifier|private
name|COSName
name|dictKey
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|COSArrayList
parameter_list|()
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|actual
operator|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param actualList The list of standard java objects      * @param cosArray The COS array object to sync to.      */
specifier|public
name|COSArrayList
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|actualList
parameter_list|,
name|COSArray
name|cosArray
parameter_list|)
block|{
name|actual
operator|=
name|actualList
expr_stmt|;
name|array
operator|=
name|cosArray
expr_stmt|;
block|}
comment|/**      * This is a really special constructor.  Sometimes the PDF spec says      * that a dictionary entry can either be a single item or an array of those      * items.  But in the PDModel interface we really just want to always return      * a java.util.List.  In the case were we get the list and never modify it      * we don't want to convert to COSArray and put one element, unless we append      * to the list.  So here we are going to create this object with a single      * item instead of a list, but allow more items to be added and then converted      * to an array.      *      * @param actualObject The PDModel object.      * @param item The COS Model object.      * @param dictionary The dictionary that holds the item, and will hold the array if an item is added.      * @param dictionaryKey The key into the dictionary to set the item.      */
specifier|public
name|COSArrayList
parameter_list|(
name|E
name|actualObject
parameter_list|,
name|COSBase
name|item
parameter_list|,
name|COSDictionary
name|dictionary
parameter_list|,
name|COSName
name|dictionaryKey
parameter_list|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|actual
operator|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
expr_stmt|;
name|actual
operator|.
name|add
argument_list|(
name|actualObject
argument_list|)
expr_stmt|;
name|parentDict
operator|=
name|dictionary
expr_stmt|;
name|dictKey
operator|=
name|dictionaryKey
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|actual
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|actual
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|actual
operator|.
name|contains
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|actual
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|actual
operator|.
name|toArray
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
parameter_list|<
name|X
parameter_list|>
name|X
index|[]
name|toArray
parameter_list|(
name|X
index|[]
name|a
parameter_list|)
block|{
return|return
name|actual
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|o
parameter_list|)
block|{
comment|//when adding if there is a parentDict then change the item
comment|//in the dictionary from a single item to an array.
if|if
condition|(
name|parentDict
operator|!=
literal|null
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
name|array
argument_list|)
expr_stmt|;
comment|//clear the parent dict so it doesn't happen again, there might be
comment|//a usecase for keeping the parentDict around but not now.
name|parentDict
operator|=
literal|null
expr_stmt|;
block|}
comment|//string is a special case because we can't subclass to be COSObjectable
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|DualCOSObjectable
name|dual
init|=
operator|(
name|DualCOSObjectable
operator|)
name|o
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|dual
operator|.
name|getFirstCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|dual
operator|.
name|getSecondCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSObjectable
operator|)
name|o
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|actual
operator|.
name|add
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|boolean
name|retval
init|=
literal|true
decl_stmt|;
name|int
name|index
init|=
name|actual
operator|.
name|indexOf
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|actual
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|array
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|actual
operator|.
name|containsAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|c
parameter_list|)
block|{
comment|//when adding if there is a parentDict then change the item
comment|//in the dictionary from a single item to an array.
if|if
condition|(
name|parentDict
operator|!=
literal|null
operator|&&
name|c
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
name|array
argument_list|)
expr_stmt|;
comment|//clear the parent dict so it doesn't happen again, there might be
comment|//a usecase for keeping the parentDict around but not now.
name|parentDict
operator|=
literal|null
expr_stmt|;
block|}
name|array
operator|.
name|addAll
argument_list|(
name|toCOSObjectList
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|actual
operator|.
name|addAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|c
parameter_list|)
block|{
comment|//when adding if there is a parentDict then change the item
comment|//in the dictionary from a single item to an array.
if|if
condition|(
name|parentDict
operator|!=
literal|null
operator|&&
name|c
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
name|array
argument_list|)
expr_stmt|;
comment|//clear the parent dict so it doesn't happen again, there might be
comment|//a usecase for keeping the parentDict around but not now.
name|parentDict
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|c
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|array
operator|.
name|addAll
argument_list|(
name|index
operator|*
literal|2
argument_list|,
name|toCOSObjectList
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|toCOSObjectList
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|actual
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|c
argument_list|)
return|;
block|}
comment|/**      * This will take an array of COSNumbers and return a COSArrayList of      * java.lang.Integer values.      *      * @param intArray The existing integer Array.      *      * @return A list that is part of the core Java collections.      */
specifier|public
specifier|static
name|List
argument_list|<
name|Integer
argument_list|>
name|convertIntegerCOSArrayToList
parameter_list|(
name|COSArray
name|intArray
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|intArray
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
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
name|intArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSNumber
name|num
decl_stmt|;
if|if
condition|(
name|intArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|instanceof
name|COSObject
condition|)
block|{
name|num
operator|=
call|(
name|COSNumber
call|)
argument_list|(
operator|(
name|COSObject
operator|)
name|intArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|num
operator|=
operator|(
name|COSNumber
operator|)
name|intArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|numbers
operator|.
name|add
argument_list|(
name|num
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|numbers
argument_list|,
name|intArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take an array of COSNumbers and return a COSArrayList of      * java.lang.Float values.      *      * @param floatArray The existing float Array.      *      * @return The list of Float objects.      */
specifier|public
specifier|static
name|List
argument_list|<
name|Float
argument_list|>
name|convertFloatCOSArrayToList
parameter_list|(
name|COSArray
name|floatArray
parameter_list|)
block|{
name|List
argument_list|<
name|Float
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|floatArray
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Float
argument_list|>
name|numbers
init|=
operator|new
name|ArrayList
argument_list|<
name|Float
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
name|floatArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|numbers
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSNumber
operator|)
name|floatArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|Float
argument_list|>
argument_list|(
name|numbers
argument_list|,
name|floatArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take an array of COSName and return a COSArrayList of      * java.lang.String values.      *      * @param nameArray The existing name Array.      *      * @return The list of String objects.      */
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|convertCOSNameCOSArrayToList
parameter_list|(
name|COSArray
name|nameArray
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|nameArray
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nameArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|names
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|nameArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|names
argument_list|,
name|nameArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take an array of COSString and return a COSArrayList of      * java.lang.String values.      *      * @param stringArray The existing name Array.      *      * @return The list of String objects.      */
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|convertCOSStringCOSArrayToList
parameter_list|(
name|COSArray
name|stringArray
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|stringArray
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|string
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|stringArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|string
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|stringArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|string
argument_list|,
name|stringArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take an list of string objects and return a COSArray of COSName      * objects.      *      * @param strings A list of strings      *      * @return An array of COSName objects      */
specifier|public
specifier|static
name|COSArray
name|convertStringListToCOSNameCOSArray
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|)
block|{
name|COSArray
name|retval
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|string
range|:
name|strings
control|)
block|{
name|retval
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|string
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will take an list of string objects and return a COSArray of COSName      * objects.      *      * @param strings A list of strings      *      * @return An array of COSName objects      */
specifier|public
specifier|static
name|COSArray
name|convertStringListToCOSStringCOSArray
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|)
block|{
name|COSArray
name|retval
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|string
range|:
name|strings
control|)
block|{
name|retval
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|string
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will convert a list of COSObjectables to an      * array list of COSBase objects.      *      * @param cosObjectableList A list of COSObjectable.      *      * @return A list of COSBase.      */
specifier|public
specifier|static
name|COSArray
name|converterToCOSArray
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|cosObjectableList
parameter_list|)
block|{
name|COSArray
name|array
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cosObjectableList
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cosObjectableList
operator|instanceof
name|COSArrayList
condition|)
block|{
comment|//if it is already a COSArrayList then we don't want to recreate the array, we want to reuse it.
name|array
operator|=
operator|(
operator|(
name|COSArrayList
argument_list|<
name|?
argument_list|>
operator|)
name|cosObjectableList
operator|)
operator|.
name|array
expr_stmt|;
block|}
else|else
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|cosObjectableList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|next
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|String
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
operator|(
name|String
operator|)
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|Integer
operator|||
name|next
operator|instanceof
name|Long
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|next
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|Float
operator|||
name|next
operator|instanceof
name|Double
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|next
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|COSObjectable
condition|)
block|{
name|COSObjectable
name|object
init|=
operator|(
name|COSObjectable
operator|)
name|next
decl_stmt|;
name|array
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
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|DualCOSObjectable
name|object
init|=
operator|(
name|DualCOSObjectable
operator|)
name|next
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
operator|.
name|getFirstCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
operator|.
name|getSecondCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|==
literal|null
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error: Don't know how to convert type to COSBase '"
operator|+
name|next
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
return|return
name|array
return|;
block|}
specifier|private
name|List
argument_list|<
name|COSBase
argument_list|>
name|toCOSObjectList
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
name|List
argument_list|<
name|COSBase
argument_list|>
name|cosObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|COSBase
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|next
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|String
condition|)
block|{
name|cosObjects
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
operator|(
name|String
operator|)
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|DualCOSObjectable
name|object
init|=
operator|(
name|DualCOSObjectable
operator|)
name|next
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
operator|.
name|getFirstCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
operator|.
name|getSecondCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSObjectable
name|cos
init|=
operator|(
name|COSObjectable
operator|)
name|next
decl_stmt|;
name|cosObjects
operator|.
name|add
argument_list|(
name|cos
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cosObjects
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|array
operator|.
name|removeAll
argument_list|(
name|toCOSObjectList
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|actual
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|array
operator|.
name|retainAll
argument_list|(
name|toCOSObjectList
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|actual
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|//when adding if there is a parentDict then change the item
comment|//in the dictionary from a single item to an array.
if|if
condition|(
name|parentDict
operator|!=
literal|null
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
operator|(
name|COSBase
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
name|actual
operator|.
name|clear
argument_list|()
expr_stmt|;
name|array
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|actual
operator|.
name|equals
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|actual
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|actual
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|E
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
if|if
condition|(
name|element
operator|instanceof
name|String
condition|)
block|{
name|COSString
name|item
init|=
operator|new
name|COSString
argument_list|(
operator|(
name|String
operator|)
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentDict
operator|!=
literal|null
operator|&&
name|index
operator|==
literal|0
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
name|item
argument_list|)
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|item
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|element
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|DualCOSObjectable
name|dual
init|=
operator|(
name|DualCOSObjectable
operator|)
name|element
decl_stmt|;
name|array
operator|.
name|set
argument_list|(
name|index
operator|*
literal|2
argument_list|,
name|dual
operator|.
name|getFirstCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|array
operator|.
name|set
argument_list|(
name|index
operator|*
literal|2
operator|+
literal|1
argument_list|,
name|dual
operator|.
name|getSecondCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|parentDict
operator|!=
literal|null
operator|&&
name|index
operator|==
literal|0
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
operator|(
operator|(
name|COSObjectable
operator|)
name|element
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|(
operator|(
name|COSObjectable
operator|)
name|element
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|actual
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
comment|//when adding if there is a parentDict then change the item
comment|//in the dictionary from a single item to an array.
if|if
condition|(
name|parentDict
operator|!=
literal|null
condition|)
block|{
name|parentDict
operator|.
name|setItem
argument_list|(
name|dictKey
argument_list|,
name|array
argument_list|)
expr_stmt|;
comment|//clear the parent dict so it doesn't happen again, there might be
comment|//a usecase for keeping the parentDict around but not now.
name|parentDict
operator|=
literal|null
expr_stmt|;
block|}
name|actual
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
name|element
operator|instanceof
name|String
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|index
argument_list|,
operator|new
name|COSString
argument_list|(
operator|(
name|String
operator|)
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|element
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
name|DualCOSObjectable
name|dual
init|=
operator|(
name|DualCOSObjectable
operator|)
name|element
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|index
operator|*
literal|2
argument_list|,
name|dual
operator|.
name|getFirstCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|index
operator|*
literal|2
operator|+
literal|1
argument_list|,
name|dual
operator|.
name|getSecondCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|add
argument_list|(
name|index
argument_list|,
operator|(
operator|(
name|COSObjectable
operator|)
name|element
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|E
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|>
name|index
operator|&&
name|array
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|instanceof
name|DualCOSObjectable
condition|)
block|{
comment|//remove both objects
name|array
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|array
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
return|return
name|actual
operator|.
name|remove
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|actual
operator|.
name|indexOf
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|actual
operator|.
name|indexOf
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|actual
operator|.
name|listIterator
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|actual
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|E
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
name|actual
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
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
literal|"COSArrayList{"
operator|+
name|array
operator|.
name|toString
argument_list|()
operator|+
literal|"}"
return|;
block|}
comment|/**      * This will return then underlying COSArray.      *       * @return the COSArray      */
specifier|public
name|COSArray
name|toList
parameter_list|()
block|{
return|return
name|array
return|;
block|}
block|}
end_class

end_unit

