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
name|ACTIONS_PROCESS
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
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
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
name|PDDocumentCatalog
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|outline
operator|.
name|PDDocumentOutline
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|outline
operator|.
name|PDOutlineItem
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
name|utils
operator|.
name|COSUtils
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
name|BookmarkValidationProcess
extends|extends
name|AbstractProcess
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
name|PDDocumentOutline
name|outlineHierarchy
init|=
name|catalog
operator|.
name|getDocumentOutline
argument_list|()
decl_stmt|;
if|if
condition|(
name|outlineHierarchy
operator|!=
literal|null
condition|)
block|{
comment|// Count entry is mandatory if there are childrens
if|if
condition|(
operator|!
name|isCountEntryPresent
argument_list|(
name|outlineHierarchy
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
operator|&&
operator|(
name|outlineHierarchy
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
operator|||
name|outlineHierarchy
operator|.
name|getLastChild
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|,
literal|"Outline Hierarchy doesn't have Count entry"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isCountEntryPositive
argument_list|(
name|ctx
argument_list|,
name|outlineHierarchy
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
operator|&&
operator|(
name|outlineHierarchy
operator|.
name|getFirstChild
argument_list|()
operator|==
literal|null
operator|||
name|outlineHierarchy
operator|.
name|getLastChild
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|,
literal|"Outline Hierarchy doesn't have First and/or Last entry(ies)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exploreOutlineLevel
argument_list|(
name|ctx
argument_list|,
name|outlineHierarchy
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"There are no Catalog entry in the Document."
argument_list|)
throw|;
block|}
block|}
comment|/** 	 * Return true if the Count entry is present in the given dictionary. 	 *  	 * @param outline 	 * @return 	 */
specifier|private
name|boolean
name|isCountEntryPresent
parameter_list|(
name|COSDictionary
name|outline
parameter_list|)
block|{
return|return
name|outline
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Count"
argument_list|)
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/** 	 * return true if Count entry> 0 	 * @param outline 	 * @param doc 	 * @return 	 */
specifier|private
name|boolean
name|isCountEntryPositive
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|outline
parameter_list|)
block|{
name|COSBase
name|countBase
init|=
name|outline
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Count"
argument_list|)
argument_list|)
decl_stmt|;
name|COSDocument
name|cosDocument
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
return|return
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|countBase
argument_list|,
name|cosDocument
argument_list|)
operator|&&
operator|(
name|COSUtils
operator|.
name|getAsInteger
argument_list|(
name|countBase
argument_list|,
name|cosDocument
argument_list|)
operator|>
literal|0
operator|)
return|;
block|}
comment|/** 	 * This method explores the Outline Item Level and call a validation method on 	 * each Outline Item. If an invalid outline item is found, the result list is 	 * updated. 	 *  	 * @param inputItem 	 *          The first outline item of the level 	 * @param ctx 	 *          The document handler which provides useful data for the level 	 *          exploration (ex : access to the PDDocument) 	 * @return true if all items are valid in this level. 	 * @throws ValidationException 	 */
specifier|protected
name|boolean
name|exploreOutlineLevel
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|PDOutlineItem
name|inputItem
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDOutlineItem
name|currentItem
init|=
name|inputItem
decl_stmt|;
while|while
condition|(
name|currentItem
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|validateItem
argument_list|(
name|ctx
argument_list|,
name|currentItem
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|currentItem
operator|=
name|currentItem
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/** 	 * This method checks the inputItem dictionary and call the 	 * exploreOutlineLevel method on the first child if it is not null. 	 *  	 * @param inputItem 	 *          outline item to validate 	 * @param ctx 	 *          The document handler which provides useful data for the level 	 *          exploration (ex : access to the PDDocument) 	 * @param result 	 * @return 	 * @throws ValidationException 	 */
specifier|protected
name|boolean
name|validateItem
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|PDOutlineItem
name|inputItem
parameter_list|)
throws|throws
name|ValidationException
block|{
name|boolean
name|isValid
init|=
literal|true
decl_stmt|;
comment|// Dest entry isn't permitted if the A entry is present
comment|// A entry isn't permitted if the Dest entry is present
comment|// If the A enntry is present, the referenced actions is validated
name|COSDictionary
name|dictionary
init|=
name|inputItem
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
name|COSBase
name|dest
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DEST
argument_list|)
decl_stmt|;
name|COSBase
name|action
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|!=
literal|null
operator|&&
name|dest
operator|!=
literal|null
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|,
literal|"Dest entry isn't permitted if the A entry is present"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|action
operator|!=
literal|null
condition|)
block|{
name|ContextHelper
operator|.
name|validateElement
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|ACTIONS_PROCESS
argument_list|)
expr_stmt|;
block|}
comment|// else no specific validation
comment|// check children
name|PDOutlineItem
name|fChild
init|=
name|inputItem
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|fChild
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isCountEntryPresent
argument_list|(
name|inputItem
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|,
literal|"Outline item doesn't have Count entry but has at least one descendant."
argument_list|)
argument_list|)
expr_stmt|;
name|isValid
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// there are some descendants, so dictionary must have a Count entry
name|isValid
operator|=
name|isValid
operator|&&
name|exploreOutlineLevel
argument_list|(
name|ctx
argument_list|,
name|fChild
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|isValid
return|;
block|}
block|}
end_class

end_unit

