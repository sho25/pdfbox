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

begin_comment
comment|/**  * A scrollable list box. Contains several text items, one or more of which shall be selected as the field value.  *   * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDListBox
extends|extends
name|PDChoice
block|{
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|public
name|PDListBox
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
comment|/**      * getValue gets the value of the "V" entry.      *       * @return The value of this entry.      *       */
annotation|@
name|Override
specifier|public
name|COSArray
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
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|value
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
operator|(
name|COSArray
operator|)
name|value
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * setValue sets the entry "V" to the given value.      *       * @param value the value      *       */
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
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
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|int
name|index
init|=
name|getSelectedIndex
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The combo box does not contain the given value."
argument_list|)
throw|;
block|}
name|selectMultiple
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|String
index|[]
condition|)
block|{
comment|// TODO multiple values
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
comment|/**      * This will get the top index "TI" value.      *      * @return the top index, default value 0.      */
specifier|public
name|int
name|getTopIndex
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|TI
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set top index "TI" value.      *      * @param topIndex the value for the top index, null will remove the value.      */
specifier|public
name|void
name|setTopIndex
parameter_list|(
name|Integer
name|topIndex
parameter_list|)
block|{
if|if
condition|(
name|topIndex
operator|!=
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|TI
argument_list|,
name|topIndex
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
name|TI
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

