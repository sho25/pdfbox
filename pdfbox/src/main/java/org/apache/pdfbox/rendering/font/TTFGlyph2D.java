begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*     Licensed to the Apache Software Foundation (ASF) under one or more    contributor license agreements.  See the NOTICE file distributed with    this work for additional information regarding copyright ownership.    The ASF licenses this file to You under the Apache License, Version 2.0    (the "License"); you may not use this file except in compliance with    the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0     Unless required by applicable law or agreed to in writing, software    distributed under the License is distributed on an "AS IS" BASIS,    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    See the License for the specific language governing permissions and    limitations under the License.   */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|rendering
operator|.
name|font
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|ttf
operator|.
name|GlyphData
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
name|HeaderTable
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
name|pdmodel
operator|.
name|font
operator|.
name|PDCIDFontType2
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
name|pdmodel
operator|.
name|font
operator|.
name|PDSimpleFont
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
name|PDTrueTypeFont
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
name|PDType0Font
import|;
end_import

begin_comment
comment|/**  * This class provides a glyph to GeneralPath conversion for TrueType fonts.  */
end_comment

begin_class
specifier|public
class|class
name|TTFGlyph2D
implements|implements
name|Glyph2D
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
name|TTFGlyph2D
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PDFont
name|font
decl_stmt|;
specifier|private
specifier|final
name|TrueTypeFont
name|ttf
decl_stmt|;
specifier|private
name|float
name|scale
init|=
literal|1.0f
decl_stmt|;
specifier|private
name|boolean
name|hasScaling
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|GeneralPath
argument_list|>
name|glyphs
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|GeneralPath
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isCIDFont
decl_stmt|;
comment|/**      * Constructor.      *      * @param ttfFont TrueType font      */
specifier|public
name|TTFGlyph2D
parameter_list|(
name|PDTrueTypeFont
name|ttfFont
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|ttfFont
operator|.
name|getTrueTypeFont
argument_list|()
argument_list|,
name|ttfFont
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param type0Font Type0 font, with CIDFontType2 descendant      */
specifier|public
name|TTFGlyph2D
parameter_list|(
name|PDType0Font
name|type0Font
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|(
operator|(
name|PDCIDFontType2
operator|)
name|type0Font
operator|.
name|getDescendantFont
argument_list|()
operator|)
operator|.
name|getTrueTypeFont
argument_list|()
argument_list|,
name|type0Font
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TTFGlyph2D
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|boolean
name|isCIDFont
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
name|this
operator|.
name|ttf
operator|=
name|ttf
expr_stmt|;
name|this
operator|.
name|isCIDFont
operator|=
name|isCIDFont
expr_stmt|;
comment|// get units per em, which is used as scaling factor
name|HeaderTable
name|header
init|=
name|this
operator|.
name|ttf
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
name|header
operator|.
name|getUnitsPerEm
argument_list|()
operator|!=
literal|1000
condition|)
block|{
comment|// in most case the scaling factor is set to 1.0f
comment|// due to the fact that units per em is set to 1000
name|scale
operator|=
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
expr_stmt|;
name|hasScaling
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPathForCharacterCode
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|gid
init|=
name|getGIDForCharacterCode
argument_list|(
name|code
argument_list|)
decl_stmt|;
return|return
name|getPathForGID
argument_list|(
name|gid
argument_list|,
name|code
argument_list|)
return|;
block|}
comment|// Try to map the given code to the corresponding glyph-ID
specifier|private
name|int
name|getGIDForCharacterCode
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
return|return
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|codeToGID
argument_list|(
name|code
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
operator|(
name|PDTrueTypeFont
operator|)
name|font
operator|)
operator|.
name|codeToGID
argument_list|(
name|code
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the path describing the glyph for the given glyphId.      *      * @param gid the GID      * @param code the character code      *      * @return the GeneralPath for the given glyphId      */
specifier|public
name|GeneralPath
name|getPathForGID
parameter_list|(
name|int
name|gid
parameter_list|,
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|GeneralPath
name|glyphPath
decl_stmt|;
if|if
condition|(
name|glyphs
operator|.
name|containsKey
argument_list|(
name|gid
argument_list|)
condition|)
block|{
name|glyphPath
operator|=
name|glyphs
operator|.
name|get
argument_list|(
name|gid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|gid
operator|==
literal|0
operator|||
name|gid
operator|>=
name|ttf
operator|.
name|getMaximumProfile
argument_list|()
operator|.
name|getNumGlyphs
argument_list|()
condition|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
name|int
name|cid
init|=
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|String
name|cidHex
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%04x"
argument_list|,
name|cid
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"No glyph for "
operator|+
name|code
operator|+
literal|" (CID "
operator|+
name|cidHex
operator|+
literal|") in font "
operator|+
name|font
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No glyph for "
operator|+
name|code
operator|+
literal|" in font "
operator|+
name|font
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ------
name|GlyphData
name|glyph
init|=
name|ttf
operator|.
name|getGlyph
argument_list|()
operator|.
name|getGlyph
argument_list|(
name|gid
argument_list|)
decl_stmt|;
comment|// workaround for Type0 "Standard 14" font handling, as Adobe has GID 0 as empty
comment|// while Microsoft uses a rectangle, which we don't want to appear
if|if
condition|(
name|isCIDFont
operator|&&
name|gid
operator|==
literal|0
operator|&&
operator|!
name|font
operator|.
name|isEmbedded
argument_list|()
operator|&&
name|PDSimpleFont
operator|.
name|isStandard14
argument_list|(
name|font
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|glyph
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|glyph
operator|==
literal|null
condition|)
block|{
comment|// empty glyph (e.g. space, newline)
name|glyphPath
operator|=
operator|new
name|GeneralPath
argument_list|()
expr_stmt|;
name|glyphs
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|glyphPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|glyphPath
operator|=
name|glyph
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|hasScaling
condition|)
block|{
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
name|glyphPath
operator|.
name|transform
argument_list|(
name|atScale
argument_list|)
expr_stmt|;
block|}
name|glyphs
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|glyphPath
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|glyphPath
operator|!=
literal|null
condition|?
operator|(
name|GeneralPath
operator|)
name|glyphPath
operator|.
name|clone
argument_list|()
else|:
literal|null
return|;
comment|// todo: expensive
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|glyphs
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

