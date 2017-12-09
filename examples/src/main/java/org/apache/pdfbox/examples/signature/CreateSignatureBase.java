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
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|UnrecoverableKeyException
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
name|security
operator|.
name|cert
operator|.
name|CertificateException
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
name|X509Certificate
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
name|Arrays
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
specifier|private
name|PrivateKey
name|privateKey
decl_stmt|;
specifier|private
name|Certificate
index|[]
name|certificateChain
decl_stmt|;
specifier|private
name|String
name|tsaUrl
decl_stmt|;
specifier|private
name|boolean
name|externalSigning
decl_stmt|;
comment|/**      * Initialize the signature creator with a keystore (pkcs12) and pin that should be used for the      * signature.      *      * @param keystore is a pkcs12 keystore.      * @param pin is the pin for the keystore / private key      * @throws KeyStoreException if the keystore has not been initialized (loaded)      * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found      * @throws UnrecoverableKeyException if the given password is wrong      * @throws CertificateException if the certificate is not valid as signing time      * @throws IOException if no certificate could be found      */
specifier|public
name|CreateSignatureBase
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|char
index|[]
name|pin
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|UnrecoverableKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|IOException
throws|,
name|CertificateException
block|{
comment|// grabs the first alias from the keystore and get the private key. An
comment|// alternative method or constructor could be used for setting a specific
comment|// alias that should be used.
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|String
name|alias
decl_stmt|;
name|Certificate
name|cert
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|alias
operator|=
name|aliases
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|setPrivateKey
argument_list|(
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|pin
argument_list|)
argument_list|)
expr_stmt|;
name|Certificate
index|[]
name|certChain
init|=
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|certChain
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|setCertificateChain
argument_list|(
name|certChain
argument_list|)
expr_stmt|;
name|cert
operator|=
name|certChain
index|[
literal|0
index|]
expr_stmt|;
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
comment|// avoid expired certificate
operator|(
operator|(
name|X509Certificate
operator|)
name|cert
operator|)
operator|.
name|checkValidity
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not find certificate"
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|final
name|void
name|setPrivateKey
parameter_list|(
name|PrivateKey
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|setCertificateChain
parameter_list|(
specifier|final
name|Certificate
index|[]
name|certificateChain
parameter_list|)
block|{
name|this
operator|.
name|certificateChain
operator|=
name|certificateChain
expr_stmt|;
block|}
specifier|public
name|String
name|getTsaUrl
parameter_list|()
block|{
return|return
name|tsaUrl
return|;
block|}
specifier|public
name|void
name|setTsaUrl
parameter_list|(
name|String
name|tsaUrl
parameter_list|)
block|{
name|this
operator|.
name|tsaUrl
operator|=
name|tsaUrl
expr_stmt|;
block|}
comment|/**      * SignatureInterface implementation.      *      * This method will be called from inside of the pdfbox and create the PKCS #7 signature.      * The given InputStream contains the bytes that are given by the byte range.      *      * This method is for internal use only.      *      * Use your favorite cryptographic library to implement PKCS #7 signature creation.      *      * @throws IOException      */
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
comment|// cannot be done private (interface)
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
argument_list|<>
argument_list|()
decl_stmt|;
name|certList
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|certificateChain
argument_list|)
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
name|certificateChain
index|[
literal|0
index|]
operator|.
name|getEncoded
argument_list|()
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
name|tsaUrl
operator|!=
literal|null
operator|&&
name|tsaUrl
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ValidationTimeStamp
name|validation
init|=
operator|new
name|ValidationTimeStamp
argument_list|(
name|tsaUrl
argument_list|)
decl_stmt|;
name|signedData
operator|=
name|validation
operator|.
name|addSignedTimeStamp
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
decl||
name|CMSException
decl||
name|TSPException
decl||
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
comment|/**      * Set if external signing scenario should be used.      * If {@code false}, SignatureInterface would be used for signing.      *<p>      *     Default: {@code false}      *</p>      * @param externalSigning {@code true} if external signing should be performed      */
specifier|public
name|void
name|setExternalSigning
parameter_list|(
name|boolean
name|externalSigning
parameter_list|)
block|{
name|this
operator|.
name|externalSigning
operator|=
name|externalSigning
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExternalSigning
parameter_list|()
block|{
return|return
name|externalSigning
return|;
block|}
block|}
end_class

end_unit

