begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|documentinterchange
operator|.
name|logicalstructure
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|COSArray
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
name|COSInteger
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
name|PDPage
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
name|documentinterchange
operator|.
name|markedcontent
operator|.
name|PDMarkedContent
import|;
end_import

begin_comment
comment|/**  * A structure element.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PDStructureElement
extends|extends
name|PDStructureNode
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"StructElem"
decl_stmt|;
comment|/**      * Constructor with required values.      *      * @param structureType the structure type      * @param parent the parent structure node      */
specifier|public
name|PDStructureElement
parameter_list|(
name|String
name|structureType
parameter_list|,
name|PDStructureNode
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|TYPE
argument_list|)
expr_stmt|;
name|this
operator|.
name|setStructureType
argument_list|(
name|structureType
argument_list|)
expr_stmt|;
name|this
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for an existing structure element.      *      * @param dic The existing dictionary.      */
specifier|public
name|PDStructureElement
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|super
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the structure type (S).      *       * @return the structure type      */
specifier|public
name|String
name|getStructureType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|S
argument_list|)
return|;
block|}
comment|/**      * Sets the structure type (S).      *       * @param structureType the structure type      */
specifier|public
name|void
name|setStructureType
parameter_list|(
name|String
name|structureType
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|structureType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the parent in the structure hierarchy (P).      *       * @return the parent in the structure hierarchy      */
specifier|public
name|PDStructureNode
name|getParent
parameter_list|()
block|{
name|COSDictionary
name|p
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|PDStructureNode
operator|.
name|create
argument_list|(
operator|(
name|COSDictionary
operator|)
name|p
argument_list|)
return|;
block|}
comment|/**      * Sets the parent in the structure hierarchy (P).      *       * @param structureNode the parent in the structure hierarchy      */
specifier|public
name|void
name|setParent
parameter_list|(
name|PDStructureNode
name|structureNode
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
name|structureNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the element identifier (ID).      *       * @return the element identifier      */
specifier|public
name|String
name|getElementIdentifier
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ID
argument_list|)
return|;
block|}
comment|/**      * Sets the element identifier (ID).      *       * @param id the element identifier      */
specifier|public
name|void
name|setElementIdentifier
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page on which some or all of the content items designated by      *  the K entry shall be rendered (Pg).      *       * @return the page on which some or all of the content items designated by      *  the K entry shall be rendered      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
name|COSDictionary
name|pageDic
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PG
argument_list|)
decl_stmt|;
if|if
condition|(
name|pageDic
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|PDPage
argument_list|(
name|pageDic
argument_list|)
return|;
block|}
comment|/**      * Sets the page on which some or all of the content items designated by      *  the K entry shall be rendered (Pg).      * @param page the page on which some or all of the content items designated      *  by the K entry shall be rendered.      */
specifier|public
name|void
name|setPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PG
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the class names together with their revision numbers (C).      *       * @return the class names      */
specifier|public
name|Revisions
argument_list|<
name|String
argument_list|>
name|getClassNames
parameter_list|()
block|{
name|COSName
name|key
init|=
name|COSName
operator|.
name|C
decl_stmt|;
name|Revisions
argument_list|<
name|String
argument_list|>
name|classNames
init|=
operator|new
name|Revisions
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|COSBase
name|c
init|=
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSName
condition|)
block|{
name|classNames
operator|.
name|addObject
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|c
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|c
decl_stmt|;
name|Iterator
argument_list|<
name|COSBase
argument_list|>
name|it
init|=
name|array
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|className
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSBase
name|item
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|item
operator|instanceof
name|COSName
condition|)
block|{
name|className
operator|=
operator|(
operator|(
name|COSName
operator|)
name|item
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|classNames
operator|.
name|addObject
argument_list|(
name|className
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|item
operator|instanceof
name|COSInteger
condition|)
block|{
name|classNames
operator|.
name|setRevisionNumber
argument_list|(
name|className
argument_list|,
operator|(
operator|(
name|COSInteger
operator|)
name|item
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|classNames
return|;
block|}
comment|/**      * Sets the class names together with their revision numbers (C).      *       * @param classNames the class names      */
specifier|public
name|void
name|setClassNames
parameter_list|(
name|Revisions
argument_list|<
name|String
argument_list|>
name|classNames
parameter_list|)
block|{
name|COSName
name|key
init|=
name|COSName
operator|.
name|C
decl_stmt|;
if|if
condition|(
operator|(
name|classNames
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|)
operator|&&
operator|(
name|classNames
operator|.
name|getRevisionNumber
argument_list|(
literal|0
argument_list|)
operator|==
literal|0
operator|)
condition|)
block|{
name|String
name|className
init|=
name|classNames
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|key
argument_list|,
name|className
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|classNames
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|className
init|=
name|classNames
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|revisionNumber
init|=
name|classNames
operator|.
name|getRevisionNumber
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|revisionNumber
operator|<
literal|0
condition|)
block|{
comment|// TODO throw Exception because revision number must be> -1?
block|}
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|revisionNumber
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a class name.      *       * @param className the class name      */
specifier|public
name|void
name|addClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|COSName
name|key
init|=
name|COSName
operator|.
name|C
decl_stmt|;
name|COSBase
name|c
init|=
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
name|array
operator|=
operator|(
name|COSArray
operator|)
name|c
expr_stmt|;
block|}
else|else
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|this
operator|.
name|getRevisionNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a class name.      *       * @param className the class name      */
specifier|public
name|void
name|removeClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|COSName
name|key
init|=
name|COSName
operator|.
name|C
decl_stmt|;
name|COSBase
name|c
init|=
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|COSName
name|name
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|c
decl_stmt|;
name|array
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|2
operator|)
operator|&&
operator|(
name|array
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
operator|==
literal|0
operator|)
condition|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|COSBase
name|directC
init|=
name|c
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSObject
condition|)
block|{
name|directC
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|c
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|directC
argument_list|)
condition|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the revision number (R).      *       * @return the revision number      */
specifier|public
name|int
name|getRevisionNumber
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Sets the revision number (R).      *       * @param revisionNumber the revision number      */
specifier|public
name|void
name|setRevisionNumber
parameter_list|(
name|int
name|revisionNumber
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|revisionNumber
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the title (T).      *       * @return the title      */
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|T
argument_list|)
return|;
block|}
comment|/**      * Sets the title (T).      *       * @param title the title      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|T
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the language (Lang).      *       * @return the language      */
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|LANG
argument_list|)
return|;
block|}
comment|/**      * Sets the language (Lang).      *       * @param language the language      */
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|LANG
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the alternate description (Alt).      *       * @return the alternate description      */
specifier|public
name|String
name|getAlternateDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ALT
argument_list|)
return|;
block|}
comment|/**      * Sets the alternate description (Alt).      *       * @param alternateDescription the alternate description      */
specifier|public
name|void
name|setAlternateDescription
parameter_list|(
name|String
name|alternateDescription
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|ALT
argument_list|,
name|alternateDescription
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the expanded form (E).      *       * @return the expanded form      */
specifier|public
name|String
name|getExpandedForm
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|E
argument_list|)
return|;
block|}
comment|/**      * Sets the expanded form (E).      *       * @param expandedForm the expanded form      */
specifier|public
name|void
name|setExpandedForm
parameter_list|(
name|String
name|expandedForm
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|E
argument_list|,
name|expandedForm
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the actual text (ActualText).      *       * @return the actual text      */
specifier|public
name|String
name|getActualText
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ACTUAL_TEXT
argument_list|)
return|;
block|}
comment|/**      * Sets the actual text (ActualText).      *       * @param actualText the actual text      */
specifier|public
name|void
name|setActualText
parameter_list|(
name|String
name|actualText
parameter_list|)
block|{
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|ACTUAL_TEXT
argument_list|,
name|actualText
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the standard structure type, the actual structure type is mapped      * to in the role map.      *       * @return the standard structure type      */
specifier|public
name|String
name|getStandardStructureType
parameter_list|()
block|{
name|String
name|type
init|=
name|this
operator|.
name|getStructureType
argument_list|()
decl_stmt|;
name|String
name|mappedType
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|mappedType
operator|=
name|this
operator|.
name|getRoleMap
argument_list|()
operator|.
name|get
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|mappedType
operator|==
literal|null
operator|)
operator|||
name|type
operator|.
name|equals
argument_list|(
name|mappedType
argument_list|)
condition|)
block|{
break|break;
block|}
name|type
operator|=
name|mappedType
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
comment|/**      * Appends a marked-content sequence kid.      *       * @param markedContent the marked-content sequence      */
specifier|public
name|void
name|appendKid
parameter_list|(
name|PDMarkedContent
name|markedContent
parameter_list|)
block|{
name|this
operator|.
name|appendKid
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|markedContent
operator|.
name|getMCID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a marked-content reference kid.      *       * @param markedContentReference the marked-content reference      */
specifier|public
name|void
name|appendKid
parameter_list|(
name|PDMarkedContentReference
name|markedContentReference
parameter_list|)
block|{
name|this
operator|.
name|appendObjectableKid
argument_list|(
name|markedContentReference
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends an object reference kid.      *       * @param objectReference the object reference      */
specifier|public
name|void
name|appendKid
parameter_list|(
name|PDObjectReference
name|objectReference
parameter_list|)
block|{
name|this
operator|.
name|appendObjectableKid
argument_list|(
name|objectReference
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts a marked-content identifier kid before a reference kid.      *       * @param markedContentIdentifier the marked-content identifier      * @param refKid the reference kid      */
specifier|public
name|void
name|insertBefore
parameter_list|(
name|COSInteger
name|markedContentIdentifier
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
name|this
operator|.
name|insertBefore
argument_list|(
name|markedContentIdentifier
argument_list|,
name|refKid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts a marked-content reference kid before a reference kid.      *       * @param markedContentReference the marked-content reference      * @param refKid the reference kid      */
specifier|public
name|void
name|insertBefore
parameter_list|(
name|PDMarkedContentReference
name|markedContentReference
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
name|this
operator|.
name|insertBefore
argument_list|(
name|markedContentReference
argument_list|,
name|refKid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts an object reference kid before a reference kid.      *       * @param objectReference the object reference      * @param refKid the reference kid      */
specifier|public
name|void
name|insertBefore
parameter_list|(
name|PDObjectReference
name|objectReference
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
name|this
operator|.
name|insertBefore
argument_list|(
name|objectReference
argument_list|,
name|refKid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a marked-content identifier kid.      *       * @param markedContentIdentifier the marked-content identifier      */
specifier|public
name|void
name|removeKid
parameter_list|(
name|COSInteger
name|markedContentIdentifier
parameter_list|)
block|{
name|this
operator|.
name|removeKid
argument_list|(
operator|(
name|COSBase
operator|)
name|markedContentIdentifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a marked-content reference kid.      *       * @param markedContentReference the marked-content reference      */
specifier|public
name|void
name|removeKid
parameter_list|(
name|PDMarkedContentReference
name|markedContentReference
parameter_list|)
block|{
name|this
operator|.
name|removeObjectableKid
argument_list|(
name|markedContentReference
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes an object reference kid.      *       * @param objectReference the object reference      */
specifier|public
name|void
name|removeKid
parameter_list|(
name|PDObjectReference
name|objectReference
parameter_list|)
block|{
name|this
operator|.
name|removeObjectableKid
argument_list|(
name|objectReference
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the structure tree root.      *       * @return the structure tree root      */
specifier|private
name|PDStructureTreeRoot
name|getStructureTreeRoot
parameter_list|()
block|{
name|PDStructureNode
name|parent
init|=
name|this
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|instanceof
name|PDStructureElement
condition|)
block|{
name|parent
operator|=
operator|(
operator|(
name|PDStructureElement
operator|)
name|parent
operator|)
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parent
operator|instanceof
name|PDStructureTreeRoot
condition|)
block|{
return|return
operator|(
name|PDStructureTreeRoot
operator|)
name|parent
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the role map.      *       * @return the role map      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRoleMap
parameter_list|()
block|{
name|PDStructureTreeRoot
name|root
init|=
name|this
operator|.
name|getStructureTreeRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
return|return
name|root
operator|.
name|getRoleMap
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

