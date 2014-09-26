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
specifier|private
name|PDResources
name|type3Resources
init|=
literal|null
decl_stmt|;
specifier|private
name|COSDictionary
name|charProcs
init|=
literal|null
decl_stmt|;
specifier|private
name|Matrix
name|fontMatrix
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
name|Encoding
name|readEncodingFromFont
parameter_list|()
throws|throws
name|IOException
block|{
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
comment|// todo: call getWidthFromFont?
name|LOG
operator|.
name|error
argument_list|(
literal|"No width for glyph "
operator|+
name|code
operator|+
literal|" in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|0
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
block|{
comment|// todo: could these be extracted from the font's stream?
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not suppported"
argument_list|)
throw|;
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
name|fontBBox
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
name|FONT_MATRIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|fontMatrix
operator|=
operator|new
name|Matrix
argument_list|(
name|array
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
comment|/**      * Returns the optional resources of the type3 stream.      *      * @return the resources bound to be used when parsing the type3 stream      */
specifier|public
name|PDResources
name|getType3Resources
parameter_list|()
block|{
if|if
condition|(
name|type3Resources
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|resources
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
name|RESOURCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|type3Resources
operator|=
operator|new
name|PDResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|type3Resources
return|;
block|}
comment|/**      * This will get the fonts bounding box.      *      * @return The fonts bounding box.      */
specifier|public
name|PDRectangle
name|getFontBBox
parameter_list|()
block|{
name|COSArray
name|rect
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
name|rect
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|rect
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
name|PDRectangle
name|rect
init|=
name|getFontBBox
argument_list|()
decl_stmt|;
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
name|getWidth
argument_list|()
argument_list|,
name|rect
operator|.
name|getHeight
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
comment|/**      * Returns the stream of the glyph representing by the given character      *       * @param code char code      * @return the stream to be used to render the glyph      * @throws IOException If something went wrong when getting the stream.      */
specifier|public
name|COSStream
name|getCharStream
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
literal|null
decl_stmt|;
name|String
name|cMapsTo
init|=
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|cMapsTo
operator|!=
literal|null
condition|)
block|{
name|stream
operator|=
operator|(
name|COSStream
operator|)
name|getCharProcs
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|cMapsTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|stream
return|;
block|}
block|}
end_class

end_unit

