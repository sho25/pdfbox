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
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This is the used for the LZWDecode filter.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
class|class
name|LZWNode
block|{
specifier|private
name|long
name|code
decl_stmt|;
specifier|private
name|Map
name|subNodes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * This will get the number of children.      *      * @return The number of children.      */
specifier|public
name|int
name|childCount
parameter_list|()
block|{
return|return
name|subNodes
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * This will set the node for a particular byte.      *      * @param b The byte for that node.      * @param node The node to add.      */
specifier|public
name|void
name|setNode
parameter_list|(
name|byte
name|b
parameter_list|,
name|LZWNode
name|node
parameter_list|)
block|{
name|subNodes
operator|.
name|put
argument_list|(
operator|new
name|Byte
argument_list|(
name|b
argument_list|)
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the node that is a direct sub node of this node.      *      * @param data The byte code to the node.      *      * @return The node at that value if it exists.      */
specifier|public
name|LZWNode
name|getNode
parameter_list|(
name|byte
name|data
parameter_list|)
block|{
return|return
operator|(
name|LZWNode
operator|)
name|subNodes
operator|.
name|get
argument_list|(
operator|new
name|Byte
argument_list|(
name|data
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will traverse the tree until it gets to the sub node.      * This will return null if the node does not exist.      *      * @param data The path to the node.      *      * @return The node that resides at the data path.      */
specifier|public
name|LZWNode
name|getNode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|LZWNode
name|current
init|=
name|this
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data
operator|.
name|length
operator|&&
name|current
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|current
operator|=
name|current
operator|.
name|getNode
argument_list|(
name|data
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|current
return|;
block|}
comment|/** Getter for property code.      * @return Value of property code.      */
specifier|public
name|long
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
comment|/** Setter for property code.      * @param codeValue New value of property code.      */
specifier|public
name|void
name|setCode
parameter_list|(
name|long
name|codeValue
parameter_list|)
block|{
name|code
operator|=
name|codeValue
expr_stmt|;
block|}
block|}
end_class

end_unit

