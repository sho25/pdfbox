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
name|PDPostScriptXObject
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

begin_class
specifier|public
class|class
name|XObjPostscriptValidator
extends|extends
name|AbstractXObjValidator
block|{
specifier|public
name|XObjPostscriptValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDPostScriptXObject
name|xobj
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|xobj
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see AbstractXObjValidator#validate()      */
annotation|@
name|Override
specifier|protected
name|void
name|checkMandatoryFields
parameter_list|()
block|{
comment|// PostScript XObjects are forbidden. Whatever the result of this function,
comment|// the validation will fail
block|}
block|}
end_class

end_unit

