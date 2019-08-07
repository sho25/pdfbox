begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*     Licensed to the Apache Software Foundation (ASF) under one or more    contributor license agreements.  See the NOTICE file distributed with    this work for additional information regarding copyright ownership.    The ASF licenses this file to You under the Apache License, Version 2.0    (the "License"); you may not use this file except in compliance with    the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0     Unless required by applicable law or agreed to in writing, software    distributed under the License is distributed on an "AS IS" BASIS,    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    See the License for the specific language governing permissions and    limitations under the License.   */
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
comment|/**  * This class is based on code from Apache Batik a subproject of Apache XMLGraphics.  * see http://xmlgraphics.apache.org/batik/ for further details.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|GlyfDescript
implements|implements
name|GlyphDescription
block|{
comment|// Flags describing a coordinate of a glyph.
comment|/**      * if set, the point is on the curve.      */
specifier|public
specifier|static
specifier|final
name|byte
name|ON_CURVE
init|=
literal|0x01
decl_stmt|;
comment|/**      * if set, the x-coordinate is 1 byte long.      */
specifier|public
specifier|static
specifier|final
name|byte
name|X_SHORT_VECTOR
init|=
literal|0x02
decl_stmt|;
comment|/**      * if set, the y-coordinate is 1 byte long.      */
specifier|public
specifier|static
specifier|final
name|byte
name|Y_SHORT_VECTOR
init|=
literal|0x04
decl_stmt|;
comment|/**      * if set, the next byte specifies the number of additional       * times this set of flags is to be repeated.      */
specifier|public
specifier|static
specifier|final
name|byte
name|REPEAT
init|=
literal|0x08
decl_stmt|;
comment|/**      * This flag as two meanings, depending on how the      * x-short vector flags is set.      * If the x-short vector is set, this bit describes the sign      * of the value, with 1 equaling positive and 0 positive.      * If the x-short vector is not set and this bit is also not      * set, the current x-coordinate is a signed 16-bit delta vector.      */
specifier|public
specifier|static
specifier|final
name|byte
name|X_DUAL
init|=
literal|0x10
decl_stmt|;
comment|/**      * This flag as two meanings, depending on how the      * y-short vector flags is set.      * If the y-short vector is set, this bit describes the sign      * of the value, with 1 equaling positive and 0 positive.      * If the y-short vector is not set and this bit is also not      * set, the current y-coordinate is a signed 16-bit delta vector.      */
specifier|public
specifier|static
specifier|final
name|byte
name|Y_DUAL
init|=
literal|0x20
decl_stmt|;
specifier|private
name|int
index|[]
name|instructions
decl_stmt|;
specifier|private
specifier|final
name|int
name|contourCount
decl_stmt|;
comment|/**      * Constructor.      *       * @param numberOfContours the number of contours      * @param bais the stream to be read      * @throws IOException is thrown if something went wrong      */
name|GlyfDescript
parameter_list|(
name|short
name|numberOfContours
parameter_list|,
name|TTFDataStream
name|bais
parameter_list|)
throws|throws
name|IOException
block|{
name|contourCount
operator|=
name|numberOfContours
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|resolve
parameter_list|()
block|{     }
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|getContourCount
parameter_list|()
block|{
return|return
name|contourCount
return|;
block|}
comment|/**      * Returns the hinting instructions.      * @return an array containing the hinting instructions.      */
specifier|public
name|int
index|[]
name|getInstructions
parameter_list|()
block|{
return|return
name|instructions
return|;
block|}
comment|/**      * Read the hinting instructions.      * @param bais the stream to be read      * @param count the number of instructions to be read       * @throws IOException is thrown if something went wrong      */
name|void
name|readInstructions
parameter_list|(
name|TTFDataStream
name|bais
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|IOException
block|{
name|instructions
operator|=
name|bais
operator|.
name|readUnsignedByteArray
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

