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
name|COSObject
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
name|PDResources
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
name|COSObjectable
import|;
end_import

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
name|ColorConvertOp
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

begin_comment
comment|/**  * A color space specifies how the colours of graphics objects will be painted on the page.  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDColorSpace
implements|implements
name|COSObjectable
block|{
comment|/**      * Creates a color space space given a name or array.      * @param colorSpace the color space COS object      * @return a new color space      * @throws IOException if the color space is unknown or cannot be created      */
specifier|public
specifier|static
name|PDColorSpace
name|create
parameter_list|(
name|COSBase
name|colorSpace
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|create
argument_list|(
name|colorSpace
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a color space given a name or array.      * @param colorSpace the color space COS object      * @param resources the current resources.      * @return a new color space      * @throws MissingException if the color space is missing from the resources dictionary      * @throws IOException if the color space is unknown or cannot be created      */
specifier|public
specifier|static
name|PDColorSpace
name|create
parameter_list|(
name|COSBase
name|colorSpace
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|colorSpace
operator|instanceof
name|COSObject
condition|)
block|{
return|return
name|create
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|colorSpace
operator|)
operator|.
name|getObject
argument_list|()
argument_list|,
name|resources
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|colorSpace
decl_stmt|;
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|DEVICECMYK
operator|||
name|name
operator|==
name|COSName
operator|.
name|CMYK
condition|)
block|{
return|return
name|PDDeviceCMYK
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|DEVICERGB
operator|||
name|name
operator|==
name|COSName
operator|.
name|RGB
condition|)
block|{
return|return
name|PDDeviceRGB
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|DEVICEGRAY
operator|||
name|name
operator|==
name|COSName
operator|.
name|G
condition|)
block|{
return|return
name|PDDeviceGray
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|PATTERN
condition|)
block|{
return|return
operator|new
name|PDPattern
argument_list|(
name|resources
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|PDColorSpace
name|cs
init|=
name|resources
operator|.
name|getColorSpace
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingException
argument_list|(
literal|"Missing color space: "
operator|+
name|name
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|cs
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|MissingException
argument_list|(
literal|"Unknown color space: "
operator|+
name|name
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|colorSpace
decl_stmt|;
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// TODO cache these returned color spaces?
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|CALGRAY
condition|)
block|{
return|return
operator|new
name|PDCalGray
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|CALRGB
condition|)
block|{
return|return
operator|new
name|PDCalRGB
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|DEVICEN
condition|)
block|{
return|return
operator|new
name|PDDeviceN
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|INDEXED
operator|||
name|name
operator|==
name|COSName
operator|.
name|I
condition|)
block|{
return|return
operator|new
name|PDIndexed
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|SEPARATION
condition|)
block|{
return|return
operator|new
name|PDSeparation
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|ICCBASED
condition|)
block|{
return|return
operator|new
name|PDICCBased
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|LAB
condition|)
block|{
return|return
operator|new
name|PDLab
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|PATTERN
condition|)
block|{
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
operator|new
name|PDPattern
argument_list|(
name|resources
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|PDPattern
argument_list|(
name|resources
argument_list|,
name|PDColorSpace
operator|.
name|create
argument_list|(
name|array
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|name
operator|==
name|COSName
operator|.
name|DEVICECMYK
operator|||
name|name
operator|==
name|COSName
operator|.
name|CMYK
operator|||
name|name
operator|==
name|COSName
operator|.
name|DEVICERGB
operator|||
name|name
operator|==
name|COSName
operator|.
name|RGB
operator|||
name|name
operator|==
name|COSName
operator|.
name|DEVICEGRAY
operator|||
name|name
operator|==
name|COSName
operator|.
name|PATTERN
condition|)
block|{
comment|// not allowed in an array, but we sometimes encounter these regardless
return|return
name|create
argument_list|(
name|name
argument_list|,
name|resources
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid color space kind: "
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected a name or array but got: "
operator|+
name|colorSpace
argument_list|)
throw|;
block|}
block|}
comment|// array for the given parameters
specifier|protected
name|COSArray
name|array
decl_stmt|;
comment|/**      * Returns the name of the color space.      * @return the name of the color space      */
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the number of components in this color space      * @return the number of components in this color space      */
specifier|public
specifier|abstract
name|int
name|getNumberOfComponents
parameter_list|()
function_decl|;
comment|/**      * Returns the default decode array for this color space.      * @return the default decode array      */
specifier|public
specifier|abstract
name|float
index|[]
name|getDefaultDecode
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
function_decl|;
comment|/**      * Returns the initial color value for this color space.      * @return the initial color value for this color space      */
specifier|public
specifier|abstract
name|PDColor
name|getInitialColor
parameter_list|()
function_decl|;
comment|/**      * Returns the RGB equivalent of the given color value.      * @param value a color value with component values between 0 and 1      * @return an array of R,G,B value between 0 and 255      * @throws IOException if the color conversion fails      */
specifier|public
specifier|abstract
name|float
index|[]
name|toRGB
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the (A)RGB equivalent of the given raster.      * @param raster the source raster      * @return an (A)RGB buffered image      * @throws IOException if the color conversion fails      */
specifier|public
specifier|abstract
name|BufferedImage
name|toRGBImage
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the (A)RGB equivalent of the given raster, using the given AWT color space      * to perform the conversion.      * @param raster the source raster      * @param colorSpace the AWT      * @return an (A)RGB buffered image      */
specifier|protected
name|BufferedImage
name|toRGBImageAWT
parameter_list|(
name|WritableRaster
name|raster
parameter_list|,
name|ColorSpace
name|colorSpace
parameter_list|)
block|{
comment|//
comment|// WARNING: this method is performance sensitive, modify with care!
comment|//
comment|// ICC Profile color transforms are only fast when performed using ColorConvertOp
name|ColorModel
name|colorModel
init|=
operator|new
name|ComponentColorModel
argument_list|(
name|colorSpace
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
name|src
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
name|BufferedImage
name|dest
init|=
operator|new
name|BufferedImage
argument_list|(
name|raster
operator|.
name|getWidth
argument_list|()
argument_list|,
name|raster
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
decl_stmt|;
name|ColorConvertOp
name|op
init|=
operator|new
name|ColorConvertOp
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|op
operator|.
name|filter
argument_list|(
name|src
argument_list|,
name|dest
argument_list|)
expr_stmt|;
return|return
name|dest
return|;
block|}
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|array
return|;
block|}
specifier|public
specifier|static
class|class
name|MissingException
extends|extends
name|IOException
block|{
specifier|private
name|MissingException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

