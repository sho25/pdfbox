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
name|encoding
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
name|Collections
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
comment|/**  * A PostScript Encoding vector.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Encoding
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NOTDEF
init|=
literal|".notdef"
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
comment|/**      * This is a mapping from a character code to a character name.      */
specifier|protected
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
comment|/**      * This will add a character encoding.      *      * @param code The character code that matches the character.      * @param name The name of the character.      */
specifier|protected
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
comment|/**      * This will get the character code for the name.      *      * @param name The name of the character.      * @return The code for the character or null if it is not in the encoding.      */
specifier|public
name|Integer
name|getCode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|nameToCode
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *      * @param code The character code.      * @return The name of the character.      */
specifier|public
name|String
name|getName
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|String
name|name
init|=
name|codeToName
operator|.
name|get
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|NOTDEF
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * This will take a character code and get the name from the code.      *      * @param c The character.      * @return The name of the character.      * @throws IOException If there is no name for the character.      */
specifier|public
name|String
name|getNameFromCharacter
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
comment|/**      * This will get the character from the code.      *      * @param code The character code.      * @return The printable character for the code.      * @throws IOException If there is not name for the character.      */
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
return|return
name|getCharacter
argument_list|(
name|getName
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will get the character from the name.      *      * @param name The name of the character.      * @return The printable character for the code.      */
specifier|public
specifier|static
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
name|character
operator|=
name|name
expr_stmt|;
block|}
return|return
name|character
return|;
block|}
comment|/**      * Returns an unmodifiable view of the code to name mapping.      *       * @return the Code2Name map      */
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
block|}
end_class

end_unit

