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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|afm
operator|.
name|FontMetrics
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
name|cmap
operator|.
name|CMap
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
name|util
operator|.
name|BoundingBox
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
name|io
operator|.
name|IOUtils
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
name|COSArrayList
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
name|COSObjectable
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

begin_comment
comment|/**  * This is the base class for all PDF fonts.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDFont
implements|implements
name|COSObjectable
implements|,
name|PDFontLike
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
name|PDFont
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Matrix
name|DEFAULT_FONT_MATRIX
init|=
operator|new
name|Matrix
argument_list|(
literal|0.001f
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0.001f
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|COSDictionary
name|dict
decl_stmt|;
specifier|private
specifier|final
name|CMap
name|toUnicodeCMap
decl_stmt|;
specifier|private
specifier|final
name|FontMetrics
name|afmStandard14
decl_stmt|;
comment|// AFM for standard 14 fonts
specifier|private
name|PDFontDescriptor
name|fontDescriptor
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Float
argument_list|>
name|widths
decl_stmt|;
specifier|private
name|float
name|avgFontWidth
decl_stmt|;
specifier|private
name|float
name|fontWidthOfSpace
init|=
operator|-
literal|1f
decl_stmt|;
comment|/**      * Constructor for embedding.      */
name|PDFont
parameter_list|()
block|{
name|dict
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
expr_stmt|;
name|toUnicodeCMap
operator|=
literal|null
expr_stmt|;
name|fontDescriptor
operator|=
literal|null
expr_stmt|;
name|afmStandard14
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Constructor for Standard 14.      */
name|PDFont
parameter_list|(
name|String
name|baseFont
parameter_list|)
block|{
name|dict
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|toUnicodeCMap
operator|=
literal|null
expr_stmt|;
name|afmStandard14
operator|=
name|Standard14Fonts
operator|.
name|getAFM
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
if|if
condition|(
name|afmStandard14
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No AFM for font "
operator|+
name|baseFont
argument_list|)
throw|;
block|}
name|fontDescriptor
operator|=
name|PDType1FontEmbedder
operator|.
name|buildFontDescriptor
argument_list|(
name|afmStandard14
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fontDictionary Font dictionary.      */
specifier|protected
name|PDFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|dict
operator|=
name|fontDictionary
expr_stmt|;
comment|// standard 14 fonts use an AFM
name|afmStandard14
operator|=
name|Standard14Fonts
operator|.
name|getAFM
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// may be null (it usually is)
comment|// font descriptor
name|COSDictionary
name|fd
init|=
operator|(
name|COSDictionary
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|)
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|fontDescriptor
operator|=
operator|new
name|PDFontDescriptor
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|afmStandard14
operator|!=
literal|null
condition|)
block|{
comment|// build font descriptor from the AFM
name|fontDescriptor
operator|=
name|PDType1FontEmbedder
operator|.
name|buildFontDescriptor
argument_list|(
name|afmStandard14
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fontDescriptor
operator|=
literal|null
expr_stmt|;
block|}
comment|// ToUnicode CMap
name|COSBase
name|toUnicode
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|toUnicode
operator|!=
literal|null
condition|)
block|{
name|toUnicodeCMap
operator|=
name|readCMap
argument_list|(
name|toUnicode
argument_list|)
expr_stmt|;
if|if
condition|(
name|toUnicodeCMap
operator|!=
literal|null
operator|&&
operator|!
name|toUnicodeCMap
operator|.
name|hasUnicodeMappings
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid ToUnicode CMap in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|toUnicodeCMap
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Returns the AFM if this is a Standard 14 font.      */
specifier|protected
specifier|final
name|FontMetrics
name|getStandard14AFM
parameter_list|()
block|{
return|return
name|afmStandard14
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
return|return
name|fontDescriptor
return|;
block|}
comment|/**      * Sets the font descriptor when embedding a font.      */
specifier|protected
specifier|final
name|void
name|setFontDescriptor
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|this
operator|.
name|fontDescriptor
operator|=
name|fontDescriptor
expr_stmt|;
block|}
comment|/**      /**      * Reads a CMap given a COS Stream or Name. May return null if a predefined CMap does not exist.      *      * @param base COSName or COSStream      */
specifier|protected
specifier|final
name|CMap
name|readCMap
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|base
operator|instanceof
name|COSName
condition|)
block|{
comment|// predefined CMap
name|String
name|name
init|=
operator|(
operator|(
name|COSName
operator|)
name|base
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|CMapManager
operator|.
name|getPredefinedCMap
argument_list|(
name|name
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
comment|// embedded CMap
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
operator|(
operator|(
name|COSStream
operator|)
name|base
operator|)
operator|.
name|getUnfilteredStream
argument_list|()
expr_stmt|;
return|return
name|CMapManager
operator|.
name|parseCMap
argument_list|(
name|input
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected Name or Stream"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|dict
return|;
block|}
annotation|@
name|Override
specifier|public
name|Vector
name|getPositionVector
parameter_list|(
name|int
name|code
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Horizontal fonts have no position vector"
argument_list|)
throw|;
block|}
comment|/**      * Returns the displacement vector (w0, w1) in text space, for the given character.      * For horizontal text only the x component is used, for vertical text only the y component.      *      * @param code character code      * @return displacement vector      */
specifier|public
name|Vector
name|getDisplacement
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|Vector
argument_list|(
name|getWidth
argument_list|(
name|code
argument_list|)
operator|/
literal|1000
argument_list|,
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getWidth
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Acrobat overrides the widths in the font program on the conforming reader's system with
comment|// the widths specified in the font dictionary." (Adobe Supplement to the ISO 32000)
comment|//
comment|// Note: The Adobe Supplement says that the override happens "If the font program is not
comment|// embedded", however PDFBOX-427 shows that it also applies to embedded fonts.
comment|// Type1, Type1C, Type3
if|if
condition|(
name|dict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|)
operator|||
name|dict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|MISSING_WIDTH
argument_list|)
condition|)
block|{
name|int
name|firstChar
init|=
name|dict
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|lastChar
init|=
name|dict
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|getWidths
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|code
operator|>=
name|firstChar
operator|&&
name|code
operator|<=
name|lastChar
condition|)
block|{
return|return
name|getWidths
argument_list|()
operator|.
name|get
argument_list|(
name|code
operator|-
name|firstChar
argument_list|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
return|return
name|fd
operator|.
name|getMissingWidth
argument_list|()
return|;
comment|// default is 0
block|}
block|}
comment|// standard 14 font widths are specified by an AFM
if|if
condition|(
name|isStandard14
argument_list|()
condition|)
block|{
return|return
name|getStandard14Width
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|// if there's nothing to override with, then obviously we fall back to the font
return|return
name|getWidthFromFont
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * Returns the glyph width from the AFM if this is a Standard 14 font.      *       * @param code character code      * @return width in 1/1000 text space      */
specifier|protected
specifier|abstract
name|float
name|getStandard14Width
parameter_list|(
name|int
name|code
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|float
name|getWidthFromFont
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|boolean
name|isEmbedded
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|float
name|getHeight
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Encodes the given string for use in a PDF content stream.      *      * @param text Any Unicode text.      * @return Array of PDF content stream bytes.      * @throws IOException If the text could not be encoded.      */
specifier|public
specifier|final
name|byte
index|[]
name|encode
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|offset
init|=
literal|0
init|;
name|offset
operator|<
name|text
operator|.
name|length
argument_list|()
condition|;
control|)
block|{
name|int
name|codePoint
init|=
name|text
operator|.
name|codePointAt
argument_list|(
name|offset
argument_list|)
decl_stmt|;
comment|// multi-byte encoding with 1 to 4 bytes
name|byte
index|[]
name|bytes
init|=
name|encode
argument_list|(
name|codePoint
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|Character
operator|.
name|charCount
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
block|}
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/**      * Encodes the given Unicode code point for use in a PDF content stream.      * Content streams use a multi-byte encoding with 1 to 4 bytes.      *      *<p>This method is called when embedding text in PDFs and when filling in fields.      *      * @param unicode Unicode code point.      * @return Array of 1 to 4 PDF content stream bytes.      * @throws IOException If the text could not be encoded.      */
specifier|protected
specifier|abstract
name|byte
index|[]
name|encode
parameter_list|(
name|int
name|unicode
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the width of the given Unicode string.      *      * @param text The text to get the width of.      * @return The width of the string in 1/1000 units of text space.      * @throws IOException If there is an error getting the width information.      */
specifier|public
name|float
name|getStringWidth
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
name|encode
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|float
name|width
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|code
init|=
name|readCode
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|width
operator|+=
name|getWidth
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
return|return
name|width
return|;
block|}
comment|/**      * This will get the average font width for all characters.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      */
comment|// todo: this method is highly suspicious, the average glyph width is not usually a good metric
annotation|@
name|Override
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
block|{
name|float
name|average
decl_stmt|;
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
name|dict
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
literal|0
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
comment|/**      * Reads a character code from a content stream string. Codes may be up to 4 bytes long.      *      * @param in string stream      * @return character code      * @throws IOException if the CMap or stream cannot be read      */
specifier|public
specifier|abstract
name|int
name|readCode
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the Unicode character sequence which corresponds to the given character code.      *      * @param code character code      * @param customGlyphList a custom glyph list to use instead of the Adobe Glyph List      * @return Unicode character(s)      */
specifier|public
name|String
name|toUnicode
parameter_list|(
name|int
name|code
parameter_list|,
name|GlyphList
name|customGlyphList
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toUnicode
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * Returns the Unicode character sequence which corresponds to the given character code.      *      * @param code character code      * @return Unicode character(s)      */
specifier|public
name|String
name|toUnicode
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
comment|// if the font dictionary containsName a ToUnicode CMap, use that CMap
if|if
condition|(
name|toUnicodeCMap
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|toUnicodeCMap
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|toUnicodeCMap
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Identity-"
argument_list|)
condition|)
block|{
comment|// handle the undocumented case of using Identity-H/V as a ToUnicode CMap, this
comment|// isn't  actually valid as the Identity-x CMaps are code->CID maps, not
comment|// code->Unicode maps. See sample_fonts_solidconvertor.pdf for an example.
return|return
operator|new
name|String
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
name|code
block|}
argument_list|)
return|;
block|}
else|else
block|{
comment|// proceed as normal
return|return
name|toUnicodeCMap
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
return|;
block|}
block|}
comment|// if no value has been produced, there is no way to obtain Unicode for the character.
comment|// this behaviour can be overridden is subclasses, but this method *must* return null here
return|return
literal|null
return|;
block|}
comment|/**      * This will always return "Font" for fonts.      *       * @return The type of object that this is.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
return|;
block|}
comment|/**      * This will get the subtype of font.      */
specifier|public
name|String
name|getSubType
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|BoundingBox
name|getBoundingBox
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * The widths of the characters. This will be null for the standard 14 fonts.      *      * @return The widths of the characters.      */
specifier|protected
specifier|final
name|List
argument_list|<
name|Float
argument_list|>
name|getWidths
parameter_list|()
block|{
if|if
condition|(
name|widths
operator|==
literal|null
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|dict
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
name|array
operator|!=
literal|null
condition|)
block|{
name|widths
operator|=
name|COSArrayList
operator|.
name|convertFloatCOSArrayToList
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|widths
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|widths
return|;
block|}
annotation|@
name|Override
specifier|public
name|Matrix
name|getFontMatrix
parameter_list|()
block|{
return|return
name|DEFAULT_FONT_MATRIX
return|;
block|}
comment|/**      * Determines the width of the space character.      *       * @return the width of the space character      */
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
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TO_UNICODE
argument_list|)
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
name|toUnicodeCMap
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
name|getWidth
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
name|getWidth
argument_list|(
literal|32
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
literal|"Can't determine the width of the space character, assuming 250"
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
comment|/**      * Returns true if the font uses vertical writing mode.      */
specifier|public
specifier|abstract
name|boolean
name|isVertical
parameter_list|()
function_decl|;
comment|/**      * Returns true if this font is one of the "Standard 14" fonts and receives special handling.      */
specifier|public
name|boolean
name|isStandard14
parameter_list|()
block|{
comment|// this logic is based on Acrobat's behaviour, see see PDFBOX-2372
comment|// embedded fonts never get special treatment
if|if
condition|(
name|isEmbedded
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// if the name matches, this is a Standard 14 font
return|return
name|Standard14Fonts
operator|.
name|containsName
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Adds the given Unicode point to the subset.      *       * @param codePoint Unicode code point      */
specifier|public
specifier|abstract
name|void
name|addToSubset
parameter_list|(
name|int
name|codePoint
parameter_list|)
function_decl|;
comment|/**      * Replaces this font with a subset containing only the given Unicode characters.      *      * @throws IOException if the subset could not be written      */
specifier|public
specifier|abstract
name|void
name|subset
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if this font will be subset when embedded.      */
specifier|public
specifier|abstract
name|boolean
name|willBeSubset
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|boolean
name|isDamaged
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
return|return
name|other
operator|instanceof
name|PDFont
operator|&&
operator|(
operator|(
name|PDFont
operator|)
name|other
operator|)
operator|.
name|getCOSObject
argument_list|()
operator|==
name|this
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" "
operator|+
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

