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
operator|.
name|pagedrawer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Point2D
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
name|util
operator|.
name|PDFOperator
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
name|operator
operator|.
name|OperatorProcessor
import|;
end_import

begin_comment
comment|/**  * m Begins a new subpath.  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MoveTo
extends|extends
name|OperatorProcessor
block|{
annotation|@
name|Override
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
name|operands
parameter_list|)
block|{
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|COSNumber
name|x
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSNumber
name|y
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Point2D
name|pos
init|=
name|drawer
operator|.
name|transformedPoint
argument_list|(
name|x
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|y
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
name|drawer
operator|.
name|getLinePath
argument_list|()
operator|.
name|moveTo
argument_list|(
operator|(
name|float
operator|)
name|pos
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|pos
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

