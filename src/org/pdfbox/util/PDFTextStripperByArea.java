begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
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
name|StringWriter
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
name|HashMap
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
name|Vector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSStream
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
name|PDPage
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
name|common
operator|.
name|PDStream
import|;
end_import

begin_comment
comment|/**  * This will extract text from a specified region in the PDF.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFTextStripperByArea
extends|extends
name|PDFTextStripper
block|{
specifier|private
name|List
name|regions
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|regionArea
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|regionCharacterList
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|regionText
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      * @throws IOException If there is an error loading properties.      */
specifier|public
name|PDFTextStripperByArea
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
name|setPageSeparator
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a new region to group text by.      *       * @param regionName The name of the region.      * @param rect The rectangle area to retrieve the text from.      */
specifier|public
name|void
name|addRegion
parameter_list|(
name|String
name|regionName
parameter_list|,
name|Rectangle2D
name|rect
parameter_list|)
block|{
name|regions
operator|.
name|add
argument_list|(
name|regionName
argument_list|)
expr_stmt|;
name|regionArea
operator|.
name|put
argument_list|(
name|regionName
argument_list|,
name|rect
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of regions that have been setup.        *       * @return A list of java.lang.String objects to identify the region names.      */
specifier|public
name|List
name|getRegions
parameter_list|()
block|{
return|return
name|regions
return|;
block|}
comment|/**      * Get the text for the region, this should be called after extractRegions().      *       * @param regionName The name of the region to get the text from.      * @return The text that was identified in that region.      */
specifier|public
name|String
name|getTextForRegion
parameter_list|(
name|String
name|regionName
parameter_list|)
block|{
name|StringWriter
name|text
init|=
operator|(
name|StringWriter
operator|)
name|regionText
operator|.
name|get
argument_list|(
name|regionName
argument_list|)
decl_stmt|;
return|return
name|text
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Process the page to extract the region text.      *       * @param page The page to extract the regions from.      * @throws IOException If there is an error while extracting text.      */
specifier|public
name|void
name|extractRegions
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|Iterator
name|regionIter
init|=
name|regions
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|regionIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|//reset the stored text for the region so this class
comment|//can be reused.
name|String
name|regionName
init|=
operator|(
name|String
operator|)
name|regionIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Vector
name|regionCharactersByArticle
init|=
operator|new
name|Vector
argument_list|()
decl_stmt|;
name|regionCharactersByArticle
operator|.
name|add
argument_list|(
operator|new
name|ArrayList
argument_list|()
argument_list|)
expr_stmt|;
name|regionCharacterList
operator|.
name|put
argument_list|(
name|regionName
argument_list|,
name|regionCharactersByArticle
argument_list|)
expr_stmt|;
name|regionText
operator|.
name|put
argument_list|(
name|regionName
argument_list|,
operator|new
name|StringWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PDStream
name|contentStream
init|=
name|page
operator|.
name|getContents
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentStream
operator|!=
literal|null
condition|)
block|{
name|COSStream
name|contents
init|=
name|contentStream
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|processPage
argument_list|(
name|page
argument_list|,
name|contents
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|void
name|showCharacter
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|Iterator
name|regionIter
init|=
name|regionArea
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|regionIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|region
init|=
operator|(
name|String
operator|)
name|regionIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Rectangle2D
name|rect
init|=
operator|(
name|Rectangle2D
operator|)
name|regionArea
operator|.
name|get
argument_list|(
name|region
argument_list|)
decl_stmt|;
if|if
condition|(
name|rect
operator|.
name|contains
argument_list|(
name|text
operator|.
name|getX
argument_list|()
argument_list|,
name|text
operator|.
name|getY
argument_list|()
argument_list|)
condition|)
block|{
name|charactersByArticle
operator|=
operator|(
name|Vector
operator|)
name|regionCharacterList
operator|.
name|get
argument_list|(
name|region
argument_list|)
expr_stmt|;
name|super
operator|.
name|showCharacter
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will print the text to the output stream.      *      * @throws IOException If there is an error writing the text.      */
specifier|protected
name|void
name|flushText
parameter_list|()
throws|throws
name|IOException
block|{
name|Iterator
name|regionIter
init|=
name|regionArea
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|regionIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|region
init|=
operator|(
name|String
operator|)
name|regionIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|charactersByArticle
operator|=
operator|(
name|Vector
operator|)
name|regionCharacterList
operator|.
name|get
argument_list|(
name|region
argument_list|)
expr_stmt|;
name|output
operator|=
operator|(
name|StringWriter
operator|)
name|regionText
operator|.
name|get
argument_list|(
name|region
argument_list|)
expr_stmt|;
name|super
operator|.
name|flushText
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

