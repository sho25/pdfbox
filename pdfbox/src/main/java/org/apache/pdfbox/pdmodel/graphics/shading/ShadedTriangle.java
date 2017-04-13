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
name|Point
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
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * This is an assistant class for accomplishing type 4, 5, 6 and 7 shading. It  * describes a triangle actually, which is used to compose a patch. It contains  * the degenerated cases, a triangle degenerates to a line or to a point. This  * was done as part of GSoC2014, Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|ShadedTriangle
block|{
specifier|protected
specifier|final
name|Point2D
index|[]
name|corner
decl_stmt|;
comment|// vertices coordinates of a triangle
specifier|protected
specifier|final
name|float
index|[]
index|[]
name|color
decl_stmt|;
specifier|private
specifier|final
name|double
name|area
decl_stmt|;
comment|// area of the triangle
comment|/*      degree = 3 describes a normal triangle,       degree = 2 when a triangle degenerates to a line,      degree = 1 when a triangle degenerates to a point      */
specifier|private
specifier|final
name|int
name|degree
decl_stmt|;
comment|// describes a rasterized line when a triangle degerates to a line, otherwise null
specifier|private
specifier|final
name|Line
name|line
decl_stmt|;
comment|// corner's edge (the opposite edge of a corner) equation value
specifier|private
specifier|final
name|double
name|v0
decl_stmt|;
specifier|private
specifier|final
name|double
name|v1
decl_stmt|;
specifier|private
specifier|final
name|double
name|v2
decl_stmt|;
comment|/**      * Constructor.      *      * @param p an array of the 3 vertices of a triangle      * @param c an array of color corresponding the vertex array p      */
name|ShadedTriangle
parameter_list|(
name|Point2D
index|[]
name|p
parameter_list|,
name|float
index|[]
index|[]
name|c
parameter_list|)
block|{
name|corner
operator|=
name|p
operator|.
name|clone
argument_list|()
expr_stmt|;
name|color
operator|=
name|c
operator|.
name|clone
argument_list|()
expr_stmt|;
name|area
operator|=
name|getArea
argument_list|(
name|p
index|[
literal|0
index|]
argument_list|,
name|p
index|[
literal|1
index|]
argument_list|,
name|p
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|degree
operator|=
name|calcDeg
argument_list|(
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|degree
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|overlaps
argument_list|(
name|corner
index|[
literal|1
index|]
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|)
operator|&&
operator|!
name|overlaps
argument_list|(
name|corner
index|[
literal|0
index|]
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|)
condition|)
block|{
name|Point
name|p0
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
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
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
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
name|line
operator|=
operator|new
name|Line
argument_list|(
name|p0
argument_list|,
name|p1
argument_list|,
name|color
index|[
literal|0
index|]
argument_list|,
name|color
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Point
name|p0
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
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
name|p1
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
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
name|line
operator|=
operator|new
name|Line
argument_list|(
name|p0
argument_list|,
name|p1
argument_list|,
name|color
index|[
literal|1
index|]
argument_list|,
name|color
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|line
operator|=
literal|null
expr_stmt|;
block|}
name|v0
operator|=
name|edgeEquationValue
argument_list|(
name|p
index|[
literal|0
index|]
argument_list|,
name|p
index|[
literal|1
index|]
argument_list|,
name|p
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|v1
operator|=
name|edgeEquationValue
argument_list|(
name|p
index|[
literal|1
index|]
argument_list|,
name|p
index|[
literal|2
index|]
argument_list|,
name|p
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|v2
operator|=
name|edgeEquationValue
argument_list|(
name|p
index|[
literal|2
index|]
argument_list|,
name|p
index|[
literal|0
index|]
argument_list|,
name|p
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calculate the degree value of a triangle.      *      * @param p 3 vertices coordinates      * @return number of unique points in the 3 vertices of a triangle, 3, 2 or      * 1      */
specifier|private
name|int
name|calcDeg
parameter_list|(
name|Point2D
index|[]
name|p
parameter_list|)
block|{
name|Set
argument_list|<
name|Point
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Point2D
name|itp
range|:
name|p
control|)
block|{
name|Point
name|np
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|itp
operator|.
name|getX
argument_list|()
operator|*
literal|1000
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|itp
operator|.
name|getY
argument_list|()
operator|*
literal|1000
argument_list|)
argument_list|)
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|np
argument_list|)
expr_stmt|;
block|}
return|return
name|set
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getDeg
parameter_list|()
block|{
return|return
name|degree
return|;
block|}
comment|/**      * get the boundary of a triangle.      *      * @return {xmin, xmax, ymin, ymax}      */
specifier|public
name|int
index|[]
name|getBoundary
parameter_list|()
block|{
name|int
index|[]
name|boundary
init|=
operator|new
name|int
index|[
literal|4
index|]
decl_stmt|;
name|int
name|x0
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|0
index|]
operator|.
name|getX
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|x1
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|1
index|]
operator|.
name|getX
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|x2
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|2
index|]
operator|.
name|getX
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|y0
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|0
index|]
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|y1
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|1
index|]
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|y2
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|corner
index|[
literal|2
index|]
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|boundary
index|[
literal|0
index|]
operator|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|x0
argument_list|,
name|x1
argument_list|)
argument_list|,
name|x2
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|1
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|x0
argument_list|,
name|x1
argument_list|)
argument_list|,
name|x2
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|2
index|]
operator|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|y0
argument_list|,
name|y1
argument_list|)
argument_list|,
name|y2
argument_list|)
expr_stmt|;
name|boundary
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|y0
argument_list|,
name|y1
argument_list|)
argument_list|,
name|y2
argument_list|)
expr_stmt|;
return|return
name|boundary
return|;
block|}
comment|/**      * Get the line of a triangle.      *      * @return points of the line, or null if this triangle isn't a line      */
specifier|public
name|Line
name|getLine
parameter_list|()
block|{
return|return
name|line
return|;
block|}
comment|/**      * Whether a point is contained in this ShadedTriangle.      *      * @param p the target point      * @return false if p is outside of this triangle, otherwise true      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
if|if
condition|(
name|degree
operator|==
literal|1
condition|)
block|{
return|return
name|overlaps
argument_list|(
name|corner
index|[
literal|0
index|]
argument_list|,
name|p
argument_list|)
operator||
name|overlaps
argument_list|(
name|corner
index|[
literal|1
index|]
argument_list|,
name|p
argument_list|)
operator||
name|overlaps
argument_list|(
name|corner
index|[
literal|2
index|]
argument_list|,
name|p
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|degree
operator|==
literal|2
condition|)
block|{
name|Point
name|tp
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|p
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
name|p
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|line
operator|.
name|linePoints
operator|.
name|contains
argument_list|(
name|tp
argument_list|)
return|;
block|}
comment|/*          the following code judges whether a point is contained in a normal triangle,           taking the on edge case as contained          */
name|double
name|pv0
init|=
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|1
index|]
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
comment|/*          if corner[0] and point p are on different sides of line from corner[1] to corner[2],           p is outside of the triangle          */
if|if
condition|(
name|pv0
operator|*
name|v0
operator|<
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|double
name|pv1
init|=
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|,
name|corner
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|/*          if vertex corner[1] and point p are on different sides of line from corner[2] to corner[0],           p is outside of the triangle          */
if|if
condition|(
name|pv1
operator|*
name|v1
operator|<
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|double
name|pv2
init|=
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|0
index|]
argument_list|,
name|corner
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
comment|/*          only left one case:          if corner[1] and point p are on different sides of line from corner[2] to corner[0],           p is outside of the triangle, otherwise p is contained in the triangle          */
return|return
name|pv2
operator|*
name|v2
operator|>=
literal|0
return|;
comment|// !(pv2 * v2< 0)
block|}
comment|/*      check whether two points overlaps each other, as points' coordinates are       of type double, the coordinates' accuracy used here is 0.001      */
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
comment|/*      two points can define a line equation, adjust the form of the equation to       let the rhs equals 0, calculate the lhs value by plugging the coordinate       of p in the lhs expression      */
specifier|private
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
comment|// calcuate the area of a triangle
specifier|private
name|double
name|getArea
parameter_list|(
name|Point2D
name|a
parameter_list|,
name|Point2D
name|b
parameter_list|,
name|Point2D
name|c
parameter_list|)
block|{
return|return
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|c
operator|.
name|getX
argument_list|()
operator|-
name|b
operator|.
name|getX
argument_list|()
operator|)
operator|*
operator|(
name|c
operator|.
name|getY
argument_list|()
operator|-
name|a
operator|.
name|getY
argument_list|()
operator|)
operator|-
operator|(
name|c
operator|.
name|getX
argument_list|()
operator|-
name|a
operator|.
name|getX
argument_list|()
operator|)
operator|*
operator|(
name|c
operator|.
name|getY
argument_list|()
operator|-
name|b
operator|.
name|getY
argument_list|()
operator|)
argument_list|)
operator|/
literal|2.0
return|;
block|}
comment|/**      * Calculate the color of a point.      *      * @param p the target point      * @return an array denotes the point's color      */
specifier|public
name|float
index|[]
name|calcColor
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
name|int
name|numberOfColorComponents
init|=
name|color
index|[
literal|0
index|]
operator|.
name|length
decl_stmt|;
name|float
index|[]
name|pCol
init|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
switch|switch
condition|(
name|degree
condition|)
block|{
case|case
literal|1
case|:
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
comment|// average
name|pCol
index|[
name|i
index|]
operator|=
operator|(
name|color
index|[
literal|0
index|]
index|[
name|i
index|]
operator|+
name|color
index|[
literal|1
index|]
index|[
name|i
index|]
operator|+
name|color
index|[
literal|2
index|]
index|[
name|i
index|]
operator|)
operator|/
literal|3.0f
expr_stmt|;
block|}
break|break;
case|case
literal|2
case|:
comment|// linear interpolation
name|Point
name|tp
init|=
operator|new
name|Point
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|p
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
name|p
operator|.
name|getY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|line
operator|.
name|calcColor
argument_list|(
name|tp
argument_list|)
return|;
default|default:
name|float
name|aw
init|=
call|(
name|float
call|)
argument_list|(
name|getArea
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|1
index|]
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|)
operator|/
name|area
argument_list|)
decl_stmt|;
name|float
name|bw
init|=
call|(
name|float
call|)
argument_list|(
name|getArea
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|2
index|]
argument_list|,
name|corner
index|[
literal|0
index|]
argument_list|)
operator|/
name|area
argument_list|)
decl_stmt|;
name|float
name|cw
init|=
call|(
name|float
call|)
argument_list|(
name|getArea
argument_list|(
name|p
argument_list|,
name|corner
index|[
literal|0
index|]
argument_list|,
name|corner
index|[
literal|1
index|]
argument_list|)
operator|/
name|area
argument_list|)
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
comment|// barycentric interpolation
name|pCol
index|[
name|i
index|]
operator|=
name|color
index|[
literal|0
index|]
index|[
name|i
index|]
operator|*
name|aw
operator|+
name|color
index|[
literal|1
index|]
index|[
name|i
index|]
operator|*
name|bw
operator|+
name|color
index|[
literal|2
index|]
index|[
name|i
index|]
operator|*
name|cw
expr_stmt|;
block|}
break|break;
block|}
return|return
name|pCol
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|corner
index|[
literal|0
index|]
operator|+
literal|" "
operator|+
name|corner
index|[
literal|1
index|]
operator|+
literal|" "
operator|+
name|corner
index|[
literal|2
index|]
return|;
block|}
block|}
end_class

end_unit

