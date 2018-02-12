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
name|multipdf
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
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
name|interactive
operator|.
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDField
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
comment|/**  * Test merging different PDFs with AcroForms.  *   *   */
end_comment

begin_class
specifier|public
class|class
name|MergeAcroFormsTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|IN_DIR
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/pdfbox/multipdf"
argument_list|)
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
literal|"target/test-output/merge/"
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/*      * Test LegacyMode merge      */
annotation|@
name|Test
specifier|public
name|void
name|testLegacyModeMerge
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|File
name|toBeMerged
init|=
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
literal|"AcroFormForMerge.pdf"
argument_list|)
decl_stmt|;
name|File
name|pdfOutput
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"PDFBoxLegacyMerge-SameMerged.pdf"
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setDestinationFileName
argument_list|(
name|pdfOutput
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|toBeMerged
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|toBeMerged
argument_list|)
expr_stmt|;
name|merger
operator|.
name|mergeDocuments
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
init|(
name|PDDocument
name|compliantDocument
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
literal|"PDFBoxLegacyMerge-SameMerged.pdf"
argument_list|)
argument_list|)
init|;
name|PDDocument
name|toBeCompared
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"PDFBoxLegacyMerge-SameMerged.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDAcroForm
name|compliantAcroForm
init|=
name|compliantDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDAcroForm
name|toBeComparedAcroForm
init|=
name|toBeCompared
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be the same number of root fields"
argument_list|,
name|compliantAcroForm
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|toBeComparedAcroForm
operator|.
name|getFields
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PDField
name|compliantField
range|:
name|compliantAcroForm
operator|.
name|getFieldTree
argument_list|()
control|)
block|{
name|assertNotNull
argument_list|(
literal|"There shall be a field with the same FQN"
argument_list|,
name|toBeComparedAcroForm
operator|.
name|getField
argument_list|(
name|compliantField
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|PDField
name|toBeComparedField
init|=
name|toBeComparedAcroForm
operator|.
name|getField
argument_list|(
name|compliantField
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|compareFieldProperties
argument_list|(
name|compliantField
argument_list|,
name|toBeComparedField
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PDField
name|toBeComparedField
range|:
name|toBeComparedAcroForm
operator|.
name|getFieldTree
argument_list|()
control|)
block|{
name|assertNotNull
argument_list|(
literal|"There shall be a field with the same FQN"
argument_list|,
name|compliantAcroForm
operator|.
name|getField
argument_list|(
name|toBeComparedField
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|PDField
name|compliantField
init|=
name|compliantAcroForm
operator|.
name|getField
argument_list|(
name|toBeComparedField
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|compareFieldProperties
argument_list|(
name|toBeComparedField
argument_list|,
name|compliantField
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|compareFieldProperties
parameter_list|(
name|PDField
name|sourceField
parameter_list|,
name|PDField
name|toBeComapredField
parameter_list|)
block|{
comment|// List of keys for comparison
comment|// Don't include too complex properties such as AP as this will fail the test because
comment|// of a stack overflow when
specifier|final
name|String
index|[]
name|keys
init|=
block|{
literal|"FT"
block|,
literal|"T"
block|,
literal|"TU"
block|,
literal|"TM"
block|,
literal|"Ff"
block|,
literal|"V"
block|,
literal|"DV"
block|,
literal|"Opts"
block|,
literal|"TI"
block|,
literal|"I"
block|,
literal|"Rect"
block|,
literal|"DA"
block|, }
decl_stmt|;
name|COSDictionary
name|sourceFieldCos
init|=
name|sourceField
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSDictionary
name|toBeComparedCos
init|=
name|toBeComapredField
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|COSBase
name|sourceBase
init|=
name|sourceFieldCos
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|COSBase
name|toBeComparedBase
init|=
name|toBeComparedCos
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceBase
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"The content of the field properties shall be the same"
argument_list|,
name|sourceBase
operator|.
name|toString
argument_list|()
argument_list|,
name|toBeComparedBase
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNull
argument_list|(
literal|"If the source property is null the compared property shall be null too"
argument_list|,
name|toBeComparedBase
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*      * PDFBOX-1031 Ensure that after merging the PDFs there is an Annots entry per page.      */
annotation|@
name|Test
specifier|public
name|void
name|testAnnotsEntry
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Merge the PDFs form PDFBOX-1031
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|URL
name|url1
init|=
operator|new
name|URL
argument_list|(
literal|"https://issues.apache.org/jira/secure/attachment/12481683/1.pdf"
argument_list|)
decl_stmt|;
name|InputStream
name|is1
init|=
name|url1
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|URL
name|url2
init|=
operator|new
name|URL
argument_list|(
literal|"https://issues.apache.org/jira/secure/attachment/12481684/2.pdf"
argument_list|)
decl_stmt|;
name|InputStream
name|is2
init|=
name|url2
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|File
name|pdfOutput
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"PDFBOX-1031.pdf"
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setDestinationFileName
argument_list|(
name|pdfOutput
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is1
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is2
argument_list|)
expr_stmt|;
name|merger
operator|.
name|mergeDocuments
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Test merge result
name|PDDocument
name|mergedPDF
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfOutput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 2 pages"
argument_list|,
literal|2
argument_list|,
name|mergedPDF
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be an /Annots entry for the first page"
argument_list|,
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ANNOTS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 1 annotation for the first page"
argument_list|,
literal|1
argument_list|,
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getAnnotations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be an /Annots entry for the second page"
argument_list|,
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|1
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ANNOTS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 1 annotation for the second page"
argument_list|,
literal|1
argument_list|,
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getAnnotations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|mergedPDF
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/*      * PDFBOX-1100 Ensure that after merging the PDFs there is an AP and V entry.      */
annotation|@
name|Test
specifier|public
name|void
name|testAPEntry
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Merge the PDFs form PDFBOX-1100
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|URL
name|url1
init|=
operator|new
name|URL
argument_list|(
literal|"https://issues.apache.org/jira/secure/attachment/12490774/a.pdf"
argument_list|)
decl_stmt|;
name|InputStream
name|is1
init|=
name|url1
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|URL
name|url2
init|=
operator|new
name|URL
argument_list|(
literal|"https://issues.apache.org/jira/secure/attachment/12490775/b.pdf"
argument_list|)
decl_stmt|;
name|InputStream
name|is2
init|=
name|url2
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|File
name|pdfOutput
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"PDFBOX-1100.pdf"
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setDestinationFileName
argument_list|(
name|pdfOutput
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is1
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is2
argument_list|)
expr_stmt|;
name|merger
operator|.
name|mergeDocuments
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Test merge result
name|PDDocument
name|mergedPDF
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfOutput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 2 pages"
argument_list|,
literal|2
argument_list|,
name|mergedPDF
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|PDAcroForm
name|acroForm
init|=
name|mergedPDF
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDField
name|formField
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"Testfeld"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be an /AP entry for the field"
argument_list|,
name|formField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be a /V entry for the field"
argument_list|,
name|formField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
argument_list|)
expr_stmt|;
name|formField
operator|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"Testfeld2"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be an /AP entry for the field"
argument_list|,
name|formField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"There shall be a /V entry for the field"
argument_list|,
name|formField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
argument_list|)
expr_stmt|;
name|mergedPDF
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

