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
name|Comparator
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
comment|/**  * A 'kern' table in a true type font.  *   * @author Glenn Adams  */
end_comment

begin_class
specifier|public
class|class
name|KerningSubtable
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
name|KerningSubtable
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// coverage field bit masks and values
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_HORIZONTAL
init|=
literal|0x0001
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_MINIMUMS
init|=
literal|0x0002
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_CROSS_STREAM
init|=
literal|0x0004
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_FORMAT
init|=
literal|0xFF00
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_HORIZONTAL_SHIFT
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_MINIMUMS_SHIFT
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_CROSS_STREAM_SHIFT
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|COVERAGE_FORMAT_SHIFT
init|=
literal|8
decl_stmt|;
comment|// true if horizontal kerning
specifier|private
name|boolean
name|horizontal
decl_stmt|;
comment|// true if minimum adjustment values (versus kerning values)
specifier|private
name|boolean
name|minimums
decl_stmt|;
comment|// true if cross-stream (block progression) kerning
specifier|private
name|boolean
name|crossStream
decl_stmt|;
comment|// format specific pair data
specifier|private
name|PairData
name|pairs
decl_stmt|;
name|KerningSubtable
parameter_list|()
block|{     }
comment|/**      * This will read the required data from the stream.      *       * @param data The stream to read the data from.      * @param version The version of the table to be read      * @throws IOException If there is an error reading the data.      */
name|void
name|read
parameter_list|(
name|TTFDataStream
name|data
parameter_list|,
name|int
name|version
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|version
operator|==
literal|0
condition|)
block|{
name|readSubtable0
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|version
operator|==
literal|1
condition|)
block|{
name|readSubtable1
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
comment|/**      * Determine if subtable is designated for use in horizontal writing modes and      * contains inline progression kerning pairs (not block progression "cross stream")      * kerning pairs.      *      * @return true if subtable is for horizontal kerning      */
specifier|public
name|boolean
name|isHorizontalKerning
parameter_list|()
block|{
return|return
name|isHorizontalKerning
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/**      * Determine if subtable is designated for use in horizontal writing modes, contains      * kerning pairs (as opposed to minimum pairs), and, if CROSS is true, then return      * cross stream designator; otherwise, if CROSS is false, return true if cross stream      * designator is false.      *      * @param cross if true, then return cross stream designator in horizontal modes      * @return true if subtable is for horizontal kerning in horizontal modes      */
specifier|public
name|boolean
name|isHorizontalKerning
parameter_list|(
name|boolean
name|cross
parameter_list|)
block|{
if|if
condition|(
operator|!
name|horizontal
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|minimums
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|cross
condition|)
block|{
return|return
name|crossStream
return|;
block|}
else|else
block|{
return|return
operator|!
name|crossStream
return|;
block|}
block|}
comment|/**      * Obtain kerning adjustments for GLYPHS sequence, where the      * Nth returned adjustment is associated with the Nth glyph      * and the succeeding non-zero glyph in the GLYPHS sequence.      *      * Kerning adjustments are returned in font design coordinates.      *      * @param glyphs a (possibly empty) array of glyph identifiers      * @return a (possibly empty) array of kerning adjustments      */
specifier|public
name|int
index|[]
name|getKerning
parameter_list|(
name|int
index|[]
name|glyphs
parameter_list|)
block|{
name|int
index|[]
name|kerning
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|pairs
operator|!=
literal|null
condition|)
block|{
name|int
name|ng
init|=
name|glyphs
operator|.
name|length
decl_stmt|;
name|kerning
operator|=
operator|new
name|int
index|[
name|ng
index|]
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
name|ng
condition|;
operator|++
name|i
control|)
block|{
name|int
name|l
init|=
name|glyphs
index|[
name|i
index|]
decl_stmt|;
name|int
name|r
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
name|i
operator|+
literal|1
init|;
name|k
operator|<
name|ng
condition|;
operator|++
name|k
control|)
block|{
name|int
name|g
init|=
name|glyphs
index|[
name|k
index|]
decl_stmt|;
if|if
condition|(
name|g
operator|>=
literal|0
condition|)
block|{
name|r
operator|=
name|g
expr_stmt|;
break|break;
block|}
block|}
name|kerning
index|[
name|i
index|]
operator|=
name|getKerning
argument_list|(
name|l
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No kerning subtable data available due to an unsupported kerning subtable version"
argument_list|)
expr_stmt|;
block|}
return|return
name|kerning
return|;
block|}
comment|/**      * Obtain kerning adjustment for glyph pair {L,R}.      *      * @param l left member of glyph pair      * @param r right member of glyph pair      * @return a (possibly zero) kerning adjustment      */
specifier|public
name|int
name|getKerning
parameter_list|(
name|int
name|l
parameter_list|,
name|int
name|r
parameter_list|)
block|{
if|if
condition|(
name|pairs
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No kerning subtable data available due to an unsupported kerning subtable version"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
return|return
name|pairs
operator|.
name|getKerning
argument_list|(
name|l
argument_list|,
name|r
argument_list|)
return|;
block|}
specifier|private
name|void
name|readSubtable0
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|version
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsupported kerning sub-table version: "
operator|+
name|version
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|length
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
operator|<
literal|6
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Kerning sub-table too short, got "
operator|+
name|length
operator|+
literal|" bytes, expect 6 or more."
argument_list|)
throw|;
block|}
name|int
name|coverage
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
if|if
condition|(
name|isBitsSet
argument_list|(
name|coverage
argument_list|,
name|COVERAGE_HORIZONTAL
argument_list|,
name|COVERAGE_HORIZONTAL_SHIFT
argument_list|)
condition|)
block|{
name|this
operator|.
name|horizontal
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isBitsSet
argument_list|(
name|coverage
argument_list|,
name|COVERAGE_MINIMUMS
argument_list|,
name|COVERAGE_MINIMUMS_SHIFT
argument_list|)
condition|)
block|{
name|this
operator|.
name|minimums
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isBitsSet
argument_list|(
name|coverage
argument_list|,
name|COVERAGE_CROSS_STREAM
argument_list|,
name|COVERAGE_CROSS_STREAM_SHIFT
argument_list|)
condition|)
block|{
name|this
operator|.
name|crossStream
operator|=
literal|true
expr_stmt|;
block|}
name|int
name|format
init|=
name|getBits
argument_list|(
name|coverage
argument_list|,
name|COVERAGE_FORMAT
argument_list|,
name|COVERAGE_FORMAT_SHIFT
argument_list|)
decl_stmt|;
if|if
condition|(
name|format
operator|==
literal|0
condition|)
block|{
name|readSubtable0Format0
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|format
operator|==
literal|2
condition|)
block|{
name|readSubtable0Format2
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Skipped kerning subtable due to an unsupported kerning subtable version: "
operator|+
name|format
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|readSubtable0Format0
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|pairs
operator|=
operator|new
name|PairData0Format0
argument_list|()
expr_stmt|;
name|pairs
operator|.
name|read
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readSubtable0Format2
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Kerning subtable format 2 not yet supported."
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readSubtable1
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Kerning subtable format 1 not yet supported."
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|boolean
name|isBitsSet
parameter_list|(
name|int
name|bits
parameter_list|,
name|int
name|mask
parameter_list|,
name|int
name|shift
parameter_list|)
block|{
return|return
name|getBits
argument_list|(
name|bits
argument_list|,
name|mask
argument_list|,
name|shift
argument_list|)
operator|!=
literal|0
return|;
block|}
specifier|private
specifier|static
name|int
name|getBits
parameter_list|(
name|int
name|bits
parameter_list|,
name|int
name|mask
parameter_list|,
name|int
name|shift
parameter_list|)
block|{
return|return
operator|(
name|bits
operator|&
name|mask
operator|)
operator|>>
name|shift
return|;
block|}
specifier|private
interface|interface
name|PairData
block|{
name|void
name|read
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|int
name|getKerning
parameter_list|(
name|int
name|l
parameter_list|,
name|int
name|r
parameter_list|)
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|PairData0Format0
implements|implements
name|Comparator
argument_list|<
name|int
index|[]
argument_list|>
implements|,
name|PairData
block|{
specifier|private
name|int
name|searchRange
decl_stmt|;
specifier|private
name|int
index|[]
index|[]
name|pairs
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|read
parameter_list|(
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|numPairs
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|searchRange
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
operator|/
literal|6
expr_stmt|;
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
name|pairs
operator|=
operator|new
name|int
index|[
name|numPairs
index|]
index|[
literal|3
index|]
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
name|numPairs
condition|;
operator|++
name|i
control|)
block|{
name|int
name|left
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|right
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|value
init|=
name|data
operator|.
name|readSignedShort
argument_list|()
decl_stmt|;
name|pairs
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|left
expr_stmt|;
name|pairs
index|[
name|i
index|]
index|[
literal|1
index|]
operator|=
name|right
expr_stmt|;
name|pairs
index|[
name|i
index|]
index|[
literal|2
index|]
operator|=
name|value
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getKerning
parameter_list|(
name|int
name|l
parameter_list|,
name|int
name|r
parameter_list|)
block|{
name|int
index|[]
name|key
init|=
operator|new
name|int
index|[]
block|{
name|l
block|,
name|r
block|,
literal|0
block|}
decl_stmt|;
name|int
name|index
init|=
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|pairs
argument_list|,
name|key
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
return|return
name|pairs
index|[
name|index
index|]
index|[
literal|2
index|]
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|int
index|[]
name|p1
parameter_list|,
name|int
index|[]
name|p2
parameter_list|)
block|{
assert|assert
name|p1
operator|!=
literal|null
assert|;
assert|assert
name|p1
operator|.
name|length
operator|>=
literal|2
assert|;
assert|assert
name|p2
operator|!=
literal|null
assert|;
assert|assert
name|p2
operator|.
name|length
operator|>=
literal|2
assert|;
name|int
name|cmp1
init|=
name|Integer
operator|.
name|compare
argument_list|(
name|p1
index|[
literal|0
index|]
argument_list|,
name|p2
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp1
operator|!=
literal|0
condition|)
block|{
return|return
name|cmp1
return|;
block|}
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|p1
index|[
literal|1
index|]
argument_list|,
name|p2
index|[
literal|1
index|]
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

