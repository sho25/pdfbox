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
name|encoding
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|MissingResourceException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * PostScript glyph list, maps glyph names to Unicode characters.  */
end_comment

begin_class
specifier|public
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
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|NAME_TO_UNICODE
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
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|UNICODE_TO_NAME
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
static|static
block|{
comment|// Loads the official glyph List based on adobes glyph list
name|loadGlyphs
argument_list|(
literal|"org/apache/pdfbox/resources/glyphlist.properties"
argument_list|)
expr_stmt|;
comment|// Loads some additional glyph mappings
name|loadGlyphs
argument_list|(
literal|"org/apache/pdfbox/resources/additional_glyphlist.properties"
argument_list|)
expr_stmt|;
comment|// Load an external glyph list file that user can give as JVM property
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
name|File
name|external
init|=
operator|new
name|File
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|external
operator|.
name|exists
argument_list|()
condition|)
block|{
name|loadGlyphs
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
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
comment|// todo: this is not desirable in many cases, should be done much later, e.g. TextStripper
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
literal|"fi"
argument_list|,
literal|"fi"
argument_list|)
expr_stmt|;
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
literal|"fl"
argument_list|,
literal|"fl"
argument_list|)
expr_stmt|;
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
literal|"ffi"
argument_list|,
literal|"ffi"
argument_list|)
expr_stmt|;
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
literal|"ff"
argument_list|,
literal|"ff"
argument_list|)
expr_stmt|;
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
literal|"pi"
argument_list|,
literal|"pi"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|NAME_TO_UNICODE
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|UNICODE_TO_NAME
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|loadGlyphs
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|Properties
name|glyphProperties
init|=
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|glyphProperties
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingResourceException
argument_list|(
literal|"Glyphlist not found: "
operator|+
name|path
argument_list|,
name|Encoding
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|path
argument_list|)
throw|;
block|}
name|Enumeration
argument_list|<
name|?
argument_list|>
name|names
init|=
name|glyphProperties
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|name
range|:
name|Collections
operator|.
name|list
argument_list|(
name|names
argument_list|)
control|)
block|{
name|String
name|glyphName
init|=
name|name
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|unicodeValue
init|=
name|glyphProperties
operator|.
name|getProperty
argument_list|(
name|glyphName
argument_list|)
decl_stmt|;
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|unicodeValue
argument_list|,
literal|" "
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|StringBuilder
name|value
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|int
name|characterCode
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tokenizer
operator|.
name|nextToken
argument_list|()
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|value
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|characterCode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|NAME_TO_UNICODE
operator|.
name|containsKey
argument_list|(
name|glyphName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"duplicate value for "
operator|+
name|glyphName
operator|+
literal|" -> "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NAME_TO_UNICODE
operator|.
name|put
argument_list|(
name|glyphName
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while reading the glyph property file."
argument_list|,
name|io
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will take a character code and get the name from the code.      *      * @param c Unicode character      * @return PostScript glyph name, or ".notdef"      */
specifier|public
specifier|static
name|String
name|unicodeToName
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|String
name|name
init|=
name|UNICODE_TO_NAME
operator|.
name|get
argument_list|(
name|Character
operator|.
name|toString
argument_list|(
name|c
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
comment|/**      * Returns the Unicode character sequence for the given glyph name, or null if there isn't any.      *      * @param name PostScript glyph name      * @return Unicode character(s), or null.      */
specifier|public
specifier|static
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
name|NAME_TO_UNICODE
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
name|NAME_TO_UNICODE
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

