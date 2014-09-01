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
package|;
end_package

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
name|TexturePaint
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
name|Point2D
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
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|RoundingMode
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
name|rendering
operator|.
name|PDFRenderer
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
name|rendering
operator|.
name|TilingPatternDrawer
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
comment|/**  * AWT Paint for a tiling pattern, which consists of a small repeating graphical  * figure.  *  * @author Andreas Lehmkühler  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|TilingPaint
extends|extends
name|TexturePaint
block|{
comment|/**      * Creates a new colored tiling Paint.      *      * @param pattern tiling pattern dictionary      */
specifier|public
name|TilingPaint
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|getImage
argument_list|(
name|renderer
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
argument_list|,
name|getTransformedRect
argument_list|(
name|pattern
argument_list|,
name|matrix
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new uncolored tiling Paint.      *      * @param pattern tiling pattern dictionary      * @param colorSpace color space for this tiling      * @param color color for this tiling      */
specifier|public
name|TilingPaint
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|,
name|PDColorSpace
name|colorSpace
parameter_list|,
name|PDColor
name|color
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|getImage
argument_list|(
name|renderer
argument_list|,
name|pattern
argument_list|,
name|colorSpace
argument_list|,
name|color
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
argument_list|,
name|getTransformedRect
argument_list|(
name|pattern
argument_list|,
name|matrix
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//  gets rect in parent content stream coordinates
specifier|private
specifier|static
name|Rectangle2D
name|getTransformedRect
parameter_list|(
name|PDTilingPattern
name|pattern
parameter_list|,
name|Matrix
name|matrix
parameter_list|)
block|{
name|float
name|x
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|width
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|height
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
comment|// xStep and yStep, but ignore 32767 steps
if|if
condition|(
name|pattern
operator|.
name|getXStep
argument_list|()
operator|!=
literal|0
operator|&&
name|pattern
operator|.
name|getXStep
argument_list|()
operator|!=
name|Short
operator|.
name|MAX_VALUE
condition|)
block|{
name|width
operator|=
name|pattern
operator|.
name|getXStep
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pattern
operator|.
name|getYStep
argument_list|()
operator|!=
literal|0
operator|&&
name|pattern
operator|.
name|getYStep
argument_list|()
operator|!=
name|Short
operator|.
name|MAX_VALUE
condition|)
block|{
name|height
operator|=
name|pattern
operator|.
name|getYStep
argument_list|()
expr_stmt|;
block|}
name|Rectangle2D
name|rectangle
decl_stmt|;
name|AffineTransform
name|at
init|=
name|matrix
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|Point2D
name|p1
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|Point2D
name|p2
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
operator|+
name|width
argument_list|,
name|y
operator|+
name|height
argument_list|)
decl_stmt|;
name|at
operator|.
name|transform
argument_list|(
name|p1
argument_list|,
name|p1
argument_list|)
expr_stmt|;
name|at
operator|.
name|transform
argument_list|(
name|p2
argument_list|,
name|p2
argument_list|)
expr_stmt|;
comment|// at.createTransformedShape(rect).getBounds2D() gets empty rectangle
comment|// when negative numbers, so we do it the hard way
name|rectangle
operator|=
operator|new
name|Rectangle2D
operator|.
name|Float
argument_list|(
operator|(
name|float
operator|)
name|Math
operator|.
name|min
argument_list|(
name|p1
operator|.
name|getX
argument_list|()
argument_list|,
name|p2
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|float
operator|)
name|Math
operator|.
name|min
argument_list|(
name|p1
operator|.
name|getY
argument_list|()
argument_list|,
name|p2
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|,
operator|(
name|float
operator|)
name|Math
operator|.
name|abs
argument_list|(
name|p2
operator|.
name|getX
argument_list|()
operator|-
name|p1
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|float
operator|)
name|Math
operator|.
name|abs
argument_list|(
name|p2
operator|.
name|getY
argument_list|()
operator|-
name|p1
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|rectangle
return|;
block|}
comment|// get lower left coord of bbox
specifier|private
specifier|static
name|Point2D
name|getTransformedPoint
parameter_list|(
name|PDTilingPattern
name|pattern
parameter_list|,
name|Matrix
name|matrix
parameter_list|)
block|{
name|float
name|x
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|width
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|height
init|=
name|pattern
operator|.
name|getBBox
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|AffineTransform
name|at
init|=
name|matrix
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|Point2D
name|p1
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|Point2D
name|p2
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
operator|+
name|width
argument_list|,
name|y
operator|+
name|height
argument_list|)
decl_stmt|;
name|at
operator|.
name|transform
argument_list|(
name|p1
argument_list|,
name|p1
argument_list|)
expr_stmt|;
name|at
operator|.
name|transform
argument_list|(
name|p2
argument_list|,
name|p2
argument_list|)
expr_stmt|;
return|return
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
operator|(
name|float
operator|)
name|Math
operator|.
name|min
argument_list|(
name|p1
operator|.
name|getX
argument_list|()
argument_list|,
name|p2
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|float
operator|)
name|Math
operator|.
name|min
argument_list|(
name|p1
operator|.
name|getY
argument_list|()
argument_list|,
name|p2
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|// gets image in parent stream coordinates
specifier|private
specifier|static
name|BufferedImage
name|getImage
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|,
name|PDColorSpace
name|colorSpace
parameter_list|,
name|PDColor
name|color
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
throws|throws
name|IOException
block|{
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
name|ColorModel
name|cm
init|=
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
decl_stmt|;
name|Rectangle2D
name|rect
init|=
name|getTransformedRect
argument_list|(
name|pattern
argument_list|,
name|matrix
argument_list|)
decl_stmt|;
name|float
name|width
init|=
operator|(
name|float
operator|)
name|rect
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|height
init|=
operator|(
name|float
operator|)
name|rect
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|rasterWidth
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|ceiling
argument_list|(
name|width
operator|*
name|Math
operator|.
name|abs
argument_list|(
name|xform
operator|.
name|getScaleX
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|rasterHeight
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|ceiling
argument_list|(
name|height
operator|*
name|Math
operator|.
name|abs
argument_list|(
name|xform
operator|.
name|getScaleY
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// create raster
name|WritableRaster
name|raster
init|=
name|cm
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|rasterWidth
argument_list|,
name|rasterHeight
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|raster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|matrix
operator|=
name|matrix
operator|.
name|clone
argument_list|()
expr_stmt|;
name|Point2D
name|p
init|=
name|getTransformedPoint
argument_list|(
name|pattern
argument_list|,
name|matrix
argument_list|)
decl_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
name|matrix
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
operator|-
operator|(
name|float
operator|)
name|p
operator|.
name|getX
argument_list|()
argument_list|)
expr_stmt|;
comment|// tx
name|matrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|matrix
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
operator|-
operator|(
name|float
operator|)
name|p
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
comment|// ty
comment|// TODO: need to make it easy to use a custom TilingPatternDrawer
name|PageDrawer
name|drawer
init|=
operator|new
name|TilingPatternDrawer
argument_list|(
name|renderer
argument_list|)
decl_stmt|;
name|PDRectangle
name|pdRect
init|=
operator|new
name|PDRectangle
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
decl_stmt|;
name|Graphics2D
name|graphics
init|=
name|image
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
comment|// transform without the translation
name|AffineTransform
name|at
init|=
operator|new
name|AffineTransform
argument_list|(
name|xform
operator|.
name|getScaleX
argument_list|()
argument_list|,
name|xform
operator|.
name|getShearY
argument_list|()
argument_list|,
operator|-
name|xform
operator|.
name|getShearX
argument_list|()
argument_list|,
name|xform
operator|.
name|getScaleY
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|graphics
operator|.
name|transform
argument_list|(
name|at
argument_list|)
expr_stmt|;
name|drawer
operator|.
name|drawTilingPattern
argument_list|(
name|graphics
argument_list|,
name|pattern
argument_list|,
name|pdRect
argument_list|,
name|matrix
argument_list|,
name|colorSpace
argument_list|,
name|color
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|image
return|;
block|}
comment|/**      * Returns the closest integer which is larger than the given number.      * Uses BigDecimal to avoid floating point error which would cause gaps in the tiling.      */
specifier|private
specifier|static
name|int
name|ceiling
parameter_list|(
name|double
name|num
parameter_list|)
block|{
name|BigDecimal
name|decimal
init|=
operator|new
name|BigDecimal
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|decimal
operator|.
name|setScale
argument_list|(
literal|5
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
expr_stmt|;
comment|// 5 decimal places of accuracy
return|return
name|decimal
operator|.
name|intValue
argument_list|()
return|;
block|}
annotation|@
name|Override
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
block|}
end_class

end_unit

