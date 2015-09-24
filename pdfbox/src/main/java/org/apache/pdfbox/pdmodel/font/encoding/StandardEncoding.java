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
name|font
operator|.
name|encoding
package|;
end_package

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
name|COSName
import|;
end_import

begin_comment
comment|/**  * This is an interface to a text encoder.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|StandardEncoding
extends|extends
name|Encoding
block|{
specifier|private
specifier|static
specifier|final
name|int
name|CHAR_CODE
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CHAR_NAME
init|=
literal|1
decl_stmt|;
comment|/**      * Table of octal character codes and their corresponding names.      */
specifier|private
specifier|static
specifier|final
name|Object
index|[]
index|[]
name|STANDARD_ENCODING_TABLE
init|=
block|{
block|{
literal|0101
block|,
literal|"A"
block|}
block|,
block|{
literal|0341
block|,
literal|"AE"
block|}
block|,
block|{
literal|0102
block|,
literal|"B"
block|}
block|,
block|{
literal|0103
block|,
literal|"C"
block|}
block|,
block|{
literal|0104
block|,
literal|"D"
block|}
block|,
block|{
literal|0105
block|,
literal|"E"
block|}
block|,
block|{
literal|0106
block|,
literal|"F"
block|}
block|,
block|{
literal|0107
block|,
literal|"G"
block|}
block|,
block|{
literal|0110
block|,
literal|"H"
block|}
block|,
block|{
literal|0111
block|,
literal|"I"
block|}
block|,
block|{
literal|0112
block|,
literal|"J"
block|}
block|,
block|{
literal|0113
block|,
literal|"K"
block|}
block|,
block|{
literal|0114
block|,
literal|"L"
block|}
block|,
block|{
literal|0350
block|,
literal|"Lslash"
block|}
block|,
block|{
literal|0115
block|,
literal|"M"
block|}
block|,
block|{
literal|0116
block|,
literal|"N"
block|}
block|,
block|{
literal|0117
block|,
literal|"O"
block|}
block|,
block|{
literal|0352
block|,
literal|"OE"
block|}
block|,
block|{
literal|0351
block|,
literal|"Oslash"
block|}
block|,
block|{
literal|0120
block|,
literal|"P"
block|}
block|,
block|{
literal|0121
block|,
literal|"Q"
block|}
block|,
block|{
literal|0122
block|,
literal|"R"
block|}
block|,
block|{
literal|0123
block|,
literal|"S"
block|}
block|,
block|{
literal|0124
block|,
literal|"T"
block|}
block|,
block|{
literal|0125
block|,
literal|"U"
block|}
block|,
block|{
literal|0126
block|,
literal|"V"
block|}
block|,
block|{
literal|0127
block|,
literal|"W"
block|}
block|,
block|{
literal|0130
block|,
literal|"X"
block|}
block|,
block|{
literal|0131
block|,
literal|"Y"
block|}
block|,
block|{
literal|0132
block|,
literal|"Z"
block|}
block|,
block|{
literal|0141
block|,
literal|"a"
block|}
block|,
block|{
literal|0302
block|,
literal|"acute"
block|}
block|,
block|{
literal|0361
block|,
literal|"ae"
block|}
block|,
block|{
literal|0046
block|,
literal|"ampersand"
block|}
block|,
block|{
literal|0136
block|,
literal|"asciicircum"
block|}
block|,
block|{
literal|0176
block|,
literal|"asciitilde"
block|}
block|,
block|{
literal|0052
block|,
literal|"asterisk"
block|}
block|,
block|{
literal|0100
block|,
literal|"at"
block|}
block|,
block|{
literal|0142
block|,
literal|"b"
block|}
block|,
block|{
literal|0134
block|,
literal|"backslash"
block|}
block|,
block|{
literal|0174
block|,
literal|"bar"
block|}
block|,
block|{
literal|0173
block|,
literal|"braceleft"
block|}
block|,
block|{
literal|0175
block|,
literal|"braceright"
block|}
block|,
block|{
literal|0133
block|,
literal|"bracketleft"
block|}
block|,
block|{
literal|0135
block|,
literal|"bracketright"
block|}
block|,
block|{
literal|0306
block|,
literal|"breve"
block|}
block|,
block|{
literal|0267
block|,
literal|"bullet"
block|}
block|,
block|{
literal|0143
block|,
literal|"c"
block|}
block|,
block|{
literal|0317
block|,
literal|"caron"
block|}
block|,
block|{
literal|0313
block|,
literal|"cedilla"
block|}
block|,
block|{
literal|0242
block|,
literal|"cent"
block|}
block|,
block|{
literal|0303
block|,
literal|"circumflex"
block|}
block|,
block|{
literal|0072
block|,
literal|"colon"
block|}
block|,
block|{
literal|0054
block|,
literal|"comma"
block|}
block|,
block|{
literal|0250
block|,
literal|"currency"
block|}
block|,
block|{
literal|0144
block|,
literal|"d"
block|}
block|,
block|{
literal|0262
block|,
literal|"dagger"
block|}
block|,
block|{
literal|0263
block|,
literal|"daggerdbl"
block|}
block|,
block|{
literal|0310
block|,
literal|"dieresis"
block|}
block|,
block|{
literal|0044
block|,
literal|"dollar"
block|}
block|,
block|{
literal|0307
block|,
literal|"dotaccent"
block|}
block|,
block|{
literal|0365
block|,
literal|"dotlessi"
block|}
block|,
block|{
literal|0145
block|,
literal|"e"
block|}
block|,
block|{
literal|0070
block|,
literal|"eight"
block|}
block|,
block|{
literal|0274
block|,
literal|"ellipsis"
block|}
block|,
block|{
literal|0320
block|,
literal|"emdash"
block|}
block|,
block|{
literal|0261
block|,
literal|"endash"
block|}
block|,
block|{
literal|0075
block|,
literal|"equal"
block|}
block|,
block|{
literal|0041
block|,
literal|"exclam"
block|}
block|,
block|{
literal|0241
block|,
literal|"exclamdown"
block|}
block|,
block|{
literal|0146
block|,
literal|"f"
block|}
block|,
block|{
literal|0256
block|,
literal|"fi"
block|}
block|,
block|{
literal|0065
block|,
literal|"five"
block|}
block|,
block|{
literal|0257
block|,
literal|"fl"
block|}
block|,
block|{
literal|0246
block|,
literal|"florin"
block|}
block|,
block|{
literal|0064
block|,
literal|"four"
block|}
block|,
block|{
literal|0244
block|,
literal|"fraction"
block|}
block|,
block|{
literal|0147
block|,
literal|"g"
block|}
block|,
block|{
literal|0373
block|,
literal|"germandbls"
block|}
block|,
block|{
literal|0301
block|,
literal|"grave"
block|}
block|,
block|{
literal|0076
block|,
literal|"greater"
block|}
block|,
block|{
literal|0253
block|,
literal|"guillemotleft"
block|}
block|,
block|{
literal|0273
block|,
literal|"guillemotright"
block|}
block|,
block|{
literal|0254
block|,
literal|"guilsinglleft"
block|}
block|,
block|{
literal|0255
block|,
literal|"guilsinglright"
block|}
block|,
block|{
literal|0150
block|,
literal|"h"
block|}
block|,
block|{
literal|0315
block|,
literal|"hungarumlaut"
block|}
block|,
block|{
literal|0055
block|,
literal|"hyphen"
block|}
block|,
block|{
literal|0151
block|,
literal|"i"
block|}
block|,
block|{
literal|0152
block|,
literal|"j"
block|}
block|,
block|{
literal|0153
block|,
literal|"k"
block|}
block|,
block|{
literal|0154
block|,
literal|"l"
block|}
block|,
block|{
literal|0074
block|,
literal|"less"
block|}
block|,
block|{
literal|0370
block|,
literal|"lslash"
block|}
block|,
block|{
literal|0155
block|,
literal|"m"
block|}
block|,
block|{
literal|0305
block|,
literal|"macron"
block|}
block|,
block|{
literal|0156
block|,
literal|"n"
block|}
block|,
block|{
literal|0071
block|,
literal|"nine"
block|}
block|,
block|{
literal|0043
block|,
literal|"numbersign"
block|}
block|,
block|{
literal|0157
block|,
literal|"o"
block|}
block|,
block|{
literal|0372
block|,
literal|"oe"
block|}
block|,
block|{
literal|0316
block|,
literal|"ogonek"
block|}
block|,
block|{
literal|0061
block|,
literal|"one"
block|}
block|,
block|{
literal|0343
block|,
literal|"ordfeminine"
block|}
block|,
block|{
literal|0353
block|,
literal|"ordmasculine"
block|}
block|,
block|{
literal|0371
block|,
literal|"oslash"
block|}
block|,
block|{
literal|0160
block|,
literal|"p"
block|}
block|,
block|{
literal|0266
block|,
literal|"paragraph"
block|}
block|,
block|{
literal|0050
block|,
literal|"parenleft"
block|}
block|,
block|{
literal|0051
block|,
literal|"parenright"
block|}
block|,
block|{
literal|0045
block|,
literal|"percent"
block|}
block|,
block|{
literal|0056
block|,
literal|"period"
block|}
block|,
block|{
literal|0264
block|,
literal|"periodcentered"
block|}
block|,
block|{
literal|0275
block|,
literal|"perthousand"
block|}
block|,
block|{
literal|0053
block|,
literal|"plus"
block|}
block|,
block|{
literal|0161
block|,
literal|"q"
block|}
block|,
block|{
literal|0077
block|,
literal|"question"
block|}
block|,
block|{
literal|0277
block|,
literal|"questiondown"
block|}
block|,
block|{
literal|0042
block|,
literal|"quotedbl"
block|}
block|,
block|{
literal|0271
block|,
literal|"quotedblbase"
block|}
block|,
block|{
literal|0252
block|,
literal|"quotedblleft"
block|}
block|,
block|{
literal|0272
block|,
literal|"quotedblright"
block|}
block|,
block|{
literal|0140
block|,
literal|"quoteleft"
block|}
block|,
block|{
literal|0047
block|,
literal|"quoteright"
block|}
block|,
block|{
literal|0270
block|,
literal|"quotesinglbase"
block|}
block|,
block|{
literal|0251
block|,
literal|"quotesingle"
block|}
block|,
block|{
literal|0162
block|,
literal|"r"
block|}
block|,
block|{
literal|0312
block|,
literal|"ring"
block|}
block|,
block|{
literal|0163
block|,
literal|"s"
block|}
block|,
block|{
literal|0247
block|,
literal|"section"
block|}
block|,
block|{
literal|0073
block|,
literal|"semicolon"
block|}
block|,
block|{
literal|0067
block|,
literal|"seven"
block|}
block|,
block|{
literal|0066
block|,
literal|"six"
block|}
block|,
block|{
literal|0057
block|,
literal|"slash"
block|}
block|,
block|{
literal|0040
block|,
literal|"space"
block|}
block|,
block|{
literal|0243
block|,
literal|"sterling"
block|}
block|,
block|{
literal|0164
block|,
literal|"t"
block|}
block|,
block|{
literal|0063
block|,
literal|"three"
block|}
block|,
block|{
literal|0304
block|,
literal|"tilde"
block|}
block|,
block|{
literal|0062
block|,
literal|"two"
block|}
block|,
block|{
literal|0165
block|,
literal|"u"
block|}
block|,
block|{
literal|0137
block|,
literal|"underscore"
block|}
block|,
block|{
literal|0166
block|,
literal|"v"
block|}
block|,
block|{
literal|0167
block|,
literal|"w"
block|}
block|,
block|{
literal|0170
block|,
literal|"x"
block|}
block|,
block|{
literal|0171
block|,
literal|"y"
block|}
block|,
block|{
literal|0245
block|,
literal|"yen"
block|}
block|,
block|{
literal|0172
block|,
literal|"z"
block|}
block|,
block|{
literal|0060
block|,
literal|"zero"
block|}
block|}
decl_stmt|;
comment|/**      * Singleton instance of this class.      *      * @since Apache PDFBox 1.3.0      */
specifier|public
specifier|static
specifier|final
name|StandardEncoding
name|INSTANCE
init|=
operator|new
name|StandardEncoding
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|StandardEncoding
parameter_list|()
block|{
for|for
control|(
name|Object
index|[]
name|encodingEntry
range|:
name|STANDARD_ENCODING_TABLE
control|)
block|{
name|add
argument_list|(
operator|(
name|Integer
operator|)
name|encodingEntry
index|[
name|CHAR_CODE
index|]
argument_list|,
name|encodingEntry
index|[
name|CHAR_NAME
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|STANDARD_ENCODING
return|;
block|}
block|}
end_class

end_unit

