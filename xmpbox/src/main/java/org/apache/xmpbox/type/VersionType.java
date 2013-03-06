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
name|Calendar
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

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"stVer"
argument_list|,
name|namespace
operator|=
literal|"http://ns.adobe.com/xap/1.0/sType/Version#"
argument_list|)
specifier|public
class|class
name|VersionType
extends|extends
name|AbstractStructuredType
block|{
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Text
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|COMMENTS
init|=
literal|"comments"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ResourceEvent
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"event"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ProperName
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MODIFIER
init|=
literal|"modifier"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Date
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MODIFY_DATE
init|=
literal|"modifyDate"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Text
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
decl_stmt|;
comment|/**      *       * @param metadata      *            The metadata to attach to this property      * @param namespace      *            the namespace URI to associate to this property      * @param prefix      *            The prefix to set for this property      * @param propertyName      *            The local Name of this thumbnail type      */
specifier|public
name|VersionType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
name|getNamespace
argument_list|()
argument_list|,
name|getPreferedPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getComments
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|COMMENTS
argument_list|)
return|;
block|}
specifier|public
name|void
name|setComments
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|COMMENTS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResourceEventType
name|getEvent
parameter_list|()
block|{
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
return|return
name|getDatePropertyAsCalendar
argument_list|(
name|MODIFY_DATE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setModifyDate
parameter_list|(
name|Calendar
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MODIFY_DATE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|VERSION
argument_list|)
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|VERSION
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getModifier
parameter_list|()
block|{
return|return
name|getPropertyValueAsString
argument_list|(
name|MODIFIER
argument_list|)
return|;
block|}
specifier|public
name|void
name|setModifier
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MODIFIER
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

