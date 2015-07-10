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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_comment
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|HeaderTable
extends|extends
name|TTFTable
block|{
comment|/**      * Tag to identify this table.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"head"
decl_stmt|;
comment|/**      * Bold macStyle flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|MAC_STYLE_BOLD
init|=
literal|1
decl_stmt|;
comment|/**      * Italic macStyle flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|MAC_STYLE_ITALIC
init|=
literal|2
decl_stmt|;
specifier|private
name|float
name|version
decl_stmt|;
specifier|private
name|float
name|fontRevision
decl_stmt|;
specifier|private
name|long
name|checkSumAdjustment
decl_stmt|;
specifier|private
name|long
name|magicNumber
decl_stmt|;
specifier|private
name|int
name|flags
decl_stmt|;
specifier|private
name|int
name|unitsPerEm
decl_stmt|;
specifier|private
name|Calendar
name|created
decl_stmt|;
specifier|private
name|Calendar
name|modified
decl_stmt|;
specifier|private
name|short
name|xMin
decl_stmt|;
specifier|private
name|short
name|yMin
decl_stmt|;
specifier|private
name|short
name|xMax
decl_stmt|;
specifier|private
name|short
name|yMax
decl_stmt|;
specifier|private
name|int
name|macStyle
decl_stmt|;
specifier|private
name|int
name|lowestRecPPEM
decl_stmt|;
specifier|private
name|short
name|fontDirectionHint
decl_stmt|;
specifier|private
name|short
name|indexToLocFormat
decl_stmt|;
specifier|private
name|short
name|glyphDataFormat
decl_stmt|;
name|HeaderTable
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
name|version
operator|=
name|data
operator|.
name|read32Fixed
argument_list|()
expr_stmt|;
name|fontRevision
operator|=
name|data
operator|.
name|read32Fixed
argument_list|()
expr_stmt|;
name|checkSumAdjustment
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|magicNumber
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|flags
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|unitsPerEm
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|created
operator|=
name|data
operator|.
name|readInternationalDate
argument_list|()
expr_stmt|;
name|modified
operator|=
name|data
operator|.
name|readInternationalDate
argument_list|()
expr_stmt|;
name|xMin
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|yMin
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|xMax
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|yMax
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|macStyle
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|lowestRecPPEM
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|fontDirectionHint
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|indexToLocFormat
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|glyphDataFormat
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the checkSumAdjustment.      */
specifier|public
name|long
name|getCheckSumAdjustment
parameter_list|()
block|{
return|return
name|checkSumAdjustment
return|;
block|}
comment|/**      * @param checkSumAdjustmentValue The checkSumAdjustment to set.      */
specifier|public
name|void
name|setCheckSumAdjustment
parameter_list|(
name|long
name|checkSumAdjustmentValue
parameter_list|)
block|{
name|this
operator|.
name|checkSumAdjustment
operator|=
name|checkSumAdjustmentValue
expr_stmt|;
block|}
comment|/**      * @return Returns the created.      */
specifier|public
name|Calendar
name|getCreated
parameter_list|()
block|{
return|return
name|created
return|;
block|}
comment|/**      * @param createdValue The created to set.      */
specifier|public
name|void
name|setCreated
parameter_list|(
name|Calendar
name|createdValue
parameter_list|)
block|{
name|this
operator|.
name|created
operator|=
name|createdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the flags.      */
specifier|public
name|int
name|getFlags
parameter_list|()
block|{
return|return
name|flags
return|;
block|}
comment|/**      * @param flagsValue The flags to set.      */
specifier|public
name|void
name|setFlags
parameter_list|(
name|int
name|flagsValue
parameter_list|)
block|{
name|this
operator|.
name|flags
operator|=
name|flagsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the fontDirectionHint.      */
specifier|public
name|short
name|getFontDirectionHint
parameter_list|()
block|{
return|return
name|fontDirectionHint
return|;
block|}
comment|/**      * @param fontDirectionHintValue The fontDirectionHint to set.      */
specifier|public
name|void
name|setFontDirectionHint
parameter_list|(
name|short
name|fontDirectionHintValue
parameter_list|)
block|{
name|this
operator|.
name|fontDirectionHint
operator|=
name|fontDirectionHintValue
expr_stmt|;
block|}
comment|/**      * @return Returns the fontRevision.      */
specifier|public
name|float
name|getFontRevision
parameter_list|()
block|{
return|return
name|fontRevision
return|;
block|}
comment|/**      * @param fontRevisionValue The fontRevision to set.      */
specifier|public
name|void
name|setFontRevision
parameter_list|(
name|float
name|fontRevisionValue
parameter_list|)
block|{
name|this
operator|.
name|fontRevision
operator|=
name|fontRevisionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the glyphDataFormat.      */
specifier|public
name|short
name|getGlyphDataFormat
parameter_list|()
block|{
return|return
name|glyphDataFormat
return|;
block|}
comment|/**      * @param glyphDataFormatValue The glyphDataFormat to set.      */
specifier|public
name|void
name|setGlyphDataFormat
parameter_list|(
name|short
name|glyphDataFormatValue
parameter_list|)
block|{
name|this
operator|.
name|glyphDataFormat
operator|=
name|glyphDataFormatValue
expr_stmt|;
block|}
comment|/**      * @return Returns the indexToLocFormat.      */
specifier|public
name|short
name|getIndexToLocFormat
parameter_list|()
block|{
return|return
name|indexToLocFormat
return|;
block|}
comment|/**      * @param indexToLocFormatValue The indexToLocFormat to set.      */
specifier|public
name|void
name|setIndexToLocFormat
parameter_list|(
name|short
name|indexToLocFormatValue
parameter_list|)
block|{
name|this
operator|.
name|indexToLocFormat
operator|=
name|indexToLocFormatValue
expr_stmt|;
block|}
comment|/**      * @return Returns the lowestRecPPEM.      */
specifier|public
name|int
name|getLowestRecPPEM
parameter_list|()
block|{
return|return
name|lowestRecPPEM
return|;
block|}
comment|/**      * @param lowestRecPPEMValue The lowestRecPPEM to set.      */
specifier|public
name|void
name|setLowestRecPPEM
parameter_list|(
name|int
name|lowestRecPPEMValue
parameter_list|)
block|{
name|this
operator|.
name|lowestRecPPEM
operator|=
name|lowestRecPPEMValue
expr_stmt|;
block|}
comment|/**      * @return Returns the macStyle.      */
specifier|public
name|int
name|getMacStyle
parameter_list|()
block|{
return|return
name|macStyle
return|;
block|}
comment|/**      * @param macStyleValue The macStyle to set.      */
specifier|public
name|void
name|setMacStyle
parameter_list|(
name|int
name|macStyleValue
parameter_list|)
block|{
name|this
operator|.
name|macStyle
operator|=
name|macStyleValue
expr_stmt|;
block|}
comment|/**      * @return Returns the magicNumber.      */
specifier|public
name|long
name|getMagicNumber
parameter_list|()
block|{
return|return
name|magicNumber
return|;
block|}
comment|/**      * @param magicNumberValue The magicNumber to set.      */
specifier|public
name|void
name|setMagicNumber
parameter_list|(
name|long
name|magicNumberValue
parameter_list|)
block|{
name|this
operator|.
name|magicNumber
operator|=
name|magicNumberValue
expr_stmt|;
block|}
comment|/**      * @return Returns the modified.      */
specifier|public
name|Calendar
name|getModified
parameter_list|()
block|{
return|return
name|modified
return|;
block|}
comment|/**      * @param modifiedValue The modified to set.      */
specifier|public
name|void
name|setModified
parameter_list|(
name|Calendar
name|modifiedValue
parameter_list|)
block|{
name|this
operator|.
name|modified
operator|=
name|modifiedValue
expr_stmt|;
block|}
comment|/**      * @return Returns the unitsPerEm.      */
specifier|public
name|int
name|getUnitsPerEm
parameter_list|()
block|{
return|return
name|unitsPerEm
return|;
block|}
comment|/**      * @param unitsPerEmValue The unitsPerEm to set.      */
specifier|public
name|void
name|setUnitsPerEm
parameter_list|(
name|int
name|unitsPerEmValue
parameter_list|)
block|{
name|this
operator|.
name|unitsPerEm
operator|=
name|unitsPerEmValue
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
comment|/**      * @return Returns the xMax.      */
specifier|public
name|short
name|getXMax
parameter_list|()
block|{
return|return
name|xMax
return|;
block|}
comment|/**      * @param maxValue The xMax to set.      */
specifier|public
name|void
name|setXMax
parameter_list|(
name|short
name|maxValue
parameter_list|)
block|{
name|xMax
operator|=
name|maxValue
expr_stmt|;
block|}
comment|/**      * @return Returns the xMin.      */
specifier|public
name|short
name|getXMin
parameter_list|()
block|{
return|return
name|xMin
return|;
block|}
comment|/**      * @param minValue The xMin to set.      */
specifier|public
name|void
name|setXMin
parameter_list|(
name|short
name|minValue
parameter_list|)
block|{
name|xMin
operator|=
name|minValue
expr_stmt|;
block|}
comment|/**      * @return Returns the yMax.      */
specifier|public
name|short
name|getYMax
parameter_list|()
block|{
return|return
name|yMax
return|;
block|}
comment|/**      * @param maxValue The yMax to set.      */
specifier|public
name|void
name|setYMax
parameter_list|(
name|short
name|maxValue
parameter_list|)
block|{
name|yMax
operator|=
name|maxValue
expr_stmt|;
block|}
comment|/**      * @return Returns the yMin.      */
specifier|public
name|short
name|getYMin
parameter_list|()
block|{
return|return
name|yMin
return|;
block|}
comment|/**      * @param minValue The yMin to set.      */
specifier|public
name|void
name|setYMin
parameter_list|(
name|short
name|minValue
parameter_list|)
block|{
name|yMin
operator|=
name|minValue
expr_stmt|;
block|}
block|}
end_class

end_unit

