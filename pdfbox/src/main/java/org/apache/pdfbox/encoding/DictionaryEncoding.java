begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* /*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
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
name|COSBase
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
name|COSDictionary
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
name|cos
operator|.
name|COSNumber
import|;
end_import

begin_comment
comment|/**  * This will perform the encoding from a dictionary.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|DictionaryEncoding
extends|extends
name|Encoding
block|{
specifier|private
specifier|final
name|COSDictionary
name|encoding
decl_stmt|;
specifier|private
specifier|final
name|Encoding
name|baseEncoding
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|differences
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
comment|/**      * Creates a new DictionaryEncoding for embedding.      */
specifier|public
name|DictionaryEncoding
parameter_list|(
name|COSName
name|baseEncoding
parameter_list|,
name|COSArray
name|differences
parameter_list|)
block|{
name|encoding
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|encoding
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|COSName
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|encoding
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DIFFERENCES
argument_list|,
name|differences
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseEncoding
operator|!=
name|COSName
operator|.
name|STANDARD_ENCODING
condition|)
block|{
name|encoding
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|,
name|baseEncoding
argument_list|)
expr_stmt|;
name|this
operator|.
name|baseEncoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|baseEncoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|baseEncoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|baseEncoding
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a new DictionaryEncoding from a PDF.      *      * @param fontEncoding The encoding dictionary.      */
specifier|public
name|DictionaryEncoding
parameter_list|(
name|COSDictionary
name|fontEncoding
parameter_list|,
name|boolean
name|isNonSymbolic
parameter_list|,
name|Encoding
name|builtIn
parameter_list|)
block|{
name|encoding
operator|=
name|fontEncoding
expr_stmt|;
if|if
condition|(
name|encoding
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
condition|)
block|{
name|COSName
name|name
init|=
name|encoding
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
decl_stmt|;
name|baseEncoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isNonSymbolic
condition|)
block|{
comment|// Otherwise, for a nonsymbolic font, it is StandardEncoding
name|baseEncoding
operator|=
name|StandardEncoding
operator|.
name|INSTANCE
expr_stmt|;
block|}
else|else
block|{
comment|// and for a symbolic font, it is the font's built-in encoding."
name|baseEncoding
operator|=
name|builtIn
expr_stmt|;
if|if
condition|(
name|builtIn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Built-in Encoding required for symbolic font"
argument_list|)
throw|;
block|}
block|}
name|codeToName
operator|.
name|putAll
argument_list|(
name|baseEncoding
operator|.
name|codeToName
argument_list|)
expr_stmt|;
name|names
operator|.
name|addAll
argument_list|(
name|baseEncoding
operator|.
name|names
argument_list|)
expr_stmt|;
comment|// now replace with the differences
name|COSArray
name|differences
init|=
operator|(
name|COSArray
operator|)
name|encoding
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DIFFERENCES
argument_list|)
decl_stmt|;
name|int
name|currentIndex
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|differences
operator|!=
literal|null
operator|&&
name|i
operator|<
name|differences
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|next
init|=
name|differences
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSNumber
condition|)
block|{
name|currentIndex
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|next
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|next
decl_stmt|;
name|add
argument_list|(
name|currentIndex
argument_list|,
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|differences
operator|.
name|put
argument_list|(
name|currentIndex
argument_list|,
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|currentIndex
operator|++
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the base encoding.      */
specifier|public
name|Encoding
name|getBaseEncoding
parameter_list|()
block|{
return|return
name|baseEncoding
return|;
block|}
comment|/**      * Returns the Differences array.      */
specifier|public
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|getDifferences
parameter_list|()
block|{
return|return
name|differences
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
block|}
end_class

end_unit

