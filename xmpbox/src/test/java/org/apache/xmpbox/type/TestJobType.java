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
name|Collection
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
name|TestJobType
extends|extends
name|AbstractStructuredTypeTester
block|{
specifier|protected
name|JobType
name|structured
init|=
literal|null
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|before
argument_list|()
expr_stmt|;
name|structured
operator|=
operator|new
name|JobType
argument_list|(
name|xmp
argument_list|,
literal|"job"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TestJobType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractStructuredType
argument_list|>
name|clz
parameter_list|,
name|String
name|field
parameter_list|,
name|Types
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|clz
argument_list|,
name|field
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AbstractStructuredType
name|getStructured
parameter_list|()
block|{
return|return
name|structured
return|;
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
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
name|JobType
operator|.
name|class
block|,
literal|"id"
block|,
name|Types
operator|.
name|Text
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
name|JobType
operator|.
name|class
block|,
literal|"name"
block|,
name|Types
operator|.
name|Text
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
name|JobType
operator|.
name|class
block|,
literal|"url"
block|,
name|Types
operator|.
name|URL
block|}
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

