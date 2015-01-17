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
operator|.
name|FieldUtils
operator|.
name|KeyValue
import|;
end_import

begin_comment
comment|/**  * A choice field contains several text items, one or more of which shall be selected as the field value.  *   * @author sug  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDChoice
extends|extends
name|PDVariableText
block|{
comment|/**      *  Ff-flags.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_COMBO
init|=
literal|1
operator|<<
literal|17
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_SORT
init|=
literal|1
operator|<<
literal|19
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_MULTI_SELECT
init|=
literal|1
operator|<<
literal|21
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_DO_NOT_SPELL_CHECK
init|=
literal|1
operator|<<
literal|22
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_COMMIT_ON_SEL_CHANGE
init|=
literal|1
operator|<<
literal|26
decl_stmt|;
comment|/**      * @see PDFieldTreeNode#PDFieldTreeNode(PDAcroForm)      *      * @param theAcroForm The acroform.      */
specifier|protected
name|PDChoice
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
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|protected
name|PDChoice
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
comment|/**      * Get the fields default value.      *       * The value is stored in the field dictionaries "DV" entry.      *       * @return The value of this entry.      */
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * Set the default value for the field.      *       * @param value the value      */
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
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getOptions
argument_list|()
operator|.
name|indexOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The list box does not contain the given value."
argument_list|)
throw|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DV
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
block|}
comment|/**      * This will get the option values "Opt".      *       *<p>      * For a choice field the options array can either be an array      * of text strings or an array of a two-element arrays.<br/>      * The method always only returns either the text strings or,      * in case of two-element arrays, an array of the first element of       * the two-element arrays      *</p>         *<p>      * Use {@link #getOptionsExportValues()} and {@link #getOptionsDisplayValues()}      * to get the entries of two-element arrays.      *</p>      *       * @return List containing the export values.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
name|COSBase
name|values
init|=
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
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
name|values
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
name|values
operator|instanceof
name|COSArray
condition|)
block|{
comment|// test if there is a single text or a two-element array
name|COSBase
name|entry
init|=
operator|(
operator|(
name|COSArray
operator|)
name|values
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|instanceof
name|COSString
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
name|values
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|exportValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|numItems
init|=
operator|(
operator|(
name|COSArray
operator|)
name|values
operator|)
operator|.
name|size
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
name|numItems
condition|;
name|i
operator|++
control|)
block|{
name|COSArray
name|pair
init|=
call|(
name|COSArray
call|)
argument_list|(
operator|(
name|COSArray
operator|)
name|values
argument_list|)
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSString
name|displayValue
init|=
operator|(
name|COSString
operator|)
name|pair
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|exportValues
operator|.
name|add
argument_list|(
name|displayValue
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exportValues
return|;
block|}
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
comment|/**      * This will set the display values - the 'Opt' key.      *       *<p>      * The Opt array specifies the list of options in the choice field either      * as an array of text strings representing the display value       * or as an array of a two-element array where the      * first element is the export value and the second the display value.      *</p>      *<p>      * To set both the export and the display value use {@link #setOptions(List, List)}      *</p>       *      * @param displayValues List containing all possible options.      */
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|displayValues
parameter_list|)
block|{
if|if
condition|(
name|displayValues
operator|!=
literal|null
operator|&&
operator|!
name|displayValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|isSort
argument_list|()
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|displayValues
argument_list|)
expr_stmt|;
block|}
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|COSArrayList
operator|.
name|convertStringListToCOSStringCOSArray
argument_list|(
name|displayValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
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
comment|/**      * This will set the display and export values - the 'Opt' key.      *      *<p>      * This will set both, the export value and the display value      * of the choice field. If either one of the parameters is null or an       * empty list is supplied the options will      * be removed.      *</p>      *<p>      * An {@link IllegalArgumentException} will be thrown if the      * number of items in the list differ.      *</p>      *      * @see #setOptions(List)      * @param exportValues List containing all possible export values.      * @param displayValues List containing all possible display values.      */
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|exportValues
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|displayValues
parameter_list|)
block|{
if|if
condition|(
name|exportValues
operator|!=
literal|null
operator|&&
name|displayValues
operator|!=
literal|null
operator|&&
operator|!
name|exportValues
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|displayValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|exportValues
operator|.
name|size
argument_list|()
operator|!=
name|displayValues
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The number of entries for exportValue and displayValue shall be the same."
argument_list|)
throw|;
block|}
else|else
block|{
name|List
argument_list|<
name|KeyValue
argument_list|>
name|keyValuePairs
init|=
name|FieldUtils
operator|.
name|toKeyValueList
argument_list|(
name|exportValues
argument_list|,
name|displayValues
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSort
argument_list|()
condition|)
block|{
name|FieldUtils
operator|.
name|sortByValue
argument_list|(
name|keyValuePairs
argument_list|)
expr_stmt|;
block|}
name|COSArray
name|options
init|=
operator|new
name|COSArray
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
name|exportValues
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSArray
name|entry
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|entry
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|keyValuePairs
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|keyValuePairs
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getDictionary
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
comment|/**      * This will get the display values from the options.      *       *<p>      * For options with an array of text strings the display value and export value      * are the same.<br/>      * For options with an array of two-element arrays the display value is the       * second entry in the two-element array.      *</p>      *       * @return List containing all the display values.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptionsDisplayValues
parameter_list|()
block|{
name|COSBase
name|values
init|=
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
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
name|values
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
name|values
operator|instanceof
name|COSArray
condition|)
block|{
comment|// test if there is a single text or a two-element array
name|COSBase
name|entry
init|=
operator|(
operator|(
name|COSArray
operator|)
name|values
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|instanceof
name|COSString
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
name|values
argument_list|)
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|displayValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|numItems
init|=
operator|(
operator|(
name|COSArray
operator|)
name|values
operator|)
operator|.
name|size
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
name|numItems
condition|;
name|i
operator|++
control|)
block|{
name|COSArray
name|pair
init|=
call|(
name|COSArray
call|)
argument_list|(
operator|(
name|COSArray
operator|)
name|values
argument_list|)
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSString
name|displayValue
init|=
operator|(
name|COSString
operator|)
name|pair
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|displayValues
operator|.
name|add
argument_list|(
name|displayValue
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|displayValues
return|;
block|}
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
comment|/**      * This will get the export values from the options.      *       *<p>      * For options with an array of text strings the display value and export value      * are the same.<br/>      * For options with an array of two-element arrays the export value is the       * first entry in the two-element array.      *</p>      *      * @return List containing all export values.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptionsExportValues
parameter_list|()
block|{
return|return
name|getOptions
argument_list|()
return|;
block|}
comment|/**      * This will get the indices of the selected options - the 'I' key.      *<p>      * This is only needed if a choice field allows multiple selections and      * two different items have the same export value or more than one values      * is selected.      *</p>      *<p>The indices are zero-based</p>      *      * @return List containing the indices of all selected options.      */
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|getSelectedOptionsIndex
parameter_list|()
block|{
name|COSBase
name|value
init|=
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|I
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|COSArrayList
operator|.
name|convertIntegerCOSArrayToList
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
name|Integer
operator|>
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * This will set the indices of the selected options - the 'I' key.      *<p>      * This method is preferred over {@link #setValue(List)} for choice fields which      *<ul>      *<li>do support multiple selections</li>      *<li>have export values with the same value</li>      *</ul>      *</p>      *<p>      * Setting the index will set the value too.      *</p>      *      * @param values List containing the indices of all selected options.      */
specifier|public
name|void
name|setSelectedOptionsIndex
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|)
block|{
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
if|if
condition|(
operator|!
name|isMultiSelect
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Setting the indices is not allowed for choice fields not allowing multiple selections."
argument_list|)
throw|;
block|}
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|I
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|I
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Determines if Sort is set.      *       *<p>      * If set, the field’s option items shall be sorted alphabetically.      * The sorting has to be done when writing the PDF. PDF Readers are supposed to      * display the options in the order in which they occur in the Opt array.       *</p>      *       * @return true if the options are sorted.      */
specifier|public
name|boolean
name|isSort
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
name|FLAG_SORT
argument_list|)
return|;
block|}
comment|/**      * Set the Sort bit.      *       * @see #isSort()      * @param sort The value for Sort.      */
specifier|public
name|void
name|setSort
parameter_list|(
name|boolean
name|sort
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
name|FLAG_SORT
argument_list|,
name|sort
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if MultiSelect is set.      *       * @return true if multi select is allowed.      */
specifier|public
name|boolean
name|isMultiSelect
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
name|FLAG_MULTI_SELECT
argument_list|)
return|;
block|}
comment|/**      * Set the MultiSelect bit.      *      * @param multiSelect The value for MultiSelect.      */
specifier|public
name|void
name|setMultiSelect
parameter_list|(
name|boolean
name|multiSelect
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
name|FLAG_MULTI_SELECT
argument_list|,
name|multiSelect
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if DoNotSpellCheck is set.      *       * @return true if spell checker is disabled.      */
specifier|public
name|boolean
name|isDoNotSpellCheck
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
name|FLAG_DO_NOT_SPELL_CHECK
argument_list|)
return|;
block|}
comment|/**      * Set the DoNotSpellCheck bit.      *      * @param doNotSpellCheck The value for DoNotSpellCheck.      */
specifier|public
name|void
name|setDoNotSpellCheck
parameter_list|(
name|boolean
name|doNotSpellCheck
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
name|FLAG_DO_NOT_SPELL_CHECK
argument_list|,
name|doNotSpellCheck
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if CommitOnSelChange is set.      *       * @return true if value shall be committed as soon as a selection is made.      */
specifier|public
name|boolean
name|isCommitOnSelChange
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
name|FLAG_COMMIT_ON_SEL_CHANGE
argument_list|)
return|;
block|}
comment|/**      * Set the CommitOnSelChange bit.      *      * @param commitOnSelChange The value for CommitOnSelChange.      */
specifier|public
name|void
name|setCommitOnSelChange
parameter_list|(
name|boolean
name|commitOnSelChange
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
name|FLAG_COMMIT_ON_SEL_CHANGE
argument_list|,
name|commitOnSelChange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if Combo is set.      *       * @return true if value the choice is a combo box..      */
specifier|public
name|boolean
name|isCombo
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
name|FLAG_COMBO
argument_list|)
return|;
block|}
comment|/**      * Set the Combo bit.      *      * @param combo The value for Combo.      */
specifier|public
name|void
name|setCombo
parameter_list|(
name|boolean
name|combo
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
name|FLAG_COMBO
argument_list|,
name|combo
argument_list|)
expr_stmt|;
block|}
comment|/**      * setValue sets the entry "V" to the given value.      *       * @param value the value      *       */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getOptions
argument_list|()
operator|.
name|indexOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The list box does not contain the given value."
argument_list|)
throw|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
comment|// remove I key for single valued choice field
name|setSelectedOptionsIndex
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
block|}
comment|// TODO create/update appearance
block|}
comment|/**      * setValue sets the entry "V" to the given value.      *       * @param values the list of values      */
specifier|public
name|void
name|setValue
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
if|if
condition|(
operator|!
name|isMultiSelect
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The list box does not allow multiple selections."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|getOptions
argument_list|()
operator|.
name|containsAll
argument_list|(
name|values
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The values are not contained in the selectable options."
argument_list|)
throw|;
block|}
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|COSArrayList
operator|.
name|convertStringListToCOSStringCOSArray
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|updateSelectedOptionsIndex
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
comment|// TODO create/update appearance
block|}
comment|/**      * getValue gets the value of the "V" entry.      *       * @return The value of this entry.      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getValue
parameter_list|()
block|{
name|COSBase
name|value
init|=
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
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
comment|/**      * Update the 'I' key based on values set.      */
specifier|private
name|void
name|updateSelectedOptionsIndex
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
name|getOptions
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|indices
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|indices
operator|.
name|add
argument_list|(
name|options
operator|.
name|indexOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Indices have to be "... array of integers, sorted in ascending order ..."
name|Collections
operator|.
name|sort
argument_list|(
name|indices
argument_list|)
expr_stmt|;
name|setSelectedOptionsIndex
argument_list|(
name|indices
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

