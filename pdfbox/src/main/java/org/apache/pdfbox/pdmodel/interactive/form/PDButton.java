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
name|COSArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * A button field represents an interactive control on the screen  * that the user can manipulate with the mouse.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDButton
extends|extends
name|PDField
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
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_RADIO
init|=
literal|1
operator|<<
literal|15
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_PUSHBUTTON
init|=
literal|1
operator|<<
literal|16
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_RADIOS_IN_UNISON
init|=
literal|1
operator|<<
literal|25
decl_stmt|;
comment|/**      * @see PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroform.      */
name|PDButton
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
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parentNode the parent node of the node to be created      */
specifier|protected
name|PDButton
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
comment|/**      * This will get the option values "Opt" entry of the pdf button.      *      * @return A list of java.lang.String values.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|strings
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|strings
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the field options values.      *       * The fields options represent the export value of each annotation in the field.       * It may be used to:      *<ul>      *<li>represent the export values in non-Latin writing systems.</li>      *<li>allow radio buttons to be checked independently, even       *  if they have the same export value.</li>      *</ul>      *       * Providing an empty list or null will remove the entry.      *       * @param options The list of options for the button.      */
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|options
parameter_list|)
block|{
if|if
condition|(
name|options
operator|==
literal|null
operator|||
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

