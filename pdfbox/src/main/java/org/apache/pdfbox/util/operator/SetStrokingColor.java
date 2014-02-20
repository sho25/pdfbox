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
operator|.
name|operator
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
name|graphics
operator|.
name|color
operator|.
name|PDColor
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
name|PDColorSpace
import|;
end_import

begin_comment
comment|/**  * SC,SCN Sets the colour to use for stroking stroking operations.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|SetStrokingColor
extends|extends
name|SetColor
block|{
comment|/**      * Returns the stroking color.      * @return The stroking color.      */
specifier|protected
name|PDColor
name|getColor
parameter_list|()
block|{
return|return
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
return|;
block|}
comment|/**      * Sets the stroking color.      * @param color The new stroking color.      */
specifier|protected
name|void
name|setColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
block|{
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|setStrokingColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the stroking color space.      * @return The stroking color space.      */
specifier|protected
name|PDColorSpace
name|getColorSpace
parameter_list|()
block|{
return|return
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColorSpace
argument_list|()
return|;
block|}
block|}
end_class

end_unit

