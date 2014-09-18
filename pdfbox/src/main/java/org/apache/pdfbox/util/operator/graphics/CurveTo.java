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
name|graphics
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
name|util
operator|.
name|operator
operator|.
name|Operator
import|;
end_import

begin_comment
comment|/**  * c Append curved segment to path.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|CurveTo
extends|extends
name|GraphicsOperatorProcessor
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|operands
parameter_list|)
throws|throws
name|IOException
block|{
name|COSNumber
name|x1
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
name|y1
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
name|COSNumber
name|x2
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|COSNumber
name|y2
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|COSNumber
name|x3
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|COSNumber
name|y3
init|=
operator|(
name|COSNumber
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Point2D
name|point1
init|=
name|context
operator|.
name|transformedPoint
argument_list|(
name|x1
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|y1
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
name|Point2D
name|point2
init|=
name|context
operator|.
name|transformedPoint
argument_list|(
name|x2
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|y2
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
name|Point2D
name|point3
init|=
name|context
operator|.
name|transformedPoint
argument_list|(
name|x3
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|y3
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|curveTo
argument_list|(
operator|(
name|float
operator|)
name|point1
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|point1
operator|.
name|getY
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|point2
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|point2
operator|.
name|getY
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|point3
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|point3
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
end_class

end_unit

