begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * Intermediate class extended by the shading types 4,5,6 and 7 that contains the common methods  * used by these classes.  *  * @author Shaola Ren  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|abstract
class|class
name|TriangleBasedShadingContext
extends|extends
name|ShadingContext
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
name|TriangleBasedShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|int
name|bitsPerCoordinate
decl_stmt|;
specifier|protected
name|int
name|bitsPerColorComponent
decl_stmt|;
specifier|protected
name|int
name|numberOfColorComponents
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|hasFunction
decl_stmt|;
comment|// map of pixels within triangles to their RGB color
specifier|private
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|pixelTable
decl_stmt|;
comment|/**      * Constructor.      *      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @throws IOException if there is an error getting the color space or doing background color conversion.      */
name|TriangleBasedShadingContext
parameter_list|(
name|PDShading
name|shading
parameter_list|,
name|ColorModel
name|cm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|matrix
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|shading
argument_list|,
name|cm
argument_list|,
name|xform
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
name|PDTriangleBasedShadingType
name|triangleBasedShadingType
init|=
operator|(
name|PDTriangleBasedShadingType
operator|)
name|shading
decl_stmt|;
name|hasFunction
operator|=
name|shading
operator|.
name|getFunction
argument_list|()
operator|!=
literal|null
expr_stmt|;
name|bitsPerCoordinate
operator|=
name|triangleBasedShadingType
operator|.
name|getBitsPerCoordinate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerCoordinate: "
operator|+
operator|(
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bitsPerCoordinate
argument_list|)
operator|-
literal|1
operator|)
argument_list|)
expr_stmt|;
name|bitsPerColorComponent
operator|=
name|triangleBasedShadingType
operator|.
name|getBitsPerComponent
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerColorComponent: "
operator|+
name|bitsPerColorComponent
argument_list|)
expr_stmt|;
name|numberOfColorComponents
operator|=
name|hasFunction
condition|?
literal|1
else|:
name|getShadingColorSpace
argument_list|()
operator|.
name|getNumberOfComponents
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"numberOfColorComponents: "
operator|+
name|numberOfColorComponents
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the pixel table.      */
specifier|protected
specifier|final
name|void
name|createPixelTable
parameter_list|(
name|Rectangle
name|deviceBounds
parameter_list|)
throws|throws
name|IOException
block|{
name|pixelTable
operator|=
name|calcPixelTable
argument_list|(
name|deviceBounds
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calculate every point and its color and store them in a Hash table.      *      * @return a Hash table which contains all the points' positions and colors of one image      */
specifier|abstract
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|calcPixelTable
parameter_list|(
name|Rectangle
name|deviceBounds
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Get the points from the triangles, calculate their color and add point-color mappings.      */
specifier|protected
name|void
name|calcPixelTable
parameter_list|(
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|triangleList
parameter_list|,
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|,
name|Rectangle
name|deviceBounds
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|ShadedTriangle
name|tri
range|:
name|triangleList
control|)
block|{
name|int
name|degree
init|=
name|tri
operator|.
name|getDeg
argument_list|()
decl_stmt|;
if|if
condition|(
name|degree
operator|==
literal|2
condition|)
block|{
name|Line
name|line
init|=
name|tri
operator|.
name|getLine
argument_list|()
decl_stmt|;
for|for
control|(
name|Point
name|p
range|:
name|line
operator|.
name|linePoints
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|evalFunctionAndConvertToRGB
argument_list|(
name|line
operator|.
name|calcColor
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|int
index|[]
name|boundary
init|=
name|tri
operator|.
name|getBoundary
argument_list|()
decl_stmt|;
name|boundary
index|[
literal|0
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|boundary
index|[
literal|0
index|]
argument_list|,
name|deviceBounds
operator|.
name|x
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|1
index|]
operator|=
name|Math
operator|.
name|min
argument_list|(
name|boundary
index|[
literal|1
index|]
argument_list|,
name|deviceBounds
operator|.
name|x
operator|+
name|deviceBounds
operator|.
name|width
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|2
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|boundary
index|[
literal|2
index|]
argument_list|,
name|deviceBounds
operator|.
name|y
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|min
argument_list|(
name|boundary
index|[
literal|3
index|]
argument_list|,
name|deviceBounds
operator|.
name|y
operator|+
name|deviceBounds
operator|.
name|height
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
name|boundary
index|[
literal|0
index|]
init|;
name|x
operator|<=
name|boundary
index|[
literal|1
index|]
condition|;
name|x
operator|++
control|)
block|{
for|for
control|(
name|int
name|y
init|=
name|boundary
index|[
literal|2
index|]
init|;
name|y
operator|<=
name|boundary
index|[
literal|3
index|]
condition|;
name|y
operator|++
control|)
block|{
name|Point
name|p
init|=
operator|new
name|IntPoint
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
if|if
condition|(
name|tri
operator|.
name|contains
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|evalFunctionAndConvertToRGB
argument_list|(
name|tri
operator|.
name|calcColor
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// "fatten" triangle by drawing the borders with Bresenham's line algorithm
comment|// Inspiration: Raph Levien in http://bugs.ghostscript.com/show_bug.cgi?id=219588
name|Point
name|p0
init|=
operator|new
name|IntPoint
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|0
index|]
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|0
index|]
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Point
name|p1
init|=
operator|new
name|IntPoint
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|1
index|]
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|1
index|]
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Point
name|p2
init|=
operator|new
name|IntPoint
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|2
index|]
operator|.
name|getX
argument_list|()
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|tri
operator|.
name|corner
index|[
literal|2
index|]
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Line
name|l1
init|=
operator|new
name|Line
argument_list|(
name|p0
argument_list|,
name|p1
argument_list|,
name|tri
operator|.
name|color
index|[
literal|0
index|]
argument_list|,
name|tri
operator|.
name|color
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|Line
name|l2
init|=
operator|new
name|Line
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|,
name|tri
operator|.
name|color
index|[
literal|1
index|]
argument_list|,
name|tri
operator|.
name|color
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|Line
name|l3
init|=
operator|new
name|Line
argument_list|(
name|p2
argument_list|,
name|p0
argument_list|,
name|tri
operator|.
name|color
index|[
literal|2
index|]
argument_list|,
name|tri
operator|.
name|color
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|Point
name|p
range|:
name|l1
operator|.
name|linePoints
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|evalFunctionAndConvertToRGB
argument_list|(
name|l1
operator|.
name|calcColor
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Point
name|p
range|:
name|l2
operator|.
name|linePoints
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|evalFunctionAndConvertToRGB
argument_list|(
name|l2
operator|.
name|calcColor
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Point
name|p
range|:
name|l3
operator|.
name|linePoints
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|evalFunctionAndConvertToRGB
argument_list|(
name|l3
operator|.
name|calcColor
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Convert color to RGB color value, using function if required, then convert from the shading      * color space to an RGB value, which is encoded into an integer.      */
specifier|private
name|int
name|evalFunctionAndConvertToRGB
parameter_list|(
name|float
index|[]
name|values
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|hasFunction
condition|)
block|{
name|values
operator|=
name|getShading
argument_list|()
operator|.
name|evalFunction
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
return|return
name|convertToRGB
argument_list|(
name|values
argument_list|)
return|;
block|}
comment|/**      * Returns true if the shading has an empty data stream.      */
specifier|abstract
name|boolean
name|isDataEmpty
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|final
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|super
operator|.
name|getColorModel
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|super
operator|.
name|dispose
argument_list|()
expr_stmt|;
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
name|isDataEmpty
argument_list|()
operator|||
name|getBackground
argument_list|()
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
name|Point
name|p
init|=
operator|new
name|IntPoint
argument_list|(
name|x
operator|+
name|col
argument_list|,
name|y
operator|+
name|row
argument_list|)
decl_stmt|;
name|int
name|value
decl_stmt|;
name|Integer
name|v
init|=
name|pixelTable
operator|.
name|get
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|getBackground
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|value
operator|=
name|getRgbBackground
argument_list|()
expr_stmt|;
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

