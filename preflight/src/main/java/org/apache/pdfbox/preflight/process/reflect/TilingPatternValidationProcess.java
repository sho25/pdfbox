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
operator|.
name|reflect
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
name|ERROR_GRAPHIC_INVALID_PATTERN_DEFINITION
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
name|pattern
operator|.
name|PDTilingPattern
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
name|process
operator|.
name|AbstractProcess
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

begin_class
specifier|public
class|class
name|TilingPatternValidationProcess
extends|extends
name|AbstractProcess
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|context
parameter_list|)
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
if|if
condition|(
name|vPath
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
operator|!
name|vPath
operator|.
name|isExpectedType
argument_list|(
name|PDTilingPattern
operator|.
name|class
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
name|ERROR_GRAPHIC_MISSING_OBJECT
argument_list|,
literal|"Tiling pattern validation required at least a PDPage"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDTilingPattern
name|tilingPattern
init|=
operator|(
name|PDTilingPattern
operator|)
name|vPath
operator|.
name|peek
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
name|vPath
operator|.
name|getClosestPathElement
argument_list|(
name|PDPage
operator|.
name|class
argument_list|)
decl_stmt|;
name|checkMandatoryFields
argument_list|(
name|context
argument_list|,
name|page
argument_list|,
name|tilingPattern
argument_list|)
expr_stmt|;
name|parseResources
argument_list|(
name|context
argument_list|,
name|page
argument_list|,
name|tilingPattern
argument_list|)
expr_stmt|;
name|parsePatternContent
argument_list|(
name|context
argument_list|,
name|page
argument_list|,
name|tilingPattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|parseResources
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDResources
name|resources
init|=
name|pattern
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
comment|/**      * Validate the Pattern content like Color and Show Text Operators using an instance of ContentStreamWrapper.      */
specifier|protected
name|void
name|parsePatternContent
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightContentStream
name|csWrapper
init|=
operator|new
name|PreflightContentStream
argument_list|(
name|context
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|csWrapper
operator|.
name|validPatternContentStream
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method checks if required fields are present.      */
specifier|protected
name|void
name|checkMandatoryFields
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDTilingPattern
name|pattern
parameter_list|)
block|{
name|COSDictionary
name|dictionary
init|=
name|pattern
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
name|boolean
name|res
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|res
operator|=
name|res
operator|&&
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
operator|!=
literal|null
expr_stmt|;
name|res
operator|=
name|res
operator|&&
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|PAINT_TYPE
argument_list|)
operator|!=
literal|null
expr_stmt|;
name|res
operator|=
name|res
operator|&&
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TILING_TYPE
argument_list|)
operator|!=
literal|null
expr_stmt|;
name|res
operator|=
name|res
operator|&&
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|X_STEP
argument_list|)
operator|!=
literal|null
expr_stmt|;
name|res
operator|=
name|res
operator|&&
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|Y_STEP
argument_list|)
operator|!=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|res
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_PATTERN_DEFINITION
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

