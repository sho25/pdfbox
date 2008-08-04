begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdfparser
package|;
end_package

begin_comment
comment|/**  * This class represents a PDF xref.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFXref
block|{
specifier|private
name|long
name|count
decl_stmt|;
specifier|private
name|long
name|start
decl_stmt|;
comment|/**      * constructor.      *      * @param startValue The start attribute.      * @param countValue The count attribute.      */
specifier|public
name|PDFXref
parameter_list|(
name|long
name|startValue
parameter_list|,
name|long
name|countValue
parameter_list|)
block|{
name|setStart
argument_list|(
name|startValue
argument_list|)
expr_stmt|;
name|setCount
argument_list|(
name|countValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the count attribute.      *      * @return The count.      */
specifier|public
name|long
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
comment|/**      * This will get the start attribute.      *      * @return The start.      */
specifier|public
name|long
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
comment|/**      * This will set the count attribute.      *      * @param newCount The new count.      */
specifier|private
name|void
name|setCount
parameter_list|(
name|long
name|newCount
parameter_list|)
block|{
name|count
operator|=
name|newCount
expr_stmt|;
block|}
comment|/**      * This will set the start attribute.      *      * @param newStart The new start attribute.      */
specifier|private
name|void
name|setStart
parameter_list|(
name|long
name|newStart
parameter_list|)
block|{
name|start
operator|=
name|newStart
expr_stmt|;
block|}
block|}
end_class

end_unit

