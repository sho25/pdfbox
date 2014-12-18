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
name|EOFException
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
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageInputStream
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
name|cos
operator|.
name|COSDictionary
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
comment|/**  * This class is extended in Type6ShadingContext and Type7ShadingContext. This  * was done as part of GSoC2014, Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
specifier|abstract
class|class
name|PatchMeshesShadingContext
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
name|PatchMeshesShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|float
index|[]
name|background
decl_stmt|;
comment|// background values.
specifier|protected
name|int
name|rgbBackground
decl_stmt|;
specifier|protected
specifier|final
name|PDShading
name|patchMeshesShadingType
decl_stmt|;
comment|// the following fields are not intialized in this abstract class
specifier|protected
name|List
argument_list|<
name|Patch
argument_list|>
name|patchList
decl_stmt|;
comment|// patch list
specifier|protected
name|int
name|bitsPerFlag
decl_stmt|;
comment|// bits per flag
specifier|protected
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|pixelTable
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param dBounds device bounds      * @throws IOException if something went wrong      */
specifier|protected
name|PatchMeshesShadingContext
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
name|dBounds
argument_list|)
expr_stmt|;
name|patchMeshesShadingType
operator|=
name|shading
expr_stmt|;
name|bitsPerFlag
operator|=
operator|(
operator|(
name|PDShadingType6
operator|)
name|shading
operator|)
operator|.
name|getBitsPerFlag
argument_list|()
expr_stmt|;
name|patchList
operator|=
operator|new
name|ArrayList
argument_list|<
name|Patch
argument_list|>
argument_list|()
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
comment|/**      * Create a patch list from a data stream, the returned list contains all      * the patches contained in the data stream.      *      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param cosDictionary dictionary object to give the image information      * @param rangeX range for coordinate x      * @param rangeY range for coordinate y      * @param colRange range for color      * @param numP number of control points, 12 for type 6 shading and 16 for      * type 7 shading      * @return the obtained patch list      * @throws IOException when something went wrong      */
specifier|protected
name|ArrayList
argument_list|<
name|Patch
argument_list|>
name|getPatchList
parameter_list|(
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|COSDictionary
name|cosDictionary
parameter_list|,
name|PDRange
name|rangeX
parameter_list|,
name|PDRange
name|rangeY
parameter_list|,
name|PDRange
index|[]
name|colRange
parameter_list|,
name|int
name|numP
parameter_list|)
throws|throws
name|IOException
block|{
name|ArrayList
argument_list|<
name|Patch
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Patch
argument_list|>
argument_list|()
decl_stmt|;
name|long
name|maxSrcCoord
init|=
operator|(
name|long
operator|)
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
decl_stmt|;
name|long
name|maxSrcColor
init|=
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bitsPerColorComponent
argument_list|)
operator|-
literal|1
decl_stmt|;
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|cosDictionary
decl_stmt|;
name|ImageInputStream
name|mciis
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|cosStream
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|Point2D
index|[]
name|implicitEdge
init|=
operator|new
name|Point2D
index|[
literal|4
index|]
decl_stmt|;
name|float
index|[]
index|[]
name|implicitCornerColor
init|=
operator|new
name|float
index|[
literal|2
index|]
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
name|byte
name|flag
init|=
operator|(
name|byte
operator|)
literal|0
decl_stmt|;
try|try
block|{
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|boolean
name|isFree
init|=
operator|(
name|flag
operator|==
literal|0
operator|)
decl_stmt|;
name|Patch
name|current
init|=
name|readPatch
argument_list|(
name|mciis
argument_list|,
name|isFree
argument_list|,
name|implicitEdge
argument_list|,
name|implicitCornerColor
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
name|ctm
argument_list|,
name|xform
argument_list|,
name|numP
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|list
operator|.
name|add
argument_list|(
operator|(
name|Patch
operator|)
name|current
argument_list|)
expr_stmt|;
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|flag
condition|)
block|{
case|case
literal|0
case|:
break|break;
case|case
literal|1
case|:
name|implicitEdge
operator|=
name|current
operator|.
name|getFlag1Edge
argument_list|()
expr_stmt|;
name|implicitCornerColor
operator|=
name|current
operator|.
name|getFlag1Color
argument_list|()
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|implicitEdge
operator|=
name|current
operator|.
name|getFlag2Edge
argument_list|()
expr_stmt|;
name|implicitCornerColor
operator|=
name|current
operator|.
name|getFlag2Color
argument_list|()
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|implicitEdge
operator|=
name|current
operator|.
name|getFlag3Edge
argument_list|()
expr_stmt|;
name|implicitCornerColor
operator|=
name|current
operator|.
name|getFlag3Color
argument_list|()
expr_stmt|;
break|break;
default|default:
name|LOG
operator|.
name|warn
argument_list|(
literal|"bad flag: "
operator|+
name|flag
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
break|break;
block|}
block|}
name|mciis
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * Read a single patch from a data stream, a patch contains information of      * its coordinates and color parameters.      *      * @param input the image source data stream      * @param isFree whether this is a free patch      * @param implicitEdge implicit edge when a patch is not free, otherwise      * it's not used      * @param implicitCornerColor implicit colors when a patch is not free,      * otherwise it's not used      * @param maxSrcCoord the maximum coordinate value calculated from source      * data      * @param maxSrcColor the maximum color value calculated from source data      * @param rangeX range for coordinate x      * @param rangeY range for coordinate y      * @param colRange range for color      * @param ctm current transformation matrix      * @param xform transformation for user to device space      * @param numP number of control points, 12 for type 6 shading and 16 for      * type 7 shading      * @return a single patch      * @throws IOException when something went wrong      */
specifier|protected
name|Patch
name|readPatch
parameter_list|(
name|ImageInputStream
name|input
parameter_list|,
name|boolean
name|isFree
parameter_list|,
name|Point2D
index|[]
name|implicitEdge
parameter_list|,
name|float
index|[]
index|[]
name|implicitCornerColor
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
name|colRange
parameter_list|,
name|Matrix
name|ctm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|int
name|numP
parameter_list|)
throws|throws
name|IOException
block|{
name|float
index|[]
index|[]
name|color
init|=
operator|new
name|float
index|[
literal|4
index|]
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
name|Point2D
index|[]
name|points
init|=
operator|new
name|Point2D
index|[
name|numP
index|]
decl_stmt|;
name|int
name|pStart
init|=
literal|4
decl_stmt|,
name|cStart
init|=
literal|2
decl_stmt|;
if|if
condition|(
name|isFree
condition|)
block|{
name|pStart
operator|=
literal|0
expr_stmt|;
name|cStart
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|points
index|[
literal|0
index|]
operator|=
name|implicitEdge
index|[
literal|0
index|]
expr_stmt|;
name|points
index|[
literal|1
index|]
operator|=
name|implicitEdge
index|[
literal|1
index|]
expr_stmt|;
name|points
index|[
literal|2
index|]
operator|=
name|implicitEdge
index|[
literal|2
index|]
expr_stmt|;
name|points
index|[
literal|3
index|]
operator|=
name|implicitEdge
index|[
literal|3
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
name|i
operator|++
control|)
block|{
name|color
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
name|implicitCornerColor
index|[
literal|0
index|]
index|[
name|i
index|]
expr_stmt|;
name|color
index|[
literal|1
index|]
index|[
name|i
index|]
operator|=
name|implicitCornerColor
index|[
literal|1
index|]
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
try|try
block|{
for|for
control|(
name|int
name|i
init|=
name|pStart
init|;
name|i
operator|<
name|numP
condition|;
name|i
operator|++
control|)
block|{
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
name|px
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
name|py
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
name|Point2D
name|tmp
init|=
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|px
argument_list|,
name|py
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
name|points
index|[
name|i
index|]
operator|=
name|tmp
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|cStart
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|numberOfColorComponents
condition|;
name|j
operator|++
control|)
block|{
name|long
name|c
init|=
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerColorComponent
argument_list|)
decl_stmt|;
name|color
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
operator|(
name|float
operator|)
name|interpolate
argument_list|(
name|c
argument_list|,
name|maxSrcColor
argument_list|,
name|colRange
index|[
name|j
index|]
operator|.
name|getMin
argument_list|()
argument_list|,
name|colRange
index|[
name|j
index|]
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"EOF"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|generatePatch
argument_list|(
name|points
argument_list|,
name|color
argument_list|)
return|;
block|}
comment|/**      * Create a patch using control points and 4 corner color values, in      * Type6ShadingContext, a CoonsPatch is returned; in Type6ShadingContext, a      * TensorPatch is returned.      *      * @param points 12 or 16 control points      * @param color 4 corner colors      * @return a patch instance      */
specifier|abstract
name|Patch
name|generatePatch
parameter_list|(
name|Point2D
index|[]
name|points
parameter_list|,
name|float
index|[]
index|[]
name|color
parameter_list|)
function_decl|;
comment|// get a point coordinate on a line by linear interpolation
specifier|private
name|double
name|interpolate
parameter_list|(
name|double
name|x
parameter_list|,
name|long
name|maxValue
parameter_list|,
name|float
name|rangeMin
parameter_list|,
name|float
name|rangeMax
parameter_list|)
block|{
return|return
name|rangeMin
operator|+
operator|(
name|x
operator|/
name|maxValue
operator|)
operator|*
operator|(
name|rangeMax
operator|-
name|rangeMin
operator|)
return|;
block|}
comment|/**      * Calculate every point and its color and store them in a Hash table.      *      * @return a Hash table which contains all the points' positions and colors      * of one image      */
specifier|protected
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|calcPixelTable
parameter_list|()
block|{
name|Map
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
for|for
control|(
name|Patch
name|it
range|:
name|patchList
control|)
block|{
name|super
operator|.
name|calcPixelTable
argument_list|(
name|it
operator|.
name|listOfTriangles
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
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
name|patchList
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
name|patchList
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

