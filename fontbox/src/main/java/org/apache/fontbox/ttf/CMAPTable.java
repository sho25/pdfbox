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
comment|/**  * The CMAP table of a true type font.  *   * @author Ben Litchfield (ben@benlitchfield.com)  *   */
end_comment

begin_class
specifier|public
class|class
name|CMAPTable
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
comment|/**      * A constant for the platform.      */
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_MISC
init|=
literal|0
decl_stmt|;
comment|/**      * A constant for the platform.      */
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_MACINTOSH
init|=
literal|1
decl_stmt|;
comment|/**      * A constant for the platform.      */
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_WINDOWS
init|=
literal|3
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_SYMBOL
init|=
literal|0
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE
init|=
literal|1
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_SHIFT_JIS
init|=
literal|2
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_BIG5
init|=
literal|3
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_PRC
init|=
literal|4
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WANSUNG
init|=
literal|5
decl_stmt|;
comment|/**      * An encoding constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_JOHAB
init|=
literal|6
decl_stmt|;
specifier|private
name|CMAPEncodingEntry
index|[]
name|cmaps
decl_stmt|;
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
name|CMAPEncodingEntry
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
name|CMAPEncodingEntry
name|cmap
init|=
operator|new
name|CMAPEncodingEntry
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
name|CMAPEncodingEntry
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
name|CMAPEncodingEntry
index|[]
name|cmapsValue
parameter_list|)
block|{
name|cmaps
operator|=
name|cmapsValue
expr_stmt|;
block|}
block|}
end_class

end_unit

