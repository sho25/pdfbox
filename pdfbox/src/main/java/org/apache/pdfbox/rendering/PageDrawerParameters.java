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
name|rendering
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
name|PDPage
import|;
end_import

begin_comment
comment|/**  * Parameters for a PageDrawer. This class ensures allows PDFRenderer and PageDrawer to share  * private implementation data in a future-proof manner, while still allowing end-users to create  * their own subclasses of PageDrawer.  *   * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PageDrawerParameters
block|{
specifier|private
specifier|final
name|PDFRenderer
name|renderer
decl_stmt|;
specifier|private
specifier|final
name|PDPage
name|page
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|subsamplingAllowed
decl_stmt|;
comment|/**      * Package-private constructor.      */
name|PageDrawerParameters
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|boolean
name|subsamplingAllowed
parameter_list|)
block|{
name|this
operator|.
name|renderer
operator|=
name|renderer
expr_stmt|;
name|this
operator|.
name|page
operator|=
name|page
expr_stmt|;
name|this
operator|.
name|subsamplingAllowed
operator|=
name|subsamplingAllowed
expr_stmt|;
block|}
comment|/**      * Returns the page.      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * Returns the renderer.      */
name|PDFRenderer
name|getRenderer
parameter_list|()
block|{
return|return
name|renderer
return|;
block|}
comment|/**      * Returns whether to allow subsampling of images.      */
specifier|public
name|boolean
name|isSubsamplingAllowed
parameter_list|()
block|{
return|return
name|subsamplingAllowed
return|;
block|}
block|}
end_class

end_unit

