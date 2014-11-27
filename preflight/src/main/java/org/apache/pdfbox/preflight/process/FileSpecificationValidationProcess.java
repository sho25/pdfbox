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
name|ERROR_SYNTAX_EMBEDDED_FILES
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
name|FILE_SPECIFICATION_KEY_EMBEDDED_FILE
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
name|FILE_SPECIFICATION_VALUE_TYPE
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

begin_comment
comment|/**  *   * This validation process check that FileSpec dictionaries are confirming with the PDF/A-1b specification.  */
end_comment

begin_class
specifier|public
class|class
name|FileSpecificationValidationProcess
extends|extends
name|AbstractProcess
block|{
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
name|pdfDoc
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSDocument
name|cDoc
init|=
name|pdfDoc
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|lCOSObj
init|=
name|cDoc
operator|.
name|getObjects
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|lCOSObj
control|)
block|{
name|COSBase
name|cBase
init|=
operator|(
operator|(
name|COSObject
operator|)
name|o
operator|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|cBase
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|cBase
decl_stmt|;
name|String
name|type
init|=
name|dic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|FILE_SPECIFICATION_VALUE_TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
name|COSName
operator|.
name|F
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// ---- It is a file specification
name|validateFileSpecification
argument_list|(
name|ctx
argument_list|,
name|dic
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Validate a FileSpec dictionary, a FileSpec dictionary mustn't have the EF (EmbeddedFile) entry.      *       * @param ctx the preflight context.      * @param fileSpec the FileSpec Dictionary.      * @return the list of validation errors.      */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validateFileSpecification
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|fileSpec
parameter_list|)
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// ---- Check dictionary entries
comment|// ---- Only the EF entry is forbidden
if|if
condition|(
name|fileSpec
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|FILE_SPECIFICATION_KEY_EMBEDDED_FILE
argument_list|)
argument_list|)
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
name|ERROR_SYNTAX_EMBEDDED_FILES
argument_list|,
literal|"EmbeddedFile entry is present in a FileSpecification dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

