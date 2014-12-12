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
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield (ben@benlitchfield.com)  */
end_comment

begin_class
specifier|public
class|class
name|HorizontalMetricsTable
extends|extends
name|TTFTable
block|{
comment|/**      * A tag that identifies this table type.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"hmtx"
decl_stmt|;
specifier|private
name|int
index|[]
name|advanceWidth
decl_stmt|;
specifier|private
name|short
index|[]
name|leftSideBearing
decl_stmt|;
specifier|private
name|short
index|[]
name|nonHorizontalLeftSideBearing
decl_stmt|;
specifier|private
name|int
name|numHMetrics
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
name|HorizontalHeaderTable
name|hHeader
init|=
name|ttf
operator|.
name|getHorizontalHeader
argument_list|()
decl_stmt|;
name|numHMetrics
operator|=
name|hHeader
operator|.
name|getNumberOfHMetrics
argument_list|()
expr_stmt|;
name|int
name|numGlyphs
init|=
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
decl_stmt|;
name|int
name|bytesRead
init|=
literal|0
decl_stmt|;
name|advanceWidth
operator|=
operator|new
name|int
index|[
name|numHMetrics
index|]
expr_stmt|;
name|leftSideBearing
operator|=
operator|new
name|short
index|[
name|numHMetrics
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
name|numHMetrics
condition|;
name|i
operator|++
control|)
block|{
name|advanceWidth
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|leftSideBearing
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|bytesRead
operator|+=
literal|4
expr_stmt|;
block|}
if|if
condition|(
name|bytesRead
operator|<
name|getLength
argument_list|()
condition|)
block|{
name|int
name|numberNonHorizontal
init|=
name|numGlyphs
operator|-
name|numHMetrics
decl_stmt|;
comment|// handle bad fonts with too many hmetrics
if|if
condition|(
name|numberNonHorizontal
operator|<
literal|0
condition|)
block|{
name|numberNonHorizontal
operator|=
name|numGlyphs
expr_stmt|;
block|}
name|nonHorizontalLeftSideBearing
operator|=
operator|new
name|short
index|[
name|numberNonHorizontal
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
name|numberNonHorizontal
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|bytesRead
operator|<
name|getLength
argument_list|()
condition|)
block|{
name|nonHorizontalLeftSideBearing
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|bytesRead
operator|+=
literal|2
expr_stmt|;
block|}
block|}
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Returns the advance width for the given GID.      *      * @param gid GID      */
specifier|public
name|int
name|getAdvanceWidth
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
if|if
condition|(
name|gid
operator|<
name|numHMetrics
condition|)
block|{
return|return
name|advanceWidth
index|[
name|gid
index|]
return|;
block|}
else|else
block|{
comment|// monospaced fonts may not have a width for every glyph
comment|// the last one is for subsequent glyphs
return|return
name|advanceWidth
index|[
name|advanceWidth
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
block|}
block|}
end_class

end_unit

