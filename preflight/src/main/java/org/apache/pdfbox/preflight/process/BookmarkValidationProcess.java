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
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ERROR_SYNTAX_NOCATALOG
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
name|cos
operator|.
name|COSNull
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
name|COSObject
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
name|DESTINATION_PROCESS
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
annotation|@
name|Override
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
name|COSDictionary
name|dict
init|=
name|outlineHierarchy
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|checkIndirectObjects
argument_list|(
name|ctx
argument_list|,
name|dict
argument_list|)
condition|)
block|{
return|return;
block|}
name|COSObject
name|firstObj
init|=
name|toCOSObject
argument_list|(
name|dict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|)
argument_list|)
decl_stmt|;
name|COSObject
name|lastObj
init|=
name|toCOSObject
argument_list|(
name|dict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|LAST
argument_list|)
argument_list|)
decl_stmt|;
comment|// Count entry is mandatory if there are childrens
if|if
condition|(
operator|!
name|isCountEntryPresent
argument_list|(
name|dict
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
name|dict
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
argument_list|,
name|firstObj
argument_list|,
name|lastObj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_NOCATALOG
argument_list|,
literal|"There is no /Catalog entry in the Document"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Return true if the Count entry is present in the given dictionary.      *       * @param outline the dictionary representing the document outline.      * @return true if the Count entry is present.      */
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
name|COUNT
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * return true if Count entry&gt; 0      *       * @param ctx the preflight context.      * @param outline the dictionary representing the document outline.      * @return true if the Count entry&gt; 0.      */
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
name|COUNT
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
comment|/**      * This method explores the Outline Item Level and calls a validation method on each Outline Item. If an invalid      * outline item is found, the result list is updated.      *       * @param ctx the preflight context.      * @param inputItem The first outline item of the level.      * @param firstObj The first PDF object of the level.      * @param lastObj The last PDF object of the level.      * @return true if all items are valid in this level.      * @throws ValidationException      */
specifier|protected
name|boolean
name|exploreOutlineLevel
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|PDOutlineItem
name|inputItem
parameter_list|,
name|COSObject
name|firstObj
parameter_list|,
name|COSObject
name|lastObj
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDOutlineItem
name|currentItem
init|=
name|inputItem
decl_stmt|;
name|COSObject
name|currentObj
init|=
name|firstObj
decl_stmt|;
name|Set
argument_list|<
name|COSObject
argument_list|>
name|levelObjects
init|=
operator|new
name|HashSet
argument_list|<
name|COSObject
argument_list|>
argument_list|()
decl_stmt|;
name|levelObjects
operator|.
name|add
argument_list|(
name|firstObj
argument_list|)
expr_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|currentItem
operator|!=
literal|null
operator|&&
name|inputItem
operator|.
name|getPreviousSibling
argument_list|()
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
literal|"The value of /Prev of first object "
operator|+
name|firstObj
operator|+
literal|" on a level is "
operator|+
name|inputItem
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|PREV
argument_list|)
operator|+
literal|", but shouldn't exist"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
while|while
condition|(
name|currentItem
operator|!=
literal|null
condition|)
block|{
name|COSObject
name|realPrevObject
init|=
name|currentObj
decl_stmt|;
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
name|result
operator|=
literal|false
expr_stmt|;
block|}
name|currentObj
operator|=
name|toCOSObject
argument_list|(
name|currentItem
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|NEXT
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|levelObjects
operator|.
name|contains
argument_list|(
name|currentObj
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
literal|"Loop detected: /Next "
operator|+
name|currentObj
operator|+
literal|" is already in the list"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|levelObjects
operator|.
name|add
argument_list|(
name|currentObj
argument_list|)
expr_stmt|;
name|currentItem
operator|=
name|currentItem
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
if|if
condition|(
name|currentItem
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|realPrevObject
operator|.
name|equals
argument_list|(
name|lastObj
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
literal|"Last object on a level isn't the expected /Last: "
operator|+
name|lastObj
operator|+
literal|", but is "
operator|+
name|currentObj
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
name|COSObject
name|prevObject
init|=
name|toCOSObject
argument_list|(
name|currentItem
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|PREV
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|realPrevObject
operator|.
name|equals
argument_list|(
name|prevObject
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
literal|"The value of /Prev at "
operator|+
name|currentObj
operator|+
literal|" doesn't point to previous object "
operator|+
name|realPrevObject
operator|+
literal|", but to "
operator|+
name|prevObject
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * This method checks the inputItem dictionary and call the exploreOutlineLevel method on the first child if it is      * not null.      *       * @param ctx the preflight context.      * @param inputItem outline item to validate      * @return the validation result.      * @throws ValidationException      */
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
comment|// If the A entry is present, the referenced actions is validated
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
operator|!
name|checkIndirectObjects
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
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
elseif|else
if|if
condition|(
name|dest
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
name|dest
argument_list|,
name|DESTINATION_PROCESS
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
literal|"Outline item doesn't have Count entry but has at least one descendant"
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
name|COSObject
name|firstObj
init|=
name|toCOSObject
argument_list|(
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|)
argument_list|)
decl_stmt|;
name|COSObject
name|lastObj
init|=
name|toCOSObject
argument_list|(
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|LAST
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|firstObj
operator|==
literal|null
operator|&&
name|lastObj
operator|!=
literal|null
operator|)
operator|||
operator|(
name|firstObj
operator|!=
literal|null
operator|&&
name|lastObj
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
literal|"/First and /Last are both required if there are outline entries"
argument_list|)
argument_list|)
expr_stmt|;
name|isValid
operator|=
literal|false
expr_stmt|;
block|}
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
argument_list|,
name|firstObj
argument_list|,
name|lastObj
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|isValid
return|;
block|}
comment|// verify that if certain named items exist, that they are indirect objects
specifier|private
name|boolean
name|checkIndirectObjects
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|dictionary
parameter_list|)
block|{
comment|// Parent, Prev, Next, First and Last must be indirect objects
if|if
condition|(
operator|!
name|checkIndirectObject
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|COSName
operator|.
name|PARENT
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|checkIndirectObject
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|COSName
operator|.
name|PREV
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|checkIndirectObject
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|COSName
operator|.
name|NEXT
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|checkIndirectObject
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|COSName
operator|.
name|FIRST
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|checkIndirectObject
argument_list|(
name|ctx
argument_list|,
name|dictionary
argument_list|,
name|COSName
operator|.
name|LAST
argument_list|)
return|;
block|}
comment|// verify that if the named item exists, that it is is an indirect object
specifier|private
name|boolean
name|checkIndirectObject
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|dictionary
parameter_list|,
name|COSName
name|name
parameter_list|)
block|{
name|COSBase
name|item
init|=
name|dictionary
operator|.
name|getItem
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|==
literal|null
operator|||
name|item
operator|instanceof
name|COSNull
operator|||
name|item
operator|instanceof
name|COSObject
condition|)
block|{
return|return
literal|true
return|;
block|}
name|addValidationError
argument_list|(
name|ctx
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|,
literal|"/"
operator|+
name|name
operator|.
name|getName
argument_list|()
operator|+
literal|" entry must be an indirect object"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/**      * Returns a COSBase as a COSObject or null if null or COSNull. To avoid      * trouble, this method is to be called only after having called      * {@link #checkIndirectObjects()}.      *      * @param base should be null, COSNull or a COSObject.      * @return null if the parameter is COSNull or null; or else a COSObject.      * @throws IllegalArgumentException if the parameter is not null, COSNull or      * a COSObject.      */
specifier|private
name|COSObject
name|toCOSObject
parameter_list|(
name|COSBase
name|base
parameter_list|)
block|{
if|if
condition|(
name|base
operator|==
literal|null
operator|||
name|base
operator|instanceof
name|COSNull
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSObject
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Paremater "
operator|+
name|base
operator|+
literal|" should be null, COSNull or a COSObject"
argument_list|)
throw|;
block|}
return|return
operator|(
name|COSObject
operator|)
name|base
return|;
block|}
block|}
end_class

end_unit

