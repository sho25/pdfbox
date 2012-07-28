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
name|XmpConstants
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
name|JobType
extends|extends
name|AbstractStructuredType
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ELEMENT_NS
init|=
literal|"http://ns.adobe.com/xap/1.0/sType/Job#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERED_PREFIX
init|=
literal|"stJob"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"id"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URL"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|URL
init|=
literal|"url"
decl_stmt|;
specifier|public
name|JobType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|this
argument_list|(
name|metadata
argument_list|,
name|PREFERED_PREFIX
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JobType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|fieldPrefix
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|XmpConstants
operator|.
name|RDF_NAMESPACE
argument_list|,
name|fieldPrefix
argument_list|)
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
name|fieldPrefix
argument_list|,
name|ELEMENT_NS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|URL
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|ID
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|NAME
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|URL
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFieldsNamespace
parameter_list|()
block|{
return|return
name|ELEMENT_NS
return|;
block|}
block|}
end_class

end_unit

