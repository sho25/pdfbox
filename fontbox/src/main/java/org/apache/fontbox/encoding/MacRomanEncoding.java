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
name|fontbox
operator|.
name|encoding
package|;
end_package

begin_comment
comment|/**  * This is the MacRomanEncoding.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|MacRomanEncoding
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
specifier|public
specifier|static
specifier|final
name|Object
index|[]
index|[]
name|MAC_ROMAN_ENCODING_TABLE
init|=
block|{
block|{
literal|0101
block|,
literal|"A"
block|}
block|,
block|{
literal|0256
block|,
literal|"AE"
block|}
block|,
block|{
literal|0347
block|,
literal|"Aacute"
block|}
block|,
block|{
literal|0345
block|,
literal|"Acircumflex"
block|}
block|,
block|{
literal|0200
block|,
literal|"Adieresis"
block|}
block|,
block|{
literal|0313
block|,
literal|"Agrave"
block|}
block|,
block|{
literal|0201
block|,
literal|"Aring"
block|}
block|,
block|{
literal|0314
block|,
literal|"Atilde"
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
literal|0202
block|,
literal|"Ccedilla"
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
literal|0203
block|,
literal|"Eacute"
block|}
block|,
block|{
literal|0346
block|,
literal|"Ecircumflex"
block|}
block|,
block|{
literal|0350
block|,
literal|"Edieresis"
block|}
block|,
block|{
literal|0351
block|,
literal|"Egrave"
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
literal|0352
block|,
literal|"Iacute"
block|}
block|,
block|{
literal|0353
block|,
literal|"Icircumflex"
block|}
block|,
block|{
literal|0354
block|,
literal|"Idieresis"
block|}
block|,
block|{
literal|0355
block|,
literal|"Igrave"
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
literal|0204
block|,
literal|"Ntilde"
block|}
block|,
block|{
literal|0117
block|,
literal|"O"
block|}
block|,
block|{
literal|0316
block|,
literal|"OE"
block|}
block|,
block|{
literal|0356
block|,
literal|"Oacute"
block|}
block|,
block|{
literal|0357
block|,
literal|"Ocircumflex"
block|}
block|,
block|{
literal|0205
block|,
literal|"Odieresis"
block|}
block|,
block|{
literal|0361
block|,
literal|"Ograve"
block|}
block|,
block|{
literal|0257
block|,
literal|"Oslash"
block|}
block|,
block|{
literal|0315
block|,
literal|"Otilde"
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
literal|0362
block|,
literal|"Uacute"
block|}
block|,
block|{
literal|0363
block|,
literal|"Ucircumflex"
block|}
block|,
block|{
literal|0206
block|,
literal|"Udieresis"
block|}
block|,
block|{
literal|0364
block|,
literal|"Ugrave"
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
literal|0331
block|,
literal|"Ydieresis"
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
literal|0207
block|,
literal|"aacute"
block|}
block|,
block|{
literal|0211
block|,
literal|"acircumflex"
block|}
block|,
block|{
literal|0253
block|,
literal|"acute"
block|}
block|,
block|{
literal|0212
block|,
literal|"adieresis"
block|}
block|,
block|{
literal|0276
block|,
literal|"ae"
block|}
block|,
block|{
literal|0210
block|,
literal|"agrave"
block|}
block|,
block|{
literal|046
block|,
literal|"ampersand"
block|}
block|,
block|{
literal|0214
block|,
literal|"aring"
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
literal|052
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
literal|0213
block|,
literal|"atilde"
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
literal|0371
block|,
literal|"breve"
block|}
block|,
block|{
literal|0245
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
literal|0377
block|,
literal|"caron"
block|}
block|,
block|{
literal|0215
block|,
literal|"ccedilla"
block|}
block|,
block|{
literal|0374
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
literal|0366
block|,
literal|"circumflex"
block|}
block|,
block|{
literal|072
block|,
literal|"colon"
block|}
block|,
block|{
literal|054
block|,
literal|"comma"
block|}
block|,
block|{
literal|0251
block|,
literal|"copyright"
block|}
block|,
block|{
literal|0333
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
literal|0240
block|,
literal|"dagger"
block|}
block|,
block|{
literal|0340
block|,
literal|"daggerdbl"
block|}
block|,
block|{
literal|0241
block|,
literal|"degree"
block|}
block|,
block|{
literal|0254
block|,
literal|"dieresis"
block|}
block|,
block|{
literal|0326
block|,
literal|"divide"
block|}
block|,
block|{
literal|044
block|,
literal|"dollar"
block|}
block|,
block|{
literal|0372
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
literal|0216
block|,
literal|"eacute"
block|}
block|,
block|{
literal|0220
block|,
literal|"ecircumflex"
block|}
block|,
block|{
literal|0221
block|,
literal|"edieresis"
block|}
block|,
block|{
literal|0217
block|,
literal|"egrave"
block|}
block|,
block|{
literal|070
block|,
literal|"eight"
block|}
block|,
block|{
literal|0311
block|,
literal|"ellipsis"
block|}
block|,
block|{
literal|0321
block|,
literal|"emdash"
block|}
block|,
block|{
literal|0320
block|,
literal|"endash"
block|}
block|,
block|{
literal|075
block|,
literal|"equal"
block|}
block|,
block|{
literal|041
block|,
literal|"exclam"
block|}
block|,
block|{
literal|0301
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
literal|0336
block|,
literal|"fi"
block|}
block|,
block|{
literal|065
block|,
literal|"five"
block|}
block|,
block|{
literal|0337
block|,
literal|"fl"
block|}
block|,
block|{
literal|0304
block|,
literal|"florin"
block|}
block|,
block|{
literal|064
block|,
literal|"four"
block|}
block|,
block|{
literal|0332
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
literal|0247
block|,
literal|"germandbls"
block|}
block|,
block|{
literal|0140
block|,
literal|"grave"
block|}
block|,
block|{
literal|076
block|,
literal|"greater"
block|}
block|,
block|{
literal|0307
block|,
literal|"guillemotleft"
block|}
block|,
block|{
literal|0310
block|,
literal|"guillemotright"
block|}
block|,
block|{
literal|0334
block|,
literal|"guilsinglleft"
block|}
block|,
block|{
literal|0335
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
literal|0375
block|,
literal|"hungarumlaut"
block|}
block|,
block|{
literal|055
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
literal|0222
block|,
literal|"iacute"
block|}
block|,
block|{
literal|0224
block|,
literal|"icircumflex"
block|}
block|,
block|{
literal|0225
block|,
literal|"idieresis"
block|}
block|,
block|{
literal|0223
block|,
literal|"igrave"
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
literal|074
block|,
literal|"less"
block|}
block|,
block|{
literal|0302
block|,
literal|"logicalnot"
block|}
block|,
block|{
literal|0155
block|,
literal|"m"
block|}
block|,
block|{
literal|0370
block|,
literal|"macron"
block|}
block|,
block|{
literal|0265
block|,
literal|"mu"
block|}
block|,
block|{
literal|0156
block|,
literal|"n"
block|}
block|,
block|{
literal|071
block|,
literal|"nine"
block|}
block|,
block|{
literal|0226
block|,
literal|"ntilde"
block|}
block|,
block|{
literal|043
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
literal|0227
block|,
literal|"oacute"
block|}
block|,
block|{
literal|0231
block|,
literal|"ocircumflex"
block|}
block|,
block|{
literal|0232
block|,
literal|"odieresis"
block|}
block|,
block|{
literal|0317
block|,
literal|"oe"
block|}
block|,
block|{
literal|0376
block|,
literal|"ogonek"
block|}
block|,
block|{
literal|0230
block|,
literal|"ograve"
block|}
block|,
block|{
literal|061
block|,
literal|"one"
block|}
block|,
block|{
literal|0273
block|,
literal|"ordfeminine"
block|}
block|,
block|{
literal|0274
block|,
literal|"ordmasculine"
block|}
block|,
block|{
literal|0277
block|,
literal|"oslash"
block|}
block|,
block|{
literal|0233
block|,
literal|"otilde"
block|}
block|,
block|{
literal|0160
block|,
literal|"p"
block|}
block|,
block|{
literal|0246
block|,
literal|"paragraph"
block|}
block|,
block|{
literal|050
block|,
literal|"parenleft"
block|}
block|,
block|{
literal|051
block|,
literal|"parenright"
block|}
block|,
block|{
literal|045
block|,
literal|"percent"
block|}
block|,
block|{
literal|056
block|,
literal|"period"
block|}
block|,
block|{
literal|0341
block|,
literal|"periodcentered"
block|}
block|,
block|{
literal|0344
block|,
literal|"perthousand"
block|}
block|,
block|{
literal|053
block|,
literal|"plus"
block|}
block|,
block|{
literal|0261
block|,
literal|"plusminus"
block|}
block|,
block|{
literal|0161
block|,
literal|"q"
block|}
block|,
block|{
literal|077
block|,
literal|"question"
block|}
block|,
block|{
literal|0300
block|,
literal|"questiondown"
block|}
block|,
block|{
literal|042
block|,
literal|"quotedbl"
block|}
block|,
block|{
literal|0343
block|,
literal|"quotedblbase"
block|}
block|,
block|{
literal|0322
block|,
literal|"quotedblleft"
block|}
block|,
block|{
literal|0323
block|,
literal|"quotedblright"
block|}
block|,
block|{
literal|0324
block|,
literal|"quoteleft"
block|}
block|,
block|{
literal|0325
block|,
literal|"quoteright"
block|}
block|,
block|{
literal|0342
block|,
literal|"quotesinglbase"
block|}
block|,
block|{
literal|047
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
literal|0250
block|,
literal|"registered"
block|}
block|,
block|{
literal|0373
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
literal|0244
block|,
literal|"section"
block|}
block|,
block|{
literal|073
block|,
literal|"semicolon"
block|}
block|,
block|{
literal|067
block|,
literal|"seven"
block|}
block|,
block|{
literal|066
block|,
literal|"six"
block|}
block|,
block|{
literal|057
block|,
literal|"slash"
block|}
block|,
block|{
literal|040
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
literal|063
block|,
literal|"three"
block|}
block|,
block|{
literal|0367
block|,
literal|"tilde"
block|}
block|,
block|{
literal|0252
block|,
literal|"trademark"
block|}
block|,
block|{
literal|062
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
literal|0234
block|,
literal|"uacute"
block|}
block|,
block|{
literal|0236
block|,
literal|"ucircumflex"
block|}
block|,
block|{
literal|0237
block|,
literal|"udieresis"
block|}
block|,
block|{
literal|0235
block|,
literal|"ugrave"
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
literal|0330
block|,
literal|"ydieresis"
block|}
block|,
block|{
literal|0264
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
literal|060
block|,
literal|"zero"
block|}
block|}
decl_stmt|;
comment|/**      * Singleton instance of this class.      *      */
specifier|public
specifier|static
specifier|final
name|MacRomanEncoding
name|INSTANCE
init|=
operator|new
name|MacRomanEncoding
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|MacRomanEncoding
parameter_list|()
block|{
for|for
control|(
name|Object
index|[]
name|encodingEntry
range|:
name|MAC_ROMAN_ENCODING_TABLE
control|)
block|{
name|addCharacterEncoding
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
block|}
end_class

end_unit

