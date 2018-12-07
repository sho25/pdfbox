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
name|Color
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
name|color
operator|.
name|CMMException
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
name|color
operator|.
name|ProfileDataException
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
name|DataBuffer
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|COSStream
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
name|PDDocument
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
name|COSArrayList
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
name|PDStream
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
name|Charsets
import|;
end_import

begin_comment
comment|/**  * ICCBased color spaces are based on a cross-platform color profile as defined by the  * International Color Consortium (ICC).  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDICCBased
extends|extends
name|PDCIEBasedColorSpace
block|{
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
name|PDICCBased
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PDStream
name|stream
decl_stmt|;
specifier|private
name|int
name|numberOfComponents
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|ICC_Profile
name|iccProfile
decl_stmt|;
specifier|private
name|PDColorSpace
name|alternateColorSpace
decl_stmt|;
specifier|private
name|ICC_ColorSpace
name|awtColorSpace
decl_stmt|;
specifier|private
name|PDColor
name|initialColor
decl_stmt|;
specifier|private
name|boolean
name|isRGB
init|=
literal|false
decl_stmt|;
comment|// allows to force using alternate color space instead of ICC color space for performance
comment|// reasons with LittleCMS (LCMS), see PDFBOX-4309
comment|// WARNING: do not activate this in a conforming reader
specifier|private
name|boolean
name|useOnlyAlternateColorSpace
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|IS_KCMS
decl_stmt|;
static|static
block|{
name|String
name|cmmProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"sun.java2d.cmm"
argument_list|)
decl_stmt|;
name|IS_KCMS
operator|=
operator|!
name|isMinJdk8
argument_list|()
operator|||
literal|"sun.java2d.cmm.kcms.KcmsServiceProvider"
operator|.
name|equals
argument_list|(
name|cmmProperty
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new ICC color space with an empty stream.      * @param doc the document to store the ICC data      */
specifier|public
name|PDICCBased
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|ICCBASED
argument_list|)
expr_stmt|;
name|stream
operator|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new ICC color space using the PDF array.      *      * @param iccArray the ICC stream object      * @throws IOException if there is an error reading the ICC profile or if the parameter      * is invalid.      */
specifier|public
name|PDICCBased
parameter_list|(
name|COSArray
name|iccArray
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|iccArray
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"ICCBased colorspace array must have two elements"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|iccArray
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|COSStream
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"ICCBased colorspace array must have a stream as second element"
argument_list|)
throw|;
block|}
name|useOnlyAlternateColorSpace
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.pdfbox.rendering.UseAlternateInsteadOfICCColorSpace"
argument_list|)
operator|!=
literal|null
expr_stmt|;
name|array
operator|=
name|iccArray
expr_stmt|;
name|stream
operator|=
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|iccArray
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|loadICCProfile
argument_list|()
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
name|ICCBASED
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Get the underlying ICC profile stream.      * @return the underlying ICC profile stream      */
specifier|public
name|PDStream
name|getPDStream
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
comment|/**      * Load the ICC profile, or init alternateColorSpace color space.      */
specifier|private
name|void
name|loadICCProfile
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|useOnlyAlternateColorSpace
condition|)
block|{
try|try
block|{
name|fallbackToAlternateColorSpace
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error initializing alternate color space: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
try|try
init|(
name|InputStream
name|input
init|=
name|this
operator|.
name|stream
operator|.
name|createInputStream
argument_list|()
init|)
block|{
comment|// if the embedded profile is sRGB then we can use Java's built-in profile, which
comment|// results in a large performance gain as it's our native color space, see PDFBOX-2587
name|ICC_Profile
name|profile
decl_stmt|;
synchronized|synchronized
init|(
name|LOG
init|)
block|{
name|profile
operator|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|input
argument_list|)
expr_stmt|;
if|if
condition|(
name|is_sRGB
argument_list|(
name|profile
argument_list|)
condition|)
block|{
name|isRGB
operator|=
literal|true
expr_stmt|;
name|awtColorSpace
operator|=
operator|(
name|ICC_ColorSpace
operator|)
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
expr_stmt|;
name|iccProfile
operator|=
name|awtColorSpace
operator|.
name|getProfile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|profile
operator|=
name|ensureDisplayProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
name|awtColorSpace
operator|=
operator|new
name|ICC_ColorSpace
argument_list|(
name|profile
argument_list|)
expr_stmt|;
name|iccProfile
operator|=
name|profile
expr_stmt|;
block|}
comment|// set initial colour
name|float
index|[]
name|initial
init|=
operator|new
name|float
index|[
name|getNumberOfComponents
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|getNumberOfComponents
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|initial
index|[
name|c
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|getRangeForComponent
argument_list|(
name|c
argument_list|)
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initialColor
operator|=
operator|new
name|PDColor
argument_list|(
name|initial
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|IS_KCMS
condition|)
block|{
comment|// do things that trigger a ProfileDataException
comment|// or CMMException due to invalid profiles, see PDFBOX-1295 and PDFBOX-1740 (ü-file)
comment|// or ArrayIndexOutOfBoundsException, see PDFBOX-3610
comment|// also triggers a ProfileDataException for PDFBOX-3549 with KCMS
operator|new
name|Color
argument_list|(
name|awtColorSpace
argument_list|,
operator|new
name|float
index|[
name|getNumberOfComponents
argument_list|()
index|]
argument_list|,
literal|1f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// PDFBOX-4015: this one triggers "CMMException: LCMS error 13" with LCMS
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
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ProfileDataException
decl||
name|CMMException
decl||
name|IllegalArgumentException
decl||
name|ArrayIndexOutOfBoundsException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|fallbackToAlternateColorSpace
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|fallbackToAlternateColorSpace
parameter_list|(
name|Exception
name|e
parameter_list|)
throws|throws
name|IOException
block|{
name|awtColorSpace
operator|=
literal|null
expr_stmt|;
name|alternateColorSpace
operator|=
name|getAlternateColorSpace
argument_list|()
expr_stmt|;
if|if
condition|(
name|alternateColorSpace
operator|.
name|equals
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
condition|)
block|{
name|isRGB
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't read embedded ICC profile ("
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
operator|+
literal|"), using alternate color space: "
operator|+
name|alternateColorSpace
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initialColor
operator|=
name|alternateColorSpace
operator|.
name|getInitialColor
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns true if the given profile is represents sRGB.      */
specifier|private
name|boolean
name|is_sRGB
parameter_list|(
name|ICC_Profile
name|profile
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|profile
operator|.
name|getData
argument_list|(
name|ICC_Profile
operator|.
name|icSigHead
argument_list|)
argument_list|,
name|ICC_Profile
operator|.
name|icHdrModel
argument_list|,
name|ICC_Profile
operator|.
name|icHdrModel
operator|+
literal|7
argument_list|)
decl_stmt|;
name|String
name|deviceModel
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
return|return
name|deviceModel
operator|.
name|equals
argument_list|(
literal|"sRGB"
argument_list|)
return|;
block|}
comment|// PDFBOX-4114: fix profile that has the wrong display class,
comment|// as done by Harald Kuhr in twelvemonkeys JPEGImageReader.ensureDisplayProfile()
specifier|private
specifier|static
name|ICC_Profile
name|ensureDisplayProfile
parameter_list|(
name|ICC_Profile
name|profile
parameter_list|)
block|{
if|if
condition|(
name|profile
operator|.
name|getProfileClass
argument_list|()
operator|!=
name|ICC_Profile
operator|.
name|CLASS_DISPLAY
condition|)
block|{
name|byte
index|[]
name|profileData
init|=
name|profile
operator|.
name|getData
argument_list|()
decl_stmt|;
comment|// Need to clone entire profile, due to a OpenJDK bug
if|if
condition|(
name|profileData
index|[
name|ICC_Profile
operator|.
name|icHdrRenderingIntent
index|]
operator|==
name|ICC_Profile
operator|.
name|icPerceptual
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"ICC profile is Perceptual, ignoring, treating as Display class"
argument_list|)
expr_stmt|;
name|intToBigEndian
argument_list|(
name|ICC_Profile
operator|.
name|icSigDisplayClass
argument_list|,
name|profileData
argument_list|,
name|ICC_Profile
operator|.
name|icHdrDeviceClass
argument_list|)
expr_stmt|;
return|return
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|profileData
argument_list|)
return|;
block|}
block|}
return|return
name|profile
return|;
block|}
specifier|private
specifier|static
name|void
name|intToBigEndian
parameter_list|(
name|int
name|value
parameter_list|,
name|byte
index|[]
name|array
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|array
index|[
name|index
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|value
operator|>>
literal|24
argument_list|)
expr_stmt|;
name|array
index|[
name|index
operator|+
literal|1
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|value
operator|>>
literal|16
argument_list|)
expr_stmt|;
name|array
index|[
name|index
operator|+
literal|2
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|value
operator|>>
literal|8
argument_list|)
expr_stmt|;
name|array
index|[
name|index
operator|+
literal|3
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|value
argument_list|)
expr_stmt|;
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
throws|throws
name|IOException
block|{
if|if
condition|(
name|isRGB
condition|)
block|{
return|return
name|value
return|;
block|}
if|if
condition|(
name|awtColorSpace
operator|!=
literal|null
condition|)
block|{
comment|// PDFBOX-2142: clamp bad values
comment|// WARNING: toRGB is very slow when used with LUT-based ICC profiles
return|return
name|awtColorSpace
operator|.
name|toRGB
argument_list|(
name|clampColors
argument_list|(
name|awtColorSpace
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|alternateColorSpace
operator|.
name|toRGB
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
specifier|private
name|float
index|[]
name|clampColors
parameter_list|(
name|ICC_ColorSpace
name|cs
parameter_list|,
name|float
index|[]
name|value
parameter_list|)
block|{
name|float
index|[]
name|result
init|=
operator|new
name|float
index|[
name|value
operator|.
name|length
index|]
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
name|value
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|float
name|minValue
init|=
name|cs
operator|.
name|getMinValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|float
name|maxValue
init|=
name|cs
operator|.
name|getMaxValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|result
index|[
name|i
index|]
operator|=
name|value
index|[
name|i
index|]
operator|<
name|minValue
condition|?
name|minValue
else|:
operator|(
name|value
index|[
name|i
index|]
operator|>
name|maxValue
condition|?
name|maxValue
else|:
name|value
index|[
name|i
index|]
operator|)
expr_stmt|;
block|}
return|return
name|result
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
if|if
condition|(
name|awtColorSpace
operator|!=
literal|null
condition|)
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
else|else
block|{
return|return
name|alternateColorSpace
operator|.
name|toRGBImage
argument_list|(
name|raster
argument_list|)
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
if|if
condition|(
name|numberOfComponents
operator|<
literal|0
condition|)
block|{
name|numberOfComponents
operator|=
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
expr_stmt|;
block|}
return|return
name|numberOfComponents
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
if|if
condition|(
name|awtColorSpace
operator|!=
literal|null
condition|)
block|{
name|int
name|n
init|=
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|float
index|[]
name|decode
init|=
operator|new
name|float
index|[
name|n
operator|*
literal|2
index|]
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
name|n
condition|;
name|i
operator|++
control|)
block|{
name|decode
index|[
name|i
operator|*
literal|2
index|]
operator|=
name|awtColorSpace
operator|.
name|getMinValue
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|decode
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
operator|=
name|awtColorSpace
operator|.
name|getMaxValue
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|decode
return|;
block|}
else|else
block|{
return|return
name|alternateColorSpace
operator|.
name|getDefaultDecode
argument_list|(
name|bitsPerComponent
argument_list|)
return|;
block|}
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
comment|/**      * Returns a list of alternate color spaces for non-conforming readers.      * WARNING: Do not use the information in a conforming reader.      * @return A list of alternateColorSpace color spaces.      * @throws IOException If there is an error getting the alternateColorSpace color spaces.      */
specifier|public
name|PDColorSpace
name|getAlternateColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|alternate
init|=
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ALTERNATE
argument_list|)
decl_stmt|;
name|COSArray
name|alternateArray
decl_stmt|;
if|if
condition|(
name|alternate
operator|==
literal|null
condition|)
block|{
name|alternateArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|int
name|numComponents
init|=
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|COSName
name|csName
decl_stmt|;
switch|switch
condition|(
name|numComponents
condition|)
block|{
case|case
literal|1
case|:
name|csName
operator|=
name|COSName
operator|.
name|DEVICEGRAY
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|csName
operator|=
name|COSName
operator|.
name|DEVICERGB
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|csName
operator|=
name|COSName
operator|.
name|DEVICECMYK
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown color space number of components:"
operator|+
name|numComponents
argument_list|)
throw|;
block|}
name|alternateArray
operator|.
name|add
argument_list|(
name|csName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|alternate
operator|instanceof
name|COSArray
condition|)
block|{
name|alternateArray
operator|=
operator|(
name|COSArray
operator|)
name|alternate
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|alternate
operator|instanceof
name|COSName
condition|)
block|{
name|alternateArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|alternateArray
operator|.
name|add
argument_list|(
name|alternate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: expected COSArray or COSName and not "
operator|+
name|alternate
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|PDColorSpace
operator|.
name|create
argument_list|(
name|alternateArray
argument_list|)
return|;
block|}
comment|/**      * Returns the range for a certain component number.      * This will never return null.      * If it is not present then the range 0..1 will be returned.      * @param n the component number to get the range for      * @return the range for this component      */
specifier|public
name|PDRange
name|getRangeForComponent
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|COSArray
name|rangeArray
init|=
operator|(
name|COSArray
operator|)
name|stream
operator|.
name|getCOSObject
argument_list|()
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
operator|||
name|rangeArray
operator|.
name|size
argument_list|()
operator|<
name|getNumberOfComponents
argument_list|()
operator|*
literal|2
condition|)
block|{
return|return
operator|new
name|PDRange
argument_list|()
return|;
comment|// 0..1
block|}
return|return
operator|new
name|PDRange
argument_list|(
name|rangeArray
argument_list|,
name|n
argument_list|)
return|;
block|}
comment|/**      * Returns the metadata stream for this object, or null if there is no metadata stream.      * @return the metadata stream, or null if there is none      */
specifier|public
name|COSStream
name|getMetadata
parameter_list|()
block|{
return|return
operator|(
name|COSStream
operator|)
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
return|;
block|}
comment|/**      * Returns the type of the color space in the ICC profile. If the ICC profile is invalid, the      * type of the alternate colorspace is returned, which will be one of      * {@link ColorSpace#TYPE_GRAY TYPE_GRAY}, {@link ColorSpace#TYPE_RGB TYPE_RGB},      * {@link ColorSpace#TYPE_CMYK TYPE_CMYK}, or -1 if that one is invalid.      *      * @return an ICC color space type. See {@link ColorSpace#getType()} and the static values of      * {@link ColorSpace} for more details.      */
specifier|public
name|int
name|getColorSpaceType
parameter_list|()
block|{
if|if
condition|(
name|iccProfile
operator|!=
literal|null
condition|)
block|{
return|return
name|iccProfile
operator|.
name|getColorSpaceType
argument_list|()
return|;
block|}
comment|// if the ICC Profile could not be read
switch|switch
condition|(
name|alternateColorSpace
operator|.
name|getNumberOfComponents
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_GRAY
return|;
case|case
literal|3
case|:
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_RGB
return|;
case|case
literal|4
case|:
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_CMYK
return|;
default|default:
comment|// should not happen as all ICC color spaces in PDF must have 1,3, or 4 components
return|return
operator|-
literal|1
return|;
block|}
block|}
comment|/**      * Sets the number of color components.      * @param n the number of color components      */
comment|// TODO it's probably not safe to use this
annotation|@
name|Deprecated
specifier|public
name|void
name|setNumberOfComponents
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|numberOfComponents
operator|=
name|n
expr_stmt|;
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the list of alternateColorSpace color spaces.      *      * @param list the list of color space objects      */
specifier|public
name|void
name|setAlternateColorSpaces
parameter_list|(
name|List
argument_list|<
name|PDColorSpace
argument_list|>
name|list
parameter_list|)
block|{
name|COSArray
name|altArray
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|altArray
operator|=
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ALTERNATE
argument_list|,
name|altArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the range for this color space.      * @param range the new range for the a component      * @param n the component to set the range for      */
specifier|public
name|void
name|setRangeForComponent
parameter_list|(
name|PDRange
name|range
parameter_list|,
name|int
name|n
parameter_list|)
block|{
name|COSArray
name|rangeArray
init|=
operator|(
name|COSArray
operator|)
name|stream
operator|.
name|getCOSObject
argument_list|()
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
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|stream
operator|.
name|getCOSObject
argument_list|()
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
block|}
comment|// extend range array with default values if needed
while|while
condition|(
name|rangeArray
operator|.
name|size
argument_list|()
operator|<
operator|(
name|n
operator|+
literal|1
operator|)
operator|*
literal|2
condition|)
block|{
name|rangeArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|rangeArray
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|rangeArray
operator|.
name|set
argument_list|(
name|n
operator|*
literal|2
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
name|n
operator|*
literal|2
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
comment|/**      * Sets the metadata stream that is associated with this color space.      * @param metadata the new metadata stream      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|COSStream
name|metadata
parameter_list|)
block|{
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
literal|"{numberOfComponents: "
operator|+
name|getNumberOfComponents
argument_list|()
operator|+
literal|"}"
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isMinJdk8
parameter_list|()
block|{
comment|// strategy from lucene-solr/lucene/core/src/java/org/apache/lucene/util/Constants.java
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
decl_stmt|;
specifier|final
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|version
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|major
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|minor
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|minor
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|major
operator|>
literal|1
operator|||
operator|(
name|major
operator|==
literal|1
operator|&&
name|minor
operator|>=
literal|8
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
comment|// maybe some new numbering scheme in the 22nd century
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

