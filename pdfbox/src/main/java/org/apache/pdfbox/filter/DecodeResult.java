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
name|filter
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
name|cos
operator|.
name|COSDictionary
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
name|graphics
operator|.
name|color
operator|.
name|PDJPXColorSpace
import|;
end_import

begin_comment
comment|/**  * The result of a filter decode operation. Allows information such as color space to be  * extracted from image streams, and for stream parameters to be repaired during reading.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DecodeResult
block|{
comment|/** Default decode result. */
specifier|public
specifier|static
specifier|final
name|DecodeResult
name|DEFAULT
init|=
operator|new
name|DecodeResult
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|parameters
decl_stmt|;
specifier|private
name|PDJPXColorSpace
name|colorSpace
decl_stmt|;
name|DecodeResult
parameter_list|(
name|COSDictionary
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
name|DecodeResult
parameter_list|(
name|COSDictionary
name|parameters
parameter_list|,
name|PDJPXColorSpace
name|colorSpace
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|colorSpace
operator|=
name|colorSpace
expr_stmt|;
block|}
comment|/**      * Returns the stream parameters, repaired using the embedded stream data.      * @return the repaired stream parameters, or an empty dictionary      */
specifier|public
name|COSDictionary
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Returns the embedded JPX color space, if any.      * @return the the embedded JPX color space, or null if there is none.      */
specifier|public
name|PDJPXColorSpace
name|getJPXColorSpace
parameter_list|()
block|{
return|return
name|colorSpace
return|;
block|}
comment|// Sets the JPX color space
name|void
name|setColorSpace
parameter_list|(
name|PDJPXColorSpace
name|colorSpace
parameter_list|)
block|{
name|this
operator|.
name|colorSpace
operator|=
name|colorSpace
expr_stmt|;
block|}
block|}
end_class

end_unit

