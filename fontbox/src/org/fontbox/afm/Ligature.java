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
comment|/**  * This class represents a ligature, which is an entry of the CharMetrics.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|Ligature
block|{
specifier|private
name|String
name|successor
decl_stmt|;
specifier|private
name|String
name|ligature
decl_stmt|;
comment|/** Getter for property ligature.      * @return Value of property ligature.      */
specifier|public
name|String
name|getLigature
parameter_list|()
block|{
return|return
name|ligature
return|;
block|}
comment|/** Setter for property ligature.      * @param lig New value of property ligature.      */
specifier|public
name|void
name|setLigature
parameter_list|(
name|String
name|lig
parameter_list|)
block|{
name|ligature
operator|=
name|lig
expr_stmt|;
block|}
comment|/** Getter for property successor.      * @return Value of property successor.      */
specifier|public
name|String
name|getSuccessor
parameter_list|()
block|{
return|return
name|successor
return|;
block|}
comment|/** Setter for property successor.      * @param successorValue New value of property successor.      */
specifier|public
name|void
name|setSuccessor
parameter_list|(
name|String
name|successorValue
parameter_list|)
block|{
name|successor
operator|=
name|successorValue
expr_stmt|;
block|}
block|}
end_class

end_unit

