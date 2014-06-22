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

begin_comment
comment|/**  * Object representation for arrays content This Class could be used to define directly a property with more than one  * field (structure) and also schemas  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|ComplexPropertyContainer
comment|/* extends AbstractField */
block|{
specifier|private
name|List
argument_list|<
name|AbstractField
argument_list|>
name|properties
decl_stmt|;
comment|// private Map<String, Attribute> attributes;
comment|/**      * Complex Property type constructor (namespaceURI is given)      *       */
specifier|public
name|ComplexPropertyContainer
parameter_list|()
block|{
name|properties
operator|=
operator|new
name|ArrayList
argument_list|<
name|AbstractField
argument_list|>
argument_list|()
expr_stmt|;
comment|// attributes = new HashMap<String, Attribute>();
block|}
comment|// /**
comment|// * Get an attribute with its name in this entity
comment|// *
comment|// * @param qualifiedName
comment|// * the full qualified name of the attribute wanted
comment|// * @return The attribute property
comment|// */
comment|// public Attribute getAttribute(String qualifiedName) {
comment|// return attributes.get(qualifiedName);
comment|// }
comment|// /**
comment|// * Get attributes list defined for this entity
comment|// *
comment|// * @return Attributes list
comment|// */
comment|// public List<Attribute> getAllAttributes() {
comment|// return new ArrayList<Attribute>(attributes.values());
comment|// }
comment|// /**
comment|// * Set a new attribute for this entity
comment|// *
comment|// * @param value
comment|// * The Attribute property to add
comment|// */
comment|// public void setAttribute(Attribute value) {
comment|// if (attributes.containsKey(value.getQualifiedName())) {
comment|// // if same name in element, attribute will be replaced
comment|// attributes.remove(value.getQualifiedName());
comment|// }
comment|// if (value.getNamespace() == null) {
comment|// attributes.put(value.getQualifiedName(), value);
comment|// } else {
comment|// attributes.put(value.getQualifiedName(), value);
comment|// }
comment|// }
comment|// /**
comment|// * Remove an attribute of this entity
comment|// *
comment|// * @param qualifiedName
comment|// * the full qualified name of the attribute wanted
comment|// */
comment|// public void removeAttribute(String qualifiedName) {
comment|// if (containsAttribute(qualifiedName)) {
comment|// attributes.remove(qualifiedName);
comment|// }
comment|//
comment|// }
comment|// /**
comment|// * Check if an attribute is declared for this entity
comment|// *
comment|// * @param qualifiedName
comment|// * the full qualified name of the attribute concerned
comment|// * @return true if attribute is present
comment|// */
comment|// public boolean containsAttribute(String qualifiedName) {
comment|// return attributes.containsKey(qualifiedName);
comment|// }
comment|/**      * Give the first property found in this container with type and localname expected      *       * @param localName      *            the localname of property wanted      * @param type      *            the property type of property wanted      * @return the property wanted      */
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
comment|/**      * Add a property to the current structure      *       * @param obj      *            the property to add      */
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
block|}
comment|/**      * Return all children associated to this property      *       * @return All Properties contained in this container      */
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
comment|/**      * Return all properties with this specified localName      *       * @param localName      *            the local name wanted      * @return All properties with local name which match with localName given      */
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
comment|/**      * Check if two property are similar      *       * @param prop1      *            First property      * @param prop2      *            Second property      * @return True if these properties are equals      */
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
condition|)
block|{
name|String
name|pn1
init|=
name|prop1
operator|.
name|getPropertyName
argument_list|()
decl_stmt|;
name|String
name|pn2
init|=
name|prop2
operator|.
name|getPropertyName
argument_list|()
decl_stmt|;
if|if
condition|(
name|pn1
operator|==
literal|null
condition|)
block|{
return|return
name|pn2
operator|==
literal|null
return|;
block|}
else|else
block|{
if|if
condition|(
name|pn1
operator|.
name|equals
argument_list|(
name|pn2
argument_list|)
condition|)
block|{
return|return
name|prop1
operator|.
name|equals
argument_list|(
name|prop2
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Check if a XMPFieldObject is in the complex property      *       * @param property      *            The property to check      * @return True if property is present in this container      */
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
comment|/**      * Remove a property      *       * @param property      *            The property to remove      */
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
block|}
block|}
block|}
end_class

end_unit

