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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|contentstream
operator|.
name|PDFStreamEngine
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
name|PDPage
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
name|encoding
operator|.
name|GlyphList
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
name|common
operator|.
name|PDRectangle
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
name|PDType3Font
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
name|graphics
operator|.
name|state
operator|.
name|PDGraphicsState
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
name|Vector
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
name|contentstream
operator|.
name|operator
operator|.
name|DrawObject
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
name|contentstream
operator|.
name|operator
operator|.
name|state
operator|.
name|Concatenate
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
name|contentstream
operator|.
name|operator
operator|.
name|state
operator|.
name|Restore
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
name|contentstream
operator|.
name|operator
operator|.
name|state
operator|.
name|Save
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
name|contentstream
operator|.
name|operator
operator|.
name|state
operator|.
name|SetGraphicsStateParameters
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
name|contentstream
operator|.
name|operator
operator|.
name|state
operator|.
name|SetMatrix
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|BeginText
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|EndText
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetFontAndSize
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetTextHorizontalScaling
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|ShowTextAdjusted
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|ShowTextLine
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|ShowTextLineAndSpace
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|MoveText
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|MoveTextSetLeading
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|NextLine
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetCharSpacing
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetTextLeading
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetTextRenderingMode
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetTextRise
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|SetWordSpacing
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
name|contentstream
operator|.
name|operator
operator|.
name|text
operator|.
name|ShowText
import|;
end_import

