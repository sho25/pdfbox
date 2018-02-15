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
name|cmap
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
name|java
operator|.
name|net
operator|.
name|URL
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * This will test the CMap implementation.  *  */
end_comment

begin_class
specifier|public
class|class
name|TestCMap
extends|extends
name|TestCase
block|{
comment|/**      * Check whether the mapping is working correct.      * @throws IOException If something went wrong during adding a mapping      */
specifier|public
name|void
name|testLookup
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bs
init|=
operator|new
name|byte
index|[
literal|1
index|]
decl_stmt|;
name|bs
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
literal|200
expr_stmt|;
name|CMap
name|cMap
init|=
operator|new
name|CMap
argument_list|()
decl_stmt|;
name|cMap
operator|.
name|addCharMapping
argument_list|(
name|bs
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"a"
operator|.
name|equals
argument_list|(
name|cMap
operator|.
name|toUnicode
argument_list|(
literal|200
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3997: test unicode that is above the basic multilingual plane, here: helicopter      * symbol, or D83D DE81 in the Noto Emoji font.      *      * @throws IOException      */
specifier|public
name|void
name|testPDFBox3997
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|is
decl_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Downloading Noto Emoji font..."
argument_list|)
expr_stmt|;
name|is
operator|=
operator|new
name|URL
argument_list|(
literal|"https://issues.apache.org/jira/secure/attachment/12896461/NotoEmoji-Regular.ttf"
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Download finished!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Noto Emoji font could not be downloaded, test skipped"
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
init|(
name|TrueTypeFont
name|ttf
init|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parse
argument_list|(
name|is
argument_list|)
init|)
block|{
name|CmapLookup
name|cmap
init|=
name|ttf
operator|.
name|getUnicodeCmapLookup
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|886
argument_list|,
name|cmap
operator|.
name|getGlyphId
argument_list|(
literal|0x1F681
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

