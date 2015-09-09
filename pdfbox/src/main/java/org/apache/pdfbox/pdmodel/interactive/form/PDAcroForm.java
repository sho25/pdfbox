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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|cos
operator|.
name|COSNumber
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
name|PDDocument
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
name|PDResources
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
name|FDFCatalog
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
name|FDFDictionary
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
name|FDFDocument
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

begin_comment
comment|/**  * An interactive form, also known as an AcroForm.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDAcroForm
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_SIGNATURES_EXIST
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_APPEND_ONLY
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDField
argument_list|>
name|fieldCache
decl_stmt|;
comment|/**      * Constructor.      *      * @param doc The document that this form is part of.      */
specifier|public
name|PDAcroForm
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FIELDS
argument_list|,
operator|new
name|COSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param doc The document that this form is part of.      * @param form The existing acroForm.      */
specifier|public
name|PDAcroForm
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|COSDictionary
name|form
parameter_list|)
block|{
name|document
operator|=
name|doc
expr_stmt|;
name|dictionary
operator|=
name|form
expr_stmt|;
block|}
comment|/**      * This will get the document associated with this form.      *      * @return The PDF document.      */
name|PDDocument
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
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
comment|/**      * This method will import an entire FDF document into the PDF document      * that this acroform is part of.      *      * @param fdf The FDF document to import.      *      * @throws IOException If there is an error doing the import.      */
specifier|public
name|void
name|importFDF
parameter_list|(
name|FDFDocument
name|fdf
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|FDFField
argument_list|>
name|fields
init|=
name|fdf
operator|.
name|getCatalog
argument_list|()
operator|.
name|getFDF
argument_list|()
operator|.
name|getFields
argument_list|()
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|FDFField
name|field
range|:
name|fields
control|)
block|{
name|FDFField
name|fdfField
init|=
name|field
decl_stmt|;
name|PDField
name|docField
init|=
name|getField
argument_list|(
name|fdfField
operator|.
name|getPartialFieldName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|docField
operator|!=
literal|null
condition|)
block|{
name|docField
operator|.
name|importFDF
argument_list|(
name|fdfField
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will export all FDF form data.      *      * @return An FDF document used to export the document.      * @throws IOException If there is an error when exporting the document.      */
specifier|public
name|FDFDocument
name|exportFDF
parameter_list|()
throws|throws
name|IOException
block|{
name|FDFDocument
name|fdf
init|=
operator|new
name|FDFDocument
argument_list|()
decl_stmt|;
name|FDFCatalog
name|catalog
init|=
name|fdf
operator|.
name|getCatalog
argument_list|()
decl_stmt|;
name|FDFDictionary
name|fdfDict
init|=
operator|new
name|FDFDictionary
argument_list|()
decl_stmt|;
name|catalog
operator|.
name|setFDF
argument_list|(
name|fdfDict
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|FDFField
argument_list|>
name|fdfFields
init|=
operator|new
name|ArrayList
argument_list|<
name|FDFField
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDField
argument_list|>
name|fields
init|=
name|getFields
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|field
range|:
name|fields
control|)
block|{
name|fdfFields
operator|.
name|add
argument_list|(
name|field
operator|.
name|exportFDF
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fdfDict
operator|.
name|setID
argument_list|(
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentID
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|fdfFields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fdfDict
operator|.
name|setFields
argument_list|(
name|fdfFields
argument_list|)
expr_stmt|;
block|}
return|return
name|fdf
return|;
block|}
comment|/**      * This will return all of the documents root fields.      *       * A field might have children that are fields (non-terminal field) or does not      * have children which are fields (terminal fields).      *       * The fields within an AcroForm are organized in a tree structure. The documents root fields       * might either be terminal fields, non-terminal fields or a mixture of both. Non-terminal fields      * mark branches which contents can be retrieved using {@link PDNonTerminalField#getChildren()}.      *       * @return A list of the documents root fields.      *       */
specifier|public
name|List
argument_list|<
name|PDField
argument_list|>
name|getFields
parameter_list|()
block|{
name|COSArray
name|cosFields
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
name|FIELDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosFields
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|PDField
argument_list|>
name|pdFields
init|=
operator|new
name|ArrayList
argument_list|<
name|PDField
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
name|cosFields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|element
init|=
operator|(
name|COSDictionary
operator|)
name|cosFields
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|PDField
name|field
init|=
name|PDField
operator|.
name|fromDictionary
argument_list|(
name|this
argument_list|,
name|element
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|!=
literal|null
condition|)
block|{
name|pdFields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|COSArrayList
argument_list|<
name|PDField
argument_list|>
argument_list|(
name|pdFields
argument_list|,
name|cosFields
argument_list|)
return|;
block|}
comment|/**      * Set the documents root fields.      *      * @param fields The fields that are part of the documents root fields.      */
specifier|public
name|void
name|setFields
parameter_list|(
name|List
argument_list|<
name|PDField
argument_list|>
name|fields
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FIELDS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|fields
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will tell this form to cache the fields into a Map structure      * for fast access via the getField method.  The default is false.  You would      * want this to be false if you were changing the COSDictionary behind the scenes,      * otherwise setting this to true is acceptable.      *      * @param cache A boolean telling if we should cache the fields.      */
specifier|public
name|void
name|setCacheFields
parameter_list|(
name|boolean
name|cache
parameter_list|)
block|{
if|if
condition|(
name|cache
condition|)
block|{
name|fieldCache
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDField
argument_list|>
argument_list|()
expr_stmt|;
comment|// fixme: this code does not cache non-terminal fields or their kids
name|List
argument_list|<
name|PDField
argument_list|>
name|fields
init|=
name|getFields
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|field
range|:
name|fields
control|)
block|{
name|fieldCache
operator|.
name|put
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|fieldCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if this acro form is caching the fields.      *      * @return true if the fields are being cached.      */
specifier|public
name|boolean
name|isCachingFields
parameter_list|()
block|{
return|return
name|fieldCache
operator|!=
literal|null
return|;
block|}
comment|/**      * This will get a field by name, possibly using the cache if setCache is true.      *      * @param fullyQualifiedName The name of the field to get.      * @return The field with that name of null if one was not found.      */
specifier|public
name|PDField
name|getField
parameter_list|(
name|String
name|fullyQualifiedName
parameter_list|)
block|{
name|PDField
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fieldCache
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|fieldCache
operator|.
name|get
argument_list|(
name|fullyQualifiedName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|nameSubSection
init|=
name|fullyQualifiedName
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|COSArray
name|fields
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
name|FIELDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
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
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
operator|&&
name|retval
operator|==
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|element
init|=
operator|(
name|COSDictionary
operator|)
name|fields
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|COSString
name|fieldName
init|=
operator|(
name|COSString
operator|)
name|element
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|T
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|fullyQualifiedName
argument_list|)
operator|||
name|fieldName
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|nameSubSection
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|PDField
name|root
init|=
name|PDField
operator|.
name|fromDictionary
argument_list|(
name|this
argument_list|,
name|element
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nameSubSection
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|PDField
name|kid
init|=
name|root
operator|.
name|findKid
argument_list|(
name|nameSubSection
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|kid
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|kid
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|root
expr_stmt|;
block|}
block|}
else|else
block|{
name|retval
operator|=
name|root
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Get the default appearance.      *       * @return the DA element of the dictionary object      */
specifier|public
name|String
name|getDefaultAppearance
parameter_list|()
block|{
name|COSString
name|defaultAppearance
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
decl_stmt|;
return|return
name|defaultAppearance
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * Set the default appearance.      *       * @param daValue a string describing the default appearance      */
specifier|public
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|daValue
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|,
name|daValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * True if the viewing application should construct the appearances of all field widgets.      * The default value is false.      *       * @return the value of NeedAppearances, false if the value isn't set      */
specifier|public
name|boolean
name|getNeedAppearances
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|NEED_APPEARANCES
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Set the NeedAppearances value. If this is false, PDFBox will create appearances for all field      * widget.      *       * @param value the value for NeedAppearances      */
specifier|public
name|void
name|setNeedAppearances
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|NEED_APPEARANCES
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the default resources for the acro form.      *      * @return The default resources.      */
specifier|public
name|PDResources
name|getDefaultResources
parameter_list|()
block|{
name|PDResources
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dr
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
name|DR
argument_list|)
decl_stmt|;
if|if
condition|(
name|dr
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDResources
argument_list|(
name|dr
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the default resources for the acroform.      *      * @param dr The new default resources.      */
specifier|public
name|void
name|setDefaultResources
parameter_list|(
name|PDResources
name|dr
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DR
argument_list|,
name|dr
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will tell if the AcroForm has XFA content.      *      * @return true if the AcroForm is an XFA form      */
specifier|public
name|boolean
name|hasXFA
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|XFA
argument_list|)
return|;
block|}
comment|/**      * This will tell if the AcroForm is a dynamic XFA form.      *      * @return true if the AcroForm is a dynamic XFA form      */
specifier|public
name|boolean
name|xfaIsDynamic
parameter_list|()
block|{
return|return
name|hasXFA
argument_list|()
operator|&&
name|getFields
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Get the XFA resource, the XFA resource is only used for PDF 1.5+ forms.      *      * @return The xfa resource or null if it does not exist.      */
specifier|public
name|PDXFAResource
name|getXFA
parameter_list|()
block|{
name|PDXFAResource
name|xfa
init|=
literal|null
decl_stmt|;
name|COSBase
name|base
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|XFA
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|xfa
operator|=
operator|new
name|PDXFAResource
argument_list|(
name|base
argument_list|)
expr_stmt|;
block|}
return|return
name|xfa
return|;
block|}
comment|/**      * Set the XFA resource, this is only used for PDF 1.5+ forms.      *      * @param xfa The xfa resource.      */
specifier|public
name|void
name|setXFA
parameter_list|(
name|PDXFAResource
name|xfa
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|XFA
argument_list|,
name|xfa
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the 'quadding' or justification of the text to be displayed.      * 0 - Left(default)<br/>      * 1 - Centered<br />      * 2 - Right<br />      * Please see the QUADDING_CONSTANTS.      *      * @return The justification of the text strings.      */
specifier|public
name|int
name|getQ
parameter_list|()
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
name|COSNumber
name|number
init|=
operator|(
name|COSNumber
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|Q
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|number
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the quadding/justification of the text.  See QUADDING constants.      *      * @param q The new text justification.      */
specifier|public
name|void
name|setQ
parameter_list|(
name|int
name|q
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if SignaturesExist is set.      *       * @return true if the document contains at least one signature.      */
specifier|public
name|boolean
name|isSignaturesExist
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|SIG_FLAGS
argument_list|,
name|FLAG_SIGNATURES_EXIST
argument_list|)
return|;
block|}
comment|/**      * Set the SignaturesExist bit.      *      * @param signaturesExist The value for SignaturesExist.      */
specifier|public
name|void
name|setSignaturesExist
parameter_list|(
name|boolean
name|signaturesExist
parameter_list|)
block|{
name|dictionary
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|SIG_FLAGS
argument_list|,
name|FLAG_SIGNATURES_EXIST
argument_list|,
name|signaturesExist
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines if AppendOnly is set.      *       * @return true if the document contains signatures that may be invalidated if the file is saved.      */
specifier|public
name|boolean
name|isAppendOnly
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|SIG_FLAGS
argument_list|,
name|FLAG_APPEND_ONLY
argument_list|)
return|;
block|}
comment|/**      * Set the AppendOnly bit.      *      * @param appendOnly The value for AppendOnly.      */
specifier|public
name|void
name|setAppendOnly
parameter_list|(
name|boolean
name|appendOnly
parameter_list|)
block|{
name|dictionary
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|SIG_FLAGS
argument_list|,
name|FLAG_APPEND_ONLY
argument_list|,
name|appendOnly
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

