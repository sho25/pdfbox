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
name|fontbox
operator|.
name|FontBoxFont
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
name|pdmodel
operator|.
name|PDResources
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
name|pdmodel
operator|.
name|font
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
comment|/**  * A PostScript Type 3 Font.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDType3Font
extends|extends
name|PDSimpleFont
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
name|PDType3Font
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDResources
name|resources
decl_stmt|;
specifier|private
name|COSDictionary
name|charProcs
decl_stmt|;
specifier|private
name|Matrix
name|fontMatrix
decl_stmt|;
specifier|private
name|BoundingBox
name|fontBBox
decl_stmt|;
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDType3Font
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
name|readEncoding
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|readEncoding
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|encodingBase
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodingBase
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|encodingName
init|=
operator|(
name|COSName
operator|)
name|encodingBase
decl_stmt|;
name|encoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|encodingName
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|encodingBase
operator|instanceof
name|COSDictionary
condition|)
block|{
name|encoding
operator|=
operator|new
name|DictionaryEncoding
argument_list|(
operator|(
name|COSDictionary
operator|)
name|encodingBase
argument_list|)
expr_stmt|;
block|}
name|glyphList
operator|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Encoding
name|readEncodingFromFont
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Type 3 fonts do not have a built-in encoding
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for Type 3 fonts"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|Boolean
name|isFontSymbolic
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Type 3 fonts do not use vector paths
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for Type 3 fonts"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|base
init|=
name|getCharProcs
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|base
operator|instanceof
name|COSStream
return|;
block|}
annotation|@
name|Override
specifier|public
name|FontBoxFont
name|getFontBoxFont
parameter_list|()
block|{
comment|// Type 3 fonts do not use FontBox fonts
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for Type 3 fonts"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
name|getFontMatrix
argument_list|()
operator|.
name|transform
argument_list|(
operator|new
name|Vector
argument_list|(
name|getWidth
argument_list|(
name|code
argument_list|)
argument_list|,
literal|0
argument_list|)
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
operator|!
name|getWidths
argument_list|()
operator|.
name|isEmpty
argument_list|()
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
name|Float
name|w
init|=
name|getWidths
argument_list|()
operator|.
name|get
argument_list|(
name|code
operator|-
name|firstChar
argument_list|)
decl_stmt|;
return|return
name|w
operator|==
literal|null
condition|?
literal|0
else|:
name|w
return|;
block|}
else|else
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
condition|)
block|{
return|return
name|fd
operator|.
name|getMissingWidth
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|getWidthFromFont
argument_list|(
name|code
argument_list|)
return|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|float
name|getWidthFromFont
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|PDType3CharProc
name|charProc
init|=
name|getCharProc
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|charProc
operator|==
literal|null
operator|||
name|charProc
operator|.
name|getContentStream
argument_list|()
operator|==
literal|null
operator|||
name|charProc
operator|.
name|getContentStream
argument_list|()
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|charProc
operator|.
name|getWidth
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmbedded
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getHeight
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
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
comment|// the following values are all more or less accurate at least all are average
comment|// values. Maybe we'll find another way to get those value for every single glyph
comment|// in the future if needed
name|PDRectangle
name|bbox
init|=
name|desc
operator|.
name|getFontBoundingBox
argument_list|()
decl_stmt|;
name|float
name|retval
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|bbox
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|retval
argument_list|,
literal|0
argument_list|)
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
name|Float
operator|.
name|compare
argument_list|(
name|retval
argument_list|,
literal|0
argument_list|)
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
name|Float
operator|.
name|compare
argument_list|(
name|retval
argument_list|,
literal|0
argument_list|)
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
return|return
name|retval
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|protected
name|byte
index|[]
name|encode
parameter_list|(
name|int
name|unicode
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented: Type3"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|readCode
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|in
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Matrix
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
name|COSBase
name|base
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_MATRIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|fontMatrix
operator|=
operator|new
name|Matrix
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
block|}
return|return
name|fontMatrix
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDamaged
parameter_list|()
block|{
comment|// there's no font file to load
return|return
literal|false
return|;
block|}
comment|/**      * Returns the optional resources of the type3 stream.      *      * @return the resources bound to be used when parsing the type3 stream      */
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|COSBase
name|base
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|this
operator|.
name|resources
operator|=
operator|new
name|PDResources
argument_list|(
operator|(
name|COSDictionary
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resources
return|;
block|}
comment|/**      * This will get the fonts bounding box from its dictionary.      *      * @return The fonts bounding box.      */
specifier|public
name|PDRectangle
name|getFontBBox
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_BBOX
argument_list|)
decl_stmt|;
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
annotation|@
name|Override
specifier|public
name|BoundingBox
name|getBoundingBox
parameter_list|()
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
name|generateBoundingBox
argument_list|()
expr_stmt|;
block|}
return|return
name|fontBBox
return|;
block|}
specifier|private
name|BoundingBox
name|generateBoundingBox
parameter_list|()
block|{
name|PDRectangle
name|rect
init|=
name|getFontBBox
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isNonZeroBoundingBox
argument_list|(
name|rect
argument_list|)
condition|)
block|{
comment|// Plan B: get the max bounding box of the glyphs
name|COSDictionary
name|cp
init|=
name|getCharProcs
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|name
range|:
name|cp
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|base
init|=
name|cp
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
name|PDType3CharProc
name|charProc
init|=
operator|new
name|PDType3CharProc
argument_list|(
name|this
argument_list|,
operator|(
name|COSStream
operator|)
name|base
argument_list|)
decl_stmt|;
try|try
block|{
name|PDRectangle
name|glyphBBox
init|=
name|charProc
operator|.
name|getGlyphBBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|glyphBBox
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|rect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|glyphBBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|rect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|glyphBBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|rect
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
name|glyphBBox
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|rect
operator|.
name|getUpperRightY
argument_list|()
argument_list|,
name|glyphBBox
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
name|LOG
operator|.
name|debug
argument_list|(
literal|"error getting the glyph bounding box - font bounding box will be used"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
operator|new
name|BoundingBox
argument_list|(
name|rect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|rect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|rect
operator|.
name|getUpperRightX
argument_list|()
argument_list|,
name|rect
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the dictionary containing all streams to be used to render the glyphs.      *       * @return the dictionary containing all glyph streams.      */
specifier|public
name|COSDictionary
name|getCharProcs
parameter_list|()
block|{
if|if
condition|(
name|charProcs
operator|==
literal|null
condition|)
block|{
name|charProcs
operator|=
operator|(
name|COSDictionary
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CHAR_PROCS
argument_list|)
expr_stmt|;
block|}
return|return
name|charProcs
return|;
block|}
comment|/**      * Returns the stream of the glyph for the given character code      *       * @param code character code      * @return the stream to be used to render the glyph      */
specifier|public
name|PDType3CharProc
name|getCharProc
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|String
name|name
init|=
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|COSBase
name|base
init|=
name|getCharProcs
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|new
name|PDType3CharProc
argument_list|(
name|this
argument_list|,
operator|(
name|COSStream
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

