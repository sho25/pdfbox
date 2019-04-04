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
name|graphics
operator|.
name|optionalcontent
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
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents the optional content properties dictionary.  *  * @since PDF 1.5  */
end_comment

begin_class
specifier|public
class|class
name|PDOptionalContentProperties
implements|implements
name|COSObjectable
block|{
comment|/**      * Enumeration for the BaseState dictionary entry on the "D" dictionary.      */
specifier|public
enum|enum
name|BaseState
block|{
comment|/** The "ON" value. */
name|ON
parameter_list|(
name|COSName
operator|.
name|ON
parameter_list|)
operator|,
comment|/** The "OFF" value. */
constructor|OFF(COSName.OFF
block|)
enum|,
comment|/** The "Unchanged" value. */
name|UNCHANGED
parameter_list|(
name|COSName
operator|.
name|UNCHANGED
parameter_list|)
constructor_decl|;
specifier|private
specifier|final
name|COSName
name|name
decl_stmt|;
specifier|private
name|BaseState
parameter_list|(
name|COSName
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Returns the PDF name for the state.          * @return the name of the state          */
specifier|public
name|COSName
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
comment|/**          * Returns the base state represented by the given {@link COSName}.          * @param state the state name          * @return the state enum value          */
specifier|public
specifier|static
name|BaseState
name|valueOf
parameter_list|(
name|COSName
name|state
parameter_list|)
block|{
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return
name|BaseState
operator|.
name|ON
return|;
block|}
return|return
name|BaseState
operator|.
name|valueOf
argument_list|(
name|state
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

begin_decl_stmt
specifier|private
specifier|final
name|COSDictionary
name|dict
decl_stmt|;
end_decl_stmt

begin_comment
comment|/**      * Creates a new optional content properties dictionary.      */
end_comment

begin_constructor
specifier|public
name|PDOptionalContentProperties
parameter_list|()
block|{
name|this
operator|.
name|dict
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|this
operator|.
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OCGS
argument_list|,
operator|new
name|COSArray
argument_list|()
argument_list|)
expr_stmt|;
name|COSDictionary
name|d
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
comment|// Name optional but required for PDF/A-3
name|d
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
literal|"Top"
argument_list|)
expr_stmt|;
name|this
operator|.
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_comment
comment|/**      * Creates a new instance based on a given {@link COSDictionary}.      * @param props the dictionary      */
end_comment

begin_constructor
specifier|public
name|PDOptionalContentProperties
parameter_list|(
name|COSDictionary
name|props
parameter_list|)
block|{
name|this
operator|.
name|dict
operator|=
name|props
expr_stmt|;
block|}
end_constructor

begin_comment
comment|/** {@inheritDoc} */
end_comment

begin_function
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|dict
return|;
block|}
end_function

begin_function
specifier|private
name|COSArray
name|getOCGs
parameter_list|()
block|{
name|COSArray
name|ocgs
init|=
name|this
operator|.
name|dict
operator|.
name|getCOSArray
argument_list|(
name|COSName
operator|.
name|OCGS
argument_list|)
decl_stmt|;
if|if
condition|(
name|ocgs
operator|==
literal|null
condition|)
block|{
name|ocgs
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|this
operator|.
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OCGS
argument_list|,
name|ocgs
argument_list|)
expr_stmt|;
comment|//OCGs is required
block|}
return|return
name|ocgs
return|;
block|}
end_function

begin_function
specifier|private
name|COSDictionary
name|getD
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|this
operator|.
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|base
return|;
block|}
name|COSDictionary
name|d
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
comment|// Name optional but required for PDF/A-3
name|d
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
literal|"Top"
argument_list|)
expr_stmt|;
comment|// D is required
name|this
operator|.
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|d
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
end_function

begin_comment
comment|/**      * Returns the first optional content group of the given name.      *      * @param name the group name      * @return the optional content group or null, if there is no such group      */
end_comment

