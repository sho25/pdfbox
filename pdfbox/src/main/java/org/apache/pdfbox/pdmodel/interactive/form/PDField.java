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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
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
name|fdf
operator|.
name|FDFField
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
name|action
operator|.
name|PDFormFieldAdditionalActions
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

begin_comment
comment|/**  * A field in an interactive form.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDField
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_READ_ONLY
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_REQUIRED
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_NO_EXPORT
init|=
literal|1
operator|<<
literal|2
decl_stmt|;
specifier|private
specifier|final
name|PDAcroForm
name|acroForm
decl_stmt|;
specifier|private
specifier|final
name|PDNonTerminalField
name|parent
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      */
name|PDField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
name|this
argument_list|(
name|acroForm
argument_list|,
operator|new
name|COSDictionary
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *  @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDField
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
name|this
operator|.
name|acroForm
operator|=
name|acroForm
expr_stmt|;
name|this
operator|.
name|dictionary
operator|=
name|field
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|/**      * Creates a COSField subclass from the given COS field. This is for reading fields from PDFs.      *      * @param form the form that the field is part of      * @param field the dictionary representing a field element      * @param parent the parent node of the node to be created, or null if root.      * @return a new PDField instance      */
specifier|static
name|PDField
name|fromDictionary
parameter_list|(
name|PDAcroForm
name|form
parameter_list|,
name|COSDictionary
name|field
parameter_list|,
name|PDNonTerminalField
name|parent
parameter_list|)
block|{
return|return
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|form
argument_list|,
name|field
argument_list|,
name|parent
argument_list|)
return|;
block|}
comment|/**      * Returns the given attribute, inheriting from parent nodes if necessary.      *      * @param key the key to look up      * @return COS value for the given key      */
specifier|protected
name|COSBase
name|getInheritableAttribute
parameter_list|(
name|COSName
name|key
parameter_list|)
block|{
if|if
condition|(
name|dictionary
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getInheritableAttribute
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|acroForm
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
comment|/**      * Get the FT entry of the field. This is a read only field and is set depending on the actual type. The field type      * is an inheritable attribute.      *       * @return The Field type.      *       */
specifier|public
specifier|abstract
name|String
name|getFieldType
parameter_list|()
function_decl|;
comment|/**      * Returns a string representation of the "V" entry, or an empty string.      *       * @return The list of widget annotations.      */
specifier|public
specifier|abstract
name|String
name|getValueAsString
parameter_list|()
function_decl|;
comment|/**      * Sets the value of the field.      *      * @param value the new field value.      *       * @throws IOException if the value could not be set      */
specifier|public
specifier|abstract
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the widget annotations associated with this field.      *       * For {@link PDNonTerminalField} the list will be empty as non terminal fields      * have no visual representation in the form.      *       * @return a List of {@link PDAnnotationWidget} annotations.      */
specifier|public
specifier|abstract
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|getWidgets
parameter_list|()
function_decl|;
comment|/**      * sets the field to be read-only.      *       * @param readonly The new flag for readonly.      */
specifier|public
name|void
name|setReadOnly
parameter_list|(
name|boolean
name|readonly
parameter_list|)
block|{
name|dictionary
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_READ_ONLY
argument_list|,
name|readonly
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * @return true if the field is readonly      */
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_READ_ONLY
argument_list|)
return|;
block|}
comment|/**      * sets the field to be required.      *       * @param required The new flag for required.      */
specifier|public
name|void
name|setRequired
parameter_list|(
name|boolean
name|required
parameter_list|)
block|{
name|dictionary
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_REQUIRED
argument_list|,
name|required
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * @return true if the field is required      */
specifier|public
name|boolean
name|isRequired
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_REQUIRED
argument_list|)
return|;
block|}
comment|/**      * sets the field to be not exported.      *       * @param noExport The new flag for noExport.      */
specifier|public
name|void
name|setNoExport
parameter_list|(
name|boolean
name|noExport
parameter_list|)
block|{
name|dictionary
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_NO_EXPORT
argument_list|,
name|noExport
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * @return true if the field is not to be exported.      */
specifier|public
name|boolean
name|isNoExport
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_NO_EXPORT
argument_list|)
return|;
block|}
comment|/**      * This will get the flags for this field.      *       * @return flags The set of flags.      */
specifier|public
specifier|abstract
name|int
name|getFieldFlags
parameter_list|()
function_decl|;
comment|/**      * This will set the flags for this field.      *       * @param flags The new flags.      */
specifier|public
name|void
name|setFieldFlags
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|flags
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the additional actions for this field. This will return null if there      * are no additional actions for this field.      *      * @return The actions of the field.      */
specifier|public
name|PDFormFieldAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSDictionary
name|aa
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AA
argument_list|)
decl_stmt|;
if|if
condition|(
name|aa
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDFormFieldAdditionalActions
argument_list|(
name|aa
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will import a fdf field from a fdf document.      *       * @param fdfField The fdf field to import.      * @throws IOException If there is an error importing the data for this field.      */
name|void
name|importFDF
parameter_list|(
name|FDFField
name|fdfField
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|fieldValue
init|=
name|fdfField
operator|.
name|getCOSValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldValue
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|fieldValue
argument_list|)
expr_stmt|;
block|}
name|Integer
name|ff
init|=
name|fdfField
operator|.
name|getFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|setFieldFlags
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// these are suppose to be ignored if the Ff is set.
name|Integer
name|setFf
init|=
name|fdfField
operator|.
name|getSetFieldFlags
argument_list|()
decl_stmt|;
name|int
name|fieldFlags
init|=
name|getFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|setFf
operator|!=
literal|null
condition|)
block|{
name|int
name|setFfInt
init|=
name|setFf
decl_stmt|;
name|fieldFlags
operator|=
name|fieldFlags
operator||
name|setFfInt
expr_stmt|;
name|setFieldFlags
argument_list|(
name|fieldFlags
argument_list|)
expr_stmt|;
block|}
name|Integer
name|clrFf
init|=
name|fdfField
operator|.
name|getClearFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|clrFf
operator|!=
literal|null
condition|)
block|{
comment|// we have to clear the bits of the document fields for every bit that is
comment|// set in this field.
comment|//
comment|// Example:
comment|// docFf = 1011
comment|// clrFf = 1101
comment|// clrFfValue = 0010;
comment|// newValue = 1011& 0010 which is 0010
name|int
name|clrFfValue
init|=
name|clrFf
decl_stmt|;
name|clrFfValue
operator|^=
literal|0xFFFFFFFF
expr_stmt|;
name|fieldFlags
operator|=
name|fieldFlags
operator|&
name|clrFfValue
expr_stmt|;
name|setFieldFlags
argument_list|(
name|fieldFlags
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Exports this field and its children as FDF.      */
specifier|abstract
name|FDFField
name|exportFDF
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Get the parent field to this field, or null if none exists.      *       * @return The parent field.      */
specifier|public
name|PDNonTerminalField
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|/**      * This will find one of the child elements. The name array are the components of the name to search down the tree      * of names. The nameIndex is where to start in that array. This method is called recursively until it finds the end      * point based on the name array.      *       * @param name An array that picks the path to the field.      * @param nameIndex The index into the array.      * @return The field at the endpoint or null if none is found.      */
name|PDField
name|findKid
parameter_list|(
name|String
index|[]
name|name
parameter_list|,
name|int
name|nameIndex
parameter_list|)
block|{
name|PDField
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|retval
operator|==
literal|null
operator|&&
name|i
operator|<
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|kidDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
index|[
name|nameIndex
index|]
operator|.
name|equals
argument_list|(
name|kidDictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|T
argument_list|)
argument_list|)
condition|)
block|{
name|retval
operator|=
name|PDField
operator|.
name|fromDictionary
argument_list|(
name|acroForm
argument_list|,
name|kidDictionary
argument_list|,
operator|(
name|PDNonTerminalField
operator|)
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|retval
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
operator|>
name|nameIndex
operator|+
literal|1
condition|)
block|{
name|retval
operator|=
name|retval
operator|.
name|findKid
argument_list|(
name|name
argument_list|,
name|nameIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the acroform that this field is part of.      *       * @return The form this field is on.      */
specifier|public
name|PDAcroForm
name|getAcroForm
parameter_list|()
block|{
return|return
name|acroForm
return|;
block|}
comment|/**      * This will get the dictionary associated with this field.      *       * @return the dictionary that this class wraps.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Returns the partial name of the field.      *       * @return the name of the field      */
specifier|public
name|String
name|getPartialName
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|T
argument_list|)
return|;
block|}
comment|/**      * This will set the partial name of the field.      *       * @param name The new name for the field.      */
specifier|public
name|void
name|setPartialName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|T
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the fully qualified name of the field, which is a concatenation of the names of all the parents fields.      *       * @return the name of the field      */
specifier|public
name|String
name|getFullyQualifiedName
parameter_list|()
block|{
name|String
name|finalName
init|=
name|getPartialName
argument_list|()
decl_stmt|;
name|String
name|parentName
init|=
name|parent
operator|!=
literal|null
condition|?
name|parent
operator|.
name|getFullyQualifiedName
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|parentName
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|finalName
operator|!=
literal|null
condition|)
block|{
name|finalName
operator|=
name|parentName
operator|+
literal|"."
operator|+
name|finalName
expr_stmt|;
block|}
else|else
block|{
name|finalName
operator|=
name|parentName
expr_stmt|;
block|}
block|}
return|return
name|finalName
return|;
block|}
comment|/**      * Gets the alternate name of the field.      *       * @return the alternate name of the field      */
specifier|public
name|String
name|getAlternateFieldName
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|TU
argument_list|)
return|;
block|}
comment|/**      * This will set the alternate name of the field.      *       * @param alternateFieldName the alternate name of the field      */
specifier|public
name|void
name|setAlternateFieldName
parameter_list|(
name|String
name|alternateFieldName
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|TU
argument_list|,
name|alternateFieldName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the mapping name of the field.      *       * The mapping name shall be used when exporting interactive form field      * data from the document.      *       * @return the mapping name of the field      */
specifier|public
name|String
name|getMappingName
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|TM
argument_list|)
return|;
block|}
comment|/**      * This will set the mapping name of the field.      *       * @param mappingName the mapping name of the field      */
specifier|public
name|void
name|setMappingName
parameter_list|(
name|String
name|mappingName
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|TM
argument_list|,
name|mappingName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"{type: "
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" value: "
operator|+
name|getInheritableAttribute
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

