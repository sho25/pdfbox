begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|fdf
package|;
end_package

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
import|;
end_import

begin_comment
comment|/**  * This represents an Icon fit dictionary for an FDF field.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFIconFit
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|fit
decl_stmt|;
comment|/**      * A scale option.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_OPTION_ALWAYS
init|=
literal|"A"
decl_stmt|;
comment|/**      * A scale option.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_OPTION_ONLY_WHEN_ICON_IS_BIGGER
init|=
literal|"B"
decl_stmt|;
comment|/**      * A scale option.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_OPTION_ONLY_WHEN_ICON_IS_SMALLER
init|=
literal|"S"
decl_stmt|;
comment|/**      * A scale option.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_OPTION_NEVER
init|=
literal|"N"
decl_stmt|;
comment|/**      * Scale to fill with of annotation, disregarding aspect ratio.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_TYPE_ANAMORPHIC
init|=
literal|"A"
decl_stmt|;
comment|/**      * Scale to fit width or height, smaller of two, while retaining aspect ration.      */
specifier|public
specifier|static
specifier|final
name|String
name|SCALE_TYPE_PROPORTIONAL
init|=
literal|"P"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFIconFit
parameter_list|()
block|{
name|fit
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param f The icon fit dictionary.      */
specifier|public
name|FDFIconFit
parameter_list|(
name|COSDictionary
name|f
parameter_list|)
block|{
name|fit
operator|=
name|f
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|fit
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|fit
return|;
block|}
comment|/**      * This will get the scale option.  See the SCALE_OPTION_XXX constants.  This      * is guaranteed to never return null.  Default: Always      *      * @return The scale option.      */
specifier|public
name|String
name|getScaleOption
parameter_list|()
block|{
name|String
name|retval
init|=
name|fit
operator|.
name|getNameAsString
argument_list|(
literal|"SW"
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|SCALE_OPTION_ALWAYS
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the scale option for the icon.  Set the SCALE_OPTION_XXX constants.      *      * @param option The scale option.      */
specifier|public
name|void
name|setScaleOption
parameter_list|(
name|String
name|option
parameter_list|)
block|{
name|fit
operator|.
name|setName
argument_list|(
literal|"SW"
argument_list|,
name|option
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the scale type.  See the SCALE_TYPE_XXX constants.  This is      * guaranteed to never return null.  Default: Proportional      *      * @return The scale type.      */
specifier|public
name|String
name|getScaleType
parameter_list|()
block|{
name|String
name|retval
init|=
name|fit
operator|.
name|getNameAsString
argument_list|(
literal|"S"
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|SCALE_TYPE_PROPORTIONAL
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the scale type.  See the SCALE_TYPE_XXX constants.      *      * @param scale The scale type.      */
specifier|public
name|void
name|setScaleType
parameter_list|(
name|String
name|scale
parameter_list|)
block|{
name|fit
operator|.
name|setName
argument_list|(
literal|"S"
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is guaranteed to never return null.<br />      *      * To quote the PDF Spec      * "An array of two numbers between 0.0 and 1.0 indicating the fraction of leftover      * space to allocate at the left and bottom of the icon. A value of [0.0 0.0] positions the      * icon at the bottom-left corner of the annotation rectangle; a value of [0.5 0.5] centers it      * within the rectangle. This entry is used only if the icon is scaled proportionally. Default      * value: [0.5 0.5]."      *      * @return The fractional space to allocate.      */
specifier|public
name|PDRange
name|getFractionalSpaceToAllocate
parameter_list|()
block|{
name|PDRange
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|fit
operator|.
name|getDictionaryObject
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRange
argument_list|()
expr_stmt|;
name|retval
operator|.
name|setMin
argument_list|(
literal|.5f
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setMax
argument_list|(
literal|.5f
argument_list|)
expr_stmt|;
name|setFractionalSpaceToAllocate
argument_list|(
name|retval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|PDRange
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set frational space to allocate.      *      * @param space The space to allocate.      */
specifier|public
name|void
name|setFractionalSpaceToAllocate
parameter_list|(
name|PDRange
name|space
parameter_list|)
block|{
name|fit
operator|.
name|setItem
argument_list|(
literal|"A"
argument_list|,
name|space
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will tell if the icon should scale to fit the annotation bounds.  Default: false      *      * @return A flag telling if the icon should scale.      */
specifier|public
name|boolean
name|shouldScaleToFitAnnotation
parameter_list|()
block|{
return|return
name|fit
operator|.
name|getBoolean
argument_list|(
literal|"FB"
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will tell the icon to scale.      *      * @param value The flag value.      */
specifier|public
name|void
name|setScaleToFitAnnotation
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|fit
operator|.
name|setBoolean
argument_list|(
literal|"FB"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

