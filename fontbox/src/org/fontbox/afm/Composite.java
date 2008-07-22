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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * This class represents composite character data.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|Composite
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|List
name|parts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/** Getter for property name.      * @return Value of property name.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/** Setter for property name.      * @param nameValue New value of property name.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|nameValue
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|nameValue
expr_stmt|;
block|}
comment|/**      * This will add a composite part.      *      * @param part The composite part to add.      */
specifier|public
name|void
name|addPart
parameter_list|(
name|CompositePart
name|part
parameter_list|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
comment|/** Getter for property parts.      * @return Value of property parts.      */
specifier|public
name|List
name|getParts
parameter_list|()
block|{
return|return
name|parts
return|;
block|}
comment|/** Setter for property parts.      * @param partsList New value of property parts.      */
specifier|public
name|void
name|setParts
parameter_list|(
name|List
name|partsList
parameter_list|)
block|{
name|this
operator|.
name|parts
operator|=
name|partsList
expr_stmt|;
block|}
block|}
end_class

end_unit

