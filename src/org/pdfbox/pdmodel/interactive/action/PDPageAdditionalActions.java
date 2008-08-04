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
name|interactive
operator|.
name|action
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
name|action
operator|.
name|type
operator|.
name|PDAction
import|;
end_import

begin_comment
comment|/**  * This class represents a page object's dictionary of actions  * that occur due to events.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Panagiotis Toumasis (ptoumasis@mail.gr)  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDPageAdditionalActions
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|actions
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDPageAdditionalActions
parameter_list|()
block|{
name|actions
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDPageAdditionalActions
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|actions
operator|=
name|a
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
comment|/**      * This will get an action to be performed when the page      * is opened. This action is independent of any that may be      * defined by the OpenAction entry in the document catalog,      * and is executed after such an action.      *      * @return The O entry of page object's additional actions dictionary.      */
specifier|public
name|PDAction
name|getO
parameter_list|()
block|{
name|COSDictionary
name|o
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"O"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set an action to be performed when the page      * is opened. This action is independent of any that may be      * defined by the OpenAction entry in the document catalog,      * and is executed after such an action.      *      * @param o The action to be performed.      */
specifier|public
name|void
name|setO
parameter_list|(
name|PDAction
name|o
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"O"
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get an action to be performed when the page      * is closed. This action applies to the page being closed,      * and is executed before any other page opened.      *      * @return The C entry of page object's additional actions dictionary.      */
specifier|public
name|PDAction
name|getC
parameter_list|()
block|{
name|COSDictionary
name|c
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"C"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set an action to be performed when the page      * is closed. This action applies to the page being closed,      * and is executed before any other page opened.      *      * @param c The action to be performed.      */
specifier|public
name|void
name|setC
parameter_list|(
name|PDAction
name|c
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"C"
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

