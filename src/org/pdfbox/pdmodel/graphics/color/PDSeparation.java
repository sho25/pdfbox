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
name|common
operator|.
name|function
operator|.
name|PDFunction
import|;
end_import

begin_comment
comment|/**  * This class represents a Separation color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDSeparation
extends|extends
name|PDColorSpace
block|{
comment|/**      * The name of this color space.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"Separation"
decl_stmt|;
specifier|private
name|COSArray
name|array
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDSeparation
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
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param separation The array containing all separation information.      */
specifier|public
name|PDSeparation
parameter_list|(
name|COSArray
name|separation
parameter_list|)
block|{
name|array
operator|=
name|separation
expr_stmt|;
block|}
comment|/**      * This will return the name of the color space.  For a PDSeparation object      * this will always return "Separation"      *      * @return The name of the color space.      */
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
comment|/**      * This will get the separation name.      *      * @return The name in the separation.      */
specifier|public
name|String
name|getColorantName
parameter_list|()
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
name|name
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will set the separation name.      *      * @param name The separation name.      */
specifier|public
name|void
name|setColorantName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|1
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the alternate color space for this separation.      *      * @return The alternate color space.      *      * @throws IOException If there is an error getting the alternate color space.      */
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
name|array
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
return|return
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|alternate
argument_list|)
return|;
block|}
comment|/**      * This will set the alternate color space.      *      * @param cs The alternate color space.      */
specifier|public
name|void
name|setAlternateColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|COSBase
name|space
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
name|space
operator|=
name|cs
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
literal|2
argument_list|,
name|space
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the tint transform function.      *      * @return The tint transform function.      *       * @throws IOException If there is an error creating the PDFunction      */
specifier|public
name|PDFunction
name|getTintTransform
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFunction
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
literal|3
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the tint transform function.      *      * @param tint The tint transform function.      */
specifier|public
name|void
name|setTintTransform
parameter_list|(
name|PDFunction
name|tint
parameter_list|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|3
argument_list|,
name|tint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

