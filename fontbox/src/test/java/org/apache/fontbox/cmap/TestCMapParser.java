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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * This will test the CMapParser implementation.  *  * @version $Revision$  */
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
name|byte
index|[]
name|bytes1
init|=
block|{
literal|0
block|,
literal|65
block|}
decl_stmt|;
name|assertTrue
argument_list|(
literal|"A"
operator|.
name|equals
argument_list|(
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
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes2
init|=
block|{
literal|1
block|,
literal|24
block|}
decl_stmt|;
name|String
name|str2
init|=
literal|"\u0118"
decl_stmt|;
name|assertTrue
argument_list|(
name|str2
operator|.
name|equals
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

