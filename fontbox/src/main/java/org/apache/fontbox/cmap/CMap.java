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
name|fontbox
operator|.
name|cmap
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * This class represents a CMap file.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|CMap
block|{
specifier|private
name|int
name|wmode
init|=
literal|0
decl_stmt|;
specifier|private
name|String
name|cmapName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|cmapVersion
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|cmapType
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|registry
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|ordering
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|supplement
init|=
literal|0
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CodespaceRange
argument_list|>
name|codeSpaceRanges
init|=
operator|new
name|ArrayList
argument_list|<
name|CodespaceRange
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|singleByteMappings
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|doubleByteMappings
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|cid2charMappings
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|char2CIDMappings
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|CIDRange
argument_list|>
name|cidRanges
init|=
operator|new
name|LinkedList
argument_list|<
name|CIDRange
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new instance of CMap.      */
specifier|public
name|CMap
parameter_list|()
block|{
comment|//default constructor
block|}
comment|/**      * This will tell if this cmap has any one byte mappings.      *       * @return true If there are any one byte mappings, false otherwise.      */
specifier|public
name|boolean
name|hasOneByteMappings
parameter_list|()
block|{
return|return
name|singleByteMappings
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      * This will tell if this cmap has any two byte mappings.      *       * @return true If there are any two byte mappings, false otherwise.      */
specifier|public
name|boolean
name|hasTwoByteMappings
parameter_list|()
block|{
return|return
name|doubleByteMappings
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/**      * This will tell if this cmap has any CID mappings.      *       * @return true If there are any CID mappings, false otherwise.      */
specifier|public
name|boolean
name|hasCIDMappings
parameter_list|()
block|{
return|return
operator|!
name|char2CIDMappings
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|cidRanges
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * This will perform a lookup into the map.      *      * @param code The code used to lookup.      * @param offset The offset into the byte array.      * @param length The length of the data we are getting.      *      * @return The string that matches the lookup.      */
specifier|public
name|String
name|lookup
parameter_list|(
name|byte
index|[]
name|code
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|lookup
argument_list|(
name|getCodeFromArray
argument_list|(
name|code
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/**      * This will perform a lookup into the map.      *      * @param code The code used to lookup.      * @param length The length of the data we are getting.      *      * @return The string that matches the lookup.      */
specifier|public
name|String
name|lookup
parameter_list|(
name|int
name|code
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|1
condition|)
block|{
name|result
operator|=
name|singleByteMappings
operator|.
name|get
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|length
operator|==
literal|2
condition|)
block|{
name|result
operator|=
name|doubleByteMappings
operator|.
name|get
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|lookupCID
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * This will perform a lookup into the CID map.      *      * @param cid The CID used to lookup.      *      * @return The string that matches the lookup.      */
specifier|public
name|String
name|lookupCID
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
if|if
condition|(
name|cid2charMappings
operator|.
name|containsKey
argument_list|(
name|cid
argument_list|)
condition|)
block|{
return|return
name|cid2charMappings
operator|.
name|get
argument_list|(
name|cid
argument_list|)
return|;
block|}
else|else
block|{
for|for
control|(
name|CIDRange
name|range
range|:
name|cidRanges
control|)
block|{
name|int
name|ch
init|=
name|range
operator|.
name|unmap
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|Character
operator|.
name|toString
argument_list|(
operator|(
name|char
operator|)
name|ch
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will perform a lookup into the CID map.      *      * @param code The code used to lookup.      *      * @return The CID that matches the lookup.      */
specifier|public
name|int
name|lookupCID
parameter_list|(
name|byte
index|[]
name|code
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|isInCodeSpaceRanges
argument_list|(
name|code
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
condition|)
block|{
name|int
name|codeAsInt
init|=
name|getCodeFromArray
argument_list|(
name|code
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|char2CIDMappings
operator|.
name|containsKey
argument_list|(
name|codeAsInt
argument_list|)
condition|)
block|{
return|return
name|char2CIDMappings
operator|.
name|get
argument_list|(
name|codeAsInt
argument_list|)
return|;
block|}
else|else
block|{
for|for
control|(
name|CIDRange
name|range
range|:
name|cidRanges
control|)
block|{
name|int
name|ch
init|=
name|range
operator|.
name|map
argument_list|(
operator|(
name|char
operator|)
name|codeAsInt
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|ch
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**      * Convert the given part of a byte array to an integer.      * @param data the byte array      * @param offset The offset into the byte array.      * @param length The length of the data we are getting.      * @return the resulting integer      */
specifier|private
name|int
name|getCodeFromArray
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|int
name|code
init|=
literal|0
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|code
operator|<<=
literal|8
expr_stmt|;
name|code
operator||=
operator|(
name|data
index|[
name|offset
operator|+
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
expr_stmt|;
block|}
return|return
name|code
return|;
block|}
comment|/**      * This will add a mapping.      *      * @param src The src to the mapping.      * @param dest The dest to the mapping.      *      * @throws IOException if the src is invalid.      */
specifier|public
name|void
name|addMapping
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|String
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|srcLength
init|=
name|src
operator|.
name|length
decl_stmt|;
name|int
name|intSrc
init|=
name|getCodeFromArray
argument_list|(
name|src
argument_list|,
literal|0
argument_list|,
name|srcLength
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcLength
operator|==
literal|1
condition|)
block|{
name|singleByteMappings
operator|.
name|put
argument_list|(
name|intSrc
argument_list|,
name|dest
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|srcLength
operator|==
literal|2
condition|)
block|{
name|doubleByteMappings
operator|.
name|put
argument_list|(
name|intSrc
argument_list|,
name|dest
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Mapping code should be 1 or two bytes and not "
operator|+
name|src
operator|.
name|length
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will add a CID mapping.      *      * @param src The CID to the mapping.      * @param dest The dest to the mapping.      *      * @throws IOException if the src is invalid.      */
specifier|public
name|void
name|addCIDMapping
parameter_list|(
name|int
name|src
parameter_list|,
name|String
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
name|cid2charMappings
operator|.
name|put
argument_list|(
name|src
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|char2CIDMappings
operator|.
name|put
argument_list|(
name|dest
argument_list|,
name|src
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add a CID Range.      *      * @param src The CID Range to be added.      * @param dest The starting cid.      *      */
specifier|public
name|void
name|addCIDRange
parameter_list|(
name|char
name|from
parameter_list|,
name|char
name|to
parameter_list|,
name|int
name|cid
parameter_list|)
block|{
name|cidRanges
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|CIDRange
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add a codespace range.      *      * @param range A single codespace range.      */
specifier|public
name|void
name|addCodespaceRange
parameter_list|(
name|CodespaceRange
name|range
parameter_list|)
block|{
name|codeSpaceRanges
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
comment|/**      * Getter for property codeSpaceRanges.      *      * @return Value of property codeSpaceRanges.      */
specifier|public
name|List
argument_list|<
name|CodespaceRange
argument_list|>
name|getCodeSpaceRanges
parameter_list|()
block|{
return|return
name|codeSpaceRanges
return|;
block|}
comment|/**      * Implementation of the usecmap operator.  This will      * copy all of the mappings from one cmap to another.      *       * @param cmap The cmap to load mappings from.      */
specifier|public
name|void
name|useCmap
parameter_list|(
name|CMap
name|cmap
parameter_list|)
block|{
name|this
operator|.
name|codeSpaceRanges
operator|.
name|addAll
argument_list|(
name|cmap
operator|.
name|codeSpaceRanges
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleByteMappings
operator|.
name|putAll
argument_list|(
name|cmap
operator|.
name|singleByteMappings
argument_list|)
expr_stmt|;
name|this
operator|.
name|doubleByteMappings
operator|.
name|putAll
argument_list|(
name|cmap
operator|.
name|doubleByteMappings
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Check whether the given byte array is in codespace ranges or not.      *        *  @param code The byte array to look for in the codespace range.      *        *  @return true if the given byte array is in the codespace range.      */
specifier|public
name|boolean
name|isInCodeSpaceRanges
parameter_list|(
name|byte
index|[]
name|code
parameter_list|)
block|{
return|return
name|isInCodeSpaceRanges
argument_list|(
name|code
argument_list|,
literal|0
argument_list|,
name|code
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**      *  Check whether the given byte array is in codespace ranges or not.      *        *  @param code The byte array to look for in the codespace range.      *  @param offset The starting offset within the byte array.      *  @param length The length of the part of the array.      *        *  @return true if the given byte array is in the codespace range.      */
specifier|public
name|boolean
name|isInCodeSpaceRanges
parameter_list|(
name|byte
index|[]
name|code
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CodespaceRange
argument_list|>
name|it
init|=
name|codeSpaceRanges
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CodespaceRange
name|range
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|range
operator|!=
literal|null
operator|&&
name|range
operator|.
name|isInRange
argument_list|(
name|code
argument_list|,
name|offset
argument_list|,
name|length
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
comment|/**      * Returns the WMode of a CMap.      *      * 0 represents a horizontal and 1 represents a vertical orientation.      *       * @return      */
specifier|public
name|int
name|getWMode
parameter_list|()
block|{
return|return
name|wmode
return|;
block|}
comment|/**      * Sets the WMode of a CMap.      *       * @param newWMode the new WMode.      */
specifier|public
name|void
name|setWMode
parameter_list|(
name|int
name|newWMode
parameter_list|)
block|{
name|wmode
operator|=
name|newWMode
expr_stmt|;
block|}
comment|/**      * Returns the name of the CMap.      *       * @return the CMap name.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|cmapName
return|;
block|}
comment|/**      * Sets the name of the CMap.      *       * @param name the CMap name.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|cmapName
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Returns the version of the CMap.      *       * @return the CMap version.      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|cmapVersion
return|;
block|}
comment|/**      * Sets the version of the CMap.      *       * @param version the CMap version.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|cmapVersion
operator|=
name|version
expr_stmt|;
block|}
comment|/**      * Returns the type of the CMap.      *       * @return the CMap type.      */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|cmapType
return|;
block|}
comment|/**      * Sets the type of the CMap.      *       * @param type the CMap type.      */
specifier|public
name|void
name|setType
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|cmapType
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns the registry of the CIDSystemInfo.      *       * @return the registry.      */
specifier|public
name|String
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
comment|/**      * Sets the registry of the CIDSystemInfo.      *       * @param newRegistry the registry.      */
specifier|public
name|void
name|setRegistry
parameter_list|(
name|String
name|newRegistry
parameter_list|)
block|{
name|registry
operator|=
name|newRegistry
expr_stmt|;
block|}
comment|/**      * Returns the ordering of the CIDSystemInfo.      *       * @return the ordering.      */
specifier|public
name|String
name|getOrdering
parameter_list|()
block|{
return|return
name|ordering
return|;
block|}
comment|/**      * Sets the ordering of the CIDSystemInfo.      *       * @param newOrdering the ordering.      */
specifier|public
name|void
name|setOrdering
parameter_list|(
name|String
name|newOrdering
parameter_list|)
block|{
name|ordering
operator|=
name|newOrdering
expr_stmt|;
block|}
comment|/**      * Returns the supplement of the CIDSystemInfo.      *       * @return the supplement.      */
specifier|public
name|int
name|getSupplement
parameter_list|()
block|{
return|return
name|supplement
return|;
block|}
comment|/**      * Sets the supplement of the CIDSystemInfo.      *       * @param newSupplement the supplement.      */
specifier|public
name|void
name|setSupplement
parameter_list|(
name|int
name|newSupplement
parameter_list|)
block|{
name|supplement
operator|=
name|newSupplement
expr_stmt|;
block|}
block|}
end_class

end_unit

