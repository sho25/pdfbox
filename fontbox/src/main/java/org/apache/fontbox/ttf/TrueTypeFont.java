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
name|GeneralPath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|ArrayList
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
name|Collection
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|FontBoxFont
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
name|model
operator|.
name|GsubData
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
name|FontBoxFont
implements|,
name|Closeable
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
name|TrueTypeFont
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|TTFDataStream
name|data
decl_stmt|;
specifier|private
specifier|volatile
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|postScriptNames
decl_stmt|;
specifier|private
specifier|final
name|Object
name|lockReadtable
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Object
name|lockPSNames
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|enabledGsubFeatures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
annotation|@
name|Override
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
comment|/**      * Returns the raw bytes of the given table.      * @param table the table to read.      * @throws IOException if there was an error accessing the table.      */
specifier|public
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
synchronized|synchronized
init|(
name|lockReadtable
init|)
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
block|}
comment|/**      * This will get the table for the given tag.      *       * @param tag the name of the table to be returned      * @return The table with the given tag.      * @throws IOException if there was an error reading the table.      */
specifier|protected
name|TTFTable
name|getTable
parameter_list|(
name|String
name|tag
parameter_list|)
throws|throws
name|IOException
block|{
comment|// after the initial parsing of the ttf there aren't any write operations
comment|// to the HashMap anymore, so that we don't have to synchronize the read access
name|TTFTable
name|ttfTable
init|=
name|tables
operator|.
name|get
argument_list|(
name|tag
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttfTable
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|ttfTable
operator|.
name|initialized
condition|)
block|{
synchronized|synchronized
init|(
name|lockReadtable
init|)
block|{
if|if
condition|(
operator|!
name|ttfTable
operator|.
name|initialized
condition|)
block|{
name|readTable
argument_list|(
name|ttfTable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|ttfTable
return|;
block|}
comment|/**      * This will get the naming table for the true type font.      *       * @return The naming table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|NamingTable
name|getNaming
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|NamingTable
operator|)
name|getTable
argument_list|(
name|NamingTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the postscript table for this TTF.      *       * @return The postscript table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|PostScriptTable
name|getPostScript
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|PostScriptTable
operator|)
name|getTable
argument_list|(
name|PostScriptTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the OS/2 table for this TTF.      *       * @return The OS/2 table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|OS2WindowsMetricsTable
name|getOS2Windows
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|OS2WindowsMetricsTable
operator|)
name|getTable
argument_list|(
name|OS2WindowsMetricsTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the maxp table for this TTF.      *       * @return The maxp table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|MaximumProfileTable
name|getMaximumProfile
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|MaximumProfileTable
operator|)
name|getTable
argument_list|(
name|MaximumProfileTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the head table for this TTF.      *       * @return The head table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|HeaderTable
name|getHeader
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|HeaderTable
operator|)
name|getTable
argument_list|(
name|HeaderTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the hhea table for this TTF.      *       * @return The hhea table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|HorizontalHeaderTable
name|getHorizontalHeader
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|HorizontalHeaderTable
operator|)
name|getTable
argument_list|(
name|HorizontalHeaderTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the hmtx table for this TTF.      *       * @return The hmtx table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|HorizontalMetricsTable
name|getHorizontalMetrics
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|HorizontalMetricsTable
operator|)
name|getTable
argument_list|(
name|HorizontalMetricsTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the loca table for this TTF.      *       * @return The loca table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|IndexToLocationTable
name|getIndexToLocation
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|IndexToLocationTable
operator|)
name|getTable
argument_list|(
name|IndexToLocationTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the glyf table for this TTF.      *       * @return The glyf table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|GlyphTable
name|getGlyph
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|GlyphTable
operator|)
name|getTable
argument_list|(
name|GlyphTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the "cmap" table for this TTF.      *       * @return The "cmap" table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|CmapTable
name|getCmap
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|CmapTable
operator|)
name|getTable
argument_list|(
name|CmapTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the vhea table for this TTF.      *       * @return The vhea table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|VerticalHeaderTable
name|getVerticalHeader
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|VerticalHeaderTable
operator|)
name|getTable
argument_list|(
name|VerticalHeaderTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the vmtx table for this TTF.      *       * @return The vmtx table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|VerticalMetricsTable
name|getVerticalMetrics
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|VerticalMetricsTable
operator|)
name|getTable
argument_list|(
name|VerticalMetricsTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the VORG table for this TTF.      *       * @return The VORG table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|VerticalOriginTable
name|getVerticalOrigin
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|VerticalOriginTable
operator|)
name|getTable
argument_list|(
name|VerticalOriginTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the "kern" table for this TTF.      *       * @return The "kern" table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|KerningTable
name|getKerning
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|KerningTable
operator|)
name|getTable
argument_list|(
name|KerningTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the "gsub" table for this TTF.      *      * @return The "gsub" table or null if it doesn't exist.      * @throws IOException if there was an error reading the table.      */
specifier|public
name|GlyphSubstitutionTable
name|getGsub
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|GlyphSubstitutionTable
operator|)
name|getTable
argument_list|(
name|GlyphSubstitutionTable
operator|.
name|TAG
argument_list|)
return|;
block|}
comment|/**      * Get the data of the TrueType Font      * program representing the stream used to build this       * object (normally from the TTFParser object).      *       * @return COSStream TrueType font program stream      *       * @throws IOException If there is an error getting the font data.      */
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
comment|/**      * Get the data size of the TrueType Font program representing the stream used to build this      * object (normally from the TTFParser object).      *      * @return the size.      */
specifier|public
name|long
name|getOriginalDataSize
parameter_list|()
block|{
return|return
name|data
operator|.
name|getOriginalDataSize
argument_list|()
return|;
block|}
comment|/**      * Read the given table if necessary. Package-private, used by TTFParser only.      *       * @param table the table to be initialized      *       * @throws IOException if there was an error reading the table.      */
name|void
name|readTable
parameter_list|(
name|TTFTable
name|table
parameter_list|)
throws|throws
name|IOException
block|{
comment|// PDFBOX-4219: synchronize on data because it is accessed by several threads
comment|// when PDFBox is accessing a standard 14 font for the first time
synchronized|synchronized
init|(
name|data
init|)
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
block|}
comment|/**      * Returns the number of glyphs (MaximumProfile.numGlyphs).      *       * @return the number of glyphs      * @throws IOException if there was an error reading the table.      */
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
comment|/**      * Returns the units per EM (Header.unitsPerEm).      *       * @return units per EM      * @throws IOException if there was an error reading the table.      */
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
comment|/**      * Returns the width for the given GID.      *       * @param gid the GID      * @return the width      * @throws IOException if there was an error reading the metrics table.      */
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
comment|/**      * Returns the height for the given GID.      *       * @param gid the GID      * @return the height      * @throws IOException if there was an error reading the metrics table.      */
specifier|public
name|int
name|getAdvanceHeight
parameter_list|(
name|int
name|gid
parameter_list|)
throws|throws
name|IOException
block|{
name|VerticalMetricsTable
name|vmtx
init|=
name|getVerticalMetrics
argument_list|()
decl_stmt|;
if|if
condition|(
name|vmtx
operator|!=
literal|null
condition|)
block|{
return|return
name|vmtx
operator|.
name|getAdvanceHeight
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
name|void
name|readPostScriptNames
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|psnames
init|=
name|postScriptNames
decl_stmt|;
if|if
condition|(
name|psnames
operator|==
literal|null
condition|)
block|{
comment|// the getter is already synchronized
name|PostScriptTable
name|post
init|=
name|getPostScript
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|lockPSNames
init|)
block|{
name|psnames
operator|=
name|postScriptNames
expr_stmt|;
if|if
condition|(
name|psnames
operator|==
literal|null
condition|)
block|{
name|String
index|[]
name|names
init|=
name|post
operator|!=
literal|null
condition|?
name|post
operator|.
name|getGlyphNames
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|psnames
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|names
operator|.
name|length
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
name|names
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|psnames
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
else|else
block|{
name|psnames
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|postScriptNames
operator|=
name|psnames
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Returns the best Unicode from the font (the most general). The PDF spec says that "The means      * by which this is accomplished are implementation-dependent."      *       * @throws IOException if the font could not be read      * @deprecated Use {@link #getUnicodeCmapLookup()} instead      */
annotation|@
name|Deprecated
specifier|public
name|CmapSubtable
name|getUnicodeCmap
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getUnicodeCmap
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns the best Unicode from the font (the most general). The PDF spec says that "The means      * by which this is accomplished are implementation-dependent."      *       * @param isStrict False if we allow falling back to any cmap, even if it's not Unicode.      * @throws IOException if the font could not be read, or there is no Unicode cmap      * @deprecated Use {@link #getUnicodeCmapLookup(boolean)} instead      */
annotation|@
name|Deprecated
specifier|public
name|CmapSubtable
name|getUnicodeCmap
parameter_list|(
name|boolean
name|isStrict
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getUnicodeCmapImpl
argument_list|(
name|isStrict
argument_list|)
return|;
block|}
comment|/**      * Returns the best Unicode from the font (the most general). The PDF spec says that "The means      * by which this is accomplished are implementation-dependent."      *      * The returned cmap will perform glyph substitution.      *      * @throws IOException if the font could not be read      */
specifier|public
name|CmapLookup
name|getUnicodeCmapLookup
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getUnicodeCmapLookup
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns the best Unicode from the font (the most general). The PDF spec says that "The means      * by which this is accomplished are implementation-dependent."      *      * The returned cmap will perform glyph substitution.      *      * @param isStrict False if we allow falling back to any cmap, even if it's not Unicode.      * @throws IOException if the font could not be read, or there is no Unicode cmap      */
specifier|public
name|CmapLookup
name|getUnicodeCmapLookup
parameter_list|(
name|boolean
name|isStrict
parameter_list|)
throws|throws
name|IOException
block|{
name|CmapSubtable
name|cmap
init|=
name|getUnicodeCmapImpl
argument_list|(
name|isStrict
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|enabledGsubFeatures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|GlyphSubstitutionTable
name|table
init|=
name|getGsub
argument_list|()
decl_stmt|;
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SubstitutingCmapLookup
argument_list|(
name|cmap
argument_list|,
name|table
argument_list|,
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|enabledGsubFeatures
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|cmap
return|;
block|}
specifier|private
name|CmapSubtable
name|getUnicodeCmapImpl
parameter_list|(
name|boolean
name|isStrict
parameter_list|)
throws|throws
name|IOException
block|{
name|CmapTable
name|cmapTable
init|=
name|getCmap
argument_list|()
decl_stmt|;
if|if
condition|(
name|cmapTable
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isStrict
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"The TrueType font "
operator|+
name|getName
argument_list|()
operator|+
literal|" does not contain a 'cmap' table"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
name|PLATFORM_WINDOWS
argument_list|,
name|CmapTable
operator|.
name|ENCODING_WIN_UNICODE_FULL
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
if|if
condition|(
name|isStrict
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"The TrueType font does not contain a Unicode cmap"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|cmapTable
operator|.
name|getCmaps
argument_list|()
operator|.
name|length
operator|>
literal|0
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
block|}
return|return
name|cmap
return|;
block|}
comment|/**      * Returns the GID for the given PostScript name, if the "post" table is present.      * @param name the PostScript name.      */
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
if|if
condition|(
name|postScriptNames
operator|!=
literal|null
condition|)
block|{
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
name|CmapLookup
name|cmap
init|=
name|getUnicodeCmapLookup
argument_list|(
literal|false
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
specifier|public
name|GsubData
name|getGsubData
parameter_list|()
throws|throws
name|IOException
block|{
name|GlyphSubstitutionTable
name|table
init|=
name|getGsub
argument_list|()
decl_stmt|;
if|if
condition|(
name|table
operator|==
literal|null
condition|)
block|{
return|return
name|GsubData
operator|.
name|NO_DATA_FOUND
return|;
block|}
return|return
name|table
operator|.
name|getGsubData
argument_list|()
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
operator|||
name|codePoint
operator|>=
literal|0xE000
condition|)
comment|// disallowed code area
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
if|if
condition|(
name|unicode
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
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
comment|// must scaled by caller using FontMatrix
return|return
name|glyph
operator|.
name|getPath
argument_list|()
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
return|return
name|getAdvanceWidth
argument_list|(
name|gid
argument_list|)
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
name|List
argument_list|<
name|Number
argument_list|>
name|getFontMatrix
parameter_list|()
throws|throws
name|IOException
block|{
name|float
name|scale
init|=
literal|1000f
operator|/
name|getUnitsPerEm
argument_list|()
decl_stmt|;
return|return
name|Arrays
operator|.
expr|<
name|Number
operator|>
name|asList
argument_list|(
literal|0.001f
operator|*
name|scale
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0.001f
operator|*
name|scale
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Enable a particular glyph substitution feature. This feature might not be supported by the      * font, or might not be implemented in PDFBox yet.      *      * @param featureTag The GSUB feature to enable      */
specifier|public
name|void
name|enableGsubFeature
parameter_list|(
name|String
name|featureTag
parameter_list|)
block|{
name|enabledGsubFeatures
operator|.
name|add
argument_list|(
name|featureTag
argument_list|)
expr_stmt|;
block|}
comment|/**      * Disable a particular glyph substitution feature.      *      * @param featureTag The GSUB feature to disable      */
specifier|public
name|void
name|disableGsubFeature
parameter_list|(
name|String
name|featureTag
parameter_list|)
block|{
name|enabledGsubFeatures
operator|.
name|remove
argument_list|(
name|featureTag
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enable glyph substitutions for vertical writing.      */
specifier|public
name|void
name|enableVerticalSubstitutions
parameter_list|()
block|{
name|enableGsubFeature
argument_list|(
literal|"vrt2"
argument_list|)
expr_stmt|;
name|enableGsubFeature
argument_list|(
literal|"vert"
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error getting the NamingTable for the font"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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

