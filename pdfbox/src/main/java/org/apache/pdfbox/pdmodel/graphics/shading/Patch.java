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
name|geom
operator|.
name|Point2D
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
name|List
import|;
end_import

begin_comment
comment|/**  * Patch is extended by CoonsPatch and TensorPatch. This was done as part of  * GSoC2014, Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
specifier|abstract
class|class
name|Patch
block|{
specifier|protected
name|Point2D
index|[]
index|[]
name|controlPoints
decl_stmt|;
specifier|protected
name|float
index|[]
index|[]
name|cornerColor
decl_stmt|;
comment|/*      level = {levelU, levelV}, levelU defines the patch's u direction edges should be       divided into 2^levelU parts, level V defines the patch's v direction edges should      be divided into 2^levelV parts      */
specifier|protected
name|int
index|[]
name|level
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|listOfTriangles
decl_stmt|;
comment|/**      * Constructor of Patch.      *      * @param ctl control points, size is 12 (for type 6 shading) or 16 (for      * type 7 shading)      * @param color 4 corner's colors      */
name|Patch
parameter_list|(
name|Point2D
index|[]
name|ctl
parameter_list|,
name|float
index|[]
index|[]
name|color
parameter_list|)
block|{
name|cornerColor
operator|=
name|color
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get the implicit edge for flag = 1.      *      * @return implicit control points      */
specifier|protected
specifier|abstract
name|Point2D
index|[]
name|getFlag1Edge
parameter_list|()
function_decl|;
comment|/**      * Get the implicit edge for flag = 2.      *      * @return implicit control points      */
specifier|protected
specifier|abstract
name|Point2D
index|[]
name|getFlag2Edge
parameter_list|()
function_decl|;
comment|/**      * Get the implicit edge for flag = 3.      *      * @return implicit control points      */
specifier|protected
specifier|abstract
name|Point2D
index|[]
name|getFlag3Edge
parameter_list|()
function_decl|;
comment|/**      * Get the implicit color for flag = 1.      *      * @return color      */
specifier|protected
name|float
index|[]
index|[]
name|getFlag1Color
parameter_list|()
block|{
name|int
name|numberOfColorComponents
init|=
name|cornerColor
index|[
literal|0
index|]
operator|.
name|length
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
name|implicitCornerColor
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|1
index|]
index|[
name|i
index|]
expr_stmt|;
name|implicitCornerColor
index|[
literal|1
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|2
index|]
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|implicitCornerColor
return|;
block|}
comment|/**      * Get implicit color for flag = 2.      *      * @return color      */
specifier|protected
name|float
index|[]
index|[]
name|getFlag2Color
parameter_list|()
block|{
name|int
name|numberOfColorComponents
init|=
name|cornerColor
index|[
literal|0
index|]
operator|.
name|length
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
name|implicitCornerColor
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|2
index|]
index|[
name|i
index|]
expr_stmt|;
name|implicitCornerColor
index|[
literal|1
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|3
index|]
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|implicitCornerColor
return|;
block|}
comment|/**      * Get implicit color for flag = 3.      *      * @return color      */
specifier|protected
name|float
index|[]
index|[]
name|getFlag3Color
parameter_list|()
block|{
name|int
name|numberOfColorComponents
init|=
name|cornerColor
index|[
literal|0
index|]
operator|.
name|length
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
name|implicitCornerColor
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|3
index|]
index|[
name|i
index|]
expr_stmt|;
name|implicitCornerColor
index|[
literal|1
index|]
index|[
name|i
index|]
operator|=
name|cornerColor
index|[
literal|0
index|]
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|implicitCornerColor
return|;
block|}
comment|/**      * Calculate the distance from point ps to point pe.      *      * @param ps one end of a line      * @param pe the other end of the line      * @return length of the line      */
specifier|protected
name|double
name|getLen
parameter_list|(
name|Point2D
name|ps
parameter_list|,
name|Point2D
name|pe
parameter_list|)
block|{
name|double
name|x
init|=
name|pe
operator|.
name|getX
argument_list|()
operator|-
name|ps
operator|.
name|getX
argument_list|()
decl_stmt|;
name|double
name|y
init|=
name|pe
operator|.
name|getY
argument_list|()
operator|-
name|ps
operator|.
name|getY
argument_list|()
decl_stmt|;
return|return
name|Math
operator|.
name|sqrt
argument_list|(
name|x
operator|*
name|x
operator|+
name|y
operator|*
name|y
argument_list|)
return|;
block|}
comment|/**      * Whether the for control points are on a line.      *      * @param ctl an edge's control points, the size of ctl is 4      * @return true when 4 control points are on a line, otherwise false      */
specifier|protected
name|boolean
name|isEdgeALine
parameter_list|(
name|Point2D
index|[]
name|ctl
parameter_list|)
block|{
name|double
name|ctl1
init|=
name|Math
operator|.
name|abs
argument_list|(
name|edgeEquationValue
argument_list|(
name|ctl
index|[
literal|1
index|]
argument_list|,
name|ctl
index|[
literal|0
index|]
argument_list|,
name|ctl
index|[
literal|3
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|double
name|ctl2
init|=
name|Math
operator|.
name|abs
argument_list|(
name|edgeEquationValue
argument_list|(
name|ctl
index|[
literal|2
index|]
argument_list|,
name|ctl
index|[
literal|0
index|]
argument_list|,
name|ctl
index|[
literal|3
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|double
name|x
init|=
name|Math
operator|.
name|abs
argument_list|(
name|ctl
index|[
literal|0
index|]
operator|.
name|getX
argument_list|()
operator|-
name|ctl
index|[
literal|3
index|]
operator|.
name|getX
argument_list|()
argument_list|)
decl_stmt|;
name|double
name|y
init|=
name|Math
operator|.
name|abs
argument_list|(
name|ctl
index|[
literal|0
index|]
operator|.
name|getY
argument_list|()
operator|-
name|ctl
index|[
literal|3
index|]
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|ctl1
operator|<=
name|x
operator|&&
name|ctl2
operator|<=
name|x
operator|)
operator|||
operator|(
name|ctl1
operator|<=
name|y
operator|&&
name|ctl2
operator|<=
name|y
operator|)
return|;
block|}
comment|/**      * A line from point p1 to point p2 defines an equation, adjust the form of      * the equation to let the rhs equals 0, then calculate the lhs value by      * plugging the coordinate of p in the lhs expression.      *      * @param p target point      * @param p1 one end of a line      * @param p2 the other end of a line      * @return calculated value      */
specifier|protected
name|double
name|edgeEquationValue
parameter_list|(
name|Point2D
name|p
parameter_list|,
name|Point2D
name|p1
parameter_list|,
name|Point2D
name|p2
parameter_list|)
block|{
return|return
operator|(
name|p2
operator|.
name|getY
argument_list|()
operator|-
name|p1
operator|.
name|getY
argument_list|()
operator|)
operator|*
operator|(
name|p
operator|.
name|getX
argument_list|()
operator|-
name|p1
operator|.
name|getX
argument_list|()
operator|)
operator|-
operator|(
name|p2
operator|.
name|getX
argument_list|()
operator|-
name|p1
operator|.
name|getX
argument_list|()
operator|)
operator|*
operator|(
name|p
operator|.
name|getY
argument_list|()
operator|-
name|p1
operator|.
name|getY
argument_list|()
operator|)
return|;
block|}
comment|/**      * An assistant method to accomplish type 6 and type 7 shading.      *      * @param patchCC all the crossing point coordinates and color of a grid      * @return a ShadedTriangle list which can compose the grid patch      */
specifier|protected
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|getShadedTriangles
parameter_list|(
name|CoordinateColorPair
index|[]
index|[]
name|patchCC
parameter_list|)
block|{
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|szV
init|=
name|patchCC
operator|.
name|length
decl_stmt|;
name|int
name|szU
init|=
name|patchCC
index|[
literal|0
index|]
operator|.
name|length
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|szV
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
literal|1
init|;
name|j
operator|<
name|szU
condition|;
name|j
operator|++
control|)
block|{
name|Point2D
name|p0
init|=
name|patchCC
index|[
name|i
operator|-
literal|1
index|]
index|[
name|j
operator|-
literal|1
index|]
operator|.
name|coordinate
decl_stmt|,
name|p1
init|=
name|patchCC
index|[
name|i
operator|-
literal|1
index|]
index|[
name|j
index|]
operator|.
name|coordinate
decl_stmt|,
name|p2
init|=
name|patchCC
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|coordinate
decl_stmt|,
name|p3
init|=
name|patchCC
index|[
name|i
index|]
index|[
name|j
operator|-
literal|1
index|]
operator|.
name|coordinate
decl_stmt|;
name|boolean
name|ll
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|overlaps
argument_list|(
name|p0
argument_list|,
name|p1
argument_list|)
operator|||
name|overlaps
argument_list|(
name|p0
argument_list|,
name|p3
argument_list|)
condition|)
block|{
name|ll
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// p0, p1 and p3 are in counter clock wise order, p1 has priority over p0, p3 has priority over p1
name|Point2D
index|[]
name|llCorner
init|=
block|{
name|p0
block|,
name|p1
block|,
name|p3
block|}
decl_stmt|;
name|float
index|[]
index|[]
name|llColor
init|=
block|{
name|patchCC
index|[
name|i
operator|-
literal|1
index|]
index|[
name|j
operator|-
literal|1
index|]
operator|.
name|color
block|,
name|patchCC
index|[
name|i
operator|-
literal|1
index|]
index|[
name|j
index|]
operator|.
name|color
block|,
name|patchCC
index|[
name|i
index|]
index|[
name|j
operator|-
literal|1
index|]
operator|.
name|color
block|}
decl_stmt|;
name|ShadedTriangle
name|tmpll
init|=
operator|new
name|ShadedTriangle
argument_list|(
name|llCorner
argument_list|,
name|llColor
argument_list|)
decl_stmt|;
comment|// lower left triangle
name|list
operator|.
name|add
argument_list|(
name|tmpll
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ll
operator|&&
operator|(
name|overlaps
argument_list|(
name|p2
argument_list|,
name|p1
argument_list|)
operator|||
name|overlaps
argument_list|(
name|p2
argument_list|,
name|p3
argument_list|)
operator|)
condition|)
block|{                 }
else|else
block|{
comment|// p3, p1 and p2 are in counter clock wise order, p1 has priority over p3, p2 has priority over p1
name|Point2D
index|[]
name|urCorner
init|=
block|{
name|p3
block|,
name|p1
block|,
name|p2
block|}
decl_stmt|;
name|float
index|[]
index|[]
name|urColor
init|=
block|{
name|patchCC
index|[
name|i
index|]
index|[
name|j
operator|-
literal|1
index|]
operator|.
name|color
block|,
name|patchCC
index|[
name|i
operator|-
literal|1
index|]
index|[
name|j
index|]
operator|.
name|color
block|,
name|patchCC
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|color
block|}
decl_stmt|;
name|ShadedTriangle
name|tmpur
init|=
operator|new
name|ShadedTriangle
argument_list|(
name|urCorner
argument_list|,
name|urColor
argument_list|)
decl_stmt|;
comment|// upper right triangle
name|list
operator|.
name|add
argument_list|(
name|tmpur
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
comment|// whether two points p0 and p1 are degenerated into one point within the coordinates' accuracy 0.001
specifier|private
name|boolean
name|overlaps
parameter_list|(
name|Point2D
name|p0
parameter_list|,
name|Point2D
name|p1
parameter_list|)
block|{
return|return
name|Math
operator|.
name|abs
argument_list|(
name|p0
operator|.
name|getX
argument_list|()
operator|-
name|p1
operator|.
name|getX
argument_list|()
argument_list|)
operator|<
literal|0.001
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|p0
operator|.
name|getY
argument_list|()
operator|-
name|p1
operator|.
name|getY
argument_list|()
argument_list|)
operator|<
literal|0.001
return|;
block|}
block|}
end_class

end_unit

