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
name|util
package|;
end_package

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

begin_comment
comment|/**  * This class will be used for matrix manipulation.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.14 $  */
end_comment

begin_class
specifier|public
class|class
name|Matrix
implements|implements
name|Cloneable
block|{
specifier|static
specifier|final
name|float
index|[]
name|DEFAULT_SINGLE
init|=
block|{
literal|1
block|,
literal|0
block|,
literal|0
block|,
comment|//  a  b  0
literal|0
block|,
literal|1
block|,
literal|0
block|,
comment|//  c  d  0
literal|0
block|,
literal|0
block|,
literal|1
comment|//  tx ty 1
block|}
decl_stmt|;
specifier|private
name|float
index|[]
name|single
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|Matrix
parameter_list|()
block|{
name|single
operator|=
operator|new
name|float
index|[
name|DEFAULT_SINGLE
operator|.
name|length
index|]
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      */
specifier|public
name|Matrix
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
name|single
operator|=
operator|new
name|float
index|[
name|DEFAULT_SINGLE
operator|.
name|length
index|]
expr_stmt|;
name|single
index|[
literal|0
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|single
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|single
index|[
literal|3
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|single
index|[
literal|4
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|single
index|[
literal|6
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|single
index|[
literal|7
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      */
specifier|public
name|Matrix
parameter_list|(
name|float
name|a
parameter_list|,
name|float
name|b
parameter_list|,
name|float
name|c
parameter_list|,
name|float
name|d
parameter_list|,
name|float
name|e
parameter_list|,
name|float
name|f
parameter_list|)
block|{
name|single
operator|=
operator|new
name|float
index|[
name|DEFAULT_SINGLE
operator|.
name|length
index|]
expr_stmt|;
name|single
index|[
literal|0
index|]
operator|=
name|a
expr_stmt|;
name|single
index|[
literal|1
index|]
operator|=
name|b
expr_stmt|;
name|single
index|[
literal|3
index|]
operator|=
name|c
expr_stmt|;
name|single
index|[
literal|4
index|]
operator|=
name|d
expr_stmt|;
name|single
index|[
literal|6
index|]
operator|=
name|e
expr_stmt|;
name|single
index|[
literal|7
index|]
operator|=
name|f
expr_stmt|;
name|single
index|[
literal|8
index|]
operator|=
literal|1
expr_stmt|;
block|}
comment|/**      * This method resets the numbers in this Matrix to the original values, which are      * the values that a newly constructed Matrix would have.      */
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|DEFAULT_SINGLE
argument_list|,
literal|0
argument_list|,
name|single
argument_list|,
literal|0
argument_list|,
name|DEFAULT_SINGLE
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create an affine transform from this matrix's values.      *      * @return An affine transform with this matrix's values.      */
specifier|public
name|AffineTransform
name|createAffineTransform
parameter_list|()
block|{
name|AffineTransform
name|retval
init|=
operator|new
name|AffineTransform
argument_list|(
name|single
index|[
literal|0
index|]
argument_list|,
name|single
index|[
literal|1
index|]
argument_list|,
name|single
index|[
literal|3
index|]
argument_list|,
name|single
index|[
literal|4
index|]
argument_list|,
name|single
index|[
literal|6
index|]
argument_list|,
name|single
index|[
literal|7
index|]
argument_list|)
decl_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Set the values of the matrix from the AffineTransform.      *      * @param af The transform to get the values from.      */
specifier|public
name|void
name|setFromAffineTransform
parameter_list|(
name|AffineTransform
name|af
parameter_list|)
block|{
name|single
index|[
literal|0
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getScaleX
argument_list|()
expr_stmt|;
name|single
index|[
literal|1
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getShearY
argument_list|()
expr_stmt|;
name|single
index|[
literal|3
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getShearX
argument_list|()
expr_stmt|;
name|single
index|[
literal|4
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getScaleY
argument_list|()
expr_stmt|;
name|single
index|[
literal|6
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getTranslateX
argument_list|()
expr_stmt|;
name|single
index|[
literal|7
index|]
operator|=
operator|(
name|float
operator|)
name|af
operator|.
name|getTranslateY
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will get a matrix value at some point.      *      * @param row The row to get the value from.      * @param column The column to get the value from.      *      * @return The value at the row/column position.      */
specifier|public
name|float
name|getValue
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
return|return
name|single
index|[
name|row
operator|*
literal|3
operator|+
name|column
index|]
return|;
block|}
comment|/**      * This will set a value at a position.      *      * @param row The row to set the value at.      * @param column the column to set the value at.      * @param value The value to set at the position.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|,
name|float
name|value
parameter_list|)
block|{
name|single
index|[
name|row
operator|*
literal|3
operator|+
name|column
index|]
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Return a single dimension array of all values in the matrix.      *      * @return The values ot this matrix.      */
specifier|public
name|float
index|[]
index|[]
name|getValues
parameter_list|()
block|{
name|float
index|[]
index|[]
name|retval
init|=
operator|new
name|float
index|[
literal|3
index|]
index|[
literal|3
index|]
decl_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|0
index|]
expr_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|1
index|]
expr_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|2
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|3
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|4
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|5
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|6
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|7
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|8
index|]
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Return a single dimension array of all values in the matrix.      *      * @return The values ot this matrix.      */
specifier|public
name|double
index|[]
index|[]
name|getValuesAsDouble
parameter_list|()
block|{
name|double
index|[]
index|[]
name|retval
init|=
operator|new
name|double
index|[
literal|3
index|]
index|[
literal|3
index|]
decl_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|0
index|]
expr_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|1
index|]
expr_stmt|;
name|retval
index|[
literal|0
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|2
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|3
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|4
index|]
expr_stmt|;
name|retval
index|[
literal|1
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|5
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|0
index|]
operator|=
name|single
index|[
literal|6
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|1
index|]
operator|=
name|single
index|[
literal|7
index|]
expr_stmt|;
name|retval
index|[
literal|2
index|]
index|[
literal|2
index|]
operator|=
name|single
index|[
literal|8
index|]
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Concatenates (premultiplies) the given matrix to this matrix.      *      * @param matrix The matrix to concatenate.      */
specifier|public
name|void
name|concatenate
parameter_list|(
name|Matrix
name|matrix
parameter_list|)
block|{
name|matrix
operator|.
name|multiply
argument_list|(
name|this
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Translates this matrix by the given vector.      *      * @param vector 2D vector      */
specifier|public
name|void
name|translate
parameter_list|(
name|Vector
name|vector
parameter_list|)
block|{
name|Matrix
name|m
init|=
name|Matrix
operator|.
name|getTranslatingInstance
argument_list|(
name|vector
operator|.
name|getX
argument_list|()
argument_list|,
name|vector
operator|.
name|getY
argument_list|()
argument_list|)
decl_stmt|;
name|concatenate
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will take the current matrix and multipy it with a matrix that is passed in.      *      * @param b The matrix to multiply by.      *      * @return The result of the two multiplied matrices.      */
specifier|public
name|Matrix
name|multiply
parameter_list|(
name|Matrix
name|b
parameter_list|)
block|{
return|return
name|this
operator|.
name|multiply
argument_list|(
name|b
argument_list|,
operator|new
name|Matrix
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This method multiplies this Matrix with the specified other Matrix, storing the product in the specified      * result Matrix. By reusing Matrix instances like this, multiplication chains can be executed without having      * to create many temporary Matrix objects.      *<p/>      * It is allowed to have (other == this) or (result == this) or indeed (other == result) but if this is done,      * the backing float[] matrix values may be copied in order to ensure a correct product.      *      * @param other the second operand Matrix in the multiplication      * @param result the Matrix instance into which the result should be stored. If result is null, a new Matrix      *               instance is created.      * @return the product of the two matrices.      */
specifier|public
name|Matrix
name|multiply
parameter_list|(
name|Matrix
name|other
parameter_list|,
name|Matrix
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|other
operator|!=
literal|null
operator|&&
name|other
operator|.
name|single
operator|!=
literal|null
condition|)
block|{
comment|// the operands
name|float
index|[]
name|thisOperand
init|=
name|this
operator|.
name|single
decl_stmt|;
name|float
index|[]
name|otherOperand
init|=
name|other
operator|.
name|single
decl_stmt|;
comment|// We're multiplying 2 sets of floats together to produce a third, but we allow
comment|// any of these float[] instances to be the same objects.
comment|// There is the possibility then to overwrite one of the operands with result values
comment|// and therefore corrupt the result.
comment|// If either of these operands are the same float[] instance as the result, then
comment|// they need to be copied.
if|if
condition|(
name|this
operator|==
name|result
condition|)
block|{
specifier|final
name|float
index|[]
name|thisOrigVals
init|=
operator|new
name|float
index|[
name|this
operator|.
name|single
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|this
operator|.
name|single
argument_list|,
literal|0
argument_list|,
name|thisOrigVals
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|single
operator|.
name|length
argument_list|)
expr_stmt|;
name|thisOperand
operator|=
name|thisOrigVals
expr_stmt|;
block|}
if|if
condition|(
name|other
operator|==
name|result
condition|)
block|{
specifier|final
name|float
index|[]
name|otherOrigVals
init|=
operator|new
name|float
index|[
name|other
operator|.
name|single
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|other
operator|.
name|single
argument_list|,
literal|0
argument_list|,
name|otherOrigVals
argument_list|,
literal|0
argument_list|,
name|other
operator|.
name|single
operator|.
name|length
argument_list|)
expr_stmt|;
name|otherOperand
operator|=
name|otherOrigVals
expr_stmt|;
block|}
name|result
operator|.
name|single
index|[
literal|0
index|]
operator|=
name|thisOperand
index|[
literal|0
index|]
operator|*
name|otherOperand
index|[
literal|0
index|]
operator|+
name|thisOperand
index|[
literal|1
index|]
operator|*
name|otherOperand
index|[
literal|3
index|]
operator|+
name|thisOperand
index|[
literal|2
index|]
operator|*
name|otherOperand
index|[
literal|6
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|1
index|]
operator|=
name|thisOperand
index|[
literal|0
index|]
operator|*
name|otherOperand
index|[
literal|1
index|]
operator|+
name|thisOperand
index|[
literal|1
index|]
operator|*
name|otherOperand
index|[
literal|4
index|]
operator|+
name|thisOperand
index|[
literal|2
index|]
operator|*
name|otherOperand
index|[
literal|7
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|2
index|]
operator|=
name|thisOperand
index|[
literal|0
index|]
operator|*
name|otherOperand
index|[
literal|2
index|]
operator|+
name|thisOperand
index|[
literal|1
index|]
operator|*
name|otherOperand
index|[
literal|5
index|]
operator|+
name|thisOperand
index|[
literal|2
index|]
operator|*
name|otherOperand
index|[
literal|8
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|3
index|]
operator|=
name|thisOperand
index|[
literal|3
index|]
operator|*
name|otherOperand
index|[
literal|0
index|]
operator|+
name|thisOperand
index|[
literal|4
index|]
operator|*
name|otherOperand
index|[
literal|3
index|]
operator|+
name|thisOperand
index|[
literal|5
index|]
operator|*
name|otherOperand
index|[
literal|6
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|4
index|]
operator|=
name|thisOperand
index|[
literal|3
index|]
operator|*
name|otherOperand
index|[
literal|1
index|]
operator|+
name|thisOperand
index|[
literal|4
index|]
operator|*
name|otherOperand
index|[
literal|4
index|]
operator|+
name|thisOperand
index|[
literal|5
index|]
operator|*
name|otherOperand
index|[
literal|7
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|5
index|]
operator|=
name|thisOperand
index|[
literal|3
index|]
operator|*
name|otherOperand
index|[
literal|2
index|]
operator|+
name|thisOperand
index|[
literal|4
index|]
operator|*
name|otherOperand
index|[
literal|5
index|]
operator|+
name|thisOperand
index|[
literal|5
index|]
operator|*
name|otherOperand
index|[
literal|8
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|6
index|]
operator|=
name|thisOperand
index|[
literal|6
index|]
operator|*
name|otherOperand
index|[
literal|0
index|]
operator|+
name|thisOperand
index|[
literal|7
index|]
operator|*
name|otherOperand
index|[
literal|3
index|]
operator|+
name|thisOperand
index|[
literal|8
index|]
operator|*
name|otherOperand
index|[
literal|6
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|7
index|]
operator|=
name|thisOperand
index|[
literal|6
index|]
operator|*
name|otherOperand
index|[
literal|1
index|]
operator|+
name|thisOperand
index|[
literal|7
index|]
operator|*
name|otherOperand
index|[
literal|4
index|]
operator|+
name|thisOperand
index|[
literal|8
index|]
operator|*
name|otherOperand
index|[
literal|7
index|]
expr_stmt|;
name|result
operator|.
name|single
index|[
literal|8
index|]
operator|=
name|thisOperand
index|[
literal|6
index|]
operator|*
name|otherOperand
index|[
literal|2
index|]
operator|+
name|thisOperand
index|[
literal|7
index|]
operator|*
name|otherOperand
index|[
literal|5
index|]
operator|+
name|thisOperand
index|[
literal|8
index|]
operator|*
name|otherOperand
index|[
literal|8
index|]
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Transforms the given point by this matrix.      *      * @param point point to transform      */
specifier|public
name|void
name|transform
parameter_list|(
name|Point2D
name|point
parameter_list|)
block|{
name|float
name|x
init|=
operator|(
name|float
operator|)
name|point
operator|.
name|getX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
operator|(
name|float
operator|)
name|point
operator|.
name|getY
argument_list|()
decl_stmt|;
name|float
name|a
init|=
name|single
index|[
literal|0
index|]
decl_stmt|;
name|float
name|b
init|=
name|single
index|[
literal|1
index|]
decl_stmt|;
name|float
name|c
init|=
name|single
index|[
literal|3
index|]
decl_stmt|;
name|float
name|d
init|=
name|single
index|[
literal|4
index|]
decl_stmt|;
name|float
name|e
init|=
name|single
index|[
literal|6
index|]
decl_stmt|;
name|float
name|f
init|=
name|single
index|[
literal|7
index|]
decl_stmt|;
name|point
operator|.
name|setLocation
argument_list|(
name|x
operator|*
name|a
operator|+
name|y
operator|*
name|c
operator|+
name|e
argument_list|,
name|x
operator|*
name|b
operator|+
name|y
operator|*
name|d
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
comment|/**      * Transforms the given point by this matrix.      *      * @param x x-coordinate      * @param y y-coordinate      */
specifier|public
name|Point2D
operator|.
name|Float
name|transformPoint
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
block|{
name|float
name|a
init|=
name|single
index|[
literal|0
index|]
decl_stmt|;
name|float
name|b
init|=
name|single
index|[
literal|1
index|]
decl_stmt|;
name|float
name|c
init|=
name|single
index|[
literal|3
index|]
decl_stmt|;
name|float
name|d
init|=
name|single
index|[
literal|4
index|]
decl_stmt|;
name|float
name|e
init|=
name|single
index|[
literal|6
index|]
decl_stmt|;
name|float
name|f
init|=
name|single
index|[
literal|7
index|]
decl_stmt|;
return|return
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
operator|*
name|a
operator|+
name|y
operator|*
name|c
operator|+
name|e
argument_list|,
name|x
operator|*
name|b
operator|+
name|y
operator|*
name|d
operator|+
name|f
argument_list|)
return|;
block|}
comment|/**      * Transforms the given point by this matrix.      *      * @param vector @2D vector      */
specifier|public
name|Vector
name|transform
parameter_list|(
name|Vector
name|vector
parameter_list|)
block|{
name|float
name|a
init|=
name|single
index|[
literal|0
index|]
decl_stmt|;
name|float
name|b
init|=
name|single
index|[
literal|1
index|]
decl_stmt|;
name|float
name|c
init|=
name|single
index|[
literal|3
index|]
decl_stmt|;
name|float
name|d
init|=
name|single
index|[
literal|4
index|]
decl_stmt|;
name|float
name|e
init|=
name|single
index|[
literal|6
index|]
decl_stmt|;
name|float
name|f
init|=
name|single
index|[
literal|7
index|]
decl_stmt|;
name|float
name|x
init|=
name|vector
operator|.
name|getX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
name|vector
operator|.
name|getY
argument_list|()
decl_stmt|;
return|return
operator|new
name|Vector
argument_list|(
name|x
operator|*
name|a
operator|+
name|y
operator|*
name|c
operator|+
name|e
argument_list|,
name|x
operator|*
name|b
operator|+
name|y
operator|*
name|d
operator|+
name|f
argument_list|)
return|;
block|}
comment|/**      * Create a new matrix with just the scaling operators.      *      * @return A new matrix with just the scaling operators.      */
specifier|public
name|Matrix
name|extractScaling
parameter_list|()
block|{
name|Matrix
name|retval
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|retval
operator|.
name|single
index|[
literal|0
index|]
operator|=
name|this
operator|.
name|single
index|[
literal|0
index|]
expr_stmt|;
name|retval
operator|.
name|single
index|[
literal|4
index|]
operator|=
name|this
operator|.
name|single
index|[
literal|4
index|]
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Convenience method to create a scaled instance.      *      * @param x The xscale operator.      * @param y The yscale operator.      * @return A new matrix with just the x/y scaling      */
specifier|public
specifier|static
name|Matrix
name|getScaleInstance
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
block|{
name|Matrix
name|retval
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|retval
operator|.
name|single
index|[
literal|0
index|]
operator|=
name|x
expr_stmt|;
name|retval
operator|.
name|single
index|[
literal|4
index|]
operator|=
name|y
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Create a new matrix with just the translating operators.      *      * @return A new matrix with just the translating operators.      */
specifier|public
name|Matrix
name|extractTranslating
parameter_list|()
block|{
name|Matrix
name|retval
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|retval
operator|.
name|single
index|[
literal|6
index|]
operator|=
name|this
operator|.
name|single
index|[
literal|6
index|]
expr_stmt|;
name|retval
operator|.
name|single
index|[
literal|7
index|]
operator|=
name|this
operator|.
name|single
index|[
literal|7
index|]
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Convenience method to create a translating instance.      *      * @param x The x translating operator.      * @param y The y translating operator.      * @return A new matrix with just the x/y translating.      */
specifier|public
specifier|static
name|Matrix
name|getTranslatingInstance
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
block|{
name|Matrix
name|retval
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|retval
operator|.
name|single
index|[
literal|6
index|]
operator|=
name|x
expr_stmt|;
name|retval
operator|.
name|single
index|[
literal|7
index|]
operator|=
name|y
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Produces a copy of the first matrix, with the second matrix concatenated.      *      * @param a The matrix to copy.      * @param b The matrix to concatenate.      */
specifier|public
specifier|static
name|Matrix
name|concatenate
parameter_list|(
name|Matrix
name|a
parameter_list|,
name|Matrix
name|b
parameter_list|)
block|{
name|Matrix
name|copy
init|=
name|a
operator|.
name|clone
argument_list|()
decl_stmt|;
name|copy
operator|.
name|concatenate
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**      * Clones this object.      * @return cloned matrix as an object.      */
annotation|@
name|Override
specifier|public
name|Matrix
name|clone
parameter_list|()
block|{
name|Matrix
name|clone
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|single
argument_list|,
literal|0
argument_list|,
name|clone
operator|.
name|single
argument_list|,
literal|0
argument_list|,
literal|9
argument_list|)
expr_stmt|;
return|return
name|clone
return|;
block|}
comment|/**      * This will return a string representation of the matrix.      *      * @return The matrix as a string.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|"[["
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|0
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|1
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|2
index|]
operator|+
literal|"]["
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|3
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|4
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|5
index|]
operator|+
literal|"]["
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|6
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|7
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|single
index|[
literal|8
index|]
operator|+
literal|"]]"
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Get the xscaling factor of this matrix.      * @return The x-scale.      */
specifier|public
name|float
name|getXScale
parameter_list|()
block|{
name|float
name|xScale
init|=
name|single
index|[
literal|0
index|]
decl_stmt|;
comment|/**          * BM: if the trm is rotated, the calculation is a little more complicated          *          * The rotation matrix multiplied with the scaling matrix is:          * (   x   0   0)    ( cos  sin  0)    ( x*cos x*sin   0)          * (   0   y   0) *  (-sin  cos  0)  = (-y*sin y*cos   0)          * (   0   0   1)    (   0    0  1)    (     0     0   1)          *          * So, if you want to deduce x from the matrix you take          * M(0,0) = x*cos and M(0,1) = x*sin and use the theorem of Pythagoras          *          * sqrt(M(0,0)^2+M(0,1)^2) =          * sqrt(x2*cos2+x2*sin2) =          * sqrt(x2*(cos2+sin2)) =<- here is the trick cos2+sin2 is one          * sqrt(x2) =          * abs(x)          */
if|if
condition|(
operator|!
operator|(
name|single
index|[
literal|1
index|]
operator|==
literal|0.0f
operator|&&
name|single
index|[
literal|3
index|]
operator|==
literal|0.0f
operator|)
condition|)
block|{
name|xScale
operator|=
operator|(
name|float
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
name|Math
operator|.
name|pow
argument_list|(
name|single
index|[
literal|0
index|]
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
name|single
index|[
literal|1
index|]
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|xScale
return|;
block|}
comment|/**      * Get the y scaling factor of this matrix.      * @return The y-scale factor.      */
specifier|public
name|float
name|getYScale
parameter_list|()
block|{
name|float
name|yScale
init|=
name|single
index|[
literal|4
index|]
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|single
index|[
literal|1
index|]
operator|==
literal|0.0f
operator|&&
name|single
index|[
literal|3
index|]
operator|==
literal|0.0f
operator|)
condition|)
block|{
name|yScale
operator|=
operator|(
name|float
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
name|Math
operator|.
name|pow
argument_list|(
name|single
index|[
literal|3
index|]
argument_list|,
literal|2
argument_list|)
operator|+
name|Math
operator|.
name|pow
argument_list|(
name|single
index|[
literal|4
index|]
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|yScale
return|;
block|}
comment|/**      * Get the x position in the matrix.      * @return The x-position.      */
specifier|public
name|float
name|getXPosition
parameter_list|()
block|{
return|return
name|single
index|[
literal|6
index|]
return|;
block|}
comment|/**      * Get the y position.      * @return The y position.      */
specifier|public
name|float
name|getYPosition
parameter_list|()
block|{
return|return
name|single
index|[
literal|7
index|]
return|;
block|}
comment|/**      * Returns a COS array which represnets this matrix.      */
specifier|public
name|COSArray
name|toCOSArray
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|7
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
block|}
end_class

end_unit

