begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|graphics
operator|.
name|pattern
operator|.
name|PDTilingPattern
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDPatternContentStream
extends|extends
name|PDAbstractContentStream
block|{
comment|/**      * Create a new tiling pattern content stream.      *      * @param pattern The tiling pattern stream to write to.      *       * @throws IOException If there is an error writing to the form contents.      */
specifier|public
name|PDPatternContentStream
parameter_list|(
name|PDTilingPattern
name|pattern
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|pattern
operator|.
name|getContentStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
argument_list|,
name|pattern
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

