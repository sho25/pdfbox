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
name|interactive
operator|.
name|form
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
name|rendering
operator|.
name|TestPDFToImage
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
name|Before
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

begin_class
specifier|public
class|class
name|AcroFormsRotationTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|OUT_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|IN_DIR
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/form"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_OF_PDF
init|=
literal|"AcroFormsRotation.pdf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"Lorem ipsum dolor sit amet, consetetur sadipscing elitr,"
operator|+
literal|" sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua."
decl_stmt|;
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|NAME_OF_PDF
argument_list|)
argument_list|)
expr_stmt|;
name|acroForm
operator|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
expr_stmt|;
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fillFields
parameter_list|()
throws|throws
name|IOException
block|{
comment|// portrait page
comment|// single line fields
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.single.rotation0"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.single.rotation90"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.single.rotation180"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.single.rotation270"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
comment|// multiline fields
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.multi.rotation0"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.multi.rotation90"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.multi.rotation180"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.portrait.multi.rotation270"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
comment|// 90 degrees rotated page
comment|// single line fields
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.single.rotation0"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"pdfbox.page90.single.rotation0"
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.single.rotation90"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"pdfbox.page90.single.rotation90"
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.single.rotation180"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"pdfbox.page90.single.rotation180"
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.single.rotation270"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"pdfbox.page90.single.rotation270"
argument_list|)
expr_stmt|;
comment|// multiline fields
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.multi.rotation0"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.multi.rotation90"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.multi.rotation180"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|field
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox.page90.multi.rotation270"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
name|field
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"\n"
operator|+
name|TEST_VALUE
argument_list|)
expr_stmt|;
comment|// compare rendering
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|NAME_OF_PDF
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|TestPDFToImage
name|testPDFToImage
init|=
operator|new
name|TestPDFToImage
argument_list|(
name|TestPDFToImage
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|testPDFToImage
operator|.
name|doTestFile
argument_list|(
name|file
argument_list|,
name|IN_DIR
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|OUT_DIR
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
condition|)
block|{
comment|// don't fail, rendering is different on different systems, result
comment|// must be viewed manually
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Rendering of "
operator|+
name|file
operator|+
literal|" failed or is not identical to expected rendering in "
operator|+
name|IN_DIR
operator|+
literal|" directory"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
