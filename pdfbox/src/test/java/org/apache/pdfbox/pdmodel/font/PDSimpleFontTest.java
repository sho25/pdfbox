begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2011 adam.  *   *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|awt
operator|.
name|image
operator|.
name|BufferedImage
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
name|PDPage
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
name|PDSimpleFontTest
block|{
specifier|public
name|PDSimpleFontTest
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
comment|/**      * Test of the error reported in PDFBox-998      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox988
parameter_list|()
throws|throws
name|Exception
block|{
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|PDSimpleFontTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"F001u_3_7j.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|pages
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|pages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
name|page
operator|.
name|convertToImage
argument_list|()
decl_stmt|;
comment|// The alligation is that convertToImage() will crash the JVM or hang
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Test of the error reported in PDFBox-1019      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox1019
parameter_list|()
throws|throws
name|Exception
block|{
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|PDSimpleFontTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"256.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|pages
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|pages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
name|page
operator|.
name|convertToImage
argument_list|()
decl_stmt|;
comment|// The alligation is that convertToImage() will crash the JVM or hang
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

