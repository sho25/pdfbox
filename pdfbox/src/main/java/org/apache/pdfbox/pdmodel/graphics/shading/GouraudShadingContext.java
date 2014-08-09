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
name|PaintContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Point
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
name|Raster
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|ImageInputStream
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
name|COSArray
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
name|PDRange
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
comment|/**  * Shades Gouraud triangles for Type4ShadingContext and Type5ShadingContext.  * @author Andreas Lehmkühler  * @author Tilman Hausherr  * @author Shaola Ren  */
end_comment

begin_class
specifier|abstract
class|class
name|GouraudShadingContext
extends|extends
name|TriangleBasedShadingContext
implements|implements
name|PaintContext
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
name|GouraudShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** number of color components. */
specifier|protected
name|int
name|numberOfColorComponents
decl_stmt|;
comment|/** triangle list. */
specifier|protected
name|ArrayList
argument_list|<
name|ShadedTriangle
argument_list|>
name|triangleList
decl_stmt|;
comment|/** background values.*/
specifier|protected
name|float
index|[]
name|background
decl_stmt|;
specifier|protected
name|int
name|rgbBackground
decl_stmt|;
specifier|protected
name|HashMap
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|pixelTable
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @throws IOException if something went wrong      */
specifier|protected
name|GouraudShadingContext
parameter_list|(
name|PDShading
name|shading
parameter_list|,
name|ColorModel
name|colorModel
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeight
parameter_list|,
name|Rectangle
name|dBounds
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|shading
argument_list|,
name|colorModel
argument_list|,
name|xform
argument_list|,
name|ctm
argument_list|,
name|pageHeight
argument_list|,
name|dBounds
argument_list|)
expr_stmt|;
name|triangleList
operator|=
operator|new
name|ArrayList
argument_list|<
name|ShadedTriangle
argument_list|>
argument_list|()
expr_stmt|;
name|numberOfColorComponents
operator|=
name|hasFunction
condition|?
literal|1
else|:
name|shadingColorSpace
operator|.
name|getNumberOfComponents
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Background: "
operator|+
name|shading
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
name|COSArray
name|bg
init|=
name|shading
operator|.
name|getBackground
argument_list|()
decl_stmt|;
if|if
condition|(
name|bg
operator|!=
literal|null
condition|)
block|{
name|background
operator|=
name|bg
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
name|rgbBackground
operator|=
name|convertToRGB
argument_list|(
name|background
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Read a vertex from the bit input stream performs interpolations.      * @param input bit input stream      * @param flag the flag or any value if not relevant      * @param maxSrcCoord max value for source coordinate (2^bits-1)      * @param maxSrcColor max value for source color (2^bits-1)      * @param rangeX dest range for X      * @param rangeY dest range for Y      * @param colRangeTab dest range array for colors      * @return a new vertex with the flag and the interpolated values      * @throws IOException if something went wrong      */
specifier|protected
name|Vertex
name|readVertex
parameter_list|(
name|ImageInputStream
name|input
parameter_list|,
name|long
name|maxSrcCoord
parameter_list|,
name|long
name|maxSrcColor
parameter_list|,
name|PDRange
name|rangeX
parameter_list|,
name|PDRange
name|rangeY
parameter_list|,
name|PDRange
index|[]
name|colRangeTab
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
throws|throws
name|IOException
block|{
name|float
index|[]
name|colorComponentTab
init|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
name|long
name|x
init|=
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerCoordinate
argument_list|)
decl_stmt|;
name|long
name|y
init|=
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerCoordinate
argument_list|)
decl_stmt|;
name|double
name|dstX
init|=
name|interpolate
argument_list|(
name|x
argument_list|,
name|maxSrcCoord
argument_list|,
name|rangeX
operator|.
name|getMin
argument_list|()
argument_list|,
name|rangeX
operator|.
name|getMax
argument_list|()
argument_list|)
decl_stmt|;
name|double
name|dstY
init|=
name|interpolate
argument_list|(
name|y
argument_list|,
name|maxSrcCoord
argument_list|,
name|rangeY
operator|.
name|getMin
argument_list|()
argument_list|,
name|rangeY
operator|.
name|getMax
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"coord: "
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"[%06X,%06X] -> [%f,%f]"
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|dstX
argument_list|,
name|dstY
argument_list|)
argument_list|)
expr_stmt|;
name|Point2D
name|tmp
init|=
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|dstX
argument_list|,
name|dstY
argument_list|)
decl_stmt|;
name|transformPoint
argument_list|(
name|tmp
argument_list|,
name|ctm
argument_list|,
name|xform
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|numberOfColorComponents
condition|;
operator|++
name|n
control|)
block|{
name|int
name|color
init|=
operator|(
name|int
operator|)
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerColorComponent
argument_list|)
decl_stmt|;
name|colorComponentTab
index|[
name|n
index|]
operator|=
operator|(
name|float
operator|)
name|interpolate
argument_list|(
name|color
argument_list|,
name|maxSrcColor
argument_list|,
name|colRangeTab
index|[
name|n
index|]
operator|.
name|getMin
argument_list|()
argument_list|,
name|colRangeTab
index|[
name|n
index|]
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"color["
operator|+
name|n
operator|+
literal|"]: "
operator|+
name|color
operator|+
literal|"/"
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|color
argument_list|)
operator|+
literal|"-> color["
operator|+
name|n
operator|+
literal|"]: "
operator|+
name|colorComponentTab
index|[
name|n
index|]
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Vertex
argument_list|(
name|tmp
argument_list|,
name|colorComponentTab
argument_list|)
return|;
block|}
specifier|protected
name|HashMap
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|calcPixelTable
parameter_list|()
block|{
name|HashMap
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|super
operator|.
name|calcPixelTable
argument_list|(
name|triangleList
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|triangleList
operator|=
literal|null
expr_stmt|;
name|outputColorModel
operator|=
literal|null
expr_stmt|;
name|shadingColorSpace
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|outputColorModel
return|;
block|}
comment|/**      * Calculate the interpolation, see p.345 pdf spec 1.7.      * @param src src value      * @param srcMax max src value (2^bits-1)      * @param dstMin min dst value      * @param dstMax max dst value      * @return interpolated value      */
specifier|private
name|float
name|interpolate
parameter_list|(
name|float
name|src
parameter_list|,
name|long
name|srcMax
parameter_list|,
name|float
name|dstMin
parameter_list|,
name|float
name|dstMax
parameter_list|)
block|{
return|return
name|dstMin
operator|+
operator|(
name|src
operator|*
operator|(
name|dstMax
operator|-
name|dstMin
operator|)
operator|/
name|srcMax
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
name|Raster
name|getRaster
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|w
parameter_list|,
name|int
name|h
parameter_list|)
block|{
name|WritableRaster
name|raster
init|=
name|getColorModel
argument_list|()
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
decl_stmt|;
name|int
index|[]
name|data
init|=
operator|new
name|int
index|[
name|w
operator|*
name|h
operator|*
literal|4
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|triangleList
operator|.
name|isEmpty
argument_list|()
operator|||
name|background
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|row
init|=
literal|0
init|;
name|row
operator|<
name|h
condition|;
name|row
operator|++
control|)
block|{
name|int
name|currentY
init|=
name|y
operator|+
name|row
decl_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|currentY
argument_list|<
name|minBBoxY
operator|||
name|currentY
argument_list|>
name|maxBBoxY
condition|)
block|{
continue|continue;
block|}
block|}
for|for
control|(
name|int
name|col
init|=
literal|0
init|;
name|col
operator|<
name|w
condition|;
name|col
operator|++
control|)
block|{
name|int
name|currentX
init|=
name|x
operator|+
name|col
decl_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|currentX
argument_list|<
name|minBBoxX
operator|||
name|currentX
argument_list|>
name|maxBBoxX
condition|)
block|{
continue|continue;
block|}
block|}
name|Point
name|p
init|=
operator|new
name|Point
argument_list|(
name|currentX
argument_list|,
name|currentY
argument_list|)
decl_stmt|;
name|int
name|value
decl_stmt|;
if|if
condition|(
name|pixelTable
operator|.
name|containsKey
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|value
operator|=
name|pixelTable
operator|.
name|get
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|background
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|rgbBackground
expr_stmt|;
block|}
else|else
block|{
continue|continue;
block|}
block|}
name|int
name|index
init|=
operator|(
name|row
operator|*
name|w
operator|+
name|col
operator|)
operator|*
literal|4
decl_stmt|;
name|data
index|[
name|index
index|]
operator|=
name|value
operator|&
literal|255
expr_stmt|;
name|value
operator|>>=
literal|8
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|=
name|value
operator|&
literal|255
expr_stmt|;
name|value
operator|>>=
literal|8
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|=
name|value
operator|&
literal|255
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|3
index|]
operator|=
literal|255
expr_stmt|;
block|}
block|}
block|}
name|raster
operator|.
name|setPixels
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|w
argument_list|,
name|h
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return
name|raster
return|;
block|}
block|}
end_class

end_unit

