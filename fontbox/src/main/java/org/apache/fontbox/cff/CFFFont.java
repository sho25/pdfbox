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
name|cff
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
name|Collection
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
name|HashSet
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
name|fontbox
operator|.
name|cff
operator|.
name|charset
operator|.
name|CFFCharset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|encoding
operator|.
name|CFFEncoding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
operator|.
name|Type1CharStringReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
operator|.
name|Type1Mapping
import|;
end_import

begin_comment
comment|/**  * This class represents a CFF/Type2 Font.  *   * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|CFFFont
implements|implements
name|Type1CharStringReader
block|{
specifier|private
name|String
name|fontname
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|topDict
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|privateDict
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|CFFEncoding
name|fontEncoding
init|=
literal|null
decl_stmt|;
specifier|private
name|CFFCharset
name|fontCharset
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|charStringsDict
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|IndexData
name|globalSubrIndex
init|=
literal|null
decl_stmt|;
specifier|private
name|IndexData
name|localSubrIndex
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Type2CharString
argument_list|>
name|charStringCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Type2CharString
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The name of the font.      *       * @return the name of the font      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|fontname
return|;
block|}
comment|/**      * Sets the name of the font.      *       * @param name the name of the font      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|fontname
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Returns the value for the given name from the dictionary.      *       * @param name the name of the value      * @return the value of the name if available      */
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|topDictValue
init|=
name|topDict
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|topDictValue
operator|!=
literal|null
condition|)
block|{
return|return
name|topDictValue
return|;
block|}
name|Object
name|privateDictValue
init|=
name|privateDict
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|privateDictValue
operator|!=
literal|null
condition|)
block|{
return|return
name|privateDictValue
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Adds the given key/value pair to the top dictionary.      *       * @param name the given key      * @param value the given value      */
specifier|public
name|void
name|addValueToTopDict
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|topDict
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the top dictionary.      *       * @return the dictionary      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getTopDict
parameter_list|()
block|{
return|return
name|topDict
return|;
block|}
comment|/**      * Adds the given key/value pair to the private dictionary.      *       * @param name the given key      * @param value the given value      */
specifier|public
name|void
name|addValueToPrivateDict
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|privateDict
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the private dictionary.      *       * @return the dictionary      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getPrivateDict
parameter_list|()
block|{
return|return
name|privateDict
return|;
block|}
comment|/**      * Get the mappings for the font as Type1Mapping      *      * @return the Type1Mapping      */
specifier|public
name|Collection
argument_list|<
name|?
extends|extends
name|Type1Mapping
argument_list|>
name|getType1Mappings
parameter_list|()
block|{
return|return
name|getMappings
argument_list|()
return|;
block|}
comment|/**      * Get the mapping (code/SID/charname/bytes) for this font.      *       * @return mappings for codes&lt; 256 and for codes&gt;= 256      */
specifier|public
name|Collection
argument_list|<
name|CFFFont
operator|.
name|Mapping
argument_list|>
name|getMappings
parameter_list|()
block|{
name|List
argument_list|<
name|Mapping
argument_list|>
name|mappings
init|=
operator|new
name|ArrayList
argument_list|<
name|Mapping
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|mappedNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CFFEncoding
operator|.
name|Entry
name|entry
range|:
name|fontEncoding
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|String
name|charName
init|=
name|fontCharset
operator|.
name|getName
argument_list|(
name|entry
operator|.
name|getSID
argument_list|()
argument_list|)
decl_stmt|;
comment|// Predefined encoding
if|if
condition|(
name|charName
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|byte
index|[]
name|bytes
init|=
name|charStringsDict
operator|.
name|get
argument_list|(
name|charName
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytes
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Mapping
name|mapping
init|=
name|createMapping
argument_list|(
name|entry
operator|.
name|getCode
argument_list|()
argument_list|,
name|entry
operator|.
name|getSID
argument_list|()
argument_list|,
name|charName
argument_list|,
name|bytes
argument_list|)
decl_stmt|;
name|mappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|mappedNames
operator|.
name|add
argument_list|(
name|charName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fontEncoding
operator|instanceof
name|CFFParser
operator|.
name|EmbeddedEncoding
condition|)
block|{
name|CFFParser
operator|.
name|EmbeddedEncoding
name|embeddedEncoding
init|=
operator|(
name|CFFParser
operator|.
name|EmbeddedEncoding
operator|)
name|fontEncoding
decl_stmt|;
for|for
control|(
name|CFFParser
operator|.
name|EmbeddedEncoding
operator|.
name|Supplement
name|supplement
range|:
name|embeddedEncoding
operator|.
name|getSupplements
argument_list|()
control|)
block|{
name|String
name|charName
init|=
name|fontCharset
operator|.
name|getName
argument_list|(
name|supplement
operator|.
name|getGlyph
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|charName
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|byte
index|[]
name|bytes
init|=
name|charStringsDict
operator|.
name|get
argument_list|(
name|charName
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytes
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Mapping
name|mapping
init|=
name|createMapping
argument_list|(
name|supplement
operator|.
name|getCode
argument_list|()
argument_list|,
name|supplement
operator|.
name|getGlyph
argument_list|()
argument_list|,
name|charName
argument_list|,
name|bytes
argument_list|)
decl_stmt|;
name|mappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|mappedNames
operator|.
name|add
argument_list|(
name|charName
argument_list|)
expr_stmt|;
block|}
block|}
comment|// XXX
name|int
name|code
init|=
literal|256
decl_stmt|;
for|for
control|(
name|CFFCharset
operator|.
name|Entry
name|entry
range|:
name|fontCharset
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|mappedNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|byte
index|[]
name|bytes
init|=
name|this
operator|.
name|charStringsDict
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytes
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Mapping
name|mapping
init|=
name|createMapping
argument_list|(
name|code
operator|++
argument_list|,
name|entry
operator|.
name|getSID
argument_list|()
argument_list|,
name|name
argument_list|,
name|bytes
argument_list|)
decl_stmt|;
name|mappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|mappedNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|mappings
return|;
block|}
comment|/**      * Returns a new mapping. Overridden in subclasses.      *      * @param code chatacter code      * @param sid SID      * @param name glyph name      * @param bytes charstring bytes      * @return a new mapping object      */
specifier|protected
name|Mapping
name|createMapping
parameter_list|(
name|int
name|code
parameter_list|,
name|int
name|sid
parameter_list|,
name|String
name|name
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|Mapping
name|mapping
init|=
operator|new
name|Mapping
argument_list|()
decl_stmt|;
name|mapping
operator|.
name|setCode
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setSID
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|mapping
return|;
block|}
comment|/**      * Return the Width value of the given Glyph identifier.      *       * @param sid SID      * @return -1 if the SID is missing from the Font.      * @throws IOException if something went wrong      *       */
specifier|public
name|int
name|getWidth
parameter_list|(
name|int
name|sid
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Mapping
name|m
range|:
name|getMappings
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getSID
argument_list|()
operator|==
name|sid
condition|)
block|{
name|Type1CharString
name|charstring
init|=
name|m
operator|.
name|getType1CharString
argument_list|()
decl_stmt|;
return|return
name|charstring
operator|.
name|getWidth
argument_list|()
return|;
block|}
block|}
comment|// SID not found, return the nodef width
name|int
name|nominalWidth
init|=
name|getNominalWidthX
argument_list|(
name|sid
argument_list|)
decl_stmt|;
name|int
name|defaultWidth
init|=
name|getDefaultWidthX
argument_list|(
name|sid
argument_list|)
decl_stmt|;
return|return
name|getNotDefWidth
argument_list|(
name|defaultWidth
argument_list|,
name|nominalWidth
argument_list|)
return|;
block|}
comment|/**      * Returns the witdth of the .notdef character.      *       * @param defaultWidth default width      * @param nominalWidth nominal width      * @return the calculated width for the .notdef character      * @throws IOException if something went wrong      */
specifier|protected
name|int
name|getNotDefWidth
parameter_list|(
name|int
name|defaultWidth
parameter_list|,
name|int
name|nominalWidth
parameter_list|)
throws|throws
name|IOException
block|{
name|Type1CharString
name|charstring
init|=
name|getType1CharString
argument_list|(
literal|".notdef"
argument_list|)
decl_stmt|;
return|return
name|charstring
operator|.
name|getWidth
argument_list|()
operator|!=
literal|0
condition|?
name|charstring
operator|.
name|getWidth
argument_list|()
operator|+
name|nominalWidth
else|:
name|defaultWidth
return|;
block|}
comment|/**      * Returns the CFFEncoding of the font.      *       * @return the encoding      */
specifier|public
name|CFFEncoding
name|getEncoding
parameter_list|()
block|{
return|return
name|fontEncoding
return|;
block|}
comment|/**      * Sets the CFFEncoding of the font.      *       * @param encoding the given CFFEncoding      */
specifier|public
name|void
name|setEncoding
parameter_list|(
name|CFFEncoding
name|encoding
parameter_list|)
block|{
name|fontEncoding
operator|=
name|encoding
expr_stmt|;
block|}
comment|/**      * Returns the CFFCharset of the font.      *       * @return the charset      */
specifier|public
name|CFFCharset
name|getCharset
parameter_list|()
block|{
return|return
name|fontCharset
return|;
block|}
comment|/**      * Sets the CFFCharset of the font.      *       * @param charset the given CFFCharset      */
specifier|public
name|void
name|setCharset
parameter_list|(
name|CFFCharset
name|charset
parameter_list|)
block|{
name|fontCharset
operator|=
name|charset
expr_stmt|;
block|}
comment|/**      * Returns the SID for a given glyph name.      * @param name glyph name      * @return SID      */
specifier|private
name|int
name|getSIDForName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|sid
init|=
literal|0
decl_stmt|;
comment|// .notdef
for|for
control|(
name|Mapping
name|m
range|:
name|getMappings
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|sid
operator|=
name|m
operator|.
name|getSID
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
name|sid
return|;
block|}
comment|/**      * Returns the character strings dictionary.      *       * @return the dictionary      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|getCharStringsDict
parameter_list|()
block|{
return|return
name|charStringsDict
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Type1CharString
name|getType1CharString
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getType1CharString
argument_list|(
name|name
argument_list|,
name|getSIDForName
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns the Type 1 CharString for the character with the given name and SID.      *      * @return Type 1 CharString      */
specifier|private
name|Type1CharString
name|getType1CharString
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sid
parameter_list|)
throws|throws
name|IOException
block|{
name|Type2CharString
name|type2
init|=
name|charStringCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|type2
operator|==
literal|null
condition|)
block|{
name|Type2CharStringParser
name|parser
init|=
operator|new
name|Type2CharStringParser
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|type2seq
init|=
name|parser
operator|.
name|parse
argument_list|(
name|charStringsDict
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|,
name|globalSubrIndex
argument_list|,
name|localSubrIndex
argument_list|)
decl_stmt|;
name|type2
operator|=
operator|new
name|Type2CharString
argument_list|(
name|this
argument_list|,
name|fontname
argument_list|,
name|name
argument_list|,
name|type2seq
argument_list|,
name|getDefaultWidthX
argument_list|(
name|sid
argument_list|)
argument_list|,
name|getNominalWidthX
argument_list|(
name|sid
argument_list|)
argument_list|)
expr_stmt|;
name|charStringCache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type2
argument_list|)
expr_stmt|;
block|}
return|return
name|type2
return|;
block|}
comment|/**      * Returns the defaultWidthX for the given SID.      *      * @param sid SID      * @return defaultWidthX      */
specifier|protected
name|int
name|getDefaultWidthX
parameter_list|(
name|int
name|sid
parameter_list|)
block|{
name|Number
name|num
init|=
operator|(
name|Number
operator|)
name|getProperty
argument_list|(
literal|"defaultWidthX"
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|null
condition|)
block|{
return|return
literal|1000
return|;
block|}
return|return
name|num
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Returns the nominalWidthX for the given SID.      *      * @param sid SID      * @return defaultWidthX      */
specifier|protected
name|int
name|getNominalWidthX
parameter_list|(
name|int
name|sid
parameter_list|)
block|{
name|Number
name|num
init|=
operator|(
name|Number
operator|)
name|getProperty
argument_list|(
literal|"nominalWidthX"
argument_list|)
decl_stmt|;
if|if
condition|(
name|num
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|num
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[name="
operator|+
name|fontname
operator|+
literal|", topDict="
operator|+
name|topDict
operator|+
literal|", privateDict="
operator|+
name|privateDict
operator|+
literal|", encoding="
operator|+
name|fontEncoding
operator|+
literal|", charset="
operator|+
name|fontCharset
operator|+
literal|", charStringsDict="
operator|+
name|charStringsDict
operator|+
literal|"]"
return|;
block|}
comment|/**      * Sets the global subroutine index data.      *       * @param globalSubrIndexValue the IndexData object containing the global subroutines      */
specifier|public
name|void
name|setGlobalSubrIndex
parameter_list|(
name|IndexData
name|globalSubrIndexValue
parameter_list|)
block|{
name|globalSubrIndex
operator|=
name|globalSubrIndexValue
expr_stmt|;
block|}
comment|/**      * Returns the global subroutine index data.      *       * @return the dictionary      */
specifier|public
name|IndexData
name|getGlobalSubrIndex
parameter_list|()
block|{
return|return
name|globalSubrIndex
return|;
block|}
comment|/**      * Returns the local subroutine index data.      *       * @return the dictionary      */
specifier|public
name|IndexData
name|getLocalSubrIndex
parameter_list|()
block|{
return|return
name|localSubrIndex
return|;
block|}
comment|/**      * Sets the local subroutine index data.      *       * @param localSubrIndexValue the IndexData object containing the local subroutines      */
specifier|public
name|void
name|setLocalSubrIndex
parameter_list|(
name|IndexData
name|localSubrIndexValue
parameter_list|)
block|{
name|localSubrIndex
operator|=
name|localSubrIndexValue
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
class|class
name|Mapping
implements|implements
name|Type1Mapping
block|{
specifier|private
name|int
name|mappedCode
decl_stmt|;
specifier|private
name|int
name|mappedSID
decl_stmt|;
specifier|private
name|String
name|mappedName
decl_stmt|;
specifier|private
name|byte
index|[]
name|mappedBytes
decl_stmt|;
comment|/**          * {@inheritDoc}          */
specifier|public
name|Type1CharString
name|getType1CharString
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|CFFFont
operator|.
name|this
operator|.
name|getType1CharString
argument_list|(
name|mappedName
argument_list|,
name|mappedSID
argument_list|)
return|;
block|}
comment|/**          * {@inheritDoc}          */
specifier|public
name|int
name|getCode
parameter_list|()
block|{
return|return
name|mappedCode
return|;
block|}
specifier|protected
name|void
name|setCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|mappedCode
operator|=
name|code
expr_stmt|;
block|}
comment|/**          * Gets the value for the SID.          *          * @return the SID          */
specifier|public
name|int
name|getSID
parameter_list|()
block|{
return|return
name|mappedSID
return|;
block|}
specifier|protected
name|void
name|setSID
parameter_list|(
name|int
name|sid
parameter_list|)
block|{
name|this
operator|.
name|mappedSID
operator|=
name|sid
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|mappedName
return|;
block|}
specifier|protected
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|mappedName
operator|=
name|name
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|mappedBytes
return|;
block|}
specifier|protected
name|void
name|setBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|this
operator|.
name|mappedBytes
operator|=
name|bytes
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

