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
name|encoding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * {@link Charset} implementation for the "PDFDocEncoding" from the PDF specification.  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|PDFDocEncodingCharset
extends|extends
name|SingleByteCharset
block|{
comment|/** Singleton instance. */
specifier|public
specifier|static
specifier|final
name|PDFDocEncodingCharset
name|INSTANCE
init|=
operator|new
name|PDFDocEncodingCharset
argument_list|()
decl_stmt|;
comment|/**      * Creates a new "PDFDocEncoding" charset.      */
specifier|public
name|PDFDocEncodingCharset
parameter_list|()
block|{
name|super
argument_list|(
literal|"PDFDocEncoding"
argument_list|,
literal|null
argument_list|,
name|createEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|char
index|[]
name|createEncoding
parameter_list|()
block|{
name|char
index|[]
name|encoding
init|=
operator|new
name|char
index|[
literal|256
index|]
decl_stmt|;
comment|//Initialize with basically ISO-8859-1
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|encoding
index|[
name|i
index|]
operator|=
operator|(
name|char
operator|)
name|i
expr_stmt|;
block|}
comment|//...then do all deviations (based on the table in ISO 32000-1:2008)
comment|//block 1
name|encoding
index|[
literal|0x18
index|]
operator|=
literal|'\u02D8'
expr_stmt|;
comment|//BREVE
name|encoding
index|[
literal|0x19
index|]
operator|=
literal|'\u02C7'
expr_stmt|;
comment|//CARON
name|encoding
index|[
literal|0x1A
index|]
operator|=
literal|'\u02C6'
expr_stmt|;
comment|//MODIFIER LETTER CIRCUMFLEX ACCENT
name|encoding
index|[
literal|0x1B
index|]
operator|=
literal|'\u02D9'
expr_stmt|;
comment|//DOT ABOVE
name|encoding
index|[
literal|0x1C
index|]
operator|=
literal|'\u02DD'
expr_stmt|;
comment|//DOUBLE ACUTE ACCENT
name|encoding
index|[
literal|0x1D
index|]
operator|=
literal|'\u02DB'
expr_stmt|;
comment|//OGONEK
name|encoding
index|[
literal|0x1E
index|]
operator|=
literal|'\u02DA'
expr_stmt|;
comment|//RING ABOVE
name|encoding
index|[
literal|0x1F
index|]
operator|=
literal|'\u02DC'
expr_stmt|;
comment|//SMALL TILDE
comment|//block 2
name|encoding
index|[
literal|0x7F
index|]
operator|=
name|REPLACEMENT_CHARACTER
expr_stmt|;
comment|//undefined
name|encoding
index|[
literal|0x80
index|]
operator|=
literal|'\u2022'
expr_stmt|;
comment|//BULLET
name|encoding
index|[
literal|0x81
index|]
operator|=
literal|'\u2020'
expr_stmt|;
comment|//DAGGER
name|encoding
index|[
literal|0x82
index|]
operator|=
literal|'\u2021'
expr_stmt|;
comment|//DOUBLE DAGGER
name|encoding
index|[
literal|0x83
index|]
operator|=
literal|'\u2026'
expr_stmt|;
comment|//HORIZONTAL ELLIPSIS
name|encoding
index|[
literal|0x84
index|]
operator|=
literal|'\u2014'
expr_stmt|;
comment|//EM DASH
name|encoding
index|[
literal|0x85
index|]
operator|=
literal|'\u2013'
expr_stmt|;
comment|//EN DASH
name|encoding
index|[
literal|0x86
index|]
operator|=
literal|'\u0192'
expr_stmt|;
comment|//LATIN SMALL LETTER SCRIPT F
name|encoding
index|[
literal|0x87
index|]
operator|=
literal|'\u2044'
expr_stmt|;
comment|//FRACTION SLASH (solidus)
name|encoding
index|[
literal|0x88
index|]
operator|=
literal|'\u2039'
expr_stmt|;
comment|//SINGLE LEFT-POINTING ANGLE QUOTATION MARK
name|encoding
index|[
literal|0x89
index|]
operator|=
literal|'\u203A'
expr_stmt|;
comment|//SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
name|encoding
index|[
literal|0x8A
index|]
operator|=
literal|'\u2212'
expr_stmt|;
comment|//MINUS SIGN
name|encoding
index|[
literal|0x8B
index|]
operator|=
literal|'\u2030'
expr_stmt|;
comment|//PER MILLE SIGN
name|encoding
index|[
literal|0x8C
index|]
operator|=
literal|'\u201E'
expr_stmt|;
comment|//DOUBLE LOW-9 QUOTATION MARK (quotedblbase)
name|encoding
index|[
literal|0x8D
index|]
operator|=
literal|'\u201C'
expr_stmt|;
comment|//LEFT DOUBLE QUOTATION MARK (double quote left)
name|encoding
index|[
literal|0x8E
index|]
operator|=
literal|'\u201D'
expr_stmt|;
comment|//RIGHT DOUBLE QUOTATION MARK (quotedblright)
name|encoding
index|[
literal|0x8F
index|]
operator|=
literal|'\u2018'
expr_stmt|;
comment|//LEFT SINGLE QUOTATION MARK (quoteleft)
name|encoding
index|[
literal|0x90
index|]
operator|=
literal|'\u2019'
expr_stmt|;
comment|//RIGHT SINGLE QUOTATION MARK (quoteright)
name|encoding
index|[
literal|0x91
index|]
operator|=
literal|'\u201A'
expr_stmt|;
comment|//SINGLE LOW-9 QUOTATION MARK (quotesinglbase)
name|encoding
index|[
literal|0x92
index|]
operator|=
literal|'\u2122'
expr_stmt|;
comment|//TRADE MARK SIGN
name|encoding
index|[
literal|0x93
index|]
operator|=
literal|'\uFB01'
expr_stmt|;
comment|//LATIN SMALL LIGATURE FI
name|encoding
index|[
literal|0x94
index|]
operator|=
literal|'\uFB02'
expr_stmt|;
comment|//LATIN SMALL LIGATURE FL
name|encoding
index|[
literal|0x95
index|]
operator|=
literal|'\u0141'
expr_stmt|;
comment|//LATIN CAPITAL LETTER L WITH STROKE
name|encoding
index|[
literal|0x96
index|]
operator|=
literal|'\u0152'
expr_stmt|;
comment|//LATIN CAPITAL LIGATURE OE
name|encoding
index|[
literal|0x97
index|]
operator|=
literal|'\u0160'
expr_stmt|;
comment|//LATIN CAPITAL LETTER S WITH CARON
name|encoding
index|[
literal|0x98
index|]
operator|=
literal|'\u0178'
expr_stmt|;
comment|//LATIN CAPITAL LETTER Y WITH DIAERESIS
name|encoding
index|[
literal|0x99
index|]
operator|=
literal|'\u017D'
expr_stmt|;
comment|//LATIN CAPITAL LETTER Z WITH CARON
name|encoding
index|[
literal|0x9A
index|]
operator|=
literal|'\u0131'
expr_stmt|;
comment|//LATIN SMALL LETTER DOTLESS I
name|encoding
index|[
literal|0x9B
index|]
operator|=
literal|'\u0142'
expr_stmt|;
comment|//LATIN SMALL LETTER L WITH STROKE
name|encoding
index|[
literal|0x9C
index|]
operator|=
literal|'\u0153'
expr_stmt|;
comment|//LATIN SMALL LIGATURE OE
name|encoding
index|[
literal|0x9D
index|]
operator|=
literal|'\u0161'
expr_stmt|;
comment|//LATIN SMALL LETTER S WITH CARON
name|encoding
index|[
literal|0x9E
index|]
operator|=
literal|'\u017E'
expr_stmt|;
comment|//LATIN SMALL LETTER Z WITH CARON
name|encoding
index|[
literal|0x9F
index|]
operator|=
name|REPLACEMENT_CHARACTER
expr_stmt|;
comment|//undefined
name|encoding
index|[
literal|0xA0
index|]
operator|=
literal|'\u20AC'
expr_stmt|;
comment|//EURO SIGN
comment|//end of deviations
return|return
name|encoding
return|;
block|}
block|}
end_class

end_unit

