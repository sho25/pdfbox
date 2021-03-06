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
name|schema
operator|.
name|DublinCoreSchema
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
name|xmpbox
operator|.
name|type
operator|.
name|StructuredType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Test with 2 dublinCore with different prefix (Test comportment of XMPMetadata)  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|DoubleSameTypeSchemaTest
block|{
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|testInit
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoubleDublinCore
parameter_list|()
throws|throws
name|Exception
block|{
name|DublinCoreSchema
name|dc1
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|String
name|ownPrefix
init|=
literal|"test"
decl_stmt|;
name|DublinCoreSchema
name|dc2
init|=
operator|new
name|DublinCoreSchema
argument_list|(
name|metadata
argument_list|,
name|ownPrefix
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|dc2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|creators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|creators
operator|.
name|add
argument_list|(
literal|"creator1"
argument_list|)
expr_stmt|;
name|creators
operator|.
name|add
argument_list|(
literal|"creator2"
argument_list|)
expr_stmt|;
name|String
name|format
init|=
literal|"application/pdf"
decl_stmt|;
name|dc1
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|dc1
operator|.
name|addCreator
argument_list|(
name|creators
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|dc1
operator|.
name|addCreator
argument_list|(
name|creators
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|coverage
init|=
literal|"Coverage"
decl_stmt|;
name|dc2
operator|.
name|setCoverage
argument_list|(
name|coverage
argument_list|)
expr_stmt|;
name|dc2
operator|.
name|addCreator
argument_list|(
name|creators
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|dc2
operator|.
name|addCreator
argument_list|(
name|creators
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|StructuredType
name|stDub
init|=
name|DublinCoreSchema
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// We can't use metadata.getDublinCoreSchema() due to specification of
comment|// XMPBox (see Javadoc of XMPMetadata)
name|Assert
operator|.
name|assertEquals
argument_list|(
name|format
argument_list|,
operator|(
operator|(
name|DublinCoreSchema
operator|)
name|metadata
operator|.
name|getSchema
argument_list|(
name|stDub
operator|.
name|preferedPrefix
argument_list|()
argument_list|,
name|stDub
operator|.
name|namespace
argument_list|()
argument_list|)
operator|)
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|coverage
argument_list|,
operator|(
operator|(
name|DublinCoreSchema
operator|)
name|metadata
operator|.
name|getSchema
argument_list|(
name|ownPrefix
argument_list|,
name|stDub
operator|.
name|namespace
argument_list|()
argument_list|)
operator|)
operator|.
name|getCoverage
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|schems
init|=
name|metadata
operator|.
name|getAllSchemas
argument_list|()
decl_stmt|;
name|DublinCoreSchema
name|dc
decl_stmt|;
for|for
control|(
name|XMPSchema
name|xmpSchema
range|:
name|schems
control|)
block|{
name|dc
operator|=
operator|(
name|DublinCoreSchema
operator|)
name|xmpSchema
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|dc
operator|.
name|getCreators
argument_list|()
operator|.
name|containsAll
argument_list|(
name|creators
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

