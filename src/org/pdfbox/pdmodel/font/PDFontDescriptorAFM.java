begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
package|;
end_package

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
name|org
operator|.
name|fontbox
operator|.
name|afm
operator|.
name|FontMetric
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
name|PDRectangle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
import|;
end_import

begin_comment
comment|/**  * This class represents the font descriptor when the font information  * is coming from an AFM file.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFontDescriptorAFM
extends|extends
name|PDFontDescriptor
block|{
specifier|private
name|FontMetric
name|afm
decl_stmt|;
comment|/**      * Constructor.      *      * @param afmFile The AFM file.      */
specifier|public
name|PDFontDescriptorAFM
parameter_list|(
name|FontMetric
name|afmFile
parameter_list|)
block|{
name|afm
operator|=
name|afmFile
expr_stmt|;
block|}
comment|/**      * Get the font name.      *      * @return The name of the font.      */
specifier|public
name|String
name|getFontName
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getFontName
argument_list|()
return|;
block|}
comment|/**      * This will set the font name.      *      * @param fontName The new name for the font.      */
specifier|public
name|void
name|setFontName
parameter_list|(
name|String
name|fontName
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * A string representing the preferred font family.      *      * @return The font family.      */
specifier|public
name|String
name|getFontFamily
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getFamilyName
argument_list|()
return|;
block|}
comment|/**      * This will set the font family.      *      * @param fontFamily The font family.      */
specifier|public
name|void
name|setFontFamily
parameter_list|(
name|String
name|fontFamily
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * The weight of the font.  According to the PDF spec "possible values are      * 100, 200, 300, 400, 500, 600, 700, 800 or 900"  Where a higher number is      * more weight and appears to be more bold.      *      * @return The font weight.      */
specifier|public
name|float
name|getFontWeight
parameter_list|()
block|{
name|String
name|weight
init|=
name|afm
operator|.
name|getWeight
argument_list|()
decl_stmt|;
name|float
name|retval
init|=
literal|500
decl_stmt|;
if|if
condition|(
name|weight
operator|!=
literal|null
operator|&&
name|weight
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"bold"
argument_list|)
condition|)
block|{
name|retval
operator|=
literal|900
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|weight
operator|!=
literal|null
operator|&&
name|weight
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"light"
argument_list|)
condition|)
block|{
name|retval
operator|=
literal|100
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the weight of the font.      *      * @param fontWeight The new weight of the font.      */
specifier|public
name|void
name|setFontWeight
parameter_list|(
name|float
name|fontWeight
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * A string representing the preferred font stretch.      *      * @return The font stretch.      */
specifier|public
name|String
name|getFontStretch
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * This will set the font stretch.      *      * @param fontStretch The font stretch      */
specifier|public
name|void
name|setFontStretch
parameter_list|(
name|String
name|fontStretch
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the font flags.      *      * @return The font flags.      */
specifier|public
name|int
name|getFlags
parameter_list|()
block|{
comment|//I believe that the only flag that AFM supports is the is fixed pitch
return|return
name|afm
operator|.
name|isFixedPitch
argument_list|()
condition|?
literal|1
else|:
literal|0
return|;
block|}
comment|/**      * This will set the font flags.      *      * @param flags The new font flags.      */
specifier|public
name|void
name|setFlags
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the fonts bouding box.      *      * @return The fonts bouding box.      */
specifier|public
name|PDRectangle
name|getFontBoundingBox
parameter_list|()
block|{
name|BoundingBox
name|box
init|=
name|afm
operator|.
name|getFontBBox
argument_list|()
decl_stmt|;
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|box
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|box
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the fonts bounding box.      *      * @param rect The new bouding box.      */
specifier|public
name|void
name|setFontBoundingBox
parameter_list|(
name|PDRectangle
name|rect
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the italic angle for the font.      *      * @return The italic angle.      */
specifier|public
name|float
name|getItalicAngle
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getItalicAngle
argument_list|()
return|;
block|}
comment|/**      * This will set the italic angle for the font.      *      * @param angle The new italic angle for the font.      */
specifier|public
name|void
name|setItalicAngle
parameter_list|(
name|float
name|angle
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the ascent for the font.      *      * @return The ascent.      */
specifier|public
name|float
name|getAscent
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getAscender
argument_list|()
return|;
block|}
comment|/**      * This will set the ascent for the font.      *      * @param ascent The new ascent for the font.      */
specifier|public
name|void
name|setAscent
parameter_list|(
name|float
name|ascent
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the descent for the font.      *      * @return The descent.      */
specifier|public
name|float
name|getDescent
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getDescender
argument_list|()
return|;
block|}
comment|/**      * This will set the descent for the font.      *      * @param descent The new descent for the font.      */
specifier|public
name|void
name|setDescent
parameter_list|(
name|float
name|descent
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the leading for the font.      *      * @return The leading.      */
specifier|public
name|float
name|getLeading
parameter_list|()
block|{
comment|//AFM does not support setting the leading so we will just ignore it.
return|return
literal|0f
return|;
block|}
comment|/**      * This will set the leading for the font.      *      * @param leading The new leading for the font.      */
specifier|public
name|void
name|setLeading
parameter_list|(
name|float
name|leading
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the CapHeight for the font.      *      * @return The cap height.      */
specifier|public
name|float
name|getCapHeight
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getCapHeight
argument_list|()
return|;
block|}
comment|/**      * This will set the cap height for the font.      *      * @param capHeight The new cap height for the font.      */
specifier|public
name|void
name|setCapHeight
parameter_list|(
name|float
name|capHeight
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the x height for the font.      *      * @return The x height.      */
specifier|public
name|float
name|getXHeight
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getXHeight
argument_list|()
return|;
block|}
comment|/**      * This will set the x height for the font.      *      * @param xHeight The new x height for the font.      */
specifier|public
name|void
name|setXHeight
parameter_list|(
name|float
name|xHeight
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the stemV for the font.      *      * @return The stem v value.      */
specifier|public
name|float
name|getStemV
parameter_list|()
block|{
comment|//afm does not have a stem v
return|return
literal|0
return|;
block|}
comment|/**      * This will set the stem V for the font.      *      * @param stemV The new stem v for the font.      */
specifier|public
name|void
name|setStemV
parameter_list|(
name|float
name|stemV
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the stemH for the font.      *      * @return The stem h value.      */
specifier|public
name|float
name|getStemH
parameter_list|()
block|{
comment|//afm does not have a stem h
return|return
literal|0
return|;
block|}
comment|/**      * This will set the stem H for the font.      *      * @param stemH The new stem h for the font.      */
specifier|public
name|void
name|setStemH
parameter_list|(
name|float
name|stemH
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the average width for the font.      *      * @return The average width value.      *      * @throws IOException If there is an error calculating the average width.      */
specifier|public
name|float
name|getAverageWidth
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|afm
operator|.
name|getAverageCharacterWidth
argument_list|()
return|;
block|}
comment|/**      * This will set the average width for the font.      *      * @param averageWidth The new average width for the font.      */
specifier|public
name|void
name|setAverageWidth
parameter_list|(
name|float
name|averageWidth
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the max width for the font.      *      * @return The max width value.      */
specifier|public
name|float
name|getMaxWidth
parameter_list|()
block|{
comment|//afm does not support max width;
return|return
literal|0
return|;
block|}
comment|/**      * This will set the max width for the font.      *      * @param maxWidth The new max width for the font.      */
specifier|public
name|void
name|setMaxWidth
parameter_list|(
name|float
name|maxWidth
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the missing width for the font.      *      * @return The missing width value.      */
specifier|public
name|float
name|getMissingWidth
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
comment|/**      * This will set the missing width for the font.      *      * @param missingWidth The new missing width for the font.      */
specifier|public
name|void
name|setMissingWidth
parameter_list|(
name|float
name|missingWidth
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
comment|/**      * This will get the character set for the font.      *      * @return The character set value.      */
specifier|public
name|String
name|getCharSet
parameter_list|()
block|{
return|return
name|afm
operator|.
name|getCharacterSet
argument_list|()
return|;
block|}
comment|/**      * This will set the character set for the font.      *      * @param charSet The new character set for the font.      */
specifier|public
name|void
name|setCharacterSet
parameter_list|(
name|String
name|charSet
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The AFM Font descriptor is immutable"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

