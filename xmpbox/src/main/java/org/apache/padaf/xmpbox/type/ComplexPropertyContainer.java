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
name|xmpbox
operator|.
name|type
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
name|Iterator
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
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * Object representation for arrays content This Class could be used to define  * directly a property with more than one field (structure) and also schemas  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|ComplexPropertyContainer
extends|extends
name|AbstractField
block|{
specifier|private
name|List
argument_list|<
name|AbstractField
argument_list|>
name|properties
decl_stmt|;
comment|/** 	 * Complex Property type constructor (namespaceURI is given) 	 *  	 * @param metadata 	 *            The metadata to attach to this property 	 * @param namespaceURI 	 *            The namespace URI to associate to this property 	 * @param prefix 	 *            The prefix to set for this property 	 * @param propertyName 	 *            The local Name of this property 	 */
specifier|public
name|ComplexPropertyContainer
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|namespaceURI
argument_list|,
name|prefix
argument_list|,
name|propertyName
argument_list|)
expr_stmt|;
name|properties
operator|=
operator|new
name|ArrayList
argument_list|<
name|AbstractField
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Give the first property found in this container with type and localname 	 * expected 	 *  	 * @param localName 	 *            the localname of property wanted 	 * @param type 	 *            the property type of property wanted 	 * @return the property wanted 	 */
specifier|protected
name|AbstractField
name|getFirstEquivalentProperty
parameter_list|(
name|String
name|localName
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|AbstractField
argument_list|>
name|list
init|=
name|getPropertiesByLocalName
argument_list|(
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractField
name|abstractField
range|:
name|list
control|)
block|{
if|if
condition|(
name|abstractField
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|abstractField
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Add a property to the current structure 	 *  	 * @param obj the property to add 	 */
specifier|public
name|void
name|addProperty
parameter_list|(
name|AbstractField
name|obj
parameter_list|)
block|{
if|if
condition|(
name|containsProperty
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|removeProperty
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
comment|// COMMENTS REPRESENTS CLUES TO USE SAME PROPERTY AT MORE THAN ONE PLACE
comment|// BUT IT CREATE PROBLEM TO FIND AND ERASE CLONED ELEMENT
comment|// Node cloned = obj.getElement().cloneNode(true);
comment|// parent.adoptNode(cloned);
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|obj
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
comment|// element.appendChild(cloned);
block|}
comment|/** 	 * Return all children associated to this property 	 *  	 * @return All Properties contained in this container 	 */
specifier|public
name|List
argument_list|<
name|AbstractField
argument_list|>
name|getAllProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/** 	 * Return all properties with this specified localName 	 *  	 * @param localName 	 *            the local name wanted 	 * @return All properties with local name which match with localName given 	 */
specifier|public
name|List
argument_list|<
name|AbstractField
argument_list|>
name|getPropertiesByLocalName
parameter_list|(
name|String
name|localName
parameter_list|)
block|{
name|List
argument_list|<
name|AbstractField
argument_list|>
name|absFields
init|=
name|getAllProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|absFields
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|AbstractField
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractField
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractField
name|abstractField
range|:
name|absFields
control|)
block|{
if|if
condition|(
name|abstractField
operator|.
name|getPropertyName
argument_list|()
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|abstractField
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|list
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Check if two property are similar 	 *  	 * @param prop1 	 *            First property 	 * @param prop2 	 *            Second property 	 * @return True if these properties are equals 	 */
specifier|public
name|boolean
name|isSameProperty
parameter_list|(
name|AbstractField
name|prop1
parameter_list|,
name|AbstractField
name|prop2
parameter_list|)
block|{
if|if
condition|(
name|prop1
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getClass
argument_list|()
argument_list|)
operator|&&
name|prop1
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|prop1
operator|.
name|getElement
argument_list|()
operator|.
name|getTextContent
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getElement
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** 	 * Check if a XMPFieldObject is in the complex property 	 *  	 * @param property 	 *            The property to check 	 * @return True if property is present in this container 	 */
specifier|public
name|boolean
name|containsProperty
parameter_list|(
name|AbstractField
name|property
parameter_list|)
block|{
name|Iterator
argument_list|<
name|AbstractField
argument_list|>
name|it
init|=
name|getAllProperties
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|AbstractField
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|isSameProperty
argument_list|(
name|tmp
argument_list|,
name|property
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** 	 * Remove a property 	 *  	 * @param property 	 *            The property to remove 	 */
specifier|public
name|void
name|removeProperty
parameter_list|(
name|AbstractField
name|property
parameter_list|)
block|{
if|if
condition|(
name|containsProperty
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|properties
operator|.
name|remove
argument_list|(
name|property
argument_list|)
expr_stmt|;
name|Element
name|element
init|=
name|getElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|NodeList
name|nodes
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|boolean
name|canRemove
init|=
literal|false
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|property
operator|.
name|getElement
argument_list|()
argument_list|)
condition|)
block|{
name|canRemove
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// remove out of the loop to avoid concurrent exception
if|if
condition|(
name|canRemove
condition|)
block|{
name|element
operator|.
name|removeChild
argument_list|(
name|property
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

