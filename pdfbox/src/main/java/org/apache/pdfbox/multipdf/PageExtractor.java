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
name|multipdf
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

begin_comment
comment|/**  * This class will extract one or more sequential pages and create a new document.  * @author Adam Nichols (adam@apache.org)  */
end_comment

begin_class
specifier|public
class|class
name|PageExtractor
block|{
specifier|private
name|PDDocument
name|sourceDocument
decl_stmt|;
comment|// first page to extract is page 1 (by default)
specifier|private
name|int
name|startPage
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|endPage
init|=
literal|0
decl_stmt|;
comment|/**       * Creates a new instance of PageExtractor      * @param sourceDocument The document to split.      */
specifier|public
name|PageExtractor
parameter_list|(
name|PDDocument
name|sourceDocument
parameter_list|)
block|{
name|this
operator|.
name|sourceDocument
operator|=
name|sourceDocument
expr_stmt|;
name|endPage
operator|=
name|sourceDocument
operator|.
name|getNumberOfPages
argument_list|()
expr_stmt|;
block|}
comment|/**       * Creates a new instance of PageExtractor      * @param sourceDocument The document to split.      * @param startPage The first page you want extracted (inclusive)      * @param endPage The last page you want extracted (inclusive)      */
specifier|public
name|PageExtractor
parameter_list|(
name|PDDocument
name|sourceDocument
parameter_list|,
name|int
name|startPage
parameter_list|,
name|int
name|endPage
parameter_list|)
block|{
name|this
argument_list|(
name|sourceDocument
argument_list|)
expr_stmt|;
name|this
operator|.
name|startPage
operator|=
name|startPage
expr_stmt|;
name|this
operator|.
name|endPage
operator|=
name|endPage
expr_stmt|;
block|}
comment|/**      * This will take a document and extract the desired pages into a new       * document.  Both startPage and endPage are included in the extracted       * document.  If the endPage is greater than the number of pages in the       * source document, it will go to the end of the document.  If startPage is      * less than 1, it'll start with page 1.  If startPage is greater than       * endPage or greater than the number of pages in the source document, a       * blank document will be returned.      *       * @return The extracted document      * @throws IOException If there is an IOError      */
specifier|public
name|PDDocument
name|extract
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|extractedDocument
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|extractedDocument
operator|.
name|setDocumentInformation
argument_list|(
name|sourceDocument
operator|.
name|getDocumentInformation
argument_list|()
argument_list|)
expr_stmt|;
name|extractedDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setViewerPreferences
argument_list|(
name|sourceDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getViewerPreferences
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startPage
init|;
name|i
operator|<=
name|endPage
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|page
init|=
name|sourceDocument
operator|.
name|getPage
argument_list|(
name|i
operator|-
literal|1
argument_list|)
decl_stmt|;
name|PDPage
name|imported
init|=
name|extractedDocument
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
name|getCropBox
argument_list|()
argument_list|)
expr_stmt|;
name|imported
operator|.
name|setMediaBox
argument_list|(
name|page
operator|.
name|getMediaBox
argument_list|()
argument_list|)
expr_stmt|;
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
name|getRotation
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|extractedDocument
return|;
block|}
comment|/**      * Gets the first page number to be extracted.      * @return the first page number which should be extracted      */
specifier|public
name|int
name|getStartPage
parameter_list|()
block|{
return|return
name|startPage
return|;
block|}
comment|/**      * Sets the first page number to be extracted.      * @param startPage the first page number which should be extracted      */
specifier|public
name|void
name|setStartPage
parameter_list|(
name|int
name|startPage
parameter_list|)
block|{
name|this
operator|.
name|startPage
operator|=
name|startPage
expr_stmt|;
block|}
comment|/**      * Gets the last page number (inclusive) to be extracted.      * @return the last page number which should be extracted      */
specifier|public
name|int
name|getEndPage
parameter_list|()
block|{
return|return
name|endPage
return|;
block|}
comment|/**      * Sets the last page number to be extracted.      * @param endPage the last page number which should be extracted      */
specifier|public
name|void
name|setEndPage
parameter_list|(
name|int
name|endPage
parameter_list|)
block|{
name|this
operator|.
name|endPage
operator|=
name|endPage
expr_stmt|;
block|}
block|}
end_class

end_unit

