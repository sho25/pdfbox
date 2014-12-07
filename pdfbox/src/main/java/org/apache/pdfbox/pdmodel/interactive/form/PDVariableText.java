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
name|COSNumber
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

begin_comment
comment|/**  * Base class for fields which use "Variable Text".  * These fields construct an appearance stream dynamically at viewing time.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDVariableText
extends|extends
name|PDField
block|{
comment|/**      * Ff flags.      */
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_MULTILINE
init|=
literal|1
operator|<<
literal|12
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_PASSWORD
init|=
literal|1
operator|<<
literal|13
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_FILE_SELECT
init|=
literal|1
operator|<<
literal|20
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
name|FLAG_DO_NOT_SCROLL
init|=
literal|1
operator|<<
literal|23
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_COMB
init|=
literal|1
operator|<<
literal|24
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_RICH_TEXT
init|=
literal|1
operator|<<
literal|25
decl_stmt|;
comment|/**      * DA    Default appearance.      */
specifier|private
name|COSString
name|defaultAppearance
decl_stmt|;
comment|/**      * A Q value.      */
specifier|public
specifier|static
specifier|final
name|int
name|QUADDING_LEFT
init|=
literal|0
decl_stmt|;
comment|/**      * A Q value.      */
specifier|public
specifier|static
specifier|final
name|int
name|QUADDING_CENTERED
init|=
literal|1
decl_stmt|;
comment|/**      * A Q value.      */
specifier|public
specifier|static
specifier|final
name|int
name|QUADDING_RIGHT
init|=
literal|2
decl_stmt|;
comment|/**      * @see PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroform.      */
name|PDVariableText
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
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|protected
name|PDVariableText
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
block|}
comment|/**      * @return true if the field is multiline      */
specifier|public
name|boolean
name|isMultiline
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
name|FLAG_MULTILINE
argument_list|)
return|;
block|}
comment|/**      * Set the multiline bit.      *      * @param multiline The value for the multiline.      */
specifier|public
name|void
name|setMultiline
parameter_list|(
name|boolean
name|multiline
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
name|FLAG_MULTILINE
argument_list|,
name|multiline
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the field is a password field.      */
specifier|public
name|boolean
name|isPassword
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
name|FLAG_PASSWORD
argument_list|)
return|;
block|}
comment|/**      * Set the password bit.      *      * @param password The value for the password.      */
specifier|public
name|void
name|setPassword
parameter_list|(
name|boolean
name|password
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
name|FLAG_PASSWORD
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the field is a file select field.      */
specifier|public
name|boolean
name|isFileSelect
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
name|FLAG_FILE_SELECT
argument_list|)
return|;
block|}
comment|/**      * Set the file select bit.      *      * @param fileSelect The value for the fileSelect.      */
specifier|public
name|void
name|setFileSelect
parameter_list|(
name|boolean
name|fileSelect
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
name|FLAG_FILE_SELECT
argument_list|,
name|fileSelect
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the field is not suppose to spell check.      */
specifier|public
name|boolean
name|doNotSpellCheck
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
comment|/**      * Set the doNotSpellCheck bit.      *      * @param doNotSpellCheck The value for the doNotSpellCheck.      */
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
comment|/**      * @return true if the field is not suppose to scroll.      */
specifier|public
name|boolean
name|doNotScroll
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
name|FLAG_DO_NOT_SCROLL
argument_list|)
return|;
block|}
comment|/**      * Set the doNotScroll bit.      *      * @param doNotScroll The value for the doNotScroll.      */
specifier|public
name|void
name|setDoNotScroll
parameter_list|(
name|boolean
name|doNotScroll
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
name|FLAG_DO_NOT_SCROLL
argument_list|,
name|doNotScroll
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the field is not suppose to comb the text display.      */
specifier|public
name|boolean
name|isComb
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
name|FLAG_COMB
argument_list|)
return|;
block|}
comment|/**      * Set the comb bit.      *      * @param comb The value for the comb.      */
specifier|public
name|void
name|setComb
parameter_list|(
name|boolean
name|comb
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
name|FLAG_COMB
argument_list|,
name|comb
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return true if the field is a rich text field.      */
specifier|public
name|boolean
name|isRichText
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
name|FLAG_RICH_TEXT
argument_list|)
return|;
block|}
comment|/**      * Set the richText bit.      *      * @param richText The value for the richText.      */
specifier|public
name|void
name|setRichText
parameter_list|(
name|boolean
name|richText
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
name|FLAG_RICH_TEXT
argument_list|,
name|richText
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default appearance.      *       * @return the DA element of the dictionary object      */
specifier|public
name|COSString
name|getDefaultAppearance
parameter_list|()
block|{
if|if
condition|(
name|defaultAppearance
operator|==
literal|null
condition|)
block|{
name|COSBase
name|daValue
init|=
name|getDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
decl_stmt|;
if|if
condition|(
name|daValue
operator|!=
literal|null
condition|)
block|{
name|defaultAppearance
operator|=
operator|(
name|COSString
operator|)
name|daValue
expr_stmt|;
block|}
block|}
comment|// the default appearance is inheritable
comment|// maybe the parent provides a default appearance
if|if
condition|(
name|defaultAppearance
operator|==
literal|null
condition|)
block|{
name|PDFieldTreeNode
name|parent
init|=
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|instanceof
name|PDVariableText
condition|)
block|{
name|defaultAppearance
operator|=
operator|(
operator|(
name|PDVariableText
operator|)
name|parent
operator|)
operator|.
name|getDefaultAppearance
argument_list|()
expr_stmt|;
block|}
block|}
comment|// the default appearance is inheritable
comment|// the acroform should provide a default appearance
if|if
condition|(
name|defaultAppearance
operator|==
literal|null
condition|)
block|{
name|defaultAppearance
operator|=
name|getAcroForm
argument_list|()
operator|.
name|getDefaultAppearance
argument_list|()
expr_stmt|;
block|}
return|return
name|defaultAppearance
return|;
block|}
comment|/**      * Set the default appearance.      *       * @param daValue a string describing the default appearance      */
specifier|public
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|daValue
parameter_list|)
block|{
if|if
condition|(
name|daValue
operator|!=
literal|null
condition|)
block|{
name|defaultAppearance
operator|=
operator|new
name|COSString
argument_list|(
name|daValue
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DA
argument_list|,
name|defaultAppearance
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|defaultAppearance
operator|=
literal|null
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the 'quadding' or justification of the text to be displayed.      * 0 - Left(default)<br/>      * 1 - Centered<br />      * 2 - Right<br />      * Please see the QUADDING_CONSTANTS.      *      * @return The justification of the text strings.      */
specifier|public
name|int
name|getQ
parameter_list|()
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
name|COSNumber
name|number
init|=
operator|(
name|COSNumber
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|Q
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|number
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// the Q value is inheritable
comment|// the acroform should provide a Q default value
name|retval
operator|=
name|getAcroForm
argument_list|()
operator|.
name|getQ
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the quadding/justification of the text.  See QUADDING constants.      *      * @param q The new text justification.      */
specifier|public
name|void
name|setQ
parameter_list|(
name|int
name|q
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getDefaultValue
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
comment|// Text fields don't support the "DV" entry.
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Text fields don't support the \"DV\" entry."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

