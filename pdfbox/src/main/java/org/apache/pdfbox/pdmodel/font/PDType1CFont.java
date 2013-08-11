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
name|Arrays
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
name|List
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
name|afm
operator|.
name|AFMParser
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
name|fontbox
operator|.
name|cff
operator|.
name|AFMFormatter
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
name|CFFParser
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
name|COSFloat
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
name|PDMatrix
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
comment|/**  * This class represents a CFF/Type2 Font (aka Type1C Font).  *   * @author Villu Ruusmann  *   */
end_comment

begin_class
specifier|public
class|class
name|PDType1CFont
extends|extends
name|PDSimpleFont
block|{
specifier|private
name|CFFFont
name|cffFont
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|fontname
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToCharacter
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|characterToCode
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|FontMetric
name|fontMetric
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Float
argument_list|>
name|glyphWidths
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Float
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Float
argument_list|>
name|glyphHeights
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Float
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Float
name|avgWidth
init|=
literal|null
decl_stmt|;
specifier|private
name|PDRectangle
name|fontBBox
init|=
literal|null
decl_stmt|;
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
name|PDType1CFont
operator|.
name|class
argument_list|)
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
specifier|private
name|Map
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
comment|/**      * Constructor.      *       * @param fontDictionary the corresponding dictionary      * @throws IOException it something went wrong      */
specifier|public
name|PDType1CFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
name|load
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|bytes
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
name|String
name|character
init|=
name|getCharacter
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|character
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No character for code "
operator|+
operator|(
name|bytes
index|[
name|offset
index|]
operator|&
literal|0xff
operator|)
operator|+
literal|" in "
operator|+
name|fontname
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|character
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|encodeToCID
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|length
operator|>
literal|2
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|code
init|=
name|bytes
index|[
name|offset
index|]
operator|&
literal|0xff
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|2
condition|)
block|{
name|code
operator|=
name|code
operator|*
literal|256
operator|+
name|bytes
index|[
name|offset
operator|+
literal|1
index|]
operator|&
literal|0xff
expr_stmt|;
block|}
return|return
name|code
return|;
block|}
specifier|private
name|String
name|getCharacter
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|int
name|code
init|=
name|encodeToCID
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|code
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|String
operator|)
name|codeToCharacter
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|float
name|getFontWidth
parameter_list|(
name|byte
index|[]
name|bytes
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
name|String
name|name
init|=
name|getName
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|&&
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|SPACE_BYTES
argument_list|,
name|bytes
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No name for code "
operator|+
operator|(
name|bytes
index|[
name|offset
index|]
operator|&
literal|0xff
operator|)
operator|+
literal|" in "
operator|+
name|fontname
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
name|Float
name|width
init|=
operator|(
name|Float
operator|)
name|glyphWidths
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|width
operator|==
literal|null
condition|)
block|{
name|width
operator|=
name|Float
operator|.
name|valueOf
argument_list|(
name|getFontMetric
argument_list|()
operator|.
name|getCharacterWidth
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|glyphWidths
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
return|return
name|width
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|float
name|getFontHeight
parameter_list|(
name|byte
index|[]
name|bytes
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
name|String
name|name
init|=
name|getName
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No name for code "
operator|+
operator|(
name|bytes
index|[
name|offset
index|]
operator|&
literal|0xff
operator|)
operator|+
literal|" in "
operator|+
name|fontname
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
name|float
name|height
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|glyphHeights
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|height
operator|=
name|getFontMetric
argument_list|()
operator|.
name|getCharacterHeight
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|glyphHeights
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
return|return
name|height
return|;
block|}
specifier|private
name|String
name|getName
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|length
operator|>
literal|2
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|code
init|=
name|bytes
index|[
name|offset
index|]
operator|&
literal|0xff
decl_stmt|;
if|if
condition|(
name|length
operator|==
literal|2
condition|)
block|{
name|code
operator|=
name|code
operator|*
literal|256
operator|+
name|bytes
index|[
name|offset
operator|+
literal|1
index|]
operator|&
literal|0xff
expr_stmt|;
block|}
return|return
name|codeToName
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|float
name|getStringWidth
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|width
init|=
literal|0
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
name|string
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|character
init|=
name|string
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Integer
name|code
init|=
name|getCode
argument_list|(
name|character
argument_list|)
decl_stmt|;
if|if
condition|(
name|code
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No code for character "
operator|+
name|character
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
name|width
operator|+=
name|getFontWidth
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|code
operator|.
name|intValue
argument_list|()
block|}
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|width
return|;
block|}
specifier|private
name|Integer
name|getCode
parameter_list|(
name|String
name|character
parameter_list|)
block|{
return|return
name|characterToCode
operator|.
name|get
argument_list|(
name|character
argument_list|)
return|;
block|}
comment|/**      * Returns the glyph index of the given character code.      *       * @param code the character code      * @return the glyph index      */
specifier|protected
name|Integer
name|getGlyphIndex
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|codeToGlyph
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|avgWidth
operator|==
literal|null
condition|)
block|{
name|avgWidth
operator|=
name|getFontMetric
argument_list|()
operator|.
name|getAverageCharacterWidth
argument_list|()
expr_stmt|;
block|}
return|return
name|avgWidth
operator|.
name|floatValue
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PDRectangle
name|getFontBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|fontBBox
operator|==
literal|null
condition|)
block|{
name|fontBBox
operator|=
operator|new
name|PDRectangle
argument_list|(
name|getFontMetric
argument_list|()
operator|.
name|getFontBBox
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|fontBBox
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PDMatrix
name|getFontMatrix
parameter_list|()
block|{
if|if
condition|(
name|fontMatrix
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|cffFont
operator|.
name|getProperty
argument_list|(
literal|"FontMatrix"
argument_list|)
decl_stmt|;
if|if
condition|(
name|numbers
operator|!=
literal|null
operator|&&
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|6
condition|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Number
name|number
range|:
name|numbers
control|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|number
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|fontMatrix
operator|=
operator|new
name|PDMatrix
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|getFontMatrix
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|fontMatrix
return|;
block|}
specifier|private
name|FontMetric
name|getFontMetric
parameter_list|()
block|{
if|if
condition|(
name|fontMetric
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|fontMetric
operator|=
name|prepareFontMetric
argument_list|(
name|cffFont
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
literal|"An error occured while extracting the font metrics!"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fontMetric
return|;
block|}
specifier|private
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|cffBytes
init|=
name|loadBytes
argument_list|()
decl_stmt|;
name|CFFParser
name|cffParser
init|=
operator|new
name|CFFParser
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CFFFont
argument_list|>
name|fonts
init|=
name|cffParser
operator|.
name|parse
argument_list|(
name|cffBytes
argument_list|)
decl_stmt|;
name|String
name|baseFontName
init|=
name|getBaseFont
argument_list|()
decl_stmt|;
if|if
condition|(
name|fonts
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
name|baseFontName
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CFFFont
name|font
range|:
name|fonts
control|)
block|{
if|if
condition|(
name|baseFontName
operator|.
name|equals
argument_list|(
name|font
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|cffFont
operator|=
name|font
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|cffFont
operator|==
literal|null
condition|)
block|{
name|cffFont
operator|=
operator|(
name|CFFFont
operator|)
name|fonts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// chache the font name
name|fontname
operator|=
name|cffFont
operator|.
name|getName
argument_list|()
expr_stmt|;
comment|// TODO is this really needed?
name|Number
name|defaultWidthX
init|=
operator|(
name|Number
operator|)
name|this
operator|.
name|cffFont
operator|.
name|getProperty
argument_list|(
literal|"defaultWidthX"
argument_list|)
decl_stmt|;
name|glyphWidths
operator|.
name|put
argument_list|(
literal|null
argument_list|,
name|Float
operator|.
name|valueOf
argument_list|(
name|defaultWidthX
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// calculate some mappings to be used for rendering and text extraction
name|Encoding
name|encoding
init|=
name|getFontEncoding
argument_list|()
decl_stmt|;
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
name|String
name|character
init|=
literal|null
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
name|character
operator|=
name|encoding
operator|.
name|getCharacter
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|character
operator|==
literal|null
condition|)
block|{
name|character
operator|=
name|Encoding
operator|.
name|getCharacterForName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|character
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"uni"
operator|+
name|hexString
argument_list|(
name|code
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|character
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|Character
operator|.
name|toChars
argument_list|(
name|code
argument_list|)
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
operator|++
argument_list|)
expr_stmt|;
name|codeToName
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|codeToCharacter
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|character
argument_list|)
expr_stmt|;
name|characterToCode
operator|.
name|put
argument_list|(
name|character
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|byte
index|[]
name|loadBytes
parameter_list|()
throws|throws
name|IOException
block|{
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
operator|&&
name|fd
operator|instanceof
name|PDFontDescriptorDictionary
condition|)
block|{
name|PDStream
name|ff3Stream
init|=
operator|(
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fd
operator|)
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
if|if
condition|(
name|ff3Stream
operator|!=
literal|null
condition|)
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|ff3Stream
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|count
init|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|os
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|os
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|String
name|hexString
parameter_list|(
name|int
name|code
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|String
name|string
init|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|code
argument_list|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
while|while
condition|(
name|string
operator|.
name|length
argument_list|()
operator|<
name|length
condition|)
block|{
name|string
operator|=
operator|(
literal|"0"
operator|+
name|string
operator|)
expr_stmt|;
block|}
return|return
name|string
return|;
block|}
specifier|private
name|FontMetric
name|prepareFontMetric
parameter_list|(
name|CFFFont
name|font
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|afmBytes
init|=
name|AFMFormatter
operator|.
name|format
argument_list|(
name|font
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|afmBytes
argument_list|)
decl_stmt|;
try|try
block|{
name|AFMParser
name|afmParser
init|=
operator|new
name|AFMParser
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|afmParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|FontMetric
name|result
init|=
name|afmParser
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// Replace default FontBBox value with a newly computed one
name|BoundingBox
name|bounds
init|=
name|result
operator|.
name|getFontBBox
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|int
operator|)
name|bounds
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|int
operator|)
name|bounds
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|int
operator|)
name|bounds
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|int
operator|)
name|bounds
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|font
operator|.
name|addValueToTopDict
argument_list|(
literal|"FontBBox"
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the raw data of the font as CFFFont.      *       * @return the cffFont      * @throws IOException if something went wrong      */
specifier|public
name|CFFFont
name|getCFFFont
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|cffFont
return|;
block|}
block|}
end_class

end_unit

