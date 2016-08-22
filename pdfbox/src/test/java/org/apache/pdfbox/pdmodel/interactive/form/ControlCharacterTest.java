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
name|util
operator|.
name|ArrayList
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
name|cos
operator|.
name|COSString
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|annotation
operator|.
name|PDAnnotationWidget
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
comment|/**  * Test handling some special characters when setting a fields value.  *   * Compare the results of setting the values using PDFBox with setting the values  * via Acrobat using JavaScript and manual input.  *   * The JavaScript used for acrobat is  *   *<pre>  * {@code  * this.getField("acrobat-nul").value = "NUL\0NUL";  * this.getField("acrobat-tab").value = "TAB\tTAB";  * this.getField("acrobat-space").value = "SPACE SPACE";  * this.getField("acrobat-cr").value = "CR\rCR";  * this.getField("acrobat-lf").value = "LF\nLF";  * this.getField("acrobat-crlf").value = "CRLF\r\nCRLF";  * this.getField("acrobat-lfcr").value = "LFCR\n\rLFCR";  * this.getField("acrobat-linebreak").value = "linebreak\u2028linebreak";  * this.getField("acrobat-paragraphbreak").value = "paragraphbreak\u2029paragraphbreak";  * }  *</pre>  *   * @see<a href="https://issues.apache.org/jira/browse/PDFBOX-3461">https://issues.apache.org/jira/browse/PDFBOX-3461</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|ControlCharacterTest
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
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/form"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_OF_PDF
init|=
literal|"ControlCharacters.pdf"
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
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|characterNUL
parameter_list|()
throws|throws
name|IOException
block|{
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-nul"
argument_list|)
operator|.
name|setValue
argument_list|(
literal|"NUL\0NUL"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|characterTAB
parameter_list|()
throws|throws
name|IOException
block|{
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-tab"
argument_list|)
operator|.
name|setValue
argument_list|(
literal|"TAB\tTAB"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterSPACE
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-space"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"SPACE SPACE"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-space"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterCR
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-cr"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"CR\rCR"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-cr"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterLF
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-lf"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"LF\nLF"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-lf"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterCRLF
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-crlf"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"CRLF\r\nCRLF"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-crlf"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterLFCR
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-lfcr"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"LFCR\n\rLFCR"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-lfcr"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterUnicodeLinebreak
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-linebreak"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"linebreak\u2028linebreak"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-linebreak"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|characterUnicodeParagraphbreak
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"pdfbox-paragraphbreak"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"paragraphbreak\u2029paragraphbreak"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pdfboxValues
init|=
name|getStringsFromStream
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acrobatValues
init|=
name|getStringsFromStream
argument_list|(
name|acroForm
operator|.
name|getField
argument_list|(
literal|"acrobat-paragraphbreak"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pdfboxValues
argument_list|,
name|acrobatValues
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|String
argument_list|>
name|getStringsFromStream
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAnnotationWidget
name|widget
init|=
name|field
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|widget
operator|.
name|getNormalAppearanceStream
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|token
init|=
name|parser
operator|.
name|parseNextToken
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|stringValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|COSString
condition|)
block|{
comment|// TODO: improve the string output to better match
comment|// trimming as Acrobat adds spaces to strings
comment|// where we don't
name|stringValues
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|token
operator|)
operator|.
name|getString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|token
operator|=
name|parser
operator|.
name|parseNextToken
argument_list|()
expr_stmt|;
block|}
return|return
name|stringValues
return|;
block|}
block|}
end_class

end_unit

