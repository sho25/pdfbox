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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
comment|/**  * A Composite (Type 0) font.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDType0Font
extends|extends
name|PDFont
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
name|PDType0Font
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PDCIDFont
name|descendantFont
decl_stmt|;
specifier|private
name|CMap
name|cMap
decl_stmt|,
name|cMapUCS2
decl_stmt|;
specifier|private
name|boolean
name|isCMapPredefined
decl_stmt|;
specifier|private
name|PDCIDFontType2Embedder
name|embedder
decl_stmt|;
comment|/**     * Loads a TTF to be embedded into a document.     *     * @param doc The PDF document that will hold the embedded font.     * @param file A TrueType font.     * @return A Type0 font with a CIDFontType2 descendant.     * @throws IOException If there is an error reading the font file.     */
specifier|public
specifier|static
name|PDType0Font
name|load
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDType0Font
argument_list|(
name|doc
argument_list|,
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**     * Loads a TTF to be embedded into a document.     *     * @param doc The PDF document that will hold the embedded font.     * @param input A TrueType font.     * @return A Type0 font with a CIDFontType2 descendant.     * @throws IOException If there is an error reading the font stream.     */
specifier|public
specifier|static
name|PDType0Font
name|load
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDType0Font
argument_list|(
name|doc
argument_list|,
name|input
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Loads a TTF to be embedded into a document.      *      * @param doc The PDF document that will hold the embedded font.      * @param input A TrueType font.      * @param embedSubset True if the font will be subset before embedding      * @return A Type0 font with a CIDFontType2 descendant.      * @throws IOException If there is an error reading the font stream.      */
specifier|public
specifier|static
name|PDType0Font
name|load
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|input
parameter_list|,
name|boolean
name|embedSubset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PDType0Font
argument_list|(
name|doc
argument_list|,
name|input
argument_list|,
name|embedSubset
argument_list|)
return|;
block|}
comment|/**      * Constructor for reading a Type0 font from a PDF file.      *       * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDType0Font
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
name|COSArray
name|descendantFonts
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
name|DESCENDANT_FONTS
argument_list|)
decl_stmt|;
name|COSDictionary
name|descendantFontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|descendantFonts
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|descendantFontDictionary
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing descendant font dictionary"
argument_list|)
throw|;
block|}
name|readEncoding
argument_list|()
expr_stmt|;
name|fetchCMapUCS2
argument_list|()
expr_stmt|;
name|descendantFont
operator|=
name|PDFontFactory
operator|.
name|createDescendantFont
argument_list|(
name|descendantFontDictionary
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**     * Private. Creates a new TrueType font for embedding.     */
specifier|private
name|PDType0Font
parameter_list|(
name|PDDocument
name|document
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
name|embedder
operator|=
operator|new
name|PDCIDFontType2Embedder
argument_list|(
name|document
argument_list|,
name|dict
argument_list|,
name|ttfStream
argument_list|,
name|embedSubset
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|descendantFont
operator|=
name|embedder
operator|.
name|getCIDFont
argument_list|()
expr_stmt|;
name|readEncoding
argument_list|()
expr_stmt|;
name|fetchCMapUCS2
argument_list|()
expr_stmt|;
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
if|if
condition|(
operator|!
name|willBeSubset
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This font was created with subsetting disabled"
argument_list|)
throw|;
block|}
name|embedder
operator|.
name|addToSubset
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
name|willBeSubset
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"This font was created with subsetting disabled"
argument_list|)
throw|;
block|}
name|embedder
operator|.
name|subset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|willBeSubset
parameter_list|()
block|{
return|return
name|embedder
operator|.
name|needsSubset
argument_list|()
return|;
block|}
comment|/**      * Reads the font's Encoding entry, which should be a CMap name/stream.      */
specifier|private
name|void
name|readEncoding
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|encoding
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
name|encoding
operator|instanceof
name|COSName
condition|)
block|{
comment|// predefined CMap
name|COSName
name|encodingName
init|=
operator|(
name|COSName
operator|)
name|encoding
decl_stmt|;
name|cMap
operator|=
name|CMapManager
operator|.
name|getPredefinedCMap
argument_list|(
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cMap
operator|!=
literal|null
condition|)
block|{
name|isCMapPredefined
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing required CMap"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|cMap
operator|=
name|readCMap
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
if|if
condition|(
name|cMap
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing required CMap"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|cMap
operator|.
name|hasCIDMappings
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid Encoding CMap in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Fetches the corresponding UCS2 CMap if the font's CMap is predefined.      */
specifier|private
name|void
name|fetchCMapUCS2
parameter_list|()
throws|throws
name|IOException
block|{
comment|// if the font is composite and uses a predefined cmap (excluding Identity-H/V) then
comment|// or if its decendant font uses Adobe-GB1/CNS1/Japan1/Korea1
if|if
condition|(
name|isCMapPredefined
condition|)
block|{
comment|// a) Map the character code to a CID using the font's CMap
comment|// b) Obtain the ROS from the font's CIDSystemInfo
comment|// c) Construct a second CMap name by concatenating the ROS in the format "R-O-UCS2"
comment|// d) Obtain the CMap with the constructed name
comment|// e) Map the CID according to the CMap from step d), producing a Unicode value
name|String
name|cMapName
init|=
literal|null
decl_stmt|;
comment|// get the encoding CMap
name|COSBase
name|encoding
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
name|encoding
operator|instanceof
name|COSName
condition|)
block|{
name|cMapName
operator|=
operator|(
operator|(
name|COSName
operator|)
name|encoding
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
comment|// try to find the corresponding Unicode (UC2) CMap
if|if
condition|(
name|cMapName
operator|!=
literal|null
operator|&&
operator|!
name|cMapName
operator|.
name|equals
argument_list|(
literal|"Identity-H"
argument_list|)
operator|&&
operator|!
name|cMapName
operator|.
name|equals
argument_list|(
literal|"Identity-V"
argument_list|)
condition|)
block|{
name|CMap
name|cMap
init|=
name|CMapManager
operator|.
name|getPredefinedCMap
argument_list|(
name|cMapName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cMap
operator|!=
literal|null
condition|)
block|{
name|String
name|ucs2Name
init|=
name|cMap
operator|.
name|getRegistry
argument_list|()
operator|+
literal|"-"
operator|+
name|cMap
operator|.
name|getOrdering
argument_list|()
operator|+
literal|"-UCS2"
decl_stmt|;
name|CMap
name|ucs2CMap
init|=
name|CMapManager
operator|.
name|getPredefinedCMap
argument_list|(
name|ucs2Name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ucs2CMap
operator|!=
literal|null
condition|)
block|{
name|cMapUCS2
operator|=
name|ucs2CMap
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Returns the PostScript name of the font.      */
specifier|public
name|String
name|getBaseFont
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
return|;
block|}
comment|/**      * Returns the descendant font.      */
specifier|public
name|PDCIDFont
name|getDescendantFont
parameter_list|()
block|{
return|return
name|descendantFont
return|;
block|}
comment|/**      * Returns the font's CMap.      */
specifier|public
name|CMap
name|getCMap
parameter_list|()
block|{
return|return
name|cMap
return|;
block|}
comment|/**      * Returns the font's UCS2 CMap, only present this font uses a predefined CMap.      */
specifier|public
name|CMap
name|getCMapUCS2
parameter_list|()
block|{
return|return
name|cMapUCS2
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
name|descendantFont
operator|.
name|getFontDescriptor
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
return|return
name|descendantFont
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isVertical
parameter_list|()
block|{
return|return
name|cMap
operator|.
name|getWMode
argument_list|()
operator|==
literal|1
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
return|return
name|descendantFont
operator|.
name|getHeight
argument_list|(
name|code
argument_list|)
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
return|return
name|descendantFont
operator|.
name|encode
argument_list|(
name|unicode
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
block|{
return|return
name|descendantFont
operator|.
name|getAverageFontWidth
argument_list|()
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
comment|// units are always 1/1000 text space, font matrix is not used, see FOP-2252
return|return
name|descendantFont
operator|.
name|getPositionVector
argument_list|(
name|code
argument_list|)
operator|.
name|scale
argument_list|(
operator|-
literal|1
operator|/
literal|1000f
argument_list|)
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
if|if
condition|(
name|isVertical
argument_list|()
condition|)
block|{
return|return
operator|new
name|Vector
argument_list|(
literal|0
argument_list|,
name|descendantFont
operator|.
name|getVerticalDisplacementVectorY
argument_list|(
name|code
argument_list|)
operator|/
literal|1000f
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getDisplacement
argument_list|(
name|code
argument_list|)
return|;
block|}
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
return|return
name|descendantFont
operator|.
name|getWidth
argument_list|(
name|code
argument_list|)
return|;
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
return|return
name|descendantFont
operator|.
name|getWidthFromFont
argument_list|(
name|code
argument_list|)
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
name|descendantFont
operator|.
name|isEmbedded
argument_list|()
return|;
block|}
annotation|@
name|Override
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
comment|// try to use a ToUnicode CMap
name|String
name|unicode
init|=
name|super
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
return|return
name|unicode
return|;
block|}
if|if
condition|(
name|isCMapPredefined
operator|&&
name|cMapUCS2
operator|!=
literal|null
condition|)
block|{
comment|// if the font is composite and uses a predefined cmap (excluding Identity-H/V) then
comment|// or if its decendant font uses Adobe-GB1/CNS1/Japan1/Korea1
comment|// a) Map the character code to a character identifier (CID) according to the font?s CMap
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// e) Map the CID according to the CMap from step d), producing a Unicode value
return|return
name|cMapUCS2
operator|.
name|toUnicode
argument_list|(
name|cid
argument_list|)
return|;
block|}
else|else
block|{
comment|// if no value has been produced, there is no way to obtain Unicode for the character.
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getBaseFont
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|BoundingBox
name|getBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|descendantFont
operator|.
name|getBoundingBox
argument_list|()
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
name|cMap
operator|.
name|readCode
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/**      * Returns the CID for the given character code. If not found then CID 0 is returned.      *      * @param code character code      * @return CID      */
specifier|public
name|int
name|codeToCID
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|descendantFont
operator|.
name|codeToCID
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**      * Returns the GID for the given character code.      *      * @param code character code      * @return GID      */
specifier|public
name|int
name|codeToGID
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|descendantFont
operator|.
name|codeToGID
argument_list|(
name|code
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isStandard14
parameter_list|()
block|{
return|return
literal|false
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
name|descendantFont
operator|.
name|isDamaged
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
name|String
name|descendant
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getDescendantFont
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|descendant
operator|=
name|getDescendantFont
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
return|return
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/"
operator|+
name|descendant
operator|+
literal|" "
operator|+
name|getBaseFont
argument_list|()
return|;
block|}
block|}
end_class

end_unit

