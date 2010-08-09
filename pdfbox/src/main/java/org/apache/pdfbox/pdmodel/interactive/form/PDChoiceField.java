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
name|cos
operator|.
name|COSString
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
name|PDAcroForm
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
comment|/**  * A class for handling the PDF field as a choicefield.  *  * @author sug  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|PDChoiceField
extends|extends
name|PDVariableText
block|{
comment|/**      * @see org.apache.pdfbox.pdmodel.interactive.form.PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroForm for this field.      * @param field The field for this choice field.      */
specifier|public
name|PDChoiceField
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.pdfbox.pdmodel.interactive.form.PDField#setValue(java.lang.String)      *      * @param optionValue The new value for this text field.      *      * @throws IOException If there is an error calculating the appearance stream or the value in not one      *   of the existing options.      */
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
name|int
name|indexSelected
init|=
operator|-
literal|1
decl_stmt|;
name|COSArray
name|options
init|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Opt"
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: You cannot set a value for a choice field if there are no options."
argument_list|)
throw|;
block|}
else|else
block|{
comment|// YXJ: Changed the order of the loops. Acrobat produces PDF's
comment|// where sometimes there is 1 string and the rest arrays.
comment|// This code works either way.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|options
operator|.
name|size
argument_list|()
operator|&&
name|indexSelected
operator|==
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|option
init|=
name|options
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|keyValuePair
init|=
operator|(
name|COSArray
operator|)
name|option
decl_stmt|;
name|COSString
name|key
init|=
operator|(
name|COSString
operator|)
name|keyValuePair
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSString
name|value
init|=
operator|(
name|COSString
operator|)
name|keyValuePair
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|optionValue
operator|.
name|equals
argument_list|(
name|key
operator|.
name|getString
argument_list|()
argument_list|)
operator|||
name|optionValue
operator|.
name|equals
argument_list|(
name|value
operator|.
name|getString
argument_list|()
argument_list|)
condition|)
block|{
comment|//have the parent draw the appearance stream with the value
name|super
operator|.
name|setValue
argument_list|(
name|value
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
comment|//but then use the key as the V entry
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"V"
argument_list|)
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|indexSelected
operator|=
name|i
expr_stmt|;
block|}
block|}
else|else
block|{
name|COSString
name|value
init|=
operator|(
name|COSString
operator|)
name|option
decl_stmt|;
if|if
condition|(
name|optionValue
operator|.
name|equals
argument_list|(
name|value
operator|.
name|getString
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|setValue
argument_list|(
name|optionValue
argument_list|)
expr_stmt|;
name|indexSelected
operator|=
name|i
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|indexSelected
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: '"
operator|+
name|optionValue
operator|+
literal|"' was not an available option."
argument_list|)
throw|;
block|}
else|else
block|{
name|COSArray
name|indexArray
init|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"I"
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexArray
operator|!=
literal|null
condition|)
block|{
name|indexArray
operator|.
name|clear
argument_list|()
expr_stmt|;
name|indexArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|indexSelected
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

