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
name|util
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
name|PDPage
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * Split a document into several other documents.  *  * @author Mario Ivankovits (mario@ops.co.at)  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|Splitter
block|{
comment|/**      * The source PDF document.      */
specifier|protected
name|PDDocument
name|pdfDocument
decl_stmt|;
comment|/**      * The current PDF document that contains the splitted page.      */
specifier|protected
name|PDDocument
name|currentDocument
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|splitAtPage
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|startPage
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
specifier|private
name|int
name|endPage
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PDDocument
argument_list|>
name|newDocuments
init|=
literal|null
decl_stmt|;
comment|/**      * The current page number that we are processing, zero based.      */
specifier|protected
name|int
name|pageNumber
init|=
literal|0
decl_stmt|;
comment|/**      * This will take a document and split into several other documents.      *      * @param document The document to split.      *      * @return A list of all the split documents.      *      * @throws IOException If there is an IOError      */
specifier|public
name|List
argument_list|<
name|PDDocument
argument_list|>
name|split
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|newDocuments
operator|=
operator|new
name|ArrayList
argument_list|<
name|PDDocument
argument_list|>
argument_list|()
expr_stmt|;
name|pdfDocument
operator|=
name|document
expr_stmt|;
name|List
name|pages
init|=
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|processPages
argument_list|(
name|pages
argument_list|)
expr_stmt|;
return|return
name|newDocuments
return|;
block|}
comment|/**      * This will tell the splitting algorithm where to split the pages.  The default      * is 1, so every page will become a new document.  If it was to then each document would      * contain 2 pages.  So it the source document had 5 pages it would split into      * 3 new documents, 2 documents containing 2 pages and 1 document containing one      * page.      *      * @param split The number of pages each split document should contain.      */
specifier|public
name|void
name|setSplitAtPage
parameter_list|(
name|int
name|split
parameter_list|)
block|{
if|if
condition|(
name|split
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error split must be at least one page."
argument_list|)
throw|;
block|}
name|splitAtPage
operator|=
name|split
expr_stmt|;
block|}
comment|/**      * This will return how many pages each split document will contain.      *      * @return The split parameter.      */
specifier|public
name|int
name|getSplitAtPage
parameter_list|()
block|{
return|return
name|splitAtPage
return|;
block|}
comment|/**      * This will set the start page.      *       * @param start the start page      */
specifier|public
name|void
name|setStartPage
parameter_list|(
name|int
name|start
parameter_list|)
block|{
if|if
condition|(
name|start
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error split must be at least one page."
argument_list|)
throw|;
block|}
name|startPage
operator|=
name|start
expr_stmt|;
block|}
comment|/**      * This will return the start page.      *      * @return The start page.      */
specifier|public
name|int
name|getStartPage
parameter_list|()
block|{
return|return
name|startPage
return|;
block|}
comment|/**      * This will set the end page.      *       * @param end the end page      */
specifier|public
name|void
name|setEndPage
parameter_list|(
name|int
name|end
parameter_list|)
block|{
if|if
condition|(
name|end
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error split must be at least one page."
argument_list|)
throw|;
block|}
name|endPage
operator|=
name|end
expr_stmt|;
block|}
comment|/**      * This will return the end page.      *      * @return The end page.      */
specifier|public
name|int
name|getEndPage
parameter_list|()
block|{
return|return
name|endPage
return|;
block|}
comment|/**      * Interface method to handle the start of the page processing.      *      * @param pages The list of pages from the source document.      *      * @throws IOException If an IO error occurs.      */
specifier|protected
name|void
name|processPages
parameter_list|(
name|List
name|pages
parameter_list|)
throws|throws
name|IOException
block|{
name|Iterator
name|iter
init|=
name|pages
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pageNumber
operator|+
literal|1
operator|>=
name|startPage
operator|&&
name|pageNumber
operator|+
literal|1
operator|<=
name|endPage
condition|)
block|{
name|processNextPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|pageNumber
operator|>
name|endPage
condition|)
block|{
break|break;
block|}
else|else
block|{
name|pageNumber
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Interface method, you can control where a document gets split by implementing      * this method.  By default a split occurs at every page.  If you wanted to split      * based on some complex logic then you could override this method.  For example.      *<code>      * protected void createNewDocumentIfNecessary()      * {      *     if( isPrime( pageNumber ) )      *     {      *         super.createNewDocumentIfNecessary();      *     }      * }      *</code>      *      * @throws IOException If there is an error creating the new document.      */
specifier|protected
name|void
name|createNewDocumentIfNecessary
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|isNewDocNecessary
argument_list|()
condition|)
block|{
name|createNewDocument
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Check if it is necessary to create a new document.      *      * @return true If a new document should be created.      */
specifier|protected
name|boolean
name|isNewDocNecessary
parameter_list|()
block|{
return|return
name|pageNumber
operator|%
name|splitAtPage
operator|==
literal|0
operator|||
name|currentDocument
operator|==
literal|null
return|;
block|}
comment|/**      * Create a new document to write the splitted contents to.      *      * @throws IOException If there is an problem creating the new document.      */
specifier|protected
name|void
name|createNewDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|currentDocument
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|currentDocument
operator|.
name|setDocumentInformation
argument_list|(
name|pdfDocument
operator|.
name|getDocumentInformation
argument_list|()
argument_list|)
expr_stmt|;
name|currentDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setViewerPreferences
argument_list|(
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getViewerPreferences
argument_list|()
argument_list|)
expr_stmt|;
name|newDocuments
operator|.
name|add
argument_list|(
name|currentDocument
argument_list|)
expr_stmt|;
block|}
comment|/**      * Interface to start processing a new page.      *      * @param page The page that is about to get processed.      *      * @throws IOException If there is an error creating the new document.      */
specifier|protected
name|void
name|processNextPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|createNewDocumentIfNecessary
argument_list|()
expr_stmt|;
name|PDPage
name|imported
init|=
name|currentDocument
operator|.
name|importPage
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|imported
operator|.
name|setCropBox
argument_list|(
name|page
operator|.
name|findCropBox
argument_list|()
argument_list|)
expr_stmt|;
name|imported
operator|.
name|setMediaBox
argument_list|(
name|page
operator|.
name|findMediaBox
argument_list|()
argument_list|)
expr_stmt|;
comment|// only the resources of the page will be copied
name|imported
operator|.
name|setResources
argument_list|(
name|page
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
name|imported
operator|.
name|setRotation
argument_list|(
name|page
operator|.
name|findRotation
argument_list|()
argument_list|)
expr_stmt|;
name|pageNumber
operator|++
expr_stmt|;
block|}
block|}
end_class

end_unit

