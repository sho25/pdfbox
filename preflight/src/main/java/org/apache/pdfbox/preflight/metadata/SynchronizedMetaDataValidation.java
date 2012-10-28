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
name|metadata
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
name|Calendar
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
name|padaf
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
name|padaf
operator|.
name|xmpbox
operator|.
name|DateConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|AdobePDFSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
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
name|padaf
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
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
name|padaf
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TextType
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
name|PDDocumentInformation
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

begin_comment
comment|/**  * Class which check if document information available in a document are  * synchronized with XMP  *   * @author Germain Costenobel  *   */
end_comment

begin_class
specifier|public
class|class
name|SynchronizedMetaDataValidation
block|{
comment|/**    * Analyze if Title embedded in Document Information dictionary and in XMP    * properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param dc    *          Dublin Core Schema    * @param ve    *          The list of validation errors    */
specifier|protected
name|void
name|analyzeTitleProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|DublinCoreSchema
name|dc
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|title
init|=
name|dico
operator|.
name|getTitle
argument_list|()
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|!=
literal|null
condition|)
block|{
comment|// Check the x-default value, if not found, check with the first value
comment|// found
if|if
condition|(
name|dc
operator|.
name|getTitle
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|.
name|getTitle
argument_list|(
literal|"x-default"
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|dc
operator|.
name|getTitle
argument_list|(
literal|"x-default"
argument_list|)
operator|.
name|equals
argument_list|(
name|title
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Title"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// This search of first value is made just to keep compatibility
comment|// with lot of PDF documents
comment|// which use title without lang definition
comment|// REM : MAY we have to delete this option in the future
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
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
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
if|if
condition|(
operator|!
operator|(
operator|(
name|TextType
operator|)
name|tmp
operator|)
operator|.
name|getStringValue
argument_list|()
operator|.
name|equals
argument_list|(
name|title
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Title"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Title"
argument_list|,
literal|"Property is badly defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Title"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Title"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"Title"
argument_list|,
literal|"Dublin Core"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if Author(s) embedded in Document Information dictionary and in XMP    * properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param dc    *          Dublin Core Schema    * @param ve    *          The list of validation errors    */
specifier|protected
name|void
name|analyzeAuthorProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|DublinCoreSchema
name|dc
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|author
init|=
name|dico
operator|.
name|getAuthor
argument_list|()
decl_stmt|;
if|if
condition|(
name|author
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|.
name|getCreatorsProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|.
name|getCreators
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Author"
argument_list|,
literal|"In XMP metadata, Author(s) must be represented by a single entry in a text array (dc:creator) "
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|dc
operator|.
name|getCreators
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Author"
argument_list|,
literal|"Property is defined as null"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|dc
operator|.
name|getCreators
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|author
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Author"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Author"
argument_list|,
literal|"Property is not defined in XMP Metadata"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"Author"
argument_list|,
literal|"Dublin Core"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if Subject(s) embedded in Document Information dictionary and in    * XMP properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param dc    *          Dublin Core Schema    * @param ve    *          The list of validation errors    */
specifier|protected
name|void
name|analyzeSubjectProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|DublinCoreSchema
name|dc
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|subject
init|=
name|dico
operator|.
name|getSubject
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|!=
literal|null
condition|)
block|{
comment|// PDF/A Conformance Erratum (2007) specifies XMP Subject
comment|// as a Text type embedded in the dc:description["x-default"].
if|if
condition|(
name|dc
operator|.
name|getDescriptionProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dc
operator|.
name|getDescription
argument_list|(
literal|"x-default"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Subject"
argument_list|,
literal|"Subject not found in XMP (dc:description[\"x-default\"] not found)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|dc
operator|.
name|getDescription
argument_list|(
literal|"x-default"
argument_list|)
operator|.
name|equals
argument_list|(
name|subject
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Subject"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Subject"
argument_list|,
literal|"Property is defined as null"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"Subject"
argument_list|,
literal|"Dublin Core"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if Keyword(s) embedded in Document Information dictionary and in    * XMP properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param pdf    *          PDF Schema    * @param ve    *          The list of validation errors    */
specifier|protected
name|void
name|analyzeKeywordsProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|AdobePDFSchema
name|pdf
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|keyword
init|=
name|dico
operator|.
name|getKeywords
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyword
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pdf
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pdf
operator|.
name|getKeywordsProperty
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Keywords"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|pdf
operator|.
name|getKeywords
argument_list|()
operator|.
name|equals
argument_list|(
name|keyword
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Keywords"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"Keywords"
argument_list|,
literal|"PDF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if Producer embedded in Document Information dictionary and in XMP    * properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param pdf    *          PDF Schema    * @param ve    *          The list of validation errors    */
specifier|protected
name|void
name|analyzeProducerProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|AdobePDFSchema
name|pdf
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|producer
init|=
name|dico
operator|.
name|getProducer
argument_list|()
decl_stmt|;
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pdf
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|pdf
operator|.
name|getProducerProperty
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"Producer"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|pdf
operator|.
name|getProducer
argument_list|()
operator|.
name|equals
argument_list|(
name|producer
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"Producer"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"Producer"
argument_list|,
literal|"PDF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if the creator tool embedded in Document Information dictionary and    * in XMP properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param xmp    *          XMP Basic Schema    * @param ve    *          The list of validation errors    *     */
specifier|protected
name|void
name|analyzeCreatorToolProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|XMPBasicSchema
name|xmp
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
block|{
name|String
name|creatorTool
init|=
name|dico
operator|.
name|getCreator
argument_list|()
decl_stmt|;
if|if
condition|(
name|creatorTool
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|xmp
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|xmp
operator|.
name|getCreatorToolProperty
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"CreatorTool"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|xmp
operator|.
name|getCreatorTool
argument_list|()
operator|.
name|equals
argument_list|(
name|creatorTool
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"CreatorTool"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"CreatorTool"
argument_list|,
literal|"PDF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if the CreationDate embedded in Document Information dictionary and    * in XMP properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param xmp    *          XMP Basic Schema    * @param ve    *          The list of validation errors    * @throws ValidationException    */
specifier|protected
name|void
name|analyzeCreationDateProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|XMPBasicSchema
name|xmp
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
throws|throws
name|ValidationException
block|{
name|Calendar
name|creationDate
decl_stmt|;
try|try
block|{
name|creationDate
operator|=
name|dico
operator|.
name|getCreationDate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// If there is an error while converting this property to a date
throw|throw
name|formatAccessException
argument_list|(
literal|"Document Information"
argument_list|,
literal|"CreationDate"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|creationDate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|xmp
operator|!=
literal|null
condition|)
block|{
name|Calendar
name|xmpCreationDate
init|=
name|xmp
operator|.
name|getCreateDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|xmpCreationDate
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"CreationDate"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|DateConverter
operator|.
name|toISO8601
argument_list|(
name|xmpCreationDate
argument_list|)
operator|.
name|equals
argument_list|(
name|DateConverter
operator|.
name|toISO8601
argument_list|(
name|creationDate
argument_list|)
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"CreationDate"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"CreationDate"
argument_list|,
literal|"Basic XMP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Analyze if the ModifyDate embedded in Document Information dictionary and    * in XMP properties are synchronized    *     * @param dico    *          Document Information Dictionary    * @param xmp    *          XMP Basic Schema    * @param ve    *          The list of validation errors    * @throws ValidationException    */
specifier|protected
name|void
name|analyzeModifyDateProperty
parameter_list|(
name|PDDocumentInformation
name|dico
parameter_list|,
name|XMPBasicSchema
name|xmp
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
parameter_list|)
throws|throws
name|ValidationException
block|{
name|Calendar
name|modifyDate
decl_stmt|;
try|try
block|{
name|modifyDate
operator|=
name|dico
operator|.
name|getModificationDate
argument_list|()
expr_stmt|;
if|if
condition|(
name|modifyDate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|xmp
operator|!=
literal|null
condition|)
block|{
name|Calendar
name|xmpModifyDate
init|=
name|xmp
operator|.
name|getModifyDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|xmpModifyDate
operator|==
literal|null
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentXMPPropertyError
argument_list|(
literal|"ModifyDate"
argument_list|,
literal|"Property is not defined"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|DateConverter
operator|.
name|toISO8601
argument_list|(
name|xmpModifyDate
argument_list|)
operator|.
name|equals
argument_list|(
name|DateConverter
operator|.
name|toISO8601
argument_list|(
name|modifyDate
argument_list|)
argument_list|)
condition|)
block|{
name|ve
operator|.
name|add
argument_list|(
name|unsynchronizedMetaDataError
argument_list|(
literal|"ModificationDate"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ve
operator|.
name|add
argument_list|(
name|AbsentSchemaMetaDataError
argument_list|(
literal|"ModifyDate"
argument_list|,
literal|"Basic XMP"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// If there is an error while converting this property to a date
throw|throw
name|formatAccessException
argument_list|(
literal|"Document Information"
argument_list|,
literal|"ModifyDate"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Check if document information entries and XMP information are synchronized    *     * @param document    *          the PDF Document    * @param metadata    *          the XMP MetaData    * @return List of validation errors    * @throws ValidationException    */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validateMetadataSynchronization
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|XMPMetadata
name|metadata
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
argument_list|<
name|ValidationError
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Document provided is null"
argument_list|)
throw|;
block|}
else|else
block|{
name|PDDocumentInformation
name|dico
init|=
name|document
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadata
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Metadata provided are null"
argument_list|)
throw|;
block|}
else|else
block|{
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|getDublinCoreSchema
argument_list|()
decl_stmt|;
comment|// TITLE
name|analyzeTitleProperty
argument_list|(
name|dico
argument_list|,
name|dc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|analyzeAuthorProperty
argument_list|(
name|dico
argument_list|,
name|dc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|analyzeSubjectProperty
argument_list|(
name|dico
argument_list|,
name|dc
argument_list|,
name|ve
argument_list|)
expr_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|metadata
operator|.
name|getAdobePDFSchema
argument_list|()
decl_stmt|;
comment|// KEYWORDS
name|analyzeKeywordsProperty
argument_list|(
name|dico
argument_list|,
name|pdf
argument_list|,
name|ve
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|analyzeProducerProperty
argument_list|(
name|dico
argument_list|,
name|pdf
argument_list|,
name|ve
argument_list|)
expr_stmt|;
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|getXMPBasicSchema
argument_list|()
decl_stmt|;
comment|// CREATOR TOOL
name|analyzeCreatorToolProperty
argument_list|(
name|dico
argument_list|,
name|xmp
argument_list|,
name|ve
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|analyzeCreationDateProperty
argument_list|(
name|dico
argument_list|,
name|xmp
argument_list|,
name|ve
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|analyzeModifyDateProperty
argument_list|(
name|dico
argument_list|,
name|xmp
argument_list|,
name|ve
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ve
return|;
block|}
comment|/**    * Return a validationError formatted when a schema has not the expected    * prefix    *     * @param prefFound    * @param prefExpected    * @param schema    * @return    */
specifier|protected
name|ValidationError
name|unexpectedPrefixFoundError
parameter_list|(
name|String
name|prefFound
parameter_list|,
name|String
name|prefExpected
parameter_list|,
name|String
name|schema
parameter_list|)
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
name|schema
argument_list|)
operator|.
name|append
argument_list|(
literal|" found but prefix used is '"
argument_list|)
operator|.
name|append
argument_list|(
name|prefFound
argument_list|)
operator|.
name|append
argument_list|(
literal|"', prefix '"
argument_list|)
operator|.
name|append
argument_list|(
name|prefExpected
argument_list|)
operator|.
name|append
argument_list|(
literal|"' is expected."
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_WRONG_NS_PREFIX
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Return an exception formatted on IOException when accessing metadata    *     * @param type    *          type of property (Document Info or XMP)    * @param target    *          the name of the metadata    * @param cause    *          the raised IOException    * @return the generated exception    */
specifier|protected
name|ValidationException
name|formatAccessException
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|target
parameter_list|,
name|Throwable
name|cause
parameter_list|)
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
literal|"Cannot treat "
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|target
argument_list|)
operator|.
name|append
argument_list|(
literal|" property"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValidationException
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|cause
argument_list|)
return|;
block|}
comment|/**    * Return an exception formatted on IOException when accessing on metadata    * schema    *     * @param target    *          the name of the schema    * @param cause    *          the raised IOException    * @return the generated exception    */
specifier|protected
name|ValidationException
name|SchemaAccessException
parameter_list|(
name|String
name|target
parameter_list|,
name|Throwable
name|cause
parameter_list|)
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
literal|"Cannot access to the "
argument_list|)
operator|.
name|append
argument_list|(
name|target
argument_list|)
operator|.
name|append
argument_list|(
literal|" schema"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValidationException
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|cause
argument_list|)
return|;
block|}
comment|/**    * Return a formatted validation error when metadata are not synchronized    *     * @param target    *          the concerned property    * @return the generated validation error    */
specifier|protected
name|ValidationError
name|unsynchronizedMetaDataError
parameter_list|(
name|String
name|target
parameter_list|)
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
name|target
argument_list|)
operator|.
name|append
argument_list|(
literal|" present in the document catalog dictionary doesn't match with XMP information"
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
comment|/**    * Return a formatted validation error when a specific metadata schema can't    * be found    *     * @param target    *          the concerned property    * @param schema    *          the XMP schema which can't be found    * @return the generated validation error    */
specifier|protected
name|ValidationError
name|AbsentSchemaMetaDataError
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|schema
parameter_list|)
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
name|target
argument_list|)
operator|.
name|append
argument_list|(
literal|" present in the document catalog dictionary can't be found in XMP information ("
argument_list|)
operator|.
name|append
argument_list|(
name|schema
argument_list|)
operator|.
name|append
argument_list|(
literal|" schema not declared)"
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
comment|/**    * Return a formatted validation error when a specific XMP property can't be    * found    *     * @param target    *          the concerned property    * @param details    *          comments about the XMP property    * @return the generated validation error    */
specifier|protected
name|ValidationError
name|AbsentXMPPropertyError
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|details
parameter_list|)
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
name|target
argument_list|)
operator|.
name|append
argument_list|(
literal|" present in the document catalog dictionary can't be found in XMP information ("
argument_list|)
operator|.
name|append
argument_list|(
name|details
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
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
return|;
block|}
block|}
end_class

end_unit

