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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
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

begin_comment
comment|/**  * An image in a PDF document.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDImage
extends|extends
name|COSObjectable
block|{
comment|/**      * Returns the content of this image as an AWT buffered image with an (A)RGB color space.      * The size of the returned image is the larger of the size of the image itself or its mask.       * @return content of this image as a buffered image.      * @throws IOException      */
name|BufferedImage
name|getImage
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the content of this image as an AWT buffered image with an (A)RGB colored space.      * Only the subregion specified is rendered, and is subsampled by advancing the specified amount      * of rows and columns in the source image for every resulting pixel.      *      * Note that unlike {@link PDImage#getImage() the unparameterized version}, this method does      * not cache the resulting image.      * @param region The region of the source image to get, or null if the entire image is needed.      *               The actual region will be clipped to the dimensions of the source image.      * @param subsampling The amount of rows and columns to advance for every output pixel, a value      *                  of 1 meaning every pixel will be read      * @return subsampled content of the requested subregion as a buffered image.      * @throws IOException      */
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
function_decl|;
comment|/**      * Returns an ARGB image filled with the given paint and using this image as a mask.      * @param paint the paint to fill the visible portions of the image with      * @return a masked image filled with the given paint      * @throws IOException if the image cannot be read      * @throws IllegalStateException if the image is not a stencil.      */
name|BufferedImage
name|getStencilImage
parameter_list|(
name|Paint
name|paint
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns an InputStream containing the image data, irrespective of whether this is an      * inline image or an image XObject.      * @return Decoded stream      * @throws IOException if the data could not be read.      */
name|InputStream
name|createInputStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns an InputStream containing the image data, irrespective of whether this is an      * inline image or an image XObject. The given filters will not be decoded.      * @param stopFilters A list of filters to stop decoding at.      * @return Decoded stream      * @throws IOException if the data could not be read.      */
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
function_decl|;
comment|/**      * Returns an InputStream, passing additional options to each filter. As a side effect, the      * filterSubsampled flag is set in {@link DecodeOptions}.      *      * @param options Additional decoding options passed to the filters used      * @return Decoded stream      * @throws IOException if the data could not be read      */
name|InputStream
name|createInputStream
parameter_list|(
name|DecodeOptions
name|options
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if the image has no data.      */
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**      * Returns true if the image is a stencil mask.      */
name|boolean
name|isStencil
parameter_list|()
function_decl|;
comment|/**      * Sets whether or not the image is a stencil.      * This corresponds to the {@code ImageMask} entry in the image stream's dictionary.      * @param isStencil True to make the image a stencil.      */
name|void
name|setStencil
parameter_list|(
name|boolean
name|isStencil
parameter_list|)
function_decl|;
comment|/**      * Returns bits per component of this image, or -1 if one has not been set.      */
name|int
name|getBitsPerComponent
parameter_list|()
function_decl|;
comment|/**      * Set the number of bits per component.      * @param bitsPerComponent The number of bits per component.      */
name|void
name|setBitsPerComponent
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
function_decl|;
comment|/**      * Returns the image's color space.      * @throws IOException If there is an error getting the color space.      */
name|PDColorSpace
name|getColorSpace
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Sets the color space for this image.      * @param colorSpace The color space for this image.      */
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
function_decl|;
comment|/**      * Returns height of this image, or -1 if one has not been set.      */
name|int
name|getHeight
parameter_list|()
function_decl|;
comment|/**      * Sets the height of the image.      * @param height The height of the image.      */
name|void
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
function_decl|;
comment|/**      * Returns the width of this image, or -1 if one has not been set.      */
name|int
name|getWidth
parameter_list|()
function_decl|;
comment|/**      * Sets the width of the image.      * @param width The width of the image.      */
name|void
name|setWidth
parameter_list|(
name|int
name|width
parameter_list|)
function_decl|;
comment|/**      * Sets the decode array.      * @param decode  the new decode array.      */
name|void
name|setDecode
parameter_list|(
name|COSArray
name|decode
parameter_list|)
function_decl|;
comment|/**      * Returns the decode array.      */
name|COSArray
name|getDecode
parameter_list|()
function_decl|;
comment|/**      * Returns true if the image should be interpolated when rendered.      */
name|boolean
name|getInterpolate
parameter_list|()
function_decl|;
comment|/**      * Sets the Interpolate flag, true for high-quality image scaling.      */
name|void
name|setInterpolate
parameter_list|(
name|boolean
name|value
parameter_list|)
function_decl|;
comment|/**      * Returns the suffix for this image type, e.g. "jpg"      */
name|String
name|getSuffix
parameter_list|()
function_decl|;
comment|/**      * Convert this image to a COS object.      *      * @return The cos object that matches this image object.      */
annotation|@
name|Override
name|COSDictionary
name|getCOSObject
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

