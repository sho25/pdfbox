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
name|MaximumProfileTable
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
literal|"maxp"
decl_stmt|;
specifier|private
name|float
name|version
decl_stmt|;
specifier|private
name|int
name|numGlyphs
decl_stmt|;
specifier|private
name|int
name|maxPoints
decl_stmt|;
specifier|private
name|int
name|maxContours
decl_stmt|;
specifier|private
name|int
name|maxCompositePoints
decl_stmt|;
specifier|private
name|int
name|maxCompositeContours
decl_stmt|;
specifier|private
name|int
name|maxZones
decl_stmt|;
specifier|private
name|int
name|maxTwilightPoints
decl_stmt|;
specifier|private
name|int
name|maxStorage
decl_stmt|;
specifier|private
name|int
name|maxFunctionDefs
decl_stmt|;
specifier|private
name|int
name|maxInstructionDefs
decl_stmt|;
specifier|private
name|int
name|maxStackElements
decl_stmt|;
specifier|private
name|int
name|maxSizeOfInstructions
decl_stmt|;
specifier|private
name|int
name|maxComponentElements
decl_stmt|;
specifier|private
name|int
name|maxComponentDepth
decl_stmt|;
comment|/**      * @return Returns the maxComponentDepth.      */
specifier|public
name|int
name|getMaxComponentDepth
parameter_list|()
block|{
return|return
name|maxComponentDepth
return|;
block|}
comment|/**      * @param maxComponentDepthValue The maxComponentDepth to set.      */
specifier|public
name|void
name|setMaxComponentDepth
parameter_list|(
name|int
name|maxComponentDepthValue
parameter_list|)
block|{
name|this
operator|.
name|maxComponentDepth
operator|=
name|maxComponentDepthValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxComponentElements.      */
specifier|public
name|int
name|getMaxComponentElements
parameter_list|()
block|{
return|return
name|maxComponentElements
return|;
block|}
comment|/**      * @param maxComponentElementsValue The maxComponentElements to set.      */
specifier|public
name|void
name|setMaxComponentElements
parameter_list|(
name|int
name|maxComponentElementsValue
parameter_list|)
block|{
name|this
operator|.
name|maxComponentElements
operator|=
name|maxComponentElementsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxCompositeContours.      */
specifier|public
name|int
name|getMaxCompositeContours
parameter_list|()
block|{
return|return
name|maxCompositeContours
return|;
block|}
comment|/**      * @param maxCompositeContoursValue The maxCompositeContours to set.      */
specifier|public
name|void
name|setMaxCompositeContours
parameter_list|(
name|int
name|maxCompositeContoursValue
parameter_list|)
block|{
name|this
operator|.
name|maxCompositeContours
operator|=
name|maxCompositeContoursValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxCompositePoints.      */
specifier|public
name|int
name|getMaxCompositePoints
parameter_list|()
block|{
return|return
name|maxCompositePoints
return|;
block|}
comment|/**      * @param maxCompositePointsValue The maxCompositePoints to set.      */
specifier|public
name|void
name|setMaxCompositePoints
parameter_list|(
name|int
name|maxCompositePointsValue
parameter_list|)
block|{
name|this
operator|.
name|maxCompositePoints
operator|=
name|maxCompositePointsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxContours.      */
specifier|public
name|int
name|getMaxContours
parameter_list|()
block|{
return|return
name|maxContours
return|;
block|}
comment|/**      * @param maxContoursValue The maxContours to set.      */
specifier|public
name|void
name|setMaxContours
parameter_list|(
name|int
name|maxContoursValue
parameter_list|)
block|{
name|this
operator|.
name|maxContours
operator|=
name|maxContoursValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxFunctionDefs.      */
specifier|public
name|int
name|getMaxFunctionDefs
parameter_list|()
block|{
return|return
name|maxFunctionDefs
return|;
block|}
comment|/**      * @param maxFunctionDefsValue The maxFunctionDefs to set.      */
specifier|public
name|void
name|setMaxFunctionDefs
parameter_list|(
name|int
name|maxFunctionDefsValue
parameter_list|)
block|{
name|this
operator|.
name|maxFunctionDefs
operator|=
name|maxFunctionDefsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxInstructionDefs.      */
specifier|public
name|int
name|getMaxInstructionDefs
parameter_list|()
block|{
return|return
name|maxInstructionDefs
return|;
block|}
comment|/**      * @param maxInstructionDefsValue The maxInstructionDefs to set.      */
specifier|public
name|void
name|setMaxInstructionDefs
parameter_list|(
name|int
name|maxInstructionDefsValue
parameter_list|)
block|{
name|this
operator|.
name|maxInstructionDefs
operator|=
name|maxInstructionDefsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxPoints.      */
specifier|public
name|int
name|getMaxPoints
parameter_list|()
block|{
return|return
name|maxPoints
return|;
block|}
comment|/**      * @param maxPointsValue The maxPoints to set.      */
specifier|public
name|void
name|setMaxPoints
parameter_list|(
name|int
name|maxPointsValue
parameter_list|)
block|{
name|this
operator|.
name|maxPoints
operator|=
name|maxPointsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxSizeOfInstructions.      */
specifier|public
name|int
name|getMaxSizeOfInstructions
parameter_list|()
block|{
return|return
name|maxSizeOfInstructions
return|;
block|}
comment|/**      * @param maxSizeOfInstructionsValue The maxSizeOfInstructions to set.      */
specifier|public
name|void
name|setMaxSizeOfInstructions
parameter_list|(
name|int
name|maxSizeOfInstructionsValue
parameter_list|)
block|{
name|this
operator|.
name|maxSizeOfInstructions
operator|=
name|maxSizeOfInstructionsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxStackElements.      */
specifier|public
name|int
name|getMaxStackElements
parameter_list|()
block|{
return|return
name|maxStackElements
return|;
block|}
comment|/**      * @param maxStackElementsValue The maxStackElements to set.      */
specifier|public
name|void
name|setMaxStackElements
parameter_list|(
name|int
name|maxStackElementsValue
parameter_list|)
block|{
name|this
operator|.
name|maxStackElements
operator|=
name|maxStackElementsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxStorage.      */
specifier|public
name|int
name|getMaxStorage
parameter_list|()
block|{
return|return
name|maxStorage
return|;
block|}
comment|/**      * @param maxStorageValue The maxStorage to set.      */
specifier|public
name|void
name|setMaxStorage
parameter_list|(
name|int
name|maxStorageValue
parameter_list|)
block|{
name|this
operator|.
name|maxStorage
operator|=
name|maxStorageValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxTwilightPoints.      */
specifier|public
name|int
name|getMaxTwilightPoints
parameter_list|()
block|{
return|return
name|maxTwilightPoints
return|;
block|}
comment|/**      * @param maxTwilightPointsValue The maxTwilightPoints to set.      */
specifier|public
name|void
name|setMaxTwilightPoints
parameter_list|(
name|int
name|maxTwilightPointsValue
parameter_list|)
block|{
name|this
operator|.
name|maxTwilightPoints
operator|=
name|maxTwilightPointsValue
expr_stmt|;
block|}
comment|/**      * @return Returns the maxZones.      */
specifier|public
name|int
name|getMaxZones
parameter_list|()
block|{
return|return
name|maxZones
return|;
block|}
comment|/**      * @param maxZonesValue The maxZones to set.      */
specifier|public
name|void
name|setMaxZones
parameter_list|(
name|int
name|maxZonesValue
parameter_list|)
block|{
name|this
operator|.
name|maxZones
operator|=
name|maxZonesValue
expr_stmt|;
block|}
comment|/**      * @return Returns the numGlyphs.      */
specifier|public
name|int
name|getNumGlyphs
parameter_list|()
block|{
return|return
name|numGlyphs
return|;
block|}
comment|/**      * @param numGlyphsValue The numGlyphs to set.      */
specifier|public
name|void
name|setNumGlyphs
parameter_list|(
name|int
name|numGlyphsValue
parameter_list|)
block|{
name|this
operator|.
name|numGlyphs
operator|=
name|numGlyphsValue
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
name|numGlyphs
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxPoints
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxContours
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxCompositePoints
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxCompositeContours
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxZones
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxTwilightPoints
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxStorage
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxFunctionDefs
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxInstructionDefs
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxStackElements
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxSizeOfInstructions
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxComponentElements
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|maxComponentDepth
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
block|}
end_class

end_unit

