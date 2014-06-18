begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2010 adam.  *   *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdfparser
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
name|net
operator|.
name|URL
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
name|assertNotNull
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
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
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
comment|/**  *  * @author adam  */
end_comment

begin_class
specifier|public
class|class
name|ConformingPDFParserTest
block|{
specifier|public
name|ConformingPDFParserTest
parameter_list|()
block|{     }
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|tearDownClass
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{     }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{     }
comment|/**      * Test of parse method, of class ConformingPDFParser.      */
annotation|@
name|Test
specifier|public
name|void
name|testParse
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|inputUrl
init|=
name|ConformingPDFParser
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"gdb-refcard.pdf"
argument_list|)
decl_stmt|;
name|File
name|inputFile
init|=
operator|new
name|File
argument_list|(
name|inputUrl
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|ConformingPDFParser
name|instance
init|=
operator|new
name|ConformingPDFParser
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
name|instance
operator|.
name|parse
argument_list|()
expr_stmt|;
name|COSDictionary
name|trailer
init|=
name|instance
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|trailer
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Trailer: "
operator|+
name|instance
operator|.
name|getDocument
argument_list|()
operator|.
name|getTrailer
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|trailer
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
literal|"Root"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
literal|"Info"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|trailer
operator|.
name|getDictionaryObject
argument_list|(
literal|"Size"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

