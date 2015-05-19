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
name|COSStream
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
name|PDResources
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
name|PDTerminalField
block|{
specifier|static
specifier|final
name|int
name|QUADDING_LEFT
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|QUADDING_CENTERED
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|QUADDING_RIGHT
init|=
literal|2
decl_stmt|;
comment|/**      * @see PDTerminalField#PDTerminalField(PDAcroForm)      *      * @param acroForm The acroform.      */
name|PDVariableText
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
name|PDVariableText
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
comment|/**      * Get the default appearance.      *       * This is an inheritable attribute.      *       * The default appearance contains a set of default graphics and text operators      * to define the field’s text size and color.      *       * @return the DA element of the dictionary object      */
specifier|public
name|String
name|getDefaultAppearance
parameter_list|()
block|{
name|COSString
name|defaultAppearance
init|=
operator|(
name|COSString
operator|)
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
decl_stmt|;
return|return
name|defaultAppearance
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * Get the default appearance.      *      * This is an inheritable attribute.      *      * The default appearance contains a set of default graphics and text operators      * to define the field’s text size and color.      *      * @return the DA element of the dictionary object      */
name|PDAppearanceString
name|getDefaultAppearanceString
parameter_list|()
throws|throws
name|IOException
block|{
name|COSString
name|da
init|=
operator|(
name|COSString
operator|)
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
decl_stmt|;
name|PDResources
name|dr
init|=
name|getAcroForm
argument_list|()
operator|.
name|getDefaultResources
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDAppearanceString
argument_list|(
name|da
argument_list|,
name|dr
argument_list|)
return|;
block|}
comment|/**      * Set the default appearance.      *       * This will set the local default appearance for the variable text field only not       * affecting a default appearance in the parent hierarchy.      *       * Providing null as the value will remove the local default appearance.      *       * @param daValue a string describing the default appearance      */
specifier|public
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|daValue
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|,
name|daValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default style string.      *       * The default style string defines the default style for      * rich text fields.      *       * @return the DS element of the dictionary object      */
specifier|public
name|String
name|getDefaultStyleString
parameter_list|()
block|{
name|COSString
name|defaultStyleString
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DS
argument_list|)
decl_stmt|;
return|return
name|defaultStyleString
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * Set the default style string.      *       * Providing null as the value will remove the default style string.      *       * @param defaultStyleString a string describing the default style.      */
specifier|public
name|void
name|setDefaultStyleString
parameter_list|(
name|String
name|defaultStyleString
parameter_list|)
block|{
if|if
condition|(
name|defaultStyleString
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DS
argument_list|,
operator|new
name|COSString
argument_list|(
name|defaultStyleString
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dictionary
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|DS
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the 'quadding' or justification of the text to be displayed.      *       * This is an inheritable attribute.      *       * 0 - Left(default)<br/>      * 1 - Centered<br />      * 2 - Right<br />      * Please see the QUADDING_CONSTANTS.      *      * @return The justification of the text strings.      */
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
name|getInheritableAttribute
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
name|dictionary
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
comment|/**      * Get the fields rich text value.      *       * @return the rich text value string      * @throws IOException if the field dictionary entry is not a text type      */
specifier|public
name|String
name|getRichTextValue
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getStringOrStream
argument_list|(
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|RV
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set the fields rich text value.      *       *<p>      * Setting the rich text value will not generate the appearance      * for the field.      *<br/>      * You can set {@link PDAcroForm#setNeedAppearances(Boolean)} to      * signal a conforming reader to generate the appearance stream.      *</p>      *       * Providing null as the value will remove the default style string.      *       * @param richTextValue a rich text string      */
specifier|public
name|void
name|setRichTextValue
parameter_list|(
name|String
name|richTextValue
parameter_list|)
block|{
if|if
condition|(
name|richTextValue
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RV
argument_list|,
operator|new
name|COSString
argument_list|(
name|richTextValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dictionary
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|RV
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get a text as text stream.      *      * Some dictionary entries allow either a text or a text stream.      *      * @param base the potential text or text stream      * @return the text stream      */
specifier|protected
specifier|final
name|String
name|getStringOrStream
parameter_list|(
name|COSBase
name|base
parameter_list|)
block|{
if|if
condition|(
name|base
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
name|base
operator|instanceof
name|COSString
condition|)
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|base
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|(
operator|(
name|COSStream
operator|)
name|base
operator|)
operator|.
name|getString
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
block|}
end_class

end_unit

