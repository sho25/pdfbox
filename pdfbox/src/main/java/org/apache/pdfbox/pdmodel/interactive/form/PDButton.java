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
name|org
operator|.
name|apache
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
name|COSString
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
name|pdmodel
operator|.
name|common
operator|.
name|COSArrayList
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/**  * A button field represents an interactive control on the screen  * that the user can manipulate with the mouse.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDButton
extends|extends
name|PDField
block|{
comment|/**      * The value for the Off state for PDCheckbox and PDRadioButton.      *       * This shall not be confused with the OFF state as it is used within      * other parts of a PDF.       *       */
specifier|static
specifier|final
name|COSName
name|OFF
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Off"
argument_list|)
decl_stmt|;
comment|/**      * A Ff flag. If set, the field is a set of radio buttons      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_RADIO
init|=
literal|1
operator|<<
literal|15
decl_stmt|;
comment|/**      * A Ff flag. If set, the field is a pushbutton.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_PUSHBUTTON
init|=
literal|1
operator|<<
literal|16
decl_stmt|;
comment|/**      * A Ff flag. If set, radio buttons individual fields, using the same      * value for the on state will turn on and off in unison.      */
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
comment|/**      * @see PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroform.      */
specifier|public
name|PDButton
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|)
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FT
argument_list|,
name|COSName
operator|.
name|BTN
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|public
name|PDButton
parameter_list|(
name|PDAcroForm
name|acroForm
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
name|acroForm
argument_list|,
name|field
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if push button bit is set.      *       * @return true if type of button field is a push button.      */
specifier|public
name|boolean
name|isPushButton
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_PUSHBUTTON
argument_list|)
return|;
block|}
comment|/**      * Set the push button bit.      *      * @param pushbutton if true the button field is treated as a push button field.      */
specifier|public
name|void
name|setPushButton
parameter_list|(
name|boolean
name|pushbutton
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_PUSHBUTTON
argument_list|,
name|pushbutton
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if radio button bit is set.      *       * @return true if type of button field is a push button.      */
specifier|public
name|boolean
name|isRadioButton
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_RADIO
argument_list|)
return|;
block|}
comment|/**      * Set the radio button bit.      *      * @param radiobutton if true the button field is treated as a radio button field.      */
specifier|public
name|void
name|setRadioButton
parameter_list|(
name|boolean
name|radiobutton
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_RADIO
argument_list|,
name|radiobutton
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
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
name|DV
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
elseif|else
if|if
condition|(
name|attribute
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|attribute
operator|)
operator|.
name|getName
argument_list|()
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
comment|/**      * Set the fields default value.      *       * The field value holds a name object which is corresponding to the       * appearance state representing the corresponding appearance       * from the appearance directory.      *      * The default value is used to represent the initial state of the      * field or to revert when resetting the form.      *       * @param defaultValue the new field value.      */
annotation|@
name|Override
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
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
name|COSName
operator|.
name|getPDFName
argument_list|(
name|defaultValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the option values - the "Opt" entry.      *       *<p>The option values are used to define the export values      * for the field to       *<ul>      *<li>hold values in non-Latin writing systems as name objects, which represent the field value, are limited      *      to PDFDocEncoding      *</li>      *<li>allow radio buttons having the same export value to be handled independently      *</li>      *</ul>      *</p>      *       * @return List containing all possible options. If there is no Opt entry an empty list will be returned.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
name|COSBase
name|value
init|=
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|COSString
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|array
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|value
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|COSArray
condition|)
block|{
return|return
name|COSArrayList
operator|.
name|convertCOSStringCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|value
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * This will set the options.      *       * @see #getOptions()      * @param values List containing all possible options. Supplying null or an empty list will remove the Opt entry.      */
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|removeInheritableAttribute
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setInheritableAttribute
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|COSArrayList
operator|.
name|convertStringListToCOSStringCOSArray
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

