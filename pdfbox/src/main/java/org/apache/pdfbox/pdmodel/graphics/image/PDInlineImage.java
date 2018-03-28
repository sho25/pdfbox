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
name|image
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|List
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
name|COSDictionary
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
name|filter
operator|.
name|DecodeOptions
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
name|filter
operator|.
name|DecodeResult
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterFactory
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
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
name|graphics
operator|.
name|color
operator|.
name|PDDeviceGray
import|;
end_import

begin_comment
comment|/**  * An inline image object which uses a special syntax to express the data for a  * small image directly within the content stream.  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDInlineImage
implements|implements
name|PDImage
block|{
comment|// image parameters
specifier|private
specifier|final
name|COSDictionary
name|parameters
decl_stmt|;
comment|// the current resources, contains named color spaces
specifier|private
specifier|final
name|PDResources
name|resources
decl_stmt|;
comment|// image data
specifier|private
specifier|final
name|byte
index|[]
name|rawData
decl_stmt|;
specifier|private
specifier|final
name|byte
index|[]
name|decodedData
decl_stmt|;
comment|/**      * Creates an inline image from the given parameters and data.      *      * @param parameters the image parameters      * @param data the image data      * @param resources the current resources      * @throws IOException if the stream cannot be decoded      */
specifier|public
name|PDInlineImage
parameter_list|(
name|COSDictionary
name|parameters
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|rawData
operator|=
name|data
expr_stmt|;
name|DecodeResult
name|decodeResult
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|==
literal|null
operator|||
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|decodedData
operator|=
name|data
expr_stmt|;
block|}
else|else
block|{
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|data
operator|.
name|length
argument_list|)
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
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|// TODO handling of abbreviated names belongs here, rather than in other classes
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|decodeResult
operator|=
name|filter
operator|.
name|decode
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|parameters
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|decodedData
operator|=
name|out
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
comment|// repair parameters
if|if
condition|(
name|decodeResult
operator|!=
literal|null
condition|)
block|{
name|parameters
operator|.
name|addAll
argument_list|(
name|decodeResult
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getBitsPerComponent
parameter_list|()
block|{
if|if
condition|(
name|isStencil
argument_list|()
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|BPC
argument_list|,
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBitsPerComponent
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
block|{
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|BPC
argument_list|,
name|bitsPerComponent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|cs
init|=
name|parameters
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CS
argument_list|,
name|COSName
operator|.
name|COLORSPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
return|return
name|createColorSpace
argument_list|(
name|cs
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|isStencil
argument_list|()
condition|)
block|{
comment|// stencil mask color space must be gray, it is often missing
return|return
name|PDDeviceGray
operator|.
name|INSTANCE
return|;
block|}
else|else
block|{
comment|// an image without a color space is always broken
throw|throw
operator|new
name|IOException
argument_list|(
literal|"could not determine inline image color space"
argument_list|)
throw|;
block|}
block|}
comment|// deliver the long name of a device colorspace, or the parameter
specifier|private
name|COSBase
name|toLongName
parameter_list|(
name|COSBase
name|cs
parameter_list|)
block|{
if|if
condition|(
name|COSName
operator|.
name|RGB
operator|.
name|equals
argument_list|(
name|cs
argument_list|)
condition|)
block|{
return|return
name|COSName
operator|.
name|DEVICERGB
return|;
block|}
if|if
condition|(
name|COSName
operator|.
name|CMYK
operator|.
name|equals
argument_list|(
name|cs
argument_list|)
condition|)
block|{
return|return
name|COSName
operator|.
name|DEVICECMYK
return|;
block|}
if|if
condition|(
name|COSName
operator|.
name|G
operator|.
name|equals
argument_list|(
name|cs
argument_list|)
condition|)
block|{
return|return
name|COSName
operator|.
name|DEVICEGRAY
return|;
block|}
return|return
name|cs
return|;
block|}
specifier|private
name|PDColorSpace
name|createColorSpace
parameter_list|(
name|COSBase
name|cs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|cs
operator|instanceof
name|COSName
condition|)
block|{
return|return
name|PDColorSpace
operator|.
name|create
argument_list|(
name|toLongName
argument_list|(
name|cs
argument_list|)
argument_list|,
name|resources
argument_list|)
return|;
block|}
if|if
condition|(
name|cs
operator|instanceof
name|COSArray
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|cs
operator|)
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|COSArray
name|srcArray
init|=
operator|(
name|COSArray
operator|)
name|cs
decl_stmt|;
name|COSBase
name|csType
init|=
name|srcArray
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|I
operator|.
name|equals
argument_list|(
name|csType
argument_list|)
operator|||
name|COSName
operator|.
name|INDEXED
operator|.
name|equals
argument_list|(
name|csType
argument_list|)
condition|)
block|{
name|COSArray
name|dstArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|dstArray
operator|.
name|addAll
argument_list|(
name|srcArray
argument_list|)
expr_stmt|;
name|dstArray
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|COSName
operator|.
name|INDEXED
argument_list|)
expr_stmt|;
name|dstArray
operator|.
name|set
argument_list|(
literal|1
argument_list|,
name|toLongName
argument_list|(
name|srcArray
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|PDColorSpace
operator|.
name|create
argument_list|(
name|dstArray
argument_list|,
name|resources
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Illegal type of inline image color space: "
operator|+
name|csType
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Illegal type of object for inline image color space: "
operator|+
name|cs
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|COSBase
name|base
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorSpace
operator|!=
literal|null
condition|)
block|{
name|base
operator|=
name|colorSpace
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|parameters
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CS
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|H
argument_list|,
name|COSName
operator|.
name|HEIGHT
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|H
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|COSName
operator|.
name|WIDTH
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setWidth
parameter_list|(
name|int
name|width
parameter_list|)
block|{
name|parameters
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getInterpolate
parameter_list|()
block|{
return|return
name|parameters
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|I
argument_list|,
name|COSName
operator|.
name|INTERPOLATE
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setInterpolate
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|parameters
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|I
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a list of filters applied to this stream, or null if there are none.      *      * @return a list of filters applied to this stream      */
comment|// TODO return an empty list if there are none?
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFilters
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
literal|null
decl_stmt|;
name|COSBase
name|filters
init|=
name|parameters
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|COSName
operator|.
name|FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|filters
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
name|filters
decl_stmt|;
name|names
operator|=
operator|new
name|COSArrayList
argument_list|<>
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|name
argument_list|,
name|parameters
argument_list|,
name|COSName
operator|.
name|FILTER
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|names
operator|=
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|filters
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
comment|/**      * Sets which filters are applied to this stream.      *      * @param filters the filters to apply to this stream.      */
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|filters
parameter_list|)
block|{
name|COSBase
name|obj
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDecode
parameter_list|(
name|COSArray
name|decode
parameter_list|)
block|{
name|parameters
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|decode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|COSArray
name|getDecode
parameter_list|()
block|{
return|return
operator|(
name|COSArray
operator|)
name|parameters
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|COSName
operator|.
name|DECODE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isStencil
parameter_list|()
block|{
return|return
name|parameters
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|IM
argument_list|,
name|COSName
operator|.
name|IMAGE_MASK
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setStencil
parameter_list|(
name|boolean
name|isStencil
parameter_list|)
block|{
name|parameters
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|IM
argument_list|,
name|isStencil
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|createInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|decodedData
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|createInputStream
parameter_list|(
name|DecodeOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Decode options are irrelevant for inline image, as the data is always buffered.
return|return
name|createInputStream
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|createInputStream
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|stopFilters
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|filters
init|=
name|getFilters
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|rawData
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|rawData
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|filters
operator|!=
literal|null
operator|&&
name|i
operator|<
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|// TODO handling of abbreviated names belongs here, rather than in other classes
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|stopFilters
operator|.
name|contains
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
break|break;
block|}
else|else
block|{
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|filter
operator|.
name|decode
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|parameters
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|decodedData
operator|.
name|length
operator|==
literal|0
return|;
block|}
comment|/**      * Returns the inline image data.      */
specifier|public
name|byte
index|[]
name|getData
parameter_list|()
block|{
return|return
name|decodedData
return|;
block|}
annotation|@
name|Override
specifier|public
name|BufferedImage
name|getImage
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|SampledImageReader
operator|.
name|getRGBImage
argument_list|(
name|this
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|BufferedImage
name|getImage
parameter_list|(
name|Rectangle
name|region
parameter_list|,
name|int
name|subsampling
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|SampledImageReader
operator|.
name|getRGBImage
argument_list|(
name|this
argument_list|,
name|region
argument_list|,
name|subsampling
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|BufferedImage
name|getStencilImage
parameter_list|(
name|Paint
name|paint
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isStencil
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Image is not a stencil"
argument_list|)
throw|;
block|}
return|return
name|SampledImageReader
operator|.
name|getStencilImage
argument_list|(
name|this
argument_list|,
name|paint
argument_list|)
return|;
block|}
comment|/**      * Returns the suffix for this image type, e.g. jpg/png.      *      * @return The image suffix.      */
annotation|@
name|Override
specifier|public
name|String
name|getSuffix
parameter_list|()
block|{
comment|// TODO implement me
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

