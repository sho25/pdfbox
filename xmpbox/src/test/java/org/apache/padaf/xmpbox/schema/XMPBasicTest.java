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
name|Calendar
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
name|XMPBasicTest
extends|extends
name|AbstractXMPSchemaTest
block|{
specifier|public
name|XMPBasicTest
parameter_list|(
name|String
name|prop
parameter_list|,
name|PropertyType
name|type
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
name|super
argument_list|(
name|prop
argument_list|,
name|type
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
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
name|createAndAddXMPBasicSchema
argument_list|()
expr_stmt|;
name|schemaClass
operator|=
name|XMPBasicSchema
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
literal|"Advisory"
argument_list|,
name|Types
operator|.
name|XPath
argument_list|,
name|Cardinality
operator|.
name|Bag
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"xpath1"
block|,
literal|"xpath2"
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
literal|"BaseURL"
argument_list|,
name|Types
operator|.
name|URL
argument_list|,
literal|"URL"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"CreateDate"
argument_list|,
name|Types
operator|.
name|Date
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"CreatorTool"
argument_list|,
name|Types
operator|.
name|AgentName
argument_list|,
literal|"CreatorTool"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Identifier"
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
literal|"id1"
block|,
literal|"id2"
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
literal|"Label"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"label"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"MetadataDate"
argument_list|,
name|Types
operator|.
name|Date
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"ModifyDate"
argument_list|,
name|Types
operator|.
name|Date
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Nickname"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"nick name"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Rating"
argument_list|,
name|Types
operator|.
name|Integer
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Thumbnails"
argument_list|,
name|Types
operator|.
name|Thumbnail
argument_list|,
name|Cardinality
operator|.
name|Alt
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

