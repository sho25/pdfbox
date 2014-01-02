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
name|GeneralPath
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
comment|/**  * Implementation of content stream operator for page drawer.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|AppendRectangleToPath
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : re : append rectangle to path.      * @param operator The operator that is being executed.      * @param arguments List      */
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
name|arguments
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
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|COSNumber
name|w
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|COSNumber
name|h
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|double
name|x1
init|=
name|x
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
name|double
name|y1
init|=
name|y
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
comment|// create a pair of coordinates for the transformation
name|double
name|x2
init|=
name|w
operator|.
name|doubleValue
argument_list|()
operator|+
name|x1
decl_stmt|;
name|double
name|y2
init|=
name|h
operator|.
name|doubleValue
argument_list|()
operator|+
name|y1
decl_stmt|;
name|Point2D
name|startCoords
init|=
name|drawer
operator|.
name|transformedPoint
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|)
decl_stmt|;
name|Point2D
name|endCoords
init|=
name|drawer
operator|.
name|transformedPoint
argument_list|(
name|x2
argument_list|,
name|y2
argument_list|)
decl_stmt|;
name|float
name|width
init|=
call|(
name|float
call|)
argument_list|(
name|endCoords
operator|.
name|getX
argument_list|()
operator|-
name|startCoords
operator|.
name|getX
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|height
init|=
call|(
name|float
call|)
argument_list|(
name|endCoords
operator|.
name|getY
argument_list|()
operator|-
name|startCoords
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|xStart
init|=
operator|(
name|float
operator|)
name|startCoords
operator|.
name|getX
argument_list|()
decl_stmt|;
name|float
name|yStart
init|=
operator|(
name|float
operator|)
name|startCoords
operator|.
name|getY
argument_list|()
decl_stmt|;
comment|// To ensure that the path is created in the right direction,
comment|// we have to create it by combining single lines instead of
comment|// creating a simple rectangle
name|GeneralPath
name|path
init|=
name|drawer
operator|.
name|getLinePath
argument_list|()
decl_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
name|xStart
argument_list|,
name|yStart
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|xStart
operator|+
name|width
argument_list|,
name|yStart
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|xStart
operator|+
name|width
argument_list|,
name|yStart
operator|+
name|height
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|xStart
argument_list|,
name|yStart
operator|+
name|height
argument_list|)
expr_stmt|;
comment|// close the subpath instead of adding the last line
comment|// so that a possible set line cap style isn't taken into account
comment|// at the "beginning" of the rectangle
name|path
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

