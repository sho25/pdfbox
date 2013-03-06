begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
operator|.
name|container
package|;
end_package

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
name|pdmodel
operator|.
name|font
operator|.
name|PDFont
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
name|preflight
operator|.
name|font
operator|.
name|util
operator|.
name|CIDToGIDMap
import|;
end_import

begin_class
specifier|public
class|class
name|CIDType2Container
extends|extends
name|FontContainer
block|{
specifier|protected
name|CIDToGIDMap
name|cidToGid
init|=
literal|null
decl_stmt|;
specifier|protected
name|TrueTypeFont
name|ttf
init|=
literal|null
decl_stmt|;
specifier|public
name|CIDType2Container
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|float
name|getFontProgramWidth
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
name|float
name|foundWidth
init|=
operator|-
literal|1
decl_stmt|;
specifier|final
name|int
name|glyphIndex
init|=
name|getGlyphIndex
argument_list|(
name|cid
argument_list|)
decl_stmt|;
comment|// if glyph exists we can check the width
if|if
condition|(
name|this
operator|.
name|ttf
operator|!=
literal|null
operator|&&
name|this
operator|.
name|ttf
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyphs
argument_list|()
operator|.
name|length
operator|>
name|glyphIndex
condition|)
block|{
comment|/*              * In a Mono space font program, the length of the AdvanceWidth array must be one. According to the TrueType              * font specification, the Last Value of the AdvanceWidth array is apply to the subsequent glyphs. So if the              * GlyphId is greater than the length of the array the last entry is used.              */
name|int
name|numberOfLongHorMetrics
init|=
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
operator|.
name|getNumberOfHMetrics
argument_list|()
decl_stmt|;
name|int
name|unitsPerEm
init|=
name|ttf
operator|.
name|getHeader
argument_list|()
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|int
index|[]
name|advanceGlyphWidths
init|=
name|ttf
operator|.
name|getHorizontalMetrics
argument_list|()
operator|.
name|getAdvanceWidth
argument_list|()
decl_stmt|;
name|float
name|glypdWidth
init|=
name|advanceGlyphWidths
index|[
name|numberOfLongHorMetrics
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|glyphIndex
operator|<
name|numberOfLongHorMetrics
condition|)
block|{
name|glypdWidth
operator|=
name|advanceGlyphWidths
index|[
name|glyphIndex
index|]
expr_stmt|;
block|}
name|foundWidth
operator|=
operator|(
operator|(
name|glypdWidth
operator|*
literal|1000
operator|)
operator|/
name|unitsPerEm
operator|)
expr_stmt|;
block|}
return|return
name|foundWidth
return|;
block|}
comment|/**      * If CIDToGID map is Identity, the GID equals to the CID. Otherwise the conversion is done by the CIDToGID map      *       * @param cid      * @return -1 CID doesn't match with a GID      */
specifier|private
name|int
name|getGlyphIndex
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
name|int
name|glyphIndex
init|=
name|cid
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|cidToGid
operator|!=
literal|null
condition|)
block|{
name|glyphIndex
operator|=
name|cidToGid
operator|.
name|getGID
argument_list|(
name|cid
argument_list|)
expr_stmt|;
if|if
condition|(
name|glyphIndex
operator|==
name|cidToGid
operator|.
name|NOTDEF_GLYPH_INDEX
condition|)
block|{
name|glyphIndex
operator|=
operator|-
literal|14
expr_stmt|;
block|}
block|}
return|return
name|glyphIndex
return|;
block|}
specifier|public
name|void
name|setCidToGid
parameter_list|(
name|CIDToGIDMap
name|cidToGid
parameter_list|)
block|{
name|this
operator|.
name|cidToGid
operator|=
name|cidToGid
expr_stmt|;
block|}
specifier|public
name|void
name|setTtf
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
block|{
name|this
operator|.
name|ttf
operator|=
name|ttf
expr_stmt|;
block|}
block|}
end_class

end_unit

