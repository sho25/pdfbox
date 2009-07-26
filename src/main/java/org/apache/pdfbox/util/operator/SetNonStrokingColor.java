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
name|PDDeviceGray
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
name|PDDeviceRGB
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
name|PDDeviceCMYK
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
name|PDICCBased
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
name|PDCalRGB
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
name|PDSeparation
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
name|util
operator|.
name|PDFOperator
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

begin_comment
comment|/**  *<p>Set the non stroking color space.</p>  *   * @author<a href="mailto:andreas@lehmi.de">Andreas Lehmkühler</a>  * @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|SetNonStrokingColor
extends|extends
name|OperatorProcessor
block|{
comment|/**      * sc,scn Set color space for non stroking operations.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If an error occurs while processing the font.      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
name|PDColorSpace
name|colorSpace
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|colorSpace
operator|!=
literal|null
condition|)
block|{
name|OperatorProcessor
name|newOperator
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDDeviceGray
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingGrayColor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDDeviceRGB
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingRGBColor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDDeviceCMYK
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingCMYKColor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDICCBased
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingICCBasedColor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDCalRGB
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingCalRGBColor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDSeparation
condition|)
block|{
name|newOperator
operator|=
operator|new
name|SetNonStrokingSeparation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|newOperator
operator|!=
literal|null
condition|)
block|{
name|newOperator
operator|.
name|setContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|newOperator
operator|.
name|process
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"Not supported colorspace "
operator|+
name|colorSpace
operator|.
name|getName
argument_list|()
operator|+
literal|" within operator "
operator|+
name|operator
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|logger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"Colorspace not found in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".process!!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

