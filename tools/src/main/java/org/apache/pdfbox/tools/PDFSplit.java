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
name|IOException
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
name|multipdf
operator|.
name|Splitter
import|;
end_import

begin_comment
comment|/**  * This is the main program that will take a pdf document and split it into  * a number of other documents.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
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
name|OUTPUT_PREFIX
init|=
literal|"-outputPrefix"
decl_stmt|;
specifier|private
name|PDFSplit
parameter_list|()
block|{     }
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be one and a reference to a file.      *      * @throws IOException If there is an error parsing the document.      */
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
name|IOException
block|{
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|split
init|=
literal|null
decl_stmt|;
name|String
name|startPage
init|=
literal|null
decl_stmt|;
name|String
name|endPage
init|=
literal|null
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
name|String
name|outputPrefix
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
name|OUTPUT_PREFIX
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|outputPrefix
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
if|if
condition|(
name|outputPrefix
operator|==
literal|null
condition|)
block|{
name|outputPrefix
operator|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|PDDocument
argument_list|>
name|documents
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
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|int
name|numberOfPages
init|=
name|document
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|boolean
name|startEndPageSet
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|startPage
operator|!=
literal|null
condition|)
block|{
name|splitter
operator|.
name|setStartPage
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|startPage
argument_list|)
argument_list|)
expr_stmt|;
name|startEndPageSet
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|split
operator|==
literal|null
condition|)
block|{
name|splitter
operator|.
name|setSplitAtPage
argument_list|(
name|numberOfPages
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|endPage
operator|!=
literal|null
condition|)
block|{
name|splitter
operator|.
name|setEndPage
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|endPage
argument_list|)
argument_list|)
expr_stmt|;
name|startEndPageSet
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|split
operator|==
literal|null
condition|)
block|{
name|splitter
operator|.
name|setSplitAtPage
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|endPage
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|split
operator|!=
literal|null
condition|)
block|{
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
block|}
else|else
block|{
if|if
condition|(
operator|!
name|startEndPageSet
condition|)
block|{
name|splitter
operator|.
name|setSplitAtPage
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
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
name|outputPrefix
operator|+
literal|"-"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
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
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar PDFSplit [options]<inputfile>\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -password<password>  : Password to decrypt document\n"
operator|+
literal|"  -split<integer>   : split after this many pages (default 1, if startPage and endPage are unset)\n"
operator|+
literal|"  -startPage<integer>   : start page\n"
operator|+
literal|"  -endPage<integer>   : end page\n"
operator|+
literal|"  -outputPrefix<prefix> : Filename prefix for splitted files\n"
operator|+
literal|"<inputfile>            : The PDF document to use\n"
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
block|}
end_class

end_unit

