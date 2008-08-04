begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * This is the Factory for creating and returning the correct  * field elements.  *  * @author sug  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFieldFactory
block|{
specifier|private
specifier|static
specifier|final
name|int
name|RADIO_BITMASK
init|=
literal|32768
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PUSHBUTTON_BITMASK
init|=
literal|65536
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|RADIOS_IN_UNISON_BITMASK
init|=
literal|33554432
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIELD_TYPE_BTN
init|=
literal|"Btn"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIELD_TYPE_TX
init|=
literal|"Tx"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIELD_TYPE_CH
init|=
literal|"Ch"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIELD_TYPE_SIG
init|=
literal|"Sig"
decl_stmt|;
comment|/**      * Utility class so no constructor.      */
specifier|private
name|PDFieldFactory
parameter_list|()
block|{
comment|//do nothing.
block|}
comment|/**      * This method creates a COSField subclass from the given field.      * The field is a PDF Dictionary object that must represent a      * field element. - othewise null is returned      *      * @param acroForm The form that the field will be part of.      * @param field The dictionary representing a field element      *      * @return a subclass to COSField according to the kind of field passed to createField      * @throws IOException If there is an error determining the field type.      */
specifier|public
specifier|static
name|PDField
name|createField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|)
throws|throws
name|IOException
block|{
name|PDField
name|pdField
init|=
operator|new
name|PDUnknownField
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
decl_stmt|;
if|if
condition|(
name|isButton
argument_list|(
name|pdField
argument_list|)
condition|)
block|{
name|int
name|flags
init|=
name|pdField
operator|.
name|getFieldFlags
argument_list|()
decl_stmt|;
comment|//BJL, I have found that the radio flag bit is not always set
comment|//and that sometimes there is just a kids dictionary.
comment|//so, if there is a kids dictionary then it must be a radio button
comment|//group.
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Kids"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
operator|||
name|isRadio
argument_list|(
name|flags
argument_list|)
condition|)
block|{
name|pdField
operator|=
operator|new
name|PDRadioCollection
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPushButton
argument_list|(
name|flags
argument_list|)
condition|)
block|{
name|pdField
operator|=
operator|new
name|PDPushButton
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pdField
operator|=
operator|new
name|PDCheckbox
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isChoiceField
argument_list|(
name|pdField
argument_list|)
condition|)
block|{
name|pdField
operator|=
operator|new
name|PDChoiceField
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isTextbox
argument_list|(
name|pdField
argument_list|)
condition|)
block|{
name|pdField
operator|=
operator|new
name|PDTextbox
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isSignature
argument_list|(
name|pdField
argument_list|)
condition|)
block|{
name|pdField
operator|=
operator|new
name|PDSignature
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//do nothing and return an unknown field type.
block|}
return|return
name|pdField
return|;
block|}
comment|/**      * This method determines if the given      * field is a radiobutton collection.      *      * @param flags The field flags.      *      * @return the result of the determination      */
specifier|private
specifier|static
name|boolean
name|isRadio
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
return|return
operator|(
name|flags
operator|&
name|RADIO_BITMASK
operator|)
operator|>
literal|0
return|;
block|}
comment|/**      * This method determines if the given      * field is a pushbutton.      *      * @param flags The field flags.      *      * @return the result of the determination      */
specifier|private
specifier|static
name|boolean
name|isPushButton
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
return|return
operator|(
name|flags
operator|&
name|PUSHBUTTON_BITMASK
operator|)
operator|>
literal|0
return|;
block|}
comment|/**      * This method determines if the given field is a choicefield      * Choicefields are either listboxes or comboboxes.      *      * @param field the field to determine      * @return the result of the determination      */
specifier|private
specifier|static
name|boolean
name|isChoiceField
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FIELD_TYPE_CH
operator|.
name|equals
argument_list|(
name|field
operator|.
name|findFieldType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This method determines if the given field is a button.      *      * @param field the field to determine      * @return the result of the determination      *       * @throws IOException If there is an error determining the field type.      */
specifier|private
specifier|static
name|boolean
name|isButton
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|ft
init|=
name|field
operator|.
name|findFieldType
argument_list|()
decl_stmt|;
name|boolean
name|retval
init|=
name|FIELD_TYPE_BTN
operator|.
name|equals
argument_list|(
name|ft
argument_list|)
decl_stmt|;
name|List
name|kids
init|=
name|field
operator|.
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|ft
operator|==
literal|null
operator|&&
name|kids
operator|!=
literal|null
operator|&&
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|//sometimes if it is a button the type is only defined by one
comment|//of the kids entries
name|Object
name|obj
init|=
name|kids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSDictionary
name|kidDict
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|PDField
condition|)
block|{
name|kidDict
operator|=
operator|(
operator|(
name|PDField
operator|)
name|obj
operator|)
operator|.
name|getDictionary
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|PDAnnotationWidget
condition|)
block|{
name|kidDict
operator|=
operator|(
operator|(
name|PDAnnotationWidget
operator|)
name|obj
operator|)
operator|.
name|getDictionary
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:Unexpected type of kids field:"
operator|+
name|obj
argument_list|)
throw|;
block|}
name|retval
operator|=
name|isButton
argument_list|(
operator|new
name|PDUnknownField
argument_list|(
name|field
operator|.
name|getAcroForm
argument_list|()
argument_list|,
name|kidDict
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This method determines if the given field is a signature.      *      * @param field the field to determine      * @return the result of the determination      */
specifier|private
specifier|static
name|boolean
name|isSignature
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FIELD_TYPE_SIG
operator|.
name|equals
argument_list|(
name|field
operator|.
name|findFieldType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This method determines if the given field is a Textbox.      *      * @param field the field to determine      * @return the result of the determination      */
specifier|private
specifier|static
name|boolean
name|isTextbox
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FIELD_TYPE_TX
operator|.
name|equals
argument_list|(
name|field
operator|.
name|findFieldType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

