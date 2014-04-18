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
comment|/**  * This class will be used to signify a range.  a(min)&lt;= a*&lt;= a(max)  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDRange
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSArray
name|rangeArray
decl_stmt|;
specifier|private
name|int
name|startingIndex
decl_stmt|;
comment|/**      * Constructor with an initial range of 0..1.      */
specifier|public
name|PDRange
parameter_list|()
block|{
name|rangeArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|rangeArray
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
name|rangeArray
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
name|startingIndex
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Constructor assumes a starting index of 0.      *      * @param range The array that describes the range.      */
specifier|public
name|PDRange
parameter_list|(
name|COSArray
name|range
parameter_list|)
block|{
name|rangeArray
operator|=
name|range
expr_stmt|;
block|}
comment|/**      * Constructor with an index into an array.  Because some arrays specify      * multiple ranges ie [ 0,1,  0,2,  2,3 ] It is convenient for this      * class to take an index into an array.  So if you want this range to      * represent 0,2 in the above example then you would say<code>new PDRange( array, 1 )</code>.      *      * @param range The array that describes the index      * @param index The range index into the array for the start of the range.      */
specifier|public
name|PDRange
parameter_list|(
name|COSArray
name|range
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|rangeArray
operator|=
name|range
expr_stmt|;
name|startingIndex
operator|=
name|index
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|rangeArray
return|;
block|}
comment|/**      * This will get the underlying array value.      *      * @return The cos object that this object wraps.      */
specifier|public
name|COSArray
name|getCOSArray
parameter_list|()
block|{
return|return
name|rangeArray
return|;
block|}
comment|/**      * This will get the minimum value of the range.      *      * @return The min value.      */
specifier|public
name|float
name|getMin
parameter_list|()
block|{
name|COSNumber
name|min
init|=
operator|(
name|COSNumber
operator|)
name|rangeArray
operator|.
name|getObject
argument_list|(
name|startingIndex
operator|*
literal|2
argument_list|)
decl_stmt|;
return|return
name|min
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the minimum value for the range.      *      * @param min The new minimum for the range.      */
specifier|public
name|void
name|setMin
parameter_list|(
name|float
name|min
parameter_list|)
block|{
name|rangeArray
operator|.
name|set
argument_list|(
name|startingIndex
operator|*
literal|2
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|min
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the maximum value of the range.      *      * @return The max value.      */
specifier|public
name|float
name|getMax
parameter_list|()
block|{
name|COSNumber
name|max
init|=
operator|(
name|COSNumber
operator|)
name|rangeArray
operator|.
name|getObject
argument_list|(
name|startingIndex
operator|*
literal|2
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
name|max
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * This will set the maximum value for the range.      *      * @param max The new maximum for the range.      */
specifier|public
name|void
name|setMax
parameter_list|(
name|float
name|max
parameter_list|)
block|{
name|rangeArray
operator|.
name|set
argument_list|(
name|startingIndex
operator|*
literal|2
operator|+
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|max
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

