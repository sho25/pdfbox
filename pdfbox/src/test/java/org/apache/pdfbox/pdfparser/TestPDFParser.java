begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
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
name|FileInputStream
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
name|net
operator|.
name|URISyntaxException
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
name|io
operator|.
name|RandomAccessBufferedFileInputStream
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
name|RandomAccessRead
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
name|ScratchFile
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
name|PDDocumentInformation
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
name|util
operator|.
name|DateConverter
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
name|TestPDFParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PATH_OF_PDF
init|=
literal|"src/test/resources/input/yaddatest.pdf"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|tmpDirectory
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
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
specifier|private
name|int
name|numberOfTmpFiles
init|=
literal|0
decl_stmt|;
comment|/**      * Initialize the number of tmp file before the test      *       * @throws Exception      */
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|numberOfTmpFiles
operator|=
name|getNumberOfTempFile
argument_list|()
expr_stmt|;
block|}
comment|/**      * Count the number of temporary files      *       * @return      */
specifier|private
name|int
name|getNumberOfTempFile
parameter_list|()
block|{
name|int
name|result
init|=
literal|0
decl_stmt|;
name|File
index|[]
name|tmpPdfs
init|=
name|tmpDirectory
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
name|name
operator|.
name|startsWith
argument_list|(
name|COSParser
operator|.
name|TMP_FILE_PREFIX
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|"pdf"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpPdfs
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|tmpPdfs
operator|.
name|length
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserFile
parameter_list|()
throws|throws
name|IOException
block|{
name|executeParserTest
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|executeParserTest
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserFileScratchFile
parameter_list|()
throws|throws
name|IOException
block|{
name|executeParserTest
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserInputStreamScratchFile
parameter_list|()
throws|throws
name|IOException
block|{
name|executeParserTest
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
argument_list|,
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFParserMissingCatalog
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
comment|// PDFBOX-3060
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TestPDFParser
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"MissingCatalog.pdf"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test whether /Info dictionary is retrieved correctly when rebuilding the trailer of a corrupt      * file. An incorrect algorithm would result in an outline dictionary being mistaken for an      * /Info.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3208
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3208-L33MUTT2SVCWGCS6UIYL5TH3PNPXHIS6.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDDocumentInformation
name|di
init|=
name|doc
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Liquent Enterprise Services"
argument_list|,
name|di
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Liquent services server"
argument_list|,
name|di
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Amyuni PDF Converter version 4.0.0.9"
argument_list|,
name|di
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|di
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|di
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"892B77DE781B4E71A1BEFB81A51A5ABC_20140326022424.docx"
argument_list|,
name|di
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"D:20140326142505-02'00'"
argument_list|)
argument_list|,
name|di
operator|.
name|getCreationDate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"20140326172513Z"
argument_list|)
argument_list|,
name|di
operator|.
name|getModificationDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test whether the /Info is retrieved correctly when rebuilding the trailer of a corrupt file,      * despite the /Info dictionary not having a modification date.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3940
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3940-079977.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDDocumentInformation
name|di
init|=
name|doc
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unknown"
argument_list|,
name|di
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C:REGULA~1IREGSFR_EQ_EM.WP"
argument_list|,
name|di
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acrobat PDFWriter 3.02 for Windows"
argument_list|,
name|di
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|di
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|di
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C:REGULA~1IREGSFR_EQ_EM.PDF"
argument_list|,
name|di
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"Tuesday, July 28, 1998 4:00:09 PM"
argument_list|)
argument_list|,
name|di
operator|.
name|getCreationDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * PDFBOX-3783: test parsing of file with trash after %%EOF.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3783
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3783-72GLBIGUC6LB46ELZFBARRJTLN4RBSQM.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * PDFBOX-3785, PDFBOX-3957:      * Test whether truncated file with several revisions has correct page count.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3785
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3785-202097.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * PDFBOX-3947: test parsing of file with broken object stream.      *      * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3947
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3947-670064.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * PDFBOX-3948: test parsing of file with object stream containing some unexpected newlines.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3948
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3948-EUWO6SQS5TM4VGOMRD3FLXZHU35V2CP2.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * PDFBOX-3949: test parsing of file with incomplete object stream.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3949
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3949-MKFYUGZWS3OPXLLVU2Z4LWCTVA5WNOGF.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * PDFBOX-3950: test parsing and rendering of truncated file with missing pages.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3950
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3950-23EGDHXSBBYQLKYOKGZUOVYVNE675PRD.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|PDFRenderer
name|renderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|doc
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
name|doc
operator|.
name|getNumberOfPages
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
try|try
block|{
name|renderer
operator|.
name|renderImage
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
literal|3
operator|&&
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Missing descendant font array"
argument_list|)
condition|)
block|{
continue|continue;
block|}
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
comment|/**      * PDFBOX-3951: test parsing of truncated file.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3951
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3951-FIHUZWDDL2VGPOE34N6YHWSIGSH5LVGZ.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|143
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * PDFBOX-3964: test parsing of broken file.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3964
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3964-c687766d68ac766be3f02aaec5e0d713_2.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test whether /Info dictionary is retrieved correctly in brute force search for the      * Info/Catalog dictionaries.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3977
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-3977-63NGFQRI44HQNPIPEJH5W2TBM6DJZWMI.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDDocumentInformation
name|di
init|=
name|doc
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"QuarkXPress(tm) 6.52"
argument_list|,
name|di
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acrobat Distiller 7.0 pour Macintosh"
argument_list|,
name|di
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Fich sal Fabr corr1 (Page 6)"
argument_list|,
name|di
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"D:20070608151915+02'00'"
argument_list|)
argument_list|,
name|di
operator|.
name|getCreationDate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"D:20080604152122+02'00'"
argument_list|)
argument_list|,
name|di
operator|.
name|getModificationDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test parsing the "genko_oc_shiryo1.pdf" file, which is susceptible to regression.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testParseGenko
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"genko_oc_shiryo1.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test parsing the file from PDFBOX-4338, which brought an      * ArrayIndexOutOfBoundsException before the bug was fixed.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox4338
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-4338.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test parsing the file from PDFBOX-4339, which brought a      * NullPointerException before the bug was fixed.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox4339
parameter_list|()
throws|throws
name|IOException
block|{
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-4339.pdf"
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|executeParserTest
parameter_list|(
name|RandomAccessRead
name|source
parameter_list|,
name|MemoryUsageSetting
name|memUsageSetting
parameter_list|)
throws|throws
name|IOException
block|{
name|ScratchFile
name|scratchFile
init|=
operator|new
name|ScratchFile
argument_list|(
name|memUsageSetting
argument_list|)
decl_stmt|;
name|PDFParser
name|pdfParser
init|=
operator|new
name|PDFParser
argument_list|(
name|source
argument_list|,
name|scratchFile
argument_list|)
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc
init|=
name|pdfParser
operator|.
name|parse
argument_list|()
init|)
block|{
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
name|source
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// number tmp file must be the same
name|assertEquals
argument_list|(
name|numberOfTmpFiles
argument_list|,
name|getNumberOfTempFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

