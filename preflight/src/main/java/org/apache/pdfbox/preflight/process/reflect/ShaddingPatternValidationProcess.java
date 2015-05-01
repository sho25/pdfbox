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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|EXTGSTATE_PROCESS
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
name|ERROR_GRAPHIC_INVALID_UNKNOWN_COLOR_SPACE
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
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
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
name|shading
operator|.
name|PDShading
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
name|ColorSpaceHelper
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
name|ColorSpaceHelperFactory
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
name|ColorSpaceHelperFactory
operator|.
name|ColorSpaceRestriction
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
name|ShaddingPatternValidationProcess
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
if|if
condition|(
operator|!
name|vPath
operator|.
name|isExpectedType
argument_list|(
name|PDShading
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
literal|"ShadingPattern validation required at least a PDResources"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDShading
name|shaddingResource
init|=
operator|(
name|PDShading
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
name|checkColorSpace
argument_list|(
name|context
argument_list|,
name|page
argument_list|,
name|shaddingResource
argument_list|)
expr_stmt|;
name|checkGraphicState
argument_list|(
name|context
argument_list|,
name|page
argument_list|,
name|shaddingResource
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Checks if the ColorSapce entry is consistent which rules of the PDF Reference and the ISO 190005-1:2005      * Specification.      *       * This method is called by the validate method.      *       * @param context the preflight context.      * @param page the page to check.      * @param shadingRes the Shading pattern to check.      * @throws ValidationException      */
specifier|protected
name|void
name|checkColorSpace
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDShading
name|shadingRes
parameter_list|)
throws|throws
name|ValidationException
block|{
try|try
block|{
name|PDColorSpace
name|pColorSpace
init|=
name|shadingRes
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|PreflightConfiguration
name|config
init|=
name|context
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|ColorSpaceHelperFactory
name|csFact
init|=
name|config
operator|.
name|getColorSpaceHelperFact
argument_list|()
decl_stmt|;
name|ColorSpaceHelper
name|csh
init|=
name|csFact
operator|.
name|getColorSpaceHelper
argument_list|(
name|context
argument_list|,
name|pColorSpace
argument_list|,
name|ColorSpaceRestriction
operator|.
name|NO_PATTERN
argument_list|)
decl_stmt|;
name|csh
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_UNKNOWN_COLOR_SPACE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check the Extended Graphic State contains in the ShadingPattern dictionary if it is present. To check this      * ExtGState, this method uses the net.awl.edoc.pdfa.validation.graphics.ExtGStateContainer object.      *       * @param context the preflight context.      * @param page the page to check.      * @param shadingRes the Shading pattern to check.      * @throws ValidationException      */
specifier|protected
name|void
name|checkGraphicState
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDShading
name|shadingRes
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSDictionary
name|resources
init|=
operator|(
name|COSDictionary
operator|)
name|shadingRes
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|TRANPARENCY_DICTIONARY_KEY_EXTGSTATE
argument_list|)
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
name|EXTGSTATE_PROCESS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

