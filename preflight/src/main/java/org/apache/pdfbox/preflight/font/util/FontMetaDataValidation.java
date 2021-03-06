begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
operator|.
name|util
package|;
end_package

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
name|Iterator
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
name|pdmodel
operator|.
name|font
operator|.
name|PDFontDescriptor
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
name|preflight
operator|.
name|PreflightConstants
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
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|preflight
operator|.
name|exception
operator|.
name|ValidationException
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
name|preflight
operator|.
name|font
operator|.
name|descriptor
operator|.
name|FontDescriptorHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPRightsManagementSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|AbstractField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|ArrayProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|BooleanType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TextType
import|;
end_import

begin_comment
comment|/**  * Class used to validate the MetaData entry of the Font File Stream dictionary.  */
end_comment

begin_class
specifier|public
class|class
name|FontMetaDataValidation
block|{
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validatePDFAIdentifer
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|PDFontDescriptor
name|fontDesc
parameter_list|)
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|analyseFontName
argument_list|(
name|metadata
argument_list|,
name|fontDesc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
name|analyseRights
argument_list|(
name|metadata
argument_list|,
name|fontDesc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
return|return
name|ve
return|;
block|}
comment|/**      * Value of the dc:title must be the same as the FontName in the font descriptor.      *       * @param metadata      *            XMPMetaData of the Font File Stream      * @param fontDesc      *            The FontDescriptor dictionary      * @param ve      *            the list of validation error to update if the validation fails      */
specifier|public
name|boolean
name|analyseFontName
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|PDFontDescriptor
name|fontDesc
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|fontName
init|=
name|fontDesc
operator|.
name|getFontName
argument_list|()
decl_stmt|;
name|String
name|noSubSetName
init|=
name|fontName
decl_stmt|;
if|if
condition|(
name|FontDescriptorHelper
operator|.
name|isSubSet
argument_list|(
name|fontName
argument_list|)
condition|)
block|{
name|noSubSetName
operator|=
name|fontName
operator|.
name|split
argument_list|(
literal|"\\+"
argument_list|)
index|[
literal|1
index|]
expr_stmt|;
block|}
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|getDublinCoreSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|dc
operator|!=
literal|null
operator|&&
name|dc
operator|.
name|getTitleProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|defaultTitle
init|=
name|dc
operator|.
name|getTitle
argument_list|(
literal|"x-default"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultTitle
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|defaultTitle
operator|.
name|equals
argument_list|(
name|fontName
argument_list|)
operator|&&
operator|(
name|noSubSetName
operator|!=
literal|null
operator|&&
operator|!
name|defaultTitle
operator|.
name|equals
argument_list|(
name|noSubSetName
argument_list|)
operator|)
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|80
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"FontName"
argument_list|)
operator|.
name|append
argument_list|(
literal|" present in the FontDescriptor dictionary doesn't match with XMP information dc:title of the Font File Stream."
argument_list|)
expr_stmt|;
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_MISMATCH
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// --- default value is the right one
return|return
literal|true
return|;
block|}
else|else
block|{
name|Iterator
argument_list|<
name|AbstractField
argument_list|>
name|it
init|=
name|dc
operator|.
name|getTitleProperty
argument_list|()
operator|.
name|getContainer
argument_list|()
operator|.
name|getAllProperties
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|empty
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|empty
operator|=
literal|false
expr_stmt|;
name|AbstractField
name|tmp
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
condition|)
block|{
name|String
name|val
init|=
operator|(
operator|(
name|TextType
operator|)
name|tmp
operator|)
operator|.
name|getStringValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|.
name|equals
argument_list|(
name|fontName
argument_list|)
operator|||
name|val
operator|.
name|equals
argument_list|(
name|noSubSetName
argument_list|)
condition|)
block|{
comment|// value found, return
return|return
literal|true
return|;
block|}
block|}
block|}
comment|// title doesn't match, it is an error.
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|80
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"FontName"
argument_list|)
expr_stmt|;
if|if
condition|(
name|empty
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" present in the FontDescriptor dictionary can't be found in XMP information the Font File Stream."
argument_list|)
expr_stmt|;
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_PROPERTY_MISSING
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" present in the FontDescriptor dictionary doesn't match with XMP information dc:title of the Font File Stream."
argument_list|)
expr_stmt|;
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_MISMATCH
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * If XMP MetaData is present, they must have the following information :      *<UL>      *<li>dc:rights      *<li>Marked (with the value true)      *<li>Owner      *<li>UsageTerms      *</UL>      *       * @param metadata      *            XMPMetaData of the Font File Stream      * @param fontDesc      *            The FontDescriptor dictionary      * @param ve      *            the list of validation error to update if the validation fails      * @return true if the analysis found no problems, false if it did.      */
specifier|public
name|boolean
name|analyseRights
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|PDFontDescriptor
name|fontDesc
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|getDublinCoreSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|dc
operator|!=
literal|null
condition|)
block|{
name|ArrayProperty
name|copyrights
init|=
name|dc
operator|.
name|getRightsProperty
argument_list|()
decl_stmt|;
if|if
condition|(
name|copyrights
operator|==
literal|null
operator|||
name|copyrights
operator|.
name|getContainer
argument_list|()
operator|==
literal|null
operator|||
name|copyrights
operator|.
name|getContainer
argument_list|()
operator|.
name|getAllProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_PROPERTY_MISSING
argument_list|,
literal|"CopyRights is missing from the XMP information (dc:rights) of the Font File Stream."
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|XMPRightsManagementSchema
name|rights
init|=
name|metadata
operator|.
name|getXMPRightsManagementSchema
argument_list|()
decl_stmt|;
if|if
condition|(
name|rights
operator|!=
literal|null
condition|)
block|{
name|BooleanType
name|marked
init|=
name|rights
operator|.
name|getMarkedProperty
argument_list|()
decl_stmt|;
if|if
condition|(
name|marked
operator|!=
literal|null
operator|&&
operator|!
name|marked
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_PROPERTY_MISSING
argument_list|,
literal|"the XMP information (xmpRights:Marked) is invalid for the Font File Stream."
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/*              * rights.getUsageTerms()& rights.getOwnerValue() should be present but it is only a recommendation : may              * be it should be useful to append a Warning if these entries are missing.              */
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

