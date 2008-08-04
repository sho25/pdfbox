begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
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
name|ByteArrayInputStream
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
name|security
operator|.
name|cert
operator|.
name|CertificateFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|COSArray
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
name|COSDictionary
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
name|cos
operator|.
name|COSString
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
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * This will read a document from the filesystem, decrypt it and do something with the signature.  *  * usage: java org.pdfbox.examples.signature.ShowSignature&lt;password&gt;&lt;inputfile&gt;  *  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
class|class
name|ShowSignature
block|{
comment|/**      * This is the entry point for the application.      *      * @param args The command-line arguments.      *      * @throws Exception If there is an error reading the file.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|ShowSignature
name|show
init|=
operator|new
name|ShowSignature
argument_list|()
decl_stmt|;
name|show
operator|.
name|showSignature
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|showSignature
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|password
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|infile
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|infile
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
name|document
operator|.
name|decrypt
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Warning: Document is not encrypted."
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|trailer
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
name|COSDictionary
name|root
init|=
operator|(
name|COSDictionary
operator|)
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ROOT
argument_list|)
decl_stmt|;
name|COSDictionary
name|acroForm
init|=
operator|(
name|COSDictionary
operator|)
name|root
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"AcroForm"
argument_list|)
argument_list|)
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|acroForm
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Fields"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|field
init|=
operator|(
name|COSDictionary
operator|)
name|fields
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|field
operator|.
name|getNameAsString
argument_list|(
literal|"FT"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"Sig"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|COSDictionary
name|cert
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"V"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Certificate found"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Name="
operator|+
name|cert
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Name"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Modified="
operator|+
name|cert
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"M"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|COSName
name|subFilter
init|=
operator|(
name|COSName
operator|)
name|cert
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"SubFilter"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|subFilter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|subFilter
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"adbe.x509.rsa_sha1"
argument_list|)
condition|)
block|{
name|COSString
name|certString
init|=
operator|(
name|COSString
operator|)
name|cert
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Cert"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|certData
init|=
name|certString
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|certStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|certData
argument_list|)
decl_stmt|;
name|Collection
name|certs
init|=
name|factory
operator|.
name|generateCertificates
argument_list|(
name|certStream
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certs="
operator|+
name|certs
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subFilter
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"adbe.pkcs7.sha1"
argument_list|)
condition|)
block|{
name|COSString
name|certString
init|=
operator|(
name|COSString
operator|)
name|cert
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Contents"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|certData
init|=
name|certString
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|certStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|certData
argument_list|)
decl_stmt|;
name|Collection
name|certs
init|=
name|factory
operator|.
name|generateCertificates
argument_list|(
name|certStream
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"certs="
operator|+
name|certs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown certificate type:"
operator|+
name|subFilter
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing subfilter for cert dictionary"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Signature found, but no certificate"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will print a usage message.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: java org.pdfbox.examples.signature.ShowSignature "
operator|+
literal|"<password><inputfile>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

