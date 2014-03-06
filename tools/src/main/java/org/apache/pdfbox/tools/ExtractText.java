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
name|tools
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|PDDocumentCatalog
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
name|PDDocumentNameDictionary
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
name|PDEmbeddedFilesNameTreeNode
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
name|common
operator|.
name|COSObjectable
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
name|common
operator|.
name|filespecification
operator|.
name|PDComplexFileSpecification
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
name|common
operator|.
name|filespecification
operator|.
name|PDEmbeddedFile
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
name|AccessPermission
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
name|StandardDecryptionMaterial
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
name|PDFTextStripper
import|;
end_import

begin_comment
comment|/**  * This is the main program that simply parses the pdf document and transforms it  * into text.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.14 $  */
end_comment

begin_class
specifier|public
class|class
name|ExtractText
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODING
init|=
literal|"-encoding"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONSOLE
init|=
literal|"-console"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|START_PAGE
init|=
literal|"-startPage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|END_PAGE
init|=
literal|"-endPage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SORT
init|=
literal|"-sort"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IGNORE_BEADS
init|=
literal|"-ignoreBeads"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEBUG
init|=
literal|"-debug"
decl_stmt|;
comment|// jjb - added simple HTML output
specifier|private
specifier|static
specifier|final
name|String
name|HTML
init|=
literal|"-html"
decl_stmt|;
comment|// enables pdfbox to skip corrupt objects
specifier|private
specifier|static
specifier|final
name|String
name|FORCE
init|=
literal|"-force"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NONSEQ
init|=
literal|"-nonSeq"
decl_stmt|;
comment|/*      * debug flag      */
specifier|private
name|boolean
name|debug
init|=
literal|false
decl_stmt|;
comment|/**      * private constructor.     */
specifier|private
name|ExtractText
parameter_list|()
block|{
comment|//static class
block|}
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be one and a reference to a file.      *      * @throws Exception If there is an error parsing the document.      */
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
name|ExtractText
name|extractor
init|=
operator|new
name|ExtractText
argument_list|()
decl_stmt|;
name|extractor
operator|.
name|startExtraction
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Starts the text extraction.      *        * @param args the commandline arguments.      *       * @throws Exception if something went wrong.      */
specifier|public
name|void
name|startExtraction
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|toConsole
init|=
literal|false
decl_stmt|;
name|boolean
name|toHTML
init|=
literal|false
decl_stmt|;
name|boolean
name|force
init|=
literal|false
decl_stmt|;
name|boolean
name|sort
init|=
literal|false
decl_stmt|;
name|boolean
name|separateBeads
init|=
literal|true
decl_stmt|;
name|boolean
name|useNonSeqParser
init|=
literal|false
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|encoding
init|=
literal|null
decl_stmt|;
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|String
name|outputFile
init|=
literal|null
decl_stmt|;
comment|// Defaults to text files
name|String
name|ext
init|=
literal|".txt"
decl_stmt|;
name|int
name|startPage
init|=
literal|1
decl_stmt|;
name|int
name|endPage
init|=
name|Integer
operator|.
name|MAX_VALUE
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|PASSWORD
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|ENCODING
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|encoding
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|START_PAGE
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|startPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|HTML
argument_list|)
condition|)
block|{
name|toHTML
operator|=
literal|true
expr_stmt|;
name|ext
operator|=
literal|".html"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|SORT
argument_list|)
condition|)
block|{
name|sort
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|IGNORE_BEADS
argument_list|)
condition|)
block|{
name|separateBeads
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|DEBUG
argument_list|)
condition|)
block|{
name|debug
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|END_PAGE
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|endPage
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|CONSOLE
argument_list|)
condition|)
block|{
name|toConsole
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|FORCE
argument_list|)
condition|)
block|{
name|force
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|NONSEQ
argument_list|)
condition|)
block|{
name|useNonSeqParser
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|outputFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Writer
name|output
init|=
literal|null
decl_stmt|;
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|long
name|startTime
init|=
name|startProcessing
argument_list|(
literal|"Loading PDF "
operator|+
name|pdfFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|outputFile
operator|==
literal|null
operator|&&
name|pdfFile
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|outputFile
operator|=
operator|new
name|File
argument_list|(
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
operator|+
name|ext
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|useNonSeqParser
condition|)
block|{
name|document
operator|=
name|PDDocument
operator|.
name|loadNonSeq
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
literal|null
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|,
name|force
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
name|StandardDecryptionMaterial
name|sdm
init|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|document
operator|.
name|openProtection
argument_list|(
name|sdm
argument_list|)
expr_stmt|;
block|}
block|}
name|AccessPermission
name|ap
init|=
name|document
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ap
operator|.
name|canExtractContent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"You do not have permission to extract text"
argument_list|)
throw|;
block|}
name|stopProcessing
argument_list|(
literal|"Time for loading: "
argument_list|,
name|startTime
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|encoding
operator|==
literal|null
operator|)
operator|&&
operator|(
name|toHTML
operator|)
condition|)
block|{
name|encoding
operator|=
literal|"UTF-8"
expr_stmt|;
block|}
if|if
condition|(
name|toConsole
condition|)
block|{
name|output
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|output
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//use default encoding
name|output
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|PDFTextStripper
name|stripper
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|toHTML
condition|)
block|{
name|stripper
operator|=
operator|new
name|PDFText2HTML
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stripper
operator|=
operator|new
name|PDFTextStripper
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
name|stripper
operator|.
name|setForceParsing
argument_list|(
name|force
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setSortByPosition
argument_list|(
name|sort
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setShouldSeparateByBeads
argument_list|(
name|separateBeads
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setStartPage
argument_list|(
name|startPage
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setEndPage
argument_list|(
name|endPage
argument_list|)
expr_stmt|;
name|startTime
operator|=
name|startProcessing
argument_list|(
literal|"Starting text extraction"
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Writing to "
operator|+
name|outputFile
argument_list|)
expr_stmt|;
block|}
comment|// Extract text for main document:
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
comment|// ... also for any embedded PDFs:
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDDocumentNameDictionary
name|names
init|=
name|catalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|PDEmbeddedFilesNameTreeNode
name|embeddedFiles
init|=
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddedFiles
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
name|embeddedFileNames
init|=
name|embeddedFiles
operator|.
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddedFileNames
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
name|ent
range|:
name|embeddedFileNames
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Processing embedded file "
operator|+
name|ent
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
argument_list|)
expr_stmt|;
block|}
name|PDComplexFileSpecification
name|spec
init|=
operator|(
name|PDComplexFileSpecification
operator|)
name|ent
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|PDEmbeddedFile
name|file
init|=
name|spec
operator|.
name|getEmbeddedFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|getSubtype
argument_list|()
operator|.
name|equals
argument_list|(
literal|"application/pdf"
argument_list|)
condition|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"  is PDF (size="
operator|+
name|file
operator|.
name|getSize
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|fis
init|=
name|file
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|PDDocument
name|subDoc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|subDoc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|stripper
operator|.
name|writeText
argument_list|(
name|subDoc
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|subDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
name|stopProcessing
argument_list|(
literal|"Time for extraction: "
argument_list|,
name|startTime
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
specifier|private
name|long
name|startProcessing
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
specifier|private
name|void
name|stopProcessing
parameter_list|(
name|String
name|message
parameter_list|,
name|long
name|startTime
parameter_list|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|long
name|stopTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|float
name|elapsedTime
init|=
operator|(
call|(
name|float
call|)
argument_list|(
name|stopTime
operator|-
name|startTime
argument_list|)
operator|)
operator|/
literal|1000
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
operator|+
name|elapsedTime
operator|+
literal|" seconds"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will print the usage requirements and exit.      */
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
literal|"Usage: java -jar pdfbox-app-x.y.z.jar ExtractText [OPTIONS]<PDF file> [Text File]\n"
operator|+
literal|"  -password<password>        Password to decrypt document\n"
operator|+
literal|"  -encoding<output encoding> (ISO-8859-1,UTF-16BE,UTF-16LE,...)\n"
operator|+
literal|"  -console                     Send text to console instead of file\n"
operator|+
literal|"  -html                        Output in HTML format instead of raw text\n"
operator|+
literal|"  -sort                        Sort the text before writing\n"
operator|+
literal|"  -ignoreBeads                 Disables the separation by beads\n"
operator|+
literal|"  -force                       Enables pdfbox to ignore corrupt objects\n"
operator|+
literal|"  -debug                       Enables debug output about the time consumption of every stage\n"
operator|+
literal|"  -startPage<number>          The first page to start extraction(1 based)\n"
operator|+
literal|"  -endPage<number>            The last page to extract(inclusive)\n"
operator|+
literal|"  -nonSeq                      Enables the new non-sequential parser\n"
operator|+
literal|"<PDF file>                   The PDF document to use\n"
operator|+
literal|"  [Text File]                  The file to write the text to\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

