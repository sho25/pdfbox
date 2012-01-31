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
name|pattern
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|PDExtendedGraphicsState
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
name|graphics
operator|.
name|pattern
operator|.
name|PDPatternResources
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
name|graphics
operator|.
name|shading
operator|.
name|AxialShadingPaint
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
name|graphics
operator|.
name|shading
operator|.
name|PDShadingResources
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
name|graphics
operator|.
name|shading
operator|.
name|PDShadingType2
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
comment|/**  * This represents the resources for a shading pattern.  *  * @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|PDShadingPatternResources
extends|extends
name|PDPatternResources
block|{
specifier|private
name|PDExtendedGraphicsState
name|extendedGraphicsState
decl_stmt|;
specifier|private
name|PDShadingResources
name|shading
decl_stmt|;
specifier|private
name|COSArray
name|matrix
init|=
literal|null
decl_stmt|;
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDShadingPatternResources
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDShadingPatternResources
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|getCOSDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PATTERN_TYPE
argument_list|,
name|PDPatternResources
operator|.
name|SHADING_PATTERN
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prepopulated pattern resources.      *      * @param resourceDictionary The COSDictionary for this pattern resource.      */
specifier|public
name|PDShadingPatternResources
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getPatternType
parameter_list|()
block|{
return|return
name|PDPatternResources
operator|.
name|SHADING_PATTERN
return|;
block|}
comment|/**      * This will get the optional Matrix of a Pattern.      * It maps the form space into the user space      * @return the form matrix      */
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
name|Matrix
name|returnMatrix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|matrix
operator|==
literal|null
condition|)
block|{
name|matrix
operator|=
operator|(
name|COSArray
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matrix
operator|!=
literal|null
condition|)
block|{
name|returnMatrix
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|returnMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|matrix
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|returnMatrix
return|;
block|}
comment|/**      * Sets the optional Matrix entry for the Pattern.      * @param transform the transformation matrix      */
specifier|public
name|void
name|setMatrix
parameter_list|(
name|AffineTransform
name|transform
parameter_list|)
block|{
name|matrix
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|double
index|[]
name|values
init|=
operator|new
name|double
index|[
literal|6
index|]
decl_stmt|;
name|transform
operator|.
name|getMatrix
argument_list|(
name|values
argument_list|)
expr_stmt|;
for|for
control|(
name|double
name|v
range|:
name|values
control|)
block|{
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|(
name|float
operator|)
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the extended graphics state for this pattern.      *      * @return The extended graphics state for this pattern.      */
specifier|public
name|PDExtendedGraphicsState
name|getExtendedGraphicsState
parameter_list|()
block|{
if|if
condition|(
name|extendedGraphicsState
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|dictionary
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|EXT_G_STATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionary
operator|!=
literal|null
condition|)
block|{
name|extendedGraphicsState
operator|=
operator|new
name|PDExtendedGraphicsState
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|extendedGraphicsState
return|;
block|}
comment|/**      * This will set the extended graphics state for this pattern.      *      * @param extendedGraphicsState The new extended graphics state for this pattern.      */
specifier|public
name|void
name|setExtendedGraphicsState
parameter_list|(
name|PDExtendedGraphicsState
name|extendedGraphicsState
parameter_list|)
block|{
name|this
operator|.
name|extendedGraphicsState
operator|=
name|extendedGraphicsState
expr_stmt|;
if|if
condition|(
name|extendedGraphicsState
operator|!=
literal|null
condition|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EXT_G_STATE
argument_list|,
name|extendedGraphicsState
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|EXT_G_STATE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the shading resources for this pattern.      *      * @return The shading resourcesfor this pattern.      *       * @throws IOException if something went wrong      */
specifier|public
name|PDShadingResources
name|getShading
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|shading
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|dictionary
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SHADING
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionary
operator|!=
literal|null
condition|)
block|{
name|shading
operator|=
name|PDShadingResources
operator|.
name|create
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|shading
return|;
block|}
comment|/**      * This will set the shading resources for this pattern.      *      * @param shadingResources The new shading resources for this pattern.      */
specifier|public
name|void
name|setShading
parameter_list|(
name|PDShadingResources
name|shadingResources
parameter_list|)
block|{
name|shading
operator|=
name|shadingResources
expr_stmt|;
if|if
condition|(
name|shadingResources
operator|!=
literal|null
condition|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SHADING
argument_list|,
name|shadingResources
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|SHADING
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Paint
name|getPaint
parameter_list|(
name|int
name|pageHeight
parameter_list|)
throws|throws
name|IOException
block|{
name|Paint
name|paint
init|=
literal|null
decl_stmt|;
name|PDShadingResources
name|shadingResources
init|=
name|getShading
argument_list|()
decl_stmt|;
name|int
name|shadingType
init|=
name|shadingResources
operator|!=
literal|null
condition|?
name|shadingResources
operator|.
name|getShadingType
argument_list|()
else|:
literal|0
decl_stmt|;
switch|switch
condition|(
name|shadingType
condition|)
block|{
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE2
case|:
name|paint
operator|=
operator|new
name|AxialShadingPaint
argument_list|(
operator|(
name|PDShadingType2
operator|)
name|getShading
argument_list|()
argument_list|,
literal|null
argument_list|,
name|pageHeight
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE1
case|:
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE3
case|:
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE4
case|:
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE5
case|:
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE6
case|:
case|case
name|PDShadingResources
operator|.
name|SHADING_TYPE7
case|:
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error: Unsupported shading type "
operator|+
name|shadingType
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown shading type "
operator|+
name|shadingType
argument_list|)
throw|;
block|}
return|return
name|paint
return|;
block|}
block|}
end_class

end_unit

