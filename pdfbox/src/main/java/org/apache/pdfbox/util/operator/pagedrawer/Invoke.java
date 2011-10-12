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
name|PDPage
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
name|graphics
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
name|pdmodel
operator|.
name|graphics
operator|.
name|xobject
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
name|xobject
operator|.
name|PDXObjectForm
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
name|xobject
operator|.
name|PDXObjectImage
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
comment|/**  * Implementation of content stream operator for page drawer.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|Invoke
extends|extends
name|OperatorProcessor
block|{
comment|/**      * Log instance.      */
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
comment|/**      * process : Do : Paint the specified XObject (section 4.7).      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If there is an error invoking the sub object.      */
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
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|PDPage
name|page
init|=
name|drawer
operator|.
name|getPage
argument_list|()
decl_stmt|;
name|COSName
name|objectName
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
operator|(
name|PDXObject
operator|)
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
literal|"Can't find the XObject for '"
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
name|PDXObjectImage
condition|)
block|{
name|PDXObjectImage
name|image
init|=
operator|(
name|PDXObjectImage
operator|)
name|xobject
decl_stmt|;
try|try
block|{
if|if
condition|(
name|image
operator|.
name|getImageMask
argument_list|()
condition|)
block|{
comment|// set the current non stroking colorstate, so that it can
comment|// be used to create a stencil masked image
name|image
operator|.
name|setStencilColor
argument_list|(
name|drawer
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|BufferedImage
name|awtImage
init|=
name|image
operator|.
name|getRGBImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|awtImage
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"getRGBImage returned NULL"
argument_list|)
expr_stmt|;
return|return;
comment|//TODO PKOCH
block|}
name|int
name|imageWidth
init|=
name|awtImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|imageHeight
init|=
name|awtImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|double
name|pageHeight
init|=
name|drawer
operator|.
name|getPageSize
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"imageWidth: "
operator|+
name|imageWidth
operator|+
literal|"\t\timageHeight: "
operator|+
name|imageHeight
argument_list|)
expr_stmt|;
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
name|float
name|yScaling
init|=
name|ctm
operator|.
name|getYScale
argument_list|()
decl_stmt|;
name|float
name|angle
init|=
operator|(
name|float
operator|)
name|Math
operator|.
name|acos
argument_list|(
name|ctm
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
operator|/
name|ctm
operator|.
name|getXScale
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctm
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|<
literal|0
operator|&&
name|ctm
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
operator|>
literal|0
condition|)
block|{
name|angle
operator|=
operator|(
operator|-
literal|1
operator|)
operator|*
name|angle
expr_stmt|;
block|}
name|ctm
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
call|(
name|float
call|)
argument_list|(
name|pageHeight
operator|-
name|ctm
operator|.
name|getYPosition
argument_list|()
operator|-
name|Math
operator|.
name|cos
argument_list|(
name|angle
argument_list|)
operator|*
name|yScaling
argument_list|)
argument_list|)
expr_stmt|;
name|ctm
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
call|(
name|float
call|)
argument_list|(
name|ctm
operator|.
name|getXPosition
argument_list|()
operator|-
name|Math
operator|.
name|sin
argument_list|(
name|angle
argument_list|)
operator|*
name|yScaling
argument_list|)
argument_list|)
expr_stmt|;
comment|// because of the moved 0,0-reference, we have to shear in the opposite direction
name|ctm
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|(
operator|-
literal|1
operator|)
operator|*
name|ctm
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ctm
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
operator|(
operator|-
literal|1
operator|)
operator|*
name|ctm
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|AffineTransform
name|ctmAT
init|=
name|ctm
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|ctmAT
operator|.
name|scale
argument_list|(
literal|1f
operator|/
name|imageWidth
argument_list|,
literal|1f
operator|/
name|imageHeight
argument_list|)
expr_stmt|;
name|drawer
operator|.
name|drawImage
argument_list|(
name|awtImage
argument_list|,
name|ctmAT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
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
name|PDXObjectForm
condition|)
block|{
comment|// save the graphics state
name|context
operator|.
name|getGraphicsStack
argument_list|()
operator|.
name|push
argument_list|(
operator|(
name|PDGraphicsState
operator|)
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
name|PDXObjectForm
name|form
init|=
operator|(
name|PDXObjectForm
operator|)
name|xobject
decl_stmt|;
name|COSStream
name|invoke
init|=
operator|(
name|COSStream
operator|)
name|form
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|PDResources
name|pdResources
init|=
name|form
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|pdResources
operator|==
literal|null
condition|)
block|{
name|pdResources
operator|=
name|page
operator|.
name|findResources
argument_list|()
expr_stmt|;
block|}
comment|// if there is an optional form matrix, we have to
comment|// map the form space to the user space
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
name|getContext
argument_list|()
operator|.
name|processSubStream
argument_list|(
name|page
argument_list|,
name|pdResources
argument_list|,
name|invoke
argument_list|)
expr_stmt|;
comment|// restore the graphics state
name|context
operator|.
name|setGraphicsState
argument_list|(
operator|(
name|PDGraphicsState
operator|)
name|context
operator|.
name|getGraphicsStack
argument_list|()
operator|.
name|pop
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

