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
name|cos
operator|.
name|COSArray
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
name|cos
operator|.
name|COSBase
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Sets the color to use for stroking or non-stroking operations.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SetColor
extends|extends
name|OperatorProcessor
block|{
comment|/**      * sc,scn,SC,SCN Sets the colour to use for stroking or non-stroking operations.      * @param operator the operator that is being executed.      * @param arguments list of operands      * @throws IOException if the color space cannot be read      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|addAll
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
name|setColor
argument_list|(
operator|new
name|PDColor
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns either the stroking or non-stroking color value.      * @return The stroking or non-stroking color value.      */
specifier|protected
specifier|abstract
name|PDColor
name|getColor
parameter_list|()
function_decl|;
comment|/**      * Sets either the stroking or non-stroking color value.      * @param color The stroking or non-stroking color value.      */
specifier|protected
specifier|abstract
name|void
name|setColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
function_decl|;
comment|/**      * Returns either the stroking or non-stroking color space.      * @return The stroking or non-stroking color space.      */
specifier|protected
specifier|abstract
name|PDColorSpace
name|getColorSpace
parameter_list|()
function_decl|;
block|}
end_class

end_unit

