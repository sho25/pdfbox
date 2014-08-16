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
name|java
operator|.
name|io
operator|.
name|IOException
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
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_EDIT
init|=
literal|0x40000
decl_stmt|;
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
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
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|optionValue
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|isEditable
init|=
operator|(
name|getFieldFlags
argument_list|()
operator|&
name|FLAG_EDIT
operator|)
operator|!=
literal|0
decl_stmt|;
name|int
name|index
init|=
name|getSelectedIndex
argument_list|(
name|optionValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
operator|&&
operator|!
name|isEditable
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Combo box does not contain the given value"
argument_list|)
throw|;
block|}
name|super
operator|.
name|setValue
argument_list|(
name|optionValue
argument_list|)
expr_stmt|;
name|selectMultiple
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

