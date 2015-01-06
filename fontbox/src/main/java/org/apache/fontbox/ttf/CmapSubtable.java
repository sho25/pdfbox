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
comment|/**  * A "cmap" subtable.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|CmapSubtable
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
name|CmapSubtable
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|LEAD_OFFSET
init|=
literal|0xD800
operator|-
operator|(
literal|0x10000
operator|>>
literal|10
operator|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|SURROGATE_OFFSET
init|=
literal|0x10000
operator|-
operator|(
literal|0xD800
operator|<<
literal|10
operator|)
operator|-
literal|0xDC00
decl_stmt|;
specifier|private
name|int
name|platformId
decl_stmt|;
specifier|private
name|int
name|platformEncodingId
decl_stmt|;
specifier|private
name|long
name|subTableOffset
decl_stmt|;
specifier|private
name|int
index|[]
name|glyphIdToCharacterCode
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|characterCodeToGlyphId
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initData
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|platformId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|platformEncodingId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|subTableOffset
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will read the required data from the stream.      *       * @param cmap the CMAP this encoding belongs to.      * @param numGlyphs number of glyphs.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initSubtable
parameter_list|(
name|CmapTable
name|cmap
parameter_list|,
name|int
name|numGlyphs
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|data
operator|.
name|seek
argument_list|(
name|cmap
operator|.
name|getOffset
argument_list|()
operator|+
name|subTableOffset
argument_list|)
expr_stmt|;
name|int
name|subtableFormat
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|long
name|length
decl_stmt|;
name|long
name|version
decl_stmt|;
if|if
condition|(
name|subtableFormat
operator|<
literal|8
condition|)
block|{
name|length
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|version
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// read an other UnsignedShort to read a Fixed32
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|length
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|version
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
block|}
switch|switch
condition|(
name|subtableFormat
condition|)
block|{
case|case
literal|0
case|:
name|processSubtype0
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|processSubtype2
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|processSubtype4
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|6
case|:
name|processSubtype6
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|8
case|:
name|processSubtype8
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|10
case|:
name|processSubtype10
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|12
case|:
name|processSubtype12
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|13
case|:
name|processSubtype13
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
case|case
literal|14
case|:
name|processSubtype14
argument_list|(
name|data
argument_list|,
name|numGlyphs
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown cmap format:"
operator|+
name|subtableFormat
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reads a format 8 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype8
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
comment|// --- is32 is a 65536 BITS array ( = 8192 BYTES)
name|int
index|[]
name|is32
init|=
name|data
operator|.
name|readUnsignedByteArray
argument_list|(
literal|8192
argument_list|)
decl_stmt|;
name|long
name|nbGroups
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
comment|// --- nbGroups shouldn't be greater than 65536
if|if
condition|(
name|nbGroups
operator|>
literal|65536
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"CMap ( Subtype8 ) is invalid"
argument_list|)
throw|;
block|}
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
name|numGlyphs
argument_list|)
expr_stmt|;
comment|// -- Read all sub header
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nbGroups
condition|;
operator|++
name|i
control|)
block|{
name|long
name|firstCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|endCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|startGlyph
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
comment|// -- process simple validation
if|if
condition|(
name|firstCode
operator|>
name|endCode
operator|||
literal|0
operator|>
name|firstCode
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Range invalid"
argument_list|)
throw|;
block|}
for|for
control|(
name|long
name|j
init|=
name|firstCode
init|;
name|j
operator|<=
name|endCode
condition|;
operator|++
name|j
control|)
block|{
comment|// -- Convert the Character code in decimal
if|if
condition|(
name|j
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"[Sub Format 8] Invalid Character code"
argument_list|)
throw|;
block|}
name|int
name|currentCharCode
decl_stmt|;
if|if
condition|(
operator|(
name|is32
index|[
operator|(
name|int
operator|)
name|j
operator|/
literal|8
index|]
operator|&
operator|(
literal|1
operator|<<
operator|(
operator|(
name|int
operator|)
name|j
operator|%
literal|8
operator|)
operator|)
operator|)
operator|==
literal|0
condition|)
block|{
name|currentCharCode
operator|=
operator|(
name|int
operator|)
name|j
expr_stmt|;
block|}
else|else
block|{
comment|// the character code uses a 32bits format
comment|// convert it in decimal : see http://www.unicode.org/faq//utf_bom.html#utf16-4
name|long
name|lead
init|=
name|LEAD_OFFSET
operator|+
operator|(
name|j
operator|>>
literal|10
operator|)
decl_stmt|;
name|long
name|trail
init|=
literal|0xDC00
operator|+
operator|(
name|j
operator|&
literal|0x3FF
operator|)
decl_stmt|;
name|long
name|codepoint
init|=
operator|(
name|lead
operator|<<
literal|10
operator|)
operator|+
name|trail
operator|+
name|SURROGATE_OFFSET
decl_stmt|;
if|if
condition|(
name|codepoint
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"[Sub Format 8] Invalid Character code"
argument_list|)
throw|;
block|}
name|currentCharCode
operator|=
operator|(
name|int
operator|)
name|codepoint
expr_stmt|;
block|}
name|long
name|glyphIndex
init|=
name|startGlyph
operator|+
operator|(
name|j
operator|-
name|firstCode
operator|)
decl_stmt|;
if|if
condition|(
name|glyphIndex
operator|>
name|numGlyphs
operator|||
name|glyphIndex
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"CMap contains an invalid glyph index"
argument_list|)
throw|;
block|}
name|glyphIdToCharacterCode
index|[
operator|(
name|int
operator|)
name|glyphIndex
index|]
operator|=
name|currentCharCode
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
name|currentCharCode
argument_list|,
operator|(
name|int
operator|)
name|glyphIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Reads a format 10 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype10
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|startCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|numChars
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|numChars
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid number of Characters"
argument_list|)
throw|;
block|}
if|if
condition|(
name|startCode
argument_list|<
literal|0
operator|||
name|startCode
argument_list|>
literal|0x0010FFFF
operator|||
operator|(
name|startCode
operator|+
name|numChars
operator|)
operator|>
literal|0x0010FFFF
operator|||
operator|(
operator|(
name|startCode
operator|+
name|numChars
operator|)
operator|>=
literal|0x0000D800
operator|&&
operator|(
name|startCode
operator|+
name|numChars
operator|)
operator|<=
literal|0x0000DFFF
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid Characters codes"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reads a format 12 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype12
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|nbGroups
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
name|numGlyphs
argument_list|)
expr_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nbGroups
condition|;
operator|++
name|i
control|)
block|{
name|long
name|firstCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|endCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|startGlyph
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|firstCode
argument_list|<
literal|0
operator|||
name|firstCode
argument_list|>
literal|0x0010FFFF
operator|||
name|firstCode
operator|>=
literal|0x0000D800
operator|&&
name|firstCode
operator|<=
literal|0x0000DFFF
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid characters codes"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endCode
operator|>
literal|0
operator|&&
name|endCode
argument_list|<
name|firstCode
operator|||
name|endCode
argument_list|>
literal|0x0010FFFF
operator|||
name|endCode
operator|>=
literal|0x0000D800
operator|&&
name|endCode
operator|<=
literal|0x0000DFFF
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid characters codes"
argument_list|)
throw|;
block|}
for|for
control|(
name|long
name|j
init|=
literal|0
init|;
name|j
operator|<=
name|endCode
operator|-
name|firstCode
condition|;
operator|++
name|j
control|)
block|{
name|long
name|glyphIndex
init|=
name|startGlyph
operator|+
name|j
decl_stmt|;
if|if
condition|(
name|glyphIndex
operator|>=
name|numGlyphs
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Format 12 cmap contains an invalid glyph index"
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|firstCode
operator|+
name|j
operator|>
literal|0x10FFFF
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Format 12 cmap contains character beyond UCS-4"
argument_list|)
expr_stmt|;
block|}
name|glyphIdToCharacterCode
index|[
operator|(
name|int
operator|)
name|glyphIndex
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|firstCode
operator|+
name|j
argument_list|)
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
call|(
name|int
call|)
argument_list|(
name|firstCode
operator|+
name|j
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|glyphIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Reads a format 13 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype13
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|nbGroups
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nbGroups
condition|;
operator|++
name|i
control|)
block|{
name|long
name|firstCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|endCode
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
name|long
name|glyphId
init|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|glyphId
operator|>
name|numGlyphs
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Format 13 cmap contains an invalid glyph index"
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|firstCode
argument_list|<
literal|0
operator|||
name|firstCode
argument_list|>
literal|0x0010FFFF
operator|||
operator|(
name|firstCode
operator|>=
literal|0x0000D800
operator|&&
name|firstCode
operator|<=
literal|0x0000DFFF
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid Characters codes"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|(
name|endCode
operator|>
literal|0
operator|&&
name|endCode
operator|<
name|firstCode
operator|)
operator|||
name|endCode
operator|>
literal|0x0010FFFF
operator|||
operator|(
name|endCode
operator|>=
literal|0x0000D800
operator|&&
name|endCode
operator|<=
literal|0x0000DFFF
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid Characters codes"
argument_list|)
throw|;
block|}
for|for
control|(
name|long
name|j
init|=
literal|0
init|;
name|j
operator|<=
name|endCode
operator|-
name|firstCode
condition|;
operator|++
name|j
control|)
block|{
if|if
condition|(
name|firstCode
operator|+
name|j
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Character Code greater than Integer.MAX_VALUE"
argument_list|)
throw|;
block|}
if|if
condition|(
name|firstCode
operator|+
name|j
operator|>
literal|0x10FFFF
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Format 13 cmap contains character beyond UCS-4"
argument_list|)
expr_stmt|;
block|}
name|glyphIdToCharacterCode
index|[
operator|(
name|int
operator|)
name|glyphId
index|]
operator|=
call|(
name|int
call|)
argument_list|(
name|firstCode
operator|+
name|j
argument_list|)
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
call|(
name|int
call|)
argument_list|(
name|firstCode
operator|+
name|j
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|glyphId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Reads a format 14 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype14
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Unicode Variation Sequences (UVS)
comment|// see http://blogs.adobe.com/CCJKType/2013/05/opentype-cmap-table-ramblings.html
name|LOG
operator|.
name|warn
argument_list|(
literal|"Format 14 cmap table is not supported and will be ignored"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads a format 6 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype6
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|firstCode
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
name|numGlyphs
argument_list|)
expr_stmt|;
name|int
index|[]
name|glyphIdArray
init|=
name|data
operator|.
name|readUnsignedShortArray
argument_list|(
name|entryCount
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
name|entryCount
condition|;
name|i
operator|++
control|)
block|{
name|glyphIdToCharacterCode
index|[
name|glyphIdArray
index|[
name|i
index|]
index|]
operator|=
name|firstCode
operator|+
name|i
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
operator|(
name|firstCode
operator|+
name|i
operator|)
argument_list|,
name|glyphIdArray
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Reads a format 4 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype4
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|segCountX2
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|segCount
init|=
name|segCountX2
operator|/
literal|2
decl_stmt|;
name|int
name|searchRange
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|entrySelector
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|rangeShift
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
index|[]
name|endCount
init|=
name|data
operator|.
name|readUnsignedShortArray
argument_list|(
name|segCount
argument_list|)
decl_stmt|;
name|int
name|reservedPad
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
index|[]
name|startCount
init|=
name|data
operator|.
name|readUnsignedShortArray
argument_list|(
name|segCount
argument_list|)
decl_stmt|;
name|int
index|[]
name|idDelta
init|=
name|data
operator|.
name|readUnsignedShortArray
argument_list|(
name|segCount
argument_list|)
decl_stmt|;
name|int
index|[]
name|idRangeOffset
init|=
name|data
operator|.
name|readUnsignedShortArray
argument_list|(
name|segCount
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|tmpGlyphToChar
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|long
name|currentPosition
init|=
name|data
operator|.
name|getCurrentPosition
argument_list|()
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
name|segCount
condition|;
name|i
operator|++
control|)
block|{
name|int
name|start
init|=
name|startCount
index|[
name|i
index|]
decl_stmt|;
name|int
name|end
init|=
name|endCount
index|[
name|i
index|]
decl_stmt|;
name|int
name|delta
init|=
name|idDelta
index|[
name|i
index|]
decl_stmt|;
name|int
name|rangeOffset
init|=
name|idRangeOffset
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|start
operator|!=
literal|65535
operator|&&
name|end
operator|!=
literal|65535
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
name|start
init|;
name|j
operator|<=
name|end
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|rangeOffset
operator|==
literal|0
condition|)
block|{
name|int
name|glyphid
init|=
operator|(
name|j
operator|+
name|delta
operator|)
operator|%
literal|65536
decl_stmt|;
name|tmpGlyphToChar
operator|.
name|put
argument_list|(
name|glyphid
argument_list|,
name|j
argument_list|)
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
name|j
argument_list|,
name|glyphid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|long
name|glyphOffset
init|=
name|currentPosition
operator|+
operator|(
operator|(
name|rangeOffset
operator|/
literal|2
operator|)
operator|+
operator|(
name|j
operator|-
name|start
operator|)
operator|+
operator|(
name|i
operator|-
name|segCount
operator|)
operator|)
operator|*
literal|2
decl_stmt|;
name|data
operator|.
name|seek
argument_list|(
name|glyphOffset
argument_list|)
expr_stmt|;
name|int
name|glyphIndex
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
if|if
condition|(
name|glyphIndex
operator|!=
literal|0
condition|)
block|{
name|glyphIndex
operator|+=
name|delta
expr_stmt|;
name|glyphIndex
operator|=
name|glyphIndex
operator|%
literal|65536
expr_stmt|;
if|if
condition|(
operator|!
name|tmpGlyphToChar
operator|.
name|containsKey
argument_list|(
name|glyphIndex
argument_list|)
condition|)
block|{
name|tmpGlyphToChar
operator|.
name|put
argument_list|(
name|glyphIndex
argument_list|,
name|j
argument_list|)
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
name|j
argument_list|,
name|glyphIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
comment|/*          * this is the final result key=glyphId, value is character codes Create an array that contains MAX(GlyphIds)          * element, or -1          */
if|if
condition|(
name|tmpGlyphToChar
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"cmap format 4 subtable is empty"
argument_list|)
expr_stmt|;
return|return;
block|}
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
name|Collections
operator|.
name|max
argument_list|(
name|tmpGlyphToChar
operator|.
name|keySet
argument_list|()
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|tmpGlyphToChar
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// link the glyphId with the right character code
name|glyphIdToCharacterCode
index|[
name|entry
operator|.
name|getKey
argument_list|()
index|]
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Read a format 2 subtable.      *       * @param data the data stream of the to be parsed ttf font      * @param numGlyphs number of glyphs to be read      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype2
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|numGlyphs
parameter_list|)
throws|throws
name|IOException
block|{
name|int
index|[]
name|subHeaderKeys
init|=
operator|new
name|int
index|[
literal|256
index|]
decl_stmt|;
comment|// ---- keep the Max Index of the SubHeader array to know its length
name|int
name|maxSubHeaderIndex
init|=
literal|0
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
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|subHeaderKeys
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxSubHeaderIndex
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxSubHeaderIndex
argument_list|,
name|subHeaderKeys
index|[
name|i
index|]
operator|/
literal|8
argument_list|)
expr_stmt|;
block|}
comment|// ---- Read all SubHeaders to avoid useless seek on DataSource
name|SubHeader
index|[]
name|subHeaders
init|=
operator|new
name|SubHeader
index|[
name|maxSubHeaderIndex
operator|+
literal|1
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
operator|<=
name|maxSubHeaderIndex
condition|;
operator|++
name|i
control|)
block|{
name|int
name|firstCode
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|short
name|idDelta
init|=
name|data
operator|.
name|readSignedShort
argument_list|()
decl_stmt|;
name|int
name|idRangeOffset
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
operator|-
operator|(
name|maxSubHeaderIndex
operator|+
literal|1
operator|-
name|i
operator|-
literal|1
operator|)
operator|*
literal|8
operator|-
literal|2
decl_stmt|;
name|subHeaders
index|[
name|i
index|]
operator|=
operator|new
name|SubHeader
argument_list|(
name|firstCode
argument_list|,
name|entryCount
argument_list|,
name|idDelta
argument_list|,
name|idRangeOffset
argument_list|)
expr_stmt|;
block|}
name|long
name|startGlyphIndexOffset
init|=
name|data
operator|.
name|getCurrentPosition
argument_list|()
decl_stmt|;
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
name|numGlyphs
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|maxSubHeaderIndex
condition|;
operator|++
name|i
control|)
block|{
name|SubHeader
name|sh
init|=
name|subHeaders
index|[
name|i
index|]
decl_stmt|;
name|int
name|firstCode
init|=
name|sh
operator|.
name|getFirstCode
argument_list|()
decl_stmt|;
name|int
name|idRangeOffset
init|=
name|sh
operator|.
name|getIdRangeOffset
argument_list|()
decl_stmt|;
name|int
name|idDelta
init|=
name|sh
operator|.
name|getIdDelta
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|sh
operator|.
name|getEntryCount
argument_list|()
decl_stmt|;
name|data
operator|.
name|seek
argument_list|(
name|startGlyphIndexOffset
operator|+
name|idRangeOffset
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|entryCount
condition|;
operator|++
name|j
control|)
block|{
comment|// ---- compute the Character Code
name|int
name|charCode
init|=
name|i
decl_stmt|;
name|charCode
operator|=
operator|(
name|charCode
operator|<<
literal|8
operator|)
operator|+
operator|(
name|firstCode
operator|+
name|j
operator|)
expr_stmt|;
comment|// ---- Go to the CharacterCOde position in the Sub Array
comment|// of the glyphIndexArray
comment|// glyphIndexArray contains Unsigned Short so add (j * 2) bytes
comment|// at the index position
name|int
name|p
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
comment|// ---- compute the glyphIndex
if|if
condition|(
name|p
operator|>
literal|0
condition|)
block|{
name|p
operator|=
operator|(
name|p
operator|+
name|idDelta
operator|)
operator|%
literal|65536
expr_stmt|;
block|}
name|glyphIdToCharacterCode
index|[
name|p
index|]
operator|=
name|charCode
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
name|charCode
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Initialize the CMapEntry when it is a subtype 0.      *       * @param data the data stream of the to be parsed ttf font      * @throws IOException If there is an error parsing the true type font.      */
specifier|protected
name|void
name|processSubtype0
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|glyphMapping
init|=
name|data
operator|.
name|read
argument_list|(
literal|256
argument_list|)
decl_stmt|;
name|glyphIdToCharacterCode
operator|=
name|newGlyphIdToCharacterCode
argument_list|(
literal|256
argument_list|)
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
name|glyphMapping
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|glyphIndex
init|=
operator|(
name|glyphMapping
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
decl_stmt|;
name|glyphIdToCharacterCode
index|[
name|glyphIndex
index|]
operator|=
name|i
expr_stmt|;
name|characterCodeToGlyphId
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|glyphIndex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Workaround for the fact that glyphIdToCharacterCode doesn't distinguish between      * missing character codes and code 0.      */
specifier|private
name|int
index|[]
name|newGlyphIdToCharacterCode
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|int
index|[]
name|gidToCode
init|=
operator|new
name|int
index|[
name|size
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|gidToCode
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|gidToCode
return|;
block|}
comment|/**      * @return Returns the platformEncodingId.      */
specifier|public
name|int
name|getPlatformEncodingId
parameter_list|()
block|{
return|return
name|platformEncodingId
return|;
block|}
comment|/**      * @param platformEncodingIdValue The platformEncodingId to set.      */
specifier|public
name|void
name|setPlatformEncodingId
parameter_list|(
name|int
name|platformEncodingIdValue
parameter_list|)
block|{
name|platformEncodingId
operator|=
name|platformEncodingIdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the platformId.      */
specifier|public
name|int
name|getPlatformId
parameter_list|()
block|{
return|return
name|platformId
return|;
block|}
comment|/**      * @param platformIdValue The platformId to set.      */
specifier|public
name|void
name|setPlatformId
parameter_list|(
name|int
name|platformIdValue
parameter_list|)
block|{
name|platformId
operator|=
name|platformIdValue
expr_stmt|;
block|}
comment|/**      * Returns the GlyphId linked with the given character code.      *      * @param characterCode the given character code to be mapped      * @return glyphId the corresponding glyph id for the given character code      */
specifier|public
name|int
name|getGlyphId
parameter_list|(
name|int
name|characterCode
parameter_list|)
block|{
name|Integer
name|glyphId
init|=
name|characterCodeToGlyphId
operator|.
name|get
argument_list|(
name|characterCode
argument_list|)
decl_stmt|;
return|return
name|glyphId
operator|==
literal|null
condition|?
literal|0
else|:
name|glyphId
return|;
block|}
comment|/**      * Returns the character code for the given GID, or null if there is none.      *      * @param gid glyph id      * @return character code      */
specifier|public
name|Integer
name|getCharacterCode
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
if|if
condition|(
name|gid
operator|<
literal|0
operator|||
name|gid
operator|>=
name|glyphIdToCharacterCode
operator|.
name|length
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// workaround for the fact that glyphIdToCharacterCode doesn't distinguish between
comment|// missing character codes and code 0.
name|int
name|code
init|=
name|glyphIdToCharacterCode
index|[
name|gid
index|]
decl_stmt|;
if|if
condition|(
name|code
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|code
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"{"
operator|+
name|getPlatformId
argument_list|()
operator|+
literal|" "
operator|+
name|getPlatformEncodingId
argument_list|()
operator|+
literal|"}"
return|;
block|}
comment|/**      *       * Class used to manage CMap - Format 2.      *       */
specifier|private
class|class
name|SubHeader
block|{
specifier|private
specifier|final
name|int
name|firstCode
decl_stmt|;
specifier|private
specifier|final
name|int
name|entryCount
decl_stmt|;
comment|/**          * used to compute the GlyphIndex : P = glyphIndexArray.SubArray[pos] GlyphIndex = P + idDelta % 65536.          */
specifier|private
specifier|final
name|short
name|idDelta
decl_stmt|;
comment|/**          * Number of bytes to skip to reach the firstCode in the glyphIndexArray.          */
specifier|private
specifier|final
name|int
name|idRangeOffset
decl_stmt|;
specifier|private
name|SubHeader
parameter_list|(
name|int
name|firstCodeValue
parameter_list|,
name|int
name|entryCountValue
parameter_list|,
name|short
name|idDeltaValue
parameter_list|,
name|int
name|idRangeOffsetValue
parameter_list|)
block|{
name|firstCode
operator|=
name|firstCodeValue
expr_stmt|;
name|entryCount
operator|=
name|entryCountValue
expr_stmt|;
name|idDelta
operator|=
name|idDeltaValue
expr_stmt|;
name|idRangeOffset
operator|=
name|idRangeOffsetValue
expr_stmt|;
block|}
comment|/**          * @return the firstCode          */
specifier|private
name|int
name|getFirstCode
parameter_list|()
block|{
return|return
name|firstCode
return|;
block|}
comment|/**          * @return the entryCount          */
specifier|private
name|int
name|getEntryCount
parameter_list|()
block|{
return|return
name|entryCount
return|;
block|}
comment|/**          * @return the idDelta          */
specifier|private
name|short
name|getIdDelta
parameter_list|()
block|{
return|return
name|idDelta
return|;
block|}
comment|/**          * @return the idRangeOffset          */
specifier|private
name|int
name|getIdRangeOffset
parameter_list|()
block|{
return|return
name|idRangeOffset
return|;
block|}
block|}
block|}
end_class

end_unit

