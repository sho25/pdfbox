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
name|xobject
package|;
end_package

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

begin_comment
comment|/**  * This class is responsible for combining a base image with an SMask-based transparency  * image to form a composite image.  * See section 11.5 of the pdf specification for details on Soft Masks.  *<p/>  * Briefly however, an Smask is a supplementary greyscale image whose RGB-values define  * a transparency mask which, when combined appropriately with the base image,  * allows per-pixel transparency to be applied.  *<p/>  * Note that Smasks are not required for any image and if the smask is not present  * in the pdf file, the image will have no transparent pixels.  *  * @author Neil McErlean  */
end_comment

begin_class
specifier|public
class|class
name|CompositeImage
block|{
specifier|private
name|BufferedImage
name|baseImage
decl_stmt|;
specifier|private
name|BufferedImage
name|smaskImage
decl_stmt|;
comment|/**      * Standard constructor.      * @param baseImage the base Image.      * @param smaskImage the transparency image.      *      */
specifier|public
name|CompositeImage
parameter_list|(
name|BufferedImage
name|baseImage
parameter_list|,
name|BufferedImage
name|smaskImage
parameter_list|)
block|{
name|this
operator|.
name|baseImage
operator|=
name|baseImage
expr_stmt|;
name|this
operator|.
name|smaskImage
operator|=
name|smaskImage
expr_stmt|;
block|}
comment|/**      * This method applies the specified transparency mask to a given image and returns a new BufferedImage      * whose alpha values are computed from the transparency mask (smask) image.      */
specifier|public
name|BufferedImage
name|createMaskedImage
parameter_list|(
name|COSArray
name|decodeArray
parameter_list|)
throws|throws
name|IOException
block|{
comment|// The decode array should only be [0 1] or [1 0]. See PDF spec.
comment|// [0 1] means the smask's RGB values give transparency. Default: see PDF spec section 8.9.5.1
comment|// [1 0] means the smask's RGB values give opacity.
name|boolean
name|isOpaque
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|decodeArray
operator|!=
literal|null
condition|)
block|{
name|isOpaque
operator|=
name|decodeArray
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
operator|>
name|decodeArray
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|baseImageWidth
init|=
name|baseImage
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|baseImageHeight
init|=
name|baseImage
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|BufferedImage
name|result
init|=
operator|new
name|BufferedImage
argument_list|(
name|baseImageWidth
argument_list|,
name|baseImageHeight
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|baseImageWidth
condition|;
name|x
operator|++
control|)
block|{
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|baseImageHeight
condition|;
name|y
operator|++
control|)
block|{
name|int
name|rgb
init|=
name|baseImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|int
name|alpha
init|=
name|smaskImage
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
comment|// The smask image defines a transparency mask but it has no alpha values itself, instead
comment|// using the greyscale values to indicate transparency.
comment|// 0xAARRGGBB
comment|// We need to remove any alpha value in the main image.
name|int
name|rgbOnly
init|=
literal|0x00FFFFFF
operator|&
name|rgb
decl_stmt|;
comment|// We need to use one of the rgb values as the new alpha value for the main image.
comment|// It seems the mask is greyscale, so it shouldn't matter whether we use R, G or B
comment|// as the indicator of transparency.
if|if
condition|(
name|isOpaque
condition|)
block|{
name|alpha
operator|=
operator|~
name|alpha
expr_stmt|;
block|}
name|int
name|alphaOnly
init|=
name|alpha
operator|<<
literal|24
decl_stmt|;
name|result
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgbOnly
operator||
name|alphaOnly
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

