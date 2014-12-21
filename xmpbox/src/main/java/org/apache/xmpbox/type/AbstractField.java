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
name|HashMap
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
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_comment
comment|/**  * Astract Object representation of a XMP 'field' (-> Properties and specific Schemas)  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractField
block|{
specifier|private
specifier|final
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|private
name|String
name|propertyName
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|attributes
decl_stmt|;
comment|/**      * Constructor of a XMP Field      *       * @param metadata      *            The metadata to attach to this field      * @param propertyName      *            the local name to set for this field      */
specifier|public
name|AbstractField
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get the propertyName (or localName)      *       * @return the local Name      */
specifier|public
specifier|final
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
specifier|public
specifier|final
name|void
name|setPropertyName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|propertyName
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Set a new attribute for this entity      *       * @param value      *            The Attribute property to add      */
specifier|public
specifier|final
name|void
name|setAttribute
parameter_list|(
name|Attribute
name|value
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|.
name|containsKey
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// if same name in element, attribute will be replaced
name|attributes
operator|.
name|remove
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|attributes
operator|.
name|put
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check if an attribute is declared for this entity      *       * @param qualifiedName      *            the full qualified name of the attribute concerned      * @return true if attribute is present      */
specifier|public
specifier|final
name|boolean
name|containsAttribute
parameter_list|(
name|String
name|qualifiedName
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|containsKey
argument_list|(
name|qualifiedName
argument_list|)
return|;
block|}
comment|/**      * Get an attribute with its name in this entity      *       * @param qualifiedName      *            the full qualified name of the attribute wanted      * @return The attribute property      */
specifier|public
specifier|final
name|Attribute
name|getAttribute
parameter_list|(
name|String
name|qualifiedName
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|qualifiedName
argument_list|)
return|;
block|}
comment|/**      * Get attributes list defined for this entity      *       * @return Attributes list      */
specifier|public
specifier|final
name|List
argument_list|<
name|Attribute
argument_list|>
name|getAllAttributes
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Attribute
argument_list|>
argument_list|(
name|attributes
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Remove an attribute of this entity      *       * @param qualifiedName      *            the full qualified name of the attribute wanted      */
specifier|public
specifier|final
name|void
name|removeAttribute
parameter_list|(
name|String
name|qualifiedName
parameter_list|)
block|{
if|if
condition|(
name|containsAttribute
argument_list|(
name|qualifiedName
argument_list|)
condition|)
block|{
name|attributes
operator|.
name|remove
argument_list|(
name|qualifiedName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|XMPMetadata
name|getMetadata
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
specifier|public
specifier|abstract
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/**      * Get the prefix of this entity      *       * @return the prefix specified      */
specifier|public
specifier|abstract
name|String
name|getPrefix
parameter_list|()
function_decl|;
block|}
end_class

end_unit

