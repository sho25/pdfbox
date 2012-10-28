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
name|schema
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
name|Collection
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
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|Cardinality
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
name|Types
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|XMPMediaManagementTest
extends|extends
name|AbstractXMPSchemaTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|initTempMetaData
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
name|schema
operator|=
name|metadata
operator|.
name|createAndAddXMPMediaManagementSchema
argument_list|()
expr_stmt|;
name|schemaClass
operator|=
name|XMPMediaManagementSchema
operator|.
name|class
expr_stmt|;
block|}
annotation|@
name|Parameters
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|initializeParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"DocumentID"
argument_list|,
name|Types
operator|.
name|URI
argument_list|,
literal|"uuid:FB031973-5E75-11B2-8F06-E7F5C101C07A"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Manager"
argument_list|,
name|Types
operator|.
name|AgentName
argument_list|,
literal|"Raoul"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"ManageTo"
argument_list|,
name|Types
operator|.
name|URI
argument_list|,
literal|"uuid:36"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"ManageUI"
argument_list|,
name|Types
operator|.
name|URI
argument_list|,
literal|"uuid:3635"
argument_list|)
argument_list|)
expr_stmt|;
comment|//		data.add(wrapProperty("ManageFrom", "ResourceRef", "uuid:36"));
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"InstanceID"
argument_list|,
name|Types
operator|.
name|URI
argument_list|,
literal|"uuid:42"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"OriginalDocumentID"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"uuid:142"
argument_list|)
argument_list|)
expr_stmt|;
comment|//		data.add(wrapProperty("RenditionClass", "Text", "myclass"));
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"RenditionParams"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"my params"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"VersionID"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"14"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Versions"
argument_list|,
name|Types
operator|.
name|Version
argument_list|,
name|Cardinality
operator|.
name|Seq
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"History"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
name|Cardinality
operator|.
name|Seq
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"action 1"
block|,
literal|"action 2"
block|,
literal|"action 3"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Ingredients"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
name|Cardinality
operator|.
name|Bag
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"resource1"
block|,
literal|"resource2"
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|public
name|XMPMediaManagementTest
parameter_list|(
name|String
name|property
parameter_list|,
name|PropertyType
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|property
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

