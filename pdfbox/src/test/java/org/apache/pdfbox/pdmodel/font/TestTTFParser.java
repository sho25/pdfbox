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
name|pdmodel
operator|.
name|font
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
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|CmapSubtable
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
name|CmapTable
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
name|NameRecord
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
name|PostScriptTable
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
name|TTFParser
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
name|TrueTypeFont
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
name|encoding
operator|.
name|Encoding
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
name|encoding
operator|.
name|GlyphList
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
name|encoding
operator|.
name|WinAnsiEncoding
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
name|Test
import|;
end_import

begin_comment
comment|/**  * A test for correctly parsing TTF files.  */
end_comment

begin_class
specifier|public
class|class
name|TestTTFParser
block|{
comment|/**      * Test the post table parser.      *       * @throws IOException if an error occurs.      */
annotation|@
name|Test
specifier|public
name|void
name|testPostTable
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|arialIs
init|=
name|TestTTFParser
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/pdfbox/ttf/ArialMT.ttf"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|arialIs
argument_list|)
expr_stmt|;
name|TTFParser
name|parser
init|=
operator|new
name|TTFParser
argument_list|()
decl_stmt|;
name|TrueTypeFont
name|arial
init|=
name|parser
operator|.
name|parse
argument_list|(
name|arialIs
argument_list|)
decl_stmt|;
name|CmapTable
name|cmapTable
init|=
name|arial
operator|.
name|getCmap
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cmapTable
argument_list|)
expr_stmt|;
name|CmapSubtable
index|[]
name|cmaps
init|=
name|cmapTable
operator|.
name|getCmaps
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cmaps
argument_list|)
expr_stmt|;
name|CmapSubtable
name|cmap
init|=
literal|null
decl_stmt|;
for|for
control|(
name|CmapSubtable
name|e
range|:
name|cmaps
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getPlatformId
argument_list|()
operator|==
name|NameRecord
operator|.
name|PLATFORM_WINDOWS
operator|&&
name|e
operator|.
name|getPlatformEncodingId
argument_list|()
operator|==
name|NameRecord
operator|.
name|ENCODING_WINDOWS_UNICODE_BMP
condition|)
block|{
name|cmap
operator|=
name|e
expr_stmt|;
break|break;
block|}
block|}
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cmap
argument_list|)
expr_stmt|;
name|PostScriptTable
name|post
init|=
name|arial
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|post
argument_list|)
expr_stmt|;
name|String
index|[]
name|glyphNames
init|=
name|arial
operator|.
name|getPostScript
argument_list|()
operator|.
name|getGlyphNames
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|glyphNames
argument_list|)
expr_stmt|;
comment|// test a WGL4 (Macintosh standard) name
name|int
name|gid
init|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
literal|0x2122
argument_list|)
decl_stmt|;
comment|// TRADE MARK SIGN
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"trademark"
argument_list|,
name|glyphNames
index|[
name|gid
index|]
argument_list|)
expr_stmt|;
comment|// test an additional name
name|gid
operator|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
literal|0x20AC
argument_list|)
expr_stmt|;
comment|// EURO SIGN
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Euro"
argument_list|,
name|glyphNames
index|[
name|gid
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

