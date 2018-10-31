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
name|util
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

begin_comment
comment|/**  * This is a simple text extraction example to get started. For more advance usage, see the  * ExtractTextByArea and the DrawPrintTextLocations examples in this subproject, as well as the  * ExtractText tool in the tools subproject.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|ExtractTextSimple
block|{
specifier|private
name|ExtractTextSimple
parameter_list|()
block|{
comment|// example class should not be instantiated
block|}
comment|/**      * This will print the documents text page by page.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing or extracting the document.      */
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
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
init|)
block|{
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
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
comment|// This example uses sorting, but in some cases it is more useful to switch it off,
comment|// e.g. in some files with columns where the PDF content stream respects the
comment|// column order.
name|stripper
operator|.
name|setSortByPosition
argument_list|(
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|p
init|=
literal|1
init|;
name|p
operator|<=
name|document
operator|.
name|getNumberOfPages
argument_list|()
condition|;
operator|++
name|p
control|)
block|{
comment|// Set the page interval to extract. If you don't, then all pages would be extracted.
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
comment|// let the magic happen
name|String
name|text
init|=
name|stripper
operator|.
name|getText
argument_list|(
name|document
argument_list|)
decl_stmt|;
comment|// do some nice output with a header
name|String
name|pageStr
init|=
name|String
operator|.
name|format
argument_list|(
literal|"page %d:"
argument_list|,
name|p
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|pageStr
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
name|pageStr
operator|.
name|length
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|text
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// If the extracted text is empty or gibberish, please try extracting text
comment|// with Adobe Reader first before asking for help. Also read the FAQ
comment|// on the website:
comment|// https://pdfbox.apache.org/2.0/faq.html#text-extraction
block|}
block|}
block|}
comment|/**      * This will print the usage for this document.      */
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
literal|"Usage: java "
operator|+
name|ExtractTextSimple
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

