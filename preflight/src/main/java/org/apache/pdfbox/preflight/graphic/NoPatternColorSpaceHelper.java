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
name|graphic
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
name|ERROR_GRAPHIC_INVALID_PATTERN_COLOR_SPACE_FORBIDDEN
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

begin_comment
comment|/**  * This class defines restrictions on Pattern ColorSpace. It checks the consistency of the Color space with the  * DestOutputIntent, if the color space is a Pattern the validation will fail.  */
end_comment

begin_class
specifier|public
class|class
name|NoPatternColorSpaceHelper
extends|extends
name|StandardColorSpaceHelper
block|{
specifier|public
name|NoPatternColorSpaceHelper
parameter_list|(
name|PreflightContext
name|_context
parameter_list|,
name|PDColorSpace
name|_cs
parameter_list|)
block|{
name|super
argument_list|(
name|_context
argument_list|,
name|_cs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method updates the given list with a ValidationError (ERROR_GRAPHIC_INVALID_PATTERN_COLOR_SPACE_FORBIDDEN)      * and returns false.      */
specifier|protected
name|void
name|processPatternColorSpace
parameter_list|(
name|PDColorSpace
name|pdcs
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_PATTERN_COLOR_SPACE_FORBIDDEN
argument_list|,
literal|"Pattern color space is forbidden"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

