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

begin_comment
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield (ben@benlitchfield.com)  *   */
end_comment

begin_class
specifier|public
class|class
name|GlyphTable
extends|extends
name|TTFTable
block|{
comment|/**      * Tag to identify this table.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"glyf"
decl_stmt|;
specifier|private
name|GlyphData
index|[]
name|glyphs
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|read
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|IndexToLocationTable
name|loc
init|=
name|ttf
operator|.
name|getIndexToLocation
argument_list|()
decl_stmt|;
comment|// the glyph offsets
name|long
index|[]
name|offsets
init|=
name|loc
operator|.
name|getOffsets
argument_list|()
decl_stmt|;
comment|// number of glyphs
name|int
name|numGlyphs
init|=
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
decl_stmt|;
comment|// the end of the glyph table
comment|// should not be 0, but sometimes is, see PDFBOX-2044
comment|// structure of this table: see
comment|// https://developer.apple.com/fonts/TTRefMan/RM06/Chap6loca.html
name|long
name|endOfGlyphs
init|=
name|offsets
index|[
name|numGlyphs
index|]
decl_stmt|;
name|long
name|offset
init|=
name|getOffset
argument_list|()
decl_stmt|;
name|glyphs
operator|=
operator|new
name|GlyphData
index|[
name|numGlyphs
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
name|numGlyphs
condition|;
name|i
operator|++
control|)
block|{
comment|// end of glyphs reached?
if|if
condition|(
name|endOfGlyphs
operator|!=
literal|0
operator|&&
name|endOfGlyphs
operator|==
name|offsets
index|[
name|i
index|]
condition|)
block|{
break|break;
block|}
comment|// the current glyph isn't defined
comment|// if the next offset is equal or smaller to the current offset
if|if
condition|(
name|offsets
index|[
name|i
operator|+
literal|1
index|]
operator|<=
name|offsets
index|[
name|i
index|]
condition|)
block|{
continue|continue;
block|}
name|glyphs
index|[
name|i
index|]
operator|=
operator|new
name|GlyphData
argument_list|()
expr_stmt|;
name|data
operator|.
name|seek
argument_list|(
name|offset
operator|+
name|offsets
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|glyphs
index|[
name|i
index|]
operator|.
name|initData
argument_list|(
name|this
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
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
name|GlyphData
name|glyph
init|=
name|glyphs
index|[
name|i
index|]
decl_stmt|;
comment|// resolve composite glyphs
if|if
condition|(
name|glyph
operator|!=
literal|null
operator|&&
name|glyph
operator|.
name|getDescription
argument_list|()
operator|.
name|isComposite
argument_list|()
condition|)
block|{
name|glyph
operator|.
name|getDescription
argument_list|()
operator|.
name|resolve
argument_list|()
expr_stmt|;
block|}
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the glyphs.      */
specifier|public
name|GlyphData
index|[]
name|getGlyphs
parameter_list|()
block|{
return|return
name|glyphs
return|;
block|}
comment|/**      * @param glyphsValue The glyphs to set.      */
specifier|public
name|void
name|setGlyphs
parameter_list|(
name|GlyphData
index|[]
name|glyphsValue
parameter_list|)
block|{
name|glyphs
operator|=
name|glyphsValue
expr_stmt|;
block|}
block|}
end_class

end_unit

