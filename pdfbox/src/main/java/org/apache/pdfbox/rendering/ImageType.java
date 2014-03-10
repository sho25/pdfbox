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
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_comment
comment|/**  * Image type for rendering.  */
end_comment

begin_enum
specifier|public
enum|enum
name|ImageType
block|{
comment|/** Black or white. */
name|BINARY
block|{
annotation|@
name|Override
name|int
name|toBufferedImageType
parameter_list|()
block|{
return|return
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
return|;
block|}
block|}
block|,
comment|/** Shades of gray */
name|GRAY
block|{
annotation|@
name|Override
name|int
name|toBufferedImageType
parameter_list|()
block|{
return|return
name|BufferedImage
operator|.
name|TYPE_BYTE_GRAY
return|;
block|}
block|}
block|,
comment|/** Red, Green, Blue */
name|RGB
block|{
annotation|@
name|Override
name|int
name|toBufferedImageType
parameter_list|()
block|{
return|return
name|BufferedImage
operator|.
name|TYPE_INT_RGB
return|;
block|}
block|}
block|,
comment|/** Alpha, Red, Green, Blue */
name|ARGB
block|{
annotation|@
name|Override
name|int
name|toBufferedImageType
parameter_list|()
block|{
return|return
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
return|;
block|}
block|}
block|;
specifier|abstract
name|int
name|toBufferedImageType
parameter_list|()
function_decl|;
block|}
end_enum

end_unit

