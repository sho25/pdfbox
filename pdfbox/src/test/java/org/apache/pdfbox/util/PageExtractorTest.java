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
name|util
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

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|fail
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

begin_comment
comment|/**  * Test suite for PageExtractor.  *  * This is just some simple tests based on a test document.  It merely ensures  * that the correct number of pages are extracted as this is virtually the only  * thing which could go wrong when coping pages from one PDF to a new one.  *  * @author Adam Nichols (adam@apache.org)  */
end_comment

begin_class
specifier|public
class|class
name|PageExtractorTest
extends|extends
name|TestCase
block|{
specifier|public
name|PageExtractorTest
parameter_list|(
name|String
name|testName
parameter_list|)
block|{
name|super
argument_list|(
name|testName
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|private
name|void
name|closeDoc
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* Can't do much about this... */
block|}
empty_stmt|;
block|}
block|}
comment|/**      * Test of extract method, of class org.apache.pdfbox.util.PageExtractor.      */
specifier|public
name|void
name|testExtract
parameter_list|()
throws|throws
name|Exception
block|{
name|PDDocument
name|sourcePdf
init|=
literal|null
decl_stmt|;
name|PDDocument
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
try|try
block|{
comment|// this should work for most users
name|sourcePdf
operator|=
name|PDDocument
operator|.
name|loadLegacy
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/input/cweb.pdf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// in case your directory structure is different, just change this to be the correct path
name|sourcePdf
operator|=
name|PDDocument
operator|.
name|loadLegacy
argument_list|(
operator|new
name|File
argument_list|(
literal|"pdfbox/pdfbox/src/test/resources/input/cweb.pdf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PageExtractor
name|instance
init|=
operator|new
name|PageExtractor
argument_list|(
name|sourcePdf
argument_list|)
decl_stmt|;
name|result
operator|=
name|instance
operator|.
name|extract
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|sourcePdf
operator|.
name|getNumberOfPages
argument_list|()
argument_list|,
name|result
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|PageExtractor
argument_list|(
name|sourcePdf
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|result
operator|=
name|instance
operator|.
name|extract
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|PageExtractor
argument_list|(
name|sourcePdf
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|result
operator|=
name|instance
operator|.
name|extract
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|result
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|PageExtractor
argument_list|(
name|sourcePdf
argument_list|,
literal|5
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|result
operator|=
name|instance
operator|.
name|extract
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|result
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|PageExtractor
argument_list|(
name|sourcePdf
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|result
operator|=
name|instance
operator|.
name|extract
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeDoc
argument_list|(
name|sourcePdf
argument_list|)
expr_stmt|;
name|closeDoc
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

