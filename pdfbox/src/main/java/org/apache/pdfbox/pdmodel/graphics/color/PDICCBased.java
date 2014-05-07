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
name|WritableRaster
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * ICCBased colour spaces are based on a cross-platform colour profile as defined by the  * International Color Consortium (ICC).  *  * @author Ben Litchfield  * @author John Hewson  */
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
name|array
operator|.
name|add
argument_list|(
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new ICC color space using the PDF array.      * @param iccArray the ICC stream object      */
specifier|public
name|PDICCBased
parameter_list|(
name|COSArray
name|iccArray
parameter_list|)
throws|throws
name|IOException
block|{
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
comment|// load the ICC profile, or init alternateColorSpace color space
specifier|private
name|void
name|loadICCProfile
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|profile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|profile
operator|=
name|stream
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
name|iccProfile
operator|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|profile
argument_list|)
expr_stmt|;
name|awtColorSpace
operator|=
operator|new
name|ICC_ColorSpace
argument_list|(
name|iccProfile
argument_list|)
expr_stmt|;
comment|// set initial colour
name|float
index|[]
name|initial
init|=
operator|new
name|float
index|[
name|getColorSpaceType
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
argument_list|)
expr_stmt|;
comment|// create a color in order to trigger a ProfileDataException
comment|// or CMMException due to invalid profiles, see PDFBOX-1295 and PDFBOX-1740
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
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|ProfileDataException
operator|||
name|e
operator|instanceof
name|CMMException
condition|)
block|{
comment|// fall back to alternateColorSpace color space
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't read embedded ICC profile, using alternate color space"
argument_list|)
expr_stmt|;
name|awtColorSpace
operator|=
literal|null
expr_stmt|;
name|alternateColorSpace
operator|=
name|getAlternateColorSpaces
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|initialColor
operator|=
name|alternateColorSpace
operator|.
name|getInitialColor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
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
name|awtColorSpace
operator|!=
literal|null
condition|)
block|{
comment|// WARNING: toRGB is very slow when used with LUT-based ICC profiles
return|return
name|awtColorSpace
operator|.
name|toRGB
argument_list|(
name|value
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
name|getStream
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
name|List
argument_list|<
name|PDColorSpace
argument_list|>
name|getAlternateColorSpaces
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|alternate
init|=
name|stream
operator|.
name|getStream
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
if|if
condition|(
name|numComponents
operator|==
literal|1
condition|)
block|{
name|csName
operator|=
name|COSName
operator|.
name|DEVICEGRAY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|numComponents
operator|==
literal|3
condition|)
block|{
name|csName
operator|=
name|COSName
operator|.
name|DEVICERGB
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|numComponents
operator|==
literal|4
condition|)
block|{
name|csName
operator|=
name|COSName
operator|.
name|DEVICECMYK
expr_stmt|;
block|}
else|else
block|{
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
name|List
argument_list|<
name|PDColorSpace
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|PDColorSpace
argument_list|>
argument_list|()
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
name|alternateArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|PDColorSpace
operator|.
name|create
argument_list|(
name|alternateArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
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
name|getStream
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
name|getStream
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
comment|/**      * Returns the type of the color space in the ICC profile.      * Will be one of {@code TYPE_GRAY}, {@code TYPE_RGB}, or {@code TYPE_CMYK}.      * @return an ICC color space type      */
specifier|public
name|int
name|getColorSpaceType
parameter_list|()
block|{
return|return
name|iccProfile
operator|.
name|getColorSpaceType
argument_list|()
return|;
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
name|getStream
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
comment|/**      * Sets the list of alternateColorSpace color spaces.      * This should be a list of PDColorSpace objects.      * @param list the list of color space objects      */
specifier|public
name|void
name|setAlternateColorSpaces
parameter_list|(
name|List
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
name|getStream
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
name|getStream
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
name|getStream
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
name|getStream
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
block|}
end_class

end_unit

