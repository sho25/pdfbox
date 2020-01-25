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
name|pdfparser
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
name|COSDocument
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
name|io
operator|.
name|IOUtils
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
name|RandomAccessRead
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
name|ScratchFile
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
name|encryption
operator|.
name|InvalidPasswordException
import|;
end_import

begin_class
specifier|public
class|class
name|PDFParser
extends|extends
name|COSParser
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
name|PDFParser
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source source representing the pdf.      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
literal|""
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param scratchFile use a {@link ScratchFile} for temporary storage.      *       * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
literal|""
argument_list|,
name|scratchFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param scratchFile use a {@link ScratchFile} for temporary storage.      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|scratchFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      * Unrestricted main memory will be used for buffering PDF streams.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param keyStore key store to be used for decryption when using public key security       * @param alias alias to be used for decryption when using public key security      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|InputStream
name|keyStore
parameter_list|,
name|String
name|alias
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
name|keyStore
argument_list|,
name|alias
argument_list|,
name|ScratchFile
operator|.
name|getMainMemoryOnlyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param source input representing the pdf.      * @param decryptionPassword password to be used for decryption.      * @param keyStore key store to be used for decryption when using public key security       * @param alias alias to be used for decryption when using public key security      * @param scratchFile buffer handler for temporary storage; it will be closed on      *        {@link COSDocument#close()}      *      * @throws IOException If something went wrong.      */
specifier|public
name|PDFParser
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|String
name|decryptionPassword
parameter_list|,
name|InputStream
name|keyStore
parameter_list|,
name|String
name|alias
parameter_list|,
name|ScratchFile
name|scratchFile
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|source
argument_list|,
name|decryptionPassword
argument_list|,
name|keyStore
argument_list|,
name|alias
argument_list|)
expr_stmt|;
name|fileLen
operator|=
name|source
operator|.
name|length
argument_list|()
expr_stmt|;
name|init
argument_list|(
name|scratchFile
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
name|String
name|eofLookupRangeStr
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYSPROP_EOFLOOKUPRANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|eofLookupRangeStr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|setEOFLookupRange
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|eofLookupRangeStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"System property "
operator|+
name|SYSPROP_EOFLOOKUPRANGE
operator|+
literal|" does not contain an integer value, but: '"
operator|+
name|eofLookupRangeStr
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
name|document
operator|=
operator|new
name|COSDocument
argument_list|(
name|scratchFile
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * The initial parse will first parse only the trailer, the xrefstart and all xref tables to have a pointer (offset)      * to all the pdf's objects. It can handle linearized pdfs, which will have an xref at the end pointing to an xref      * at the beginning of the file. Last the root object is parsed.      *       * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException If something went wrong.      */
specifier|protected
name|void
name|initialParse
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|trailer
init|=
name|retrieveTrailer
argument_list|()
decl_stmt|;
name|COSDictionary
name|root
init|=
name|trailer
operator|.
name|getCOSDictionary
argument_list|(
name|COSName
operator|.
name|ROOT
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing root object specification in trailer."
argument_list|)
throw|;
block|}
comment|// in some pdfs the type value "Catalog" is missing in the root object
if|if
condition|(
name|isLenient
argument_list|()
operator|&&
operator|!
name|root
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
condition|)
block|{
name|root
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|CATALOG
argument_list|)
expr_stmt|;
block|}
comment|// check pages dictionaries
name|checkPages
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|document
operator|.
name|setDecrypted
argument_list|()
expr_stmt|;
name|initialParseDone
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This will parse the stream and populate the PDDocument object. This will close the keystore stream when it is      * done parsing. Lenient mode is active by default.      *      * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException If there is an error reading from the stream or corrupt data is found.      */
specifier|public
name|PDDocument
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|parse
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * This will parse the stream and populate the PDDocument object. This will close the keystore stream when it is      * done parsing.      *      * @param lenient activate leniency if set to true      * @throws InvalidPasswordException If the password is incorrect.      * @throws IOException If there is an error reading from the stream or corrupt data is found.      */
specifier|public
name|PDDocument
name|parse
parameter_list|(
name|boolean
name|lenient
parameter_list|)
throws|throws
name|IOException
block|{
name|setLenient
argument_list|(
name|lenient
argument_list|)
expr_stmt|;
comment|// set to false if all is processed
name|boolean
name|exceptionOccurred
init|=
literal|true
decl_stmt|;
try|try
block|{
comment|// PDFBOX-1922 read the version header and rewind
if|if
condition|(
operator|!
name|parsePDFHeader
argument_list|()
operator|&&
operator|!
name|parseFDFHeader
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Header doesn't contain versioninfo"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|initialParseDone
condition|)
block|{
name|initialParse
argument_list|()
expr_stmt|;
block|}
name|exceptionOccurred
operator|=
literal|false
expr_stmt|;
name|PDDocument
name|pdDocument
init|=
name|createDocument
argument_list|()
decl_stmt|;
name|pdDocument
operator|.
name|setEncryptionDictionary
argument_list|(
name|getEncryption
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pdDocument
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|exceptionOccurred
operator|&&
name|document
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Create the resulting document. Maybe overwritten if the parser uses another class as document.      *       * @return the resulting document      * @throws IOException if the method is called before parsing the document      */
specifier|protected
name|PDDocument
name|createDocument
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDDocument
argument_list|(
name|getDocument
argument_list|()
argument_list|,
name|source
argument_list|,
name|getAccessPermission
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

