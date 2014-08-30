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
name|ttf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Windows Glyph List 4 (WGL4) names for Mac glyphs.  */
end_comment

begin_class
specifier|public
class|class
name|WGL4Names
block|{
comment|/**      * The number of standard mac glyph names.      */
specifier|public
specifier|static
specifier|final
name|int
name|NUMBER_OF_MAC_GLYPHS
init|=
literal|258
decl_stmt|;
comment|/**      * The 258 standard mac glyph names a used in 'post' format 1 and 2.      */
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|MAC_GLYPH_NAMES
init|=
operator|new
name|String
index|[]
block|{
literal|".notdef"
block|,
literal|".null"
block|,
literal|"nonmarkingreturn"
block|,
literal|"space"
block|,
literal|"exclam"
block|,
literal|"quotedbl"
block|,
literal|"numbersign"
block|,
literal|"dollar"
block|,
literal|"percent"
block|,
literal|"ampersand"
block|,
literal|"quotesingle"
block|,
literal|"parenleft"
block|,
literal|"parenright"
block|,
literal|"asterisk"
block|,
literal|"plus"
block|,
literal|"comma"
block|,
literal|"hyphen"
block|,
literal|"period"
block|,
literal|"slash"
block|,
literal|"zero"
block|,
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|,
literal|"four"
block|,
literal|"five"
block|,
literal|"six"
block|,
literal|"seven"
block|,
literal|"eight"
block|,
literal|"nine"
block|,
literal|"colon"
block|,
literal|"semicolon"
block|,
literal|"less"
block|,
literal|"equal"
block|,
literal|"greater"
block|,
literal|"question"
block|,
literal|"at"
block|,
literal|"A"
block|,
literal|"B"
block|,
literal|"C"
block|,
literal|"D"
block|,
literal|"E"
block|,
literal|"F"
block|,
literal|"G"
block|,
literal|"H"
block|,
literal|"I"
block|,
literal|"J"
block|,
literal|"K"
block|,
literal|"L"
block|,
literal|"M"
block|,
literal|"N"
block|,
literal|"O"
block|,
literal|"P"
block|,
literal|"Q"
block|,
literal|"R"
block|,
literal|"S"
block|,
literal|"T"
block|,
literal|"U"
block|,
literal|"V"
block|,
literal|"W"
block|,
literal|"X"
block|,
literal|"Y"
block|,
literal|"Z"
block|,
literal|"bracketleft"
block|,
literal|"backslash"
block|,
literal|"bracketright"
block|,
literal|"asciicircum"
block|,
literal|"underscore"
block|,
literal|"grave"
block|,
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|,
literal|"d"
block|,
literal|"e"
block|,
literal|"f"
block|,
literal|"g"
block|,
literal|"h"
block|,
literal|"i"
block|,
literal|"j"
block|,
literal|"k"
block|,
literal|"l"
block|,
literal|"m"
block|,
literal|"n"
block|,
literal|"o"
block|,
literal|"p"
block|,
literal|"q"
block|,
literal|"r"
block|,
literal|"s"
block|,
literal|"t"
block|,
literal|"u"
block|,
literal|"v"
block|,
literal|"w"
block|,
literal|"x"
block|,
literal|"y"
block|,
literal|"z"
block|,
literal|"braceleft"
block|,
literal|"bar"
block|,
literal|"braceright"
block|,
literal|"asciitilde"
block|,
literal|"Adieresis"
block|,
literal|"Aring"
block|,
literal|"Ccedilla"
block|,
literal|"Eacute"
block|,
literal|"Ntilde"
block|,
literal|"Odieresis"
block|,
literal|"Udieresis"
block|,
literal|"aacute"
block|,
literal|"agrave"
block|,
literal|"acircumflex"
block|,
literal|"adieresis"
block|,
literal|"atilde"
block|,
literal|"aring"
block|,
literal|"ccedilla"
block|,
literal|"eacute"
block|,
literal|"egrave"
block|,
literal|"ecircumflex"
block|,
literal|"edieresis"
block|,
literal|"iacute"
block|,
literal|"igrave"
block|,
literal|"icircumflex"
block|,
literal|"idieresis"
block|,
literal|"ntilde"
block|,
literal|"oacute"
block|,
literal|"ograve"
block|,
literal|"ocircumflex"
block|,
literal|"odieresis"
block|,
literal|"otilde"
block|,
literal|"uacute"
block|,
literal|"ugrave"
block|,
literal|"ucircumflex"
block|,
literal|"udieresis"
block|,
literal|"dagger"
block|,
literal|"degree"
block|,
literal|"cent"
block|,
literal|"sterling"
block|,
literal|"section"
block|,
literal|"bullet"
block|,
literal|"paragraph"
block|,
literal|"germandbls"
block|,
literal|"registered"
block|,
literal|"copyright"
block|,
literal|"trademark"
block|,
literal|"acute"
block|,
literal|"dieresis"
block|,
literal|"notequal"
block|,
literal|"AE"
block|,
literal|"Oslash"
block|,
literal|"infinity"
block|,
literal|"plusminus"
block|,
literal|"lessequal"
block|,
literal|"greaterequal"
block|,
literal|"yen"
block|,
literal|"mu"
block|,
literal|"partialdiff"
block|,
literal|"summation"
block|,
literal|"product"
block|,
literal|"pi"
block|,
literal|"integral"
block|,
literal|"ordfeminine"
block|,
literal|"ordmasculine"
block|,
literal|"Omega"
block|,
literal|"ae"
block|,
literal|"oslash"
block|,
literal|"questiondown"
block|,
literal|"exclamdown"
block|,
literal|"logicalnot"
block|,
literal|"radical"
block|,
literal|"florin"
block|,
literal|"approxequal"
block|,
literal|"Delta"
block|,
literal|"guillemotleft"
block|,
literal|"guillemotright"
block|,
literal|"ellipsis"
block|,
literal|"nonbreakingspace"
block|,
literal|"Agrave"
block|,
literal|"Atilde"
block|,
literal|"Otilde"
block|,
literal|"OE"
block|,
literal|"oe"
block|,
literal|"endash"
block|,
literal|"emdash"
block|,
literal|"quotedblleft"
block|,
literal|"quotedblright"
block|,
literal|"quoteleft"
block|,
literal|"quoteright"
block|,
literal|"divide"
block|,
literal|"lozenge"
block|,
literal|"ydieresis"
block|,
literal|"Ydieresis"
block|,
literal|"fraction"
block|,
literal|"currency"
block|,
literal|"guilsinglleft"
block|,
literal|"guilsinglright"
block|,
literal|"fi"
block|,
literal|"fl"
block|,
literal|"daggerdbl"
block|,
literal|"periodcentered"
block|,
literal|"quotesinglbase"
block|,
literal|"quotedblbase"
block|,
literal|"perthousand"
block|,
literal|"Acircumflex"
block|,
literal|"Ecircumflex"
block|,
literal|"Aacute"
block|,
literal|"Edieresis"
block|,
literal|"Egrave"
block|,
literal|"Iacute"
block|,
literal|"Icircumflex"
block|,
literal|"Idieresis"
block|,
literal|"Igrave"
block|,
literal|"Oacute"
block|,
literal|"Ocircumflex"
block|,
literal|"apple"
block|,
literal|"Ograve"
block|,
literal|"Uacute"
block|,
literal|"Ucircumflex"
block|,
literal|"Ugrave"
block|,
literal|"dotlessi"
block|,
literal|"circumflex"
block|,
literal|"tilde"
block|,
literal|"macron"
block|,
literal|"breve"
block|,
literal|"dotaccent"
block|,
literal|"ring"
block|,
literal|"cedilla"
block|,
literal|"hungarumlaut"
block|,
literal|"ogonek"
block|,
literal|"caron"
block|,
literal|"Lslash"
block|,
literal|"lslash"
block|,
literal|"Scaron"
block|,
literal|"scaron"
block|,
literal|"Zcaron"
block|,
literal|"zcaron"
block|,
literal|"brokenbar"
block|,
literal|"Eth"
block|,
literal|"eth"
block|,
literal|"Yacute"
block|,
literal|"yacute"
block|,
literal|"Thorn"
block|,
literal|"thorn"
block|,
literal|"minus"
block|,
literal|"multiply"
block|,
literal|"onesuperior"
block|,
literal|"twosuperior"
block|,
literal|"threesuperior"
block|,
literal|"onehalf"
block|,
literal|"onequarter"
block|,
literal|"threequarters"
block|,
literal|"franc"
block|,
literal|"Gbreve"
block|,
literal|"gbreve"
block|,
literal|"Idotaccent"
block|,
literal|"Scedilla"
block|,
literal|"scedilla"
block|,
literal|"Cacute"
block|,
literal|"cacute"
block|,
literal|"Ccaron"
block|,
literal|"ccaron"
block|,
literal|"dcroat"
block|}
decl_stmt|;
comment|/**      * The indices of the standard mac glyph names.      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|MAC_GLYPH_NAMES_INDICES
decl_stmt|;
static|static
block|{
name|MAC_GLYPH_NAMES_INDICES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|NUMBER_OF_MAC_GLYPHS
condition|;
operator|++
name|i
control|)
block|{
name|MAC_GLYPH_NAMES_INDICES
operator|.
name|put
argument_list|(
name|MAC_GLYPH_NAMES
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

