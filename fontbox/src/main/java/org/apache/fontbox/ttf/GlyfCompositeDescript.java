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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * Glyph description for composite glyphs.  Composite glyphs are made up of one  * or more simple glyphs, usually with some sort of transformation applied to each.  *  * This class is based on code from Apache Batik a subproject of Apache XMLGraphics.  * see http://xmlgraphics.apache.org/batik/ for further details.  */
end_comment

begin_class
specifier|public
class|class
name|GlyfCompositeDescript
extends|extends
name|GlyfDescript
block|{
specifier|private
name|List
argument_list|<
name|GlyfCompositeComp
argument_list|>
name|components
init|=
operator|new
name|ArrayList
argument_list|<
name|GlyfCompositeComp
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|GlyphData
index|[]
name|glyphs
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|beingResolved
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|resolved
init|=
literal|false
decl_stmt|;
comment|/**      * Constructor.      *       * @param bais the stream to be read      * @param glyphTable the Glyphtable containing all glyphs      * @throws IOException is thrown if something went wrong      */
specifier|public
name|GlyfCompositeDescript
parameter_list|(
name|TTFDataStream
name|bais
parameter_list|,
name|GlyphTable
name|glyphTable
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|glyphs
operator|=
name|glyphTable
operator|.
name|getGlyphs
argument_list|()
expr_stmt|;
comment|// Get all of the composite components
name|GlyfCompositeComp
name|comp
decl_stmt|;
do|do
block|{
name|comp
operator|=
operator|new
name|GlyfCompositeComp
argument_list|(
name|bais
argument_list|)
expr_stmt|;
name|components
operator|.
name|add
argument_list|(
name|comp
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
operator|(
name|comp
operator|.
name|getFlags
argument_list|()
operator|&
name|GlyfCompositeComp
operator|.
name|MORE_COMPONENTS
operator|)
operator|!=
literal|0
condition|)
do|;
comment|// Are there hinting instructions to read?
if|if
condition|(
operator|(
name|comp
operator|.
name|getFlags
argument_list|()
operator|&
name|GlyfCompositeComp
operator|.
name|WE_HAVE_INSTRUCTIONS
operator|)
operator|!=
literal|0
condition|)
block|{
name|readInstructions
argument_list|(
name|bais
argument_list|,
operator|(
name|bais
operator|.
name|read
argument_list|()
operator|<<
literal|8
operator||
name|bais
operator|.
name|read
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|resolve
parameter_list|()
block|{
if|if
condition|(
name|resolved
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|beingResolved
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Circular reference in GlyfCompositeDesc"
argument_list|)
expr_stmt|;
return|return;
block|}
name|beingResolved
operator|=
literal|true
expr_stmt|;
name|int
name|firstIndex
init|=
literal|0
decl_stmt|;
name|int
name|firstContour
init|=
literal|0
decl_stmt|;
name|Iterator
argument_list|<
name|GlyfCompositeComp
argument_list|>
name|i
init|=
name|components
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|GlyfCompositeComp
name|comp
init|=
operator|(
name|GlyfCompositeComp
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setFirstIndex
argument_list|(
name|firstIndex
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setFirstContour
argument_list|(
name|firstContour
argument_list|)
expr_stmt|;
name|GlyphDescription
name|desc
decl_stmt|;
name|desc
operator|=
name|getGlypDescription
argument_list|(
name|comp
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
name|desc
operator|.
name|resolve
argument_list|()
expr_stmt|;
name|firstIndex
operator|+=
name|desc
operator|.
name|getPointCount
argument_list|()
expr_stmt|;
name|firstContour
operator|+=
name|desc
operator|.
name|getContourCount
argument_list|()
expr_stmt|;
block|}
block|}
name|resolved
operator|=
literal|true
expr_stmt|;
name|beingResolved
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getEndPtOfContours
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
init|=
name|getCompositeCompEndPt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|gd
operator|.
name|getEndPtOfContours
argument_list|(
name|i
operator|-
name|c
operator|.
name|getFirstContour
argument_list|()
argument_list|)
operator|+
name|c
operator|.
name|getFirstIndex
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|byte
name|getFlags
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
init|=
name|getCompositeComp
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|gd
operator|.
name|getFlags
argument_list|(
name|i
operator|-
name|c
operator|.
name|getFirstIndex
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|short
name|getXCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
init|=
name|getCompositeComp
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|i
operator|-
name|c
operator|.
name|getFirstIndex
argument_list|()
decl_stmt|;
name|int
name|x
init|=
name|gd
operator|.
name|getXCoordinate
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|int
name|y
init|=
name|gd
operator|.
name|getYCoordinate
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|short
name|x1
init|=
operator|(
name|short
operator|)
name|c
operator|.
name|scaleX
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|x1
operator|+=
name|c
operator|.
name|getXTranslate
argument_list|()
expr_stmt|;
return|return
name|x1
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|short
name|getYCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
init|=
name|getCompositeComp
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|i
operator|-
name|c
operator|.
name|getFirstIndex
argument_list|()
decl_stmt|;
name|int
name|x
init|=
name|gd
operator|.
name|getXCoordinate
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|int
name|y
init|=
name|gd
operator|.
name|getYCoordinate
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|short
name|y1
init|=
operator|(
name|short
operator|)
name|c
operator|.
name|scaleY
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|y1
operator|+=
name|c
operator|.
name|getYTranslate
argument_list|()
expr_stmt|;
return|return
name|y1
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|isComposite
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getPointCount
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resolved
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"getPointCount called on unresolved GlyfCompositeDescript"
argument_list|)
expr_stmt|;
block|}
name|GlyfCompositeComp
name|c
init|=
operator|(
name|GlyfCompositeComp
operator|)
name|components
operator|.
name|get
argument_list|(
name|components
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|getFirstIndex
argument_list|()
operator|+
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
operator|.
name|getPointCount
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getContourCount
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resolved
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"getContourCount called on unresolved GlyfCompositeDescript"
argument_list|)
expr_stmt|;
block|}
name|GlyfCompositeComp
name|c
init|=
operator|(
name|GlyfCompositeComp
operator|)
name|components
operator|.
name|get
argument_list|(
name|components
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|getFirstContour
argument_list|()
operator|+
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
operator|.
name|getContourCount
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getComponentCount
parameter_list|()
block|{
return|return
name|components
operator|.
name|size
argument_list|()
return|;
block|}
specifier|private
name|GlyfCompositeComp
name|getCompositeComp
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|components
operator|.
name|size
argument_list|()
condition|;
name|n
operator|++
control|)
block|{
name|c
operator|=
operator|(
name|GlyfCompositeComp
operator|)
name|components
operator|.
name|get
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getFirstIndex
argument_list|()
operator|<=
name|i
operator|&&
name|i
operator|<
operator|(
name|c
operator|.
name|getFirstIndex
argument_list|()
operator|+
name|gd
operator|.
name|getPointCount
argument_list|()
operator|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|GlyfCompositeComp
name|getCompositeCompEndPt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|GlyfCompositeComp
name|c
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|components
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|c
operator|=
operator|(
name|GlyfCompositeComp
operator|)
name|components
operator|.
name|get
argument_list|(
name|j
argument_list|)
expr_stmt|;
name|GlyphDescription
name|gd
init|=
name|getGlypDescription
argument_list|(
name|c
operator|.
name|getGlyphIndex
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getFirstContour
argument_list|()
operator|<=
name|i
operator|&&
name|i
operator|<
operator|(
name|c
operator|.
name|getFirstContour
argument_list|()
operator|+
name|gd
operator|.
name|getContourCount
argument_list|()
operator|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|GlyphDescription
name|getGlypDescription
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|glyphs
operator|!=
literal|null
operator|&&
name|index
operator|<
name|glyphs
operator|.
name|length
condition|)
block|{
return|return
name|glyphs
index|[
name|index
index|]
operator|.
name|getDescription
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

