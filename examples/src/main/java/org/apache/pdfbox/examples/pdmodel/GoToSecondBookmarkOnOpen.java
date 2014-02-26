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
name|pdmodel
package|;
end_package

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
name|action
operator|.
name|PDActionGoTo
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|documentnavigation
operator|.
name|outline
operator|.
name|PDDocumentOutline
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
name|documentnavigation
operator|.
name|outline
operator|.
name|PDOutlineItem
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * This is an example on how to an action to go to the second page when the PDF is opened.  *  * Usage: java org.apache.pdfbox.examples.pdmodel.GoToSecondPageOnOpen&lt;input-pdf&gt;&lt;output-pdf&gt;  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|GoToSecondBookmarkOnOpen
block|{
specifier|private
name|GoToSecondBookmarkOnOpen
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
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
name|args
index|[
literal|0
index|]
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Cannot add bookmark destination to encrypted documents."
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
name|List
name|pages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
if|if
condition|(
name|pages
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: The PDF must have at least 2 pages."
argument_list|)
throw|;
block|}
name|PDDocumentOutline
name|bookmarks
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getDocumentOutline
argument_list|()
decl_stmt|;
if|if
condition|(
name|bookmarks
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: The PDF does not contain any bookmarks"
argument_list|)
throw|;
block|}
name|PDOutlineItem
name|item
init|=
name|bookmarks
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|PDDestination
name|dest
init|=
name|item
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|PDActionGoTo
name|action
init|=
operator|new
name|PDActionGoTo
argument_list|()
decl_stmt|;
name|action
operator|.
name|setDestination
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setOpenAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.GoToSecondBookmarkOnOpen"
operator|+
literal|"<input-pdf><output-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

