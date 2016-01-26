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
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|TrueTypeFont
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
name|PDDocument
name|document
decl_stmt|;
specifier|private
specifier|final
name|PDType0Font
name|parent
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dict
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|cidFont
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToUni
decl_stmt|;
comment|/**      * Creates a new TrueType font embedder for the given TTF as a PDCIDFontType2.      *      * @param document parent document      * @param dict font dictionary      * @param ttf True Type Font      * @param parent parent Type 0 font      * @throws IOException if the TTF could not be read      */
name|PDCIDFontType2Embedder
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|TrueTypeFont
name|ttf
parameter_list|,
name|boolean
name|embedSubset
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
name|ttf
argument_list|,
name|embedSubset
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|dict
operator|=
name|dict
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
comment|// build GID -> Unicode map
name|gidToUni
operator|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|ttf
operator|.
name|getMaximumProfile
argument_list|()
operator|.
name|getNumGlyphs
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// skip composite glyph components that have no code point
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
if|if
condition|(
name|codePoint
operator|!=
literal|null
condition|)
block|{
name|gidToUni
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|codePoint
argument_list|)
expr_stmt|;
comment|// CID = GID
block|}
block|}
comment|// ToUnicode CMap
name|buildToUnicodeCMap
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Rebuild a font subset.      */
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
comment|// build CID2GIDMap, because the content stream has been written with the old GIDs
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cidToGid
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|gidToCid
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|gidToCid
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|newGID
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|int
name|oldGID
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|cidToGid
operator|.
name|put
argument_list|(
name|oldGID
argument_list|,
name|newGID
argument_list|)
expr_stmt|;
block|}
comment|// rebuild the relevant part of the font
name|buildFontFile2
argument_list|(
name|ttfSubset
argument_list|)
expr_stmt|;
name|addNameTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|buildWidths
argument_list|(
name|cidToGid
argument_list|)
expr_stmt|;
name|buildCIDToGIDMap
argument_list|(
name|cidToGid
argument_list|)
expr_stmt|;
name|buildCIDSet
argument_list|(
name|cidToGid
argument_list|)
expr_stmt|;
name|buildToUnicodeCMap
argument_list|(
name|gidToCid
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|buildToUnicodeCMap
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|newGIDToOldCID
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
comment|// optional CID2GIDMap for subsetting
name|int
name|cid
decl_stmt|;
if|if
condition|(
name|newGIDToOldCID
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|newGIDToOldCID
operator|.
name|containsKey
argument_list|(
name|gid
argument_list|)
condition|)
block|{
continue|continue;
block|}
else|else
block|{
name|cid
operator|=
name|newGIDToOldCID
operator|.
name|get
argument_list|(
name|gid
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cid
operator|=
name|gid
expr_stmt|;
block|}
comment|// skip composite glyph components that have no code point
name|Integer
name|codePoint
init|=
name|gidToUni
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
comment|// old GID -> Unicode
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
name|cid
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
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
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
name|setVersion
argument_list|(
literal|1.5f
argument_list|)
expr_stmt|;
block|}
block|}
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|,
name|stream
argument_list|)
expr_stmt|;
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
name|buildWidths
argument_list|(
name|cidFont
argument_list|)
expr_stmt|;
comment|// CIDToGIDMap
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
name|void
name|addNameTag
parameter_list|(
name|String
name|tag
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
decl_stmt|;
name|String
name|newName
init|=
name|tag
operator|+
name|name
decl_stmt|;
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|fontDescriptor
operator|.
name|setFontName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|cidFont
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|buildCIDToGIDMap
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cidToGid
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|cidMax
init|=
name|Collections
operator|.
name|max
argument_list|(
name|cidToGid
operator|.
name|keySet
argument_list|()
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
operator|<=
name|cidMax
condition|;
name|i
operator|++
control|)
block|{
name|int
name|gid
decl_stmt|;
if|if
condition|(
name|cidToGid
operator|.
name|containsKey
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|gid
operator|=
name|cidToGid
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|gid
operator|=
literal|0
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
operator|new
name|byte
index|[]
block|{
call|(
name|byte
call|)
argument_list|(
name|gid
operator|>>
literal|8
operator|&
literal|0xff
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|gid
operator|&
literal|0xff
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|input
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
name|input
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|,
name|stream
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|cidFont
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Builds the CIDSet entry, required by PDF/A. This lists all CIDs in the font.      */
specifier|private
name|void
name|buildCIDSet
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cidToGid
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|Collections
operator|.
name|max
argument_list|(
name|cidToGid
operator|.
name|keySet
argument_list|()
argument_list|)
operator|/
literal|8
operator|+
literal|1
index|]
decl_stmt|;
for|for
control|(
name|int
name|cid
range|:
name|cidToGid
operator|.
name|keySet
argument_list|()
control|)
block|{
name|int
name|mask
init|=
literal|1
operator|<<
literal|7
operator|-
name|cid
operator|%
literal|8
decl_stmt|;
name|bytes
index|[
name|cid
operator|/
literal|8
index|]
operator||=
name|mask
expr_stmt|;
block|}
name|InputStream
name|input
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
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
name|input
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|fontDescriptor
operator|.
name|setCIDSet
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Builds wieths with a custom CIDToGIDMap (for embedding font subset).      */
specifier|private
name|void
name|buildWidths
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|cidToGid
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
name|COSArray
name|widths
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|COSArray
name|ws
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|int
name|prev
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|cid
range|:
name|cidToGid
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|cidToGid
operator|.
name|containsKey
argument_list|(
name|cid
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|int
name|gid
init|=
name|cidToGid
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|float
name|width
init|=
name|ttf
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
operator|*
name|scaling
decl_stmt|;
comment|// c [w1 w2 ... wn]
if|if
condition|(
name|prev
operator|!=
name|cid
operator|-
literal|1
condition|)
block|{
name|ws
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|widths
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
comment|// c
name|widths
operator|.
name|add
argument_list|(
name|ws
argument_list|)
expr_stmt|;
block|}
name|ws
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|Math
operator|.
name|round
argument_list|(
name|width
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// wi
name|prev
operator|=
name|cid
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
name|widths
argument_list|)
expr_stmt|;
block|}
comment|/**      * Build widths with Identity CIDToGIDMap (for embedding full font).      */
specifier|private
name|void
name|buildWidths
parameter_list|(
name|COSDictionary
name|cidFont
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|cidMax
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
name|cidMax
operator|*
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|cid
init|=
literal|0
init|;
name|cid
operator|<
name|cidMax
condition|;
name|cid
operator|++
control|)
block|{
name|gidwidths
index|[
name|cid
operator|*
literal|2
index|]
operator|=
name|cid
expr_stmt|;
name|gidwidths
index|[
name|cid
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
name|cid
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
block|}
enum|enum
name|State
block|{
name|FIRST
block|,
name|BRACKET
block|,
name|SERIAL
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
name|State
name|state
init|=
name|State
operator|.
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
name|State
operator|.
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
name|State
operator|.
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
name|State
operator|.
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
name|State
operator|.
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
name|State
operator|.
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
operator|new
name|PDCIDFontType2
argument_list|(
name|cidFont
argument_list|,
name|parent
argument_list|,
name|ttf
argument_list|)
return|;
block|}
block|}
end_class

end_unit

