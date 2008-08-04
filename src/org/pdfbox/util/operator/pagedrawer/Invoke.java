begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
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
comment|/**  * Implementation of content stream operator for page drawer.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|Invoke
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : re : append rectangle to path.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If there is an error invoking the sub object.      */
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
name|Dimension
name|pageSize
init|=
name|drawer
operator|.
name|getPageSize
argument_list|()
decl_stmt|;
name|Graphics2D
name|graphics
init|=
name|drawer
operator|.
name|getGraphics
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
name|BufferedImage
name|awtImage
init|=
name|image
operator|.
name|getRGBImage
argument_list|()
decl_stmt|;
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
name|int
name|width
init|=
name|awtImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|awtImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|double
name|rotationInRadians
init|=
operator|(
name|page
operator|.
name|findRotation
argument_list|()
operator|*
name|Math
operator|.
name|PI
operator|)
operator|/
literal|180
decl_stmt|;
name|AffineTransform
name|rotation
init|=
operator|new
name|AffineTransform
argument_list|()
decl_stmt|;
name|rotation
operator|.
name|setToRotation
argument_list|(
name|rotationInRadians
argument_list|)
expr_stmt|;
name|AffineTransform
name|rotationInverse
init|=
name|rotation
operator|.
name|createInverse
argument_list|()
decl_stmt|;
name|Matrix
name|rotationInverseMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|rotationInverseMatrix
operator|.
name|setFromAffineTransform
argument_list|(
name|rotationInverse
argument_list|)
expr_stmt|;
name|Matrix
name|rotationMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|rotationMatrix
operator|.
name|setFromAffineTransform
argument_list|(
name|rotation
argument_list|)
expr_stmt|;
name|Matrix
name|unrotatedCTM
init|=
name|ctm
operator|.
name|multiply
argument_list|(
name|rotationInverseMatrix
argument_list|)
decl_stmt|;
name|Matrix
name|scalingParams
init|=
name|unrotatedCTM
operator|.
name|extractScaling
argument_list|()
decl_stmt|;
name|Matrix
name|scalingMatrix
init|=
name|Matrix
operator|.
name|getScaleInstance
argument_list|(
literal|1f
operator|/
name|width
argument_list|,
literal|1f
operator|/
name|height
argument_list|)
decl_stmt|;
name|scalingParams
operator|=
name|scalingParams
operator|.
name|multiply
argument_list|(
name|scalingMatrix
argument_list|)
expr_stmt|;
name|Matrix
name|translationParams
init|=
name|unrotatedCTM
operator|.
name|extractTranslating
argument_list|()
decl_stmt|;
name|Matrix
name|translationMatrix
init|=
literal|null
decl_stmt|;
name|int
name|pageRotation
init|=
name|page
operator|.
name|findRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|pageRotation
operator|==
literal|0
condition|)
block|{
name|translationParams
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
operator|-
name|translationParams
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|translationMatrix
operator|=
name|Matrix
operator|.
name|getTranslatingInstance
argument_list|(
literal|0
argument_list|,
operator|(
name|float
operator|)
name|pageSize
operator|.
name|getHeight
argument_list|()
operator|-
name|height
operator|*
name|scalingParams
operator|.
name|getYScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pageRotation
operator|==
literal|90
condition|)
block|{
name|translationMatrix
operator|=
name|Matrix
operator|.
name|getTranslatingInstance
argument_list|(
literal|0
argument_list|,
operator|(
name|float
operator|)
name|pageSize
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//TODO need to figure out other cases
block|}
name|translationParams
operator|=
name|translationParams
operator|.
name|multiply
argument_list|(
name|translationMatrix
argument_list|)
expr_stmt|;
name|AffineTransform
name|at
init|=
operator|new
name|AffineTransform
argument_list|(
name|scalingParams
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|scalingParams
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|translationParams
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
argument_list|,
name|translationParams
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
comment|//at.setToTranslation( pageSize.getHeight()-ctm.getValue(2,0),ctm.getValue(2,1) );
comment|//at.setToScale( ctm.getValue(0,0)/width, ctm.getValue(1,1)/height);
comment|//at.setToRotation( (page.findRotation() * Math.PI)/180 );
comment|//AffineTransform rotation = new AffineTransform();
comment|//rotation.rotate( (90*Math.PI)/180);
comment|/*                                  // The transformation should be done                  // 1 - Translation                 // 2 - Rotation                 // 3 - Scale or Skew                 AffineTransform at = new AffineTransform();                  // Translation                 at = new AffineTransform();                 //at.setToTranslation((double)ctm.getValue(0,0),                 //                    (double)ctm.getValue(0,1));                                  // Rotation                 //AffineTransform toAdd = new AffineTransform();                 toAdd.setToRotation(1.5705);                 toAdd.setToRotation(ctm.getValue(2,0)*(Math.PI/180));                 at.concatenate(toAdd);                 */
comment|// Scale / Skew?
comment|//toAdd.setToScale(1, 1);
comment|//at.concatenate(toAdd);
name|graphics
operator|.
name|drawImage
argument_list|(
name|awtImage
argument_list|,
name|at
argument_list|,
literal|null
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
block|}
else|else
block|{
comment|//unknown xobject type
block|}
comment|//invoke named object.
block|}
block|}
end_class

end_unit

