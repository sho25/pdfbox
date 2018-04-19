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
name|multipdf
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
name|cos
operator|.
name|COSArray
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
name|cos
operator|.
name|COSObject
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
name|io
operator|.
name|MemoryUsageSetting
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
name|PDPageTree
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDStructureElement
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDStructureTreeRoot
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageFitDestination
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
specifier|private
specifier|static
specifier|final
name|File
name|TARGETPDFDIR
init|=
operator|new
name|File
argument_list|(
literal|"target/pdfs"
argument_list|)
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
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
comment|// once again, with scratch file
name|checkMergeIdentical
argument_list|(
literal|"PDFBox.GlobalResourceMergeTest.Doc01.decoded.pdf"
argument_list|,
literal|"PDFBox.GlobalResourceMergeTest.Doc02.decoded.pdf"
argument_list|,
literal|"GlobalResourceMergeTestResult2.pdf"
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests whether the merge of two PDF files with JPEG and CCITT works. A few revisions before      * 1704911 this test failed because the clone utility attempted to decode and re-encode the      * streams, see PDFBOX-2893 on 23.9.2015.      *      * @throws IOException if something goes wrong.      */
specifier|public
name|void
name|testJpegCcitt
parameter_list|()
throws|throws
name|IOException
block|{
name|checkMergeIdentical
argument_list|(
literal|"jpegrgb.pdf"
argument_list|,
literal|"multitiff.pdf"
argument_list|,
literal|"JpegMultiMergeTestResult.pdf"
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
comment|// once again, with scratch file
name|checkMergeIdentical
argument_list|(
literal|"jpegrgb.pdf"
argument_list|,
literal|"multitiff.pdf"
argument_list|,
literal|"JpegMultiMergeTestResult.pdf"
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// see PDFBOX-2893
specifier|public
name|void
name|testPDFMergerUtility2
parameter_list|()
throws|throws
name|IOException
block|{
name|checkMergeIdentical
argument_list|(
literal|"PDFBox.GlobalResourceMergeTest.Doc01.pdf"
argument_list|,
literal|"PDFBox.GlobalResourceMergeTest.Doc02.pdf"
argument_list|,
literal|"GlobalResourceMergeTestResult.pdf"
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
comment|// once again, with scratch file
name|checkMergeIdentical
argument_list|(
literal|"PDFBox.GlobalResourceMergeTest.Doc01.pdf"
argument_list|,
literal|"PDFBox.GlobalResourceMergeTest.Doc02.pdf"
argument_list|,
literal|"GlobalResourceMergeTestResult2.pdf"
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3972: Test that OpenAction page destination isn't lost after merge.      *       * @throws IOException       */
specifier|public
name|void
name|testPDFMergerOpenAction
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc1
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|doc1
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc1
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc1
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc1
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"MergerOpenActionTest1.pdf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PDPageDestination
name|dest
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc2
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|doc2
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc2
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|doc2
operator|.
name|addPage
argument_list|(
operator|new
name|PDPage
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|=
operator|new
name|PDPageFitDestination
argument_list|()
expr_stmt|;
name|dest
operator|.
name|setPage
argument_list|(
name|doc2
operator|.
name|getPage
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|doc2
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setOpenAction
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|doc2
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"MergerOpenActionTest2.pdf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|TARGETTESTDIR
argument_list|,
literal|"MergerOpenActionTest1.pdf"
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
name|TARGETTESTDIR
argument_list|,
literal|"MergerOpenActionTest2.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|pdfMergerUtility
operator|.
name|setDestinationFileName
argument_list|(
name|TARGETTESTDIR
operator|+
literal|"MergerOpenActionTestResult.pdf"
argument_list|)
expr_stmt|;
name|pdfMergerUtility
operator|.
name|mergeDocuments
argument_list|(
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
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
literal|"MergerOpenActionTestResult.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDDocumentCatalog
name|documentCatalog
init|=
name|mergedDoc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|dest
operator|=
operator|(
name|PDPageDestination
operator|)
name|documentCatalog
operator|.
name|getOpenAction
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|documentCatalog
operator|.
name|getPages
argument_list|()
operator|.
name|indexOf
argument_list|(
name|dest
operator|.
name|getPage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * PDFBOX-3999: check that page entries in the structure tree only reference pages from the page      * tree, i.e. that no orphan pages exist.      *       * @throws IOException       */
specifier|public
name|void
name|testStructureTreeMerge
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFMergerUtility
name|pdfMergerUtility
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|PDDocument
name|src
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDDocument
name|dst
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|pdfMergerUtility
operator|.
name|appendDocument
argument_list|(
name|dst
argument_list|,
name|src
argument_list|)
expr_stmt|;
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
name|dst
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance-merged.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|dst
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDDocument
name|doc
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
literal|"PDFBOX-3999-GeneralForbearance-merged.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDPageTree
name|pageTree
init|=
name|doc
operator|.
name|getPages
argument_list|()
decl_stmt|;
comment|// check for orphan pages in the StructTreeRoot/K and StructTreeRoot/ParentTree trees.
name|PDStructureTreeRoot
name|structureTreeRoot
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getStructureTreeRoot
argument_list|()
decl_stmt|;
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|structureTreeRoot
operator|.
name|getParentTree
argument_list|()
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|structureTreeRoot
operator|.
name|getK
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3999: check that no streams are kept from the source document by the destination      * document, despite orphan annotations remaining in the structure tree.      *      * @throws IOException      */
specifier|public
name|void
name|testStructureTreeMerge2
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFMergerUtility
name|pdfMergerUtility
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|doc
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
name|doc
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance-flattened.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDDocument
name|src
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
literal|"PDFBOX-3999-GeneralForbearance-flattened.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|PDDocument
name|dst
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
literal|"PDFBOX-3999-GeneralForbearance-flattened.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|pdfMergerUtility
operator|.
name|appendDocument
argument_list|(
name|dst
argument_list|,
name|src
argument_list|)
expr_stmt|;
comment|// before solving PDFBOX-3999, the close() below brought
comment|// IOException: COSStream has been closed and cannot be read.
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
name|dst
operator|.
name|save
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance-flattened-merged.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|dst
operator|.
name|close
argument_list|()
expr_stmt|;
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETTESTDIR
argument_list|,
literal|"PDFBOX-3999-GeneralForbearance-flattened-merged.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|PDPageTree
name|pageTree
init|=
name|doc
operator|.
name|getPages
argument_list|()
decl_stmt|;
comment|// check for orphan pages in the StructTreeRoot/K and StructTreeRoot/ParentTree trees.
name|PDStructureTreeRoot
name|structureTreeRoot
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getStructureTreeRoot
argument_list|()
decl_stmt|;
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|structureTreeRoot
operator|.
name|getParentTree
argument_list|()
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|structureTreeRoot
operator|.
name|getK
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Each element can be an array, a dictionary or a number.
comment|// See PDF specification Table 37 - Entries in a number tree node dictionary
comment|// See PDF specification Table 322 - Entries in the structure tree root
comment|// See PDF specification Table 323 - Entries in a structure element dictionary
comment|// example of file with /Kids: 000153.pdf 000208.pdf 000314.pdf 000359.pdf 000671.pdf
comment|// from digitalcorpora site
specifier|private
name|void
name|checkElement
parameter_list|(
name|PDPageTree
name|pageTree
parameter_list|,
name|COSBase
name|base
parameter_list|)
block|{
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|base2
range|:
operator|(
name|COSArray
operator|)
name|base
control|)
block|{
if|if
condition|(
name|base2
operator|instanceof
name|COSObject
condition|)
block|{
name|base2
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|base2
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|base2
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|kdict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|PG
argument_list|)
condition|)
block|{
name|PDStructureElement
name|structureElement
init|=
operator|new
name|PDStructureElement
argument_list|(
name|kdict
argument_list|)
decl_stmt|;
name|checkForPage
argument_list|(
name|pageTree
argument_list|,
name|structureElement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
condition|)
block|{
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|kdict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// if we're in a number tree, check /Nums and /Kids
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
condition|)
block|{
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|kdict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|)
condition|)
block|{
name|checkElement
argument_list|(
name|pageTree
argument_list|,
name|kdict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NUMS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// checks that the result file of a merge has the same rendering as the two source files
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
parameter_list|,
name|MemoryUsageSetting
name|memUsageSetting
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|src1PageCount
decl_stmt|;
name|BufferedImage
index|[]
name|src1ImageTab
decl_stmt|;
try|try
init|(
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
operator|(
name|String
operator|)
literal|null
argument_list|)
init|)
block|{
name|src1PageCount
operator|=
name|srcDoc1
operator|.
name|getNumberOfPages
argument_list|()
expr_stmt|;
name|PDFRenderer
name|src1PdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|srcDoc1
argument_list|)
decl_stmt|;
name|src1ImageTab
operator|=
operator|new
name|BufferedImage
index|[
name|src1PageCount
index|]
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
block|}
name|int
name|src2PageCount
decl_stmt|;
name|BufferedImage
index|[]
name|src2ImageTab
decl_stmt|;
try|try
init|(
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
operator|(
name|String
operator|)
literal|null
argument_list|)
init|)
block|{
name|src2PageCount
operator|=
name|srcDoc2
operator|.
name|getNumberOfPages
argument_list|()
expr_stmt|;
name|PDFRenderer
name|src2PdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|srcDoc2
argument_list|)
decl_stmt|;
name|src2ImageTab
operator|=
operator|new
name|BufferedImage
index|[
name|src2PageCount
index|]
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
block|}
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
argument_list|(
name|memUsageSetting
argument_list|)
expr_stmt|;
try|try
init|(
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
operator|(
name|String
operator|)
literal|null
argument_list|)
init|)
block|{
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
block|}
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
specifier|private
name|void
name|checkForPage
parameter_list|(
name|PDPageTree
name|pageTree
parameter_list|,
name|PDStructureElement
name|structureElement
parameter_list|)
block|{
name|PDPage
name|page
init|=
name|structureElement
operator|.
name|getPage
argument_list|()
decl_stmt|;
if|if
condition|(
name|page
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Page is not in the page tree"
argument_list|,
name|pageTree
operator|.
name|indexOf
argument_list|(
name|page
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

