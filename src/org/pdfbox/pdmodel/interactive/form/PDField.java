begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDTextStream
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|util
operator|.
name|BitFlagHelper
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
comment|/**  * This is the superclass for a Field element in a PDF.  * Based on the COS object model from PDFBox.  *  * @author sug  * @version $Revision: 1.23 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDField
implements|implements
name|COSObjectable
block|{
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_READ_ONLY
init|=
literal|1
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_REQUIRED
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
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
name|PDAcroForm
name|acroForm
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      *      * @param theAcroForm The form that this field is part of.      */
specifier|public
name|PDField
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|)
block|{
name|acroForm
operator|=
name|theAcroForm
expr_stmt|;
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
comment|//no required fields in base field class
block|}
comment|/**      * Creates a COSField from a COSDictionary, expected to be      * a correct object definition for a field in PDF.      *      * @param theAcroForm The form that this field is part of.      * @param field the PDF objet to represent as a field.      */
specifier|public
name|PDField
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|)
block|{
name|acroForm
operator|=
name|theAcroForm
expr_stmt|;
name|dictionary
operator|=
name|field
expr_stmt|;
block|}
comment|/**      * Returns the partial name of the field.      *      * @return the name of the field      */
specifier|public
name|String
name|getPartialName
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
literal|"T"
argument_list|)
return|;
block|}
comment|/**      * This will set the partial name of the field.      *      * @param name The new name for the field.      */
specifier|public
name|void
name|setPartialName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
literal|"T"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the fully qualified name of the field, which is a concatenation of      * the names of all the parents fields.      *      * @return the name of the field      *      * @throws IOException If there is an error generating the fully qualified name.      */
specifier|public
name|String
name|getFullyQualifiedName
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|parent
init|=
name|getParent
argument_list|()
decl_stmt|;
name|String
name|parentName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parentName
operator|=
name|parent
operator|.
name|getFullyQualifiedName
argument_list|()
expr_stmt|;
block|}
name|String
name|finalName
init|=
name|getPartialName
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentName
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
return|return
name|finalName
return|;
block|}
comment|/**      * Get the FT entry of the field.  This is a read only field and is set depending      * on the actual type.  The field type is an inheritable attribute.  This method will      * return only the direct value on this object.  Use the findFieldType for an upward      * recursive search.      *      * @return The Field type.      *      * @see PDField#findFieldType()      */
specifier|public
name|String
name|getFieldType
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"FT"
argument_list|)
return|;
block|}
comment|/**      * Find the field type and optionally do a recursive upward search.  Sometimes the fieldtype      * will be specified on the parent instead of the direct object.  This will look at this      * object for the field type, if none is specified then it will look to the parent if there      * is a parent.  If there is no parent and no field type has been found then this      * will return null.      *      * @return The field type or null if none was found.      */
specifier|public
name|String
name|findFieldType
parameter_list|()
block|{
return|return
name|findFieldType
argument_list|(
name|getDictionary
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|findFieldType
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|String
name|retval
init|=
name|dic
operator|.
name|getNameAsString
argument_list|(
literal|"FT"
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|parent
init|=
operator|(
name|COSDictionary
operator|)
name|dic
operator|.
name|getDictionaryObject
argument_list|(
literal|"Parent"
argument_list|,
literal|"P"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|findFieldType
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * setValue sets the fields value to a given string.      *      * @param value the string value      *      * @throws IOException If there is an error creating the appearance stream.      */
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
comment|/**      * getValue gets the fields value to as a string.      *      * @return The string value of this field.      *      * @throws IOException If there is an error getting the value.      */
specifier|public
specifier|abstract
name|String
name|getValue
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * sets the field to be read-only.      *      * @param readonly The new flag for readonly.      */
specifier|public
name|void
name|setReadonly
parameter_list|(
name|boolean
name|readonly
parameter_list|)
block|{
name|BitFlagHelper
operator|.
name|setFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_READ_ONLY
argument_list|,
name|readonly
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true if the field is readonly      */
specifier|public
name|boolean
name|isReadonly
parameter_list|()
block|{
return|return
name|BitFlagHelper
operator|.
name|getFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_READ_ONLY
argument_list|)
return|;
block|}
comment|/**      * sets the field to be required.      *      * @param required The new flag for required.      */
specifier|public
name|void
name|setRequired
parameter_list|(
name|boolean
name|required
parameter_list|)
block|{
name|BitFlagHelper
operator|.
name|setFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_REQUIRED
argument_list|,
name|required
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true if the field is required      */
specifier|public
name|boolean
name|isRequired
parameter_list|()
block|{
return|return
name|BitFlagHelper
operator|.
name|getFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_REQUIRED
argument_list|)
return|;
block|}
comment|/**      * sets the field to be not exported..      *      * @param noExport The new flag for noExport.      */
specifier|public
name|void
name|setNoExport
parameter_list|(
name|boolean
name|noExport
parameter_list|)
block|{
name|BitFlagHelper
operator|.
name|setFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_NO_EXPORT
argument_list|,
name|noExport
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true if the field is not to be exported.      */
specifier|public
name|boolean
name|isNoExport
parameter_list|()
block|{
return|return
name|BitFlagHelper
operator|.
name|getFlag
argument_list|(
name|getDictionary
argument_list|()
argument_list|,
literal|"Ff"
argument_list|,
name|FLAG_NO_EXPORT
argument_list|)
return|;
block|}
comment|/**      * This will get the flags for this field.      *      * @return flags The set of flags.      */
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
name|getPDFName
argument_list|(
literal|"Ff"
argument_list|)
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
return|return
name|retval
return|;
block|}
comment|/**      * This will set the flags for this field.      *      * @param flags The new flags.      */
specifier|public
name|void
name|setFieldFlags
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
name|COSInteger
name|ff
init|=
operator|new
name|COSInteger
argument_list|(
name|flags
argument_list|)
decl_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Ff"
argument_list|)
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will import a fdf field from a fdf document.      *      * @param fdfField The fdf field to import.      *      * @throws IOException If there is an error importing the data for this field.      */
specifier|public
name|void
name|importFDF
parameter_list|(
name|FDFField
name|fdfField
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|fieldValue
init|=
name|fdfField
operator|.
name|getValue
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
name|fieldValue
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fieldValue
operator|instanceof
name|String
condition|)
block|{
name|setValue
argument_list|(
operator|(
name|String
operator|)
name|fieldValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fieldValue
operator|instanceof
name|PDTextStream
condition|)
block|{
name|setValue
argument_list|(
operator|(
operator|(
name|PDTextStream
operator|)
name|fieldValue
operator|)
operator|.
name|getAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown field type:"
operator|+
name|fieldValue
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
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//these are suppose to be ignored if the Ff is set.
name|Integer
name|setFf
init|=
name|fdfField
operator|.
name|getSetFieldFlags
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
operator|.
name|intValue
argument_list|()
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
comment|//we have to clear the bits of the document fields for every bit that is
comment|//set in this field.
comment|//
comment|//Example:
comment|//docFf = 1011
comment|//clrFf = 1101
comment|//clrFfValue = 0010;
comment|//newValue = 1011& 0010 which is 0010
name|int
name|clrFfValue
init|=
name|clrFf
operator|.
name|intValue
argument_list|()
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
name|PDAnnotationWidget
name|widget
init|=
name|getWidget
argument_list|()
decl_stmt|;
if|if
condition|(
name|widget
operator|!=
literal|null
condition|)
block|{
name|int
name|annotFlags
init|=
name|widget
operator|.
name|getAnnotationFlags
argument_list|()
decl_stmt|;
name|Integer
name|f
init|=
name|fdfField
operator|.
name|getWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
operator|&&
name|widget
operator|!=
literal|null
condition|)
block|{
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|f
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//these are suppose to be ignored if the F is set.
name|Integer
name|setF
init|=
name|fdfField
operator|.
name|getSetWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|setF
operator|!=
literal|null
condition|)
block|{
name|annotFlags
operator|=
name|annotFlags
operator||
name|setF
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|annotFlags
argument_list|)
expr_stmt|;
block|}
name|Integer
name|clrF
init|=
name|fdfField
operator|.
name|getClearWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|clrF
operator|!=
literal|null
condition|)
block|{
comment|//we have to clear the bits of the document fields for every bit that is
comment|//set in this field.
comment|//
comment|//Example:
comment|//docF = 1011
comment|//clrF = 1101
comment|//clrFValue = 0010;
comment|//newValue = 1011& 0010 which is 0010
name|int
name|clrFValue
init|=
name|clrF
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|clrFValue
operator|^=
literal|0xFFFFFFFFL
expr_stmt|;
name|annotFlags
operator|=
name|annotFlags
operator|&
name|clrFValue
expr_stmt|;
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|annotFlags
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|List
name|fdfKids
init|=
name|fdfField
operator|.
name|getKids
argument_list|()
decl_stmt|;
name|List
name|pdKids
init|=
name|getKids
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|fdfKids
operator|!=
literal|null
operator|&&
name|i
operator|<
name|fdfKids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|FDFField
name|fdfChild
init|=
operator|(
name|FDFField
operator|)
name|fdfKids
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|fdfName
init|=
name|fdfChild
operator|.
name|getPartialFieldName
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|pdKids
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Object
name|pdChildObj
init|=
name|pdKids
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
if|if
condition|(
name|pdChildObj
operator|instanceof
name|PDField
condition|)
block|{
name|PDField
name|pdChild
init|=
operator|(
name|PDField
operator|)
name|pdChildObj
decl_stmt|;
if|if
condition|(
name|fdfName
operator|!=
literal|null
operator|&&
name|fdfName
operator|.
name|equals
argument_list|(
name|pdChild
operator|.
name|getPartialName
argument_list|()
argument_list|)
condition|)
block|{
name|pdChild
operator|.
name|importFDF
argument_list|(
name|fdfChild
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * This will get the single associated widget that is part of this field.  This      * occurs when the Widget is embedded in the fields dictionary.  Sometimes there      * are multiple sub widgets associated with this field, in which case you want to      * use getKids().  If the kids entry is specified, then the first entry in that      * list will be returned.      *      * @return The widget that is associated with this field.      * @throws IOException If there is an error getting the widget object.      */
specifier|public
name|PDAnnotationWidget
name|getWidget
parameter_list|()
throws|throws
name|IOException
block|{
name|PDAnnotationWidget
name|retval
init|=
literal|null
decl_stmt|;
name|List
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDAnnotationWidget
argument_list|(
name|getDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Object
name|firstKid
init|=
name|kids
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstKid
operator|instanceof
name|PDAnnotationWidget
condition|)
block|{
name|retval
operator|=
operator|(
name|PDAnnotationWidget
operator|)
name|firstKid
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|(
operator|(
name|PDField
operator|)
name|firstKid
operator|)
operator|.
name|getWidget
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|retval
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Get the parent field to this field, or null if none exists.      *      * @return The parent field.      *      * @throws IOException If there is an error creating the parent field.      */
specifier|public
name|PDField
name|getParent
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|parent
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|parentDic
init|=
operator|(
name|COSDictionary
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Parent"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentDic
operator|!=
literal|null
condition|)
block|{
name|parent
operator|=
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|getAcroForm
argument_list|()
argument_list|,
name|parentDic
argument_list|)
expr_stmt|;
block|}
return|return
name|parent
return|;
block|}
comment|/**      * Set the parent of this field.      *      * @param parent The parent to this field.      */
specifier|public
name|void
name|setParent
parameter_list|(
name|PDField
name|parent
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"Parent"
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will find one of the child elements.  The name array are the components      * of the name to search down the tree of names.  The nameIndex is where to      * start in that array.  This method is called recursively until it finds      * the end point based on the name array.      *      * @param name An array that picks the path to the field.      * @param nameIndex The index into the array.      * @return The field at the endpoint or null if none is found.      * @throws IOException If there is an error creating the field.      */
specifier|public
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
throws|throws
name|IOException
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
name|getDictionary
argument_list|()
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
literal|"T"
argument_list|)
argument_list|)
condition|)
block|{
name|retval
operator|=
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|acroForm
argument_list|,
name|kidDictionary
argument_list|)
expr_stmt|;
if|if
condition|(
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
comment|/**      * This will get all the kids of this field.  The values in the list      * will either be PDWidget or PDField.  Normally they will be PDWidget objects      * unless this is a non-terminal field and they will be child PDField objects.      *      * @return A list of either PDWidget or PDField objects.      * @throws IOException If there is an error retrieving the kids.      */
specifier|public
name|List
name|getKids
parameter_list|()
throws|throws
name|IOException
block|{
name|List
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
name|getDictionary
argument_list|()
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
name|List
name|kidsList
init|=
operator|new
name|ArrayList
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
name|COSDictionary
name|parent
init|=
operator|(
name|COSDictionary
operator|)
name|kidDictionary
operator|.
name|getDictionaryObject
argument_list|(
literal|"Parent"
argument_list|)
decl_stmt|;
if|if
condition|(
name|kidDictionary
operator|.
name|getDictionaryObject
argument_list|(
literal|"FT"
argument_list|)
operator|!=
literal|null
operator|||
operator|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getDictionaryObject
argument_list|(
literal|"FT"
argument_list|)
operator|!=
literal|null
operator|)
condition|)
block|{
name|kidsList
operator|.
name|add
argument_list|(
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|acroForm
argument_list|,
name|kidDictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"Widget"
operator|.
name|equals
argument_list|(
name|kidDictionary
operator|.
name|getNameAsString
argument_list|(
literal|"Subtype"
argument_list|)
argument_list|)
condition|)
block|{
name|kidsList
operator|.
name|add
argument_list|(
operator|new
name|PDAnnotationWidget
argument_list|(
name|kidDictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//
name|kidsList
operator|.
name|add
argument_list|(
name|PDFieldFactory
operator|.
name|createField
argument_list|(
name|acroForm
argument_list|,
name|kidDictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|kidsList
argument_list|,
name|kids
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of kids.      *      * @param kids The list of child widgets.      */
specifier|public
name|void
name|setKids
parameter_list|(
name|List
name|kids
parameter_list|)
block|{
name|COSArray
name|kidsArray
init|=
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|kids
argument_list|)
decl_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
name|kidsArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a string representation of this field.      *      * @return A string representation of this field.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"V"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will get the acroform that this field is part of.      *      * @return The form this field is on.      */
specifier|public
name|PDAcroForm
name|getAcroForm
parameter_list|()
block|{
return|return
name|acroForm
return|;
block|}
comment|/**      * This will set the form this field is on.      *      * @param value The new form to use.      */
specifier|public
name|void
name|setAcroForm
parameter_list|(
name|PDAcroForm
name|value
parameter_list|)
block|{
name|acroForm
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * This will get the dictionary associated with this field.      *      * @return The dictionary that this class wraps.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Get the additional actions for this field.  This will return null      * if there are no additional actions for this field.      *      * @return The actions of the field.      */
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
literal|"AA"
argument_list|)
decl_stmt|;
name|PDFormFieldAdditionalActions
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|aa
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFormFieldAdditionalActions
argument_list|(
name|aa
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the actions of the field.      *      * @param actions The field actions.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDFormFieldAdditionalActions
name|actions
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
literal|"AA"
argument_list|,
name|actions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

