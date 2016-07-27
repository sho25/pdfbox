begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|fontencodingpane
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
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
name|LinkedHashMap
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
name|javax
operator|.
name|swing
operator|.
name|JPanel
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
name|font
operator|.
name|PDCIDFont
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
name|PDFont
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
name|PDType0Font
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that shows the CIDToGID table along with unicode characters for Type0Fonts when descendent  * font is of type PDCIDFontType2.  */
end_comment

begin_class
class|class
name|Type0Font
extends|extends
name|FontPane
block|{
specifier|private
specifier|final
name|FontEncodingView
name|view
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NO_GLYPH
init|=
literal|"No glyph"
decl_stmt|;
specifier|private
name|int
name|totalAvailableGlyph
init|=
literal|0
decl_stmt|;
comment|/**      * Constructor.      * @param descendantFont PDCIDFontType2 instance.      * @param parentFont PDFont instance.      * @throws IOException If fails to parse cidtogid map.      */
name|Type0Font
parameter_list|(
name|PDCIDFont
name|descendantFont
parameter_list|,
name|PDType0Font
name|parentFont
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
index|[]
index|[]
name|cidtogid
init|=
name|readCIDToGIDMap
argument_list|(
name|descendantFont
argument_list|,
name|parentFont
argument_list|)
decl_stmt|;
if|if
condition|(
name|cidtogid
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Font"
argument_list|,
name|descendantFont
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"CID count"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|cidtogid
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|FontEncodingView
argument_list|(
name|cidtogid
argument_list|,
name|attributes
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"CID"
block|,
literal|"GID"
block|,
literal|"Unicode Character"
block|,
literal|"Glyph"
block|}
argument_list|,
name|getYBounds
argument_list|(
name|cidtogid
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
index|[]
index|[]
name|tab
init|=
name|readMap
argument_list|(
name|descendantFont
argument_list|,
name|parentFont
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Font"
argument_list|,
name|descendantFont
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"CID count"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|tab
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Glyph count"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|totalAvailableGlyph
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|FontEncodingView
argument_list|(
name|tab
argument_list|,
name|attributes
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Code"
block|,
literal|"CID"
block|,
literal|"GID"
block|,
literal|"Unicode Character"
block|,
literal|"Glyph"
block|}
argument_list|,
name|getYBounds
argument_list|(
name|tab
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Object
index|[]
index|[]
name|readMap
parameter_list|(
name|PDCIDFont
name|descendantFont
parameter_list|,
name|PDType0Font
name|parentFont
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|codes
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|code
init|=
literal|0
init|;
name|code
operator|<
literal|65535
condition|;
operator|++
name|code
control|)
block|{
if|if
condition|(
name|descendantFont
operator|.
name|hasGlyph
argument_list|(
name|code
argument_list|)
condition|)
block|{
operator|++
name|codes
expr_stmt|;
block|}
block|}
name|Object
index|[]
index|[]
name|tab
init|=
operator|new
name|Object
index|[
name|codes
index|]
index|[
literal|5
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|code
init|=
literal|0
init|;
name|code
operator|<
literal|65535
condition|;
operator|++
name|code
control|)
block|{
if|if
condition|(
name|descendantFont
operator|.
name|hasGlyph
argument_list|(
name|code
argument_list|)
condition|)
block|{
name|tab
index|[
name|index
index|]
index|[
literal|0
index|]
operator|=
name|code
expr_stmt|;
name|tab
index|[
name|index
index|]
index|[
literal|1
index|]
operator|=
name|descendantFont
operator|.
name|codeToCID
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|tab
index|[
name|index
index|]
index|[
literal|2
index|]
operator|=
name|descendantFont
operator|.
name|codeToGID
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|tab
index|[
name|index
index|]
index|[
literal|3
index|]
operator|=
name|parentFont
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|GeneralPath
name|path
init|=
name|descendantFont
operator|.
name|getPath
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|tab
index|[
name|index
index|]
index|[
literal|4
index|]
operator|=
name|path
expr_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|getBounds2D
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
operator|++
name|totalAvailableGlyph
expr_stmt|;
block|}
operator|++
name|index
expr_stmt|;
block|}
block|}
return|return
name|tab
return|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|readCIDToGIDMap
parameter_list|(
name|PDCIDFont
name|font
parameter_list|,
name|PDFont
name|parentFont
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
index|[]
index|[]
name|cid2gid
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dict
init|=
name|font
operator|.
name|getCOSObject
argument_list|()
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
name|Object
index|[
name|numberOfInts
index|]
index|[
literal|4
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
index|[
literal|0
index|]
operator|=
name|index
expr_stmt|;
name|cid2gid
index|[
name|index
index|]
index|[
literal|1
index|]
operator|=
name|gid
expr_stmt|;
if|if
condition|(
name|gid
operator|!=
literal|0
operator|&&
name|parentFont
operator|.
name|toUnicode
argument_list|(
name|index
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|cid2gid
index|[
name|index
index|]
index|[
literal|2
index|]
operator|=
name|parentFont
operator|.
name|toUnicode
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
name|GeneralPath
name|path
init|=
name|font
operator|.
name|getPath
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|cid2gid
index|[
name|index
index|]
index|[
literal|3
index|]
operator|=
name|path
expr_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|getBounds2D
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
operator|++
name|totalAvailableGlyph
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|JPanel
name|getPanel
parameter_list|()
block|{
if|if
condition|(
name|view
operator|!=
literal|null
condition|)
block|{
return|return
name|view
operator|.
name|getPanel
argument_list|()
return|;
block|}
name|JPanel
name|panel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|panel
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|500
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|panel
return|;
block|}
block|}
end_class

end_unit

