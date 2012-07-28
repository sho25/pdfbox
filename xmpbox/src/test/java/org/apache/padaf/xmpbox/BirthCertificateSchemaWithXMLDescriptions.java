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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|PropertyExtensionDefinition
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
name|SchemaExtensionDefinition
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
name|type
operator|.
name|AbstractField
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
name|type
operator|.
name|Attribute
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
name|type
operator|.
name|ComplexPropertyContainer
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
name|type
operator|.
name|PropertyType
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
name|type
operator|.
name|TextType
import|;
end_import

begin_class
annotation|@
name|SchemaExtensionDefinition
argument_list|(
name|schema
operator|=
literal|"Birth-Certificate Schema"
argument_list|,
name|valueType_description
operator|=
literal|"/org/apache/padaf/xmpbox/valueTypeDescription.xml"
argument_list|,
name|property_descriptions
operator|=
literal|"propertiesDescription.xml"
argument_list|)
specifier|public
class|class
name|BirthCertificateSchemaWithXMLDescriptions
extends|extends
name|XMPSchema
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PREFERED_PREFIX
init|=
literal|"adn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://test.apache.com/xap/adn/"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|FIRST_NAME
init|=
literal|"firstname"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"seq Text"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|LAST_NAME
init|=
literal|"lastname"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|BIRTH_PLACE
init|=
literal|"birth-place"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Date"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|BIRTH_DATE
init|=
literal|"birth-date"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|BIRTH_COUNTRY
init|=
literal|"birth-country"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"mailaddress"
argument_list|)
annotation|@
name|PropertyExtensionDefinition
argument_list|(
name|propertyCategory
operator|=
literal|"external"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MAIL_ADR
init|=
literal|"mail"
decl_stmt|;
specifier|public
name|BirthCertificateSchemaWithXMLDescriptions
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|PREFERED_PREFIX
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
literal|null
argument_list|,
literal|"xmlns"
argument_list|,
literal|"madn"
argument_list|,
literal|"http://test.withfield.com/vt/"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setFirstname
parameter_list|(
name|String
name|fn
parameter_list|)
block|{
name|this
operator|.
name|setTextPropertyValueAsSimple
argument_list|(
name|FIRST_NAME
argument_list|,
name|fn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addLastname
parameter_list|(
name|String
name|ln
parameter_list|)
block|{
name|this
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
name|LAST_NAME
argument_list|,
name|ln
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBirthPlace
parameter_list|(
name|String
name|city
parameter_list|)
block|{
name|this
operator|.
name|setTextPropertyValueAsSimple
argument_list|(
name|BIRTH_PLACE
argument_list|,
name|city
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBirthCountry
parameter_list|(
name|String
name|country
parameter_list|)
block|{
name|this
operator|.
name|setTextPropertyValueAsSimple
argument_list|(
name|BIRTH_COUNTRY
argument_list|,
name|country
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBirthDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|this
operator|.
name|setDatePropertyValueAsSimple
argument_list|(
name|BIRTH_DATE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFirstname
parameter_list|()
block|{
return|return
name|this
operator|.
name|getUnqualifiedTextProperty
argument_list|(
name|FIRST_NAME
argument_list|)
operator|.
name|getStringValue
argument_list|()
return|;
block|}
specifier|public
name|String
name|getLastname
parameter_list|()
block|{
return|return
name|this
operator|.
name|getUnqualifiedTextProperty
argument_list|(
name|LAST_NAME
argument_list|)
operator|.
name|getStringValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setMailaddr
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|domain
parameter_list|)
block|{
name|ComplexPropertyContainer
name|field
init|=
operator|new
name|ComplexPropertyContainer
argument_list|(
name|getMetadata
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getLocalPrefix
argument_list|()
argument_list|,
name|MAIL_ADR
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
literal|null
argument_list|,
literal|"rdf"
argument_list|,
literal|"parseType"
argument_list|,
literal|"Resource"
argument_list|)
argument_list|)
expr_stmt|;
name|TextType
name|namePart
init|=
operator|new
name|TextType
argument_list|(
name|getMetadata
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|"madn"
argument_list|,
literal|"name"
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|TextType
name|domainPart
init|=
operator|new
name|TextType
argument_list|(
name|getMetadata
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|"madn"
argument_list|,
literal|"domain"
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|field
operator|.
name|addProperty
argument_list|(
name|namePart
argument_list|)
expr_stmt|;
name|field
operator|.
name|addProperty
argument_list|(
name|domainPart
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ComplexPropertyContainer
name|getMailField
parameter_list|()
block|{
name|AbstractField
name|afield
init|=
name|this
operator|.
name|getPropertyAsSimple
argument_list|(
name|MAIL_ADR
argument_list|)
decl_stmt|;
if|if
condition|(
name|afield
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|afield
operator|instanceof
name|ComplexPropertyContainer
condition|)
block|{
return|return
operator|(
name|ComplexPropertyContainer
operator|)
name|afield
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|MAIL_ADR
operator|+
literal|" property found but not seems to be a field"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|TextType
name|getTextType
parameter_list|(
name|String
name|nameProp
parameter_list|)
block|{
name|ComplexPropertyContainer
name|field
init|=
name|getMailField
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|AbstractField
argument_list|>
name|it
init|=
name|field
operator|.
name|getAllProperties
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|AbstractField
name|aProp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|aProp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|aProp
operator|.
name|getPropertyName
argument_list|()
operator|.
name|equals
argument_list|(
name|nameProp
argument_list|)
condition|)
block|{
if|if
condition|(
name|aProp
operator|instanceof
name|TextType
condition|)
block|{
return|return
operator|(
name|TextType
operator|)
name|aProp
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|nameProp
operator|+
literal|" property found but not seems to be in expected type"
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMailName
parameter_list|()
block|{
name|TextType
name|name
init|=
name|getTextType
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
operator|.
name|getStringValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getMailDomain
parameter_list|()
block|{
name|TextType
name|name
init|=
name|getTextType
argument_list|(
literal|"domain"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
operator|.
name|getStringValue
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

