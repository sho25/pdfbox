begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
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
name|Iterator
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_import
import|import
name|org
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
comment|/**  * This is an interface to a text encoder.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.15 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Encoding
implements|implements
name|COSObjectable
block|{
comment|/**      * This is a mapping from a character code to a character name.      */
specifier|protected
name|Map
name|codeToName
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * This is a mapping from a character name to a character code.      */
specifier|protected
name|Map
name|nameToCode
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
name|NAME_TO_CHARACTER
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
name|CHARACTER_TO_NAME
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|BufferedReader
name|glyphStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|InputStream
name|resource
init|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
literal|"Resources/glyphlist.txt"
argument_list|)
decl_stmt|;
name|glyphStream
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|glyphStream
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|//lines starting with # are comments which we can ignore.
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
name|int
name|semicolonIndex
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
if|if
condition|(
name|semicolonIndex
operator|>=
literal|0
condition|)
block|{
try|try
block|{
name|String
name|characterName
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|semicolonIndex
argument_list|)
decl_stmt|;
name|String
name|unicodeValue
init|=
name|line
operator|.
name|substring
argument_list|(
name|semicolonIndex
operator|+
literal|1
argument_list|,
name|line
operator|.
name|length
argument_list|()
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
name|String
name|value
init|=
literal|""
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
operator|+=
operator|(
name|char
operator|)
name|characterCode
expr_stmt|;
block|}
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|characterName
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|nfe
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
name|io
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|glyphStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|glyphStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|".notdef"
argument_list|)
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"fi"
argument_list|)
argument_list|,
literal|"fi"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"fl"
argument_list|)
argument_list|,
literal|"fl"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ffi"
argument_list|)
argument_list|,
literal|"ffi"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ff"
argument_list|)
argument_list|,
literal|"ff"
argument_list|)
expr_stmt|;
name|NAME_TO_CHARACTER
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"pi"
argument_list|)
argument_list|,
literal|"pi"
argument_list|)
expr_stmt|;
name|Iterator
name|keys
init|=
name|NAME_TO_CHARACTER
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|NAME_TO_CHARACTER
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|CHARACTER_TO_NAME
operator|.
name|put
argument_list|(
name|value
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will add a character encoding.      *      * @param code The character code that matches the character.      * @param name The name of the character.      */
specifier|protected
name|void
name|addCharacterEncoding
parameter_list|(
name|int
name|code
parameter_list|,
name|COSName
name|name
parameter_list|)
block|{
name|Integer
name|intCode
init|=
operator|new
name|Integer
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|codeToName
operator|.
name|put
argument_list|(
name|intCode
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
name|intCode
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the character code for the name.      *      * @param name The name of the character.      *      * @return The code for the character.      *      * @throws IOException If there is no character code for the name.      */
specifier|public
name|int
name|getCode
parameter_list|(
name|COSName
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Integer
name|code
init|=
operator|(
name|Integer
operator|)
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
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|code
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *      * @param code The character code.      *      * @return The name of the character.      *      * @throws IOException If there is no name for the code.      */
specifier|public
name|COSName
name|getName
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|codeToName
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
name|code
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
comment|//lets be forgiving for now
name|name
operator|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"space"
argument_list|)
expr_stmt|;
comment|//throw new IOException( getClass().getName() +
comment|//                       ": No name for character code '" + code + "'" );
block|}
return|return
name|name
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *      * @param c The character.      *      * @return The name of the character.      *      * @throws IOException If there is no name for the character.      */
specifier|public
name|COSName
name|getNameFromCharacter
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|CHARACTER_TO_NAME
operator|.
name|get
argument_list|(
literal|""
operator|+
name|c
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
comment|/**      * This will get the character from the code.      *      * @param code The character code.      *      * @return The printable character for the code.      *      * @throws IOException If there is not name for the character.      */
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
name|character
init|=
name|getCharacter
argument_list|(
name|getName
argument_list|(
name|code
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|character
return|;
block|}
comment|/**      * This will get the character from the name.      *      * @param name The name of the character.      *      * @return The printable character for the code.      */
specifier|public
specifier|static
name|String
name|getCharacter
parameter_list|(
name|COSName
name|name
parameter_list|)
block|{
name|String
name|character
init|=
operator|(
name|String
operator|)
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
name|character
operator|=
name|name
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|character
return|;
block|}
block|}
end_class

end_unit

