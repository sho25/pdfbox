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
name|contentstream
operator|.
name|operator
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|OperatorName
block|{
comment|// non stroking color
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_COLOR
init|=
literal|"sc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_COLOR_N
init|=
literal|"scn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_RGB
init|=
literal|"rg"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_GRAY
init|=
literal|"g"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_CMYK
init|=
literal|"k"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NON_STROKING_COLORSPACE
init|=
literal|"cs"
decl_stmt|;
comment|// stroking color
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLOR
init|=
literal|"SC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLOR_N
init|=
literal|"SCN"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLOR_RGB
init|=
literal|"RG"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLOR_GRAY
init|=
literal|"G"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLOR_CMYK
init|=
literal|"K"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKING_COLORSPACE
init|=
literal|"CS"
decl_stmt|;
comment|// marked content
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_MARKED_CONTENT_SEQ
init|=
literal|"BDC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_MARKED_CONTENT
init|=
literal|"BMC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|END_MARKED_CONTENT
init|=
literal|"EMC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DRAW_OBJECT
init|=
literal|"Do"
decl_stmt|;
comment|// state
specifier|public
specifier|static
specifier|final
name|String
name|CONCAT
init|=
literal|"cm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESTORE
init|=
literal|"Q"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAVE
init|=
literal|"q"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_FLATNESS
init|=
literal|"i"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_GRAPHICS_STATE_PARAMS
init|=
literal|"gs"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_LINE_CAPSTYLE
init|=
literal|"J"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_LINE_DASHPATTERN
init|=
literal|"d"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_LINE_JOINSTYLE
init|=
literal|"j"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_LINE_MITERLIMIT
init|=
literal|"M"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_LINE_WIDTH
init|=
literal|"w"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_MATRIX
init|=
literal|"Tm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_RENDERINGINTENT
init|=
literal|"ri"
decl_stmt|;
comment|// graphics
specifier|public
specifier|static
specifier|final
name|String
name|APPEND_RECT
init|=
literal|"re"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_INLINE_IMAGE
init|=
literal|"BI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_INLINE_IMAGE_DATA
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|END_INLINE_IMAGE
init|=
literal|"EI"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIP_EVEN_ODD
init|=
literal|"W*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIP_NON_ZERO
init|=
literal|"W"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLOSE_AND_STROKE
init|=
literal|"s"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLOSE_FILL_EVEN_ODD_AND_STROKE
init|=
literal|"b*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLOSE_FILL_NON_ZERO_AND_STROKE
init|=
literal|"b"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLOSE_PATH
init|=
literal|"h"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CURVE_TO
init|=
literal|"c"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CURVE_TO_REPLICATE_FINAL_POINT
init|=
literal|"y"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CURVE_TO_REPLICATE_INITIAL_POINT
init|=
literal|"v"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENDPATH
init|=
literal|"n"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILL_EVEN_ODD_AND_STROKE
init|=
literal|"B*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILL_EVEN_ODD
init|=
literal|"f*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILL_NON_ZERO_AND_STROKE
init|=
literal|"B"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILL_NON_ZERO
init|=
literal|"f"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LEGACY_FILL_NON_ZERO
init|=
literal|"F"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LINE_TO
init|=
literal|"l"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MOVE_TO
init|=
literal|"m"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHADING_FILL
init|=
literal|"sh"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STROKE_PATH
init|=
literal|"S"
decl_stmt|;
comment|// text
specifier|public
specifier|static
specifier|final
name|String
name|BEGIN_TEXT
init|=
literal|"BT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|END_TEXT
init|=
literal|"ET"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MOVE_TEXT
init|=
literal|"Td"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MOVE_TEXT_SET_LEADING
init|=
literal|"TD"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NEXT_LINE
init|=
literal|"T*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_CHAR_SPACING
init|=
literal|"Tc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_FONT_AND_SIZE
init|=
literal|"Tf"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_TEXT_HORIZONTAL_SCALING
init|=
literal|"Tz"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_TEXT_LEADING
init|=
literal|"TL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_TEXT_RENDERINGMODE
init|=
literal|"Tr"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_TEXT_RISE
init|=
literal|"Ts"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SET_WORD_SPACING
init|=
literal|"Tw"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_TEXT
init|=
literal|"Tj"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_TEXT_ADJUSTED
init|=
literal|"TJ"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_TEXT_LINE
init|=
literal|"'"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_TEXT_LINE_AND_SPACE
init|=
literal|"\""
decl_stmt|;
comment|/**      * private constructor      */
specifier|private
name|OperatorName
parameter_list|()
block|{     }
block|}
end_class

end_unit

