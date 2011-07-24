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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_interface
specifier|public
interface|interface
name|PdfAValidator
block|{
comment|/**    * Compute the validation of the given PDF file. If the PDF is valid,    * ValidationResult contains no error and the method isValid return true.    * Otherwise, the ValidationResult contains at least one ValidationError and    * the method isValid return false.    *     * @param source    *          DataSource which represents the PDF file.    * @return an instance of ValidationResult    * @throws ValidationException    */
name|ValidationResult
name|validate
parameter_list|(
name|DataSource
name|source
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**    * Return the version qualified name of the product    */
name|String
name|getFullName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

