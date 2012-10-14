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
name|pdmodel
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
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|font
operator|.
name|FontRenderContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|font
operator|.
name|GlyphVector
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
name|Point2D
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
name|NoninvertibleTransformException
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
name|HashMap
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
name|afm
operator|.
name|FontMetric
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
name|cos
operator|.
name|COSArray
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
name|cos
operator|.
name|COSBase
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
name|cos
operator|.
name|COSDictionary
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
name|cos
operator|.
name|COSName
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
name|cos
operator|.
name|COSNumber
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
name|cos
operator|.
name|COSStream
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
name|DictionaryEncoding
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
name|EncodingManager
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
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_comment
comment|/**  * This class contains implementation details of the simple pdf fonts.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.18 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDSimpleFont
extends|extends
name|PDFont
block|{
specifier|private
specifier|final
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
name|mFontSizes
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
argument_list|(
literal|128
argument_list|)
decl_stmt|;
specifier|private
name|float
name|avgFontWidth
init|=
literal|0.0f
decl_stmt|;
specifier|private
name|float
name|avgFontHeight
init|=
literal|0.0f
decl_stmt|;
specifier|private
name|float
name|fontWidthOfSpace
init|=
operator|-
literal|1f
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|SPACE_BYTES
init|=
block|{
operator|(
name|byte
operator|)
literal|32
block|}
decl_stmt|;
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
name|PDSimpleFont
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDSimpleFont
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDSimpleFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**     * Looks up, creates, returns  the AWT Font.     *      * @return returns the awt font to bes used for rendering      * @throws IOException if something went wrong.     */
specifier|public
name|Font
name|getawtFont
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Not yet implemented:"
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|drawString
parameter_list|(
name|String
name|string
parameter_list|,
name|int
index|[]
name|codePoints
parameter_list|,
name|Graphics
name|g
parameter_list|,
name|float
name|fontSize
parameter_list|,
name|AffineTransform
name|at
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
throws|throws
name|IOException
block|{
name|Font
name|awtFont
init|=
name|getawtFont
argument_list|()
decl_stmt|;
name|FontRenderContext
name|frc
init|=
operator|new
name|FontRenderContext
argument_list|(
operator|new
name|AffineTransform
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|GlyphVector
name|glyphs
init|=
literal|null
decl_stmt|;
name|boolean
name|useCodepoints
init|=
name|codePoints
operator|!=
literal|null
operator|&&
name|isType0Font
argument_list|()
decl_stmt|;
name|PDFont
name|descendantFont
init|=
name|useCodepoints
condition|?
operator|(
operator|(
name|PDType0Font
operator|)
name|this
operator|)
operator|.
name|getDescendantFont
argument_list|()
else|:
literal|null
decl_stmt|;
comment|// symbolic fonts may trigger the same fontmanager.so/dll error as described below
if|if
condition|(
name|useCodepoints
operator|&&
operator|!
name|descendantFont
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|isSymbolic
argument_list|()
condition|)
block|{
name|PDCIDFontType2Font
name|cid2Font
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|descendantFont
operator|instanceof
name|PDCIDFontType2Font
condition|)
block|{
name|cid2Font
operator|=
operator|(
name|PDCIDFontType2Font
operator|)
name|descendantFont
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|cid2Font
operator|!=
literal|null
operator|&&
name|cid2Font
operator|.
name|hasCIDToGIDMap
argument_list|()
operator|)
operator|||
name|isFontSubstituted
condition|)
block|{
comment|// we still have to use the string if a CIDToGIDMap is used
name|glyphs
operator|=
name|awtFont
operator|.
name|createGlyphVector
argument_list|(
name|frc
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|glyphs
operator|=
name|awtFont
operator|.
name|createGlyphVector
argument_list|(
name|frc
argument_list|,
name|codePoints
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// mdavis - fix fontmanager.so/dll on sun.font.FileFont.getGlyphImage
comment|// for font with bad cmaps?
comment|// Type1 fonts are not affected as they don't have cmaps
if|if
condition|(
operator|!
name|isType1Font
argument_list|()
operator|&&
name|awtFont
operator|.
name|canDisplayUpTo
argument_list|(
name|string
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Changing font on<"
operator|+
name|string
operator|+
literal|"> from<"
operator|+
name|awtFont
operator|.
name|getName
argument_list|()
operator|+
literal|"> to the default font"
argument_list|)
expr_stmt|;
name|awtFont
operator|=
name|Font
operator|.
name|decode
argument_list|(
literal|null
argument_list|)
operator|.
name|deriveFont
argument_list|(
literal|1f
argument_list|)
expr_stmt|;
block|}
name|glyphs
operator|=
name|awtFont
operator|.
name|createGlyphVector
argument_list|(
name|frc
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
name|Graphics2D
name|g2d
init|=
operator|(
name|Graphics2D
operator|)
name|g
decl_stmt|;
name|g2d
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_ON
argument_list|)
expr_stmt|;
name|writeFont
argument_list|(
name|g2d
argument_list|,
name|at
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|glyphs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the font height for a character.      *      * @param c The character code to get the width for.      * @param offset The offset into the array.      * @param length The length of the data.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
specifier|public
name|float
name|getFontHeight
parameter_list|(
name|byte
index|[]
name|c
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
comment|// maybe there is already a precalculated value
if|if
condition|(
name|avgFontHeight
operator|>
literal|0
condition|)
block|{
return|return
name|avgFontHeight
return|;
block|}
name|float
name|retval
init|=
literal|0
decl_stmt|;
name|FontMetric
name|metric
init|=
name|getAFM
argument_list|()
decl_stmt|;
if|if
condition|(
name|metric
operator|!=
literal|null
condition|)
block|{
name|int
name|code
init|=
name|getCodeFromArray
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|Encoding
name|encoding
init|=
name|getFontEncoding
argument_list|()
decl_stmt|;
name|String
name|characterName
init|=
name|encoding
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|retval
operator|=
name|metric
operator|.
name|getCharacterHeight
argument_list|(
name|characterName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDFontDescriptor
name|desc
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
comment|// the following values are all more or less accurate
comment|// at least all are average values. Maybe we'll find
comment|// another way to get those value for every single glyph
comment|// in the future if needed
name|PDRectangle
name|fontBBox
init|=
name|desc
operator|.
name|getFontBoundingBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontBBox
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|fontBBox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|0
condition|)
block|{
name|retval
operator|=
name|desc
operator|.
name|getCapHeight
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|0
condition|)
block|{
name|retval
operator|=
name|desc
operator|.
name|getAscent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|0
condition|)
block|{
name|retval
operator|=
name|desc
operator|.
name|getXHeight
argument_list|()
expr_stmt|;
if|if
condition|(
name|retval
operator|>
literal|0
condition|)
block|{
name|retval
operator|-=
name|desc
operator|.
name|getDescent
argument_list|()
expr_stmt|;
block|}
block|}
name|avgFontHeight
operator|=
name|retval
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the font width for a character.      *      * @param c The character code to get the width for.      * @param offset The offset into the array.      * @param length The length of the data.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
specifier|public
name|float
name|getFontWidth
parameter_list|(
name|byte
index|[]
name|c
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|code
init|=
name|getCodeFromArray
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|Float
name|fontWidth
init|=
name|mFontSizes
operator|.
name|get
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontWidth
operator|==
literal|null
condition|)
block|{
name|fontWidth
operator|=
name|getFontWidth
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
name|fontWidth
operator|<=
literal|0
condition|)
block|{
comment|//hmm should this be in PDType1Font??
name|fontWidth
operator|=
name|getFontWidthFromAFMFile
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
name|mFontSizes
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|fontWidth
argument_list|)
expr_stmt|;
block|}
return|return
name|fontWidth
return|;
block|}
comment|/**      * This will get the average font width for all characters.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
throws|throws
name|IOException
block|{
name|float
name|average
init|=
literal|0.0f
decl_stmt|;
comment|//AJW
if|if
condition|(
name|avgFontWidth
operator|!=
literal|0.0f
condition|)
block|{
name|average
operator|=
name|avgFontWidth
expr_stmt|;
block|}
else|else
block|{
name|float
name|totalWidth
init|=
literal|0.0f
decl_stmt|;
name|float
name|characterCount
init|=
literal|0.0f
decl_stmt|;
name|COSArray
name|widths
init|=
operator|(
name|COSArray
operator|)
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|)
decl_stmt|;
if|if
condition|(
name|widths
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|widths
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSNumber
name|fontWidth
init|=
operator|(
name|COSNumber
operator|)
name|widths
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontWidth
operator|.
name|floatValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|totalWidth
operator|+=
name|fontWidth
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|characterCount
operator|+=
literal|1
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|totalWidth
operator|>
literal|0
condition|)
block|{
name|average
operator|=
name|totalWidth
operator|/
name|characterCount
expr_stmt|;
block|}
else|else
block|{
name|average
operator|=
name|getAverageFontWidthFromAFMFile
argument_list|()
expr_stmt|;
block|}
name|avgFontWidth
operator|=
name|average
expr_stmt|;
block|}
return|return
name|average
return|;
block|}
comment|/**      * This will get the ToUnicode object.      *      * @return The ToUnicode object.      */
specifier|public
name|COSBase
name|getToUnicode
parameter_list|()
block|{
return|return
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|)
return|;
block|}
comment|/**      * This will set the ToUnicode object.      *      * @param unicode The unicode object.      */
specifier|public
name|void
name|setToUnicode
parameter_list|(
name|COSBase
name|unicode
parameter_list|)
block|{
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|,
name|unicode
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the fonts bounding box.      *      * @return The fonts bouding box.      *      * @throws IOException If there is an error getting the bounding box.      */
specifier|public
name|PDRectangle
name|getFontBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getFontDescriptor
argument_list|()
operator|.
name|getFontBoundingBox
argument_list|()
return|;
block|}
comment|/**      * This will draw a string on a canvas using the font.      *      * @param g2d The graphics to draw onto.      * @param at The transformation matrix with all information for scaling and shearing of the font.      * @param x The x coordinate to draw at.      * @param y The y coordinate to draw at.      * @param glyphs The GlyphVector containing the glyphs to be drawn.      *      */
specifier|protected
name|void
name|writeFont
parameter_list|(
specifier|final
name|Graphics2D
name|g2d
parameter_list|,
specifier|final
name|AffineTransform
name|at
parameter_list|,
specifier|final
name|float
name|x
parameter_list|,
specifier|final
name|float
name|y
parameter_list|,
specifier|final
name|GlyphVector
name|glyphs
parameter_list|)
block|{
comment|// check if we have a rotation
if|if
condition|(
operator|!
name|at
operator|.
name|isIdentity
argument_list|()
condition|)
block|{
try|try
block|{
name|AffineTransform
name|atInv
init|=
name|at
operator|.
name|createInverse
argument_list|()
decl_stmt|;
comment|// do only apply the size of the transform, rotation will be realized by rotating the graphics,
comment|// otherwise the hp printers will not render the font
comment|// apply the transformation to the graphics, which should be the same as applying the
comment|// transformation itself to the text
name|g2d
operator|.
name|transform
argument_list|(
name|at
argument_list|)
expr_stmt|;
comment|// translate the coordinates
name|Point2D
operator|.
name|Float
name|newXy
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|atInv
operator|.
name|transform
argument_list|(
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|,
name|newXy
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|drawGlyphVector
argument_list|(
name|glyphs
argument_list|,
operator|(
name|float
operator|)
name|newXy
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|newXy
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
comment|// restore the original transformation
name|g2d
operator|.
name|transform
argument_list|(
name|atInv
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoninvertibleTransformException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".writeFont"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|g2d
operator|.
name|drawGlyphVector
argument_list|(
name|glyphs
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|void
name|determineEncoding
parameter_list|()
block|{
name|String
name|cmapName
init|=
literal|null
decl_stmt|;
name|COSName
name|encodingName
init|=
literal|null
decl_stmt|;
name|COSBase
name|encoding
init|=
name|getEncoding
argument_list|()
decl_stmt|;
name|Encoding
name|fontEncoding
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|encoding
operator|instanceof
name|COSName
condition|)
block|{
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|encodingName
operator|=
operator|(
name|COSName
operator|)
name|encoding
expr_stmt|;
name|cmap
operator|=
name|cmapObjects
operator|.
name|get
argument_list|(
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|cmapName
operator|=
name|encodingName
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|cmapName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fontEncoding
operator|=
name|EncodingManager
operator|.
name|INSTANCE
operator|.
name|getEncoding
argument_list|(
name|encodingName
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
name|debug
argument_list|(
literal|"Debug: Could not find encoding for "
operator|+
name|encodingName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|COSStream
condition|)
block|{
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|COSStream
name|encodingStream
init|=
operator|(
name|COSStream
operator|)
name|encoding
decl_stmt|;
try|try
block|{
name|cmap
operator|=
name|parseCmap
argument_list|(
literal|null
argument_list|,
name|encodingStream
operator|.
name|getUnfilteredStream
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
literal|"Error: Could not parse the embedded CMAP"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|COSDictionary
condition|)
block|{
try|try
block|{
name|fontEncoding
operator|=
operator|new
name|DictionaryEncoding
argument_list|(
operator|(
name|COSDictionary
operator|)
name|encoding
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
literal|"Error: Could not create the DictionaryEncoding"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|setFontEncoding
argument_list|(
name|fontEncoding
argument_list|)
expr_stmt|;
name|extractToUnicodeEncoding
argument_list|()
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|cmapName
operator|!=
literal|null
condition|)
block|{
name|String
name|resourceName
init|=
name|resourceRootCMAP
operator|+
name|cmapName
decl_stmt|;
try|try
block|{
name|cmap
operator|=
name|parseCmap
argument_list|(
name|resourceRootCMAP
argument_list|,
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resourceName
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|encodingName
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not parse predefined CMAP file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
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
literal|"Error: Could not find predefined CMAP file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|extractToUnicodeEncoding
parameter_list|()
block|{
name|COSName
name|encodingName
init|=
literal|null
decl_stmt|;
name|String
name|cmapName
init|=
literal|null
decl_stmt|;
name|COSBase
name|toUnicode
init|=
name|getToUnicode
argument_list|()
decl_stmt|;
if|if
condition|(
name|toUnicode
operator|!=
literal|null
condition|)
block|{
name|setHasToUnicode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|toUnicode
operator|instanceof
name|COSStream
condition|)
block|{
try|try
block|{
name|toUnicodeCmap
operator|=
name|parseCmap
argument_list|(
name|resourceRootCMAP
argument_list|,
operator|(
operator|(
name|COSStream
operator|)
name|toUnicode
operator|)
operator|.
name|getUnfilteredStream
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
literal|"Error: Could not load embedded ToUnicode CMap"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|toUnicode
operator|instanceof
name|COSName
condition|)
block|{
name|encodingName
operator|=
operator|(
name|COSName
operator|)
name|toUnicode
expr_stmt|;
name|toUnicodeCmap
operator|=
name|cmapObjects
operator|.
name|get
argument_list|(
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|toUnicodeCmap
operator|==
literal|null
condition|)
block|{
name|cmapName
operator|=
name|encodingName
operator|.
name|getName
argument_list|()
expr_stmt|;
name|String
name|resourceName
init|=
name|resourceRootCMAP
operator|+
name|cmapName
decl_stmt|;
try|try
block|{
name|toUnicodeCmap
operator|=
name|parseCmap
argument_list|(
name|resourceRootCMAP
argument_list|,
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resourceName
argument_list|)
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
literal|"Error: Could not find predefined ToUnicode CMap file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|toUnicodeCmap
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not parse predefined ToUnicode CMap file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isFontSubstituted
init|=
literal|false
decl_stmt|;
comment|/**      * This will get the value for isFontSubstituted, which indicates      * if the font was substituted due to a problem with the embedded one.      *       * @return true if the font was substituted      */
specifier|protected
name|boolean
name|isFontSubstituted
parameter_list|()
block|{
return|return
name|isFontSubstituted
return|;
block|}
comment|/**      * This will set  the value for isFontSubstituted.      *       * @param isSubstituted true if the font was substituted      */
specifier|protected
name|void
name|setIsFontSubstituted
parameter_list|(
name|boolean
name|isSubstituted
parameter_list|)
block|{
name|isFontSubstituted
operator|=
name|isSubstituted
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|float
name|getSpaceWidth
parameter_list|()
block|{
if|if
condition|(
name|fontWidthOfSpace
operator|==
operator|-
literal|1f
condition|)
block|{
name|COSBase
name|toUnicode
init|=
name|getToUnicode
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|toUnicode
operator|!=
literal|null
condition|)
block|{
name|int
name|spaceMapping
init|=
name|toUnicodeCmap
operator|.
name|getSpaceMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|spaceMapping
operator|>
operator|-
literal|1
condition|)
block|{
name|fontWidthOfSpace
operator|=
name|getFontWidth
argument_list|(
name|spaceMapping
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|fontWidthOfSpace
operator|=
name|getFontWidth
argument_list|(
name|SPACE_BYTES
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// use the average font width as fall back
if|if
condition|(
name|fontWidthOfSpace
operator|<=
literal|0
condition|)
block|{
name|fontWidthOfSpace
operator|=
name|getAverageFontWidth
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't determine the width of the space character using 250 as default"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fontWidthOfSpace
operator|=
literal|250f
expr_stmt|;
block|}
block|}
return|return
name|fontWidthOfSpace
return|;
block|}
block|}
end_class

end_unit

