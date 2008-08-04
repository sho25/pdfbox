begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|DataBufferByte
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
name|DataBufferInt
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
name|IndexColorModel
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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|filter
operator|.
name|FilterManager
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|util
operator|.
name|ImageParameters
import|;
end_import

begin_comment
comment|/**  * This class represents an inlined image.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PDInlinedImage
block|{
specifier|private
name|ImageParameters
name|params
decl_stmt|;
specifier|private
name|byte
index|[]
name|imageData
decl_stmt|;
comment|/**      * This will get the image parameters.      *      * @return The image parameters.      */
specifier|public
name|ImageParameters
name|getImageParameters
parameter_list|()
block|{
return|return
name|params
return|;
block|}
comment|/**      * This will set the image parameters for this image.      *      * @param imageParams The imageParams.      */
specifier|public
name|void
name|setImageParameters
parameter_list|(
name|ImageParameters
name|imageParams
parameter_list|)
block|{
name|params
operator|=
name|imageParams
expr_stmt|;
block|}
comment|/**      * Get the bytes for the image.      *      * @return The image data.      */
specifier|public
name|byte
index|[]
name|getImageData
parameter_list|()
block|{
return|return
name|imageData
return|;
block|}
comment|/**      * Set the bytes that make up the image.      *      * @param value The image data.      */
specifier|public
name|void
name|setImageData
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
name|imageData
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will take the inlined image information and create a java.awt.Image from      * it.      *      * @return The image that this object represents.      *       * @throws IOException If there is an error creating the image.      */
specifier|public
name|BufferedImage
name|createImage
parameter_list|()
throws|throws
name|IOException
block|{
comment|/*          * This was the previous implementation, not sure which is better right now.          *         byte[] transparentColors = new byte[]{(byte)0xFF,(byte)0xFF};         byte[] colors=new byte[]{0, (byte)0xFF};         IndexColorModel colorModel = new IndexColorModel( 1, 2, colors, colors, colors, transparentColors );         BufferedImage image = new BufferedImage(             params.getWidth(),             params.getHeight(),             BufferedImage.TYPE_BYTE_BINARY,             colorModel );         DataBufferByte buffer = new DataBufferByte( getImageData(), 1 );         WritableRaster raster =             Raster.createPackedRaster(                 buffer,                 params.getWidth(),                 params.getHeight(),                 params.getBitsPerComponent(),                 new Point(0,0) );         image.setData( raster );         return image;          */
comment|//verify again pci32.pdf before changing below
name|PDColorSpace
name|pcs
init|=
name|params
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|ColorModel
name|colorModel
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|pcs
operator|!=
literal|null
condition|)
block|{
name|colorModel
operator|=
name|params
operator|.
name|getColorSpace
argument_list|()
operator|.
name|createColorModel
argument_list|(
name|params
operator|.
name|getBitsPerComponent
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|transparentColors
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|}
decl_stmt|;
name|byte
index|[]
name|colors
init|=
operator|new
name|byte
index|[]
block|{
literal|0
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|}
decl_stmt|;
name|colorModel
operator|=
operator|new
name|IndexColorModel
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|colors
argument_list|,
name|colors
argument_list|,
name|colors
argument_list|,
name|transparentColors
argument_list|)
expr_stmt|;
block|}
name|List
name|filters
init|=
name|params
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|byte
index|[]
name|finalData
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|filters
operator|==
literal|null
condition|)
block|{
name|finalData
operator|=
name|getImageData
argument_list|()
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
name|getImageData
argument_list|()
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|getImageData
argument_list|()
operator|.
name|length
argument_list|)
decl_stmt|;
name|FilterManager
name|filterManager
init|=
operator|new
name|FilterManager
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
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Filter
name|filter
init|=
name|filterManager
operator|.
name|getFilter
argument_list|(
operator|(
name|String
operator|)
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
name|params
operator|.
name|getDictionary
argument_list|()
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
name|finalData
operator|=
name|out
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
name|WritableRaster
name|raster
init|=
name|colorModel
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|params
operator|.
name|getWidth
argument_list|()
argument_list|,
name|params
operator|.
name|getHeight
argument_list|()
argument_list|)
decl_stmt|;
comment|/*    Raster.createPackedRaster(                 buffer,                 params.getWidth(),                 params.getHeight(),                 params.getBitsPerComponent(),                 new Point(0,0) );                 */
name|DataBuffer
name|rasterBuffer
init|=
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|rasterBuffer
operator|instanceof
name|DataBufferByte
condition|)
block|{
name|DataBufferByte
name|byteBuffer
init|=
operator|(
name|DataBufferByte
operator|)
name|rasterBuffer
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|byteBuffer
operator|.
name|getData
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|finalData
argument_list|,
literal|0
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rasterBuffer
operator|instanceof
name|DataBufferInt
condition|)
block|{
name|DataBufferInt
name|byteBuffer
init|=
operator|(
name|DataBufferInt
operator|)
name|rasterBuffer
decl_stmt|;
name|int
index|[]
name|data
init|=
name|byteBuffer
operator|.
name|getData
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
name|finalData
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|data
index|[
name|i
index|]
operator|=
operator|(
name|finalData
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
expr_stmt|;
block|}
block|}
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
name|image
operator|.
name|setData
argument_list|(
name|raster
argument_list|)
expr_stmt|;
return|return
name|image
return|;
block|}
block|}
end_class

end_unit

