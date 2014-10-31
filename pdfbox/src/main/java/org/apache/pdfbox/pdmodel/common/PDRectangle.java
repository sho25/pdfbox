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
name|common
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
name|GeneralPath
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
name|COSBase
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
name|COSFloat
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
name|COSNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
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
comment|/**  * A rectangle in a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDRectangle
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|static
specifier|final
name|float
name|POINTS_PER_INCH
init|=
literal|72
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|float
name|MM_PER_INCH
init|=
literal|1
operator|/
operator|(
literal|10
operator|*
literal|2.54f
operator|)
operator|*
name|POINTS_PER_INCH
decl_stmt|;
comment|/** A rectangle the size of U.S. Letter, 8.5" x 11". */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|LETTER
init|=
operator|new
name|PDRectangle
argument_list|(
literal|8.5f
operator|*
name|POINTS_PER_INCH
argument_list|,
literal|11f
operator|*
name|POINTS_PER_INCH
argument_list|)
decl_stmt|;
comment|/**  A rectangle the size of U.S. Legal, 8.5" x 14". */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|LEGAL
init|=
operator|new
name|PDRectangle
argument_list|(
literal|8.5f
operator|*
name|POINTS_PER_INCH
argument_list|,
literal|14f
operator|*
name|POINTS_PER_INCH
argument_list|)
decl_stmt|;
comment|/**  A rectangle the size of A0 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A0
init|=
operator|new
name|PDRectangle
argument_list|(
literal|841
operator|*
name|MM_PER_INCH
argument_list|,
literal|1189
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/** A rectangle the size of A1 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A1
init|=
operator|new
name|PDRectangle
argument_list|(
literal|594
operator|*
name|MM_PER_INCH
argument_list|,
literal|841
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/**  A rectangle the size of A2 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A2
init|=
operator|new
name|PDRectangle
argument_list|(
literal|420
operator|*
name|MM_PER_INCH
argument_list|,
literal|594
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/** A rectangle the size of A3 Paper.  */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A3
init|=
operator|new
name|PDRectangle
argument_list|(
literal|297
operator|*
name|MM_PER_INCH
argument_list|,
literal|420
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/**  A rectangle the size of A4 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A4
init|=
operator|new
name|PDRectangle
argument_list|(
literal|210
operator|*
name|MM_PER_INCH
argument_list|,
literal|297
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/** A rectangle the size of A5 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A5
init|=
operator|new
name|PDRectangle
argument_list|(
literal|148
operator|*
name|MM_PER_INCH
argument_list|,
literal|210
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
comment|/**  A rectangle the size of A6 Paper. */
specifier|public
specifier|static
specifier|final
name|PDRectangle
name|A6
init|=
operator|new
name|PDRectangle
argument_list|(
literal|105
operator|*
name|MM_PER_INCH
argument_list|,
literal|148
operator|*
name|MM_PER_INCH
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|COSArray
name|rectArray
decl_stmt|;
comment|/**      * Constructor.      *      * Initializes to 0,0,0,0      */
specifier|public
name|PDRectangle
parameter_list|()
block|{
name|this
argument_list|(
literal|0.0f
argument_list|,
literal|0.0f
argument_list|,
literal|0.0f
argument_list|,
literal|0.0f
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param width The width of the rectangle.      * @param height The height of the rectangle.      */
specifier|public
name|PDRectangle
parameter_list|(
name|float
name|width
parameter_list|,
name|float
name|height
parameter_list|)
block|{
name|this
argument_list|(
literal|0.0f
argument_list|,
literal|0.0f
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param x the x coordinate of the rectangle      * @param y the y coordinate of the rectangle      * @param width The width of the rectangle.      * @param height The height of the rectangle.      */
specifier|public
name|PDRectangle
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|width
parameter_list|,
name|float
name|height
parameter_list|)
block|{
name|rectArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|y
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|x
operator|+
name|width
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|y
operator|+
name|height
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param box the bounding box to be used for the rectangle      */
specifier|public
name|PDRectangle
parameter_list|(
name|BoundingBox
name|box
parameter_list|)
block|{
name|rectArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|box
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|box
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|box
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|box
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param array An array of numbers as specified in the PDF Reference for a rectangle type.      */
specifier|public
name|PDRectangle
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
name|float
index|[]
name|values
init|=
name|array
operator|.
name|toFloatArray
argument_list|()
decl_stmt|;
name|rectArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
comment|// we have to start with the lower left corner
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|,
name|values
index|[
literal|2
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|values
index|[
literal|1
index|]
argument_list|,
name|values
index|[
literal|3
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|,
name|values
index|[
literal|2
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|rectArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|values
index|[
literal|1
index|]
argument_list|,
name|values
index|[
literal|3
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method to determine if the x/y point is inside this rectangle.      * @param x The x-coordinate to test.      * @param y The y-coordinate to test.      * @return True if the point is inside this rectangle.      */
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
name|float
name|llx
init|=
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|urx
init|=
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|lly
init|=
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|ury
init|=
name|getUpperRightY
argument_list|()
decl_stmt|;
return|return
name|x
operator|>=
name|llx
operator|&&
name|x
operator|<=
name|urx
operator|&&
name|y
operator|>=
name|lly
operator|&&
name|y
operator|<=
name|ury
return|;
block|}
comment|/**      * This will create a translated rectangle based off of this rectangle, such      * that the new rectangle retains the same dimensions(height/width), but the      * lower left x,y values are zero.<br />      * 100, 100, 400, 400 (llx, lly, urx, ury )<br />      * will be translated to 0,0,300,300      *      * @return A new rectangle that has been translated back to the origin.      */
specifier|public
name|PDRectangle
name|createRetranslatedRectangle
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|retval
operator|.
name|setUpperRightX
argument_list|(
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setUpperRightY
argument_list|(
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * This will get the underlying array for this rectangle.      *      * @return The cos array.      */
specifier|public
name|COSArray
name|getCOSArray
parameter_list|()
block|{
return|return
name|rectArray
return|;
block|}
comment|/**      * This will get the lower left x coordinate.      *      * @return The lower left x.      */
specifier|public
name|float
name|getLowerLeftX
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|rectArray
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the lower left x coordinate.      *      * @param value The lower left x.      */
specifier|public
name|void
name|setLowerLeftX
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|rectArray
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the lower left y coordinate.      *      * @return The lower left y.      */
specifier|public
name|float
name|getLowerLeftY
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|rectArray
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the lower left y coordinate.      *      * @param value The lower left y.      */
specifier|public
name|void
name|setLowerLeftY
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|rectArray
operator|.
name|set
argument_list|(
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the upper right x coordinate.      *      * @return The upper right x .      */
specifier|public
name|float
name|getUpperRightX
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|rectArray
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the upper right x coordinate.      *      * @param value The upper right x .      */
specifier|public
name|void
name|setUpperRightX
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|rectArray
operator|.
name|set
argument_list|(
literal|2
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the upper right y coordinate.      *      * @return The upper right y.      */
specifier|public
name|float
name|getUpperRightY
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|rectArray
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the upper right y coordinate.      *      * @param value The upper right y.      */
specifier|public
name|void
name|setUpperRightY
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|rectArray
operator|.
name|set
argument_list|(
literal|3
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
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
comment|/**      * Returns a copy of this rectangle which has been transformed using the given matrix.      */
specifier|public
name|PDRectangle
name|transform
parameter_list|(
name|Matrix
name|matrix
parameter_list|)
block|{
name|Point2D
operator|.
name|Float
name|lowerLeft
init|=
name|matrix
operator|.
name|transformPoint
argument_list|(
name|getLowerLeftX
argument_list|()
argument_list|,
name|getLowerLeftY
argument_list|()
argument_list|)
decl_stmt|;
name|Point2D
operator|.
name|Float
name|upperRight
init|=
name|matrix
operator|.
name|transformPoint
argument_list|(
name|getUpperRightX
argument_list|()
argument_list|,
name|getUpperRightY
argument_list|()
argument_list|)
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|lowerLeft
operator|.
name|x
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|lowerLeft
operator|.
name|y
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|upperRight
operator|.
name|x
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|upperRight
operator|.
name|y
argument_list|)
expr_stmt|;
return|return
name|rect
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|rectArray
return|;
block|}
comment|/**      * Returns a general path equivalent to this rectangle. This method avoids the problems      * caused by Rectangle2D not working well with -ve rectangles.      */
specifier|public
name|GeneralPath
name|toGeneralPath
parameter_list|()
block|{
name|float
name|x1
init|=
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y1
init|=
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|x2
init|=
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|y2
init|=
name|getUpperRightY
argument_list|()
decl_stmt|;
name|GeneralPath
name|path
init|=
operator|new
name|GeneralPath
argument_list|()
decl_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|x2
argument_list|,
name|y1
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|x2
argument_list|,
name|y2
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
name|x1
argument_list|,
name|y2
argument_list|)
expr_stmt|;
name|path
operator|.
name|closePath
argument_list|()
expr_stmt|;
return|return
name|path
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
block|}
end_class

end_unit

