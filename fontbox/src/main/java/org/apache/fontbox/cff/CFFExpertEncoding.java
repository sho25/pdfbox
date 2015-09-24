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
comment|/**  * This is specialized CFFEncoding. It's used if the EncodingId of a font is set to 1.  *   * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CFFExpertEncoding
extends|extends
name|CFFEncoding
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
name|CHAR_SID
init|=
literal|1
decl_stmt|;
comment|/**      * Table of character codes and their corresponding sid.      */
specifier|private
specifier|static
specifier|final
name|int
index|[]
index|[]
name|CFF_EXPERT_ENCODING_TABLE
init|=
block|{
block|{
literal|0
block|,
literal|0
block|}
block|,
block|{
literal|1
block|,
literal|0
block|}
block|,
block|{
literal|2
block|,
literal|0
block|}
block|,
block|{
literal|3
block|,
literal|0
block|}
block|,
block|{
literal|4
block|,
literal|0
block|}
block|,
block|{
literal|5
block|,
literal|0
block|}
block|,
block|{
literal|6
block|,
literal|0
block|}
block|,
block|{
literal|7
block|,
literal|0
block|}
block|,
block|{
literal|8
block|,
literal|0
block|}
block|,
block|{
literal|9
block|,
literal|0
block|}
block|,
block|{
literal|10
block|,
literal|0
block|}
block|,
block|{
literal|11
block|,
literal|0
block|}
block|,
block|{
literal|12
block|,
literal|0
block|}
block|,
block|{
literal|13
block|,
literal|0
block|}
block|,
block|{
literal|14
block|,
literal|0
block|}
block|,
block|{
literal|15
block|,
literal|0
block|}
block|,
block|{
literal|16
block|,
literal|0
block|}
block|,
block|{
literal|17
block|,
literal|0
block|}
block|,
block|{
literal|18
block|,
literal|0
block|}
block|,
block|{
literal|19
block|,
literal|0
block|}
block|,
block|{
literal|20
block|,
literal|0
block|}
block|,
block|{
literal|21
block|,
literal|0
block|}
block|,
block|{
literal|22
block|,
literal|0
block|}
block|,
block|{
literal|23
block|,
literal|0
block|}
block|,
block|{
literal|24
block|,
literal|0
block|}
block|,
block|{
literal|25
block|,
literal|0
block|}
block|,
block|{
literal|26
block|,
literal|0
block|}
block|,
block|{
literal|27
block|,
literal|0
block|}
block|,
block|{
literal|28
block|,
literal|0
block|}
block|,
block|{
literal|29
block|,
literal|0
block|}
block|,
block|{
literal|30
block|,
literal|0
block|}
block|,
block|{
literal|31
block|,
literal|0
block|}
block|,
block|{
literal|32
block|,
literal|1
block|}
block|,
block|{
literal|33
block|,
literal|229
block|}
block|,
block|{
literal|34
block|,
literal|230
block|}
block|,
block|{
literal|35
block|,
literal|0
block|}
block|,
block|{
literal|36
block|,
literal|231
block|}
block|,
block|{
literal|37
block|,
literal|232
block|}
block|,
block|{
literal|38
block|,
literal|233
block|}
block|,
block|{
literal|39
block|,
literal|234
block|}
block|,
block|{
literal|40
block|,
literal|235
block|}
block|,
block|{
literal|41
block|,
literal|236
block|}
block|,
block|{
literal|42
block|,
literal|237
block|}
block|,
block|{
literal|43
block|,
literal|238
block|}
block|,
block|{
literal|44
block|,
literal|13
block|}
block|,
block|{
literal|45
block|,
literal|14
block|}
block|,
block|{
literal|46
block|,
literal|15
block|}
block|,
block|{
literal|47
block|,
literal|99
block|}
block|,
block|{
literal|48
block|,
literal|239
block|}
block|,
block|{
literal|49
block|,
literal|240
block|}
block|,
block|{
literal|50
block|,
literal|241
block|}
block|,
block|{
literal|51
block|,
literal|242
block|}
block|,
block|{
literal|52
block|,
literal|243
block|}
block|,
block|{
literal|53
block|,
literal|244
block|}
block|,
block|{
literal|54
block|,
literal|245
block|}
block|,
block|{
literal|55
block|,
literal|246
block|}
block|,
block|{
literal|56
block|,
literal|247
block|}
block|,
block|{
literal|57
block|,
literal|248
block|}
block|,
block|{
literal|58
block|,
literal|27
block|}
block|,
block|{
literal|59
block|,
literal|28
block|}
block|,
block|{
literal|60
block|,
literal|249
block|}
block|,
block|{
literal|61
block|,
literal|250
block|}
block|,
block|{
literal|62
block|,
literal|251
block|}
block|,
block|{
literal|63
block|,
literal|252
block|}
block|,
block|{
literal|64
block|,
literal|0
block|}
block|,
block|{
literal|65
block|,
literal|253
block|}
block|,
block|{
literal|66
block|,
literal|254
block|}
block|,
block|{
literal|67
block|,
literal|255
block|}
block|,
block|{
literal|68
block|,
literal|256
block|}
block|,
block|{
literal|69
block|,
literal|257
block|}
block|,
block|{
literal|70
block|,
literal|0
block|}
block|,
block|{
literal|71
block|,
literal|0
block|}
block|,
block|{
literal|72
block|,
literal|0
block|}
block|,
block|{
literal|73
block|,
literal|258
block|}
block|,
block|{
literal|74
block|,
literal|0
block|}
block|,
block|{
literal|75
block|,
literal|0
block|}
block|,
block|{
literal|76
block|,
literal|259
block|}
block|,
block|{
literal|77
block|,
literal|260
block|}
block|,
block|{
literal|78
block|,
literal|261
block|}
block|,
block|{
literal|79
block|,
literal|262
block|}
block|,
block|{
literal|80
block|,
literal|0
block|}
block|,
block|{
literal|81
block|,
literal|0
block|}
block|,
block|{
literal|82
block|,
literal|263
block|}
block|,
block|{
literal|83
block|,
literal|264
block|}
block|,
block|{
literal|84
block|,
literal|265
block|}
block|,
block|{
literal|85
block|,
literal|0
block|}
block|,
block|{
literal|86
block|,
literal|266
block|}
block|,
block|{
literal|87
block|,
literal|109
block|}
block|,
block|{
literal|88
block|,
literal|110
block|}
block|,
block|{
literal|89
block|,
literal|267
block|}
block|,
block|{
literal|90
block|,
literal|268
block|}
block|,
block|{
literal|91
block|,
literal|269
block|}
block|,
block|{
literal|92
block|,
literal|0
block|}
block|,
block|{
literal|93
block|,
literal|270
block|}
block|,
block|{
literal|94
block|,
literal|271
block|}
block|,
block|{
literal|95
block|,
literal|272
block|}
block|,
block|{
literal|96
block|,
literal|273
block|}
block|,
block|{
literal|97
block|,
literal|274
block|}
block|,
block|{
literal|98
block|,
literal|275
block|}
block|,
block|{
literal|99
block|,
literal|276
block|}
block|,
block|{
literal|100
block|,
literal|277
block|}
block|,
block|{
literal|101
block|,
literal|278
block|}
block|,
block|{
literal|102
block|,
literal|279
block|}
block|,
block|{
literal|103
block|,
literal|280
block|}
block|,
block|{
literal|104
block|,
literal|281
block|}
block|,
block|{
literal|105
block|,
literal|282
block|}
block|,
block|{
literal|106
block|,
literal|283
block|}
block|,
block|{
literal|107
block|,
literal|284
block|}
block|,
block|{
literal|108
block|,
literal|285
block|}
block|,
block|{
literal|109
block|,
literal|286
block|}
block|,
block|{
literal|110
block|,
literal|287
block|}
block|,
block|{
literal|111
block|,
literal|288
block|}
block|,
block|{
literal|112
block|,
literal|289
block|}
block|,
block|{
literal|113
block|,
literal|290
block|}
block|,
block|{
literal|114
block|,
literal|291
block|}
block|,
block|{
literal|115
block|,
literal|292
block|}
block|,
block|{
literal|116
block|,
literal|293
block|}
block|,
block|{
literal|117
block|,
literal|294
block|}
block|,
block|{
literal|118
block|,
literal|295
block|}
block|,
block|{
literal|119
block|,
literal|296
block|}
block|,
block|{
literal|120
block|,
literal|297
block|}
block|,
block|{
literal|121
block|,
literal|298
block|}
block|,
block|{
literal|122
block|,
literal|299
block|}
block|,
block|{
literal|123
block|,
literal|300
block|}
block|,
block|{
literal|124
block|,
literal|301
block|}
block|,
block|{
literal|125
block|,
literal|302
block|}
block|,
block|{
literal|126
block|,
literal|303
block|}
block|,
block|{
literal|127
block|,
literal|0
block|}
block|,
block|{
literal|128
block|,
literal|0
block|}
block|,
block|{
literal|129
block|,
literal|0
block|}
block|,
block|{
literal|130
block|,
literal|0
block|}
block|,
block|{
literal|131
block|,
literal|0
block|}
block|,
block|{
literal|132
block|,
literal|0
block|}
block|,
block|{
literal|133
block|,
literal|0
block|}
block|,
block|{
literal|134
block|,
literal|0
block|}
block|,
block|{
literal|135
block|,
literal|0
block|}
block|,
block|{
literal|136
block|,
literal|0
block|}
block|,
block|{
literal|137
block|,
literal|0
block|}
block|,
block|{
literal|138
block|,
literal|0
block|}
block|,
block|{
literal|139
block|,
literal|0
block|}
block|,
block|{
literal|140
block|,
literal|0
block|}
block|,
block|{
literal|141
block|,
literal|0
block|}
block|,
block|{
literal|142
block|,
literal|0
block|}
block|,
block|{
literal|143
block|,
literal|0
block|}
block|,
block|{
literal|144
block|,
literal|0
block|}
block|,
block|{
literal|145
block|,
literal|0
block|}
block|,
block|{
literal|146
block|,
literal|0
block|}
block|,
block|{
literal|147
block|,
literal|0
block|}
block|,
block|{
literal|148
block|,
literal|0
block|}
block|,
block|{
literal|149
block|,
literal|0
block|}
block|,
block|{
literal|150
block|,
literal|0
block|}
block|,
block|{
literal|151
block|,
literal|0
block|}
block|,
block|{
literal|152
block|,
literal|0
block|}
block|,
block|{
literal|153
block|,
literal|0
block|}
block|,
block|{
literal|154
block|,
literal|0
block|}
block|,
block|{
literal|155
block|,
literal|0
block|}
block|,
block|{
literal|156
block|,
literal|0
block|}
block|,
block|{
literal|157
block|,
literal|0
block|}
block|,
block|{
literal|158
block|,
literal|0
block|}
block|,
block|{
literal|159
block|,
literal|0
block|}
block|,
block|{
literal|160
block|,
literal|0
block|}
block|,
block|{
literal|161
block|,
literal|304
block|}
block|,
block|{
literal|162
block|,
literal|305
block|}
block|,
block|{
literal|163
block|,
literal|306
block|}
block|,
block|{
literal|164
block|,
literal|0
block|}
block|,
block|{
literal|165
block|,
literal|0
block|}
block|,
block|{
literal|166
block|,
literal|307
block|}
block|,
block|{
literal|167
block|,
literal|308
block|}
block|,
block|{
literal|168
block|,
literal|309
block|}
block|,
block|{
literal|169
block|,
literal|310
block|}
block|,
block|{
literal|170
block|,
literal|311
block|}
block|,
block|{
literal|171
block|,
literal|0
block|}
block|,
block|{
literal|172
block|,
literal|312
block|}
block|,
block|{
literal|173
block|,
literal|0
block|}
block|,
block|{
literal|174
block|,
literal|0
block|}
block|,
block|{
literal|175
block|,
literal|313
block|}
block|,
block|{
literal|176
block|,
literal|0
block|}
block|,
block|{
literal|177
block|,
literal|0
block|}
block|,
block|{
literal|178
block|,
literal|314
block|}
block|,
block|{
literal|179
block|,
literal|315
block|}
block|,
block|{
literal|180
block|,
literal|0
block|}
block|,
block|{
literal|181
block|,
literal|0
block|}
block|,
block|{
literal|182
block|,
literal|316
block|}
block|,
block|{
literal|183
block|,
literal|317
block|}
block|,
block|{
literal|184
block|,
literal|318
block|}
block|,
block|{
literal|185
block|,
literal|0
block|}
block|,
block|{
literal|186
block|,
literal|0
block|}
block|,
block|{
literal|187
block|,
literal|0
block|}
block|,
block|{
literal|188
block|,
literal|158
block|}
block|,
block|{
literal|189
block|,
literal|155
block|}
block|,
block|{
literal|190
block|,
literal|163
block|}
block|,
block|{
literal|191
block|,
literal|319
block|}
block|,
block|{
literal|192
block|,
literal|320
block|}
block|,
block|{
literal|193
block|,
literal|321
block|}
block|,
block|{
literal|194
block|,
literal|322
block|}
block|,
block|{
literal|195
block|,
literal|323
block|}
block|,
block|{
literal|196
block|,
literal|324
block|}
block|,
block|{
literal|197
block|,
literal|325
block|}
block|,
block|{
literal|198
block|,
literal|0
block|}
block|,
block|{
literal|199
block|,
literal|0
block|}
block|,
block|{
literal|200
block|,
literal|326
block|}
block|,
block|{
literal|201
block|,
literal|150
block|}
block|,
block|{
literal|202
block|,
literal|164
block|}
block|,
block|{
literal|203
block|,
literal|169
block|}
block|,
block|{
literal|204
block|,
literal|327
block|}
block|,
block|{
literal|205
block|,
literal|328
block|}
block|,
block|{
literal|206
block|,
literal|329
block|}
block|,
block|{
literal|207
block|,
literal|330
block|}
block|,
block|{
literal|208
block|,
literal|331
block|}
block|,
block|{
literal|209
block|,
literal|332
block|}
block|,
block|{
literal|210
block|,
literal|333
block|}
block|,
block|{
literal|211
block|,
literal|334
block|}
block|,
block|{
literal|212
block|,
literal|335
block|}
block|,
block|{
literal|213
block|,
literal|336
block|}
block|,
block|{
literal|214
block|,
literal|337
block|}
block|,
block|{
literal|215
block|,
literal|338
block|}
block|,
block|{
literal|216
block|,
literal|339
block|}
block|,
block|{
literal|217
block|,
literal|340
block|}
block|,
block|{
literal|218
block|,
literal|341
block|}
block|,
block|{
literal|219
block|,
literal|342
block|}
block|,
block|{
literal|220
block|,
literal|343
block|}
block|,
block|{
literal|221
block|,
literal|344
block|}
block|,
block|{
literal|222
block|,
literal|345
block|}
block|,
block|{
literal|223
block|,
literal|346
block|}
block|,
block|{
literal|224
block|,
literal|347
block|}
block|,
block|{
literal|225
block|,
literal|348
block|}
block|,
block|{
literal|226
block|,
literal|349
block|}
block|,
block|{
literal|227
block|,
literal|350
block|}
block|,
block|{
literal|228
block|,
literal|351
block|}
block|,
block|{
literal|229
block|,
literal|352
block|}
block|,
block|{
literal|230
block|,
literal|353
block|}
block|,
block|{
literal|231
block|,
literal|354
block|}
block|,
block|{
literal|232
block|,
literal|355
block|}
block|,
block|{
literal|233
block|,
literal|356
block|}
block|,
block|{
literal|234
block|,
literal|357
block|}
block|,
block|{
literal|235
block|,
literal|358
block|}
block|,
block|{
literal|236
block|,
literal|359
block|}
block|,
block|{
literal|237
block|,
literal|360
block|}
block|,
block|{
literal|238
block|,
literal|361
block|}
block|,
block|{
literal|239
block|,
literal|362
block|}
block|,
block|{
literal|240
block|,
literal|363
block|}
block|,
block|{
literal|241
block|,
literal|364
block|}
block|,
block|{
literal|242
block|,
literal|365
block|}
block|,
block|{
literal|243
block|,
literal|366
block|}
block|,
block|{
literal|244
block|,
literal|367
block|}
block|,
block|{
literal|245
block|,
literal|368
block|}
block|,
block|{
literal|246
block|,
literal|369
block|}
block|,
block|{
literal|247
block|,
literal|370
block|}
block|,
block|{
literal|248
block|,
literal|371
block|}
block|,
block|{
literal|249
block|,
literal|372
block|}
block|,
block|{
literal|250
block|,
literal|373
block|}
block|,
block|{
literal|251
block|,
literal|374
block|}
block|,
block|{
literal|252
block|,
literal|375
block|}
block|,
block|{
literal|253
block|,
literal|376
block|}
block|,
block|{
literal|254
block|,
literal|377
block|}
block|,
block|{
literal|255
block|,
literal|378
block|}
block|}
decl_stmt|;
specifier|private
name|CFFExpertEncoding
parameter_list|()
block|{     }
comment|/**      * Returns an instance of the CFFExportEncoding class.      * @return an instance of CFFExportEncoding      */
specifier|public
specifier|static
name|CFFExpertEncoding
name|getInstance
parameter_list|()
block|{
return|return
name|CFFExpertEncoding
operator|.
name|INSTANCE
return|;
block|}
specifier|private
specifier|static
specifier|final
name|CFFExpertEncoding
name|INSTANCE
init|=
operator|new
name|CFFExpertEncoding
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|int
index|[]
name|encodingEntry
range|:
name|CFF_EXPERT_ENCODING_TABLE
control|)
block|{
name|INSTANCE
operator|.
name|add
argument_list|(
name|encodingEntry
index|[
name|CHAR_CODE
index|]
argument_list|,
name|encodingEntry
index|[
name|CHAR_SID
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

