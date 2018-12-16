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
name|PDNamedDestination
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
name|PDPageDestination
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
name|PDOutlineNode
import|;
end_import

begin_comment
comment|/**  * This is an example on how to access the bookmarks that are part of a pdf document.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|PrintBookmarks
block|{
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      */
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
else|else
block|{
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
name|PrintBookmarks
name|meta
init|=
operator|new
name|PrintBookmarks
argument_list|()
decl_stmt|;
name|PDDocumentOutline
name|outline
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
name|outline
operator|!=
literal|null
condition|)
block|{
name|meta
operator|.
name|printBookmark
argument_list|(
name|document
argument_list|,
name|outline
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"This document does not contain any bookmarks"
argument_list|)
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
literal|"Usage: java "
operator|+
name|PrintBookmarks
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the documents bookmarks to System.out.      *      * @param document The document.      * @param bookmark The bookmark to print out.      * @param indentation A pretty printing parameter      *      * @throws IOException If there is an error getting the page count.      */
specifier|public
name|void
name|printBookmark
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|PDOutlineNode
name|bookmark
parameter_list|,
name|String
name|indentation
parameter_list|)
throws|throws
name|IOException
block|{
name|PDOutlineItem
name|current
init|=
name|bookmark
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
comment|// one could also use current.findDestinationPage(document) to get the page number,
comment|// but this example does it the hard way to explain the different types
comment|// Note that bookmarks can also do completely different things, e.g. link to a website,
comment|// or to an external file. This example focuses on internal pages.
if|if
condition|(
name|current
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|PDPageDestination
condition|)
block|{
name|PDPageDestination
name|pd
init|=
operator|(
name|PDPageDestination
operator|)
name|current
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indentation
operator|+
literal|"Destination page: "
operator|+
operator|(
name|pd
operator|.
name|retrievePageNumber
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|PDNamedDestination
condition|)
block|{
name|PDPageDestination
name|pd
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|findNamedDestinationPage
argument_list|(
operator|(
name|PDNamedDestination
operator|)
name|current
operator|.
name|getDestination
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pd
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
name|indentation
operator|+
literal|"Destination page: "
operator|+
operator|(
name|pd
operator|.
name|retrievePageNumber
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|getDestination
argument_list|()
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
name|indentation
operator|+
literal|"Destination class: "
operator|+
name|current
operator|.
name|getDestination
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|getAction
argument_list|()
operator|instanceof
name|PDActionGoTo
condition|)
block|{
name|PDActionGoTo
name|gta
init|=
operator|(
name|PDActionGoTo
operator|)
name|current
operator|.
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|gta
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|PDPageDestination
condition|)
block|{
name|PDPageDestination
name|pd
init|=
operator|(
name|PDPageDestination
operator|)
name|gta
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indentation
operator|+
literal|"Destination page: "
operator|+
operator|(
name|pd
operator|.
name|retrievePageNumber
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|gta
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|PDNamedDestination
condition|)
block|{
name|PDPageDestination
name|pd
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|findNamedDestinationPage
argument_list|(
operator|(
name|PDNamedDestination
operator|)
name|gta
operator|.
name|getDestination
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pd
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
name|indentation
operator|+
literal|"Destination page: "
operator|+
operator|(
name|pd
operator|.
name|retrievePageNumber
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
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
name|indentation
operator|+
literal|"Destination class2: "
operator|+
name|gta
operator|.
name|getDestination
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|getAction
argument_list|()
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
name|indentation
operator|+
literal|"Action class: "
operator|+
name|current
operator|.
name|getAction
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indentation
operator|+
name|current
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|printBookmark
argument_list|(
name|document
argument_list|,
name|current
argument_list|,
name|indentation
operator|+
literal|"    "
argument_list|)
expr_stmt|;
name|current
operator|=
name|current
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

