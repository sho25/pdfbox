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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A class for mapping Unicode codepoints to OpenType script tags  *  * @author Aaron Madlon-Kay  *  * @see<a href="https://www.microsoft.com/typography/otspec/scripttags.htm">Microsoft Typography:  * Script Tags</a>  * @see<a href="https://www.unicode.org/reports/tr24/">Unicode Script Property</a>  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OpenTypeScript
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|OpenTypeScript
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERITED
init|=
literal|"Inherited"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN
init|=
literal|"Unknown"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TAG_DEFAULT
init|=
literal|"DFLT"
decl_stmt|;
comment|/**      * A map associating Unicode scripts with one or more OpenType script tags. Script tags are not      * necessarily the same as Unicode scripts. A single Unicode script may correspond to multiple      * tags, especially when there has been a revision to the latter (e.g. Bengali -> [bng2, beng]).      * When there are multiple tags, they are ordered from newest to oldest.      *      * @see<a href="https://www.microsoft.com/typography/otspec/scripttags.htm">Microsoft      * Typography: Script Tags</a>      */
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|UNICODE_SCRIPT_TO_OPENTYPE_TAG_MAP
decl_stmt|;
static|static
block|{
name|Object
index|[]
index|[]
name|table
init|=
block|{
block|{
literal|"Adlam"
block|,
operator|new
name|String
index|[]
block|{
literal|"adlm"
block|}
block|}
block|,
block|{
literal|"Ahom"
block|,
operator|new
name|String
index|[]
block|{
literal|"ahom"
block|}
block|}
block|,
block|{
literal|"Anatolian_Hieroglyphs"
block|,
operator|new
name|String
index|[]
block|{
literal|"hluw"
block|}
block|}
block|,
block|{
literal|"Arabic"
block|,
operator|new
name|String
index|[]
block|{
literal|"arab"
block|}
block|}
block|,
block|{
literal|"Armenian"
block|,
operator|new
name|String
index|[]
block|{
literal|"armn"
block|}
block|}
block|,
block|{
literal|"Avestan"
block|,
operator|new
name|String
index|[]
block|{
literal|"avst"
block|}
block|}
block|,
block|{
literal|"Balinese"
block|,
operator|new
name|String
index|[]
block|{
literal|"bali"
block|}
block|}
block|,
block|{
literal|"Bamum"
block|,
operator|new
name|String
index|[]
block|{
literal|"bamu"
block|}
block|}
block|,
block|{
literal|"Bassa_Vah"
block|,
operator|new
name|String
index|[]
block|{
literal|"bass"
block|}
block|}
block|,
block|{
literal|"Batak"
block|,
operator|new
name|String
index|[]
block|{
literal|"batk"
block|}
block|}
block|,
block|{
literal|"Bengali"
block|,
operator|new
name|String
index|[]
block|{
literal|"bng2"
block|,
literal|"beng"
block|}
block|}
block|,
block|{
literal|"Bhaiksuki"
block|,
operator|new
name|String
index|[]
block|{
literal|"bhks"
block|}
block|}
block|,
block|{
literal|"Bopomofo"
block|,
operator|new
name|String
index|[]
block|{
literal|"bopo"
block|}
block|}
block|,
block|{
literal|"Brahmi"
block|,
operator|new
name|String
index|[]
block|{
literal|"brah"
block|}
block|}
block|,
block|{
literal|"Braille"
block|,
operator|new
name|String
index|[]
block|{
literal|"brai"
block|}
block|}
block|,
block|{
literal|"Buginese"
block|,
operator|new
name|String
index|[]
block|{
literal|"bugi"
block|}
block|}
block|,
block|{
literal|"Buhid"
block|,
operator|new
name|String
index|[]
block|{
literal|"buhd"
block|}
block|}
block|,
comment|// Byzantine Music: byzm
block|{
literal|"Canadian_Aboriginal"
block|,
operator|new
name|String
index|[]
block|{
literal|"cans"
block|}
block|}
block|,
block|{
literal|"Carian"
block|,
operator|new
name|String
index|[]
block|{
literal|"cari"
block|}
block|}
block|,
block|{
literal|"Caucasian_Albanian"
block|,
operator|new
name|String
index|[]
block|{
literal|"aghb"
block|}
block|}
block|,
block|{
literal|"Chakma"
block|,
operator|new
name|String
index|[]
block|{
literal|"cakm"
block|}
block|}
block|,
block|{
literal|"Cham"
block|,
operator|new
name|String
index|[]
block|{
literal|"cham"
block|}
block|}
block|,
block|{
literal|"Cherokee"
block|,
operator|new
name|String
index|[]
block|{
literal|"cher"
block|}
block|}
block|,
block|{
literal|"Common"
block|,
operator|new
name|String
index|[]
block|{
name|TAG_DEFAULT
block|}
block|}
block|,
comment|// "Default" in OpenType
block|{
literal|"Coptic"
block|,
operator|new
name|String
index|[]
block|{
literal|"copt"
block|}
block|}
block|,
block|{
literal|"Cuneiform"
block|,
operator|new
name|String
index|[]
block|{
literal|"xsux"
block|}
block|}
block|,
comment|// "Sumero-Akkadian Cuneiform" in OpenType
block|{
literal|"Cypriot"
block|,
operator|new
name|String
index|[]
block|{
literal|"cprt"
block|}
block|}
block|,
block|{
literal|"Cyrillic"
block|,
operator|new
name|String
index|[]
block|{
literal|"cyrl"
block|}
block|}
block|,
block|{
literal|"Deseret"
block|,
operator|new
name|String
index|[]
block|{
literal|"dsrt"
block|}
block|}
block|,
block|{
literal|"Devanagari"
block|,
operator|new
name|String
index|[]
block|{
literal|"dev2"
block|,
literal|"deva"
block|}
block|}
block|,
block|{
literal|"Duployan"
block|,
operator|new
name|String
index|[]
block|{
literal|"dupl"
block|}
block|}
block|,
block|{
literal|"Egyptian_Hieroglyphs"
block|,
operator|new
name|String
index|[]
block|{
literal|"egyp"
block|}
block|}
block|,
block|{
literal|"Elbasan"
block|,
operator|new
name|String
index|[]
block|{
literal|"elba"
block|}
block|}
block|,
block|{
literal|"Ethiopic"
block|,
operator|new
name|String
index|[]
block|{
literal|"ethi"
block|}
block|}
block|,
block|{
literal|"Georgian"
block|,
operator|new
name|String
index|[]
block|{
literal|"geor"
block|}
block|}
block|,
block|{
literal|"Glagolitic"
block|,
operator|new
name|String
index|[]
block|{
literal|"glag"
block|}
block|}
block|,
block|{
literal|"Gothic"
block|,
operator|new
name|String
index|[]
block|{
literal|"goth"
block|}
block|}
block|,
block|{
literal|"Grantha"
block|,
operator|new
name|String
index|[]
block|{
literal|"gran"
block|}
block|}
block|,
block|{
literal|"Greek"
block|,
operator|new
name|String
index|[]
block|{
literal|"grek"
block|}
block|}
block|,
block|{
literal|"Gujarati"
block|,
operator|new
name|String
index|[]
block|{
literal|"gjr2"
block|,
literal|"gujr"
block|}
block|}
block|,
block|{
literal|"Gurmukhi"
block|,
operator|new
name|String
index|[]
block|{
literal|"gur2"
block|,
literal|"guru"
block|}
block|}
block|,
block|{
literal|"Han"
block|,
operator|new
name|String
index|[]
block|{
literal|"hani"
block|}
block|}
block|,
comment|// "CJK Ideographic" in OpenType
block|{
literal|"Hangul"
block|,
operator|new
name|String
index|[]
block|{
literal|"hang"
block|}
block|}
block|,
comment|// Hangul Jamo: jamo
block|{
literal|"Hanunoo"
block|,
operator|new
name|String
index|[]
block|{
literal|"hano"
block|}
block|}
block|,
block|{
literal|"Hatran"
block|,
operator|new
name|String
index|[]
block|{
literal|"hatr"
block|}
block|}
block|,
block|{
literal|"Hebrew"
block|,
operator|new
name|String
index|[]
block|{
literal|"hebr"
block|}
block|}
block|,
block|{
literal|"Hiragana"
block|,
operator|new
name|String
index|[]
block|{
literal|"kana"
block|}
block|}
block|,
block|{
literal|"Imperial_Aramaic"
block|,
operator|new
name|String
index|[]
block|{
literal|"armi"
block|}
block|}
block|,
block|{
name|INHERITED
block|,
operator|new
name|String
index|[]
block|{
name|INHERITED
block|}
block|}
block|,
block|{
literal|"Inscriptional_Pahlavi"
block|,
operator|new
name|String
index|[]
block|{
literal|"phli"
block|}
block|}
block|,
block|{
literal|"Inscriptional_Parthian"
block|,
operator|new
name|String
index|[]
block|{
literal|"prti"
block|}
block|}
block|,
block|{
literal|"Javanese"
block|,
operator|new
name|String
index|[]
block|{
literal|"java"
block|}
block|}
block|,
block|{
literal|"Kaithi"
block|,
operator|new
name|String
index|[]
block|{
literal|"kthi"
block|}
block|}
block|,
block|{
literal|"Kannada"
block|,
operator|new
name|String
index|[]
block|{
literal|"knd2"
block|,
literal|"knda"
block|}
block|}
block|,
block|{
literal|"Katakana"
block|,
operator|new
name|String
index|[]
block|{
literal|"kana"
block|}
block|}
block|,
block|{
literal|"Kayah_Li"
block|,
operator|new
name|String
index|[]
block|{
literal|"kali"
block|}
block|}
block|,
block|{
literal|"Kharoshthi"
block|,
operator|new
name|String
index|[]
block|{
literal|"khar"
block|}
block|}
block|,
block|{
literal|"Khmer"
block|,
operator|new
name|String
index|[]
block|{
literal|"khmr"
block|}
block|}
block|,
block|{
literal|"Khojki"
block|,
operator|new
name|String
index|[]
block|{
literal|"khoj"
block|}
block|}
block|,
block|{
literal|"Khudawadi"
block|,
operator|new
name|String
index|[]
block|{
literal|"sind"
block|}
block|}
block|,
block|{
literal|"Lao"
block|,
operator|new
name|String
index|[]
block|{
literal|"lao "
block|}
block|}
block|,
block|{
literal|"Latin"
block|,
operator|new
name|String
index|[]
block|{
literal|"latn"
block|}
block|}
block|,
block|{
literal|"Lepcha"
block|,
operator|new
name|String
index|[]
block|{
literal|"lepc"
block|}
block|}
block|,
block|{
literal|"Limbu"
block|,
operator|new
name|String
index|[]
block|{
literal|"limb"
block|}
block|}
block|,
block|{
literal|"Linear_A"
block|,
operator|new
name|String
index|[]
block|{
literal|"lina"
block|}
block|}
block|,
block|{
literal|"Linear_B"
block|,
operator|new
name|String
index|[]
block|{
literal|"linb"
block|}
block|}
block|,
block|{
literal|"Lisu"
block|,
operator|new
name|String
index|[]
block|{
literal|"lisu"
block|}
block|}
block|,
block|{
literal|"Lycian"
block|,
operator|new
name|String
index|[]
block|{
literal|"lyci"
block|}
block|}
block|,
block|{
literal|"Lydian"
block|,
operator|new
name|String
index|[]
block|{
literal|"lydi"
block|}
block|}
block|,
block|{
literal|"Mahajani"
block|,
operator|new
name|String
index|[]
block|{
literal|"mahj"
block|}
block|}
block|,
block|{
literal|"Malayalam"
block|,
operator|new
name|String
index|[]
block|{
literal|"mlm2"
block|,
literal|"mlym"
block|}
block|}
block|,
block|{
literal|"Mandaic"
block|,
operator|new
name|String
index|[]
block|{
literal|"mand"
block|}
block|}
block|,
block|{
literal|"Manichaean"
block|,
operator|new
name|String
index|[]
block|{
literal|"mani"
block|}
block|}
block|,
block|{
literal|"Marchen"
block|,
operator|new
name|String
index|[]
block|{
literal|"marc"
block|}
block|}
block|,
comment|// Mathematical Alphanumeric Symbols: math
block|{
literal|"Meetei_Mayek"
block|,
operator|new
name|String
index|[]
block|{
literal|"mtei"
block|}
block|}
block|,
block|{
literal|"Mende_Kikakui"
block|,
operator|new
name|String
index|[]
block|{
literal|"mend"
block|}
block|}
block|,
block|{
literal|"Meroitic_Cursive"
block|,
operator|new
name|String
index|[]
block|{
literal|"merc"
block|}
block|}
block|,
block|{
literal|"Meroitic_Hieroglyphs"
block|,
operator|new
name|String
index|[]
block|{
literal|"mero"
block|}
block|}
block|,
block|{
literal|"Miao"
block|,
operator|new
name|String
index|[]
block|{
literal|"plrd"
block|}
block|}
block|,
block|{
literal|"Modi"
block|,
operator|new
name|String
index|[]
block|{
literal|"modi"
block|}
block|}
block|,
block|{
literal|"Mongolian"
block|,
operator|new
name|String
index|[]
block|{
literal|"mong"
block|}
block|}
block|,
block|{
literal|"Mro"
block|,
operator|new
name|String
index|[]
block|{
literal|"mroo"
block|}
block|}
block|,
block|{
literal|"Multani"
block|,
operator|new
name|String
index|[]
block|{
literal|"mult"
block|}
block|}
block|,
comment|// Musical Symbols: musc
block|{
literal|"Myanmar"
block|,
operator|new
name|String
index|[]
block|{
literal|"mym2"
block|,
literal|"mymr"
block|}
block|}
block|,
block|{
literal|"Nabataean"
block|,
operator|new
name|String
index|[]
block|{
literal|"nbat"
block|}
block|}
block|,
block|{
literal|"Newa"
block|,
operator|new
name|String
index|[]
block|{
literal|"newa"
block|}
block|}
block|,
block|{
literal|"New_Tai_Lue"
block|,
operator|new
name|String
index|[]
block|{
literal|"talu"
block|}
block|}
block|,
block|{
literal|"Nko"
block|,
operator|new
name|String
index|[]
block|{
literal|"nko "
block|}
block|}
block|,
block|{
literal|"Ogham"
block|,
operator|new
name|String
index|[]
block|{
literal|"ogam"
block|}
block|}
block|,
block|{
literal|"Ol_Chiki"
block|,
operator|new
name|String
index|[]
block|{
literal|"olck"
block|}
block|}
block|,
block|{
literal|"Old_Italic"
block|,
operator|new
name|String
index|[]
block|{
literal|"ital"
block|}
block|}
block|,
block|{
literal|"Old_Hungarian"
block|,
operator|new
name|String
index|[]
block|{
literal|"hung"
block|}
block|}
block|,
block|{
literal|"Old_North_Arabian"
block|,
operator|new
name|String
index|[]
block|{
literal|"narb"
block|}
block|}
block|,
block|{
literal|"Old_Permic"
block|,
operator|new
name|String
index|[]
block|{
literal|"perm"
block|}
block|}
block|,
block|{
literal|"Old_Persian"
block|,
operator|new
name|String
index|[]
block|{
literal|"xpeo"
block|}
block|}
block|,
block|{
literal|"Old_South_Arabian"
block|,
operator|new
name|String
index|[]
block|{
literal|"sarb"
block|}
block|}
block|,
block|{
literal|"Old_Turkic"
block|,
operator|new
name|String
index|[]
block|{
literal|"orkh"
block|}
block|}
block|,
block|{
literal|"Oriya"
block|,
operator|new
name|String
index|[]
block|{
literal|"ory2"
block|,
literal|"orya"
block|}
block|}
block|,
comment|// "Odia (formerly Oriya)" in OpenType
block|{
literal|"Osage"
block|,
operator|new
name|String
index|[]
block|{
literal|"osge"
block|}
block|}
block|,
block|{
literal|"Osmanya"
block|,
operator|new
name|String
index|[]
block|{
literal|"osma"
block|}
block|}
block|,
block|{
literal|"Pahawh_Hmong"
block|,
operator|new
name|String
index|[]
block|{
literal|"hmng"
block|}
block|}
block|,
block|{
literal|"Palmyrene"
block|,
operator|new
name|String
index|[]
block|{
literal|"palm"
block|}
block|}
block|,
block|{
literal|"Pau_Cin_Hau"
block|,
operator|new
name|String
index|[]
block|{
literal|"pauc"
block|}
block|}
block|,
block|{
literal|"Phags_Pa"
block|,
operator|new
name|String
index|[]
block|{
literal|"phag"
block|}
block|}
block|,
block|{
literal|"Phoenician"
block|,
operator|new
name|String
index|[]
block|{
literal|"phnx"
block|}
block|}
block|,
block|{
literal|"Psalter_Pahlavi"
block|,
operator|new
name|String
index|[]
block|{
literal|"phlp"
block|}
block|}
block|,
block|{
literal|"Rejang"
block|,
operator|new
name|String
index|[]
block|{
literal|"rjng"
block|}
block|}
block|,
block|{
literal|"Runic"
block|,
operator|new
name|String
index|[]
block|{
literal|"runr"
block|}
block|}
block|,
block|{
literal|"Samaritan"
block|,
operator|new
name|String
index|[]
block|{
literal|"samr"
block|}
block|}
block|,
block|{
literal|"Saurashtra"
block|,
operator|new
name|String
index|[]
block|{
literal|"saur"
block|}
block|}
block|,
block|{
literal|"Sharada"
block|,
operator|new
name|String
index|[]
block|{
literal|"shrd"
block|}
block|}
block|,
block|{
literal|"Shavian"
block|,
operator|new
name|String
index|[]
block|{
literal|"shaw"
block|}
block|}
block|,
block|{
literal|"Siddham"
block|,
operator|new
name|String
index|[]
block|{
literal|"sidd"
block|}
block|}
block|,
block|{
literal|"SignWriting"
block|,
operator|new
name|String
index|[]
block|{
literal|"sgnw"
block|}
block|}
block|,
block|{
literal|"Sinhala"
block|,
operator|new
name|String
index|[]
block|{
literal|"sinh"
block|}
block|}
block|,
block|{
literal|"Sora_Sompeng"
block|,
operator|new
name|String
index|[]
block|{
literal|"sora"
block|}
block|}
block|,
block|{
literal|"Sundanese"
block|,
operator|new
name|String
index|[]
block|{
literal|"sund"
block|}
block|}
block|,
block|{
literal|"Syloti_Nagri"
block|,
operator|new
name|String
index|[]
block|{
literal|"sylo"
block|}
block|}
block|,
block|{
literal|"Syriac"
block|,
operator|new
name|String
index|[]
block|{
literal|"syrc"
block|}
block|}
block|,
block|{
literal|"Tagalog"
block|,
operator|new
name|String
index|[]
block|{
literal|"tglg"
block|}
block|}
block|,
block|{
literal|"Tagbanwa"
block|,
operator|new
name|String
index|[]
block|{
literal|"tagb"
block|}
block|}
block|,
block|{
literal|"Tai_Le"
block|,
operator|new
name|String
index|[]
block|{
literal|"tale"
block|}
block|}
block|,
block|{
literal|"Tai_Tham"
block|,
operator|new
name|String
index|[]
block|{
literal|"lana"
block|}
block|}
block|,
block|{
literal|"Tai_Viet"
block|,
operator|new
name|String
index|[]
block|{
literal|"tavt"
block|}
block|}
block|,
block|{
literal|"Takri"
block|,
operator|new
name|String
index|[]
block|{
literal|"takr"
block|}
block|}
block|,
block|{
literal|"Tamil"
block|,
operator|new
name|String
index|[]
block|{
literal|"tml2"
block|,
literal|"taml"
block|}
block|}
block|,
block|{
literal|"Tangut"
block|,
operator|new
name|String
index|[]
block|{
literal|"tang"
block|}
block|}
block|,
block|{
literal|"Telugu"
block|,
operator|new
name|String
index|[]
block|{
literal|"tel2"
block|,
literal|"telu"
block|}
block|}
block|,
block|{
literal|"Thaana"
block|,
operator|new
name|String
index|[]
block|{
literal|"thaa"
block|}
block|}
block|,
block|{
literal|"Thai"
block|,
operator|new
name|String
index|[]
block|{
literal|"thai"
block|}
block|}
block|,
block|{
literal|"Tibetan"
block|,
operator|new
name|String
index|[]
block|{
literal|"tibt"
block|}
block|}
block|,
block|{
literal|"Tifinagh"
block|,
operator|new
name|String
index|[]
block|{
literal|"tfng"
block|}
block|}
block|,
block|{
literal|"Tirhuta"
block|,
operator|new
name|String
index|[]
block|{
literal|"tirh"
block|}
block|}
block|,
block|{
literal|"Ugaritic"
block|,
operator|new
name|String
index|[]
block|{
literal|"ugar"
block|}
block|}
block|,
block|{
name|UNKNOWN
block|,
operator|new
name|String
index|[]
block|{
name|TAG_DEFAULT
block|}
block|}
block|,
block|{
literal|"Vai"
block|,
operator|new
name|String
index|[]
block|{
literal|"vai "
block|}
block|}
block|,
block|{
literal|"Warang_Citi"
block|,
operator|new
name|String
index|[]
block|{
literal|"wara"
block|}
block|}
block|,
block|{
literal|"Yi"
block|,
operator|new
name|String
index|[]
block|{
literal|"yi  "
block|}
block|}
block|}
decl_stmt|;
name|UNICODE_SCRIPT_TO_OPENTYPE_TAG_MAP
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|table
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|table
control|)
block|{
name|Object
index|[]
name|array
init|=
operator|(
name|Object
index|[]
operator|)
name|obj
decl_stmt|;
name|UNICODE_SCRIPT_TO_OPENTYPE_TAG_MAP
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|array
index|[
literal|0
index|]
argument_list|,
operator|(
name|String
index|[]
operator|)
name|array
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|int
index|[]
name|unicodeRangeStarts
decl_stmt|;
specifier|private
specifier|static
name|String
index|[]
name|unicodeRangeScripts
decl_stmt|;
static|static
block|{
name|String
name|path
init|=
literal|"org/apache/fontbox/unicode/Scripts.txt"
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|OpenTypeScript
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
init|)
block|{
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|parseScriptsFile
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not find '"
operator|+
name|path
operator|+
literal|"', mirroring char map will be empty: "
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not parse Scripts.txt, mirroring char map will be empty: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|OpenTypeScript
parameter_list|()
block|{     }
specifier|private
specifier|static
name|void
name|parseScriptsFile
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|int
index|[]
argument_list|,
name|String
argument_list|>
name|unicodeRanges
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
operator|new
name|Comparator
argument_list|<
name|int
index|[]
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|int
index|[]
name|o1
parameter_list|,
name|int
index|[]
name|o2
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|o1
index|[
literal|0
index|]
argument_list|,
name|o2
index|[
literal|0
index|]
argument_list|)
return|;
block|}
empty_stmt|;
block|}
argument_list|)
decl_stmt|;
try|try
init|(
name|LineNumberReader
name|rd
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
init|)
block|{
name|int
index|[]
name|lastRange
init|=
block|{
name|Integer
operator|.
name|MIN_VALUE
block|,
name|Integer
operator|.
name|MIN_VALUE
block|}
decl_stmt|;
name|String
name|lastScript
init|=
literal|null
decl_stmt|;
do|do
block|{
name|String
name|s
init|=
name|rd
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
break|break;
block|}
comment|// ignore comments
name|int
name|comment
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
if|if
condition|(
name|comment
operator|!=
operator|-
literal|1
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|comment
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|<
literal|2
condition|)
block|{
continue|continue;
block|}
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|s
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
name|int
name|nFields
init|=
name|st
operator|.
name|countTokens
argument_list|()
decl_stmt|;
if|if
condition|(
name|nFields
operator|<
literal|2
condition|)
block|{
continue|continue;
block|}
name|String
name|characters
init|=
name|st
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|script
init|=
name|st
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
index|[]
name|range
init|=
operator|new
name|int
index|[
literal|2
index|]
decl_stmt|;
name|int
name|rangeDelim
init|=
name|characters
operator|.
name|indexOf
argument_list|(
literal|".."
argument_list|)
decl_stmt|;
if|if
condition|(
name|rangeDelim
operator|==
operator|-
literal|1
condition|)
block|{
name|range
index|[
literal|0
index|]
operator|=
name|range
index|[
literal|1
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|characters
argument_list|,
literal|16
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|range
index|[
literal|0
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|characters
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|rangeDelim
argument_list|)
argument_list|,
literal|16
argument_list|)
expr_stmt|;
name|range
index|[
literal|1
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|characters
operator|.
name|substring
argument_list|(
name|rangeDelim
operator|+
literal|2
argument_list|)
argument_list|,
literal|16
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|range
index|[
literal|0
index|]
operator|==
name|lastRange
index|[
literal|1
index|]
operator|+
literal|1
operator|&&
name|script
operator|.
name|equals
argument_list|(
name|lastScript
argument_list|)
condition|)
block|{
comment|// Combine with previous range
name|lastRange
index|[
literal|1
index|]
operator|=
name|range
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|unicodeRanges
operator|.
name|put
argument_list|(
name|range
argument_list|,
name|script
argument_list|)
expr_stmt|;
name|lastRange
operator|=
name|range
expr_stmt|;
name|lastScript
operator|=
name|script
expr_stmt|;
block|}
block|}
do|while
condition|(
literal|true
condition|)
do|;
block|}
name|unicodeRangeStarts
operator|=
operator|new
name|int
index|[
name|unicodeRanges
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|unicodeRangeScripts
operator|=
operator|new
name|String
index|[
name|unicodeRanges
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|int
index|[]
argument_list|,
name|String
argument_list|>
name|e
range|:
name|unicodeRanges
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|unicodeRangeStarts
index|[
name|i
index|]
operator|=
name|e
operator|.
name|getKey
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
name|unicodeRangeScripts
index|[
name|i
index|]
operator|=
name|e
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
comment|/**      * Obtain the Unicode script associated with the given Unicode codepoint.      *      * @param codePoint      * @return A Unicode script string, or {@code #UNKNOWN} if unknown      */
specifier|private
specifier|static
name|String
name|getUnicodeScript
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
name|ensureValidCodePoint
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
name|int
name|type
init|=
name|Character
operator|.
name|getType
argument_list|(
name|codePoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Character
operator|.
name|UNASSIGNED
condition|)
block|{
return|return
name|UNKNOWN
return|;
block|}
name|int
name|scriptIndex
init|=
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|unicodeRangeStarts
argument_list|,
name|codePoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptIndex
operator|<
literal|0
condition|)
block|{
name|scriptIndex
operator|=
operator|-
name|scriptIndex
operator|-
literal|2
expr_stmt|;
block|}
return|return
name|unicodeRangeScripts
index|[
name|scriptIndex
index|]
return|;
block|}
comment|/**      * Obtain the OpenType script tags associated with the given Unicode codepoint.      *      * The result may contain the special value {@code #INHERITED}, which indicates that the      * codepoint's script can only be determined by its context.      *      * Unknown codepoints are mapped to {@code #TAG_DEFAULT}.      *      * @param codePoint      * @return An array of four-char script tags      */
specifier|public
specifier|static
name|String
index|[]
name|getScriptTags
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
name|ensureValidCodePoint
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
name|String
name|unicode
init|=
name|getUnicodeScript
argument_list|(
name|codePoint
argument_list|)
decl_stmt|;
return|return
name|UNICODE_SCRIPT_TO_OPENTYPE_TAG_MAP
operator|.
name|get
argument_list|(
name|unicode
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|ensureValidCodePoint
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
if|if
condition|(
name|codePoint
argument_list|<
name|Character
operator|.
name|MIN_CODE_POINT
operator|||
name|codePoint
argument_list|>
name|Character
operator|.
name|MAX_CODE_POINT
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid codepoint: "
operator|+
name|codePoint
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

