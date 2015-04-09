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
name|io
operator|.
name|InputStream
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

begin_comment
comment|/**  * This class represents a CMap file.  *  * @author Ben Litchfield  */
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
comment|// code lengths
specifier|private
specifier|final
name|List
argument_list|<
name|CodespaceRange
argument_list|>
name|codespaceRanges
init|=
operator|new
name|ArrayList
argument_list|<
name|CodespaceRange
argument_list|>
argument_list|()
decl_stmt|;
comment|// Unicode mappings
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|charToUnicode
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
comment|// CID mappings
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|codeToCid
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
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
name|codeToCidRanges
init|=
operator|new
name|LinkedList
argument_list|<
name|CIDRange
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SPACE
init|=
literal|" "
decl_stmt|;
specifier|private
name|int
name|spaceMapping
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Creates a new instance of CMap.      */
name|CMap
parameter_list|()
block|{     }
comment|/**      * This will tell if this cmap has any CID mappings.      *       * @return true If there are any CID mappings, false otherwise.      */
specifier|public
name|boolean
name|hasCIDMappings
parameter_list|()
block|{
return|return
operator|!
name|codeToCid
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|codeToCidRanges
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * This will tell if this cmap has any Unicode mappings.      *      * @return true If there are any Unicode mappings, false otherwise.      */
specifier|public
name|boolean
name|hasUnicodeMappings
parameter_list|()
block|{
return|return
operator|!
name|charToUnicode
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Returns the sequence of Unicode characters for the given character code.      *      * @param code character code      * @return Unicode characters (may be more than one, e.g "fi" ligature)      */
specifier|public
name|String
name|toUnicode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|charToUnicode
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * Reads a character code from a string in the content stream.      *<p>>See "CMap Mapping" and "Handling Undefined Characters" in PDF32000 for more details.      *      * @param in string stream      * @return character code      * @throws IOException if there was an error reading the stream or CMap      */
specifier|public
name|int
name|readCode
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
comment|// save the position in the string
name|in
operator|.
name|mark
argument_list|(
literal|4
argument_list|)
expr_stmt|;
comment|// mapping algorithm
name|List
argument_list|<
name|Byte
argument_list|>
name|bytes
init|=
operator|new
name|ArrayList
argument_list|<
name|Byte
argument_list|>
argument_list|(
literal|4
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
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|bytes
operator|.
name|add
argument_list|(
operator|(
name|byte
operator|)
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|CodespaceRange
name|range
range|:
name|codespaceRanges
control|)
block|{
if|if
condition|(
name|range
operator|.
name|isFullMatch
argument_list|(
name|bytes
argument_list|)
condition|)
block|{
return|return
name|toInt
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
block|}
comment|// reset to the original position in the string
name|in
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// modified mapping algorithm
name|bytes
operator|=
operator|new
name|ArrayList
argument_list|<
name|Byte
argument_list|>
argument_list|(
literal|4
argument_list|)
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
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|bytes
operator|.
name|add
argument_list|(
operator|(
name|byte
operator|)
name|in
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CodespaceRange
name|match
init|=
literal|null
decl_stmt|;
name|CodespaceRange
name|shortest
init|=
literal|null
decl_stmt|;
for|for
control|(
name|CodespaceRange
name|range
range|:
name|codespaceRanges
control|)
block|{
if|if
condition|(
name|range
operator|.
name|isPartialMatch
argument_list|(
name|bytes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
argument_list|)
condition|)
block|{
if|if
condition|(
name|match
operator|==
literal|null
condition|)
block|{
name|match
operator|=
name|range
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|range
operator|.
name|getStart
argument_list|()
operator|.
name|length
operator|<
name|match
operator|.
name|getStart
argument_list|()
operator|.
name|length
condition|)
block|{
comment|// for multiple matches, choose the codespace with the shortest codes
name|match
operator|=
name|range
expr_stmt|;
block|}
block|}
comment|// find shortest range
if|if
condition|(
name|shortest
operator|==
literal|null
operator|||
name|range
operator|.
name|getStart
argument_list|()
operator|.
name|length
operator|<
name|shortest
operator|.
name|getStart
argument_list|()
operator|.
name|length
condition|)
block|{
name|shortest
operator|=
name|range
expr_stmt|;
block|}
block|}
comment|// if there are no matches, the range with the shortest codes is chosen
if|if
condition|(
name|match
operator|==
literal|null
condition|)
block|{
name|match
operator|=
name|shortest
expr_stmt|;
block|}
comment|// we're done when we have enough bytes for the matched range
if|if
condition|(
name|match
operator|!=
literal|null
operator|&&
name|match
operator|.
name|getStart
argument_list|()
operator|.
name|length
operator|==
name|bytes
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|toInt
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"CMap is invalid"
argument_list|)
throw|;
block|}
comment|/**      * Returns an int given a List<Byte>      */
specifier|private
name|int
name|toInt
parameter_list|(
name|List
argument_list|<
name|Byte
argument_list|>
name|data
parameter_list|)
block|{
name|int
name|code
init|=
literal|0
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|data
control|)
block|{
name|code
operator|<<=
literal|8
expr_stmt|;
name|code
operator||=
operator|(
name|b
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
comment|/**      * Returns the CID for the given character code.      *      * @param code character code      * @return CID      */
specifier|public
name|int
name|toCID
parameter_list|(
name|int
name|code
parameter_list|)
block|{
if|if
condition|(
name|codeToCid
operator|.
name|containsKey
argument_list|(
name|code
argument_list|)
condition|)
block|{
return|return
name|codeToCid
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
for|for
control|(
name|CIDRange
name|range
range|:
name|codeToCidRanges
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
name|code
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
literal|0
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
comment|/**      * This will add a character code to Unicode character sequence mapping.      *      * @param codes The character codes to map from.      * @param unicode The Unicode characters to map to.      */
name|void
name|addCharMapping
parameter_list|(
name|byte
index|[]
name|codes
parameter_list|,
name|String
name|unicode
parameter_list|)
block|{
name|int
name|code
init|=
name|getCodeFromArray
argument_list|(
name|codes
argument_list|,
literal|0
argument_list|,
name|codes
operator|.
name|length
argument_list|)
decl_stmt|;
name|charToUnicode
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|unicode
argument_list|)
expr_stmt|;
comment|// fixme: ugly little hack
if|if
condition|(
name|SPACE
operator|.
name|equals
argument_list|(
name|unicode
argument_list|)
condition|)
block|{
name|spaceMapping
operator|=
name|code
expr_stmt|;
block|}
block|}
comment|/**      * This will add a CID mapping.      *      * @param code character code      * @param cid CID      */
name|void
name|addCIDMapping
parameter_list|(
name|int
name|code
parameter_list|,
name|int
name|cid
parameter_list|)
block|{
name|codeToCid
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will add a CID Range.      *      * @param from starting charactor of the CID range.      * @param to ending character of the CID range.      * @param cid the cid to be started with.      *      */
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
name|codeToCidRanges
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
name|void
name|addCodespaceRange
parameter_list|(
name|CodespaceRange
name|range
parameter_list|)
block|{
name|codespaceRanges
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
comment|/**      * Implementation of the usecmap operator.  This will      * copy all of the mappings from one cmap to another.      *       * @param cmap The cmap to load mappings from.      */
name|void
name|useCmap
parameter_list|(
name|CMap
name|cmap
parameter_list|)
block|{
name|this
operator|.
name|codespaceRanges
operator|.
name|addAll
argument_list|(
name|cmap
operator|.
name|codespaceRanges
argument_list|)
expr_stmt|;
name|this
operator|.
name|charToUnicode
operator|.
name|putAll
argument_list|(
name|cmap
operator|.
name|charToUnicode
argument_list|)
expr_stmt|;
name|this
operator|.
name|codeToCid
operator|.
name|putAll
argument_list|(
name|cmap
operator|.
name|codeToCid
argument_list|)
expr_stmt|;
name|this
operator|.
name|codeToCidRanges
operator|.
name|addAll
argument_list|(
name|cmap
operator|.
name|codeToCidRanges
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the WMode of a CMap.      *      * 0 represents a horizontal and 1 represents a vertical orientation.      *       * @return the wmode      */
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
comment|/**       * Returns the mapping for the space character.      *       * @return the mapped code for the space character      */
specifier|public
name|int
name|getSpaceMapping
parameter_list|()
block|{
return|return
name|spaceMapping
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|cmapName
return|;
block|}
block|}
end_class

end_unit

