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
name|ArrayList
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
comment|/**  * Radio button fields contain a set of related buttons that can each be on or off.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDRadioButton
extends|extends
name|PDButton
block|{
comment|/**      * A Ff flag.      */
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_NO_TOGGLE_TO_OFF
init|=
literal|1
operator|<<
literal|14
decl_stmt|;
comment|/**      * @see PDField#PDField(PDAcroForm)      *      * @param acroForm The acroform.      */
specifier|public
name|PDRadioButton
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
name|setRadioButton
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDRadioButton
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
comment|/**      * From the PDF Spec<br/>      * If set, a group of radio buttons within a radio button field that use the same value for the on state will turn      * on and off in unison; that is if one is checked, they are all checked. If clear, the buttons are mutually      * exclusive (the same behavior as HTML radio buttons).      *      * @param radiosInUnison The new flag for radiosInUnison.      */
specifier|public
name|void
name|setRadiosInUnison
parameter_list|(
name|boolean
name|radiosInUnison
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
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_RADIOS_IN_UNISON
argument_list|)
return|;
block|}
comment|/**      * This will get the selected export values.      *<p>      * A RadioButton might have an export value to allow field values      * which can not be encoded as PDFDocEncoding or for the same export value       * being assigned to multiple RadioButtons in a group.<br/>      * To define an export value the RadioButton must define options {@link #setExportValues(List)}      * which correspond to the individual items within the RadioButton.</p>      *<p>      * The method will either return the corresponding values from the options entry or in case there      * is no such entry the fields value</p>      *       * @return the export value of the field.      * @throws IOException in case the fields value can not be retrieved      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSelectedExportValues
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|onValues
init|=
name|getSelectableOnValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|exportValues
init|=
name|getExportValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|selectedExportValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|exportValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|selectedExportValues
operator|.
name|add
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|selectedExportValues
return|;
block|}
else|else
block|{
name|String
name|fieldValue
init|=
name|getValue
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|onValue
range|:
name|onValues
control|)
block|{
if|if
condition|(
name|onValue
operator|.
name|compareTo
argument_list|(
name|fieldValue
argument_list|)
operator|==
literal|0
condition|)
block|{
name|selectedExportValues
operator|.
name|add
argument_list|(
name|exportValues
operator|.
name|get
argument_list|(
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selectedExportValues
return|;
block|}
block|}
comment|/**      * Sets the selected radio button, given its name.      *       * @param value Name of radio button to select      * @throws IOException if the value could not be set      * @throws IllegalArgumentException if the value is not a valid option.      */
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
name|applyChange
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the default value.      *      * @param value Name of radio button to select      * @throws IllegalArgumentException if the value is not a valid option.      */
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
comment|/**      * Get the values to set individual radio buttons to the on state.      *       *<p>The On value could be an arbitrary string as long as it is within the limitations of      * a PDF name object. The Off value shall always be 'Off'. If not set or not part of the normal      * appearance keys 'Off' is the default</p>      *      * @return the value setting the check box to the On state.       *          If an empty string is returned there is no appearance definition.      */
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOnValues
parameter_list|()
block|{
comment|// we need a set as the radio buttons can appear multiple times
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
name|addAll
argument_list|(
name|getSelectableOnValues
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|onValues
return|;
block|}
comment|/**      * Checks value.      *      * @param value Name of radio button to select      * @throws IllegalArgumentException if the value is not a valid option.      */
specifier|private
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
literal|"' is not a valid option for the radio button "
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
comment|/**      * Get all potential ON values.      *       * @return the ON values.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getSelectableOnValues
parameter_list|()
block|{
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
comment|// we need a set as the radio buttons can appear multiple times
name|List
argument_list|<
name|String
argument_list|>
name|onValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
block|}
end_class

end_unit

