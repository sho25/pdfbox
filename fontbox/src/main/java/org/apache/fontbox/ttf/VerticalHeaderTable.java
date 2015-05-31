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
comment|/**  * A vertical header 'vhea' table in a TrueType or OpenType font.  *  * Supports versions 1.0 and 1.1, for which the only difference is changing  * the specification names and descriptions of the ascender, descender,  * and lineGap fields to vertTypoAscender, vertTypoDescender, vertTypeLineGap.  *  * This table is required by the OpenType CJK Font Guidelines for "all  * OpenType fonts that are used for vertical writing".  *   * This table is specified in both the TrueType and OpenType specifications.  *   * @author Glenn Adams  *   */
end_comment

begin_class
specifier|public
class|class
name|VerticalHeaderTable
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
literal|"vhea"
decl_stmt|;
specifier|private
name|float
name|version
decl_stmt|;
specifier|private
name|short
name|ascender
decl_stmt|;
specifier|private
name|short
name|descender
decl_stmt|;
specifier|private
name|short
name|lineGap
decl_stmt|;
specifier|private
name|int
name|advanceHeightMax
decl_stmt|;
specifier|private
name|short
name|minTopSideBearing
decl_stmt|;
specifier|private
name|short
name|minBottomSideBearing
decl_stmt|;
specifier|private
name|short
name|yMaxExtent
decl_stmt|;
specifier|private
name|short
name|caretSlopeRise
decl_stmt|;
specifier|private
name|short
name|caretSlopeRun
decl_stmt|;
specifier|private
name|short
name|caretOffset
decl_stmt|;
specifier|private
name|short
name|reserved1
decl_stmt|;
specifier|private
name|short
name|reserved2
decl_stmt|;
specifier|private
name|short
name|reserved3
decl_stmt|;
specifier|private
name|short
name|reserved4
decl_stmt|;
specifier|private
name|short
name|metricDataFormat
decl_stmt|;
specifier|private
name|int
name|numberOfVMetrics
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
annotation|@
name|Override
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
name|version
operator|=
name|data
operator|.
name|read32Fixed
argument_list|()
expr_stmt|;
name|ascender
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|descender
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|lineGap
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|advanceHeightMax
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|minTopSideBearing
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|minBottomSideBearing
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|yMaxExtent
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|caretSlopeRise
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|caretSlopeRun
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|caretOffset
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|reserved1
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|reserved2
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|reserved3
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|reserved4
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|metricDataFormat
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|numberOfVMetrics
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the advanceHeightMax.      */
specifier|public
name|int
name|getAdvanceHeightMax
parameter_list|()
block|{
return|return
name|advanceHeightMax
return|;
block|}
comment|/**      * @return Returns the ascender.      */
specifier|public
name|short
name|getAscender
parameter_list|()
block|{
return|return
name|ascender
return|;
block|}
comment|/**      * @return Returns the caretSlopeRise.      */
specifier|public
name|short
name|getCaretSlopeRise
parameter_list|()
block|{
return|return
name|caretSlopeRise
return|;
block|}
comment|/**      * @return Returns the caretSlopeRun.      */
specifier|public
name|short
name|getCaretSlopeRun
parameter_list|()
block|{
return|return
name|caretSlopeRun
return|;
block|}
comment|/**      * @return Returns the caretOffset.      */
specifier|public
name|short
name|getCaretOffset
parameter_list|()
block|{
return|return
name|caretOffset
return|;
block|}
comment|/**      * @return Returns the descender.      */
specifier|public
name|short
name|getDescender
parameter_list|()
block|{
return|return
name|descender
return|;
block|}
comment|/**      * @return Returns the lineGap.      */
specifier|public
name|short
name|getLineGap
parameter_list|()
block|{
return|return
name|lineGap
return|;
block|}
comment|/**      * @return Returns the metricDataFormat.      */
specifier|public
name|short
name|getMetricDataFormat
parameter_list|()
block|{
return|return
name|metricDataFormat
return|;
block|}
comment|/**      * @return Returns the minTopSideBearing.      */
specifier|public
name|short
name|getMinTopSideBearing
parameter_list|()
block|{
return|return
name|minTopSideBearing
return|;
block|}
comment|/**      * @return Returns the minBottomSideBearing.      */
specifier|public
name|short
name|getMinBottomSideBearing
parameter_list|()
block|{
return|return
name|minBottomSideBearing
return|;
block|}
comment|/**      * @return Returns the numberOfVMetrics.      */
specifier|public
name|int
name|getNumberOfVMetrics
parameter_list|()
block|{
return|return
name|numberOfVMetrics
return|;
block|}
comment|/**      * @return Returns the reserved1.      */
specifier|public
name|short
name|getReserved1
parameter_list|()
block|{
return|return
name|reserved1
return|;
block|}
comment|/**      * @return Returns the reserved2.      */
specifier|public
name|short
name|getReserved2
parameter_list|()
block|{
return|return
name|reserved2
return|;
block|}
comment|/**      * @return Returns the reserved3.      */
specifier|public
name|short
name|getReserved3
parameter_list|()
block|{
return|return
name|reserved3
return|;
block|}
comment|/**      * @return Returns the reserved4.      */
specifier|public
name|short
name|getReserved4
parameter_list|()
block|{
return|return
name|reserved4
return|;
block|}
comment|/**      * @return Returns the version.      */
specifier|public
name|float
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * @return Returns the yMaxExtent.      */
specifier|public
name|short
name|getYMaxExtent
parameter_list|()
block|{
return|return
name|yMaxExtent
return|;
block|}
block|}
end_class

end_unit

