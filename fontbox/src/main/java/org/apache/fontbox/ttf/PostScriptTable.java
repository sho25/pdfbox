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
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield (ben@benlitchfield.com)  */
end_comment

begin_class
specifier|public
class|class
name|PostScriptTable
extends|extends
name|TTFTable
block|{
specifier|private
name|float
name|formatType
decl_stmt|;
specifier|private
name|float
name|italicAngle
decl_stmt|;
specifier|private
name|short
name|underlinePosition
decl_stmt|;
specifier|private
name|short
name|underlineThickness
decl_stmt|;
specifier|private
name|long
name|isFixedPitch
decl_stmt|;
specifier|private
name|long
name|minMemType42
decl_stmt|;
specifier|private
name|long
name|maxMemType42
decl_stmt|;
specifier|private
name|long
name|mimMemType1
decl_stmt|;
specifier|private
name|long
name|maxMemType1
decl_stmt|;
specifier|private
name|String
index|[]
name|glyphNames
init|=
literal|null
decl_stmt|;
comment|/**      * A tag that identifies this table type.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"post"
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
annotation|@
name|Override
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
name|formatType
operator|=
name|data
operator|.
name|read32Fixed
argument_list|()
expr_stmt|;
name|italicAngle
operator|=
name|data
operator|.
name|read32Fixed
argument_list|()
expr_stmt|;
name|underlinePosition
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|underlineThickness
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|isFixedPitch
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|minMemType42
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|maxMemType42
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|mimMemType1
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|maxMemType1
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
if|if
condition|(
name|formatType
operator|==
literal|1.0f
condition|)
block|{
comment|/*              * This TrueType font file contains exactly the 258 glyphs in the standard Macintosh TrueType.              */
name|glyphNames
operator|=
operator|new
name|String
index|[
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|WGL4Names
operator|.
name|MAC_GLYPH_NAMES
argument_list|,
literal|0
argument_list|,
name|glyphNames
argument_list|,
literal|0
argument_list|,
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|formatType
operator|==
literal|2.0f
condition|)
block|{
name|int
name|numGlyphs
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
index|[]
name|glyphNameIndex
init|=
operator|new
name|int
index|[
name|numGlyphs
index|]
decl_stmt|;
name|glyphNames
operator|=
operator|new
name|String
index|[
name|numGlyphs
index|]
expr_stmt|;
name|int
name|maxIndex
init|=
name|Integer
operator|.
name|MIN_VALUE
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
name|numGlyphs
condition|;
name|i
operator|++
control|)
block|{
name|int
name|index
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|glyphNameIndex
index|[
name|i
index|]
operator|=
name|index
expr_stmt|;
comment|// PDFBOX-808: Index numbers between 32768 and 65535 are
comment|// reserved for future use, so we should just ignore them
if|if
condition|(
name|index
operator|<=
literal|32767
condition|)
block|{
name|maxIndex
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxIndex
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
name|String
index|[]
name|nameArray
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|maxIndex
operator|>=
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
condition|)
block|{
name|nameArray
operator|=
operator|new
name|String
index|[
name|maxIndex
operator|-
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
operator|+
literal|1
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
name|maxIndex
operator|-
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|int
name|numberOfChars
init|=
name|data
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
name|nameArray
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readString
argument_list|(
name|numberOfChars
argument_list|)
expr_stmt|;
block|}
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
name|int
name|index
init|=
name|glyphNameIndex
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|index
operator|<
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
condition|)
block|{
name|glyphNames
index|[
name|i
index|]
operator|=
name|WGL4Names
operator|.
name|MAC_GLYPH_NAMES
index|[
name|index
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|index
operator|>=
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
operator|&&
name|index
operator|<=
literal|32767
condition|)
block|{
name|glyphNames
index|[
name|i
index|]
operator|=
name|nameArray
index|[
name|index
operator|-
name|WGL4Names
operator|.
name|NUMBER_OF_MAC_GLYPHS
index|]
expr_stmt|;
block|}
else|else
block|{
comment|// PDFBOX-808: Index numbers between 32768 and 65535 are
comment|// reserved for future use, so we should just ignore them
name|glyphNames
index|[
name|i
index|]
operator|=
literal|".undefined"
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|formatType
operator|==
literal|2.5f
condition|)
block|{
name|int
index|[]
name|glyphNameIndex
init|=
operator|new
name|int
index|[
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
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
operator|<
name|glyphNameIndex
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|offset
init|=
name|data
operator|.
name|readSignedByte
argument_list|()
decl_stmt|;
name|glyphNameIndex
index|[
name|i
index|]
operator|=
name|i
operator|+
literal|1
operator|+
name|offset
expr_stmt|;
block|}
name|glyphNames
operator|=
operator|new
name|String
index|[
name|glyphNameIndex
operator|.
name|length
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
name|glyphNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|WGL4Names
operator|.
name|MAC_GLYPH_NAMES
index|[
name|glyphNameIndex
index|[
name|i
index|]
index|]
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|glyphNames
index|[
name|i
index|]
operator|=
name|name
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|formatType
operator|==
literal|3.0f
condition|)
block|{
comment|// no postscript information is provided.
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the formatType.      */
specifier|public
name|float
name|getFormatType
parameter_list|()
block|{
return|return
name|formatType
return|;
block|}
comment|/**      * @param formatTypeValue The formatType to set.      */
specifier|public
name|void
name|setFormatType
parameter_list|(
name|float
name|formatTypeValue
parameter_list|)
block|{
name|this
operator|.
name|formatType
operator|=
name|formatTypeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the isFixedPitch.      */
specifier|public
name|long
name|getIsFixedPitch
parameter_list|()
block|{
return|return
name|isFixedPitch
return|;
block|}
comment|/**      * @param isFixedPitchValue The isFixedPitch to set.      */
specifier|public
name|void
name|setIsFixedPitch
parameter_list|(
name|long
name|isFixedPitchValue
parameter_list|)
block|{
name|this
operator|.
name|isFixedPitch
operator|=
name|isFixedPitchValue
expr_stmt|;
block|}
comment|/**      * @return Returns the italicAngle.      */
specifier|public
name|float
name|getItalicAngle
parameter_list|()
block|{
return|return
name|italicAngle
return|;
block|}
comment|/**      * @param italicAngleValue The italicAngle to set.      */
specifier|public
name|void
name|setItalicAngle
parameter_list|(
name|float
name|italicAngleValue
parameter_list|)
block|{
name|this
operator|.
name|italicAngle
operator|=
name|italicAngleValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxMemType1.      */
specifier|public
name|long
name|getMaxMemType1
parameter_list|()
block|{
return|return
name|maxMemType1
return|;
block|}
comment|/**      * @param maxMemType1Value The maxMemType1 to set.      */
specifier|public
name|void
name|setMaxMemType1
parameter_list|(
name|long
name|maxMemType1Value
parameter_list|)
block|{
name|this
operator|.
name|maxMemType1
operator|=
name|maxMemType1Value
expr_stmt|;
block|}
comment|/**      * @return Returns the maxMemType42.      */
specifier|public
name|long
name|getMaxMemType42
parameter_list|()
block|{
return|return
name|maxMemType42
return|;
block|}
comment|/**      * @param maxMemType42Value The maxMemType42 to set.      */
specifier|public
name|void
name|setMaxMemType42
parameter_list|(
name|long
name|maxMemType42Value
parameter_list|)
block|{
name|this
operator|.
name|maxMemType42
operator|=
name|maxMemType42Value
expr_stmt|;
block|}
comment|/**      * @return Returns the mimMemType1.      */
specifier|public
name|long
name|getMinMemType1
parameter_list|()
block|{
return|return
name|mimMemType1
return|;
block|}
comment|/**      * @param mimMemType1Value The mimMemType1 to set.      */
specifier|public
name|void
name|setMimMemType1
parameter_list|(
name|long
name|mimMemType1Value
parameter_list|)
block|{
name|this
operator|.
name|mimMemType1
operator|=
name|mimMemType1Value
expr_stmt|;
block|}
comment|/**      * @return Returns the minMemType42.      */
specifier|public
name|long
name|getMinMemType42
parameter_list|()
block|{
return|return
name|minMemType42
return|;
block|}
comment|/**      * @param minMemType42Value The minMemType42 to set.      */
specifier|public
name|void
name|setMinMemType42
parameter_list|(
name|long
name|minMemType42Value
parameter_list|)
block|{
name|this
operator|.
name|minMemType42
operator|=
name|minMemType42Value
expr_stmt|;
block|}
comment|/**      * @return Returns the underlinePosition.      */
specifier|public
name|short
name|getUnderlinePosition
parameter_list|()
block|{
return|return
name|underlinePosition
return|;
block|}
comment|/**      * @param underlinePositionValue The underlinePosition to set.      */
specifier|public
name|void
name|setUnderlinePosition
parameter_list|(
name|short
name|underlinePositionValue
parameter_list|)
block|{
name|this
operator|.
name|underlinePosition
operator|=
name|underlinePositionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the underlineThickness.      */
specifier|public
name|short
name|getUnderlineThickness
parameter_list|()
block|{
return|return
name|underlineThickness
return|;
block|}
comment|/**      * @param underlineThicknessValue The underlineThickness to set.      */
specifier|public
name|void
name|setUnderlineThickness
parameter_list|(
name|short
name|underlineThicknessValue
parameter_list|)
block|{
name|this
operator|.
name|underlineThickness
operator|=
name|underlineThicknessValue
expr_stmt|;
block|}
comment|/**      * @return Returns the glyphNames.      */
specifier|public
name|String
index|[]
name|getGlyphNames
parameter_list|()
block|{
return|return
name|glyphNames
return|;
block|}
comment|/**      * @param glyphNamesValue The glyphNames to set.      */
specifier|public
name|void
name|setGlyphNames
parameter_list|(
name|String
index|[]
name|glyphNamesValue
parameter_list|)
block|{
name|this
operator|.
name|glyphNames
operator|=
name|glyphNamesValue
expr_stmt|;
block|}
comment|/**      * @return Returns the glyph name.      */
specifier|public
name|String
name|getName
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
if|if
condition|(
name|gid
argument_list|<
literal|0
operator|||
name|glyphNames
operator|==
literal|null
operator|||
name|gid
argument_list|>
name|glyphNames
operator|.
name|length
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|glyphNames
index|[
name|gid
index|]
return|;
block|}
block|}
end_class

end_unit