begin_function
specifier|public
name|PDOptionalContentGroup
name|getGroup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|COSArray
name|ocgs
init|=
name|getOCGs
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|o
range|:
name|ocgs
control|)
block|{
name|COSDictionary
name|ocg
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|String
name|groupName
init|=
name|ocg
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDOptionalContentGroup
argument_list|(
name|ocg
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
end_function

begin_comment
comment|/**      * Adds an optional content group (OCG).      * @param ocg the optional content group      */
end_comment

begin_function
specifier|public
name|void
name|addGroup
parameter_list|(
name|PDOptionalContentGroup
name|ocg
parameter_list|)
block|{
name|COSArray
name|ocgs
init|=
name|getOCGs
argument_list|()
decl_stmt|;
name|ocgs
operator|.
name|add
argument_list|(
name|ocg
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
comment|//By default, add new group to the "Order" entry so it appears in the user interface
name|COSArray
name|order
init|=
operator|(
name|COSArray
operator|)
name|getD
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ORDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|order
operator|==
literal|null
condition|)
block|{
name|order
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|getD
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ORDER
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
name|order
operator|.
name|add
argument_list|(
name|ocg
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**      * Returns the collection of all optional content groups.      * @return the optional content groups      */
end_comment

begin_function
specifier|public
name|Collection
argument_list|<
name|PDOptionalContentGroup
argument_list|>
name|getOptionalContentGroups
parameter_list|()
block|{
name|Collection
argument_list|<
name|PDOptionalContentGroup
argument_list|>
name|coll
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|COSArray
name|ocgs
init|=
name|getOCGs
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|base
range|:
name|ocgs
control|)
block|{
name|coll
operator|.
name|add
argument_list|(
operator|new
name|PDOptionalContentGroup
argument_list|(
name|toDictionary
argument_list|(
name|base
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|coll
return|;
block|}
end_function

begin_comment
comment|/**      * Returns the base state for optional content groups.      * @return the base state      */
end_comment

begin_function
specifier|public
name|BaseState
name|getBaseState
parameter_list|()
block|{
name|COSDictionary
name|d
init|=
name|getD
argument_list|()
decl_stmt|;
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|d
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|BASE_STATE
argument_list|)
decl_stmt|;
return|return
name|BaseState
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**      * Sets the base state for optional content groups.      * @param state the base state      */
end_comment

begin_function
specifier|public
name|void
name|setBaseState
parameter_list|(
name|BaseState
name|state
parameter_list|)
block|{
name|COSDictionary
name|d
init|=
name|getD
argument_list|()
decl_stmt|;
name|d
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BASE_STATE
argument_list|,
name|state
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**      * Lists all optional content group names.      * @return an array of all names      */
end_comment

begin_function
specifier|public
name|String
index|[]
name|getGroupNames
parameter_list|()
block|{
name|COSArray
name|ocgs
init|=
operator|(
name|COSArray
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OCGS
argument_list|)
decl_stmt|;
name|int
name|size
init|=
name|ocgs
operator|.
name|size
argument_list|()
decl_stmt|;
name|String
index|[]
name|groups
init|=
operator|new
name|String
index|[
name|size
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
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|obj
init|=
name|ocgs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSDictionary
name|ocg
init|=
name|toDictionary
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|groups
index|[
name|i
index|]
operator|=
name|ocg
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
expr_stmt|;
block|}
return|return
name|groups
return|;
block|}
end_function

begin_comment
comment|/**      * Indicates whether a particular optional content group is found in the PDF file.      * @param groupName the group name      * @return true if the group exists, false otherwise      */
end_comment

begin_function
specifier|public
name|boolean
name|hasGroup
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|String
index|[]
name|layers
init|=
name|getGroupNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|layer
range|:
name|layers
control|)
block|{
if|if
condition|(
name|layer
operator|.
name|equals
argument_list|(
name|groupName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_comment
comment|/**      * Indicates whether<em>at least one</em> optional content group with this name is enabled.      * There may be disabled optional content groups with this name even if this function returns      * true.      *      * @param groupName the group name      * @return true if at least one group is enabled      */
end_comment

begin_function
specifier|public
name|boolean
name|isGroupEnabled
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
name|COSArray
name|ocgs
init|=
name|getOCGs
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|o
range|:
name|ocgs
control|)
block|{
name|COSDictionary
name|ocg
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|ocg
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|isGroupEnabled
argument_list|(
operator|new
name|PDOptionalContentGroup
argument_list|(
name|ocg
argument_list|)
argument_list|)
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
end_function

begin_comment
comment|/**      * Indicates whether an optional content group is enabled.      * @param group the group object      * @return true if the group is enabled      */
end_comment

begin_function
specifier|public
name|boolean
name|isGroupEnabled
parameter_list|(
name|PDOptionalContentGroup
name|group
parameter_list|)
block|{
comment|//TODO handle Optional Content Configuration Dictionaries,
comment|//i.e. OCProperties/Configs
name|PDOptionalContentProperties
operator|.
name|BaseState
name|baseState
init|=
name|getBaseState
argument_list|()
decl_stmt|;
name|boolean
name|enabled
init|=
operator|!
name|baseState
operator|.
name|equals
argument_list|(
name|BaseState
operator|.
name|OFF
argument_list|)
decl_stmt|;
comment|//TODO What to do with BaseState.Unchanged?
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
return|return
name|enabled
return|;
block|}
name|COSDictionary
name|d
init|=
name|getD
argument_list|()
decl_stmt|;
name|COSBase
name|base
init|=
name|d
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ON
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|o
range|:
operator|(
name|COSArray
operator|)
name|base
control|)
block|{
name|COSDictionary
name|dictionary
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionary
operator|==
name|group
operator|.
name|getCOSObject
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
name|base
operator|=
name|d
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OFF
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|o
range|:
operator|(
name|COSArray
operator|)
name|base
control|)
block|{
name|COSDictionary
name|dictionary
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionary
operator|==
name|group
operator|.
name|getCOSObject
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
name|enabled
return|;
block|}
end_function

begin_function
specifier|private
name|COSDictionary
name|toDictionary
parameter_list|(
name|COSBase
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|COSObject
condition|)
block|{
return|return
call|(
name|COSDictionary
call|)
argument_list|(
operator|(
name|COSObject
operator|)
name|o
argument_list|)
operator|.
name|getObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|o
return|;
block|}
block|}
end_function

begin_comment
comment|/**      * Enables or disables all optional content groups with the given name.      *      * @param groupName the group name      * @param enable true to enable, false to disable      * @return true if at least one group with this name already had an on or off setting, false      * otherwise      */
end_comment

begin_function
specifier|public
name|boolean
name|setGroupEnabled
parameter_list|(
name|String
name|groupName
parameter_list|,
name|boolean
name|enable
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
name|COSArray
name|ocgs
init|=
name|getOCGs
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|o
range|:
name|ocgs
control|)
block|{
name|COSDictionary
name|ocg
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|ocg
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|setGroupEnabled
argument_list|(
operator|new
name|PDOptionalContentGroup
argument_list|(
name|ocg
argument_list|)
argument_list|,
name|enable
argument_list|)
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
end_function

begin_comment
comment|/**      * Enables or disables an optional content group.      * @param group the group object      * @param enable true to enable, false to disable      * @return true if the group already had an on or off setting, false otherwise      */
end_comment

begin_function
specifier|public
name|boolean
name|setGroupEnabled
parameter_list|(
name|PDOptionalContentGroup
name|group
parameter_list|,
name|boolean
name|enable
parameter_list|)
block|{
name|COSArray
name|on
decl_stmt|;
name|COSArray
name|off
decl_stmt|;
name|COSDictionary
name|d
init|=
name|getD
argument_list|()
decl_stmt|;
name|COSBase
name|base
init|=
name|d
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ON
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSArray
operator|)
condition|)
block|{
name|on
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|d
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ON
argument_list|,
name|on
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|on
operator|=
operator|(
name|COSArray
operator|)
name|base
expr_stmt|;
block|}
name|base
operator|=
name|d
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OFF
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSArray
operator|)
condition|)
block|{
name|off
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|d
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OFF
argument_list|,
name|off
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|off
operator|=
operator|(
name|COSArray
operator|)
name|base
expr_stmt|;
block|}
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|enable
condition|)
block|{
for|for
control|(
name|COSBase
name|o
range|:
name|off
control|)
block|{
name|COSDictionary
name|groupDictionary
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupDictionary
operator|==
name|group
operator|.
name|getCOSObject
argument_list|()
condition|)
block|{
comment|//enable group
name|off
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|on
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|COSBase
name|o
range|:
name|on
control|)
block|{
name|COSDictionary
name|groupDictionary
init|=
name|toDictionary
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupDictionary
operator|==
name|group
operator|.
name|getCOSObject
argument_list|()
condition|)
block|{
comment|//disable group
name|on
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|off
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
if|if
condition|(
name|enable
condition|)
block|{
name|on
operator|.
name|add
argument_list|(
name|group
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|off
operator|.
name|add
argument_list|(
name|group
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|found
return|;
block|}
end_function

unit|}
end_unit

