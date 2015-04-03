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
comment|/**  * This represents a destination to a page at an x,y coordinate with a zoom setting.  * The default x,y,z will be whatever is the current value in the viewer application and  * are not required.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDPageXYZDestination
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
literal|"XYZ"
decl_stmt|;
comment|/**      * Default constructor.      *      */
specifier|public
name|PDPageXYZDestination
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|array
operator|.
name|growToSize
argument_list|(
literal|5
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
name|PDPageXYZDestination
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
comment|/**      * Get the left x coordinate.  Return values of 0 or -1 imply that the current x-coordinate      * will be used.      *      * @return The left x coordinate.      */
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
comment|/**      * Set the left x-coordinate, values 0 or -1 imply that the current x-coordinate      * will be used.      * @param x The left x coordinate.      */
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
operator|(
name|COSBase
operator|)
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
comment|/**      * Get the top y coordinate.  Return values of 0 or -1 imply that the current y-coordinate      * will be used.      *      * @return The top y coordinate.      */
specifier|public
name|int
name|getTop
parameter_list|()
block|{
return|return
name|array
operator|.
name|getInt
argument_list|(
literal|3
argument_list|)
return|;
block|}
comment|/**      * Set the top y-coordinate, values 0 or -1 imply that the current y-coordinate      * will be used.      * @param y The top ycoordinate.      */
specifier|public
name|void
name|setTop
parameter_list|(
name|int
name|y
parameter_list|)
block|{
name|array
operator|.
name|growToSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
if|if
condition|(
name|y
operator|==
operator|-
literal|1
condition|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|3
argument_list|,
operator|(
name|COSBase
operator|)
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
literal|3
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the zoom value.  Return values of 0 or -1 imply that the current zoom      * will be used.      *      * @return The zoom value for the page.      */
specifier|public
name|float
name|getZoom
parameter_list|()
block|{
name|COSBase
name|obj
init|=
name|array
operator|.
name|getObject
argument_list|(
literal|4
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**      * Set the zoom value for the page, values 0 or -1 imply that the current zoom      * will be used.      * @param zoom The zoom value.      */
specifier|public
name|void
name|setZoom
parameter_list|(
name|float
name|zoom
parameter_list|)
block|{
name|array
operator|.
name|growToSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
if|if
condition|(
name|zoom
operator|==
operator|-
literal|1
condition|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|4
argument_list|,
operator|(
name|COSBase
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|array
operator|.
name|set
argument_list|(
literal|4
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|zoom
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

