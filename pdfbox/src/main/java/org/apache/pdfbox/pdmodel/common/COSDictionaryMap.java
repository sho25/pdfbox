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
name|Collection
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
name|COSBoolean
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * This is a Map that will automatically sync the contents to a COSDictionary.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|COSDictionaryMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
specifier|private
specifier|final
name|COSDictionary
name|map
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|actuals
decl_stmt|;
comment|/**      * Constructor for this map.      *      * @param actualsMap The map with standard java objects as values.      * @param dicMap The map with COSBase objects as values.      */
specifier|public
name|COSDictionaryMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|actualsMap
parameter_list|,
name|COSDictionary
name|dicMap
parameter_list|)
block|{
name|actuals
operator|=
name|actualsMap
expr_stmt|;
name|map
operator|=
name|dicMap
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
name|map
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
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|actuals
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|actuals
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|actuals
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|COSObjectable
name|object
init|=
operator|(
name|COSObjectable
operator|)
name|value
decl_stmt|;
name|map
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
argument_list|,
name|object
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|actuals
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|map
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|actuals
operator|.
name|remove
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|actuals
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|actuals
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|actuals
operator|.
name|values
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|actuals
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
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
name|boolean
name|retval
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|COSDictionaryMap
condition|)
block|{
name|COSDictionaryMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|other
init|=
operator|(
name|COSDictionaryMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|o
decl_stmt|;
name|retval
operator|=
name|other
operator|.
name|map
operator|.
name|equals
argument_list|(
name|this
operator|.
name|map
argument_list|)
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
name|String
name|toString
parameter_list|()
block|{
return|return
name|actuals
operator|.
name|toString
argument_list|()
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
name|map
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * This will take a map&lt;java.lang.String,org.apache.pdfbox.pdmodel.COSObjectable&gt;      * and convert it into a COSDictionary.      *      * @param someMap A map containing COSObjectables      *      * @return A proper COSDictionary      */
specifier|public
specifier|static
name|COSDictionary
name|convert
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|someMap
parameter_list|)
block|{
name|COSDictionary
name|dic
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|someMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|COSObjectable
name|object
init|=
operator|(
name|COSObjectable
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|,
name|object
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|dic
return|;
block|}
comment|/**      * This will take a COS dictionary and convert it into COSDictionaryMap.  All cos      * objects will be converted to their primitive form.      *      * @param map The COS mappings.      * @return A standard java map.      * @throws IOException If there is an error during the conversion.      */
specifier|public
specifier|static
name|COSDictionaryMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|convertBasicTypesToMap
parameter_list|(
name|COSDictionary
name|map
parameter_list|)
throws|throws
name|IOException
block|{
name|COSDictionaryMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|actualMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|cosObj
init|=
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|actualObject
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cosObj
operator|instanceof
name|COSString
condition|)
block|{
name|actualObject
operator|=
operator|(
operator|(
name|COSString
operator|)
name|cosObj
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObj
operator|instanceof
name|COSInteger
condition|)
block|{
name|actualObject
operator|=
operator|(
operator|(
name|COSInteger
operator|)
name|cosObj
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObj
operator|instanceof
name|COSName
condition|)
block|{
name|actualObject
operator|=
operator|(
operator|(
name|COSName
operator|)
name|cosObj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObj
operator|instanceof
name|COSFloat
condition|)
block|{
name|actualObject
operator|=
operator|(
operator|(
name|COSFloat
operator|)
name|cosObj
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosObj
operator|instanceof
name|COSBoolean
condition|)
block|{
name|actualObject
operator|=
operator|(
operator|(
name|COSBoolean
operator|)
name|cosObj
operator|)
operator|.
name|getValue
argument_list|()
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:unknown type of object to convert:"
operator|+
name|cosObj
argument_list|)
throw|;
block|}
name|actualMap
operator|.
name|put
argument_list|(
name|key
operator|.
name|getName
argument_list|()
argument_list|,
name|actualObject
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|<>
argument_list|(
name|actualMap
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

