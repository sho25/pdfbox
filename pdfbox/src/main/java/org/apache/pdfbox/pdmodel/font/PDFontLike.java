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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
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
name|util
operator|.
name|Matrix
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
name|util
operator|.
name|Vector
import|;
end_import

begin_comment
comment|/**  * A font-like object.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDFontLike
block|{
comment|/**      * Returns the name of this font, either the PostScript "BaseName" or the Type 3 "Name".      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the font descriptor, may be null.      */
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns the font matrix, which represents the transformation from glyph space to text space.      */
specifier|public
name|Matrix
name|getFontMatrix
parameter_list|()
function_decl|;
comment|/**      * Returns the font's bounding box.      */
specifier|public
specifier|abstract
name|BoundingBox
name|getBoundingBox
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the position vector (v), in text space, for the given character.      * This represents the position of vertical origin relative to horizontal origin, for      * horizontal writing it will always be (0, 0). For vertical writing both x and y are set.      *      * @param code character code      * @return position vector      */
specifier|public
name|Vector
name|getPositionVector
parameter_list|(
name|int
name|code
parameter_list|)
function_decl|;
comment|/**      * Returns the height of the given character, in glyph space. This can be expensive to      * calculate. Results are only approximate.      *      * @param code character code      */
specifier|public
specifier|abstract
name|float
name|getHeight
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the advance width of the given character, in glyph space.      *      * @param code character code      */
specifier|public
name|float
name|getWidth
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the width of a glyph in the embedded font file.      *      * @param code character code      * @return width in glyph space      * @throws IOException if the font could not be read      */
specifier|public
specifier|abstract
name|float
name|getWidthFromFont
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if the font file is embedded in the PDF.      */
specifier|public
specifier|abstract
name|boolean
name|isEmbedded
parameter_list|()
function_decl|;
comment|/**      * Returns true if the embedded font file is damaged.      */
specifier|public
specifier|abstract
name|boolean
name|isDamaged
parameter_list|()
function_decl|;
comment|/**      * This will get the average font width for all characters.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      */
comment|// todo: this method is highly suspicious, the average glyph width is not usually a good metric
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

