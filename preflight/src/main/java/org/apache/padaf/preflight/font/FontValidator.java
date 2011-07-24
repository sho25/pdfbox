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
name|padaf
operator|.
name|preflight
operator|.
name|font
package|;
end_package

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
name|preflight
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
name|padaf
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
name|padaf
operator|.
name|preflight
operator|.
name|font
operator|.
name|AbstractFontContainer
operator|.
name|State
import|;
end_import

begin_interface
specifier|public
interface|interface
name|FontValidator
block|{
comment|/**    * Call this method to validate the font wrapped by this interface    * implementation. Return true if the validation succeed, false otherwise. If    * the validation failed, the error is updated in the FontContainer with the    * right error code.    *     * @return    */
specifier|public
specifier|abstract
name|boolean
name|validate
parameter_list|()
throws|throws
name|ValidationException
function_decl|;
comment|/**    * Return the State of the Font Validation. Three values are possible :    *<UL>    *<li>VALID : there are no errors    *<li>MAYBE : Metrics aren't consistent of the FontProgram isn't embedded,    * but it can be valid.    *<li>INVALID : the validation fails    *</UL>    *     * @return    */
specifier|public
name|State
name|getState
parameter_list|()
function_decl|;
comment|/**    * Return all validation errors.    *     * @return    */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getValdiationErrors
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

