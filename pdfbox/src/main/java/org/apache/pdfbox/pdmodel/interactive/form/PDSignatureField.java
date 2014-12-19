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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
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
name|digitalsignature
operator|.
name|PDSeedValue
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
name|digitalsignature
operator|.
name|PDSignature
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A signature field is a form field that contains a digital signature.  *  * @author Ben Litchfield  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|PDSignatureField
extends|extends
name|PDField
block|{
comment|/**      * Constructor.      *       * @param theAcroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|public
name|PDSignatureField
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
comment|// dirty hack to avoid npe caused through getWidget() method
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|ANNOT
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|PDAnnotationWidget
operator|.
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see PDField#PDField(PDAcroForm)      *      * @param theAcroForm The acroForm for this field.      * @throws IOException If there is an error while resolving partial name for the signature field      *         or getting the widget object.      */
specifier|public
name|PDSignatureField
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FT
argument_list|,
name|COSName
operator|.
name|SIG
argument_list|)
expr_stmt|;
name|getWidget
argument_list|()
operator|.
name|setLocked
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getWidget
argument_list|()
operator|.
name|setPrinted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setPartialName
argument_list|(
name|generatePartialName
argument_list|()
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|ANNOT
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|PDAnnotationWidget
operator|.
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generate a unique name for the signature.      * @return the signature's unique name      */
specifier|private
name|String
name|generatePartialName
parameter_list|()
block|{
name|PDAcroForm
name|acroForm
init|=
name|getAcroForm
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDFieldTreeNode
argument_list|>
name|fields
init|=
name|acroForm
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|String
name|fieldName
init|=
literal|"Signature"
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|sigNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PDFieldTreeNode
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|field
operator|instanceof
name|PDSignatureField
condition|)
block|{
name|sigNames
operator|.
name|add
argument_list|(
name|field
operator|.
name|getPartialName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|i
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|sigNames
operator|.
name|contains
argument_list|(
name|fieldName
operator|+
name|i
argument_list|)
condition|)
block|{
operator|++
name|i
expr_stmt|;
block|}
return|return
name|fieldName
operator|+
name|i
return|;
block|}
comment|/**      * Return a string rep of this object.      *      * @return A string rep of this object.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PDSignatureField"
return|;
block|}
comment|/**      * Add a signature dictionary to the signature field.      *       * @param value is the PDSignatureField      */
specifier|public
name|void
name|setSignature
parameter_list|(
name|PDSignature
name|value
parameter_list|)
block|{
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the signature dictionary.      *       * @return the signature dictionary      *       */
specifier|public
name|PDSignature
name|getSignature
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
return|;
block|}
comment|/**      * Add a signature dictionary to the signature field.      *       * @param value is the PDSignatureField      */
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
name|value
operator|==
literal|null
condition|)
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
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|PDSignature
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
operator|(
name|PDSignature
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the signature dictionary.      *       * @return the signature dictionary      *       */
specifier|public
name|PDSignature
name|getValue
parameter_list|()
block|{
name|COSBase
name|dictionary
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
name|dictionary
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|PDSignature
argument_list|(
operator|(
name|COSDictionary
operator|)
name|dictionary
argument_list|)
return|;
block|}
comment|/**      *<p>(Optional; PDF 1.5) A seed value dictionary containing information      * that constrains the properties of a signature that is applied to the      * field.</p>      *      * @return the seed value dictionary as PDSeedValue      */
specifier|public
name|PDSeedValue
name|getSeedValue
parameter_list|()
block|{
name|COSDictionary
name|dict
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
name|SV
argument_list|)
decl_stmt|;
name|PDSeedValue
name|sv
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|sv
operator|=
operator|new
name|PDSeedValue
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|sv
return|;
block|}
comment|/**      *<p>(Optional; PDF 1.) A seed value dictionary containing information      * that constrains the properties of a signature that is applied to the      * field.</p>      *      * @param sv is the seed value dictionary as PDSeedValue      */
specifier|public
name|void
name|setSeedValue
parameter_list|(
name|PDSeedValue
name|sv
parameter_list|)
block|{
if|if
condition|(
name|sv
operator|!=
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SV
argument_list|,
name|sv
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getDefaultValue
parameter_list|()
block|{
comment|// Signature fields don't support the "DV" entry.
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
comment|// Signature fields don't support the "DV" entry.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Signature fields don't support the \"DV\" entry."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

