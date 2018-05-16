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
name|pdfbox
operator|.
name|it
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|List
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
name|CmapLookup
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
name|GsubWorker
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
name|GsubWorkerFactory
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
name|GsubWorkerForBengali
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDType0Font
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
name|Ignore
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
comment|/**  * Integration test for {@link GsubWorkerForBengali}. Has various combinations of glyphs to test  * proper working of the GSUB system.  *  * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|GsubWorkerForBengaliTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|LOHIT_BENGALI_TTF
init|=
literal|"/org/apache/pdfbox/ttf/Lohit-Bengali.ttf"
decl_stmt|;
specifier|private
name|CmapLookup
name|cmapLookup
decl_stmt|;
specifier|private
name|GsubWorker
name|gsubWorkerForBengali
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDType0Font
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|GsubWorkerForBengaliTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|LOHIT_BENGALI_TTF
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|cmapLookup
operator|=
name|font
operator|.
name|getCmapLookup
argument_list|()
expr_stmt|;
name|gsubWorkerForBengali
operator|=
operator|new
name|GsubWorkerFactory
argument_list|()
operator|.
name|getGsubWorker
argument_list|(
name|cmapLookup
argument_list|,
name|font
operator|.
name|getGsubData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_simple_hosshoi_kar
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|56
argument_list|,
literal|102
argument_list|,
literal|91
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"আমি"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_ja_phala
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|89
argument_list|,
literal|156
argument_list|,
literal|101
argument_list|,
literal|97
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"ব্যাস"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_e_kar
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|438
argument_list|,
literal|89
argument_list|,
literal|94
argument_list|,
literal|101
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"বেলা"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_o_kar
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|108
argument_list|,
literal|89
argument_list|,
literal|101
argument_list|,
literal|97
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"বোস"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
name|void
name|testApplyTransforms_o_kar_repeated_1_not_working_yet
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|108
argument_list|,
literal|96
argument_list|,
literal|101
argument_list|,
literal|108
argument_list|,
literal|94
argument_list|,
literal|101
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"ষোলো"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
name|void
name|testApplyTransforms_o_kar_repeated_2_not_working_yet
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|108
argument_list|,
literal|73
argument_list|,
literal|101
argument_list|,
literal|108
argument_list|,
literal|77
argument_list|,
literal|101
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"ছোটো"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_ou_kar
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|108
argument_list|,
literal|91
argument_list|,
literal|114
argument_list|,
literal|94
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"মৌল"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_oi_kar
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|439
argument_list|,
literal|89
argument_list|,
literal|93
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"বৈর"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_kha_e_murddhana_swa_e_khiwa
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|167
argument_list|,
literal|103
argument_list|,
literal|438
argument_list|,
literal|93
argument_list|,
literal|93
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"ক্ষীরের"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_ra_phala
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|274
argument_list|,
literal|82
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"দ্রুত"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_ref
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|85
argument_list|,
literal|104
argument_list|,
literal|440
argument_list|,
literal|82
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"ধুর্ত"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_ra_e_hosshu
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|352
argument_list|,
literal|108
argument_list|,
literal|87
argument_list|,
literal|101
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"রুপো"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_la_e_la_e
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|67
argument_list|,
literal|108
argument_list|,
literal|369
argument_list|,
literal|101
argument_list|,
literal|94
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"কল্লোল"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testApplyTransforms_khanda_ta
parameter_list|()
block|{
comment|// given
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsAfterGsub
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|98
argument_list|,
literal|78
argument_list|,
literal|101
argument_list|,
literal|113
argument_list|)
decl_stmt|;
comment|// when
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|gsubWorkerForBengali
operator|.
name|applyTransforms
argument_list|(
name|getGlyphIds
argument_list|(
literal|"হঠাৎ"
argument_list|)
argument_list|)
decl_stmt|;
comment|// then
name|assertEquals
argument_list|(
name|glyphsAfterGsub
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|getGlyphIds
parameter_list|(
name|String
name|word
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|originalGlyphIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|unicodeChar
range|:
name|word
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|int
name|glyphId
init|=
name|cmapLookup
operator|.
name|getGlyphId
argument_list|(
name|unicodeChar
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|glyphId
operator|>
literal|0
argument_list|)
expr_stmt|;
name|originalGlyphIds
operator|.
name|add
argument_list|(
name|glyphId
argument_list|)
expr_stmt|;
block|}
return|return
name|originalGlyphIds
return|;
block|}
block|}
end_class

end_unit

