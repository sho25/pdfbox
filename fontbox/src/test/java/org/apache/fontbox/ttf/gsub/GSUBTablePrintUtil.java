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
name|ttf
operator|.
name|gsub
package|;
end_package

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
name|fontbox
operator|.
name|ttf
operator|.
name|CmapLookup
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
name|ttf
operator|.
name|GSUBTableDebugger
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
name|ttf
operator|.
name|model
operator|.
name|GsubData
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
name|ttf
operator|.
name|model
operator|.
name|ScriptFeature
import|;
end_import

begin_comment
comment|/**  * This class is to be used mainly for debugging purposes. It is used in {@link GSUBTableDebugger}. It is used to print  * all the GSUB Glyphs to characters. The format is:<br>  *   *&lt;Serial no.&gt;.)&lt;Space separated characters to be replaced&gt; : RawUnicode: [&lt;Space separated unicode  * representation of each character to be replaced in hexadecimal&gt;] :&lt;The compound character&gt; :&lt;The  * GlyphId with which these characters are replaced&gt;  *   * @author Palash Ray  *   */
end_comment

begin_class
specifier|public
class|class
name|GSUBTablePrintUtil
block|{
specifier|public
name|void
name|printCharacterToGlyph
parameter_list|(
name|GsubData
name|gsubData
parameter_list|,
name|CmapLookup
name|cmap
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Format:\n<Serial no.>.)<Space separated characters to be replaced> : RawUnicode: [<Space separated unicode representation of each character to be replaced in hexadecimal>] :<The compound character> :<The GlyphId with which these characters are replaced>"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|rawGSubTableData
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|featureName
range|:
name|gsubData
operator|.
name|getSupportedFeatures
argument_list|()
control|)
block|{
name|ScriptFeature
name|scriptFeature
init|=
name|gsubData
operator|.
name|getFeature
argument_list|(
name|featureName
argument_list|)
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsToBeReplaced
range|:
name|scriptFeature
operator|.
name|getAllGlyphIdsForSubstitution
argument_list|()
control|)
block|{
name|rawGSubTableData
operator|.
name|put
argument_list|(
name|scriptFeature
operator|.
name|getReplacementForGlyphs
argument_list|(
name|glyphsToBeReplaced
argument_list|)
argument_list|,
name|glyphsToBeReplaced
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|featureName
range|:
name|gsubData
operator|.
name|getSupportedFeatures
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"******************      "
operator|+
name|featureName
operator|+
literal|"      ******************"
argument_list|)
expr_stmt|;
name|ScriptFeature
name|scriptFeature
init|=
name|gsubData
operator|.
name|getFeature
argument_list|(
name|featureName
argument_list|)
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsToBeReplaced
range|:
name|scriptFeature
operator|.
name|getAllGlyphIdsForSubstitution
argument_list|()
control|)
block|{
name|String
name|unicodeText
init|=
name|getUnicodeString
argument_list|(
name|rawGSubTableData
argument_list|,
name|cmap
argument_list|,
name|glyphsToBeReplaced
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|++
name|index
operator|+
literal|".) "
operator|+
name|getExplainedUnicodeText
argument_list|(
name|unicodeText
argument_list|)
operator|+
literal|" : "
operator|+
name|scriptFeature
operator|.
name|getReplacementForGlyphs
argument_list|(
name|glyphsToBeReplaced
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getUnicodeChar
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|rawGSubTableData
parameter_list|,
name|CmapLookup
name|cmap
parameter_list|,
name|Integer
name|glyphId
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|keyChars
init|=
name|cmap
operator|.
name|getCharCodes
argument_list|(
name|glyphId
argument_list|)
decl_stmt|;
comment|// its a compound glyph
if|if
condition|(
name|keyChars
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|constituentGlyphs
init|=
name|rawGSubTableData
operator|.
name|get
argument_list|(
name|glyphId
argument_list|)
decl_stmt|;
if|if
condition|(
name|constituentGlyphs
operator|==
literal|null
operator|||
name|constituentGlyphs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"lookup for the glyphId: "
operator|+
name|glyphId
operator|+
literal|" failed, as no corresponding Unicode char found mapped to it"
decl_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|message
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|getUnicodeString
argument_list|(
name|rawGSubTableData
argument_list|,
name|cmap
argument_list|,
name|constituentGlyphs
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|unicodeChar
range|:
name|keyChars
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|unicodeChar
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|private
name|String
name|getUnicodeString
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|rawGSubTableData
parameter_list|,
name|CmapLookup
name|cmap
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIDs
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|glyphId
range|:
name|glyphIDs
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getUnicodeChar
argument_list|(
name|rawGSubTableData
argument_list|,
name|cmap
argument_list|,
name|glyphId
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getExplainedUnicodeText
parameter_list|(
name|String
name|unicodeText
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|unicode
range|:
name|unicodeText
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|unicode
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" RawUnicode: ["
argument_list|)
expr_stmt|;
for|for
control|(
name|char
name|unicode
range|:
name|unicodeText
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\\u0"
argument_list|)
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|unicode
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"] : "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|unicodeText
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

