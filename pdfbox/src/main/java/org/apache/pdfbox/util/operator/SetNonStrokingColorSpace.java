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
name|cos
operator|.
name|COSName
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
name|PDColorSpaceFactory
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
name|util
operator|.
name|PDFOperator
import|;
end_import

begin_comment
comment|/**  *<p>Set the non stroking color space.</p>  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|SetNonStrokingColorSpace
extends|extends
name|OperatorProcessor
block|{
specifier|private
specifier|static
specifier|final
name|float
index|[]
name|EMPTY_FLOAT_ARRAY
init|=
operator|new
name|float
index|[
literal|0
index|]
decl_stmt|;
comment|/**      * cs Set color space for non stroking operations.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If an error occurs while processing the font.      */
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
comment|//      (PDF 1.1) Set color space for stroking operations
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDColorSpace
name|cs
init|=
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|name
argument_list|,
name|context
operator|.
name|getColorSpaces
argument_list|()
argument_list|)
decl_stmt|;
name|PDColorState
name|colorInstance
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
decl_stmt|;
name|colorInstance
operator|.
name|setColorSpace
argument_list|(
name|cs
argument_list|)
expr_stmt|;
name|int
name|numComponents
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
name|EMPTY_FLOAT_ARRAY
decl_stmt|;
if|if
condition|(
name|numComponents
operator|>=
literal|0
condition|)
block|{
name|values
operator|=
operator|new
name|float
index|[
name|numComponents
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numComponents
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
literal|0f
expr_stmt|;
block|}
if|if
condition|(
name|cs
operator|instanceof
name|PDDeviceCMYK
condition|)
block|{
name|values
index|[
literal|3
index|]
operator|=
literal|1f
expr_stmt|;
block|}
block|}
name|colorInstance
operator|.
name|setColorSpaceValue
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

