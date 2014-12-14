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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|IOException
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
name|COSArray
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
name|COSInteger
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
name|PDStream
import|;
end_import

begin_comment
comment|/**  * Embedded PDCIDFontType2 builder. Helper class to populate a PDCIDFontType2 and its parent  * PDType0Font from a TTF.  *  * @author Keiji Suzuki  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|PDCIDFontType2Embedder
extends|extends
name|TrueTypeEmbedder
block|{
specifier|private
specifier|final
name|PDType0Font
name|parent
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|cidFont
decl_stmt|;
comment|/**      * Creates a new TrueType font embedder for the given TTF as a PDCIDFontType2.      *      * @param document parent document      * @param dict font dictionary      * @param ttfStream TTF stream      * @param parent parent Type 0 font      * @throws IOException if the TTF could not be read      */
name|PDCIDFontType2Embedder
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|InputStream
name|ttfStream
parameter_list|,
name|PDType0Font
name|parent
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
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
comment|// parent Type 0 font
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
name|TYPE0
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|,
name|COSName
operator|.
name|IDENTITY_H
argument_list|)
expr_stmt|;
comment|// CID = GID
comment|// descendant CIDFont
name|cidFont
operator|=
name|createCIDFont
argument_list|()
expr_stmt|;
name|COSArray
name|descendantFonts
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|descendantFonts
operator|.
name|add
argument_list|(
name|cidFont
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DESCENDANT_FONTS
argument_list|,
name|descendantFonts
argument_list|)
expr_stmt|;
comment|// ToUnicode CMap
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|,
name|createToUnicodeCMap
argument_list|(
name|document
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|PDStream
name|createToUnicodeCMap
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|ToUnicodeWriter
name|toUniWriter
init|=
operator|new
name|ToUnicodeWriter
argument_list|()
decl_stmt|;
name|boolean
name|hasSurrogates
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|gid
init|=
literal|1
init|,
name|max
init|=
name|ttf
operator|.
name|getMaximumProfile
argument_list|()
operator|.
name|getNumGlyphs
argument_list|()
init|;
name|gid
operator|<=
name|max
condition|;
name|gid
operator|++
control|)
block|{
name|Integer
name|codePoint
init|=
name|cmap
operator|.
name|getCharacterCode
argument_list|(
name|gid
argument_list|)
decl_stmt|;
comment|// skip composite glyph components that have no code point
if|if
condition|(
name|codePoint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|codePoint
operator|>
literal|0xFFFF
condition|)
block|{
name|hasSurrogates
operator|=
literal|true
expr_stmt|;
block|}
name|toUniWriter
operator|.
name|add
argument_list|(
name|gid
argument_list|,
operator|new
name|String
argument_list|(
operator|new
name|int
index|[]
block|{
name|codePoint
block|}
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|toUniWriter
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|InputStream
name|cMapStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|PDStream
name|stream
init|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|,
name|cMapStream
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|stream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
comment|// surrogate code points, requires PDF 1.5
if|if
condition|(
name|hasSurrogates
condition|)
block|{
name|float
name|version
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|<
literal|1.5
condition|)
block|{
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|setVersion
argument_list|(
literal|1.5f
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|stream
return|;
block|}
specifier|private
name|COSDictionary
name|toCIDSystemInfo
parameter_list|(
name|String
name|registry
parameter_list|,
name|String
name|ordering
parameter_list|,
name|int
name|supplement
parameter_list|)
block|{
name|COSDictionary
name|info
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|,
name|ordering
argument_list|)
expr_stmt|;
name|info
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|SUPPLEMENT
argument_list|,
name|supplement
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
specifier|private
name|COSDictionary
name|createCIDFont
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|cidFont
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
comment|// Type, Subtype
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
expr_stmt|;
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|CID_FONT_TYPE2
argument_list|)
expr_stmt|;
comment|// BaseFont
name|cidFont
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
comment|// CIDSystemInfo
name|COSDictionary
name|info
init|=
name|toCIDSystemInfo
argument_list|(
literal|"Adobe"
argument_list|,
literal|"Identity"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|,
name|info
argument_list|)
expr_stmt|;
comment|// FontDescriptor
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|,
name|fontDescriptor
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// W - widths
name|int
name|numGlyphs
init|=
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
decl_stmt|;
name|int
index|[]
name|gidwidths
init|=
operator|new
name|int
index|[
name|numGlyphs
operator|*
literal|2
index|]
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
name|numGlyphs
condition|;
name|i
operator|++
control|)
block|{
name|gidwidths
index|[
name|i
operator|*
literal|2
index|]
operator|=
name|i
expr_stmt|;
name|gidwidths
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
operator|=
name|ttf
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getAdvanceWidth
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|getWidths
argument_list|(
name|gidwidths
argument_list|)
argument_list|)
expr_stmt|;
comment|// CIDToGIDMap - todo: optional (can be used for easy sub-setting)
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|,
name|COSName
operator|.
name|IDENTITY
argument_list|)
expr_stmt|;
return|return
name|cidFont
return|;
block|}
specifier|private
name|COSArray
name|getWidths
parameter_list|(
name|int
index|[]
name|widths
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|widths
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"length of widths must be> 0"
argument_list|)
throw|;
block|}
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
name|long
name|lastCid
init|=
name|widths
index|[
literal|0
index|]
decl_stmt|;
name|long
name|lastValue
init|=
name|Math
operator|.
name|round
argument_list|(
name|widths
index|[
literal|1
index|]
operator|*
name|scaling
argument_list|)
decl_stmt|;
name|COSArray
name|inner
init|=
literal|null
decl_stmt|;
name|COSArray
name|outer
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastCid
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|int
name|FIRST
init|=
literal|0
decl_stmt|,
name|BRACKET
init|=
literal|1
decl_stmt|,
name|SERIAL
init|=
literal|2
decl_stmt|;
name|int
name|state
init|=
name|FIRST
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|widths
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|long
name|cid
init|=
name|widths
index|[
name|i
index|]
decl_stmt|;
name|long
name|value
init|=
name|Math
operator|.
name|round
argument_list|(
name|widths
index|[
name|i
operator|+
literal|1
index|]
operator|*
name|scaling
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|FIRST
case|:
if|if
condition|(
name|cid
operator|==
name|lastCid
operator|+
literal|1
operator|&&
name|value
operator|==
name|lastValue
condition|)
block|{
name|state
operator|=
name|SERIAL
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cid
operator|==
name|lastCid
operator|+
literal|1
condition|)
block|{
name|state
operator|=
name|BRACKET
expr_stmt|;
name|inner
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inner
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|inner
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|BRACKET
case|:
if|if
condition|(
name|cid
operator|==
name|lastCid
operator|+
literal|1
operator|&&
name|value
operator|==
name|lastValue
condition|)
block|{
name|state
operator|=
name|SERIAL
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|inner
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastCid
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cid
operator|==
name|lastCid
operator|+
literal|1
condition|)
block|{
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|state
operator|=
name|FIRST
expr_stmt|;
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|inner
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|SERIAL
case|:
if|if
condition|(
name|cid
operator|!=
name|lastCid
operator|+
literal|1
operator|||
name|value
operator|!=
name|lastValue
condition|)
block|{
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastCid
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|=
name|FIRST
expr_stmt|;
block|}
break|break;
block|}
name|lastValue
operator|=
name|value
expr_stmt|;
name|lastCid
operator|=
name|cid
expr_stmt|;
block|}
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|FIRST
case|:
name|inner
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|inner
argument_list|)
expr_stmt|;
break|break;
case|case
name|BRACKET
case|:
name|inner
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|inner
argument_list|)
expr_stmt|;
break|break;
case|case
name|SERIAL
case|:
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastCid
argument_list|)
argument_list|)
expr_stmt|;
name|outer
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|lastValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|outer
return|;
block|}
comment|/**      * Returns the descendant CIDFont.      */
specifier|public
name|PDCIDFont
name|getCIDFont
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFontFactory
operator|.
name|createDescendantFont
argument_list|(
name|cidFont
argument_list|,
name|parent
argument_list|)
return|;
block|}
block|}
end_class

end_unit

