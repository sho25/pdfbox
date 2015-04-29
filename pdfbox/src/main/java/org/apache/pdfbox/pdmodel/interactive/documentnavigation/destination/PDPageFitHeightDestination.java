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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
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

begin_comment
comment|/**  * This represents a destination to a page at a x location and the height is magnified  * to just fit on the screen.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDPageFitHeightDestination
extends|extends
name|PDPageDestination
block|{
comment|/**      * The type of this destination.      */
specifier|protected
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"FitV"
decl_stmt|;
comment|/**      * The type of this destination.      */
specifier|protected
specifier|static
specifier|final
name|String
name|TYPE_BOUNDED
init|=
literal|"FitBV"
decl_stmt|;
comment|/**      * Default constructor.      *      */
specifier|public
name|PDPageFitHeightDestination
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|array
operator|.
name|growToSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|array
operator|.
name|setName
argument_list|(
literal|1
argument_list|,
name|TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from an existing destination array.      *      * @param arr The destination array.      */
specifier|public
name|PDPageFitHeightDestination
parameter_list|(
name|COSArray
name|arr
parameter_list|)
block|{
name|super
argument_list|(
name|arr
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the left x coordinate.  A return value of -1 implies that the current x-coordinate      * will be used.      *      * @return The left x coordinate.      */
specifier|public
name|int
name|getLeft
parameter_list|()
block|{
return|return
name|array
operator|.
name|getInt
argument_list|(
literal|2
argument_list|)
return|;
block|}
comment|/**      * Set the left x-coordinate, a value of -1 implies that the current x-coordinate      * will be used.      * @param x The left x coordinate.      */
specifier|public
name|void
name|setLeft
parameter_list|(
name|int
name|x
parameter_list|)
block|{
name|array
operator|.
name|growToSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
if|if
condition|(
name|x
operator|==
operator|-
literal|1
condition|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|2
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
name|x
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A flag indicating if this page destination should just fit bounding box of the PDF.      *      * @return true If the destination should fit just the bounding box.      */
specifier|public
name|boolean
name|fitBoundingBox
parameter_list|()
block|{
return|return
name|TYPE_BOUNDED
operator|.
name|equals
argument_list|(
name|array
operator|.
name|getName
argument_list|(
literal|1
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set if this page destination should just fit the bounding box.  The default is false.      *      * @param fitBoundingBox A flag indicating if this should fit the bounding box.      */
specifier|public
name|void
name|setFitBoundingBox
parameter_list|(
name|boolean
name|fitBoundingBox
parameter_list|)
block|{
name|array
operator|.
name|growToSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|fitBoundingBox
condition|)
block|{
name|array
operator|.
name|setName
argument_list|(
literal|1
argument_list|,
name|TYPE_BOUNDED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|setName
argument_list|(
literal|1
argument_list|,
name|TYPE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

