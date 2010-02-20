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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * A class for handling the PDF field as a checkbox.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author sug  * @version $Revision: 1.11 $  */
end_comment

begin_class
specifier|public
class|class
name|PDCheckbox
extends|extends
name|PDChoiceButton
block|{
specifier|private
specifier|static
specifier|final
name|COSName
name|KEY
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"AS"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|COSName
name|OFF_VALUE
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Off"
argument_list|)
decl_stmt|;
specifier|private
name|COSName
name|value
decl_stmt|;
comment|/**      * @see PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroForm for this field.      * @param field The checkbox field dictionary      */
specifier|public
name|PDCheckbox
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
name|COSDictionary
name|ap
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"AP"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|!=
literal|null
condition|)
block|{
name|COSBase
name|n
init|=
name|ap
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
argument_list|)
decl_stmt|;
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
name|name
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
name|name
operator|.
name|equals
argument_list|(
name|OFF_VALUE
argument_list|)
condition|)
block|{
name|value
operator|=
name|name
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|value
operator|=
operator|(
name|COSName
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"V"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if this radio button is currently checked or not.      *      * @return true If the radio button is checked.      */
specifier|public
name|boolean
name|isChecked
parameter_list|()
block|{
name|boolean
name|retval
init|=
literal|false
decl_stmt|;
name|String
name|onValue
init|=
name|getOnValue
argument_list|()
decl_stmt|;
name|COSName
name|radioValue
init|=
operator|(
name|COSName
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|radioValue
operator|!=
literal|null
operator|&&
name|value
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
name|retval
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Checks the radiobutton.      */
specifier|public
name|void
name|check
parameter_list|()
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|KEY
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unchecks the radiobutton.      */
specifier|public
name|void
name|unCheck
parameter_list|()
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|KEY
argument_list|,
name|OFF_VALUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
literal|"V"
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
if|if
condition|(
name|newValue
operator|==
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|KEY
argument_list|,
name|OFF_VALUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|KEY
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the value of the radio button.      *      * @return The value of the radio button.      */
specifier|public
name|String
name|getOffValue
parameter_list|()
block|{
return|return
name|OFF_VALUE
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will get the value of the radio button.      *      * @return The value of the radio button.      */
specifier|public
name|String
name|getOnValue
parameter_list|()
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|ap
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
name|getPDFName
argument_list|(
literal|"AP"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
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
name|OFF_VALUE
argument_list|)
condition|)
block|{
name|retval
operator|=
name|key
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * getValue gets the fields value to as a string.      *      * @return The string value of this field.      *      * @throws IOException If there is an error getting the value.      */
specifier|public
name|String
name|getValue
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"V"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

