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
comment|/**  * This is an interface to a text encoder.  */
end_comment

begin_class
specifier|public
class|class
name|MacExpertEncoding
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
name|MAC_EXPERT_ENCODING_TABLE
init|=
block|{
block|{
literal|0276
block|,
literal|"AEsmall"
block|}
block|,
block|{
literal|0207
block|,
literal|"Aacutesmall"
block|}
block|,
block|{
literal|0211
block|,
literal|"Acircumflexsmall"
block|}
block|,
block|{
literal|047
block|,
literal|"Acutesmall"
block|}
block|,
block|{
literal|0212
block|,
literal|"Adieresissmall"
block|}
block|,
block|{
literal|0210
block|,
literal|"Agravesmall"
block|}
block|,
block|{
literal|0214
block|,
literal|"Aringsmall"
block|}
block|,
block|{
literal|0141
block|,
literal|"Asmall"
block|}
block|,
block|{
literal|0213
block|,
literal|"Atildesmall"
block|}
block|,
block|{
literal|0363
block|,
literal|"Brevesmall"
block|}
block|,
block|{
literal|0142
block|,
literal|"Bsmall"
block|}
block|,
block|{
literal|0256
block|,
literal|"Caronsmall"
block|}
block|,
block|{
literal|0215
block|,
literal|"Ccedillasmall"
block|}
block|,
block|{
literal|0311
block|,
literal|"Cedillasmall"
block|}
block|,
block|{
literal|0136
block|,
literal|"Circumflexsmall"
block|}
block|,
block|{
literal|0143
block|,
literal|"Csmall"
block|}
block|,
block|{
literal|0254
block|,
literal|"Dieresissmall"
block|}
block|,
block|{
literal|0372
block|,
literal|"Dotaccentsmall"
block|}
block|,
block|{
literal|0144
block|,
literal|"Dsmall"
block|}
block|,
block|{
literal|0216
block|,
literal|"Eacutesmall"
block|}
block|,
block|{
literal|0220
block|,
literal|"Ecircumflexsmall"
block|}
block|,
block|{
literal|0221
block|,
literal|"Edieresissmall"
block|}
block|,
block|{
literal|0217
block|,
literal|"Egravesmall"
block|}
block|,
block|{
literal|0145
block|,
literal|"Esmall"
block|}
block|,
block|{
literal|0104
block|,
literal|"Ethsmall"
block|}
block|,
block|{
literal|0146
block|,
literal|"Fsmall"
block|}
block|,
block|{
literal|0140
block|,
literal|"Gravesmall"
block|}
block|,
block|{
literal|0147
block|,
literal|"Gsmall"
block|}
block|,
block|{
literal|0150
block|,
literal|"Hsmall"
block|}
block|,
block|{
literal|042
block|,
literal|"Hungarumlautsmall"
block|}
block|,
block|{
literal|0222
block|,
literal|"Iacutesmall"
block|}
block|,
block|{
literal|0224
block|,
literal|"Icircumflexsmall"
block|}
block|,
block|{
literal|0225
block|,
literal|"Idieresissmall"
block|}
block|,
block|{
literal|0223
block|,
literal|"Igravesmall"
block|}
block|,
block|{
literal|0151
block|,
literal|"Ismall"
block|}
block|,
block|{
literal|0152
block|,
literal|"Jsmall"
block|}
block|,
block|{
literal|0153
block|,
literal|"Ksmall"
block|}
block|,
block|{
literal|0302
block|,
literal|"Lslashsmall"
block|}
block|,
block|{
literal|0154
block|,
literal|"Lsmall"
block|}
block|,
block|{
literal|0364
block|,
literal|"Macronsmall"
block|}
block|,
block|{
literal|0155
block|,
literal|"Msmall"
block|}
block|,
block|{
literal|0156
block|,
literal|"Nsmall"
block|}
block|,
block|{
literal|0226
block|,
literal|"Ntildesmall"
block|}
block|,
block|{
literal|0317
block|,
literal|"OEsmall"
block|}
block|,
block|{
literal|0227
block|,
literal|"Oacutesmall"
block|}
block|,
block|{
literal|0231
block|,
literal|"Ocircumflexsmall"
block|}
block|,
block|{
literal|0232
block|,
literal|"Odieresissmall"
block|}
block|,
block|{
literal|0362
block|,
literal|"Ogoneksmall"
block|}
block|,
block|{
literal|0230
block|,
literal|"Ogravesmall"
block|}
block|,
block|{
literal|0277
block|,
literal|"Oslashsmall"
block|}
block|,
block|{
literal|0157
block|,
literal|"Osmall"
block|}
block|,
block|{
literal|0233
block|,
literal|"Otildesmall"
block|}
block|,
block|{
literal|0160
block|,
literal|"Psmall"
block|}
block|,
block|{
literal|0161
block|,
literal|"Qsmall"
block|}
block|,
block|{
literal|0373
block|,
literal|"Ringsmall"
block|}
block|,
block|{
literal|0162
block|,
literal|"Rsmall"
block|}
block|,
block|{
literal|0247
block|,
literal|"Scaronsmall"
block|}
block|,
block|{
literal|0163
block|,
literal|"Ssmall"
block|}
block|,
block|{
literal|0271
block|,
literal|"Thornsmall"
block|}
block|,
block|{
literal|0176
block|,
literal|"Tildesmall"
block|}
block|,
block|{
literal|0164
block|,
literal|"Tsmall"
block|}
block|,
block|{
literal|0234
block|,
literal|"Uacutesmall"
block|}
block|,
block|{
literal|0236
block|,
literal|"Ucircumflexsmall"
block|}
block|,
block|{
literal|0237
block|,
literal|"Udieresissmall"
block|}
block|,
block|{
literal|0235
block|,
literal|"Ugravesmall"
block|}
block|,
block|{
literal|0165
block|,
literal|"Usmall"
block|}
block|,
block|{
literal|0166
block|,
literal|"Vsmall"
block|}
block|,
block|{
literal|0167
block|,
literal|"Wsmall"
block|}
block|,
block|{
literal|0170
block|,
literal|"Xsmall"
block|}
block|,
block|{
literal|0264
block|,
literal|"Yacutesmall"
block|}
block|,
block|{
literal|0330
block|,
literal|"Ydieresissmall"
block|}
block|,
block|{
literal|0171
block|,
literal|"Ysmall"
block|}
block|,
block|{
literal|0275
block|,
literal|"Zcaronsmall"
block|}
block|,
block|{
literal|0172
block|,
literal|"Zsmall"
block|}
block|,
block|{
literal|046
block|,
literal|"ampersandsmall"
block|}
block|,
block|{
literal|0201
block|,
literal|"asuperior"
block|}
block|,
block|{
literal|0365
block|,
literal|"bsuperior"
block|}
block|,
block|{
literal|0251
block|,
literal|"centinferior"
block|}
block|,
block|{
literal|043
block|,
literal|"centoldstyle"
block|}
block|,
block|{
literal|0202
block|,
literal|"centsuperior"
block|}
block|,
block|{
literal|072
block|,
literal|"colon"
block|}
block|,
block|{
literal|0173
block|,
literal|"colonmonetary"
block|}
block|,
block|{
literal|054
block|,
literal|"comma"
block|}
block|,
block|{
literal|0262
block|,
literal|"commainferior"
block|}
block|,
block|{
literal|0370
block|,
literal|"commasuperior"
block|}
block|,
block|{
literal|0266
block|,
literal|"dollarinferior"
block|}
block|,
block|{
literal|044
block|,
literal|"dollaroldstyle"
block|}
block|,
block|{
literal|045
block|,
literal|"dollarsuperior"
block|}
block|,
block|{
literal|0353
block|,
literal|"dsuperior"
block|}
block|,
block|{
literal|0245
block|,
literal|"eightinferior"
block|}
block|,
block|{
literal|070
block|,
literal|"eightoldstyle"
block|}
block|,
block|{
literal|0241
block|,
literal|"eightsuperior"
block|}
block|,
block|{
literal|0344
block|,
literal|"esuperior"
block|}
block|,
block|{
literal|0326
block|,
literal|"exclamdownsmall"
block|}
block|,
block|{
literal|041
block|,
literal|"exclamsmall"
block|}
block|,
block|{
literal|0126
block|,
literal|"ff"
block|}
block|,
block|{
literal|0131
block|,
literal|"ffi"
block|}
block|,
block|{
literal|0132
block|,
literal|"ffl"
block|}
block|,
block|{
literal|0127
block|,
literal|"fi"
block|}
block|,
block|{
literal|0320
block|,
literal|"figuredash"
block|}
block|,
block|{
literal|0114
block|,
literal|"fiveeighths"
block|}
block|,
block|{
literal|0260
block|,
literal|"fiveinferior"
block|}
block|,
block|{
literal|065
block|,
literal|"fiveoldstyle"
block|}
block|,
block|{
literal|0336
block|,
literal|"fivesuperior"
block|}
block|,
block|{
literal|0130
block|,
literal|"fl"
block|}
block|,
block|{
literal|0242
block|,
literal|"fourinferior"
block|}
block|,
block|{
literal|064
block|,
literal|"fouroldstyle"
block|}
block|,
block|{
literal|0335
block|,
literal|"foursuperior"
block|}
block|,
block|{
literal|057
block|,
literal|"fraction"
block|}
block|,
block|{
literal|055
block|,
literal|"hyphen"
block|}
block|,
block|{
literal|0137
block|,
literal|"hypheninferior"
block|}
block|,
block|{
literal|0321
block|,
literal|"hyphensuperior"
block|}
block|,
block|{
literal|0351
block|,
literal|"isuperior"
block|}
block|,
block|{
literal|0361
block|,
literal|"lsuperior"
block|}
block|,
block|{
literal|0367
block|,
literal|"msuperior"
block|}
block|,
block|{
literal|0273
block|,
literal|"nineinferior"
block|}
block|,
block|{
literal|071
block|,
literal|"nineoldstyle"
block|}
block|,
block|{
literal|0341
block|,
literal|"ninesuperior"
block|}
block|,
block|{
literal|0366
block|,
literal|"nsuperior"
block|}
block|,
block|{
literal|053
block|,
literal|"onedotenleader"
block|}
block|,
block|{
literal|0112
block|,
literal|"oneeighth"
block|}
block|,
block|{
literal|0174
block|,
literal|"onefitted"
block|}
block|,
block|{
literal|0110
block|,
literal|"onehalf"
block|}
block|,
block|{
literal|0301
block|,
literal|"oneinferior"
block|}
block|,
block|{
literal|061
block|,
literal|"oneoldstyle"
block|}
block|,
block|{
literal|0107
block|,
literal|"onequarter"
block|}
block|,
block|{
literal|0332
block|,
literal|"onesuperior"
block|}
block|,
block|{
literal|0116
block|,
literal|"onethird"
block|}
block|,
block|{
literal|0257
block|,
literal|"osuperior"
block|}
block|,
block|{
literal|0133
block|,
literal|"parenleftinferior"
block|}
block|,
block|{
literal|050
block|,
literal|"parenleftsuperior"
block|}
block|,
block|{
literal|0135
block|,
literal|"parenrightinferior"
block|}
block|,
block|{
literal|051
block|,
literal|"parenrightsuperior"
block|}
block|,
block|{
literal|056
block|,
literal|"period"
block|}
block|,
block|{
literal|0263
block|,
literal|"periodinferior"
block|}
block|,
block|{
literal|0371
block|,
literal|"periodsuperior"
block|}
block|,
block|{
literal|0300
block|,
literal|"questiondownsmall"
block|}
block|,
block|{
literal|077
block|,
literal|"questionsmall"
block|}
block|,
block|{
literal|0345
block|,
literal|"rsuperior"
block|}
block|,
block|{
literal|0175
block|,
literal|"rupiah"
block|}
block|,
block|{
literal|073
block|,
literal|"semicolon"
block|}
block|,
block|{
literal|0115
block|,
literal|"seveneighths"
block|}
block|,
block|{
literal|0246
block|,
literal|"seveninferior"
block|}
block|,
block|{
literal|067
block|,
literal|"sevenoldstyle"
block|}
block|,
block|{
literal|0340
block|,
literal|"sevensuperior"
block|}
block|,
block|{
literal|0244
block|,
literal|"sixinferior"
block|}
block|,
block|{
literal|066
block|,
literal|"sixoldstyle"
block|}
block|,
block|{
literal|0337
block|,
literal|"sixsuperior"
block|}
block|,
block|{
literal|040
block|,
literal|"space"
block|}
block|,
block|{
literal|0352
block|,
literal|"ssuperior"
block|}
block|,
block|{
literal|0113
block|,
literal|"threeeighths"
block|}
block|,
block|{
literal|0243
block|,
literal|"threeinferior"
block|}
block|,
block|{
literal|063
block|,
literal|"threeoldstyle"
block|}
block|,
block|{
literal|0111
block|,
literal|"threequarters"
block|}
block|,
block|{
literal|075
block|,
literal|"threequartersemdash"
block|}
block|,
block|{
literal|0334
block|,
literal|"threesuperior"
block|}
block|,
block|{
literal|0346
block|,
literal|"tsuperior"
block|}
block|,
block|{
literal|052
block|,
literal|"twodotenleader"
block|}
block|,
block|{
literal|0252
block|,
literal|"twoinferior"
block|}
block|,
block|{
literal|062
block|,
literal|"twooldstyle"
block|}
block|,
block|{
literal|0333
block|,
literal|"twosuperior"
block|}
block|,
block|{
literal|0117
block|,
literal|"twothirds"
block|}
block|,
block|{
literal|0274
block|,
literal|"zeroinferior"
block|}
block|,
block|{
literal|060
block|,
literal|"zerooldstyle"
block|}
block|,
block|{
literal|0342
block|,
literal|"zerosuperior"
block|}
block|}
decl_stmt|;
comment|/**      * Singleton instance of this class.      */
specifier|public
specifier|static
specifier|final
name|MacExpertEncoding
name|INSTANCE
init|=
operator|new
name|MacExpertEncoding
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|MacExpertEncoding
parameter_list|()
block|{
for|for
control|(
name|Object
index|[]
name|encodingEntry
range|:
name|MAC_EXPERT_ENCODING_TABLE
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
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|MAC_EXPERT_ENCODING
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getEncodingName
parameter_list|()
block|{
return|return
literal|"MacExpertEncoding"
return|;
block|}
block|}
end_class

end_unit

