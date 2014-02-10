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
name|pattern
operator|.
name|tiling
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
name|Transparency
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
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
name|AffineTransformOp
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
name|awt
operator|.
name|image
operator|.
name|ColorModel
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
name|ComponentColorModel
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
name|DataBuffer
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
name|WritableRaster
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
name|pattern
operator|.
name|PDTilingPatternResources
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
comment|/**  * This represents the Paint of an axial shading.  *   */
end_comment

begin_class
specifier|public
class|class
name|ColoredTilingPaint
implements|implements
name|Paint
block|{
specifier|private
name|PDTilingPatternResources
name|patternResources
decl_stmt|;
specifier|private
name|ColorModel
name|outputColorModel
decl_stmt|;
comment|/**      * Constructor.      *       * @param resources tiling pattern resources      *       */
specifier|public
name|ColoredTilingPaint
parameter_list|(
name|PDTilingPatternResources
name|resources
parameter_list|)
block|{
name|patternResources
operator|=
name|resources
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getTransparency
parameter_list|()
block|{
return|return
name|Transparency
operator|.
name|TRANSLUCENT
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
comment|// get the pattern matrix
name|Matrix
name|patternMatrix
init|=
name|patternResources
operator|.
name|getMatrix
argument_list|()
decl_stmt|;
name|AffineTransform
name|patternAT
init|=
name|patternMatrix
operator|!=
literal|null
condition|?
name|patternMatrix
operator|.
name|createAffineTransform
argument_list|()
else|:
literal|null
decl_stmt|;
comment|// get the bounding box
name|PDRectangle
name|box
init|=
name|patternResources
operator|.
name|getBBox
argument_list|()
decl_stmt|;
name|Rectangle2D
name|rect
init|=
operator|new
name|Rectangle
argument_list|(
operator|(
name|int
operator|)
name|box
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|box
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|box
operator|.
name|getWidth
argument_list|()
argument_list|,
operator|(
name|int
operator|)
name|box
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|rect
operator|=
name|xform
operator|.
name|createTransformedShape
argument_list|(
name|rect
argument_list|)
operator|.
name|getBounds2D
argument_list|()
expr_stmt|;
name|int
name|width
init|=
operator|(
name|int
operator|)
name|rect
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
operator|(
name|int
operator|)
name|rect
operator|.
name|getHeight
argument_list|()
decl_stmt|;
if|if
condition|(
name|patternAT
operator|!=
literal|null
condition|)
block|{
name|rect
operator|=
name|patternAT
operator|.
name|createTransformedShape
argument_list|(
name|rect
argument_list|)
operator|.
name|getBounds2D
argument_list|()
expr_stmt|;
block|}
name|PDRectangle
name|bBox
init|=
operator|new
name|PDRectangle
argument_list|(
operator|(
name|float
operator|)
name|rect
operator|.
name|getMinX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|rect
operator|.
name|getMinY
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|rect
operator|.
name|getMaxX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|rect
operator|.
name|getMaxY
argument_list|()
argument_list|)
decl_stmt|;
comment|// xStep + yStep
name|double
index|[]
name|steps
init|=
operator|new
name|double
index|[]
block|{
name|patternResources
operator|.
name|getXStep
argument_list|()
block|,
name|patternResources
operator|.
name|getYStep
argument_list|()
block|}
decl_stmt|;
name|xform
operator|.
name|deltaTransform
argument_list|(
name|steps
argument_list|,
literal|0
argument_list|,
name|steps
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|patternAT
operator|!=
literal|null
condition|)
block|{
name|patternAT
operator|.
name|deltaTransform
argument_list|(
name|steps
argument_list|,
literal|0
argument_list|,
name|steps
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|xStep
init|=
call|(
name|int
call|)
argument_list|(
name|steps
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|yStep
init|=
call|(
name|int
call|)
argument_list|(
name|steps
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|ColorSpace
name|outputCS
init|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
decl_stmt|;
name|outputColorModel
operator|=
operator|new
name|ComponentColorModel
argument_list|(
name|outputCS
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|TRANSLUCENT
argument_list|,
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
expr_stmt|;
name|WritableRaster
name|raster
init|=
name|outputColorModel
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|width
argument_list|,
name|height
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
operator|new
name|BufferedImage
argument_list|(
name|outputColorModel
argument_list|,
name|raster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|BufferedImage
name|tilingImage
init|=
literal|null
decl_stmt|;
try|try
block|{
name|PageDrawer
name|drawer
init|=
operator|new
name|PageDrawer
argument_list|()
decl_stmt|;
name|drawer
operator|.
name|drawStream
argument_list|(
name|image
operator|.
name|getGraphics
argument_list|()
argument_list|,
operator|(
name|COSStream
operator|)
name|patternResources
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|patternResources
operator|.
name|getResources
argument_list|()
argument_list|,
name|box
argument_list|)
expr_stmt|;
name|drawer
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|AffineTransform
name|imageTransform
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|patternAT
operator|!=
literal|null
operator|&&
operator|!
name|patternAT
operator|.
name|isIdentity
argument_list|()
condition|)
block|{
comment|// get the scaling factor for each dimension
name|imageTransform
operator|=
name|AffineTransform
operator|.
name|getScaleInstance
argument_list|(
name|patternMatrix
operator|.
name|getXScale
argument_list|()
argument_list|,
name|patternMatrix
operator|.
name|getYScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|imageTransform
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
block|}
name|imageTransform
operator|.
name|scale
argument_list|(
literal|1.0
argument_list|,
operator|-
literal|1.0
argument_list|)
expr_stmt|;
name|imageTransform
operator|.
name|translate
argument_list|(
literal|0
argument_list|,
operator|-
name|height
argument_list|)
expr_stmt|;
name|AffineTransformOp
name|scaleOP
init|=
operator|new
name|AffineTransformOp
argument_list|(
name|imageTransform
argument_list|,
name|AffineTransformOp
operator|.
name|TYPE_BILINEAR
argument_list|)
decl_stmt|;
name|tilingImage
operator|=
name|scaleOP
operator|.
name|filter
argument_list|(
name|image
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
operator|new
name|ColoredTilingContext
argument_list|(
name|outputColorModel
argument_list|,
name|tilingImage
operator|.
name|getData
argument_list|()
argument_list|,
name|xStep
argument_list|,
name|yStep
argument_list|,
name|bBox
argument_list|)
return|;
block|}
block|}
end_class

end_unit

