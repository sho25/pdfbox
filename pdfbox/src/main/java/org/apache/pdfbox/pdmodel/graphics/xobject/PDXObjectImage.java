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
name|java
operator|.
name|io
operator|.
name|File
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
name|PDColorSpaceFactory
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
name|PDGraphicsState
import|;
end_import

begin_comment
comment|/**  * The prototype for all PDImages.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author mathiak  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDXObjectImage
extends|extends
name|PDXObject
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDXObjectImage
operator|.
name|class
argument_list|)
decl_stmt|;
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
specifier|private
name|PDGraphicsState
name|graphicsState
decl_stmt|;
comment|/**      * Standard constuctor.      *      * @param imageStream The XObject is passed as a COSStream.      * @param fileSuffix The file suffix, jpg/png.      */
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
comment|/**      * Standard constuctor.      *      * @param doc The document to store the stream in.      * @param fileSuffix The file suffix, jpg/png.      */
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
comment|/**      * Returns an java.awt.Image, that can be used for display etc.      *      * @return This PDF object as an AWT image.      *      * @throws IOException If there is an error creating the image.      */
specifier|public
specifier|abstract
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns a PDXObjectImage of the SMask image, if there is one.      * See section 11.5 of the pdf specification for details on Soft Masks.      *      * @return the PDXObjectImage of the SMask if there is one, else<code>null</code>.      */
specifier|public
name|PDXObjectImage
name|getSMaskImage
parameter_list|()
throws|throws
name|IOException
block|{
name|COSStream
name|cosStream
init|=
name|getPDStream
argument_list|()
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|COSBase
name|smask
init|=
name|cosStream
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
name|smask
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
operator|(
name|PDXObjectImage
operator|)
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|smask
argument_list|)
return|;
block|}
block|}
comment|/**      * Writes the Image to out.      * @param out the OutputStream that the Image is written to.      * @throws IOException when somethings wrong with out      */
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
comment|/**      * Writes the image to a file with the filename + an appropriate suffix, like "Image.jpg".      * The suffix is automatically set by the      * @param filename the filename      * @throws IOException When somethings wrong with the corresponding file.      */
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
comment|/**      * Writes the image to a file with the filename + an appropriate suffix, like "Image.jpg".      * The suffix is automatically set by the      * @param file the file      * @throws IOException When somethings wrong with the corresponding file.      */
specifier|public
name|void
name|write2file
parameter_list|(
name|File
name|file
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
name|file
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
comment|/**      * Get the height of the image.      *      * @return The height of the image.      */
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
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the height of the image.      *      * @param height The height of the image.      */
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
name|COSName
operator|.
name|HEIGHT
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the width of the image.      *      * @return The width of the image.      */
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
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the width of the image.      *      * @param width The width of the image.      */
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
name|COSName
operator|.
name|WIDTH
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
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to return NULL from createColorSpace branch"
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to return NULL from CCITT branch"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|JBIG2_DECODE
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
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to return NULL from JBIG2 branch"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|getImageMask
argument_list|()
condition|)
block|{
comment|//Stencil Mask branch.  Section 4.8.5 of the reference, page 350 in version 1.7.
name|retval
operator|=
name|graphicsState
operator|.
name|getNonStrokingColor
argument_list|()
operator|.
name|getColorSpace
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Stencil Mask branch returning "
operator|+
name|retval
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//throw new IOException("Trace the Stencil Mask!!!!");
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to return NULL from unhandled branch."
operator|+
literal|" filter = "
operator|+
name|filter
argument_list|)
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
name|COLORSPACE
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suffix for this image type, jpg/png.      *      * @return The image suffix.      */
specifier|public
name|String
name|getSuffix
parameter_list|()
block|{
return|return
name|suffix
return|;
block|}
comment|/**      * Get the ImageMask flag. Used in Stencil Masking.  Section 4.8.5 of the spec.      *      * @return The ImageMask flag.  This is optional and defaults to False, so if it does not exist, we return False      */
specifier|public
name|boolean
name|getImageMask
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
comment|/**      * Allow the Invoke operator to set the graphics state so that,       * in the case of an Image Mask, we can get to the current nonstroking colorspace.      * @param newGS The new graphicstate      */
specifier|public
name|void
name|setGraphicsState
parameter_list|(
name|PDGraphicsState
name|newGS
parameter_list|)
block|{
name|graphicsState
operator|=
name|newGS
expr_stmt|;
block|}
comment|/**      * Returns the Decode Array of an XObjectImage.      * @return the decode array      */
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
operator|!=
literal|null
operator|&&
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
comment|/**       * Returns the optional mask of a XObjectImage if there is one.      *       * @return The mask as COSArray otherwise null.      */
specifier|public
name|COSArray
name|getMask
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
operator|!=
literal|null
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
block|}
end_class

end_unit

