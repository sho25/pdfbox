begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|net
operator|.
name|URI
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|util
operator|.
name|autodetect
operator|.
name|FontFileFinder
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|TTFSubsetterTest
block|{
comment|/**      * Test of PDFBOX-2854: empty subset with all tables.      *       * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testEmptySubset
parameter_list|()
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|x
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
literal|"src/test/resources/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|".notdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|subset
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test of PDFBOX-2854: empty subset with selected tables.      *       * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testEmptySubset2
parameter_list|()
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|x
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
literal|"src/test/resources/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
comment|// List copied from TrueTypeEmbedder.java
name|List
argument_list|<
name|String
argument_list|>
name|tables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"head"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hhea"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"loca"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"maxp"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"cvt "
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"prep"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"glyf"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hmtx"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"fpgm"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"gasp"
argument_list|)
expr_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|x
argument_list|,
name|tables
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|".notdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|subset
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test of PDFBOX-2854: subset with one glyph.      *       * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testNonEmptySubset
parameter_list|()
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|full
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
literal|"src/test/resources/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|full
argument_list|)
decl_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|'a'
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|".notdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|subset
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|subset
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|subset
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|full
operator|.
name|getAdvanceWidth
argument_list|(
name|full
operator|.
name|nameToGID
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|,
name|subset
operator|.
name|getAdvanceWidth
argument_list|(
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|full
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|full
operator|.
name|nameToGID
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|,
name|subset
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test of PDFBOX-3319: check that widths and left side bearings in partially monospaced font      * are kept.      *      * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3319
parameter_list|()
throws|throws
name|IOException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Searching for SimHei font..."
argument_list|)
expr_stmt|;
name|FontFileFinder
name|fontFileFinder
init|=
operator|new
name|FontFileFinder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|files
init|=
name|fontFileFinder
operator|.
name|find
argument_list|()
decl_stmt|;
name|File
name|simhei
init|=
literal|null
decl_stmt|;
for|for
control|(
name|URI
name|uri
range|:
name|files
control|)
block|{
if|if
condition|(
name|uri
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"simhei.ttf"
argument_list|)
condition|)
block|{
name|simhei
operator|=
operator|new
name|File
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|simhei
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"SimHei font not available on this machine, test skipped"
argument_list|)
expr_stmt|;
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"SimHei font found!"
argument_list|)
expr_stmt|;
name|TrueTypeFont
name|full
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
name|simhei
argument_list|)
decl_stmt|;
comment|// List copied from TrueTypeEmbedder.java
comment|// Without it, the test would fail because of missing post table in source font
name|List
argument_list|<
name|String
argument_list|>
name|tables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"head"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hhea"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"loca"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"maxp"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"cvt "
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"prep"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"glyf"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hmtx"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"fpgm"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"gasp"
argument_list|)
expr_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|full
argument_list|,
name|tables
argument_list|)
decl_stmt|;
name|String
name|chinese
init|=
literal|"中国你好!"
decl_stmt|;
for|for
control|(
name|int
name|offset
init|=
literal|0
init|;
name|offset
operator|<
name|chinese
operator|.
name|length
argument_list|()
condition|;
control|)
block|{
name|int
name|codePoint
init|=
name|chinese
operator|.
name|codePointAt
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|Character
operator|.
name|charCount
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
block|}
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|ttfSubsetter
operator|.
name|getGIDMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Integer
name|newGID
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Integer
name|oldGID
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|full
operator|.
name|getAdvanceWidth
argument_list|(
name|oldGID
argument_list|)
argument_list|,
name|subset
operator|.
name|getAdvanceWidth
argument_list|(
name|newGID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|full
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|oldGID
argument_list|)
argument_list|,
name|subset
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|newGID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Test of PDFBOX-3379: check that left side bearings in partially monospaced font are kept.      *       * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3379
parameter_list|()
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|full
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
literal|"target/pdfs/DejaVuSansMono.ttf"
argument_list|)
decl_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|full
argument_list|)
decl_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|'A'
argument_list|)
expr_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|'B'
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|".notdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"space"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"B"
block|,
literal|"space"
block|}
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|assertEquals
argument_list|(
name|full
operator|.
name|getAdvanceWidth
argument_list|(
name|full
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|,
name|subset
operator|.
name|getAdvanceWidth
argument_list|(
name|subset
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|full
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|full
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|,
name|subset
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getLeftSideBearing
argument_list|(
name|subset
operator|.
name|nameToGID
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Test of PDFBOX-3757: check that PostScript names that are not part of WGL4Names don't get      * shuffled in buildPostTable().      *      * @throws java.io.IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3757
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/ttf/LiberationSans-Regular.ttf"
argument_list|)
decl_stmt|;
name|TrueTypeFont
name|ttf
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
name|testFile
argument_list|)
decl_stmt|;
name|TTFSubsetter
name|ttfSubsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|ttf
argument_list|)
decl_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|'Ö'
argument_list|)
expr_stmt|;
name|ttfSubsetter
operator|.
name|add
argument_list|(
literal|'\u200A'
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ttfSubsetter
operator|.
name|writeToStream
argument_list|(
name|baos
argument_list|)
expr_stmt|;
try|try
init|(
name|TrueTypeFont
name|subset
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|subset
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|".notdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"O"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"Odieresis"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"uni200A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|subset
operator|.
name|nameToGID
argument_list|(
literal|"dieresis.uc"
argument_list|)
argument_list|)
expr_stmt|;
name|PostScriptTable
name|pst
init|=
name|subset
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|pst
operator|.
name|getName
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|".notdef"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pst
operator|.
name|getName
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"O"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pst
operator|.
name|getName
argument_list|(
literal|2
argument_list|)
argument_list|,
literal|"Odieresis"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pst
operator|.
name|getName
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|"uni200A"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pst
operator|.
name|getName
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|"dieresis.uc"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Hair space path should be empty"
argument_list|,
name|subset
operator|.
name|getPath
argument_list|(
literal|"uni200A"
argument_list|)
operator|.
name|getBounds2D
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"UC dieresis path should not be empty"
argument_list|,
name|subset
operator|.
name|getPath
argument_list|(
literal|"dieresis.uc"
argument_list|)
operator|.
name|getBounds2D
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

