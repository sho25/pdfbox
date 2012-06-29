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
name|exception
package|;
end_package

begin_class
specifier|public
class|class
name|MissingValidationProcessException
extends|extends
name|ValidationException
block|{
comment|/** 	 * Name of the Missing process 	 */
specifier|private
name|String
name|processName
decl_stmt|;
specifier|public
name|MissingValidationProcessException
parameter_list|(
name|String
name|process
parameter_list|)
block|{
name|super
argument_list|(
name|process
operator|+
literal|" is missing, validation can't be done"
argument_list|)
expr_stmt|;
name|this
operator|.
name|processName
operator|=
name|process
expr_stmt|;
block|}
specifier|public
name|String
name|getProcessName
parameter_list|()
block|{
return|return
name|processName
return|;
block|}
block|}
end_class

end_unit

