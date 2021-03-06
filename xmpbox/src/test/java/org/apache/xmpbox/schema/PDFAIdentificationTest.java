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
name|PDFAIdentificationTest
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
name|createAndAddPFAIdentificationSchema
argument_list|()
expr_stmt|;
name|schemaClass
operator|=
name|PDFAIdentificationSchema
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
argument_list|<>
argument_list|()
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"part"
argument_list|,
name|Types
operator|.
name|Integer
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"amd"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"2005"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"conformance"
argument_list|,
name|Types
operator|.
name|Text
argument_list|,
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|public
name|PDFAIdentificationTest
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

