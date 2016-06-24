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
name|HashSet
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

begin_comment
comment|/**  * A PostScript encoding vector, maps character codes to glyph names.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Encoding
implements|implements
name|COSObjectable
block|{
comment|/**      * This will get an encoding by name. May return null.      *      * @param name The name of the encoding to get.      * @return The encoding that matches the name.      */
specifier|public
specifier|static
name|Encoding
name|getInstance
parameter_list|(
name|COSName
name|name
parameter_list|)
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
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
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
argument_list|(
literal|250
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|inverted
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
literal|250
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|names
decl_stmt|;
comment|/**      * Returns an unmodifiable view of the code -&gt; name mapping.      *       * @return the code -&gt; name map      */
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
comment|/**      * Returns an unmodifiable view of the name -&gt; code mapping. More than one name may map to      * the same code.      *      * @return the name -&gt; code map      */
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
name|inverted
argument_list|)
return|;
block|}
comment|/**      * This will add a character encoding.      *       * @param code character code      * @param name PostScript glyph name      */
specifier|protected
name|void
name|add
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
name|inverted
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if the encoding has a mapping for the given name value.      *       * @param name PostScript glyph name      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// we have to wait until all add() calls are done before building the name cache
comment|// otherwise /Differences won't be accounted for
if|if
condition|(
name|names
operator|==
literal|null
condition|)
block|{
name|names
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|codeToName
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|names
operator|.
name|addAll
argument_list|(
name|codeToName
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|names
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Determines if the encoding has a mapping for the given code value.      *       * @param code character code      */
specifier|public
name|boolean
name|contains
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
comment|/**      * This will take a character code and get the name from the code.      *       * @param code character code      * @return PostScript glyph name      */
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
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
return|return
literal|".notdef"
return|;
block|}
comment|/**      * Returns the name of this encoding.      */
specifier|public
specifier|abstract
name|String
name|getEncodingName
parameter_list|()
function_decl|;
block|}
end_class

end_unit

