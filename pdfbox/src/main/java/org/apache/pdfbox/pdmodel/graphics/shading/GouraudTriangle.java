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
name|geom
operator|.
name|Point2D
import|;
end_import

begin_comment
comment|/**  * A Gouraud triangle, used in Type 4 and Type 5 shadings.  * @author Tilman Hausherr  */
end_comment

begin_class
class|class
name|GouraudTriangle
block|{
comment|/** point A of the triangle. */
specifier|protected
specifier|final
name|Point2D
name|pointA
decl_stmt|;
comment|/** point B of the triangle. */
specifier|protected
specifier|final
name|Point2D
name|pointB
decl_stmt|;
comment|/** point C of the triangle. */
specifier|protected
specifier|final
name|Point2D
name|pointC
decl_stmt|;
comment|/** the color of point A. */
specifier|protected
specifier|final
name|float
index|[]
name|colorA
decl_stmt|;
comment|/** the color of point B. */
specifier|protected
specifier|final
name|float
index|[]
name|colorB
decl_stmt|;
comment|/** the color of point C. */
specifier|protected
specifier|final
name|float
index|[]
name|colorC
decl_stmt|;
comment|// intermediate constants
specifier|private
specifier|final
name|double
name|xBminusA
decl_stmt|;
specifier|private
specifier|final
name|double
name|yBminusA
decl_stmt|;
specifier|private
specifier|final
name|double
name|xCminusA
decl_stmt|;
specifier|private
specifier|final
name|double
name|yCminusA
decl_stmt|;
specifier|private
specifier|final
name|double
name|xCminusB
decl_stmt|;
specifier|private
specifier|final
name|double
name|yCminusB
decl_stmt|;
specifier|private
specifier|final
name|double
name|area
decl_stmt|;
comment|/**      * Constructor for using 3 points and their colors.      * @param a point A of the triangle      * @param aColor color of point A      * @param b point B of the triangle      * @param bColor color of point B      * @param c point C of the triangle      * @param cColor color of point C      */
specifier|public
name|GouraudTriangle
parameter_list|(
name|Point2D
name|a
parameter_list|,
name|float
index|[]
name|aColor
parameter_list|,
name|Point2D
name|b
parameter_list|,
name|float
index|[]
name|bColor
parameter_list|,
name|Point2D
name|c
parameter_list|,
name|float
index|[]
name|cColor
parameter_list|)
block|{
name|pointA
operator|=
name|a
expr_stmt|;
name|pointB
operator|=
name|b
expr_stmt|;
name|pointC
operator|=
name|c
expr_stmt|;
name|colorA
operator|=
name|aColor
expr_stmt|;
name|colorB
operator|=
name|bColor
expr_stmt|;
name|colorC
operator|=
name|cColor
expr_stmt|;
comment|// calculate constants
name|xBminusA
operator|=
name|pointB
operator|.
name|getX
argument_list|()
operator|-
name|pointA
operator|.
name|getX
argument_list|()
expr_stmt|;
name|yBminusA
operator|=
name|pointB
operator|.
name|getY
argument_list|()
operator|-
name|pointA
operator|.
name|getY
argument_list|()
expr_stmt|;
name|xCminusA
operator|=
name|pointC
operator|.
name|getX
argument_list|()
operator|-
name|pointA
operator|.
name|getX
argument_list|()
expr_stmt|;
name|yCminusA
operator|=
name|pointC
operator|.
name|getY
argument_list|()
operator|-
name|pointA
operator|.
name|getY
argument_list|()
expr_stmt|;
name|xCminusB
operator|=
name|pointC
operator|.
name|getX
argument_list|()
operator|-
name|pointB
operator|.
name|getX
argument_list|()
expr_stmt|;
name|yCminusB
operator|=
name|pointC
operator|.
name|getY
argument_list|()
operator|-
name|pointB
operator|.
name|getY
argument_list|()
expr_stmt|;
name|area
operator|=
name|getArea
argument_list|(
name|pointA
argument_list|,
name|pointB
argument_list|,
name|pointC
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check whether the point is within the triangle.      * @param p Point      * @return true if yes, false if no      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
comment|// inspiration:
comment|// http://stackoverflow.com/a/9755252/535646
comment|// see also:
comment|// http://math.stackexchange.com/q/51326
comment|// http://www.gamedev.net/topic/295943-is-this-a-better-point-in-triangle-test-2d/
comment|// java function can't be used because java polygon class takes integer coordinates
name|double
name|xPminusA
init|=
name|p
operator|.
name|getX
argument_list|()
operator|-
name|pointA
operator|.
name|getX
argument_list|()
decl_stmt|;
name|double
name|yPminusA
init|=
name|p
operator|.
name|getY
argument_list|()
operator|-
name|pointA
operator|.
name|getY
argument_list|()
decl_stmt|;
name|boolean
name|signAB
init|=
operator|(
name|xBminusA
operator|*
name|yPminusA
operator|-
name|yBminusA
operator|*
name|xPminusA
operator|)
operator|>
literal|0
decl_stmt|;
if|if
condition|(
operator|(
name|xCminusA
operator|*
name|yPminusA
operator|-
name|yCminusA
operator|*
name|xPminusA
operator|>
literal|0
operator|)
operator|==
name|signAB
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
name|xCminusB
operator|*
operator|(
name|p
operator|.
name|getY
argument_list|()
operator|-
name|pointB
operator|.
name|getY
argument_list|()
operator|)
operator|-
name|yCminusB
operator|*
operator|(
name|p
operator|.
name|getX
argument_list|()
operator|-
name|pointB
operator|.
name|getX
argument_list|()
operator|)
operator|>
literal|0
operator|)
operator|==
name|signAB
return|;
block|}
comment|// returns the area of a triangle
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
comment|// inspiration: http://stackoverflow.com/a/2145584/535646
comment|// test: http://www.mathopenref.com/coordtrianglearea.html
return|return
name|Math
operator|.
name|abs
argument_list|(
operator|(
name|a
operator|.
name|getX
argument_list|()
operator|-
name|c
operator|.
name|getX
argument_list|()
operator|)
operator|*
operator|(
name|b
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
name|a
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
argument_list|)
operator|/
literal|2
return|;
block|}
comment|/**      * Tell whether a triangle is empty.      * @return true if the area is empty, false if not      */
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|area
operator|==
literal|0
return|;
block|}
comment|/**      * calculate color weights with barycentric interpolation.      * @param p point within triangle      * @return array of weights (between 0 and 1) for a b c      */
specifier|public
name|double
index|[]
name|getWeights
parameter_list|(
name|Point2D
name|p
parameter_list|)
block|{
comment|// http://classes.soe.ucsc.edu/cmps160/Fall10/resources/barycentricInterpolation.pdf
return|return
operator|new
name|double
index|[]
block|{
name|getArea
argument_list|(
name|pointB
argument_list|,
name|pointC
argument_list|,
name|p
argument_list|)
operator|/
name|area
block|,
name|getArea
argument_list|(
name|pointA
argument_list|,
name|pointC
argument_list|,
name|p
argument_list|)
operator|/
name|area
block|,
name|getArea
argument_list|(
name|pointA
argument_list|,
name|pointB
argument_list|,
name|p
argument_list|)
operator|/
name|area
block|}
return|;
block|}
block|}
end_class

end_unit

