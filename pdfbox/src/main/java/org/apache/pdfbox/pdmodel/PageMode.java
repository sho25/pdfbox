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
name|pdmodel
package|;
end_package

begin_comment
comment|/**  * A name object specifying how the document shall be displayed when opened.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|PageMode
block|{
comment|/** Neither the outline nor the thumbnails are displayed. */
name|USE_NONE
argument_list|(
literal|"UseNone"
argument_list|)
block|,
comment|/** Show bookmarks when pdf is opened. */
name|USE_OUTLINES
argument_list|(
literal|"UseOutlines"
argument_list|)
block|,
comment|/** Show thumbnails when pdf is opened. */
name|USE_THUMBS
argument_list|(
literal|"UseThumbs"
argument_list|)
block|,
comment|/** Full screen mode with no menu bar, window controls. */
name|FULL_SCREEN
argument_list|(
literal|"FullScreen"
argument_list|)
block|,
comment|/** Optional content group panel is visible when opened. */
name|USE_OPTIONAL_CONTENT
argument_list|(
literal|"UseOC"
argument_list|)
block|,
comment|/** Attachments panel is visible. */
name|USE_ATTACHMENTS
argument_list|(
literal|"UseAttachments"
argument_list|)
block|;
specifier|public
specifier|static
name|PageMode
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|PageMode
name|instance
range|:
name|PageMode
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|instance
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|instance
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
name|PageMode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Returns the string value, as used in a PDF file.      */
specifier|public
name|String
name|stringValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_enum

end_unit

