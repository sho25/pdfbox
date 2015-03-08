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
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|HorizontalHeaderTable
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
literal|"hhea"
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
name|advanceWidthMax
decl_stmt|;
specifier|private
name|short
name|minLeftSideBearing
decl_stmt|;
specifier|private
name|short
name|minRightSideBearing
decl_stmt|;
specifier|private
name|short
name|xMaxExtent
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
name|reserved5
decl_stmt|;
specifier|private
name|short
name|metricDataFormat
decl_stmt|;
specifier|private
name|int
name|numberOfHMetrics
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
name|advanceWidthMax
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|minLeftSideBearing
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|minRightSideBearing
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|xMaxExtent
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
name|reserved5
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
name|numberOfHMetrics
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
comment|/**      * @return Returns the advanceWidthMax.      */
specifier|public
name|int
name|getAdvanceWidthMax
parameter_list|()
block|{
return|return
name|advanceWidthMax
return|;
block|}
comment|/**      * @param advanceWidthMaxValue The advanceWidthMax to set.      */
specifier|public
name|void
name|setAdvanceWidthMax
parameter_list|(
name|int
name|advanceWidthMaxValue
parameter_list|)
block|{
name|this
operator|.
name|advanceWidthMax
operator|=
name|advanceWidthMaxValue
expr_stmt|;
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
comment|/**      * @param ascenderValue The ascender to set.      */
specifier|public
name|void
name|setAscender
parameter_list|(
name|short
name|ascenderValue
parameter_list|)
block|{
name|this
operator|.
name|ascender
operator|=
name|ascenderValue
expr_stmt|;
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
comment|/**      * @param caretSlopeRiseValue The caretSlopeRise to set.      */
specifier|public
name|void
name|setCaretSlopeRise
parameter_list|(
name|short
name|caretSlopeRiseValue
parameter_list|)
block|{
name|this
operator|.
name|caretSlopeRise
operator|=
name|caretSlopeRiseValue
expr_stmt|;
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
comment|/**      * @param caretSlopeRunValue The caretSlopeRun to set.      */
specifier|public
name|void
name|setCaretSlopeRun
parameter_list|(
name|short
name|caretSlopeRunValue
parameter_list|)
block|{
name|this
operator|.
name|caretSlopeRun
operator|=
name|caretSlopeRunValue
expr_stmt|;
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
comment|/**      * @param descenderValue The descender to set.      */
specifier|public
name|void
name|setDescender
parameter_list|(
name|short
name|descenderValue
parameter_list|)
block|{
name|this
operator|.
name|descender
operator|=
name|descenderValue
expr_stmt|;
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
comment|/**      * @param lineGapValue The lineGap to set.      */
specifier|public
name|void
name|setLineGap
parameter_list|(
name|short
name|lineGapValue
parameter_list|)
block|{
name|this
operator|.
name|lineGap
operator|=
name|lineGapValue
expr_stmt|;
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
comment|/**      * @param metricDataFormatValue The metricDataFormat to set.      */
specifier|public
name|void
name|setMetricDataFormat
parameter_list|(
name|short
name|metricDataFormatValue
parameter_list|)
block|{
name|this
operator|.
name|metricDataFormat
operator|=
name|metricDataFormatValue
expr_stmt|;
block|}
comment|/**      * @return Returns the minLeftSideBearing.      */
specifier|public
name|short
name|getMinLeftSideBearing
parameter_list|()
block|{
return|return
name|minLeftSideBearing
return|;
block|}
comment|/**      * @param minLeftSideBearingValue The minLeftSideBearing to set.      */
specifier|public
name|void
name|setMinLeftSideBearing
parameter_list|(
name|short
name|minLeftSideBearingValue
parameter_list|)
block|{
name|this
operator|.
name|minLeftSideBearing
operator|=
name|minLeftSideBearingValue
expr_stmt|;
block|}
comment|/**      * @return Returns the minRightSideBearing.      */
specifier|public
name|short
name|getMinRightSideBearing
parameter_list|()
block|{
return|return
name|minRightSideBearing
return|;
block|}
comment|/**      * @param minRightSideBearingValue The minRightSideBearing to set.      */
specifier|public
name|void
name|setMinRightSideBearing
parameter_list|(
name|short
name|minRightSideBearingValue
parameter_list|)
block|{
name|this
operator|.
name|minRightSideBearing
operator|=
name|minRightSideBearingValue
expr_stmt|;
block|}
comment|/**      * @return Returns the numberOfHMetrics.      */
specifier|public
name|int
name|getNumberOfHMetrics
parameter_list|()
block|{
return|return
name|numberOfHMetrics
return|;
block|}
comment|/**      * @param numberOfHMetricsValue The numberOfHMetrics to set.      */
specifier|public
name|void
name|setNumberOfHMetrics
parameter_list|(
name|int
name|numberOfHMetricsValue
parameter_list|)
block|{
name|this
operator|.
name|numberOfHMetrics
operator|=
name|numberOfHMetricsValue
expr_stmt|;
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
comment|/**      * @param reserved1Value The reserved1 to set.      */
specifier|public
name|void
name|setReserved1
parameter_list|(
name|short
name|reserved1Value
parameter_list|)
block|{
name|this
operator|.
name|reserved1
operator|=
name|reserved1Value
expr_stmt|;
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
comment|/**      * @param reserved2Value The reserved2 to set.      */
specifier|public
name|void
name|setReserved2
parameter_list|(
name|short
name|reserved2Value
parameter_list|)
block|{
name|this
operator|.
name|reserved2
operator|=
name|reserved2Value
expr_stmt|;
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
comment|/**      * @param reserved3Value The reserved3 to set.      */
specifier|public
name|void
name|setReserved3
parameter_list|(
name|short
name|reserved3Value
parameter_list|)
block|{
name|this
operator|.
name|reserved3
operator|=
name|reserved3Value
expr_stmt|;
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
comment|/**      * @param reserved4Value The reserved4 to set.      */
specifier|public
name|void
name|setReserved4
parameter_list|(
name|short
name|reserved4Value
parameter_list|)
block|{
name|this
operator|.
name|reserved4
operator|=
name|reserved4Value
expr_stmt|;
block|}
comment|/**      * @return Returns the reserved5.      */
specifier|public
name|short
name|getReserved5
parameter_list|()
block|{
return|return
name|reserved5
return|;
block|}
comment|/**      * @param reserved5Value The reserved5 to set.      */
specifier|public
name|void
name|setReserved5
parameter_list|(
name|short
name|reserved5Value
parameter_list|)
block|{
name|this
operator|.
name|reserved5
operator|=
name|reserved5Value
expr_stmt|;
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
comment|/**      * @param versionValue The version to set.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|float
name|versionValue
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|versionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the xMaxExtent.      */
specifier|public
name|short
name|getXMaxExtent
parameter_list|()
block|{
return|return
name|xMaxExtent
return|;
block|}
comment|/**      * @param maxExtentValue The xMaxExtent to set.      */
specifier|public
name|void
name|setXMaxExtent
parameter_list|(
name|short
name|maxExtentValue
parameter_list|)
block|{
name|xMaxExtent
operator|=
name|maxExtentValue
expr_stmt|;
block|}
block|}
end_class

end_unit

