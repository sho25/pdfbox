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
name|java
operator|.
name|awt
operator|.
name|Transparency
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
name|ComponentColorModel
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

begin_comment
comment|/**  * Colours in the DeviceRGB colour space are specified according to the additive  * RGB (red-green-blue) colour model.  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDDeviceRGB
extends|extends
name|PDDeviceColorSpace
block|{
comment|/**  This is the single instance of this class. */
specifier|public
specifier|static
specifier|final
name|PDDeviceRGB
name|INSTANCE
init|=
operator|new
name|PDDeviceRGB
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|PDColor
name|initialColor
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|this
argument_list|)
decl_stmt|;
specifier|private
specifier|volatile
name|ColorSpace
name|awtColorSpace
decl_stmt|;
specifier|private
name|PDDeviceRGB
parameter_list|()
block|{     }
comment|/**      * Lazy setting of the AWT color space due to JDK race condition.      */
specifier|private
name|void
name|init
parameter_list|()
block|{
comment|// no need to synchronize this check as it is atomic
if|if
condition|(
name|awtColorSpace
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|// we might have been waiting for another thread, so check again
if|if
condition|(
name|awtColorSpace
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|awtColorSpace
operator|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
expr_stmt|;
comment|// there is a JVM bug which results in a CMMException which appears to be a race
comment|// condition caused by lazy initialization of the color transform, so we perform
comment|// an initial color conversion while we're still synchronized, see PDFBOX-2184
name|awtColorSpace
operator|.
name|toRGB
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
block|}
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
name|DEVICERGB
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
return|return
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|0
block|,
literal|1
block|,
literal|0
block|,
literal|1
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
return|return
name|initialColor
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
name|init
argument_list|()
expr_stmt|;
return|return
name|awtColorSpace
operator|.
name|toRGB
argument_list|(
name|value
argument_list|)
return|;
block|}
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
name|init
argument_list|()
expr_stmt|;
name|ColorModel
name|colorModel
init|=
operator|new
name|ComponentColorModel
argument_list|(
name|awtColorSpace
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|OPAQUE
argument_list|,
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|.
name|getDataType
argument_list|()
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
operator|new
name|BufferedImage
argument_list|(
name|colorModel
argument_list|,
name|raster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|//
comment|// WARNING: this method is performance sensitive, modify with care!
comment|//
comment|// Please read PDFBOX-3854 and look at the related commits first.
comment|// The current code returns TYPE_INT_RGB images which prevents slowness due to threads
comment|// blocking each other when TYPE_CUSTOM images are used.
comment|// ColorConvertOp is not used here because it has a larger memory footprint and no further
comment|// performance improvement.
comment|// The multiparameter setRGB() call is not used because it brings no improvement.
name|BufferedImage
name|dest
init|=
operator|new
name|BufferedImage
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|int
name|width
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|image
operator|.
name|getHeight
argument_list|()
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
name|width
condition|;
operator|++
name|x
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
name|height
condition|;
operator|++
name|y
control|)
block|{
name|dest
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dest
return|;
block|}
block|}
end_class

end_unit

