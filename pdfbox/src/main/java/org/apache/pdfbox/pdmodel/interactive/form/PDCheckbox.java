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

begin_comment
comment|/**  * A check box toggles between two states, on and off.  *  * @author Ben Litchfield  * @author sug  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDCheckbox
extends|extends
name|PDButton
block|{
comment|/**      * @see PDFieldTreeNode#PDFieldTreeNode(PDAcroForm)      *      * @param theAcroForm The acroform.      */
specifier|public
name|PDCheckbox
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
name|PDCheckbox
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
comment|/**      * This will tell if this radio button is currently checked or not.      *      * @return true If the radio button is checked.      * @throws IOException       */
specifier|public
name|boolean
name|isChecked
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|onValue
init|=
name|getOnValue
argument_list|()
decl_stmt|;
name|String
name|fieldValue
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fieldValue
operator|=
name|getValue
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// getting there means that the field value
comment|// doesn't have a supported type.
comment|// Ignoring as that will also mean that the field is not checked.
comment|// Setting the value explicitly as Code Analysis (Sonar) doesn't like
comment|// empty catch blocks.
return|return
literal|false
return|;
block|}
name|COSName
name|radioValue
init|=
operator|(
name|COSName
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
decl_stmt|;
if|if
condition|(
name|radioValue
operator|!=
literal|null
operator|&&
name|fieldValue
operator|!=
literal|null
operator|&&
name|radioValue
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|onValue
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Checks the check box.      */
specifier|public
name|void
name|check
parameter_list|()
block|{
name|String
name|onValue
init|=
name|getOnValue
argument_list|()
decl_stmt|;
name|setValue
argument_list|(
name|onValue
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|onValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unchecks the check box.      */
specifier|public
name|void
name|unCheck
parameter_list|()
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|PDButton
operator|.
name|OFF
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the value assigned to the OFF state.      *      * @return The value of the check box.      */
specifier|public
name|String
name|getOffValue
parameter_list|()
block|{
return|return
name|PDButton
operator|.
name|OFF
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will get the value assigned to the ON state.      *      * @return The value of the check box.      */
specifier|public
name|String
name|getOnValue
parameter_list|()
block|{
name|COSDictionary
name|ap
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
decl_stmt|;
name|COSBase
name|n
init|=
name|ap
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
comment|//N can be a COSDictionary or a COSStream
if|if
condition|(
name|n
operator|instanceof
name|COSDictionary
condition|)
block|{
for|for
control|(
name|COSName
name|key
range|:
operator|(
operator|(
name|COSDictionary
operator|)
name|n
operator|)
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|equals
argument_list|(
name|PDButton
operator|.
name|OFF
argument_list|)
condition|)
block|{
return|return
name|key
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|""
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getValue
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|attribute
init|=
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
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
name|attribute
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|attribute
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected a COSName entry but got "
operator|+
name|attribute
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Set the field value.      *       * The field value holds a name object which is corresponding to the       * appearance state representing the corresponding appearance       * from the appearance directory.      *      * The default value is Off.      *       * @param value the new field value value.      */
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
operator|==
literal|null
condition|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|PDButton
operator|.
name|OFF
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSName
name|nameValue
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|nameValue
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|nameValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

