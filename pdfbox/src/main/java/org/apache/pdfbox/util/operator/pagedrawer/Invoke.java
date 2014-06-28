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
name|AffineTransform
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
name|Area
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
name|awt
operator|.
name|image
operator|.
name|BufferedImage
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|cos
operator|.
name|COSStream
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
name|filter
operator|.
name|MissingImageReaderException
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
name|rendering
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
name|PDResources
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
name|common
operator|.
name|PDRectangle
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
name|form
operator|.
name|PDFormXObject
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
name|image
operator|.
name|PDImageXObject
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
name|PDXObject
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
name|state
operator|.
name|PDGraphicsState
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
name|Matrix
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
comment|/**  * Do Draws an XObject.  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Invoke
extends|extends
name|OperatorProcessor
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Invoke
operator|.
name|class
argument_list|)
decl_stmt|;
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
throws|throws
name|IOException
block|{
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|COSName
name|objectName
init|=
operator|(
name|COSName
operator|)
name|operands
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
name|drawer
operator|.
name|getResources
argument_list|()
operator|.
name|getXObjects
argument_list|()
decl_stmt|;
name|PDXObject
name|xobject
init|=
name|xobjects
operator|.
name|get
argument_list|(
name|objectName
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't find the XObject named '"
operator|+
name|objectName
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|PDImageXObject
condition|)
block|{
name|PDImageXObject
name|image
init|=
operator|(
name|PDImageXObject
operator|)
name|xobject
decl_stmt|;
try|try
block|{
name|BufferedImage
name|awtImage
decl_stmt|;
if|if
condition|(
name|image
operator|.
name|isStencil
argument_list|()
condition|)
block|{
name|PDColorSpace
name|colorSpace
init|=
name|drawer
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
decl_stmt|;
name|PDColor
name|color
init|=
name|drawer
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
decl_stmt|;
name|awtImage
operator|=
name|image
operator|.
name|getStencilImage
argument_list|(
name|colorSpace
operator|.
name|toPaint
argument_list|(
name|drawer
operator|.
name|getRenderer
argument_list|()
argument_list|,
name|color
argument_list|)
argument_list|)
expr_stmt|;
comment|//<--- TODO: pass page height?
block|}
else|else
block|{
name|awtImage
operator|=
name|image
operator|.
name|getImage
argument_list|()
expr_stmt|;
block|}
name|Matrix
name|ctm
init|=
name|drawer
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|AffineTransform
name|imageTransform
init|=
name|ctm
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|drawer
operator|.
name|drawImage
argument_list|(
name|awtImage
argument_list|,
name|imageTransform
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingImageReaderException
name|e
parameter_list|)
block|{
comment|// missing ImageIO plug-in  TODO how far should we escalate this? (after all the user can fix the problem)
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO we probably shouldn't catch Exception, what errors are expected here?
name|LOG
operator|.
name|error
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
name|PDFormXObject
name|form
init|=
operator|(
name|PDFormXObject
operator|)
name|xobject
decl_stmt|;
if|if
condition|(
name|form
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
operator|&&
name|COSName
operator|.
name|TRANSPARENCY
operator|.
name|equals
argument_list|(
name|form
operator|.
name|getGroup
argument_list|()
operator|.
name|getSubType
argument_list|()
argument_list|)
condition|)
block|{
name|PageDrawer
operator|.
name|TransparencyGroup
name|group
init|=
name|drawer
operator|.
name|createTransparencyGroup
argument_list|(
name|form
argument_list|)
decl_stmt|;
comment|// draw the result of the transparency group to the page
name|group
operator|.
name|draw
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// save the graphics state
name|context
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|COSStream
name|formContentStream
init|=
name|form
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
comment|// find some optional resources, instead of using the current resources
name|PDResources
name|pdResources
init|=
name|form
operator|.
name|getResources
argument_list|()
decl_stmt|;
comment|// if there is an optional form matrix, we have to map the form space to the user space
name|Matrix
name|matrix
init|=
name|form
operator|.
name|getMatrix
argument_list|()
decl_stmt|;
if|if
condition|(
name|matrix
operator|!=
literal|null
condition|)
block|{
name|Matrix
name|xobjectCTM
init|=
name|matrix
operator|.
name|multiply
argument_list|(
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|setCurrentTransformationMatrix
argument_list|(
name|xobjectCTM
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|form
operator|.
name|getBBox
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|PDGraphicsState
name|graphicsState
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
decl_stmt|;
name|PDRectangle
name|bBox
init|=
name|form
operator|.
name|getBBox
argument_list|()
decl_stmt|;
name|float
name|x1
init|=
name|bBox
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y1
init|=
name|bBox
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|x2
init|=
name|bBox
operator|.
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|y2
init|=
name|bBox
operator|.
name|getUpperRightY
argument_list|()
decl_stmt|;
name|Point2D
name|p0
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
name|p1
init|=
name|drawer
operator|.
name|transformedPoint
argument_list|(
name|x2
argument_list|,
name|y1
argument_list|)
decl_stmt|;
name|Point2D
name|p2
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
name|Point2D
name|p3
init|=
name|drawer
operator|.
name|transformedPoint
argument_list|(
name|x1
argument_list|,
name|y2
argument_list|)
decl_stmt|;
name|GeneralPath
name|bboxPath
init|=
operator|new
name|GeneralPath
argument_list|()
decl_stmt|;
name|bboxPath
operator|.
name|moveTo
argument_list|(
operator|(
name|float
operator|)
name|p0
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p0
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|bboxPath
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p1
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p1
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|bboxPath
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p2
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p2
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|bboxPath
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p3
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p3
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|bboxPath
operator|.
name|closePath
argument_list|()
expr_stmt|;
name|Area
name|resultClippingArea
init|=
operator|new
name|Area
argument_list|(
name|graphicsState
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
decl_stmt|;
name|Area
name|newArea
init|=
operator|new
name|Area
argument_list|(
name|bboxPath
argument_list|)
decl_stmt|;
name|resultClippingArea
operator|.
name|intersect
argument_list|(
name|newArea
argument_list|)
expr_stmt|;
name|graphicsState
operator|.
name|setCurrentClippingPath
argument_list|(
name|resultClippingArea
argument_list|)
expr_stmt|;
block|}
name|getContext
argument_list|()
operator|.
name|processSubStream
argument_list|(
name|pdResources
argument_list|,
name|formContentStream
argument_list|)
expr_stmt|;
comment|// restore the graphics state
name|context
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

