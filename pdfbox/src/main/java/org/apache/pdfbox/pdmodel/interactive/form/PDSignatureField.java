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
name|HashSet
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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

begin_comment
comment|/**  * A signature field is a form field that contains a digital signature.  *  * @author Ben Litchfield  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|PDSignatureField
extends|extends
name|PDTerminalField
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDSignatureField
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @see PDTerminalField#PDTerminalField(PDAcroForm)      *      * @param acroForm The acroForm for this field.      */
specifier|public
name|PDSignatureField
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
name|getCOSObject
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
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setLocked
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node to be created      */
name|PDSignatureField
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
comment|/**      * Generate a unique name for the signature.      *       * @return the signature's unique name      */
specifier|private
name|String
name|generatePartialName
parameter_list|()
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|field
range|:
name|getAcroForm
argument_list|()
operator|.
name|getFieldTree
argument_list|()
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
comment|/**      * Sets the value of this field to be the given signature.      *       * @param value is the PDSignatureField      */
specifier|public
name|void
name|setValue
parameter_list|(
name|PDSignature
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|applyChange
argument_list|()
expr_stmt|;
block|}
comment|/**      *<b>This will throw an UnsupportedOperationException if used as the signature fields      * value can't be set using a String</b>      *       * @param value the plain text value.      *       * @throws UnsupportedOperationException in all cases!      */
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Signature fields don't support setting the value as String "
operator|+
literal|"- use setValue(PDSignature value) instead"
argument_list|)
throw|;
block|}
comment|/**      * Sets the default value of this field to be the given signature.      *      * @param value is the PDSignatureField      */
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|PDSignature
name|value
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DV
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the signature contained in this field.      *       * @return A signature dictionary.      */
specifier|public
name|PDSignature
name|getValue
parameter_list|()
block|{
name|COSBase
name|value
init|=
name|getCOSObject
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
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDSignature
argument_list|(
operator|(
name|COSDictionary
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the default value, if any.      *      * @return A signature dictionary.      */
specifier|public
name|PDSignature
name|getDefaultValue
parameter_list|()
block|{
name|COSBase
name|value
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
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
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getValueAsString
parameter_list|()
block|{
name|PDSignature
name|signature
init|=
name|getValue
argument_list|()
decl_stmt|;
return|return
name|signature
operator|!=
literal|null
condition|?
name|signature
operator|.
name|toString
argument_list|()
else|:
literal|""
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
name|getCOSObject
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
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SV
argument_list|,
name|sv
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
name|void
name|constructAppearances
parameter_list|()
throws|throws
name|IOException
block|{
name|PDAnnotationWidget
name|widget
init|=
name|this
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|widget
operator|!=
literal|null
condition|)
block|{
comment|// check if the signature is visible
if|if
condition|(
name|widget
operator|.
name|getRectangle
argument_list|()
operator|==
literal|null
operator|||
name|Float
operator|.
name|compare
argument_list|(
name|widget
operator|.
name|getRectangle
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|==
literal|0
operator|&&
name|Float
operator|.
name|compare
argument_list|(
name|widget
operator|.
name|getRectangle
argument_list|()
operator|.
name|getWidth
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|==
literal|0
operator|||
name|widget
operator|.
name|isNoView
argument_list|()
operator|||
name|widget
operator|.
name|isHidden
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// TODO: implement appearance generation for signatures
name|LOG
operator|.
name|warn
argument_list|(
literal|"Appearance generation for signature fields not yet implemented - you need to generate/update that manually"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

