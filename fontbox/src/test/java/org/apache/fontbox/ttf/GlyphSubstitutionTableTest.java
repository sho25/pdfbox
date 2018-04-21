begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements. See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License. You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

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
name|Arrays
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
name|HashMap
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
name|Map
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
name|GlyphSubstitutionTableTest
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DATA_POSITION_FOR_GSUB_TABLE
init|=
literal|120544
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|EXPECTED_FEATURE_NAMES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"abvs"
argument_list|,
literal|"akhn"
argument_list|,
literal|"blwf"
argument_list|,
literal|"blws"
argument_list|,
literal|"half"
argument_list|,
literal|"haln"
argument_list|,
literal|"init"
argument_list|,
literal|"nukt"
argument_list|,
literal|"pres"
argument_list|,
literal|"pstf"
argument_list|,
literal|"rphf"
argument_list|,
literal|"vatu"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetRawGSubData
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|MemoryTTFDataStream
name|memoryTTFDataStream
init|=
operator|new
name|MemoryTTFDataStream
argument_list|(
name|GlyphSubstitutionTableTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/ttf/Lohit-Bengali.ttf"
argument_list|)
argument_list|)
decl_stmt|;
name|memoryTTFDataStream
operator|.
name|seek
argument_list|(
name|DATA_POSITION_FOR_GSUB_TABLE
argument_list|)
expr_stmt|;
name|GlyphSubstitutionTable
name|testClass
init|=
operator|new
name|GlyphSubstitutionTable
argument_list|(
literal|null
argument_list|)
decl_stmt|;
comment|// when
name|testClass
operator|.
name|read
argument_list|(
literal|null
argument_list|,
name|memoryTTFDataStream
argument_list|)
expr_stmt|;
comment|// then
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|rawGsubData
init|=
name|testClass
operator|.
name|getRawGSubData
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rawGsubData
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|rawGsubData
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|featureNames
init|=
name|rawGsubData
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|EXPECTED_FEATURE_NAMES
argument_list|)
argument_list|,
name|featureNames
argument_list|)
expr_stmt|;
name|String
name|templatePathToFile
init|=
literal|"/gsub/lohit_bengali/bng2/%s.txt"
decl_stmt|;
for|for
control|(
name|String
name|featureName
range|:
name|EXPECTED_FEATURE_NAMES
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"******* Testing feature: "
operator|+
name|featureName
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|expectedGsubTableRawData
init|=
name|getExpectedGsubTableRawData
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|templatePathToFile
argument_list|,
name|featureName
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedGsubTableRawData
argument_list|,
name|rawGsubData
operator|.
name|get
argument_list|(
name|featureName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|getExpectedGsubTableRawData
parameter_list|(
name|String
name|pathToResource
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|gsubData
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|TestTTFParser
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|pathToResource
argument_list|)
argument_list|)
argument_list|)
init|;
init|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|br
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|line
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
index|[]
name|lineSplittedByKeyValue
init|=
name|line
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|lineSplittedByKeyValue
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid format"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Integer
argument_list|>
name|oldGlyphIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|lineSplittedByKeyValue
index|[
literal|0
index|]
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|oldGlyphIds
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Integer
name|newGlyphId
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|lineSplittedByKeyValue
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|gsubData
operator|.
name|put
argument_list|(
name|oldGlyphIds
argument_list|,
name|newGlyphId
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|gsubData
return|;
block|}
block|}
end_class

end_unit

