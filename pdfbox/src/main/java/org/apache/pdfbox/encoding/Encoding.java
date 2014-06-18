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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
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

begin_comment
comment|/**  * This is an interface to a text encoder.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Encoding
implements|implements
name|COSObjectable
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
name|Encoding
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * This will get an encoding by name.      *      * @param name The name of the encoding to get.      * @return The encoding that matches the name.      * @throws IOException if there is no encoding with that name.      */
specifier|public
specifier|static
name|Encoding
name|getInstance
parameter_list|(
name|COSName
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|COSName
operator|.
name|STANDARD_ENCODING
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|StandardEncoding
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|WIN_ANSI_ENCODING
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|WinAnsiEncoding
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|MAC_ROMAN_ENCODING
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|MacRomanEncoding
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|PDF_DOC_ENCODING
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|PdfDocEncoding
operator|.
name|INSTANCE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown encoding for '"
operator|+
name|name
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
comment|/** Identifies a non-mapped character. */
specifier|public
specifier|static
specifier|final
name|String
name|NOTDEF
init|=
literal|".notdef"
decl_stmt|;
comment|/**      * This is a mapping from a character code to a character name.      */
specifier|protected
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
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
comment|/**      * This is a mapping from a character name to a character code.      */
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nameToCode
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
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|NAME_TO_CHARACTER
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
name|CHARACTER_TO_NAME
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
name|loadGlyphProperties
argument_list|(
literal|"org/apache/pdfbox/resources/glyphlist.properties"
argument_list|)
expr_stmt|;
comment|// Loads some additional glyph mappings
name|loadGlyphProperties
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
name|loadGlyphProperties
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
comment|// can occur on Sytem.getProperty
block|{
comment|// PDFBOX-1946 ignore and continue
block|}
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|NOTDEF
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
literal|"fi"
argument_list|,
literal|"fi"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
literal|"fl"
argument_list|,
literal|"fl"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
literal|"ffi"
argument_list|,
literal|"ffi"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
literal|"ff"
argument_list|,
literal|"ff"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
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
name|NAME_TO_CHARACTER
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|CHARACTER_TO_NAME
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
comment|/**      * Loads a glyph list from a given location and populates the NAME_TO_CHARACTER hashmap for character lookups.      *       * @param location - The string location of the glyphlist file      */
specifier|private
specifier|static
name|void
name|loadGlyphProperties
parameter_list|(
name|String
name|location
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
name|location
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
name|location
argument_list|,
name|Encoding
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|location
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
name|NAME_TO_CHARACTER
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
literal|"duplicate value for characterName="
operator|+
name|glyphName
operator|+
literal|","
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NAME_TO_CHARACTER
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
comment|/**      * Returns an unmodifiable view of the Code2Name mapping.      *       * @return the Code2Name map      */
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|getCodeToNameMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|codeToName
argument_list|)
return|;
block|}
comment|/**      * Returns an unmodifiable view of the Name2Code mapping.      *       * @return the Name2Code map      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getNameToCodeMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|nameToCode
argument_list|)
return|;
block|}
comment|/**      * This will add a character encoding.      *       * @param code The character code that matches the character.      * @param name The name of the character.      */
specifier|public
name|void
name|addCharacterEncoding
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|codeToName
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|nameToCode
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if the encoding has a mapping for the given name value.      *       * @param name the source value for the mapping      * @return the mapped value      */
specifier|public
name|boolean
name|hasCodeForName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|nameToCode
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Determines if the encoding has a mapping for the given code value.      *       * @param code the source value for the mapping      * @return the mapped value      */
specifier|public
name|boolean
name|hasNameForCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|codeToName
operator|.
name|containsKey
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * This will get the character code for the name.      *       * @param name The name of the character.      *       * @return The code for the character.      *       * @throws IOException If there is no character code for the name.      */
specifier|public
name|int
name|getCode
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Integer
name|code
init|=
name|nameToCode
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No character code for character name '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|code
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *       * @param code The character code.      *       * @return The name of the character.      *       * @throws IOException If there is no name for the code.      */
specifier|public
name|String
name|getName
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|codeToName
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *       * @param c The character.      *       * @return The name of the character.      *       * @throws IOException If there is no name for the character.      */
specifier|public
name|String
name|getNameForCharacter
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|CHARACTER_TO_NAME
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No name for character '"
operator|+
name|c
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * This will take a name and get the character code for that name.      *       * @param name The name.      *       * @return The name of the character.      *       */
specifier|public
specifier|static
name|String
name|getCharacterForName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|NAME_TO_CHARACTER
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|NAME_TO_CHARACTER
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No character for name "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will get the character from the code.      *       * @param code The character code.      *       * @return The printable character for the code.      *       * @throws IOException If there is not name for the character.      */
specifier|public
name|String
name|getCharacter
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|getCharacter
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will get the character from the name.      *       * @param name The name of the character.      *       * @return The printable character for the code.      */
specifier|public
name|String
name|getCharacter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|character
init|=
name|NAME_TO_CHARACTER
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|character
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
name|character
operator|=
name|getCharacter
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
comment|// test for Unicode name
comment|// (uniXXXX - XXXX must be a multiple of four;
comment|// each representing a hexadecimal Unicode code point)
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"uni"
argument_list|)
condition|)
block|{
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
name|characterCode
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
name|characterCode
operator|>
literal|0xD7FF
operator|&&
name|characterCode
operator|<
literal|0xE000
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unicode character name with not allowed code area: "
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
name|characterCode
argument_list|)
expr_stmt|;
block|}
block|}
name|character
operator|=
name|uniStr
operator|.
name|toString
argument_list|()
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|character
argument_list|)
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
name|character
operator|=
name|name
expr_stmt|;
block|}
block|}
comment|// test for an alternate Unicode name representation
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"u"
argument_list|)
condition|)
block|{
try|try
block|{
name|int
name|characterCode
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
name|characterCode
operator|>
literal|0xD7FF
operator|&&
name|characterCode
operator|<
literal|0xE000
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unicode character name with not allowed code area: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|character
operator|=
name|String
operator|.
name|valueOf
argument_list|(
operator|(
name|char
operator|)
name|characterCode
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|character
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
name|character
operator|=
name|name
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|nameToCode
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|int
name|code
init|=
name|nameToCode
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|character
operator|=
name|Character
operator|.
name|toString
argument_list|(
operator|(
name|char
operator|)
name|code
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|character
operator|=
name|name
expr_stmt|;
block|}
block|}
return|return
name|character
return|;
block|}
block|}
end_class

end_unit

