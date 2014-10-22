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
name|COSInteger
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
name|action
operator|.
name|PDFormFieldAdditionalActions
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
name|util
operator|.
name|appearance
operator|.
name|AppearanceGenerator
import|;
end_import

begin_comment
comment|/**  * A field in an interactive form.  * Fields may be one of four types: button, text, choice, or signature.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDField
extends|extends
name|PDFieldTreeNode
block|{
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      */
specifier|protected
name|PDField
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
name|PDField
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
comment|/**      * This will return a string representation of this field.      *       * @return A string representation of this field.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
return|;
block|}
comment|/**      * Get the additional actions for this field. This will return null if there are no additional actions for this      * field.      *       * @return The actions of the field.      */
specifier|public
name|PDFormFieldAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSDictionary
name|aa
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
name|AA
argument_list|)
decl_stmt|;
name|PDFormFieldAdditionalActions
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|aa
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFormFieldAdditionalActions
argument_list|(
name|aa
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the actions of the field.      *       * @param actions The field actions.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDFormFieldAdditionalActions
name|actions
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|actions
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|getFieldFlags
parameter_list|()
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
name|COSInteger
name|ff
init|=
operator|(
name|COSInteger
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|ff
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|getParent
argument_list|()
operator|.
name|getFieldFlags
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|getFieldType
parameter_list|()
block|{
name|String
name|fieldType
init|=
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldType
operator|==
literal|null
operator|&&
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fieldType
operator|=
name|getParent
argument_list|()
operator|.
name|getFieldType
argument_list|()
expr_stmt|;
block|}
return|return
name|fieldType
return|;
block|}
comment|/**      * Update the fields appearance stream.      *       * The fields appearance stream needs to be updated to reflect the new field      * value. This will be done only if the NeedAppearances flag has not been set.      */
specifier|protected
name|void
name|updateFieldAppearances
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getAcroForm
argument_list|()
operator|.
name|isNeedAppearances
argument_list|()
condition|)
block|{
name|AppearanceGenerator
operator|.
name|generateFieldAppearances
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

