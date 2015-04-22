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
name|cos
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
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * Test for PDFDocEncoding.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDFDocEncodingTest
block|{
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|deviations
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
comment|// all deviations (based on the table in ISO 32000-1:2008)
comment|// block 1
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02D8'
argument_list|)
argument_list|)
expr_stmt|;
comment|// BREVE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02C7'
argument_list|)
argument_list|)
expr_stmt|;
comment|// CARON
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02C6'
argument_list|)
argument_list|)
expr_stmt|;
comment|// MODIFIER LETTER CIRCUMFLEX ACCENT
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02D9'
argument_list|)
argument_list|)
expr_stmt|;
comment|// DOT ABOVE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02DD'
argument_list|)
argument_list|)
expr_stmt|;
comment|// DOUBLE ACUTE ACCENT
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02DB'
argument_list|)
argument_list|)
expr_stmt|;
comment|// OGONEK
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02DA'
argument_list|)
argument_list|)
expr_stmt|;
comment|// RING ABOVE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u02DC'
argument_list|)
argument_list|)
expr_stmt|;
comment|// SMALL TILDE
comment|// block 2
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2022'
argument_list|)
argument_list|)
expr_stmt|;
comment|// BULLET
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2020'
argument_list|)
argument_list|)
expr_stmt|;
comment|// DAGGER
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2021'
argument_list|)
argument_list|)
expr_stmt|;
comment|// DOUBLE DAGGER
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2026'
argument_list|)
argument_list|)
expr_stmt|;
comment|// HORIZONTAL ELLIPSIS
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2014'
argument_list|)
argument_list|)
expr_stmt|;
comment|// EM DASH
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2013'
argument_list|)
argument_list|)
expr_stmt|;
comment|// EN DASH
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0192'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LETTER SCRIPT F
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2044'
argument_list|)
argument_list|)
expr_stmt|;
comment|// FRACTION SLASH (solidus)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2039'
argument_list|)
argument_list|)
expr_stmt|;
comment|// SINGLE LEFT-POINTING ANGLE QUOTATION MARK
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u203A'
argument_list|)
argument_list|)
expr_stmt|;
comment|// SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2212'
argument_list|)
argument_list|)
expr_stmt|;
comment|// MINUS SIGN
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2030'
argument_list|)
argument_list|)
expr_stmt|;
comment|// PER MILLE SIGN
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u201E'
argument_list|)
argument_list|)
expr_stmt|;
comment|// DOUBLE LOW-9 QUOTATION MARK (quotedblbase)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u201C'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LEFT DOUBLE QUOTATION MARK (quotedblleft)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u201D'
argument_list|)
argument_list|)
expr_stmt|;
comment|// RIGHT DOUBLE QUOTATION MARK (quotedblright)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2018'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LEFT SINGLE QUOTATION MARK (quoteleft)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2019'
argument_list|)
argument_list|)
expr_stmt|;
comment|// RIGHT SINGLE QUOTATION MARK (quoteright)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u201A'
argument_list|)
argument_list|)
expr_stmt|;
comment|// SINGLE LOW-9 QUOTATION MARK (quotesinglbase)
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u2122'
argument_list|)
argument_list|)
expr_stmt|;
comment|// TRADE MARK SIGN
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\uFB01'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LIGATURE FI
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\uFB02'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LIGATURE FL
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0141'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN CAPITAL LETTER L WITH STROKE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0152'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN CAPITAL LIGATURE OE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0160'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN CAPITAL LETTER S WITH CARON
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0178'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN CAPITAL LETTER Y WITH DIAERESIS
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u017D'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN CAPITAL LETTER Z WITH CARON
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0131'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LETTER DOTLESS I
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0142'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LETTER L WITH STROKE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0153'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LIGATURE OE
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u0161'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LETTER S WITH CARON
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u017E'
argument_list|)
argument_list|)
expr_stmt|;
comment|// LATIN SMALL LETTER Z WITH CARON
name|deviations
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|'\u20AC'
argument_list|)
argument_list|)
expr_stmt|;
comment|// EURO SIGN
comment|// end of deviations
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeviations
parameter_list|()
block|{
for|for
control|(
name|String
name|deviation
range|:
name|deviations
control|)
block|{
name|COSString
name|cosString
init|=
operator|new
name|COSString
argument_list|(
name|deviation
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|cosString
operator|.
name|getString
argument_list|()
argument_list|,
name|deviation
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

