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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
package|;
end_package

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
name|cos
operator|.
name|COSNumber
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

begin_comment
comment|/**  * This represents a destination to a page, see subclasses for specific parameters.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDPageDestination
extends|extends
name|PDDestination
block|{
comment|/**      * Storage for the page destination.      */
specifier|protected
name|COSArray
name|array
decl_stmt|;
comment|/**      * Constructor to create empty page destination.      *      */
specifier|protected
name|PDPageDestination
parameter_list|()
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor to create empty page destination.      *      * @param arr A page destination array.      */
specifier|protected
name|PDPageDestination
parameter_list|(
name|COSArray
name|arr
parameter_list|)
block|{
name|array
operator|=
name|arr
expr_stmt|;
block|}
comment|/**      * This will get the page for this destination.  A page destination      * can either reference a page or a page number(when doing a remote destination to       * another PDF).  If this object is referencing by page number then this method will      * return null and getPageNumber should be used.      *       * @return The page for this destination.      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
name|PDPage
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|COSBase
name|page
init|=
name|array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|page
operator|instanceof
name|COSDictionary
condition|)
block|{
name|retval
operator|=
operator|new
name|PDPage
argument_list|(
operator|(
name|COSDictionary
operator|)
name|page
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the page for this destination.      *       * @param page The page for the destination.      */
specifier|public
name|void
name|setPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the page number for this destination.  A page destination      * can either reference a page or a page number(when doing a remote destination to       * another PDF).  If this object is referencing by page number then this method will      * return that number, otherwise -1 will be returned.      *       * @return The page number for this destination.      */
specifier|public
name|int
name|getPageNumber
parameter_list|()
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|COSBase
name|page
init|=
name|array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|page
operator|instanceof
name|COSNumber
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|page
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the page number for this destination.      *       * @param pageNumber The page for the destination.      */
specifier|public
name|void
name|setPageNumber
parameter_list|(
name|int
name|pageNumber
parameter_list|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|pageNumber
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|array
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSArray
name|getCOSArray
parameter_list|()
block|{
return|return
name|array
return|;
block|}
block|}
end_class

end_unit

