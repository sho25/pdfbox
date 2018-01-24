begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotation
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
name|handlers
operator|.
name|PDAppearanceHandler
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
name|handlers
operator|.
name|PDFreeTextAppearanceHandler
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
name|PDVariableText
import|;
end_import

begin_comment
comment|/**  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationFreeText
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"FreeText"
decl_stmt|;
specifier|private
name|PDAppearanceHandler
name|freeTextAppearanceHandler
decl_stmt|;
specifier|public
name|PDAnnotationFreeText
parameter_list|()
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a FreeText annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationFreeText
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default appearance.      *       * @return a string describing the default appearance.      */
specifier|public
name|String
name|getDefaultAppearance
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
return|;
block|}
comment|/**      * Set the default appearance.      *      * @param daValue a string describing the default appearance.      */
specifier|public
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|daValue
parameter_list|)
block|{
name|getCOSObject
argument_list|()
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
comment|/**      * Get the default style string.      *      * The default style string defines the default style for rich text fields.      *      * @return the DS element of the dictionary object      */
specifier|public
name|String
name|getDefaultStyleString
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|)
return|;
block|}
comment|/**      * Set the default style string.      *      * Providing null as the value will remove the default style string.      *      * @param defaultStyleString a string describing the default style.      */
specifier|public
name|void
name|setDefaultStyleString
parameter_list|(
name|String
name|defaultStyleString
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|,
name|defaultStyleString
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the 'quadding' or justification of the text to be displayed.      *<br>      * 0 - Left (default)<br>      * 1 - Centered<br>      * 2 - Right<br>      * Please see the QUADDING_CONSTANTS in {@link PDVariableText }.      *      * @return The justification of the text strings.      */
specifier|public
name|int
name|getQ
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the quadding/justification of the text. Please see the QUADDING_CONSTANTS      * in {@link PDVariableText }.      *      * @param q The new text justification.      */
specifier|public
name|void
name|setQ
parameter_list|(
name|int
name|q
parameter_list|)
block|{
name|getCOSObject
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
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param freeTextAppearanceHandler      */
specifier|public
name|void
name|setCustomFreeTextAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|freeTextAppearanceHandler
parameter_list|)
block|{
name|this
operator|.
name|freeTextAppearanceHandler
operator|=
name|freeTextAppearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|()
block|{
if|if
condition|(
name|freeTextAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDFreeTextAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDFreeTextAppearanceHandler
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|appearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|freeTextAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

