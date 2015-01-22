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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|HorizontalMetricsTable
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
name|COSDictionary
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
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
name|pdmodel
operator|.
name|common
operator|.
name|COSArrayList
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
name|pdmodel
operator|.
name|font
operator|.
name|encoding
operator|.
name|Encoding
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
name|pdmodel
operator|.
name|font
operator|.
name|encoding
operator|.
name|GlyphList
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
name|pdmodel
operator|.
name|font
operator|.
name|encoding
operator|.
name|WinAnsiEncoding
import|;
end_import

begin_comment
comment|/**  * Embedded PDTrueTypeFont builder. Helper class to populate a PDTrueTypeFont from a TTF.  *  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|final
class|class
name|PDTrueTypeFontEmbedder
extends|extends
name|TrueTypeEmbedder
block|{
specifier|private
specifier|final
name|Encoding
name|fontEncoding
decl_stmt|;
comment|/**      * Creates a new TrueType font embedder for the given TTF as a PDTrueTypeFont.      *      * @param document parent document      * @param dict font dictionary      * @param ttfStream TTF stream      * @throws IOException if the TTF could not be read      */
name|PDTrueTypeFontEmbedder
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|InputStream
name|ttfStream
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|document
argument_list|,
name|dict
argument_list|,
name|ttfStream
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|TRUE_TYPE
argument_list|)
expr_stmt|;
comment|// only support WinAnsiEncoding encoding right now
name|Encoding
name|encoding
init|=
operator|new
name|WinAnsiEncoding
argument_list|()
decl_stmt|;
name|GlyphList
name|glyphList
init|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
decl_stmt|;
name|this
operator|.
name|fontEncoding
operator|=
name|encoding
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|,
name|encoding
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|fontDescriptor
operator|.
name|setSymbolic
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fontDescriptor
operator|.
name|setNonSymbolic
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// add the font descriptor
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|,
name|fontDescriptor
argument_list|)
expr_stmt|;
comment|// set the glyph widths
name|setWidths
argument_list|(
name|dict
argument_list|,
name|glyphList
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the glyph widths in the font dictionary.      */
specifier|private
name|void
name|setWidths
parameter_list|(
name|COSDictionary
name|font
parameter_list|,
name|GlyphList
name|glyphList
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|scaling
init|=
literal|1000f
operator|/
name|ttf
operator|.
name|getHeader
argument_list|()
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|HorizontalMetricsTable
name|hmtx
init|=
name|ttf
operator|.
name|getHorizontalMetrics
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
name|getFontEncoding
argument_list|()
operator|.
name|getCodeToNameMap
argument_list|()
decl_stmt|;
name|int
name|firstChar
init|=
name|Collections
operator|.
name|min
argument_list|(
name|codeToName
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|lastChar
init|=
name|Collections
operator|.
name|max
argument_list|(
name|codeToName
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|widths
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|lastChar
operator|-
name|firstChar
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lastChar
operator|-
name|firstChar
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|widths
operator|.
name|add
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// a character code is mapped to a glyph name via the provided font encoding
comment|// afterwards, the glyph name is translated to a glyph ID.
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|codeToName
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|code
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|code
operator|>=
name|firstChar
operator|&&
name|code
operator|<=
name|lastChar
condition|)
block|{
name|String
name|uni
init|=
name|glyphList
operator|.
name|toUnicode
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|charCode
init|=
name|uni
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|gid
init|=
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|charCode
argument_list|)
decl_stmt|;
name|widths
operator|.
name|set
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|-
name|firstChar
argument_list|,
name|Math
operator|.
name|round
argument_list|(
name|hmtx
operator|.
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
operator|*
name|scaling
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|font
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|,
name|firstChar
argument_list|)
expr_stmt|;
name|font
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|,
name|lastChar
argument_list|)
expr_stmt|;
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|widths
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the font's encoding.      */
specifier|public
name|Encoding
name|getFontEncoding
parameter_list|()
block|{
return|return
name|fontEncoding
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|buildSubset
parameter_list|(
name|InputStream
name|ttfSubset
parameter_list|,
name|String
name|tag
parameter_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCid
parameter_list|)
throws|throws
name|IOException
block|{
comment|// use PDType0Font instead
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

