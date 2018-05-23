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
name|java
operator|.
name|awt
operator|.
name|AlphaComposite
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Composite
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|CompositeContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|Raster
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|WritableRaster
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

begin_comment
comment|/**  * AWT composite for blend modes.  *   * @author Kühn&amp; Weyh Software GmbH  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|BlendComposite
implements|implements
name|Composite
block|{
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
name|BlendComposite
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Creates a blend composite      *      * @param blendMode Desired blend mode      * @param constantAlpha Constant alpha, must be in the inclusive range      * [0.0...1.0] or it will be clipped.      * @return a blend composite.      */
specifier|public
specifier|static
name|Composite
name|getInstance
parameter_list|(
name|BlendMode
name|blendMode
parameter_list|,
name|float
name|constantAlpha
parameter_list|)
block|{
if|if
condition|(
name|constantAlpha
operator|<
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"using 0 instead of incorrect Alpha "
operator|+
name|constantAlpha
argument_list|)
expr_stmt|;
name|constantAlpha
operator|=
literal|0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|constantAlpha
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"using 1 instead of incorrect Alpha "
operator|+
name|constantAlpha
argument_list|)
expr_stmt|;
name|constantAlpha
operator|=
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|blendMode
operator|==
name|BlendMode
operator|.
name|NORMAL
condition|)
block|{
return|return
name|AlphaComposite
operator|.
name|getInstance
argument_list|(
name|AlphaComposite
operator|.
name|SRC_OVER
argument_list|,
name|constantAlpha
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|BlendComposite
argument_list|(
name|blendMode
argument_list|,
name|constantAlpha
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|final
name|BlendMode
name|blendMode
decl_stmt|;
specifier|private
specifier|final
name|float
name|constantAlpha
decl_stmt|;
specifier|private
name|BlendComposite
parameter_list|(
name|BlendMode
name|blendMode
parameter_list|,
name|float
name|constantAlpha
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|blendMode
operator|=
name|blendMode
expr_stmt|;
name|this
operator|.
name|constantAlpha
operator|=
name|constantAlpha
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|CompositeContext
name|createContext
parameter_list|(
name|ColorModel
name|srcColorModel
parameter_list|,
name|ColorModel
name|dstColorModel
parameter_list|,
name|RenderingHints
name|hints
parameter_list|)
block|{
return|return
operator|new
name|BlendCompositeContext
argument_list|(
name|srcColorModel
argument_list|,
name|dstColorModel
argument_list|,
name|hints
argument_list|)
return|;
block|}
class|class
name|BlendCompositeContext
implements|implements
name|CompositeContext
block|{
specifier|private
specifier|final
name|ColorModel
name|srcColorModel
decl_stmt|;
specifier|private
specifier|final
name|ColorModel
name|dstColorModel
decl_stmt|;
specifier|private
specifier|final
name|RenderingHints
name|hints
decl_stmt|;
name|BlendCompositeContext
parameter_list|(
name|ColorModel
name|srcColorModel
parameter_list|,
name|ColorModel
name|dstColorModel
parameter_list|,
name|RenderingHints
name|hints
parameter_list|)
block|{
name|this
operator|.
name|srcColorModel
operator|=
name|srcColorModel
expr_stmt|;
name|this
operator|.
name|dstColorModel
operator|=
name|dstColorModel
expr_stmt|;
name|this
operator|.
name|hints
operator|=
name|hints
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
comment|// nothing needed
block|}
annotation|@
name|Override
specifier|public
name|void
name|compose
parameter_list|(
name|Raster
name|src
parameter_list|,
name|Raster
name|dstIn
parameter_list|,
name|WritableRaster
name|dstOut
parameter_list|)
block|{
name|int
name|x0
init|=
name|src
operator|.
name|getMinX
argument_list|()
decl_stmt|;
name|int
name|y0
init|=
name|src
operator|.
name|getMinY
argument_list|()
decl_stmt|;
name|int
name|width
init|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|src
operator|.
name|getWidth
argument_list|()
argument_list|,
name|dstIn
operator|.
name|getWidth
argument_list|()
argument_list|)
argument_list|,
name|dstOut
operator|.
name|getWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|height
init|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|src
operator|.
name|getHeight
argument_list|()
argument_list|,
name|dstIn
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|,
name|dstOut
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|x1
init|=
name|x0
operator|+
name|width
decl_stmt|;
name|int
name|y1
init|=
name|y0
operator|+
name|height
decl_stmt|;
name|int
name|dstInXShift
init|=
name|dstIn
operator|.
name|getMinX
argument_list|()
operator|-
name|x0
decl_stmt|;
name|int
name|dstInYShift
init|=
name|dstIn
operator|.
name|getMinY
argument_list|()
operator|-
name|y0
decl_stmt|;
name|int
name|dstOutXShift
init|=
name|dstOut
operator|.
name|getMinX
argument_list|()
operator|-
name|x0
decl_stmt|;
name|int
name|dstOutYShift
init|=
name|dstOut
operator|.
name|getMinY
argument_list|()
operator|-
name|y0
decl_stmt|;
name|ColorSpace
name|srcColorSpace
init|=
name|srcColorModel
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|int
name|numSrcColorComponents
init|=
name|srcColorModel
operator|.
name|getNumColorComponents
argument_list|()
decl_stmt|;
name|int
name|numSrcComponents
init|=
name|src
operator|.
name|getNumBands
argument_list|()
decl_stmt|;
name|boolean
name|srcHasAlpha
init|=
operator|(
name|numSrcComponents
operator|>
name|numSrcColorComponents
operator|)
decl_stmt|;
name|ColorSpace
name|dstColorSpace
init|=
name|dstColorModel
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|int
name|numDstColorComponents
init|=
name|dstColorModel
operator|.
name|getNumColorComponents
argument_list|()
decl_stmt|;
name|int
name|numDstComponents
init|=
name|dstIn
operator|.
name|getNumBands
argument_list|()
decl_stmt|;
name|boolean
name|dstHasAlpha
init|=
operator|(
name|numDstComponents
operator|>
name|numDstColorComponents
operator|)
decl_stmt|;
name|int
name|srcColorSpaceType
init|=
name|srcColorSpace
operator|.
name|getType
argument_list|()
decl_stmt|;
name|int
name|dstColorSpaceType
init|=
name|dstColorSpace
operator|.
name|getType
argument_list|()
decl_stmt|;
name|boolean
name|subtractive
init|=
operator|(
name|dstColorSpaceType
operator|!=
name|ColorSpace
operator|.
name|TYPE_RGB
operator|)
operator|&&
operator|(
name|dstColorSpaceType
operator|!=
name|ColorSpace
operator|.
name|TYPE_GRAY
operator|)
decl_stmt|;
name|boolean
name|blendModeIsSeparable
init|=
name|blendMode
operator|instanceof
name|SeparableBlendMode
decl_stmt|;
name|SeparableBlendMode
name|separableBlendMode
init|=
name|blendModeIsSeparable
condition|?
operator|(
name|SeparableBlendMode
operator|)
name|blendMode
else|:
literal|null
decl_stmt|;
name|NonSeparableBlendMode
name|nonSeparableBlendMode
init|=
operator|!
name|blendModeIsSeparable
condition|?
operator|(
name|NonSeparableBlendMode
operator|)
name|blendMode
else|:
literal|null
decl_stmt|;
name|boolean
name|needsColorConversion
init|=
operator|!
name|srcColorSpace
operator|.
name|equals
argument_list|(
name|dstColorSpace
argument_list|)
decl_stmt|;
name|Object
name|srcPixel
init|=
literal|null
decl_stmt|;
name|Object
name|dstPixel
init|=
literal|null
decl_stmt|;
name|float
index|[]
name|srcComponents
init|=
operator|new
name|float
index|[
name|numSrcComponents
index|]
decl_stmt|;
comment|// PDFBOX-3501 let getNormalizedComponents allocate to avoid
comment|// ArrayIndexOutOfBoundsException for bitonal target
name|float
index|[]
name|dstComponents
init|=
literal|null
decl_stmt|;
name|float
index|[]
name|srcColor
init|=
operator|new
name|float
index|[
name|numSrcColorComponents
index|]
decl_stmt|;
name|float
index|[]
name|srcConverted
decl_stmt|;
name|float
index|[]
name|dstConverted
decl_stmt|;
name|float
index|[]
name|rgbResult
init|=
name|blendModeIsSeparable
condition|?
literal|null
else|:
operator|new
name|float
index|[
name|dstHasAlpha
condition|?
literal|4
else|:
literal|3
index|]
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
name|y0
init|;
name|y
operator|<
name|y1
condition|;
name|y
operator|++
control|)
block|{
for|for
control|(
name|int
name|x
init|=
name|x0
init|;
name|x
operator|<
name|x1
condition|;
name|x
operator|++
control|)
block|{
name|srcPixel
operator|=
name|src
operator|.
name|getDataElements
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|srcPixel
argument_list|)
expr_stmt|;
name|dstPixel
operator|=
name|dstIn
operator|.
name|getDataElements
argument_list|(
name|dstInXShift
operator|+
name|x
argument_list|,
name|dstInYShift
operator|+
name|y
argument_list|,
name|dstPixel
argument_list|)
expr_stmt|;
name|srcComponents
operator|=
name|srcColorModel
operator|.
name|getNormalizedComponents
argument_list|(
name|srcPixel
argument_list|,
name|srcComponents
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|dstComponents
operator|=
name|dstColorModel
operator|.
name|getNormalizedComponents
argument_list|(
name|dstPixel
argument_list|,
name|dstComponents
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|float
name|srcAlpha
init|=
name|srcHasAlpha
condition|?
name|srcComponents
index|[
name|numSrcColorComponents
index|]
else|:
literal|1.0f
decl_stmt|;
name|float
name|dstAlpha
init|=
name|dstHasAlpha
condition|?
name|dstComponents
index|[
name|numDstColorComponents
index|]
else|:
literal|1.0f
decl_stmt|;
name|srcAlpha
operator|=
name|srcAlpha
operator|*
name|constantAlpha
expr_stmt|;
name|float
name|resultAlpha
init|=
name|dstAlpha
operator|+
name|srcAlpha
operator|-
name|srcAlpha
operator|*
name|dstAlpha
decl_stmt|;
name|float
name|srcAlphaRatio
init|=
operator|(
name|resultAlpha
operator|>
literal|0
operator|)
condition|?
name|srcAlpha
operator|/
name|resultAlpha
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|separableBlendMode
operator|!=
literal|null
condition|)
block|{
comment|// convert color
name|System
operator|.
name|arraycopy
argument_list|(
name|srcComponents
argument_list|,
literal|0
argument_list|,
name|srcColor
argument_list|,
literal|0
argument_list|,
name|numSrcColorComponents
argument_list|)
expr_stmt|;
if|if
condition|(
name|needsColorConversion
condition|)
block|{
comment|// TODO - very very slow - Hash results???
name|float
index|[]
name|cieXYZ
init|=
name|srcColorSpace
operator|.
name|toCIEXYZ
argument_list|(
name|srcColor
argument_list|)
decl_stmt|;
name|srcConverted
operator|=
name|dstColorSpace
operator|.
name|fromCIEXYZ
argument_list|(
name|cieXYZ
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|srcConverted
operator|=
name|srcColor
expr_stmt|;
block|}
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|numDstColorComponents
condition|;
name|k
operator|++
control|)
block|{
name|float
name|srcValue
init|=
name|srcConverted
index|[
name|k
index|]
decl_stmt|;
name|float
name|dstValue
init|=
name|dstComponents
index|[
name|k
index|]
decl_stmt|;
if|if
condition|(
name|subtractive
condition|)
block|{
name|srcValue
operator|=
literal|1
operator|-
name|srcValue
expr_stmt|;
name|dstValue
operator|=
literal|1
operator|-
name|dstValue
expr_stmt|;
block|}
name|float
name|value
init|=
name|separableBlendMode
operator|.
name|blendChannel
argument_list|(
name|srcValue
argument_list|,
name|dstValue
argument_list|)
decl_stmt|;
name|value
operator|=
name|srcValue
operator|+
name|dstAlpha
operator|*
operator|(
name|value
operator|-
name|srcValue
operator|)
expr_stmt|;
name|value
operator|=
name|dstValue
operator|+
name|srcAlphaRatio
operator|*
operator|(
name|value
operator|-
name|dstValue
operator|)
expr_stmt|;
if|if
condition|(
name|subtractive
condition|)
block|{
name|value
operator|=
literal|1
operator|-
name|value
expr_stmt|;
block|}
name|dstComponents
index|[
name|k
index|]
operator|=
name|value
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Nonseparable blend modes are computed in RGB color space.
comment|// TODO - CMYK color spaces need special treatment.
if|if
condition|(
name|srcColorSpaceType
operator|==
name|ColorSpace
operator|.
name|TYPE_RGB
condition|)
block|{
name|srcConverted
operator|=
name|srcComponents
expr_stmt|;
block|}
else|else
block|{
name|srcConverted
operator|=
name|srcColorSpace
operator|.
name|toRGB
argument_list|(
name|srcComponents
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dstColorSpaceType
operator|==
name|ColorSpace
operator|.
name|TYPE_RGB
condition|)
block|{
name|dstConverted
operator|=
name|dstComponents
expr_stmt|;
block|}
else|else
block|{
name|dstConverted
operator|=
name|dstColorSpace
operator|.
name|toRGB
argument_list|(
name|dstComponents
argument_list|)
expr_stmt|;
block|}
name|nonSeparableBlendMode
operator|.
name|blend
argument_list|(
name|srcConverted
argument_list|,
name|dstConverted
argument_list|,
name|rgbResult
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
literal|3
condition|;
name|k
operator|++
control|)
block|{
name|float
name|srcValue
init|=
name|srcConverted
index|[
name|k
index|]
decl_stmt|;
name|float
name|dstValue
init|=
name|dstConverted
index|[
name|k
index|]
decl_stmt|;
name|float
name|value
init|=
name|rgbResult
index|[
name|k
index|]
decl_stmt|;
name|value
operator|=
name|Math
operator|.
name|max
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|value
argument_list|,
literal|1.0f
argument_list|)
argument_list|,
literal|0.0f
argument_list|)
expr_stmt|;
name|value
operator|=
name|srcValue
operator|+
name|dstAlpha
operator|*
operator|(
name|value
operator|-
name|srcValue
operator|)
expr_stmt|;
name|value
operator|=
name|dstValue
operator|+
name|srcAlphaRatio
operator|*
operator|(
name|value
operator|-
name|dstValue
operator|)
expr_stmt|;
name|rgbResult
index|[
name|k
index|]
operator|=
name|value
expr_stmt|;
block|}
if|if
condition|(
name|dstColorSpaceType
operator|==
name|ColorSpace
operator|.
name|TYPE_RGB
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|rgbResult
argument_list|,
literal|0
argument_list|,
name|dstComponents
argument_list|,
literal|0
argument_list|,
name|dstComponents
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|float
index|[]
name|temp
init|=
name|dstColorSpace
operator|.
name|fromRGB
argument_list|(
name|rgbResult
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|temp
argument_list|,
literal|0
argument_list|,
name|dstComponents
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|dstComponents
operator|.
name|length
argument_list|,
name|temp
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dstHasAlpha
condition|)
block|{
name|dstComponents
index|[
name|numDstColorComponents
index|]
operator|=
name|resultAlpha
expr_stmt|;
block|}
name|dstPixel
operator|=
name|dstColorModel
operator|.
name|getDataElements
argument_list|(
name|dstComponents
argument_list|,
literal|0
argument_list|,
name|dstPixel
argument_list|)
expr_stmt|;
name|dstOut
operator|.
name|setDataElements
argument_list|(
name|dstOutXShift
operator|+
name|x
argument_list|,
name|dstOutYShift
operator|+
name|y
argument_list|,
name|dstPixel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

