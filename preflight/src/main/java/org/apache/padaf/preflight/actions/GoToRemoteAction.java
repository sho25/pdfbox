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
name|actions
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ACTION_DICTIONARY_KEY_F
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ERROR_ACTION_MISING_KEY
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

begin_comment
comment|/**  * ActionManager for the GoToRemote action GoToRemoteAction is valid if the F  * entry is present.  */
end_comment

begin_class
specifier|public
class|class
name|GoToRemoteAction
extends|extends
name|GoToAction
block|{
comment|/**    *     * @param amFact    *          Instance of ActionManagerFactory used to create ActionManager to    *          check Next actions.    * @param adict    *          the COSDictionary of the action wrapped by this class.    * @param cDoc    *          the COSDocument from which the action comes from.    * @param aaKey    *          The name of the key which identify the action in a additional    *          action dictionary.    */
specifier|public
name|GoToRemoteAction
parameter_list|(
name|ActionManagerFactory
name|amFact
parameter_list|,
name|COSDictionary
name|adict
parameter_list|,
name|COSDocument
name|doc
parameter_list|,
name|String
name|aaKey
parameter_list|)
block|{
name|super
argument_list|(
name|amFact
argument_list|,
name|adict
argument_list|,
name|doc
argument_list|,
name|aaKey
argument_list|)
expr_stmt|;
block|}
comment|/*    * (non-Javadoc)    *     * @see    * net.awl.edoc.pdfa.validation.actions.AbstractActionManager#valid(java.util    * .List)    */
annotation|@
name|Override
specifier|protected
name|boolean
name|innerValid
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|error
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|innerValid
argument_list|(
name|error
argument_list|)
condition|)
block|{
name|COSBase
name|f
init|=
name|this
operator|.
name|actionDictionnary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|ACTION_DICTIONARY_KEY_F
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_MISING_KEY
argument_list|,
literal|"F entry is mandatory for the GoToRemoteActions"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

