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
name|Graphics
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
name|COSInteger
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
name|common
operator|.
name|PDStream
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
name|HashMap
name|mFontSizes
init|=
operator|new
name|HashMap
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
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|drawString
parameter_list|(
name|String
name|string
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
name|System
operator|.
name|err
operator|.
name|println
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
block|}
comment|/**      * This will get the font width for a character.      *      * @param c The character code to get the width for.      * @param offset The offset into the array.      * @param length The length of the data.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
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
name|float
name|retval
init|=
literal|0
decl_stmt|;
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
name|Encoding
name|encoding
init|=
name|getEncoding
argument_list|()
decl_stmt|;
name|COSName
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
operator|.
name|getName
argument_list|()
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
name|float
name|xHeight
init|=
name|desc
operator|.
name|getXHeight
argument_list|()
decl_stmt|;
name|float
name|capHeight
init|=
name|desc
operator|.
name|getCapHeight
argument_list|()
decl_stmt|;
if|if
condition|(
name|xHeight
operator|!=
literal|0f
operator|&&
name|capHeight
operator|!=
literal|0
condition|)
block|{
comment|//do an average of these two.  Can we do better???
name|retval
operator|=
operator|(
name|xHeight
operator|+
name|capHeight
operator|)
operator|/
literal|2f
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xHeight
operator|!=
literal|0
condition|)
block|{
name|retval
operator|=
name|xHeight
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|capHeight
operator|!=
literal|0
condition|)
block|{
name|retval
operator|=
name|capHeight
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
literal|0
expr_stmt|;
block|}
comment|//hmm, not sure if this is 100% correct
comment|//but gives a height, Should we add Descent as well??
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
name|float
name|fontWidth
init|=
literal|0
decl_stmt|;
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
name|Integer
name|codeI
init|=
operator|new
name|Integer
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|mFontSizes
operator|.
name|containsKey
argument_list|(
name|codeI
argument_list|)
condition|)
block|{
name|Float
name|fontWidthF
init|=
operator|(
name|Float
operator|)
name|mFontSizes
operator|.
name|get
argument_list|(
name|codeI
argument_list|)
decl_stmt|;
name|fontWidth
operator|=
name|fontWidthF
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|//hmm should this be in a subclass??
name|COSInteger
name|firstChar
init|=
operator|(
name|COSInteger
operator|)
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FIRST_CHAR
argument_list|)
decl_stmt|;
name|COSInteger
name|lastChar
init|=
operator|(
name|COSInteger
operator|)
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|LAST_CHAR
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstChar
operator|!=
literal|null
operator|&&
name|lastChar
operator|!=
literal|null
condition|)
block|{
name|long
name|first
init|=
name|firstChar
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|long
name|last
init|=
name|lastChar
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|code
operator|>=
name|first
operator|&&
name|code
operator|<=
name|last
operator|&&
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|WIDTHS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|COSArray
name|widthArray
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
name|COSNumber
name|fontWidthObject
init|=
operator|(
name|COSNumber
operator|)
name|widthArray
operator|.
name|getObject
argument_list|(
call|(
name|int
call|)
argument_list|(
name|code
operator|-
name|first
argument_list|)
argument_list|)
decl_stmt|;
name|fontWidth
operator|=
name|fontWidthObject
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fontWidth
operator|=
name|getFontWidthFromAFMFile
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
name|codeI
argument_list|,
operator|new
name|Float
argument_list|(
name|fontWidth
argument_list|)
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
comment|/**      * This will get the font descriptor for this font.      *      * @return The font descriptor for this font.      *      * @throws IOException If there is an error parsing an AFM file, or unable to      *      create a PDFontDescriptor object.      */
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFontDescriptor
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|fd
init|=
operator|(
name|COSDictionary
operator|)
name|font
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
operator|==
literal|null
condition|)
block|{
name|FontMetric
name|afm
init|=
name|getAFM
argument_list|()
decl_stmt|;
if|if
condition|(
name|afm
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFontDescriptorAFM
argument_list|(
name|afm
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|PDFontDescriptorDictionary
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the font descriptor.      *      * @param fontDescriptor The font descriptor.      */
specifier|public
name|void
name|setFontDescriptor
parameter_list|(
name|PDFontDescriptorDictionary
name|fontDescriptor
parameter_list|)
block|{
name|COSDictionary
name|dic
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fontDescriptor
operator|!=
literal|null
condition|)
block|{
name|dic
operator|=
name|fontDescriptor
operator|.
name|getCOSDictionary
argument_list|()
expr_stmt|;
block|}
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|,
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the ToUnicode stream.      *      * @return The ToUnicode stream.      * @throws IOException If there is an error getting the stream.      */
specifier|public
name|PDStream
name|getToUnicode
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDStream
operator|.
name|createFromCOS
argument_list|(
name|font
operator|.
name|getDictionaryObject
argument_list|(
literal|"ToUnicode"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the ToUnicode stream.      *      * @param unicode The unicode stream.      */
specifier|public
name|void
name|setToUnicode
parameter_list|(
name|PDStream
name|unicode
parameter_list|)
block|{
name|font
operator|.
name|setItem
argument_list|(
literal|"ToUnicode"
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
block|}
end_class

end_unit

