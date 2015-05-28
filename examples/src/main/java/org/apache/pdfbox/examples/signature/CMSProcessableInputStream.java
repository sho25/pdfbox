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
name|examples
operator|.
name|signature
package|;
end_package

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1ObjectIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|CMSObjectIdentifiers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSTypedData
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

begin_comment
comment|/**  * Wraps a InputStream into a CMSProcessable object for bouncy castle.  * It's an alternative to the CMSProcessableByteArray.  * @author Thomas Chojecki  */
end_comment

begin_class
class|class
name|CMSProcessableInputStream
implements|implements
name|CMSTypedData
block|{
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
specifier|final
name|ASN1ObjectIdentifier
name|contentType
decl_stmt|;
name|CMSProcessableInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|CMSObjectIdentifiers
operator|.
name|data
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
name|CMSProcessableInputStream
parameter_list|(
name|ASN1ObjectIdentifier
name|type
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
name|contentType
operator|=
name|type
expr_stmt|;
name|in
operator|=
name|is
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getContent
parameter_list|()
block|{
return|return
name|in
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|CMSException
block|{
comment|// read the content only one time
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8
operator|*
literal|1024
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ASN1ObjectIdentifier
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
block|}
end_class

end_unit

