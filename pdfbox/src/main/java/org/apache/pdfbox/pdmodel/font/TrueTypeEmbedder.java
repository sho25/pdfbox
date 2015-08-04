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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|ttf
operator|.
name|CmapSubtable
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
name|ttf
operator|.
name|HeaderTable
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
name|ttf
operator|.
name|HorizontalHeaderTable
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
name|ttf
operator|.
name|OS2WindowsMetricsTable
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
name|ttf
operator|.
name|PostScriptTable
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
name|ttf
operator|.
name|TTFParser
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
name|ttf
operator|.
name|TTFSubsetter
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
name|ttf
operator|.
name|TrueTypeFont
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
name|PDDocument
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
comment|/**  * Common functionality for embedding TrueType fonts.  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|abstract
class|class
name|TrueTypeEmbedder
implements|implements
name|Subsetter
block|{
specifier|private
specifier|static
specifier|final
name|int
name|ITALIC
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|OBLIQUE
init|=
literal|256
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BASE25
init|=
literal|"BCDEFGHIJKLMNOPQRSTUVWXYZ"
decl_stmt|;
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
specifier|protected
name|TrueTypeFont
name|ttf
decl_stmt|;
specifier|protected
name|PDFontDescriptor
name|fontDescriptor
decl_stmt|;
specifier|protected
specifier|final
name|CmapSubtable
name|cmap
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|subsetCodePoints
init|=
operator|new
name|HashSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|embedSubset
decl_stmt|;
comment|/**      * Creates a new TrueType font for embedding.      */
name|TrueTypeEmbedder
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSDictionary
name|dict
parameter_list|,
name|InputStream
name|ttfStream
parameter_list|,
name|boolean
name|embedSubset
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|embedSubset
operator|=
name|embedSubset
expr_stmt|;
name|buildFontFile2
argument_list|(
name|ttfStream
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
name|ttf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// choose a Unicode "cmap"
name|cmap
operator|=
name|ttf
operator|.
name|getUnicodeCmap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|buildFontFile2
parameter_list|(
name|InputStream
name|ttfStream
parameter_list|)
throws|throws
name|IOException
block|{
name|PDStream
name|stream
init|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|,
name|ttfStream
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|,
name|stream
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// as the stream was closed within the PDStream constructor, we have to recreate it
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
name|stream
operator|.
name|createInputStream
argument_list|()
expr_stmt|;
name|ttf
operator|=
operator|new
name|TTFParser
argument_list|()
operator|.
name|parseEmbedded
argument_list|(
name|input
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isEmbeddingPermitted
argument_list|(
name|ttf
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"This font does not permit embedding"
argument_list|)
throw|;
block|}
if|if
condition|(
name|fontDescriptor
operator|==
literal|null
condition|)
block|{
name|fontDescriptor
operator|=
name|createFontDescriptor
argument_list|(
name|ttf
argument_list|)
expr_stmt|;
block|}
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
name|fontDescriptor
operator|.
name|setFontFile2
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if the fsType in the OS/2 table permits embedding.      */
specifier|private
name|boolean
name|isEmbeddingPermitted
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|fsType
init|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getFsType
argument_list|()
decl_stmt|;
name|int
name|exclusive
init|=
name|fsType
operator|&
literal|0x8
decl_stmt|;
comment|// bits 0-3 are a set of exclusive bits
if|if
condition|(
operator|(
name|exclusive
operator|&
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_RESTRICTED
operator|)
operator|==
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_RESTRICTED
condition|)
block|{
comment|// restricted License embedding
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|(
name|exclusive
operator|&
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_BITMAP_ONLY
operator|)
operator|==
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_BITMAP_ONLY
condition|)
block|{
comment|// bitmap embedding only
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if the fsType in the OS/2 table permits subsetting.      */
specifier|private
name|boolean
name|isSubsettingPermitted
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|fsType
init|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getFsType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|fsType
operator|&
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_NO_SUBSETTING
operator|)
operator|==
name|OS2WindowsMetricsTable
operator|.
name|FSTYPE_NO_SUBSETTING
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Creates a new font descriptor dictionary for the given TTF.      */
specifier|private
name|PDFontDescriptor
name|createFontDescriptor
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFontDescriptor
name|fd
init|=
operator|new
name|PDFontDescriptor
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setFontName
argument_list|(
name|ttf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|OS2WindowsMetricsTable
name|os2
init|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
decl_stmt|;
name|PostScriptTable
name|post
init|=
name|ttf
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
comment|// Flags
name|fd
operator|.
name|setFixedPitch
argument_list|(
name|post
operator|.
name|getIsFixedPitch
argument_list|()
operator|>
literal|0
operator|||
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
operator|.
name|getNumberOfHMetrics
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|int
name|fsSelection
init|=
name|os2
operator|.
name|getFsSelection
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setItalic
argument_list|(
operator|(
name|fsSelection
operator|&
name|ITALIC
operator|)
operator|==
name|fsSelection
operator|||
operator|(
name|fsSelection
operator|&
name|OBLIQUE
operator|)
operator|==
name|fsSelection
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|os2
operator|.
name|getFamilyClass
argument_list|()
condition|)
block|{
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_CLAREDON_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_FREEFORM_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_MODERN_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_OLDSTYLE_SERIFS
case|:
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_SLAB_SERIFS
case|:
name|fd
operator|.
name|setSerif
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|FAMILY_CLASS_SCRIPTS
case|:
name|fd
operator|.
name|setScript
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
name|fd
operator|.
name|setFontWeight
argument_list|(
name|os2
operator|.
name|getWeightClass
argument_list|()
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setSymbolic
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setNonSymbolic
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// ItalicAngle
name|fd
operator|.
name|setItalicAngle
argument_list|(
name|post
operator|.
name|getItalicAngle
argument_list|()
argument_list|)
expr_stmt|;
comment|// FontBBox
name|HeaderTable
name|header
init|=
name|ttf
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|float
name|scaling
init|=
literal|1000f
operator|/
name|header
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|header
operator|.
name|getXMin
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|header
operator|.
name|getYMin
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|header
operator|.
name|getXMax
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|header
operator|.
name|getYMax
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setFontBoundingBox
argument_list|(
name|rect
argument_list|)
expr_stmt|;
comment|// Ascent, Descent
name|HorizontalHeaderTable
name|hHeader
init|=
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setAscent
argument_list|(
name|hHeader
operator|.
name|getAscender
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setDescent
argument_list|(
name|hHeader
operator|.
name|getDescender
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
comment|// CapHeight, XHeight
if|if
condition|(
name|os2
operator|.
name|getVersion
argument_list|()
operator|>=
literal|1.2
condition|)
block|{
name|fd
operator|.
name|setCapHeight
argument_list|(
name|os2
operator|.
name|getCapHeight
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setXHeight
argument_list|(
name|os2
operator|.
name|getHeight
argument_list|()
operator|*
name|scaling
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// estimate by summing the typographical +ve ascender and -ve descender
name|fd
operator|.
name|setCapHeight
argument_list|(
operator|(
name|os2
operator|.
name|getTypoAscender
argument_list|()
operator|+
name|os2
operator|.
name|getTypoDescender
argument_list|()
operator|)
operator|*
name|scaling
argument_list|)
expr_stmt|;
comment|// estimate by halving the typographical ascender
name|fd
operator|.
name|setXHeight
argument_list|(
name|os2
operator|.
name|getTypoAscender
argument_list|()
operator|/
literal|2.0f
operator|*
name|scaling
argument_list|)
expr_stmt|;
block|}
comment|// StemV - there's no true TTF equivalent of this, so we estimate it
name|fd
operator|.
name|setStemV
argument_list|(
name|fd
operator|.
name|getFontBoundingBox
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|*
literal|.13f
argument_list|)
expr_stmt|;
return|return
name|fd
return|;
block|}
comment|/**      * Returns the FontBox font.      */
specifier|public
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|()
block|{
return|return
name|ttf
return|;
block|}
comment|/**      * Returns the font descriptor.      */
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
return|return
name|fontDescriptor
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addToSubset
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
name|subsetCodePoints
operator|.
name|add
argument_list|(
name|codePoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|subset
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|isSubsettingPermitted
argument_list|(
name|ttf
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"This font does not permit subsetting"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|embedSubset
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Subsetting is disabled"
argument_list|)
throw|;
block|}
comment|// PDF spec required tables (if present), all others will be removed
name|List
argument_list|<
name|String
argument_list|>
name|tables
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"head"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hhea"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"loca"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"maxp"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"cvt "
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"prep"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"glyf"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"hmtx"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
literal|"fpgm"
argument_list|)
expr_stmt|;
comment|// Windows ClearType
name|tables
operator|.
name|add
argument_list|(
literal|"gasp"
argument_list|)
expr_stmt|;
comment|// set the GIDs to subset
name|TTFSubsetter
name|subsetter
init|=
operator|new
name|TTFSubsetter
argument_list|(
name|getTrueTypeFont
argument_list|()
argument_list|,
name|tables
argument_list|)
decl_stmt|;
name|subsetter
operator|.
name|addAll
argument_list|(
name|subsetCodePoints
argument_list|)
expr_stmt|;
comment|// calculate deterministic tag based on the chosen subset
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCid
init|=
name|subsetter
operator|.
name|getGIDMap
argument_list|()
decl_stmt|;
name|String
name|tag
init|=
name|getTag
argument_list|(
name|gidToCid
argument_list|)
decl_stmt|;
name|subsetter
operator|.
name|setPrefix
argument_list|(
name|tag
argument_list|)
expr_stmt|;
comment|// save the subset font
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|subsetter
operator|.
name|writeToStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// re-build the embedded font
name|buildSubset
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|tag
argument_list|,
name|gidToCid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if the font needs to be subset.      */
specifier|public
name|boolean
name|needsSubset
parameter_list|()
block|{
return|return
name|embedSubset
return|;
block|}
comment|/**      * Rebuild a font subset.      */
specifier|protected
specifier|abstract
name|void
name|buildSubset
parameter_list|(
name|InputStream
name|ttfSubset
parameter_list|,
name|String
name|tag
parameter_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCid
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns an uppercase 6-character unique tag for the given subset.      */
specifier|public
name|String
name|getTag
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCid
parameter_list|)
block|{
comment|// deterministic
name|long
name|num
init|=
name|gidToCid
operator|.
name|hashCode
argument_list|()
decl_stmt|;
comment|// base25 encode
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
do|do
block|{
name|long
name|div
init|=
name|num
operator|/
literal|25
decl_stmt|;
name|int
name|mod
init|=
call|(
name|int
call|)
argument_list|(
name|num
operator|%
literal|25
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|BASE25
operator|.
name|charAt
argument_list|(
name|mod
argument_list|)
argument_list|)
expr_stmt|;
name|num
operator|=
name|div
expr_stmt|;
block|}
do|while
condition|(
name|num
operator|!=
literal|0
operator|&&
name|sb
operator|.
name|length
argument_list|()
operator|<
literal|6
condition|)
do|;
comment|// pad
while|while
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|<
literal|6
condition|)
block|{
name|sb
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|'A'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'+'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

