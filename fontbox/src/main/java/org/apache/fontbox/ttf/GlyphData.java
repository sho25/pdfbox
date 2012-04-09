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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
import|;
end_import

begin_comment
comment|/**  * A glyph data record in the glyf table.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|GlyphData
block|{
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
name|BoundingBox
name|boundingBox
init|=
literal|null
decl_stmt|;
specifier|private
name|short
name|numberOfContours
decl_stmt|;
specifier|private
name|GlyfDescript
name|glyphDescription
init|=
literal|null
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initData
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
name|numberOfContours
operator|=
name|data
operator|.
name|readSignedShort
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
name|boundingBox
operator|=
operator|new
name|BoundingBox
argument_list|(
name|xMin
argument_list|,
name|yMin
argument_list|,
name|xMax
argument_list|,
name|yMax
argument_list|)
expr_stmt|;
if|if
condition|(
name|numberOfContours
operator|>=
literal|0
condition|)
block|{
comment|// create a simple glyph
name|glyphDescription
operator|=
operator|new
name|GlyfSimpleDescript
argument_list|(
name|numberOfContours
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create a composite glyph
name|glyphDescription
operator|=
operator|new
name|GlyfCompositeDescript
argument_list|(
name|data
argument_list|,
name|ttf
operator|.
name|getGlyph
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return Returns the boundingBox.      */
specifier|public
name|BoundingBox
name|getBoundingBox
parameter_list|()
block|{
return|return
name|boundingBox
return|;
block|}
comment|/**      * @param boundingBoxValue The boundingBox to set.      */
specifier|public
name|void
name|setBoundingBox
parameter_list|(
name|BoundingBox
name|boundingBoxValue
parameter_list|)
block|{
name|this
operator|.
name|boundingBox
operator|=
name|boundingBoxValue
expr_stmt|;
block|}
comment|/**      * @return Returns the numberOfContours.      */
specifier|public
name|short
name|getNumberOfContours
parameter_list|()
block|{
return|return
name|numberOfContours
return|;
block|}
comment|/**      * @param numberOfContoursValue The numberOfContours to set.      */
specifier|public
name|void
name|setNumberOfContours
parameter_list|(
name|short
name|numberOfContoursValue
parameter_list|)
block|{
name|this
operator|.
name|numberOfContours
operator|=
name|numberOfContoursValue
expr_stmt|;
block|}
comment|/**      * Returns the description of the glyph.      * @return the glyph description      */
specifier|public
name|GlyphDescription
name|getDescription
parameter_list|()
block|{
return|return
name|glyphDescription
return|;
block|}
comment|/**      * Returns the xMax value.      * @return the XMax value      */
specifier|public
name|short
name|getXMaximum
parameter_list|()
block|{
return|return
name|xMax
return|;
block|}
comment|/**      * Returns the xMin value.      * @return the xMin value      */
specifier|public
name|short
name|getXMinimum
parameter_list|()
block|{
return|return
name|xMin
return|;
block|}
comment|/**      * Returns the yMax value.      * @return the yMax value      */
specifier|public
name|short
name|getYMaximum
parameter_list|()
block|{
return|return
name|yMax
return|;
block|}
comment|/**      * Returns the yMin value.      * @return the yMin value      */
specifier|public
name|short
name|getYMinimum
parameter_list|()
block|{
return|return
name|yMin
return|;
block|}
block|}
end_class

end_unit

