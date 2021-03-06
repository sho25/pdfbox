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
package|;
end_package

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
name|graphics
operator|.
name|color
operator|.
name|PDOutputIntent
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test PDDocument Catalog functionality.  *  */
end_comment

begin_class
specifier|public
class|class
name|TestPDDocumentCatalog
block|{
comment|/**      * Test getPageLabels().      *       * Test case for      *<a href="https://issues.apache.org/jira/browse/PDFBOX-90"      *>PDFBOX-90</a> - Support explicit retrieval of page labels.      *         * @throws IOException in case the document can not be parsed.      */
annotation|@
name|Test
specifier|public
name|void
name|retrievePageLabels
parameter_list|()
throws|throws
name|IOException
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
name|Loader
operator|.
name|loadPDF
argument_list|(
name|TestPDDocumentCatalog
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test_pagelabels.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|cat
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|String
index|[]
name|labels
init|=
name|cat
operator|.
name|getPageLabels
argument_list|()
operator|.
name|getLabelsByPageIndices
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|labels
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A1"
argument_list|,
name|labels
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A2"
argument_list|,
name|labels
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A3"
argument_list|,
name|labels
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"i"
argument_list|,
name|labels
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ii"
argument_list|,
name|labels
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iii"
argument_list|,
name|labels
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"iv"
argument_list|,
name|labels
index|[
literal|6
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v"
argument_list|,
name|labels
index|[
literal|7
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"vi"
argument_list|,
name|labels
index|[
literal|8
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"vii"
argument_list|,
name|labels
index|[
literal|9
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Appendix I"
argument_list|,
name|labels
index|[
literal|10
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Appendix II"
argument_list|,
name|labels
index|[
literal|11
index|]
argument_list|)
expr_stmt|;
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
comment|/**      * Test page labels for malformed PDF.      *       * Test case for      *<a href="https://issues.apache.org/jira/browse/PDFBOX-900"      *>PDFBOX-900</a> - Handle malformed PDFs      *         * @throws IOException in case the document can not be parsed.      */
annotation|@
name|Test
specifier|public
name|void
name|retrievePageLabelsOnMalformedPdf
parameter_list|()
throws|throws
name|IOException
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
name|Loader
operator|.
name|loadPDF
argument_list|(
name|TestPDDocumentCatalog
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"badpagelabels.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|cat
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
comment|// getLabelsByPageIndices() should not throw an exception
name|cat
operator|.
name|getPageLabels
argument_list|()
operator|.
name|getLabelsByPageIndices
argument_list|()
expr_stmt|;
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
comment|/**      * Test getNumberOfPages().      *       * Test case for      *<a href="https://issues.apache.org/jira/browse/PDFBOX-911"      *>PDFBOX-911</a> - Method PDDocument.getNumberOfPages() returns wrong      * number of pages      *       * @throws IOException in case the document can not be parsed.      */
annotation|@
name|Test
specifier|public
name|void
name|retrieveNumberOfPages
parameter_list|()
throws|throws
name|IOException
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
name|Loader
operator|.
name|loadPDF
argument_list|(
name|TestPDDocumentCatalog
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test.unc.pdf"
argument_list|)
argument_list|)
expr_stmt|;
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
comment|/**      * Test OutputIntents functionality.      *       * Test case for      *<a https://issues.apache.org/jira/browse/PDFBOX-2687">PDFBOX-2687</a>      * ClassCastException when trying to get OutputIntents or add to it.      *       * @throws IOException in case the document can not be parsed.      */
annotation|@
name|Test
specifier|public
name|void
name|handleOutputIntents
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
name|InputStream
name|colorProfile
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|Loader
operator|.
name|loadPDF
argument_list|(
name|TestPDDocumentCatalog
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test.unc.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
comment|// retrieve OutputIntents
name|List
argument_list|<
name|PDOutputIntent
argument_list|>
name|outputIntents
init|=
name|catalog
operator|.
name|getOutputIntents
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|outputIntents
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// add an OutputIntent
name|colorProfile
operator|=
name|TestPDDocumentCatalog
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"sRGB.icc"
argument_list|)
expr_stmt|;
comment|// create output intent
name|PDOutputIntent
name|oi
init|=
operator|new
name|PDOutputIntent
argument_list|(
name|doc
argument_list|,
name|colorProfile
argument_list|)
decl_stmt|;
name|oi
operator|.
name|setInfo
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|oi
operator|.
name|setOutputCondition
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|oi
operator|.
name|setOutputConditionIdentifier
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|oi
operator|.
name|setRegistryName
argument_list|(
literal|"http://www.color.org"
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|addOutputIntent
argument_list|(
name|oi
argument_list|)
expr_stmt|;
comment|// retrieve OutputIntents
name|outputIntents
operator|=
name|catalog
operator|.
name|getOutputIntents
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|outputIntents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// set OutputIntents
name|catalog
operator|.
name|setOutputIntents
argument_list|(
name|outputIntents
argument_list|)
expr_stmt|;
name|outputIntents
operator|=
name|catalog
operator|.
name|getOutputIntents
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|outputIntents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|colorProfile
operator|!=
literal|null
condition|)
block|{
name|colorProfile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|handleBooleanInOpenAction
parameter_list|()
throws|throws
name|IOException
block|{
comment|//PDFBOX-3772 -- allow for COSBoolean
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|OPEN_ACTION
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getOpenAction
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

