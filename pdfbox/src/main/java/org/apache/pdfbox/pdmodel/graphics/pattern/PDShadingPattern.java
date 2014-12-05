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
name|state
operator|.
name|PDExternalGraphicsState
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
name|PDShading
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
comment|/**  * A shading pattern dictionary.  *  * @author Andreas Lehmkühler  */
end_comment

begin_class
specifier|public
class|class
name|PDShadingPattern
extends|extends
name|PDAbstractPattern
block|{
specifier|private
name|PDExternalGraphicsState
name|externalGraphicsState
decl_stmt|;
specifier|private
name|PDShading
name|shading
decl_stmt|;
comment|/**      * Creates a new shading pattern.      */
specifier|public
name|PDShadingPattern
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
name|PDAbstractPattern
operator|.
name|TYPE_SHADING_PATTERN
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new shading pattern from the given COS dictionary.      * @param resourceDictionary The COSDictionary for this pattern resource.      */
specifier|public
name|PDShadingPattern
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
annotation|@
name|Override
specifier|public
name|int
name|getPatternType
parameter_list|()
block|{
return|return
name|PDAbstractPattern
operator|.
name|TYPE_SHADING_PATTERN
return|;
block|}
comment|/**      * Returns the pattern matrix.      */
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
name|Matrix
name|matrix
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
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
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|matrix
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
name|matrix
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
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// default value is the identity matrix
name|matrix
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
block|}
return|return
name|matrix
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
name|COSArray
name|matrix
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
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
comment|/**      * This will get the external graphics state for this pattern.      * @return The extended graphics state for this pattern.      */
specifier|public
name|PDExternalGraphicsState
name|getExternalGraphicsState
parameter_list|()
block|{
if|if
condition|(
name|externalGraphicsState
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
name|externalGraphicsState
operator|=
operator|new
name|PDExternalGraphicsState
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|externalGraphicsState
return|;
block|}
comment|/**      * This will set the external graphics state for this pattern.      * @param externalGraphicsState The new extended graphics state for this pattern.      */
specifier|public
name|void
name|setExternalGraphicsState
parameter_list|(
name|PDExternalGraphicsState
name|externalGraphicsState
parameter_list|)
block|{
name|this
operator|.
name|externalGraphicsState
operator|=
name|externalGraphicsState
expr_stmt|;
if|if
condition|(
name|externalGraphicsState
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
name|externalGraphicsState
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
comment|/**      * This will get the shading resources for this pattern.      * @return The shading resources for this pattern.      * @throws IOException if something went wrong      */
specifier|public
name|PDShading
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
name|PDShading
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
comment|/**      * This will set the shading resources for this pattern.      * @param shadingResources The new shading resources for this pattern.      */
specifier|public
name|void
name|setShading
parameter_list|(
name|PDShading
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
block|}
end_class

end_unit

