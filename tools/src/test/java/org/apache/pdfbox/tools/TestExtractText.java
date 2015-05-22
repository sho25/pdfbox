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
name|tools
package|;
end_package

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
name|PrintStream
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
comment|/**  * Test suite for ExtractText.   */
end_comment

begin_class
specifier|public
class|class
name|TestExtractText
extends|extends
name|TestCase
block|{
comment|/**      * Run the text extraction test using a pdf with embedded pdfs.      *       * @throws Exception if something went wrong      */
specifier|public
name|void
name|testEmbeddedPDFs
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|outBytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|stdout
init|=
name|System
operator|.
name|out
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|outBytes
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|ExtractText
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/org/apache/pdfbox/testPDFPackage.pdf"
block|,
literal|"-console"
block|,
literal|"-encoding UTF-8"
block|}
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Restore stdout
name|System
operator|.
name|setOut
argument_list|(
name|stdout
argument_list|)
expr_stmt|;
block|}
name|String
name|result
init|=
name|outBytes
operator|.
name|toString
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"PDF1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"PDF2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

