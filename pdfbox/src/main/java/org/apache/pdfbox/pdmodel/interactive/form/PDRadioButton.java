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
name|java
operator|.
name|util
operator|.
name|List
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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * Radio button fields contain a set of related buttons that can each be on or off.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDRadioButton
extends|extends
name|PDButton
block|{
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_NO_TOGGLE_TO_OFF
init|=
literal|1
operator|<<
literal|14
decl_stmt|;
comment|/**      * @see PDFieldTreeNode#PDFieldTreeNode(PDAcroForm)      *      * @param theAcroForm The acroform.      */
specifier|public
name|PDRadioButton
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
name|PDRadioButton
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
comment|/**      * From the PDF Spec<br/>      * If set, a group of radio buttons within a radio button field that use the same value for the on state will turn      * on and off in unison; that is if one is checked, they are all checked. If clear, the buttons are mutually      * exclusive (the same behavior as HTML radio buttons).      *      * @param radiosInUnison The new flag for radiosInUnison.      */
specifier|public
name|void
name|setRadiosInUnison
parameter_list|(
name|boolean
name|radiosInUnison
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
name|FLAG_RADIOS_IN_UNISON
argument_list|,
name|radiosInUnison
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true If the flag is set for radios in unison.      */
specifier|public
name|boolean
name|isRadiosInUnison
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
name|FLAG_RADIOS_IN_UNISON
argument_list|)
return|;
block|}
comment|/**      * This will get the export value.      *<p>      * A RadioButton might have an export value to allow field values      * which can not be encoded as PDFDocEncoding or for the same export value       * being assigned to multiple RadioButtons in a group.<br/>      * To define an export value the RadioButton must define options {@link #setOptions(List)}      * which correspond to the individual items within the RadioButton.</p>      *<p>      * The method will either return the value from the options entry or in case there      * is no such entry the fields value</p>      *       * @return the export value of the field.      * @throws IOException in case the fields value can not be retrieved      */
specifier|public
name|String
name|getExportValue
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
name|getOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
name|String
name|fieldValue
init|=
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|COSObjectable
argument_list|>
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|COSObjectable
name|kid
range|:
name|kids
control|)
block|{
if|if
condition|(
name|kid
operator|instanceof
name|PDCheckbox
condition|)
block|{
name|PDCheckbox
name|btn
init|=
operator|(
name|PDCheckbox
operator|)
name|kid
decl_stmt|;
if|if
condition|(
name|btn
operator|.
name|getOnValue
argument_list|()
operator|.
name|equals
argument_list|(
name|fieldValue
argument_list|)
condition|)
block|{
break|break;
block|}
name|idx
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|idx
operator|<=
name|options
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|options
operator|.
name|get
argument_list|(
name|idx
argument_list|)
return|;
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
comment|/**      * Set the field value.      *       * The field value holds a name object which is corresponding to the       * appearance state of the child field being in the on state.      *       * The default value is Off.      *       * @param fieldValue the COSName object to set the field value.      */
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
if|if
condition|(
name|fieldValue
operator|==
literal|null
condition|)
block|{
name|removeInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|fieldValue
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|COSObjectable
argument_list|>
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
for|for
control|(
name|COSObjectable
name|kid
range|:
name|kids
control|)
block|{
if|if
condition|(
name|kid
operator|instanceof
name|PDCheckbox
condition|)
block|{
name|PDCheckbox
name|btn
init|=
operator|(
name|PDCheckbox
operator|)
name|kid
decl_stmt|;
if|if
condition|(
name|btn
operator|.
name|getOnValue
argument_list|()
operator|.
name|equals
argument_list|(
name|fieldValue
argument_list|)
condition|)
block|{
name|btn
operator|.
name|check
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|btn
operator|.
name|unCheck
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

