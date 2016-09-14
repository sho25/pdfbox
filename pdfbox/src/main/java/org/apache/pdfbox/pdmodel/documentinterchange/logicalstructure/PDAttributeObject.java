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
name|documentinterchange
operator|.
name|logicalstructure
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
name|common
operator|.
name|PDDictionaryWrapper
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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDExportFormatAttributeObject
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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDLayoutAttributeObject
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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDListAttributeObject
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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDPrintFieldAttributeObject
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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDTableAttributeObject
import|;
end_import

begin_comment
comment|/**  * An attribute object.  *  * @author Johannes Koch  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAttributeObject
extends|extends
name|PDDictionaryWrapper
block|{
comment|/**      * Creates an attribute object.      *       * @param dictionary the dictionary      * @return the attribute object      */
specifier|public
specifier|static
name|PDAttributeObject
name|create
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|String
name|owner
init|=
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|O
argument_list|)
decl_stmt|;
if|if
condition|(
name|PDUserAttributeObject
operator|.
name|OWNER_USER_PROPERTIES
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDUserAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDListAttributeObject
operator|.
name|OWNER_LIST
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDListAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDPrintFieldAttributeObject
operator|.
name|OWNER_PRINT_FIELD
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDPrintFieldAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDTableAttributeObject
operator|.
name|OWNER_TABLE
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDTableAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDLayoutAttributeObject
operator|.
name|OWNER_LAYOUT
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDLayoutAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDExportFormatAttributeObject
operator|.
name|OWNER_XML_1_00
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_HTML_3_20
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_HTML_4_01
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_OEB_1_00
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_RTF_1_05
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_CSS_1_00
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
operator|||
name|PDExportFormatAttributeObject
operator|.
name|OWNER_CSS_2_00
operator|.
name|equals
argument_list|(
name|owner
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDExportFormatAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
return|return
operator|new
name|PDDefaultAttributeObject
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
specifier|private
name|PDStructureElement
name|structureElement
decl_stmt|;
comment|/**      * Gets the structure element.      *       * @return the structure element      */
specifier|private
name|PDStructureElement
name|getStructureElement
parameter_list|()
block|{
return|return
name|this
operator|.
name|structureElement
return|;
block|}
comment|/**      * Sets the structure element.      *       * @param structureElement the structure element      */
specifier|protected
name|void
name|setStructureElement
parameter_list|(
name|PDStructureElement
name|structureElement
parameter_list|)
block|{
name|this
operator|.
name|structureElement
operator|=
name|structureElement
expr_stmt|;
block|}
comment|/**      * Default constructor.      */
specifier|public
name|PDAttributeObject
parameter_list|()
block|{     }
comment|/**      * Creates a new attribute object with a given dictionary.      *       * @param dictionary the dictionary      */
specifier|public
name|PDAttributeObject
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the owner of the attributes.      *       * @return the owner of the attributes      */
specifier|public
name|String
name|getOwner
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|O
argument_list|)
return|;
block|}
comment|/**      * Sets the owner of the attributes.      *       * @param owner the owner of the attributes      */
specifier|protected
name|void
name|setOwner
parameter_list|(
name|String
name|owner
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|O
argument_list|,
name|owner
argument_list|)
expr_stmt|;
block|}
comment|/**      * Detects whether there are no properties in the attribute object.      *       * @return<code>true</code> if the attribute object is empty,      *<code>false</code> otherwise      */
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
comment|// only entry is the owner?
return|return
operator|(
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|)
operator|&&
operator|(
name|this
operator|.
name|getOwner
argument_list|()
operator|!=
literal|null
operator|)
return|;
block|}
comment|/**      * Notifies the attribute object change listeners if the attribute is changed.      *       * @param oldBase old value      * @param newBase new value      */
specifier|protected
name|void
name|potentiallyNotifyChanged
parameter_list|(
name|COSBase
name|oldBase
parameter_list|,
name|COSBase
name|newBase
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|isValueChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
condition|)
block|{
name|this
operator|.
name|notifyChanged
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Is the value changed?      *       * @param oldValue old value      * @param newValue new value      * @return<code>true</code> if the value is changed,<code>false</code>      * otherwise      */
specifier|private
name|boolean
name|isValueChanged
parameter_list|(
name|COSBase
name|oldValue
parameter_list|,
name|COSBase
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|oldValue
operator|==
literal|null
condition|)
block|{
return|return
name|newValue
operator|!=
literal|null
return|;
block|}
return|return
operator|!
name|oldValue
operator|.
name|equals
argument_list|(
name|newValue
argument_list|)
return|;
block|}
comment|/**      * Notifies the attribute object change listeners about a change in this      * attribute object.      */
specifier|protected
name|void
name|notifyChanged
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|getStructureElement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|getStructureElement
argument_list|()
operator|.
name|attributeChanged
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"O="
operator|+
name|this
operator|.
name|getOwner
argument_list|()
return|;
block|}
comment|/**      * Creates a String representation of an Object array.      *       * @param array the Object array      * @return the String representation      */
specifier|protected
specifier|static
name|String
name|arrayToString
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"["
argument_list|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Creates a String representation of a float array.      *       * @param array the float array      * @return the String representation      */
specifier|protected
specifier|static
name|String
name|arrayToString
parameter_list|(
name|float
index|[]
name|array
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"["
argument_list|)
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
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

