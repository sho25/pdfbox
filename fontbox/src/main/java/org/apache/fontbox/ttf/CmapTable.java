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
name|fontbox
operator|.
name|ttf
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

begin_comment
comment|/**  * The "cmap" table of a true type font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|CmapTable
extends|extends
name|TTFTable
block|{
comment|/**      * A tag used to identify this table.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"cmap"
decl_stmt|;
comment|// platform
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_UNICODE
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_MACINTOSH
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_WINDOWS
init|=
literal|3
decl_stmt|;
comment|// Mac encodings
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_MAC_ROMAN
init|=
literal|0
decl_stmt|;
comment|// Windows encodings
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_SYMBOL
init|=
literal|0
decl_stmt|;
comment|// Unicode, non-standard character set
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_UNICODE_BMP
init|=
literal|1
decl_stmt|;
comment|// Unicode BMP (UCS-2)
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_SHIFT_JIS
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_BIG5
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_PRC
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_WANSUNG
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_JOHAB
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WIN_UNICODE_FULL
init|=
literal|10
decl_stmt|;
comment|// Unicode Full (UCS-4)
comment|// Unicode encodings
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_1_0
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_1_1
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_2_0_BMP
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_2_0_FULL
init|=
literal|4
decl_stmt|;
specifier|private
name|CmapSubtable
index|[]
name|cmaps
decl_stmt|;
name|CmapTable
parameter_list|(
name|TrueTypeFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|read
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|version
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|numberOfTables
init|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|cmaps
operator|=
operator|new
name|CmapSubtable
index|[
name|numberOfTables
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfTables
condition|;
name|i
operator|++
control|)
block|{
name|CmapSubtable
name|cmap
init|=
operator|new
name|CmapSubtable
argument_list|()
decl_stmt|;
name|cmap
operator|.
name|initData
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|cmaps
index|[
name|i
index|]
operator|=
name|cmap
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfTables
condition|;
name|i
operator|++
control|)
block|{
name|cmaps
index|[
name|i
index|]
operator|.
name|initSubtable
argument_list|(
name|this
argument_list|,
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the cmaps.      */
specifier|public
name|CmapSubtable
index|[]
name|getCmaps
parameter_list|()
block|{
return|return
name|cmaps
return|;
block|}
comment|/**      * @param cmapsValue The cmaps to set.      */
specifier|public
name|void
name|setCmaps
parameter_list|(
name|CmapSubtable
index|[]
name|cmapsValue
parameter_list|)
block|{
name|cmaps
operator|=
name|cmapsValue
expr_stmt|;
block|}
comment|/**      * Returns the subtable, if any, for the given platform and encoding.      */
specifier|public
name|CmapSubtable
name|getSubtable
parameter_list|(
name|int
name|platformId
parameter_list|,
name|int
name|platformEncodingId
parameter_list|)
block|{
for|for
control|(
name|CmapSubtable
name|cmap
range|:
name|cmaps
control|)
block|{
if|if
condition|(
name|cmap
operator|.
name|getPlatformId
argument_list|()
operator|==
name|platformId
operator|&&
name|cmap
operator|.
name|getPlatformEncodingId
argument_list|()
operator|==
name|platformEncodingId
condition|)
block|{
return|return
name|cmap
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

