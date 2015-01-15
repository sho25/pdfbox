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
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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
name|io
operator|.
name|IOUtils
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
name|nist
operator|.
name|NISTObjectIdentifiers
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
name|oiw
operator|.
name|OIWObjectIdentifiers
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
name|pkcs
operator|.
name|PKCSObjectIdentifiers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TSPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TimeStampRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TimeStampRequestGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TimeStampResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|tsp
operator|.
name|TimeStampToken
import|;
end_import

begin_comment
comment|/**  * Time Stamping Authority (TSA) Client [RFC 3161].  * @author Vakhtang Koroghlishvili  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|TSAClient
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TSAClient
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|URL
name|url
decl_stmt|;
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
specifier|private
specifier|final
name|MessageDigest
name|digest
decl_stmt|;
comment|/**      *      * @param url the URL of the TSA service      * @param username user name of TSA      * @param password password of TSA      * @param digest the message digest to use      */
specifier|public
name|TSAClient
parameter_list|(
name|URL
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|MessageDigest
name|digest
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|digest
operator|=
name|digest
expr_stmt|;
block|}
comment|/**      *      * @param messageImprint imprint of message contents      * @return the encoded time stamp token      * @throws IOException if there was an error with the connection or data from the TSA server,      *                     or if the time stamp response could not be validated      */
specifier|public
name|byte
index|[]
name|getTimeStampToken
parameter_list|(
name|byte
index|[]
name|messageImprint
parameter_list|)
throws|throws
name|IOException
block|{
name|digest
operator|.
name|reset
argument_list|()
expr_stmt|;
name|byte
index|[]
name|hash
init|=
name|digest
operator|.
name|digest
argument_list|(
name|messageImprint
argument_list|)
decl_stmt|;
comment|// 32-bit cryptographic nonce
name|SecureRandom
name|random
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
name|int
name|nonce
init|=
name|random
operator|.
name|nextInt
argument_list|()
decl_stmt|;
comment|// generate TSA request
name|TimeStampRequestGenerator
name|tsaGenerator
init|=
operator|new
name|TimeStampRequestGenerator
argument_list|()
decl_stmt|;
name|tsaGenerator
operator|.
name|setCertReq
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ASN1ObjectIdentifier
name|oid
init|=
name|getHashObjectIdentifier
argument_list|(
name|digest
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|TimeStampRequest
name|request
init|=
name|tsaGenerator
operator|.
name|generate
argument_list|(
name|oid
argument_list|,
name|hash
argument_list|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|nonce
argument_list|)
argument_list|)
decl_stmt|;
comment|// get TSA response
name|byte
index|[]
name|tsaResponse
init|=
name|getTSAResponse
argument_list|(
name|request
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|TimeStampResponse
name|response
decl_stmt|;
try|try
block|{
name|response
operator|=
operator|new
name|TimeStampResponse
argument_list|(
name|tsaResponse
argument_list|)
expr_stmt|;
name|response
operator|.
name|validate
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TSPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|TimeStampToken
name|token
init|=
name|response
operator|.
name|getTimeStampToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Response does not have a time stamp token"
argument_list|)
throw|;
block|}
return|return
name|token
operator|.
name|getEncoded
argument_list|()
return|;
block|}
comment|// gets response data for the given encoded TimeStampRequest data
comment|// throws IOException if a connection to the TSA cannot be established
specifier|private
name|byte
index|[]
name|getTSAResponse
parameter_list|(
name|byte
index|[]
name|request
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Opening connection to TSA server"
argument_list|)
expr_stmt|;
comment|// todo: support proxy servers
name|URLConnection
name|connection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/timestamp-query"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Established connection to TSA server"
argument_list|)
expr_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|username
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|password
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
block|}
comment|// read response
name|OutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
name|output
operator|=
name|connection
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Waiting for response from TSA server"
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|response
decl_stmt|;
try|try
block|{
name|input
operator|=
name|connection
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|response
operator|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Received response from TSA server"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
comment|// returns the ASN.1 OID of the given hash algorithm
specifier|private
name|ASN1ObjectIdentifier
name|getHashObjectIdentifier
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"MD2"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|md2
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"MD5"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|md5
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"SHA-1"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|idSHA1
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"SHA-224"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha224
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"SHA-256"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha256
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"SHA-384"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha384
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|algorithm
operator|.
name|equals
argument_list|(
literal|"SHA-512"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha512
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ASN1ObjectIdentifier
argument_list|(
name|algorithm
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

