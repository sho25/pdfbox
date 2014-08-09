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
name|util
operator|.
name|HashSet
import|;
end_import

begin_comment
comment|/**  * This class describes a rasterized line. This was done as part of GSoC2014,  * Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|Line
block|{
specifier|private
specifier|final
name|Point
name|point0
decl_stmt|;
specifier|private
specifier|final
name|Point
name|point1
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|color0
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|color1
decl_stmt|;
specifier|protected
specifier|final
name|HashSet
argument_list|<
name|Point
argument_list|>
name|linePoints
decl_stmt|;
comment|// all the points in this rasterized line
comment|/**      * Constructor of class Line.      *      * @param p0 one end of a line      * @param p1 the other end of the line      * @param c0 color of point p0      * @param c1 color of point p1      */
specifier|public
name|Line
parameter_list|(
name|Point
name|p0
parameter_list|,
name|Point
name|p1
parameter_list|,
name|float
index|[]
name|c0
parameter_list|,
name|float
index|[]
name|c1
parameter_list|)
block|{
name|point0
operator|=
name|p0
expr_stmt|;
name|point1
operator|=
name|p1
expr_stmt|;
name|color0
operator|=
name|c0
operator|.
name|clone
argument_list|()
expr_stmt|;
name|color1
operator|=
name|c1
operator|.
name|clone
argument_list|()
expr_stmt|;
name|linePoints
operator|=
name|calcLine
argument_list|(
name|point0
operator|.
name|x
argument_list|,
name|point0
operator|.
name|y
argument_list|,
name|point1
operator|.
name|x
argument_list|,
name|point1
operator|.
name|y
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calculate the points of a line with Bresenham's line algorithm      *<a      * href="http://en.wikipedia.org/wiki/Bresenham's_line_algorithm">Bresenham's      * line algorithm</a>      *      * @param x0 coordinate      * @param y0 coordinate      * @param x1 coordinate      * @param y1 coordinate      * @return all the points on the rasterized line from (x0, y0) to (x1, y1)      */
specifier|private
name|HashSet
argument_list|<
name|Point
argument_list|>
name|calcLine
parameter_list|(
name|int
name|x0
parameter_list|,
name|int
name|y0
parameter_list|,
name|int
name|x1
parameter_list|,
name|int
name|y1
parameter_list|)
block|{
name|HashSet
argument_list|<
name|Point
argument_list|>
name|points
init|=
operator|new
name|HashSet
argument_list|<
name|Point
argument_list|>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|int
name|dx
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|Math
operator|.
name|abs
argument_list|(
name|x1
operator|-
name|x0
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|dy
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|Math
operator|.
name|abs
argument_list|(
name|y1
operator|-
name|y0
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|sx
init|=
name|x0
operator|<
name|x1
condition|?
literal|1
else|:
operator|-
literal|1
decl_stmt|;
name|int
name|sy
init|=
name|y0
operator|<
name|y1
condition|?
literal|1
else|:
operator|-
literal|1
decl_stmt|;
name|int
name|err
init|=
name|dx
operator|-
name|dy
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|points
operator|.
name|add
argument_list|(
operator|new
name|Point
argument_list|(
name|x0
argument_list|,
name|y0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|x0
operator|==
name|x1
operator|&&
name|y0
operator|==
name|y1
condition|)
block|{
break|break;
block|}
name|int
name|e2
init|=
literal|2
operator|*
name|err
decl_stmt|;
if|if
condition|(
name|e2
operator|>
operator|-
name|dy
condition|)
block|{
name|err
operator|=
name|err
operator|-
name|dy
expr_stmt|;
name|x0
operator|=
name|x0
operator|+
name|sx
expr_stmt|;
block|}
if|if
condition|(
name|e2
operator|<
name|dx
condition|)
block|{
name|err
operator|=
name|err
operator|+
name|dx
expr_stmt|;
name|y0
operator|=
name|y0
operator|+
name|sy
expr_stmt|;
block|}
block|}
return|return
name|points
return|;
block|}
comment|/**      * Calculate the color of a point on a rasterized line by linear      * interpolation.      *      * @param p target point, p should always be contained in linePoints      * @return color      */
specifier|protected
name|float
index|[]
name|calcColor
parameter_list|(
name|Point
name|p
parameter_list|)
block|{
name|int
name|numberOfColorComponents
init|=
name|color0
operator|.
name|length
decl_stmt|;
name|float
index|[]
name|pc
init|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
if|if
condition|(
name|point0
operator|.
name|x
operator|==
name|point1
operator|.
name|x
operator|&&
name|point0
operator|.
name|y
operator|==
name|point1
operator|.
name|y
condition|)
block|{
return|return
name|color0
return|;
block|}
elseif|else
if|if
condition|(
name|point0
operator|.
name|x
operator|==
name|point1
operator|.
name|x
condition|)
block|{
name|float
name|l
init|=
name|point1
operator|.
name|y
operator|-
name|point0
operator|.
name|y
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
name|pc
index|[
name|i
index|]
operator|=
call|(
name|float
call|)
argument_list|(
name|color0
index|[
name|i
index|]
operator|*
operator|(
name|point1
operator|.
name|y
operator|-
name|p
operator|.
name|y
operator|)
operator|/
name|l
operator|+
name|color1
index|[
name|i
index|]
operator|*
operator|(
name|p
operator|.
name|y
operator|-
name|point0
operator|.
name|y
operator|)
operator|/
name|l
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|float
name|l
init|=
name|point1
operator|.
name|x
operator|-
name|point0
operator|.
name|x
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
name|pc
index|[
name|i
index|]
operator|=
call|(
name|float
call|)
argument_list|(
name|color0
index|[
name|i
index|]
operator|*
operator|(
name|point1
operator|.
name|x
operator|-
name|p
operator|.
name|x
operator|)
operator|/
name|l
operator|+
name|color1
index|[
name|i
index|]
operator|*
operator|(
name|p
operator|.
name|x
operator|-
name|point0
operator|.
name|x
operator|)
operator|/
name|l
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|pc
return|;
block|}
block|}
end_class

end_unit

