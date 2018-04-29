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
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|gsub
operator|.
name|GSUBTablePrintUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|model
operator|.
name|GsubData
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
comment|/**  * This class is to be used mainly for debugging purposes. Prints the GSUB Feature table for debugging.  *   * @author Palash Ray  */
end_comment

begin_class
specifier|public
class|class
name|GSUBTableDebugger
block|{
specifier|private
specifier|static
specifier|final
name|String
name|LOHIT_BENGALI_FONT_FILE
init|=
literal|"/ttf/Lohit-Bengali.ttf"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|printLohitBengaliTTF
parameter_list|()
throws|throws
name|IOException
block|{
name|MemoryTTFDataStream
name|memoryTTFDataStream
init|=
operator|new
name|MemoryTTFDataStream
argument_list|(
name|GSUBTableDebugger
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|LOHIT_BENGALI_FONT_FILE
argument_list|)
argument_list|)
decl_stmt|;
name|memoryTTFDataStream
operator|.
name|seek
argument_list|(
name|GlyphSubstitutionTableTest
operator|.
name|DATA_POSITION_FOR_GSUB_TABLE
argument_list|)
expr_stmt|;
name|GlyphSubstitutionTable
name|glyphSubstitutionTable
init|=
operator|new
name|GlyphSubstitutionTable
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|glyphSubstitutionTable
operator|.
name|read
argument_list|(
literal|null
argument_list|,
name|memoryTTFDataStream
argument_list|)
expr_stmt|;
name|TrueTypeFont
name|trueTypeFont
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
name|GSUBTableDebugger
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|LOHIT_BENGALI_FONT_FILE
argument_list|)
argument_list|)
decl_stmt|;
name|GsubData
name|gsubData
init|=
name|glyphSubstitutionTable
operator|.
name|getGsubData
argument_list|()
decl_stmt|;
operator|new
name|GSUBTablePrintUtil
argument_list|()
operator|.
name|printCharacterToGlyph
argument_list|(
name|gsubData
argument_list|,
name|trueTypeFont
operator|.
name|getUnicodeCmapLookup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

