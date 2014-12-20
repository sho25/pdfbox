begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|org
operator|.
name|apache
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
name|apache
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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

begin_comment
comment|/**  * A check box toggles between two states, on and off.  *  * @author Ben Litchfield  * @author sug  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDCheckbox
extends|extends
name|PDButton
block|{
specifier|private
name|COSName
name|value
decl_stmt|;
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|public
name|PDCheckbox
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|,
name|PDFieldTreeNode
name|parentNode
parameter_list|)
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|,
name|field
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
name|COSDictionary
name|ap
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|!=
literal|null
condition|)
block|{
name|COSBase
name|n
init|=
name|ap
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|instanceof
name|COSDictionary
condition|)
block|{
for|for
control|(
name|COSName
name|name
range|:
operator|(
operator|(
name|COSDictionary
operator|)
name|n
operator|)
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|OFF
argument_list|)
condition|)
block|{
name|value
operator|=
name|name
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|value
operator|=
operator|(
name|COSName
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if this radio button is currently checked or not.      *      * @return true If the radio button is checked.      */
specifier|public
name|boolean
name|isChecked
parameter_list|()
block|{
name|boolean
name|retval
init|=
literal|false
decl_stmt|;
name|String
name|onValue
init|=
name|getOnValue
argument_list|()
decl_stmt|;
name|COSName
name|radioValue
init|=
operator|(
name|COSName
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
decl_stmt|;
if|if
condition|(
name|radioValue
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
operator|&&
name|radioValue
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|onValue
argument_list|)
condition|)
block|{
name|retval
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Checks the radiobutton.      */
specifier|public
name|void
name|check
parameter_list|()
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unchecks the radiobutton.      */
specifier|public
name|void
name|unCheck
parameter_list|()
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|COSName
operator|.
name|OFF
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the value of the radio button.      *      * @return The value of the radio button.      */
specifier|public
name|String
name|getOffValue
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|OFF
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will get the value of the radio button.      *      * @return The value of the radio button.      */
specifier|public
name|String
name|getOnValue
parameter_list|()
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|ap
init|=
operator|(
name|COSDictionary
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
decl_stmt|;
name|COSBase
name|n
init|=
name|ap
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
comment|//N can be a COSDictionary or a COSStream
if|if
condition|(
name|n
operator|instanceof
name|COSDictionary
condition|)
block|{
for|for
control|(
name|COSName
name|key
range|:
operator|(
operator|(
name|COSDictionary
operator|)
name|n
operator|)
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|OFF
argument_list|)
condition|)
block|{
name|retval
operator|=
name|key
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
annotation|@
name|Override
specifier|public
name|COSName
name|getDefaultValue
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|attribute
init|=
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
name|COSName
operator|)
name|attribute
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected a COSName entry but got "
operator|+
name|attribute
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Set the fields default value.      *       * The field value holds a name object which is corresponding to the       * appearance state representing the corresponding appearance       * from the appearance directory.      *      * The default value is used to represent the initial state of the      * checkbox or to revert when resetting the form.      *       * @param defaultValue the COSName object to set the field value.      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|COSName
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|defaultValue
operator|==
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DV
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|COSName
name|getValue
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
return|;
block|}
comment|/**      * Set the field value.      *       * The field value holds a name object which is corresponding to the       * appearance state representing the corresponding appearance       * from the appearance directory.      *      * The default value is Off.      *       * @param value the COSName object to set the field value.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|COSName
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|COSName
operator|.
name|OFF
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

