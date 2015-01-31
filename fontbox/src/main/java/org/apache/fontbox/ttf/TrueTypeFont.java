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
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|GeneralPath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|fontbox
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
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
import|;
end_import

begin_comment
comment|/**  * A TrueType font file.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TrueTypeFont
implements|implements
name|Type1Equivalent
block|{
specifier|private
name|float
name|version
decl_stmt|;
specifier|private
name|int
name|numberOfGlyphs
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|unitsPerEm
init|=
operator|-
literal|1
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|TTFTable
argument_list|>
name|tables
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TTFTable
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|TTFDataStream
name|data
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|postScriptNames
decl_stmt|;
comment|/**      * Constructor.  Clients should use the TTFParser to create a new TrueTypeFont object.      *       * @param fontData The font data.      */
name|TrueTypeFont
parameter_list|(
name|TTFDataStream
name|fontData
parameter_list|)
block|{
name|data
operator|=
name|fontData
expr_stmt|;
block|}
comment|/**      * Close the underlying resources.      *       * @throws IOException If there is an error closing the resources.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|data
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Returns the version.      */
specifier|public
name|float
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * Set the version. Package-private, used by TTFParser only.      * @param versionValue The version to set.      */
name|void
name|setVersion
parameter_list|(
name|float
name|versionValue
parameter_list|)
block|{
name|version
operator|=
name|versionValue
expr_stmt|;
block|}
comment|/**      * Add a table definition. Package-private, used by TTFParser only.      *       * @param table The table to add.      */
name|void
name|addTable
parameter_list|(
name|TTFTable
name|table
parameter_list|)
block|{
name|tables
operator|.
name|put
argument_list|(
name|table
operator|.
name|getTag
argument_list|()
argument_list|,
name|table
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get all of the tables.      *       * @return All of the tables.      */
specifier|public
name|Collection
argument_list|<
name|TTFTable
argument_list|>
name|getTables
parameter_list|()
block|{
return|return
name|tables
operator|.
name|values
argument_list|()
return|;
block|}
comment|/**      * Get all of the tables.      *      * @return All of the tables.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|TTFTable
argument_list|>
name|getTableMap
parameter_list|()
block|{
return|return
name|tables
return|;
block|}
comment|/**      * Returns the war bytes of the given table.      */
specifier|public
specifier|synchronized
name|byte
index|[]
name|getTableBytes
parameter_list|(
name|TTFTable
name|table
parameter_list|)
throws|throws
name|IOException
block|{
comment|// save current position
name|long
name|currentPosition
init|=
name|data
operator|.
name|getCurrentPosition
argument_list|()
decl_stmt|;
name|data
operator|.
name|seek
argument_list|(
name|table
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
comment|// read all data
name|byte
index|[]
name|bytes
init|=
name|data
operator|.
name|read
argument_list|(
operator|(
name|int
operator|)
name|table
operator|.
name|getLength
argument_list|()
argument_list|)
decl_stmt|;
comment|// restore current position
name|data
operator|.
name|seek
argument_list|(
name|currentPosition
argument_list|)
expr_stmt|;
return|return
name|bytes
return|;
block|}
comment|/**      * This will get the naming table for the true type font.      *       * @return The naming table.      */
specifier|public
specifier|synchronized
name|NamingTable
name|getNaming
parameter_list|()
throws|throws
name|IOException
block|{
name|NamingTable
name|naming
init|=
operator|(
name|NamingTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|NamingTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|naming
operator|!=
literal|null
operator|&&
operator|!
name|naming
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|naming
argument_list|)
expr_stmt|;
block|}
return|return
name|naming
return|;
block|}
comment|/**      * Get the postscript table for this TTF.      *       * @return The postscript table.      */
specifier|public
specifier|synchronized
name|PostScriptTable
name|getPostScript
parameter_list|()
throws|throws
name|IOException
block|{
name|PostScriptTable
name|postscript
init|=
operator|(
name|PostScriptTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|PostScriptTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|postscript
operator|!=
literal|null
operator|&&
operator|!
name|postscript
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|postscript
argument_list|)
expr_stmt|;
block|}
return|return
name|postscript
return|;
block|}
comment|/**      * Get the OS/2 table for this TTF.      *       * @return The OS/2 table.      */
specifier|public
specifier|synchronized
name|OS2WindowsMetricsTable
name|getOS2Windows
parameter_list|()
throws|throws
name|IOException
block|{
name|OS2WindowsMetricsTable
name|os2WindowsMetrics
init|=
operator|(
name|OS2WindowsMetricsTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|OS2WindowsMetricsTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|os2WindowsMetrics
operator|!=
literal|null
operator|&&
operator|!
name|os2WindowsMetrics
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|os2WindowsMetrics
argument_list|)
expr_stmt|;
block|}
return|return
name|os2WindowsMetrics
return|;
block|}
comment|/**      * Get the maxp table for this TTF.      *       * @return The maxp table.      */
specifier|public
specifier|synchronized
name|MaximumProfileTable
name|getMaximumProfile
parameter_list|()
throws|throws
name|IOException
block|{
name|MaximumProfileTable
name|maximumProfile
init|=
operator|(
name|MaximumProfileTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|MaximumProfileTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|maximumProfile
operator|!=
literal|null
operator|&&
operator|!
name|maximumProfile
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|maximumProfile
argument_list|)
expr_stmt|;
block|}
return|return
name|maximumProfile
return|;
block|}
comment|/**      * Get the head table for this TTF.      *       * @return The head table.      */
specifier|public
specifier|synchronized
name|HeaderTable
name|getHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|HeaderTable
name|header
init|=
operator|(
name|HeaderTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|HeaderTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
operator|!
name|header
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
return|return
name|header
return|;
block|}
comment|/**      * Get the hhea table for this TTF.      *       * @return The hhea table.      */
specifier|public
specifier|synchronized
name|HorizontalHeaderTable
name|getHorizontalHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|HorizontalHeaderTable
name|horizontalHeader
init|=
operator|(
name|HorizontalHeaderTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|HorizontalHeaderTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|horizontalHeader
operator|!=
literal|null
operator|&&
operator|!
name|horizontalHeader
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|horizontalHeader
argument_list|)
expr_stmt|;
block|}
return|return
name|horizontalHeader
return|;
block|}
comment|/**      * Get the hmtx table for this TTF.      *       * @return The hmtx table.      */
specifier|public
specifier|synchronized
name|HorizontalMetricsTable
name|getHorizontalMetrics
parameter_list|()
throws|throws
name|IOException
block|{
name|HorizontalMetricsTable
name|horizontalMetrics
init|=
operator|(
name|HorizontalMetricsTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|HorizontalMetricsTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|horizontalMetrics
operator|!=
literal|null
operator|&&
operator|!
name|horizontalMetrics
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|horizontalMetrics
argument_list|)
expr_stmt|;
block|}
return|return
name|horizontalMetrics
return|;
block|}
comment|/**      * Get the loca table for this TTF.      *       * @return The loca table.      */
specifier|public
specifier|synchronized
name|IndexToLocationTable
name|getIndexToLocation
parameter_list|()
throws|throws
name|IOException
block|{
name|IndexToLocationTable
name|indexToLocation
init|=
operator|(
name|IndexToLocationTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|IndexToLocationTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexToLocation
operator|!=
literal|null
operator|&&
operator|!
name|indexToLocation
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|indexToLocation
argument_list|)
expr_stmt|;
block|}
return|return
name|indexToLocation
return|;
block|}
comment|/**      * Get the glyf table for this TTF.      *       * @return The glyf table.      */
specifier|public
specifier|synchronized
name|GlyphTable
name|getGlyph
parameter_list|()
throws|throws
name|IOException
block|{
name|GlyphTable
name|glyph
init|=
operator|(
name|GlyphTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|GlyphTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|glyph
operator|!=
literal|null
operator|&&
operator|!
name|glyph
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|glyph
argument_list|)
expr_stmt|;
block|}
return|return
name|glyph
return|;
block|}
comment|/**      * Get the "cmap" table for this TTF.      *       * @return The "cmap" table.      */
specifier|public
specifier|synchronized
name|CmapTable
name|getCmap
parameter_list|()
throws|throws
name|IOException
block|{
name|CmapTable
name|cmap
init|=
operator|(
name|CmapTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|CmapTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmap
operator|!=
literal|null
operator|&&
operator|!
name|cmap
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|readTable
argument_list|(
name|cmap
argument_list|)
expr_stmt|;
block|}
return|return
name|cmap
return|;
block|}
comment|/**      * This permit to get the data of the True Type Font      * program representing the stream used to build this       * object (normally from the TTFParser object).      *       * @return COSStream True type font program stream      *       * @throws IOException If there is an error getting the font data.      */
specifier|public
name|InputStream
name|getOriginalData
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|data
operator|.
name|getOriginalData
argument_list|()
return|;
block|}
comment|/**      * Read the given table if necessary. Package-private, used by TTFParser only.      *       * @param table the table to be initialized      */
name|void
name|readTable
parameter_list|(
name|TTFTable
name|table
parameter_list|)
throws|throws
name|IOException
block|{
comment|// save current position
name|long
name|currentPosition
init|=
name|data
operator|.
name|getCurrentPosition
argument_list|()
decl_stmt|;
name|data
operator|.
name|seek
argument_list|(
name|table
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|read
argument_list|(
name|this
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// restore current position
name|data
operator|.
name|seek
argument_list|(
name|currentPosition
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the number of glyphs (MaximuProfile.numGlyphs).      *       * @return the number of glyphs      */
specifier|public
name|int
name|getNumberOfGlyphs
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|numberOfGlyphs
operator|==
operator|-
literal|1
condition|)
block|{
name|MaximumProfileTable
name|maximumProfile
init|=
name|getMaximumProfile
argument_list|()
decl_stmt|;
if|if
condition|(
name|maximumProfile
operator|!=
literal|null
condition|)
block|{
name|numberOfGlyphs
operator|=
name|maximumProfile
operator|.
name|getNumGlyphs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// this should never happen
name|numberOfGlyphs
operator|=
literal|0
expr_stmt|;
block|}
block|}
return|return
name|numberOfGlyphs
return|;
block|}
comment|/**      * Returns the units per EM (Header.unitsPerEm).      *       * @return units per EM      */
specifier|public
name|int
name|getUnitsPerEm
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|unitsPerEm
operator|==
operator|-
literal|1
condition|)
block|{
name|HeaderTable
name|header
init|=
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|unitsPerEm
operator|=
name|header
operator|.
name|getUnitsPerEm
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// this should never happen
name|unitsPerEm
operator|=
literal|0
expr_stmt|;
block|}
block|}
return|return
name|unitsPerEm
return|;
block|}
comment|/**      * Returns the width for the given GID.      *       * @param gid the GID      * @return the width      */
specifier|public
name|int
name|getAdvanceWidth
parameter_list|(
name|int
name|gid
parameter_list|)
throws|throws
name|IOException
block|{
name|HorizontalMetricsTable
name|hmtx
init|=
name|getHorizontalMetrics
argument_list|()
decl_stmt|;
if|if
condition|(
name|hmtx
operator|!=
literal|null
condition|)
block|{
return|return
name|hmtx
operator|.
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
return|;
block|}
else|else
block|{
comment|// this should never happen
return|return
literal|250
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|getNaming
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getNaming
argument_list|()
operator|.
name|getPostScriptName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|readPostScriptNames
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|postScriptNames
operator|==
literal|null
condition|)
block|{
name|postScriptNames
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
if|if
condition|(
name|getPostScript
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|names
init|=
name|getPostScript
argument_list|()
operator|.
name|getGlyphNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|postScriptNames
operator|.
name|put
argument_list|(
name|names
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
block|}
block|}
comment|/**      * Returns the best Unicode from the font (the most general). The PDF spec says that "The means      * by which this is accomplished are implementation-dependent."      */
specifier|private
name|CmapSubtable
name|getUnicodeCmap
parameter_list|(
name|CmapTable
name|cmapTable
parameter_list|)
block|{
if|if
condition|(
name|cmapTable
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|CmapSubtable
name|cmap
init|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_UNICODE
argument_list|,
name|CmapTable
operator|.
name|ENCODING_UNICODE_2_0_FULL
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|cmap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_UNICODE
argument_list|,
name|CmapTable
operator|.
name|ENCODING_UNICODE_2_0_BMP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|cmap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_WINDOWS
argument_list|,
name|CmapTable
operator|.
name|ENCODING_WIN_UNICODE_BMP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
comment|// Microsoft's "Recommendations for OpenType Fonts" says that "Symbol" encoding
comment|// actually means "Unicode, non-standard character set"
name|cmap
operator|=
name|cmapTable
operator|.
name|getSubtable
argument_list|(
name|CmapTable
operator|.
name|PLATFORM_WINDOWS
argument_list|,
name|CmapTable
operator|.
name|ENCODING_WIN_SYMBOL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
comment|// fallback to the first cmap (may not be Unicode, so may produce poor results)
name|cmap
operator|=
name|cmapTable
operator|.
name|getCmaps
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|cmap
return|;
block|}
comment|/**      * Returns the GID for the given PostScript name, if the "post" table is present.      */
specifier|public
name|int
name|nameToGID
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
comment|// look up in 'post' table
name|readPostScriptNames
argument_list|()
expr_stmt|;
name|Integer
name|gid
init|=
name|postScriptNames
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|!=
literal|null
operator|&&
name|gid
operator|>
literal|0
operator|&&
name|gid
operator|<
name|getMaximumProfile
argument_list|()
operator|.
name|getNumGlyphs
argument_list|()
condition|)
block|{
return|return
name|gid
return|;
block|}
comment|// look up in 'cmap'
name|int
name|uni
init|=
name|parseUniName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|uni
operator|>
operator|-
literal|1
condition|)
block|{
name|CmapSubtable
name|cmap
init|=
name|getUnicodeCmap
argument_list|(
name|getCmap
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|cmap
operator|.
name|getGlyphId
argument_list|(
name|uni
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * Parses a Unicode PostScript name in the format uniXXXX.      */
specifier|private
name|int
name|parseUniName
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"uni"
argument_list|)
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|==
literal|7
condition|)
block|{
name|int
name|nameLength
init|=
name|name
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|uniStr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|chPos
init|=
literal|3
init|;
name|chPos
operator|+
literal|4
operator|<=
name|nameLength
condition|;
name|chPos
operator|+=
literal|4
control|)
block|{
name|int
name|codePoint
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|chPos
argument_list|,
name|chPos
operator|+
literal|4
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
if|if
condition|(
name|codePoint
operator|<=
literal|0xD7FF
operator|&&
name|codePoint
operator|>=
literal|0xE000
condition|)
block|{
name|uniStr
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|codePoint
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|unicode
init|=
name|uniStr
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|unicode
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|gid
init|=
name|nameToGID
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// some glyphs have no outlines (e.g. space, table, newline)
name|GlyphData
name|glyph
init|=
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|glyph
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
else|else
block|{
name|GeneralPath
name|path
init|=
name|glyph
operator|.
name|getPath
argument_list|()
decl_stmt|;
comment|// scale to 1000upem, per PostScript convention
name|float
name|scale
init|=
literal|1000f
operator|/
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|AffineTransform
name|atScale
init|=
name|AffineTransform
operator|.
name|getScaleInstance
argument_list|(
name|scale
argument_list|,
name|scale
argument_list|)
decl_stmt|;
name|path
operator|.
name|transform
argument_list|(
name|atScale
argument_list|)
expr_stmt|;
return|return
name|path
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|float
name|getWidth
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Integer
name|gid
init|=
name|nameToGID
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|width
init|=
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
decl_stmt|;
name|int
name|unitsPerEM
init|=
name|getUnitsPerEm
argument_list|()
decl_stmt|;
if|if
condition|(
name|unitsPerEM
operator|!=
literal|1000
condition|)
block|{
name|width
operator|*=
literal|1000f
operator|/
name|unitsPerEM
expr_stmt|;
block|}
return|return
name|width
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|nameToGID
argument_list|(
name|name
argument_list|)
operator|!=
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|Encoding
name|getEncoding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|BoundingBox
name|getFontBBox
parameter_list|()
throws|throws
name|IOException
block|{
name|short
name|xMin
init|=
name|getHeader
argument_list|()
operator|.
name|getXMin
argument_list|()
decl_stmt|;
name|short
name|xMax
init|=
name|getHeader
argument_list|()
operator|.
name|getXMax
argument_list|()
decl_stmt|;
name|short
name|yMin
init|=
name|getHeader
argument_list|()
operator|.
name|getYMin
argument_list|()
decl_stmt|;
name|short
name|yMax
init|=
name|getHeader
argument_list|()
operator|.
name|getYMax
argument_list|()
decl_stmt|;
name|float
name|scale
init|=
literal|1000f
operator|/
name|getUnitsPerEm
argument_list|()
decl_stmt|;
return|return
operator|new
name|BoundingBox
argument_list|(
name|xMin
operator|*
name|scale
argument_list|,
name|yMin
operator|*
name|scale
argument_list|,
name|xMax
operator|*
name|scale
argument_list|,
name|yMax
operator|*
name|scale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|getNaming
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getNaming
argument_list|()
operator|.
name|getPostScriptName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"(null)"
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|"(null - "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
block|}
end_class

end_unit

