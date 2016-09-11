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
name|ArrayList
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
name|common
operator|.
name|COSArrayList
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
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * A node in the structure tree.  *   * @author Johannes Koch  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDStructureNode
implements|implements
name|COSObjectable
block|{
comment|/**      * Creates a node in the structure tree. Can be either a structure tree root,      *  or a structure element.      *       * @param node the node dictionary      * @return the structure node      */
specifier|public
specifier|static
name|PDStructureNode
name|create
parameter_list|(
name|COSDictionary
name|node
parameter_list|)
block|{
name|String
name|type
init|=
name|node
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"StructTreeRoot"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDStructureTreeRoot
argument_list|(
name|node
argument_list|)
return|;
block|}
if|if
condition|(
operator|(
name|type
operator|==
literal|null
operator|)
operator|||
literal|"StructElem"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDStructureElement
argument_list|(
name|node
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Dictionary must not include a Type entry with a value that is neither StructTreeRoot nor StructElem."
argument_list|)
throw|;
block|}
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * {@inheritDoc}      */
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Constructor.      *      * @param type the type      */
specifier|protected
name|PDStructureNode
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|this
operator|.
name|dictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for an existing structure node.      *      * @param dictionary The existing dictionary.      */
specifier|protected
name|PDStructureNode
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * Returns the type.      *       * @return the type      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
return|;
block|}
comment|/**      * Returns a list of objects for the kids (K).      *       * @return a list of objects for the kids      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getKids
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|kidObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|COSBase
name|k
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|kid
range|:
operator|(
operator|(
name|COSArray
operator|)
name|k
operator|)
control|)
block|{
name|Object
name|kidObject
init|=
name|this
operator|.
name|createObject
argument_list|(
name|kid
argument_list|)
decl_stmt|;
if|if
condition|(
name|kidObject
operator|!=
literal|null
condition|)
block|{
name|kidObjects
operator|.
name|add
argument_list|(
name|kidObject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Object
name|kidObject
init|=
name|this
operator|.
name|createObject
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|kidObject
operator|!=
literal|null
condition|)
block|{
name|kidObjects
operator|.
name|add
argument_list|(
name|kidObject
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|kidObjects
return|;
block|}
comment|/**      * Sets the kids (K).      *       * @param kids the kids      */
specifier|public
name|void
name|setKids
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|kids
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|kids
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a structure element kid.      *       * @param structureElement the structure element      */
specifier|public
name|void
name|appendKid
parameter_list|(
name|PDStructureElement
name|structureElement
parameter_list|)
block|{
name|this
operator|.
name|appendObjectableKid
argument_list|(
name|structureElement
argument_list|)
expr_stmt|;
name|structureElement
operator|.
name|setParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends an objectable kid.      *       * @param objectable the objectable      */
specifier|protected
name|void
name|appendObjectableKid
parameter_list|(
name|COSObjectable
name|objectable
parameter_list|)
block|{
if|if
condition|(
name|objectable
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|appendKid
argument_list|(
name|objectable
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a COS base kid.      *       * @param object the COS base      */
specifier|protected
name|void
name|appendKid
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|COSBase
name|k
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|==
literal|null
condition|)
block|{
comment|// currently no kid: set new kid as kids
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|instanceof
name|COSArray
condition|)
block|{
comment|// currently more than one kid: add new kid to existing array
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|k
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// currently one kid: put current and new kid into array and set array as kids
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Inserts a structure element kid before a reference kid.      *       * @param newKid the structure element      * @param refKid the reference kid      */
specifier|public
name|void
name|insertBefore
parameter_list|(
name|PDStructureElement
name|newKid
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
name|this
operator|.
name|insertObjectableBefore
argument_list|(
name|newKid
argument_list|,
name|refKid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts an objectable kid before a reference kid.      *       * @param newKid the objectable      * @param refKid the reference kid      */
specifier|protected
name|void
name|insertObjectableBefore
parameter_list|(
name|COSObjectable
name|newKid
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
if|if
condition|(
name|newKid
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|insertBefore
argument_list|(
name|newKid
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|refKid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts an COS base kid before a reference kid.      *       * @param newKid the COS base      * @param refKid the reference kid      */
specifier|protected
name|void
name|insertBefore
parameter_list|(
name|COSBase
name|newKid
parameter_list|,
name|Object
name|refKid
parameter_list|)
block|{
if|if
condition|(
name|newKid
operator|==
literal|null
operator|||
name|refKid
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|COSBase
name|k
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|COSBase
name|refKidBase
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|refKid
operator|instanceof
name|COSObjectable
condition|)
block|{
name|refKidBase
operator|=
operator|(
operator|(
name|COSObjectable
operator|)
name|refKid
operator|)
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|refKid
operator|instanceof
name|COSInteger
condition|)
block|{
name|refKidBase
operator|=
operator|(
name|COSBase
operator|)
name|refKid
expr_stmt|;
block|}
if|if
condition|(
name|k
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
name|k
decl_stmt|;
name|int
name|refIndex
init|=
name|array
operator|.
name|indexOfObject
argument_list|(
name|refKidBase
argument_list|)
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|refIndex
argument_list|,
name|newKid
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|onlyKid
init|=
name|k
operator|.
name|equals
argument_list|(
name|refKidBase
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|onlyKid
operator|&&
operator|(
name|k
operator|instanceof
name|COSObject
operator|)
condition|)
block|{
name|COSBase
name|kObj
init|=
operator|(
operator|(
name|COSObject
operator|)
name|k
operator|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|onlyKid
operator|=
name|kObj
operator|.
name|equals
argument_list|(
name|refKidBase
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onlyKid
condition|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|newKid
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|refKidBase
argument_list|)
expr_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Removes a structure element kid.      *       * @param structureElement the structure element      * @return<code>true</code> if the kid was removed,<code>false</code> otherwise      */
specifier|public
name|boolean
name|removeKid
parameter_list|(
name|PDStructureElement
name|structureElement
parameter_list|)
block|{
name|boolean
name|removed
init|=
name|this
operator|.
name|removeObjectableKid
argument_list|(
name|structureElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|removed
condition|)
block|{
name|structureElement
operator|.
name|setParent
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|removed
return|;
block|}
comment|/**      * Removes an objectable kid.      *       * @param objectable the objectable      * @return<code>true</code> if the kid was removed,<code>false</code> otherwise      */
specifier|protected
name|boolean
name|removeObjectableKid
parameter_list|(
name|COSObjectable
name|objectable
parameter_list|)
block|{
if|if
condition|(
name|objectable
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|this
operator|.
name|removeKid
argument_list|(
name|objectable
operator|.
name|getCOSObject
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Removes a COS base kid.      *       * @param object the COS base      * @return<code>true</code> if the kid was removed,<code>false</code> otherwise      */
specifier|protected
name|boolean
name|removeKid
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|COSBase
name|k
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|==
literal|null
condition|)
block|{
comment|// no kids: objectable is not a kid
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|k
operator|instanceof
name|COSArray
condition|)
block|{
comment|// currently more than one kid: remove kid from existing array
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|k
decl_stmt|;
name|boolean
name|removed
init|=
name|array
operator|.
name|removeObject
argument_list|(
name|object
argument_list|)
decl_stmt|;
comment|// if now only one kid: set remaining kid as kids
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
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
return|return
name|removed
return|;
block|}
else|else
block|{
comment|// currently one kid: if current kid equals given object, remove kids entry
name|boolean
name|onlyKid
init|=
name|k
operator|.
name|equals
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|onlyKid
operator|&&
operator|(
name|k
operator|instanceof
name|COSObject
operator|)
condition|)
block|{
name|COSBase
name|kObj
init|=
operator|(
operator|(
name|COSObject
operator|)
name|k
operator|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|onlyKid
operator|=
name|kObj
operator|.
name|equals
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onlyKid
condition|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Creates an object for a kid of this structure node.      * The type of object depends on the type of the kid. It can be      *<ul>      *<li>a {@link PDStructureElement},</li>      *<li>a {@link org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation PDAnnotation},</li>      *<li>a {@link org.apache.pdfbox.pdmodel.graphics.PDXObject PDXObject},</li>      *<li>a {@link PDMarkedContentReference}</li>      *<li>a {@link Integer}</li>      *</ul>      *       * @param kid the kid      * @return the object      */
specifier|protected
name|Object
name|createObject
parameter_list|(
name|COSBase
name|kid
parameter_list|)
block|{
name|COSDictionary
name|kidDic
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|kid
operator|instanceof
name|COSDictionary
condition|)
block|{
name|kidDic
operator|=
operator|(
name|COSDictionary
operator|)
name|kid
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kid
operator|instanceof
name|COSObject
condition|)
block|{
name|COSBase
name|base
init|=
operator|(
operator|(
name|COSObject
operator|)
name|kid
operator|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|kidDic
operator|=
operator|(
name|COSDictionary
operator|)
name|base
expr_stmt|;
block|}
block|}
if|if
condition|(
name|kidDic
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|kidDic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|type
operator|==
literal|null
operator|)
operator|||
name|PDStructureElement
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// A structure element dictionary denoting another structure
comment|// element
return|return
operator|new
name|PDStructureElement
argument_list|(
name|kidDic
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDObjectReference
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// An object reference dictionary denoting a PDF object
return|return
operator|new
name|PDObjectReference
argument_list|(
name|kidDic
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|PDMarkedContentReference
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// A marked-content reference dictionary denoting a
comment|// marked-content sequence
return|return
operator|new
name|PDMarkedContentReference
argument_list|(
name|kidDic
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|kid
operator|instanceof
name|COSInteger
condition|)
block|{
comment|// An integer marked-content identifier denoting a
comment|// marked-content sequence
name|COSInteger
name|mcid
init|=
operator|(
name|COSInteger
operator|)
name|kid
decl_stmt|;
return|return
name|mcid
operator|.
name|intValue
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

