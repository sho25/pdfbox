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

begin_comment
comment|/**  * A non terminal field in an interactive form.  *   * A non terminal field is a node in the fields tree node whose descendants  * are fields.   *   * The attributes such as FT (field type) or V (field value) do not logically  * belong to the non terminal field but are inheritable attributes  * for descendant terminal fields.   *   */
end_comment

begin_class
specifier|public
class|class
name|PDNonTerminalField
extends|extends
name|PDFieldTreeNode
block|{
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      */
specifier|public
name|PDNonTerminalField
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
specifier|public
name|PDNonTerminalField
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
comment|// There is no need to look up the parent hierarchy within a non terminal field
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
comment|// There is no need to look up the parent hierarchy within a non terminal field
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
comment|// There is no need to look up the parent hierarchy within a non terminal field
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|fieldValue
parameter_list|)
block|{
comment|// There is no need to look up the parent hierarchy within a non terminal field
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|fieldValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Object
name|getDefaultValue
parameter_list|()
block|{
comment|// There is no need to look up the parent hierarchy within a non terminal field
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
comment|// There is no need to look up the parent hierarchy within a non terminal field
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

