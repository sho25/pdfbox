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
name|io
operator|.
name|IOUtils
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
name|ResourceLoader
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
name|ICC_ColorSpace
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
name|ICC_Profile
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * Allows colors to be specified according to the subtractive CMYK (cyan, magenta, yellow, black)  * model typical of printers and other paper-based output devices.  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDDeviceCMYK
extends|extends
name|PDDeviceColorSpace
block|{
comment|/**  The single instance of this class. */
specifier|public
specifier|static
specifier|final
name|PDDeviceCMYK
name|INSTANCE
decl_stmt|;
static|static
block|{
try|try
block|{
name|INSTANCE
operator|=
operator|new
name|PDDeviceCMYK
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|final
name|ICC_ColorSpace
name|awtColorSpace
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|PDColor
name|INITIAL_COLOR
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
block|,
literal|1
block|}
argument_list|)
decl_stmt|;
specifier|private
name|PDDeviceCMYK
parameter_list|()
throws|throws
name|IOException
block|{
name|awtColorSpace
operator|=
name|getAWTColorSpace
argument_list|()
expr_stmt|;
block|}
comment|// loads the ICC color profile for CMYK
specifier|private
specifier|static
name|ICC_ColorSpace
name|getAWTColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
name|ICC_ColorSpace
name|colorSpace
decl_stmt|;
name|InputStream
name|profile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Properties
name|properties
init|=
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
literal|"org/apache/pdfbox/resources/PDDeviceCMYK.properties"
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
name|profile
operator|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"DeviceCMYK"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|profile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Default CMYK color profile could not be loaded"
argument_list|)
throw|;
block|}
name|ICC_Profile
name|iccProfile
init|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|profile
argument_list|)
decl_stmt|;
name|colorSpace
operator|=
operator|new
name|ICC_ColorSpace
argument_list|(
name|iccProfile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|profile
argument_list|)
expr_stmt|;
block|}
return|return
name|colorSpace
return|;
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
name|DEVICECMYK
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
block|{
return|return
literal|4
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
name|INITIAL_COLOR
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
return|return
name|toRGBImageAWT
argument_list|(
name|raster
argument_list|,
name|awtColorSpace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

