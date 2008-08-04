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
name|graphics
operator|.
name|color
package|;
end_package

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
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
name|image
operator|.
name|ColorModel
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

begin_comment
comment|/**  * This class represents a Pattern color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDPattern
extends|extends
name|PDColorSpace
block|{
specifier|private
name|COSArray
name|array
decl_stmt|;
comment|/**      * The name of this color space.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"Pattern"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDPattern
parameter_list|()
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
name|getPDFName
argument_list|(
name|NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param pattern The pattern array.      */
specifier|public
name|PDPattern
parameter_list|(
name|COSArray
name|pattern
parameter_list|)
block|{
name|array
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**      * This will return the name of the color space.      *      * @return The name of the color space.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|NAME
return|;
block|}
comment|/**      * This will get the number of components that this color space is made up of.      *      * @return The number of components in this color space.      *      * @throws IOException If there is an error getting the number of color components.      */
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|/**      * Create a Java colorspace for this colorspace.      *      * @return A color space that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color space.      */
specifier|public
name|ColorSpace
name|createColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented"
argument_list|)
throw|;
block|}
comment|/**      * Create a Java color model for this colorspace.      *      * @param bpc The number of bits per component.      *       * @return A color model that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color model.      */
specifier|public
name|ColorModel
name|createColorModel
parameter_list|(
name|int
name|bpc
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

