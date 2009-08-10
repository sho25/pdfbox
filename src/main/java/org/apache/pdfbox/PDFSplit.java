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
name|FileInputStream
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
name|exceptions
operator|.
name|InvalidPasswordException
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
name|exceptions
operator|.
name|COSVisitorException
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
name|pdfparser
operator|.
name|PDFParser
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
name|pdfwriter
operator|.
name|COSWriter
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
name|Splitter
import|;
end_import

begin_comment
comment|/**  * This is the main program that will take a pdf document and split it into  * a number of other documents.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFSplit
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
name|SPLIT
init|=
literal|"-split"
decl_stmt|;
specifier|private
name|PDFSplit
parameter_list|()
block|{     }
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
name|PDFSplit
name|split
init|=
operator|new
name|PDFSplit
argument_list|()
decl_stmt|;
name|split
operator|.
name|split
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|split
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|split
init|=
literal|"1"
decl_stmt|;
name|Splitter
name|splitter
init|=
operator|new
name|Splitter
argument_list|()
decl_stmt|;
name|String
name|pdfFile
init|=
literal|null
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
name|SPLIT
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
name|split
operator|=
name|args
index|[
name|i
index|]
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
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
name|List
name|documents
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
operator|new
name|FileInputStream
argument_list|(
name|pdfFile
argument_list|)
expr_stmt|;
name|document
operator|=
name|parseDocument
argument_list|(
name|input
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
try|try
block|{
name|document
operator|.
name|decrypt
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|4
condition|)
comment|//they supplied the wrong password
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The supplied password is incorrect."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//they didn't suppply a password and the default of "" was wrong.
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: The document is encrypted."
argument_list|)
expr_stmt|;
name|usage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|splitter
operator|.
name|setSplitAtPage
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|split
argument_list|)
argument_list|)
expr_stmt|;
name|documents
operator|=
name|splitter
operator|.
name|split
argument_list|(
name|document
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|documents
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDDocument
name|doc
init|=
operator|(
name|PDDocument
operator|)
name|documents
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
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
literal|"-"
operator|+
name|i
operator|+
literal|".pdf"
decl_stmt|;
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|input
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|documents
operator|!=
literal|null
operator|&&
name|i
operator|<
name|documents
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDDocument
name|doc
init|=
operator|(
name|PDDocument
operator|)
name|documents
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
specifier|final
name|void
name|writeDocument
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
throws|,
name|COSVisitorException
block|{
name|FileOutputStream
name|output
init|=
literal|null
decl_stmt|;
name|COSWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|output
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|COSWriter
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|doc
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
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will parse a document.      *      * @param input The input stream for the document.      *      * @return The document.      *      * @throws IOException If there is an error parsing the document.      */
specifier|private
specifier|static
name|PDDocument
name|parseDocument
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getPDDocument
argument_list|()
return|;
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
literal|"Usage: java org.apache.pdfbox.PDFSplit [OPTIONS]<PDF file>\n"
operator|+
literal|"  -password<password>        Password to decrypt document\n"
operator|+
literal|"  -split<integer>         split after this many pages\n"
operator|+
literal|"<PDF file>                   The PDF document to use\n"
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

