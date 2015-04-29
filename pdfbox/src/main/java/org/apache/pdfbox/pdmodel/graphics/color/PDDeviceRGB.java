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
specifier|private
specifier|static
specifier|final
name|ColorSpace
name|colorSpaceRGB
init|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
decl_stmt|;
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
name|PDDeviceRGB
parameter_list|()
block|{
comment|// there is a JVM bug which results in a CMMException which appears to be a race
comment|// condition caused by lazy initialization of the color transform, so we perform
comment|// an initial color conversion while we're still in a static context, see PDFBOX-2184
name|colorSpaceRGB
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
block|}
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
name|DEVICERGB
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * @inheritDoc      */
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
return|return
name|colorSpaceRGB
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
name|ColorModel
name|colorModel
init|=
operator|new
name|ComponentColorModel
argument_list|(
name|colorSpaceRGB
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
return|return
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
return|;
block|}
block|}
end_class

end_unit

