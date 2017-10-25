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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
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
name|MessageDigest
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
name|PDDocument
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
name|PDSignature
import|;
end_import

begin_comment
comment|/**  * An example for timestamp-singing a PDF for PADeS-Specification. The document will only be  * extended by a signed Timestamp (Signed Timestamp and Hash-Value of the document are signed by a  * Time Stamp Authority (TSA)).  *  * @author Alexis Suter  */
end_comment

begin_class
specifier|public
class|class
name|CreateSignedTimestamp
extends|extends
name|CreateSignedTimestampBase
block|{
comment|/**      * Initialize the signed timestamp creator      */
specifier|public
name|CreateSignedTimestamp
parameter_list|()
block|{     }
comment|/**      * Signs the given PDF file. Alters the original file on disk.      *       * @param file the PDF file to sign      * @throws IOException if the file could not be read or written      */
specifier|public
name|void
name|signDetached
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|signDetached
argument_list|(
name|file
argument_list|,
name|file
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Signs the given PDF file.      *       * @param inFile input PDF file      * @param outFile output PDF file      * @throws IOException if the input file could not be read      */
specifier|public
name|void
name|signDetached
parameter_list|(
name|File
name|inFile
parameter_list|,
name|File
name|outFile
parameter_list|)
throws|throws
name|IOException
block|{
name|signDetached
argument_list|(
name|inFile
argument_list|,
name|outFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Signs the given PDF file.      *       * @param inFile input PDF file      * @param outFile output PDF file      * @param tsaClient optional TSA client      * @throws IOException if the input file could not be read      */
specifier|public
name|void
name|signDetached
parameter_list|(
name|File
name|inFile
parameter_list|,
name|File
name|outFile
parameter_list|,
name|TSAClient
name|tsaClient
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|inFile
operator|==
literal|null
operator|||
operator|!
name|inFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Document for signing does not exist"
argument_list|)
throw|;
block|}
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
comment|// sign
try|try
init|(
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|inFile
argument_list|)
init|)
block|{
name|signDetached
argument_list|(
name|doc
argument_list|,
name|fos
argument_list|,
name|tsaClient
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|signDetached
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|TSAClient
name|tsaClient
parameter_list|)
throws|throws
name|IOException
block|{
name|setTsaClient
argument_list|(
name|tsaClient
argument_list|)
expr_stmt|;
name|int
name|accessPermissions
init|=
name|SigUtils
operator|.
name|getMDPPermission
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessPermissions
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No changes to the document are permitted due to DocMDP transform parameters dictionary"
argument_list|)
throw|;
block|}
comment|// create signature dictionary
name|PDSignature
name|signature
init|=
operator|new
name|PDSignature
argument_list|()
decl_stmt|;
name|signature
operator|.
name|setType
argument_list|(
name|COSName
operator|.
name|DOC_TIME_STAMP
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setFilter
argument_list|(
name|PDSignature
operator|.
name|FILTER_ADOBE_PPKLITE
argument_list|)
expr_stmt|;
name|signature
operator|.
name|setSubFilter
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ETSI.RFC3161"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Optional: certify
if|if
condition|(
name|accessPermissions
operator|==
literal|0
condition|)
block|{
name|SigUtils
operator|.
name|setMDPPermission
argument_list|(
name|document
argument_list|,
name|signature
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
comment|// register signature dictionary and sign interface
name|document
operator|.
name|addSignature
argument_list|(
name|signature
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// write incremental (only for signing purpose)
name|document
operator|.
name|saveIncremental
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
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
name|IOException
throws|,
name|GeneralSecurityException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|tsaUrl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
index|[
literal|1
index|]
operator|.
name|equals
argument_list|(
literal|"-tsa"
argument_list|)
condition|)
block|{
name|tsaUrl
operator|=
name|args
index|[
literal|2
index|]
expr_stmt|;
block|}
else|else
block|{
name|usage
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// TSA client
name|TSAClient
name|tsaClient
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tsaUrl
operator|!=
literal|null
condition|)
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-256"
argument_list|)
decl_stmt|;
name|tsaClient
operator|=
operator|new
name|TSAClient
argument_list|(
operator|new
name|URL
argument_list|(
name|tsaUrl
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|digest
argument_list|)
expr_stmt|;
block|}
comment|// sign PDF
name|CreateSignedTimestamp
name|signing
init|=
operator|new
name|CreateSignedTimestamp
argument_list|()
decl_stmt|;
name|File
name|inFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|inFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|substring
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|inFile
operator|.
name|getParent
argument_list|()
argument_list|,
name|substring
operator|+
literal|"_timestamped.pdf"
argument_list|)
decl_stmt|;
name|signing
operator|.
name|signDetached
argument_list|(
name|inFile
argument_list|,
name|outFile
argument_list|,
name|tsaClient
argument_list|)
expr_stmt|;
block|}
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
literal|"usage: java "
operator|+
name|CreateSignedTimestamp
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
literal|"<pdf_to_sign>\n"
operator|+
literal|"options:\n"
operator|+
literal|"  -tsa<url>    sign timestamp using the given TSA server\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

