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
name|filter
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
name|OutputStream
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

begin_comment
comment|/**  *  * @author adam.nichols  */
end_comment

begin_class
specifier|public
class|class
name|CryptFilter
implements|implements
name|Filter
block|{
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|compressedData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|encryptionName
init|=
operator|(
name|COSName
operator|)
name|options
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionName
operator|==
literal|null
operator|||
name|encryptionName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|IDENTITY
argument_list|)
condition|)
block|{
comment|// currently the only supported implementation is the Identity crypt filter
name|Filter
name|identityFilter
init|=
operator|new
name|IdentityFilter
argument_list|()
decl_stmt|;
name|identityFilter
operator|.
name|decode
argument_list|(
name|compressedData
argument_list|,
name|result
argument_list|,
name|options
argument_list|,
name|filterIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unsupported crypt filter "
operator|+
name|encryptionName
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|encryptionName
init|=
operator|(
name|COSName
operator|)
name|options
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionName
operator|==
literal|null
operator|||
name|encryptionName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|IDENTITY
argument_list|)
condition|)
block|{
comment|// currently the only supported implementation is the Identity crypt filter
name|Filter
name|identityFilter
init|=
operator|new
name|IdentityFilter
argument_list|()
decl_stmt|;
name|identityFilter
operator|.
name|encode
argument_list|(
name|rawData
argument_list|,
name|result
argument_list|,
name|options
argument_list|,
name|filterIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unsupported crypt filter "
operator|+
name|encryptionName
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

