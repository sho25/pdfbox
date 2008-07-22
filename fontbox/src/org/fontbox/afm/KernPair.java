begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.fontbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of fontbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.fontbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|afm
package|;
end_package

begin_comment
comment|/**  * This represents some kern pair data.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|KernPair
block|{
specifier|private
name|String
name|firstKernCharacter
decl_stmt|;
specifier|private
name|String
name|secondKernCharacter
decl_stmt|;
specifier|private
name|float
name|x
decl_stmt|;
specifier|private
name|float
name|y
decl_stmt|;
comment|/** Getter for property firstKernCharacter.      * @return Value of property firstKernCharacter.      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getFirstKernCharacter
parameter_list|()
block|{
return|return
name|firstKernCharacter
return|;
block|}
comment|/** Setter for property firstKernCharacter.      * @param firstKernCharacterValue New value of property firstKernCharacter.      */
specifier|public
name|void
name|setFirstKernCharacter
parameter_list|(
name|String
name|firstKernCharacterValue
parameter_list|)
block|{
name|firstKernCharacter
operator|=
name|firstKernCharacterValue
expr_stmt|;
block|}
comment|/** Getter for property secondKernCharacter.      * @return Value of property secondKernCharacter.      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getSecondKernCharacter
parameter_list|()
block|{
return|return
name|secondKernCharacter
return|;
block|}
comment|/** Setter for property secondKernCharacter.      * @param secondKernCharacterValue New value of property secondKernCharacter.      */
specifier|public
name|void
name|setSecondKernCharacter
parameter_list|(
name|String
name|secondKernCharacterValue
parameter_list|)
block|{
name|secondKernCharacter
operator|=
name|secondKernCharacterValue
expr_stmt|;
block|}
comment|/** Getter for property x.      * @return Value of property x.      */
specifier|public
name|float
name|getX
parameter_list|()
block|{
return|return
name|x
return|;
block|}
comment|/** Setter for property x.      * @param xValue New value of property x.      */
specifier|public
name|void
name|setX
parameter_list|(
name|float
name|xValue
parameter_list|)
block|{
name|x
operator|=
name|xValue
expr_stmt|;
block|}
comment|/** Getter for property y.      * @return Value of property y.      */
specifier|public
name|float
name|getY
parameter_list|()
block|{
return|return
name|y
return|;
block|}
comment|/** Setter for property y.      * @param yValue New value of property y.      */
specifier|public
name|void
name|setY
parameter_list|(
name|float
name|yValue
parameter_list|)
block|{
name|y
operator|=
name|yValue
expr_stmt|;
block|}
block|}
end_class

end_unit

