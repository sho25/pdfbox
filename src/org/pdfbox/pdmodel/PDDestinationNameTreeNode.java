begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
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
name|PDNameTreeNode
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
import|;
end_import

begin_comment
comment|/**  * This class holds all of the name trees that are available at the document level.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDDestinationNameTreeNode
extends|extends
name|PDNameTreeNode
block|{
comment|/**      * Constructor.      */
specifier|public
name|PDDestinationNameTreeNode
parameter_list|()
block|{
name|super
argument_list|(
name|PDPageDestination
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dic The COS dictionary.      */
specifier|public
name|PDDestinationNameTreeNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|super
argument_list|(
name|dic
argument_list|,
name|PDPageDestination
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|Object
name|convertCOSToPD
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|destination
init|=
name|base
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
comment|//the destination is sometimes stored in the D dictionary
comment|//entry instead of being directly an array, so just dereference
comment|//it for now
name|destination
operator|=
operator|(
operator|(
name|COSDictionary
operator|)
name|base
operator|)
operator|.
name|getDictionaryObject
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
block|}
return|return
name|PDDestination
operator|.
name|create
argument_list|(
name|destination
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|PDNameTreeNode
name|createChildNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
return|return
operator|new
name|PDDestinationNameTreeNode
argument_list|(
name|dic
argument_list|)
return|;
block|}
block|}
end_class

end_unit