begin_comment
comment|/**  * PDFStreamEngine subclass for advanced processing of text via TextPosition.  *  * @see org.apache.pdfbox.text.TextPosition  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
class|class
name|PDFTextStreamEngine
extends|extends
name|PDFStreamEngine
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
name|PDFTextStreamEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|int
name|pageRotation
decl_stmt|;
specifier|private
name|PDRectangle
name|pageSize
decl_stmt|;
specifier|private
specifier|final
name|GlyphList
name|glyphList
decl_stmt|;
comment|/**      * Constructor.      */
name|PDFTextStreamEngine
parameter_list|()
throws|throws
name|IOException
block|{
name|addOperator
argument_list|(
operator|new
name|BeginText
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|Concatenate
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|DrawObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// special text version
name|addOperator
argument_list|(
operator|new
name|EndText
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetGraphicsStateParameters
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|Save
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|Restore
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|NextLine
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetCharSpacing
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|MoveText
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|MoveTextSetLeading
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetFontAndSize
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|ShowText
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|ShowTextAdjusted
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetTextLeading
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetMatrix
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetTextRenderingMode
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetTextRise
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetWordSpacing
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|SetTextHorizontalScaling
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|ShowTextLine
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|ShowTextLineAndSpace
argument_list|()
argument_list|)
expr_stmt|;
comment|// load additional glyph list for Unicode mapping
name|String
name|path
init|=
literal|"org/apache/pdfbox/resources/glyphlist/additional.txt"
decl_stmt|;
name|InputStream
name|input
init|=
name|GlyphList
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|glyphList
operator|=
operator|new
name|GlyphList
argument_list|(
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will initialise and process the contents of the stream.      *      * @param page the page to process      * @throws java.io.IOException if there is an error accessing the stream.      */
annotation|@
name|Override
specifier|public
name|void
name|processPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|pageRotation
operator|=
name|page
operator|.
name|getRotation
argument_list|()
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|page
operator|.
name|getCropBox
argument_list|()
expr_stmt|;
name|super
operator|.
name|processPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method was originally written by Ben Litchfield for PDFStreamEngine.      */
annotation|@
name|Override
specifier|protected
name|void
name|showGlyph
parameter_list|(
name|Matrix
name|textRenderingMatrix
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|int
name|code
parameter_list|,
name|String
name|unicode
parameter_list|,
name|Vector
name|displacement
parameter_list|)
throws|throws
name|IOException
block|{
comment|//
comment|// legacy calculations which were previously in PDFStreamEngine
comment|//
name|PDGraphicsState
name|state
init|=
name|getGraphicsState
argument_list|()
decl_stmt|;
name|Matrix
name|ctm
init|=
name|state
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|float
name|fontSize
init|=
name|state
operator|.
name|getTextState
argument_list|()
operator|.
name|getFontSize
argument_list|()
decl_stmt|;
name|float
name|horizontalScaling
init|=
name|state
operator|.
name|getTextState
argument_list|()
operator|.
name|getHorizontalScaling
argument_list|()
operator|/
literal|100f
decl_stmt|;
name|Matrix
name|textMatrix
init|=
name|getTextMatrix
argument_list|()
decl_stmt|;
comment|// 1/2 the bbox is used as the height todo: why?
name|float
name|glyphHeight
init|=
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
decl_stmt|;
comment|// transformPoint from glyph space -> text space
name|float
name|height
init|=
name|font
operator|.
name|getFontMatrix
argument_list|()
operator|.
name|transformPoint
argument_list|(
literal|0
argument_list|,
name|glyphHeight
argument_list|)
operator|.
name|y
decl_stmt|;
comment|// (modified) combined displacement, this is calculated *without* taking the character
comment|// spacing and word spacing into account, due to legacy code in TextStripper
name|float
name|tx
init|=
name|displacement
operator|.
name|getX
argument_list|()
operator|*
name|fontSize
operator|*
name|horizontalScaling
decl_stmt|;
name|float
name|ty
init|=
literal|0
decl_stmt|;
comment|// todo: support vertical writing mode
comment|// (modified) combined displacement matrix
name|Matrix
name|td
init|=
name|Matrix
operator|.
name|getTranslateInstance
argument_list|(
name|tx
argument_list|,
name|ty
argument_list|)
decl_stmt|;
comment|// (modified) text rendering matrix
name|Matrix
name|nextTextRenderingMatrix
init|=
name|td
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
operator|.
name|multiply
argument_list|(
name|ctm
argument_list|)
decl_stmt|;
comment|// text space -> device space
name|float
name|nextX
init|=
name|nextTextRenderingMatrix
operator|.
name|getTranslateX
argument_list|()
decl_stmt|;
name|float
name|nextY
init|=
name|nextTextRenderingMatrix
operator|.
name|getTranslateY
argument_list|()
decl_stmt|;
comment|// (modified) width and height calculations
name|float
name|dxDisplay
init|=
name|nextX
operator|-
name|textRenderingMatrix
operator|.
name|getTranslateX
argument_list|()
decl_stmt|;
name|float
name|dyDisplay
init|=
name|height
operator|*
name|textRenderingMatrix
operator|.
name|getScalingFactorY
argument_list|()
decl_stmt|;
comment|//
comment|// start of the original method
comment|//
comment|// Note on variable names. There are three different units being used in this code.
comment|// Character sizes are given in glyph units, text locations are initially given in text
comment|// units, and we want to save the data in display units. The variable names should end with
comment|// Text or Disp to represent if the values are in text or disp units (no glyph units are
comment|// saved).
name|float
name|glyphSpaceToTextSpaceFactor
init|=
literal|1
operator|/
literal|1000f
decl_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDType3Font
condition|)
block|{
name|glyphSpaceToTextSpaceFactor
operator|=
name|font
operator|.
name|getFontMatrix
argument_list|()
operator|.
name|getScaleX
argument_list|()
expr_stmt|;
block|}
name|float
name|spaceWidthText
init|=
literal|0
decl_stmt|;
try|try
block|{
comment|// to avoid crash as described in PDFBOX-614, see what the space displacement should be
name|spaceWidthText
operator|=
name|font
operator|.
name|getSpaceWidth
argument_list|()
operator|*
name|glyphSpaceToTextSpaceFactor
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|exception
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spaceWidthText
operator|==
literal|0
condition|)
block|{
name|spaceWidthText
operator|=
name|font
operator|.
name|getAverageFontWidth
argument_list|()
operator|*
name|glyphSpaceToTextSpaceFactor
expr_stmt|;
comment|// the average space width appears to be higher than necessary so make it smaller
name|spaceWidthText
operator|*=
literal|.80f
expr_stmt|;
block|}
if|if
condition|(
name|spaceWidthText
operator|==
literal|0
condition|)
block|{
name|spaceWidthText
operator|=
literal|1.0f
expr_stmt|;
comment|// if could not find font, use a generic value
block|}
comment|// the space width has to be transformed into display units
name|float
name|spaceWidthDisplay
init|=
name|spaceWidthText
operator|*
name|textRenderingMatrix
operator|.
name|getScalingFactorX
argument_list|()
decl_stmt|;
comment|// use our additional glyph list for Unicode mapping
name|unicode
operator|=
name|font
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|,
name|glyphList
argument_list|)
expr_stmt|;
comment|// when there is no Unicode mapping available, Acrobat simply coerces the character code
comment|// into Unicode, so we do the same. Subclasses of PDFStreamEngine don't necessarily want
comment|// this, which is why we leave it until this point in PDFTextStreamEngine.
if|if
condition|(
name|unicode
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|font
operator|instanceof
name|PDSimpleFont
condition|)
block|{
name|char
name|c
init|=
operator|(
name|char
operator|)
name|code
decl_stmt|;
name|unicode
operator|=
operator|new
name|String
argument_list|(
operator|new
name|char
index|[]
block|{
name|c
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Acrobat doesn't seem to coerce composite font's character codes, instead it
comment|// skips them. See the "allah2.pdf" TestTextStripper file.
return|return;
block|}
block|}
name|processTextPosition
argument_list|(
operator|new
name|TextPosition
argument_list|(
name|pageRotation
argument_list|,
name|pageSize
operator|.
name|getWidth
argument_list|()
argument_list|,
name|pageSize
operator|.
name|getHeight
argument_list|()
argument_list|,
name|textRenderingMatrix
argument_list|,
name|nextX
argument_list|,
name|nextY
argument_list|,
name|dyDisplay
argument_list|,
name|dxDisplay
argument_list|,
name|spaceWidthDisplay
argument_list|,
name|unicode
argument_list|,
operator|new
name|int
index|[]
block|{
name|code
block|}
argument_list|,
name|font
argument_list|,
name|fontSize
argument_list|,
call|(
name|int
call|)
argument_list|(
name|fontSize
operator|*
name|textRenderingMatrix
operator|.
name|getScalingFactorX
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A method provided as an event interface to allow a subclass to perform some specific      * functionality when text needs to be processed.      *      * @param text The text to be processed.      */
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
comment|// subclasses can override to provide specific functionality
block|}
block|}
end_class

end_unit

