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
name|cff
package|;
end_package

begin_comment
comment|/**  * This is specialized CFFCharset. It's used if the CharsetId of a font is set to 0.  *   * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CFFISOAdobeCharset
extends|extends
name|CFFCharset
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
comment|/**      * Table of character codes and their corresponding names.      */
specifier|private
specifier|static
specifier|final
name|Object
index|[]
index|[]
name|CFF_ISO_ADOBE_CHARSET_TABLE
init|=
block|{
block|{
literal|0
block|,
literal|".notdef"
block|}
block|,
block|{
literal|1
block|,
literal|"space"
block|}
block|,
block|{
literal|2
block|,
literal|"exclam"
block|}
block|,
block|{
literal|3
block|,
literal|"quotedbl"
block|}
block|,
block|{
literal|4
block|,
literal|"numbersign"
block|}
block|,
block|{
literal|5
block|,
literal|"dollar"
block|}
block|,
block|{
literal|6
block|,
literal|"percent"
block|}
block|,
block|{
literal|7
block|,
literal|"ampersand"
block|}
block|,
block|{
literal|8
block|,
literal|"quoteright"
block|}
block|,
block|{
literal|9
block|,
literal|"parenleft"
block|}
block|,
block|{
literal|10
block|,
literal|"parenright"
block|}
block|,
block|{
literal|11
block|,
literal|"asterisk"
block|}
block|,
block|{
literal|12
block|,
literal|"plus"
block|}
block|,
block|{
literal|13
block|,
literal|"comma"
block|}
block|,
block|{
literal|14
block|,
literal|"hyphen"
block|}
block|,
block|{
literal|15
block|,
literal|"period"
block|}
block|,
block|{
literal|16
block|,
literal|"slash"
block|}
block|,
block|{
literal|17
block|,
literal|"zero"
block|}
block|,
block|{
literal|18
block|,
literal|"one"
block|}
block|,
block|{
literal|19
block|,
literal|"two"
block|}
block|,
block|{
literal|20
block|,
literal|"three"
block|}
block|,
block|{
literal|21
block|,
literal|"four"
block|}
block|,
block|{
literal|22
block|,
literal|"five"
block|}
block|,
block|{
literal|23
block|,
literal|"six"
block|}
block|,
block|{
literal|24
block|,
literal|"seven"
block|}
block|,
block|{
literal|25
block|,
literal|"eight"
block|}
block|,
block|{
literal|26
block|,
literal|"nine"
block|}
block|,
block|{
literal|27
block|,
literal|"colon"
block|}
block|,
block|{
literal|28
block|,
literal|"semicolon"
block|}
block|,
block|{
literal|29
block|,
literal|"less"
block|}
block|,
block|{
literal|30
block|,
literal|"equal"
block|}
block|,
block|{
literal|31
block|,
literal|"greater"
block|}
block|,
block|{
literal|32
block|,
literal|"question"
block|}
block|,
block|{
literal|33
block|,
literal|"at"
block|}
block|,
block|{
literal|34
block|,
literal|"A"
block|}
block|,
block|{
literal|35
block|,
literal|"B"
block|}
block|,
block|{
literal|36
block|,
literal|"C"
block|}
block|,
block|{
literal|37
block|,
literal|"D"
block|}
block|,
block|{
literal|38
block|,
literal|"E"
block|}
block|,
block|{
literal|39
block|,
literal|"F"
block|}
block|,
block|{
literal|40
block|,
literal|"G"
block|}
block|,
block|{
literal|41
block|,
literal|"H"
block|}
block|,
block|{
literal|42
block|,
literal|"I"
block|}
block|,
block|{
literal|43
block|,
literal|"J"
block|}
block|,
block|{
literal|44
block|,
literal|"K"
block|}
block|,
block|{
literal|45
block|,
literal|"L"
block|}
block|,
block|{
literal|46
block|,
literal|"M"
block|}
block|,
block|{
literal|47
block|,
literal|"N"
block|}
block|,
block|{
literal|48
block|,
literal|"O"
block|}
block|,
block|{
literal|49
block|,
literal|"P"
block|}
block|,
block|{
literal|50
block|,
literal|"Q"
block|}
block|,
block|{
literal|51
block|,
literal|"R"
block|}
block|,
block|{
literal|52
block|,
literal|"S"
block|}
block|,
block|{
literal|53
block|,
literal|"T"
block|}
block|,
block|{
literal|54
block|,
literal|"U"
block|}
block|,
block|{
literal|55
block|,
literal|"V"
block|}
block|,
block|{
literal|56
block|,
literal|"W"
block|}
block|,
block|{
literal|57
block|,
literal|"X"
block|}
block|,
block|{
literal|58
block|,
literal|"Y"
block|}
block|,
block|{
literal|59
block|,
literal|"Z"
block|}
block|,
block|{
literal|60
block|,
literal|"bracketleft"
block|}
block|,
block|{
literal|61
block|,
literal|"backslash"
block|}
block|,
block|{
literal|62
block|,
literal|"bracketright"
block|}
block|,
block|{
literal|63
block|,
literal|"asciicircum"
block|}
block|,
block|{
literal|64
block|,
literal|"underscore"
block|}
block|,
block|{
literal|65
block|,
literal|"quoteleft"
block|}
block|,
block|{
literal|66
block|,
literal|"a"
block|}
block|,
block|{
literal|67
block|,
literal|"b"
block|}
block|,
block|{
literal|68
block|,
literal|"c"
block|}
block|,
block|{
literal|69
block|,
literal|"d"
block|}
block|,
block|{
literal|70
block|,
literal|"e"
block|}
block|,
block|{
literal|71
block|,
literal|"f"
block|}
block|,
block|{
literal|72
block|,
literal|"g"
block|}
block|,
block|{
literal|73
block|,
literal|"h"
block|}
block|,
block|{
literal|74
block|,
literal|"i"
block|}
block|,
block|{
literal|75
block|,
literal|"j"
block|}
block|,
block|{
literal|76
block|,
literal|"k"
block|}
block|,
block|{
literal|77
block|,
literal|"l"
block|}
block|,
block|{
literal|78
block|,
literal|"m"
block|}
block|,
block|{
literal|79
block|,
literal|"n"
block|}
block|,
block|{
literal|80
block|,
literal|"o"
block|}
block|,
block|{
literal|81
block|,
literal|"p"
block|}
block|,
block|{
literal|82
block|,
literal|"q"
block|}
block|,
block|{
literal|83
block|,
literal|"r"
block|}
block|,
block|{
literal|84
block|,
literal|"s"
block|}
block|,
block|{
literal|85
block|,
literal|"t"
block|}
block|,
block|{
literal|86
block|,
literal|"u"
block|}
block|,
block|{
literal|87
block|,
literal|"v"
block|}
block|,
block|{
literal|88
block|,
literal|"w"
block|}
block|,
block|{
literal|89
block|,
literal|"x"
block|}
block|,
block|{
literal|90
block|,
literal|"y"
block|}
block|,
block|{
literal|91
block|,
literal|"z"
block|}
block|,
block|{
literal|92
block|,
literal|"braceleft"
block|}
block|,
block|{
literal|93
block|,
literal|"bar"
block|}
block|,
block|{
literal|94
block|,
literal|"braceright"
block|}
block|,
block|{
literal|95
block|,
literal|"asciitilde"
block|}
block|,
block|{
literal|96
block|,
literal|"exclamdown"
block|}
block|,
block|{
literal|97
block|,
literal|"cent"
block|}
block|,
block|{
literal|98
block|,
literal|"sterling"
block|}
block|,
block|{
literal|99
block|,
literal|"fraction"
block|}
block|,
block|{
literal|100
block|,
literal|"yen"
block|}
block|,
block|{
literal|101
block|,
literal|"florin"
block|}
block|,
block|{
literal|102
block|,
literal|"section"
block|}
block|,
block|{
literal|103
block|,
literal|"currency"
block|}
block|,
block|{
literal|104
block|,
literal|"quotesingle"
block|}
block|,
block|{
literal|105
block|,
literal|"quotedblleft"
block|}
block|,
block|{
literal|106
block|,
literal|"guillemotleft"
block|}
block|,
block|{
literal|107
block|,
literal|"guilsinglleft"
block|}
block|,
block|{
literal|108
block|,
literal|"guilsinglright"
block|}
block|,
block|{
literal|109
block|,
literal|"fi"
block|}
block|,
block|{
literal|110
block|,
literal|"fl"
block|}
block|,
block|{
literal|111
block|,
literal|"endash"
block|}
block|,
block|{
literal|112
block|,
literal|"dagger"
block|}
block|,
block|{
literal|113
block|,
literal|"daggerdbl"
block|}
block|,
block|{
literal|114
block|,
literal|"periodcentered"
block|}
block|,
block|{
literal|115
block|,
literal|"paragraph"
block|}
block|,
block|{
literal|116
block|,
literal|"bullet"
block|}
block|,
block|{
literal|117
block|,
literal|"quotesinglbase"
block|}
block|,
block|{
literal|118
block|,
literal|"quotedblbase"
block|}
block|,
block|{
literal|119
block|,
literal|"quotedblright"
block|}
block|,
block|{
literal|120
block|,
literal|"guillemotright"
block|}
block|,
block|{
literal|121
block|,
literal|"ellipsis"
block|}
block|,
block|{
literal|122
block|,
literal|"perthousand"
block|}
block|,
block|{
literal|123
block|,
literal|"questiondown"
block|}
block|,
block|{
literal|124
block|,
literal|"grave"
block|}
block|,
block|{
literal|125
block|,
literal|"acute"
block|}
block|,
block|{
literal|126
block|,
literal|"circumflex"
block|}
block|,
block|{
literal|127
block|,
literal|"tilde"
block|}
block|,
block|{
literal|128
block|,
literal|"macron"
block|}
block|,
block|{
literal|129
block|,
literal|"breve"
block|}
block|,
block|{
literal|130
block|,
literal|"dotaccent"
block|}
block|,
block|{
literal|131
block|,
literal|"dieresis"
block|}
block|,
block|{
literal|132
block|,
literal|"ring"
block|}
block|,
block|{
literal|133
block|,
literal|"cedilla"
block|}
block|,
block|{
literal|134
block|,
literal|"hungarumlaut"
block|}
block|,
block|{
literal|135
block|,
literal|"ogonek"
block|}
block|,
block|{
literal|136
block|,
literal|"caron"
block|}
block|,
block|{
literal|137
block|,
literal|"emdash"
block|}
block|,
block|{
literal|138
block|,
literal|"AE"
block|}
block|,
block|{
literal|139
block|,
literal|"ordfeminine"
block|}
block|,
block|{
literal|140
block|,
literal|"Lslash"
block|}
block|,
block|{
literal|141
block|,
literal|"Oslash"
block|}
block|,
block|{
literal|142
block|,
literal|"OE"
block|}
block|,
block|{
literal|143
block|,
literal|"ordmasculine"
block|}
block|,
block|{
literal|144
block|,
literal|"ae"
block|}
block|,
block|{
literal|145
block|,
literal|"dotlessi"
block|}
block|,
block|{
literal|146
block|,
literal|"lslash"
block|}
block|,
block|{
literal|147
block|,
literal|"oslash"
block|}
block|,
block|{
literal|148
block|,
literal|"oe"
block|}
block|,
block|{
literal|149
block|,
literal|"germandbls"
block|}
block|,
block|{
literal|150
block|,
literal|"onesuperior"
block|}
block|,
block|{
literal|151
block|,
literal|"logicalnot"
block|}
block|,
block|{
literal|152
block|,
literal|"mu"
block|}
block|,
block|{
literal|153
block|,
literal|"trademark"
block|}
block|,
block|{
literal|154
block|,
literal|"Eth"
block|}
block|,
block|{
literal|155
block|,
literal|"onehalf"
block|}
block|,
block|{
literal|156
block|,
literal|"plusminus"
block|}
block|,
block|{
literal|157
block|,
literal|"Thorn"
block|}
block|,
block|{
literal|158
block|,
literal|"onequarter"
block|}
block|,
block|{
literal|159
block|,
literal|"divide"
block|}
block|,
block|{
literal|160
block|,
literal|"brokenbar"
block|}
block|,
block|{
literal|161
block|,
literal|"degree"
block|}
block|,
block|{
literal|162
block|,
literal|"thorn"
block|}
block|,
block|{
literal|163
block|,
literal|"threequarters"
block|}
block|,
block|{
literal|164
block|,
literal|"twosuperior"
block|}
block|,
block|{
literal|165
block|,
literal|"registered"
block|}
block|,
block|{
literal|166
block|,
literal|"minus"
block|}
block|,
block|{
literal|167
block|,
literal|"eth"
block|}
block|,
block|{
literal|168
block|,
literal|"multiply"
block|}
block|,
block|{
literal|169
block|,
literal|"threesuperior"
block|}
block|,
block|{
literal|170
block|,
literal|"copyright"
block|}
block|,
block|{
literal|171
block|,
literal|"Aacute"
block|}
block|,
block|{
literal|172
block|,
literal|"Acircumflex"
block|}
block|,
block|{
literal|173
block|,
literal|"Adieresis"
block|}
block|,
block|{
literal|174
block|,
literal|"Agrave"
block|}
block|,
block|{
literal|175
block|,
literal|"Aring"
block|}
block|,
block|{
literal|176
block|,
literal|"Atilde"
block|}
block|,
block|{
literal|177
block|,
literal|"Ccedilla"
block|}
block|,
block|{
literal|178
block|,
literal|"Eacute"
block|}
block|,
block|{
literal|179
block|,
literal|"Ecircumflex"
block|}
block|,
block|{
literal|180
block|,
literal|"Edieresis"
block|}
block|,
block|{
literal|181
block|,
literal|"Egrave"
block|}
block|,
block|{
literal|182
block|,
literal|"Iacute"
block|}
block|,
block|{
literal|183
block|,
literal|"Icircumflex"
block|}
block|,
block|{
literal|184
block|,
literal|"Idieresis"
block|}
block|,
block|{
literal|185
block|,
literal|"Igrave"
block|}
block|,
block|{
literal|186
block|,
literal|"Ntilde"
block|}
block|,
block|{
literal|187
block|,
literal|"Oacute"
block|}
block|,
block|{
literal|188
block|,
literal|"Ocircumflex"
block|}
block|,
block|{
literal|189
block|,
literal|"Odieresis"
block|}
block|,
block|{
literal|190
block|,
literal|"Ograve"
block|}
block|,
block|{
literal|191
block|,
literal|"Otilde"
block|}
block|,
block|{
literal|192
block|,
literal|"Scaron"
block|}
block|,
block|{
literal|193
block|,
literal|"Uacute"
block|}
block|,
block|{
literal|194
block|,
literal|"Ucircumflex"
block|}
block|,
block|{
literal|195
block|,
literal|"Udieresis"
block|}
block|,
block|{
literal|196
block|,
literal|"Ugrave"
block|}
block|,
block|{
literal|197
block|,
literal|"Yacute"
block|}
block|,
block|{
literal|198
block|,
literal|"Ydieresis"
block|}
block|,
block|{
literal|199
block|,
literal|"Zcaron"
block|}
block|,
block|{
literal|200
block|,
literal|"aacute"
block|}
block|,
block|{
literal|201
block|,
literal|"acircumflex"
block|}
block|,
block|{
literal|202
block|,
literal|"adieresis"
block|}
block|,
block|{
literal|203
block|,
literal|"agrave"
block|}
block|,
block|{
literal|204
block|,
literal|"aring"
block|}
block|,
block|{
literal|205
block|,
literal|"atilde"
block|}
block|,
block|{
literal|206
block|,
literal|"ccedilla"
block|}
block|,
block|{
literal|207
block|,
literal|"eacute"
block|}
block|,
block|{
literal|208
block|,
literal|"ecircumflex"
block|}
block|,
block|{
literal|209
block|,
literal|"edieresis"
block|}
block|,
block|{
literal|210
block|,
literal|"egrave"
block|}
block|,
block|{
literal|211
block|,
literal|"iacute"
block|}
block|,
block|{
literal|212
block|,
literal|"icircumflex"
block|}
block|,
block|{
literal|213
block|,
literal|"idieresis"
block|}
block|,
block|{
literal|214
block|,
literal|"igrave"
block|}
block|,
block|{
literal|215
block|,
literal|"ntilde"
block|}
block|,
block|{
literal|216
block|,
literal|"oacute"
block|}
block|,
block|{
literal|217
block|,
literal|"ocircumflex"
block|}
block|,
block|{
literal|218
block|,
literal|"odieresis"
block|}
block|,
block|{
literal|219
block|,
literal|"ograve"
block|}
block|,
block|{
literal|220
block|,
literal|"otilde"
block|}
block|,
block|{
literal|221
block|,
literal|"scaron"
block|}
block|,
block|{
literal|222
block|,
literal|"uacute"
block|}
block|,
block|{
literal|223
block|,
literal|"ucircumflex"
block|}
block|,
block|{
literal|224
block|,
literal|"udieresis"
block|}
block|,
block|{
literal|225
block|,
literal|"ugrave"
block|}
block|,
block|{
literal|226
block|,
literal|"yacute"
block|}
block|,
block|{
literal|227
block|,
literal|"ydieresis"
block|}
block|,
block|{
literal|228
block|,
literal|"zcaron"
block|}
block|}
decl_stmt|;
specifier|private
name|CFFISOAdobeCharset
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns an instance of the CFFExpertSubsetCharset class.      * @return an instance of CFFExpertSubsetCharset      */
specifier|public
specifier|static
name|CFFISOAdobeCharset
name|getInstance
parameter_list|()
block|{
return|return
name|CFFISOAdobeCharset
operator|.
name|INSTANCE
return|;
block|}
specifier|private
specifier|static
specifier|final
name|CFFISOAdobeCharset
name|INSTANCE
init|=
operator|new
name|CFFISOAdobeCharset
argument_list|()
decl_stmt|;
static|static
block|{
name|int
name|gid
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
index|[]
name|charsetEntry
range|:
name|CFF_ISO_ADOBE_CHARSET_TABLE
control|)
block|{
name|INSTANCE
operator|.
name|addSID
argument_list|(
name|gid
operator|++
argument_list|,
operator|(
name|Integer
operator|)
name|charsetEntry
index|[
name|CHAR_CODE
index|]
argument_list|,
name|charsetEntry
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

