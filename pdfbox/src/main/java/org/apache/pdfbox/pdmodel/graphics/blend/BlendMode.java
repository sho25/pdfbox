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
name|blend
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
name|COSName
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Blend mode.  *  * @author Kühn&amp; Weyh Software GmbH  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BlendMode
block|{
comment|/**      * Determines the blend mode from the BM entry in the COS ExtGState.      *      * @param cosBlendMode name or array      * @return blending mode      */
specifier|public
specifier|static
name|BlendMode
name|getInstance
parameter_list|(
name|COSBase
name|cosBlendMode
parameter_list|)
block|{
name|BlendMode
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cosBlendMode
operator|instanceof
name|COSName
condition|)
block|{
name|result
operator|=
name|BLEND_MODES
operator|.
name|get
argument_list|(
operator|(
name|COSName
operator|)
name|cosBlendMode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cosBlendMode
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|cosBlendModeArray
init|=
operator|(
name|COSArray
operator|)
name|cosBlendMode
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cosBlendModeArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|=
name|BLEND_MODES
operator|.
name|get
argument_list|(
name|cosBlendModeArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
return|return
name|BlendMode
operator|.
name|COMPATIBLE
return|;
block|}
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|NORMAL
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|srcValue
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|COMPATIBLE
init|=
name|NORMAL
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|MULTIPLY
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|srcValue
operator|*
name|dstValue
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|SCREEN
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|srcValue
operator|+
name|dstValue
operator|-
name|srcValue
operator|*
name|dstValue
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|OVERLAY
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
operator|(
name|dstValue
operator|<=
literal|0.5
operator|)
condition|?
literal|2
operator|*
name|dstValue
operator|*
name|srcValue
else|:
literal|2
operator|*
operator|(
name|srcValue
operator|+
name|dstValue
operator|-
name|srcValue
operator|*
name|dstValue
operator|)
operator|-
literal|1
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|DARKEN
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|Math
operator|.
name|min
argument_list|(
name|srcValue
argument_list|,
name|dstValue
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|LIGHTEN
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
name|srcValue
argument_list|,
name|dstValue
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|COLOR_DODGE
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
operator|(
name|srcValue
operator|<
literal|1
operator|)
condition|?
name|Math
operator|.
name|min
argument_list|(
literal|1
argument_list|,
name|dstValue
operator|/
operator|(
literal|1
operator|-
name|srcValue
operator|)
argument_list|)
else|:
literal|1
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|COLOR_BURN
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
operator|(
name|srcValue
operator|>
literal|0
operator|)
condition|?
literal|1
operator|-
name|Math
operator|.
name|min
argument_list|(
literal|1
argument_list|,
operator|(
literal|1
operator|-
name|dstValue
operator|)
operator|/
name|srcValue
argument_list|)
else|:
literal|0
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|HARD_LIGHT
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
operator|(
name|srcValue
operator|<=
literal|0.5
operator|)
condition|?
literal|2
operator|*
name|dstValue
operator|*
name|srcValue
else|:
literal|2
operator|*
operator|(
name|srcValue
operator|+
name|dstValue
operator|-
name|srcValue
operator|*
name|dstValue
operator|)
operator|-
literal|1
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|SOFT_LIGHT
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
if|if
condition|(
name|srcValue
operator|<=
literal|0.5
condition|)
block|{
return|return
name|dstValue
operator|-
operator|(
literal|1
operator|-
literal|2
operator|*
name|srcValue
operator|)
operator|*
name|dstValue
operator|*
operator|(
literal|1
operator|-
name|dstValue
operator|)
return|;
block|}
else|else
block|{
name|float
name|d
init|=
operator|(
name|dstValue
operator|<=
literal|0.25
operator|)
condition|?
operator|(
operator|(
literal|16
operator|*
name|dstValue
operator|-
literal|12
operator|)
operator|*
name|dstValue
operator|+
literal|4
operator|)
operator|*
name|dstValue
else|:
operator|(
name|float
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
name|dstValue
argument_list|)
decl_stmt|;
return|return
name|dstValue
operator|+
operator|(
literal|2
operator|*
name|srcValue
operator|-
literal|1
operator|)
operator|*
operator|(
name|d
operator|-
name|dstValue
operator|)
return|;
block|}
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|DIFFERENCE
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|Math
operator|.
name|abs
argument_list|(
name|dstValue
operator|-
name|srcValue
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|SeparableBlendMode
name|EXCLUSION
init|=
operator|new
name|SeparableBlendMode
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
block|{
return|return
name|dstValue
operator|+
name|srcValue
operator|-
literal|2
operator|*
name|dstValue
operator|*
name|srcValue
return|;
block|}
block|}
decl_stmt|;
comment|// this map *must* come after the declarations above, otherwise its values will be null
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|COSName
argument_list|,
name|BlendMode
argument_list|>
name|BLEND_MODES
init|=
name|createBlendModeMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|COSName
argument_list|,
name|BlendMode
argument_list|>
name|createBlendModeMap
parameter_list|()
block|{
name|Map
argument_list|<
name|COSName
argument_list|,
name|BlendMode
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|COSName
argument_list|,
name|BlendMode
argument_list|>
argument_list|(
literal|13
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|NORMAL
argument_list|,
name|BlendMode
operator|.
name|NORMAL
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|COMPATIBLE
argument_list|,
name|BlendMode
operator|.
name|COMPATIBLE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|MULTIPLY
argument_list|,
name|BlendMode
operator|.
name|MULTIPLY
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|SCREEN
argument_list|,
name|BlendMode
operator|.
name|SCREEN
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|OVERLAY
argument_list|,
name|BlendMode
operator|.
name|OVERLAY
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|DARKEN
argument_list|,
name|BlendMode
operator|.
name|DARKEN
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|LIGHTEN
argument_list|,
name|BlendMode
operator|.
name|LIGHTEN
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|COLOR_DODGE
argument_list|,
name|BlendMode
operator|.
name|COLOR_DODGE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|COLOR_BURN
argument_list|,
name|BlendMode
operator|.
name|COLOR_BURN
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|HARD_LIGHT
argument_list|,
name|BlendMode
operator|.
name|HARD_LIGHT
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|SOFT_LIGHT
argument_list|,
name|BlendMode
operator|.
name|SOFT_LIGHT
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|DIFFERENCE
argument_list|,
name|BlendMode
operator|.
name|DIFFERENCE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|EXCLUSION
argument_list|,
name|BlendMode
operator|.
name|EXCLUSION
argument_list|)
expr_stmt|;
comment|// TODO - non-separable blending modes
return|return
name|map
return|;
block|}
name|BlendMode
parameter_list|()
block|{     }
block|}
end_class

end_unit

