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
name|COSName
import|;
end_import

begin_comment
comment|/**  * A combo box consisting of a drop-down list.  * May be accompanied by an editable text box in which non-predefined values may be entered.  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDComboBox
extends|extends
name|PDChoice
block|{
comment|/**      *  Ff-flag.      */
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_EDIT
init|=
literal|1
operator|<<
literal|18
decl_stmt|;
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|public
name|PDComboBox
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
comment|/**      * Determines if Edit is set.      *       * @return true if the combo box shall include an editable text box as well as a drop-down list.      */
specifier|public
name|boolean
name|isEdit
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
name|FLAG_EDIT
argument_list|)
return|;
block|}
comment|/**      * Set the Edit bit.      *      * @param edit The value for Edit.      */
specifier|public
name|void
name|setEdit
parameter_list|(
name|boolean
name|edit
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
name|FLAG_EDIT
argument_list|,
name|edit
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
operator|(
name|getFieldFlags
argument_list|()
operator|&
name|FLAG_EDIT
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The combo box isn't editable."
argument_list|)
throw|;
block|}
name|super
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * setValue sets the entry "V" to the given value.      *       * @param value the value      *       */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
index|[]
name|value
parameter_list|)
block|{
if|if
condition|(
operator|(
name|getFieldFlags
argument_list|()
operator|&
name|FLAG_EDIT
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The combo box isn't editable."
argument_list|)
throw|;
block|}
name|super
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

