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
name|HashSet
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
comment|/**  * A button field represents an interactive control on the screen  * that the user can manipulate with the mouse.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDButton
extends|extends
name|PDTerminalField
block|{
comment|/**      * A Ff flag. If set, the field is a set of radio buttons      */
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
specifier|static
specifier|final
name|int
name|FLAG_RADIOS_IN_UNISON
init|=
literal|1
operator|<<
literal|25
decl_stmt|;
comment|/**      * @see PDField#PDField(PDAcroForm)      *      * @param acroForm The acroform.      */
specifier|public
name|PDButton
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
name|getCOSObject
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
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDButton
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
comment|/**      * Determines if push button bit is set.      *       * @return true if type of button field is a push button.      */
specifier|public
name|boolean
name|isPushButton
parameter_list|()
block|{
return|return
name|getCOSObject
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
name|getCOSObject
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
name|getCOSObject
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
name|getCOSObject
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
comment|/**      * Returns the selected value. May be empty if NoToggleToOff is set but there is no value      * selected.      *       * @return A non-null string.      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
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
comment|/**      * Sets the selected option given its name.      *       * @param value Name of option to select      * @throws IOException if the value could not be set      * @throws IllegalArgumentException if the value is not a valid option.      */
annotation|@
name|Override
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
name|checkValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// if there are export values/an Opt entry there is a different
comment|// approach to setting the value
name|boolean
name|hasExportValues
init|=
name|getExportValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|hasExportValues
condition|)
block|{
name|updateByOption
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateByValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|applyChange
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the default value, if any.      *      * @return A non-null string.      */
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
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
comment|/**      * Sets the default value.      *      * @param value Name of option to select      * @throws IllegalArgumentException if the value is not a valid option.      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|checkValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
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
comment|/**      * This will get the export values.      *       *<p>The export values are defined in the field dictionaries /Opt key.</p>      *       *<p>The option values are used to define the export values      * for the field to       *<ul>      *<li>hold values in non-Latin writing systems as name objects, which represent the field value, are limited      *      to PDFDocEncoding      *</li>      *<li>allow radio buttons having the same export value to be handled independently      *</li>      *</ul>      *</p>      *       * @return List containing all possible export values. If there is no Opt entry an empty list will be returned.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExportValues
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
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * This will set the export values.      *       * @see #getExportValues()      * @param values List containing all possible export values. Supplying null or an empty list will remove the Opt entry.      */
specifier|public
name|void
name|setExportValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|COSArray
name|cosValues
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
operator|!
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cosValues
operator|=
name|COSArrayList
operator|.
name|convertStringListToCOSStringCOSArray
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|cosValues
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
name|void
name|constructAppearances
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|exportValues
init|=
name|getExportValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|exportValues
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// the value is the index value of the option. So we need to get that
comment|// and use it to set the value
try|try
block|{
name|int
name|optionsIndex
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|optionsIndex
operator|<
name|exportValues
operator|.
name|size
argument_list|()
condition|)
block|{
name|updateByOption
argument_list|(
name|exportValues
operator|.
name|get
argument_list|(
name|optionsIndex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// silently ignore that
comment|// and don't update the appearance
block|}
block|}
else|else
block|{
name|updateByValue
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the values to set individual buttons within a group to the on state.      *       *<p>The On value could be an arbitrary string as long as it is within the limitations of      * a PDF name object. The Off value shall always be 'Off'. If not set or not part of the normal      * appearance keys 'Off' is the default</p>      *      * @return the potential values setting the check box to the On state.       *         If an empty Set is returned there is no appearance definition.      */
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOnValues
parameter_list|()
block|{
comment|// we need a set as the field can appear multiple times
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
if|if
condition|(
name|getExportValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|onValues
operator|.
name|addAll
argument_list|(
name|getExportValues
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|onValues
return|;
block|}
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
name|this
operator|.
name|getWidgets
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|widgets
control|)
block|{
name|PDAppearanceDictionary
name|apDictionary
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
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
name|onValues
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|onValues
return|;
block|}
comment|/**      * Checks value.      *      * @param value Name of radio button to select      * @throws IllegalArgumentException if the value is not a valid option.      */
name|void
name|checkValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|onValues
init|=
name|getOnValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|value
argument_list|)
operator|!=
literal|0
operator|&&
operator|!
name|onValues
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"value '"
operator|+
name|value
operator|+
literal|"' is not a valid option for the field "
operator|+
name|getFullyQualifiedName
argument_list|()
operator|+
literal|", valid values are: "
operator|+
name|onValues
operator|+
literal|" and "
operator|+
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|updateByValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// update the appearance state (AS)
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|getWidgets
argument_list|()
control|)
block|{
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|(
name|COSDictionary
operator|)
name|appearanceEntry
operator|.
name|getCOSObject
argument_list|()
operator|)
operator|.
name|containsKey
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widget
operator|.
name|getCOSObject
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
name|Off
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|updateByOption
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
name|getWidgets
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
name|getExportValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|widgets
operator|.
name|size
argument_list|()
operator|!=
name|options
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The number of options doesn't match the number of widgets"
argument_list|)
throw|;
block|}
comment|// the value is the index of the matching option
name|int
name|optionsIndex
init|=
name|options
operator|.
name|indexOf
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|optionsIndex
argument_list|)
argument_list|)
expr_stmt|;
comment|// update the appearance state (AS)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|widgets
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDAnnotationWidget
name|widget
init|=
name|widgets
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|compareTo
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|==
literal|0
condition|)
block|{
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widget
operator|.
name|getCOSObject
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
name|Off
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

