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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|COSArray
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
name|PDPage
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
name|PDPageContentStream
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
name|text
operator|.
name|PDFTextStripper
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
name|text
operator|.
name|TextPosition
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
name|Matrix
import|;
end_import

begin_comment
comment|/**  * This is the main program that simply parses the pdf document and transforms it  * into text.  *  * @author Ben Litchfield  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ExtractText
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
name|ExtractText
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"squid:S2068"
block|}
argument_list|)
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
specifier|private
specifier|static
specifier|final
name|String
name|HTML
init|=
literal|"-html"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALWAYSNEXT
init|=
literal|"-alwaysNext"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROTATION_MAGIC
init|=
literal|"-rotationMagic"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STD_ENCODING
init|=
literal|"UTF-8"
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
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be one and a reference to a file.      *      * @throws IOException if there is an error reading the document or extracting the text.      */
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
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
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
comment|/**      * Starts the text extraction.      *        * @param args the commandline arguments.      * @throws IOException if there is an error reading the document or extracting the text.      */
specifier|public
name|void
name|startExtraction
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
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
name|alwaysNext
init|=
literal|false
decl_stmt|;
name|boolean
name|rotationMagic
init|=
literal|false
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"squid:S2068"
block|}
argument_list|)
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|encoding
init|=
name|STD_ENCODING
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
name|ALWAYSNEXT
argument_list|)
condition|)
block|{
name|alwaysNext
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
name|ROTATION_MAGIC
argument_list|)
condition|)
block|{
name|rotationMagic
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
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
name|password
argument_list|)
expr_stmt|;
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
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|toHTML
operator|&&
operator|!
name|STD_ENCODING
operator|.
name|equals
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|STD_ENCODING
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The encoding parameter is ignored when writing html output."
argument_list|)
expr_stmt|;
block|}
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
name|PDFTextStripper
name|stripper
decl_stmt|;
if|if
condition|(
name|toHTML
condition|)
block|{
comment|// HTML stripper can't work page by page because of startDocument() callback
name|stripper
operator|=
operator|new
name|PDFText2HTML
argument_list|()
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
block|}
else|else
block|{
if|if
condition|(
name|rotationMagic
condition|)
block|{
name|stripper
operator|=
operator|new
name|FilteredTextStripper
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|stripper
operator|=
operator|new
name|PDFTextStripper
argument_list|()
expr_stmt|;
block|}
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
comment|// Extract text for main document:
name|extractPages
argument_list|(
name|startPage
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|endPage
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
argument_list|,
name|stripper
argument_list|,
name|document
argument_list|,
name|output
argument_list|,
name|rotationMagic
argument_list|,
name|alwaysNext
argument_list|)
expr_stmt|;
block|}
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
name|PDComplexFileSpecification
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
name|PDComplexFileSpecification
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
literal|"application/pdf"
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getSubtype
argument_list|()
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
try|try
init|(
name|InputStream
name|fis
init|=
name|file
operator|.
name|createInputStream
argument_list|()
init|;                                         PDDocument subDoc = PDDocument.load(fis)
block|)
block|{
if|if
condition|(
name|toHTML
condition|)
block|{
comment|// will not really work because of HTML header + footer
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
else|else
block|{
name|extractPages
argument_list|(
literal|1
argument_list|,
name|subDoc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|,
name|stripper
argument_list|,
name|subDoc
argument_list|,
name|output
argument_list|,
name|rotationMagic
argument_list|,
name|alwaysNext
argument_list|)
expr_stmt|;
block|}
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
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_function
specifier|private
name|void
name|extractPages
parameter_list|(
name|int
name|startPage
parameter_list|,
name|int
name|endPage
parameter_list|,
name|PDFTextStripper
name|stripper
parameter_list|,
name|PDDocument
name|document
parameter_list|,
name|Writer
name|output
parameter_list|,
name|boolean
name|rotationMagic
parameter_list|,
name|boolean
name|alwaysNext
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|p
init|=
name|startPage
init|;
name|p
operator|<=
name|endPage
condition|;
operator|++
name|p
control|)
block|{
name|stripper
operator|.
name|setStartPage
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|setEndPage
argument_list|(
name|p
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|rotationMagic
condition|)
block|{
name|PDPage
name|page
init|=
name|document
operator|.
name|getPage
argument_list|(
name|p
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|rotation
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
name|page
operator|.
name|setRotation
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|AngleCollector
name|angleCollector
init|=
operator|new
name|AngleCollector
argument_list|()
decl_stmt|;
name|angleCollector
operator|.
name|setStartPage
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|angleCollector
operator|.
name|setEndPage
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|angleCollector
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
operator|new
name|NullWriter
argument_list|()
argument_list|)
expr_stmt|;
comment|// rotation magic
for|for
control|(
name|int
name|angle
range|:
name|angleCollector
operator|.
name|getAngles
argument_list|()
control|)
block|{
comment|// prepend a transformation
comment|// (we could skip these parts for angle 0, but it doesn't matter much)
try|try
init|(
name|PDPageContentStream
name|cs
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|,
name|PDPageContentStream
operator|.
name|AppendMode
operator|.
name|PREPEND
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|cs
operator|.
name|transform
argument_list|(
name|Matrix
operator|.
name|getRotateInstance
argument_list|(
operator|-
name|Math
operator|.
name|toRadians
argument_list|(
name|angle
argument_list|)
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
comment|// remove prepended transformation
operator|(
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
operator|)
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|page
operator|.
name|setRotation
argument_list|(
name|rotation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|alwaysNext
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to process page "
operator|+
name|p
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_function

begin_function
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
end_function

begin_function
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
end_function

begin_function
specifier|static
name|int
name|getAngle
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
comment|// should this become a part of TextPosition?
name|Matrix
name|m
init|=
name|text
operator|.
name|getTextMatrix
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|m
operator|.
name|concatenate
argument_list|(
name|text
operator|.
name|getFont
argument_list|()
operator|.
name|getFontMatrix
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|Math
operator|.
name|toDegrees
argument_list|(
name|Math
operator|.
name|atan2
argument_list|(
name|m
operator|.
name|getShearY
argument_list|()
argument_list|,
name|m
operator|.
name|getScaleY
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**      * This will print the usage requirements and exit.      */
end_comment

begin_function
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar ExtractText [options]<inputfile> [output-text-file]\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -password<password>        : Password to decrypt document\n"
operator|+
literal|"  -encoding<output encoding> : UTF-8 (default) or ISO-8859-1, UTF-16BE,\n"
operator|+
literal|"                                UTF-16LE, etc.\n"
operator|+
literal|"  -console                    : Send text to console instead of file\n"
operator|+
literal|"  -html                       : Output in HTML format instead of raw text\n"
operator|+
literal|"  -sort                       : Sort the text before writing\n"
operator|+
literal|"  -ignoreBeads                : Disables the separation by beads\n"
operator|+
literal|"  -debug                      : Enables debug output about the time consumption\n"
operator|+
literal|"                                of every stage\n"
operator|+
literal|"  -alwaysNext                 : Process next page (if applicable) despite\n"
operator|+
literal|"                                IOException (ignored when -html)\n"
operator|+
literal|"  -rotationMagic              : Analyze each page for rotated/skewed text,\n"
operator|+
literal|"                                rotate to 0° and extract separately\n"
operator|+
literal|"                                (slower, and ignored when -html)\n"
operator|+
literal|"  -startPage<number>         : The first page to start extraction (1 based)\n"
operator|+
literal|"  -endPage<number>           : The last page to extract (1 based, inclusive)\n"
operator|+
literal|"<inputfile>                 : The PDF document to use\n"
operator|+
literal|"  [output-text-file]          : The file to write the text to"
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
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
end_function

begin_comment
unit|}
comment|/**  * Collect all angles while doing text extraction. Angles are in degrees and rounded to the closest  * integer (to avoid slight differences from floating point arithmetic resulting in similarly  * angled glyphs being treated separately). This class must be constructed for each page so that the  * angle set is initialized.  */
end_comment

begin_expr_stmt
unit|class
name|AngleCollector
expr|extends
name|PDFTextStripper
block|{
specifier|private
name|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|angles
operator|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
block|;
name|AngleCollector
argument_list|()
throws|throws
name|IOException
block|{     }
name|Set
argument_list|<
name|Integer
argument_list|>
name|getAngles
argument_list|()
block|{
return|return
name|angles
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|int
name|angle
init|=
name|ExtractText
operator|.
name|getAngle
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|angle
operator|=
operator|(
name|angle
operator|+
literal|360
operator|)
operator|%
literal|360
expr_stmt|;
name|angles
operator|.
name|add
argument_list|(
name|angle
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
unit|}
comment|/**  * TextStripper that only processes glyphs that have angle 0.  */
end_comment

begin_expr_stmt
unit|class
name|FilteredTextStripper
expr|extends
name|PDFTextStripper
block|{
name|FilteredTextStripper
argument_list|()
throws|throws
name|IOException
block|{     }
annotation|@
name|Override
specifier|protected
name|void
name|processTextPosition
argument_list|(
name|TextPosition
name|text
argument_list|)
block|{
name|int
name|angle
operator|=
name|ExtractText
operator|.
name|getAngle
argument_list|(
name|text
argument_list|)
block|;
if|if
condition|(
name|angle
operator|==
literal|0
condition|)
block|{
name|super
operator|.
name|processTextPosition
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
end_expr_stmt

begin_comment
unit|} }
comment|/**  * Dummy output.  */
end_comment

begin_class
class|class
name|NullWriter
extends|extends
name|Writer
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
comment|// do nothing
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
comment|// do nothing
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
comment|// do nothing
block|}
block|}
end_class

end_unit

