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
name|GeneralPath
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
name|cff
operator|.
name|CFFCIDFont
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
name|cff
operator|.
name|CFFType1Font
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
name|Type2CharString
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
name|PDStream
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
comment|/**  * Type 0 CIDFont (CFF).  *   * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDCIDFontType0
extends|extends
name|PDCIDFont
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
name|PDCIDFontType0
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|CFFCIDFont
name|cidFont
decl_stmt|;
comment|// Top DICT that uses CIDFont operators
specifier|private
specifier|final
name|FontBoxFont
name|t1Font
decl_stmt|;
comment|// Top DICT that does not use CIDFont operators
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
name|glyphHeights
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isEmbedded
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isDamaged
decl_stmt|;
specifier|private
name|Float
name|avgWidth
init|=
literal|null
decl_stmt|;
specifier|private
name|Matrix
name|fontMatrix
decl_stmt|;
specifier|private
specifier|final
name|AffineTransform
name|fontMatrixTransform
decl_stmt|;
comment|/**      * Constructor.      *       * @param fontDictionary The font dictionary according to the PDF specification.      * @param parent The parent font.      */
specifier|public
name|PDCIDFontType0
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|,
name|PDType0Font
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|PDStream
name|ff3Stream
init|=
name|fd
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
name|bytes
operator|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|ff3Stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|fontIsDamaged
init|=
literal|false
decl_stmt|;
name|CFFFont
name|cffFont
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bytes
operator|!=
literal|null
operator|&&
name|bytes
operator|.
name|length
operator|>
literal|0
operator|&&
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xff
operator|)
operator|==
literal|'%'
condition|)
block|{
comment|// PDFBOX-2642 contains a corrupt PFB font instead of a CFF
name|LOG
operator|.
name|warn
argument_list|(
literal|"Found PFB but expected embedded CFF font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bytes
operator|!=
literal|null
condition|)
block|{
name|CFFParser
name|cffParser
init|=
operator|new
name|CFFParser
argument_list|()
decl_stmt|;
try|try
block|{
name|cffFont
operator|=
name|cffParser
operator|.
name|parse
argument_list|(
name|bytes
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't read the embedded CFF font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fontIsDamaged
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cffFont
operator|!=
literal|null
condition|)
block|{
comment|// embedded
if|if
condition|(
name|cffFont
operator|instanceof
name|CFFCIDFont
condition|)
block|{
name|cidFont
operator|=
operator|(
name|CFFCIDFont
operator|)
name|cffFont
expr_stmt|;
name|t1Font
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|cidFont
operator|=
literal|null
expr_stmt|;
name|t1Font
operator|=
name|cffFont
expr_stmt|;
block|}
name|isEmbedded
operator|=
literal|true
expr_stmt|;
name|isDamaged
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// find font or substitute
name|CIDFontMapping
name|mapping
init|=
name|FontMapper
operator|.
name|getCIDFont
argument_list|(
name|getBaseFont
argument_list|()
argument_list|,
name|getFontDescriptor
argument_list|()
argument_list|,
name|getCIDSystemInfo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapping
operator|.
name|isCIDFont
argument_list|()
condition|)
block|{
name|cidFont
operator|=
operator|(
name|CFFCIDFont
operator|)
name|mapping
operator|.
name|getFont
argument_list|()
operator|.
name|getCFF
argument_list|()
operator|.
name|getFont
argument_list|()
expr_stmt|;
name|t1Font
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|cidFont
operator|=
literal|null
expr_stmt|;
name|t1Font
operator|=
name|mapping
operator|.
name|getTrueTypeFont
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mapping
operator|.
name|isFallback
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Using fallback "
operator|+
name|t1Font
operator|.
name|getName
argument_list|()
operator|+
literal|" for CID-keyed font "
operator|+
name|getBaseFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|isEmbedded
operator|=
literal|false
expr_stmt|;
name|isDamaged
operator|=
name|fontIsDamaged
expr_stmt|;
block|}
name|fontMatrixTransform
operator|=
name|getFontMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
expr_stmt|;
name|fontMatrixTransform
operator|.
name|scale
argument_list|(
literal|1000
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
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
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
decl_stmt|;
if|if
condition|(
name|cidFont
operator|!=
literal|null
condition|)
block|{
name|numbers
operator|=
name|cidFont
operator|.
name|getFontMatrix
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|numbers
operator|=
name|t1Font
operator|.
name|getFontMatrix
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
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
return|;
block|}
block|}
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
name|fontMatrix
operator|=
operator|new
name|Matrix
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fontMatrix
operator|=
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
expr_stmt|;
block|}
block|}
return|return
name|fontMatrix
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
name|cidFont
operator|!=
literal|null
condition|)
block|{
return|return
name|cidFont
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
else|else
block|{
try|try
block|{
return|return
name|t1Font
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
operator|new
name|BoundingBox
argument_list|()
return|;
block|}
block|}
block|}
comment|/**      * Returns the embedded CFF CIDFont, or null if the substitute is not a CFF font.      */
specifier|public
name|CFFFont
name|getCFFFont
parameter_list|()
block|{
if|if
condition|(
name|cidFont
operator|!=
literal|null
condition|)
block|{
return|return
name|cidFont
return|;
block|}
elseif|else
if|if
condition|(
name|t1Font
operator|instanceof
name|CFFType1Font
condition|)
block|{
return|return
operator|(
name|CFFType1Font
operator|)
name|t1Font
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the Type 2 charstring for the given CID, or null if the substituted font does not      * contain Type 2 charstrings.      *      * @param cid CID      * @throws IOException if the charstring could not be read      */
specifier|public
name|Type2CharString
name|getType2CharString
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|cidFont
operator|!=
literal|null
condition|)
block|{
return|return
name|cidFont
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|t1Font
operator|instanceof
name|CFFType1Font
condition|)
block|{
return|return
operator|(
operator|(
name|CFFType1Font
operator|)
name|t1Font
operator|)
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the name of the glyph with the given character code. This is done by looking up the      * code in the parent font's ToUnicode map and generating a glyph name from that.      */
specifier|private
name|String
name|getGlyphName
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|unicodes
init|=
name|parent
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicodes
operator|==
literal|null
condition|)
block|{
return|return
literal|".notdef"
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"uni%04X"
argument_list|,
name|unicodes
operator|.
name|codePointAt
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|Type2CharString
name|charstring
init|=
name|getType2CharString
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|charstring
operator|!=
literal|null
condition|)
block|{
return|return
name|charstring
operator|.
name|getPath
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|isEmbedded
operator|&&
name|t1Font
operator|instanceof
name|CFFType1Font
condition|)
block|{
return|return
operator|(
operator|(
name|CFFType1Font
operator|)
name|t1Font
operator|)
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|t1Font
operator|.
name|getPath
argument_list|(
name|getGlyphName
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|Type2CharString
name|charstring
init|=
name|getType2CharString
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|charstring
operator|!=
literal|null
condition|)
block|{
return|return
name|charstring
operator|.
name|getGID
argument_list|()
operator|!=
literal|0
return|;
block|}
elseif|else
if|if
condition|(
name|isEmbedded
operator|&&
name|t1Font
operator|instanceof
name|CFFType1Font
condition|)
block|{
return|return
operator|(
operator|(
name|CFFType1Font
operator|)
name|t1Font
operator|)
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
operator|.
name|getGID
argument_list|()
operator|!=
literal|0
return|;
block|}
else|else
block|{
return|return
name|t1Font
operator|.
name|hasGlyph
argument_list|(
name|getGlyphName
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the CID for the given character code. If not found then CID 0 is returned.      *      * @param code character code      * @return CID      */
annotation|@
name|Override
specifier|public
name|int
name|codeToCID
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|parent
operator|.
name|getCMap
argument_list|()
operator|.
name|toCID
argument_list|(
name|code
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|codeToGID
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidFont
operator|!=
literal|null
condition|)
block|{
comment|// The CIDs shall be used to determine the GID value for the glyph procedure using the
comment|// charset table in the CFF program
return|return
name|cidFont
operator|.
name|getCharset
argument_list|()
operator|.
name|getGIDForCID
argument_list|(
name|cid
argument_list|)
return|;
block|}
else|else
block|{
comment|// The CIDs shall be used directly as GID values
return|return
name|cid
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|encode
parameter_list|(
name|int
name|unicode
parameter_list|)
block|{
comment|// todo: we can use a known character collection CMap for a CIDFont
comment|//       and an Encoding for Type 1-equivalent
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|float
name|width
decl_stmt|;
if|if
condition|(
name|cidFont
operator|!=
literal|null
condition|)
block|{
name|width
operator|=
name|getType2CharString
argument_list|(
name|cid
argument_list|)
operator|.
name|getWidth
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isEmbedded
operator|&&
name|t1Font
operator|instanceof
name|CFFType1Font
condition|)
block|{
name|width
operator|=
operator|(
operator|(
name|CFFType1Font
operator|)
name|t1Font
operator|)
operator|.
name|getType2CharString
argument_list|(
name|cid
argument_list|)
operator|.
name|getWidth
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|width
operator|=
name|t1Font
operator|.
name|getWidth
argument_list|(
name|getGlyphName
argument_list|(
name|code
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Point2D
name|p
init|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|width
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|fontMatrixTransform
operator|.
name|transform
argument_list|(
name|p
argument_list|,
name|p
argument_list|)
expr_stmt|;
return|return
operator|(
name|float
operator|)
name|p
operator|.
name|getX
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
name|isEmbedded
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDamaged
parameter_list|()
block|{
return|return
name|isDamaged
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
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
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
name|cid
argument_list|)
condition|)
block|{
name|height
operator|=
operator|(
name|float
operator|)
name|getType2CharString
argument_list|(
name|cid
argument_list|)
operator|.
name|getBounds
argument_list|()
operator|.
name|getHeight
argument_list|()
expr_stmt|;
name|glyphHeights
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
return|return
name|height
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
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
name|getAverageCharacterWidth
argument_list|()
expr_stmt|;
block|}
return|return
name|avgWidth
return|;
block|}
comment|// todo: this is a replacement for FontMetrics method
specifier|private
name|float
name|getAverageCharacterWidth
parameter_list|()
block|{
comment|// todo: not implemented, highly suspect
return|return
literal|500
return|;
block|}
block|}
end_class

end_unit

