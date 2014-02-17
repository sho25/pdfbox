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
name|pdmodel
operator|.
name|common
operator|.
name|function
operator|.
name|PDFunction
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
name|color
operator|.
name|PDDeviceN
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
name|PDDeviceRGB
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
name|PDSeparation
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
comment|/**  *  * Helper class that Type4ShadingContext and Type5ShadingContext must extend;  * does the shading of Gouraud triangles.  *  * @author lehmi  * @author Tilman Hausherr  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|GouraudShadingContext
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
specifier|private
name|ColorModel
name|outputColorModel
decl_stmt|;
specifier|private
name|PDColorSpace
name|colorSpace
decl_stmt|;
comment|/**      * number of color components.      */
specifier|protected
name|int
name|numberOfColorComponents
decl_stmt|;
comment|/**      * triangle list.      */
specifier|protected
name|ArrayList
argument_list|<
name|GouraudTriangle
argument_list|>
name|triangleList
decl_stmt|;
comment|/**      * bits per coordinate.      */
specifier|protected
name|int
name|bitsPerCoordinate
decl_stmt|;
comment|/**      * bits per color component.      */
specifier|protected
name|int
name|bitsPerColorComponent
decl_stmt|;
comment|/**      * background values.      */
specifier|protected
name|float
index|[]
name|background
decl_stmt|;
specifier|private
name|ColorSpace
name|shadingColorSpace
decl_stmt|;
specifier|private
name|PDFunction
name|shadingTinttransform
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|hasFunction
decl_stmt|;
specifier|private
specifier|final
name|PDShadingResources
name|gouraudShadingType
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shadingType the shading type to be used      * @param colorModelValue the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @throws IOException if something went wrong      *      */
specifier|protected
name|GouraudShadingContext
parameter_list|(
name|PDShadingResources
name|shadingType
parameter_list|,
name|ColorModel
name|colorModelValue
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|int
name|pageHeight
parameter_list|)
throws|throws
name|IOException
block|{
name|gouraudShadingType
operator|=
name|shadingType
expr_stmt|;
name|triangleList
operator|=
operator|new
name|ArrayList
argument_list|<
name|GouraudTriangle
argument_list|>
argument_list|()
expr_stmt|;
name|colorSpace
operator|=
name|shadingType
operator|.
name|getColorSpace
argument_list|()
expr_stmt|;
name|hasFunction
operator|=
name|shadingType
operator|.
name|getFunction
argument_list|()
operator|!=
literal|null
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"colorSpace: "
operator|+
name|colorSpace
argument_list|)
expr_stmt|;
name|numberOfColorComponents
operator|=
name|colorSpace
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"BBox: "
operator|+
name|shadingType
operator|.
name|getBBox
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Background: "
operator|+
name|shadingType
operator|.
name|getBackground
argument_list|()
argument_list|)
expr_stmt|;
comment|// create the output colormodel using RGB+alpha as colorspace
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
comment|// get the shading colorSpace
try|try
block|{
if|if
condition|(
operator|!
operator|(
name|colorSpace
operator|instanceof
name|PDDeviceRGB
operator|)
condition|)
block|{
comment|// we have to create an instance of the shading colorspace if it isn't RGB
name|shadingColorSpace
operator|=
name|colorSpace
operator|.
name|getJavaColorSpace
argument_list|()
expr_stmt|;
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDDeviceN
condition|)
block|{
name|shadingTinttransform
operator|=
operator|(
operator|(
name|PDDeviceN
operator|)
name|colorSpace
operator|)
operator|.
name|getTintTransform
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|PDSeparation
condition|)
block|{
name|shadingTinttransform
operator|=
operator|(
operator|(
name|PDSeparation
operator|)
name|colorSpace
operator|)
operator|.
name|getTintTransform
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while creating colorSpace"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Read a vertex from the bit input stream and do the interpolations      * described in the PDF specification.      *      * @param input bit input stream      * @param flag the flag or any value if not relevant      * @param maxSrcCoord max value for source coordinate (2^bits-1)      * @param maxSrcColor max value for source color (2^bits-1)      * @param rangeX dest range for X      * @param rangeY dest range for Y      * @param colRangeTab dest range array for colors      *      * @return a new vertex with the flag and the interpolated values      *      * @throws IOException if something went wrong      */
specifier|protected
name|Vertex
name|readVertex
parameter_list|(
name|ImageInputStream
name|input
parameter_list|,
name|byte
name|flag
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
name|flag
argument_list|,
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|dstX
argument_list|,
name|dstY
argument_list|)
argument_list|,
name|colorComponentTab
argument_list|)
return|;
block|}
comment|/**      * transform a list of vertices from shading to user space (if applicable)      * and from user to device space.      *      * @param vertexList list of vertices      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      */
specifier|protected
name|void
name|transformVertices
parameter_list|(
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
name|vertexList
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|int
name|pageHeight
parameter_list|)
block|{
for|for
control|(
name|Vertex
name|v
range|:
name|vertexList
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|v
argument_list|)
expr_stmt|;
comment|// this segment "inspired" by RadialShadingContext
if|if
condition|(
name|ctm
operator|!=
literal|null
condition|)
block|{
comment|// transform from shading to user space
name|ctm
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|transform
argument_list|(
name|v
operator|.
name|point
argument_list|,
name|v
operator|.
name|point
argument_list|)
expr_stmt|;
comment|// transform from user to device space
name|xform
operator|.
name|transform
argument_list|(
name|v
operator|.
name|point
argument_list|,
name|v
operator|.
name|point
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the shading is used as pattern colorspace in combination
comment|// with a fill-, stroke- or showText-operator
comment|// move the 0,0-reference including the y-translation from user to device space
name|v
operator|.
name|point
operator|=
operator|new
name|Point
operator|.
name|Double
argument_list|(
name|v
operator|.
name|point
operator|.
name|getX
argument_list|()
argument_list|,
name|pageHeight
operator|+
name|xform
operator|.
name|getTranslateY
argument_list|()
operator|-
name|v
operator|.
name|point
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
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
name|colorSpace
operator|=
literal|null
expr_stmt|;
name|shadingColorSpace
operator|=
literal|null
expr_stmt|;
name|shadingTinttransform
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
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
comment|/**      * Calculate the interpolation, see p.345 pdf spec 1.7.      *      * @param src src value      * @param srcMax max src value (2^bits-1)      * @param dstMin min dst value      * @param dstMax max dst value      * @return interpolated value      */
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
comment|/**      * {@inheritDoc}      */
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
name|Point2D
name|p
init|=
operator|new
name|Point
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
name|GouraudTriangle
name|triangle
init|=
literal|null
decl_stmt|;
for|for
control|(
name|GouraudTriangle
name|tryTriangle
range|:
name|triangleList
control|)
block|{
if|if
condition|(
name|tryTriangle
operator|.
name|contains
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|triangle
operator|=
name|tryTriangle
expr_stmt|;
break|break;
block|}
block|}
name|float
index|[]
name|values
decl_stmt|;
if|if
condition|(
name|triangle
operator|!=
literal|null
condition|)
block|{
name|double
index|[]
name|weights
init|=
name|triangle
operator|.
name|getWeights
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|values
operator|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfColorComponents
condition|;
operator|++
name|i
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
call|(
name|float
call|)
argument_list|(
name|triangle
operator|.
name|colorA
index|[
name|i
index|]
operator|*
name|weights
index|[
literal|0
index|]
operator|+
name|triangle
operator|.
name|colorB
index|[
name|i
index|]
operator|*
name|weights
index|[
literal|1
index|]
operator|+
name|triangle
operator|.
name|colorC
index|[
name|i
index|]
operator|*
name|weights
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
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
name|values
operator|=
name|background
expr_stmt|;
block|}
else|else
block|{
continue|continue;
block|}
block|}
if|if
condition|(
name|hasFunction
condition|)
block|{
try|try
block|{
name|values
operator|=
name|gouraudShadingType
operator|.
name|evalFunction
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while processing a function"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|// convert color values from shading colorspace to RGB
if|if
condition|(
name|shadingColorSpace
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|shadingTinttransform
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|values
operator|=
name|shadingTinttransform
operator|.
name|eval
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while processing a function"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
name|values
operator|=
name|shadingColorSpace
operator|.
name|toRGB
argument_list|(
name|values
argument_list|)
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
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|0
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|1
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|1
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|data
index|[
name|index
operator|+
literal|2
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|values
index|[
literal|2
index|]
operator|*
literal|255
argument_list|)
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

