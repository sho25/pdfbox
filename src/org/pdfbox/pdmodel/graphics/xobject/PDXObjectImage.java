begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004-2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
name|io
operator|.
name|FileOutputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|org
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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDColorSpaceFactory
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
name|PDDeviceGray
import|;
end_import

begin_comment
comment|/**  * The prototype for all PDImages.   *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author mathiak  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDXObjectImage
extends|extends
name|PDXObject
block|{
comment|/**      * The XObject subtype.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Image"
decl_stmt|;
comment|/**      * This contains the suffix used when writing to file.      */
specifier|private
name|String
name|suffix
decl_stmt|;
comment|/**      * Standard constuctor.       *       * @param imageStream The XObject is passed as a COSStream.      * @param fileSuffix The file suffix, jpg/png.      */
specifier|public
name|PDXObjectImage
parameter_list|(
name|PDStream
name|imageStream
parameter_list|,
name|String
name|fileSuffix
parameter_list|)
block|{
name|super
argument_list|(
name|imageStream
argument_list|)
expr_stmt|;
name|suffix
operator|=
name|fileSuffix
expr_stmt|;
block|}
comment|/**      * Standard constuctor.       *       * @param doc The document to store the stream in.      * @param fileSuffix The file suffix, jpg/png.      */
specifier|public
name|PDXObjectImage
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|String
name|fileSuffix
parameter_list|)
block|{
name|super
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|getCOSStream
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
name|suffix
operator|=
name|fileSuffix
expr_stmt|;
block|}
comment|/**      * Returns an java.awt.Image, that can be used for display etc.        *       * @return This PDF object as an AWT image.      *       * @throws IOException If there is an error creating the image.      */
specifier|public
specifier|abstract
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Writes the Image to out.       * @param out the OutputStream that the Image is written to.       * @throws IOException when somethings wrong with out      */
specifier|public
specifier|abstract
name|void
name|write2OutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Writes the image to a file with the filename + an appropriate suffix, like "Image.jpg".       * The suffix is automatically set by the         * @param filename the filename      * @throws IOException When somethings wrong with the corresponding file.       */
specifier|public
name|void
name|write2file
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|filename
operator|+
literal|"."
operator|+
name|suffix
argument_list|)
expr_stmt|;
name|write2OutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Get the height of the image.      *       * @return The height of the image.      */
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
literal|"Height"
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the height of the image.      *       * @param height The height of the image.      */
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
literal|"Height"
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the width of the image.      *       * @return The width of the image.      */
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
literal|"Width"
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the width of the image.      *       * @param width The width of the image.      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|int
name|width
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
literal|"Width"
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
comment|/**      * The bits per component of this image.  This will return -1 if one has not      * been set.      *      * @return The number of bits per component.      */
specifier|public
name|int
name|getBitsPerComponent
parameter_list|()
block|{
return|return
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"BPC"
block|,
literal|"BitsPerComponent"
block|}
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the number of bits per component.      *      * @param bpc The number of bits per component.      */
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
literal|"BitsPerComponent"
argument_list|,
name|bpc
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the color space or null if none exists.      *      * @return The color space for this image.      *      * @throws IOException If there is an error getting the colorspace.      */
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
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"CS"
block|,
literal|"ColorSpace"
block|}
argument_list|)
decl_stmt|;
name|PDColorSpace
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|cs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//there are some cases where the 'required' CS value is not present
comment|//but we know that it will be grayscale for a CCITT filter.
name|COSBase
name|filter
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Filter"
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|CCITTFAX_DECODE
operator|.
name|equals
argument_list|(
name|filter
argument_list|)
operator|||
name|COSName
operator|.
name|CCITTFAX_DECODE_ABBREVIATION
operator|.
name|equals
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDDeviceGray
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the color space for this image.      *      * @param cs The color space for this image.      */
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|COSBase
name|base
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|base
operator|=
name|cs
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ColorSpace"
argument_list|)
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suffix for this image type, jpg/png.      *       * @return The image suffix.      */
specifier|public
name|String
name|getSuffix
parameter_list|()
block|{
return|return
name|suffix
return|;
block|}
block|}
end_class

end_unit

