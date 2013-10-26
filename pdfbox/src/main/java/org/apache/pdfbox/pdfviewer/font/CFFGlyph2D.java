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
name|pdfviewer
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
name|LinkedHashMap
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
name|cff
operator|.
name|CFFFont
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
name|cff
operator|.
name|CharStringRenderer
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
name|encoding
operator|.
name|Encoding
import|;
end_import

begin_comment
comment|/**  * This class provides a glyph to GeneralPath conversion for CFF fonts.  *   */
end_comment

begin_class
specifier|public
class|class
name|CFFGlyph2D
implements|implements
name|Glyph2D
block|{
comment|/**      * Log instance.      */
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
name|CFFGlyph2D
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|HashMap
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
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|codeToGlyph
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
specifier|private
name|String
name|fontname
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *       */
specifier|public
name|CFFGlyph2D
parameter_list|(
name|CFFFont
name|cffFont
parameter_list|,
name|Encoding
name|encoding
parameter_list|)
block|{
name|fontname
operator|=
name|cffFont
operator|.
name|getName
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nameToCode
init|=
name|encoding
operator|!=
literal|null
condition|?
name|encoding
operator|.
name|getNameToCodeMap
argument_list|()
else|:
literal|null
decl_stmt|;
name|Collection
argument_list|<
name|CFFFont
operator|.
name|Mapping
argument_list|>
name|mappings
init|=
name|cffFont
operator|.
name|getMappings
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToNameMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CFFFont
operator|.
name|Mapping
name|mapping
range|:
name|mappings
control|)
block|{
name|codeToNameMap
operator|.
name|put
argument_list|(
name|mapping
operator|.
name|getCode
argument_list|()
argument_list|,
name|mapping
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CharStringRenderer
name|renderer
init|=
name|cffFont
operator|.
name|createRenderer
argument_list|()
decl_stmt|;
name|int
name|glyphId
init|=
literal|0
decl_stmt|;
for|for
control|(
name|CFFFont
operator|.
name|Mapping
name|mapping
range|:
name|mappings
control|)
block|{
name|GeneralPath
name|glyph
init|=
literal|null
decl_stmt|;
try|try
block|{
name|glyph
operator|=
name|renderer
operator|.
name|render
argument_list|(
name|mapping
operator|.
name|toType1Sequence
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"CFF glyph rendering fails!"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|glyph
operator|!=
literal|null
condition|)
block|{
name|glyphs
operator|.
name|put
argument_list|(
name|glyphId
argument_list|,
name|glyph
argument_list|)
expr_stmt|;
name|int
name|code
init|=
name|mapping
operator|.
name|getSID
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|mapping
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|nameToCode
operator|!=
literal|null
operator|&&
name|nameToCode
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|code
operator|=
name|nameToCode
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|codeToGlyph
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|glyphId
argument_list|)
expr_stmt|;
name|glyphId
operator|++
expr_stmt|;
block|}
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPathForGlyphId
parameter_list|(
name|int
name|glyphId
parameter_list|)
block|{
if|if
condition|(
name|glyphs
operator|.
name|containsKey
argument_list|(
name|glyphId
argument_list|)
condition|)
block|{
return|return
name|glyphs
operator|.
name|get
argument_list|(
name|glyphId
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|fontname
operator|+
literal|": glyph "
operator|+
name|glyphId
operator|+
literal|" not found!"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPathForCharactercode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
if|if
condition|(
name|codeToGlyph
operator|.
name|containsKey
argument_list|(
name|code
argument_list|)
condition|)
block|{
return|return
name|getPathForGlyphId
argument_list|(
name|codeToGlyph
operator|.
name|get
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|fontname
operator|+
literal|": glyphmapping for "
operator|+
name|code
operator|+
literal|" not found!"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|getNumberOfGlyphs
parameter_list|()
block|{
if|if
condition|(
name|glyphs
operator|!=
literal|null
condition|)
block|{
return|return
name|glyphs
operator|.
name|size
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
if|if
condition|(
name|glyphs
operator|!=
literal|null
condition|)
block|{
name|glyphs
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|codeToGlyph
operator|!=
literal|null
condition|)
block|{
name|codeToGlyph
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

