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
name|color
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
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
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
name|BufferedImage
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * A Lab colour space is a CIE-based ABC colour space with two transformation stages.  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDLab
extends|extends
name|PDCIEDictionaryBasedColorSpace
block|{
specifier|private
name|PDColor
name|initialColor
decl_stmt|;
comment|/**      * Creates a new Lab color space.      */
specifier|public
name|PDLab
parameter_list|()
block|{
name|super
argument_list|(
name|COSName
operator|.
name|LAB
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new Lab color space from a PDF array.      * @param lab the color space array      */
specifier|public
name|PDLab
parameter_list|(
name|COSArray
name|lab
parameter_list|)
block|{
name|super
argument_list|(
name|lab
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|LAB
operator|.
name|getName
argument_list|()
return|;
block|}
comment|//
comment|// WARNING: this method is performance sensitive, modify with care!
comment|//
annotation|@
name|Override
specifier|public
name|BufferedImage
name|toRGBImage
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|width
init|=
name|raster
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|raster
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|BufferedImage
name|rgbImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|WritableRaster
name|rgbRaster
init|=
name|rgbImage
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|float
name|minA
init|=
name|getARange
argument_list|()
operator|.
name|getMin
argument_list|()
decl_stmt|;
name|float
name|maxA
init|=
name|getARange
argument_list|()
operator|.
name|getMax
argument_list|()
decl_stmt|;
name|float
name|minB
init|=
name|getBRange
argument_list|()
operator|.
name|getMin
argument_list|()
decl_stmt|;
name|float
name|maxB
init|=
name|getBRange
argument_list|()
operator|.
name|getMax
argument_list|()
decl_stmt|;
comment|// always three components: ABC
name|float
index|[]
name|abc
init|=
operator|new
name|float
index|[
literal|3
index|]
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
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
literal|0
init|;
name|x
operator|<
name|width
condition|;
name|x
operator|++
control|)
block|{
name|raster
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|abc
argument_list|)
expr_stmt|;
comment|// 0..255 -> 0..1
name|abc
index|[
literal|0
index|]
operator|/=
literal|255
expr_stmt|;
name|abc
index|[
literal|1
index|]
operator|/=
literal|255
expr_stmt|;
name|abc
index|[
literal|2
index|]
operator|/=
literal|255
expr_stmt|;
comment|// scale to range
name|abc
index|[
literal|0
index|]
operator|*=
literal|100
expr_stmt|;
name|abc
index|[
literal|1
index|]
operator|=
name|minA
operator|+
operator|(
name|abc
index|[
literal|1
index|]
operator|*
operator|(
name|maxA
operator|-
name|minA
operator|)
operator|)
expr_stmt|;
name|abc
index|[
literal|2
index|]
operator|=
name|minB
operator|+
operator|(
name|abc
index|[
literal|2
index|]
operator|*
operator|(
name|maxB
operator|-
name|minB
operator|)
operator|)
expr_stmt|;
name|float
index|[]
name|rgb
init|=
name|toRGB
argument_list|(
name|abc
argument_list|)
decl_stmt|;
comment|// 0..1 -> 0..255
name|rgb
index|[
literal|0
index|]
operator|*=
literal|255
expr_stmt|;
name|rgb
index|[
literal|1
index|]
operator|*=
literal|255
expr_stmt|;
name|rgb
index|[
literal|2
index|]
operator|*=
literal|255
expr_stmt|;
name|rgbRaster
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgb
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rgbImage
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|toRGB
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
block|{
comment|// CIE LAB to RGB, see http://en.wikipedia.org/wiki/Lab_color_space
comment|// L*
name|float
name|lstar
init|=
operator|(
name|value
index|[
literal|0
index|]
operator|+
literal|16f
operator|)
operator|*
operator|(
literal|1f
operator|/
literal|116f
operator|)
decl_stmt|;
comment|// TODO: how to use the blackpoint? scale linearly between black& white?
comment|// XYZ
name|float
name|x
init|=
name|wpX
operator|*
name|inverse
argument_list|(
name|lstar
operator|+
name|value
index|[
literal|1
index|]
operator|*
operator|(
literal|1f
operator|/
literal|500f
operator|)
argument_list|)
decl_stmt|;
name|float
name|y
init|=
name|wpY
operator|*
name|inverse
argument_list|(
name|lstar
argument_list|)
decl_stmt|;
name|float
name|z
init|=
name|wpZ
operator|*
name|inverse
argument_list|(
name|lstar
operator|-
name|value
index|[
literal|2
index|]
operator|*
operator|(
literal|1f
operator|/
literal|200f
operator|)
argument_list|)
decl_stmt|;
return|return
name|convXYZtoRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|z
argument_list|)
return|;
block|}
comment|// reverse transformation (f^-1)
specifier|private
name|float
name|inverse
parameter_list|(
name|float
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|>
literal|6.0
operator|/
literal|29.0
condition|)
block|{
return|return
name|x
operator|*
name|x
operator|*
name|x
return|;
block|}
else|else
block|{
return|return
operator|(
literal|108f
operator|/
literal|841f
operator|)
operator|*
operator|(
name|x
operator|-
operator|(
literal|4f
operator|/
literal|29f
operator|)
operator|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|getDefaultDecode
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
block|{
name|PDRange
name|a
init|=
name|getARange
argument_list|()
decl_stmt|;
name|PDRange
name|b
init|=
name|getARange
argument_list|()
decl_stmt|;
return|return
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|100
block|,
name|a
operator|.
name|getMin
argument_list|()
block|,
name|a
operator|.
name|getMax
argument_list|()
block|,
name|b
operator|.
name|getMin
argument_list|()
block|,
name|b
operator|.
name|getMax
argument_list|()
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDColor
name|getInitialColor
parameter_list|()
block|{
if|if
condition|(
name|initialColor
operator|==
literal|null
condition|)
block|{
name|initialColor
operator|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|getARange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
block|,
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|getBRange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
block|}
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|initialColor
return|;
block|}
comment|/**      * creates a range array with default values (-100..100 -100..100).      * @return the new range array.      */
specifier|private
name|COSArray
name|getDefaultRangeArray
parameter_list|()
block|{
name|COSArray
name|range
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|range
return|;
block|}
comment|/**      * This will get the valid range for the "a" component.      * If none is found then the default will be returned, which is -100..100.      * @return the "a" range.      */
specifier|public
name|PDRange
name|getARange
parameter_list|()
block|{
name|COSArray
name|rangeArray
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|rangeArray
operator|==
literal|null
condition|)
block|{
name|rangeArray
operator|=
name|getDefaultRangeArray
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|PDRange
argument_list|(
name|rangeArray
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will get the valid range for the "b" component.      * If none is found  then the default will be returned, which is -100..100.      * @return the "b" range.      */
specifier|public
name|PDRange
name|getBRange
parameter_list|()
block|{
name|COSArray
name|rangeArray
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|rangeArray
operator|==
literal|null
condition|)
block|{
name|rangeArray
operator|=
name|getDefaultRangeArray
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|PDRange
argument_list|(
name|rangeArray
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * This will set the a range for the "a" component.      * @param range the new range for the "a" component,       * or null if defaults (-100..100) are to be set.      */
specifier|public
name|void
name|setARange
parameter_list|(
name|PDRange
name|range
parameter_list|)
block|{
name|setComponentRangeArray
argument_list|(
name|range
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the "b" range for this color space.      * @param range the new range for the "b" component,      * or null if defaults (-100..100) are to be set.      */
specifier|public
name|void
name|setBRange
parameter_list|(
name|PDRange
name|range
parameter_list|)
block|{
name|setComponentRangeArray
argument_list|(
name|range
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setComponentRangeArray
parameter_list|(
name|PDRange
name|range
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|COSArray
name|rangeArray
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|rangeArray
operator|==
literal|null
condition|)
block|{
name|rangeArray
operator|=
name|getDefaultRangeArray
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
comment|// reset to defaults
name|rangeArray
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
name|index
operator|+
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rangeArray
operator|.
name|set
argument_list|(
name|index
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|range
operator|.
name|getMin
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
name|index
operator|+
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|range
operator|.
name|getMax
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|rangeArray
argument_list|)
expr_stmt|;
name|initialColor
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

