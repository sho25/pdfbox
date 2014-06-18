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
comment|/**  * A TrueType font file.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TrueTypeFont
block|{
specifier|private
specifier|final
name|Log
name|log
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
specifier|private
name|int
index|[]
name|advanceWidths
init|=
literal|null
decl_stmt|;
specifier|private
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
comment|/**      * This will get the naming table for the true type font.      *       * @return The naming table.      */
specifier|public
name|NamingTable
name|getNaming
parameter_list|()
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
name|PostScriptTable
name|getPostScript
parameter_list|()
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
name|OS2WindowsMetricsTable
name|getOS2Windows
parameter_list|()
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
name|MaximumProfileTable
name|getMaximumProfile
parameter_list|()
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
name|HeaderTable
name|getHeader
parameter_list|()
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
name|HorizontalHeaderTable
name|getHorizontalHeader
parameter_list|()
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
name|HorizontalMetricsTable
name|getHorizontalMetrics
parameter_list|()
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
name|IndexToLocationTable
name|getIndexToLocation
parameter_list|()
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
name|GlyphTable
name|getGlyph
parameter_list|()
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
comment|/**      * Get the cmap table for this TTF.      *       * @return The cmap table.      */
specifier|public
name|CMAPTable
name|getCMAP
parameter_list|()
block|{
name|CMAPTable
name|cmap
init|=
operator|(
name|CMAPTable
operator|)
name|tables
operator|.
name|get
argument_list|(
name|CMAPTable
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
block|{
try|try
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
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"An error occured when reading table "
operator|+
name|table
operator|.
name|getTag
argument_list|()
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the number of glyphs (MaximuProfile.numGlyphs).      *       * @return the number of glyphs      */
specifier|public
name|int
name|getNumberOfGlyphs
parameter_list|()
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
comment|/**      * Returns the width for the given glyph code.      *       * @param code the glyph code      * @return the width      */
specifier|public
name|int
name|getAdvanceWidth
parameter_list|(
name|int
name|code
parameter_list|)
block|{
if|if
condition|(
name|advanceWidths
operator|==
literal|null
condition|)
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
name|advanceWidths
operator|=
name|hmtx
operator|.
name|getAdvanceWidth
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// this should never happen
name|advanceWidths
operator|=
operator|new
name|int
index|[]
block|{
literal|250
block|}
expr_stmt|;
block|}
block|}
if|if
condition|(
name|advanceWidths
operator|.
name|length
operator|>
name|code
condition|)
block|{
return|return
name|advanceWidths
index|[
name|code
index|]
return|;
block|}
else|else
block|{
comment|// monospaced fonts may not have a width for every glyph
comment|// the last one is for subsequent glyphs
return|return
name|advanceWidths
index|[
name|advanceWidths
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
block|}
block|}
end_class

end_unit

