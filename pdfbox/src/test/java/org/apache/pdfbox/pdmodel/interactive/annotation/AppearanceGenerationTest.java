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
name|annotation
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
name|assertTrue
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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
name|COSFloat
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
comment|/*  * Tests the appearance generation for annotations generated using  * Adobe Reader DC.  *   * - gets the annotation  * - gets the appearance stream and it's tokens  * - removes the appearance stream  * - regenerates the appearance  * - compares the tokens from the original to the ones created by PDFBox  *   * For the initial work only the token operators are compared to ensure the   * same basic operation. Upon refinement the operators parameters could also be  * verified.  *   */
end_comment

begin_class
specifier|public
class|class
name|AppearanceGenerationTest
block|{
comment|// delta for comparing equality of float values
comment|// a difference in float values smaller than this
comment|// will be treated equal between Adobe and PDFBox
comment|// values.
comment|// TODO: revisit that number as our code improves
specifier|private
specifier|static
specifier|final
name|float
name|DELTA
init|=
literal|3e-3f
decl_stmt|;
comment|// the location of the annotation
specifier|static
name|PDRectangle
name|rectangle
decl_stmt|;
specifier|private
name|PDDocument
name|document
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
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/annotation"
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
literal|"target/test-output/pdmodel/interactive/annotation"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_OF_PDF
init|=
literal|"AnnotationTypes.pdf"
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
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|// Test currently disabled as the content stream differs
annotation|@
name|Test
specifier|public
name|void
name|rectangleFullStrokeNoFill
parameter_list|()
throws|throws
name|IOException
block|{
name|PDPage
name|page
init|=
name|document
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDAnnotation
name|annotation
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// get the tokens of the content stream generated by Adobe
name|PDAppearanceStream
name|appearanceContentStream
init|=
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
decl_stmt|;
name|PDFStreamParser
name|streamParser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|appearanceContentStream
argument_list|)
decl_stmt|;
name|streamParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokensForOriginal
init|=
name|streamParser
operator|.
name|getTokens
argument_list|()
decl_stmt|;
comment|// get the tokens for the content stream generated by PDFBox
name|annotation
operator|.
name|getCOSObject
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|constructAppearances
argument_list|()
expr_stmt|;
name|appearanceContentStream
operator|=
name|annotation
operator|.
name|getNormalAppearanceStream
argument_list|()
expr_stmt|;
name|streamParser
operator|=
operator|new
name|PDFStreamParser
argument_list|(
name|appearanceContentStream
argument_list|)
expr_stmt|;
name|streamParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokensForPdfbox
init|=
name|streamParser
operator|.
name|getTokens
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The number of tokens in the content stream should be the same"
argument_list|,
name|tokensForOriginal
operator|.
name|size
argument_list|()
argument_list|,
name|tokensForPdfbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|actualToken
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|tokenForOriginal
range|:
name|tokensForOriginal
control|)
block|{
name|Object
name|tokenForPdfbox
init|=
name|tokensForPdfbox
operator|.
name|get
argument_list|(
name|actualToken
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The tokens should have the same type"
argument_list|,
name|tokenForOriginal
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|tokenForPdfbox
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenForOriginal
operator|instanceof
name|Operator
condition|)
block|{
name|assertEquals
argument_list|(
literal|"The operator generated by PDFBox should be the same Operator"
argument_list|,
operator|(
operator|(
name|Operator
operator|)
name|tokenForOriginal
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
operator|(
name|Operator
operator|)
name|tokenForPdfbox
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenForOriginal
operator|instanceof
name|COSFloat
condition|)
block|{
name|assertTrue
argument_list|(
literal|"The difference between the numbers should be smaller than "
operator|+
name|DELTA
argument_list|,
operator|(
name|Math
operator|.
name|abs
argument_list|(
operator|(
operator|(
name|COSFloat
operator|)
name|tokenForOriginal
operator|)
operator|.
name|floatValue
argument_list|()
operator|-
operator|(
operator|(
name|COSFloat
operator|)
name|tokenForPdfbox
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
operator|<
name|DELTA
operator|)
argument_list|)
expr_stmt|;
block|}
name|actualToken
operator|++
expr_stmt|;
block|}
comment|// Save the file for manual comparison for now
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|NAME_OF_PDF
operator|+
literal|"-newAP.pdf"
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
comment|// we should render similar to Adobe Reader using the original file
annotation|@
name|Test
specifier|public
name|void
name|renderTest
parameter_list|()
throws|throws
name|IOException
block|{
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

