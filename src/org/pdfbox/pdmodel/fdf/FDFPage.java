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
name|pdmodel
operator|.
name|fdf
package|;
end_package

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
name|List
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
name|COSArray
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
name|COSBase
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
name|COSDictionary
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
name|COSArrayList
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
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This represents an FDF page that is part of the FDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFPage
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|page
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFPage
parameter_list|()
block|{
name|page
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param p The FDF page.      */
specifier|public
name|FDFPage
parameter_list|(
name|COSDictionary
name|p
parameter_list|)
block|{
name|page
operator|=
name|p
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * This will get a list of FDFTemplage objects that describe the named pages      * that serve as templates.      *      * @return A list of templates.      */
specifier|public
name|List
name|getTemplates
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
literal|"Templates"
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|List
name|objects
init|=
operator|new
name|ArrayList
argument_list|()
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
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|objects
operator|.
name|add
argument_list|(
operator|new
name|FDFTemplate
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|objects
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * A list of FDFTemplate objects.      *      * @param templates A list of templates for this Page.      */
specifier|public
name|void
name|setTemplates
parameter_list|(
name|List
name|templates
parameter_list|)
block|{
name|page
operator|.
name|setItem
argument_list|(
literal|"Templates"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|templates
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the FDF page info object.      *      * @return The Page info.      */
specifier|public
name|FDFPageInfo
name|getPageInfo
parameter_list|()
block|{
name|FDFPageInfo
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|page
operator|.
name|getDictionaryObject
argument_list|(
literal|"Info"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|FDFPageInfo
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the page info.      *      * @param info The new page info dictionary.      */
specifier|public
name|void
name|setPageInfo
parameter_list|(
name|FDFPageInfo
name|info
parameter_list|)
block|{
name|page
operator|.
name|setItem
argument_list|(
literal|"Info"
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

