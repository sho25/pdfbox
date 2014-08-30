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
package|;
end_package

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
name|container
operator|.
name|FontContainer
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

begin_class
specifier|public
specifier|abstract
class|class
name|FontValidator
parameter_list|<
name|T
extends|extends
name|FontContainer
parameter_list|>
block|{
specifier|protected
name|T
name|fontContainer
decl_stmt|;
specifier|protected
name|PreflightContext
name|context
decl_stmt|;
specifier|protected
name|FontDescriptorHelper
argument_list|<
name|T
argument_list|>
name|descriptorHelper
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUB_SET_PATTERN
init|=
literal|"^[A-Z]{6}\\+.*"
decl_stmt|;
specifier|public
name|FontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|T
name|fContainer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|fontContainer
operator|=
name|fContainer
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|addFontContainer
argument_list|(
name|dict
argument_list|,
name|fContainer
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|isSubSet
parameter_list|(
name|String
name|fontName
parameter_list|)
block|{
return|return
name|fontName
operator|.
name|matches
argument_list|(
name|SUB_SET_PATTERN
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getSubSetPatternDelimiter
parameter_list|()
block|{
return|return
literal|"\\+"
return|;
block|}
specifier|public
specifier|abstract
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
function_decl|;
specifier|protected
name|void
name|checkEncoding
parameter_list|()
block|{
comment|// nothing to check for PDF/A-1b
block|}
specifier|protected
name|void
name|checkToUnicode
parameter_list|()
block|{
comment|// nothing to check for PDF/A-1b
block|}
specifier|public
name|T
name|getFontContainer
parameter_list|()
block|{
return|return
name|fontContainer
return|;
block|}
block|}
end_class

end_unit

