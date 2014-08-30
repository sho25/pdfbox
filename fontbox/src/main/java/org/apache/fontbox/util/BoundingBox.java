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
name|fontbox
operator|.
name|util
package|;
end_package

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
comment|/**  * This is an implementation of a bounding box.  This was originally written for the  * AMF parser.  *  * @author Ben Litchfield (ben@benlitchfield.com)  */
end_comment

begin_class
specifier|public
class|class
name|BoundingBox
block|{
specifier|private
name|float
name|lowerLeftX
decl_stmt|;
specifier|private
name|float
name|lowerLeftY
decl_stmt|;
specifier|private
name|float
name|upperRightX
decl_stmt|;
specifier|private
name|float
name|upperRightY
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|BoundingBox
parameter_list|()
block|{     }
comment|/**      * Constructor.      *       * @param minX lower left x value      * @param minY lower left y value      * @param maxX upper right x value      * @param maxY upper right y value      */
specifier|public
name|BoundingBox
parameter_list|(
name|float
name|minX
parameter_list|,
name|float
name|minY
parameter_list|,
name|float
name|maxX
parameter_list|,
name|float
name|maxY
parameter_list|)
block|{
name|lowerLeftX
operator|=
name|minX
expr_stmt|;
name|lowerLeftY
operator|=
name|minY
expr_stmt|;
name|upperRightX
operator|=
name|maxX
expr_stmt|;
name|upperRightY
operator|=
name|maxY
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param numbers list of four numbers      */
specifier|public
name|BoundingBox
parameter_list|(
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
parameter_list|)
block|{
name|lowerLeftX
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|lowerLeftY
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|upperRightX
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|upperRightY
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
comment|/**      * Getter for property lowerLeftX.      *      * @return Value of property lowerLeftX.      */
specifier|public
name|float
name|getLowerLeftX
parameter_list|()
block|{
return|return
name|lowerLeftX
return|;
block|}
comment|/**      * Setter for property lowerLeftX.      *      * @param lowerLeftXValue New value of property lowerLeftX.      */
specifier|public
name|void
name|setLowerLeftX
parameter_list|(
name|float
name|lowerLeftXValue
parameter_list|)
block|{
name|this
operator|.
name|lowerLeftX
operator|=
name|lowerLeftXValue
expr_stmt|;
block|}
comment|/**      * Getter for property lowerLeftY.      *      * @return Value of property lowerLeftY.      */
specifier|public
name|float
name|getLowerLeftY
parameter_list|()
block|{
return|return
name|lowerLeftY
return|;
block|}
comment|/**      * Setter for property lowerLeftY.      *      * @param lowerLeftYValue New value of property lowerLeftY.      */
specifier|public
name|void
name|setLowerLeftY
parameter_list|(
name|float
name|lowerLeftYValue
parameter_list|)
block|{
name|this
operator|.
name|lowerLeftY
operator|=
name|lowerLeftYValue
expr_stmt|;
block|}
comment|/**      * Getter for property upperRightX.      *      * @return Value of property upperRightX.      */
specifier|public
name|float
name|getUpperRightX
parameter_list|()
block|{
return|return
name|upperRightX
return|;
block|}
comment|/**      * Setter for property upperRightX.      *      * @param upperRightXValue New value of property upperRightX.      */
specifier|public
name|void
name|setUpperRightX
parameter_list|(
name|float
name|upperRightXValue
parameter_list|)
block|{
name|this
operator|.
name|upperRightX
operator|=
name|upperRightXValue
expr_stmt|;
block|}
comment|/**      * Getter for property upperRightY.      *      * @return Value of property upperRightY.      */
specifier|public
name|float
name|getUpperRightY
parameter_list|()
block|{
return|return
name|upperRightY
return|;
block|}
comment|/**      * Setter for property upperRightY.      *      * @param upperRightYValue New value of property upperRightY.      */
specifier|public
name|void
name|setUpperRightY
parameter_list|(
name|float
name|upperRightYValue
parameter_list|)
block|{
name|this
operator|.
name|upperRightY
operator|=
name|upperRightYValue
expr_stmt|;
block|}
comment|/**      * This will get the width of this rectangle as calculated by      * upperRightX - lowerLeftX.      *      * @return The width of this rectangle.      */
specifier|public
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|getUpperRightX
argument_list|()
operator|-
name|getLowerLeftX
argument_list|()
return|;
block|}
comment|/**      * This will get the height of this rectangle as calculated by      * upperRightY - lowerLeftY.      *      * @return The height of this rectangle.      */
specifier|public
name|float
name|getHeight
parameter_list|()
block|{
return|return
name|getUpperRightY
argument_list|()
operator|-
name|getLowerLeftY
argument_list|()
return|;
block|}
comment|/**      * Checks if a point is inside this rectangle.      *       * @param x The x coordinate.      * @param y The y coordinate.      *       * @return true If the point is on the edge or inside the rectangle bounds.       */
specifier|public
name|boolean
name|contains
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
block|{
return|return
name|x
operator|>=
name|lowerLeftX
operator|&&
name|x
operator|<=
name|upperRightX
operator|&&
name|y
operator|>=
name|lowerLeftY
operator|&&
name|y
operator|<=
name|upperRightY
return|;
block|}
comment|/**      * This will return a string representation of this rectangle.      *      * @return This object as a string.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"["
operator|+
name|getLowerLeftX
argument_list|()
operator|+
literal|","
operator|+
name|getLowerLeftY
argument_list|()
operator|+
literal|","
operator|+
name|getUpperRightX
argument_list|()
operator|+
literal|","
operator|+
name|getUpperRightY
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|/**      * Unions the given bounding boxes and puts the result into the       * specified result bounding box.      *       * @param bBox1 the first bounding box to be combined with each other      * @param bBox2 the second bounding box to be combined with each other      * @param result the bounding box that holds the results of the union      *       */
specifier|public
specifier|static
name|void
name|union
parameter_list|(
name|BoundingBox
name|bBox1
parameter_list|,
name|BoundingBox
name|bBox2
parameter_list|,
name|BoundingBox
name|result
parameter_list|)
block|{
name|float
name|x1
init|=
name|Math
operator|.
name|min
argument_list|(
name|bBox1
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|bBox2
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|y1
init|=
name|Math
operator|.
name|min
argument_list|(
name|bBox1
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|bBox2
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|x2
init|=
name|Math
operator|.
name|max
argument_list|(
name|bBox1
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
name|bBox2
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|y2
init|=
name|Math
operator|.
name|max
argument_list|(
name|bBox1
operator|.
name|getUpperRightY
argument_list|()
argument_list|,
name|bBox2
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|x2
operator|<
name|x1
condition|)
block|{
name|float
name|temp
init|=
name|x1
decl_stmt|;
name|x1
operator|=
name|x2
expr_stmt|;
name|x2
operator|=
name|temp
expr_stmt|;
block|}
if|if
condition|(
name|y2
operator|<
name|y1
condition|)
block|{
name|float
name|temp
init|=
name|y1
decl_stmt|;
name|y1
operator|=
name|y2
expr_stmt|;
name|y2
operator|=
name|temp
expr_stmt|;
block|}
name|result
operator|=
operator|new
name|BoundingBox
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|,
name|x2
argument_list|,
name|y2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

