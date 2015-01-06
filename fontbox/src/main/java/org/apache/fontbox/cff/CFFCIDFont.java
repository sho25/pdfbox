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
name|type1
operator|.
name|Type1CharStringReader
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * A Type 0 CIDFont represented in a CFF file. Thread safe.  *  * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|CFFCIDFont
extends|extends
name|CFFFont
block|{
specifier|private
name|String
name|registry
decl_stmt|;
specifier|private
name|String
name|ordering
decl_stmt|;
specifier|private
name|int
name|supplement
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|fontDictionaries
init|=
operator|new
name|LinkedList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|privateDictionaries
init|=
operator|new
name|LinkedList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|FDSelect
name|fdSelect
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|CIDKeyedType2CharString
argument_list|>
name|charStringCache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Integer
argument_list|,
name|CIDKeyedType2CharString
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
comment|/**      * Returns the registry value.      * * @return the registry      */
specifier|public
name|String
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
comment|/**      * Sets the registry value.      *      * @param registry the registry to set      */
name|void
name|setRegistry
parameter_list|(
name|String
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
comment|/**      * Returns the ordering value.      *      * @return the ordering      */
specifier|public
name|String
name|getOrdering
parameter_list|()
block|{
return|return
name|ordering
return|;
block|}
comment|/**      * Sets the ordering value.      *      * @param ordering the ordering to set      */
name|void
name|setOrdering
parameter_list|(
name|String
name|ordering
parameter_list|)
block|{
name|this
operator|.
name|ordering
operator|=
name|ordering
expr_stmt|;
block|}
comment|/**      * Returns the supplement value.      *      * @return the supplement      */
specifier|public
name|int
name|getSupplement
parameter_list|()
block|{
return|return
name|supplement
return|;
block|}
comment|/**      * Sets the supplement value.      *      * @param supplement the supplement to set      */
name|void
name|setSupplement
parameter_list|(
name|int
name|supplement
parameter_list|)
block|{
name|this
operator|.
name|supplement
operator|=
name|supplement
expr_stmt|;
block|}
comment|/**      * Returns the font dictionaries.      *      * @return the fontDict      */
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getFontDicts
parameter_list|()
block|{
return|return
name|fontDictionaries
return|;
block|}
comment|/**      * Sets the font dictionaries.      *      * @param fontDict the fontDict to set      */
name|void
name|setFontDict
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|fontDict
parameter_list|)
block|{
name|this
operator|.
name|fontDictionaries
operator|=
name|fontDict
expr_stmt|;
block|}
comment|/**      * Returns the private dictionary.      *      * @return the privDict      */
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getPrivDicts
parameter_list|()
block|{
return|return
name|privateDictionaries
return|;
block|}
comment|/**      * Sets the private dictionary.      *      * @param privDict the privDict to set      */
name|void
name|setPrivDict
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|privDict
parameter_list|)
block|{
name|this
operator|.
name|privateDictionaries
operator|=
name|privDict
expr_stmt|;
block|}
comment|/**      * Returns the fdSelect value.      *      * @return the fdSelect      */
specifier|public
name|FDSelect
name|getFdSelect
parameter_list|()
block|{
return|return
name|fdSelect
return|;
block|}
comment|/**      * Sets the fdSelect value.      *      * @param fdSelect the fdSelect to set      */
name|void
name|setFdSelect
parameter_list|(
name|FDSelect
name|fdSelect
parameter_list|)
block|{
name|this
operator|.
name|fdSelect
operator|=
name|fdSelect
expr_stmt|;
block|}
comment|/**      * Returns the defaultWidthX for the given GID.      *      * @param gid GID      */
specifier|private
name|int
name|getDefaultWidthX
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
name|int
name|fdArrayIndex
init|=
name|this
operator|.
name|fdSelect
operator|.
name|getFDIndex
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|fdArrayIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|1000
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|privDict
init|=
name|this
operator|.
name|privateDictionaries
operator|.
name|get
argument_list|(
name|fdArrayIndex
argument_list|)
decl_stmt|;
return|return
name|privDict
operator|.
name|containsKey
argument_list|(
literal|"defaultWidthX"
argument_list|)
condition|?
operator|(
operator|(
name|Number
operator|)
name|privDict
operator|.
name|get
argument_list|(
literal|"defaultWidthX"
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
else|:
literal|1000
return|;
block|}
comment|/**      * Returns the nominalWidthX for the given GID.      *      * @param gid GID      */
specifier|private
name|int
name|getNominalWidthX
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
name|int
name|fdArrayIndex
init|=
name|this
operator|.
name|fdSelect
operator|.
name|getFDIndex
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|fdArrayIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|0
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|privDict
init|=
name|this
operator|.
name|privateDictionaries
operator|.
name|get
argument_list|(
name|fdArrayIndex
argument_list|)
decl_stmt|;
return|return
name|privDict
operator|.
name|containsKey
argument_list|(
literal|"nominalWidthX"
argument_list|)
condition|?
operator|(
operator|(
name|Number
operator|)
name|privDict
operator|.
name|get
argument_list|(
literal|"nominalWidthX"
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
else|:
literal|0
return|;
block|}
comment|/**      * Returns the LocalSubrIndex for the given GID.      *      * @param gid GID      */
specifier|private
name|IndexData
name|getLocalSubrIndex
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
name|int
name|fdArrayIndex
init|=
name|this
operator|.
name|fdSelect
operator|.
name|getFDIndex
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|fdArrayIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|new
name|IndexData
argument_list|(
literal|0
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|privDict
init|=
name|this
operator|.
name|privateDictionaries
operator|.
name|get
argument_list|(
name|fdArrayIndex
argument_list|)
decl_stmt|;
return|return
operator|(
name|IndexData
operator|)
name|privDict
operator|.
name|get
argument_list|(
literal|"Subrs"
argument_list|)
return|;
block|}
comment|/**      * Returns the Type 2 charstring for the given CID.      *      * @param cid CID      * @throws IOException if the charstring could not be read      */
specifier|public
name|CIDKeyedType2CharString
name|getType2CharString
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|IOException
block|{
name|CIDKeyedType2CharString
name|type2
init|=
name|charStringCache
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|type2
operator|==
literal|null
condition|)
block|{
name|int
name|gid
init|=
name|charset
operator|.
name|getGIDForCID
argument_list|(
name|cid
argument_list|)
decl_stmt|;
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
name|cid
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
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|type2
operator|=
operator|new
name|CIDKeyedType2CharString
argument_list|(
name|reader
argument_list|,
name|fontName
argument_list|,
name|cid
argument_list|,
name|gid
argument_list|,
name|type2seq
argument_list|,
name|getDefaultWidthX
argument_list|(
name|gid
argument_list|)
argument_list|,
name|getNominalWidthX
argument_list|(
name|gid
argument_list|)
argument_list|)
expr_stmt|;
name|charStringCache
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|type2
argument_list|)
expr_stmt|;
block|}
return|return
name|type2
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
comment|// our parser guarantees that FontMatrix will be present and correct in the Top DICT
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
name|CFFCIDFont
operator|.
name|this
operator|.
name|getType2CharString
argument_list|(
literal|0
argument_list|)
return|;
comment|// .notdef
block|}
block|}
block|}
end_class

end_unit

