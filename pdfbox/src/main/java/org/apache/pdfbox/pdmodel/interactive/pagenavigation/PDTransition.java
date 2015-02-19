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
name|pagenavigation
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
name|COSBoolean
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
name|COSDictionary
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
name|COSInteger
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
name|COSName
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
name|pdmodel
operator|.
name|common
operator|.
name|PDDictionaryWrapper
import|;
end_import

begin_comment
comment|/**  * Represents a page transition as defined in paragraph 12.4.4.1 of PDF 32000-1:2008  *   * @author Andrea Vacondio  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDTransition
extends|extends
name|PDDictionaryWrapper
block|{
comment|/**      * creates a new transition with default "replace" style {@link PDTransitionStyle#R}      */
specifier|public
name|PDTransition
parameter_list|()
block|{
name|this
argument_list|(
name|PDTransitionStyle
operator|.
name|R
argument_list|)
expr_stmt|;
block|}
comment|/**      * creates a new transition with the given style.      *       * @param style      */
specifier|public
name|PDTransition
parameter_list|(
name|PDTransitionStyle
name|style
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|TRANS
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|style
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * creates a new transition for an existing dictionary      *       * @param dictionary      */
specifier|public
name|PDTransition
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the style for this transition      * @see PDTransitionStyle#valueOf(String)      */
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|PDTransitionStyle
operator|.
name|R
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return The dimension in which the specified transition effect shall occur or the default      * {@link PDTransitionDimension#H} if no dimension is found.      * @see PDTransitionDimension      */
specifier|public
name|String
name|getDimension
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|DM
argument_list|,
name|PDTransitionDimension
operator|.
name|H
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets the dimension in which the specified transition effect shall occur. Only for {@link PDTransitionStyle#Split}      * and {@link PDTransitionStyle#Blinds}.      */
specifier|public
name|void
name|setDimension
parameter_list|(
name|PDTransitionDimension
name|dimension
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|DM
argument_list|,
name|dimension
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The direction of motion for the specified transition effect or the default {@link PDTransitionMotion#I}      * if no motion is found.      * @see PDTransitionMotion      */
specifier|public
name|String
name|getMotion
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|M
argument_list|,
name|PDTransitionMotion
operator|.
name|I
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets the direction of motion for the specified transition effect. Only for {@link PDTransitionStyle#Split},      * {@link PDTransitionStyle#Blinds} and {@link PDTransitionStyle#Fly}.      */
specifier|public
name|void
name|setMotion
parameter_list|(
name|PDTransitionMotion
name|motion
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|M
argument_list|,
name|motion
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the direction in which the specified transition effect shall moves. It can be either a {@link COSInteger}      * or {@link COSName#NONE}. Default to {@link COSInteger#ZERO}      * @see PDTransitionDirection      */
specifier|public
name|COSBase
name|getDirection
parameter_list|()
block|{
name|COSBase
name|item
init|=
name|getCOSDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DI
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|==
literal|null
condition|)
block|{
return|return
name|COSInteger
operator|.
name|ZERO
return|;
block|}
return|return
name|item
return|;
block|}
comment|/**      * Sets the direction in which the specified transition effect shall moves. Only for {@link PDTransitionStyle#Wipe},      * {@link PDTransitionStyle#Glitter}, {@link PDTransitionStyle#Fly}, {@link PDTransitionStyle#Cover},      * {@link PDTransitionStyle#Uncover} and {@link PDTransitionStyle#Push}.      */
specifier|public
name|void
name|setDirection
parameter_list|(
name|PDTransitionDirection
name|direction
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DI
argument_list|,
name|direction
operator|.
name|getCOSBase
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The duration in seconds of the transition effect or the default 1 if no duration is found.      */
specifier|public
name|float
name|getDuration
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * @param duration The duration of the transition effect, in seconds.      */
specifier|public
name|void
name|setDuration
parameter_list|(
name|float
name|duration
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|duration
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return The starting or ending scale at which the changes shall be drawn or the default 1 if no scale is found.      * Only for {@link PDTransitionStyle#Fly}.      */
specifier|public
name|float
name|getFlyScale
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|SS
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * @param scale The starting or ending scale at which the changes shall be drawn. Only for      * {@link PDTransitionStyle#Fly}.      */
specifier|public
name|void
name|setFlyScale
parameter_list|(
name|float
name|scale
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SS
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|scale
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the area that shall be flown in is rectangular and opaque. Default is false. Only for      * {@link PDTransitionStyle#Fly}.      */
specifier|public
name|boolean
name|isFlyAreaOpaque
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|B
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * @param opaque If true, the area that shall be flown in is rectangular and opaque. Only for      * {@link PDTransitionStyle#Fly}.      */
specifier|public
name|void
name|setFlyAreaOpaque
parameter_list|(
name|boolean
name|opaque
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|B
argument_list|,
name|COSBoolean
operator|.
name|getBoolean
argument_list|(
name|opaque
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

