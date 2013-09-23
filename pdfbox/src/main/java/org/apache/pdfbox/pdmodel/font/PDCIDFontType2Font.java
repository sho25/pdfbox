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
name|common
operator|.
name|PDStream
import|;
end_import

begin_comment
comment|/**  * This is implementation of the CIDFontType2 Font.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDCIDFontType2Font
extends|extends
name|PDCIDFont
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
name|PDCIDFontType2Font
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Boolean
name|hasCIDToGIDMap
init|=
literal|null
decl_stmt|;
specifier|private
name|Boolean
name|hasIdentityCIDToGIDMap
init|=
literal|null
decl_stmt|;
specifier|private
name|int
index|[]
name|cid2gid
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDCIDFontType2Font
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|CID_FONT_TYPE2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDCIDFontType2Font
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
comment|/**      * read the CIDToGID map.      */
specifier|private
name|void
name|readCIDToGIDMapping
parameter_list|()
block|{
name|COSBase
name|map
init|=
name|font
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
try|try
block|{
name|byte
index|[]
name|mapAsBytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
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
name|cid2gid
index|[
name|index
index|]
operator|=
name|getCodeFromArray
argument_list|(
name|mapAsBytes
argument_list|,
name|offset
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|offset
operator|+=
literal|2
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
literal|"Can't read the CIDToGIDMap"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Indicates if this font has a CIDToGIDMap.      *       * @return returns true if the font has a CIDToGIDMap.      */
specifier|public
name|boolean
name|hasCIDToGIDMap
parameter_list|()
block|{
if|if
condition|(
name|hasCIDToGIDMap
operator|==
literal|null
condition|)
block|{
name|COSBase
name|map
init|=
name|font
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
operator|!=
literal|null
operator|&&
name|map
operator|instanceof
name|COSStream
condition|)
block|{
name|hasCIDToGIDMap
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
else|else
block|{
name|hasCIDToGIDMap
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
return|return
name|hasCIDToGIDMap
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Indicates if this font has an identity CIDToGIDMap.      *       * @return returns true if the font has an identity CIDToGIDMap.      */
specifier|public
name|boolean
name|hasIdentityCIDToGIDMap
parameter_list|()
block|{
if|if
condition|(
name|hasIdentityCIDToGIDMap
operator|==
literal|null
condition|)
block|{
name|COSBase
name|map
init|=
name|font
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
operator|!=
literal|null
operator|&&
name|map
operator|instanceof
name|COSName
condition|)
block|{
name|hasIdentityCIDToGIDMap
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
else|else
block|{
name|hasIdentityCIDToGIDMap
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
return|return
name|hasIdentityCIDToGIDMap
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Maps the given CID to the correspondent GID.      *       * @param cid the given CID      * @return the mapped GID, or -1 if something went wrong.      */
specifier|public
name|int
name|mapCIDToGID
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
if|if
condition|(
name|hasCIDToGIDMap
argument_list|()
condition|)
block|{
if|if
condition|(
name|cid2gid
operator|==
literal|null
condition|)
block|{
name|readCIDToGIDMapping
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cid2gid
operator|!=
literal|null
operator|&&
name|cid
operator|<
name|cid2gid
operator|.
name|length
condition|)
block|{
return|return
name|cid2gid
index|[
name|cid
index|]
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
comment|// identity is the default value
return|return
name|cid
return|;
block|}
block|}
comment|/**      * Returns the CID2GID mapping if present.      *       * @return the CID2GID mapping      */
specifier|public
name|int
index|[]
name|getCID2GID
parameter_list|()
block|{
if|if
condition|(
name|hasCIDToGIDMap
argument_list|()
condition|)
block|{
if|if
condition|(
name|cid2gid
operator|==
literal|null
condition|)
block|{
name|readCIDToGIDMapping
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|cid2gid
return|;
block|}
comment|/**      * Returns the embedded true type font.      *       * @return the true type font      * @throws IOException exception if something went wrong      */
specifier|public
name|TrueTypeFont
name|getTTFFont
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFontDescriptorDictionary
name|fd
init|=
operator|(
name|PDFontDescriptorDictionary
operator|)
name|getFontDescriptor
argument_list|()
decl_stmt|;
name|PDStream
name|ff2Stream
init|=
name|fd
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
name|TrueTypeFont
name|trueTypeFont
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff2Stream
operator|!=
literal|null
condition|)
block|{
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|trueTypeFont
operator|=
name|ttfParser
operator|.
name|parseTTF
argument_list|(
name|ff2Stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|trueTypeFont
return|;
block|}
block|}
end_class

end_unit

