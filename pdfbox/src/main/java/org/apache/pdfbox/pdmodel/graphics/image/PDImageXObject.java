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
name|Graphics2D
import|;
end_import

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
name|RenderingHints
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|COSInputStream
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
name|PDMetadata
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
name|pdmodel
operator|.
name|graphics
operator|.
name|PDXObject
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
comment|/**  * An Image XObject.  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDImageXObject
extends|extends
name|PDXObject
implements|implements
name|PDImage
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
name|PDImageXObject
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SoftReference
argument_list|<
name|BufferedImage
argument_list|>
name|cachedImage
decl_stmt|;
specifier|private
name|PDColorSpace
name|colorSpace
decl_stmt|;
specifier|private
specifier|final
name|PDResources
name|resources
decl_stmt|;
comment|// current resource dictionary (has color spaces)
comment|/**      * Creates a thumbnail Image XObject from the given COSBase and name.      * @param cosStream the COS stream      * @return an XObject      * @throws IOException if there is an error creating the XObject.      */
specifier|public
specifier|static
name|PDImageXObject
name|createThumbnail
parameter_list|(
name|COSStream
name|cosStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// thumbnails are special, any non-null subtype is treated as being "Image"
name|PDStream
name|pdStream
init|=
operator|new
name|PDStream
argument_list|(
name|cosStream
argument_list|)
decl_stmt|;
return|return
operator|new
name|PDImageXObject
argument_list|(
name|pdStream
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates an Image XObject in the given document.      * @param document the current document      * @throws java.io.IOException if there is an error creating the XObject.      */
specifier|public
name|PDImageXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|PDStream
argument_list|(
name|document
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates an Image XObject in the given document using the given filtered stream.      * @param document the current document      * @param encodedStream an encoded stream of image data      * @param cosFilter the filter or a COSArray of filters      * @param width the image width      * @param height the image height      * @param bitsPerComponent the bits per component      * @param initColorSpace the color space      * @throws IOException if there is an error creating the XObject.      */
specifier|public
name|PDImageXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|InputStream
name|encodedStream
parameter_list|,
name|COSBase
name|cosFilter
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|int
name|bitsPerComponent
parameter_list|,
name|PDColorSpace
name|initColorSpace
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|createRawStream
argument_list|(
name|document
argument_list|,
name|encodedStream
argument_list|)
argument_list|,
name|COSName
operator|.
name|IMAGE
argument_list|)
expr_stmt|;
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|cosFilter
argument_list|)
expr_stmt|;
name|resources
operator|=
literal|null
expr_stmt|;
name|colorSpace
operator|=
literal|null
expr_stmt|;
name|setBitsPerComponent
argument_list|(
name|bitsPerComponent
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|setColorSpace
argument_list|(
name|initColorSpace
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a COS stream from raw (encoded) data.      */
specifier|private
specifier|static
name|COSStream
name|createRawStream
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|InputStream
name|rawInput
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
name|OutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
name|output
operator|=
name|stream
operator|.
name|createRawOutputStream
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|rawInput
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|stream
return|;
block|}
comment|/**      * Creates an Image XObject with the given stream as its contents and current color spaces.      * @param stream the XObject stream to read      * @param resources the current resources      * @throws java.io.IOException if there is an error creating the XObject.      */
specifier|public
name|PDImageXObject
parameter_list|(
name|PDStream
name|stream
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|stream
argument_list|,
name|resources
argument_list|,
name|stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a PDImageXObject from an image file, see {@link #createFromFile(File, PDDocument)} for      * more details.      *      * @param imagePath the image file path.      * @param doc the document that shall use this PDImageXObject.      * @return a PDImageXObject.      * @throws IOException if there is an error when reading the file or creating the      * PDImageXObject, or if the image type is not supported.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromFile
parameter_list|(
name|String
name|imagePath
parameter_list|,
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromFile
argument_list|(
operator|new
name|File
argument_list|(
name|imagePath
argument_list|)
argument_list|,
name|doc
argument_list|)
return|;
block|}
comment|/**      * Create a PDImageXObject from an image file. The file format is determined by the file name      * suffix. The following suffixes are supported: jpg, jpeg, tif, tiff, gif, bmp and png. This is      * a convenience method that calls {@link JPEGFactory#createFromStream},      * {@link CCITTFactory#createFromFile} or {@link ImageIO#read} combined with      * {@link LosslessFactory#createFromImage}. (The later can also be used to create a      * PDImageXObject from a BufferedImage).      *      * @param file the image file.      * @param doc the document that shall use this PDImageXObject.      * @return a PDImageXObject.      * @throws IOException if there is an error when reading the file or creating the      * PDImageXObject.      * @throws IllegalArgumentException if the image type is not supported.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromFile
parameter_list|(
name|File
name|file
parameter_list|,
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|dot
init|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|dot
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Image type not supported: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|String
name|ext
init|=
name|name
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"jpg"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|"jpeg"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
condition|)
block|{
return|return
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|doc
argument_list|,
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
literal|"tif"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|"tiff"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
condition|)
block|{
return|return
name|CCITTFactory
operator|.
name|createFromFile
argument_list|(
name|doc
argument_list|,
name|file
argument_list|)
return|;
block|}
if|if
condition|(
literal|"gif"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|"bmp"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
operator|||
literal|"png"
operator|.
name|equals
argument_list|(
name|ext
argument_list|)
condition|)
block|{
name|BufferedImage
name|bim
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|file
argument_list|)
decl_stmt|;
return|return
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|doc
argument_list|,
name|bim
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Image type not supported: "
operator|+
name|name
argument_list|)
throw|;
block|}
comment|// repairs parameters using decode result
specifier|private
name|PDImageXObject
parameter_list|(
name|PDStream
name|stream
parameter_list|,
name|PDResources
name|resources
parameter_list|,
name|COSInputStream
name|input
parameter_list|)
block|{
name|super
argument_list|(
name|repair
argument_list|(
name|stream
argument_list|,
name|input
argument_list|)
argument_list|,
name|COSName
operator|.
name|IMAGE
argument_list|)
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|colorSpace
operator|=
name|input
operator|.
name|getDecodeResult
argument_list|()
operator|.
name|getJPXColorSpace
argument_list|()
expr_stmt|;
block|}
comment|// repairs parameters using decode result
specifier|private
specifier|static
name|PDStream
name|repair
parameter_list|(
name|PDStream
name|stream
parameter_list|,
name|COSInputStream
name|input
parameter_list|)
block|{
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|addAll
argument_list|(
name|input
operator|.
name|getDecodeResult
argument_list|()
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|stream
return|;
block|}
comment|/**      * Returns the metadata associated with this XObject, or null if there is none.      * @return the metadata associated with this object.      */
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosStream
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDMetadata
argument_list|(
name|cosStream
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the metadata associated with this XObject, or null if there is none.      * @param meta the metadata associated with this object      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|METADATA
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the key of this XObject in the structural parent tree.      * @return this object's key the structural parent tree      */
specifier|public
name|int
name|getStructParent
parameter_list|()
block|{
return|return
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENT
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Sets the key of this XObject in the structural parent tree.      * @param key the new key for this XObject      */
specifier|public
name|void
name|setStructParent
parameter_list|(
name|int
name|key
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENT
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      * The returned images are cached via a SoftReference.      */
annotation|@
name|Override
specifier|public
name|BufferedImage
name|getImage
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|cachedImage
operator|!=
literal|null
condition|)
block|{
name|BufferedImage
name|cached
init|=
name|cachedImage
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|cached
operator|!=
literal|null
condition|)
block|{
return|return
name|cached
return|;
block|}
block|}
comment|// get image as RGB
name|BufferedImage
name|image
init|=
name|SampledImageReader
operator|.
name|getRGBImage
argument_list|(
name|this
argument_list|,
name|getColorKeyMask
argument_list|()
argument_list|)
decl_stmt|;
comment|// soft mask (overrides explicit mask)
name|PDImageXObject
name|softMask
init|=
name|getSoftMask
argument_list|()
decl_stmt|;
if|if
condition|(
name|softMask
operator|!=
literal|null
condition|)
block|{
name|image
operator|=
name|applyMask
argument_list|(
name|image
argument_list|,
name|softMask
operator|.
name|getOpaqueImage
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// explicit mask - to be applied only if /ImageMask true
name|PDImageXObject
name|mask
init|=
name|getMask
argument_list|()
decl_stmt|;
if|if
condition|(
name|mask
operator|!=
literal|null
operator|&&
name|mask
operator|.
name|isStencil
argument_list|()
condition|)
block|{
name|image
operator|=
name|applyMask
argument_list|(
name|image
argument_list|,
name|mask
operator|.
name|getOpaqueImage
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
name|cachedImage
operator|=
operator|new
name|SoftReference
argument_list|<
name|BufferedImage
argument_list|>
argument_list|(
name|image
argument_list|)
expr_stmt|;
return|return
name|image
return|;
block|}
comment|/**      * {@inheritDoc}      * The returned images are not cached.      */
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
comment|/**      * Returns an RGB buffered image containing the opaque image stream without any masks applied.      * If this Image XObject is a mask then the buffered image will contain the raw mask.      * @return the image without any masks applied      * @throws IOException if the image cannot be read      */
specifier|public
name|BufferedImage
name|getOpaqueImage
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
comment|// explicit mask: RGB + Binary -> ARGB
comment|// soft mask: RGB + Gray -> ARGB
specifier|private
name|BufferedImage
name|applyMask
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|BufferedImage
name|mask
parameter_list|,
name|boolean
name|isSoft
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|mask
operator|==
literal|null
condition|)
block|{
return|return
name|image
return|;
block|}
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
comment|// scale mask to fit image, or image to fit mask, whichever is larger
if|if
condition|(
name|mask
operator|.
name|getWidth
argument_list|()
operator|<
name|width
operator|||
name|mask
operator|.
name|getHeight
argument_list|()
operator|<
name|height
condition|)
block|{
name|mask
operator|=
name|scaleImage
argument_list|(
name|mask
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mask
operator|.
name|getWidth
argument_list|()
operator|>
name|width
operator|||
name|mask
operator|.
name|getHeight
argument_list|()
operator|>
name|height
condition|)
block|{
name|width
operator|=
name|mask
operator|.
name|getWidth
argument_list|()
expr_stmt|;
name|height
operator|=
name|mask
operator|.
name|getHeight
argument_list|()
expr_stmt|;
name|image
operator|=
name|scaleImage
argument_list|(
name|image
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|// compose to ARGB
name|BufferedImage
name|masked
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
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|WritableRaster
name|src
init|=
name|image
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|WritableRaster
name|dest
init|=
name|masked
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|WritableRaster
name|alpha
init|=
name|mask
operator|.
name|getRaster
argument_list|()
decl_stmt|;
name|float
index|[]
name|rgb
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
name|float
index|[]
name|rgba
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
name|float
index|[]
name|alphaPixel
init|=
literal|null
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
name|src
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgb
argument_list|)
expr_stmt|;
name|rgba
index|[
literal|0
index|]
operator|=
name|rgb
index|[
literal|0
index|]
expr_stmt|;
name|rgba
index|[
literal|1
index|]
operator|=
name|rgb
index|[
literal|1
index|]
expr_stmt|;
name|rgba
index|[
literal|2
index|]
operator|=
name|rgb
index|[
literal|2
index|]
expr_stmt|;
name|alphaPixel
operator|=
name|alpha
operator|.
name|getPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|alphaPixel
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSoft
condition|)
block|{
name|rgba
index|[
literal|3
index|]
operator|=
name|alphaPixel
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|rgba
index|[
literal|3
index|]
operator|=
literal|255
operator|-
name|alphaPixel
index|[
literal|0
index|]
expr_stmt|;
block|}
name|dest
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|rgba
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|masked
return|;
block|}
comment|/**      * High-quality image scaling.      */
specifier|private
name|BufferedImage
name|scaleImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|)
block|{
name|BufferedImage
name|image2
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
name|Graphics2D
name|g
init|=
name|image2
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|g
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_INTERPOLATION
argument_list|,
name|RenderingHints
operator|.
name|VALUE_INTERPOLATION_BICUBIC
argument_list|)
expr_stmt|;
name|g
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_RENDERING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_RENDER_QUALITY
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
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
literal|null
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|image2
return|;
block|}
comment|/**      * Returns the Mask Image XObject associated with this image, or null if there is none.      * @return Mask Image XObject      */
specifier|public
name|PDImageXObject
name|getMask
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|mask
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|mask
operator|instanceof
name|COSArray
condition|)
block|{
comment|// color key mask, no explicit mask to return
return|return
literal|null
return|;
block|}
else|else
block|{
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosStream
operator|!=
literal|null
condition|)
block|{
comment|// always DeviceGray
return|return
operator|new
name|PDImageXObject
argument_list|(
operator|new
name|PDStream
argument_list|(
name|cosStream
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the color key mask array associated with this image, or null if there is none.      * @return Mask Image XObject      */
specifier|public
name|COSArray
name|getColorKeyMask
parameter_list|()
block|{
name|COSBase
name|mask
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|mask
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
name|COSArray
operator|)
name|mask
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the Soft Mask Image XObject associated with this image, or null if there is none.      * @return the SMask Image XObject, or null.      */
specifier|public
name|PDImageXObject
name|getSoftMask
parameter_list|()
throws|throws
name|IOException
block|{
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosStream
operator|!=
literal|null
condition|)
block|{
comment|// always DeviceGray
return|return
operator|new
name|PDImageXObject
argument_list|(
operator|new
name|PDStream
argument_list|(
name|cosStream
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
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
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
name|COSName
operator|.
name|BPC
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
name|bpc
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
name|bpc
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
if|if
condition|(
name|colorSpace
operator|==
literal|null
condition|)
block|{
name|COSBase
name|cosBase
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|,
name|COSName
operator|.
name|CS
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosBase
operator|!=
literal|null
condition|)
block|{
name|colorSpace
operator|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|cosBase
argument_list|,
name|resources
argument_list|)
expr_stmt|;
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
literal|"could not determine color space"
argument_list|)
throw|;
block|}
block|}
return|return
name|colorSpace
return|;
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
name|getStream
argument_list|()
operator|.
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
return|return
name|getStream
argument_list|()
operator|.
name|createInputStream
argument_list|(
name|stopFilters
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
name|getStream
argument_list|()
operator|.
name|getStream
argument_list|()
operator|.
name|getLength
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|,
name|cs
operator|!=
literal|null
condition|?
name|cs
operator|.
name|getCOSObject
argument_list|()
else|:
literal|null
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
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|HEIGHT
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
name|h
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|HEIGHT
argument_list|,
name|h
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
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|WIDTH
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
name|w
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|WIDTH
argument_list|,
name|w
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
name|getCOSStream
argument_list|()
operator|.
name|getBoolean
argument_list|(
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
name|getCOSStream
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|INTERPOLATE
argument_list|,
name|value
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
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE
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
name|COSBase
name|decode
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DECODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|decode
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
name|COSArray
operator|)
name|decode
return|;
block|}
return|return
literal|null
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
name|getCOSStream
argument_list|()
operator|.
name|getBoolean
argument_list|(
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
name|getCOSStream
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|IMAGE_MASK
argument_list|,
name|isStencil
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suffix for this image type, e.g. jpg/png.      * @return The image suffix or null if not available.      */
annotation|@
name|Override
specifier|public
name|String
name|getSuffix
parameter_list|()
block|{
name|List
argument_list|<
name|COSName
argument_list|>
name|filters
init|=
name|getStream
argument_list|()
operator|.
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|==
literal|null
condition|)
block|{
return|return
literal|"png"
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
argument_list|)
condition|)
block|{
return|return
literal|"jpg"
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|JPX_DECODE
argument_list|)
condition|)
block|{
return|return
literal|"jpx"
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|)
condition|)
block|{
return|return
literal|"tiff"
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
operator|||
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|LZW_DECODE
argument_list|)
operator|||
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|RUN_LENGTH_DECODE
argument_list|)
condition|)
block|{
return|return
literal|"png"
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"getSuffix() returns null, filters: "
operator|+
name|filters
argument_list|)
expr_stmt|;
comment|// TODO more...
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

