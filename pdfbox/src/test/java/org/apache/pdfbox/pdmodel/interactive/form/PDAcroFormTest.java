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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
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
name|PDDocumentCatalog
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
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDResources
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
name|common
operator|.
name|PDRectangle
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
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

begin_comment
comment|/**  * Test for the PDButton class.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDAcroFormTest
block|{
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
decl_stmt|;
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
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|document
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|acroForm
operator|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFieldsEntry
parameter_list|()
block|{
comment|// the /Fields entry has been created with the AcroForm
comment|// as this is a required entry
name|assertNotNull
argument_list|(
name|acroForm
operator|.
name|getFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|acroForm
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// there shouldn't be an exception if there is no such field
name|assertNull
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove the required entry which is the case for some
comment|// PDFs (see PDFBOX-2965)
name|acroForm
operator|.
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|FIELDS
argument_list|)
expr_stmt|;
comment|// ensure there is always an empty collection returned
name|assertNotNull
argument_list|(
name|acroForm
operator|.
name|getFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|acroForm
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// there shouldn't be an exception if there is no such field
name|assertNull
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcroFormProperties
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|acroForm
operator|.
name|getDefaultAppearance
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|setDefaultAppearance
argument_list|(
literal|"/Helv 0 Tf 0 g"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|acroForm
operator|.
name|getDefaultAppearance
argument_list|()
argument_list|,
literal|"/Helv 0 Tf 0 g"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFlatten
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|testPdf
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
literal|"AlignmentTests.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|testPdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
operator|.
name|flatten
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|testPdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
operator|.
name|getFields
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"AlignmentTests-flattened.pdf"
argument_list|)
decl_stmt|;
name|testPdf
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|// compare rendering
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
comment|// don't fail, rendering is different on different systems, result must be viewed manually
name|System
operator|.
name|out
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
comment|/*      * Same as above but remove the page reference from the widget annotation      * before doing the flatten() to ensure that the widgets page reference is properly looked up      * (PDFBOX-3301)      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenWidgetNoRef
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|testPdf
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
literal|"AlignmentTests.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDAcroForm
name|acroForm
init|=
name|testPdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
for|for
control|(
name|PDField
name|field
range|:
name|acroForm
operator|.
name|getFieldTree
argument_list|()
control|)
block|{
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|field
operator|.
name|getWidgets
argument_list|()
control|)
block|{
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
expr_stmt|;
block|}
block|}
name|testPdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
operator|.
name|flatten
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|testPdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
operator|.
name|getFields
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"AlignmentTests-flattened-noRef.pdf"
argument_list|)
decl_stmt|;
name|testPdf
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|// compare rendering
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
comment|// don't fail, rendering is different on different systems, result must be viewed manually
name|System
operator|.
name|out
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
comment|/*      * Test that we do not modify an AcroForm with missing resource information      * when loading the document only.      * (PDFBOX-3752)      */
annotation|@
name|Test
specifier|public
name|void
name|testDontAddMissingInformationOnDocumentLoad
parameter_list|()
block|{
try|try
block|{
name|byte
index|[]
name|pdfBytes
init|=
name|createAcroFormWithMissingResourceInformation
argument_list|()
decl_stmt|;
name|PDDocument
name|pdfDocument
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfBytes
argument_list|)
decl_stmt|;
comment|// do a low level access to the AcroForm to avoid the generation of missing entries
name|PDDocumentCatalog
name|documentCatalog
init|=
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|COSDictionary
name|catalogDictionary
init|=
name|documentCatalog
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSDictionary
name|acroFormDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|catalogDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ACRO_FORM
argument_list|)
decl_stmt|;
comment|// ensure that the missing information has not been generated
name|assertNull
argument_list|(
name|acroFormDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|acroFormDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
argument_list|)
expr_stmt|;
name|pdfDocument
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Couldn't create test document, test skipped"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|/*      * Test that we add missing ressouce information to an AcroForm       * when accessing the AcroForm on the PD level      * (PDFBOX-3752)      */
annotation|@
name|Test
specifier|public
name|void
name|testAddMissingInformationOnAcroFormAccess
parameter_list|()
block|{
try|try
block|{
name|byte
index|[]
name|pdfBytes
init|=
name|createAcroFormWithMissingResourceInformation
argument_list|()
decl_stmt|;
name|PDDocument
name|pdfDocument
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfBytes
argument_list|)
decl_stmt|;
name|PDDocumentCatalog
name|documentCatalog
init|=
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
comment|// this call shall trigger the generation of missing information
name|PDAcroForm
name|theAcroForm
init|=
name|documentCatalog
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
comment|// ensure that the missing information has been generated
comment|// DA entry
name|assertEquals
argument_list|(
literal|"/Helv 0 Tf 0 g "
argument_list|,
name|theAcroForm
operator|.
name|getDefaultAppearance
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|theAcroForm
operator|.
name|getDefaultResources
argument_list|()
argument_list|)
expr_stmt|;
comment|// DR entry
name|PDResources
name|acroFormResources
init|=
name|theAcroForm
operator|.
name|getDefaultResources
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|acroFormResources
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Helv"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Helvetica"
argument_list|,
name|acroFormResources
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Helv"
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|acroFormResources
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ZaDb"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ZapfDingbats"
argument_list|,
name|acroFormResources
operator|.
name|getFont
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ZaDb"
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|pdfDocument
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Couldn't create test document, test skipped"
argument_list|)
expr_stmt|;
return|return;
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
specifier|private
name|byte
index|[]
name|createAcroFormWithMissingResourceInformation
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDAcroForm
name|newAcroForm
init|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|newAcroForm
argument_list|)
expr_stmt|;
name|PDTextField
name|textBox
init|=
operator|new
name|PDTextField
argument_list|(
name|newAcroForm
argument_list|)
decl_stmt|;
name|textBox
operator|.
name|setPartialName
argument_list|(
literal|"SampleField"
argument_list|)
expr_stmt|;
name|newAcroForm
operator|.
name|getFields
argument_list|()
operator|.
name|add
argument_list|(
name|textBox
argument_list|)
expr_stmt|;
name|PDAnnotationWidget
name|widget
init|=
name|textBox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|(
literal|50
argument_list|,
literal|750
argument_list|,
literal|200
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|widget
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|page
operator|.
name|getAnnotations
argument_list|()
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
comment|// acroForm.setNeedAppearances(true);
comment|// acroForm.getField("SampleField").getCOSObject().setString(COSName.V, "content");
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|baos
argument_list|)
expr_stmt|;
comment|// this is a working PDF
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

