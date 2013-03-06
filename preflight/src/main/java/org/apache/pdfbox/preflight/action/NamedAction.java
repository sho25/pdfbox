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
name|action
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
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_FIRST
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
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_LAST
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
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_NEXT
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
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_PREV
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
name|ERROR_ACTION_FORBIDDEN_ACTIONS_NAMED
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
name|ERROR_ACTION_MISING_KEY
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
comment|/**  * ActionManager for the Named action. Named action is valid if N entry is present with one of the four values :  *<UL>  *<li>NextPage  *<li>PrevPage  *<li>FirstPage  *<li>LastPage  *</UL>  */
end_comment

begin_class
specifier|public
class|class
name|NamedAction
extends|extends
name|AbstractActionManager
block|{
comment|/**      * @param amFact      *            Instance of ActionManagerFactory used to create ActionManager to check Next actions.      * @param adict      *            the COSDictionary of the action wrapped by this class.      * @param cDoc      *            the COSDocument from which the action comes from.      * @param aaKey      *            The name of the key which identify the action in a additional action dictionary.      */
specifier|public
name|NamedAction
parameter_list|(
name|ActionManagerFactory
name|amFact
parameter_list|,
name|COSDictionary
name|adict
parameter_list|,
name|PreflightContext
name|ctx
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
name|ctx
argument_list|,
name|aaKey
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.actions.AbstractActionManager#valid(java.util .List)      */
annotation|@
name|Override
specifier|protected
name|boolean
name|innerValid
parameter_list|()
block|{
name|String
name|n
init|=
name|this
operator|.
name|actionDictionnary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
comment|// ---- N entry is mandatory
if|if
condition|(
name|n
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|n
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
name|ERROR_ACTION_MISING_KEY
argument_list|,
literal|"N entry is mandatory for the NamedActions"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// ---- Only Predefine name actions are authorized
if|if
condition|(
operator|!
operator|(
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_FIRST
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|||
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_LAST
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|||
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_NEXT
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|||
name|ACTION_DICTIONARY_VALUE_ATYPE_NAMED_PREV
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ACTION_FORBIDDEN_ACTIONS_NAMED
argument_list|,
name|n
operator|+
literal|" isn't authorized as named action"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

