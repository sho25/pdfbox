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
name|font
operator|.
name|encoding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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

begin_comment
comment|/**  * PostScript glyph list, maps glyph names to sequences of Unicode characters.  * Instances of GlyphList are immutable.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|GlyphList
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|GlyphList
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Adobe Glyph List (AGL)
specifier|private
specifier|static
specifier|final
name|GlyphList
name|DEFAULT
init|=
name|load
argument_list|(
literal|"glyphlist.txt"
argument_list|)
decl_stmt|;
comment|// Zapf Dingbats has its own glyph list
specifier|private
specifier|static
specifier|final
name|GlyphList
name|ZAPF_DINGBATS
init|=
name|load
argument_list|(
literal|"zapfdingbats.txt"
argument_list|)
decl_stmt|;
comment|/**      * Loads a glyph list from disk.      */
specifier|private
specifier|static
name|GlyphList
name|load
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|ClassLoader
name|loader
init|=
name|GlyphList
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|String
name|path
init|=
literal|"org/apache/pdfbox/resources/glyphlist/"
decl_stmt|;
try|try
block|{
return|return
operator|new
name|GlyphList
argument_list|(
name|loader
operator|.
name|getResourceAsStream
argument_list|(
name|path
operator|+
name|filename
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
static|static
block|{
comment|// not supported in PDFBox 2.0, but we issue a warning, see PDFBOX-2379
try|try
block|{
name|String
name|location
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"glyphlist_ext"
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"glyphlist_ext is no longer supported, "
operator|+
literal|"use GlyphList.DEFAULT.addGlyphs(Properties) instead"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
comment|// can occur on System.getProperty
block|{
comment|// PDFBOX-1946 ignore and continue
block|}
block|}
comment|/**      * Returns the Adobe Glyph List (AGL).      */
specifier|public
specifier|static
name|GlyphList
name|getAdobeGlyphList
parameter_list|()
block|{
return|return
name|DEFAULT
return|;
block|}
comment|/**      * Returns the Zapf Dingbats glyph list.      */
specifier|public
specifier|static
name|GlyphList
name|getZapfDingbats
parameter_list|()
block|{
return|return
name|ZAPF_DINGBATS
return|;
block|}
comment|// read-only mappings, never modified outside GlyphList's constructor
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nameToUnicode
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|unicodeToName
decl_stmt|;
comment|// additional read/write cache for uniXXXX names
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|uniNameToUnicodeCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new GlyphList from a glyph list file.      *      * @param input glyph list in Adobe format      * @throws IOException if the glyph list could not be read      */
specifier|public
name|GlyphList
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|nameToUnicode
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|unicodeToName
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|loadList
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new GlyphList from multiple glyph list files.      *      * @param glyphList an existing glyph list to be copied      * @param input glyph list in Adobe format      * @throws IOException if the glyph list could not be read      */
specifier|public
name|GlyphList
parameter_list|(
name|GlyphList
name|glyphList
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|nameToUnicode
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|glyphList
operator|.
name|nameToUnicode
argument_list|)
expr_stmt|;
name|unicodeToName
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|glyphList
operator|.
name|unicodeToName
argument_list|)
expr_stmt|;
name|loadList
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|loadList
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|input
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
name|in
operator|.
name|ready
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|in
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|line
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid glyph list entry: "
operator|+
name|line
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
index|[]
name|unicodeList
init|=
name|parts
index|[
literal|1
index|]
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameToUnicode
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"duplicate value for "
operator|+
name|name
operator|+
literal|" -> "
operator|+
name|parts
index|[
literal|1
index|]
operator|+
literal|" "
operator|+
name|nameToUnicode
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|int
index|[]
name|codePoints
init|=
operator|new
name|int
index|[
name|unicodeList
operator|.
name|length
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|hex
range|:
name|unicodeList
control|)
block|{
name|codePoints
index|[
name|index
operator|++
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|hex
argument_list|,
literal|16
argument_list|)
expr_stmt|;
block|}
name|String
name|string
init|=
operator|new
name|String
argument_list|(
name|codePoints
argument_list|,
literal|0
argument_list|,
name|codePoints
operator|.
name|length
argument_list|)
decl_stmt|;
comment|// forward mapping
name|nameToUnicode
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|string
argument_list|)
expr_stmt|;
comment|// reverse mapping
if|if
condition|(
operator|!
name|unicodeToName
operator|.
name|containsKey
argument_list|(
name|string
argument_list|)
condition|)
block|{
name|unicodeToName
operator|.
name|put
argument_list|(
name|string
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the name for the given Unicode code point.      *      * @param codePoint Unicode code point      * @return PostScript glyph name, or ".notdef"      */
specifier|public
name|String
name|codePointToName
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
name|String
name|name
init|=
name|unicodeToName
operator|.
name|get
argument_list|(
operator|new
name|String
argument_list|(
operator|new
name|int
index|[]
block|{
name|codePoint
block|}
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|".notdef"
return|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Returns the name for a given sequence of Unicode characters.      *      * @param unicodeSequence sequence of Unicode characters      * @return PostScript glyph name, or ".notdef"      */
specifier|public
name|String
name|sequenceToName
parameter_list|(
name|String
name|unicodeSequence
parameter_list|)
block|{
name|String
name|name
init|=
name|unicodeToName
operator|.
name|get
argument_list|(
name|unicodeSequence
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|".notdef"
return|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Returns the Unicode character sequence for the given glyph name, or null if there isn't any.      *      * @param name PostScript glyph name      * @return Unicode character(s), or null.      */
specifier|public
name|String
name|toUnicode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|unicode
init|=
name|nameToUnicode
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
return|return
name|unicode
return|;
block|}
comment|// separate read/write cache for thread safety
name|unicode
operator|=
name|uniNameToUnicodeCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|unicode
operator|==
literal|null
condition|)
block|{
comment|// test if we have a suffix and if so remove it
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|unicode
operator|=
name|toUnicode
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"uni"
argument_list|)
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|==
literal|7
condition|)
block|{
comment|// test for Unicode name in the format uniXXXX where X is hex
name|int
name|nameLength
init|=
name|name
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|uniStr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|chPos
init|=
literal|3
init|;
name|chPos
operator|+
literal|4
operator|<=
name|nameLength
condition|;
name|chPos
operator|+=
literal|4
control|)
block|{
name|int
name|codePoint
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|chPos
argument_list|,
name|chPos
operator|+
literal|4
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
if|if
condition|(
name|codePoint
operator|>
literal|0xD7FF
operator|&&
name|codePoint
operator|<
literal|0xE000
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unicode character name with disallowed code area: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uniStr
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|codePoint
argument_list|)
expr_stmt|;
block|}
block|}
name|unicode
operator|=
name|uniStr
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Not a number in Unicode character name: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"u"
argument_list|)
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|==
literal|5
condition|)
block|{
comment|// test for an alternate Unicode name representation uXXXX
try|try
block|{
name|int
name|codePoint
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
if|if
condition|(
name|codePoint
operator|>
literal|0xD7FF
operator|&&
name|codePoint
operator|<
literal|0xE000
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unicode character name with disallowed code area: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|unicode
operator|=
name|String
operator|.
name|valueOf
argument_list|(
operator|(
name|char
operator|)
name|codePoint
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Not a number in Unicode character name: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|uniNameToUnicodeCache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|unicode
argument_list|)
expr_stmt|;
block|}
return|return
name|unicode
return|;
block|}
block|}
end_class

end_unit

