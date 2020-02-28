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
name|assertTrue
import|;
end_import

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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
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
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|Loader
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
name|PDFRenderer
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
comment|/**  * Test flatten different forms and compare with rendering.  *  * Some of the tests are currently disabled to not run within the CI environment  * as the test results need manual inspection. Enable as needed.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDAcroFormFlattenTest
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
literal|"target/test-output/flatten/in"
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
literal|"target/test-output/flatten/out"
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|IN_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/*      * PDFBOX-142 Filled template.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBOX142
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12742551/Testformular1.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"Testformular1.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-563 Filled template.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX563
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12425859/TestFax_56972.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"TestFax_56972.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-2469 Empty template.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX2469Empty
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12682897/FormI-9-English.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"FormI-9-English.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-2469 Filled template.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX2469Filled
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12678455/testPDF_acroForm.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"testPDF_acroForm.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-2586 Empty template.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX2586
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12689788/test.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"test-2586.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3083 Filled template rotated.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBOX3083
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12770263/mypdf.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"mypdf.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3262 Hidden fields      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX3262
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12792007/hidden_fields.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"hidden_fields.pdf"
decl_stmt|;
name|assertTrue
argument_list|(
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3396 Signed Document 1.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX3396_1
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12816014/Signed-Document-1.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"Signed-Document-1.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3396 Signed Document 2.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX3396_2
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12816016/Signed-Document-2.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"Signed-Document-2.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3396 Signed Document 3.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX3396_3
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12821307/Signed-Document-3.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"Signed-Document-3.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3396 Signed Document 4.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBOX3396_4
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12821308/Signed-Document-4.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"Signed-Document-4.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3587 Empty template.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenOpenOfficeForm
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12839977/OpenOfficeForm.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"OpenOfficeForm.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * PDFBOX-3587 Filled template.      */
comment|// @Test
specifier|public
name|void
name|testFlattenOpenOfficeFormFilled
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12840280/OpenOfficeForm_filled.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"OpenOfficeForm_filled.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-4157 Filled template.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBox4157
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12976553/PDFBOX-4157-filled.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"PDFBOX-4157-filled.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-4172 Filled template.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBox4172
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12976552/PDFBOX-4172-filled.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"PDFBOX-4172-filled.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-4615 Filled template.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBox4615
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12976452/resetboundingbox-filled.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"PDFBOX-4615-filled.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-4693: page is not rotated, but the appearance stream is.      */
annotation|@
name|Test
specifier|public
name|void
name|testFlattenPDFBox4693
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12986337/stenotypeTest-3_rotate_no_flatten.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"PDFBOX-4693-filled.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-4788: non-widget annotations are not to be removed on a page that has no widget      * annotations.      */
comment|// @Test
specifier|public
name|void
name|testFlattenPDFBox4788
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|sourceUrl
init|=
literal|"https://issues.apache.org/jira/secure/attachment/12994791/flatten.pdf"
decl_stmt|;
name|String
name|targetFileName
init|=
literal|"PDFBOX-4788.pdf"
decl_stmt|;
name|flattenAndCompare
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
comment|/*      * Flatten and compare with generated image samples.      */
specifier|private
specifier|static
name|boolean
name|flattenAndCompare
parameter_list|(
name|String
name|sourceUrl
parameter_list|,
name|String
name|targetFileName
parameter_list|)
throws|throws
name|IOException
block|{
name|generateSamples
argument_list|(
name|sourceUrl
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
name|File
name|inputFile
init|=
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|targetFileName
argument_list|)
decl_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|targetFileName
argument_list|)
decl_stmt|;
try|try
init|(
name|PDDocument
name|testPdf
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
name|inputFile
argument_list|)
init|)
block|{
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
name|testPdf
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
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
name|testPdf
operator|.
name|save
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
block|}
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
name|outputFile
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
name|fail
argument_list|(
literal|"Rendering of "
operator|+
name|outputFile
operator|+
literal|" failed or is not identical to expected rendering in "
operator|+
name|IN_DIR
operator|+
literal|" directory"
argument_list|)
expr_stmt|;
name|removeMatchingRenditions
argument_list|(
name|inputFile
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// cleanup input and output directory for matching files.
name|removeAllRenditions
argument_list|(
name|inputFile
argument_list|)
expr_stmt|;
name|inputFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|outputFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/*      * Generate the sample images to which the PDF will be compared after flatten.      */
specifier|private
specifier|static
name|void
name|generateSamples
parameter_list|(
name|String
name|sourceUrl
parameter_list|,
name|String
name|targetFile
parameter_list|)
throws|throws
name|IOException
block|{
name|getFromUrl
argument_list|(
name|sourceUrl
argument_list|,
name|targetFile
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|targetFile
argument_list|)
decl_stmt|;
try|try
init|(
name|PDDocument
name|document
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
name|file
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
init|)
block|{
name|String
name|outputPrefix
init|=
name|IN_DIR
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|'/'
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
decl_stmt|;
name|int
name|numPages
init|=
name|document
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numPages
condition|;
name|i
operator|++
control|)
block|{
name|String
name|fileName
init|=
name|outputPrefix
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
operator|+
literal|".png"
decl_stmt|;
name|BufferedImage
name|image
init|=
name|renderer
operator|.
name|renderImageWithDPI
argument_list|(
name|i
argument_list|,
literal|96
argument_list|)
decl_stmt|;
comment|// Windows native DPI
name|ImageIO
operator|.
name|write
argument_list|(
name|image
argument_list|,
literal|"PNG"
argument_list|,
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Get a PDF from URL and copy to file for processing.      */
specifier|private
specifier|static
name|void
name|getFromUrl
parameter_list|(
name|String
name|sourceUrl
parameter_list|,
name|String
name|targetFile
parameter_list|)
throws|throws
name|IOException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|sourceUrl
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
init|;
name|OutputStream
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|targetFile
argument_list|)
argument_list|)
init|)
block|{
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|length
decl_stmt|;
while|while
condition|(
operator|(
name|length
operator|=
name|is
operator|.
name|read
argument_list|(
name|b
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Remove renditions for the PDF from the input directory for which there is no      * corresponding rendition in the output directory.      * Renditions in the output directory which were identical to the ones in the      * input directory will have been deleted by the TestPDFToImage utility.      */
specifier|private
specifier|static
name|void
name|removeMatchingRenditions
parameter_list|(
specifier|final
name|File
name|inputFile
parameter_list|)
block|{
name|File
index|[]
name|testFiles
init|=
name|inputFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|startsWith
argument_list|(
name|inputFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".png"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|testFile
range|:
name|testFiles
control|)
block|{
if|if
condition|(
operator|!
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|testFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
name|testFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Remove renditions for the PDF from the input directory.      * The output directory will have been cleaned by the TestPDFToImage utility.      */
specifier|private
specifier|static
name|void
name|removeAllRenditions
parameter_list|(
specifier|final
name|File
name|inputFile
parameter_list|)
block|{
name|File
index|[]
name|testFiles
init|=
name|inputFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|startsWith
argument_list|(
name|inputFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".png"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|testFile
range|:
name|testFiles
control|)
block|{
name|testFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

