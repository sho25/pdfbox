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
comment|/**  * A CIDFont.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDCIDFont
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
name|PDCIDFont
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
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
name|widthCache
decl_stmt|;
specifier|private
name|long
name|defaultWidth
decl_stmt|;
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|protected
name|PDCIDFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|,
name|PDType0Font
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|extractWidths
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * This will get the fonts bounding box.      *      * @return The fonts bounding box.      * @throws IOException If there is an error getting the font bounding box.      */
annotation|@
name|Override
specifier|public
name|PDRectangle
name|getFontBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"getFontBoundingBox(): Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**      * This will get the default width.  The default value for the default width is 1000.      *      * @return The default width for the glyphs in this font.      */
specifier|public
name|long
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
name|COSNumber
name|number
init|=
operator|(
name|COSNumber
operator|)
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
name|number
operator|!=
literal|null
condition|)
block|{
name|defaultWidth
operator|=
name|number
operator|.
name|intValue
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
comment|/**      * This will get the font width for a character.      *      * @param c The character code to get the width for.      * @param offset The offset into the array.      * @param length The length of the data.      * @return The width is in 1000 unit of text space, ie 333 or 777      * @throws IOException If an error occurs while parsing.      */
annotation|@
name|Override
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
name|retval
init|=
name|getDefaultWidth
argument_list|()
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
name|Float
name|widthFloat
init|=
name|widthCache
operator|.
name|get
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|widthFloat
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|widthFloat
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|void
name|extractWidths
parameter_list|()
block|{
if|if
condition|(
name|widthCache
operator|==
literal|null
condition|)
block|{
name|widthCache
operator|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Float
argument_list|>
argument_list|()
expr_stmt|;
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
name|W
argument_list|)
decl_stmt|;
if|if
condition|(
name|widths
operator|!=
literal|null
condition|)
block|{
name|int
name|size
init|=
name|widths
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
name|widths
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
name|widths
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
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|widthCache
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
name|widths
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
name|widthCache
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
block|}
comment|/**      * This will get the font height for a character.      *      * @param c The character code to get the height for.      * @param offset The offset into the array.      * @param length The length of the data.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
annotation|@
name|Override
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
name|PDFontDescriptor
name|desc
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
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
comment|// do an average of these two. Can we do better???
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
return|return
name|retval
return|;
block|}
comment|/**      * This will get the average font width for all characters.      *      * @return The width is in 1000 unit of text space, ie 333 or 777      *      * @throws IOException If an error occurs while parsing.      */
annotation|@
name|Override
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
throws|throws
name|IOException
block|{
name|float
name|totalWidths
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
name|W
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
name|firstCode
init|=
operator|(
name|COSNumber
operator|)
name|widths
operator|.
name|getObject
argument_list|(
name|i
operator|++
argument_list|)
decl_stmt|;
name|COSBase
name|next
init|=
name|widths
operator|.
name|getObject
argument_list|(
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
name|COSNumber
name|width
init|=
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|totalWidths
operator|+=
name|width
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
else|else
block|{
name|i
operator|++
expr_stmt|;
name|COSNumber
name|rangeWidth
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
name|rangeWidth
operator|.
name|floatValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|totalWidths
operator|+=
name|rangeWidth
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
block|}
name|float
name|average
init|=
name|totalWidths
operator|/
name|characterCount
decl_stmt|;
if|if
condition|(
name|average
operator|<=
literal|0
condition|)
block|{
name|average
operator|=
name|getDefaultWidth
argument_list|()
expr_stmt|;
block|}
return|return
name|average
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getFontWidth
parameter_list|(
name|int
name|charCode
parameter_list|)
block|{
name|float
name|width
init|=
name|getDefaultWidth
argument_list|()
decl_stmt|;
if|if
condition|(
name|widthCache
operator|.
name|containsKey
argument_list|(
name|charCode
argument_list|)
condition|)
block|{
name|width
operator|=
name|widthCache
operator|.
name|get
argument_list|(
name|charCode
argument_list|)
expr_stmt|;
block|}
return|return
name|width
return|;
block|}
comment|/**      * Extract the CIDSystemInfo.      * @return the CIDSystemInfo as String      */
specifier|private
name|String
name|getCIDSystemInfo
parameter_list|()
block|{
name|String
name|cidSystemInfo
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
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
name|dict
operator|!=
literal|null
condition|)
block|{
name|String
name|ordering
init|=
name|dict
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|)
decl_stmt|;
name|String
name|registry
init|=
name|dict
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|)
decl_stmt|;
name|int
name|supplement
init|=
name|dict
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|SUPPLEMENT
argument_list|)
decl_stmt|;
name|cidSystemInfo
operator|=
name|registry
operator|+
literal|"-"
operator|+
name|ordering
operator|+
literal|"-"
operator|+
name|supplement
expr_stmt|;
block|}
return|return
name|cidSystemInfo
return|;
block|}
comment|// todo: do we want to do this at all? Isn't the parent Type0 font responsible for this?
annotation|@
name|Override
specifier|protected
name|void
name|determineEncoding
parameter_list|()
block|{
name|String
name|cidSystemInfo
init|=
name|getCIDSystemInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|cidSystemInfo
operator|==
literal|null
condition|)
block|{
comment|// todo: CIDSystemInfo is required, so this is an error (perform recovery?)
name|LOG
operator|.
name|error
argument_list|(
literal|"Missing CIDSystemInfo in CIDFont dictionary"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|cidSystemInfo
operator|.
name|contains
argument_list|(
literal|"Identity"
argument_list|)
condition|)
block|{
name|cidSystemInfo
operator|=
literal|"Identity-H"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cidSystemInfo
operator|.
name|startsWith
argument_list|(
literal|"Adobe-UCS-"
argument_list|)
condition|)
block|{
name|cidSystemInfo
operator|=
literal|"Adobe-Identity-UCS"
expr_stmt|;
block|}
else|else
block|{
name|cidSystemInfo
operator|=
name|cidSystemInfo
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|cidSystemInfo
operator|.
name|lastIndexOf
argument_list|(
literal|"-"
argument_list|)
argument_list|)
operator|+
literal|"-UCS2"
expr_stmt|;
block|}
name|cmap
operator|=
name|cmapObjects
operator|.
name|get
argument_list|(
name|cidSystemInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|InputStream
name|cmapStream
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// look for a predefined CMap with the given name
name|cmapStream
operator|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resourceRootCMAP
operator|+
name|cidSystemInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmapStream
operator|!=
literal|null
condition|)
block|{
name|cmap
operator|=
name|parseCmap
argument_list|(
name|resourceRootCMAP
argument_list|,
name|cmapStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not parse predefined CMAP file for '"
operator|+
name|cidSystemInfo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"'"
operator|+
name|cidSystemInfo
operator|+
literal|"' isn't a predefined CMap, most "
operator|+
literal|"likely it's embedded in the pdf itself."
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
literal|"Could not find predefined CMAP file for '"
operator|+
name|cidSystemInfo
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|cmapStream
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|encode
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
name|String
name|result
decl_stmt|;
if|if
condition|(
name|cmap
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|cmapEncoding
argument_list|(
name|getCodeFromArray
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
argument_list|,
name|length
argument_list|,
literal|true
argument_list|,
name|cmap
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|super
operator|.
name|encode
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|widthCache
operator|!=
literal|null
condition|)
block|{
name|widthCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|widthCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

