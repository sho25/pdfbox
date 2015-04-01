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

begin_comment
comment|/**  * This class is used to describe the edge of each patch for type 6 shading.  * This was done as part of GSoC2014, Tilman Hausherr is the mentor.  *  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|CubicBezierCurve
block|{
specifier|private
specifier|final
name|Point2D
index|[]
name|controlPoints
decl_stmt|;
specifier|private
specifier|final
name|int
name|level
decl_stmt|;
specifier|private
specifier|final
name|Point2D
index|[]
name|curve
decl_stmt|;
comment|/**      * Constructor of CubicBezierCurve      *      * @param ctrlPnts, 4 control points [p0, p1, p2, p3]      * @param l, dividing level, if l = 0, one cubic Bezier curve is divided      * into 2^0 = 1 segments, if l = n, one cubic Bezier curve is divided into      * 2^n segments      */
name|CubicBezierCurve
parameter_list|(
name|Point2D
index|[]
name|ctrlPnts
parameter_list|,
name|int
name|l
parameter_list|)
block|{
name|controlPoints
operator|=
name|ctrlPnts
operator|.
name|clone
argument_list|()
expr_stmt|;
name|level
operator|=
name|l
expr_stmt|;
name|curve
operator|=
name|getPoints
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get level parameter      *      * @return level      */
name|int
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
comment|// calculate sampled points on the cubic Bezier curve defined by the 4 given control points
specifier|private
name|Point2D
index|[]
name|getPoints
parameter_list|(
name|int
name|l
parameter_list|)
block|{
if|if
condition|(
name|l
operator|<
literal|0
condition|)
block|{
name|l
operator|=
literal|0
expr_stmt|;
block|}
name|int
name|sz
init|=
operator|(
literal|1
operator|<<
name|l
operator|)
operator|+
literal|1
decl_stmt|;
name|Point2D
index|[]
name|res
init|=
operator|new
name|Point2D
index|[
name|sz
index|]
decl_stmt|;
name|double
name|step
init|=
operator|(
name|double
operator|)
literal|1
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
name|double
name|tmpX
init|=
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
operator|*
name|controlPoints
index|[
literal|0
index|]
operator|.
name|getX
argument_list|()
operator|+
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
operator|*
name|controlPoints
index|[
literal|1
index|]
operator|.
name|getX
argument_list|()
operator|+
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
operator|*
name|controlPoints
index|[
literal|2
index|]
operator|.
name|getX
argument_list|()
operator|+
name|t
operator|*
name|t
operator|*
name|t
operator|*
name|controlPoints
index|[
literal|3
index|]
operator|.
name|getX
argument_list|()
decl_stmt|;
name|double
name|tmpY
init|=
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
operator|*
name|controlPoints
index|[
literal|0
index|]
operator|.
name|getY
argument_list|()
operator|+
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
operator|*
name|controlPoints
index|[
literal|1
index|]
operator|.
name|getY
argument_list|()
operator|+
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
operator|*
name|controlPoints
index|[
literal|2
index|]
operator|.
name|getY
argument_list|()
operator|+
name|t
operator|*
name|t
operator|*
name|t
operator|*
name|controlPoints
index|[
literal|3
index|]
operator|.
name|getY
argument_list|()
decl_stmt|;
name|res
index|[
name|i
index|]
operator|=
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|tmpX
argument_list|,
name|tmpY
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
comment|/**      * Get sampled points of this cubic Bezier curve.      *      * @return sampled points      */
name|Point2D
index|[]
name|getCubicBezierCurve
parameter_list|()
block|{
return|return
name|curve
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Point2D
name|p
range|:
name|controlPoints
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
return|return
literal|"Cubic Bezier curve{control points p0, p1, p2, p3: "
operator|+
name|sb
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

