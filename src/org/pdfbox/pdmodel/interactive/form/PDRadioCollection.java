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
name|form
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
name|COSDictionary
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
name|COSArrayList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|BitFlagHelper
import|;
end_import

begin_comment
comment|/**  * A class for handling the PDF field as a Radio Collection.  * This class automatically keeps track of the child radio buttons  * in the collection.  *  * @see PDCheckbox  * @author sug  * @version $Revision: 1.13 $  */
end_comment

begin_class
specifier|public
class|class
name|PDRadioCollection
extends|extends
name|PDChoiceButton
block|{
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_RADIOS_IN_UNISON
init|=
literal|1
operator|<<
literal|25
decl_stmt|;
comment|/**       * @param theAcroForm The acroForm for this field.      * @param field The field that makes up the radio collection.      *       * {@inheritDoc}      */
specifier|public
name|PDRadioCollection
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * From the PDF Spec<br/>      * If set, a group of radio buttons within a radio button field that      * use the same value for the on state will turn on and off in unison; that is if      * one is checked, they are all checked. If clear, the buttons are mutually exclusive      * (the same behavior as HTML radio buttons).      *      * @param radiosInUnison The new flag for radiosInUnison.      */
specifier|public
name|void
name|setRadiosInUnison
parameter_list|(
name|boolean
name|radiosInUnison
parameter_list|)
block|{
name|BitFlagHelper
operator|.
name|setFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_RADIOS_IN_UNISON
argument_list|,
name|radiosInUnison
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true If the flag is set for radios in unison.      */
specifier|public
name|boolean
name|isRadiosInUnison
parameter_list|()
block|{
return|return
name|BitFlagHelper
operator|.
name|getFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_RADIOS_IN_UNISON
argument_list|)
return|;
block|}
comment|/**      * This setValue method iterates the collection of radiobuttons      * and checks or unchecks each radiobutton according to the      * given value.      * If the value is not represented by any of the radiobuttons,      * then none will be checked.      *      * {@inheritDoc}      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
literal|"V"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|List
name|kids
init|=
name|getKids
argument_list|()
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
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDCheckbox
name|btn
init|=
operator|(
name|PDCheckbox
operator|)
name|kids
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|btn
operator|.
name|getOnValue
argument_list|()
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|btn
operator|.
name|check
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|btn
operator|.
name|unCheck
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * getValue gets the fields value to as a string.      *      * @return The string value of this field.      *      * @throws IOException If there is an error getting the value.      */
specifier|public
name|String
name|getValue
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
name|List
name|kids
init|=
name|getKids
argument_list|()
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
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDCheckbox
name|btn
init|=
operator|(
name|PDCheckbox
operator|)
name|kids
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|btn
operator|.
name|isChecked
argument_list|()
condition|)
block|{
name|retval
operator|=
name|btn
operator|.
name|getOnValue
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"V"
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will return a list of PDField objects that are part of this radio collection.      *      * @see PDField#getWidget()      * @return A list of PDWidget objects.      * @throws IOException if there is an error while creating the children objects.      */
specifier|public
name|List
name|getKids
parameter_list|()
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
name|List
name|kidsList
init|=
operator|new
name|ArrayList
argument_list|()
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
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|kidsList
operator|.
name|add
argument_list|(
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|getAcroForm
argument_list|()
argument_list|,
operator|(
name|COSDictionary
operator|)
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|kidsList
argument_list|,
name|kids
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

