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
name|COSNumber
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
name|pdfviewer
operator|.
name|PageDrawer
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
name|PDColorState
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

begin_comment
comment|/**  *   * @author<a href="mailto:andreas@lehmi.de">Andreas Lehmkühler</a>  * @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|SetStrokingICCBasedColor
extends|extends
name|OperatorProcessor
block|{
comment|/**      * scn Set color space for stroking operations.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If an error occurs while processing the font.      */
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
name|PDColorState
name|color
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
decl_stmt|;
name|PDColorSpace
name|cs
init|=
name|color
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|int
name|numberOfComponents
init|=
name|cs
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
name|numberOfComponents
index|]
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
name|numberOfComponents
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
name|color
operator|.
name|setColorSpaceValue
argument_list|(
name|values
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|instanceof
name|PageDrawer
condition|)
block|{
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|drawer
operator|.
name|colorChanged
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

