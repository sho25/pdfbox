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
comment|/**  * A text field is a box or space for text fill-in data typically entered from a keyboard.  * The text may be restricted to a single line or may be permitted to span multiple lines  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDTextField
extends|extends
name|PDVariableText
block|{
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
comment|/**      * @see PDField#PDField(PDAcroForm)      *      * @param acroForm The acroform.      */
specifier|public
name|PDTextField
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
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FT
argument_list|,
name|COSName
operator|.
name|TX
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDTextField
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
comment|/**      * @return true if the field is multiline      */
specifier|public
name|boolean
name|isMultiline
parameter_list|()
block|{
return|return
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
name|dictionary
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
comment|/**      * Returns the maximum number of characters of the text field.      *       * @return the maximum number of characters, returns -1 if the value isn't present      */
specifier|public
name|int
name|getMaxLen
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|MAX_LEN
argument_list|)
return|;
block|}
comment|/**      * Sets the maximum number of characters of the text field.      *       * @param maxLen the maximum number of characters      */
specifier|public
name|void
name|setMaxLen
parameter_list|(
name|int
name|maxLen
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|MAX_LEN
argument_list|,
name|maxLen
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the plain text value of this field.      *       * @param value Plain text      * @throws IOException if the value could not be set      */
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
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|applyChange
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the default value of this field.      *      * @param value Plain text      * @throws IOException if the value could not be set      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DV
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the value of this field, or an empty string.      *       * @return A non-null string.      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|getStringOrStream
argument_list|(
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns the default value of this field, or an empty string.      *      * @return A non-null string.      */
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|getStringOrStream
argument_list|(
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
argument_list|)
return|;
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
annotation|@
name|Override
name|void
name|constructAppearances
parameter_list|()
throws|throws
name|IOException
block|{
name|AppearanceGeneratorHelper
name|apHelper
decl_stmt|;
name|apHelper
operator|=
operator|new
name|AppearanceGeneratorHelper
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|apHelper
operator|.
name|setAppearanceValue
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

