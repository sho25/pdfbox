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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|annotation
operator|.
name|PDAppearanceDictionary
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAppearanceEntry
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
comment|/**      * @see PDField#PDField(PDAcroForm)      *      * @param acroForm The acroform.      */
specifier|public
name|PDCheckbox
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
name|super
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDCheckbox
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|,
name|PDNonTerminalField
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will tell if this radio button is currently checked or not.      * This is equivalent to calling {@link #getValue()}.      *      * @return true If this field is checked.      */
specifier|public
name|boolean
name|isChecked
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
operator|.
name|compareTo
argument_list|(
name|getOnValue
argument_list|()
argument_list|)
operator|==
literal|0
return|;
block|}
comment|/**      * Checks the check box.      *       * @throws IOException if the appearance couldn't be generated.      */
specifier|public
name|void
name|check
parameter_list|()
throws|throws
name|IOException
block|{
name|setValue
argument_list|(
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unchecks the check box.      *       * @throws IOException if the appearance couldn't be generated.      */
specifier|public
name|void
name|unCheck
parameter_list|()
throws|throws
name|IOException
block|{
name|setValue
argument_list|(
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the fields value entry.      *       * @return the fields value entry.      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
comment|// the dictionary shall be a name object but it might not be
comment|// so don't assume it is.
name|COSBase
name|value
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
name|value
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
comment|/**      * Returns the default value, if any.      *      * @return the fields default value.      */
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
comment|// the dictionary shall be a name object but it might not be
comment|// so don't assume it is.
name|COSBase
name|value
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
name|value
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getValueAsString
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
return|;
block|}
comment|/**      * Sets the checked value of this field.      *       *<p>To retrieve the potential On value use {@link #getOnValue()} or      * {@link #getOnValues()}. The Off value shall always be 'Off'.</p>      *      * @param value matching the On or Off state of the checkbox.      * @throws IOException if the appearance couldn't be generated.      * @throws IllegalArgumentException if the value is not a valid option for the checkbox.      */
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
if|if
condition|(
name|value
operator|.
name|compareTo
argument_list|(
name|getOnValue
argument_list|()
argument_list|)
operator|!=
literal|0
operator|&&
name|value
operator|.
name|compareTo
argument_list|(
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
operator|+
literal|" is not a valid option for the checkbox "
operator|+
name|getFullyQualifiedName
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
comment|// Update the field value and the appearance state.
comment|// Both are necessary to work properly with different viewers.
name|COSName
name|name
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|getWidgets
argument_list|()
control|)
block|{
name|widget
operator|.
name|setAppearanceState
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|applyChange
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the default value.      *      * @see #setValue(String)      * @param value matching the On or Off state of the checkbox.      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|compareTo
argument_list|(
name|getOnValue
argument_list|()
argument_list|)
operator|!=
literal|0
operator|&&
name|value
operator|.
name|compareTo
argument_list|(
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
operator|+
literal|" is not a valid option for the checkbox "
operator|+
name|getFullyQualifiedName
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|dictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|DV
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the value which sets the check box to the On state.      *       *<p>The On value should be 'Yes' but other values are possible      * so we need to look for that. On the other hand the Off value shall      * always be 'Off'. If not set or not part of the normal appearance keys      * 'Off' is the default</p>      *      * @return the value setting the check box to the On state.       *          If an empty string is returned there is no appearance definition.      */
specifier|public
name|String
name|getOnValue
parameter_list|()
block|{
name|PDAnnotationWidget
name|widget
init|=
name|this
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDAppearanceDictionary
name|apDictionary
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
name|String
name|onValue
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|apDictionary
operator|!=
literal|null
condition|)
block|{
name|PDAppearanceEntry
name|normalAppearance
init|=
name|apDictionary
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|normalAppearance
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|COSName
argument_list|>
name|entries
init|=
name|normalAppearance
operator|.
name|getSubDictionary
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|entry
range|:
name|entries
control|)
block|{
if|if
condition|(
name|COSName
operator|.
name|Off
operator|.
name|compareTo
argument_list|(
name|entry
argument_list|)
operator|!=
literal|0
condition|)
block|{
name|onValue
operator|=
name|entry
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|onValue
return|;
block|}
comment|/**      * Get the values which sets the check box to the On state.      *       *<p>This is a convenience function to provide a similar method to       * {@link PDRadioButton}</p>      *      * @see #getOnValue()      * @return the value setting the check box to the On state.       *          If an empty List is returned there is no appearance definition.      */
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOnValues
parameter_list|()
block|{
name|String
name|onValue
init|=
name|getOnValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|onValue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|onValues
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|onValues
operator|.
name|add
argument_list|(
name|onValue
argument_list|)
expr_stmt|;
return|return
name|onValues
return|;
block|}
block|}
block|}
end_class

end_unit

