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
name|Calendar
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
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPSchema
import|;
end_import

begin_class
specifier|public
class|class
name|VersionType
extends|extends
name|ComplexPropertyContainer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ELEMENT_NS
init|=
literal|"http://ns.adobe.com/xap/1.0/sType/Version#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERRED_PREFIX
init|=
literal|"stVer"
decl_stmt|;
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMMENTS
init|=
literal|"comments"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"event"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MODIFIER
init|=
literal|"modifier"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MODIFY_DATE
init|=
literal|"modifyDate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
decl_stmt|;
comment|/** 	 *  	 * @param metadata 	 *            The metadata to attach to this property 	 * @param namespace 	 *            the namespace URI to associate to this property 	 * @param prefix 	 *            The prefix to set for this property 	 * @param propertyName 	 *            The local Name of this thumbnail type 	 */
specifier|public
name|VersionType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespace
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
name|namespace
argument_list|,
name|prefix
argument_list|,
name|propertyName
argument_list|)
expr_stmt|;
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|XMPSchema
operator|.
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|ELEMENT_NS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getComments
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|COMMENTS
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setComments
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|COMMENTS
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResourceEventType
name|getEvent
parameter_list|()
block|{
comment|//		ResourceEventType event = (ResourceEventType)getPropertiesByLocalName(EVENT);
return|return
operator|(
name|ResourceEventType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|EVENT
argument_list|,
name|ResourceEventType
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEvent
parameter_list|(
name|ResourceEventType
name|value
parameter_list|)
block|{
name|this
operator|.
name|addProperty
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Calendar
name|getModifyDate
parameter_list|()
block|{
name|DateType
name|absProp
init|=
operator|(
name|DateType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MODIFY_DATE
argument_list|,
name|DateType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
name|absProp
operator|.
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setModifyDate
parameter_list|(
name|Calendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|addProperty
argument_list|(
operator|new
name|DateType
argument_list|(
name|metadata
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|MODIFY_DATE
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|VERSION
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|VERSION
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getModifier
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MODIFIER
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setModifier
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|MODIFIER
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

