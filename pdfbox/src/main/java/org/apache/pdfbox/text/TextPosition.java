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
name|pdfbox
operator|.
name|text
package|;
end_package

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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * This represents a string and a position on the screen of those characters.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TextPosition
block|{
comment|// text matrix for the start of the text object, coordinates are in display units
comment|// and have not been adjusted
specifier|private
specifier|final
name|Matrix
name|textPos
decl_stmt|;
comment|// ending X and Y coordinates in display units
specifier|private
specifier|final
name|float
name|endX
decl_stmt|;
specifier|private
specifier|final
name|float
name|endY
decl_stmt|;
specifier|private
specifier|final
name|float
name|maxTextHeight
decl_stmt|;
comment|// maximum height of text, in display units
specifier|private
specifier|final
name|int
name|rotation
decl_stmt|;
comment|// 0, 90, 180, 270 degrees of page rotation
specifier|private
specifier|final
name|float
name|x
init|=
name|Float
operator|.
name|NEGATIVE_INFINITY
decl_stmt|;
specifier|private
specifier|final
name|float
name|y
init|=
name|Float
operator|.
name|NEGATIVE_INFINITY
decl_stmt|;
specifier|private
specifier|final
name|float
name|pageHeight
decl_stmt|;
specifier|private
specifier|final
name|float
name|pageWidth
decl_stmt|;
specifier|private
specifier|final
name|float
name|widthOfSpace
decl_stmt|;
comment|// width of a space, in display units
specifier|private
specifier|final
name|int
index|[]
name|unicodeCP
decl_stmt|;
specifier|private
specifier|final
name|PDFont
name|font
decl_stmt|;
specifier|private
specifier|final
name|float
name|fontSize
decl_stmt|;
specifier|private
specifier|final
name|int
name|fontSizePt
decl_stmt|;
comment|// mutable
specifier|private
name|float
index|[]
name|widths
decl_stmt|;
specifier|private
name|String
name|string
decl_stmt|;
comment|/**      * Constructor.      *      * @param pageRotation rotation of the page that the text is located in      * @param pageWidthValue rotation of the page that the text is located in      * @param pageHeightValue rotation of the page that the text is located in      * @param textPositionSt TextMatrix for start of text (in display units)      * @param endXValue x coordinate of the end position      * @param endYValue y coordinate of the end position      * @param maxFontH Maximum height of text (in display units)      * @param individualWidth The width of the given character/string. (in ? units)      * @param spaceWidth The width of the space character. (in display units)      * @param string The character to be displayed.      * @param codePoints An array containing the codepoints of the given string.      * @param currentFont The current font for this text position.      * @param fontSizeValue The new font size.      * @param fontSizeInPt The font size in pt units.      */
specifier|public
name|TextPosition
parameter_list|(
name|int
name|pageRotation
parameter_list|,
name|float
name|pageWidthValue
parameter_list|,
name|float
name|pageHeightValue
parameter_list|,
name|Matrix
name|textPositionSt
parameter_list|,
name|float
name|endXValue
parameter_list|,
name|float
name|endYValue
parameter_list|,
name|float
name|maxFontH
parameter_list|,
name|float
name|individualWidth
parameter_list|,
name|float
name|spaceWidth
parameter_list|,
name|String
name|string
parameter_list|,
name|int
index|[]
name|codePoints
parameter_list|,
name|PDFont
name|currentFont
parameter_list|,
name|float
name|fontSizeValue
parameter_list|,
name|int
name|fontSizeInPt
parameter_list|)
block|{
name|this
operator|.
name|textPos
operator|=
name|textPositionSt
expr_stmt|;
name|this
operator|.
name|endX
operator|=
name|endXValue
expr_stmt|;
name|this
operator|.
name|endY
operator|=
name|endYValue
expr_stmt|;
name|int
name|rotation
init|=
name|pageRotation
decl_stmt|;
comment|// make sure it is 0 to 270 and no negative numbers
if|if
condition|(
name|rotation
operator|<
literal|0
condition|)
block|{
name|rotation
operator|+=
literal|360
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|>=
literal|360
condition|)
block|{
name|rotation
operator|-=
literal|360
expr_stmt|;
block|}
name|this
operator|.
name|rotation
operator|=
name|rotation
expr_stmt|;
name|this
operator|.
name|maxTextHeight
operator|=
name|maxFontH
expr_stmt|;
name|this
operator|.
name|pageHeight
operator|=
name|pageHeightValue
expr_stmt|;
name|this
operator|.
name|pageWidth
operator|=
name|pageWidthValue
expr_stmt|;
name|this
operator|.
name|widths
operator|=
operator|new
name|float
index|[]
block|{
name|individualWidth
block|}
expr_stmt|;
name|this
operator|.
name|widthOfSpace
operator|=
name|spaceWidth
expr_stmt|;
name|this
operator|.
name|string
operator|=
name|string
expr_stmt|;
name|this
operator|.
name|unicodeCP
operator|=
name|codePoints
expr_stmt|;
name|this
operator|.
name|font
operator|=
name|currentFont
expr_stmt|;
name|this
operator|.
name|fontSize
operator|=
name|fontSizeValue
expr_stmt|;
name|this
operator|.
name|fontSizePt
operator|=
name|fontSizeInPt
expr_stmt|;
block|}
comment|/**      * Return the string of characters stored in this object.      *      * @return The string on the screen.      */
specifier|public
name|String
name|getCharacter
parameter_list|()
block|{
return|return
name|string
return|;
block|}
comment|/**      * Return the codepoints of the characters stored in this object.      *      * @return an array containing all codepoints.      */
specifier|public
name|int
index|[]
name|getCodePoints
parameter_list|()
comment|// todo: NOT Unicode!!
block|{
return|return
name|unicodeCP
return|;
block|}
comment|/**      * Return the Matrix textPos stored in this object.      *      * @return The Matrix containing all infos of the starting textposition      */
specifier|public
name|Matrix
name|getTextPos
parameter_list|()
block|{
return|return
name|textPos
return|;
block|}
comment|/**      * Return the direction/orientation of the string in this object based on its text matrix.      * @return The direction of the text (0, 90, 180, or 270)      */
specifier|public
name|float
name|getDir
parameter_list|()
block|{
name|float
name|a
init|=
name|textPos
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|float
name|b
init|=
name|textPos
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|float
name|c
init|=
name|textPos
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|float
name|d
init|=
name|textPos
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
comment|// 12 0   left to right
comment|// 0 12
if|if
condition|(
name|a
operator|>
literal|0
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|b
argument_list|)
operator|<
name|d
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|c
argument_list|)
operator|<
name|a
operator|&&
name|d
operator|>
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// -12 0   right to left (upside down)
comment|// 0 -12
elseif|else
if|if
condition|(
name|a
operator|<
literal|0
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|b
argument_list|)
operator|<
name|Math
operator|.
name|abs
argument_list|(
name|d
argument_list|)
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|c
argument_list|)
operator|<
name|Math
operator|.
name|abs
argument_list|(
name|a
argument_list|)
operator|&&
name|d
operator|<
literal|0
condition|)
block|{
return|return
literal|180
return|;
block|}
comment|// 0  12    up
comment|// -12 0
elseif|else
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|a
argument_list|)
operator|<
name|Math
operator|.
name|abs
argument_list|(
name|c
argument_list|)
operator|&&
name|b
operator|>
literal|0
operator|&&
name|c
operator|<
literal|0
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|d
argument_list|)
operator|<
name|b
condition|)
block|{
return|return
literal|90
return|;
block|}
comment|// 0  -12   down
comment|// 12 0
elseif|else
if|if
condition|(
name|Math
operator|.
name|abs
argument_list|(
name|a
argument_list|)
operator|<
name|c
operator|&&
name|b
argument_list|<
literal|0
operator|&&
name|c
argument_list|>
literal|0
operator|&&
name|Math
operator|.
name|abs
argument_list|(
name|d
argument_list|)
operator|<
name|Math
operator|.
name|abs
argument_list|(
name|b
argument_list|)
condition|)
block|{
return|return
literal|270
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * Return the X starting coordinate of the text, adjusted by the given rotation amount.      * The rotation adjusts where the 0,0 location is relative to the text.      *      * @param rotation Rotation to apply (0, 90, 180, or 270).  0 will perform no adjustments.      * @return X coordinate      */
specifier|private
name|float
name|getXRot
parameter_list|(
name|float
name|rotation
parameter_list|)
block|{
if|if
condition|(
name|rotation
operator|==
literal|0
condition|)
block|{
return|return
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|90
condition|)
block|{
return|return
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|180
condition|)
block|{
return|return
name|pageWidth
operator|-
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|270
condition|)
block|{
return|return
name|pageHeight
operator|-
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * This will get the page rotation adjusted x position of the character.      * This is adjusted based on page rotation so that the upper left is 0,0.      *      * @return The x coordinate of the character.      */
specifier|public
name|float
name|getX
parameter_list|()
block|{
if|if
condition|(
name|x
operator|==
name|Float
operator|.
name|NEGATIVE_INFINITY
condition|)
block|{
return|return
name|getXRot
argument_list|(
name|rotation
argument_list|)
return|;
block|}
return|return
name|x
return|;
block|}
comment|/**      * This will get the text direction adjusted x position of the character.      * This is adjusted based on text direction so that the first character      * in that direction is in the upper left at 0,0.      *      * @return The x coordinate of the text.      */
specifier|public
name|float
name|getXDirAdj
parameter_list|()
block|{
return|return
name|getXRot
argument_list|(
name|getDir
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will get the y position of the character with 0,0 in lower left.      * This will be adjusted by the given rotation.      *      * @param rotation Rotation to apply to text to adjust the 0,0 location (0,90,180,270)      * @return The y coordinate of the text      */
specifier|private
name|float
name|getYLowerLeftRot
parameter_list|(
name|float
name|rotation
parameter_list|)
block|{
if|if
condition|(
name|rotation
operator|==
literal|0
condition|)
block|{
return|return
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|90
condition|)
block|{
return|return
name|pageWidth
operator|-
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|180
condition|)
block|{
return|return
name|pageHeight
operator|-
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rotation
operator|==
literal|270
condition|)
block|{
return|return
name|textPos
operator|.
name|getValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
comment|/**      * This will get the y position of the text, adjusted so that 0,0 is upper left and it is      * adjusted based on the page rotation.      *      * @return The adjusted y coordinate of the character.      */
specifier|public
name|float
name|getY
parameter_list|()
block|{
if|if
condition|(
name|y
operator|==
name|Float
operator|.
name|NEGATIVE_INFINITY
condition|)
block|{
if|if
condition|(
name|rotation
operator|==
literal|0
operator|||
name|rotation
operator|==
literal|180
condition|)
block|{
return|return
name|pageHeight
operator|-
name|getYLowerLeftRot
argument_list|(
name|rotation
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|pageWidth
operator|-
name|getYLowerLeftRot
argument_list|(
name|rotation
argument_list|)
return|;
block|}
block|}
return|return
name|y
return|;
block|}
comment|/**      * This will get the y position of the text, adjusted so that 0,0 is upper left and it is      * adjusted based on the text direction.      *      * @return The adjusted y coordinate of the character.      */
specifier|public
name|float
name|getYDirAdj
parameter_list|()
block|{
name|float
name|dir
init|=
name|getDir
argument_list|()
decl_stmt|;
comment|// some PDFBox code assumes that the 0,0 point is in upper left, not lower left
if|if
condition|(
name|dir
operator|==
literal|0
operator|||
name|dir
operator|==
literal|180
condition|)
block|{
return|return
name|pageHeight
operator|-
name|getYLowerLeftRot
argument_list|(
name|dir
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|pageWidth
operator|-
name|getYLowerLeftRot
argument_list|(
name|dir
argument_list|)
return|;
block|}
block|}
comment|/**      * Get the length or width of the text, based on a given rotation.      *      * @param rotation Rotation that was used to determine coordinates (0,90,180,270)      * @return Width of text in display units      */
specifier|private
name|float
name|getWidthRot
parameter_list|(
name|float
name|rotation
parameter_list|)
block|{
if|if
condition|(
name|rotation
operator|==
literal|90
operator|||
name|rotation
operator|==
literal|270
condition|)
block|{
return|return
name|Math
operator|.
name|abs
argument_list|(
name|endY
operator|-
name|textPos
operator|.
name|getYPosition
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Math
operator|.
name|abs
argument_list|(
name|endX
operator|-
name|textPos
operator|.
name|getXPosition
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * This will get the width of the string when page rotation adjusted coordinates are used.      *      * @return The width of the text in display units.      */
specifier|public
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|getWidthRot
argument_list|(
name|rotation
argument_list|)
return|;
block|}
comment|/**      * This will get the width of the string when text direction adjusted coordinates are used.      *      * @return The width of the text in display units.      */
specifier|public
name|float
name|getWidthDirAdj
parameter_list|()
block|{
return|return
name|getWidthRot
argument_list|(
name|getDir
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will get the maximum height of all characters in this string.      *      * @return The maximum height of all characters in this string.      */
specifier|public
name|float
name|getHeight
parameter_list|()
block|{
return|return
name|maxTextHeight
return|;
block|}
comment|/**      * This will get the maximum height of all characters in this string.      *      * @return The maximum height of all characters in this string.      */
specifier|public
name|float
name|getHeightDir
parameter_list|()
block|{
comment|// this is not really a rotation-dependent calculation, but this is defined for symmetry
return|return
name|maxTextHeight
return|;
block|}
comment|/**      * This will get the font size that this object is suppose to be drawn at.      *      * @return The font size.      */
specifier|public
name|float
name|getFontSize
parameter_list|()
block|{
return|return
name|fontSize
return|;
block|}
comment|/**      * This will get the font size in pt. To get this size we have to multiply the pdf-fontsize      * and the scaling from the textmatrix      *      * @return The font size in pt.      */
specifier|public
name|float
name|getFontSizeInPt
parameter_list|()
block|{
return|return
name|fontSizePt
return|;
block|}
comment|/**      * This will get the font for the text being drawn.      *      * @return The font size.      */
specifier|public
name|PDFont
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
comment|/**      * This will get the width of a space character. This is useful for some algorithms such as the      * text stripper, that need to know the width of a space character.      *      * @return The width of a space character.      */
specifier|public
name|float
name|getWidthOfSpace
parameter_list|()
block|{
return|return
name|widthOfSpace
return|;
block|}
comment|/**      * @return Returns the xScale.      */
specifier|public
name|float
name|getXScale
parameter_list|()
block|{
return|return
name|textPos
operator|.
name|getXScale
argument_list|()
return|;
block|}
comment|/**      * @return Returns the yScale.      */
specifier|public
name|float
name|getYScale
parameter_list|()
block|{
return|return
name|textPos
operator|.
name|getYScale
argument_list|()
return|;
block|}
comment|/**      * Get the widths of each individual character.      *      * @return An array that is the same length as the length of the string.      */
specifier|public
name|float
index|[]
name|getIndividualWidths
parameter_list|()
block|{
return|return
name|widths
return|;
block|}
comment|/**      * Determine if this TextPosition logically contains another (i.e. they overlap and should be      * rendered on top of each other).      *      * @param tp2 The other TestPosition to compare against      * @return True if tp2 is contained in the bounding box of this text.      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|TextPosition
name|tp2
parameter_list|)
block|{
name|double
name|thisXstart
init|=
name|getXDirAdj
argument_list|()
decl_stmt|;
name|double
name|thisXend
init|=
name|getXDirAdj
argument_list|()
operator|+
name|getWidthDirAdj
argument_list|()
decl_stmt|;
name|double
name|tp2Xstart
init|=
name|tp2
operator|.
name|getXDirAdj
argument_list|()
decl_stmt|;
name|double
name|tp2Xend
init|=
name|tp2
operator|.
name|getXDirAdj
argument_list|()
operator|+
name|tp2
operator|.
name|getWidthDirAdj
argument_list|()
decl_stmt|;
comment|// no X overlap at all so return as soon as possible
if|if
condition|(
name|tp2Xend
operator|<=
name|thisXstart
operator|||
name|tp2Xstart
operator|>=
name|thisXend
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// no Y overlap at all so return as soon as possible. Note: 0.0 is in the upper left and
comment|// y-coordinate is top of TextPosition
if|if
condition|(
name|tp2
operator|.
name|getYDirAdj
argument_list|()
operator|+
name|tp2
operator|.
name|getHeightDir
argument_list|()
operator|<
name|getYDirAdj
argument_list|()
operator|||
name|tp2
operator|.
name|getYDirAdj
argument_list|()
operator|>
name|getYDirAdj
argument_list|()
operator|+
name|getHeightDir
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// we're going to calculate the percentage of overlap, if its less than a 15% x-coordinate
comment|// overlap then we'll return false because its negligible, .15 was determined by trial and
comment|// error in the regression test files
elseif|else
if|if
condition|(
name|tp2Xstart
operator|>
name|thisXstart
operator|&&
name|tp2Xend
operator|>
name|thisXend
condition|)
block|{
name|double
name|overlap
init|=
name|thisXend
operator|-
name|tp2Xstart
decl_stmt|;
name|double
name|overlapPercent
init|=
name|overlap
operator|/
name|getWidthDirAdj
argument_list|()
decl_stmt|;
return|return
name|overlapPercent
operator|>
literal|.15
return|;
block|}
elseif|else
if|if
condition|(
name|tp2Xstart
operator|<
name|thisXstart
operator|&&
name|tp2Xend
operator|<
name|thisXend
condition|)
block|{
name|double
name|overlap
init|=
name|tp2Xend
operator|-
name|thisXstart
decl_stmt|;
name|double
name|overlapPercent
init|=
name|overlap
operator|/
name|getWidthDirAdj
argument_list|()
decl_stmt|;
return|return
name|overlapPercent
operator|>
literal|.15
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Merge a single character TextPosition into the current object. This is to be used only for      * cases where we have a diacritic that overlaps an existing TextPosition. In a graphical      * display, we could overlay them, but for text extraction we need to merge them. Use the      * contains() method to test if two objects overlap.      *      * @param diacritic TextPosition to merge into the current TextPosition.      * @param normalize Instance of TextNormalize class to be used to normalize diacritic      */
specifier|public
name|void
name|mergeDiacritic
parameter_list|(
name|TextPosition
name|diacritic
parameter_list|,
name|TextNormalize
name|normalize
parameter_list|)
block|{
if|if
condition|(
name|diacritic
operator|.
name|getCharacter
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return;
block|}
name|float
name|diacXStart
init|=
name|diacritic
operator|.
name|getXDirAdj
argument_list|()
decl_stmt|;
name|float
name|diacXEnd
init|=
name|diacXStart
operator|+
name|diacritic
operator|.
name|widths
index|[
literal|0
index|]
decl_stmt|;
name|float
name|currCharXStart
init|=
name|getXDirAdj
argument_list|()
decl_stmt|;
name|int
name|strLen
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
name|boolean
name|wasAdded
init|=
literal|false
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
name|strLen
operator|&&
operator|!
name|wasAdded
condition|;
name|i
operator|++
control|)
block|{
name|float
name|currCharXEnd
init|=
name|currCharXStart
operator|+
name|widths
index|[
name|i
index|]
decl_stmt|;
comment|// this is the case where there is an overlap of the diacritic character with the
comment|// current character and the previous character. If no previous character, just append
comment|// the diacritic after the current one
if|if
condition|(
name|diacXStart
operator|<
name|currCharXStart
operator|&&
name|diacXEnd
operator|<=
name|currCharXEnd
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|insertDiacritic
argument_list|(
name|i
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|float
name|distanceOverlapping1
init|=
name|diacXEnd
operator|-
name|currCharXStart
decl_stmt|;
name|float
name|percentage1
init|=
name|distanceOverlapping1
operator|/
name|widths
index|[
name|i
index|]
decl_stmt|;
name|float
name|distanceOverlapping2
init|=
name|currCharXStart
operator|-
name|diacXStart
decl_stmt|;
name|float
name|percentage2
init|=
name|distanceOverlapping2
operator|/
name|widths
index|[
name|i
operator|-
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|percentage1
operator|>=
name|percentage2
condition|)
block|{
name|insertDiacritic
argument_list|(
name|i
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|insertDiacritic
argument_list|(
name|i
operator|-
literal|1
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
block|}
block|}
name|wasAdded
operator|=
literal|true
expr_stmt|;
block|}
comment|// diacritic completely covers this character and therefore we assume that this is the
comment|// character the diacritic belongs to
elseif|else
if|if
condition|(
name|diacXStart
argument_list|<
name|currCharXStart
operator|&&
name|diacXEnd
argument_list|>
name|currCharXEnd
condition|)
block|{
name|insertDiacritic
argument_list|(
name|i
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
name|wasAdded
operator|=
literal|true
expr_stmt|;
block|}
comment|// otherwise, The diacritic modifies this character because its completely
comment|// contained by the character width
elseif|else
if|if
condition|(
name|diacXStart
operator|>=
name|currCharXStart
operator|&&
name|diacXEnd
operator|<=
name|currCharXEnd
condition|)
block|{
name|insertDiacritic
argument_list|(
name|i
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
name|wasAdded
operator|=
literal|true
expr_stmt|;
block|}
comment|// last character in the TextPosition so we add diacritic to the end
elseif|else
if|if
condition|(
name|diacXStart
operator|>=
name|currCharXStart
operator|&&
name|diacXEnd
operator|>
name|currCharXEnd
operator|&&
name|i
operator|==
name|strLen
operator|-
literal|1
condition|)
block|{
name|insertDiacritic
argument_list|(
name|i
argument_list|,
name|diacritic
argument_list|,
name|normalize
argument_list|)
expr_stmt|;
name|wasAdded
operator|=
literal|true
expr_stmt|;
block|}
comment|// couldn't find anything useful so we go to the next character in the TextPosition
name|currCharXStart
operator|+=
name|widths
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
comment|/**      * Inserts the diacritic TextPosition to the str of this TextPosition and updates the widths      * array to include the extra character width.      *      * @param i current character      * @param diacritic The diacritic TextPosition      * @param normalize Instance of TextNormalize class to be used to normalize diacritic      */
specifier|private
name|void
name|insertDiacritic
parameter_list|(
name|int
name|i
parameter_list|,
name|TextPosition
name|diacritic
parameter_list|,
name|TextNormalize
name|normalize
parameter_list|)
block|{
comment|// we add the diacritic to the right or left of the character depending on the direction
comment|// of the character. Note that this is only required because the text is currently stored in
comment|// presentation order and not in logical order
name|int
name|dir
init|=
name|Character
operator|.
name|getDirectionality
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|string
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|float
index|[]
name|widths2
init|=
operator|new
name|float
index|[
name|widths
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|widths
argument_list|,
literal|0
argument_list|,
name|widths2
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|dir
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT
operator|||
name|dir
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
operator|||
name|dir
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING
operator|||
name|dir
operator|==
name|Character
operator|.
name|DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|normalize
operator|.
name|normalizeDiacritic
argument_list|(
name|diacritic
operator|.
name|getCharacter
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|widths2
index|[
name|i
index|]
operator|=
literal|0
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|widths2
index|[
name|i
operator|+
literal|1
index|]
operator|=
name|widths
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|widths2
index|[
name|i
index|]
operator|=
name|widths
index|[
name|i
index|]
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|normalize
operator|.
name|normalizeDiacritic
argument_list|(
name|diacritic
operator|.
name|getCharacter
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|widths2
index|[
name|i
operator|+
literal|1
index|]
operator|=
literal|0
expr_stmt|;
block|}
comment|// get the rest of the string
name|sb
operator|.
name|append
argument_list|(
name|string
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|string
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|widths
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|widths2
argument_list|,
name|i
operator|+
literal|2
argument_list|,
name|widths
operator|.
name|length
operator|-
name|i
operator|-
literal|1
argument_list|)
expr_stmt|;
name|string
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
name|widths
operator|=
name|widths2
expr_stmt|;
block|}
comment|/**      * @return True if the current character is a diacritic char.      */
specifier|public
name|boolean
name|isDiacritic
parameter_list|()
block|{
name|String
name|text
init|=
name|this
operator|.
name|getCharacter
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|type
init|=
name|Character
operator|.
name|getType
argument_list|(
name|text
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|type
operator|==
name|Character
operator|.
name|NON_SPACING_MARK
operator|||
name|type
operator|==
name|Character
operator|.
name|MODIFIER_SYMBOL
operator|||
name|type
operator|==
name|Character
operator|.
name|MODIFIER_LETTER
return|;
block|}
comment|/**      * Show the string data for this text position.      *      * @return A human readable form of this object.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getCharacter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

