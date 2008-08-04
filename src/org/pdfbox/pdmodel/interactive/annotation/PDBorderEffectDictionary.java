begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
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
name|COSDictionary
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
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents a PDF /BE entry the border effect dictionary.  *   * @author Paul King  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|PDBorderEffectDictionary
implements|implements
name|COSObjectable
block|{
comment|/*      * The various values of the effect applied to the border as defined in the      * PDF 1.6 reference Table 8.14      */
comment|/**      * Constant for the name for no effect.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_SOLID
init|=
literal|"S"
decl_stmt|;
comment|/**      * Constant for the name of a cloudy effect.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_CLOUDY
init|=
literal|"C"
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDBorderEffectDictionary
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dict      *            a border style dictionary.      */
specifier|public
name|PDBorderEffectDictionary
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      *       * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * returns the dictionary.      *       * @return the dictionary      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will set the intensity of the applied effect.      *       * @param i      *            the intensity of the effect values 0 to 2      */
specifier|public
name|void
name|setIntensity
parameter_list|(
name|float
name|i
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFloat
argument_list|(
literal|"I"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the intensity of the applied effect.      *       * @return the intensity value 0 to 2      */
specifier|public
name|float
name|getIntensity
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"I"
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the border effect, see the STYLE_* constants for valid values.      *       * @param s      *            the border effect to use      */
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
literal|"S"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border effect, see the STYLE_* constants for valid      * values.      *       * @return the effect of the border      */
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"S"
argument_list|,
name|STYLE_SOLID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

