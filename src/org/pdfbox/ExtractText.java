begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
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
name|net
operator|.
name|MalformedURLException
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
name|org
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
name|pdfbox
operator|.
name|util
operator|.
name|PDFText2HTML
import|;
end_import

begin_import
import|import
name|org
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
comment|/**      * This is the default encoding of the text to be output.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ENCODING
init|=
literal|null
decl_stmt|;
comment|//"ISO-8859-1";
comment|//"ISO-8859-6"; //arabic
comment|//"US-ASCII";
comment|//"UTF-8";
comment|//"UTF-16";
comment|//"UTF-16BE";
comment|//"UTF-16LE";
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
name|HTML
init|=
literal|"-html"
decl_stmt|;
comment|// jjb - added simple HTML output
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
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|encoding
init|=
name|DEFAULT_ENCODING
decl_stmt|;
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|String
name|textFile
init|=
literal|null
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
name|textFile
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
try|try
block|{
comment|//basically try to load it from a url first and if the URL
comment|//is not recognized then try to load it from the file system.
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|pdfFile
argument_list|)
decl_stmt|;
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|String
name|fileName
init|=
name|url
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|textFile
operator|==
literal|null
operator|&&
name|fileName
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
operator|+
literal|".txt"
argument_list|)
decl_stmt|;
name|textFile
operator|=
name|outputFile
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|textFile
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
name|textFile
operator|=
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
literal|".txt"
expr_stmt|;
block|}
block|}
comment|//document.print();
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
name|textFile
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
name|textFile
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
literal|"Usage: java org.pdfbox.ExtractText [OPTIONS]<PDF file> [Text File]\n"
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
literal|"  -startPage<number>          The first page to start extraction(1 based)\n"
operator|+
literal|"  -endPage<number>            The last page to extract(inclusive)\n"
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

