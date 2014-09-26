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
name|edit
package|;
end_package

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
name|COSStream
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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

begin_comment
comment|/**  * @author Yegor Kozlov  */
end_comment

begin_class
specifier|public
class|class
name|TestPDPageContentStream
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSetCmykColors
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
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
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// pass a non-stroking color in CMYK color space
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
literal|0.1f
argument_list|,
literal|0.2f
argument_list|,
literal|0.3f
argument_list|,
literal|0.4f
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// now read the PDF stream and verify that the CMYK values are correct
name|COSStream
name|stream
init|=
name|page
operator|.
name|getContents
argument_list|()
operator|.
name|getStream
argument_list|()
decl_stmt|;
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|Object
argument_list|>
name|pageTokens
init|=
name|parser
operator|.
name|getTokens
argument_list|()
decl_stmt|;
comment|// expected five tokens :
comment|// [0] = COSFloat{0.1}
comment|// [1] = COSFloat{0.2}
comment|// [2] = COSFloat{0.3}
comment|// [3] = COSFloat{0.4}
comment|// [4] = PDFOperator{"k"}
name|assertEquals
argument_list|(
literal|0.1f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.2f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.3f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.4f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"k"
argument_list|,
operator|(
operator|(
name|Operator
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// same as above but for PDPageContentStream#setStrokingColor
name|page
operator|=
operator|new
name|PDPage
argument_list|()
expr_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|contentStream
operator|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// pass a non-stroking color in CMYK color space
name|contentStream
operator|.
name|setStrokingColor
argument_list|(
literal|0.5f
argument_list|,
literal|0.6f
argument_list|,
literal|0.7f
argument_list|,
literal|0.8f
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// now read the PDF stream and verify that the CMYK values are correct
name|stream
operator|=
name|page
operator|.
name|getContents
argument_list|()
operator|.
name|getStream
argument_list|()
expr_stmt|;
name|parser
operator|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|pageTokens
operator|=
name|parser
operator|.
name|getTokens
argument_list|()
expr_stmt|;
comment|// expected five tokens  :
comment|// [0] = COSFloat{0.5}
comment|// [1] = COSFloat{0.6}
comment|// [2] = COSFloat{0.7}
comment|// [3] = COSFloat{0.8}
comment|// [4] = PDFOperator{"K"}
name|assertEquals
argument_list|(
literal|0.5f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.6f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.7f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.8f
argument_list|,
operator|(
operator|(
name|COSFloat
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"K"
argument_list|,
operator|(
operator|(
name|Operator
operator|)
name|pageTokens
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

