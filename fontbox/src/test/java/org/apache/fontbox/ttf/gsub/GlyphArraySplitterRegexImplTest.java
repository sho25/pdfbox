begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|gsub
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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

begin_class
specifier|public
class|class
name|GlyphArraySplitterRegexImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSplit_1
parameter_list|()
block|{
comment|// given
name|Set
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|matchers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|84
argument_list|,
literal|93
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|102
argument_list|,
literal|82
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|104
argument_list|,
literal|87
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|GlyphArraySplitter
name|testClass
init|=
operator|new
name|GlyphArraySplitterRegexImpl
argument_list|(
name|matchers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|84
argument_list|,
literal|112
argument_list|,
literal|93
argument_list|,
literal|104
argument_list|,
literal|82
argument_list|,
literal|61
argument_list|,
literal|96
argument_list|,
literal|102
argument_list|,
literal|93
argument_list|,
literal|104
argument_list|,
literal|87
argument_list|,
literal|110
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|tokens
init|=
name|testClass
operator|.
name|split
argument_list|(
name|glyphIds
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|84
argument_list|,
literal|112
argument_list|,
literal|93
argument_list|,
literal|104
argument_list|,
literal|82
argument_list|,
literal|61
argument_list|,
literal|96
argument_list|,
literal|102
argument_list|,
literal|93
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|104
argument_list|,
literal|87
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|110
argument_list|)
argument_list|)
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplit_2
parameter_list|()
block|{
comment|// given
name|Set
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|matchers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|74
argument_list|,
literal|112
argument_list|,
literal|76
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|GlyphArraySplitter
name|testClass
init|=
operator|new
name|GlyphArraySplitterRegexImpl
argument_list|(
name|matchers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|,
literal|103
argument_list|,
literal|93
argument_list|,
literal|108
argument_list|,
literal|93
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|tokens
init|=
name|testClass
operator|.
name|split
argument_list|(
name|glyphIds
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|103
argument_list|,
literal|93
argument_list|,
literal|108
argument_list|,
literal|93
argument_list|)
argument_list|)
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplit_3
parameter_list|()
block|{
comment|// given
name|Set
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|matchers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|74
argument_list|,
literal|112
argument_list|,
literal|76
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|GlyphArraySplitter
name|testClass
init|=
operator|new
name|GlyphArraySplitterRegexImpl
argument_list|(
name|matchers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|94
argument_list|,
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|,
literal|112
argument_list|,
literal|91
argument_list|,
literal|103
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|tokens
init|=
name|testClass
operator|.
name|split
argument_list|(
name|glyphIds
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|94
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|,
literal|96
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|112
argument_list|,
literal|91
argument_list|,
literal|103
argument_list|)
argument_list|)
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSplit_4
parameter_list|()
block|{
comment|// given
name|Set
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|matchers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|112
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|76
argument_list|,
literal|112
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|GlyphArraySplitter
name|testClass
init|=
operator|new
name|GlyphArraySplitterRegexImpl
argument_list|(
name|matchers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphIds
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|94
argument_list|,
literal|167
argument_list|,
literal|112
argument_list|,
literal|91
argument_list|,
literal|103
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|tokens
init|=
name|testClass
operator|.
name|split
argument_list|(
name|glyphIds
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|94
argument_list|,
literal|167
argument_list|,
literal|112
argument_list|,
literal|91
argument_list|,
literal|103
argument_list|)
argument_list|)
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
