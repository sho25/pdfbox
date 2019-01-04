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
name|process
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ICC_Profile
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
name|io
operator|.
name|InputStream
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
name|COSDocument
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
name|COSObject
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
name|COSObjectKey
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
name|COSStream
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
name|PDDocumentCatalog
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
name|PDDocumentNameDictionary
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
name|PDEmbeddedFilesNameTreeNode
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
name|PreflightConfiguration
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
name|PreflightContext
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
name|graphic
operator|.
name|ICCProfileWrapper
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
name|utils
operator|.
name|COSUtils
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
name|utils
operator|.
name|ContextHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConfiguration
operator|.
name|ACTIONS_PROCESS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * This ValidationProcess check if the Catalog entries are confirming with the PDF/A-1b specification.  */
end_comment

begin_class
specifier|public
class|class
name|CatalogValidationProcess
extends|extends
name|AbstractProcess
block|{
specifier|protected
name|PDDocumentCatalog
name|catalog
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|listICC
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CatalogValidationProcess
parameter_list|()
block|{
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA43
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR_006
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR006
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA39
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_JC200103
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA27
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_EUROSB104
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA45
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA46
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA41
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR_001
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR_003
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR_005
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR001
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR003
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR005
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA28
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_JCW2003
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_EUROSB204
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA47
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA44
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA29
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_JC200104
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA40
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA30
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA42
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_IFRA26
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_JCN2002
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR_002
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_CGATS_TR002
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA33
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA37
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA31
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA35
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA32
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA34
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA36
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_FOGRA38
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_sRGB
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_sRGB_IEC
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_Adobe
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_bg_sRGB
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_sYCC
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_scRGB
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_scRGB_nl
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_scYCC_nl
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_ROMM
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_RIMM
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_ERIMM
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_eciRGB
argument_list|)
expr_stmt|;
name|listICC
operator|.
name|add
argument_list|(
name|ICC_CHARACTERIZATION_DATA_REGISTRY_opRGB
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isStandardICCCharacterization
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|String
name|iccStandard
range|:
name|listICC
control|)
block|{
if|if
condition|(
name|iccStandard
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDDocument
name|pdfbox
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|this
operator|.
name|catalog
operator|=
name|pdfbox
operator|.
name|getDocumentCatalog
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|catalog
operator|==
literal|null
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_NOCATALOG
argument_list|,
literal|"There are no Catalog entry in the Document"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validateActions
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|validateLang
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|validateNames
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|validateOCProperties
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|validateOutputIntent
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method validates if OpenAction entry contains forbidden action type. It checks too if an Additional Action      * is present.      *       * @param ctx      * @throws ValidationException      *             Propagate the ActionManager exception      */
specifier|protected
name|void
name|validateActions
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
name|catalog
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|ACTIONS_PROCESS
argument_list|)
expr_stmt|;
comment|// AA entry if forbidden in PDF/A-1
name|COSBase
name|aa
init|=
name|catalog
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|DICTIONARY_KEY_ADDITIONAL_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|aa
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ADDITIONAL_ACTION
argument_list|,
literal|"The AA field is forbidden for the Catalog  when the PDF is a PDF/A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * The Lang element is optional but it is recommended. This method check the Syntax of the Lang if this entry is      * present.      *       * @param ctx      * @throws ValidationException      */
specifier|protected
name|void
name|validateLang
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|String
name|lang
init|=
name|catalog
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
if|if
condition|(
name|lang
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|lang
argument_list|)
operator|&&
operator|!
name|lang
operator|.
name|matches
argument_list|(
literal|"[A-Za-z]{1,8}(-[A-Za-z]{1,8})*"
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_LANG_NOT_RFC1766
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A Catalog shall not contain the EmbeddedFiles entry.      *       * @param ctx      * @throws ValidationException      */
specifier|protected
name|void
name|validateNames
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDDocumentNameDictionary
name|names
init|=
name|catalog
operator|.
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|PDEmbeddedFilesNameTreeNode
name|efs
init|=
name|names
operator|.
name|getEmbeddedFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|efs
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_CATALOG_EMBEDDEDFILES
argument_list|,
literal|"EmbeddedFile entry is present in the Names dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|names
operator|.
name|getJavaScript
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ACTIONS_NAMED
argument_list|,
literal|"Javascript entry is present in the Names dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * A Catalog shall not contain the OCPProperties (Optional Content Properties) entry.      *       * @param ctx      * @throws ValidationException      */
specifier|protected
name|void
name|validateOCProperties
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|catalog
operator|.
name|getOCProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_CATALOG_OCPROPERTIES
argument_list|,
literal|"A Catalog shall not contain the OCPProperties entry"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks the content of each OutputIntent. The S entry must contain GTS_PDFA1. The DestOuputProfile      * must contain a valid ICC Profile Stream.      *       * If there are more than one OutputIntent, they have to use the same ICC Profile.      *       * This method returns a list of ValidationError. It is empty if no errors have been found.      *       * @param ctx      * @throws ValidationException      */
specifier|public
name|void
name|validateOutputIntent
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSDocument
name|cosDocument
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSBase
name|cBase
init|=
name|catalog
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|DOCUMENT_DICTIONARY_KEY_OUTPUT_INTENTS
argument_list|)
argument_list|)
decl_stmt|;
name|COSArray
name|outputIntents
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|cBase
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Boolean
argument_list|>
name|tmpDestOutputProfile
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|outputIntents
operator|!=
literal|null
operator|&&
name|i
operator|<
name|outputIntents
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|COSDictionary
name|outputIntentDict
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|outputIntents
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|outputIntentDict
operator|==
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"OutputIntent object is null or isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// S entry is mandatory and must be equals to GTS_PDFA1
name|String
name|sValue
init|=
name|outputIntentDict
operator|.
name|getNameAsString
argument_list|(
name|OUTPUT_INTENT_DICTIONARY_KEY_S
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|OUTPUT_INTENT_DICTIONARY_VALUE_GTS_PDFA1
operator|.
name|equals
argument_list|(
name|sValue
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_S_VALUE_INVALID
argument_list|,
literal|"The S entry of the OutputIntent isn't GTS_PDFA1"
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// OutputConditionIdentifier is a mandatory field
name|String
name|outputConditionIdentifier
init|=
name|outputIntentDict
operator|.
name|getString
argument_list|(
name|OUTPUT_INTENT_DICTIONARY_KEY_OUTPUT_CONDITION_IDENTIFIER
argument_list|)
decl_stmt|;
if|if
condition|(
name|outputConditionIdentifier
operator|==
literal|null
condition|)
block|{
comment|// empty string is authorized (it may be an application specific value)
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"The OutputIntentCondition is missing"
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|/*                  * If OutputConditionIdentifier is "Custom" or a non Standard ICC Characterization : DestOutputProfile                  * and Info are mandatory DestOutputProfile must be a ICC Profile                  *                   * Because of PDF/A conforming file needs to specify the color characteristics, the DestOutputProfile is                  * checked even if the OutputConditionIdentifier isn't "Custom"                  */
name|COSBase
name|destOutputProfile
init|=
name|outputIntentDict
operator|.
name|getItem
argument_list|(
name|OUTPUT_INTENT_DICTIONARY_KEY_DEST_OUTPUT_PROFILE
argument_list|)
decl_stmt|;
name|validateICCProfile
argument_list|(
name|destOutputProfile
argument_list|,
name|tmpDestOutputProfile
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|PreflightConfiguration
name|config
init|=
name|ctx
operator|.
name|getConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|isLazyValidation
argument_list|()
operator|&&
operator|!
name|isStandardICCCharacterization
argument_list|(
name|outputConditionIdentifier
argument_list|)
condition|)
block|{
name|String
name|info
init|=
name|outputIntentDict
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|OUTPUT_INTENT_DICTIONARY_KEY_INFO
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|info
argument_list|)
condition|)
block|{
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"The Info entry of a OutputIntent dictionary is missing"
argument_list|)
decl_stmt|;
name|error
operator|.
name|setWarning
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addValidationError
argument_list|(
name|ctx
argument_list|,
name|error
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
block|}
block|}
comment|/**      * This method checks the destOutputProfile which must be a valid ICCProfile.      *       * If another ICCProfile exists in the mapDestOutputProfile, a ValidationError      * (ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_MULTIPLE) is returned because only one profile is authorized. If the      * ICCProfile already exists in the mapDestOutputProfile, the method returns null. If the destOutputProfile contains      * an invalid ICCProfile, a ValidationError (ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_INVALID) is returned. If the      * destOutputProfile is an empty stream, a ValidationError(ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY) is returned.      *       * If the destOutputFile is valid, mapDestOutputProfile is updated, the ICCProfile is added to the document ctx and      * null is returned.      *       * @param destOutputProfile      * @param mapDestOutputProfile      * @param ctx the preflight context.      * @throws ValidationException      */
specifier|protected
name|void
name|validateICCProfile
parameter_list|(
name|COSBase
name|destOutputProfile
parameter_list|,
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Boolean
argument_list|>
name|mapDestOutputProfile
parameter_list|,
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
if|if
condition|(
name|destOutputProfile
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// destOutputProfile should be an instance of COSObject because of this is a object reference
if|if
condition|(
name|destOutputProfile
operator|instanceof
name|COSObject
condition|)
block|{
if|if
condition|(
name|mapDestOutputProfile
operator|.
name|containsKey
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|destOutputProfile
argument_list|)
argument_list|)
condition|)
block|{
comment|// the profile is already checked. continue
return|return;
block|}
elseif|else
if|if
condition|(
operator|!
name|mapDestOutputProfile
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// A DestOutputProfile exits but it isn't the same, error
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_MULTIPLE
argument_list|,
literal|"More than one ICCProfile is defined"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// else the profile will be kept in the tmpDestOutputProfile if it is valid
block|}
comment|// keep reference to avoid multiple profile definition
name|mapDestOutputProfile
operator|.
name|put
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|destOutputProfile
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|COSDocument
name|cosDocument
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSStream
name|stream
init|=
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|destOutputProfile
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"OutputIntent object uses a NULL Object"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|ICC_Profile
name|iccp
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|stream
operator|.
name|createInputStream
argument_list|()
init|)
block|{
name|iccp
operator|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|validateICCProfileNEntry
argument_list|(
name|stream
argument_list|,
name|ctx
argument_list|,
name|iccp
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|validateICCProfileVersion
argument_list|(
name|iccp
argument_list|,
name|ctx
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|ctx
operator|.
name|getIccProfileWrapper
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ctx
operator|.
name|setIccProfileWrapper
argument_list|(
operator|new
name|ICCProfileWrapper
argument_list|(
name|iccp
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// this is not a ICC_Profile
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_INVALID
argument_list|,
literal|"DestOutputProfile isn't a valid ICCProfile: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to parse the ICC Profile."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|validateICCProfileVersion
parameter_list|(
name|ICC_Profile
name|iccp
parameter_list|,
name|PreflightContext
name|ctx
parameter_list|)
block|{
name|PreflightConfiguration
name|config
init|=
name|ctx
operator|.
name|getConfig
argument_list|()
decl_stmt|;
comment|// check the ICC Profile version (6.2.2)
if|if
condition|(
name|iccp
operator|.
name|getMajorVersion
argument_list|()
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|iccp
operator|.
name|getMinorVersion
argument_list|()
operator|>
literal|0x40
condition|)
block|{
comment|// in PDF 1.4, max version is 02h.40h (meaning V 3.5)
comment|// see the ICCProfile specification (ICC.1:1998-09)page 13 - §6.1.3 :
comment|// The current profile version number is "2.4.0" (encoded as 02400000h")
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_TOO_RECENT
argument_list|,
literal|"Invalid version of the ICCProfile"
argument_list|)
decl_stmt|;
name|error
operator|.
name|setWarning
argument_list|(
name|config
operator|.
name|isLazyValidation
argument_list|()
argument_list|)
expr_stmt|;
name|addValidationError
argument_list|(
name|ctx
argument_list|,
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// else OK
block|}
elseif|else
if|if
condition|(
name|iccp
operator|.
name|getMajorVersion
argument_list|()
operator|>
literal|2
condition|)
block|{
comment|// in PDF 1.4, max version is 02h.40h (meaning V 3.5)
comment|// see the ICCProfile specification (ICC.1:1998-09)page 13 - §6.1.3 :
comment|// The current profile version number is "2.4.0" (encoded as 02400000h"
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_TOO_RECENT
argument_list|,
literal|"Invalid version of the ICCProfile"
argument_list|)
decl_stmt|;
name|error
operator|.
name|setWarning
argument_list|(
name|config
operator|.
name|isLazyValidation
argument_list|()
argument_list|)
expr_stmt|;
name|addValidationError
argument_list|(
name|ctx
argument_list|,
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// else seems less than 2, so correct
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|validateICCProfileNEntry
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|PreflightContext
name|ctx
parameter_list|,
name|ICC_Profile
name|iccp
parameter_list|)
block|{
name|COSDictionary
name|streamDict
init|=
operator|(
name|COSDictionary
operator|)
name|stream
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|streamDict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile is mandatory"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|COSBase
name|nValue
init|=
name|streamDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|nValue
operator|instanceof
name|COSNumber
operator|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile must be a number, but is "
operator|+
name|nValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|int
name|nNumberValue
init|=
operator|(
operator|(
name|COSNumber
operator|)
name|nValue
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|nNumberValue
operator|!=
literal|1
operator|&&
name|nNumberValue
operator|!=
literal|3
operator|&&
name|nNumberValue
operator|!=
literal|4
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile must be 1, 3 or 4, but is "
operator|+
name|nNumberValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|iccp
operator|.
name|getNumComponents
argument_list|()
operator|!=
name|nNumberValue
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile is "
operator|+
name|nNumberValue
operator|+
literal|" but the ICC profile has "
operator|+
name|iccp
operator|.
name|getNumComponents
argument_list|()
operator|+
literal|" components"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

