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
name|Map
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
comment|/**  * A CIDFont. A CIDFont is a PDF object that contains information about a CIDFont program. Although  * its Type value is Font, a CIDFont is not actually a font.  *  *<p>It is not usually necessary to use this class directly, prefer {@link PDType0Font}.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDCIDFont
implements|implements
name|COSObjectable
implements|,
name|PDFontLike
implements|,
name|PDVectorFont
block|{
specifier|protected
specifier|final
name|PDType0Font
name|parent
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
name|widths
decl_stmt|;
specifier|private
name|float
name|defaultWidth
decl_stmt|;
specifier|private
name|float
name|averageWidth
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
name|verticalDisplacementY
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// w1y
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Vector
argument_list|>
name|positionVectors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// v
specifier|private
name|float
index|[]
name|dw2
init|=
operator|new
name|float
index|[]
block|{
literal|880
block|,
operator|-
literal|1000
block|}
decl_stmt|;
specifier|protected
specifier|final
name|COSDictionary
name|dict
decl_stmt|;
specifier|private
name|PDFontDescriptor
name|fontDescriptor
decl_stmt|;
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
name|PDCIDFont
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
name|this
operator|.
name|dict
operator|=
name|fontDictionary
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|readWidths
argument_list|()
expr_stmt|;
name|readVerticalDisplacements
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|readWidths
parameter_list|()
block|{
name|widths
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|COSBase
name|wBase
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|W
argument_list|)
decl_stmt|;
if|if
condition|(
name|wBase
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|wArray
init|=
operator|(
name|COSArray
operator|)
name|wBase
decl_stmt|;
name|int
name|size
init|=
name|wArray
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|counter
operator|<
name|size
condition|)
block|{
name|COSNumber
name|firstCode
init|=
operator|(
name|COSNumber
operator|)
name|wArray
operator|.
name|getObject
argument_list|(
name|counter
operator|++
argument_list|)
decl_stmt|;
name|COSBase
name|next
init|=
name|wArray
operator|.
name|getObject
argument_list|(
name|counter
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|next
decl_stmt|;
name|int
name|startRange
init|=
name|firstCode
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|int
name|arraySize
init|=
name|array
operator|.
name|size
argument_list|()
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
name|arraySize
condition|;
name|i
operator|++
control|)
block|{
name|COSNumber
name|width
init|=
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|widths
operator|.
name|put
argument_list|(
name|startRange
operator|+
name|i
argument_list|,
name|width
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|COSNumber
name|secondCode
init|=
operator|(
name|COSNumber
operator|)
name|next
decl_stmt|;
name|COSNumber
name|rangeWidth
init|=
operator|(
name|COSNumber
operator|)
name|wArray
operator|.
name|getObject
argument_list|(
name|counter
operator|++
argument_list|)
decl_stmt|;
name|int
name|startRange
init|=
name|firstCode
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|int
name|endRange
init|=
name|secondCode
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|float
name|width
init|=
name|rangeWidth
operator|.
name|floatValue
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startRange
init|;
name|i
operator|<=
name|endRange
condition|;
name|i
operator|++
control|)
block|{
name|widths
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|readVerticalDisplacements
parameter_list|()
block|{
comment|// default position vector and vertical displacement vector
name|COSBase
name|dw2Base
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DW2
argument_list|)
decl_stmt|;
if|if
condition|(
name|dw2Base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|dw2Array
init|=
operator|(
name|COSArray
operator|)
name|dw2Base
decl_stmt|;
name|COSBase
name|base0
init|=
name|dw2Array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSBase
name|base1
init|=
name|dw2Array
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|base0
operator|instanceof
name|COSNumber
operator|&&
name|base1
operator|instanceof
name|COSNumber
condition|)
block|{
name|dw2
index|[
literal|0
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|base0
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|dw2
index|[
literal|1
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|base1
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
block|}
comment|// vertical metrics for individual CIDs.
name|COSBase
name|w2Base
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|W2
argument_list|)
decl_stmt|;
if|if
condition|(
name|w2Base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|w2Array
init|=
operator|(
name|COSArray
operator|)
name|w2Base
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
name|w2Array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSNumber
name|c
init|=
operator|(
name|COSNumber
operator|)
name|w2Array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSBase
name|next
init|=
name|w2Array
operator|.
name|getObject
argument_list|(
operator|++
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|next
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|int
name|cid
init|=
name|c
operator|.
name|intValue
argument_list|()
operator|+
name|j
decl_stmt|;
name|COSNumber
name|w1y
init|=
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|COSNumber
name|v1x
init|=
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|getObject
argument_list|(
operator|++
name|j
argument_list|)
decl_stmt|;
name|COSNumber
name|v1y
init|=
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|getObject
argument_list|(
operator|++
name|j
argument_list|)
decl_stmt|;
name|verticalDisplacementY
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|w1y
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|positionVectors
operator|.
name|put
argument_list|(
name|cid
argument_list|,
operator|new
name|Vector
argument_list|(
name|v1x
operator|.
name|floatValue
argument_list|()
argument_list|,
name|v1y
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|int
name|first
init|=
name|c
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|int
name|last
init|=
operator|(
operator|(
name|COSNumber
operator|)
name|next
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|COSNumber
name|w1y
init|=
operator|(
name|COSNumber
operator|)
name|w2Array
operator|.
name|getObject
argument_list|(
operator|++
name|i
argument_list|)
decl_stmt|;
name|COSNumber
name|v1x
init|=
operator|(
name|COSNumber
operator|)
name|w2Array
operator|.
name|getObject
argument_list|(
operator|++
name|i
argument_list|)
decl_stmt|;
name|COSNumber
name|v1y
init|=
operator|(
name|COSNumber
operator|)
name|w2Array
operator|.
name|getObject
argument_list|(
operator|++
name|i
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|cid
init|=
name|first
init|;
name|cid
operator|<=
name|last
condition|;
name|cid
operator|++
control|)
block|{
name|verticalDisplacementY
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|w1y
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|positionVectors
operator|.
name|put
argument_list|(
name|cid
argument_list|,
operator|new
name|Vector
argument_list|(
name|v1x
operator|.
name|floatValue
argument_list|()
argument_list|,
name|v1y
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
comment|/**      * The PostScript name of the font.      *      * @return The postscript name of the font.      */
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
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
if|if
condition|(
name|fontDescriptor
operator|==
literal|null
condition|)
block|{
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
block|}
return|return
name|fontDescriptor
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|Matrix
name|getFontMatrix
parameter_list|()
function_decl|;
comment|/**      * Returns the Type 0 font which is the parent of this font.      *      * @return parent Type 0 font      */
specifier|public
specifier|final
name|PDType0Font
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
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
comment|/**      * This will get the default width. The default value for the default width is 1000.      *      * @return The default width for the glyphs in this font.      */
specifier|private
name|float
name|getDefaultWidth
parameter_list|()
block|{
if|if
condition|(
name|defaultWidth
operator|==
literal|0
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
name|DW
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSNumber
condition|)
block|{
name|defaultWidth
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|base
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|defaultWidth
operator|=
literal|1000
expr_stmt|;
block|}
block|}
return|return
name|defaultWidth
return|;
block|}
comment|/**      * Returns the default position vector (v).      *      * @param cid CID      */
specifier|private
name|Vector
name|getDefaultPositionVector
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
return|return
operator|new
name|Vector
argument_list|(
name|getWidthForCID
argument_list|(
name|cid
argument_list|)
operator|/
literal|2
argument_list|,
name|dw2
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|private
name|float
name|getWidthForCID
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
name|Float
name|width
init|=
name|widths
operator|.
name|get
argument_list|(
name|cid
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
name|getDefaultWidth
argument_list|()
expr_stmt|;
block|}
return|return
name|width
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
name|int
name|cid
init|=
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|Vector
name|v
init|=
name|positionVectors
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
name|getDefaultPositionVector
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
comment|/**      * Returns the y-component of the vertical displacement vector (w1).      *      * @param code character code      * @return w1y      */
specifier|public
name|float
name|getVerticalDisplacementVectorY
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
name|Float
name|w1y
init|=
name|verticalDisplacementY
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|w1y
operator|==
literal|null
condition|)
block|{
name|w1y
operator|=
name|dw2
index|[
literal|1
index|]
expr_stmt|;
block|}
return|return
name|w1y
return|;
block|}
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
comment|// these widths are supposed to be consistent with the actual widths given in the CIDFont
comment|// program, but PDFBOX-563 shows that when they are not, Acrobat overrides the embedded
comment|// font widths with the widths given in the font dictionary
return|return
name|getWidthForCID
argument_list|(
name|codeToCID
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
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
comment|// todo: this method is highly suspicious, the average glyph width is not usually a good metric
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
block|{
if|if
condition|(
name|averageWidth
operator|==
literal|0
condition|)
block|{
name|float
name|totalWidths
init|=
literal|0.0f
decl_stmt|;
name|int
name|characterCount
init|=
literal|0
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
name|Float
name|width
range|:
name|widths
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|width
operator|>
literal|0
condition|)
block|{
name|totalWidths
operator|+=
name|width
expr_stmt|;
operator|++
name|characterCount
expr_stmt|;
block|}
block|}
block|}
name|averageWidth
operator|=
name|totalWidths
operator|/
name|characterCount
expr_stmt|;
if|if
condition|(
name|averageWidth
operator|<=
literal|0
operator|||
name|Float
operator|.
name|isNaN
argument_list|(
name|averageWidth
argument_list|)
condition|)
block|{
name|averageWidth
operator|=
name|getDefaultWidth
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|averageWidth
return|;
block|}
comment|/**      * Returns the CIDSystemInfo, or null if it is missing (which isn't allowed but could happen).      */
specifier|public
name|PDCIDSystemInfo
name|getCIDSystemInfo
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
name|CIDSYSTEMINFO
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDCIDSystemInfo
argument_list|(
operator|(
name|COSDictionary
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the CID for the given character code. If not found then CID 0 is returned.      *      * @param code character code      * @return CID      */
specifier|public
specifier|abstract
name|int
name|codeToCID
parameter_list|(
name|int
name|code
parameter_list|)
function_decl|;
comment|/**      * Returns the GID for the given character code.      *      * @param code character code      * @return GID      * @throws java.io.IOException      */
specifier|public
specifier|abstract
name|int
name|codeToGID
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
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
specifier|final
name|int
index|[]
name|readCIDToGIDMap
parameter_list|()
throws|throws
name|IOException
block|{
name|int
index|[]
name|cid2gid
init|=
literal|null
decl_stmt|;
name|COSBase
name|map
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|map
decl_stmt|;
name|InputStream
name|is
init|=
name|stream
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|mapAsBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|int
name|numberOfInts
init|=
name|mapAsBytes
operator|.
name|length
operator|/
literal|2
decl_stmt|;
name|cid2gid
operator|=
operator|new
name|int
index|[
name|numberOfInts
index|]
expr_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|numberOfInts
condition|;
name|index
operator|++
control|)
block|{
name|int
name|gid
init|=
operator|(
name|mapAsBytes
index|[
name|offset
index|]
operator|&
literal|0xff
operator|)
operator|<<
literal|8
operator||
name|mapAsBytes
index|[
name|offset
operator|+
literal|1
index|]
operator|&
literal|0xff
decl_stmt|;
name|cid2gid
index|[
name|index
index|]
operator|=
name|gid
expr_stmt|;
name|offset
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|cid2gid
return|;
block|}
block|}
end_class

end_unit

