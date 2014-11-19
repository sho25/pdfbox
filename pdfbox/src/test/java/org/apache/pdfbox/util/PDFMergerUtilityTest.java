begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|PDFRenderer
import|;
end_import

begin_comment
comment|/**  * Test suite for PDFMergerUtility.  *  * @author Maruan Sahyoun (PDF files)  * @author Tilman Hausherr (code)  */
end_comment

begin_class
specifier|public
class|class
name|PDFMergerUtilityTest
extends|extends
name|TestCase
block|{
specifier|final
name|String
name|SRCDIR
init|=
literal|"src/test/resources/input/merge/"
decl_stmt|;
specifier|final
name|String
name|TARGETTESTDIR
init|=
literal|"target/test-output/merge/"
decl_stmt|;
specifier|final
name|int
name|DPI
init|=
literal|96
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"could not create output directory"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Tests whether the merge of two PDF files with identically named but      * different global resources works. The two PDF files have two fonts each      * named /TT1 and /TT0 that are Arial and Courier and vice versa in the      * second file. Revisions before 1613017 fail this test because global      * resources were merged which made trouble when resources of the same kind      * had the same name.      *      * @throws IOException if something goes wrong.      */
specifier|public
name|void
name|testPDFMergerUtility
parameter_list|()
throws|throws
name|IOException
block|{
name|checkMergeIdentical
argument_list|(
literal|"PDFBox.GlobalResourceMergeTest.Doc01.decoded.pdf"
argument_list|,
literal|"PDFBox.GlobalResourceMergeTest.Doc02.decoded.pdf"
argument_list|,
literal|"GlobalResourceMergeTestResult.pdf"
argument_list|)
expr_stmt|;
block|}
comment|// checks that the result file of a merge has the same rendering as the two
comment|// source files
specifier|private
name|void
name|checkMergeIdentical
parameter_list|(
name|String
name|filename1
parameter_list|,
name|String
name|filename2
parameter_list|,
name|String
name|mergeFilename
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|srcDoc1
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|SRCDIR
argument_list|,
name|filename1
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|int
name|src1PageCount
init|=
name|srcDoc1
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|PDFRenderer
name|src1PdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|srcDoc1
argument_list|)
decl_stmt|;
name|BufferedImage
index|[]
name|src1ImageTab
init|=
operator|new
name|BufferedImage
index|[
name|src1PageCount
index|]
decl_stmt|;
for|for
control|(
name|int
name|page
init|=
literal|0
init|;
name|page
operator|<
name|src1PageCount
condition|;
operator|++
name|page
control|)
block|{
name|src1ImageTab
index|[
name|page
index|]
operator|=
name|src1PdfRenderer
operator|.
name|renderImageWithDPI
argument_list|(
name|page
argument_list|,
name|DPI
argument_list|)
expr_stmt|;
block|}
name|srcDoc1
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDDocument
name|srcDoc2
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|SRCDIR
argument_list|,
name|filename2
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|int
name|src2PageCount
init|=
name|srcDoc2
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|PDFRenderer
name|src2PdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|srcDoc2
argument_list|)
decl_stmt|;
name|BufferedImage
index|[]
name|src2ImageTab
init|=
operator|new
name|BufferedImage
index|[
name|src2PageCount
index|]
decl_stmt|;
for|for
control|(
name|int
name|page
init|=
literal|0
init|;
name|page
operator|<
name|src2PageCount
condition|;
operator|++
name|page
control|)
block|{
name|src2ImageTab
index|[
name|page
index|]
operator|=
name|src2PdfRenderer
operator|.
name|renderImageWithDPI
argument_list|(
name|page
argument_list|,
name|DPI
argument_list|)
expr_stmt|;
block|}
name|srcDoc2
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDFMergerUtility
name|pdfMergerUtility
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|pdfMergerUtility
operator|.
name|addSource
argument_list|(
operator|new
name|File
argument_list|(
name|SRCDIR
argument_list|,
name|filename1
argument_list|)
argument_list|)
expr_stmt|;
name|pdfMergerUtility
operator|.
name|addSource
argument_list|(
operator|new
name|File
argument_list|(
name|SRCDIR
argument_list|,
name|filename2
argument_list|)
argument_list|)
expr_stmt|;
name|pdfMergerUtility
operator|.
name|setDestinationFileName
argument_list|(
name|TARGETTESTDIR
operator|+
name|mergeFilename
argument_list|)
expr_stmt|;
name|pdfMergerUtility
operator|.
name|mergeDocuments
argument_list|()
expr_stmt|;
name|PDDocument
name|mergedDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
name|mergeFilename
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|PDFRenderer
name|mergePdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|mergedDoc
argument_list|)
decl_stmt|;
name|int
name|mergePageCount
init|=
name|mergedDoc
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|src1PageCount
operator|+
name|src2PageCount
argument_list|,
name|mergePageCount
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|page
init|=
literal|0
init|;
name|page
operator|<
name|src1PageCount
condition|;
operator|++
name|page
control|)
block|{
name|BufferedImage
name|bim
init|=
name|mergePdfRenderer
operator|.
name|renderImageWithDPI
argument_list|(
name|page
argument_list|,
name|DPI
argument_list|)
decl_stmt|;
name|checkImagesIdentical
argument_list|(
name|bim
argument_list|,
name|src1ImageTab
index|[
name|page
index|]
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|page
init|=
literal|0
init|;
name|page
operator|<
name|src2PageCount
condition|;
operator|++
name|page
control|)
block|{
name|int
name|mergePage
init|=
name|page
operator|+
name|src1PageCount
decl_stmt|;
name|BufferedImage
name|bim
init|=
name|mergePdfRenderer
operator|.
name|renderImageWithDPI
argument_list|(
name|mergePage
argument_list|,
name|DPI
argument_list|)
decl_stmt|;
name|checkImagesIdentical
argument_list|(
name|bim
argument_list|,
name|src2ImageTab
index|[
name|page
index|]
argument_list|)
expr_stmt|;
block|}
name|mergedDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|checkImagesIdentical
parameter_list|(
name|BufferedImage
name|bim1
parameter_list|,
name|BufferedImage
name|bim2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|bim1
operator|.
name|getHeight
argument_list|()
argument_list|,
name|bim2
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bim1
operator|.
name|getWidth
argument_list|()
argument_list|,
name|bim2
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|w
init|=
name|bim1
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
init|=
name|bim1
operator|.
name|getHeight
argument_list|()
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
name|w
condition|;
operator|++
name|i
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|h
condition|;
operator|++
name|j
control|)
block|{
name|assertEquals
argument_list|(
name|bim1
operator|.
name|getRGB
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|,
name|bim2
operator|.
name|getRGB
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

