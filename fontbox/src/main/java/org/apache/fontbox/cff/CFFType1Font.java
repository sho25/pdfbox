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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|Type1Equivalent
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
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|GeneralPath
import|;
end_import

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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * A Type 1-equivalent font program represented in a CFF file. Thread safe.  *  * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|CFFType1Font
extends|extends
name|CFFFont
implements|implements
name|Type1Equivalent
block|{
specifier|private
specifier|final
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
name|encoding
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Type2CharString
argument_list|>
name|charStringCache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Integer
argument_list|,
name|Type2CharString
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|PrivateType1CharStringReader
name|reader
init|=
operator|new
name|PrivateType1CharStringReader
argument_list|()
decl_stmt|;
comment|/**      * Private implementation of Type1CharStringReader, because only CFFType1Font can      * expose this publicly, as CIDFonts only support this for legacy 'seac' commands.      */
specifier|private
class|class
name|PrivateType1CharStringReader
implements|implements
name|Type1CharStringReader
block|{
annotation|@
name|Override
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
name|CFFType1Font
operator|.
name|this
operator|.
name|getType1CharString
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|fontName
return|;
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
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
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getWidth
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
argument_list|)
operator|.
name|getWidth
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|sid
init|=
name|charset
operator|.
name|getSID
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|gid
init|=
name|charset
operator|.
name|getGIDForSID
argument_list|(
name|sid
argument_list|)
decl_stmt|;
return|return
name|gid
operator|!=
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Number
argument_list|>
name|getFontMatrix
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|topDict
operator|.
name|get
argument_list|(
literal|"FontMatrix"
argument_list|)
return|;
block|}
comment|/**      * Returns the Type 1 charstring for the given PostScript glyph name.      *      * @param name PostScript glyph name      * @throws IOException if the charstring could not be read      */
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
comment|// some fonts have glyphs beyond their encoding, so we look up by charset SID
name|int
name|sid
init|=
name|charset
operator|.
name|getSID
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|gid
init|=
name|charset
operator|.
name|getGIDForSID
argument_list|(
name|sid
argument_list|)
decl_stmt|;
comment|// lookup in CharStrings INDEX
return|return
name|getType2CharString
argument_list|(
name|gid
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the Type 1 charstring for the given GID.      *      * @param gid GID      * @throws IOException if the charstring could not be read      */
specifier|public
name|Type2CharString
name|getType2CharString
parameter_list|(
name|int
name|gid
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
literal|"GID+"
operator|+
name|gid
decl_stmt|;
comment|// for debugging only
return|return
name|getType2CharString
argument_list|(
name|gid
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|// Returns the Type 1 charstring for the given GID, with name for debugging
specifier|private
name|Type2CharString
name|getType2CharString
parameter_list|(
name|int
name|gid
parameter_list|,
name|String
name|name
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
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|type2
operator|==
literal|null
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
name|charStrings
operator|.
name|get
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytes
operator|==
literal|null
condition|)
block|{
name|bytes
operator|=
name|charStrings
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// .notdef
block|}
name|Type2CharStringParser
name|parser
init|=
operator|new
name|Type2CharStringParser
argument_list|(
name|fontName
argument_list|,
name|name
argument_list|)
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
name|bytes
argument_list|,
name|globalSubrIndex
argument_list|,
name|getLocalSubrIndex
argument_list|()
argument_list|)
decl_stmt|;
name|type2
operator|=
operator|new
name|Type2CharString
argument_list|(
name|reader
argument_list|,
name|fontName
argument_list|,
name|name
argument_list|,
name|gid
argument_list|,
name|type2seq
argument_list|,
name|getDefaultWidthX
argument_list|()
argument_list|,
name|getNominalWidthX
argument_list|()
argument_list|)
expr_stmt|;
name|charStringCache
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|type2
argument_list|)
expr_stmt|;
block|}
return|return
name|type2
return|;
block|}
comment|/**      * Returns the private dictionary.      *      * @return the dictionary      */
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
comment|/**      * Adds the given key/value pair to the private dictionary.      *      * @param name the given key      * @param value the given value      */
comment|// todo: can't we just accept a Map?
name|void
name|addToPrivateDict
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
comment|/**      * Returns the CFFEncoding of the font.      *      * @return the encoding      */
annotation|@
name|Override
specifier|public
name|CFFEncoding
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Sets the CFFEncoding of the font.      *      * @param encoding the given CFFEncoding      */
name|void
name|setEncoding
parameter_list|(
name|CFFEncoding
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
specifier|private
name|IndexData
name|getLocalSubrIndex
parameter_list|()
block|{
return|return
operator|(
name|IndexData
operator|)
name|privateDict
operator|.
name|get
argument_list|(
literal|"Subrs"
argument_list|)
return|;
block|}
comment|// helper for looking up keys/values
specifier|private
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
specifier|private
name|int
name|getDefaultWidthX
parameter_list|()
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
specifier|private
name|int
name|getNominalWidthX
parameter_list|()
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
block|}
end_class

end_unit

