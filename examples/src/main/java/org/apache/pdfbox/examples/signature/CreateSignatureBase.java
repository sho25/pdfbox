begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|interactive
operator|.
name|digitalsignature
operator|.
name|SignatureInterface
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
name|ASN1Primitive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|X509CertificateHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|jcajce
operator|.
name|JcaCertStore
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
name|CMSSignedData
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
name|CMSSignedDataGenerator
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
name|jcajce
operator|.
name|JcaSignerInfoGeneratorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|ContentSigner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OperatorCreationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|jcajce
operator|.
name|JcaContentSignerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|jcajce
operator|.
name|JcaDigestCalculatorProviderBuilder
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
name|util
operator|.
name|Store
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|CreateSignatureBase
implements|implements
name|SignatureInterface
block|{
specifier|protected
name|PrivateKey
name|privateKey
decl_stmt|;
specifier|protected
name|Certificate
name|certificate
decl_stmt|;
specifier|protected
name|TSAClient
name|tsaClient
decl_stmt|;
comment|/**      * Does nothing. Override this if needed.      *      * @param signedData Generated CMS signed data      * @return CMSSignedData Extended CMS signed data      */
specifier|protected
name|CMSSignedData
name|signTimeStamps
parameter_list|(
name|CMSSignedData
name|signedData
parameter_list|)
throws|throws
name|IOException
throws|,
name|TSPException
block|{
return|return
name|signedData
return|;
block|}
comment|/**      * SignatureInterface implementation.      *      * This method will be called from inside of the pdfbox and create the PKCS #7 signature.      * The given InputStream contains the bytes that are given by the byte range.      *      * This method is for internal use only.<-- TODO this method should be private      *      * Use your favorite cryptographic library to implement PKCS #7 signature creation.      */
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|(
name|InputStream
name|content
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|List
argument_list|<
name|Certificate
argument_list|>
name|certList
init|=
operator|new
name|ArrayList
argument_list|<
name|Certificate
argument_list|>
argument_list|()
decl_stmt|;
name|certList
operator|.
name|add
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
name|Store
name|certs
init|=
operator|new
name|JcaCertStore
argument_list|(
name|certList
argument_list|)
decl_stmt|;
name|CMSSignedDataGenerator
name|gen
init|=
operator|new
name|CMSSignedDataGenerator
argument_list|()
decl_stmt|;
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|Certificate
name|cert
init|=
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|Certificate
operator|.
name|getInstance
argument_list|(
name|ASN1Primitive
operator|.
name|fromByteArray
argument_list|(
name|certificate
operator|.
name|getEncoded
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|ContentSigner
name|sha1Signer
init|=
operator|new
name|JcaContentSignerBuilder
argument_list|(
literal|"SHA256WithRSA"
argument_list|)
operator|.
name|build
argument_list|(
name|privateKey
argument_list|)
decl_stmt|;
name|gen
operator|.
name|addSignerInfoGenerator
argument_list|(
operator|new
name|JcaSignerInfoGeneratorBuilder
argument_list|(
operator|new
name|JcaDigestCalculatorProviderBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|sha1Signer
argument_list|,
operator|new
name|X509CertificateHolder
argument_list|(
name|cert
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|gen
operator|.
name|addCertificates
argument_list|(
name|certs
argument_list|)
expr_stmt|;
name|CMSProcessableInputStream
name|msg
init|=
operator|new
name|CMSProcessableInputStream
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|CMSSignedData
name|signedData
init|=
name|gen
operator|.
name|generate
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|tsaClient
operator|!=
literal|null
condition|)
block|{
name|signedData
operator|=
name|signTimeStamps
argument_list|(
name|signedData
argument_list|)
expr_stmt|;
block|}
return|return
name|signedData
operator|.
name|getEncoded
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
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
catch|catch
parameter_list|(
name|CMSException
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
catch|catch
parameter_list|(
name|OperatorCreationException
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
block|}
block|}
end_class

end_unit

