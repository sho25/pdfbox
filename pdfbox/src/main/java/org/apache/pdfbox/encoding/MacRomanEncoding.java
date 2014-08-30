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
comment|/**  * This is an interface to a text encoder.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  */
end_comment

begin_class
specifier|public
class|class
name|MacRomanEncoding
extends|extends
name|Encoding
block|{
comment|/**      * Singleton instance of this class.      *      * @since Apache PDFBox 1.3.0      */
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
name|add
argument_list|(
literal|0101
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0256
argument_list|,
literal|"AE"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0347
argument_list|,
literal|"Aacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0345
argument_list|,
literal|"Acircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0200
argument_list|,
literal|"Adieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0313
argument_list|,
literal|"Agrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0201
argument_list|,
literal|"Aring"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0314
argument_list|,
literal|"Atilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0102
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0103
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0202
argument_list|,
literal|"Ccedilla"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0104
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0105
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0203
argument_list|,
literal|"Eacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0346
argument_list|,
literal|"Ecircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0350
argument_list|,
literal|"Edieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0351
argument_list|,
literal|"Egrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0106
argument_list|,
literal|"F"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0107
argument_list|,
literal|"G"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0110
argument_list|,
literal|"H"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0111
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0352
argument_list|,
literal|"Iacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0353
argument_list|,
literal|"Icircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0354
argument_list|,
literal|"Idieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0355
argument_list|,
literal|"Igrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0112
argument_list|,
literal|"J"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0113
argument_list|,
literal|"K"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0114
argument_list|,
literal|"L"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0115
argument_list|,
literal|"M"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0116
argument_list|,
literal|"N"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0204
argument_list|,
literal|"Ntilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0117
argument_list|,
literal|"O"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0316
argument_list|,
literal|"OE"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0356
argument_list|,
literal|"Oacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0357
argument_list|,
literal|"Ocircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0205
argument_list|,
literal|"Odieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0361
argument_list|,
literal|"Ograve"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0257
argument_list|,
literal|"Oslash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0315
argument_list|,
literal|"Otilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0120
argument_list|,
literal|"P"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0121
argument_list|,
literal|"Q"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0122
argument_list|,
literal|"R"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0123
argument_list|,
literal|"S"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0124
argument_list|,
literal|"T"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0125
argument_list|,
literal|"U"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0362
argument_list|,
literal|"Uacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0363
argument_list|,
literal|"Ucircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0206
argument_list|,
literal|"Udieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0364
argument_list|,
literal|"Ugrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0126
argument_list|,
literal|"V"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0127
argument_list|,
literal|"W"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0130
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0131
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0331
argument_list|,
literal|"Ydieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0132
argument_list|,
literal|"Z"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0141
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0207
argument_list|,
literal|"aacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0211
argument_list|,
literal|"acircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0253
argument_list|,
literal|"acute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0212
argument_list|,
literal|"adieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0276
argument_list|,
literal|"ae"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0210
argument_list|,
literal|"agrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|046
argument_list|,
literal|"ampersand"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0214
argument_list|,
literal|"aring"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0136
argument_list|,
literal|"asciicircum"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0176
argument_list|,
literal|"asciitilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|052
argument_list|,
literal|"asterisk"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0100
argument_list|,
literal|"at"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0213
argument_list|,
literal|"atilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0142
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0134
argument_list|,
literal|"backslash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0174
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0173
argument_list|,
literal|"braceleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0175
argument_list|,
literal|"braceright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0133
argument_list|,
literal|"bracketleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0135
argument_list|,
literal|"bracketright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0371
argument_list|,
literal|"breve"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0245
argument_list|,
literal|"bullet"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0143
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0377
argument_list|,
literal|"caron"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0215
argument_list|,
literal|"ccedilla"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0374
argument_list|,
literal|"cedilla"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0242
argument_list|,
literal|"cent"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0366
argument_list|,
literal|"circumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|072
argument_list|,
literal|"colon"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|054
argument_list|,
literal|"comma"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0251
argument_list|,
literal|"copyright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0333
argument_list|,
literal|"currency"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0144
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0240
argument_list|,
literal|"dagger"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0340
argument_list|,
literal|"daggerdbl"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0241
argument_list|,
literal|"degree"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0254
argument_list|,
literal|"dieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0326
argument_list|,
literal|"divide"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|044
argument_list|,
literal|"dollar"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0372
argument_list|,
literal|"dotaccent"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0365
argument_list|,
literal|"dotlessi"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0145
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0216
argument_list|,
literal|"eacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0220
argument_list|,
literal|"ecircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0221
argument_list|,
literal|"edieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0217
argument_list|,
literal|"egrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|070
argument_list|,
literal|"eight"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0311
argument_list|,
literal|"ellipsis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0321
argument_list|,
literal|"emdash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0320
argument_list|,
literal|"endash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|075
argument_list|,
literal|"equal"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|041
argument_list|,
literal|"exclam"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0301
argument_list|,
literal|"exclamdown"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0146
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0336
argument_list|,
literal|"fi"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|065
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0337
argument_list|,
literal|"fl"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0304
argument_list|,
literal|"florin"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|064
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0332
argument_list|,
literal|"fraction"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0147
argument_list|,
literal|"g"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0247
argument_list|,
literal|"germandbls"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0140
argument_list|,
literal|"grave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|076
argument_list|,
literal|"greater"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0307
argument_list|,
literal|"guillemotleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0310
argument_list|,
literal|"guillemotright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0334
argument_list|,
literal|"guilsinglleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0335
argument_list|,
literal|"guilsinglright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0150
argument_list|,
literal|"h"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0375
argument_list|,
literal|"hungarumlaut"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|055
argument_list|,
literal|"hyphen"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0151
argument_list|,
literal|"i"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0222
argument_list|,
literal|"iacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0224
argument_list|,
literal|"icircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0225
argument_list|,
literal|"idieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0223
argument_list|,
literal|"igrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0152
argument_list|,
literal|"j"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0153
argument_list|,
literal|"k"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0154
argument_list|,
literal|"l"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|074
argument_list|,
literal|"less"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0302
argument_list|,
literal|"logicalnot"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0155
argument_list|,
literal|"m"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0370
argument_list|,
literal|"macron"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0265
argument_list|,
literal|"mu"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0156
argument_list|,
literal|"n"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|071
argument_list|,
literal|"nine"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0226
argument_list|,
literal|"ntilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|043
argument_list|,
literal|"numbersign"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0157
argument_list|,
literal|"o"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0227
argument_list|,
literal|"oacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0231
argument_list|,
literal|"ocircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0232
argument_list|,
literal|"odieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0317
argument_list|,
literal|"oe"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0376
argument_list|,
literal|"ogonek"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0230
argument_list|,
literal|"ograve"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|061
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0273
argument_list|,
literal|"ordfeminine"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0274
argument_list|,
literal|"ordmasculine"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0277
argument_list|,
literal|"oslash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0233
argument_list|,
literal|"otilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0160
argument_list|,
literal|"p"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0246
argument_list|,
literal|"paragraph"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|050
argument_list|,
literal|"parenleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|051
argument_list|,
literal|"parenright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|045
argument_list|,
literal|"percent"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|056
argument_list|,
literal|"period"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0341
argument_list|,
literal|"periodcentered"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0344
argument_list|,
literal|"perthousand"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|053
argument_list|,
literal|"plus"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0261
argument_list|,
literal|"plusminus"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0161
argument_list|,
literal|"q"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|077
argument_list|,
literal|"question"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0300
argument_list|,
literal|"questiondown"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|042
argument_list|,
literal|"quotedbl"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0343
argument_list|,
literal|"quotedblbase"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0322
argument_list|,
literal|"quotedblleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0323
argument_list|,
literal|"quotedblright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0324
argument_list|,
literal|"quoteleft"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0325
argument_list|,
literal|"quoteright"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0342
argument_list|,
literal|"quotesinglbase"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|047
argument_list|,
literal|"quotesingle"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0162
argument_list|,
literal|"r"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0250
argument_list|,
literal|"registered"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0373
argument_list|,
literal|"ring"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0163
argument_list|,
literal|"s"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0244
argument_list|,
literal|"section"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|073
argument_list|,
literal|"semicolon"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|067
argument_list|,
literal|"seven"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|066
argument_list|,
literal|"six"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|057
argument_list|,
literal|"slash"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|040
argument_list|,
literal|"space"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0243
argument_list|,
literal|"sterling"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0164
argument_list|,
literal|"t"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|063
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0367
argument_list|,
literal|"tilde"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0252
argument_list|,
literal|"trademark"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|062
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0165
argument_list|,
literal|"u"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0234
argument_list|,
literal|"uacute"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0236
argument_list|,
literal|"ucircumflex"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0237
argument_list|,
literal|"udieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0235
argument_list|,
literal|"ugrave"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0137
argument_list|,
literal|"underscore"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0166
argument_list|,
literal|"v"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0167
argument_list|,
literal|"w"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0170
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0171
argument_list|,
literal|"y"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0330
argument_list|,
literal|"ydieresis"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0264
argument_list|,
literal|"yen"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|0172
argument_list|,
literal|"z"
argument_list|)
expr_stmt|;
name|add
argument_list|(
literal|060
argument_list|,
literal|"zero"
argument_list|)
expr_stmt|;
comment|// adding an additional mapping as defined in Appendix D of the pdf spec
name|add
argument_list|(
literal|0312
argument_list|,
literal|"space"
argument_list|)
expr_stmt|;
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
name|MAC_ROMAN_ENCODING
return|;
block|}
block|}
end_class

end_unit

