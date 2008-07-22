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
comment|/**  * This class represents a piece of track kerning data.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|TrackKern
block|{
specifier|private
name|int
name|degree
decl_stmt|;
specifier|private
name|float
name|minPointSize
decl_stmt|;
specifier|private
name|float
name|minKern
decl_stmt|;
specifier|private
name|float
name|maxPointSize
decl_stmt|;
specifier|private
name|float
name|maxKern
decl_stmt|;
comment|/** Getter for property degree.      * @return Value of property degree.      */
specifier|public
name|int
name|getDegree
parameter_list|()
block|{
return|return
name|degree
return|;
block|}
comment|/** Setter for property degree.      * @param degreeValue New value of property degree.      */
specifier|public
name|void
name|setDegree
parameter_list|(
name|int
name|degreeValue
parameter_list|)
block|{
name|degree
operator|=
name|degreeValue
expr_stmt|;
block|}
comment|/** Getter for property maxKern.      * @return Value of property maxKern.      */
specifier|public
name|float
name|getMaxKern
parameter_list|()
block|{
return|return
name|maxKern
return|;
block|}
comment|/** Setter for property maxKern.      * @param maxKernValue New value of property maxKern.      */
specifier|public
name|void
name|setMaxKern
parameter_list|(
name|float
name|maxKernValue
parameter_list|)
block|{
name|maxKern
operator|=
name|maxKernValue
expr_stmt|;
block|}
comment|/** Getter for property maxPointSize.      * @return Value of property maxPointSize.      */
specifier|public
name|float
name|getMaxPointSize
parameter_list|()
block|{
return|return
name|maxPointSize
return|;
block|}
comment|/** Setter for property maxPointSize.      * @param maxPointSizeValue New value of property maxPointSize.      */
specifier|public
name|void
name|setMaxPointSize
parameter_list|(
name|float
name|maxPointSizeValue
parameter_list|)
block|{
name|maxPointSize
operator|=
name|maxPointSizeValue
expr_stmt|;
block|}
comment|/** Getter for property minKern.      * @return Value of property minKern.      */
specifier|public
name|float
name|getMinKern
parameter_list|()
block|{
return|return
name|minKern
return|;
block|}
comment|/** Setter for property minKern.      * @param minKernValue New value of property minKern.      */
specifier|public
name|void
name|setMinKern
parameter_list|(
name|float
name|minKernValue
parameter_list|)
block|{
name|minKern
operator|=
name|minKernValue
expr_stmt|;
block|}
comment|/** Getter for property minPointSize.      * @return Value of property minPointSize.      */
specifier|public
name|float
name|getMinPointSize
parameter_list|()
block|{
return|return
name|minPointSize
return|;
block|}
comment|/** Setter for property minPointSize.      * @param minPointSizeValue New value of property minPointSize.      */
specifier|public
name|void
name|setMinPointSize
parameter_list|(
name|float
name|minPointSizeValue
parameter_list|)
block|{
name|minPointSize
operator|=
name|minPointSizeValue
expr_stmt|;
block|}
block|}
end_class

end_unit

