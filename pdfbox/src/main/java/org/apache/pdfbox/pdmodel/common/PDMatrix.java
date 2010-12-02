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

begin_comment
comment|/**  * This class will be used for matrix manipulation.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PDMatrix
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSArray
name|matrix
decl_stmt|;
comment|// the number of row elements depends on the number of elements
comment|// within the given matrix
comment|// 3x3 e.g. Matrix of a CalRGB colorspace dictionary
comment|// 3x2 e.g. FontMatrix of a type 3 font
specifier|private
name|int
name|numberOfRowElements
init|=
literal|3
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDMatrix
parameter_list|()
block|{
name|matrix
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param array The array that describes the matrix.      */
specifier|public
name|PDMatrix
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|6
condition|)
block|{
name|numberOfRowElements
operator|=
literal|2
expr_stmt|;
block|}
name|matrix
operator|=
name|array
expr_stmt|;
block|}
comment|/**      * This will get the underlying array value.      *      * @return The cos object that this object wraps.      */
specifier|public
name|COSArray
name|getCOSArray
parameter_list|()
block|{
return|return
name|matrix
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|matrix
return|;
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
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
name|row
operator|*
name|numberOfRowElements
operator|+
name|column
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
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
name|matrix
operator|.
name|set
argument_list|(
name|row
operator|*
name|numberOfRowElements
operator|+
name|column
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

