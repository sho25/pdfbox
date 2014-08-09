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

begin_comment
comment|/**  * This class is used to describe a patch for type 7 shading.  * This was done as part of GSoC2014, Tilman Hausherr is the mentor.  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|TensorPatch
extends|extends
name|Patch
block|{
comment|/**      * Constructor of a patch for type 7 shading.      * @param points 16 control points      * @param color  4 corner colors      */
specifier|protected
name|TensorPatch
parameter_list|(
name|Point2D
index|[]
name|tcp
parameter_list|,
name|float
index|[]
index|[]
name|color
parameter_list|)
block|{
name|super
argument_list|(
name|tcp
argument_list|,
name|color
argument_list|)
expr_stmt|;
name|controlPoints
operator|=
name|reshapeControlPoints
argument_list|(
name|tcp
argument_list|)
expr_stmt|;
name|level
operator|=
name|calcLevel
argument_list|()
expr_stmt|;
name|listOfTriangles
operator|=
name|getTriangles
argument_list|()
expr_stmt|;
block|}
comment|/*     order the 16 1d points to a square matrix which is as the one described      in p.199 of PDF3200_2008.pdf rotated 90 degrees clockwise     */
specifier|private
name|Point2D
index|[]
index|[]
name|reshapeControlPoints
parameter_list|(
name|Point2D
index|[]
name|tcp
parameter_list|)
block|{
name|Point2D
index|[]
index|[]
name|square
init|=
operator|new
name|Point2D
index|[
literal|4
index|]
index|[
literal|4
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
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|square
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
name|tcp
index|[
name|i
index|]
expr_stmt|;
name|square
index|[
literal|3
index|]
index|[
name|i
index|]
operator|=
name|tcp
index|[
literal|9
operator|-
name|i
index|]
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|square
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|tcp
index|[
literal|12
operator|-
name|i
index|]
expr_stmt|;
name|square
index|[
name|i
index|]
index|[
literal|2
index|]
operator|=
name|tcp
index|[
literal|12
operator|+
name|i
index|]
expr_stmt|;
name|square
index|[
name|i
index|]
index|[
literal|3
index|]
operator|=
name|tcp
index|[
literal|3
operator|+
name|i
index|]
expr_stmt|;
block|}
name|square
index|[
literal|1
index|]
index|[
literal|1
index|]
operator|=
name|tcp
index|[
literal|12
index|]
expr_stmt|;
name|square
index|[
literal|2
index|]
index|[
literal|1
index|]
operator|=
name|tcp
index|[
literal|15
index|]
expr_stmt|;
return|return
name|square
return|;
block|}
comment|// calculate the dividing level from the control points
specifier|private
name|int
index|[]
name|calcLevel
parameter_list|()
block|{
name|int
index|[]
name|l
init|=
block|{
literal|4
block|,
literal|4
block|}
decl_stmt|;
name|Point2D
index|[]
name|ctlC1
init|=
operator|new
name|Point2D
index|[
literal|4
index|]
decl_stmt|;
name|Point2D
index|[]
name|ctlC2
init|=
operator|new
name|Point2D
index|[
literal|4
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|4
condition|;
name|j
operator|++
control|)
block|{
name|ctlC1
index|[
name|j
index|]
operator|=
name|controlPoints
index|[
name|j
index|]
index|[
literal|0
index|]
expr_stmt|;
name|ctlC2
index|[
name|j
index|]
operator|=
name|controlPoints
index|[
name|j
index|]
index|[
literal|3
index|]
expr_stmt|;
block|}
comment|// if two opposite edges are both lines, there is a possibility to reduce the dividing level
if|if
condition|(
name|isEdgeALine
argument_list|(
name|ctlC1
argument_list|)
operator|&
name|isEdgeALine
argument_list|(
name|ctlC2
argument_list|)
condition|)
block|{
comment|/*             if any of the 4 inner control points is out of the patch formed by the 4 edges,              keep the high dividing level,              otherwise, determine the dividing level by the lengths of edges             */
if|if
condition|(
name|isOnSameSideCC
argument_list|(
name|controlPoints
index|[
literal|1
index|]
index|[
literal|1
index|]
argument_list|)
operator||
name|isOnSameSideCC
argument_list|(
name|controlPoints
index|[
literal|1
index|]
index|[
literal|2
index|]
argument_list|)
operator||
name|isOnSameSideCC
argument_list|(
name|controlPoints
index|[
literal|2
index|]
index|[
literal|1
index|]
argument_list|)
operator||
name|isOnSameSideCC
argument_list|(
name|controlPoints
index|[
literal|2
index|]
index|[
literal|2
index|]
argument_list|)
condition|)
block|{
comment|// keep the high dividing level
block|}
else|else
block|{
comment|// length's unit is one pixel in device space
name|double
name|lc1
init|=
name|getLen
argument_list|(
name|ctlC1
index|[
literal|0
index|]
argument_list|,
name|ctlC1
index|[
literal|3
index|]
argument_list|)
decl_stmt|,
name|lc2
init|=
name|getLen
argument_list|(
name|ctlC2
index|[
literal|0
index|]
argument_list|,
name|ctlC2
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|lc1
operator|>
literal|800
operator|||
name|lc2
operator|>
literal|800
condition|)
block|{                 }
elseif|else
if|if
condition|(
name|lc1
operator|>
literal|400
operator|||
name|lc2
operator|>
literal|400
condition|)
block|{
name|l
index|[
literal|0
index|]
operator|=
literal|3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lc1
operator|>
literal|200
operator|||
name|lc2
operator|>
literal|200
condition|)
block|{
name|l
index|[
literal|0
index|]
operator|=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|l
index|[
literal|0
index|]
operator|=
literal|1
expr_stmt|;
block|}
block|}
block|}
comment|// the other two opposite edges
if|if
condition|(
name|isEdgeALine
argument_list|(
name|controlPoints
index|[
literal|0
index|]
argument_list|)
operator|&
name|isEdgeALine
argument_list|(
name|controlPoints
index|[
literal|3
index|]
argument_list|)
condition|)
block|{
if|if
condition|(
name|isOnSameSideDD
argument_list|(
name|controlPoints
index|[
literal|1
index|]
index|[
literal|1
index|]
argument_list|)
operator||
name|isOnSameSideDD
argument_list|(
name|controlPoints
index|[
literal|1
index|]
index|[
literal|2
index|]
argument_list|)
operator||
name|isOnSameSideDD
argument_list|(
name|controlPoints
index|[
literal|2
index|]
index|[
literal|1
index|]
argument_list|)
operator||
name|isOnSameSideDD
argument_list|(
name|controlPoints
index|[
literal|2
index|]
index|[
literal|2
index|]
argument_list|)
condition|)
block|{
comment|// keep the high dividing level
block|}
else|else
block|{
name|double
name|ld1
init|=
name|getLen
argument_list|(
name|controlPoints
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|,
name|controlPoints
index|[
literal|0
index|]
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
name|double
name|ld2
init|=
name|getLen
argument_list|(
name|controlPoints
index|[
literal|3
index|]
index|[
literal|0
index|]
argument_list|,
name|controlPoints
index|[
literal|3
index|]
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|ld1
operator|>
literal|800
operator|||
name|ld2
operator|>
literal|800
condition|)
block|{                 }
elseif|else
if|if
condition|(
name|ld1
operator|>
literal|400
operator|||
name|ld2
operator|>
literal|400
condition|)
block|{
name|l
index|[
literal|1
index|]
operator|=
literal|3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ld1
operator|>
literal|200
operator|||
name|ld2
operator|>
literal|200
condition|)
block|{
name|l
index|[
literal|1
index|]
operator|=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|l
index|[
literal|1
index|]
operator|=
literal|1
expr_stmt|;
block|}
block|}
block|}
return|return
name|l
return|;
block|}
comment|// whether a point is on the same side of edge C1 and edge C2
specifier|private
name|boolean
name|isOnSameSideCC
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
name|double
name|cc
init|=
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|controlPoints
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|,
name|controlPoints
index|[
literal|3
index|]
index|[
literal|0
index|]
argument_list|)
operator|*
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|controlPoints
index|[
literal|0
index|]
index|[
literal|3
index|]
argument_list|,
name|controlPoints
index|[
literal|3
index|]
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
return|return
name|cc
operator|>
literal|0
return|;
block|}
comment|// whether a point is on the same side of edge D1 and edge D2
specifier|private
name|boolean
name|isOnSameSideDD
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
name|double
name|dd
init|=
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|controlPoints
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|,
name|controlPoints
index|[
literal|0
index|]
index|[
literal|3
index|]
argument_list|)
operator|*
name|edgeEquationValue
argument_list|(
name|p
argument_list|,
name|controlPoints
index|[
literal|3
index|]
index|[
literal|0
index|]
argument_list|,
name|controlPoints
index|[
literal|3
index|]
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
return|return
name|dd
operator|>
literal|0
return|;
block|}
comment|// get a list of triangles which compose this tensor patch
specifier|private
name|ArrayList
argument_list|<
name|ShadedTriangle
argument_list|>
name|getTriangles
parameter_list|()
block|{
name|CoordinateColorPair
index|[]
index|[]
name|patchCC
init|=
name|getPatchCoordinatesColor
argument_list|()
decl_stmt|;
return|return
name|getShadedTriangles
argument_list|(
name|patchCC
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Point2D
index|[]
name|getFlag1Edge
parameter_list|()
block|{
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|implicitEdge
index|[
name|i
index|]
operator|=
name|controlPoints
index|[
name|i
index|]
index|[
literal|3
index|]
expr_stmt|;
block|}
return|return
name|implicitEdge
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Point2D
index|[]
name|getFlag2Edge
parameter_list|()
block|{
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|implicitEdge
index|[
name|i
index|]
operator|=
name|controlPoints
index|[
literal|3
index|]
index|[
literal|3
operator|-
name|i
index|]
expr_stmt|;
block|}
return|return
name|implicitEdge
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Point2D
index|[]
name|getFlag3Edge
parameter_list|()
block|{
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|implicitEdge
index|[
name|i
index|]
operator|=
name|controlPoints
index|[
literal|3
operator|-
name|i
index|]
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|implicitEdge
return|;
block|}
comment|/*     dividing a patch into a grid according to level, then calculate the coordinate and color of      each crossing point in the grid, the rule to calculate the coordinate is tensor-product which      is defined in page 119 of PDF32000_2008.pdf, the method to calculate the cooresponding color is      bilinear interpolation     */
specifier|private
name|CoordinateColorPair
index|[]
index|[]
name|getPatchCoordinatesColor
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
name|double
index|[]
index|[]
name|bernsteinPolyU
init|=
name|getBernsteinPolynomials
argument_list|(
name|level
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|szU
init|=
name|bernsteinPolyU
index|[
literal|0
index|]
operator|.
name|length
decl_stmt|;
name|double
index|[]
index|[]
name|bernsteinPolyV
init|=
name|getBernsteinPolynomials
argument_list|(
name|level
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|int
name|szV
init|=
name|bernsteinPolyV
index|[
literal|0
index|]
operator|.
name|length
decl_stmt|;
name|CoordinateColorPair
index|[]
index|[]
name|patchCC
init|=
operator|new
name|CoordinateColorPair
index|[
name|szV
index|]
index|[
name|szU
index|]
decl_stmt|;
name|double
name|stepU
init|=
literal|1.0
operator|/
operator|(
name|szU
operator|-
literal|1
operator|)
decl_stmt|;
name|double
name|stepV
init|=
literal|1.0
operator|/
operator|(
name|szV
operator|-
literal|1
operator|)
decl_stmt|;
name|double
name|v
init|=
operator|-
name|stepV
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|szV
condition|;
name|k
operator|++
control|)
block|{
comment|// v and u are the assistant parameters
name|v
operator|+=
name|stepV
expr_stmt|;
name|double
name|u
init|=
operator|-
name|stepU
decl_stmt|;
for|for
control|(
name|int
name|l
init|=
literal|0
init|;
name|l
operator|<
name|szU
condition|;
name|l
operator|++
control|)
block|{
name|double
name|tmpx
init|=
literal|0.0
decl_stmt|;
name|double
name|tmpy
init|=
literal|0.0
decl_stmt|;
comment|// these two "for" loops are for the equation to define the patch surface (coordinates)
for|for
control|(
name|int
name|i
init|=
literal|0
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
literal|4
condition|;
name|j
operator|++
control|)
block|{
name|tmpx
operator|+=
name|controlPoints
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|getX
argument_list|()
operator|*
name|bernsteinPolyU
index|[
name|i
index|]
index|[
name|l
index|]
operator|*
name|bernsteinPolyV
index|[
name|j
index|]
index|[
name|k
index|]
expr_stmt|;
name|tmpy
operator|+=
name|controlPoints
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|getY
argument_list|()
operator|*
name|bernsteinPolyU
index|[
name|i
index|]
index|[
name|l
index|]
operator|*
name|bernsteinPolyV
index|[
name|j
index|]
index|[
name|k
index|]
expr_stmt|;
block|}
block|}
name|Point2D
name|tmpC
init|=
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|tmpx
argument_list|,
name|tmpy
argument_list|)
decl_stmt|;
name|u
operator|+=
name|stepU
expr_stmt|;
name|float
index|[]
name|paramSC
init|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
for|for
control|(
name|int
name|ci
init|=
literal|0
init|;
name|ci
operator|<
name|numberOfColorComponents
condition|;
name|ci
operator|++
control|)
block|{
name|paramSC
index|[
name|ci
index|]
operator|=
call|(
name|float
call|)
argument_list|(
operator|(
literal|1
operator|-
name|v
operator|)
operator|*
operator|(
operator|(
literal|1
operator|-
name|u
operator|)
operator|*
name|cornerColor
index|[
literal|0
index|]
index|[
name|ci
index|]
operator|+
name|u
operator|*
name|cornerColor
index|[
literal|3
index|]
index|[
name|ci
index|]
operator|)
operator|+
name|v
operator|*
operator|(
operator|(
literal|1
operator|-
name|u
operator|)
operator|*
name|cornerColor
index|[
literal|1
index|]
index|[
name|ci
index|]
operator|+
name|u
operator|*
name|cornerColor
index|[
literal|2
index|]
index|[
name|ci
index|]
operator|)
argument_list|)
expr_stmt|;
comment|// bilinear interpolation
block|}
name|patchCC
index|[
name|k
index|]
index|[
name|l
index|]
operator|=
operator|new
name|CoordinateColorPair
argument_list|(
name|tmpC
argument_list|,
name|paramSC
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|patchCC
return|;
block|}
comment|// Bernstein polynomials which are defined in page 119 of PDF32000_2008.pdf
specifier|private
name|double
index|[]
index|[]
name|getBernsteinPolynomials
parameter_list|(
name|int
name|lvl
parameter_list|)
block|{
name|int
name|sz
init|=
operator|(
literal|1
operator|<<
name|lvl
operator|)
operator|+
literal|1
decl_stmt|;
name|double
index|[]
index|[]
name|poly
init|=
operator|new
name|double
index|[
literal|4
index|]
index|[
name|sz
index|]
decl_stmt|;
name|double
name|step
init|=
literal|1.0
operator|/
operator|(
name|sz
operator|-
literal|1
operator|)
decl_stmt|;
name|double
name|t
init|=
operator|-
name|step
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
name|sz
condition|;
name|i
operator|++
control|)
block|{
name|t
operator|+=
name|step
expr_stmt|;
name|poly
index|[
literal|0
index|]
index|[
name|i
index|]
operator|=
operator|(
literal|1
operator|-
name|t
operator|)
operator|*
operator|(
literal|1
operator|-
name|t
operator|)
operator|*
operator|(
literal|1
operator|-
name|t
operator|)
expr_stmt|;
name|poly
index|[
literal|1
index|]
index|[
name|i
index|]
operator|=
literal|3
operator|*
name|t
operator|*
operator|(
literal|1
operator|-
name|t
operator|)
operator|*
operator|(
literal|1
operator|-
name|t
operator|)
expr_stmt|;
name|poly
index|[
literal|2
index|]
index|[
name|i
index|]
operator|=
literal|3
operator|*
name|t
operator|*
name|t
operator|*
operator|(
literal|1
operator|-
name|t
operator|)
expr_stmt|;
name|poly
index|[
literal|3
index|]
index|[
name|i
index|]
operator|=
name|t
operator|*
name|t
operator|*
name|t
expr_stmt|;
block|}
return|return
name|poly
return|;
block|}
block|}
end_class

end_unit

