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
name|pdmodel
operator|.
name|graphics
operator|.
name|shading
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|PaintContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
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
name|geom
operator|.
name|Rectangle2D
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
name|ColorModel
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
name|PDMatrix
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

begin_comment
comment|/**  * This represents the Paint of an radial shading.  *   * @author lehmi  * @version $Revision: $  *   */
end_comment

begin_class
specifier|public
class|class
name|RadialShadingPaint
implements|implements
name|Paint
block|{
specifier|private
name|PDShadingType3
name|shading
decl_stmt|;
specifier|private
name|Matrix
name|currentTransformationMatrix
decl_stmt|;
specifier|private
name|Matrix
name|shadingMatrix
decl_stmt|;
specifier|private
name|int
name|pageHeight
decl_stmt|;
specifier|private
name|float
name|clippingHeight
decl_stmt|;
comment|/**      * Constructor.      *       * @param shadingType3 the shading resources      * @param ctm current transformation matrix      * @param pageSizeValue size of the current page      */
specifier|public
name|RadialShadingPaint
parameter_list|(
name|PDShadingType3
name|shadingType3
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeightValue
parameter_list|,
name|Matrix
name|shMatrix
parameter_list|)
block|{
name|shading
operator|=
name|shadingType3
expr_stmt|;
name|currentTransformationMatrix
operator|=
name|ctm
expr_stmt|;
name|pageHeight
operator|=
name|pageHeightValue
expr_stmt|;
name|shadingMatrix
operator|=
name|shMatrix
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param shadingType3 the shading resources      * @param ctm current transformation matrix      * @param pageSizeValue size of the current page      */
specifier|public
name|RadialShadingPaint
parameter_list|(
name|PDShadingType3
name|shadingType3
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeightValue
parameter_list|,
name|float
name|clipHeight
parameter_list|)
block|{
name|shading
operator|=
name|shadingType3
expr_stmt|;
name|currentTransformationMatrix
operator|=
name|ctm
expr_stmt|;
name|pageHeight
operator|=
name|pageHeightValue
expr_stmt|;
name|clippingHeight
operator|=
name|clipHeight
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getTransparency
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PaintContext
name|createContext
parameter_list|(
name|ColorModel
name|cm
parameter_list|,
name|Rectangle
name|deviceBounds
parameter_list|,
name|Rectangle2D
name|userBounds
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|RenderingHints
name|hints
parameter_list|)
block|{
return|return
operator|new
name|RadialShadingContext
argument_list|(
name|shading
argument_list|,
name|cm
argument_list|,
name|xform
argument_list|,
name|currentTransformationMatrix
argument_list|,
name|pageHeight
argument_list|,
name|shadingMatrix
argument_list|,
name|clippingHeight
argument_list|)
return|;
block|}
block|}
end_class

end_unit

