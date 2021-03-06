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
name|IndexToLocationTable
extends|extends
name|TTFTable
block|{
specifier|private
specifier|static
specifier|final
name|short
name|SHORT_OFFSETS
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|short
name|LONG_OFFSETS
init|=
literal|1
decl_stmt|;
comment|/**      * A tag that identifies this table type.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"loca"
decl_stmt|;
specifier|private
name|long
index|[]
name|offsets
decl_stmt|;
name|IndexToLocationTable
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
annotation|@
name|Override
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
name|HeaderTable
name|head
init|=
name|ttf
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|head
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not get head table"
argument_list|)
throw|;
block|}
name|int
name|numGlyphs
init|=
name|ttf
operator|.
name|getNumberOfGlyphs
argument_list|()
decl_stmt|;
name|offsets
operator|=
operator|new
name|long
index|[
name|numGlyphs
operator|+
literal|1
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
name|numGlyphs
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|head
operator|.
name|getIndexToLocFormat
argument_list|()
operator|==
name|SHORT_OFFSETS
condition|)
block|{
name|offsets
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
operator|*
literal|2L
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|head
operator|.
name|getIndexToLocFormat
argument_list|()
operator|==
name|LONG_OFFSETS
condition|)
block|{
name|offsets
index|[
name|i
index|]
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:TTF.loca unknown offset format: "
operator|+
name|head
operator|.
name|getIndexToLocFormat
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * @return Returns the offsets.      */
specifier|public
name|long
index|[]
name|getOffsets
parameter_list|()
block|{
return|return
name|offsets
return|;
block|}
comment|/**      * @param offsetsValue The offsets to set.      */
specifier|public
name|void
name|setOffsets
parameter_list|(
name|long
index|[]
name|offsetsValue
parameter_list|)
block|{
name|offsets
operator|=
name|offsetsValue
expr_stmt|;
block|}
block|}
end_class

end_unit

