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
operator|.
name|validation
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
name|NoSuchAlgorithmException
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
name|examples
operator|.
name|signature
operator|.
name|validation
operator|.
name|CertInformationCollector
operator|.
name|CertSignatureInformation
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
name|Hex
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
name|ASN1OctetString
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
name|ASN1Sequence
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
name|ASN1TaggedObject
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
name|DLSequence
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
name|x509
operator|.
name|GeneralName
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
name|x509
operator|.
name|X509ObjectIdentifiers
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
name|JcaX509ExtensionUtils
import|;
end_import

begin_class
specifier|public
class|class
name|CertInformationHelper
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
name|CertInformationHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CertInformationHelper
parameter_list|()
block|{     }
comment|/**      * Gets the SHA-1-Hash has of given byte[]-content.      *       * @param content to be hashed      * @return SHA-1 hash String      */
specifier|protected
specifier|static
name|String
name|getSha1Hash
parameter_list|(
name|byte
index|[]
name|content
parameter_list|)
block|{
try|try
block|{
name|MessageDigest
name|md
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
decl_stmt|;
return|return
name|Hex
operator|.
name|getString
argument_list|(
name|md
operator|.
name|digest
argument_list|(
name|content
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"No SHA-1 Algorithm found"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Extracts authority information access extension values from the given data. The Data      * structure has to be implemented as described in RFC 2459, 4.2.2.1.      *      * @param extensionValue byte[] of the extension value.      * @param certInfo where to put the found values      * @throws IOException when there is a problem with the extensionValue      */
specifier|protected
specifier|static
name|void
name|getAuthorityInfoExtensionValue
parameter_list|(
name|byte
index|[]
name|extensionValue
parameter_list|,
name|CertSignatureInformation
name|certInfo
parameter_list|)
throws|throws
name|IOException
block|{
name|ASN1Sequence
name|asn1Seq
init|=
operator|(
name|ASN1Sequence
operator|)
name|JcaX509ExtensionUtils
operator|.
name|parseExtensionValue
argument_list|(
name|extensionValue
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|asn1Seq
operator|.
name|getObjects
argument_list|()
decl_stmt|;
while|while
condition|(
name|objects
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
comment|// AccessDescription
name|ASN1Sequence
name|obj
init|=
operator|(
name|ASN1Sequence
operator|)
name|objects
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|ASN1ObjectIdentifier
name|oid
init|=
operator|(
name|ASN1ObjectIdentifier
operator|)
name|obj
operator|.
name|getObjectAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// accessLocation
name|ASN1TaggedObject
name|location
init|=
operator|(
name|ASN1TaggedObject
operator|)
name|obj
operator|.
name|getObjectAt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|oid
operator|.
name|equals
argument_list|(
name|X509ObjectIdentifiers
operator|.
name|id_ad_ocsp
argument_list|)
operator|&&
name|location
operator|.
name|getTagNo
argument_list|()
operator|==
name|GeneralName
operator|.
name|uniformResourceIdentifier
condition|)
block|{
name|ASN1OctetString
name|url
init|=
operator|(
name|ASN1OctetString
operator|)
name|location
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|certInfo
operator|.
name|setOcspUrl
argument_list|(
operator|new
name|String
argument_list|(
name|url
operator|.
name|getOctets
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oid
operator|.
name|equals
argument_list|(
name|X509ObjectIdentifiers
operator|.
name|id_ad_caIssuers
argument_list|)
condition|)
block|{
name|ASN1OctetString
name|uri
init|=
operator|(
name|ASN1OctetString
operator|)
name|location
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|certInfo
operator|.
name|setIssuerUrl
argument_list|(
operator|new
name|String
argument_list|(
name|uri
operator|.
name|getOctets
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Gets the first CRL URL from given extension value. Structure has to be      * built as in 4.2.1.14 CRL Distribution Points of RFC 2459.      *      * @param extensionValue to get the extension value from      * @return first CRL- URL or null      * @throws IOException when there is a problem with the extensionValue      */
specifier|protected
specifier|static
name|String
name|getCrlUrlFromExtensionValue
parameter_list|(
name|byte
index|[]
name|extensionValue
parameter_list|)
throws|throws
name|IOException
block|{
name|ASN1Sequence
name|asn1Seq
init|=
operator|(
name|ASN1Sequence
operator|)
name|JcaX509ExtensionUtils
operator|.
name|parseExtensionValue
argument_list|(
name|extensionValue
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|asn1Seq
operator|.
name|getObjects
argument_list|()
decl_stmt|;
while|while
condition|(
name|objects
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|DLSequence
name|obj
init|=
operator|(
name|DLSequence
operator|)
name|objects
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|ASN1TaggedObject
name|taggedObject
init|=
operator|(
name|ASN1TaggedObject
operator|)
name|obj
operator|.
name|getObjectAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|taggedObject
operator|=
operator|(
name|ASN1TaggedObject
operator|)
name|taggedObject
operator|.
name|getObject
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|taggedObject
operator|.
name|getObject
argument_list|()
operator|instanceof
name|ASN1OctetString
operator|)
condition|)
block|{
comment|// happens with SampleSignedPDFDocument.pdf
continue|continue;
block|}
name|ASN1OctetString
name|uri
init|=
operator|(
name|ASN1OctetString
operator|)
name|taggedObject
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|String
name|url
init|=
operator|new
name|String
argument_list|(
name|uri
operator|.
name|getOctets
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO Check for: DistributionPoint ::= SEQUENCE (see RFC 2459), multiples can be possible.
comment|// return first http(s)-Url for crl
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
return|return
name|url
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

