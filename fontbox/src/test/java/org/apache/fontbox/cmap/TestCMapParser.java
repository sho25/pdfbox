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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|Arrays
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
comment|/**  * This will test the CMapParser implementation.  *  */
end_comment

begin_class
specifier|public
class|class
name|TestCMapParser
extends|extends
name|TestCase
block|{
comment|/**      * Check whether the parser and the resulting mapping is working correct.      * @throws IOException If something went wrong      */
specifier|public
name|void
name|testLookup
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|resourceDir
init|=
literal|"src/test/resources/cmap"
decl_stmt|;
name|File
name|inDir
init|=
operator|new
name|File
argument_list|(
name|resourceDir
argument_list|)
decl_stmt|;
name|CMapParser
name|parser
init|=
operator|new
name|CMapParser
argument_list|()
decl_stmt|;
name|CMap
name|cMap
init|=
name|parser
operator|.
name|parse
argument_list|(
name|resourceDir
argument_list|,
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|inDir
argument_list|,
literal|"CMapTest"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// code space range
name|assertEquals
argument_list|(
literal|"codeSpaceRanges size"
argument_list|,
literal|1
argument_list|,
name|cMap
operator|.
name|getCodeSpaceRanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|byte
index|[]
name|expectedStart
init|=
block|{
literal|0
block|,
literal|0
block|}
decl_stmt|;
comment|// 00 00
specifier|final
name|byte
index|[]
name|expectedEnd
init|=
block|{
literal|2
block|,
operator|-
literal|1
block|}
decl_stmt|;
comment|// 02 FF
specifier|final
name|byte
index|[]
name|actualStart
init|=
name|cMap
operator|.
name|getCodeSpaceRanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStart
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|actualEnd
init|=
name|cMap
operator|.
name|getCodeSpaceRanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEnd
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"codeSpaceRange start"
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|expectedStart
argument_list|,
name|actualStart
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"codeSpaceRange end"
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|expectedEnd
argument_list|,
name|actualEnd
argument_list|)
argument_list|)
expr_stmt|;
comment|// char mappings
name|byte
index|[]
name|bytes1
init|=
block|{
literal|0
block|,
literal|1
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bytes 00 01 from bfrange<0001><0009><0041>"
argument_list|,
literal|"A"
argument_list|,
name|cMap
operator|.
name|lookup
argument_list|(
name|bytes1
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes2
init|=
block|{
literal|1
block|,
literal|00
block|}
decl_stmt|;
name|String
name|str2
init|=
literal|"0"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bytes 01 00 from bfrange<0100><0109><0030>"
argument_list|,
name|str2
argument_list|,
name|cMap
operator|.
name|lookup
argument_list|(
name|bytes2
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes3
init|=
block|{
literal|0
block|,
literal|10
block|}
decl_stmt|;
name|String
name|str3
init|=
literal|"*"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bytes 00 0A from bfchar<000A><002A>"
argument_list|,
name|str3
argument_list|,
name|cMap
operator|.
name|lookup
argument_list|(
name|bytes3
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes4
init|=
block|{
literal|1
block|,
literal|10
block|}
decl_stmt|;
name|String
name|str4
init|=
literal|"+"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bytes 01 0A from bfchar<010A><002B>"
argument_list|,
name|str4
argument_list|,
name|cMap
operator|.
name|lookup
argument_list|(
name|bytes4
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// CID mappings
name|int
name|cid1
init|=
literal|65
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CID 65 from cidrange<0000><00ff> 0 "
argument_list|,
literal|"A"
argument_list|,
name|cMap
operator|.
name|lookupCID
argument_list|(
name|cid1
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|cid2
init|=
literal|280
decl_stmt|;
name|String
name|strCID2
init|=
literal|"\u0118"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CID 280 from cidrange<0100><01ff> 256"
argument_list|,
name|strCID2
argument_list|,
name|cMap
operator|.
name|lookupCID
argument_list|(
name|cid2
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|cid3
init|=
literal|520
decl_stmt|;
name|String
name|strCID3
init|=
literal|"\u0208"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CID 520 from cidchar<0208> 520"
argument_list|,
name|strCID3
argument_list|,
name|cMap
operator|.
name|lookupCID
argument_list|(
name|cid3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

