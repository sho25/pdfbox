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
name|xobject
package|;
end_package

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
name|RESOURCES_PROCESS
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
name|ERROR_GRAPHIC_INVALID_BBOX
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
name|ERROR_GRAPHIC_MISSING_FIELD
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
name|ERROR_GRAPHIC_TRANSPARENCY_GROUP
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
name|ERROR_GRAPHIC_UNEXPECTED_KEY
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
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
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
name|XOBJECT_DICTIONARY_KEY_GROUP
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
name|XOBJECT_DICTIONARY_VALUE_S_TRANSPARENCY
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
name|PDPage
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
name|graphics
operator|.
name|form
operator|.
name|PDFormXObject
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
name|PreflightPath
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
name|content
operator|.
name|PreflightContentStream
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

begin_comment
comment|/**  * This class validates XObject with the Form subtype.  */
end_comment

begin_class
specifier|public
class|class
name|XObjFormValidator
extends|extends
name|AbstractXObjValidator
block|{
comment|/**      * High level object which represents the XObjectForm      */
name|PDFormXObject
name|pdXObj
init|=
literal|null
decl_stmt|;
specifier|public
name|XObjFormValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFormXObject
name|xobj
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|xobj
operator|.
name|getCOSStream
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdXObj
operator|=
name|xobj
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.graphics.AbstractXObjValidator#validate()      */
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|super
operator|.
name|validate
argument_list|()
expr_stmt|;
name|checkGroup
argument_list|()
expr_stmt|;
name|checkSubtype2Value
argument_list|()
expr_stmt|;
name|validateXObjectResources
argument_list|()
expr_stmt|;
name|validateXObjectContent
argument_list|()
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @seenet.awl.edoc.pdfa.validation.graphics.AbstractXObjValidator# checkMandatoryFields(java.util.List)      */
annotation|@
name|Override
specifier|protected
name|void
name|checkMandatoryFields
parameter_list|()
block|{
name|boolean
name|lastMod
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|LAST_MODIFIED
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|boolean
name|pieceInfo
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
literal|"PieceInfo"
argument_list|)
operator|!=
literal|null
decl_stmt|;
comment|// type and subtype checked before to create the Validator.
if|if
condition|(
name|lastMod
operator|^
name|pieceInfo
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_MISSING_FIELD
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSBase
name|bbBase
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
decl_stmt|;
comment|// ---- BBox is an Array (Rectangle)
if|if
condition|(
name|bbBase
operator|==
literal|null
operator|||
operator|!
name|COSUtils
operator|.
name|isArray
argument_list|(
name|bbBase
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_BBOX
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/**      * An Form XObject is a ContentStream. This method method uses an instance of ContentStreamWrapper to check the      * Stream of this Form XObject.      */
specifier|protected
name|void
name|validateXObjectContent
parameter_list|()
throws|throws
name|ValidationException
block|{
name|PreflightPath
name|vPath
init|=
name|context
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
name|PreflightContentStream
name|csWrapper
init|=
operator|new
name|PreflightContentStream
argument_list|(
name|context
argument_list|,
name|vPath
operator|.
name|getClosestPathElement
argument_list|(
name|PDPage
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|csWrapper
operator|.
name|validXObjContentStream
argument_list|(
name|pdXObj
argument_list|)
expr_stmt|;
block|}
comment|/**      * A Form XObject may contain a Group object (Key =" Group"). If a Group object is present, this method checks if      * the S entry is present and if its value is different from "Transparency".      *       */
specifier|protected
name|void
name|checkGroup
parameter_list|()
block|{
name|COSBase
name|baseGroup
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|XOBJECT_DICTIONARY_KEY_GROUP
argument_list|)
decl_stmt|;
name|COSDictionary
name|groupDictionary
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|baseGroup
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupDictionary
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|XOBJECT_DICTIONARY_KEY_GROUP
operator|.
name|equals
argument_list|(
name|groupDictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_MISSING_FIELD
argument_list|,
literal|"The Group dictionary hasn't Group as Type value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|sVal
init|=
name|groupDictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|S
argument_list|)
decl_stmt|;
if|if
condition|(
name|sVal
operator|==
literal|null
operator|||
name|XOBJECT_DICTIONARY_VALUE_S_TRANSPARENCY
operator|.
name|equals
argument_list|(
name|sVal
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_TRANSPARENCY_GROUP
argument_list|,
literal|"Group has a transparency S entry or the S entry is null ["
operator|+
name|xobject
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
comment|/**      * Check if there are no PS entry in the Form XObject dictionary      */
specifier|protected
name|void
name|checkPS
parameter_list|()
block|{
comment|// 6.2.4 and 6.2.5 no PS
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"PS"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|,
literal|"Unexpected 'PS' Key"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/**      * Check the SUbtype2 entry according to the §6.2.5 of the ISO 190005-1:2005 specification.      */
specifier|protected
name|void
name|checkSubtype2Value
parameter_list|()
block|{
comment|// 6.2.5 if Subtype2, value not PS
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Subtype2"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"PS"
operator|.
name|equals
argument_list|(
name|this
operator|.
name|xobject
operator|.
name|getNameAsString
argument_list|(
literal|"Subtype2"
argument_list|)
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"Unexpected 'PS' value for 'Subtype2' Key"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
specifier|protected
name|void
name|validateXObjectResources
parameter_list|()
throws|throws
name|ValidationException
block|{
name|PDResources
name|resources
init|=
name|this
operator|.
name|pdXObj
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|context
argument_list|,
name|resources
argument_list|,
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

